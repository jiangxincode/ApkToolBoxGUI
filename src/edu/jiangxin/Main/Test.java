package edu.jiangxin.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import edu.jiangxin.common.*;
import edu.jiangxin.encode.*;

public class Test {

	public static void main(String[] args) throws Exception {
		//String temp = ExtraRandom.nextString(100, "chinese");
		//System.out.println(temp);
		
		// System.out.println(nextChar());
		//System.out.println(nextString(100));
		//System.out.println(RandomChinese.nextRandomLength(12, 60));
		
		//Date randomDate = RandomDateTime.nextDate("2016-01-08", "2017-03-01");
		//System.out.println(randomDate.toString());
		
		//System.out.println(RandomEmail.nextEmail());
		//System.out.println(RandomPhoneNum.nextPhoneNum());
		//dos2Unix("temp/test.txt", "temp/unix.txt");
		//dos2Mac("temp/test.txt", "temp/mac.txt");
		//unix2Dos("temp/unix.txt", "temp/dos.txt");
		//OSPattenConvert.osDirConvert("temp/temp","temp/tempdos","toDoS");
		//OSPattenConvert.osDirConvert("temp/temp","temp/tempunix","dos2unix");
		//OSPattenConvert.osDirConvert("temp/temp","temp/tempmac","dostomAC");
		//OSPattenConvert.osDirConvert("temp/temp","temp/templinux","toLinux");
		
		//Info.showUsage();
		//Info.showLog();
		//Info.showCopyright();
		Info.showFutureFuntions();
		ArrayList<File> mylist = fileFilter.list("E:/temp/java/Test", ".java");
		System.out.println(mylist);
	}

}
