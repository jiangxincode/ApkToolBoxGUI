package edu.jiangxin.apktoolbox.file.compress;

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
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 使用java原生方法处理ZIP文件
 * 
 */
public final class MyZip extends Archiver {
	private Logger logger;
	private Configuration conf;
	private String path;

	public MyZip() {
		logger = LogManager.getLogger(this.getClass().getSimpleName());
		conf = Utils.getConfiguration();
		path = conf.getString(Constants.SEVEN_ZIP_PATH_KEY);
	}

	/**
	 * zip文件格式的过滤器
	 */
	private static FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"ZIP压缩文件(*.zip)", "zip");

	private void dfs(File[] files, ZipOutputStream zos, String fpath)
			throws IOException {
		byte[] buf = new byte[1024];
		for (File child : files) {
			if (child.isFile()) { // 文件
				FileInputStream fis = new FileInputStream(child);
				BufferedInputStream bis = new BufferedInputStream(fis);
				zos.putNextEntry(new ZipEntry(fpath + child.getName()));
				int len;
				while((len = bis.read(buf)) > 0) {
					zos.write(buf, 0, len);
				}
				bis.close();
				zos.closeEntry();
				continue;
			}
			File[] fs = child.listFiles();
			String nfpath = fpath + child.getName() + "/";
			if (fs.length <= 0) { // 空目录
				zos.putNextEntry(new ZipEntry(nfpath));
				zos.closeEntry();
			} else { // 目录非空，递归处理
				dfs(fs, zos, nfpath);
			}
		}
	}

	@Override
	public void doArchiver(File[] files, String destPath)
			throws IOException {
		/*
		 * 定义一个ZipOutputStream 对象
		 */
		FileOutputStream fos = new FileOutputStream(destPath);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ZipOutputStream zos = new ZipOutputStream(bos);
		dfs(files, zos, "");
		zos.flush();
		zos.close();
	}

	@Override
	public void doUnArchiver(File srcFile, String destPath,
							 String password) throws IOException {
		byte[] buf = new byte[1024];
		FileInputStream fis = new FileInputStream(srcFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ZipInputStream zis = new ZipInputStream(bis, Charset.forName("GBK"));
		ZipEntry zn = null;
		while ((zn = zis.getNextEntry()) != null) {
			File f = new File(destPath + "/" + zn.getName());
			if (zn.isDirectory()) {
				f.mkdirs();
			} else {
				/*
				 * 父目录不存在则创建
				 */
				File parent = f.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}

				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				int len;
				while ((len = zis.read(buf)) != -1) {
					bos.write(buf, 0, len);
				}
				bos.flush();
				bos.close();
			}
			zis.closeEntry();
		}
		zis.close();
	}

	@Override
	public FileNameExtensionFilter getFileFilter() {
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
		String cmd = String.format("%s t %s -p%s", path, target, pwd);
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
