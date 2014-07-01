package edu.jiangxin.encode;

import java.io.File;
import java.io.IOException;

public class Main {
	final static int MAXFILENUM = 100;
	static String fromEncoder = null;
	static String toEncoder = null;
	static boolean isDos2Unix = false;
	static boolean isUnix2Dox = false;
	static boolean isBackup = false;
	static boolean isRecovery = false;
	static boolean isHelp = false;
	static String[] fileName = new String[MAXFILENUM];
	
	public static void main(String[] args) throws IOException {
		for(int i=0;i<args.length;i++) {
			String temp = args[i];
			if(temp.equals("-h")) {
				Info.usage();
				break;
			} else if(!temp.startsWith("-")) {
				for(int j=0;i<args.length;i++) {
					fileName[j] = new String(args[i]);
					i++;
				}
			} else if(temp.equals("-f")) {
				i++;
				fromEncoder = args[i];
				continue;
			} else if(temp.equals("-t")) {
				i++;
				toEncoder = args[i];
				continue;
			} else if(temp.equals("-dos2unix")) {
				isDos2Unix = true;
				continue;
			} else if(temp.equals("-unix2dos")) {
				isUnix2Dox = true;
				continue;
			} else if(temp.equals("-b")) {
				isBackup = true;
				continue;
			} else if(temp.equals("-r")) {
				isRecovery = true;
				continue;
			} else if(temp.equals("-h")) {
				isHelp = true;
				break;
			}
		}
		if(isHelp) {
			Info.usage();
			return ;
		}
		for(int i=0;(i<fileName.length&&fileName[i]!=null);i++) {
			System.out.println(fileName[i]);
			File file = new File(fileName[i]);
			if(isBackup) {
				String desFileString = file.getAbsolutePath().toString()+".backup";
				String srcFileString = file.getAbsolutePath().toString();
				//System.out.println(srcFileString);
				//System.out.println(desFileString);
				FileProcess.copyFile(srcFileString, desFileString);
			}
			if(isRecovery) {
				String srcFileString = file.getAbsolutePath().toString()+".backup";
				String desFileString = file.getAbsolutePath().toString();
				//System.out.println(srcFileString);
				//System.out.println(desFileString);
				FileProcess.moveFile(srcFileString, desFileString, true);
				continue ;
			}
			if(isDos2Unix) {
				String temp = file.getAbsolutePath().toString();
				OSPattenConvert.dos2Unix(temp,temp);
			}
			if(isUnix2Dox) {
				String temp = file.getAbsolutePath().toString();
				OSPattenConvert.unix2Dos(temp,temp);
			}
			if(fromEncoder==null && toEncoder!=null) {
				String fromEncoder = JudgeFileCode.judge(file.getAbsolutePath().toString());
				if(fromEncoder!=null) {
					System.out.println("The encoder is:" + fromEncoder);
				} else {
					System.out.println("Something error!");
				}
			}
			if(toEncoder!=null) {
				String temp = file.getAbsolutePath().toString();
				System.out.println(temp);
				EncoderConvert.encodeFile(temp, fromEncoder, temp, toEncoder);
			}
		}
	}

}
