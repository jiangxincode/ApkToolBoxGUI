package edu.jiangxin.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 不同操作系统文件格式转换
 * @author jiangxin
 */
public class OSPatternConvert {
	
	/**
	 * 不同操作系统文件格式之间的转换.
	 * <p style="text-indent:2em">转换函数的真正实现函数，其它转换函数必须调用此函数。</p>
	 * @param srcFileString 源文件的文件名
	 * @param desFileString 目标文件的文件名
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
	
	/**
	 * 其它操作系统文件格式转换成Unix文件格式.
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @throws IOException
	 */
	private static void toUnix(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\n");
		System.out.println("Success to convert " + srcFileString + " to unix");
	}
	
	/**
	 * 其它操作系统文件格式转换成Dos文件格式.
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @throws IOException
	 */
	private static void toDos(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r\n");
		System.out.println("Success to convert " + srcFileString + " to dos");
	}
	
	/**
	 * 其它操作系统文件格式转换成Mac文件格式.
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @throws IOException
	 */
	private static void toMac(String srcFileString,String desFileString) throws IOException {
		convert(srcFileString, desFileString, "\r");
		System.out.println("Success to convert " + srcFileString + " to mac");
	}
	
	/**
	 * Dos文件格式转换成Unix文件格式.
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @throws IOException
	 */
	public static void dos2Unix(String srcFileString,String desFileString) throws IOException {
		toUnix(srcFileString, desFileString);
	}
	
	/**
	 * Dos文件格式转换成Mac文件格式.
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @throws IOException
	 */
	public static void dos2Mac(String srcFileString,String desFileString) throws IOException {
		toMac(srcFileString, desFileString);
	}

	/**
	 * Unix文件格式转换成Dos文件格式.
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @throws IOException
	 */
	public static void unix2Dos(String srcFileString,String desFileString) throws IOException {
		toDos(srcFileString, desFileString);
	}
	
	/**
	 * Unix文件格式转换成Mac文件格式.
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @throws IOException
	 */
	public static void unix2Mac(String srcFileString,String desFileString) throws IOException {
		toMac(srcFileString, desFileString);
	}

	/**
	 * Mac文件格式转换成Unix文件格式.
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @throws IOException
	 */
	public static void mac2Unix(String srcFileString,String desFileString) throws IOException {
		toUnix(srcFileString, desFileString);
	}
	
	/**
	 * Mac文件格式转换成Dos文件格式.
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @throws IOException
	 */
	public static void mac2Dos(String srcFileString,String desFileString) throws IOException {
		toDos(srcFileString, desFileString);
	}
	
	/**
	 * 不同操作系统文件格式之间的转换.
	 * <p style="text-indent:2em">从一个操作系统文件格式转向另一个，转换模式有patter指定，pattern的格式为：</p>
	 * <p style="text-indent:4em">os_a2os_b os_a和os_b的可能取值为linux/dos/windows/mac/unix/bsd等</p>
	 * @param srcFileString 转换前的文件
	 * @param desFileString 转换后的文件
	 * @param pattern 转换模式
	 * @throws IOException
	 */
	public static void osFileConvert(String srcFileString,String desFileString,String pattern) throws IOException {
		
		pattern = pattern.toLowerCase(); //允许输入大写字母格式的转换模式
		pattern = pattern.replace("to", "2"); //替换pattern中的to，防止误输入
		pattern = pattern.replace("linux", "unix"); //由于linux和unix文件格式相同，所以直接用unix替换linux，但会产生unix2unix类型
		pattern = pattern.replace("bsd", "unix"); //由于bsd和unix文件格式相同，所以直接用unix替换linux，但会产生unix2unix类型
		pattern = pattern.replace("windows", "dos"); //由于windows和dos文件格式相同，所以直接用unix替换linux，但会产生dos2dos类型
		boolean isToUnix = pattern.equals("2unix") || pattern.equals("mac2unix") ||pattern.equals("dos2unix") || pattern.equals("unix2unix");
		boolean isToMac = pattern.equals("2mac") ||pattern.equals("dos2mac") || pattern.equals("unix2mac");
		boolean isToDos = pattern.equals("2dos") ||pattern.equals("mac2dos") || pattern.equals("unix2dos") || pattern.equals("dos2dos");
		
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
	
	/**
	 * 不同操作系统文件格式之间的转换.
	 * <p style="text-indent:2em">从一个操作系统文件格式转向另一个，转换模式有patter指定，pattern的格式为：</p>
	 * <p style="text-indent:4em">os_a2os_b os_a和os_b的可能取值为linux/dos/windows/mac/unix/bsd等</p>
	 * @param fileString 需要转换的文件
	 * @param pattern 转换模式
	 * @see #osFileConvert(String, String, String)
	 * @throws IOException
	 */
	public static void osFileConvert(String fileString,String pattern) throws IOException {
		osFileConvert(fileString,fileString,pattern);
	}
	
	/**
	 * 不同操作系统文件格式之间的转换.
	 * <p style="text-indent:2em">转换的对象为指定目录中的所有以后缀suffix指定的文件</p>
	 * @param srcDirString 要求转换的目录
	 * @param desDirString 转化后要存放的目录
	 * @param pattern 转换模式
	 * @see #osFileConvert(String, String, String)
	 * @param suffix 过滤特定文件后缀
	 * @throws IOException
	 */
	public static void osDirConvert(String srcDirString,String desDirString,String pattern,String suffix) throws IOException {
		File srcDirFile = new File(srcDirString);
		File desDirFile = new File(desDirString);
		if(!srcDirFile.exists()) {
			System.out.println("源目录不存在" + srcDirFile.getAbsolutePath());
		}
		ArrayList<File> arrayList = FileFilterWrapper.list(srcDirString, suffix);
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
	 * 不同操作系统文件格式之间的转换.
	 * <p style="text-indent:2em">转换的对象为指定目录中的所有文件</p>
	 * @param srcDirString 要求转换的目录
	 * @param desDirString 转化后要存放的目录
	 * @param pattern 转换模式
	 * @see #osFileConvert(String, String, String)
	 * @throws IOException
	 */
	public static void osDirConvert(String srcDirString,String desDirString,String pattern) throws IOException {
		osDirConvert(srcDirString, desDirString, pattern, null);
	}
	
	/**
	 * 不同操作系统文件格式之间的转换.
	 * <p style="text-indent:2em">转换的对象为文件列表</p>
	 * @param files 要转换是文件列表
	 * @param pattern 转换模式
	 * @see #osFileConvert(String, String, String)
	 * @throws IOException
	 */
	public static void osConvertFiles(ArrayList<File> files,String pattern) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			osFileConvert(it.next().getAbsolutePath(), pattern);
		}
	}

}
