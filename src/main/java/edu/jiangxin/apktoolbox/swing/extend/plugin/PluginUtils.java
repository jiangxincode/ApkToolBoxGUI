package edu.jiangxin.apktoolbox.swing.extend.plugin;

import edu.jiangxin.apktoolbox.swing.extend.plugin.runnable.DownloadRunnable;
import edu.jiangxin.apktoolbox.swing.extend.plugin.runnable.UnzipRunnable;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PluginUtils {
    private static final Logger logger = LogManager.getLogger(PluginUtils.class.getSimpleName());

    private static final String URL_PREFIX = "https://gitee.com/jiangxinnju/apk-tool-box-gui-plugins/releases/download/";

    private static final String DOWNLOAD_VERSION = "v1.0.4";

    private PluginUtils() {
        //do nothing
    }

    public static void checkPlugin(String pluginFilename, IPreparePluginCallback callBack) {
        File pluginFile = new File(Utils.getPluginDirPath(), pluginFilename);
        File pluginDir = new File(Utils.getPluginDirPath(), pluginFilename.substring(0, pluginFilename.lastIndexOf(".")));
        if (pluginFile.exists() && pluginDir.exists()) {
            callBack.onCheckFinished(ChangeMenuPreparePluginController.RESULT_CHECK_SUCCESS);
        } else if (pluginFile.exists() && !pluginDir.exists()) {
            callBack.onCheckFinished(ChangeMenuPreparePluginController.RESULT_CHECK_ZIP_EXIST);
        } else {
            callBack.onCheckFinished(ChangeMenuPreparePluginController.RESULT_CHECK_ZIP_NOT_EXIST);
        }
    }

    public static void downloadPlugin(String pluginFilename, IPreparePluginCallback callBack) {
        String downloadUrlStr = URL_PREFIX + DOWNLOAD_VERSION + "/" + pluginFilename;
        URL url;
        try {
            url = new URL(downloadUrlStr);
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException: {}", e.getMessage());
            return;
        }
        File pluginDir = new File(Utils.getPluginDirPath());

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        DownloadRunnable downloadRunnable = new DownloadRunnable(url, pluginDir, callBack);
        executorService.submit(downloadRunnable);
    }


    public static void unzipPlugin(String pluginFilename, IPreparePluginCallback callback) {
        File pluginFile = new File(Utils.getPluginDirPath(), pluginFilename);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        UnzipRunnable unzipRunnable = new UnzipRunnable(pluginFile, callback);
        executorService.submit(unzipRunnable);
    }
}
