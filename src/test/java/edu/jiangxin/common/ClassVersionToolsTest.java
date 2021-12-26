package edu.jiangxin.common;

import org.junit.Before;
import org.junit.Test;

import edu.jiangxin.apktoolbox.file.core.ClassVersionTools;

/**
 * @author jiangxin
 * @author 2018-09-09
 *
 */
public class ClassVersionToolsTest {

    String path = "target/test-classes/ClassVersionTest/";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetClassVersion() {
        ClassVersionTools.getClassVersion(path + "TextTools-0.0.1-SNAPSHOT");
    }

    @Test
    public void testModifyClassVersion() {
        ClassVersionTools.modifyClassVersion(path + "TextTools-0.0.1-SNAPSHOT", path + "TextTools-0.0.1-SNAPSHOT-bak",
                new byte[] { 0x00, 0x03, 0x00, 0x2d });
        ClassVersionTools.getClassVersion(path + "TextTools-0.0.1-SNAPSHOT-bak");
    }

}
