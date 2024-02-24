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

public class DownloadCallable implements Callable<Boolean> {
    private static final Logger LOGGER = LogManager.getLogger(DownloadCallable.class.getSimpleName());
    private final URL url;
    private final File downloadDir;

    public DownloadCallable(URL url, File downloadDir) {
        this.url = url;
        this.downloadDir = downloadDir;
    }

    @Override
    public Boolean call() throws Exception {
        DownloadProcessDialog downloadProcessDialog = new DownloadProcessDialog();
        downloadProcessDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //https://stackoverflow.com/questions/54496606/swing-worker-cant-update-gui-components-in-modal-jdialog
        downloadProcessDialog.setModalityType(Dialog.ModalityType.MODELESS);
        downloadProcessDialog.setVisible(true);
        downloadProcessDialog.setLocationRelativeTo(null);

        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000);
            conn.connect();
        } catch (IOException e) {
            LOGGER.error("download failed: {}", e.getMessage());
            return false;
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
            int step = 0;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
                count += length;
                int progress = count * 100 / downloadLength;
                if (progress - step > 1) {
                    SwingUtilities.invokeLater(() -> {
                        downloadProcessDialog.progressBar.setValue(progress);
                        downloadProcessDialog.progressLabel.setText(progress + "%");
                    });
                    step = progress;
                }
            }
            SwingUtilities.invokeLater(downloadProcessDialog::dispose);
            return true;
        } catch (IOException e) {
            LOGGER.error("download failed: {}", e.getMessage());
            return false;
        }
    }
}
