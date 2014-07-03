/**
 * TextTools主程序
 * @author jiangxin
 */

package edu.jiangxin.encode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	final static int MAXFILENUM = 100; //The max num of the files
	
	static boolean isOSConvert = false; //Convert the file from one os pattern to another, or not
	static boolean isBackup = false; //Backup the file that will change, or not
	static boolean isRecovery = false; //Recovery the file that has changed, or not
	static boolean isHelp = false; //Display the Usage information, or not
	static boolean isSum = false; //Sum the code lines, or not
	static boolean isReName = false; //Rename the file, or not
	static boolean isRemoveComment = false; //Remove the comment, or not
	
	
	static String fromEncoder = null; //The encoder convert from
	static String toEncoder = null; //The encoder convert to
	static String pattern = null; //The pattern of OS convert
	static String suffix = null;
	static String[] fileName = new String[MAXFILENUM]; //The max num of the files
	
	static ArrayList<File> files = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		
		if(args.length == 0) {
			System.out.println("Hello world");
		}
		for(int i=0;i<args.length;i++) {
			String temp = args[i];
			if(temp.equals("-h")) { //Display the Usage information, or not
				isHelp = true;
				break;
			} else if(!temp.startsWith("-")) { //Store the files that inputed
				for(int j=0;i<args.length;i++) {
					fileName[j] = new String(args[i]);
					i++;
				}
			} else if(temp.equals("-f")) { //Store the fromEncoder
				i++;
				fromEncoder = args[i];
				continue;
			} else if(temp.equals("-t")) { //Store the toEncoder
				i++;
				toEncoder = args[i];
				continue;
			} else if(temp.equals("-os")) { //Convert the file from dos to unix, or not
				i++;
				pattern = args[i];
				isOSConvert = true;
				continue;
			} else if(temp.equals("-b")) { //Backup the file that will change, or not
				isBackup = true;
				continue;
			} else if(temp.equals("-suffix")) {
				i++;
				pattern = args[i];
				continue;
				
			} else if(temp.equals("-r")) { //Recovery the file that has changed, or not
				isRecovery = true;
				continue;
			} else if(temp.equals("-sum")) { //Sum the code lines,or not
				isSum = true;
				continue;
			} else if(temp.equals("-rename")){
				isReName = true;
				continue;
				
			} else if(temp.equals("-removecomment") || temp.equals("-rc")) {
				isRemoveComment = true;
				System.out.println("here");
				continue;
			}
		}
		if(isHelp) {
			Info.usage();
			return ;
		}
		for(int i=0;(i<fileName.length&&fileName[i]!=null);i++) { // 循环遍历各个文件
			System.out.println("Now is processing file: " + fileName[i]);
			files.addAll(fileFilter.list(fileName[i], suffix));
			
			if(isBackup) { //Backup the file
				Backup.backupFiles(files, ".backup");
				continue ;
			} else if(isRecovery) { //Recovery the file
				Recovery.recoveryFiles(files, ".backup");
				continue ;
			}
			if(isOSConvert) { //Convert OS pattern
				OSPattenConvert.osConvertFiles(files, pattern);
			}
			
			if(fromEncoder==null && toEncoder!=null) { //Dectect the encoder of the file
				fromEncoder = EncoderDetector.judgeFile(files.get(0).getAbsolutePath());
				System.out.println(files.get(0).getAbsolutePath());
				if(fromEncoder!=null) {
					System.out.println("The encoder of the file " + fileName[i] + " is: " + fromEncoder);
				} else {
					System.out.println("Something error!");
					return ;
				}
			}
			if(toEncoder!=null) { //Convert the encoder
				System.out.println(fromEncoder);
				EncoderConvert.encodeFiles(files, fromEncoder, toEncoder);
				
			}
			if(isSum) {
				int sumCode = FileProcess.sumAllFiles(files);
				System.out.println(sumCode);
			}
			if(isReName) {
				ReName.main(null);
			}
			if(isRemoveComment) {
				RemoveComments.removeJavaFilesComment(files, "UTF-8");
				System.out.println("here");
			}
		}
	}

}
