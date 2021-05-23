package edu.jiangxin.apktoolbox.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransform {
    /**
     * 秒级时间戳转换为字符串格式
     * @param timestamp 时间戳：1588766006
     * @return 2020-05-06 19:53:26
     */
    public static String timestampToString(String timestamp) {
        if (timestamp == null || timestamp.equals("null") || timestamp.isEmpty()) {
            return "";
        }

        String format = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try{
            return sdf.format(new Date(new Long(timestamp + "000")));
        }catch (Exception e) {
            return "";
        }
    }

    /**
     * 毫秒级时间戳转换成字符串格式
     * @param timestamp 毫秒级时间戳（13位）
     * @return 时间字符串
     */
    public static String milTimestampToString(String timestamp) {
        if (timestamp == null || timestamp.equals("null") || timestamp.isEmpty()) {
            return "";
        }

        String format = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.format(new Date(new Long(timestamp)));
        }catch (Exception e) {
            return "";
        }

    }

    /**
     * 日期的字符串格式转换成时间戳
     * @param string 2020-05-06 19:53:26
     * @return 秒级时间戳
     */
    public static String stringToTimestamp(String string) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.valueOf(sdf.parse(string).getTime() / 1000);
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 转换成毫秒级时间戳
     * @param string 日期的字符串表示
     * @return 毫秒级时间戳
     */
    public static String stringToMilTimestamp(String string) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.valueOf(sdf.parse(string).getTime());
        } catch (ParseException e) {
            return "";
        }
    }
}
