package edu.jiangxin.encode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 有关文件备份的工具类.
 * 主要实现文件的批量备份。
 * @author jiangxin
 * 
 */
public class Backup {

	/**
	 * 实现单个文件的备份.
	 * @param srcFileString 源文件的文件名（含路径）
	 * @param desFileString 目的文件的文件名（含路径）
	 * @param suffix 指定备份文件的附加后缀名
	 * @param isOverload 如果目的文件已存在是否复制
	 * @throws IOException
	 */
	public static void backupFile(String srcFileString, String desFileString,String suffix,
			boolean isOverload) throws IOException {

		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		srcFileString = srcFileFile.getAbsolutePath();
		desFileString = desFileFile.getAbsolutePath() + suffix;
		FileProcess.copyFile(srcFileString, desFileString,isOverload);

	}

	/**
	 * 实现对某文件的同目录下备份.
	 * see {@link #backupFile(String, String, String, boolean)}
	 * @param fileString 源文件的文件名（含路径）
	 * @param suffix 指定备份文件的附加后缀名
	 * @throws IOException
	 */
	public static void backupFile(String fileString,String suffix) throws IOException {
		backupFile(fileString, fileString, suffix, true);

	}

	/**
	 * 实现对某一文件列表在同目录下的本分.
	 * see {@link #backupFile(String, String, String, boolean)}
	 * @param files
	 * @param suffix
	 * @throws IOException
	 */
	public static void backupFiles(ArrayList<File> files,String suffix) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			backupFile(it.next().getAbsolutePath(), suffix);
		}
	}
}
