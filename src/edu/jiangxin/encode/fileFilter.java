/**
 * 文件过滤器
 * @author jiangxin
 */
package edu.jiangxin.encode;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class fileFilter {
	static ArrayList<File> arrayList = new ArrayList<>();

	public static void main(String[] args) {
		ArrayList<File> mylist = list("E:/temp/java/Test", ".java");
		System.out.println(mylist);
	}

	public static ArrayList<File> list(String name, String suffix) {

		try {
			File file = new File(name);
			if (!file.exists()) {
				System.out.println("Can't find the file!");
				return null;
			}

			if (file.isDirectory()) { //如果是目录的话，将该目录下符合条件的文件加入ArrayList
				File[] list = file.listFiles(getFileExtensionFilter(suffix));
				for (int i = 0; i < list.length; i++) {
					if(list[i].isFile()) {
						//System.out.println(list[i].toString());
						arrayList.add(list[i]);
					}
					
				}
				//System.out.println(list);

				list = file.listFiles(getDirectoryFilter()); // 过滤出所有的目录
				for (int i = 0; i < list.length; i++) {
					list(list[i].toString(), suffix);
				}

			} else { //如果是文件的话，直接将该文件加入ArrayList
				arrayList.add(file);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		//System.out.println(arrayList);
		return arrayList;

	}

	public static FilenameFilter getFileExtensionFilter(final String extension) {// 指定扩展名过滤
		if (extension == null) { //没有指定后缀，则返回该目录下所有的文件
			return new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return true;
				}
			};
		} else { //指定后缀，则返回该目录下拥有这些后缀的文件
			return new FilenameFilter() {
				public boolean accept(File file, String name) {
					boolean ret = name.endsWith(extension);
					return ret;
				}
			};
		}

	}

	public static FileFilter getDirectoryFilter() { // 得到所有的目录
		return new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();// 关键判断点
			}
		};
	}
}
