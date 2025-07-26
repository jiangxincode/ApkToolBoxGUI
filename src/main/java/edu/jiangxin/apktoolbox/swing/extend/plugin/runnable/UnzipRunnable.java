package edu.jiangxin.apktoolbox.swing.extend.plugin.runnable;

import edu.jiangxin.apktoolbox.swing.extend.plugin.ChangeMenuPreparePluginController;
import edu.jiangxin.apktoolbox.swing.extend.plugin.IPreparePluginCallback;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class UnzipRunnable extends AbstractRunnable {
    private static final Logger LOGGER = LogManager.getLogger(UnzipRunnable.class.getSimpleName());
    private final File pluginFile;
    private final boolean isPluginNeedUnzipToSeparateDir;

    public UnzipRunnable(File pluginFile, boolean isPluginNeedUnzipToSeparateDir, IPreparePluginCallback callback) {
        super("Unzipping...", callback);
        this.pluginFile = pluginFile;
        this.isPluginNeedUnzipToSeparateDir = isPluginNeedUnzipToSeparateDir;
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> progressBarDialog.setVisible(true));
        LOGGER.info("Unzip file: {}", pluginFile);
        String parentDir = pluginFile.getParent();
        if (isPluginNeedUnzipToSeparateDir) {
            String dirName = pluginFile.getName().substring(0, pluginFile.getName().lastIndexOf('.'));
            parentDir = new File(parentDir, dirName).getAbsolutePath();
            FileUtils.deleteQuietly(new File(parentDir));
            try {
                FileUtils.forceMkdir(new File(parentDir));
            } catch (IOException e) {
                LOGGER.error("forceMkdir failed: IOException");
                callback.onUnzipFinished(ChangeMenuPreparePluginController.RESULT_UNZIP_FAILED);
                return;
            }
        }
        try (ZipFile zipFile = new ZipFile(pluginFile)) {
            zipFile.setRunInThread(true);
            zipFile.extractAll(parentDir);
            while (zipFile.getProgressMonitor().getState() == ProgressMonitor.State.BUSY) {
                if (isCancelled) {
                    zipFile.getProgressMonitor().setCancelAllTasks(true);
                    break;
                }
                progress = zipFile.getProgressMonitor().getPercentDone();
                Thread.sleep(100);
            }
        } catch (IOException e) {
            LOGGER.error("unzipFile failed: IOException");
            callback.onUnzipFinished(ChangeMenuPreparePluginController.RESULT_UNZIP_FAILED);
            return;
        } catch (InterruptedException e) {
            LOGGER.error("unzipFile failed: InterruptedException");
            callback.onUnzipFinished(ChangeMenuPreparePluginController.RESULT_UNZIP_FAILED);
            Thread.currentThread().interrupt();
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
