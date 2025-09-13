package edu.jiangxin.apktoolbox.file.checksum.panel;

import edu.jiangxin.apktoolbox.file.checksum.CalculateType;
import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
import edu.jiangxin.apktoolbox.swing.extend.filepanel.FilePanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileChecksumPanel extends EasyChildTabbedPanel {
    private static final long serialVersionUID = 63924900336217723L;

    private FilePanel filePanel;

    private JPanel optionPanel;

    private JTextField sizeTextField;
    private JTextField lastModifiedTimeTextField;
    private JCheckBox md5CheckBox;
    private JTextField md5TextField;
    private JCheckBox sha1CheckBox;
    private JTextField sha1TextField;
    private JCheckBox sha256CheckBox;
    private JTextField sha256TextField;
    private JCheckBox sha384CheckBox;
    private JTextField sha384TextField;
    private JCheckBox sha512CheckBox;
    private JTextField sha512TextField;
    private JCheckBox crc32CheckBox;
    private JTextField crc32TextField;

    private JPanel operationPanel;
    private JProgressBar progressBar;

    @Override
    public void createUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createFileNamePanel();
        add(filePanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createOptionPanel();
        add(optionPanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createOperationPanel();
        add(operationPanel);
    }

    private void createFileNamePanel() {
        filePanel = new FilePanel("Choose File");
        filePanel.initialize();
        filePanel.setFileReadyCallback(file -> {
            if (file == null) {
                logger.error("file is null");
                return;
            }
            calculate(file);
        });
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(optionPanel, BoxLayout.Y_AXIS);
        optionPanel.setLayout(boxLayout);

        JPanel sizeOptionPanel = new JPanel();
        JPanel lastModifiedTimeOptionPanel = new JPanel();
        JPanel md5OptionPanel = new JPanel();
        JPanel sha1OptionPanel = new JPanel();
        JPanel sha256OptionPanel = new JPanel();
        JPanel sha384OptionPanel = new JPanel();
        JPanel sha512OptionPanel = new JPanel();
        JPanel crc32OptionPanel = new JPanel();

        optionPanel.add(sizeOptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(lastModifiedTimeOptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(md5OptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(sha1OptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(sha256OptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(sha384OptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(sha512OptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(crc32OptionPanel);

        sizeOptionPanel.setLayout(new BoxLayout(sizeOptionPanel, BoxLayout.X_AXIS));
        JLabel fileSizeLabel = new JLabel("Size:");
        sizeTextField = new JTextField();
        sizeOptionPanel.add(fileSizeLabel);
        sizeOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        sizeOptionPanel.add(sizeTextField);

        lastModifiedTimeOptionPanel.setLayout(new BoxLayout(lastModifiedTimeOptionPanel, BoxLayout.X_AXIS));
        JLabel fileLastModifiedTimeLabel = new JLabel("Last Modified Time:");
        lastModifiedTimeTextField = new JTextField();
        lastModifiedTimeOptionPanel.add(fileLastModifiedTimeLabel);
        lastModifiedTimeOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        lastModifiedTimeOptionPanel.add(lastModifiedTimeTextField);

        md5OptionPanel.setLayout(new BoxLayout(md5OptionPanel, BoxLayout.X_AXIS));
        md5CheckBox = new JCheckBox("MD5 checksum:");
        md5CheckBox.setSelected(true);
        md5TextField = new JTextField();
        md5OptionPanel.add(md5CheckBox);
        md5OptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        md5OptionPanel.add(md5TextField);

        sha1OptionPanel.setLayout(new BoxLayout(sha1OptionPanel, BoxLayout.X_AXIS));
        sha1CheckBox = new JCheckBox("SHA1 checksum:");
        sha1TextField = new JTextField();
        sha1OptionPanel.add(sha1CheckBox);
        sha1OptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        sha1OptionPanel.add(sha1TextField);

        sha256OptionPanel.setLayout(new BoxLayout(sha256OptionPanel, BoxLayout.X_AXIS));
        sha256CheckBox = new JCheckBox("SHA256 checksum:");
        sha256TextField = new JTextField();
        sha256OptionPanel.add(sha256CheckBox);
        sha256OptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        sha256OptionPanel.add(sha256TextField);

        sha384OptionPanel.setLayout(new BoxLayout(sha384OptionPanel, BoxLayout.X_AXIS));
        sha384CheckBox = new JCheckBox("SHA384 checksum:");
        sha384TextField = new JTextField();
        sha384OptionPanel.add(sha384CheckBox);
        sha384OptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        sha384OptionPanel.add(sha384TextField);

        sha512OptionPanel.setLayout(new BoxLayout(sha512OptionPanel, BoxLayout.X_AXIS));
        sha512CheckBox = new JCheckBox("SHA512 checksum:");
        sha512TextField = new JTextField();
        sha512OptionPanel.add(sha512CheckBox);
        sha512OptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        sha512OptionPanel.add(sha512TextField);

        crc32OptionPanel.setLayout(new BoxLayout(crc32OptionPanel, BoxLayout.X_AXIS));
        crc32CheckBox = new JCheckBox("CRC32 checksum:");
        crc32CheckBox.setSelected(true);
        crc32TextField = new JTextField();
        crc32OptionPanel.add(crc32CheckBox);
        crc32OptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        crc32OptionPanel.add(crc32TextField);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();

        JButton compareButton = new JButton("Compare");
        compareButton.setFocusPainted(false);
        compareButton.addActionListener(arg0 -> {
            File file = filePanel.getFile();
            if (file == null) {
                logger.error("file is null");
                return;
            }
            calculate(file);
        });

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMaximum(100);


        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        operationPanel.add(compareButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(progressBar);

    }

    private void calculate(File file) {
        progressBar.setValue(0);
        new Thread(()->{
            sizeTextField.setText(edu.jiangxin.apktoolbox.utils.FileUtils.sizeOfInHumanFormat(file));
            progressBar.setValue(progressBar.getValue() + 5);
        }).start();

        new Thread(()->{
            try {
                lastModifiedTimeTextField.setText(String.valueOf(FileUtils.lastModified(file)));
            } catch (IOException e) {
                lastModifiedTimeTextField.setText("Unknown");
            }
            progressBar.setValue(progressBar.getValue() + 5);
        }).start();

        new Thread(()->{
            if (md5CheckBox.isSelected()) {
                md5TextField.setText(calculate(CalculateType.Md5, file));
            } else {
                md5TextField.setText("");
            }
            progressBar.setValue(progressBar.getValue() + 15);
        }).start();

        new Thread(()->{
            if (sha1CheckBox.isSelected()) {
                sha1TextField.setText(calculate(CalculateType.Sha1, file));
            } else {
                sha1TextField.setText("");
            }
            progressBar.setValue(progressBar.getValue() + 15);
        }).start();

        new Thread(()->{
            if (sha256CheckBox.isSelected()) {
                sha256TextField.setText(calculate(CalculateType.Sha256, file));
            } else {
                sha256TextField.setText("");
            }
            progressBar.setValue(progressBar.getValue() + 15);
        }).start();

        new Thread(()->{
            if (sha384CheckBox.isSelected()) {
                sha384TextField.setText(calculate(CalculateType.Sha384, file));
            } else {
                sha384TextField.setText("");
            }
            progressBar.setValue(progressBar.getValue() + 15);
        }).start();

        new Thread(()->{
            if (sha512CheckBox.isSelected()) {
                sha512TextField.setText(calculate(CalculateType.Sha512, file));
            } else {
                sha512TextField.setText("");
            }
            progressBar.setValue(progressBar.getValue() + 15);
        }).start();

        new Thread(()->{
            if (crc32CheckBox.isSelected()) {
                crc32TextField.setText(calculate(CalculateType.Crc32, file));
            } else {
                crc32TextField.setText("");
            }
            progressBar.setValue(progressBar.getValue() + 15);
        }).start();
    }

    private String calculate(final CalculateType selectedHash, final File file) {
        String result = "";
        try (FileInputStream fis = new FileInputStream(file)) {
            switch (selectedHash) {
                case Md5: {
                    result = DigestUtils.md5Hex(fis);
                    break;
                }
                case Sha1: {
                    result = DigestUtils.sha1Hex(fis);
                    break;
                }
                case Sha256: {
                    result = DigestUtils.sha256Hex(fis);
                    break;
                }
                case Sha384: {
                    result = DigestUtils.sha384Hex(fis);
                    break;
                }
                case Sha512: {
                    result = DigestUtils.sha512Hex(fis);
                    break;
                }
                case Crc32: {
                    result = Long.toHexString(FileUtils.checksumCRC32(file));
                    break;
                }
                default: {
                    result = "Not support";
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("calculate, FileNotFoundException");
        } catch (IOException e) {
            logger.error("calculate, IOException");
        }
        return result;
    }
}

