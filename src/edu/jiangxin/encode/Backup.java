package edu.jiangxin.encode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Backup {
	public static void main(String[] args) {

	}

	public static void backupFile(String srcFileString, String desFileString,String suffix,
			boolean isOverload) throws IOException {

		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		srcFileString = srcFileFile.getAbsolutePath();
		desFileString = desFileFile.getAbsolutePath() + suffix;
		FileProcess.copyFile(srcFileString, desFileString,isOverload);

	}

	public static void backupFile(String srcFileString, String desFileString,String suffix) throws IOException {
		backupFile(srcFileString, desFileString, suffix,true);

	}

	public static void backupFile(String fileString,String suffix) throws IOException {
		backupFile(fileString, fileString, suffix, true);

	}

	public static void backupDirectory(String srcDirString,String desDirString) {
		File srcDirFile = new File(srcDirString);
		File desDirFile = new File(desDirString);
		if(!srcDirFile.exists()) {
			//bad
		}
	}

	public static void backupFiles(ArrayList<File> files,String suffix) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			backupFile(it.next().getAbsolutePath(), suffix);
		}
	}
}
