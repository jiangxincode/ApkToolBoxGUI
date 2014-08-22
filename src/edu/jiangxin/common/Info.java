package edu.jiangxin.common;

import java.io.IOException;

/**
 * 信息显示
 * @author jiangxin
 */
public class Info {
	
	/**
	 * 显示使用方法信息.
	 * <p style="text-indent:2em">读取README文件中的usage信息.</p>
	 * @throws IOException
	 */
	public static void showUsage() throws IOException {
		String content = FileProcess.getString("README.txt", "UTF-8", "start usage", "end usage");
		System.out.println(content);
	}
	
	/**
	 * 显示日志信息.
	 * <p style="text-indent:2em">读取README文件中的log信息.</p>
	 * @throws IOException
	 */
	public static void showLog() throws IOException {
		String content = FileProcess.getString("README.txt", "UTF-8", "start log", "end log");
		System.out.println(content);
	}
	
	/**
	 * 显示版权信息.
	 * <p style="text-indent:2em">读取README文件中的copyright信息.</p>
	 * @throws IOException
	 */
	public static void showCopyright() throws IOException {
		String content = FileProcess.getString("README.txt", "UTF-8", "start copyright", "end copyright");
		System.out.println(content);
	}
	
	/**
	 * 显示未来功能信息.
	 * <p style="text-indent:2em">读取README文件中的future functions信息.</p>
	 * @throws IOException
	 */
	public static void showFutureFuntions() throws IOException {
		String content = FileProcess.getString("README.txt", "UTF-8", "start future funtions", "end future funtions");
		System.out.println(content);
	}
	
	/**
	 * 显示所有.
	 * <p style="text-indent:2em">读取README文件.</p>
	 * @throws IOException
	 */
	public static void showAll() throws IOException {
		showUsage();
		showLog();
		showCopyright();
		showFutureFuntions();
	}

}
