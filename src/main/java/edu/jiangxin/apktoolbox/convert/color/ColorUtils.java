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

        float red, green, blue;
        if (hue >= 0 && hue < 1.0 / 6.0) {
            red = c;
            green = x;
            blue = 0;
        } else if (hue >= 1.0 / 6.0 && hue < 2.0 / 6.0) {
            red = x;
            green = c;
            blue = 0;
        } else if (hue >= 2.0 / 6.0 && hue < 3.0 / 6.0) {
            red = 0;
            green = c;
            blue = x;
        } else if (hue >= 3.0 / 6.0 && hue < 4.0 / 6.0) {
            red = 0;
            green = x;
            blue = c;
        } else if (hue >= 4.0 / 6.0 && hue < 5.0 / 6.0) {
            red = x;
            green = 0;
            blue = c;
        } else {
            red = c;
            green = 0;
            blue = x;
        }

        int redI = (int) ((red + m) * 255);
        int greenI = (int) ((green + m) * 255);
        int blueI = (int) ((blue + m) * 255);

        return new Color(redI, greenI, blueI);
    }

    public static double[] colorLab(Color color) {
        double R = color.getRed() / 255.0;
        double G = color.getGreen() / 255.0;
        double B = color.getBlue() / 255.0;

        R = pivotRgbComponent(R);
        G = pivotRgbComponent(G);
        B = pivotRgbComponent(B);

        double X = R * 0.4124564 + G * 0.3575761 + B * 0.1804375;
        double Y = R * 0.2126729 + G * 0.7151522 + B * 0.0721750;
        double Z = R * 0.0193339 + G * 0.1191920 + B * 0.9503041;

        X = X / 0.950470;
        Y = Y / 1.000000;
        Z = Z / 1.088830;

        X = pivotLabComponent(X);
        Y = pivotLabComponent(Y);
        Z = pivotLabComponent(Z);

        double L = Math.max(0, Math.min(116 * Y - 16, 100));
        double a = (X - Y) * 500;
        double bValue = (Y - Z) * 200;

        return new double[] { L, a, bValue };
    }

    public static Color lab2Color(double L, double a, double b) {
        double Y = (L + 16.0) / 116.0;
        double X = a / 500.0 + Y;
        double Z = Y - b / 200.0;

        Y = pivotLabComponent(Y);
        X = pivotLabComponent(X);
        Z = pivotLabComponent(Z);

        double r = X * 3.2404542 + Y * -1.5371385 + Z * -0.4985314;
        double g = X * -0.9692660 + Y * 1.8760108 + Z * 0.0415560;
        double bValue = X * 0.0556434 + Y * -0.2040259 + Z * 1.0572252;

        r = pivotRgbComponent(r);
        g = pivotRgbComponent(g);
        bValue = pivotRgbComponent(bValue);

        int red = (int) Math.round(r * 255);
        int green = (int) Math.round(g * 255);
        int blue = (int) Math.round(bValue * 255);

        return new Color(red, green, blue);
    }

    private static double pivotRgbComponent(double value) {
        if (value <= 0.04045) {
            return value / 12.92;
        } else {
            return Math.pow((value + 0.055) / 1.055, 2.4);
        }
    }

    private static double pivotLabComponent(double value) {
        double threshold = 6.0 / 29.0;
        if (value > threshold) {
            return value * value * value;
        } else {
            return (value - 16.0 / 116.0) / 7.787;
        }
    }
}
