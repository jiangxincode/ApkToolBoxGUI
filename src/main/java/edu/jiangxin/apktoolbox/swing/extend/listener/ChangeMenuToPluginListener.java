package edu.jiangxin.apktoolbox.swing.extend.listener;

import edu.jiangxin.apktoolbox.swing.extend.plugin.ChangeMenuPreparePluginCallBack;
import edu.jiangxin.apktoolbox.swing.extend.plugin.IPlugin;
import edu.jiangxin.apktoolbox.swing.extend.plugin.IPreparePluginCallback;
import edu.jiangxin.apktoolbox.swing.extend.plugin.PluginUtils;
import edu.jiangxin.apktoolbox.utils.Utils;

import java.io.File;

public abstract class ChangeMenuToPluginListener implements IPlugin, ChangeMenuListener {

    @Override
    public boolean isNeedPreChangeMenu() {
        return true;
    }

    @Override
    public void onPreChangeMenu(IPreChangeMenuCallBack callBack) {
        preparePlugin(new ChangeMenuPreparePluginCallBack(getPluginFilename(), isPluginNeedUnzip(), callBack));
    }

    @Override
    public void onChangeMenu() {
        String cmd = getPluginStartupCmd();
        Utils.unBlockedExecutor(cmd);
    }

    @Override
    public void preparePlugin(IPreparePluginCallback callBack) {
        callBack.onPrepareStarted();
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
