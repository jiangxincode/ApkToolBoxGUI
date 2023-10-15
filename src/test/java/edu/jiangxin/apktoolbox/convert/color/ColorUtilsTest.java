package edu.jiangxin.apktoolbox.convert.color;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ColorUtilsTest {
    @Test
    public void testHex2Color() {
        assertEquals(new Color(194, 175, 120), ColorUtils.hex2Color("0xC2AF78"));
        assertEquals(new Color(158, 151, 100), ColorUtils.hex2Color("0x9E9764"));
    }

    @Test
    public void testColor2Hex() {
        assertEquals("0xC2AF78", ColorUtils.color2Hex(new Color(194, 175, 120)));
        assertEquals("0x9E9764", ColorUtils.color2Hex(new Color(158, 151, 100)));
    }

    @Test
    public void testHsb2Color() {
        assertEquals(new Color(194, 175, 120), ColorUtils.hsb2Color(45, 38, 76));
        assertEquals(new Color(158, 151, 100), ColorUtils.hsb2Color(53, 37, 62));
    }

    @Test
    public void testColor2Hsb() {
        assertArrayEquals(new int[]{45, 38, 76}, ColorUtils.color2Hsb(new Color(194, 175, 120)));
        assertArrayEquals(new int[]{53, 37, 62}, ColorUtils.color2Hsb(new Color(158, 151, 100)));
    }

    @Test
    public void testCmyk2Color() {
        // Because of deviation, the calculated color is (194, 174, 120) instead of (194, 175, 120)
        assertEquals(new Color(194, 174, 120), ColorUtils.cmyk2Color(0, 10, 38, 24));
        // Because of deviation, the calculated color is (158, 152, 100) instead of (158, 151, 100)
        assertEquals(new Color(158, 152, 100), ColorUtils.cmyk2Color(0, 4, 37, 38));
    }

    @Test
    public void testColor2Cmyk() {
        assertArrayEquals(new int[]{0, 10, 38, 24}, ColorUtils.color2Cmyk(new Color(194, 175, 120)));
        assertArrayEquals(new int[]{0, 4, 37, 38}, ColorUtils.color2Cmyk(new Color(158, 151, 100)));
    }
}
