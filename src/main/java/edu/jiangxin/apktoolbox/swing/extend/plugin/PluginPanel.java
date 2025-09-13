package edu.jiangxin.apktoolbox.swing.extend.plugin;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Utils;

import java.io.File;
import java.io.Serial;

public abstract class PluginPanel extends EasyPanel implements IPlugin {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isNeedPreChangeMenu() {
        return true;
    }

    @Override
    public void preparePlugin(IPreparePluginCallback callBack) {
        callBack.onPrepareStarted();
    }

    @Override
    public String getPluginStartupCmd() {
        String jarPath = Utils.getPluginDirPath() + File.separator + getPluginFilename();
        return "java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\" \"" + jarPath + "\"";
    }
}
