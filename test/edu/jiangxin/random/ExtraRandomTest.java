package edu.jiangxin.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExtraRandomTest {

	@Test
	public void testExtraRandom() {
		try {
			System.out.println(ExtraRandom.nextString(100, "chinese"));
			System.out.println(ExtraRandom.nextRandomLengthString(1, 100, "chinese"));
			System.out.println(ExtraRandom.nextString(100, "chinese", null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}

}
