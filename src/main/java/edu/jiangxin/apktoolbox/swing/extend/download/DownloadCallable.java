package edu.jiangxin.apktoolbox.swing.extend.download;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DownloadCallable implements Callable<Integer> {
    private static final Logger LOGGER = LogManager.getLogger(DownloadCallable.class.getSimpleName());
    private final URL url;
    private final File downloadDir;

    private int progress = 0;

    private boolean isCancelled = false;

    public static final int DOWNLOAD_SUCCESS = 0;

    public static final int DOWNLOAD_FAILED = -1;

    public static final int DOWNLOAD_CANCELLED = 1;

    public DownloadCallable(URL url, File downloadDir) {
        this.url = url;
        this.downloadDir = downloadDir;
    }

    @Override
    public Integer call() throws Exception {

        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000);
            conn.connect();
        } catch (IOException e) {
            LOGGER.error("download failed: {}", e.getMessage());
            return DOWNLOAD_FAILED;
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
            int count = 0;
            while ((length = is.read(b)) != -1 && !isCancelled) {
                os.write(b, 0, length);
                count += length;
                progress = count * 100 / downloadLength;
            }
            if (isCancelled) {
                LOGGER.info("download cancelled");
                return DOWNLOAD_CANCELLED;
            }
            return DOWNLOAD_SUCCESS;
        } catch (IOException e) {
            LOGGER.error("download failed: {}", e.getMessage());
            return DOWNLOAD_FAILED;
        }
    }

    public int getProgress() {
        return progress;
    }

    public void cancel() {
        isCancelled = true;
    }
}
