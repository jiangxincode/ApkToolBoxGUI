package edu.jiangxin.apktoolbox.file.password.recovery.dictionary;

import edu.jiangxin.apktoolbox.file.password.recovery.RecoveryPanel;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.FileChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileHandle {

    private static Logger logger = LogManager.getLogger(FileHandle.class);

    private AtomicBoolean success;
    private boolean stop = false;

    RecoveryPanel recoveryPanel;
    private FileChecker fileChecker;

    public FileHandle(FileChecker fileChecker, AtomicBoolean success, RecoveryPanel recoveryPanel) {
        this.fileChecker = fileChecker;
        this.success = success;
        this.recoveryPanel = recoveryPanel;
    }

    public void stop() {
        this.stop = true;
    }

    public AtomicBoolean getSuccess() {
        return this.success;
    }

    public void init() {
        recoveryPanel.setIsRecovering(false);
    }

    public void handle(String line, long currentLineCount, BigFileReader bigFileReader) {
        if (success.compareAndSet(true, true) || stop) {
            return;
        }
        recoveryPanel.setProgressBarValue(Math.toIntExact(currentLineCount));

        if (fileChecker.checkPassword(line) ) {
            if (success.compareAndSet(false, true)) {
                logger.info("find password: " + line);
                recoveryPanel.setIsRecovering(false);
                bigFileReader.shutdown();
                JOptionPane.showMessageDialog(recoveryPanel, "Password[" + line + "]");
                recoveryPanel.setProgressBarValue(0);
                return;
            }
        } else {
            if (!success.compareAndSet(true, true) && !stop) {
                logger.info("try password[" + line + "] failed");
            }
        }
    }

}
