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
     * @param hueI from 0 to 360
     * @param saturationI from 0 to 100
     * @param brightnessI from 0 to 100
     */
    public static Color hsb2Color(int hueI, int saturationI, int brightnessI) {
        float hue = hueI / 360f;
        float saturation = saturationI / 100f;
        float brightness = brightnessI / 100f;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public static int[] color2Hsb(Color color) {
        float[] hsbVal = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int hueI = Math.round(hsbVal[0] * 360);
        int saturationI = Math.round(hsbVal[1] * 100);
        int brightnessI = Math.round(hsbVal[2] * 100);
        return new int[]{hueI, saturationI, brightnessI};
    }

    public static Color cmyk2Color(int cyanI, int magentaI, int yellowI, int keyI) {
        float cyan = cyanI / 100f;
        float magenta = magentaI / 100f;
        float yellow = yellowI / 100f;
        float key = keyI / 100f;
        int redI = Math.round(255 * (1 - cyan) * (1 - key));
        int greenI = Math.round(255 * (1 - magenta) * (1 - key));
        int blueI = Math.round(255 * (1 - yellow) * (1 - key));
        return new Color(redI, greenI, blueI);
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

    public static int[] color2Hsl(Color color) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;


        float max = Math.max(Math.max(red, green), blue);
        float min = Math.min(Math.min(red, green), blue);

        float hue = (max + min) / 2;
        float saturation;
        float lightness = hue;

        if (max == min) {
            hue = saturation = 0;
        } else {
            float delta = max - min;
            saturation = lightness > 0.5 ? delta / (2 - max - min) : delta / (max + min);

            if (max == red) {
                hue = (green - blue) / delta + (green < blue ? 6 : 0);
            } else if (max == green) {
                hue = (blue - red) / delta + 2;
            } else if (max == blue) {
                hue = (red - green) / delta + 4;
            }

            hue /= 6;
        }

        int hueI = Math.round(hue * 360);
        int saturationI = Math.round(saturation * 100);
        int lightnessI = Math.round(lightness * 100);
        return new int[]{hueI, saturationI, lightnessI};
    }

    public static Color hsl2Color(int hueI, int saturationI, int lightnessI) {
        float hue = hueI / 360f;
        float saturation = saturationI / 100f;
        float lightness = lightnessI / 100f;

        float c = (1 - Math.abs(2 * lightness - 1)) * saturation;
        float x = c * (1 - Math.abs((hue * 6) % 2 - 1));
        float m = lightness - c / 2;

        float r, g, b;
        if (hue >= 0 && hue < 1.0 / 6.0) {
            r = c;
            g = x;
            b = 0;
        } else if (hue >= 1.0 / 6.0 && hue < 2.0 / 6.0) {
            r = x;
            g = c;
            b = 0;
        } else if (hue >= 2.0 / 6.0 && hue < 3.0 / 6.0) {
            r = 0;
            g = c;
            b = x;
        } else if (hue >= 3.0 / 6.0 && hue < 4.0 / 6.0) {
            r = 0;
            g = x;
            b = c;
        } else if (hue >= 4.0 / 6.0 && hue < 5.0 / 6.0) {
            r = x;
            g = 0;
            b = c;
        } else {
            r = c;
            g = 0;
            b = x;
        }

        int redI = (int) ((r + m) * 255);
        int greenI = (int) ((g + m) * 255);
        int blueI = (int) ((b + m) * 255);

        return new Color(redI, greenI, blueI);
    }
}
