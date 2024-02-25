package edu.jiangxin.apktoolbox.swing.extend.plugin;

import edu.jiangxin.apktoolbox.swing.extend.plugin.download.DownloadRunnable;
import edu.jiangxin.apktoolbox.utils.FileUtils;
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
        if (pluginFile.exists()) {
            callBack.onCheckFinished(ChangeMenuPreparePluginController.RESULT_CHECK_EXIST);
        } else {
            callBack.onCheckFinished(ChangeMenuPreparePluginController.RESULT_CHECK_NOT_EXIST);
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
        boolean ret = FileUtils.unzipFile(pluginFile);
        if (ret) {
            callback.onUnzipFinished(ChangeMenuPreparePluginController.RESULT_UNZIP_SUCCESS);
        } else {
            callback.onUnzipFinished(ChangeMenuPreparePluginController.RESULT_UNZIP_FAILED);
        }
    }
}
