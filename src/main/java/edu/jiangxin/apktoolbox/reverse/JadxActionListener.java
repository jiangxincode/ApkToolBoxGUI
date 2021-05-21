package edu.jiangxin.apktoolbox.reverse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import edu.jiangxin.apktoolbox.utils.Constants;
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
            StringBuilder sb = new StringBuilder();
            sb.append("cmd /c").append(Utils.getToolsPath()).append(File.separator).append(Constants.FILENAME_JADX)
                    .append(File.separator).append("bin").append(File.separator).append("jadx-gui.bat");
            String cmd = sb.toString();
            logger.info(cmd);
            Process process = Runtime.getRuntime().exec(cmd);
            new StreamHandler(process.getInputStream(), 0).start();
            new StreamHandler(process.getErrorStream(), 1).start();
        } catch (IOException e1) {
            logger.error("open gd-gui fail", e);
        }
    }
}
