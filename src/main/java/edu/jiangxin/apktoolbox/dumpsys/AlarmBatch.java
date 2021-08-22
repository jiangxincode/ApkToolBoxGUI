package edu.jiangxin.apktoolbox.dumpsys;

import java.util.ArrayList;
import java.util.List;

public class AlarmBatch {
    private String signature;
    private String id;
    private int alarmCount;
    private long start;
    private long end;
    String flags;

    private List<Alarm> listAlarms = new ArrayList<>();

    public void appendAlarm(Alarm alarm) {
        alarm.setParentBatch(this);
        listAlarms.add(alarm);
    }

    public static AlarmBatch fromString(String batchDefinition) {
        String[] components = batchDefinition.split(" ");
        if (components.length < 4) {
            return null;
        }
        return new AlarmBatch(components);
    }

    private AlarmBatch(String[] components) {
        signature = "BATCH";
        id = components[0];

        if (components.length >= 5) {
            flags = components[4];
        }

        setParameter(components[1]);
        setParameter(components[2]);
        setParameter(components[3]);
    }

    private boolean setParameter(String expression) {
        String[] components = expression.split("=");
        if (components.length != 2) {
            return false;
        }

        if ("num".equals(components[0])) {
            alarmCount = Integer.valueOf(components[1]);
            return true;
        }

        // https://stackoverflow.com/questions/28742884/how-to-read-adb-shell-dumpsys-alarm-output
        // the start and end numbers represent the number of milliseconds that have elapsed since the system was last rebooted as described in this post, (https://stackoverflow.com/a/15400014/296725)
        // and also roughly represent the window of time in which the alarms in the batch should be triggered.
        if ("start".equals(components[0])) {
            start = Long.valueOf(components[1]);
            return true;
        }

        if ("end".equals(components[0])) {
            end = Long.valueOf(components[1]);
            return true;
        }

        return false;
    }

    public String getSignature() {
        return signature;
    }

    public String getId() {
        return id;
    }

    public int getAlarmCount() {
        return alarmCount;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public String getFlags() {
        return flags;
    }

    public List<Alarm> getListAlarms() {
        return listAlarms;
    }
}
