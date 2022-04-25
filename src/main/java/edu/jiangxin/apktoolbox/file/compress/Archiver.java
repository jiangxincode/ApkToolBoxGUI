package edu.jiangxin.apktoolbox.file.compress;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public abstract class Archiver {
	/**
	 * 打包或压缩文件
	 * @param files 需要打包和压缩的文件数组
	 * @param destPath 目标文件路径
	 * @throws IOException
	 */
	public abstract void doArchiver(File[] files, String destPath) throws IOException;
	
	/**
	 * 解压或解包文件
	 * @param srcFile 需要解压或解包的源文件
	 * @param destPath 目标路径
	 * @param password 解压密码, 为null时表示不使用密码
	 * @throws IOException
	 * @throws WrongPassException 
	 */
	public abstract void doUnArchiver(File srcFile, String destPath, String password) throws IOException, WrongPassException;
		
	/**
	 * @return 本归档类对应文件的文件过滤器
	 */
	public abstract FileNameExtensionFilter getFileFilter();
}
