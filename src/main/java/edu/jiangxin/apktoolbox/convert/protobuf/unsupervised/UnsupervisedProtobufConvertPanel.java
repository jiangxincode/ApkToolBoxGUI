package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class UnsupervisedProtobufConvertPanel extends EasyPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private JPanel contentPanel;

    private JPanel operationPanel;

    private JTextArea inputTextArea;
    private RSyntaxTextArea outputTextArea;

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createContentPanel();
        add(contentPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createContentPanel() {
        contentPanel = new JPanel();

        BoxLayout boxLayout = new BoxLayout(contentPanel, BoxLayout.X_AXIS);
        contentPanel.setLayout(boxLayout);

        inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setText("0a2f0a084a6f686e20446f6510011a106a6f686e406578616d706c652e636f6d220f0a0b3131312d3232322d33333310010a1e0a084a616e6520446f6510021a106a616e65406578616d706c652e636f6d");

        JScrollPane inputScrollPanel = new JScrollPane(inputTextArea);
        inputScrollPanel.setPreferredSize(new Dimension(200, 500));

        outputTextArea = new RSyntaxTextArea();
        outputTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        outputTextArea.setCodeFoldingEnabled(true);
        outputTextArea.setEditable(false);

        RTextScrollPane outputScrollPane = new RTextScrollPane(outputTextArea);
        outputScrollPane.setPreferredSize(new Dimension(200, 500));

        contentPanel.add(inputScrollPanel);
        contentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        contentPanel.add(outputScrollPane);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();

        BoxLayout boxLayout = new BoxLayout(operationPanel, BoxLayout.X_AXIS);
        operationPanel.setLayout(boxLayout);

        JButton convertButton = new JButton("Convert");

        convertButton.addActionListener(e -> convertProtoToJson());

        operationPanel.add(convertButton);
    }

    private void convertProtoToJson() {
        String hexString = inputTextArea.getText();
        byte[] byteArray = ByteUtil.hex2bytes(hexString);
        String jsonString = ProtobufDecoder.bytesDecoder(byteArray);
        outputTextArea.setText(jsonString);
    }
}
