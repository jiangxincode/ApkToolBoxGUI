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
import java.util.ArrayList;
import java.util.Iterator;

public class OSPattenConvert {
	
	public static void main(String[] args) throws IOException {
		//dos2Unix("temp/test.txt", "temp/unix.txt");
		//dos2Mac("temp/test.txt", "temp/mac.txt");
		//unix2Dos("temp/unix.txt", "temp/dos.txt");
		osDirConvert("temp/temp","temp/tempdos","toDoS");
		osDirConvert("temp/temp","temp/tempunix","dos2unix");
		osDirConvert("temp/temp","temp/tempmac","dostomAC");
		osDirConvert("temp/temp","temp/templinux","toLinux");
	}
	
	/**
	 * 转换函数的真正实现函数，其它转换函数必须调用此函数
	 * @param srcFileString:源文件的文件名
	 * @param desFileString:目标文件的文件名
	 * @param options:换行符，比如:\n,\r,\r\n
	 * @throws IOException
	 */
	private static void convert(String srcFileString,String desFileString,String options) throws IOException {
		
		String special = ".OSPattenConvert.temp"; //临时文件的后缀名，尽量保证不会含有同名文件
		if(srcFileString.equals(desFileString)) { //如果源文件和目标文件相同（包括路径），则使用临时文件进行转换
			srcFileString = srcFileString + special;
			FileProcess.copyFile(desFileString, srcFileString);
		}
		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		
		if(!srcFileFile.exists()) { //判断源文件是否存在
			System.out.println("源文件不存在:" + srcFileFile.getAbsolutePath());
			return ;
		}
		if(!desFileFile.getParentFile().exists()) { //判断目标文件是否存在
			desFileFile.getParentFile().mkdirs();
		}
		
		BufferedReader reader = new BufferedReader(new FileReader(srcFileFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(desFileFile));
		
		String temp = null;
		while((temp=reader.readLine())!=null) {
			writer.write(temp);
			writer.write(options);
		}
		writer.close();
		reader.close();
		
		if(srcFileString.equals(desFileString+special)) { //如果存在临时文件，则删除
			srcFileFile.delete();
		}
	}
	
	private static void toUnix(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\n");
		System.out.println("Success to convert " + srcFileString + " to unix");
	}
	
	private static void toDos(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r\n");
		System.out.println("Success to convert " + srcFileString + " to dos");
	}
	
	private static void toMac(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r");
		System.out.println("Success to convert " + srcFileString + " to mac");
	}
	
	
	public static void dos2Unix(String srcFileString,String desFileString) throws IOException {
		toUnix(srcFileString, desFileString);
	}
	
	public static void dos2Mac(String srcFileString,String desFileString) throws IOException {
		toMac(srcFileString, desFileString);
	}

	public static void unix2Dos(String srcFileString,String desFileString) throws IOException {
		toDos(srcFileString, desFileString);
	}
	
	public static void unix2Mac(String srcFileString,String desFileString) throws IOException {
		toMac(srcFileString, desFileString);
	}

	public static void mac2Unix(String srcFileString,String desFileString) throws IOException {
		toUnix(srcFileString, desFileString);
	}
	public static void mac2Dos(String srcFileString,String desFileString) throws IOException {
		toDos(srcFileString, desFileString);
	}
	
	/**
	 * 文件转换函数，从一个操作系统文件格式转向另一个
	 * @param srcFileString
	 * @param desFileString
	 * @param 转换模式
	 * @throws IOException
	 */
	public static void osFileConvert(String srcFileString,String desFileString,String pattern) throws IOException {
		
		pattern = pattern.toLowerCase(); //允许输入大写字母格式的转换模式
		pattern = pattern.replace("to", "2"); //替换pattern中的to，防止误输入
		pattern = pattern.replace("linux", "unix"); //由于linux和unix文件格式相同，所以直接用unix替换linux，但会产生unix2unix类型
		pattern = pattern.replace("bsd", "unix"); //由于bsd和unix文件格式相同，所以直接用unix替换linux，但会产生unix2unix类型
		boolean isToUnix = pattern.equals("2unix") || pattern.equals("mac2unix") ||pattern.equals("dos2unix") || pattern.equals("unix2unix");
		boolean isToMac = pattern.equals("2mac") ||pattern.equals("dos2mac") || pattern.equals("unix2mac");
		boolean isToDos = pattern.equals("2dos") ||pattern.equals("mac2dos") || pattern.equals("unix2dos");
		
		if(isToUnix) {
			toUnix(srcFileString, desFileString);
		} else if(isToDos) {
			toDos(srcFileString, desFileString);
		} else if(isToMac) {
			toMac(srcFileString, desFileString);
		} else{
			System.err.println("Error input,can't convert!");
		}
	}
	public static void osFileConvert(String fileString,String pattern) throws IOException {
		osFileConvert(fileString,fileString,pattern);
	}
	
	/**
	 * 文件目录转换函数，将其中所有的文件从一个操作系统文件格式转向另一个
	 * @param srcDirString
	 * @param desDirString
	 * @param pattern
	 * @param suffix 过滤特定文件后缀
	 * @throws IOException
	 */
	public static void osDirConvert(String srcDirString,String desDirString,String pattern,String suffix) throws IOException {
		File srcDirFile = new File(srcDirString);
		File desDirFile = new File(desDirString);
		if(!srcDirFile.exists()) {
			System.out.println("源目录不存在" + srcDirFile.getAbsolutePath());
		}
		ArrayList<File> arrayList = fileFilter.list(srcDirString, suffix);
		Iterator<File> it = arrayList.iterator();
		while(it.hasNext()) {
			File srcFileFile = it.next();
			
			String srcFileString = srcFileFile.getAbsolutePath(); //得到源文件绝对地址
			//System.out.println("srcFileString" + srcFileString);
			
			String temp = srcFileFile.getAbsolutePath().substring(srcDirFile.getAbsolutePath().toString().length());
			//System.out.println("temp" + temp);
			
			String desFileString = desDirFile.getAbsolutePath() + temp; //得到目标文件绝对地址
			//System.out.println("desFileString" + desFileString);
			
			osFileConvert(srcFileString, desFileString, pattern);
		}
		
	}
	
	/**
	 * osDirConvert(String srcDirString,String desDirString,String pattern,String suffix)的重载函数默认suffix为null
	 * @param srcDirString
	 * @param desDirString
	 * @param pattern
	 * @throws IOException
	 */
	public static void osDirConvert(String srcDirString,String desDirString,String pattern) throws IOException {
		osDirConvert(srcDirString, desDirString, pattern, null);
	}
	public static void osConvertFiles(ArrayList<File> files,String pattern) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			osFileConvert(it.next().getAbsolutePath(), pattern);
		}
	}

}
