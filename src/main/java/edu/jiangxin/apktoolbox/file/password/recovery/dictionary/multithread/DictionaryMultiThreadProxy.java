package edu.jiangxin.apktoolbox.file.password.recovery.dictionary.multithread;

import edu.jiangxin.apktoolbox.file.core.EncoderDetector;
import edu.jiangxin.apktoolbox.file.password.recovery.Synchronizer;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.FileChecker;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.IChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class DictionaryMultiThreadProxy {
    private static final Logger logger = LogManager.getLogger(DictionaryMultiThreadProxy.class.getSimpleName());

    private BigFileReader bigFileReader;
    private LineHandler lineHandler;

    private static class DictionaryMultiThreadProxyHolder {
        private static final DictionaryMultiThreadProxy instance = new DictionaryMultiThreadProxy();
    }

    private DictionaryMultiThreadProxy() {
    }

    public static DictionaryMultiThreadProxy getInstance() {
        return DictionaryMultiThreadProxy.DictionaryMultiThreadProxyHolder.instance;
    }

    public void start(int threadNum, File file, FileChecker checker, Synchronizer synchronizer, CompleteCallback completeCallback) {
        lineHandler = new LineHandler(checker, new AtomicBoolean(false), synchronizer, completeCallback);
        String charsetName = EncoderDetector.judgeFile(file.getAbsolutePath());
        BigFileReader.Builder builder = new BigFileReader.Builder(file.getAbsolutePath(), lineHandler);
        bigFileReader = builder.withThreadSize(threadNum).withCharset(charsetName).withBufferSize(1024 * 1024).build();
        bigFileReader.setCompleteCallback(completeCallback);
        bigFileReader.start();
    }

    public void cancel(Synchronizer synchronizer) {
        if (bigFileReader != null) {
            bigFileReader.shutdown();
        }
    }
}
