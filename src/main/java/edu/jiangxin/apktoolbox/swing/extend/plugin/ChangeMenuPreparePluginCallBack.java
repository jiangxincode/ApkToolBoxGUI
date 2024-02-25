package edu.jiangxin.apktoolbox.swing.extend.plugin;

import edu.jiangxin.apktoolbox.swing.extend.listener.IPreChangeMenuCallBack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 * This class is used to prepare plugin for change menu.
 *
 * All operations in this class will be executed in main thread.
 */
public class ChangeMenuPreparePluginCallBack implements IPreparePluginCallback {
    private static final Logger logger = LogManager.getLogger(ChangeMenuPreparePluginCallBack.class.getSimpleName());
    private final String pluginFilename;
    private final boolean isPluginNeedUnzip;
    private IPreChangeMenuCallBack callBack;

    public static final int CHECK_EXIST = 0;

    public static final int CHECK_NOT_EXIST = 1;

    public static final int DOWNLOAD_SUCCESS = 0;

    public static final int DOWNLOAD_FAILED = -1;

    public static final int DOWNLOAD_CANCELLED = 1;

    public static final int UNZIP_SUCCESS = 0;

    public static final int UNZIP_FAILED = -1;

    public ChangeMenuPreparePluginCallBack(String pluginFilename, boolean isPluginNeedUnzip, IPreChangeMenuCallBack callBack) {
        this.pluginFilename = pluginFilename;
        this.isPluginNeedUnzip = isPluginNeedUnzip;
        this.callBack = callBack;
    }

    @Override
    public void onPrepareStarted() {
        PluginUtils.checkPlugin(pluginFilename, this);
    }

    @Override
    public void onCheckFinished(int result) {
        if (result == CHECK_EXIST) {
            onPrepareFinished();
            return;
        }
        if (result != CHECK_NOT_EXIST) {
            logger.error("unknown result: {}", result);
            return;
        }
        int userChoose = JOptionPane.showConfirmDialog(null, "未找到对应插件，是否下载", "提示", JOptionPane.YES_NO_OPTION);
        if (userChoose != JOptionPane.YES_OPTION) {
            logger.warn("userChoose: {}", userChoose);
            return;
        }
        PluginUtils.downloadPlugin(pluginFilename, this);
    }

    @Override
    public void onDownloadFinished(int result) {
        switch (result) {
            case DOWNLOAD_SUCCESS:
                if (isPluginNeedUnzip) {
                    PluginUtils.unzipPlugin(pluginFilename, this);
                } else {
                    onPrepareFinished();
                }
                break;
            case DOWNLOAD_FAILED:
                JOptionPane.showMessageDialog(null, "下载失败，请检查网络", "错误", JOptionPane.ERROR_MESSAGE);
                break;
            case DOWNLOAD_CANCELLED:
                JOptionPane.showMessageDialog(null, "下载取消", "提示", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                logger.error("unknown result: {}", result);
        }
    }

    @Override
    public void onUnzipFinished(int result) {
        switch (result) {
            case UNZIP_SUCCESS -> onPrepareFinished();
            case UNZIP_FAILED -> JOptionPane.showMessageDialog(null, "解压失败", "错误", JOptionPane.ERROR_MESSAGE);
            default -> logger.error("unknown result: {}", result);
        }
    }

    @Override
    public void onPrepareFinished() {
        if (callBack != null) {
            callBack.onPreChangeMenuFinished();
        }
    }
}
