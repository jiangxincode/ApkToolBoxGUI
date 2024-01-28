package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.convert.color.colorspace.CielabColorSpace;
import edu.jiangxin.apktoolbox.convert.color.colorspace.CmykColorSpace;
import edu.jiangxin.apktoolbox.convert.color.colorspace.HsbColorSpace;
import edu.jiangxin.apktoolbox.convert.color.colorspace.HslColorSpace;

import java.awt.*;
import java.awt.color.ColorSpace;

public class ColorUtils {
    public static Color hex2Color(String hexColor) {
        return Color.decode(hexColor);
    }

    public static String color2Hex(Color color) {
        return String.format("0x%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color hsb2Color(int hueI, int saturationI, int brightnessI) {
        float[] hsbVal = hsbInt2Float(new int[]{hueI, saturationI, brightnessI});
        ColorSpace colorSpace = HsbColorSpace.getInstance();
        float[] rgbVal = colorSpace.toRGB(hsbVal);
        return new Color(rgbVal[0], rgbVal[1], rgbVal[2]);
    }

    public static int[] color2Hsb(Color color) {
        float[] rgbVal = color.getRGBColorComponents(null);
        ColorSpace colorSpace = HsbColorSpace.getInstance();
        float[] hsbVal = colorSpace.fromRGB(rgbVal);
        return hsbFloat2Int(hsbVal);
    }

    public static Color hsl2Color(int hueI, int saturationI, int lightnessI) {
        float[] hslVal = hslInt2Float(new int[]{hueI, saturationI, lightnessI});
        ColorSpace colorSpace = HslColorSpace.getInstance();
        float[] rgbVal = colorSpace.toRGB(hslVal);
        return new Color(rgbVal[0], rgbVal[1], rgbVal[2]);
    }

    public static int[] color2Hsl(Color color) {
        float[] rgbVal = color.getRGBColorComponents(null);
        ColorSpace colorSpace = HslColorSpace.getInstance();
        float[] hslVal = colorSpace.fromRGB(rgbVal);
        return hslFloat2Int(hslVal);
    }

    public static Color cmyk2Color(int cyanI, int magentaI, int yellowI, int keyI) {
        float[] cmykVal = cmykInt2Float(new int[]{cyanI, magentaI, yellowI, keyI});
        ColorSpace colorSpace = CmykColorSpace.getInstance();
        float[] rgbVal = colorSpace.toRGB(cmykVal);
        return new Color(rgbVal[0], rgbVal[1], rgbVal[2]);
    }

    public static int[] color2Cmyk(Color color) {
        float[] rgbVal = color.getRGBColorComponents(null);
        ColorSpace colorSpace = CmykColorSpace.getInstance();
        float[] cmykVal = colorSpace.fromRGB(rgbVal);
        return cmykFloat2Int(cmykVal);
    }

    public static Color cielab2Color(int L, int a, int b) {
        float[] cieLabVal = cielabInt2Float(new int[]{L, a, b});
        ColorSpace colorSpace = CielabColorSpace.getInstance();
        float[] rgbVal = colorSpace.toRGB(cieLabVal);
        return new Color(rgbVal[0], rgbVal[1], rgbVal[2]);
    }

    public static int[] color2Cielab(Color color) {
        float[] rgbVal = color.getRGBColorComponents(null);
        ColorSpace colorSpace = CielabColorSpace.getInstance();
        float[] cieLabVal = colorSpace.fromRGB(rgbVal);
        return cielabFloat2Int(cieLabVal);
    }

    public static float[] rgbInt2Float(int[] value) {
        float red = value[0] / 255f;
        float green = value[1] / 255f;
        float blue = value[2] / 255f;
        return new float[]{red, green, blue};
    }

    public static int[] rgbFloat2Int(float[] value) {
        int red = (int) (value[0] * 255);
        int green = (int) (value[1] * 255);
        int blue = (int) (value[2] * 255);
        return new int[]{red, green, blue};
    }

    public static int[] hsbFloat2Int(float[] value) {
        int hue = (int) (value[0] * 360);
        int saturation = (int) (value[1] * 100);
        int brightness = (int) (value[2] * 100);
        return new int[]{hue, saturation, brightness};
    }

    public static float[] hsbInt2Float(int[] value) {
        float hue = value[0] / 360f;
        float saturation = value[1] / 100f;
        float brightness = value[2] / 100f;
        return new float[]{hue, saturation, brightness};
    }

    public static int[] hslFloat2Int(float[] value) {
        int hue = (int) (value[0] * 360);
        int saturation = (int) (value[1] * 100);
        int lightness = (int) (value[2] * 100);
        return new int[]{hue, saturation, lightness};
    }

    public static float[] hslInt2Float(int[] value) {
        float hue = value[0] / 360f;
        float saturation = value[1] / 100f;
        float lightness = value[2] / 100f;
        return new float[]{hue, saturation, lightness};
    }


    public static int[] cmykFloat2Int(float[] value) {
        int cyan = (int) (value[0] * 100);
        int magenta = (int) (value[1] * 100);
        int yellow = (int) (value[2] * 100);
        int key = (int) (value[3] * 100);
        return new int[]{cyan, magenta, yellow, key};
    }

    public static float[] cmykInt2Float(int[] value) {
        float cyan = value[0] / 100f;
        float magenta = value[1] / 100f;
        float yellow = value[2] / 100f;
        float key = value[3] / 100f;
        return new float[]{cyan, magenta, yellow, key};
    }


    public static int[] cielabFloat2Int(float[] value) {
        int L = (int) value[0];
        int a = (int) value[1];
        int b = (int) value[2];
        return new int[]{L, a, b};
    }

    public static float[] cielabInt2Float(int[] value) {
        float L = (float) value[0];
        float a = (float) value[1];
        float b = (float) value[2];
        return new float[]{L, a, b};
    }
}
