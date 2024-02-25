package edu.jiangxin.apktoolbox.swing.extend.plugin.download;

import edu.jiangxin.apktoolbox.swing.extend.plugin.IPreparePluginCallback;
import edu.jiangxin.apktoolbox.swing.extend.plugin.ProgressBarDialog;

import javax.swing.*;

public abstract class AbstractRunnable implements Runnable {
    protected final IPreparePluginCallback callback;
    protected final ProgressBarDialog progressBarDialog;
    protected int progress = 0;
    protected boolean isCancelled = false;
    protected boolean isFinished = false;

    protected AbstractRunnable(String dialogTitle, IPreparePluginCallback callback) {
        this.callback = callback;
        this.progressBarDialog = new ProgressBarDialog(dialogTitle);
        progressBarDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                cancel();
            }
        });

        Timer timer = new Timer(1000, e -> {
            if (isFinished || isCancelled) {
                ((Timer) e.getSource()).stop();
                progressBarDialog.dispose();
            } else {
                progressBarDialog.setValue(progress);
            }
        });
        timer.start();
    }

    protected void cancel() {
        isCancelled = true;
    }
}