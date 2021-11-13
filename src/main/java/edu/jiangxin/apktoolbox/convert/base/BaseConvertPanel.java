package edu.jiangxin.apktoolbox.convert.base;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class BaseConvertPanel extends EasyPanel {
    private static final String DECIMAL = "Decimal";

    private static final String BINARY = "Binary";

    private static final String OCTAL = "Octal";

    private static final String HEX = "Hex";

    private JTextField decTextField;

    private JTextField binTextField;

    private JTextField octTextField;

    private JTextField hexTextField;

    private JButton clearBtn;

    private boolean isChangedByUser;

    private String strBin, strOct, strDec, strHex;

    public BaseConvertPanel() {
        setLayout(new GridLayout(5, 1));

        decTextField = new JTextField(17);
        decTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        decTextField.getDocument().putProperty("name", DECIMAL);

        binTextField = new JTextField(17);
        binTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        binTextField.getDocument().putProperty("name", BINARY);

        octTextField = new JTextField(17);
        octTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        octTextField.getDocument().putProperty("name", OCTAL);

        hexTextField = new JTextField(17);
        hexTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        hexTextField.getDocument().putProperty("name", HEX);

        strBin = strOct = strDec = strHex = "";
        isChangedByUser = true;

        JPanel binPanel = new JPanel();
        binPanel.add(new JLabel("Binary  :"));
        binPanel.add(binTextField);
        add(binPanel);

        JPanel decPanel = new JPanel();
        decPanel.add(new JLabel("Decimal :"));
        decPanel.add(decTextField);
        add(decPanel);

        JPanel octPanel = new JPanel();
        octPanel.add(new JLabel("Octal     :"));
        octPanel.add(octTextField);
        add(octPanel);

        JPanel hexPanel = new JPanel();
        hexPanel.add(new JLabel("Hex       :"));
        hexPanel.add(hexTextField);
        add(hexPanel);

        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ClearButtonActionListener());
        add(clearBtn);
    }

    class ClearButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            strBin = strOct = strDec = strHex = "";
            isChangedByUser = false;
            binTextField.setText(strBin);
            decTextField.setText(strDec);
            octTextField.setText(strOct);
            hexTextField.setText(strHex);
            isChangedByUser = true;
        }
    }

    class TextFieldDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent e) {
            //	detect(e);
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

    private synchronized void detect(DocumentEvent documentEvent) {
        Document doc = documentEvent.getDocument();
        String nameProp = doc.getProperty("name").toString();
        if (nameProp.equals(BINARY)) {
            convertMe(BINARY, binTextField.getText());
        } else if (nameProp.equals(DECIMAL)) {
            convertMe(DECIMAL, decTextField.getText());
        } else if (nameProp.equals(OCTAL)) {
            convertMe(OCTAL, octTextField.getText());
        } else if (nameProp.equals(HEX)) {
            convertMe(HEX, hexTextField.getText());
        }
    }

    public void convertMe(String type, String content) {
        if (!isChangedByUser) {
            return;
        }
        if (content.length() == 0) {
            content = "0";
        }
        if (BINARY.equals(type)) {
            if (!isBinStr(content)) {
                binTextField.setText(strBin);
                isChangedByUser = false;
                return;
            }
            int value = Integer.valueOf(content, 2);
            isChangedByUser = false;
            decTextField.setText(Integer.toString(value));
            octTextField.setText(Integer.toOctalString(value));
            hexTextField.setText(Integer.toHexString(value));
            isChangedByUser = true;
        } else if (DECIMAL.equals(type)) {
            if (!isDecimalStr(content)) {
                decTextField.setText(strDec);
                isChangedByUser = false;
                return;
            }
            int value = Integer.valueOf(content);
            isChangedByUser = false;
            binTextField.setText(Integer.toBinaryString(value));
            octTextField.setText(Integer.toOctalString(value));
            hexTextField.setText(Integer.toHexString(value));
            isChangedByUser = true;
        } else if (OCTAL.equals(type)) {
            if (!isOctStr(content)) {
                octTextField.setText(strOct);
                isChangedByUser = false;
                return;
            }
            int value = Integer.valueOf(content, 8);
            isChangedByUser = false;
            binTextField.setText(Integer.toBinaryString(value));
            decTextField.setText(Integer.toString(value));
            hexTextField.setText(Integer.toHexString(value));
            isChangedByUser = true;
        } else if (HEX.equals(type)) {
            if (!isHexString(content)) {
                hexTextField.setText(strHex);
                isChangedByUser = false;
                return;
            }
            int value = Integer.valueOf(content, 16);
            isChangedByUser = false;
            binTextField.setText(Integer.toBinaryString(value));
            octTextField.setText(Integer.toOctalString(value));
            decTextField.setText(Integer.toHexString(value));
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
