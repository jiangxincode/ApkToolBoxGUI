package edu.jiangxin.encode;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class EncoderConvertTest {

	@Test
	public void test() {
		try {
			EncoderConvert.encodeFile("temp/test1.txt", "gbk", "temp/test2.txt","UTF-8");
			EncoderConvert.encodeDir("temp/temp", "gbk", "temp/temp2","UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}

}
