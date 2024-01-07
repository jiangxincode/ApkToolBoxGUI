package edu.jiangxin.apktoolbox.reverse.apktool;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class ApktoolRebuildPanel extends EasyPanel {

    private static final long serialVersionUID = 1L;

    private JPanel srcPanel;

    private JTextField srcTextField;

    private JButton srcButton;

    private JPanel targetPanel;

    private JTextField targetTextField;

    private JButton targetButton;

    private JPanel optionPanel;

    private JCheckBox signAPK;

    private JPanel operationPanel;

    private JButton rebuildButton;

    public ApktoolRebuildPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createSrcPanel();
        add(srcPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createTargetPanel();
        add(targetPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOptionPanel();
        add(optionPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        operationPanel.add(rebuildButton);
    }

    private void createSrcPanel() {
        srcPanel = new JPanel();
        srcPanel.setLayout(new BoxLayout(srcPanel, BoxLayout.X_AXIS));
        srcTextField = new JTextField();
        srcTextField.setText(conf.getString("apktool.rebuild.src.dir"));

        srcButton = new JButton(bundle.getString("choose.dir.button"));
        srcButton.addMouseListener(new SrcButtonMouseAdapter());

        srcPanel.add(srcTextField);
        srcPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        srcPanel.add(srcButton);
    }

    private void createTargetPanel() {
        targetPanel = new JPanel();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));

        targetTextField = new JTextField();
        targetTextField.setText(conf.getString("apktool.rebuild.target.file"));

        targetButton = new JButton(bundle.getString("save.file.button"));
        targetButton.addMouseListener(new TargetButtonMouseAdapter());

        targetPanel.add(targetTextField);
        targetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        targetPanel.add(targetButton);
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));

        signAPK = new JCheckBox("sign APK");
        signAPK.setSelected(false);
        optionPanel.add(signAPK);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        add(operationPanel);

        rebuildButton = new JButton("Rebuild");
        rebuildButton.addMouseListener(new RebuildButtonMouseAdapter());
    }

    private final class SrcButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.setDialogTitle("select a directory");
            int ret = jfc.showDialog(new JLabel(), null);
            switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    File file = jfc.getSelectedFile();
                    srcTextField.setText(file.getAbsolutePath());
                    break;
                default:
                    break;
            }
        }
    }

    private final class TargetButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogType(JFileChooser.SAVE_DIALOG);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setDialogTitle("save to");
            jfc.setFileFilter(new ApkFileFilter());
            int ret = jfc.showDialog(new JLabel(), null);
            switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    File file = jfc.getSelectedFile();
                    targetTextField.setText(file.getAbsolutePath());
                    break;

                default:
                    break;
            }

        }
    }

    private final class RebuildButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            File srcFile = new File(srcTextField.getText());
            if (!srcFile.exists() || !srcFile.isDirectory()) {
                logger.error("srcFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApktoolRebuildPanel.this, "Source directory is invalid", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                srcTextField.requestFocus();
                return;
            }
            String srcPath;
            try {
                srcPath = srcFile.getCanonicalPath();
            } catch (IOException e2) {
                logger.error("getCanonicalPath fail");
                return;
            }
            conf.setProperty("apktool.rebuild.src.dir", srcPath);
            File targetFile = new File(targetTextField.getText());
            File targetParentFile = targetFile.getParentFile();
            if (!targetParentFile.exists() || !targetParentFile.isDirectory()) {
                logger.error("targetFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApktoolRebuildPanel.this, "Target file is invalid", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                targetTextField.requestFocus();
                return;
            }
            String targetPath;
            try {
                targetPath = targetFile.getCanonicalPath();
            } catch (IOException e2) {
                logger.error("getCanonicalPath fail");
                return;
            }
            conf.setProperty("apktool.rebuild.target.file", targetPath);
            StringBuilder sb = new StringBuilder();
            sb.append("java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\"").append(" \"")
                    .append(conf.getString(Constants.APKTOOL_PATH_KEY)).append("\"").append(" b ")
                    .append(srcPath).append(" -o ").append(targetPath);
            Utils.blockedExecutor(sb.toString());
            if (signAPK.isSelected()) {
                sb = new StringBuilder();
                sb.append("java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\"").append(" \"")
                        .append(conf.getString(Constants.APKTOOL_PATH_KEY)).append(" \"")
                        .append(" -keystore ").append(Utils.getToolsPath()).append(File.separator)
                        .append("debug.keystore").append(" -alias androiddebugkey -pswd android ")
                        .append(targetPath);
                Utils.blockedExecutor(sb.toString());
            }
        }
    }

    private final class ApkFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            String nameString = f.getName();
            return nameString.toLowerCase().endsWith(".apk");
        }

        @Override
        public String getDescription() {
            return "*.apk";
        }
    }
}
