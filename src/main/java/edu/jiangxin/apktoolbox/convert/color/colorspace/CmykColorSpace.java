package edu.jiangxin.apktoolbox.convert.color.colorspace;

import java.awt.color.ColorSpace;
import java.io.Serial;

public class CmykColorSpace extends ColorSpace {
    @Serial
    private static final long serialVersionUID = 1L;

    protected CmykColorSpace(int type, int numComponents) {
        super(type, numComponents);
    }

    public static CmykColorSpace getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public float[] toRGB(float[] colorvalue) {
        float c = colorvalue[0];
        float m = colorvalue[1];
        float y = colorvalue[2];
        float k = colorvalue[3];

        float r = 1 - Math.min(1, c * (1 - k) + k);
        float g = 1 - Math.min(1, m * (1 - k) + k);
        float b = 1 - Math.min(1, y * (1 - k) + k);

        return new float[]{r, g, b};
    }

    @Override
    public float[] fromRGB(float[] rgbvalue) {
        float r = rgbvalue[0];
        float g = rgbvalue[1];
        float b = rgbvalue[2];

        float k = 1 - Math.max(r, Math.max(g, b));
        float c = (1 - r - k) / (1 - k);
        float m = (1 - g - k) / (1 - k);
        float y = (1 - b - k) / (1 - k);

        return new float[]{c, m, y, k};
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
        private static final CmykColorSpace INSTANCE = new CmykColorSpace(TYPE_CMYK, 4);
    }
}