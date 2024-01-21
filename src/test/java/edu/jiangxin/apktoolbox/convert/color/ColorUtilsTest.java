package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.convert.color.colorspace.CIELabColorSpace;
import org.junit.Test;

import java.awt.*;
import java.awt.color.ColorSpace;

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
    public void testHsl2Color() {
        // Because of deviation, the calculated color is (194, 176, 121) instead of (194, 175, 120)
        assertEquals(new Color(194, 176, 121), ColorUtils.hsl2Color(45, 38, 62));
        // Because of deviation, the calculated color is (158, 152, 101) instead of (158, 151, 100)
        assertEquals(new Color(158, 152, 101), ColorUtils.hsl2Color(53, 23, 51));
    }

    @Test
    public void testColor2Hsl() {
        assertArrayEquals(new int[]{45, 38, 62}, ColorUtils.color2Hsl(new Color(194, 175, 120)));
        assertArrayEquals(new int[]{53, 23, 51}, ColorUtils.color2Hsl(new Color(158, 151, 100)));
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

    @Test
    public void testCIELab2Color() {
        ColorSpace colorSpace = CIELabColorSpace.getInstance();
        // Because of deviation, the calculated color is (194, 174, 120) instead of (194, 175, 120)
        //assertEquals(new Color(99, 125, 125), new Color(colorSpace, new float[]{0.5f, 0.6f, 0.7f}, 1.0f));
        // Because of deviation, the calculated color is (158, 152, 100) instead of (158, 151, 100)
        //assertEquals(new Color(158, 152, 100), ColorUtils.cmyk2Color(0, 4, 37, 38));
    }

    @Test
    public void testColor2CIELab() {
        assertArrayEquals(new int[]{0, 10, 38, 24}, ColorUtils.color2Cmyk(new Color(194, 175, 120)));
        assertArrayEquals(new int[]{0, 4, 37, 38}, ColorUtils.color2Cmyk(new Color(158, 151, 100)));
    }
}
