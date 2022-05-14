package edu.jiangxin.apktoolbox.convert.time;

import edu.jiangxin.apktoolbox.main.MainFrame;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransform {
    private static final Logger LOGGER = LogManager.getLogger(MainFrame.class);

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * Convert timestamp in second to a date string
     */
    public static String secondToDate(String second) {
        if (StringUtils.isEmpty(second)) {
            LOGGER.warn("timestamp is empty");
            return "";
        }

        long timestampValue;
        try {
            timestampValue = Long.parseLong(second + "000");
        } catch (NumberFormatException e) {
            LOGGER.warn("Can not convert timestamp to a long-type value");
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        try {
            return sdf.format(new Date(timestampValue));
        } catch (Exception e) {
            LOGGER.warn("Can not convert timestamp to a date");
            return "";
        }
    }

    /**
     * Convert timestamp in millisecond to a date string
     */
    public static String milliSecondToDate(String millisecond) {
        if (StringUtils.isEmpty(millisecond)) {
            LOGGER.warn("timestamp is empty");
            return "";
        }

        long timestampValue;
        try {
            timestampValue = Long.parseLong(millisecond);
        } catch (NumberFormatException e) {
            LOGGER.warn("Can not convert timestamp to a long-type value");
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        try {
            return sdf.format(new Date(timestampValue));
        } catch (Exception e) {
            LOGGER.warn("Can not convert timestamp to a date");
            return "";
        }
    }

    /**
     * Convert date string to a timestamp in second
     */
    public static String dateToSecond(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
            Date date = sdf.parse(dateString);
            return String.valueOf(date.getTime() / 1000);
        } catch (ParseException e) {
            LOGGER.warn("Can not parse this date string");
            return "";
        }
    }

    /**
     * Convert date string to a timestamp in millisecond
     */
    public static String dateToMilliSecond(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
            Date date = sdf.parse(dateString);
            return String.valueOf(date.getTime());
        } catch (ParseException e) {
            LOGGER.warn("Can not parse this date string");
            return "";
        }
    }
}
