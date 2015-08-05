package edu.jiangxin.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 有关文件备份之后的恢复的工具类
 * <p style="text-indent:2em">提供若干静态方法以实现对备份文件的恢复。</p>
 * @author jiangxin
 * @see Backup
 */
public class Recovery {
	
	/**
	 * 实现对备份文件的恢复.
	 * <p style="text-indent:2em">将由Backup.backupFile(String fileString,String suffix)方法备份的文件进行恢复。</p>
	 * see {@link Backup#backupFile(String, String)}
	 * @param fileString 备份文件
	 * @param suffix 备份文件的附加后缀名
	 * @throws IOException
	 */
	public static void recoveryFile(String fileString,String suffix) throws IOException {
		File fileFile = new File(fileString);
		String temp = fileFile.getAbsolutePath();
		if(temp.indexOf(suffix)==-1) {
			return ;
		}
		temp = temp.substring(0, temp.indexOf(suffix));
		System.out.println(temp);
		FileProcess.moveFile(fileFile.getAbsolutePath(), temp,true);

	}
	
	/**
	 * 实现对备份文件列表的恢复.
	 * <p style="text-indent:2em">将由Backup.backupFile(String fileString,String suffix)方法备份的文件进行恢复。</p>
	 * see {@link Backup#backupFile(String, String)}
	 * @param files 备份文件列表
	 * @param suffix 备份文件的附加后缀名
	 * @throws IOException
	 */
	public static void recoveryFiles(ArrayList<File> files,String suffix) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			recoveryFile(it.next().getAbsolutePath(), suffix);
		}
	}

}
