package edu.jiangxin.apktoolbox.file.password.recovery.category.dictionary.multithread;

import edu.jiangxin.apktoolbox.file.password.recovery.RecoveryPanel;
import edu.jiangxin.apktoolbox.file.password.recovery.State;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.FileChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class LineHandler {

    private static final Logger logger = LogManager.getLogger(LineHandler.class.getSimpleName());

    private AtomicBoolean success;

    private RecoveryPanel panel;
    private FileChecker fileChecker;

    private CompleteCallback completeCallback;

    public LineHandler(FileChecker fileChecker, AtomicBoolean success, RecoveryPanel panel, CompleteCallback completeCallback) {
        this.fileChecker = fileChecker;
        this.success = success;
        this.panel = panel;
        this.completeCallback = completeCallback;
    }

    public AtomicBoolean getSuccess() {
        return this.success;
    }

    public void handle(String line, long currentLineCount) {
        if (success.compareAndSet(true, true) || panel.getCurrentState() != State.WORKING) {
            return;
        }
        panel.setCurrentPassword(line);
        panel.setProgressBarValue(Math.toIntExact(currentLineCount));

        if (fileChecker.checkPassword(line)) {
            if (success.compareAndSet(false, true)) {
                logger.info("find password: " + line);
                completeCallback.onComplete(line);
            }
        } else {
            if (!success.compareAndSet(true, true) && panel.getCurrentState() == State.WORKING) {
                logger.info("try password[" + line + "] failed");
            }
        }
    }

}
