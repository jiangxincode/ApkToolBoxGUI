package edu.jiangxin.apktoolbox.android.dumpsys.alarm;

import java.util.Collections;
import java.util.List;

public class AlarmTreeTableDataNode {
    private String objectId;
    private String appPackage;
    private String alarmType;
    private String when;
    private String fireDateTime;

    private List<AlarmTreeTableDataNode> children;

    public AlarmTreeTableDataNode(String objectId, String appPackage, String alarmType, String when, String fireDateTime, List<AlarmTreeTableDataNode> children) {
        this.objectId = objectId;
        this.appPackage = appPackage;
        this.alarmType = alarmType;
        this.when = when;
        this.fireDateTime = fireDateTime;
        this.children = children;

        if (this.children == null) {
            this.children = Collections.emptyList();
        }
    }

    public String getObjectId() {
        return objectId;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public String getWhen() {
        return when;
    }

    public String getFireDateTime() {
        return fireDateTime;
    }

    public List<AlarmTreeTableDataNode> getChildren() {
        return children;
    }

    public String toString() {
        return objectId;
    }
}
