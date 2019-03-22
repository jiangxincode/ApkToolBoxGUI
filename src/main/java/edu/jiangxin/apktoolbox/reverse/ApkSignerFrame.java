package edu.jiangxin.apktoolbox.reverse;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import edu.jiangxin.apktoolbox.swing.extend.JEasyFrame;
import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;

public class ApkSignerFrame extends JEasyFrame {

    private static final long serialVersionUID = 1L;

    public ApkSignerFrame() throws HeadlessException {
        super();
        setTitle("ApkSigner");
        setSize(600, 220);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        BoxLayout boxLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        contentPane.setLayout(boxLayout);
        setContentPane(contentPane);

        JPanel apkPathPanel = new JPanel();
        apkPathPanel.setLayout(new BoxLayout(apkPathPanel, BoxLayout.X_AXIS));
        contentPane.add(apkPathPanel);

        JTextField apkPathTextField = new JTextField();
        apkPathTextField.setText(conf.getString("apksigner.apk.path"));

        JButton apkPathButton = new JButton("Select APK");
        apkPathButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setDialogTitle("select a APK file");
                int ret = jfc.showDialog(new JLabel(), null);
                switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    File file = jfc.getSelectedFile();
                    apkPathTextField.setText(file.getAbsolutePath());
                    break;
                default:
                    break;
                }

            }
        });

        apkPathPanel.add(apkPathTextField);
        apkPathPanel.add(apkPathButton);

        JPanel keyStorePathPanel = new JPanel();
        keyStorePathPanel.setLayout(new BoxLayout(keyStorePathPanel, BoxLayout.X_AXIS));
        contentPane.add(keyStorePathPanel);

        JTextField keyStorePathTextField = new JTextField();
        keyStorePathTextField.setText(conf.getString("apksigner.keystore.path"));

        JButton keyStorePathButton = new JButton("Select KeyStore");
        keyStorePathButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setDialogTitle("select a keystore file");
                int ret = jfc.showDialog(new JLabel(), null);
                switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    File file = jfc.getSelectedFile();
                    keyStorePathTextField.setText(file.getAbsolutePath());
                    break;
                default:
                    break;
                }

            }
        });

        keyStorePathPanel.add(keyStorePathTextField);
        keyStorePathPanel.add(keyStorePathButton);

        JPanel keyStorePasswordPanel = new JPanel();
        keyStorePasswordPanel.setLayout(new BoxLayout(keyStorePasswordPanel, BoxLayout.X_AXIS));
        contentPane.add(keyStorePasswordPanel);
        JPasswordField keyStorePasswordField = new JPasswordField();
        keyStorePasswordField.setText(conf.getString("apksigner.keystore.password"));
        JLabel keyStorePasswordLable = new JLabel("KeyStore Password");
        keyStorePasswordPanel.add(keyStorePasswordField);
        keyStorePasswordPanel.add(keyStorePasswordLable);

        JPanel aliasPanel = new JPanel();
        aliasPanel.setLayout(new BoxLayout(aliasPanel, BoxLayout.X_AXIS));
        contentPane.add(aliasPanel);
        JTextField aliasTextField = new JTextField();
        aliasTextField.setText(conf.getString("apksigner.alias"));
        JLabel aliasLable = new JLabel("Alias");
        aliasPanel.add(aliasTextField);
        aliasPanel.add(aliasLable);

        JPanel aliasPasswordPanel = new JPanel();
        aliasPasswordPanel.setLayout(new BoxLayout(aliasPasswordPanel, BoxLayout.X_AXIS));
        contentPane.add(aliasPasswordPanel);
        JPasswordField aliasPasswordField = new JPasswordField();
        aliasPasswordField.setText(conf.getString("apksigner.alias.password"));
        JLabel aliasPasswordLable = new JLabel("Alias Password");
        aliasPasswordPanel.add(aliasPasswordField);
        aliasPasswordPanel.add(aliasPasswordLable);

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        contentPane.add(operationPanel);

        JButton recoverButton = new JButton("recover");
        recoverButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                keyStorePathTextField.setText(conf.getString("default.apksigner.keystore.path"));
                keyStorePasswordField.setText(conf.getString("default.apksigner.keystore.password"));
                aliasTextField.setText(conf.getString("default.apksigner.alias"));
                aliasPasswordField.setText("default.apksigner.alias.password");
            }
        });

        operationPanel.add(recoverButton);

        JButton sceenshotButton = new JButton("apksigner");
        sceenshotButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                File apkFile = new File(apkPathTextField.getText());
                if (!apkFile.exists()) {
                    logger.error("srcFile is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(ApkSignerFrame.this, "apk file is invalid", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    apkPathTextField.requestFocus();
                    return;
                }
                String apkPath;
                try {
                    apkPath = apkFile.getCanonicalPath();
                } catch (IOException e2) {
                    logger.error("getCanonicalPath fail");
                    return;
                }
                conf.setProperty("apksigner.apk.path", apkPath);

                File keystoreFile = new File(keyStorePathTextField.getText());
                if (!keystoreFile.exists()) {
                    logger.error("keystoreFile is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(ApkSignerFrame.this, "keystore file is invalid", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    keyStorePathTextField.requestFocus();
                    return;
                }
                String keystorePath;
                try {
                    keystorePath = keystoreFile.getCanonicalPath();
                } catch (IOException e2) {
                    logger.error("getCanonicalPath fail");
                    return;
                }
                conf.setProperty("apksigner.keystore.path", keystorePath);

                String keystorePassword = keyStorePasswordField.getText();
                if (StringUtils.isEmpty(keystorePassword)) {
                    logger.error("keystorePassword is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(ApkSignerFrame.this, "keystorePassword is invalid", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    keyStorePasswordField.requestFocus();
                    return;
                }
                conf.setProperty("apksigner.keystore.password", keystorePassword);

                String alias = aliasTextField.getText();
                if (StringUtils.isEmpty(alias)) {
                    logger.error("alias is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(ApkSignerFrame.this, "alias is invalid", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    aliasTextField.requestFocus();
                    return;
                }
                conf.setProperty("apksigner.alias", alias);

                String alisaPassword = aliasPasswordField.getText();
                if (StringUtils.isEmpty(alisaPassword)) {
                    logger.error("alisaPassword is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(ApkSignerFrame.this, "alisaPassword is invalid", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    aliasPasswordField.requestFocus();
                    return;
                }
                conf.setProperty("apksigner.alias.password", alisaPassword);

                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\"").append(" \"")
                            .append(Utils.getToolsPath()).append(File.separator).append("apksigner.jar\"")
                            .append(" -keystore ").append(keystorePath).append(" -pswd ").append(keystorePassword)
                            .append(" -alias ").append(alias).append(" -aliaspswd ").append(alisaPassword).append(" ")
                            .append(apkPath);
                    String cmd = sb.toString();
                    logger.info(cmd);
                    Process process = Runtime.getRuntime().exec(cmd);
                    new StreamHandler(process.getInputStream(), 0).start();
                    new StreamHandler(process.getErrorStream(), 1).start();
                    process.waitFor();
                    logger.info("apksigner finish");
                } catch (IOException | InterruptedException e1) {
                    logger.error("apksigner fail", e);
                }
            }
        });

        operationPanel.add(sceenshotButton);
    }

}
