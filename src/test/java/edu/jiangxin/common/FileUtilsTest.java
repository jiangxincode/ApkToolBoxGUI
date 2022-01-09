package edu.jiangxin.common;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author jiangxin
 * @author 2018-09-09
 *
 */
public class FileUtilsTest {

    Collection<File> fileList = null;
    String dirPath = "target/test-classes/FileUtilsTest";
    String filePath = "target/test-classes/FileUtilsTest/Test01.cpp";

    @Test
    public void testListFiles01() {
        fileList = FileUtils.listFiles(new File(dirPath), new String[]{"java"}, true);
        assertEquals(4, fileList.size());
    }

    @Test
    public void tesListFiles02() {
        fileList = FileUtils.listFiles(new File(dirPath), new String[]{"cpp"}, true);
        assertEquals(2, fileList.size());
    }

    @Test
    public void tesListFiles03() {
        fileList = FileUtils.listFiles(new File(filePath), new String[]{"cpp"}, true);
        assertEquals(1, fileList.size());
    }

}
