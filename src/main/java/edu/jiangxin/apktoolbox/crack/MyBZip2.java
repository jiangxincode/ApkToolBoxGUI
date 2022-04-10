package edu.jiangxin.apktoolbox.crack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.compress.compressors.bzip2.*;

public final class MyBZip2 extends Compressor {

	private static FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"BZIP2压缩文件(*.bz2)", "bz2");
	
	@Override
	public final void doCompress(File file, String destpath) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		File gf = new File(destpath);
		FileOutputStream fos = new FileOutputStream(gf);
		BZip2CompressorOutputStream gzos = new BZip2CompressorOutputStream(fos);
		BufferedOutputStream bos = new BufferedOutputStream(gzos);
		readAndWrite(bis, bos);
	}

	@Override
	public final void doUnCompress(File srcFile, String destpath) throws IOException {
		FileInputStream fis = new FileInputStream(srcFile);
		BZip2CompressorInputStream gzis = new BZip2CompressorInputStream(fis);
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
