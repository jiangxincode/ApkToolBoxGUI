package edu.jiangxin.encode;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncoderDetectorTest {
	
	String path = "target/test-classes/EncoderDetectorTest/";
	String actual = null;
	@Test
	public void test() {
		actual = EncoderDetector.judgeFile(path+"UTF-8.txt");
		assertEquals("UTF-8", actual);
		actual = EncoderDetector.judgeFile(path+"GB2312.txt");
		assertEquals("GB2312", actual);
	}

}
