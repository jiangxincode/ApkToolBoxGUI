package edu.jiangxin.apktoolbox.reverse;

import edu.jiangxin.apktoolbox.swing.extend.listener.SelectFileListener;
import edu.jiangxin.apktoolbox.swing.extend.plugin.PluginPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class ApkSignerPanel extends PluginPanel {

    private static final long serialVersionUID = 1L;

    private JTextField apkPathTextField;

    private JTextField keyStorePathTextField;

    private JPasswordField keyStorePasswordField;

    private JTextField aliasTextField;

    private JPasswordField aliasPasswordField;

    public ApkSignerPanel() throws HeadlessException {
        super();
    }

    @Override
    public String getPluginFilename() {
        return "apksigner.jar";
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createApkPathPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createKeyStorePathPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createKeyStorePasswordPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createAliasPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createAliasPasswordPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOptionPanel();
    }

    private void createOptionPanel() {
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        add(operationPanel);

        JButton recoverButton = new JButton("recover");
        recoverButton.addActionListener(new RecoverButtonActionListener());

        JButton apkSignButton = new JButton("apksigner");
        apkSignButton.addActionListener(new ApkSignButtonActionListener());

        operationPanel.add(recoverButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(apkSignButton);
    }

    private void createAliasPasswordPanel() {
        JPanel aliasPasswordPanel = new JPanel();
        aliasPasswordPanel.setLayout(new BoxLayout(aliasPasswordPanel, BoxLayout.X_AXIS));
        add(aliasPasswordPanel);
        
        aliasPasswordField = new JPasswordField();
        aliasPasswordField.setText(conf.getString("apksigner.alias.password"));

        JLabel aliasPasswordLable = new JLabel("Alias Password");

        aliasPasswordPanel.add(aliasPasswordField);
        aliasPasswordPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        aliasPasswordPanel.add(aliasPasswordLable);
    }

    private void createAliasPanel() {
        JPanel aliasPanel = new JPanel();
        aliasPanel.setLayout(new BoxLayout(aliasPanel, BoxLayout.X_AXIS));
        add(aliasPanel);
        
        aliasTextField = new JTextField();
        aliasTextField.setText(conf.getString("apksigner.alias"));

        JLabel aliasLable = new JLabel("Alias");

        aliasPanel.add(aliasTextField);
        aliasPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        aliasPanel.add(aliasLable);
    }

    private void createKeyStorePasswordPanel() {
        JPanel keyStorePasswordPanel = new JPanel();
        keyStorePasswordPanel.setLayout(new BoxLayout(keyStorePasswordPanel, BoxLayout.X_AXIS));
        add(keyStorePasswordPanel);
        
        keyStorePasswordField = new JPasswordField();
        keyStorePasswordField.setText(conf.getString("apksigner.keystore.password"));

        JLabel keyStorePasswordLable = new JLabel("KeyStore Password");

        keyStorePasswordPanel.add(keyStorePasswordField);
        keyStorePasswordPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        keyStorePasswordPanel.add(keyStorePasswordLable);
    }

    private void createKeyStorePathPanel() {
        JPanel keyStorePathPanel = new JPanel();
        keyStorePathPanel.setLayout(new BoxLayout(keyStorePathPanel, BoxLayout.X_AXIS));
        add(keyStorePathPanel);
        
        keyStorePathTextField = new JTextField();
        keyStorePathTextField.setText(conf.getString("apksigner.keystore.path"));

        JButton keyStorePathButton = new JButton("Select KeyStore");
        keyStorePathButton.addActionListener(new SelectFileListener("select a keystore file", keyStorePathTextField));

        keyStorePathPanel.add(keyStorePathTextField);
        keyStorePathPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        keyStorePathPanel.add(keyStorePathButton);
    }

    private void createApkPathPanel() {
        JPanel apkPathPanel = new JPanel();
        apkPathPanel.setLayout(new BoxLayout(apkPathPanel, BoxLayout.X_AXIS));
        add(apkPathPanel);

        apkPathTextField = new JTextField();
        apkPathTextField.setText(conf.getString("apksigner.apk.path"));

        JButton apkPathButton = new JButton("Select APK");
        apkPathButton.addActionListener(new SelectFileListener("select a APK file", apkPathTextField));

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

        @Override
        public void actionPerformed(ActionEvent e) {
            String apkPath = checkAndGetFileContent(apkPathTextField, "apksigner.apk.path", "apk file is invalid");
            if (StringUtils.isEmpty(apkPath)) {
                return;
            }

            String keystorePath = checkAndGetFileContent(keyStorePathTextField, "apksigner.keystore.path", "keystore file is invalid");
            if (StringUtils.isEmpty(keystorePath)) {
                return;
            }

            String keystorePassword = checkAndGetStringContent(keyStorePasswordField, "apksigner.keystore.password", "keystorePassword is invalid");
            if (StringUtils.isEmpty(keystorePassword)) {
                return;
            }

            String alias = checkAndGetStringContent(aliasTextField, "apksigner.alias", "alias is invalid");
            if (StringUtils.isEmpty(alias)) {
                return;
            }

            String aliasPassword = checkAndGetStringContent(aliasPasswordField, "apksigner.alias.password", "aliasPassword is invalid");
            if (StringUtils.isEmpty(aliasPassword)) {
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(getPluginStartupCmd())
                    .append(" -keystore ").append(keystorePath).append(" -pswd ").append(keystorePassword)
                    .append(" -alias ").append(alias).append(" -aliaspswd ").append(aliasPassword).append(" ")
                    .append(apkPath);

            Utils.blockedExecutor(sb.toString());
        }
    }
}
