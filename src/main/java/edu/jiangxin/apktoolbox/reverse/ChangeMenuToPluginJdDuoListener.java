package edu.jiangxin.apktoolbox.reverse;

import edu.jiangxin.apktoolbox.swing.extend.listener.ChangeMenuToPluginListener;
import edu.jiangxin.apktoolbox.utils.Utils;

import java.io.File;

public class ChangeMenuToPluginJdDuoListener extends ChangeMenuToPluginListener {
    @Override
    public String getPluginFilename() {
        return "jd-gui-duo-2.0.79.zip";
    }

    @Override
    public boolean isPluginNeedUnzip() {
        return true;
    }

    @Override
    public String getPluginStartupCmd() {
        String dirPath = getPluginFilename().replace(".zip", "");
        return Utils.getPluginDirPath() + File.separator + dirPath + File.separator + "jd-gui-duo-2.0.79.exe";
    }
}