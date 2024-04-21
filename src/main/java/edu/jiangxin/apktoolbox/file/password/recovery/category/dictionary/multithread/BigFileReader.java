package edu.jiangxin.apktoolbox.file.password.recovery.category.dictionary.multithread;

import edu.jiangxin.apktoolbox.file.core.EncoderDetector;
import edu.jiangxin.apktoolbox.file.password.recovery.RecoveryPanel;
import edu.jiangxin.apktoolbox.file.password.recovery.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BigFileReader {
    private static final Logger logger = LogManager.getLogger(BigFileReader.class.getSimpleName());

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;

    private final String charset;
    private final int bufferSize;
    private final ScheduledThreadPoolExecutor executorService;
    private final long fileLength;
    private RandomAccessFile rAccessFile;
    private final Set<StartEndPair> startEndPairs;
    private CyclicBarrier cyclicBarrier;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final CompleteCallback callback;

    private final AtomicBoolean success = new AtomicBoolean(false);

    private final RecoveryPanel panel;

    public BigFileReader(CompleteCallback callback, RecoveryPanel panel) {
        this.panel = panel;
        File file = panel.getDictionaryFile();
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在！");
        }
        this.fileLength = file.length();
        this.charset = EncoderDetector.judgeFile(file.getAbsolutePath());
        this.bufferSize = DEFAULT_BUFFER_SIZE;
        try {
            this.rAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            logger.error("BigFileReader FileNotFoundException");
        }
        this.executorService = new ScheduledThreadPoolExecutor(panel.getThreadNum());
        this.startEndPairs = new HashSet<>();
        this.callback = callback;
    }

    public void start() {
        long everySize = fileLength / panel.getThreadNum();
        try {
            calculateStartEnd(0, everySize);
        } catch (IOException e) {
            logger.error("start", e);
            return;
        }

        final long startTime = System.currentTimeMillis();
        int parties = startEndPairs.size();
        logger.info("[TaskTracing]Parties: " + parties);
        cyclicBarrier = new CyclicBarrier(parties, () -> {
            logger.info("use time: " + (System.currentTimeMillis() - startTime) + "ms");
            logger.info("all line: " + counter.get());
            callback.onComplete(null);
        });
        for (StartEndPair pair : startEndPairs) {
            logger.info("pair: " + pair);
            executorService.execute(new SliceReaderTask(pair));
        }
    }

    private void calculateStartEnd(long start, long size) throws IOException {
        if (start > fileLength - 1) {
            return;
        }
        StartEndPair pair = new StartEndPair();
        pair.start = start;
        long endPosition = start + size - 1;
        if (endPosition >= fileLength - 1) {
            pair.end = fileLength - 1;
            startEndPairs.add(pair);
            return;
        }

        rAccessFile.seek(endPosition);
        byte tmp = (byte) rAccessFile.read();
        while (tmp != '\n' && tmp != '\r') {
            endPosition++;
            if (endPosition >= fileLength - 1) {
                endPosition = fileLength - 1;
                break;
            }
            rAccessFile.seek(endPosition);
            tmp = (byte) rAccessFile.read();
        }
        pair.end = endPosition;
        startEndPairs.add(pair);

        calculateStartEnd(endPosition + 1, size);
    }

    public void shutdown() {
        try {
            rAccessFile.close();
        } catch (IOException e) {
            logger.error("shutdown IOException");
        }
        executorService.shutdown();
        logger.info("shutdown executorService");
    }

    private void handle(byte[] bytes) throws UnsupportedEncodingException {
        if (success.compareAndSet(true, true) || panel.getCurrentState() != State.WORKING) {
            return;
        }

        String line;
        if (charset == null) {
            line = new String(bytes);
        } else {
            line = new String(bytes, charset);
        }

        panel.setCurrentPassword(line);
        panel.increaseProgressBarValue();
        counter.decrementAndGet();

        if (panel.getCurrentFileChecker().checkPassword(line)) {
            if (success.compareAndSet(false, true)) {
                logger.info("find password: {}", line);
                callback.onComplete(line);
            }
        } else {
            if (!success.compareAndSet(true, true) && panel.getCurrentState() == State.WORKING) {
                logger.info("try password[{}] failed", line);
            }
        }
    }

    private static class StartEndPair {
        public long start;
        public long end;

        @Override
        public String toString() {
            return "star=" + start + ";end=" + end;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (end ^ (end >>> 32));
            result = prime * result + (int) (start ^ (start >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            StartEndPair other = (StartEndPair) obj;
            if (end != other.end)
                return false;
            return start == other.start;
        }

    }

    private class SliceReaderTask implements Runnable {
        private final long start;
        private final long sliceSize;
        private final byte[] readBuff;

        public SliceReaderTask(StartEndPair pair) {
            this.start = pair.start;
            this.sliceSize = pair.end - pair.start + 1;
            this.readBuff = new byte[bufferSize];
        }

        @Override
        public void run() {
            try {
                MappedByteBuffer mapBuffer = rAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, start, this.sliceSize);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                for (int offset = 0; offset < sliceSize; offset += bufferSize) {
                    int readLength;
                    if (offset + bufferSize <= sliceSize) {
                        readLength = bufferSize;
                    } else {
                        readLength = (int) (sliceSize - offset);
                    }
                    mapBuffer.get(readBuff, 0, readLength);
                    for (int i = 0; i < readLength; i++) {
                        byte tmp = readBuff[i];
                        if (tmp == '\n' || tmp == '\r') {
                            if (bos.size() > 0) {
                                handle(bos.toByteArray());
                            }
                            bos.reset();
                        } else {
                            bos.write(tmp);
                        }
                    }
                }
                if (bos.size() > 0) {
                    handle(bos.toByteArray());
                }
                logger.info("[TaskTracing]Waiting number: " + cyclicBarrier.getNumberWaiting());
            } catch (Exception e) {
                logger.error("run Exception" + e.getMessage());
            }
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                logger.error("await InterruptedException");
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                logger.error("await BrokenBarrierException");
            }
        }

    }
}
