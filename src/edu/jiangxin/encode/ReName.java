/**
 * 文件重命名
 * @author jiangxin
 */
package edu.jiangxin.encode;

import java.io.File;

public class ReName {
	public static void reName(String srcDirString,String desDirString) {
		
		File srcDirFile = new File(srcDirString);
		File[] files = srcDirFile.listFiles();

		for (int i = 0; i < files.length; i++) {
			String temp = srcDirFile.getAbsolutePath().toString() + File.separator + i + files[i].getName();
			System.out.println(temp);
			boolean flag = files[i].renameTo(new File(temp));
			if (flag) {
				System.out.println("重命名成功" + (i + 1));
			} else {
				System.out.println("失败");
			}
		}
	}
	public static void main(String[] args) {
		reName("temp", "temp");
	}
}