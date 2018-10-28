package edu.jiangxin.apktoolbox.text.zhconvert;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZHUtils {

	public static String convert2Traditional(String in) {
		return ZHConverter.convert(in, ZHConverter.TRADITIONAL);
	}

	public static String convert2Simplified(String in) {
		return ZHConverter.convert(in, ZHConverter.SIMPLIFIED);
	}

	public static boolean isTraditional(String in) {

		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
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
