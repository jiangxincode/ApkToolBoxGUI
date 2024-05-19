package edu.jiangxin.apktoolbox.file.checksum.panel;

import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

public class VerifyChecksumPanel extends EasyChildTabbedPanel {
    private JPanel fileSumPanel;
    private JTextArea fileSumTextArea;

    private JPanel compareSumPanel;
    private JTextArea compareSumTextArea;

    private JPanel operationPanel;

    private JTextField resultTextField;


    @Override
    public void createUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createFileSumPanel();
        add(fileSumPanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createCompareSumPanel();
        add(compareSumPanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createFileSumPanel() {
        fileSumPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(fileSumPanel, BoxLayout.Y_AXIS);
        fileSumPanel.setLayout(boxLayout);

        JLabel fileSumLabel = new JLabel("Paste calculated file summary from \"File Checksum\" tabbed panel here:");
        fileSumTextArea = new JTextArea(5, 4);
        fileSumTextArea.setLineWrap(true);
        JScrollPane fileSumScrollPanel = new JScrollPane(fileSumTextArea);

        fileSumPanel.add(fileSumLabel);
        fileSumPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        fileSumPanel.add(fileSumScrollPanel);
    }

    private void createCompareSumPanel() {
        compareSumPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(compareSumPanel, BoxLayout.Y_AXIS);
        compareSumPanel.setLayout(boxLayout);

        JLabel compareSumLabel = new JLabel("Paste your compared file summary here:");
        compareSumTextArea = new JTextArea(5, 4);
        compareSumTextArea.setLineWrap(true);
        JScrollPane compareSumScrollPanel = new JScrollPane(compareSumTextArea);

        compareSumPanel.add(compareSumLabel);
        compareSumPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        compareSumPanel.add(compareSumScrollPanel);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();

        JButton compareButton = new JButton("Compare");
        compareButton.setFocusPainted(false);
        compareButton.addActionListener(event -> {
            String fileSum = fileSumTextArea.getText();
            if (!isValidSummary(fileSum)) {
                resultTextField.setText("File summary is not valid");
                resultTextField.setForeground(Color.YELLOW);
                return;
            }

            String compareSum = compareSumTextArea.getText();
            if (!isValidSummary(compareSum)) {
                resultTextField.setText("Compared summary is not valid");
                resultTextField.setForeground(Color.YELLOW);
                return;
            }

            if (StringUtils.equalsIgnoreCase(fileSum, compareSum)) {
                resultTextField.setText("File summary is same with compared summary");
                resultTextField.setForeground(Color.GREEN);
                return;
            }

            resultTextField.setText("File summary is different from compared summary");
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

    private boolean isValidSummary(String summary) {
        if (StringUtils.isEmpty(summary)) {
            return false;
        }
        return StringUtils.containsOnly(summary, "0123456789ABCDEFabcdef");
    }
}


