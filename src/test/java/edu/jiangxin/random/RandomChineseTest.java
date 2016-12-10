package edu.jiangxin.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class RandomChineseTest {

	@Test
	public void test() {
		try {
			System.out.println(RandomChinese.nextChar());
			System.out.println(RandomChinese.nextString(100));
			System.out.println(RandomChinese.nextRandomLength(12, 60));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(true);
	}

}
