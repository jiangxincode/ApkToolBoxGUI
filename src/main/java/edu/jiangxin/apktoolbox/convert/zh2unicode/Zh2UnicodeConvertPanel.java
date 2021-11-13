package edu.jiangxin.apktoolbox.convert.zh2unicode;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;

//http://www.jsons.cn/unicode
//http://tool.chinaz.com/tools/unicode.aspx
public class Zh2UnicodeConvertPanel extends EasyPanel {
    private JPanel zhPanel;

    private JLabel zhLabel;

    private JTextField zhTextField;

    private JPanel unicodePanel;

    private JLabel unicodeLabel;

    private JTextField unicodeTextField;

    private JPanel operationPanel;

    private JButton zh2UnicodeConvertBtn;

    private JButton unicode2ZhConvertBtn;

    public Zh2UnicodeConvertPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createZhPanel();
        add(zhPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createUnicodePanel();
        add(unicodePanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createZhPanel() {
        zhPanel = new JPanel();
        zhPanel.setLayout(new BoxLayout(zhPanel, BoxLayout.X_AXIS));

        zhLabel = new JLabel("中文: ");

        zhTextField = new JTextField();
        zhTextField.setToolTipText("输入中文");
        zhTextField.setEditable(true);

        zhPanel.add(zhLabel);
        zhPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        zhPanel.add(zhTextField);
    }

    private void createUnicodePanel() {
        unicodePanel = new JPanel();
        unicodePanel.setLayout(new BoxLayout(unicodePanel, BoxLayout.X_AXIS));

        unicodeLabel = new JLabel("Unicode: ");

        unicodeTextField = new JTextField();
        unicodeTextField.setToolTipText("Enter Unicode Character");
        unicodeTextField.setEditable(true);

        unicodePanel.add(unicodeLabel);
        unicodePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        unicodePanel.add(unicodeTextField);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        zh2UnicodeConvertBtn = new JButton("中文->Unicode");
        zh2UnicodeConvertBtn.addActionListener(e -> {
            String value = zhTextField.getText();
            unicodeTextField.setText(string2Unicode(value));
        });

        unicode2ZhConvertBtn = new JButton("Unicode->中文");
        unicode2ZhConvertBtn.addActionListener(e -> {
            String value = unicodeTextField.getText();
            zhTextField.setText(unicode2String(value));
        });

        operationPanel.add(zh2UnicodeConvertBtn);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(unicode2ZhConvertBtn);
    }

    private String string2Unicode(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        byte[] bytes;
        try {
            bytes = str.getBytes("unicode");
        } catch (UnsupportedEncodingException e) {
            logger.error("string2Unicode: UnsupportedEncodingException");
            return null;
        }
        for (int i = 2; i < bytes.length - 1; i += 2) {
            stringBuffer.append("\\u");
            String firstByte = Integer.toHexString(bytes[i] & 0xff);
            for (int j = firstByte.length(); j < 2; j++) {
                stringBuffer.append("0");
            }
            stringBuffer.append(firstByte);
            String secondByte = Integer.toHexString(bytes[i + 1] & 0xff);
            for (int j = secondByte.length(); j < 2; j++) {
                stringBuffer.append("0");
            }
            stringBuffer.append(secondByte);
        }
        return stringBuffer.toString().toLowerCase();
    }

    private String unicode2String(String unicodeStr) {
        StringBuffer stringBuffer = new StringBuffer();
        String characterArray[] = unicodeStr.toLowerCase().split("\\\\u");
        for (int i = 0; i < characterArray.length; i++) {
            if (characterArray[i].equals("")) {
                continue;
            }
            char character = (char) Integer.parseInt(characterArray[i].trim(), 16);
            stringBuffer.append(character);
        }
        return stringBuffer.toString();
    }
}
