package edu.jiangxin.random;

import java.util.Date;

public class Test {

	public static void main(String[] args) throws Exception {
		//String temp = ExtraRandom.nextString(100, "chinese");
		//System.out.println(temp);
		
		// System.out.println(nextChar());
		//System.out.println(nextString(100));
		//System.out.println(RandomChinese.nextRandomLength(12, 60));
		
		Date randomDate = RandomDateTime.nextDate("2016-01-08", "2017-03-01");
		System.out.println(randomDate.toString());
		
		//System.out.println(RandomEmail.nextEmail());
		System.out.println(RandomPhoneNum.nextPhoneNum());
	}

}
