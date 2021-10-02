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

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class ApkSignerPanel extends EasyPanel {

    private static final long serialVersionUID = 1L;

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

    private JButton apkSignButton;

    public ApkSignerPanel() throws HeadlessException {
        super();
        initUI();
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
        
        recoverButton = new JButton("recover");
        recoverButton.addMouseListener(new RecoverButtonMouseAdapter());

        apkSignButton = new JButton("apksigner");
        apkSignButton.addMouseListener(new ApkSignButtonMouseAdapter());

        operationPanel.add(recoverButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(apkSignButton);
    }

    private void createAliasPasswordPanel() {
        aliasPasswordPanel = new JPanel();
        aliasPasswordPanel.setLayout(new BoxLayout(aliasPasswordPanel, BoxLayout.X_AXIS));
        
        aliasPasswordField = new JPasswordField();
        aliasPasswordField.setText(conf.getString("apksigner.alias.password"));
        
        aliasPasswordLable = new JLabel("Alias Password");

        aliasPasswordPanel.add(aliasPasswordField);
        aliasPasswordPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        aliasPasswordPanel.add(aliasPasswordLable);
    }

    private void createAliasPanel() {
        aliasPanel = new JPanel();
        aliasPanel.setLayout(new BoxLayout(aliasPanel, BoxLayout.X_AXIS));
        
        aliasTextField = new JTextField();
        aliasTextField.setText(conf.getString("apksigner.alias"));
        
        aliasLable = new JLabel("Alias");

        aliasPanel.add(aliasTextField);
        aliasPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        aliasPanel.add(aliasLable);
    }

    private void createKeyStorePasswordPanel() {
        keyStorePasswordPanel = new JPanel();
        keyStorePasswordPanel.setLayout(new BoxLayout(keyStorePasswordPanel, BoxLayout.X_AXIS));
        
        keyStorePasswordField = new JPasswordField();
        keyStorePasswordField.setText(conf.getString("apksigner.keystore.password"));
        
        keyStorePasswordLable = new JLabel("KeyStore Password");

        keyStorePasswordPanel.add(keyStorePasswordField);
        keyStorePasswordPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        keyStorePasswordPanel.add(keyStorePasswordLable);
    }

    private void createKeyStorePathPanel() {
        keyStorePathPanel = new JPanel();
        keyStorePathPanel.setLayout(new BoxLayout(keyStorePathPanel, BoxLayout.X_AXIS));
        
        keyStorePathTextField = new JTextField();
        keyStorePathTextField.setText(conf.getString("apksigner.keystore.path"));

        keyStorePathButton = new JButton("Select KeyStore");
        keyStorePathButton.addMouseListener(new KeyStorePathButtonMouseAdapter());

        keyStorePathPanel.add(keyStorePathTextField);
        keyStorePathPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        keyStorePathPanel.add(keyStorePathButton);
    }

    private void createApkPathPanel() {
        apkPathPanel = new JPanel();
        apkPathPanel.setLayout(new BoxLayout(apkPathPanel, BoxLayout.X_AXIS));

        apkPathTextField = new JTextField();
        apkPathTextField.setText(conf.getString("apksigner.apk.path"));

        apkPathButton = new JButton("Select APK");
        apkPathButton.addMouseListener(new ApkPathButtonMouseAdapter());

        apkPathPanel.add(apkPathTextField);
        apkPathPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        apkPathPanel.add(apkPathButton);
    }

    private final class ApkPathButtonMouseAdapter extends MouseAdapter {
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
    }

    private final class KeyStorePathButtonMouseAdapter extends MouseAdapter {
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
    }
    
    private final class RecoverButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            keyStorePathTextField.setText(conf.getString("default.apksigner.keystore.path"));
            keyStorePasswordField.setText(conf.getString("default.apksigner.keystore.password"));
            aliasTextField.setText(conf.getString("default.apksigner.alias"));
            aliasPasswordField.setText("default.apksigner.alias.password");
        }
    }

    private final class ApkSignButtonMouseAdapter extends MouseAdapter {
        private File apkFile;
        private String apkPath;
        private File keystoreFile;
        private String keystorePath;
        private String keystorePassword;
        private String alias;
        private String alisaPassword;

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            apkFile = new File(apkPathTextField.getText());
            if (!apkFile.exists()) {
                logger.error("srcFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "apk file is invalid", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                apkPathTextField.requestFocus();
                return;
            }

            keystoreFile = new File(keyStorePathTextField.getText());
            if (!keystoreFile.exists()) {
                logger.error("keystoreFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "keystore file is invalid", "ERROR",
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
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "keystorePassword is invalid", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                keyStorePasswordField.requestFocus();
                return;
            }
            conf.setProperty("apksigner.keystore.password", keystorePassword);

            alias = aliasTextField.getText();
            if (StringUtils.isEmpty(alias)) {
                logger.error("alias is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "alias is invalid", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                aliasTextField.requestFocus();
                return;
            }
            conf.setProperty("apksigner.alias", alias);

            alisaPassword = aliasPasswordField.getText();
            if (StringUtils.isEmpty(alisaPassword)) {
                logger.error("alisaPassword is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(ApkSignerPanel.this, "alisaPassword is invalid", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                aliasPasswordField.requestFocus();
                return;
            }
            conf.setProperty("apksigner.alias.password", alisaPassword);

            Utils.blockedExecutor(getCmd());
        }

        private String getCmd() {
            StringBuilder sb = new StringBuilder();
            sb.append("java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\"").append(" \"")
                    .append(Utils.getToolsPath()).append(File.separator).append("apksigner.jar\"")
                    .append(" -keystore ").append(keystorePath).append(" -pswd ").append(keystorePassword)
                    .append(" -alias ").append(alias).append(" -aliaspswd ").append(alisaPassword).append(" ")
                    .append(apkPath);
            return sb.toString();
        }
    }
}
