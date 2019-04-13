package edu.jiangxin.apktoolbox.reverse;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import edu.jiangxin.apktoolbox.swing.extend.JEasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class ApkSignerPanel extends JEasyPanel {

    private static final long serialVersionUID = 1L;
    
    private static final int PANEL_WIDTH = Constants.DEFAULT_WIDTH - 50;

    private static final int PANEL_HIGHT = 230;
    
    private static final int CHILD_PANEL_HIGHT = 30;
    
    private static final int CHILD_PANEL_LEFT_WIDTH = 600;
    
    private static final int CHILD_PANEL_RIGHT_WIDTH = 130;

    private JPanel apkPathPanel;

    private JTextField apkPathTextField;

    private JButton apkPathButton;

    private JPanel keyStorePathPanel;

    private JTextField keyStorePathTextField;

    private JButton keyStorePathButton;

    private JPanel keyStorePasswordPanel;

    private JPasswordField keyStorePasswordField;

    private JLabel keyStorePasswordLable;

    private JPanel aliasPanel;

    private JTextField aliasTextField;

    private JLabel aliasLable;

    private JPanel aliasPasswordPanel;

    private JPasswordField aliasPasswordField;

    private JLabel aliasPasswordLable;

    private JPanel operationPanel;

    private JButton recoverButton;

    private JButton sceenshotButton;

    public ApkSignerPanel() throws HeadlessException {
        super();

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);
        
        Utils.setJComponentSize(this, PANEL_WIDTH, PANEL_HIGHT);

        createApkPathPanel();
        add(apkPathPanel);
        
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createKeyStorePathPanel();
        add(keyStorePathPanel);
        
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createKeyStorePasswordPanel();
        add(keyStorePasswordPanel);
        
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createAliasPanel();
        add(aliasPanel);
        
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createAliasPasswordPanel();
        add(aliasPasswordPanel);
        
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOptionPanel();
        add(operationPanel);
    }

    private void createOptionPanel() {
        operationPanel = new JPanel();
        Utils.setJComponentSize(operationPanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        
        recoverButton = new JButton("recover");
        Utils.setJComponentSize(recoverButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
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

        sceenshotButton = new JButton("apksigner");
        Utils.setJComponentSize(sceenshotButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
        sceenshotButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                File apkFile = new File(apkPathTextField.getText());
                if (!apkFile.exists()) {
                    logger.error("srcFile is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(ApkSignerPanel.this, "apk file is invalid", "ERROR",
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
                    JOptionPane.showMessageDialog(ApkSignerPanel.this, "keystore file is invalid", "ERROR",
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
                    JOptionPane.showMessageDialog(ApkSignerPanel.this, "keystorePassword is invalid", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    keyStorePasswordField.requestFocus();
                    return;
                }
                conf.setProperty("apksigner.keystore.password", keystorePassword);

                String alias = aliasTextField.getText();
                if (StringUtils.isEmpty(alias)) {
                    logger.error("alias is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(ApkSignerPanel.this, "alias is invalid", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    aliasTextField.requestFocus();
                    return;
                }
                conf.setProperty("apksigner.alias", alias);

                String alisaPassword = aliasPasswordField.getText();
                if (StringUtils.isEmpty(alisaPassword)) {
                    logger.error("alisaPassword is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(ApkSignerPanel.this, "alisaPassword is invalid", "ERROR",
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

        operationPanel.add(recoverButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(sceenshotButton);
    }

    private void createAliasPasswordPanel() {
        aliasPasswordPanel = new JPanel();
        Utils.setJComponentSize(keyStorePasswordPanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);
        aliasPasswordPanel.setLayout(new BoxLayout(aliasPasswordPanel, BoxLayout.X_AXIS));
        
        aliasPasswordField = new JPasswordField();
        Utils.setJComponentSize(aliasPasswordField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);
        aliasPasswordField.setText(conf.getString("apksigner.alias.password"));
        
        aliasPasswordLable = new JLabel("Alias Password");
        Utils.setJComponentSize(aliasPasswordLable, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
        
        aliasPasswordPanel.add(aliasPasswordField);
        aliasPasswordPanel.add(Box.createHorizontalGlue());
        aliasPasswordPanel.add(aliasPasswordLable);
    }

    private void createAliasPanel() {
        aliasPanel = new JPanel();
        Utils.setJComponentSize(keyStorePasswordPanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);
        aliasPanel.setLayout(new BoxLayout(aliasPanel, BoxLayout.X_AXIS));
        
        aliasTextField = new JTextField();
        Utils.setJComponentSize(aliasTextField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);
        aliasTextField.setText(conf.getString("apksigner.alias"));
        
        aliasLable = new JLabel("Alias");
        Utils.setJComponentSize(aliasLable, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
        
        aliasPanel.add(aliasTextField);
        aliasPanel.add(Box.createHorizontalGlue());
        aliasPanel.add(aliasLable);
    }

    private void createKeyStorePasswordPanel() {
        keyStorePasswordPanel = new JPanel();
        Utils.setJComponentSize(keyStorePasswordPanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);
        keyStorePasswordPanel.setLayout(new BoxLayout(keyStorePasswordPanel, BoxLayout.X_AXIS));
        
        keyStorePasswordField = new JPasswordField();
        Utils.setJComponentSize(keyStorePasswordField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);
        keyStorePasswordField.setText(conf.getString("apksigner.keystore.password"));
        
        keyStorePasswordLable = new JLabel("KeyStore Password");
        Utils.setJComponentSize(keyStorePasswordLable, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
        
        keyStorePasswordPanel.add(keyStorePasswordField);
        keyStorePasswordPanel.add(Box.createHorizontalGlue());
        keyStorePasswordPanel.add(keyStorePasswordLable);
    }

    private void createKeyStorePathPanel() {
        keyStorePathPanel = new JPanel();
        Utils.setJComponentSize(keyStorePathPanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);
        keyStorePathPanel.setLayout(new BoxLayout(keyStorePathPanel, BoxLayout.X_AXIS));
        
        keyStorePathTextField = new JTextField();
        Utils.setJComponentSize(keyStorePathTextField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);
        keyStorePathTextField.setText(conf.getString("apksigner.keystore.path"));

        keyStorePathButton = new JButton("Select KeyStore");
        Utils.setJComponentSize(keyStorePathButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
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
        keyStorePathPanel.add(Box.createHorizontalGlue());
        keyStorePathPanel.add(keyStorePathButton);
    }

    private void createApkPathPanel() {
        apkPathPanel = new JPanel();
        Utils.setJComponentSize(apkPathPanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);
        apkPathPanel.setLayout(new BoxLayout(apkPathPanel, BoxLayout.X_AXIS));

        apkPathTextField = new JTextField();
        Utils.setJComponentSize(apkPathTextField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);
        apkPathTextField.setText(conf.getString("apksigner.apk.path"));

        apkPathButton = new JButton("Select APK");
        Utils.setJComponentSize(apkPathButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
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
        apkPathPanel.add(Box.createHorizontalGlue());
        apkPathPanel.add(apkPathButton);
    }
    
}
