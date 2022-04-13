package edu.jiangxin.apktoolbox.file.compress;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

import java.io.*;

import javax.swing.filechooser.FileNameExtensionFilter;

public final class MyRar extends Archiver implements ICracker {

	private static FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"RAR压缩文件(*.rar)", "rar");

	@Override
	public final void doArchiver(File[] files, String destpath)
			throws IOException {
	}

	@Override
	public boolean prepareCracker() {
		try {
			Runtime.getRuntime().exec("rar.exe");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public String crack(File file, CodeIterator codeIterator) {
		boolean ret = false;
		// 系统安装winrar的路径
		String cmd = "rar.exe";
//		String cmd = "C:\\Program Files\\WinRAR\\Rar.exe";
		String target = file.getAbsolutePath();
		String pass = codeIterator.nextCode();

		while (!ret && pass != null) {
			String unrarCmd = String.format("%s t -p%s %s", cmd, pass, target);
			Runtime rt = Runtime.getRuntime();
			Process pre = null;
			try {
				pre = rt.exec(unrarCmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(pre.getInputStream(),
						"gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			BufferedReader bf = new BufferedReader(isr);
			String line = null;
			while (true) {
				try {
					if (!((line = bf.readLine()) != null)) break;
				} catch (IOException e) {
					e.printStackTrace();
				}
//				System.out.println(line);
				if (line.indexOf("全部成功") >= 0) {
					ret = true;
					break;
				}
			}
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!ret) {
				pass = codeIterator.nextCode();
			}
		}
		if (ret) {
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
	
	/**
	 * 是否已准备好(暴力破解rar密码)
	 */
	public static boolean isReady() {
		try {
			Runtime.getRuntime().exec("rar.exe");
		} catch (IOException e) {
			return false;
		}
		return true;		
	}
}
