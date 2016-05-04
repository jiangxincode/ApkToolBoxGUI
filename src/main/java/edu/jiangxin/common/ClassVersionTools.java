package edu.jiangxin.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ClassVersionTools {

	public static void getClassVersion(String srcDirString) {
		File srcDirFile = new File(srcDirString);
		if (!srcDirFile.exists()) {
			System.out.println("源目录不存在" + srcDirFile.getAbsolutePath());
		}
		ArrayList<File> arrayList = new FileFilterWrapper().list(srcDirString, ".class");
		Iterator<File> it = arrayList.iterator();
		while (it.hasNext()) {
			File srcFileFile = it.next();

			byte[] bVersion = new byte[4];
			FileInputStream fis;
			try {
				fis = new FileInputStream(srcFileFile);
				fis.read(bVersion, 0, 4);
				fis.read(bVersion, 0, 4);
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println(srcFileFile.getAbsolutePath() + bVersion[0] + bVersion[1] + bVersion[2] + bVersion[3]);

		}
	}

	public static void modifyClassVersion(String srcDirString, String desDirString) throws IOException {
		File srcDirFile = new File(srcDirString);
		File desDirFile = new File(desDirString);
		if (!srcDirFile.exists()) {
			System.out.println("源目录不存在" + srcDirFile.getAbsolutePath());
		}
		FileProcess.copyDirectory(srcDirString, desDirString);
		ArrayList<File> arrayList = new FileFilterWrapper().list(srcDirString, ".class");
		Iterator<File> it = arrayList.iterator();
		while (it.hasNext()) {
			File srcFileFile = it.next();

			FileInputStream fis = new FileInputStream(srcFileFile);
			byte[] content = new byte[1024 * 1024];
			int len = fis.read(content);
			content[4] = 0x00;
			content[5] = 0x00;
			content[6] = 0x00;
			content[7] = 0x2F;
			fis.close();

			FileOutputStream fos = new FileOutputStream(srcFileFile);
			fos.write(content, 0, len);
			fos.close();
			System.out.println("Process file success." + srcFileFile.getAbsolutePath());
		}
	}

	public static void main(String[] args) throws IOException {
		modifyClassVersion("D:\\xwork-core-2.2.1", "D:\\jiangxin");
		getClassVersion("D:\\xwork-core-2.2.1");
	}

}
