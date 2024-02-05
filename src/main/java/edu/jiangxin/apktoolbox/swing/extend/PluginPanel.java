package edu.jiangxin.apktoolbox.swing.extend;

import edu.jiangxin.apktoolbox.swing.extend.download.IPlugin;
import edu.jiangxin.apktoolbox.utils.Utils;

import java.io.File;

public abstract class PluginPanel extends EasyPanel implements IPlugin {
    @Override
    public boolean onPreChangeMenu() {
        return onCheckAndDownloadPlugin();
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
