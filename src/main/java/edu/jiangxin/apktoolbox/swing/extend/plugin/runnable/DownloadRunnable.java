package edu.jiangxin.apktoolbox.swing.extend.plugin.runnable;

import edu.jiangxin.apktoolbox.swing.extend.plugin.ChangeMenuPreparePluginController;
import edu.jiangxin.apktoolbox.swing.extend.plugin.IPreparePluginCallback;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class DownloadRunnable extends AbstractRunnable {
    private static final Logger LOGGER = LogManager.getLogger(DownloadRunnable.class.getSimpleName());
    private final URL url;
    private final File downloadDir;

    public DownloadRunnable(URL url, File downloadDir, IPreparePluginCallback callback) {
        super("Downloading...", callback);
        this.url = url;
        this.downloadDir = downloadDir;
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
        List<String> contentLengthList = map.get("Content-Length");
        int downloadLength = 1;
        if (contentLengthList != null && !contentLengthList.isEmpty()) {
            downloadLength = Integer.parseInt(contentLengthList.get(0));
        }

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
            SwingUtilities.invokeLater(() -> progressBarDialog.dispose());
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
