package edu.jiangxin.common;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class InfoTest {

	@Test
	public void test() {
		try {
			Info.showUsage();
			Info.showLog();
			Info.showCopyright();
			Info.showFutureFuntions();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(true);
		
	}

}
