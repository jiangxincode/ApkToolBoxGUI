package edu.jiangxin.apktoolbox.file.compress;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.ProcessLogOutputStream;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

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

	@Override
	public boolean prepareCracker() {
		try {
			Runtime.getRuntime().exec(path);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	//检查返回值来提升速度？
	//使用库来提升速度？
	//使用多线程来提升速度？
	//比较不同的方式来决定最终的方案
	@Override
	public boolean checkPwd(File file, String pwd) {
		String target = file.getAbsolutePath();
		String cmd = String.format("%s t -p%s %s", path, pwd, target);
		logger.info("checkPwd cmd: " + cmd);
		boolean result = false;
		try (ProcessLogOutputStream outStream = new ProcessLogOutputStream(logger, Level.INFO);
			 ProcessLogOutputStream errStream = new ProcessLogOutputStream(logger, Level.ERROR)
		) {
			CommandLine commandLine = CommandLine.parse(cmd);
			DefaultExecutor exec = new DefaultExecutor();
			PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
			exec.setStreamHandler(streamHandler);
			int exitValue = exec.execute(commandLine);
			logger.info("exitValue: [" + exitValue + "]");
			if (exitValue == 0) {
				result = true;
			}
		} catch (IOException ioe) {
			logger.error("exec fail");
		}
		return result;
	}
}
