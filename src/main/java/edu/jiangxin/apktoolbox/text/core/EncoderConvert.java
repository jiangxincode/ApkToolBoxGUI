package edu.jiangxin.apktoolbox.text.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 文件编码转换的工具类.
 * @author jiangxin
 *
 */
public class EncoderConvert {

	/**
	 * 实现对文件的编码转换.
	 * @param srcFileString 源文件文件名
	 * @param srcEncoder 源文件编码
	 * @param desFileString 目的文件文件名
	 * @param desEncoder 需要转换的编码
	 * @throws IOException
	 */
	public static void encodeFile(String srcFileString, String srcEncoder,
			String desFileString, String desEncoder) throws IOException {
		if(srcFileString.equals(desFileString)) {
			srcFileString = srcFileString + ".temp";
			FileProcess.copyFile(desFileString, srcFileString);
		}
		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);

		File parentDir = desFileFile.getParentFile();
		if(!parentDir.exists()) {
			parentDir.mkdirs();
		}

		//TODO see http://akini.mbnet.fi/java/unicodereader/
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFileFile), srcEncoder));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(desFileFile), desEncoder));
		int ch = 0;
		while((ch=reader.read())!=-1) {
			writer.write(ch);
		}
		reader.close();
		writer.close();
		System.out.println("转换完成！");
		if(srcFileString.equals(desFileString+".temp")) {
			System.out.println("here");
			srcFileFile.delete();
		}
	}

	/**
	 * 实现文件的编码转换.
	 * see {@link #encodeFile(String, String, String, String)}
	 * @param fileString 文件名
	 * @param srcEncoder 原始编码
	 * @param desEncoder 需要转换的编码
	 * @throws IOException
	 */
	public static void encodeFile(String fileString, String srcEncoder,String desEncoder) throws IOException {
		encodeFile(fileString, srcEncoder, fileString, desEncoder);
	}

	/**
	 * 实现文件夹中特定文件的批量编码转换.
	 * see {@link #encodeFile(String, String, String, String)}
	 * @param srcDirString 原始文件夹
	 * @param srcEncoder 原始编码
	 * @param desDirString 目的文件夹
	 * @param desEncoder 需要转换的编码
	 * @param suffix 指定需要转换的后缀
	 * @throws IOException
	 */
	public static void encodeDir(String srcDirString, String srcEncoder,
			String desDirString, String desEncoder,String suffix) throws IOException {
		//File srcDirFile = new File(srcDirString);
		File desDirFile = new File(desDirString);
		ArrayList<File> files = new FileFilterWrapper().list(srcDirString, suffix); // 获取所有符合条件的文件
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			File tempFile = it.next();
			String desFileString = desDirFile.getAbsolutePath()+File.separator+tempFile.getName();
			String srcFileString = tempFile.getAbsolutePath().toString();
			System.out.println(srcFileString);
			System.out.println(desFileString);
			encodeFile(srcFileString, srcEncoder,desFileString, desEncoder);
			System.out.println("转换完成！");
		}
	}

	/**
	 * 实现文件夹中特定文件的批量编码转换.
	 * see {@link #encodeFile(String, String, String, String)}
	 * @param srcDirString 原始文件夹
	 * @param srcEncoder 原始编码
	 * @param desDirString 目的文件夹
	 * @param desEncoder 需要转换的编码
	 * @throws IOException
	 */
	public static void encodeDir(String srcDirString, String srcEncoder,
			String desDirString, String desEncoder) throws IOException {
		encodeDir(srcDirString, srcEncoder, desDirString, desEncoder,null);
	}

	/**
	 * 实现文件列表批量编码转换.
	 * see {@link #encodeFile(String, String, String, String)}
	 * @param fromEncoder 原始编码
	 * @param toEncoder 需要转换的编码
	 * @throws IOException
	 */
	public static void encodeFiles(ArrayList<File> files,String fromEncoder, String toEncoder) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			encodeFile(it.next().getAbsolutePath(), fromEncoder, toEncoder);
		}
	}
}
