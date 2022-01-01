package edu.jiangxin.apktoolbox.convert.color;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ColorUtilsTest {

    @Test
    public void testHex2Color() {
        assertEquals(new Color(255, 182, 193), ColorUtils.hex2Color("#FFB6C1"));
        assertEquals(new Color(221, 160, 221), ColorUtils.hex2Color("#DDA0DD"));
        assertEquals(new Color(119, 136, 153), ColorUtils.hex2Color("#778899"));

        assertEquals(new Color(255, 182, 193), ColorUtils.hex2Color("0xFFB6C1"));
        assertEquals(new Color(221, 160, 221), ColorUtils.hex2Color("0xDDA0DD"));
        assertEquals(new Color(119, 136, 153), ColorUtils.hex2Color("0x778899"));
    }

    @Test
    public void testColor2Hex() {
        assertEquals(ColorUtils.color2Hex(new Color(255, 182, 193)), "0xFFB6C1");
        assertEquals(ColorUtils.color2Hex(new Color(221, 160, 221)), "0xDDA0DD");
        assertEquals(ColorUtils.color2Hex(new Color(119, 136, 153)), "0x778899");
    }

    @Test
    public void testCmyk2Color() {
        Color rgbColor1 = new Color(240,248,255);
        Color cmykColor1 = ColorUtils.cmyk2Color(0.0588f,0.0275f,0.0000f,0.0000f);
        assertTrue(Math.abs(rgbColor1.getRed() - cmykColor1.getRed()) <= 1);
        assertTrue(Math.abs(rgbColor1.getGreen() - cmykColor1.getGreen()) <= 1);
        assertTrue(Math.abs(rgbColor1.getBlue() - cmykColor1.getBlue()) <= 1);

        Color rgbColor2 = new Color(142,229,238);
        Color cmykColor2 = ColorUtils.cmyk2Color(0.4034f,0.0378f,0.0000f,0.0667f);
        assertTrue(Math.abs(rgbColor2.getRed() - cmykColor2.getRed()) <= 1);
        assertTrue(Math.abs(rgbColor2.getGreen() - cmykColor2.getGreen()) <= 1);
        assertTrue(Math.abs(rgbColor2.getBlue() - cmykColor2.getBlue()) <= 1);

        Color rgbColor3 = new Color(100,149,237);
        Color cmykColor3 = ColorUtils.cmyk2Color(0.5781f,0.3713f,0.0000f,0.0706f);
        assertTrue(Math.abs(rgbColor3.getRed() - cmykColor3.getRed()) <= 1);
        assertTrue(Math.abs(rgbColor3.getGreen() - cmykColor3.getGreen()) <= 1);
        assertTrue(Math.abs(rgbColor3.getBlue() - cmykColor3.getBlue()) <= 1);
    }

    @Test
    public void testColor2Cmyk() {
        float[] cmyk1 = ColorUtils.color2Cmyk(new Color(240,248,255));
        assertTrue(Math.abs(cmyk1[0] - 0.0588f) < 1e-4);
        assertTrue(Math.abs(cmyk1[1] - 0.0275f) < 1e-4);
        assertTrue(Math.abs(cmyk1[2] - 0.0000f) < 1e-4);
        assertTrue(Math.abs(cmyk1[3] - 0.0000f) < 1e-4);

        float[] cmyk2 = ColorUtils.color2Cmyk(new Color(142,229,238));
        assertTrue(Math.abs(cmyk2[0] - 0.4034f) < 1e-4);
        assertTrue(Math.abs(cmyk2[1] - 0.0378f) < 1e-4);
        assertTrue(Math.abs(cmyk2[2] - 0.0000f) < 1e-4);
        assertTrue(Math.abs(cmyk2[3] - 0.0667f) < 1e-4);

        float[] cmyk3 = ColorUtils.color2Cmyk(new Color(100,149,237));
        assertTrue(Math.abs(cmyk3[0] - 0.5781f) < 1e-4);
        assertTrue(Math.abs(cmyk3[1] - 0.3713f) < 1e-4);
        assertTrue(Math.abs(cmyk3[2] - 0.0000f) < 1e-4);
        assertTrue(Math.abs(cmyk3[3] - 0.0706f) < 1e-4);
    }
}
