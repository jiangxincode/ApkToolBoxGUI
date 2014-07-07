package edu.jiangxin.random;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 有关日期时间的随机数据生成器.
 * @author jiangxin
 *
 */
public class RandomDateTime {

	/**
	 * 获取随机日期+时间.
	 * <p>生成一个在beginDate和endDate之间的随机日期+时间。<br>
	 * @param beginDate 起始日期，格式为：yyyy-MM-dd
	 * @param endDate 结束日期，格式为：yyyy-MM-dd
	 * @return 一个随机日期+时间
	 */
	public static Date nextDate(String beginDate, String endDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date start = format.parse(beginDate);// 构造开始日期
			Date end = format.parse(endDate);// 构造结束日期
			if (start.getTime() >= end.getTime()) {
				return null;
			}
			long date = start.getTime() + (long)(Math.random() * (end.getTime() - start.getTime()));
			return new Date(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}