package edu.jiangxin.apktoolbox.swing.extend.plugin;

import edu.jiangxin.apktoolbox.swing.extend.download.DownloadCallable;
import edu.jiangxin.apktoolbox.swing.extend.download.DownloadProcessDialog;
import edu.jiangxin.apktoolbox.utils.FileUtils;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PluginUtils {
    private static final Logger logger = LogManager.getLogger(PluginUtils.class.getSimpleName());

    private static final String DOWNLOAD_VERSION = "v1.0.4";

    public static void preparePlugin(String pluginFilename, boolean isPluginNeedUnzip, IPreparePluginCallback callBack) {
        File pluginFile = new File(Utils.getPluginDirPath(), pluginFilename);
        if (pluginFile.exists()) {
            callBack.onPreparePluginFinished();
            return;
        }
        int userChoose = JOptionPane.showConfirmDialog(null, "未找到对应插件，是否下载", "提示", JOptionPane.YES_NO_OPTION);
        if (userChoose != JOptionPane.YES_OPTION) {
            logger.warn("userChoose: {}", userChoose);
            return;
        }
        String downloadUrlStr = "https://gitee.com/jiangxinnju/apk-tool-box-gui-plugins/releases/download/" + DOWNLOAD_VERSION + "/" + pluginFilename;
        URL url;
        try {
            url = new URL(downloadUrlStr);
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException: {}", e.getMessage());
            return;
        }
        File pluginDir = new File(Utils.getPluginDirPath());

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        DownloadCallable downloadCallable = new DownloadCallable(url, pluginDir);

        DownloadProcessDialog downloadProcessDialog = new DownloadProcessDialog("Downloading...");
        downloadProcessDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        downloadProcessDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                //立即终止线程池中的线程
                downloadCallable.cancel();
                try {
                    executorService.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.info("awaitTermination failed: {}", e.getMessage());
                    return;
                }
                try {
                    org.apache.commons.io.FileUtils.delete(pluginFile);
                } catch (IOException e) {
                    logger.info("delete file failed: {}", e.getMessage());
                    return;
                }
            }
        });

        //How to Use Swing Timers:
        //https://docs.oracle.com/javase/tutorial/uiswing/misc/timer.html
        Timer timer = new Timer(1000, e -> {
            if (downloadProcessDialog.progressBar.getValue() == 100) {
                ((Timer) e.getSource()).stop();
            } else {
                downloadProcessDialog.progressBar.setValue(downloadCallable.getProgress());
                downloadProcessDialog.progressLabel.setText(downloadCallable.getProgress() + "%");
            }
        });
        timer.start();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                Future<Integer> future = executorService.submit(downloadCallable);
                try {
                    int ret = future.get();
                    if (ret == DownloadCallable.DOWNLOAD_CANCELLED) {
                        JOptionPane.showMessageDialog(null, "下载取消", "提示", JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    } else if (ret == DownloadCallable.DOWNLOAD_FAILED) {
                        JOptionPane.showMessageDialog(null, "下载失败，请检查网络", "错误", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                } catch (InterruptedException e) {
                    logger.error("InterruptedException: {}", e.getMessage());
                    return null;
                } catch (ExecutionException e) {
                    logger.error("ExecutionException: {}", e.getMessage());
                    return null;
                }

                if (isPluginNeedUnzip) {
                    boolean ret = FileUtils.unzipFile(pluginFile);
                    if (!ret) {
                        JOptionPane.showMessageDialog(null, "解压失败", "错误", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
                callBack.onPreparePluginFinished();
                return null;
            }
        };
        worker.execute();
        downloadProcessDialog.setVisible(true);
    }

}
