package edu.jiangxin.apktoolbox.crack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.filechooser.FileNameExtensionFilter;

public final class MyGZip extends Compressor {
	
	private static FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"GZIP压缩文件(*.gz)", "gz");

	@Override
	public final void doCompress(File file, String destpath) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		File gf = new File(destpath);
		FileOutputStream fos = new FileOutputStream(gf);
		GZIPOutputStream gzos = new GZIPOutputStream(fos);
		BufferedOutputStream bos = new BufferedOutputStream(gzos);
		readAndWrite(bis, bos);
	}

	@Override
	public final void doUnCompress(File srcFile, String destpath) throws IOException {
		FileInputStream fis = new FileInputStream(srcFile);
		GZIPInputStream gzis = new GZIPInputStream(fis);
		BufferedInputStream bis = new BufferedInputStream(gzis);

		File tf = new File(destpath);
		FileOutputStream fos = new FileOutputStream(tf);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		readAndWrite(bis, bos);
	}

	@Override
	public final FileNameExtensionFilter getFileFilter() {
		return filter;
	}
}
