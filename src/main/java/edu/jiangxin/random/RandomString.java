package edu.jiangxin.random;

import java.util.Random;

/**
 * 随机数据生成器.
 * 主要用于测试程序、生成密码、设计抽奖程序等
 * @author jiangxin
 * 
 */
public class RandomString {
	
	/**
	 * 随机生成字符串.
	 * <p>生成一个长度为length的随机字符串，字符串中所含字符的类型由pattern和strTable共同决定。<br>
	 * <p>pattern主要包括以下几种：<br>
	 * digit:生成仅由数字组成的字符串<br>
	 * letter:生成仅由字母组成的字符串<br>
	 * upper_case:生成仅由大写字母组成的字符串<br>
	 * lower_case:生成仅由小写字母组成的字符串<br>
	 * digit_letter:生成由数字字母组成的字符串<br>
	 * ASCII:生成由ASCII字符组成的字符串，不允许使用strTable参数<br>
	 * extend_ascii:生成由扩展ASCII字符组成的字符串，不允许使用strTable参数<br>
	 * printable_ascii:生成由可打印ASCII字符组成的字符串，不允许使用strTable参数<br>
	 * chinese:生成由中文字符组成的字符串，不允许使用strTable参数<br>
	 * <p>strTable为null时，仅使用patter生成的字符表，strTable不为null时，字符表为pattern生成的字符表和strTable合并之后的结果。<br>
	 * @param length 生成字符串的长度.
	 * @param pattern 字符组成表的类型.
	 * @param strTable 附加字符组成表.
	 * @return 返回生成的随机字符串，如果参数错误，则返回null。
	 * @throws Exception
	 */
	public static String nextString(int length, String pattern,String strTable) throws Exception {
		
		pattern = pattern.toLowerCase();
		String strTemp = strTable;
		boolean isASCII = false; //原始ASCII
		boolean isExtendedASCII = false; //扩展ASCII
		boolean isPrintableASCII = false; //可打印ASCII
		boolean isChinese = false; //中文
		
		StringBuilder retStr = new StringBuilder(); // 生成的随机字符串
		if (pattern.equals("digit")) {
			strTable = "1234567890"; // 字符串仅由数字构成
		} else if (pattern.equals("letter")) {
			strTable = "abcdefghijkmnpqrstuvwxyz"
					+ "ABCDEFGHIGKLMNOPQRSTUVWXYZ"; // 字符串仅由字母构成
		} else if (pattern.equals("uppercase")) {
			strTable = "ABCDEFGHIGKLMNOPQRSTUVWXYZ"; // 字符串仅由大写字母构成
		} else if (pattern.equals("lowercase")) { // 字符串仅由小写字母构成
			strTable = "abcdefghijkmnpqrstuvwxyz";
		} else if (pattern.equals("digit_letter")) { // 字符串仅由小写构成
			strTable = "abcdefghijkmnpqrstuvwxyz"
					+ "ABCDEFGHIGKLMNOPQRSTUVWXYZ" 
					+ "1234567890"; // 字符串由数字或字母构成
		} else if (pattern.equals("ascii") && strTable == null) { // 字符串由ASCII字符构成
			isASCII = true;
		} else if (pattern.equals("extend_ascii") && strTable == null) { // 字符串由扩展ASCII字符构成
			isExtendedASCII = true;
		} else if (pattern.equals("printable_ascii") && strTable == null) { // 字符串由可打印ASCII字符构成
			isPrintableASCII = true;
		} else if (pattern.equals("chinese") && strTable == null) { // 字符串由可打印ASCII字符构成
			isChinese = true;
		} else {
			System.err.println("参数错误");
			return null;
		}
		if(strTemp!=null) { //加入附加字符表
			strTable = strTable + strTemp;
		}
		
		if (strTable != null) { //若有字符表，则按字符表输出
			for (int i = 0; i < length; i++) {
				double dbl_Random = Math.random() * strTable.length();
				int i_Random = (int) Math.floor(dbl_Random);
				retStr.append(strTable.charAt(i_Random)) ;
			}
		} else if(isASCII) {
			for(int i=0;i<length;i++) {
				int i_Random = 0;
				double dbl_Random = Math.random() * 127;
				i_Random = (int) Math.floor(dbl_Random);
				retStr.append((char)i_Random);
			}
			
		} else if(isExtendedASCII) {
			for(int i=0;i<length;i++) {
				int i_Random = 0;
				double dbl_Random = Math.random() * 255;
				i_Random = (int) Math.floor(dbl_Random);
				retStr.append((char)i_Random);
			}
		} else if(isPrintableASCII) {
			for(int i=0;i<length;i++) {
				int i_Random = 0;
				while(i_Random<32) {
				double dbl_Random = Math.random() * 127;
				i_Random = (int) Math.floor(dbl_Random);
				}
				retStr.append((char)i_Random);
			}
		} else if(isChinese) {
			retStr.append(RandomChinese.nextString(length));
		} else {
			System.err.println("参数错误");
			return null;
		}
		
		String temp = retStr.toString();
		return (temp.isEmpty()) ? null : temp;
	}
	/**
	 * 生成随机字符串.
	 * <p>生成一个长度为length的随机字符串，字符串中所含字符的类型由pattern决定。<br>
	 * @see #nextString(int length, String pattern,String strTable)
	 * @param length 生成字符串的长度.
	 * @param pattern 字符组成表的类型.
	 * @return 返回生成的随机字符串，如果参数错误，则返回null。
	 * @throws Exception 
	 */
	public static String nextString(int length, String pattern) throws Exception {
		return nextString(length, pattern,null);
	}
	
	/**
	 * 生成随机长度的随机字符串.
	 * <p>生成一个长度为介于minLength和maxLength之间的随机字符串，字符串中所含字符的类型由pattern决定。<br>
	 * @see #nextString(int length, String pattern,String strTable)
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @param pattern 字符组成表的类型
	 * @return 返回生成的随机长度的随机字符串，如果参数错误，则返回null。
	 * @throws Exception
	 */
	public static String nextRandomLengthString(int minLength,int maxLength,String pattern) throws Exception {
		String str = "";
		int length = new Random().nextInt(maxLength + 1);
		if (length < minLength) {
			str = nextRandomLengthString(minLength, maxLength,pattern);
		} else {
			str = nextString(length,pattern);
		}
		return str;
	}
}
