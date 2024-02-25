package edu.jiangxin.apktoolbox.swing.extend.plugin.download;

import edu.jiangxin.apktoolbox.swing.extend.plugin.ChangeMenuPreparePluginController;
import edu.jiangxin.apktoolbox.swing.extend.plugin.IPreparePluginCallback;
import edu.jiangxin.apktoolbox.swing.extend.plugin.ProgressBarDialog;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class DownloadRunnable implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(DownloadRunnable.class.getSimpleName());
    private final URL url;
    private final File downloadDir;
    private final IPreparePluginCallback callback;

    private final ProgressBarDialog progressBarDialog;

    private int progress = 0;

    private boolean isCancelled = false;

    private boolean isFinished = false;

    public DownloadRunnable(URL url, File downloadDir, IPreparePluginCallback callback) {
        this.url = url;
        this.downloadDir = downloadDir;
        this.callback = callback;
        progressBarDialog = new ProgressBarDialog("Downloading...");
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
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000);
            conn.connect();
        } catch (IOException e) {
            LOGGER.error("download failed: {}", e.getMessage());
            callback.onDownloadFinished(ChangeMenuPreparePluginController.RESULT_DOWNLOAD_FAILED);
            return;
        }

        Map<String, List<String>> map = conn.getHeaderFields();
        int downloadLength = Integer.parseInt(map.get("Content-Length").get(0));

        String urlStr = url.toString();
        String fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
        File downloadFile = new File(downloadDir, fileName);

        try (BufferedInputStream is = new BufferedInputStream(conn.getInputStream());
             BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(downloadFile))) {
            byte[] b = new byte[2048];
            int length;
            long count = 0;
            while ((length = is.read(b)) != -1 && !isCancelled) {
                os.write(b, 0, length);
                count += length;
                progress = (int)(count * 100 / downloadLength);
            }
        } catch (IOException e) {
            LOGGER.error("download failed: {}", e.getMessage());
            callback.onDownloadFinished(ChangeMenuPreparePluginController.RESULT_DOWNLOAD_FAILED);
            return;
        }
        if (isCancelled) {
            LOGGER.info("download cancelled");
            FileUtils.deleteQuietly(downloadFile);
            callback.onDownloadFinished(ChangeMenuPreparePluginController.RESULT_DOWNLOAD_CANCELLED);
        } else {
            isFinished = true;
            callback.onDownloadFinished(ChangeMenuPreparePluginController.RESULT_DOWNLOAD_SUCCESS);
        }
    }
}
