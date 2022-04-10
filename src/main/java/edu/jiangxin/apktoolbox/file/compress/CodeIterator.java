package edu.jiangxin.apktoolbox.file.compress;

/**
 * 密码迭代器
 *
 */
public class CodeIterator {
	private Integer num = 987000;

	public String nextCode() {
		if (num < 1000000) {
			num++;
			return num.toString();
		}
		return null;
	}

}
