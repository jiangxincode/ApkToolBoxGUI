package edu.jiangxin.common;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

public class FileFilterWrapperTest {

	@Test
	public void test() {
		ArrayList<File> mylist = FileFilterWrapper.list("E:/temp/java/JavaTest",
				".java");

		System.out.println(mylist);
		assertTrue(true);
	}

}
