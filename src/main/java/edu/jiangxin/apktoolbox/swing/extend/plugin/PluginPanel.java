package edu.jiangxin.apktoolbox.swing.extend.plugin;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.listener.IPreChangeMenuCallBack;
import edu.jiangxin.apktoolbox.utils.Utils;

import java.io.File;

public abstract class PluginPanel extends EasyPanel implements IPlugin {
    @Override
    public boolean isNeedPreChangeMenu() {
        return true;
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
        return "java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\" \"" + jarPath + "\"";
    }
}
