package edu.jiangxin.common;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import edu.jiangxin.apktoolbox.text.core.OsPatternConvert;

/**
 * @author jiangxin
 * @author 2018-09-09
 *
 */
public class OsPatternConvertTest {

    String path = "target/test-classes/OSPatternConvertTest/";

    @Test
    public void testXxx2Xxx() {
        try {
            assertTrue(new File(path + "test.txt").exists());
            OsPatternConvert.dos2Unix(path + "test.txt", path + "unix.txt");
            assertTrue(new File(path + "unix.txt").exists());
            OsPatternConvert.dos2Mac(path + "test.txt", path + "mac.txt");
            assertTrue(new File(path + "mac.txt").exists());
            OsPatternConvert.unix2Dos(path + "unix.txt", path + "dos.txt");
            assertTrue(new File(path + "dos.txt").exists());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testOsDirConvert() {
        try {
            assertTrue(new File(path + "test").exists());
            OsPatternConvert.osDirConvert(path + "test", path + "dos", "toDoS");
            assertTrue(new File(path + "dos").exists());
            OsPatternConvert.osDirConvert(path + "test", path + "unix", "dos2unix");
            assertTrue(new File(path + "unix").exists());
            OsPatternConvert.osDirConvert(path + "test", path + "mac", "dostomAC");
            assertTrue(new File(path + "mac").exists());
            OsPatternConvert.osDirConvert(path + "test", path + "linux", "toLinux");
            assertTrue(new File(path + "linux").exists());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
