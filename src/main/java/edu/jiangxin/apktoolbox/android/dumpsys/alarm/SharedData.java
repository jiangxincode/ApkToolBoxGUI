package edu.jiangxin.apktoolbox.android.dumpsys.alarm;

public class SharedData {
    private static SharedData instance = null;

    private long mTimestamp;
    private long mUptimeMs;
    private String mDateTime;

    private SharedData() {
    }

    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
    }

    public long getUptimeMs() {
        return mUptimeMs;
    }

    public void setUptimeMs(long uptimeMs) {
        this.mUptimeMs = uptimeMs;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        this.mDateTime = dateTime;
    }
}
