package edu.jiangxin.apktoolbox.file.compress;

import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;


public final class My7Zip extends Archiver {

	private static FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"7-ZIP打包文件(*.7z)", "7z");


	@Override
	public void doArchiver(File[] files, String destPath)
			throws IOException {
	}

	@Override
	public void doUnArchiver(File srcFile, String destPath, String password)
			throws IOException, WrongPassException {
	}

	@Override
	public FileNameExtensionFilter getFileFilter() {
		return filter;
	}

}
