package edu.jiangxin.encode;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import edu.jiangxin.apktoolbox.text.core.EncoderConvert;
import edu.jiangxin.apktoolbox.text.core.EncoderDetector;

public class EncoderConvertTest {

    // 测试文件如果太小可能无法准确判断编码
    // GB2312、GBK、GB18030的关系可以参考: <https://www.zhihu.com/question/19677619>
    String path = EncoderConvertTest.class.getResource("/EncoderConvertTest").getPath();

    @Test
    public void testDetectEncode01() {
        assertTrue(new File(path, "UTF-8.txt").exists());
        assertEquals("UTF-8", EncoderDetector.judgeFile(path + File.separator + "UTF-8.txt"));
    }

    @Test
    public void testDetectEncode02() {
        assertTrue(new File(path, "GB2312.txt").exists());
        String encode = EncoderDetector.judgeFile(path + File.separator + "GB2312.txt");
        assertTrue("GB2312".equals(encode) || "GBK".equals(encode) || "GB18030".equals(encode));
    }

    @Test
    public void testEncodeFile() throws IOException {
        EncoderConvert.encodeFile(path + File.separator + "UTF-8.txt", "UTF-8",
                path + File.separator + "UTF-8_TO_GB2312.txt", "GBK");
        assertTrue(new File(path, "UTF-8_TO_GB2312.txt").exists());
        String encode = EncoderDetector.judgeFile(path + File.separator + "UTF-8_TO_GB2312.txt");
        assertTrue("GB2312".equals(encode) || "GBK".equals(encode) || "GB18030".equals(encode));
    }

    @Test
    public void testEncodeDir() throws IOException {
        File srcDir = new File(path, "test");
        assertTrue(srcDir.exists());
        File[] srcFileList = srcDir.listFiles();
        for (File f : srcFileList) {
            assertEquals("UTF-8",
                    EncoderDetector.judgeFile(path + File.separator + "test" + File.separator + f.getName()));
        }

        EncoderConvert.encodeDir(path + File.separator + "test", "UTF-8", path + File.separator + "testResult",
                "GB2312");
        File desDir = new File(path, "testResult");
        assertTrue(desDir.exists());
        File[] desFileList = desDir.listFiles();
        for (File f : desFileList) {
            String encode = EncoderDetector.judgeFile(desDir.getPath() + File.separator + f.getName());
            assertTrue("GB2312".equals(encode) || "GBK".equals(encode) || "GB18030".equals(encode));
        }
    }

}
