package edu.jiangxin.apktoolbox.convert.color.colorspace;

import java.awt.color.ColorSpace;

public class HslColorSpace extends ColorSpace {

    protected HslColorSpace(int type, int numComponents) {
        super(type, numComponents);
    }

    public static HslColorSpace getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public float[] toRGB(float[] colorvalue) {
        float h = colorvalue[0];
        float s = colorvalue[1];
        float l = colorvalue[2];

        float r, g, b;

        if (s == 0f) {
            r = g = b = l;
        } else {
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRgb(p, q, h + 1/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1/3f);
        }

        return new float[]{r, g, b};
    }

    @Override
    public float[] fromRGB(float[] rgbvalue) {
        float r = rgbvalue[0];
        float g = rgbvalue[1];
        float b = rgbvalue[2];

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));

        float h, s;
        float l = (max + min) / 2;

        if (max == min) {
            h = s = 0f;
        } else {
            float delta = max - min;
            s = l > 0.5f ? delta / (2 - max - min) : delta / (max + min);
            if (max == r) {
                h = (g - b) / delta + (g < b ? 6f : 0f);
            } else if (max == g) {
                h = (b - r) / delta + 2f;
            } else {
                h = (r - g) / delta + 4f;
            }
            h /= 6f;
        }

        return new float[]{h, s, l};
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

    private float hueToRgb(float p, float q, float t) {
        if (t < 0f) t += 1f;
        if (t > 1f) t -= 1f;
        if (t < 1/6f) return p + (q - p) * 6f * t;
        if (t < 1/2f) return q;
        if (t < 2/3f) return p + (q - p) * (2/3f - t) * 6f;
        return p;
    }

    private static class Holder {
        private static final HslColorSpace INSTANCE = new HslColorSpace(TYPE_HLS, 3);
    }
}