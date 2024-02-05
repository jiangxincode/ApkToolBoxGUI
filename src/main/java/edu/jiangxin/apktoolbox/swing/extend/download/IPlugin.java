package edu.jiangxin.apktoolbox.swing.extend.download;

import edu.jiangxin.apktoolbox.utils.Utils;

public interface IPlugin {

    default boolean onCheckAndDownloadPlugin() {
        return Utils.checkAndDownloadPlugin(getPluginFilename(), isPluginNeedUnzip());
    }

    String getPluginFilename();

    boolean isPluginNeedUnzip();

    String getPluginStartupCmd();
}
