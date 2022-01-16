package edu.jiangxin.apktoolbox.convert.time;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Calendar.*;

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

    private JButton pauseButton;

    private boolean isPaused;

    private JPanel commentPanel;

    private JPanel timezonePanel;

    private JFormattedTextField inTimeTextField;
    private JComboBox inTimezoneComboBox;
    private JComboBox outTimezoneComboBox;
    private JTextField outTimeTextField;

    private static final String SDF_PATTERN = "yyyy-MM-dd HH:mm";
    private final SimpleDateFormat SDF = new SimpleDateFormat(SDF_PATTERN);
    private final DateFormatter dtFormatter = new DateFormatter(SDF);

    final static String[] sortedTimeZones;
    static {
        String[] t = TimeZone.getAvailableIDs();
        sortedTimeZones = Arrays.copyOf(t, t.length);
        Arrays.sort(sortedTimeZones, String.CASE_INSENSITIVE_ORDER);
    }

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
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createCommentPanel();
        add(commentPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createTimezonePanel();
        add(timezonePanel);
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
        currentPanel.add(currentTimestampLabel);
        currentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        currentTimestampTextField = new JTextField();
        currentPanel.add(currentTimestampTextField);
        currentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        currentTimeTitleLabel = new JLabel("当前时间：");
        currentPanel.add(currentTimeTitleLabel);
        currentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        currentTimeTextField = new JTextField();
        currentPanel.add(currentTimeTextField);
        currentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        pauseButton = new JButton("Pause");
        currentPanel.add(pauseButton);
        pauseButton.addActionListener(e -> {
            if (isPaused) {
                pauseButton.setText("Pause");
                isPaused = false;
            } else {
                pauseButton.setText("Resume");
                isPaused = true;
            }
        });

        new Thread(() -> {
            while (true) {
                long currentTimeStamp = System.currentTimeMillis() / 1000;
                if (!isPaused) {
                    currentTimestampTextField.setText(String.valueOf(currentTimeStamp));
                    currentTimeTextField.setText(DateTransform.timestampToString(String.valueOf(currentTimeStamp)));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("InterruptedException occurred");
                }
            }
        }).start();
    }

    private void createCommentPanel() {
        commentPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(commentPanel, BoxLayout.X_AXIS);
        commentPanel.setLayout(boxLayout);

        JTextArea textArea1 = new JTextArea("1s = 1000ms");
        textArea1.setEditable(false);
        JTextArea textArea2 = new JTextArea("1ms = 1000μs");
        textArea2.setEditable(false);
        JTextArea textArea3 = new JTextArea("1μs = 1000ns");
        textArea3.setEditable(false);
        JTextArea textArea4 = new JTextArea("1ns = 1000ps");
        textArea4.setEditable(false);

        commentPanel.add(textArea1);
        commentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        commentPanel.add(textArea2);
        commentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        commentPanel.add(textArea3);
        commentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        commentPanel.add(textArea4);
        commentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
    }

    private void createTimezonePanel() {
        timezonePanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(timezonePanel, BoxLayout.X_AXIS);
        timezonePanel.setLayout(boxLayout);

        JLabel label1 = new JLabel("From Time: ");
        label1.setLabelFor(inTimeTextField);
        timezonePanel.add(label1);

        inTimeTextField = new JFormattedTextField(dtFormatter);
        inTimeTextField.setToolTipText("Enter time (" + SDF_PATTERN + "): ");
        inTimeTextField.setValue(new Date());
        inTimeTextField.addPropertyChangeListener("value", event -> update());

        JLabel label2 = new JLabel("From Timezone: ");
        label2.setLabelFor(inTimezoneComboBox);
        inTimezoneComboBox = new JComboBox(sortedTimeZones);
        inTimezoneComboBox.setPreferredSize(new Dimension(70, 0));
        inTimezoneComboBox.addActionListener(e -> update());

        JLabel label3 = new JLabel("To Timezone: ");
        label3.setLabelFor(outTimezoneComboBox);
        outTimezoneComboBox = new JComboBox(sortedTimeZones);
        outTimezoneComboBox.setPreferredSize(new Dimension(70, 0));
        outTimezoneComboBox.addActionListener(e -> update());

        JLabel label4 = new JLabel("To Time: ");
        label4.setLabelFor(outTimeTextField);
        outTimeTextField = new JTextField(10);

        JButton swapButton = new JButton("Swap");
        swapButton.addActionListener(e -> {
            final String src = (String) inTimezoneComboBox.getSelectedItem();
            inTimezoneComboBox.setSelectedItem(outTimezoneComboBox.getSelectedItem());
            outTimezoneComboBox.setSelectedItem(src);
        });

        timezonePanel.add(label1);
        timezonePanel.add(inTimeTextField);
        timezonePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        timezonePanel.add(label2);
        timezonePanel.add(inTimezoneComboBox);
        timezonePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        timezonePanel.add(label3);
        timezonePanel.add(outTimezoneComboBox);
        timezonePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        timezonePanel.add(label4);
        timezonePanel.add(outTimeTextField);
        timezonePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        timezonePanel.add(swapButton);
    }

    private final class Convert1ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String timestamp = timestamp1TextField.getText();
            Integer index = timestamp1ComboBox.getSelectedIndex();
            String result = "";
            if (index.equals(0)) {
                result = DateTransform.timestampToString(timestamp);
            } else if (index.equals(1)) {
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
            } else if (index.equals(1)) {
                result = DateTransform.stringToMilTimestamp(string);
            }
            timestamp2TextField.setText(result);
        }
    }

    private static Date getDate(final int hour, final int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(HOUR_OF_DAY, hour);
        cal.set(MINUTE, minute);
        return cal.getTime();
    }


    private void update() {
        // Get the timezone objects:
        final TimeZone sourceTimezone = TimeZone.getTimeZone(
                (String) inTimezoneComboBox.getSelectedItem());
        final TimeZone destTimezone = TimeZone.getTimeZone(
                (String) outTimezoneComboBox.getSelectedItem());

        // Get the entered date:
        Date sourceDate = (Date) inTimeTextField.getValue();

        // Compute the source:
        Calendar localTime = Calendar.getInstance();
        localTime.setTime(sourceDate);
        Calendar sourceTime = Calendar.getInstance(sourceTimezone);
        sourceTime.set(localTime.get(YEAR),
                localTime.get(MONTH),
                localTime.get(DATE),
                localTime.get(HOUR_OF_DAY),
                localTime.get(MINUTE));

        // Destination:
        SimpleDateFormat sdf = (SimpleDateFormat) SDF.clone();
        sdf.setTimeZone(destTimezone);
        outTimeTextField.setText(sdf.format(sourceTime.getTime()));
    }
}
