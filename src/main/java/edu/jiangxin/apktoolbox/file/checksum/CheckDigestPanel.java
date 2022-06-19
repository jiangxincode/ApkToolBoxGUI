package edu.jiangxin.apktoolbox.file.checksum;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.filepanel.FilePanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CheckDigestPanel extends EasyPanel {
    private static final long serialVersionUID = 63924900336217723L;

    private FilePanel filePanel;

    private JTextArea fileSums;
    private JScrollPane fileSumScrollPanel;

    private JTextArea inputSums;
    private JScrollPane inputSumsScroll;

    private JPanel operationPanel;

    private JButton compareButton;

    private JTextField compareResult;

    private JComboBox<DigestType> digestTypeComboBox;
    private static final String calculating = "Calculating...";
    private static DigestType selectedHash;
    private static String hashResult;

    public CheckDigestPanel() {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createFileNamePanel();
        add(filePanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createFileSumScrollPanel();
        add(fileSumScrollPanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createInputSumScrollPanel();
        add(inputSumsScroll);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createFileNamePanel() {
        filePanel = new FilePanel("Choose File");
        filePanel.setFileReadyCallback(file -> {
            if (file != null) {
                selectedHash = (DigestType) digestTypeComboBox.getSelectedItem();
                calculate();
            }
        });
    }

    private void createFileSumScrollPanel() {
        fileSums = new JTextArea(5, 4);
        fileSums.setLineWrap(true);
        fileSums.setText("file not selected");
        fileSums.setEditable(false);
        fileSumScrollPanel = new JScrollPane(fileSums);
    }

    private void createInputSumScrollPanel() {
        inputSums = new JTextArea(5, 4);
        inputSums.setLineWrap(true);
        inputSums.setText("input sums");
        inputSumsScroll = new JScrollPane(inputSums);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();

        compareButton = new JButton("Compare");
        compareButton.setFocusPainted(false);
        compareButton.addActionListener(arg0 -> {

            if (!fileSums.getText().equals(calculating)) {
                if (selectedHash != null && filePanel.getFile() != null && inputSums.getText() != null
                        && !inputSums.getText().equals("")) {

                    if (fileSums.getText().equals(inputSums.getText())) {
                        compareResult.setText("match :)");
                    } else if (fileSums.getText().toLowerCase().equals(inputSums.getText().toLowerCase())) {
                        compareResult.setText("match :/");
                        JOptionPane.showMessageDialog(null, "Strings match but case insensitive :/", ":/",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        compareResult.setText("don't match :(");
                    }

                    return;
                }
            }

            JOptionPane.showMessageDialog(null, "Something not right :(", ":(", JOptionPane.ERROR_MESSAGE);
        });

        digestTypeComboBox = new JComboBox<>();
        digestTypeComboBox.setModel(new DefaultComboBoxModel<>(DigestType.values()));
        digestTypeComboBox.setSelectedIndex(0);
        digestTypeComboBox.addActionListener(e -> {
            DigestType selectedDigestItem = (DigestType) digestTypeComboBox.getSelectedItem();
            if (selectedHash == null || (selectedHash.getId() != selectedDigestItem.getId())) {
                selectedHash = selectedDigestItem;
                if (filePanel.getFile() != null) {
                    Thread calculateThread = (new Thread(() -> {
                        compareResult.setText("");
                        calculate();
                    }));
                    calculateThread.start();
                    fileSums.setText(calculating);
                }
            }

        });

        compareResult = new JTextField("");
        compareResult.setEditable(false);
        compareResult.setBorder(null);

        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        operationPanel.add(compareButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(digestTypeComboBox);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(compareResult);
    }

    private void calculate() {
        hashResult = calculate(selectedHash, filePanel.getFile());
        fileSums.setText(hashResult);
    }

    private String calculate(final DigestType selectedHash, final File file) {
        String result = "";
        try (FileInputStream fis = new FileInputStream(file)) {
            switch (selectedHash) {
                case MD5: {
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
                case CRC32: {
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
