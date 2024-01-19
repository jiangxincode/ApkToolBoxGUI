package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

import javax.swing.*;
import java.awt.*;

import edu.jiangxin.apktoolbox.utils.Constants;
import org.bouncycastle.util.encoders.Hex;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class ProtobufConvertPanel extends EasyPanel {

    private JPanel contentPanel;

    private JPanel operationPanel;

    private JTextArea inputTextArea;
    private RSyntaxTextArea outputArea;

    public ProtobufConvertPanel() {
        super();
        initUI();
    }

    private void initUI() {
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
        inputTextArea.setText("0a 2f 0a 08 4a 6f 68 6e 20 44 6f 65 10 01 1a 10 6a 6f 68 6e 40 65 78 61 6d 70 6c 65 2e 63 6f 6d 22 0f 0a 0b 31 31 31 2d 32 32 32 2d 33 33 33 10 01 0a 1e 0a 08 4a 61 6e 65 20 44 6f 65 10 02 1a 10 6a 61 6e 65 40 65 78 61 6d 70 6c 65 2e 63 6f 6d");

        JScrollPane inputScrollPanel = new JScrollPane(inputTextArea);
        inputScrollPanel.setPreferredSize(new Dimension(200, 500));

        outputArea = new RSyntaxTextArea();
        outputArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        outputArea.setCodeFoldingEnabled(true);
        outputArea.setEditable(false);

        RTextScrollPane outputScrollPane = new RTextScrollPane(outputArea);
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

        byte[] byteArray = Hex.decode(hexString);

        // 解析Proto二进制数据
        //YourProtoClass protoObject = YourProtoClass.parseFrom(byteArray);

        // 将Proto对象转换为JSON格式
        //String jsonString = JsonFormat.printer().print(protoObject);
        String jsonString = ProtobufDecoder.bytesDecoder(byteArray);


        // 在输出框显示JSON字符串
        outputArea.setText(jsonString);
    }
}
