package edu.jiangxin.apktoolbox.file.checksum.panel;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.filepanel.FilePanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CompareFilesPanel extends EasyPanel {
    private JPanel filePanel;
    private FilePanel firstFilePanel;
    private FilePanel secondFilePanel;

    private JPanel operationPanel;

    private JTextField resultTextField;

    public CompareFilesPanel() {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createFilePanel();
        add(filePanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER * 24));
    }

    private void createFilePanel() {
        filePanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(filePanel, BoxLayout.Y_AXIS);
        filePanel.setLayout(boxLayout);

        JLabel firstFileLabel = new JLabel("Select the first file:");
        firstFilePanel = new FilePanel("Choose");

        JLabel secondFileLabel = new JLabel("Select the second file:");
        secondFilePanel = new FilePanel("Choose");

        filePanel.add(firstFileLabel);
        filePanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        filePanel.add(firstFilePanel);
        filePanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        filePanel.add(secondFileLabel);
        filePanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        filePanel.add(secondFilePanel);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();

        JButton compareButton = new JButton("Compare");
        compareButton.setFocusPainted(false);
        compareButton.addActionListener(event -> {
            File firstFile = firstFilePanel.getFile();
            File secondFile = secondFilePanel.getFile();
            if (firstFile == null || secondFile == null) {
                resultTextField.setText("File is not valid");
                resultTextField.setForeground(Color.YELLOW);
                return;
            }

            boolean isChecksumSame = false;

            try (FileInputStream firstFis = new FileInputStream(firstFile);
                 FileInputStream secondFis = new FileInputStream(secondFile)) {
                String firstSha512 = DigestUtils.sha512Hex(firstFis);
                String secondSha512 = DigestUtils.sha512Hex(secondFis);
                isChecksumSame = StringUtils.equalsIgnoreCase(firstSha512, secondSha512);
            } catch (FileNotFoundException e) {
                logger.error("calculate, FileNotFoundException");
            } catch (IOException e) {
                logger.error("calculate, IOException");
            }

            if (isChecksumSame) {
                resultTextField.setText("File is same");
                resultTextField.setForeground(Color.GREEN);
                return;
            }

            resultTextField.setText("File is different");
            resultTextField.setForeground(Color.RED);
        });

        resultTextField = new JTextField("To be comparing");
        resultTextField.setPreferredSize(new Dimension(100, 25));
        resultTextField.setEditable(false);
        resultTextField.setBorder(null);
        Font font = new Font(null, Font.BOLD, 16);
        resultTextField.setFont(font);
        resultTextField.setForeground(Color.YELLOW);

        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        operationPanel.add(compareButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(resultTextField);
        operationPanel.add(Box.createHorizontalGlue());
    }
}