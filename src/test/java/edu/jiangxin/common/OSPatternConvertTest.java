package edu.jiangxin.common;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class OSPatternConvertTest {

	@Test
	public void test() {
		try {
			OSPatternConvert.dos2Unix("temp/test.txt", "temp/unix.txt");
			OSPatternConvert.dos2Mac("temp/test.txt", "temp/mac.txt");
			OSPatternConvert.unix2Dos("temp/unix.txt", "temp/dos.txt");
			OSPatternConvert.osDirConvert("temp/temp","temp/tempdos","toDoS");
			OSPatternConvert.osDirConvert("temp/temp","temp/tempunix","dos2unix");
			OSPatternConvert.osDirConvert("temp/temp","temp/tempmac","dostomAC");
			OSPatternConvert.osDirConvert("temp/temp","temp/templinux","toLinux");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
		
	}

}
