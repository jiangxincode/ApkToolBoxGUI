package edu.jiangxin.apktoolbox.reverse;

import edu.jiangxin.apktoolbox.swing.extend.plugin.PluginPanel;
import edu.jiangxin.apktoolbox.swing.extend.listener.SelectDirectoryListener;
import edu.jiangxin.apktoolbox.swing.extend.listener.SelectFileListener;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ApktoolPanel extends PluginPanel {
    public ApktoolPanel() throws HeadlessException {
        super();
    }

    @Override
    public String getPluginFilename() {
        return "apktool_2.12.0.jar";
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JTabbedPane categoryTabbedPane = new JTabbedPane();
        add(categoryTabbedPane);

        JPanel decodeCategoryPanel = new ApktoolDecodePanel();
        JPanel rebuildCategoryPanel = new ApktoolRebuildPanel();

        categoryTabbedPane.addTab("Decode", null, decodeCategoryPanel, "Decode the APK file");
        categoryTabbedPane.setSelectedIndex(0);
        categoryTabbedPane.addTab("Rebuild", null, rebuildCategoryPanel, "Rebuild the APK file");
    }

    abstract class ApktoolPanelBase extends JPanel {
        protected JPanel srcPanel;

        protected JPanel targetPanel;

        protected JPanel optionPanel;

        protected JPanel operationPanel;

        protected ApktoolPanelBase() throws HeadlessException {
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
            add(operationPanel);
        }

        protected void createSrcPanel() {
            srcPanel = new JPanel();
            srcPanel.setLayout(new BoxLayout(srcPanel, BoxLayout.X_AXIS));
        }

        protected void createTargetPanel() {
            targetPanel = new JPanel();
            targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
        }

        protected void createOptionPanel() {
            optionPanel = new JPanel();
            optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
        }

        protected void createOperationPanel() {
            operationPanel = new JPanel();
            operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        }
    }

    class ApktoolDecodePanel extends ApktoolPanelBase {
        private JTextField srcTextField;

        private JTextField targetTextField;

        private JCheckBox resourceIgnoreCheckBox;

        private JCheckBox overrideCheckBox;

        public ApktoolDecodePanel() throws HeadlessException {
            super();
        }

        @Override
        public void createSrcPanel() {
            super.createSrcPanel();

            srcTextField = new JTextField();
            srcTextField.setText(conf.getString("apktool.decode.src.file"));

            JButton srcButton = new JButton(bundle.getString("choose.file.button"));
            srcButton.addActionListener(new SelectFileListener("select a file", srcTextField));

            srcPanel.add(srcTextField);
            srcPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
            srcPanel.add(srcButton);
        }

        @Override
        public void createTargetPanel() {
            super.createTargetPanel();

            targetTextField = new JTextField();
            targetTextField.setText(conf.getString("apktool.decode.target.dir"));

            JButton targetButton = new JButton(bundle.getString("save.dir.button"));
            targetButton.addActionListener(new SelectDirectoryListener("Save To", targetTextField));

            targetPanel.add(targetTextField);
            targetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
            targetPanel.add(targetButton);
        }

        @Override
        public void createOptionPanel() {
            super.createOptionPanel();

            resourceIgnoreCheckBox = new JCheckBox("Ignore res");
            resourceIgnoreCheckBox.setSelected(false);
            optionPanel.add(resourceIgnoreCheckBox);

            overrideCheckBox = new JCheckBox("Override");
            overrideCheckBox.setSelected(true);
            optionPanel.add(overrideCheckBox);
        }

        @Override
        public void createOperationPanel() {
            super.createOperationPanel();

            JButton decodeButton = new JButton("Decode");
            decodeButton.addActionListener(new ApktoolDecodePanel.DecodeButtonActionListener());

            operationPanel.add(decodeButton);
        }

        private final class DecodeButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String srcPath = checkAndGetFileContent(srcTextField, "apktool.decode.src.file", "Source file is invalid");
                if (srcPath == null) {
                    return;
                }

                String targetPath = checkAndGetDirContent(targetTextField, "apktool.decode.target.dir", "Target directory is invalid");
                if (targetPath == null) {
                    return;
                }

                String srcBaseName = FilenameUtils.getBaseName(srcPath);
                StringBuilder sb = new StringBuilder();
                sb.append(ApktoolPanel.this.getPluginStartupCmd()).append(" d ")
                        .append(srcPath).append(" -o ").append(targetPath).append(File.separator).append(srcBaseName);
                if (resourceIgnoreCheckBox.isSelected()) {
                    sb.append(" -r");
                }
                if (overrideCheckBox.isSelected()) {
                    sb.append(" -f");
                }
                Utils.executor(sb.toString(), true);
            }
        }
    }

    class ApktoolRebuildPanel extends ApktoolPanelBase {
        private JTextField srcTextField;

        private JTextField targetTextField;

        private JCheckBox signAPK;

        public ApktoolRebuildPanel() throws HeadlessException {
            super();
        }

        @Override
        public void createSrcPanel() {
            super.createSrcPanel();

            srcTextField = new JTextField();
            srcTextField.setText(conf.getString("apktool.rebuild.src.dir"));

            JButton srcButton = new JButton(bundle.getString("choose.dir.button"));
            srcButton.addActionListener(new SelectDirectoryListener("Select Directory", srcTextField));

            srcPanel.add(srcTextField);
            srcPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
            srcPanel.add(srcButton);
        }

        @Override
        public void createTargetPanel() {
            super.createTargetPanel();

            targetTextField = new JTextField();
            targetTextField.setText(conf.getString("apktool.rebuild.target.file"));

            JButton targetButton = new JButton(bundle.getString("save.file.button"));
            targetButton.addActionListener(new SelectFileListener("save to", targetTextField, new ApktoolRebuildPanel.ApkFileFilter()));

            targetPanel.add(targetTextField);
            targetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
            targetPanel.add(targetButton);
        }

        @Override
        public void createOptionPanel() {
            super.createOptionPanel();

            signAPK = new JCheckBox("sign APK");
            signAPK.setSelected(false);
            optionPanel.add(signAPK);
        }

        @Override
        public void createOperationPanel() {
            super.createOperationPanel();

            JButton rebuildButton = new JButton("Rebuild");
            rebuildButton.addActionListener(new ApktoolRebuildPanel.RebuildButtonActionListener());
            operationPanel.add(rebuildButton);
        }

        private final class RebuildButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String srcPath = checkAndGetDirContent(srcTextField, "apktool.rebuild.src.dir", "Source directory is invalid");
                if (srcPath == null) {
                    return;
                }

                String targetPath = checkAndGetNewFileContent(targetTextField, "apktool.rebuild.target.file", "Target file is invalid");
                if (targetPath == null) {
                    return;
                }

                StringBuilder sb = new StringBuilder();
                sb.append(ApktoolPanel.this.getPluginStartupCmd()).append(" b ")
                        .append(srcPath).append(" -o ").append(targetPath);
                Utils.executor(sb.toString(), true);
                if (signAPK.isSelected()) {
                    sb = new StringBuilder();
                    sb.append(ApktoolPanel.this.getPluginStartupCmd())
                            .append(" -keystore ").append(Utils.getToolsPath()).append(File.separator)
                            .append("debug.keystore").append(" -alias androiddebugkey -pswd android ")
                            .append(targetPath);
                    Utils.executor(sb.toString(), true);
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
}

