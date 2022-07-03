package edu.jiangxin.apktoolbox.file.checksum.panel;

import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
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

public class CompareFilesPanel extends EasyChildTabbedPanel {
    private FilePanel firstFilePanel;
    private FilePanel secondFilePanel;

    private JPanel operationPanel;

    private JTextField resultTextField;

    @Override
    public void createUI() {
        JPanel panel = new JPanel();

        setViewportView(panel);

        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        panel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createFilePanel();
        panel.add(firstFilePanel);
        panel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        panel.add(secondFilePanel);

        panel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createOperationPanel();
        panel.add(operationPanel);

        panel.add(Box.createVerticalGlue());
    }

    private void createFilePanel() {
        firstFilePanel = new FilePanel("First File");
        secondFilePanel = new FilePanel("Second File");
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