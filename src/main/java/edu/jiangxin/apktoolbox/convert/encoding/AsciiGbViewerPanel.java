package edu.jiangxin.apktoolbox.convert.encoding;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.UnsupportedEncodingException;

public class AsciiGbViewerPanel extends EasyPanel {

    private JTextField inputField;
    private JTextArea hexArea;
    private JTextArea decArea;

    private static final String[] ASCII_NAMES = new String[128];

    static {
        for (int i = 0; i < 32; i++) {               // 控制字符
            ASCII_NAMES[i] = String.format("%-3s", getControlName(i));
        }
        ASCII_NAMES[32] = "SP";                        // 空格
        for (int i = 33; i < 127; i++) {               // 可见字符
            ASCII_NAMES[i] = String.valueOf((char) i);
        }
        ASCII_NAMES[127] = "DEL";                      // 删除
    }

    public AsciiGbViewerPanel() {
        super();
    }

    @Override
    public void initUI() {
        buildUI();
        refreshGb();
    }

    private static String getControlName(int c) {
        String[] n = {"NUL","SOH","STX","ETX","EOT","ENQ","ACK","BEL","BS","HT","LF","VT","FF","CR","SO","SI",
                "DLE","DC1","DC2","DC3","DC4","NAK","SYN","ETB","CAN","EM","SUB","ESC","FS","GS","RS","US"};
        return n[c];
    }

    private void buildUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createInputPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createHexPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createDecPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createAsciiTablePanel();
    }

    private void createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("输入汉字"));

        inputField = new JTextField("欢迎使用ApkToolBoxGUI", 20);
        inputField.getDocument().addDocumentListener(new MyDocumentListener());

        inputPanel.add(inputField);
        add(inputPanel);
    }

    private void createHexPanel() {
        JPanel hexPanel = new JPanel();
        hexPanel.setLayout(new BorderLayout());
        hexPanel.setBorder(BorderFactory.createTitledBorder("16进制表示（GB2312 编码）"));

        hexArea = new JTextArea(6, 18);
        hexArea.setEditable(false);
        hexArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        hexPanel.add(new JScrollPane(hexArea));
        add(hexPanel);
    }

    private void createDecPanel() {
        JPanel decPanel = new JPanel();
        decPanel.setLayout(new BorderLayout());
        decPanel.setBorder(BorderFactory.createTitledBorder("10进制表示（GB2312 编码）"));

        decArea = new JTextArea(6, 18);
        decArea.setEditable(false);
        decArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        decPanel.add(new JScrollPane(decArea));
        add(decPanel);
    }

    private void createAsciiTablePanel() {
        JPanel asciiTablePanel = new JPanel(new GridLayout(16, 8, 2, 2));
        asciiTablePanel.setBorder(new TitledBorder("标准 7-bit ASCII 表（128 字符），鼠标悬停可查看十/十六进制值"));
        asciiTablePanel.setPreferredSize(new Dimension(500, 400));

        for (int i = 0; i < 128; i++) {
            JLabel lab = new JLabel(ASCII_NAMES[i], SwingConstants.CENTER);
            lab.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            lab.setToolTipText("Dec=" + i + "  Hex=0x" + Integer.toHexString(i).toUpperCase());
            asciiTablePanel.add(lab);
        }

        add(asciiTablePanel);
    }

    class MyDocumentListener implements DocumentListener {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshGb(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshGb(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshGb(); }
    }

    private void refreshGb() {
        String txt = inputField.getText();
        StringBuilder hex = new StringBuilder();
        StringBuilder dec = new StringBuilder();
        try {
            byte[] gb = txt.getBytes("GB2312");
            for (byte value : gb) {
                int b = value & 0xff; // 无符号
                hex.append(String.format("%02X ", b));
                dec.append(b).append(' ');
            }
        } catch (UnsupportedEncodingException ex) {
            hex.append("GB2312 不支持");
            dec.append("GB2312 不支持");
        }
        hexArea.setText(hex.toString());
        decArea.setText(dec.toString());
    }
}
