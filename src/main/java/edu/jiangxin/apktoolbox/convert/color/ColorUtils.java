package edu.jiangxin.apktoolbox.convert.color;

import java.awt.*;

public class ColorUtils {
    public static Color hex2Color(String hexColor) {
        return Color.decode(hexColor);
    }

    public static String color2Hex(Color color) {
        return String.format("0x%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color cmyk2Color(float cyan, float magenta, float yellow, float key) {
        int red = (int) (255 * (1 - cyan) * (1 - key));
        int green = (int) (255 * (1 - magenta) * (1 - key));
        int blue = (int) (255 * (1 - yellow) * (1 - key));
        return new Color(red, green, blue);
    }

    public static float[] color2Cmyk(Color color) {
        float redInFloat = color.getRed() / 255f;
        float greenInFloat = color.getGreen() / 255f;
        float blueInFloat = color.getBlue() / 255f;
        float key = 1 - Math.max(redInFloat, blueInFloat);
        float cyan = (1 - redInFloat - key) / (1 - key);
        float magenta = (1 - greenInFloat - key) / (1 - key);
        float yellow = (1 - blueInFloat - key) / (1 - key);
        return new float[]{cyan, magenta, yellow, key};
    }
}
