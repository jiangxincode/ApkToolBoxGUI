package edu.jiangxin.random;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class RandomDateTimeTest {

	@Test
	public void test() {
		try {
			Date randomDate = RandomDateTime.nextDate("2016-01-08", "2017-03-01");
			System.out.println(randomDate.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}

}
