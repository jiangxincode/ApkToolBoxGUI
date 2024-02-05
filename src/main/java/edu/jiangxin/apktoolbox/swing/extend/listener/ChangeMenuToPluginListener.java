package edu.jiangxin.apktoolbox.swing.extend.listener;

import edu.jiangxin.apktoolbox.swing.extend.download.IPlugin;
import edu.jiangxin.apktoolbox.utils.Utils;

import java.io.File;

public abstract class ChangeMenuToPluginListener extends ChangeMenuListener implements IPlugin {
    @Override
    public boolean onPreChangeMenu() {
        return onCheckAndDownloadPlugin();
    }

    @Override
    public void onChangeMenu() {
        String cmd = getPluginStartupCmd();
        Utils.unBlockedExecutor(cmd);
    }

    @Override
    public boolean isPluginNeedUnzip() {
        return false;
    }

    @Override
    public String getPluginStartupCmd() {
        String jarPath = Utils.getPluginDirPath() + File.separator + getPluginFilename();
        return "java -Duser.language=en -Dfile.encoding=UTF8 -jar " + jarPath;
    }
}