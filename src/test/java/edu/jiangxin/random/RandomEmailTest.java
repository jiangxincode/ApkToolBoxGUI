package edu.jiangxin.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class RandomEmailTest {

	@Test
	public void test() {
		try {
			System.out.println(RandomEmail.nextEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(true);
	}

}
