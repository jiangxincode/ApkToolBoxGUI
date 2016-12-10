package edu.jiangxin.jar;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FindStrInJarTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFind() {
		String path = "target/test-classes/FindStrInJarTest/";

		FindStrInJar findInJar = new FindStrInJar("isChinese"); // 要寻找的字符串
		List<String> jarFiles = findInJar.find(path, true);
		if (jarFiles.size() == 0) {
			System.out.println("Not Found");
		} else {
			for (int i = 0; i < jarFiles.size(); i++) {
				System.out.println(jarFiles.get(i));
			}
		}
	}

}
