/**
 * TextTools主程序
 * @author jiangxin
 */

package edu.jiangxin.encode;

import java.io.File;
import java.io.IOException;

public class Main {
	final static int MAXFILENUM = 100; //The max num of the files
	
	static boolean isDos2Unix = false; //Convert the file from dos to unix, or not
	static boolean isUnix2Dox = false; //Convert the file from unxi to dos, or not
	static boolean isBackup = false; //Backup the file that will change, or not
	static boolean isRecovery = false; //Recovery the file that has changed, or not
	static boolean isHelp = false; //Display the Usage information, or not
	
	static String fromEncoder = null; //The encoder convert from
	static String toEncoder = null; //The encoder convert to
	static String[] fileName = new String[MAXFILENUM]; //The max num of the files
	
	public static void main(String[] args) throws IOException {
		
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
			} else if(temp.equals("-dos2unix")) { //Convert the file from dos to unix, or not
				isDos2Unix = true;
				continue;
			} else if(temp.equals("-unix2dos")) { //Convert the file from unxi to dos, or not
				isUnix2Dox = true;
				continue;
			} else if(temp.equals("-b")) { //Backup the file that will change, or not
				isBackup = true;
				continue;
			} else if(temp.equals("-r")) { //Recovery the file that has changed, or not
				isRecovery = true;
				continue;
			}
		}
		if(isHelp) {
			Info.usage();
			return ;
		}
		for(int i=0;(i<fileName.length&&fileName[i]!=null);i++) { // 循环遍历各个文件
			System.out.println("Now is processing file: " + fileName[i]);
			
			File file = new File(fileName[i]);
			String temp = file.getAbsolutePath().toString();
			
			if(isBackup) { //Backup the file
				String srcFileString = temp;
				String desFileString = temp + ".backup";
				FileProcess.copyFile(srcFileString, desFileString);
			} else if(isRecovery) { //Recovery the file
				String srcFileString = temp + ".backup";
				String desFileString = temp;
				FileProcess.moveFile(srcFileString, desFileString, true);
				continue ;
			}
			if(isDos2Unix) { //Convert from dos to unix
				OSPattenConvert.dos2Unix(temp,temp);
			} else if(isUnix2Dox) { //Convert from unix to dos
				OSPattenConvert.unix2Dos(temp,temp);
			}
			
			if(fromEncoder==null && toEncoder!=null) { //Dectect the encoder of the file
				String fromEncoder = EncoderDetector.judgeFile(temp);
				if(fromEncoder!=null) {
					System.out.println("The encoder of the file " + fileName[i] + " is: " + fromEncoder);
				} else {
					System.out.println("Something error!");
					return ;
				}
			}
			if(toEncoder!=null) { //Convert the encoder
				EncoderConvert.encodeFile(temp, fromEncoder, temp, toEncoder);
			}
		}
	}

}
