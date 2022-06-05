package edu.jiangxin.apktoolbox.reverse;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author jiangxin
 * @author 2018-09-30
 *
 */
public class JadxActionListener implements ActionListener {

    private static final Logger logger = LogManager.getLogger(JadxActionListener.class.getSimpleName());

    private Configuration conf;

    public JadxActionListener() {
        conf = Utils.getConfiguration();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String toolPath = conf.getString(Constants.JADX_PATH_KEY);
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

        String cmd = conf.getString(Constants.JADX_PATH_KEY);
        Utils.unBlockedExecutor(cmd);
    }
}
