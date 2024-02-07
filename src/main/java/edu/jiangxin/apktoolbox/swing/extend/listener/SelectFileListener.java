package edu.jiangxin.apktoolbox.swing.extend.listener;

import edu.jiangxin.apktoolbox.utils.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SelectFileListener implements ActionListener {

    private final String dialogTitle;
    private final JTextField pathTextField;

    private FileFilter fileFilter;

    public SelectFileListener(String dialogTitle, JTextField pathTextField) {
        super();
        this.dialogTitle = dialogTitle;
        this.pathTextField = pathTextField;
    }

    public SelectFileListener(String dialogTitle, JTextField pathTextField, FileFilter fileFilter) {
        super();
        this.dialogTitle = dialogTitle;
        this.pathTextField = pathTextField;
        this.fileFilter = fileFilter;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setDialogTitle(dialogTitle);
        if (fileFilter != null) {
            jfc.setFileFilter(fileFilter);
        }
        int ret = jfc.showDialog(new JLabel(), null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            String path = FileUtils.getCanonicalPathQuiet(file);
            pathTextField.setText(path);
        }
    }
}
