package edu.jiangxin.apktoolbox.convert.base;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BaseConvertPanel extends EasyPanel {
    private static final String PROPERTY_KEY = "name";

    private static final String DECIMAL = "Decimal";

    private static final String BINARY = "Binary";

    private static final String OCTAL = "Octal";

    private static final String HEX = "Hex";

    private JPanel binPanel;

    private JLabel binLabel;

    private JTextField binTextField;

    private JPanel octPanel;

    private JLabel octLabel;

    private JTextField octTextField;

    private JPanel decPanel;

    private JLabel decLabel;

    private JTextField decTextField;

    private JPanel hexPanel;

    private JLabel hexLabel;

    private JTextField hexTextField;

    private JButton clearBtn;

    private DocumentListener documentListener;

    private boolean isChangedByUser;

    private String strBin, strOct, strDec, strHex;

    public BaseConvertPanel() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        documentListener = new TextFieldDocumentListener();

        strBin = strOct = strDec = strHex = "";
        isChangedByUser = true;

        createBinPanel();
        add(binPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOctPanel();
        add(octPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createDecPanel();
        add(decPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createHexPanel();
        add(hexPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ClearButtonActionListener());
        add(clearBtn);
    }

    private void createBinPanel() {
        binPanel = new JPanel();
        binPanel.setLayout(new BoxLayout(binPanel, BoxLayout.X_AXIS));

        binLabel = new JLabel(BINARY + ":");

        binTextField = new JTextField();
        binTextField.getDocument().addDocumentListener(documentListener);
        binTextField.getDocument().putProperty(PROPERTY_KEY, BINARY);

        binPanel.add(binLabel);
        binPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        binPanel.add(binTextField);
    }

    private void createOctPanel() {
        octPanel = new JPanel();
        octPanel.setLayout(new BoxLayout(octPanel, BoxLayout.X_AXIS));

        octLabel = new JLabel(OCTAL + ":");

        octTextField = new JTextField();
        octTextField.getDocument().addDocumentListener(documentListener);
        octTextField.getDocument().putProperty(PROPERTY_KEY, OCTAL);

        octPanel.add(octLabel);
        octPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        octPanel.add(octTextField);
    }

    private void createDecPanel() {
        decPanel = new JPanel();
        decPanel.setLayout(new BoxLayout(decPanel, BoxLayout.X_AXIS));

        decLabel = new JLabel(DECIMAL + ":");

        decTextField = new JTextField();
        decTextField.getDocument().addDocumentListener(documentListener);
        decTextField.getDocument().putProperty(PROPERTY_KEY, DECIMAL);

        decPanel.add(decLabel);
        decPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        decPanel.add(decTextField);
    }

    private void createHexPanel() {
        hexPanel = new JPanel();
        hexPanel.setLayout(new BoxLayout(hexPanel, BoxLayout.X_AXIS));

        hexLabel = new JLabel(HEX + ":");

        hexTextField = new JTextField();
        hexTextField.getDocument().addDocumentListener(documentListener);
        hexTextField.getDocument().putProperty(PROPERTY_KEY, HEX);

        hexPanel.add(hexLabel);
        hexPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hexPanel.add(hexTextField);
    }

    class ClearButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            strBin = strOct = strDec = strHex = "";
            isChangedByUser = false;
            binTextField.setText(strBin);
            octTextField.setText(strOct);
            decTextField.setText(strDec);
            hexTextField.setText(strHex);
            isChangedByUser = true;
        }
    }

    class TextFieldDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            detect(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            detect(e);
        }
    }

    private void detect(DocumentEvent documentEvent) {
        Document doc = documentEvent.getDocument();
        Object propertyObj = doc.getProperty(PROPERTY_KEY);
        if (propertyObj == null) {
            logger.warn("property is null");
            return;
        }
        String propertyStr = propertyObj.toString();
        if (propertyStr.equals(BINARY)) {
            convertMe(BINARY, binTextField.getText());
        } else if (propertyStr.equals(OCTAL)) {
            convertMe(OCTAL, octTextField.getText());
        } else if (propertyStr.equals(DECIMAL)) {
            convertMe(DECIMAL, decTextField.getText());
        } else if (propertyStr.equals(HEX)) {
            convertMe(HEX, hexTextField.getText());
        } else {
            logger.warn("property is invalid");
        }
    }

    public void convertMe(String type, String content) {
        if (!isChangedByUser) {
            return;
        }
        if (content.length() == 0) {
            content = "0";
        }
        long value = 0;
        if (BINARY.equals(type)) {
            try {
                value = Long.valueOf(content, 2);
            } catch (NumberFormatException e) {
                logger.warn("convert error: NumberFormatException content: " + content);
                isChangedByUser = false;
                binTextField.setText(strBin);
                isChangedByUser = true;
                return;
            }

            isChangedByUser = false;
            octTextField.setText(Long.toOctalString(value));
            decTextField.setText(Long.toString(value));
            hexTextField.setText(Long.toHexString(value));
            isChangedByUser = true;
        } else if (OCTAL.equals(type)) {
            try {
                value = Long.valueOf(content, 8);
            } catch (NumberFormatException e) {
                logger.warn("convert error: NumberFormatException content: " + content);
                isChangedByUser = false;
                octTextField.setText(strOct);
                isChangedByUser = true;
                return;
            }
            isChangedByUser = false;
            binTextField.setText(Long.toBinaryString(value));
            decTextField.setText(Long.toString(value));
            hexTextField.setText(Long.toHexString(value));
            isChangedByUser = true;
        } else if (DECIMAL.equals(type)) {
            try {
                value = Long.valueOf(content, 10);
            } catch (NumberFormatException e) {
                logger.warn("convert error: NumberFormatException content: " + content);
                isChangedByUser = false;
                decTextField.setText(strDec);
                isChangedByUser = true;
                return;
            }
            isChangedByUser = false;
            binTextField.setText(Long.toBinaryString(value));
            octTextField.setText(Long.toOctalString(value));
            hexTextField.setText(Long.toHexString(value));
            isChangedByUser = true;
        } else if (HEX.equals(type)) {
            try {
                value = Long.valueOf(content, 16);
            } catch (NumberFormatException e) {
                logger.warn("convert error: NumberFormatException content: " + content);
                isChangedByUser = false;
                hexTextField.setText(strHex);
                isChangedByUser = true;
                return;
            }
            isChangedByUser = false;
            binTextField.setText(Long.toBinaryString(value));
            octTextField.setText(Long.toOctalString(value));
            decTextField.setText(Long.toString(value));
            isChangedByUser = true;
        }
        strBin = binTextField.getText();
        strOct = octTextField.getText();
        strDec = decTextField.getText();
        strHex = hexTextField.getText();
        return;
    }

    private boolean isBinStr(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++)
            if ((str.charAt(i) == '1') || (str.charAt(i) == '0'))
                ;
            else
                return false;
        return true;
    }

    private boolean isDecimalStr(String deci) {
        int len = deci.length();
        char ch;
        for (int i = 0; i < len; i++) {
            ch = deci.charAt(i);
            if ((ch >= '0') && (ch <= '9')) ;
            else
                return false;
        }
        return true;
    }

    private boolean isOctStr(String octal) {
        int len = octal.length();
        for (int i = 0; i < len; i++)
            if ((octal.charAt(i) >= '0') && (octal.charAt(i) <= '7')) ;
            else
                return false;
        return true;
    }

    private boolean isHexString(String hex) {
        int len = hex.length();
        int c;
        for (int i = 0; i < len; i++) {
            c = (int) hex.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')) ;
            else
                return false;
        }
        return true;
    }
}
