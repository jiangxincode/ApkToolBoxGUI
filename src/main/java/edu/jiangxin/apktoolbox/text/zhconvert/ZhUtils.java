package edu.jiangxin.apktoolbox.text.zhconvert;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangxin
 * @author 2018-10-28
 *
 */
public class ZhUtils {

    public static String convert2Traditional(String in) {
        return ZhConverter.convert(in, ZhConverter.TRADITIONAL);
    }

    public static String convert2Simplified(String in) {
        return ZhConverter.convert(in, ZhConverter.SIMPLIFIED);
    }

    public static boolean isTraditional(String in) {

        ZhConverter converter = ZhConverter.getInstance(ZhConverter.SIMPLIFIED);
        Properties dict = converter.getDict();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            String key = "" + c;
            if (dict.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContainsChinese(String str) {

        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    public static boolean isAllChinese(String str) {

        boolean flag = true;
        if (str == null || str.trim().isEmpty()) {
            return false;
        }

        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!isContainsChinese(str.substring(i, i + 1))) {
                flag = false;
                break;
            }
        }

        return flag;
    }
}
