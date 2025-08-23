package edu.jiangxin.apktoolbox.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author jiangxin
 * @author 2018-09-09
 *
 */
public class FileUtilsTest {

    Collection<File> fileList = null;
    String dirPath = "target/test-classes/FileUtilsTest";

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
}
