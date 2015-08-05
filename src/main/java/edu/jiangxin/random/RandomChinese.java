package edu.jiangxin.random;

import java.util.Random;

/**
 * 有关中文相关的随机数据生成器.
 * @author jiangxin
 *
 */
public class RandomChinese {
	
	/**
	 * 获取一个长度为2的字节数组，共同组成一个GBK编码的汉字.
	 * <p>原理是从汉字区位码找到汉字。在汉字区位码中分高位与底位， 其中既有简体又有繁体。位数越前生成的汉字繁体的机率越大。<br>
	 * 所以高位从171取，底位从161取， 去掉大部分的繁体和生僻字。
	 * @return 一个长度为2的字节数组
	 */
	private static byte[] nextCharArray() {
		int hightPos, lowPos; // 定义高低位
		Random random = new Random();
		hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
		lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
		byte[] bArray = new byte[2];
		bArray[0] = (new Integer(hightPos).byteValue());
		bArray[1] = (new Integer(lowPos).byteValue());
		return bArray; // 返回字符数组的第一个字符
	}

	/**
	 * 获取一个固定长度的随机中文字符.
	 * @return 返回一个随机中文字符
	 * @throws Exception
	 */
	public static char nextChar() throws Exception {
		
		char[] ret = new String(nextCharArray(), "GBk").toCharArray(); // 将两个字节按照GBK方式解析成字符串，然后将字符串转换成字节数组
		return ret[0]; // 返回字符数组的第一个字符
	}
	
	/**
	 * 获取一个固定长度的随机中文字符串.
	 * <p>获取一个长度为length的随机中文字符串，该函数不能保证该字符串拥有某种语义。
	 * @param length 要求生成字符串的长度（一个中文字符的长度为1）
	 * @return 一个长度为length的随机中文字符串
	 * @throws Exception
	 */
	public static String nextString(int length) throws Exception {
		StringBuilder strTemp = new StringBuilder();
		for (int i = 0; i < length; i++) {
			strTemp.append(new String(nextCharArray(), "GBK"));
		}
		return strTemp.toString();
	}
	
	/**
	 * 获取一个随机长度的随机中文字符串.
	 * 得到一个长度结余minLength和maxLength之间的随机长度字符串.
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @return 随机长度的随机中文字符串
	 * @throws Exception
	 */
	public static String nextRandomLength(int minLength, int maxLength) throws Exception {
		String str = "";
		int length = new Random().nextInt(maxLength + 1);
		if (length < minLength) {
			str = nextRandomLength(minLength, maxLength);
		} else {
			str = nextString(length);
		}
		return str;
	}


}