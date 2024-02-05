package edu.jiangxin.apktoolbox.reverse;

import edu.jiangxin.apktoolbox.swing.extend.PluginPanel;
import edu.jiangxin.apktoolbox.swing.extend.SelectFileActionListener;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class ApkSignerPanel extends PluginPanel {

    private static final long serialVersionUID = 1L;

    private JPanel apkPathPanel;

    private JTextField apkPathTextField;

    private JPanel keyStorePathPanel;

    private JTextField keyStorePathTextField;

    private JPanel keyStorePasswordPanel;

    private JPasswordField keyStorePasswordField;

    private JPanel aliasPanel;

    private JTextField aliasTextField;

    private JPanel aliasPasswordPanel;

    private JPasswordField aliasPasswordField;

    private JPanel operationPanel;

    public ApkSignerPanel() throws HeadlessException {
        super();
    }

    @Override
    public void onChangingMenu() {
        initUI();
    }

    @Override
    public String getPluginFilename() {
        return "apksigner.jar";
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

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
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        JButton recoverButton = new JButton("recover");
        recoverButton.addActionListener(new RecoverButtonActionListener());

        JButton apkSignButton = new JButton("apksigner");
        apkSignButton.addActionListener(new ApkSignButtonActionListener());

        operationPanel.add(recoverButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(apkSignButton);
    }

    private void createAliasPasswordPanel() {
        aliasPasswordPanel = new JPanel();
        aliasPasswordPanel.setLayout(new BoxLayout(aliasPasswordPanel, BoxLayout.X_AXIS));
        
        aliasPasswordField = new JPasswordField();
        aliasPasswordField.setText(conf.getString("apksigner.alias.password"));

        JLabel aliasPasswordLable = new JLabel("Alias Password");

        aliasPasswordPanel.add(aliasPasswordField);
        aliasPasswordPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        aliasPasswordPanel.add(aliasPasswordLable);
    }

    private void createAliasPanel() {
        aliasPanel = new JPanel();
        aliasPanel.setLayout(new BoxLayout(aliasPanel, BoxLayout.X_AXIS));
        
        aliasTextField = new JTextField();
        aliasTextField.setText(conf.getString("apksigner.alias"));

        JLabel aliasLable = new JLabel("Alias");

        aliasPanel.add(aliasTextField);
        aliasPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        aliasPanel.add(aliasLable);
    }

    private void createKeyStorePasswordPanel() {
        keyStorePasswordPanel = new JPanel();
        keyStorePasswordPanel.setLayout(new BoxLayout(keyStorePasswordPanel, BoxLayout.X_AXIS));
        
        keyStorePasswordField = new JPasswordField();
        keyStorePasswordField.setText(conf.getString("apksigner.keystore.password"));

        JLabel keyStorePasswordLable = new JLabel("KeyStore Password");

        keyStorePasswordPanel.add(keyStorePasswordField);
        keyStorePasswordPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        keyStorePasswordPanel.add(keyStorePasswordLable);
    }

    private void createKeyStorePathPanel() {
        keyStorePathPanel = new JPanel();
        keyStorePathPanel.setLayout(new BoxLayout(keyStorePathPanel, BoxLayout.X_AXIS));
        
        keyStorePathTextField = new JTextField();
        keyStorePathTextField.setText(conf.getString("apksigner.keystore.path"));

        JButton keyStorePathButton = new JButton("Select KeyStore");
        keyStorePathButton.addActionListener(new SelectFileActionListener("select a keystore file", keyStorePathTextField));

        keyStorePathPanel.add(keyStorePathTextField);
        keyStorePathPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        keyStorePathPanel.add(keyStorePathButton);
    }

    private void createApkPathPanel() {
        apkPathPanel = new JPanel();
        apkPathPanel.setLayout(new BoxLayout(apkPathPanel, BoxLayout.X_AXIS));

        apkPathTextField = new JTextField();
        apkPathTextField.setText(conf.getString("apksigner.apk.path"));

        JButton apkPathButton = new JButton("Select APK");
        apkPathButton.addActionListener(new SelectFileActionListener("select a APK file", apkPathTextField));

        apkPathPanel.add(apkPathTextField);
        apkPathPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        apkPathPanel.add(apkPathButton);
    }
    
    private final class RecoverButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            keyStorePathTextField.setText(conf.getString("default.apksigner.keystore.path"));
            keyStorePasswordField.setText(conf.getString("default.apksigner.keystore.password"));
            aliasTextField.setText(conf.getString("default.apksigner.alias"));
            aliasPasswordField.setText("default.apksigner.alias.password");
        }
    }

    private final class ApkSignButtonActionListener implements ActionListener {
        private String apkPath;
        private String keystorePath;
        private String keystorePassword;
        private String alias;
        private String alisaPassword;

        @Override
        public void actionPerformed(ActionEvent e) {
            File apkFile = new File(apkPathTextField.getText());
            if (!apkFile.exists()) {
                logger.error("srcFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "apk file is invalid", Constants.MESSAGE_DIALOG_TITLE,
                        JOptionPane.ERROR_MESSAGE);
                apkPathTextField.requestFocus();
                return;
            }

            File keystoreFile = new File(keyStorePathTextField.getText());
            if (!keystoreFile.exists()) {
                logger.error("keystoreFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "keystore file is invalid", Constants.MESSAGE_DIALOG_TITLE,
                        JOptionPane.ERROR_MESSAGE);
                keyStorePathTextField.requestFocus();
                return;
            }
            
            try {
                apkPath = apkFile.getCanonicalPath();
                keystorePath = keystoreFile.getCanonicalPath();
            } catch (IOException e2) {
                logger.error("getCanonicalPath fail");
                return;
            }
            
            conf.setProperty("apksigner.apk.path", apkPath);
            conf.setProperty("apksigner.keystore.path", keystorePath);

            keystorePassword = keyStorePasswordField.getText();
            if (StringUtils.isEmpty(keystorePassword)) {
                logger.error("keystorePassword is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "keystorePassword is invalid", Constants.MESSAGE_DIALOG_TITLE,
                        JOptionPane.ERROR_MESSAGE);
                keyStorePasswordField.requestFocus();
                return;
            }
            conf.setProperty("apksigner.keystore.password", keystorePassword);

            alias = aliasTextField.getText();
            if (StringUtils.isEmpty(alias)) {
                logger.error("alias is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "alias is invalid", Constants.MESSAGE_DIALOG_TITLE,
                        JOptionPane.ERROR_MESSAGE);
                aliasTextField.requestFocus();
                return;
            }
            conf.setProperty("apksigner.alias", alias);

            alisaPassword = aliasPasswordField.getText();
            if (StringUtils.isEmpty(alisaPassword)) {
                logger.error("alisaPassword is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "alisaPassword is invalid", Constants.MESSAGE_DIALOG_TITLE,
                        JOptionPane.ERROR_MESSAGE);
                aliasPasswordField.requestFocus();
                return;
            }
            conf.setProperty("apksigner.alias.password", alisaPassword);

            Utils.blockedExecutor(getCmd());
        }

        private String getCmd() {
            StringBuilder sb = new StringBuilder();
            sb.append(getPluginStartupCmd())
                    .append(" -keystore ").append(keystorePath).append(" -pswd ").append(keystorePassword)
                    .append(" -alias ").append(alias).append(" -aliaspswd ").append(alisaPassword).append(" ")
                    .append(apkPath);
            return sb.toString();
        }
    }
}
