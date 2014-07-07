package edu.jiangxin.common;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 文件或文件夹的简单处理
 * @author jiangxin
 */
public class FileProcess {
	final static int BUFFERSIZE = 1024*5; //The size of the buffer

	public static void main(String args[]) throws IOException {
		//copyDirectory("test1", "test2");
		//copyFile("test1/test.txt","test2/test.txt");
		//deleteDir("temp/temp2");
	}
	
	/**
	 * 复制文件
	 * @param srcFileString
	 * @param desFileString
	 * @throws IOException
	 */
	public static void copyFile(String srcFileString, String desFileString,boolean isOverload) throws IOException {
		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFileFile));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFileFile));
		//尚未进行覆盖判断 bad
		byte[] buffer = new byte[BUFFERSIZE]; // 缓冲数组
		int len;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.flush(); // 刷新此缓冲的输出流
		in.close();
		out.close();
		System.out.println("Success to copy " + srcFileString + " to " + desFileString);
	}
	public static void copyFile(String srcFileString, String desFileString) throws IOException {
		copyFile(srcFileString, desFileString,true);
	}


	/**
	 * 复制文件夹
	 * @param srcDirString
	 * @param desDirString
	 * @throws IOException
	 */
	public static void copyDirectory(String srcDirString, String desDirString) throws IOException {
		File srcDirFile = new File(srcDirString);
		File desDirFile = new File(desDirString);
		if(!desDirFile.exists()) {
			desDirFile.mkdirs(); // 新建目标目录
		}
		
		File[] files = srcDirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				String srcFileTemp = files[i].getAbsolutePath();
				String desFileTemp = desDirFile.getAbsolutePath()+File.separator+files[i].getName();
				copyFile(srcFileTemp, desFileTemp);
			}
			if (files[i].isDirectory()) {
				String srcDirTemp = files[i].getAbsolutePath();
				String desDirTemp = desDirFile.getAbsolutePath() + File.separator + files[i].getName();
				copyDirectory(srcDirTemp, desDirTemp);
			}
		}
		System.out.println("Success to copy " + srcDirString + " to " + desDirString);
	}
	
	/**
	 * 移动文件
	 * @param srcFileString
	 * @param desFileString
	 * @param isOverride
	 * @throws IOException
	 */
	public static void moveFile(String srcFileString,String desFileString,boolean isOverride) throws IOException {
		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		if(isOverride) {
			if(desFileFile.delete()) {
				System.out.println("Success to delete desFile");
			}
		} else {
			return;
		}
		copyFile(srcFileString, desFileString);
		if(srcFileFile.delete()) {
			System.out.println("Success to delete srcFile");
		}
		
	}
	
	/**
	 * 递归删除目录
	 * @param dir
	 * @return 返回是否删除成功
	 */
	public static boolean deleteDir(String dir) {
		File dirFile = new File(dir);
		if (dirFile.isDirectory()) {
			String[] files = dirFile.list();
			for (int i = 0; i < files.length; i++) { //递归删除目录中的子目录下
				String temp = dirFile.getAbsolutePath() + File.separator.toString() + files[i];
				boolean success = deleteDir(temp);
				if (!success) {
					System.out.println("Something error!");
					return false;
				}
			}
		}
		boolean ret = dirFile.delete();
		if(ret) {
			System.out.println("Success to delete " + dir);
		} else {
			System.out.println("Something error!");
		}
		return ret;
	}
	
	/**
	 * 删除目录组
	 * @param dirs
	 * @return 返回是否删除成功
	 */
	public static boolean deleteDirs(String[] dirs) {
		for(int i=0;i<dirs.length;i++) {
			boolean isSuccessful = deleteDir(dirs[i]);
			if(isSuccessful == true) {
				System.out.println("成功删除目录：" + dirs[i]);
			}
		}
		return false;
	}
	public static int sumAllFiles(ArrayList<File> arrayList) throws IOException {
		int sum = 0;
		Iterator<File> it = arrayList.iterator();
		while(it.hasNext()) {
			File file = it.next();
			sum += LineCount.count(file.getAbsoluteFile().toString());
		}
		return sum;
	}
	public static String getString(String fileString,String encode,String startString,String endString) throws IOException {
		StringBuilder content = new StringBuilder();
		String temp = null;
		File fileFile = new File(fileString);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileFile), encode));
		while((temp=reader.readLine())!=null) {
			if(temp.contains(startString)) {
				while(!(temp=reader.readLine()).contains(endString)) {
					content.append(temp);
					content.append(System.getProperty("line.separator"));
				}
			}
		}
		reader.close();
		return content.toString();
	}
}
