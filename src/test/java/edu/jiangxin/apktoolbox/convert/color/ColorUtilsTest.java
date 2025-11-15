package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.convert.color.colorspace.CielabColorSpace;
import edu.jiangxin.apktoolbox.convert.color.colorspace.CmykColorSpace;
import edu.jiangxin.apktoolbox.convert.color.colorspace.HsbColorSpace;
import edu.jiangxin.apktoolbox.convert.color.colorspace.HslColorSpace;
import edu.jiangxin.apktoolbox.convert.color.utils.ColorUtils;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.color.ColorSpace;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        ColorSpace colorSpace = HsbColorSpace.getInstance();

        float[] expectedRgbArray1 = new Color(194, 175, 120).getRGBColorComponents(null);
        float[] actualRgbArray1 = colorSpace.toRGB(new float[]{0.125f, 0.38f, 0.76f});
        assertArrayEquals(expectedRgbArray1, actualRgbArray1, 0.01f);

        float[] expectedRgbArray2 = new Color(158, 151, 100).getRGBColorComponents(null);
        float[] actualRgbArray2 = colorSpace.toRGB(new float[]{0.147f, 0.37f, 0.62f});
        assertArrayEquals(expectedRgbArray2, actualRgbArray2, 0.01f);
    }

    @Test
    public void testColor2Hsb() {
        ColorSpace colorSpace = HsbColorSpace.getInstance();

        float[] expectedHsbArray1 = new float[]{0.125f, 0.38f, 0.76f};
        float[] rgbArray1 = new Color(194, 175, 120).getRGBColorComponents(null);
        float[] actualHsbArray1 = colorSpace.fromRGB(rgbArray1);
        assertArrayEquals(expectedHsbArray1, actualHsbArray1, 0.01f);

        float[] expectedHsbArray2 = new float[]{0.147f, 0.37f, 0.62f};
        float[] rgbArray2 = new Color(158, 151, 100).getRGBColorComponents(null);
        float[] actualHsbArray2 = colorSpace.fromRGB(rgbArray2);
        assertArrayEquals(expectedHsbArray2, actualHsbArray2, 0.01f);
    }

    @Test
    public void testHsl2Rgb() {
        ColorSpace colorSpace = HslColorSpace.getInstance();

        float[] expectedRgbArray1 = new Color(194, 175, 120).getRGBColorComponents(null);
        float[] actualRgbArray1 = colorSpace.toRGB(new float[]{0.125f, 0.38f, 0.62f});
        assertArrayEquals(expectedRgbArray1, actualRgbArray1, 0.01f);

        float[] expectedRgbArray2 = new Color(158, 151, 100).getRGBColorComponents(null);
        float[] actualRgbArray2 = colorSpace.toRGB(new float[]{0.147f, 0.23f, 0.51f});
        assertArrayEquals(expectedRgbArray2, actualRgbArray2, 0.01f);
    }

    @Test
    public void testRgb2Hsl() {
        ColorSpace colorSpace = HslColorSpace.getInstance();

        float[] expectedHslArray1 = new float[]{0.125f, 0.38f, 0.62f};
        float[] rgbArray1 = new Color(194, 175, 120).getRGBColorComponents(null);
        float[] actualHslArray1 = colorSpace.fromRGB(rgbArray1);
        assertArrayEquals(expectedHslArray1, actualHslArray1, 0.01f);

        float[] expectedHslArray2 = new float[]{0.147f, 0.23f, 0.51f};
        float[] rgbArray2 = new Color(158, 151, 100).getRGBColorComponents(null);
        float[] actualHslArray2 = colorSpace.fromRGB(rgbArray2);
        assertArrayEquals(expectedHslArray2, actualHslArray2, 0.01f);
    }

    @Test
    public void testCmyk2Rgb() {
        ColorSpace colorSpace = CmykColorSpace.getInstance();

        float[] expectedRgbArray1 = new Color(194, 175, 120).getRGBColorComponents(null);
        float[] actualRgbArray1 = colorSpace.toRGB(new float[]{0f, 0.1f, 0.38f, 0.24f});
        assertArrayEquals(expectedRgbArray1, actualRgbArray1, 0.01f);

        float[] expectedRgbArray2 = new Color(158, 151, 100).getRGBColorComponents(null);
        float[] actualRgbArray2 = colorSpace.toRGB(new float[]{0, 0.04f, 0.37f, 0.38f});
        assertArrayEquals(expectedRgbArray2, actualRgbArray2, 0.01f);
    }

    @Test
    public void testRgb2Cmyk() {
        ColorSpace colorSpace = CmykColorSpace.getInstance();

        float[] expectedCmykArray1 = new float[]{0f, 0.1f, 0.38f, 0.24f};
        float[] rgbArray1 = new Color(194, 175, 120).getRGBColorComponents(null);
        float[] actualCmykArray1 = colorSpace.fromRGB(rgbArray1);
        assertArrayEquals(expectedCmykArray1, actualCmykArray1, 0.01f);

        float[] expectedCmykArray2 = new float[]{0, 0.04f, 0.37f, 0.38f};
        float[] rgbArray2 = new Color(158, 151, 100).getRGBColorComponents(null);
        float[] actualCmykArray2 = colorSpace.fromRGB(rgbArray2);
        assertArrayEquals(expectedCmykArray2, actualCmykArray2, 0.01f);
    }

    @Test
    public void testCIELab2Rgb() {
        ColorSpace colorSpace = CielabColorSpace.getInstance();

        float[] expectedRgbArray1 = new Color(194, 175, 120).getRGBColorComponents(null);
        float[] actualRgbArray1 = colorSpace.toRGB(new float[]{72.145f, -3.368f, 38.379f});
        assertArrayEquals(expectedRgbArray1, actualRgbArray1, 0.01f);

        float[] expectedRgbArray2 = new Color(158, 151, 100).getRGBColorComponents(null);
        float[] actualRgbArray2 = colorSpace.toRGB(new float[]{62.140f, -7.192f, 34.513f});
        assertArrayEquals(expectedRgbArray2, actualRgbArray2, 0.01f);
    }

    @Test
    public void testRgb2CIELab() {
        ColorSpace colorSpace = CielabColorSpace.getInstance();

        float[] expectedLabArray1 = new float[]{72.145f, -3.368f, 38.379f};
        float[] rgbArray1 = new Color(194, 175, 120).getRGBColorComponents(null);
        float[] actualLabArray1 = colorSpace.fromRGB(rgbArray1);
        assertArrayEquals(expectedLabArray1, actualLabArray1, 0.01f);

        float[] expectedLabArray2 = new float[]{62.140f, -7.192f, 34.513f};
        float[] rgbArray2 = new Color(158, 151, 100).getRGBColorComponents(null);
        float[] actualLabArray2 = colorSpace.fromRGB(rgbArray2);
        assertArrayEquals(expectedLabArray2, actualLabArray2, 0.01f);
    }
}
