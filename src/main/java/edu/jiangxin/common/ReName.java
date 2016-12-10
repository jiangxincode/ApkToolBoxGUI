package edu.jiangxin.common;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bouncycastle.util.encoders.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 文件重命名
 *
 * @author jiangxin
 */
public class ReName {
	public static void encryptFileName(String dir) throws Exception {

		File srcDirFile = new File(dir);
		File[] files = srcDirFile.listFiles();

		File recoder = new File("d:/temp/encryptFile.txt");

		StringBuilder sb = new StringBuilder();


		for (File file : files) {
			if (file.isDirectory()) {
				continue;
			}
			String oldName = file.getName();
			String newName = encode(oldName);
			boolean result = file.renameTo(new File(file.getParent() + File.separator + newName.substring(0, 10)));
			sb.append("oldName: " + oldName + "\n" + "newName: " + newName + "\n" + result + "\n");

		}
		FileUtils.writeStringToFile(recoder, sb.toString());
	}

	public static void reName(String srcDirString, String desDirString) throws Exception {

		File srcDirFile = new File(srcDirString);
		File[] files = srcDirFile.listFiles();

		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].getName());
			// String temp = srcDirFile.getAbsolutePath().toString() +
			// File.separator + i + encode(files[i].getName());
			String temp = encode(files[i].getName());
			System.out.println(temp);
			System.out.println(decode(temp));
			System.out.println();
			/*
			 * boolean flag = files[i].renameTo(new File(temp)); if (flag) {
			 * System.out.println("重命名成功" + (i + 1)); } else {
			 * System.out.println("失败"); }
			 */
		}
	}

	public static void main(String[] args) throws Exception {
		encryptFileName("D:/temp/test");
	}

	/**
	 * 字符编码
	 */
	public final static String ENCODING = "UTF-8";

	/**
	 * Base64编码
	 *
	 * @param data
	 *            待编码数据
	 * @return String 编码数据
	 * @throws Exception
	 */
	public static String encode(String data) throws Exception {

		// 执行编码
		byte[] b = Base64.encode(data.getBytes(ENCODING));

		return new String(b, ENCODING);
	}

	/**
	 * Base64解码
	 *
	 * @param data
	 *            待解码数据
	 * @return String 解码数据
	 * @throws Exception
	 */
	public static String decode(String data) throws Exception {

		// 执行解码
		byte[] b = Base64.decode(data.getBytes(ENCODING));

		return new String(b, ENCODING);
	}
}