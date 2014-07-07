package edu.jiangxin.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 有关文件备份的工具类
 * <p style="text-indent:2em">提供若干静态方法以实现对文件的备份。</p>
 * @author jiangxin
 * @see Recovery
 */
public class Backup {

	/**
	 * 实现单个文件的备份.
	 * <p style="text-indent:2em">将文件srcFileString备份为desFileString，并添加后缀suffix。</p>
	 * @param srcFileString 源文件
	 * @param desFileString 目的文件
	 * @param suffix 指定备份文件的附加后缀名
	 * @param isOverload 指定如果目标文件已存在是否复制
	 * @throws IOException
	 */
	private static void backupFile(String srcFileString, String desFileString,String suffix,
			boolean isOverload) throws IOException {

		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		srcFileString = srcFileFile.getAbsolutePath();
		desFileString = desFileFile.getAbsolutePath() + suffix;
		FileProcess.copyFile(srcFileString, desFileString, isOverload);

	}

	/**
	 * 实现对某文件的备份.
	 * <p style="text-indent:2em">将文件srcFileString添加后缀suffix后进行备份。</p>
	 * @param fileString 需要备份的文件
	 * @param suffix 指定备份文件的附加后缀名
	 * @throws IOException
	 */
	public static void backupFile(String fileString,String suffix) throws IOException {
		backupFile(fileString, fileString, suffix, true);

	}

	/**
	 * 实现对文件列表的备份.
	 * <p style="text-indent:2em">将文件列表files中的文件添加后缀suffix后进行备份</p>
	 * see {@link #backupFile(String, String)}
	 * @param files 将要备份的文件列表
	 * @param suffix 备份文件的附加后缀名
	 * @throws IOException
	 */
	public static void backupFiles(ArrayList<File> files,String suffix) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			backupFile(it.next().getAbsolutePath(), suffix);
		}
	}
}
