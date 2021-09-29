package edu.jiangxin.apktoolbox.dumpsys;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.treetable.MyAbstractTreeTableModel;
import edu.jiangxin.apktoolbox.swing.treetable.MyTreeTable;
import edu.jiangxin.apktoolbox.text.core.EncoderDetector;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Stream2StringThread;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DumpsysAlarmPanel extends EasyPanel {
    private static final int PANEL_WIDTH = Constants.DEFAULT_WIDTH - 50;

    private static final int CHILD_PANEL_HEIGHT = 30;

    private static final int CHILD_PANEL_LEFT_WIDTH = 600;

    private static final int CHILD_PANEL_RIGHT_WIDTH = 130;

    private static final int SCROLL_PANEL_WIDTH = 700;

    private static final int SCROLL_PANEL_HEIGHT = 400;

    private JPanel operationPanel;

    private JButton refreshButton;

    private JButton loadButton;

    private JPanel lastDateTimePanel;

    private JLabel lastDateTimeLabel;

    private JTextField lastDateTimeField;

    private JPanel systemUptime1Panel;

    private JLabel systemUptime1Label;

    private JTextField systemUptime1Field;

    private JPanel systemUptime2Panel;

    private JLabel systemUptime2Label;

    private JTextField systemUptime2Field;

    private JPanel packagePanel;

    private JLabel packageLabel;

    private JTextField packageField;

    private JTabbedPane tabbedPane;

    private JPanel tabularFormTabPanel;

    private JPanel rawSourceTabPanel;

    private JEditorPane editorPane;

    private MyTreeTable myTreeTable;

    private AlarmTreeTableDataNode root;

    private List<AlarmTreeTableDataNode> children;

    private String alarmInfoString;

    private boolean alarmInfoValid;

    public DumpsysAlarmPanel() throws HeadlessException {
        super();
        setCenterWidget();
        getAlarmInfoStringFromDevice();
        updateUIFromString();
    }

    private void setCenterWidget() {
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
        Utils.setJComponentSize(operationPanel, PANEL_WIDTH, CHILD_PANEL_HEIGHT);
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        refreshButton = new JButton("Refresh");
        Utils.setJComponentSize(refreshButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HEIGHT);
        refreshButton.addMouseListener(new RefreshButtonMouseAdapter());

        loadButton = new JButton("Load");
        Utils.setJComponentSize(loadButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HEIGHT);
        loadButton.addMouseListener(new LoadButtonMouseAdapter());

        operationPanel.add(refreshButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(loadButton);
    }

    private void createLastDateTimePanel() {
        lastDateTimePanel = new JPanel();
        Utils.setJComponentSize(lastDateTimePanel, PANEL_WIDTH, CHILD_PANEL_HEIGHT);

        lastDateTimeLabel = new JLabel("Last DateTime");
        Utils.setJComponentSize(lastDateTimeLabel, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HEIGHT);

        lastDateTimeField = new JTextField();
        Utils.setJComponentSize(lastDateTimeField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HEIGHT);

        lastDateTimePanel.setLayout(new BoxLayout(lastDateTimePanel, BoxLayout.X_AXIS));
        lastDateTimePanel.add(lastDateTimeLabel);
        lastDateTimePanel.add(Box.createHorizontalGlue());
        lastDateTimePanel.add(lastDateTimeField);
    }

    private void createSystemUptime1Panel() {
        systemUptime1Panel = new JPanel();
        Utils.setJComponentSize(systemUptime1Panel, PANEL_WIDTH, CHILD_PANEL_HEIGHT);

        systemUptime1Label = new JLabel("System Uptime(ms)");
        Utils.setJComponentSize(systemUptime1Label, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HEIGHT);

        systemUptime1Field = new JTextField();
        Utils.setJComponentSize(systemUptime1Field, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HEIGHT);

        systemUptime1Panel.setLayout(new BoxLayout(systemUptime1Panel, BoxLayout.X_AXIS));
        systemUptime1Panel.add(systemUptime1Label);
        systemUptime1Panel.add(Box.createHorizontalGlue());
        systemUptime1Panel.add(systemUptime1Field);
    }

    private void createSystemUptime2Panel() {
        systemUptime2Panel = new JPanel();
        Utils.setJComponentSize(systemUptime2Panel, PANEL_WIDTH, CHILD_PANEL_HEIGHT);

        systemUptime2Label = new JLabel("System Uptime");
        Utils.setJComponentSize(systemUptime2Label, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HEIGHT);

        systemUptime2Field = new JTextField();
        Utils.setJComponentSize(systemUptime2Field, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HEIGHT);

        systemUptime2Panel.setLayout(new BoxLayout(systemUptime2Panel, BoxLayout.X_AXIS));
        systemUptime2Panel.add(systemUptime2Label);
        systemUptime2Panel.add(Box.createHorizontalGlue());
        systemUptime2Panel.add(systemUptime2Field);
    }

    private void createPackagePanel() {
        packagePanel = new JPanel();
        Utils.setJComponentSize(packagePanel, PANEL_WIDTH, CHILD_PANEL_HEIGHT);

        packageLabel = new JLabel("Package");
        Utils.setJComponentSize(packageLabel, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HEIGHT);

        packageField = new JTextField();
        Utils.setJComponentSize(packageField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HEIGHT);

        packagePanel.setLayout(new BoxLayout(packagePanel, BoxLayout.X_AXIS));
        packagePanel.add(packageLabel);
        packagePanel.add(Box.createHorizontalGlue());
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
        root = new AlarmTreeTableDataNode("Root", "", "", "", "", children);
        MyAbstractTreeTableModel treeTableModel = new AlarmTreeTableDataModel(root);
        myTreeTable = new MyTreeTable(treeTableModel);
        JScrollPane scrollPane = new JScrollPane(myTreeTable);
        scrollPane.setPreferredSize(new Dimension(SCROLL_PANEL_WIDTH, SCROLL_PANEL_HEIGHT));
        tabularFormTabPanel.add(scrollPane);
    }

    private void createRawSource() {
        editorPane = new JEditorPane("text/plain", "");
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(SCROLL_PANEL_WIDTH, SCROLL_PANEL_HEIGHT));
        rawSourceTabPanel.add(scrollPane);
    }

    private void getAlarmInfoStringFromDevice() {
        Process process1 = null;
        Stream2StringThread threadInput = null;
        Stream2StringThread threadError = null;
        try {
            process1 = Runtime.getRuntime()
                    .exec("adb shell dumpsys alarm");
            threadInput = new Stream2StringThread(process1.getInputStream());
            threadError = new Stream2StringThread(process1.getErrorStream());
            threadInput.start();
            threadError.start();
            process1.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (threadInput != null) {
            alarmInfoString = threadInput.getOutputString();
            alarmInfoValid = !StringUtils.isEmpty(alarmInfoString);
            if (alarmInfoValid && threadError != null) {
                alarmInfoString = threadError.getOutputString();
            }
        }
    }

    private void getAlarmInfoStringFromFile(File file) {
        try {
            alarmInfoString = FileUtils.readFileToString(file, Charset.forName(EncoderDetector.judgeFile(file.getCanonicalPath())));
            alarmInfoValid = true;
        } catch (IOException ex) {
            logger.error("readFileToString failed", ex);
            alarmInfoValid = false;
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
        long uptimeMinutes = (uptimeSeconds % 3600) / 60;
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

    private final class RefreshButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            getAlarmInfoStringFromDevice();
            updateUIFromString();
        }
    }

    private final class LoadButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setDialogTitle("select a alarm dumpsys file");
            int ret = jfc.showDialog(new JLabel(), null);
            switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    getAlarmInfoStringFromFile(jfc.getSelectedFile());
                    break;
                default:
                    break;
            }
            updateUIFromString();
        }
    }
}
