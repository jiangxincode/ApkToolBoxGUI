package edu.jiangxin.apktoolbox.reverse;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.io.FilenameUtils;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class ApktoolDecodePanel extends EasyPanel {

    private static final long serialVersionUID = 1L;

    private JPanel srcPanel;

    private JTextField srcTextField;

    private JButton srcButton;

    private JPanel targetPanel;

    private JTextField targetTextField;

    private JButton targetButton;

    private JPanel optionPanel;

    private JCheckBox resourceIgnoreCheckBox;

    private JCheckBox overrideCheckBox;

    private JPanel operationPanel;

    private JButton decodeButton;

    public ApktoolDecodePanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        String toolPath = conf.getString(Constants.APKTOOL_PATH_KEY);
        File toolFile = null;
        if (!StringUtils.isEmpty(toolPath)) {
            toolFile = new File(toolPath);
        }
        if (StringUtils.isEmpty(toolPath) || toolFile == null || !toolFile.exists() || !toolFile.isFile()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Need Configuration", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        add(operationPanel);
    }

    private void createSrcPanel() {
        srcPanel = new JPanel();
        srcPanel.setLayout(new BoxLayout(srcPanel, BoxLayout.X_AXIS));

        srcTextField = new JTextField();
        srcTextField.setText(conf.getString("apktool.decode.src.file"));

        srcButton = new JButton("Source File");
        srcButton.addMouseListener(new SrcButtonMouseAdapter());

        srcPanel.add(srcTextField);
        srcPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        srcPanel.add(srcButton);
    }

    private void createTargetPanel() {
        targetPanel = new JPanel();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
        targetTextField = new JTextField();
        targetTextField.setText(conf.getString("apktool.decode.target.dir"));

        targetButton = new JButton("Save Dir");
        targetButton.addMouseListener(new TargetButtonMouseAdapter());

        targetPanel.add(targetTextField);
        targetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        targetPanel.add(targetButton);
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
        resourceIgnoreCheckBox = new JCheckBox("Ignore res");
        resourceIgnoreCheckBox.setSelected(false);
        optionPanel.add(resourceIgnoreCheckBox);

        overrideCheckBox = new JCheckBox("Override");
        overrideCheckBox.setSelected(true);
        optionPanel.add(overrideCheckBox);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        decodeButton = new JButton("Decode");
        decodeButton.addMouseListener(new DecodeButtonMouseAdapter());

        operationPanel.add(decodeButton);
    }

    private final class SrcButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setDialogTitle("select a file");
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
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.setDialogTitle("save to");
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

    private final class DecodeButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            File srcFile = new File(srcTextField.getText());
            if (!srcFile.exists() || !srcFile.isFile()) {
                logger.error("srcFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApktoolDecodePanel.this, "Source file is invalid", "ERROR",
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
            conf.setProperty("apktool.decode.src.file", srcPath);
            File targetFile = new File(targetTextField.getText());
            if (!targetFile.exists() || !targetFile.isDirectory()) {
                logger.error("targetFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApktoolDecodePanel.this, "Target directory is invalid", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                targetTextField.requestFocus();
                return;
            }
            String targetPath;
            String srcBaseName;
            try {
                targetPath = targetFile.getCanonicalPath();
                srcBaseName = FilenameUtils.getBaseName(srcFile.getCanonicalPath());
            } catch (IOException e2) {
                logger.error("getCanonicalPath fail");
                return;
            }
            conf.setProperty("apktool.decode.target.dir", targetPath);
            StringBuilder sb = new StringBuilder();
            sb.append("java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\"").append(" \"")
                    .append(conf.getString(Constants.APKTOOL_PATH_KEY)).append("\"").append(" d ")
                    .append(srcPath).append(" -o ").append(targetPath).append(File.separator).append(srcBaseName);
            if (resourceIgnoreCheckBox.isSelected()) {
                sb.append(" -r");
            }
            if (overrideCheckBox.isSelected()) {
                sb.append(" -f");
            }
            Utils.blockedExecutor(sb.toString());
        }
    }

}
