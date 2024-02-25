package edu.jiangxin.apktoolbox.swing.extend.plugin;

import edu.jiangxin.apktoolbox.swing.extend.listener.IPreChangeMenuCallBack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 * This class is used to prepare plugin for change menu.
 * <p>
 * All operations in this class will be executed in main thread.
 */
public class ChangeMenuPreparePluginController implements IPreparePluginCallback {
    private static final Logger logger = LogManager.getLogger(ChangeMenuPreparePluginController.class.getSimpleName());
    private final String pluginFilename;
    private final boolean isPluginNeedUnzip;
    private final IPreChangeMenuCallBack callBack;

    public static final int RESULT_CHECK_SUCCESS = 0;

    public static final int RESULT_CHECK_ZIP_EXIST = 1;

    public static final int RESULT_CHECK_ZIP_NOT_EXIST = 2;

    public static final int RESULT_DOWNLOAD_SUCCESS = 0;

    public static final int RESULT_DOWNLOAD_FAILED = -1;

    public static final int RESULT_DOWNLOAD_CANCELLED = 1;

    public static final int RESULT_UNZIP_SUCCESS = 0;

    public static final int RESULT_UNZIP_FAILED = -1;

    public static final int RESULT_UNZIP_CANCELLED = 1;

    public ChangeMenuPreparePluginController(String pluginFilename, boolean isPluginNeedUnzip, IPreChangeMenuCallBack callBack) {
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
        switch (result) {
            case RESULT_CHECK_SUCCESS -> onPrepareFinished();
            case RESULT_CHECK_ZIP_EXIST -> PluginUtils.unzipPlugin(pluginFilename, this);
            case RESULT_CHECK_ZIP_NOT_EXIST -> {
                int userChoose = JOptionPane.showConfirmDialog(null, "未找到对应插件，是否下载", "提示", JOptionPane.YES_NO_OPTION);
                if (userChoose == JOptionPane.YES_OPTION) {
                    PluginUtils.downloadPlugin(pluginFilename, this);
                }
            }
            default -> logger.info("onCheckFinished: {}", result);
        }
    }

    @Override
    public void onDownloadFinished(int result) {
        switch (result) {
            case RESULT_DOWNLOAD_SUCCESS:
                if (isPluginNeedUnzip) {
                    PluginUtils.unzipPlugin(pluginFilename, this);
                } else {
                    onPrepareFinished();
                }
                break;
            case RESULT_DOWNLOAD_FAILED:
                JOptionPane.showMessageDialog(null, "下载失败，请检查网络", "错误", JOptionPane.ERROR_MESSAGE);
                break;
            case RESULT_DOWNLOAD_CANCELLED:
                JOptionPane.showMessageDialog(null, "下载取消", "提示", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                logger.info("onDownloadFinished: {}", result);
                break;
        }
    }

    @Override
    public void onUnzipFinished(int result) {
        switch (result) {
            case RESULT_UNZIP_SUCCESS -> onPrepareFinished();
            case RESULT_UNZIP_FAILED -> JOptionPane.showMessageDialog(null, "解压失败", "错误", JOptionPane.ERROR_MESSAGE);
            case RESULT_UNZIP_CANCELLED -> JOptionPane.showMessageDialog(null, "解压取消", "提示", JOptionPane.INFORMATION_MESSAGE);
            default -> logger.info("onUnzipFinished: {}", result);
        }
    }

    @Override
    public void onPrepareFinished() {
        if (callBack != null) {
            callBack.onPreChangeMenuFinished();
        }
    }
}
