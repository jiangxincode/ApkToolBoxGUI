package edu.jiangxin.apktoolbox.file.password.recovery.dictionary;

import edu.jiangxin.apktoolbox.file.password.recovery.RecoveryPanel;
import edu.jiangxin.apktoolbox.file.password.recovery.State;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.FileChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LineHandler {

    private static final Logger logger = LogManager.getLogger(LineHandler.class.getSimpleName());

    private AtomicBoolean success;

    RecoveryPanel recoveryPanel;
    private FileChecker fileChecker;

    public LineHandler(FileChecker fileChecker, AtomicBoolean success, RecoveryPanel recoveryPanel) {
        this.fileChecker = fileChecker;
        this.success = success;
        this.recoveryPanel = recoveryPanel;
    }

    public AtomicBoolean getSuccess() {
        return this.success;
    }

    public void handle(String line, long currentLineCount, BigFileReader bigFileReader) {
        if (success.compareAndSet(true, true) || recoveryPanel.getCurrentState() != State.WORKING) {
            return;
        }
        recoveryPanel.setCurrentPassword(line);
        recoveryPanel.setProgressBarValue(Math.toIntExact(currentLineCount));

        if (fileChecker.checkPassword(line) ) {
            if (success.compareAndSet(false, true)) {
                logger.info("find password: " + line);
                recoveryPanel.setCurrentState(State.IDLE);
                bigFileReader.shutdown();
                JOptionPane.showMessageDialog(recoveryPanel, "Password[" + line + "]");
                recoveryPanel.setProgressBarValue(0);
            }
        } else {
            if (!success.compareAndSet(true, true) && recoveryPanel.getCurrentState() == State.WORKING) {
                logger.info("try password[" + line + "] failed");
            }
        }
    }

}
