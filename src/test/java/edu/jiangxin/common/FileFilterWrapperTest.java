package edu.jiangxin.common;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.jiangxin.apktoolbox.text.core.FileFilterWrapper;

public class FileFilterWrapperTest {
	
	FileFilterWrapper fileFilterWrapper = null;
	ArrayList<File> fileList = null;
	String path = "target/test-classes/FileFilterWrapperTest";
	
	@Before
	public void setUp() {
		fileFilterWrapper = new FileFilterWrapper();
	}
	
	@Test
	public void testList01() {
		fileList = fileFilterWrapper.list(path,".java");
		assertEquals(4, fileList.size());
	}
	
	@Test
	public void testList02() {
		fileList = fileFilterWrapper.list(path, ".cpp");
		assertEquals(2, fileList.size());
	}

}
