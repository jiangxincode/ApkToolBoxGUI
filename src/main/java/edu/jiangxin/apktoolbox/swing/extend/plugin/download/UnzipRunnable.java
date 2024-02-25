package edu.jiangxin.apktoolbox.swing.extend.plugin.download;

import edu.jiangxin.apktoolbox.swing.extend.plugin.ChangeMenuPreparePluginController;
import edu.jiangxin.apktoolbox.swing.extend.plugin.IPreparePluginCallback;
import edu.jiangxin.apktoolbox.swing.extend.plugin.ProgressBarDialog;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class UnzipRunnable implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(UnzipRunnable.class.getSimpleName());
    private final File pluginFile;
    private final IPreparePluginCallback callback;

    private final ProgressBarDialog progressBarDialog;

    private int progress = 0;

    private boolean isCancelled = false;

    private boolean isFinished = false;

    public UnzipRunnable(File pluginFile, IPreparePluginCallback callback) {
        this.pluginFile = pluginFile;
        this.callback = callback;
        progressBarDialog = new ProgressBarDialog("Unzipping...");
        progressBarDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                cancel();
            }
        });

        //How to Use Swing Timers: https://docs.oracle.com/javase/tutorial/uiswing/misc/timer.html
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

    public void cancel() {
        isCancelled = true;
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> progressBarDialog.setVisible(true));
        LOGGER.info("Unzip file: {}", pluginFile);
        String parentDir = pluginFile.getParent();
        try (ZipFile zipFile = new ZipFile(pluginFile)) {
            zipFile.setRunInThread(true);
            zipFile.extractAll(parentDir);
            while (zipFile.getProgressMonitor().getState() == ProgressMonitor.State.BUSY) {
                if (isCancelled) {
                    zipFile.getProgressMonitor().setCancelAllTasks(true);
                    break;
                }
                progress = zipFile.getProgressMonitor().getPercentDone();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    LOGGER.error("sleep failed: {}", e.getMessage());
                    callback.onUnzipFinished(ChangeMenuPreparePluginController.RESULT_UNZIP_FAILED);
                    return;
                }
            }
        } catch (IOException e) {
            LOGGER.error("unzipFile failed: IOException");
            callback.onUnzipFinished(ChangeMenuPreparePluginController.RESULT_UNZIP_FAILED);
            return;
        }

        if (isCancelled) {
            LOGGER.info("download cancelled");
            String fileName = pluginFile.getAbsolutePath();
            String dirName = fileName.substring(0, fileName.lastIndexOf("."));
            FileUtils.deleteQuietly(new File(dirName));
            callback.onUnzipFinished(ChangeMenuPreparePluginController.RESULT_UNZIP_CANCELLED);
        } else {
            isFinished = true;
            callback.onUnzipFinished(ChangeMenuPreparePluginController.RESULT_UNZIP_SUCCESS);
        }
    }
}
