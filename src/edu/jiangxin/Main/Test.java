package edu.jiangxin.Main;

import java.io.File;
import java.util.ArrayList;
import edu.jiangxin.common.*;

public class Test {

	public static void main(String[] args) throws Exception {

		OSPattenConvert.dos2Unix("temp/test.txt", "temp/unix.txt");
		OSPattenConvert.dos2Mac("temp/test.txt", "temp/mac.txt");
		OSPattenConvert.unix2Dos("temp/unix.txt", "temp/dos.txt");
		OSPattenConvert.osDirConvert("temp/temp","temp/tempdos","toDoS");
		OSPattenConvert.osDirConvert("temp/temp","temp/tempunix","dos2unix");
		OSPattenConvert.osDirConvert("temp/temp","temp/tempmac","dostomAC");
		OSPattenConvert.osDirConvert("temp/temp","temp/templinux","toLinux");
		
		
		ArrayList<File> mylist = fileFilter.list("E:/temp/java/Test", ".java");
		
		System.out.println(mylist);
	}

}
