/**
 * 文件编码转换
 * @author jiangxin
 */

package edu.jiangxin.encode;

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

public class EncoderConvert {
	public static final String[] extensions = null;

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
		
		//System.out.println(srcFileString);
		//System.out.println(srcEncoder);
		//System.out.println(desFileString);
		//System.out.println(desEncoder);
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
	public static void encodeFile(String fileString, String srcEncoder,String desEncoder) throws IOException {
		encodeFile(fileString, srcEncoder, fileString, desEncoder);
	}

	public static void encodeDir(String srcDirString, String srcEncoder,
			String desDirString, String desEncoder,String suffix) throws IOException {
		//File srcDirFile = new File(srcDirString);
		File desDirFile = new File(desDirString);
		ArrayList<File> files = fileFilter.list(srcDirString, suffix); // 获取所有符合条件的文件
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
	public static void encodeDir(String srcDirString, String srcEncoder,
			String desDirString, String desEncoder) throws IOException {
		encodeDir(srcDirString, srcEncoder, desDirString, desEncoder,null);
	}
	
	public static void encodeFiles(ArrayList<File> files,String fromEncoder, String toEncoder) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			encodeFile(it.next().getAbsolutePath(), fromEncoder, toEncoder);
		}
	}
	public static void main(String[] args) throws IOException {
		//encodeFile("temp/test1.txt", "gbk", "temp/test2.txt","UTF-8");
		encodeDir("temp/temp", "gbk", "temp/temp2","UTF-8");
	}
}
