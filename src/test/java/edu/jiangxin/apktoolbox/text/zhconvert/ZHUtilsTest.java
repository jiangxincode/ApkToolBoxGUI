package edu.jiangxin.apktoolbox.text.zhconvert;

import org.junit.Assert;
import org.junit.Test;

public class ZHUtilsTest {

    @Test
    public void testConverter() {

        Assert.assertEquals("有背光的机械式键盘", ZHUtils.convert2Simplified("有背光的機械式鍵盤"));

        Assert.assertEquals("有背光的機械式鍵盤", ZHUtils.convert2Traditional("有背光的机械式键盘"));
    }

    @Test
    public void test01() {
        Assert.assertTrue(ZHUtils.isTraditional("有背光的機械式鍵盤"));
    }
}
