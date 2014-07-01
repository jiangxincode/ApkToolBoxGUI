/**
 * –≈œ¢œ‘ æ
 * @author jiangxin
 */
package edu.jiangxin.encode;

public class Info {
	public static void usage() {
		//System.err.println("Usage: convert [[-f] -t] [-dos2unix|-unix2dos] [-b|-r] filename1 [filename2...]");
		System.err.println("Usage: convert [option] filename1 [filename2...]");
		
		System.err.println("\t -f The encoder of your file.If you don't know the econder,we will try to detect auto.However we can't ensure the validity!");
		System.err.println("\t -t The encoder you want to convert");
		
		System.err.println("\t -dos2unix Convert your file from Dos to Unix.Can't use with -unix2dos!");
		System.err.println("\t -unix2dos Convert your file from Unix to Dos.Can't use with -dos2unix!");
		
		System.err.println("\t -b Backup your file when Convert.It's recommended!");
		System.err.println("\t -r Recovey your file..Can't use with other options!");
		
		System.err.println("\t -h Display this usage.");
		
		System.err.println("\t filename1[,filename2...] The file that you want to convert,at most one file.");
		
		System.err.println("For example:");
		System.err.println("\t convert -f GBK -t UTF-8 test.txt");
		System.err.println("\t convert -dos2unix test.txt");
		System.err.println("\t convert -r test.txt");
	}
	
	public static void main(String[] args) {
		Info.usage();
	}

}
