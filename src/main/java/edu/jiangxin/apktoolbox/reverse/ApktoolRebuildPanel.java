package edu.jiangxin.apktoolbox.reverse;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class ApktoolRebuildPanel extends EasyPanel {

    private static final long serialVersionUID = 1L;

    public ApktoolRebuildPanel() throws HeadlessException {
        super();
        setPreferredSize(new Dimension(600, 160));
        setMaximumSize(new Dimension(600, 160));

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JPanel sourcePanel = new JPanel();
        sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.X_AXIS));
        add(sourcePanel);

        JTextField srcTextField = new JTextField();
        srcTextField.setText(conf.getString("apktool.rebuild.src.dir"));

        JButton srcButton = new JButton("Source Dir");
        srcButton.addMouseListener(new MouseAdapter() {
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
        });

        sourcePanel.add(srcTextField);
        sourcePanel.add(srcButton);

        JPanel targetPanel = new JPanel();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
        add(targetPanel);

        JTextField targetTextField = new JTextField();
        targetTextField.setText(conf.getString("apktool.rebuild.target.file"));

        JButton targetButton = new JButton("Save File");
        targetButton.addMouseListener(new MouseAdapter() {
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
        });

        targetPanel.add(targetTextField);
        targetPanel.add(targetButton);

        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
        add(optionPanel);

        JCheckBox signAPK = new JCheckBox("sign APK");
        signAPK.setSelected(false);
        optionPanel.add(signAPK);

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        add(operationPanel);

        JButton sceenshotButton = new JButton("Rebuild");
        sceenshotButton.addMouseListener(new MouseAdapter() {
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
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\"").append(" \"")
                            .append(Utils.getToolsPath()).append(File.separator).append("apktool_2.3.3.jar\"")
                            .append(" b ").append(srcPath).append(" -o ").append(targetPath);
                    String cmd = sb.toString();
                    logger.info(cmd);
                    Process process1 = Runtime.getRuntime().exec(cmd);
                    new StreamHandler(process1.getInputStream(), 0).start();
                    new StreamHandler(process1.getErrorStream(), 1).start();
                    process1.waitFor();
                    logger.info("rebuild finish");
                    if (signAPK.isSelected()) {
                        sb = new StringBuilder();
                        sb.append("java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\"").append(" \"")
                                .append(Utils.getToolsPath()).append(File.separator).append("apksigner.jar\"")
                                .append(" -keystore ").append(Utils.getToolsPath()).append(File.separator)
                                .append("debug.keystore").append(" -alias androiddebugkey -pswd android ")
                                .append(targetPath);
                        cmd = sb.toString();
                        logger.info(cmd);
                        Process process2 = Runtime.getRuntime().exec(cmd);
                        new StreamHandler(process2.getInputStream(), 0).start();
                        new StreamHandler(process2.getErrorStream(), 1).start();
                        process1.waitFor();
                        logger.info("apksign finish");
                    }
                } catch (IOException e1) {
                    logger.error("rebuild or apksign fail", e1);
                    return;
                } catch (InterruptedException e1) {
                    logger.error("rebuild or apksign fail", e1);
                    Thread.currentThread().interrupt();
                }
            }
        });

        operationPanel.add(sceenshotButton);
    }
}

class ApkFileFilter extends FileFilter {

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
