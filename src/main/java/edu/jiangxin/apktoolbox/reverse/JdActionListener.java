package edu.jiangxin.apktoolbox.reverse;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * @author jiangxin
 * @author 2018-09-30
 *
 */
public class JdActionListener implements ActionListener {

    private static final Logger logger = LogManager.getLogger(JdActionListener.class);

    private Configuration conf;

    public JdActionListener() {
        conf = Utils.getConfiguration();;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String toolPath = conf.getString(Constants.JD_GUI_PATH_KEY);
        File toolFile = null;
        if (!StringUtils.isEmpty(toolPath)) {
            toolFile = new File(toolPath);
        }
        if (StringUtils.isEmpty(toolPath) || toolFile == null || !toolFile.exists() || !toolFile.isFile()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Need Configuration", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            final String jarPath = conf.getString(Constants.JD_GUI_PATH_KEY);
            String[] cmdArray = new String[]{"java", "-Duser.language=en", "-Dfile.encoding=UTF8", "-jar", jarPath};
            logger.info(ArrayUtils.toString(cmdArray));
            Process process = Runtime.getRuntime().exec(cmdArray);
            new StreamHandler(process.getInputStream(), 0).start();
            new StreamHandler(process.getErrorStream(), 1).start();
        } catch (IOException e1) {
            logger.error("open gd-gui fail", e);
        }
    }
}
