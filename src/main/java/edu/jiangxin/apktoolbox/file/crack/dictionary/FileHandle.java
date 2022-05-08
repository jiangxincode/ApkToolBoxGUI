package edu.jiangxin.apktoolbox.file.crack.dictionary;

import edu.jiangxin.apktoolbox.file.crack.CrackPanel;
import edu.jiangxin.apktoolbox.file.crack.cracker.FileCracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileHandle {

    private static Logger logger = LogManager.getLogger(FileHandle.class);

    private AtomicBoolean success;
    private boolean stop = false;

    CrackPanel crackPanel;
    private FileCracker fileCracker;

    public FileHandle(FileCracker fileCracker, AtomicBoolean success, CrackPanel crackPanel) {
        this.fileCracker = fileCracker;
        this.success = success;
        this.crackPanel = crackPanel;
    }

    public void stop() {
        this.stop = true;
    }

    public AtomicBoolean getSuccess() {
        return this.success;
    }

    public void init() {
        crackPanel.setIsCracking(false);
    }

    public void handle(String line, long currentLineCount, BigFileReader bigFileReader) {
        if (success.compareAndSet(true, true) || stop) {
            return;
        }
        crackPanel.setProgressBarValue(Math.toIntExact(currentLineCount));

        if (fileCracker.checkPassword(line) ) {
            if (success.compareAndSet(false, true)) {
                logger.info("find password: " + line);
                crackPanel.setIsCracking(false);
                bigFileReader.shutdown();
                JOptionPane.showMessageDialog(crackPanel, "Password[" + line + "]");
                crackPanel.setProgressBarValue(0);
                return;
            }
        } else {
            if (!success.compareAndSet(true, true) && !stop) {
                logger.info("try password[" + line + "] failed");
            }
        }
    }

}
