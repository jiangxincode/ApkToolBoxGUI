package edu.jiangxin.apktoolbox.convert.zh2unicode;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;

public class Zh2UnicodeConvertPanel extends EasyPanel {
    private JPanel zhPanel;

    private JTextArea zhTextArea;

    private JPanel unicodePanel;

    private JTextArea unicodeTextArea;

    public Zh2UnicodeConvertPanel() throws HeadlessException {
        super();
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createTextPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createOperationPanel();
    }

    private void createTextPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        add(textPanel);

        createZhPanel();
        textPanel.add(zhPanel);

        textPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        createUnicodePanel();
        textPanel.add(unicodePanel);
    }

    private void createZhPanel() {
        zhPanel = new JPanel();
        zhPanel.setLayout(new BorderLayout());
        zhPanel.setBorder(BorderFactory.createTitledBorder("中文"));

        zhTextArea = new JTextArea();
        zhTextArea.setToolTipText("输入中文");
        zhTextArea.setEditable(true);

        JScrollPane zhScrollPane = new JScrollPane(zhTextArea);
        zhScrollPane.setPreferredSize(new Dimension(200, 500));

        zhPanel.add(zhScrollPane);
    }

    private void createUnicodePanel() {
        unicodePanel = new JPanel();
        unicodePanel.setLayout(new BorderLayout());
        unicodePanel.setBorder(BorderFactory.createTitledBorder("Unicode"));

        unicodeTextArea = new JTextArea();
        unicodeTextArea.setToolTipText("Enter Unicode Character");
        unicodeTextArea.setEditable(true);

        JScrollPane unicodeScrollPane = new JScrollPane(unicodeTextArea);
        unicodeScrollPane.setPreferredSize(new Dimension(200, 500));

        unicodePanel.add(unicodeScrollPane);
    }

    private void createOperationPanel() {
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        add(operationPanel);

        JButton zh2UnicodeConvertBtn = new JButton("中文->Unicode");
        zh2UnicodeConvertBtn.addActionListener(e -> {
            String value = zhTextArea.getText();
            unicodeTextArea.setText(string2Unicode(value));
        });

        JButton unicode2ZhConvertBtn = new JButton("Unicode->中文");
        unicode2ZhConvertBtn.addActionListener(e -> {
            String value = unicodeTextArea.getText();
            zhTextArea.setText(unicode2String(value));
        });

        operationPanel.add(zh2UnicodeConvertBtn);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(unicode2ZhConvertBtn);
    }

    private String string2Unicode(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes;
        try {
            bytes = str.getBytes("unicode");
        } catch (UnsupportedEncodingException e) {
            logger.error("string2Unicode: UnsupportedEncodingException");
            return null;
        }
        for (int i = 2; i < bytes.length - 1; i += 2) {
            sb.append("\\u");
            String firstByte = Integer.toHexString(bytes[i] & 0xff);
            sb.append("0".repeat(2 - firstByte.length()));
            sb.append(firstByte);
            String secondByte = Integer.toHexString(bytes[i + 1] & 0xff);
            sb.append("0".repeat(2 - secondByte.length()));
            sb.append(secondByte);
        }
        return sb.toString().toLowerCase();
    }

    private String unicode2String(String unicodeStr) {
        StringBuilder sb = new StringBuilder();
        String[] characterArray = unicodeStr.toLowerCase().split("\\\\u");
        for (String s : characterArray) {
            if (s.equals("")) {
                continue;
            }
            char character = (char) Integer.parseInt(s.trim(), 16);
            sb.append(character);
        }
        return sb.toString();
    }
}
