package edu.jiangxin.apktoolbox.file.password.recovery.category.dictionary.multithread;

import edu.jiangxin.apktoolbox.file.core.EncoderDetector;
import edu.jiangxin.apktoolbox.file.password.recovery.Synchronizer;
import edu.jiangxin.apktoolbox.file.password.recovery.category.ICategory;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.FileChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class DictionaryMultiThreadProxy implements ICategory {
    private static final Logger logger = LogManager.getLogger(DictionaryMultiThreadProxy.class.getSimpleName());

    private BigFileReader bigFileReader;

    private String password;

    private Object lock = new Object();

    private static class DictionaryMultiThreadProxyHolder {
        private static final DictionaryMultiThreadProxy instance = new DictionaryMultiThreadProxy();
    }

    private DictionaryMultiThreadProxy() {
    }

    public static DictionaryMultiThreadProxy getInstance() {
        return DictionaryMultiThreadProxy.DictionaryMultiThreadProxyHolder.instance;
    }

    public String startAndGet(int threadNum, File file, FileChecker checker, Synchronizer synchronizer) {
        CompleteCallback callback = password -> {
            synchronized (lock) {
                DictionaryMultiThreadProxy.this.password = password;
                lock.notifyAll();
            }
        };
        LineHandler lineHandler = new LineHandler(checker, new AtomicBoolean(false), synchronizer, callback);
        String charsetName = EncoderDetector.judgeFile(file.getAbsolutePath());
        BigFileReader.Builder builder = new BigFileReader.Builder(file.getAbsolutePath(), lineHandler);
        bigFileReader = builder
                .withThreadSize(threadNum)
                .withCharset(charsetName)
                .withBufferSize(1024 * 1024)
                .withOnCompleteCallback(callback)
                .build();
        bigFileReader.start();
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            logger.error("wait InterruptedException");
            Thread.currentThread().interrupt();
        }
        return password;
    }

    @Override
    public void cancel() {
        if (bigFileReader != null) {
            bigFileReader.shutdown();
        }
    }
}
