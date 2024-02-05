package edu.jiangxin.apktoolbox.reverse;

import edu.jiangxin.apktoolbox.swing.extend.listener.ChangeMenuToPluginListener;
import edu.jiangxin.apktoolbox.utils.Utils;

import java.io.File;

/**
 * @author jiangxin
 * @author 2018-09-30
 *
 */
public class ChangeMenuToPluginJadxListener extends ChangeMenuToPluginListener {
    @Override
    public String getPluginFilename() {
        return "jadx-1.4.7.zip";
    }

    @Override
    public boolean isPluginNeedUnzip() {
        return true;
    }

    @Override
    public String getPluginStartupCmd() {
        String dirPath = getPluginFilename().replace(".zip", "");
        return Utils.getPluginDirPath() + File.separator + dirPath + File.separator + "bin" + File.separator + "jadx-gui.bat";
    }
}
