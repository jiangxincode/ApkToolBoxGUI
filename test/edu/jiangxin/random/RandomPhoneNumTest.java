package edu.jiangxin.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class RandomPhoneNumTest {

	@Test
	public void test() {
		try {
			System.out.println(RandomPhoneNum.nextPhoneNum());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}

}
