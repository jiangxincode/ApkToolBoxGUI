package edu.jiangxin.common;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * 文件过滤器.
 * <p style="text-indent:2em">过滤出符合要求的目录或者文件夹。</p>
 * @author jiangxin
 */
public class FileFilterWrapper {
	
	static ArrayList<File> arrayList = new ArrayList<>();

	/**
	 * 实现对指定文件或者目录的过滤
	 * @param name 文件或者目录名称
	 * @param suffix 指定后缀
	 * @return 返回一个文件列表
	 */
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
						arrayList.add(list[i]);
					}
					
				}

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
		return arrayList;

	}

	private static FilenameFilter getFileExtensionFilter(final String extension) {// 指定扩展名过滤
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

	private static FileFilter getDirectoryFilter() { // 得到所有的目录
		return new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();// 关键判断点
			}
		};
	}
}
