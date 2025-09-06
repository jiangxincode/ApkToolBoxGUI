package edu.jiangxin.apktoolbox.file.password.recovery.category.dictionary.multithread;

import edu.jiangxin.apktoolbox.file.password.recovery.RecoveryPanel;
import edu.jiangxin.apktoolbox.file.password.recovery.category.ICategory;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;

public class DictionaryMultiThreadProxy implements ICategory {
    private static final Logger logger = LogManager.getLogger(DictionaryMultiThreadProxy.class.getSimpleName());

    private BigFileReader bigFileReader;

    private String password;

    private final Object lock = new Object();

    private static class DictionaryMultiThreadProxyHolder {
        private static final DictionaryMultiThreadProxy instance = new DictionaryMultiThreadProxy();
    }

    private DictionaryMultiThreadProxy() {
    }

    public static DictionaryMultiThreadProxy getInstance() {
        return DictionaryMultiThreadProxy.DictionaryMultiThreadProxyHolder.instance;
    }

    private String startAndGet(RecoveryPanel panel) {
        CompleteCallback callback = password -> {
            synchronized (lock) {
                this.password = password;
                lock.notifyAll();
            }
        };
        bigFileReader = new BigFileReader(callback, panel);
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
    public void start(RecoveryPanel panel) {
        File dictionaryFile = panel.getDictionaryFile();
        if (!dictionaryFile.isFile()) {
            JOptionPane.showMessageDialog(panel, "Invalid dictionary file");
            return;
        }
        int fileLineCount = Utils.getFileLineCount(dictionaryFile);
        logger.info("File line count: {}", fileLineCount);

        panel.resetProgressMaxValue(fileLineCount);

        String resultPassword = startAndGet(panel);
        panel.showResultWithDialog(resultPassword);
    }

    @Override
    public void cancel() {
        if (bigFileReader != null) {
            bigFileReader.shutdown();
        }
    }
}
