package edu.jiangxin.apktoolbox.convert.color.colorspace;

import java.awt.color.ColorSpace;

public class CielabColorSpace extends ColorSpace {
    private static ColorSpace instance = null;

    private static final long serialVersionUID = 5027741380892134289L;

    private static final ColorSpace CIEXYZ =
            getInstance(CS_CIEXYZ);

    private static final double N = 4.0 / 29.0;

    CielabColorSpace() {
        super(TYPE_Lab, 3);
    }

    public static synchronized ColorSpace getInstance() {
        if (instance == null) {
            instance = new CielabColorSpace();
        }
        return instance;
    }

    @Override
    public float[] fromCIEXYZ(float[] colorvalue) {
        double l = f(colorvalue[1]);
        double L = 116.0 * l - 16.0;
        double a = 500.0 * (f(colorvalue[0]) - l);
        double b = 200.0 * (l - f(colorvalue[2]));
        return new float[] {(float) L, (float) a, (float) b};
    }

    @Override
    public float[] toCIEXYZ(float[] colorvalue) {
        double i = (colorvalue[0] + 16.0) * (1.0 / 116.0);
        double X = fInv(i + colorvalue[1] * (1.0 / 500.0));
        double Y = fInv(i);
        double Z = fInv(i - colorvalue[2] * (1.0 / 200.0));
        return new float[] {(float) X, (float) Y, (float) Z};
    }

    @Override
    public float[] fromRGB(float[] rgbvalue) {
        float[] xyz = CIEXYZ.fromRGB(rgbvalue);
        return fromCIEXYZ(xyz);
    }

    @Override
    public float[] toRGB(float[] colorvalue) {
        float[] xyz = toCIEXYZ(colorvalue);
        return CIEXYZ.toRGB(xyz);
    }

    @Override
    public float getMaxValue(int component) {
        return 128f;
    }

    @Override
    public float getMinValue(int component) {
        return (component == 0)? 0f: -128f;
    }

    @Override
    public String getName(int idx) {
        return String.valueOf("Lab".charAt(idx));
    }

    private static double f(double x) {
        if (x > 216.0 / 24389.0) {
            return Math.cbrt(x);
        } else {
            return (841.0 / 108.0) * x + N;
        }
    }

    private static double fInv(double x) {
        if (x > 6.0 / 29.0) {
            return x*x*x;
        } else {
            return (108.0 / 841.0) * (x - N);
        }
    }

    private Object readResolve() {
        return getInstance();
    }
}