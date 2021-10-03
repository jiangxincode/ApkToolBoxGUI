package edu.jiangxin.common;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.jiangxin.apktoolbox.text.core.FileFilterWrapper;

/**
 * @author jiangxin
 * @author 2018-09-09
 *
 */
public class FileFilterWrapperTest {

    FileFilterWrapper fileFilterWrapper = null;
    List<File> fileList = null;
    String path = "target/test-classes/FileFilterWrapperTest";

    @Before
    public void setUp() {
        fileFilterWrapper = new FileFilterWrapper();
    }

    @Test
    public void testList01() {
        fileList = FileFilterWrapper.list(new File(path), new String[]{"java"}, true);
        assertEquals(4, fileList.size());
    }

    @Test
    public void testList02() {
        fileList = FileFilterWrapper.list(new File(path), new String[]{"cpp"}, true);
        assertEquals(2, fileList.size());
    }

}
