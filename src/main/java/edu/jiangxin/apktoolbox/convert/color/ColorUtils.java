package edu.jiangxin.apktoolbox.convert.color;

import java.awt.*;
import java.text.DecimalFormat;

public class ColorUtils {
    public static Color hex2Color(String hexColor) {
        return Color.decode(hexColor);
    }

    public static String color2Hex(Color color) {
        return String.format("0x%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Convert HSB component to Color Object
     * @param hue from 0 to 360
     * @param saturation from 0 to 100
     * @param brightness from 0 to 100
     */
    public static Color hsb2Color(int hue, int saturation, int brightness) {
        return Color.getHSBColor(hue / 360f, saturation / 100f, brightness / 100f);
    }

    public static int[] color2Hsb(Color color) {
        float[] hsbVal = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int hue = Math.round(hsbVal[0] * 360);
        int saturation = Math.round(hsbVal[1] * 100);
        int brightness = Math.round(hsbVal[2] * 100);
        return new int[]{hue, saturation, brightness};
    }

    public static Color cmyk2Color(int cyan, int magenta, int yellow, int key) {
        int red = Math.round(255 * (1 - cyan / 100f) * (1 - key / 100f));
        int green = Math.round(255 * (1 - magenta / 100f) * (1 - key / 100f));
        int blue = Math.round(255 * (1 - yellow / 100f) * (1 - key / 100f));
        return new Color(red, green, blue);
    }

    public static int[] color2Cmyk(Color color) {
        float redInFloat = color.getRed() / 255f;
        float greenInFloat = color.getGreen() / 255f;
        float blueInFloat = color.getBlue() / 255f;
        float max = Math.max(Math.max(redInFloat, greenInFloat), blueInFloat);
        float key = 1 - max;
        float cyan = (1 - redInFloat - key) / (1 - key);
        float magenta = (1 - greenInFloat - key) / (1 - key);
        float yellow = (1 - blueInFloat - key) / (1 - key);
        return new int[]{Math.round(cyan * 100), Math.round(magenta * 100), Math.round(yellow * 100), Math.round(key * 100)};
    }
}
