package edu.jiangxin.apktoolbox.android.dumpsys.alarm;

import edu.jiangxin.apktoolbox.file.core.EncoderDetector;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.treetable.MyAbstractTreeTableModel;
import edu.jiangxin.apktoolbox.swing.treetable.MyTreeTable;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DumpsysAlarmPanel extends EasyPanel {

    private JPanel operationPanel;

    private JPanel lastDateTimePanel;

    private JTextField lastDateTimeField;

    private JPanel systemUptime1Panel;

    private JTextField systemUptime1Field;

    private JPanel systemUptime2Panel;

    private JTextField systemUptime2Field;

    private JPanel packagePanel;

    private JTextField packageField;

    private JTabbedPane tabbedPane;

    private JPanel tabularFormTabPanel;

    private JPanel rawSourceTabPanel;

    private JEditorPane editorPane;

    private List<AlarmTreeTableDataNode> children;

    private String alarmInfoString;

    private boolean alarmInfoValid;

    public DumpsysAlarmPanel() throws HeadlessException {
        super();
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createOptionPanel();
        add(operationPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createLastDateTimePanel();
        add(lastDateTimePanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createSystemUptime1Panel();
        add(systemUptime1Panel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createSystemUptime2Panel();
        add(systemUptime2Panel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPackagePanel();
        add(packagePanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createTabbedPane();
        add(tabbedPane);
    }

    private void createOptionPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        JButton loadFromDeviceButton = new JButton("Load From Device");
        loadFromDeviceButton.addActionListener(new LoadFromDeviceButtonActionListener());

        JButton loadFromFileButton = new JButton("Load From File");
        loadFromFileButton.addActionListener(new LoadFromFileButtonActionListener());

        operationPanel.add(loadFromDeviceButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(loadFromFileButton);
        operationPanel.add(Box.createHorizontalGlue());
    }

    private void createLastDateTimePanel() {
        lastDateTimePanel = new JPanel();
        lastDateTimePanel.setLayout(new BoxLayout(lastDateTimePanel, BoxLayout.X_AXIS));

        JLabel lastDateTimeLabel = new JLabel("Last DateTime");

        lastDateTimeField = new JTextField();

        lastDateTimePanel.add(lastDateTimeLabel);
        lastDateTimePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        lastDateTimePanel.add(lastDateTimeField);
    }

    private void createSystemUptime1Panel() {
        systemUptime1Panel = new JPanel();
        systemUptime1Panel.setLayout(new BoxLayout(systemUptime1Panel, BoxLayout.X_AXIS));

        JLabel systemUptime1Label = new JLabel("System Uptime(ms)");

        systemUptime1Field = new JTextField();

        systemUptime1Panel.add(systemUptime1Label);
        systemUptime1Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        systemUptime1Panel.add(systemUptime1Field);
    }

    private void createSystemUptime2Panel() {
        systemUptime2Panel = new JPanel();
        systemUptime2Panel.setLayout(new BoxLayout(systemUptime2Panel, BoxLayout.X_AXIS));

        JLabel systemUptime2Label = new JLabel("System Uptime");

        systemUptime2Field = new JTextField();

        systemUptime2Panel.add(systemUptime2Label);
        systemUptime2Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        systemUptime2Panel.add(systemUptime2Field);
    }

    private void createPackagePanel() {
        packagePanel = new JPanel();
        packagePanel.setLayout(new BoxLayout(packagePanel, BoxLayout.X_AXIS));

        JLabel packageLabel = new JLabel("Package");

        packageField = new JTextField();

        packagePanel.add(packageLabel);
        packagePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        packagePanel.add(packageField);
    }

    private void createTabbedPane() {
        tabbedPane = new JTabbedPane();

        tabularFormTabPanel = new JPanel();
        createTable();
        tabbedPane.addTab("Tabular Form", null, tabularFormTabPanel, "Tabular Form");

        rawSourceTabPanel = new JPanel();
        createRawSource();
        tabbedPane.addTab("Raw source", null, rawSourceTabPanel, "Raw source");
    }

    private void createTable() {
        children = new ArrayList<>();
        AlarmTreeTableDataNode root = new AlarmTreeTableDataNode("Root", "", "", "", "", children);
        MyAbstractTreeTableModel treeTableModel = new AlarmTreeTableDataModel(root);
        MyTreeTable myTreeTable = new MyTreeTable(treeTableModel);
        JScrollPane scrollPane = new JScrollPane(myTreeTable);
        scrollPane.setPreferredSize(new Dimension(Constants.DEFAULT_SCROLL_PANEL_WIDTH, Constants.DEFAULT_SCROLL_PANEL_HEIGHT));
        tabularFormTabPanel.add(scrollPane);
    }

    private void createRawSource() {
        editorPane = new JEditorPane("text/plain", "");
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(Constants.DEFAULT_SCROLL_PANEL_WIDTH, Constants.DEFAULT_SCROLL_PANEL_HEIGHT));
        rawSourceTabPanel.add(scrollPane);
    }

    private void getAlarmInfoStringFromDevice() {
        final String cmd = "adb shell dumpsys alarm";
        logger.info(cmd);
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();
             ByteArrayOutputStream errStream = new ByteArrayOutputStream()
        ) {
            CommandLine commandLine = CommandLine.parse(cmd);
            DefaultExecutor exec = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
            exec.setStreamHandler(streamHandler);
            int exitValue = exec.execute(commandLine);
            logger.info("exitValue: [" + exitValue + "]");

            alarmInfoString = outStream.toString("UTF-8");
            alarmInfoValid = !StringUtils.isEmpty(alarmInfoString);
            if (!alarmInfoValid) {
                alarmInfoString = errStream.toString("UTF-8");
            }
        } catch (IOException ioe) {
            logger.error("exec fail", ioe.getMessage());
        }
    }

    private void getAlarmInfoStringFromFile() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setDialogTitle("Select a dumpsys alarm file");
        int ret = jfc.showDialog(new JLabel(), null);
        File file = jfc.getSelectedFile();
        switch (ret) {
            case JFileChooser.APPROVE_OPTION:
                try {
                    alarmInfoString = FileUtils.readFileToString(file, Charset.forName(EncoderDetector.judgeFile(file.getCanonicalPath())));
                    alarmInfoValid = true;
                } catch (IOException ex) {
                    logger.error("readFileToString failed", ex);
                    alarmInfoValid = false;
                }
                break;
            default:
                break;
        }
    }

    private void updateUIFromString() {
        alarmInfoString = StringUtils.isEmpty(alarmInfoString) ? "" : alarmInfoString;
        editorPane.setText(alarmInfoString);

        if (!alarmInfoValid) {
            children.clear();
            return;
        }

        String tmpInputString = alarmInfoString;

        String timeInformation = StringUtils.substringBetween(alarmInfoString, "nowRTC=", "mLastTimeChangeClockTime=").trim().replace(" ", "=");
        String[] components = timeInformation.split("=");

        SharedData sharedData = SharedData.getInstance();
        sharedData.setTimestamp(Long.valueOf(components[0]));
        sharedData.setDateTime(components[1] + " " + components[2]);
        sharedData.setUptimeMs(Long.valueOf(components[4]));

        long uptimeSeconds = sharedData.getUptimeMs() / 1000;
        long uptimeHours = uptimeSeconds / 3600;
        long uptimeMinutes = uptimeSeconds % 3600 / 60;
        long uptimeSecs = uptimeSeconds % 60;

        systemUptime1Field.setText(String.valueOf(sharedData.getUptimeMs()));
        systemUptime2Field.setText(uptimeHours + ":" + uptimeMinutes + ":" + uptimeSecs);
        lastDateTimeField.setText(sharedData.getDateTime());

        String pendingAlarmBatchesCountStr = StringUtils.substringBetween(tmpInputString, "Pending alarm batches: ", System.getProperty("line.separator"));
        int pendingAlarmBatches = Integer.valueOf(pendingAlarmBatchesCountStr);
        tmpInputString = StringUtils.substringAfter(tmpInputString, "Pending alarm batches: ");

        List<AlarmBatch> listBatches = new ArrayList<>();
        while (true) {
            String batchDefinition = StringUtils.substringBetween(tmpInputString, "Batch{", "}:" + System.getProperty("line.separator"));
            if (StringUtils.isEmpty(batchDefinition)) {
                break;
            }
            AlarmBatch alarmBatch = AlarmBatch.fromString(batchDefinition);
            if (alarmBatch == null) {
                break;
            }
            String alarmListDefinition = StringUtils.substringBetween(tmpInputString, "}:" + System.getProperty("line.separator"), "Batch{");
            alarmListDefinition.replace("ELAPSED_WAKEUP", "ELAPSED").replace("RTC_WAKEUP", "ELAPSED").replace("RTC", "ELAPSED");
            String[] alarmListTmp = alarmListDefinition.split("ELAPSED");
            List<String> alarmList = new ArrayList<>();
            for (int i = 0; i < alarmListTmp.length; i++) {
                if (StringUtils.isNotEmpty(alarmListTmp[i]) && StringUtils.isNotBlank(alarmListTmp[i])) {
                    alarmList.add(alarmListTmp[i]);
                }
            }

            for (int i = 0; i < alarmList.size(); ++i) {
                Alarm alarm = Alarm.fromString(alarmList.get(i));
                if (StringUtils.isNotEmpty(packageField.getText()) && StringUtils.equals(alarm.getOwnerPackageName(), packageField.getText())) {
                    continue;
                }
                alarmBatch.appendAlarm(alarm);
            }

            listBatches.add(alarmBatch);
            if (listBatches.size() == pendingAlarmBatches) {
                break;
            }

            tmpInputString = StringUtils.substringAfter(tmpInputString, "Batch{");
        }
        children.clear();
        children.addAll(alarmBatches2AlarmTreeTableDataNodeList(listBatches));
    }

    private List<AlarmTreeTableDataNode> alarmBatches2AlarmTreeTableDataNodeList(List<AlarmBatch> listBatches) {
        List<AlarmTreeTableDataNode> sonList = new ArrayList<>();
        for (AlarmBatch alarmBatch : listBatches) {
            List<AlarmTreeTableDataNode> grandsonList = new ArrayList<>();
            List<Alarm> alarmList = alarmBatch.getListAlarms();
            for (Alarm alarm : alarmList) {
                String objectId = alarm.getSignature() + " [" + alarm.getId() + "]";
                AlarmTreeTableDataNode grandson = new AlarmTreeTableDataNode(objectId, alarm.getOwnerPackageName(), alarm.getAlarmType(), String.valueOf(alarm.getWhen()),String.valueOf(alarm.getWhen()), null);
                grandsonList.add(grandson);
            }
            String objectId = alarmBatch.getSignature() + " [" + alarmBatch.getId() + "] (" + alarmBatch.getAlarmCount() + " alarms)";
            AlarmTreeTableDataNode son = new AlarmTreeTableDataNode(objectId, null, null, null, null, grandsonList);
            sonList.add(son);
        }
        return sonList;
    }

    private final class LoadFromDeviceButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getAlarmInfoStringFromDevice();
            updateUIFromString();
        }
    }

    private final class LoadFromFileButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getAlarmInfoStringFromFile();
            updateUIFromString();
        }
    }
}
