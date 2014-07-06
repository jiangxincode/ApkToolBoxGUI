/**
 * 信息显示
 * @author jiangxin
 */
package edu.jiangxin.encode;

import java.io.IOException;

public class Info {
	public static void showUsage() throws IOException {
		String content = FileProcess.getString("README.txt", "UTF-8", "start usage", "end usage");
		System.out.println(content);
	}
	public static void showLog() throws IOException {
		String content = FileProcess.getString("README.txt", "UTF-8", "start log", "end log");
		System.out.println(content);
	}
	public static void showCopyright() throws IOException {
		String content = FileProcess.getString("README.txt", "UTF-8", "start copyright", "end copyright");
		System.out.println(content);
	}
	public static void showFutureFuntions() throws IOException {
		String content = FileProcess.getString("README.txt", "UTF-8", "start future funtions", "end future funtions");
		System.out.println(content);
	}
	public static void showAll() throws IOException {
		showUsage();
		showLog();
		showCopyright();
		showFutureFuntions();
	}
	public static void main(String[] args) throws IOException {
		//showUsage();
		//showLog();
		//showCopyright();
		showFutureFuntions();
	}

}
