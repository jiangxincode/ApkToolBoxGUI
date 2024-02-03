package edu.jiangxin.apktoolbox.convert.base;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BaseConvertPanel extends EasyPanel {
    private static final String PROPERTY_KEY = "name";

    private static final String DECIMAL = "Decimal";

    private static final String BINARY = "Binary";

    private static final String OCTAL = "Octal";

    private static final String HEX = "Hex";

    private final DocumentListener documentListener;

    private final DocumentFilter documentFilter;

    private boolean isChangedByUser;

    private final List<BaseUiObject> baseUiObjects = new ArrayList<>();

    public BaseConvertPanel() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        documentListener = new TextFieldDocumentListener();
        documentFilter = new TextFieldDocumentFilter();

        isChangedByUser = true;

        baseUiObjects.add(new BaseUiObject(2));
        baseUiObjects.add(new BaseUiObject(8));
        baseUiObjects.add(new BaseUiObject(10));
        baseUiObjects.add(new BaseUiObject(16));

        for (BaseUiObject baseUiObject : baseUiObjects) {
            add(baseUiObject.panel);
            add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        }

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ClearButtonActionListener());
        add(clearBtn);
    }


    class ClearButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            isChangedByUser = false;
            for (BaseUiObject baseUiObject : baseUiObjects) {
                baseUiObject.textField.setText("");
            }
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
            Integer radix = (Integer) doc.getProperty(PROPERTY_KEY);
            if (isValidText(radix, sb.toString())) {
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
            Integer radix = (Integer) doc.getProperty(PROPERTY_KEY);
            if (isValidText(radix, sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    private void detect(DocumentEvent documentEvent) {
        Document doc = documentEvent.getDocument();
        Integer radix = (Integer) doc.getProperty(PROPERTY_KEY);
        convertMe(radix);
    }

    private void convertMe(int radix) {
        if (!isChangedByUser) {
            return;
        }

        BigInteger value = null;
        for (BaseUiObject baseUiObject : baseUiObjects) {
            if (baseUiObject.radix == radix) {
                String text = baseUiObject.textField.getText();
                if (StringUtils.isEmpty(text)) {
                    value = null;
                } else {
                    value = new BigInteger(text, radix);
                }
            }
        }
        isChangedByUser = false;
        for (BaseUiObject baseUiObject : baseUiObjects) {
            if (baseUiObject.radix != radix) {
                if (value == null) {
                    baseUiObject.textField.setText("");
                } else {
                    baseUiObject.textField.setText(value.toString(baseUiObject.radix));
                }
            }
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

    private boolean isValidText(int radix, String text) {
        switch (radix) {
            case 2 -> {
                return isBinStr(text);
            }
            case 8 -> {
                return isOctStr(text);
            }
            case 10 -> {
                return isDecStr(text);
            }
            case 16 -> {
                return isHexStr(text);
            }
            default -> {
                return false;
            }
        }
    }

    class BaseUiObject {
        int radix;

        JPanel panel;

        JLabel label;

        JTextField textField;

        BaseUiObject(int radix) {
            this.radix = radix;
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

            label = new JLabel(this + ":");
            label.setPreferredSize(new Dimension(50, 25));

            textField = new JTextField();
            textField.getDocument().addDocumentListener(documentListener);
            ((PlainDocument) textField.getDocument()).setDocumentFilter(documentFilter);
            textField.getDocument().putProperty(PROPERTY_KEY, radix);

            panel.add(label);
            panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
            panel.add(textField);
        }

        @Override
        public String toString() {
            switch (radix) {
                case 2 -> {
                    return BINARY;
                }
                case 8 -> {
                    return OCTAL;
                }
                case 10 -> {
                    return DECIMAL;
                }
                case 16 -> {
                    return HEX;
                }
                default -> {
                    return String.valueOf(radix);
                }
            }
        }
    }
}
