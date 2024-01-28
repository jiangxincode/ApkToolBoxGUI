package edu.jiangxin.apktoolbox.convert.color.colorspace;

import java.awt.*;
import java.awt.color.ColorSpace;

public class HsbColorSpace extends ColorSpace {

    protected HsbColorSpace(int type, int numComponents) {
        super(type, numComponents);
    }

    public static HsbColorSpace getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public float[] toRGB(float[] colorvalue) {
        int rgb = Color.HSBtoRGB(colorvalue[0], colorvalue[1], colorvalue[2]);
        return new float[]{
                (rgb >> 16 & 0xFF) / 255f,
                (rgb >> 8 & 0xFF) / 255f,
                (rgb & 0xFF) / 255f
        };
    }

    @Override
    public float[] fromRGB(float[] rgbvalue) {
        int r = (int) (rgbvalue[0] * 255);
        int g = (int) (rgbvalue[1] * 255);
        int b = (int) (rgbvalue[2] * 255);
        return Color.RGBtoHSB(r, g, b, null);
    }

    @Override
    public float[] toCIEXYZ(float[] colorvalue) {
        float[] rgb = toRGB(colorvalue);
        return ColorSpace.getInstance(CS_sRGB).toCIEXYZ(rgb);
    }

    @Override
    public float[] fromCIEXYZ(float[] colorvalue) {
        float[] rgb = ColorSpace.getInstance(CS_sRGB).fromCIEXYZ(colorvalue);
        return fromRGB(rgb);
    }

    private static class Holder {
        private static final HsbColorSpace INSTANCE = new HsbColorSpace(TYPE_HSV, 3);
    }
}