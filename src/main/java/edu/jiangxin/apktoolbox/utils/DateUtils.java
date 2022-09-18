package edu.jiangxin.apktoolbox.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final Logger LOGGER = LogManager.getLogger(DateUtils.class.getSimpleName());

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * Convert timestamp in second to a date string
     */
    public static String secondToHumanFormat(long second) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        try {
            return sdf.format(new Date(second * 1000));
        } catch (Exception e) {
            LOGGER.warn("Can not convert timestamp to a date");
            return "";
        }
    }

    public static String millisecondToHumanFormat(long millisecond) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        try {
            return sdf.format(new Date(millisecond));
        } catch (Exception e) {
            LOGGER.warn("Can not convert timestamp to a date");
            return "";
        }
    }

    /**
     * Convert date string to a timestamp in second
     */
    public static Long humanFormatToSecond(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
            Date date = sdf.parse(dateString);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            LOGGER.warn("Can not parse this date string");
            return 0L;
        }
    }

    /**
     * Convert date string to a timestamp in millisecond
     */
    public static Long humanFormatToMillisecond(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            LOGGER.warn("Can not parse this date string");
            return 0L;
        }
    }

    public static String getCurrentDateString() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            LOGGER.error("getCurrentDateString error", e);
        }
        return null;
    }
}
