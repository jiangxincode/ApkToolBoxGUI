package edu.jiangxin.apktoolbox.file.compress;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

import javax.swing.filechooser.FileNameExtensionFilter;

public final class MyRar extends Archiver implements ICracker {
	private Logger logger;
	private Configuration conf;
	private String path;

	public MyRar() {
		logger = LogManager.getLogger(this.getClass().getSimpleName());
		conf = Utils.getConfiguration();
		path = conf.getString(Constants.RAR_PATH_KEY);
	}

	private static FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"RAR压缩文件(*.rar)", "rar");

	@Override
	public final void doArchiver(File[] files, String destpath)
			throws IOException {
	}

	@Override
	public boolean prepareCracker() {
		try {
			Runtime.getRuntime().exec(path);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public String crack(File file, CodeIterator codeIterator) {
		boolean isHit = false;
		String target = file.getAbsolutePath();
		String pass = codeIterator.nextCode();

		while (!isHit && pass != null) {
			String unrarCmd = String.format("%s t -p%s %s", path, pass, target);
			logger.info("unrar cmd: " + unrarCmd);
			Process process = null;
			try {
				process = Runtime.getRuntime().exec(unrarCmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try (BufferedReader bf = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"))) {
				String line;
				while ((line = bf.readLine()) != null) {
					logger.info(line);
					if (line.indexOf("全部成功") >= 0 || line.indexOf("全部正常") >= 0) {
						isHit = true;
						break;
					}
				}
			} catch (IOException e) {

			}
			if (!isHit) {
				pass = codeIterator.nextCode();
			}
		}
		if (isHit) {
			return pass;
		} else {
			return null;
		}
	}

	@Override
	public final void doUnArchiver(File srcfile, String destpath,
			String password) throws IOException, WrongPassException {
		try {
			Archive a = new Archive(srcfile);
			FileHeader fh;
			while ((fh = a.nextFileHeader()) != null) {
				File f = new File(destpath + "/"
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
	public final FileNameExtensionFilter getFileFilter() {
		return filter;
	}
}
