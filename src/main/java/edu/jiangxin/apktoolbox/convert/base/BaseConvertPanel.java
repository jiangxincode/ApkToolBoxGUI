package edu.jiangxin.apktoolbox.convert.base;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

public class BaseConvertPanel extends EasyPanel {
    private static final String PROPERTY_KEY = "name";

    private static final String DECIMAL = "Decimal";

    private static final String BINARY = "Binary";

    private static final String OCTAL = "Octal";

    private static final String HEX = "Hex";

    private JPanel binPanel;

    private JTextField binTextField;

    private JPanel octPanel;

    private JTextField octTextField;

    private JPanel decPanel;

    private JTextField decTextField;

    private JPanel hexPanel;

    private JTextField hexTextField;

    private final DocumentListener documentListener;

    private final DocumentFilter documentFilter;

    private boolean isChangedByUser;

    public BaseConvertPanel() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        documentListener = new TextFieldDocumentListener();
        documentFilter = new TextFieldDocumentFilter();

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

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ClearButtonActionListener());
        add(clearBtn);
    }

    private void createBinPanel() {
        binPanel = new JPanel();
        binPanel.setLayout(new BoxLayout(binPanel, BoxLayout.X_AXIS));

        JLabel binLabel = new JLabel(BINARY + ":");

        binTextField = new JTextField();
        binTextField.getDocument().addDocumentListener(documentListener);
        ((PlainDocument) binTextField.getDocument()).setDocumentFilter(documentFilter);
        binTextField.getDocument().putProperty(PROPERTY_KEY, BINARY);

        binPanel.add(binLabel);
        binPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        binPanel.add(binTextField);
    }

    private void createOctPanel() {
        octPanel = new JPanel();
        octPanel.setLayout(new BoxLayout(octPanel, BoxLayout.X_AXIS));

        JLabel octLabel = new JLabel(OCTAL + ":");

        octTextField = new JTextField();
        octTextField.getDocument().addDocumentListener(documentListener);
        ((PlainDocument) octTextField.getDocument()).setDocumentFilter(documentFilter);
        octTextField.getDocument().putProperty(PROPERTY_KEY, OCTAL);

        octPanel.add(octLabel);
        octPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        octPanel.add(octTextField);
    }

    private void createDecPanel() {
        decPanel = new JPanel();
        decPanel.setLayout(new BoxLayout(decPanel, BoxLayout.X_AXIS));

        JLabel decLabel = new JLabel(DECIMAL + ":");

        decTextField = new JTextField();
        decTextField.getDocument().addDocumentListener(documentListener);
        ((PlainDocument) decTextField.getDocument()).setDocumentFilter(documentFilter);
        decTextField.getDocument().putProperty(PROPERTY_KEY, DECIMAL);

        decPanel.add(decLabel);
        decPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        decPanel.add(decTextField);
    }

    private void createHexPanel() {
        hexPanel = new JPanel();
        hexPanel.setLayout(new BoxLayout(hexPanel, BoxLayout.X_AXIS));

        JLabel hexLabel = new JLabel(HEX + ":");

        hexTextField = new JTextField();
        hexTextField.getDocument().addDocumentListener(documentListener);
        ((PlainDocument) hexTextField.getDocument()).setDocumentFilter(documentFilter);
        hexTextField.getDocument().putProperty(PROPERTY_KEY, HEX);

        hexPanel.add(hexLabel);
        hexPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hexPanel.add(hexTextField);
    }

    class ClearButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            isChangedByUser = false;
            binTextField.setText("");
            octTextField.setText("");
            decTextField.setText("");
            hexTextField.setText("");
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

    class TextFieldDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            Object propertyObj = doc.getProperty(PROPERTY_KEY);
            if (propertyObj == null) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            String propertyStr = propertyObj.toString();

            if (isValidText(propertyStr, sb.toString())) {
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            Object propertyObj = doc.getProperty(PROPERTY_KEY);
            if (propertyObj == null) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            String propertyStr = propertyObj.toString();

            if (isValidText(propertyStr, sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    private void detect(DocumentEvent documentEvent) {
        Document doc = documentEvent.getDocument();
        Object propertyObj = doc.getProperty(PROPERTY_KEY);
        if (propertyObj == null) {
            logger.warn("property is null");
            return;
        }
        convertMe(propertyObj.toString());
    }

    private void convertMe(String type) {
        if (!isChangedByUser) {
            return;
        }

        String valueStr = getText(type);
        BigInteger value = new BigInteger(valueStr, getRadix(type));

        isChangedByUser = false;
        switch (type) {
            case BINARY:
                octTextField.setText(value.toString(8));
                decTextField.setText(value.toString(10));
                hexTextField.setText(value.toString(16));
                break;
            case OCTAL:
                binTextField.setText(value.toString(2));
                decTextField.setText(value.toString(10));
                hexTextField.setText(value.toString(16));
                break;
            case DECIMAL:
                binTextField.setText(value.toString(2));
                octTextField.setText(value.toString(8));
                hexTextField.setText(value.toString(16));
                break;
            case HEX:
                binTextField.setText(value.toString(2));
                octTextField.setText(value.toString(8));
                decTextField.setText(value.toString(10));
                break;
        }
        isChangedByUser = true;
    }

    private boolean isBinStr(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) != '1') && (str.charAt(i) != '0')) {
                return false;
            }
        }
        return true;
    }

    private boolean isDecStr(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) < '0') || (str.charAt(i) > '9')) {
                return false;
            }
        }
        return true;
    }

    private boolean isOctStr(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) < '0') || (str.charAt(i) > '7')) {
                return false;
            }
        }
        return true;
    }

    private boolean isHexStr(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c < '0' || c > '9') && (c < 'A' || c > 'F') && (c < 'a' || c > 'f')) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidText(String type, String text) {
        switch (type) {
            case BINARY: {
                return isBinStr(text);
            }
            case OCTAL: {
                return isOctStr(text);
            }
            case DECIMAL: {
                return isDecStr(text);
            }
            case HEX: {
                return isHexStr(text);
            }
            default: {
                return false;
            }
        }
    }

    private String getText(String type) {
        switch (type) {
            case BINARY: {
                return binTextField.getText();
            }
            case OCTAL: {
                return octTextField.getText();
            }
            case DECIMAL: {
                return decTextField.getText();
            }
            case HEX: {
                return hexTextField.getText();
            }
            default: {
                return "0";
            }
        }
    }

    private int getRadix(String type) {
        switch (type) {
            case BINARY: {
                return 2;
            }
            case OCTAL: {
                return 8;
            }
            case HEX: {
                return 16;
            }
            default: {
                return 10;
            }
        }
    }
}
