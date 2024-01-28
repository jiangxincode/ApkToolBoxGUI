package edu.jiangxin.apktoolbox.swing.extend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class DirectorySelectButtonActionListener implements ActionListener {
    private static final Logger logger = LogManager.getLogger(DirectorySelectButtonActionListener.class.getSimpleName());
    private String dialogTitle;
    private JTextField pathTextField;

    public DirectorySelectButtonActionListener(String dialogTitle, JTextField pathTextField) {
        super();
        this.dialogTitle = dialogTitle;
        this.pathTextField = pathTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle(dialogTitle);
        int ret = jfc.showDialog(new JLabel(), null);
        switch (ret) {
            case JFileChooser.APPROVE_OPTION:
                File file = jfc.getSelectedFile();
                try {
                    String path = file.getCanonicalPath();
                    pathTextField.setText(path);
                } catch (IOException ex) {
                    logger.error("getCanonicalPath failed: IOException");
                }
                break;
            default:
                break;
        }
    }
}
