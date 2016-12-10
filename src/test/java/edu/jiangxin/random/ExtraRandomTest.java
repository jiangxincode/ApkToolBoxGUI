package edu.jiangxin.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExtraRandomTest {

	@Test
	public void testExtraRandom() {
		try {
			System.out.println(RandomString.nextString(100, "chinese"));
			System.out.println(RandomString.nextRandomLengthString(1, 100, "chinese"));
			System.out.println(RandomString.nextString(100, "chinese", null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(true);
	}

}
