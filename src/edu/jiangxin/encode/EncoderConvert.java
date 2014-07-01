/**
 * @author jiangxin
 */

package edu.jiangxin.encode;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class EncoderConvert {
	public static final String[] extensions = {"java"};

	public static void encodeFile(String srcFileString, String srcEncoder,
			String desFileString, String desEncoder) throws IOException {
		if(srcFileString.equals(desFileString)) {
			srcFileString = srcFileString + ".temp";
			FileProcess.copyFile(desFileString, srcFileString);
		}
		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		System.out.println(srcFileString);
		System.out.println(srcEncoder);
		System.out.println(desFileString);
		System.out.println(desEncoder);
		FileUtils.writeLines(desFileFile, desEncoder,
				FileUtils.readLines(srcFileFile, srcEncoder));
		System.out.println("转换完成！");
		if(srcFileString.equals(desFileString+".temp")) {
			System.out.println("here");
			srcFileFile.delete();
		}
	}

	public static void encodeDir(String srcDirString, String srcEncoder,
			String desDirString, String desEncoder) throws IOException {
		File srcDirFile = new File(srcDirString);
		File desDirFile = new File(desDirString);

		Collection<File> files = FileUtils.listFiles(srcDirFile, null, true); // 获取所有java文件
		for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
			File tempFile = iterator.next();
			String desFileString = desDirFile.getAbsolutePath()
					+ File.separator
					+ tempFile.getName();
			String srcFileString = tempFile.getAbsolutePath().toString();
			System.out.println(srcFileString);
			System.out.println(desFileString);
			encodeFile(srcFileString, srcEncoder,desFileString, desEncoder);
			System.out.println("转换完成！");
		}
	}
	public static void main(String[] args) throws IOException {
		//encodeFile("temp/test1.txt", "gbk", "temp/test2.txt","UTF-8");
		encodeDir("temp", "gbk", "temp2","UTF-8");
	}
}
