package edu.jiangxin.apktoolbox.reverse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.OSinfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2018-09-30
 *
 */
public class JadxActionListener implements ActionListener {

    private static final Logger logger = LogManager.getLogger(JadxActionListener.class);

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String shellPath = Utils.getToolsPath() + File.separator + Constants.FILENAME_JADX + File.separator + "bin" + File.separator;
            if (OSinfo.isWindows()) {
                shellPath += "jadx-gui.bat";
            } else {
                shellPath += "jadx-gui";
            }
            String[] cmdArray = null;
            if (OSinfo.isWindows()) {
                cmdArray = new String[]{"cmd", "/c", shellPath};
            } else {
                cmdArray = new String[]{"bash", shellPath};
            }
            logger.info(ArrayUtils.toString(cmdArray));
            Process process = Runtime.getRuntime().exec(cmdArray);
            new StreamHandler(process.getInputStream(), 0).start();
            new StreamHandler(process.getErrorStream(), 1).start();
        } catch (IOException e1) {
            logger.error("open JADX fail", e);
        }
    }
}
