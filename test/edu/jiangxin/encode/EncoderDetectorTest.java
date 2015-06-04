package edu.jiangxin.encode;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncoderDetectorTest {

	@Test
	public void test() {
		String charset = EncoderDetector.judgeFile("temp/EncoderDetector/utf8.txt");
		System.out.println(charset);
		assertTrue(true);
	}

}
