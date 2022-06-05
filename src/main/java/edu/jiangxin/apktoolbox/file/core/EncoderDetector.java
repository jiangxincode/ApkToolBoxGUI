package edu.jiangxin.apktoolbox.file.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

/**
 * @author jiangxin
 * @author 2018-09-09
 *
 */
public class EncoderDetector {
    private static final Logger logger = LogManager.getLogger(EncoderDetector.class.getSimpleName());
    private static String[] detectorCharsets;
    private static CountDownLatch countDownLatch;

    /**
     * Detect the charset of some file
     * 
     * @param fileName
     * @return charset
     */
    public static String judgeFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            logger.error("Can't find the file: " + fileName);
            return null;
        }

        detectorCharsets = new String[2];
        countDownLatch = new CountDownLatch(2);

        Thread cpDetectorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                CodepageDetectorProxy cpDetectorProxy = CodepageDetectorProxy.getInstance();

                // first one returning non-null wins the decision
                cpDetectorProxy.add(new ParsingDetector(false));
                cpDetectorProxy.add(JChardetFacade.getInstance());
                cpDetectorProxy.add(ASCIIDetector.getInstance());
                cpDetectorProxy.add(UnicodeDetector.getInstance());
                Charset charset = null;
                try {
                    // f.toURL()已经废弃，建议通过toURI()间接转换
                    charset = cpDetectorProxy.detectCodepage(file.toURI().toURL());
                } catch (IOException e) {
                    logger.error("cpDetector failed", e);
                    detectorCharsets[0] = null;
                }
                if (charset != null) {
                    detectorCharsets[0] = charset.name();
                } else {
                    detectorCharsets[0] = null;
                }
                countDownLatch.countDown();
            }
        });

        Thread universalDetectorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                UniversalDetector universalDetector = new UniversalDetector(null);
                byte[] buf = new byte[4096];
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    int nread;
                    while ((nread = fis.read(buf)) > 0 && !universalDetector.isDone()) {
                        universalDetector.handleData(buf, 0, nread);
                    }
                    universalDetector.dataEnd();
                    detectorCharsets[1] = universalDetector.getDetectedCharset();
                } catch (IOException e) {
                    logger.error("universalDetector failed", e);
                    detectorCharsets[1] = null;
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            logger.error("close fis failed", e);
                        }
                    }
                }
                countDownLatch.countDown();
            }
        });
        cpDetectorThread.start();
        universalDetectorThread.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("await failed", e);
            Thread.currentThread().interrupt();
        }
        return electBestCharset();
    }

    private static String electBestCharset() {
        StringBuilder sb = new StringBuilder();
        sb.append("cpDetector: ").append(detectorCharsets[0]).append(", universalDetector: ")
                .append(detectorCharsets[1]);
        if (StringUtils.isEmpty(detectorCharsets[0]) && StringUtils.isEmpty(detectorCharsets[1])) {
            logger.warn(sb.toString());
            return null;
        } else if (StringUtils.isEmpty(detectorCharsets[0]) && StringUtils.isNotEmpty(detectorCharsets[1])) {
            logger.info(sb.toString());
            return detectorCharsets[1];
        } else if (StringUtils.isNotEmpty(detectorCharsets[0]) && StringUtils.isEmpty(detectorCharsets[1])) {
            logger.info(sb.toString());
            return detectorCharsets[0];
        } else if (detectorCharsets[0].equals(detectorCharsets[1])) {
            logger.info(sb.toString());
            return detectorCharsets[1];
        } else {
            logger.warn(sb.toString());
            return detectorCharsets[1];
        }

    }
}
