package edu.jiangxin.apktoolbox.convert.time;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeConvertPanel extends EasyPanel {

    private JPanel convert1Panel;

    private JLabel timestamp1Label;

    private JTextField timestamp1TextField;

    private JComboBox<Object> timestamp1ComboBox;

    private JButton convert1Button;

    private JLabel time1Lable;

    private JTextField time1TextField;

    private JPanel convert2Panel;

    private JLabel time2Label;

    private JTextField time2TextField;

    private JButton convert2Button;

    private JLabel timestamp2Label;

    private JTextField timestamp2TextField;

    private JComboBox<Object> timestamp2ComboBox;

    private JPanel currentPanel;

    private JLabel currentTimestampLabel;

    private JTextField currentTimestampTextField;

    private JLabel currentTimeTitleLabel;

    private JTextField currentTimeTextField;

    public TimeConvertPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createConvert1Panel();
        add(convert1Panel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createConvert2Panel();
        add(convert2Panel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createCurrentPanel();
        add(currentPanel);

        new Thread(() -> {
            while (true) {
                long currentTimeStamp = System.currentTimeMillis() / 1000;
                currentTimestampTextField.setText(String.valueOf(currentTimeStamp));
                currentTimeTextField.setText(DateTransform.timestampToString(String.valueOf(currentTimeStamp)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("InterruptedException occurred");
                }
            }

        }).start();
    }

    private void createConvert1Panel() {
        convert1Panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(convert1Panel, BoxLayout.X_AXIS);
        convert1Panel.setLayout(boxLayout);

        timestamp1Label = new JLabel("时间戳：");
        convert1Panel.add(timestamp1Label);
        convert1Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        timestamp1TextField = new JTextField(20);
        convert1Panel.add(timestamp1TextField);
        convert1Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        timestamp1ComboBox = new JComboBox<>();
        timestamp1ComboBox.addItem("秒(s)");
        timestamp1ComboBox.addItem("毫秒(ms)");
        convert1Panel.add(timestamp1ComboBox);
        convert1Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        convert1Button = new JButton();
        convert1Button.setText("时间戳转换为时间");
        convert1Button.addActionListener(new Convert1ButtonActionListener());
        convert1Panel.add(convert1Button);
        convert1Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        time1Lable = new JLabel("时间：");
        convert1Panel.add(time1Lable);
        convert1Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        time1TextField = new JTextField(20);
        convert1Panel.add(time1TextField);
    }

    private void createConvert2Panel() {
        convert2Panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(convert2Panel, BoxLayout.X_AXIS);
        convert2Panel.setLayout(boxLayout);

        time2Label = new JLabel("时间：");
        convert2Panel.add(time2Label);
        convert2Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        time2TextField = new JTextField(20);
        convert2Panel.add(time2TextField);
        convert2Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        convert2Button = new JButton();
        convert2Button.setText("时间转换为时间戳");
        convert2Button.addActionListener(new Convert2ButtonActionListener());
        convert2Panel.add(convert2Button);
        convert2Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        timestamp2Label = new JLabel("时间戳：");
        convert2Panel.add(timestamp2Label);
        convert2Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        timestamp2TextField = new JTextField(20);
        convert2Panel.add(timestamp2TextField);
        convert2Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        timestamp2ComboBox = new JComboBox<>();
        timestamp2ComboBox.addItem("秒(s)");
        timestamp2ComboBox.addItem("毫秒(ms)");
        convert2Panel.add(timestamp2ComboBox);
    }

    private void createCurrentPanel() {
        currentPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(currentPanel, BoxLayout.X_AXIS);
        currentPanel.setLayout(boxLayout);

        currentTimestampLabel = new JLabel("当前时间戳：");
        currentTimestampLabel.setFont(new Font("Serif", Font.BOLD, 22));
        currentPanel.add(currentTimestampLabel);
        currentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        currentTimestampTextField = new JTextField();
        currentTimestampTextField.setFont(new Font("Serif", Font.BOLD, 22));
        currentPanel.add(currentTimestampTextField);
        currentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        currentTimeTitleLabel = new JLabel("当前时间：");
        currentTimeTitleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        currentPanel.add(currentTimeTitleLabel);
        currentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        currentTimeTextField = new JTextField();
        currentTimeTextField.setFont(new Font("Serif", Font.BOLD, 22));
        currentPanel.add(currentTimeTextField);
    }

    private final class Convert1ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String timestamp = timestamp1TextField.getText();
            Integer index = timestamp1ComboBox.getSelectedIndex();
            String result = "";
            if (index.equals(0)) {
                result = DateTransform.timestampToString(timestamp);
            }else if (index.equals(1)) {
                result = DateTransform.milTimestampToString(timestamp);
            }
            time1TextField.setText(result);
        }
    }

    private final class Convert2ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String string = time2TextField.getText();
            Integer index = timestamp2ComboBox.getSelectedIndex();
            String result = "";
            if (index.equals(0)) {
                result = DateTransform.stringToTimestamp(string);
            }else if (index.equals(1)) {
                result = DateTransform.stringToMilTimestamp(string);
            }
            timestamp2TextField.setText(result);
        }
    }
}
