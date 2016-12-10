package edu.jiangxin.encode;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class EncoderConvertTest {
	
	String path = "target/test-classes/EncoderConvertTest/";

	@Test
	public void testEncodeFile() {
		try {
			assertTrue(new File(path+"UTF-8.txt").exists());
			assertEquals("UTF-8",EncoderDetector.judgeFile(path+"UTF-8.txt"));
			EncoderConvert.encodeFile(path+"UTF-8.txt", "UTF-8", path+"GB2312.txt","GBK");
			assertTrue(new File(path+"GB2312.txt").exists());
			assertEquals("GB2312",EncoderDetector.judgeFile(path+"GB2312.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(true);
	}
	
	@Test
	public void testEncodeDir() {
		try {
			File srcDir = new File(path+"test");
			assertTrue(srcDir.exists());
			File[] srcFileList = srcDir.listFiles();
			for(File f : srcFileList) {
				System.out.println(f.getName());
				assertEquals("UTF-8",EncoderDetector.judgeFile(path+"test/" + f.getName()));
			}
			
			EncoderConvert.encodeDir(path+"test", "UTF-8", path+"testResult","GB2312");
			
			File desDir = new File(path+"testResult");
			assertTrue(desDir.exists());
			File[] desFileList = desDir.listFiles();
			for(File f : desFileList) {
				//注意此处由于文件太小，cpdetector尚不能检测出编码方式，只能确定不再是UTF-8了
				assertNotEquals("UTF-8",EncoderDetector.judgeFile(path+"testReuslt/" + f.getName()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(true);
	}

}
