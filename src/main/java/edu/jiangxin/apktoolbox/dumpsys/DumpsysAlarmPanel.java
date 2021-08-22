package edu.jiangxin.apktoolbox.dumpsys;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.treetable.MyAbstractTreeTableModel;
import edu.jiangxin.apktoolbox.swing.treetable.MyTreeTable;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Stream2StringThread;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class DumpsysAlarmPanel extends EasyPanel {
    private static final int PANEL_WIDTH = Constants.DEFAULT_WIDTH - 50;

    private static final int PANEL_HIGHT = 110;

    private static final int CHILD_PANEL_HIGHT = 30;

    private static final int CHILD_PANEL_LEFT_WIDTH = 600;

    private static final int CHILD_PANEL_RIGHT_WIDTH = 130;

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

    public DumpsysAlarmPanel() throws HeadlessException {
        super();
    }

    @Override
    public void onShowEasyPanel() {
        super.onShowEasyPanel();
        setCenterWidget();
        reloadData();
        //Utils.setJComponentSize(this, 700, 350);

    }

    private void setCenterWidget() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

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

    private void createLastDateTimePanel() {
        lastDateTimePanel = new JPanel();
        Utils.setJComponentSize(lastDateTimePanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);

        lastDateTimeLabel = new JLabel("Last DateTime");
        Utils.setJComponentSize(lastDateTimeLabel, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);

        lastDateTimeField = new JTextField();
        Utils.setJComponentSize(lastDateTimeField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);

        lastDateTimePanel.setLayout(new BoxLayout(lastDateTimePanel, BoxLayout.X_AXIS));
        lastDateTimePanel.add(lastDateTimeLabel);
        lastDateTimePanel.add(Box.createHorizontalGlue());
        lastDateTimePanel.add(lastDateTimeField);
    }

    private void createSystemUptime1Panel() {
        systemUptime1Panel = new JPanel();
        Utils.setJComponentSize(systemUptime1Panel, PANEL_WIDTH, CHILD_PANEL_HIGHT);

        systemUptime1Label = new JLabel("System Uptime(ms)");
        Utils.setJComponentSize(systemUptime1Label, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);

        systemUptime1Field = new JTextField();
        Utils.setJComponentSize(systemUptime1Field, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);

        systemUptime1Panel.setLayout(new BoxLayout(systemUptime1Panel, BoxLayout.X_AXIS));
        systemUptime1Panel.add(systemUptime1Label);
        systemUptime1Panel.add(Box.createHorizontalGlue());
        systemUptime1Panel.add(systemUptime1Field);
    }

    private void createSystemUptime2Panel() {
        systemUptime2Panel = new JPanel();
        Utils.setJComponentSize(systemUptime2Panel, PANEL_WIDTH, CHILD_PANEL_HIGHT);

        systemUptime2Label = new JLabel("System Uptime");
        Utils.setJComponentSize(systemUptime2Label, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);

        systemUptime2Field = new JTextField();
        Utils.setJComponentSize(systemUptime2Field, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);

        systemUptime2Panel.setLayout(new BoxLayout(systemUptime2Panel, BoxLayout.X_AXIS));
        systemUptime2Panel.add(systemUptime2Label);
        systemUptime2Panel.add(Box.createHorizontalGlue());
        systemUptime2Panel.add(systemUptime2Field);
    }

    private void createPackagePanel() {
        packagePanel = new JPanel();
        Utils.setJComponentSize(packagePanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);

        packageLabel = new JLabel("Package");
        Utils.setJComponentSize(packageLabel, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);

        packageField = new JTextField();
        Utils.setJComponentSize(packageField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);

        packagePanel.setLayout(new BoxLayout(packagePanel, BoxLayout.X_AXIS));
        packagePanel.add(packageLabel);
        packagePanel.add(Box.createHorizontalGlue());
        packagePanel.add(packageField);
    }

    private void createTabbedPane() {
        tabbedPane = new JTabbedPane();

        tabularFormTabPanel = new JPanel();
        Utils.setJComponentSize(tabularFormTabPanel, PANEL_WIDTH, 360);
        createTable();
        tabbedPane.addTab("Tabular Form", null, tabularFormTabPanel, "Tabular Form");


        rawSourceTabPanel = new JPanel();
        Utils.setJComponentSize(rawSourceTabPanel, PANEL_WIDTH, 360);
        createRawSource();
        tabbedPane.addTab("Raw source", null, rawSourceTabPanel, "Raw source");
    }

    private void createTable() {

/*        children = new ArrayList<AlarmTreeTableDataNode>();
        root = new AlarmTreeTableDataNode("Root", "", "", "", "", children);

        MyAbstractTreeTableModel treeTableModel = new AlarmTreeTableDataModel(root);

        myTreeTable = new MyTreeTable(treeTableModel);

        JScrollPane scrollPane = new JScrollPane(myTreeTable);
        Utils.setJComponentSize(scrollPane, PANEL_WIDTH, 320);
        tabularFormTabPanel.add(scrollPane);*/
    }

    private void createRawSource() {
        editorPane = new JEditorPane("text/plain", "");
        Utils.setJComponentSize(editorPane, PANEL_WIDTH, 300);
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        rawSourceTabPanel.add(scrollPane);
    }

    private void reloadData() {
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

        String inputString = null;
        if (threadInput != null) {
            inputString = threadInput.getOutputString();
            editorPane.setText(inputString);
        }
        if (StringUtils.isEmpty(inputString)) {
            if (threadError != null) {
                String errorString = threadError.getOutputString();
                editorPane.setText(errorString);
            }
        }

        if (StringUtils.isEmpty(inputString)) {
            return;
        }

        String tmpInputString = inputString;

        String timeInformation = StringUtils.substringBetween(inputString, "nowRTC=", "mLastTimeChangeClockTime=").trim().replace(" ", "=");
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
        List<AlarmTreeTableDataNode> children = alarmBatches2AlarmTreeTableDataNodeList(listBatches);

        root = new AlarmTreeTableDataNode("Root", "", "", "", "", children);

        MyAbstractTreeTableModel treeTableModel = new AlarmTreeTableDataModel(root);

        myTreeTable = new MyTreeTable(treeTableModel);

        JScrollPane scrollPane = new JScrollPane(myTreeTable);
        Utils.setJComponentSize(scrollPane, PANEL_WIDTH, 320);
        tabularFormTabPanel.add(scrollPane);
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
}
