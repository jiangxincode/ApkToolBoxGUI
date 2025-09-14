package edu.jiangxin.apktoolbox.convert.time;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.DateUtils;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeConvertPanel extends EasyPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private JSpinner timestamp2Spinner;

    private JComboBox<Object> timestampComboBox;

    private JTextField timeTextField;

    private JTextField currentTimestampTextField;

    private JTextField currentTimeTextField;

    private JButton pauseButton;

    private boolean isPaused;

    private JFormattedTextField inTimeTextField;
    private JComboBox<String> inTimezoneComboBox;
    private JComboBox<String> outTimezoneComboBox;
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
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createConvertPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createConvertButtonsPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER * 3));

        createCurrentPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER * 3));

        createCommentPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER * 3));

        createTimezonePanel();
    }

    private void createConvertPanel() {
        JPanel convertPanel = new JPanel();
        convertPanel.setLayout(new BoxLayout(convertPanel, BoxLayout.X_AXIS));
        add(convertPanel);

        JPanel timestampPanel = new JPanel();
        timestampPanel.setLayout(new BoxLayout(timestampPanel, BoxLayout.X_AXIS));

        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));

        convertPanel.add(timestampPanel);
        convertPanel.add(Box.createHorizontalStrut(40));
        convertPanel.add(timePanel);

        JLabel timestampLabel = new JLabel("TimeStamp: ");

        timestamp2Spinner = new JSpinner();
        timestamp2Spinner.setPreferredSize(new Dimension(150, 25));
        timestamp2Spinner.setModel(new SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(Long.MAX_VALUE), Long.valueOf(1L)));

        timestampComboBox = new JComboBox<>();
        timestampComboBox.addItem("Second(s)");
        timestampComboBox.addItem("Millisecond(ms)");

        timestampPanel.add(timestampLabel);
        timestampPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        timestampPanel.add(timestamp2Spinner);
        timestampPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        timestampPanel.add(timestampComboBox);

        JLabel timeLabel = new JLabel("Time: ");

        timeTextField = new JTextField(20);

        timePanel.add(timeLabel);
        timePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        timePanel.add(timeTextField);
    }

    private void createConvertButtonsPanel() {
        JPanel convertButtonsPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(convertButtonsPanel, BoxLayout.X_AXIS);
        convertButtonsPanel.setLayout(boxLayout);
        add(convertButtonsPanel);

        JButton timestamp2TimeButton = new JButton();
        timestamp2TimeButton.setText("Timestamp->Time");
        timestamp2TimeButton.addActionListener(new Timestamp2TimeButtonActionListener());
        convertButtonsPanel.add(timestamp2TimeButton);

        convertButtonsPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        JButton time2TimestampButton = new JButton();
        time2TimestampButton.setText("Time->Timestamp");
        time2TimestampButton.addActionListener(new Time2TimestampButtonActionListener());
        convertButtonsPanel.add(time2TimestampButton);

        convertButtonsPanel.add(Box.createHorizontalGlue());
    }

    private void createCurrentPanel() {
        JPanel currentPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(currentPanel, BoxLayout.X_AXIS);
        currentPanel.setLayout(boxLayout);
        add(currentPanel);

        JLabel currentTimestampLabel = new JLabel("Current timestamp: ");
        currentPanel.add(currentTimestampLabel);
        currentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        currentTimestampTextField = new JTextField();
        currentPanel.add(currentTimestampTextField);
        currentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        JLabel currentTimeTitleLabel = new JLabel("Current time: ");
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
                    currentTimeTextField.setText(DateUtils.secondToHumanFormat(currentTimeStamp));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("sleep InterruptedException");
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private void createCommentPanel() {
        JPanel commentPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(commentPanel, BoxLayout.X_AXIS);
        commentPanel.setLayout(boxLayout);
        add(commentPanel);

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
        JPanel timezonePanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(timezonePanel, BoxLayout.X_AXIS);
        timezonePanel.setLayout(boxLayout);
        add(timezonePanel);

        JLabel label1 = new JLabel("From Time: ");
        label1.setLabelFor(inTimeTextField);
        timezonePanel.add(label1);

        inTimeTextField = new JFormattedTextField(dtFormatter);
        inTimeTextField.setToolTipText("Enter time (" + SDF_PATTERN + "): ");
        inTimeTextField.setValue(new Date());
        inTimeTextField.addPropertyChangeListener("value", event -> update());

        JLabel label2 = new JLabel("From Timezone: ");
        label2.setLabelFor(inTimezoneComboBox);
        inTimezoneComboBox = new JComboBox<>(sortedTimeZones);
        inTimezoneComboBox.setPreferredSize(new Dimension(70, 0));
        inTimezoneComboBox.addActionListener(e -> update());

        JLabel label3 = new JLabel("To Timezone: ");
        label3.setLabelFor(outTimezoneComboBox);
        outTimezoneComboBox = new JComboBox<>(sortedTimeZones);
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

    private final class Timestamp2TimeButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Long timestamp = (Long) timestamp2Spinner.getValue();
            Integer index = timestampComboBox.getSelectedIndex();
            String result = "";
            if (index.equals(0)) {
                result = DateUtils.secondToHumanFormat(timestamp);
            } else if (index.equals(1)) {
                result = DateUtils.millisecondToHumanFormat(timestamp);
            }
            timeTextField.setText(result);
        }
    }

    private final class Time2TimestampButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String string = timeTextField.getText();
            Integer index = timestampComboBox.getSelectedIndex();
            Long result = 0L;
            if (index.equals(0)) {
                result = DateUtils.humanFormatToSecond(string);
            } else if (index.equals(1)) {
                result = DateUtils.humanFormatToMillisecond(string);
            }
            timestamp2Spinner.setValue(result);
        }
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
        sourceTime.set(localTime.get(Calendar.YEAR),
                localTime.get(Calendar.MONTH),
                localTime.get(Calendar.DATE),
                localTime.get(Calendar.HOUR_OF_DAY),
                localTime.get(Calendar.MINUTE));

        // Destination:
        SimpleDateFormat sdf = (SimpleDateFormat) SDF.clone();
        sdf.setTimeZone(destTimezone);
        outTimeTextField.setText(sdf.format(sourceTime.getTime()));
    }
}
