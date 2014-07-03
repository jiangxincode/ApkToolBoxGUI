package edu.jiangxin.encode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Recovery {
	public static void main(String[] args) {

	}

	public static void recoveryFile(String srcFileString, String desFileString,String suffix,
			boolean isOverload) throws IOException {

		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		srcFileString = srcFileFile.getAbsolutePath();
		String temp = desFileFile.getAbsolutePath();
		if(temp.indexOf(suffix)==-1) {
			return ;
		}
		desFileString = temp.substring(0, temp.indexOf(suffix));
		System.out.println(desFileString);
		FileProcess.moveFile(srcFileString, desFileString,isOverload);

	}
	public static void recoveryFile(String fileString,String suffix) throws IOException {
		recoveryFile(fileString, fileString, suffix, true);

	}
	public static void recoveryFiles(ArrayList<File> files,String suffix) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			recoveryFile(it.next().getAbsolutePath(), suffix);
		}
	}

}
