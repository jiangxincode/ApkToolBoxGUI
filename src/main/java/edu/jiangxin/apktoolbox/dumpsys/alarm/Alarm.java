package edu.jiangxin.apktoolbox.dumpsys.alarm;

import org.apache.commons.lang3.StringUtils;

public class Alarm {
    private String signature;
    private String alarmType;
    private int index;
    private String id;
    private int type;
    private String ownerPackageName;
    private AlarmBatch parentBatch;
    private long when;

    public String getOwnerPackageName() {
        return ownerPackageName;
    }

    public void setParentBatch(AlarmBatch parentBatch) {
        this.parentBatch = parentBatch;
    }

    public static Alarm fromString(String stringDefinition) {
        return new Alarm(stringDefinition);
    }

    private Alarm(String stringDefinition) {
        signature = "ALARM";
        parentBatch = null;

        index = Integer.valueOf(StringUtils.substringBetween(stringDefinition, " #", ": "));

        String alarmDefinition = StringUtils.substringBetween(stringDefinition, "Alarm{", "}" + System.getProperty("line.separator"));

        String[] tmp = alarmDefinition.split(" ");

        id = tmp[0];
        type = Integer.valueOf(tmp[2]);

        switch (type) {
            case 0:
                alarmType = "RTC_WAKEUP";
                break;
            case 1:
                alarmType = "RTC";
                break;
            case 2:
                alarmType = "ELAPSED_WAKEUP";
                break;
            case 3:
                alarmType = "ELAPSED";
                break;
        }

        if (tmp.length >= 5) {
            when = Long.valueOf(tmp[4]);
            ownerPackageName = StringUtils.substringBetween(alarmDefinition, tmp[4]+" ", " procName");
            if (ownerPackageName != null && ownerPackageName.contains("}")) {
                ownerPackageName = StringUtils.substringAfterLast(ownerPackageName, "}");
            }
        }
    }

    public String getSignature() {
        return signature;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public int getIndex() {
        return index;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public AlarmBatch getParentBatch() {
        return parentBatch;
    }

    public long getWhen() {
        return when;
    }
}
