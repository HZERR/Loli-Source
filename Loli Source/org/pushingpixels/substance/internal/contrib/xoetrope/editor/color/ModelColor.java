/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.xoetrope.editor.color;

import java.awt.Color;

public class ModelColor {
    public static final int NUM_COLOR_RINGS = 7;
    public static final int NUM_SEGMENTS = 24;
    protected boolean webSnap = false;
    private static ModelColor[][] baseColors;
    protected int R;
    protected int G;
    protected int B;
    protected double H;
    protected double S;
    protected double V;
    protected double rgbMinValue;
    protected double rgbMaxValue;
    public static double[] SATURATION_BANDS;
    public static double[] BRIGHTNESS_BANDS;

    public ModelColor(int R, int G, int B) {
        this(R, G, B, -1);
    }

    public ModelColor(double H, double S, double V) {
        this.setHSV(H, S, V);
    }

    public ModelColor(int R, int G, int B, int H) {
        ModelColor.initColorWheel();
        this.setRGB(R, G, B, H);
    }

    public ModelColor(ModelColor c2) {
        ModelColor.initColorWheel();
        this.R = c2.R;
        this.G = c2.G;
        this.B = c2.B;
        this.H = c2.H;
        this.S = c2.S;
        this.V = c2.V;
        this.getRgbMinMaxValues();
    }

    public int getRed() {
        return this.R;
    }

    public int getGreen() {
        return this.G;
    }

    public int getBlue() {
        return this.B;
    }

    public int getHue() {
        return (int)Math.floor(this.H + 0.5);
    }

    public double getH() {
        return this.H;
    }

    public double getSaturation() {
        return this.S;
    }

    public double getBrightness() {
        return this.V;
    }

    public void setRGB(int R, int G, int B) {
        this.setRGB(R, G, B, -1);
    }

    public void setRGB(int r2, int g2, int b2, int h2) {
        if (this.webSnap) {
            this.R = Math.round(r2 / 51) * 51;
            this.G = Math.round(g2 / 51) * 51;
            this.B = Math.round(b2 / 51) * 51;
        } else {
            this.R = r2;
            this.G = g2;
            this.B = b2;
        }
        this.checkRange();
        this.getRgbMinMaxValues();
        if (h2 > -1) {
            this.H = h2;
            this.S = this.rgbMaxValue > 0.0 ? (this.rgbMaxValue - this.rgbMinValue) / this.rgbMaxValue : 0.0;
            this.V = this.rgbMaxValue / 255.0;
        } else if (this.R == this.G && this.G == this.B) {
            this.H = 0.0;
            this.S = 0.0;
            this.V = this.rgbMaxValue / 255.0;
        } else {
            this.calculateHSVfromRGB();
        }
    }

    public void setHSV(double _h, double _s, double _v) {
        if (Math.abs(this.H - _h) > 0.499999) {
            this.H = _h;
        }
        if (Math.abs(this.S - _s) > 0.009999) {
            this.S = _s;
        }
        if (Math.abs(this.V - _v) > 0.009999) {
            this.V = _v;
        }
        if (this.S > 1.0 || this.S < 0.0) {
            double d2 = this.S = this.S < 0.0 ? 0.0 : 1.0;
        }
        if (this.V > 1.0 || this.V < 0.0) {
            this.V = this.V < 0.0 ? 0.0 : 1.0;
        }
        this.calculateRGBfromHSV();
    }

    private void checkRange() {
        if (this.R > 255) {
            this.R = 255;
        }
        if (this.G > 255) {
            this.G = 255;
        }
        if (this.B > 255) {
            this.B = 255;
        }
        if (this.webSnap) {
            this.R = Math.round(this.R / 51) * 51;
            this.G = Math.round(this.G / 51) * 51;
            this.B = Math.round(this.B / 51) * 51;
        }
    }

    private void getRgbMinMaxValues() {
        this.rgbMaxValue = Math.max(Math.max(this.R, this.G), this.B);
        this.rgbMinValue = Math.min(Math.min(this.R, this.G), this.B);
    }

    private void calculateHSVfromRGB() {
        boolean exactMatch = false;
        block0: for (int j2 = 0; j2 < 7; ++j2) {
            if (j2 == 6) {
                j2 = 6;
            }
            for (int i2 = 0; i2 < 360; i2 += 15) {
                ModelColor c2 = baseColors[i2 / 15][j2];
                if (this.R != c2.R || this.G != c2.G || this.B != c2.B) continue;
                exactMatch = true;
                this.H = c2.H;
                this.S = this.rgbMaxValue > 0.0 ? 1.0 * (this.rgbMaxValue - this.rgbMinValue) / this.rgbMaxValue : 0.0;
                this.V = this.rgbMaxValue / 255.0;
                break block0;
            }
        }
        if (!exactMatch) {
            float[] values = new float[3];
            values = Color.RGBtoHSB(this.R, this.G, this.B, values);
            float h2 = values[0];
            this.S = this.rgbMaxValue > 0.0 ? 1.0 * (this.rgbMaxValue - this.rgbMinValue) / this.rgbMaxValue : 0.0;
            this.V = this.rgbMaxValue / 255.0;
            if (h2 < 0.1666668f) {
                h2 *= 2.0f;
            } else if (h2 < 0.3333334f) {
                h2 += 0.1666667f;
            } else if (h2 < 0.6666668f) {
                h2 += 0.1666667f * ((0.6666667f - h2) / 0.3333334f);
            }
            this.H = 360.0 * (double)h2;
        }
    }

    private void calculateRGBfromHSV() {
        double h2 = this.H / 360.0;
        if (h2 < 0.3333334) {
            h2 /= 2.0;
        } else if (h2 < 0.5000001) {
            h2 -= 0.1666667;
        } else if (h2 < 0.6666668) {
            h2 = 2.0 * h2 - 0.6666667;
        }
        Color c2 = new Color(Color.HSBtoRGB((float)h2, (float)this.S, (float)this.V));
        this.R = c2.getRed();
        this.G = c2.getGreen();
        this.B = c2.getBlue();
        this.checkRange();
        this.getRgbMinMaxValues();
    }

    public void rotate(int angle) {
        if (this.S > 0.0 && this.V > 0.0) {
            double newHue = (this.H + (double)angle) % 360.0;
            this.setHSV(newHue, this.S, this.V);
        }
    }

    public void setWebSnap(boolean snapTo) {
        this.webSnap = snapTo;
    }

    public static String dec2hex(int n2) {
        String s2 = Integer.toHexString(n2);
        if (s2.length() < 2) {
            s2 = "0" + s2;
        }
        return s2.toUpperCase();
    }

    public static int hex2dec(String n2) {
        if (n2.length() == 0) {
            return 0;
        }
        return Integer.parseInt(n2, 16);
    }

    public static String toHexString(int value) {
        String hex = Integer.toHexString(value).toUpperCase();
        if (hex.length() < 2) {
            return "0" + hex;
        }
        return hex;
    }

    public static String col2Gray(double r2, double g2, double b2) {
        double lum = Math.round(r2 * 0.299 + g2 * 0.587 + b2 * 0.114);
        return ModelColor.dec2hex((int)lum) + ModelColor.dec2hex((int)lum) + ModelColor.dec2hex((int)lum);
    }

    public static ModelColor[][] getBaseColors() {
        ModelColor.initColorWheel();
        return baseColors;
    }

    private static void initColorWheel() {
        if (baseColors == null) {
            baseColors = new ModelColor[24][7];
            int idx = 0;
            for (int i2 = 0; i2 < 7; ++i2) {
                for (int j2 = 0; j2 < 24; ++j2) {
                    ModelColor.baseColors[j2][i2] = new ModelColor(15.0 * (double)j2, SATURATION_BANDS[i2], BRIGHTNESS_BANDS[i2 + 1 - j2 % 2]);
                    ++idx;
                }
            }
        }
    }

    static {
        SATURATION_BANDS = new double[]{0.4, 0.6, 0.8, 1.0, 1.0, 1.0, 1.0};
        BRIGHTNESS_BANDS = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 0.85, 0.65, 0.45};
    }
}

