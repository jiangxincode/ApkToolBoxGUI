package edu.jiangxin.apktoolbox.time;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeStampTransformPanel extends EasyPanel {

    public TimeStampTransformPanel() throws HeadlessException {
        super();
        placeComponents();
        Utils.setJComponentSize(this, 700, 350);
    }

    private void placeComponents() {
        setLayout(null);

        // 创建JLabel
        JLabel timestampLabel = new JLabel("时间戳：");
        timestampLabel.setBounds(10, 20, 80, 25);
        add(timestampLabel);

        // 创建文本域用于用户输入
        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        add(userText);

        // 下拉选择秒/毫秒
        JComboBox<Object> cmb = new JComboBox<>();
        cmb.addItem("秒(s)");
        cmb.addItem("毫秒(ms)");
        cmb.setBounds(300, 20, 80, 25);
        add(cmb);

        // 创建转换button
        JButton firstTransformToTimestampButton = new JButton();
        firstTransformToTimestampButton.setText("转换");
        firstTransformToTimestampButton.setBounds(400, 20, 60, 25);
        add(firstTransformToTimestampButton);

        // 输出的时间文本域，不可写
        JTextField outputText = new JTextField(20);
        outputText.setBounds(500,20,165,25);
        add(outputText);

        firstTransformToTimestampButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String timestamp = userText.getText();
                Integer index = cmb.getSelectedIndex();
                String result = "";
                if (index.equals(0)) {
                    result = DateTransform.timestampToString(timestamp);
                }else if (index.equals(1)) {
                    result = DateTransform.milTimestampToString(timestamp);
                }
                outputText.setText(result);
            }
        });

        // 创建JLabel
        JLabel stringTime = new JLabel("时间：");
        stringTime.setBounds(30, 100, 80, 25);
        add(stringTime);

        // 创建文本域用于用户输入
        JTextField secondUserText = new JTextField(20);
        secondUserText.setBounds(120,100,165,25);
        add(secondUserText);

        // 创建转换button
        JButton transformToTimestampButton = new JButton();
        transformToTimestampButton.setText("转换");
        transformToTimestampButton.setBounds(300, 100, 60, 25);
        add(transformToTimestampButton);

        // 输出的时间文本域，不可写
        JTextField secondOutputText = new JTextField(20);
        secondOutputText.setBounds(400,100,165,25);
        add(secondOutputText);

        // 下拉选择秒/毫秒
        JComboBox<Object> secondCmb = new JComboBox<Object>();
        secondCmb.addItem("秒(s)");
        secondCmb.addItem("毫秒(ms)");
        secondCmb.setBounds(600, 100, 80, 25);
        add(secondCmb);

        transformToTimestampButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String string = secondUserText.getText();
                Integer index = secondCmb.getSelectedIndex();
                String result = "";
                if (index.equals(0)) {
                    result = DateTransform.stringToTimestamp(string);
                }else if (index.equals(1)) {
                    result = DateTransform.stringToMilTimestamp(string);
                }
                secondOutputText.setText(result);
            }
        });

        JLabel currentTimestampTitleJlabel = new JLabel("当前时间戳：");
        currentTimestampTitleJlabel.setBounds(30, 200, 150, 50);
        currentTimestampTitleJlabel.setFont(new Font("Serif", Font.BOLD, 22));
        add(currentTimestampTitleJlabel);

        JLabel currentTimestampContentJlabel = new JLabel();
        currentTimestampContentJlabel.setBounds(200, 200, 150, 50);
        currentTimestampContentJlabel.setFont(new Font("Serif", Font.BOLD, 22));
        add(currentTimestampContentJlabel);

        JLabel currentTimeTitleJlabel = new JLabel("当前时间：");
        currentTimeTitleJlabel.setBounds(30, 240, 150, 50);
        currentTimeTitleJlabel.setFont(new Font("Serif", Font.BOLD, 22));
        add(currentTimeTitleJlabel);

        JLabel currentTimeContentJlabel = new JLabel();
        currentTimeContentJlabel.setBounds(200, 240, 300, 50);
        currentTimeContentJlabel.setFont(new Font("Serif", Font.BOLD, 22));
        add(currentTimeContentJlabel);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long currentTimeStamp = System.currentTimeMillis() / 1000;
                    currentTimestampContentJlabel.setText(String.valueOf(currentTimeStamp));
                    currentTimeContentJlabel.setText(DateTransform.timestampToString(String.valueOf(currentTimeStamp)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}
