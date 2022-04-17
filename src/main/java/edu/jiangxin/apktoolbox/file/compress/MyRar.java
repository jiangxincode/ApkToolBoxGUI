package edu.jiangxin.apktoolbox.file.compress;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class MyRar extends Archiver {
	private Logger logger;
	private Configuration conf;

	public MyRar() {
		logger = LogManager.getLogger(this.getClass().getSimpleName());
		conf = Utils.getConfiguration();
	}

	private static FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"RAR压缩文件(*.rar)", "rar");

	@Override
	public void doArchiver(File[] files, String destPath)
			throws IOException {
	}

	@Override
	public void doUnArchiver(File srcFile, String destPath,
							 String password) throws IOException, WrongPassException {
		try {
			Archive a = new Archive(srcFile);
			FileHeader fh;
			while ((fh = a.nextFileHeader()) != null) {
				File f = new File(destPath + "/"
						+ fh.getFileNameString().trim());

				if (fh.isDirectory()) {
					f.mkdirs();
					continue;
				}

				/*
				 * 父目录不存在则创建
				 */
				File parent = f.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}

				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				a.extractFile(fh, bos);

				bos.flush();
				bos.close();
			}
			a.close();
		} catch (RarException e) {
			throw new WrongPassException();
		}
	}

	@Override
	public FileNameExtensionFilter getFileFilter() {
		return filter;
	}
}
