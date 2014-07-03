package edu.jiangxin.encode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class RemoveComments {

	/** 
	 * COMMENCODES为普通代码模式,PRECOMMENTS为斜杠模式,MORELINECOMMENTS为多行注释模式,
	 * STARMODEL为多行注释下星号模式，SINGLELINECOMMENTS为单行注释模式，STRINGMODEL为字符串模式，
	 * TRANSFERMODEL为字符串转义模式
	 */
	private enum model {
		COMMENCODES, PRECOMMENTS, MORELINECOMMENTS, STARMODEL, SINGLELINECOMMENTS, STRINGMODEL, TRANSFERMODEL
	}

	private static model stats = model.COMMENCODES; // stats记录状态

	public static String remove(Reader in) throws IOException {
		StringBuilder s = new StringBuilder();
		int n;
		while ((n = in.read()) != -1) {
			switch ((char) n) {
			case '/':
				if (stats == model.COMMENCODES) {// 如果当前位普通代码模式则转到斜杠模式
					stats = model.PRECOMMENTS;
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到单行注释模式
					stats = model.SINGLELINECOMMENTS;
					s.append("  ");
				} else if (stats == model.MORELINECOMMENTS) {//
					s.append(" ");
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到普通代码模式
					stats = model.COMMENCODES;
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {
					s.append("/");
				} else if (stats == model.TRANSFERMODEL) {
					stats = model.STRINGMODEL;
					s.append("/");
				}
				break;
			case '*':
				if (stats == model.COMMENCODES) {
					s.append("*");
				} else if (stats == model.PRECOMMENTS) {// 如果为斜杠模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append("  ");
				} else if (stats == model.MORELINECOMMENTS) {// 如果当前为多行注释模式则转到星号模式
					stats = model.STARMODEL;
					s.append(" ");
				} else if (stats == model.STARMODEL) {
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {
					s.append("*");
				} else if (stats == model.TRANSFERMODEL) {
					s.append("*");
				}
				break;
			case '"':
				if (stats == model.COMMENCODES) {// 如果当前为普通代码模式则转到字符串模式
					stats = model.STRINGMODEL;
					s.append("\"");
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到普通代码模式
					stats = model.COMMENCODES;
					s.append("/\"");
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {// 如果当前为字符串模式则转到普通代码模式
					stats = model.COMMENCODES;
					s.append("\"");
				} else if (stats == model.TRANSFERMODEL) {// 如果当前为转义模式则转到字符串格式
					stats = model.STRINGMODEL;
					s.append("\"");
				}
				break;
			case '\\':
				if (stats == model.COMMENCODES) {
					s.append("\\");
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到普通代码格式
					stats = model.COMMENCODES;
					s.append("/\\");
				} else if (stats == model.MORELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {// 如果当前为字符串模式则转到字符串转移模式
					stats = model.TRANSFERMODEL;
					s.append("\\");
				} else if (stats == model.TRANSFERMODEL) {// 如果当前为字符串转义模式则转到字符串模式
					stats = model.STRINGMODEL;
					s.append("\\");
				}
				break;
			case '\n':
				if (stats == model.COMMENCODES) {
					s.append("\n");
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到普通代码格式
					stats = model.COMMENCODES;
					s.append("/\n");
				} else if (stats == model.MORELINECOMMENTS) {
					s.append("\n");
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append("\n");
				} else if (stats == model.SINGLELINECOMMENTS) {// 如果当前为单行注释模式则转到普通代码格式
					stats = model.COMMENCODES;
					s.append("\n");
				} else if (stats == model.STRINGMODEL) {
					s.append("\n");
				} else if (stats == model.TRANSFERMODEL) {
					s.append("\\n");
				}
				break;
			default:
				if (stats == model.COMMENCODES) {
					s.append((char) n);
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到普通代码格式
					stats = model.COMMENCODES;
					s.append("/" + (char) n);
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {
					s.append((char) n);
				} else if (stats == model.TRANSFERMODEL) {// 如果当前为字符串转义模式则转到字符串模式
					stats = model.STRINGMODEL;
					s.append((char) n);
				}
				break;
			}
		}
		String result = s.toString();
		//System.out.println(result);
		return result;
	}
	public static void removeJavaFileComment(String srcFileString,String desFileString,String encoding) throws IOException {
		String special = ".removeJavaFileComment.temp"; //临时文件的后缀名，尽量保证不会含有同名文件
		if(srcFileString.equals(desFileString)) { //如果源文件和目标文件相同（包括路径），则使用临时文件进行转换
			srcFileString = srcFileString + special;
			FileProcess.copyFile(desFileString, srcFileString);
		}
		
		File srcFileFile = new File(srcFileString);
		File desFileFile = new File(desFileString);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFileFile), encoding));
		String tempString = remove(reader);
		FileWriter writer = new FileWriter(desFileFile);
		writer.write(tempString);
		writer.close();
		
		if(srcFileString.equals(desFileString+special)) { //如果存在临时文件，则删除
			srcFileFile.delete();
		}
	}
	public static void removeJavaFileComment(String fileString,String encoding) throws IOException {
		removeJavaFileComment(fileString,fileString,encoding);
		
	}
	public static void removeJavaFilesComment(ArrayList<File> files,String encoding) throws IOException {
		Iterator<File> it = files.iterator();
		while(it.hasNext()) {
			removeJavaFileComment(it.next().getAbsolutePath(), encoding);
		}
	}
}
