package edu.jiangxin.apktoolbox.swing.extend.plugin;

import edu.jiangxin.apktoolbox.swing.extend.listener.IFinishCallBack;
import edu.jiangxin.apktoolbox.utils.Utils;

public interface IPlugin {

    default void onCheckAndDownloadPlugin(IFinishCallBack callBack) {
        Utils.checkAndDownloadPlugin(getPluginFilename(), isPluginNeedUnzip(), callBack);
    }

    String getPluginFilename();

    boolean isPluginNeedUnzip();

    String getPluginStartupCmd();
}
