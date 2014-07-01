/**
 * 不同操作系统文件格式转换
 * @author jiangxin
 */
package edu.jiangxin.encode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OSPattenConvert {
	public static void main(String[] args) throws IOException {
		dos2Unix("temp/test.txt", "temp/unix.txt");
		dos2Mac("temp/test.txt", "temp/mac.txt");
		dos2Linux("temp/test.txt", "temp/linux.txt");
		unix2Dos("temp/unix.txt", "temp/dos.txt");
	}
	public static void convert(String srcFileString,String desFileString,String options) throws IOException {
		String temp = null;
		if(srcFileString.equals(desFileString)) {
			srcFileString = srcFileString + ".temp";
			FileProcess.copyFile(desFileString, srcFileString);
		}
		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		BufferedReader reader = new BufferedReader(new FileReader(srcFileFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(desFileFile));
		while((temp=reader.readLine())!=null) {
			writer.write(temp);
			writer.write(options);
		}
		writer.close();
		reader.close();
		if(srcFileString.equals(desFileString+".temp")) {
			srcFileFile.delete();
		}
	}
	public static void dos2Unix(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\n");
		System.out.println("Success to convert " + srcFileString + " from dos to unix");
	}
	public static void dos2Linux(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\n");
		System.out.println("Success to convert " + srcFileString + " from dos to linux");
	}
	public static void dos2Mac(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r");
		System.out.println("Success to convert " + srcFileString + " from dos to mac");
	}

	public static void unix2Dos(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r\n");
		System.out.println("Success to convert " + srcFileString + " from unix to dos");
	}
	public static void unix2Linux(String srcFileString,String desFileString) throws IOException {
		System.out.println("Success to convert " + srcFileString + " from unix to linux");
		return ;
	}
	public static void unix2Mac(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r");
		System.out.println("Success to convert " + srcFileString + " from unix to mac");
	}

	public static void linux2Dos(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r\n");
		System.out.println("Success to convert " + srcFileString + " from linux to dos");
	}
	public static void linux2Unix(String srcFileString,String desFileString) throws IOException {
		System.out.println("Success to convert " + srcFileString + " from linux to unix");
		return ;
	}
	public static void linux2Mac(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r");
		System.out.println("Success to convert " + srcFileString + " from linux to mac");
	}
	public static void mac2Unix(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\n");
		System.out.println("Success to convert " + srcFileString + " from mac to unix");
	}
	public static void mac2dos(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r\n");
		System.out.println("Success to convert " + srcFileString + " from mac to dos");
	}
	public static void mac2linux(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\n");
		System.out.println("Success to convert " + srcFileString + " from mac to linux");
	}

}
