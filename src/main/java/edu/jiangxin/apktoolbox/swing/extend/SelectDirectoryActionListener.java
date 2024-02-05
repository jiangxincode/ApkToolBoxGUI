package edu.jiangxin.apktoolbox.swing.extend;

import edu.jiangxin.apktoolbox.utils.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SelectDirectoryActionListener implements ActionListener {
    private final String dialogTitle;
    private final JTextField pathTextField;

    public SelectDirectoryActionListener(String dialogTitle, JTextField pathTextField) {
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
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            String path = FileUtils.getCanonicalPathQuiet(file);
            pathTextField.setText(path);
        }
    }
}
