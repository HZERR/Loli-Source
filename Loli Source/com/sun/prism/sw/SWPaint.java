/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.pisces.PiscesRenderer;
import com.sun.pisces.Transform6;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWContext;
import com.sun.prism.sw.SWUtils;

final class SWPaint {
    private final SWContext context;
    private final PiscesRenderer pr;
    private final BaseTransform paintTx = new Affine2D();
    private final Transform6 piscesTx = new Transform6();
    private float compositeAlpha = 1.0f;
    private float px;
    private float py;
    private float pw;
    private float ph;

    SWPaint(SWContext sWContext, PiscesRenderer piscesRenderer) {
        this.context = sWContext;
        this.pr = piscesRenderer;
    }

    float getCompositeAlpha() {
        return this.compositeAlpha;
    }

    void setCompositeAlpha(float f2) {
        this.compositeAlpha = f2;
    }

    void setColor(Color color, float f2) {
        if (PrismSettings.debug) {
            System.out.println("PR.setColor: " + color);
        }
        this.pr.setColor((int)(color.getRed() * 255.0f), (int)(255.0f * color.getGreen()), (int)(255.0f * color.getBlue()), (int)(255.0f * color.getAlpha() * f2));
    }

    void setPaintFromShape(Paint paint, BaseTransform baseTransform, Shape shape, RectBounds rectBounds, float f2, float f3, float f4, float f5) {
        this.computePaintBounds(paint, shape, rectBounds, f2, f3, f4, f5);
        this.setPaintBeforeDraw(paint, baseTransform, this.px, this.py, this.pw, this.ph);
    }

    private void computePaintBounds(Paint paint, Shape shape, RectBounds rectBounds, float f2, float f3, float f4, float f5) {
        if (paint.isProportional()) {
            if (rectBounds != null) {
                this.px = rectBounds.getMinX();
                this.py = rectBounds.getMinY();
                this.pw = rectBounds.getWidth();
                this.ph = rectBounds.getHeight();
            } else if (shape != null) {
                RectBounds rectBounds2 = shape.getBounds();
                this.px = rectBounds2.getMinX();
                this.py = rectBounds2.getMinY();
                this.pw = rectBounds2.getWidth();
                this.ph = rectBounds2.getHeight();
            } else {
                this.px = f2;
                this.py = f3;
                this.pw = f4;
                this.ph = f5;
            }
        } else {
            this.ph = 0.0f;
            this.pw = 0.0f;
            this.py = 0.0f;
            this.px = 0.0f;
        }
    }

    void setPaintBeforeDraw(Paint paint, BaseTransform baseTransform, float f2, float f3, float f4, float f5) {
        switch (paint.getType()) {
            case COLOR: {
                this.setColor((Color)paint, this.compositeAlpha);
                break;
            }
            case LINEAR_GRADIENT: {
                LinearGradient linearGradient = (LinearGradient)paint;
                if (PrismSettings.debug) {
                    System.out.println("PR.setLinearGradient: " + linearGradient.getX1() + ", " + linearGradient.getY1() + ", " + linearGradient.getX2() + ", " + linearGradient.getY2());
                }
                this.paintTx.setTransform(baseTransform);
                SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
                float f6 = linearGradient.getX1();
                float f7 = linearGradient.getY1();
                float f8 = linearGradient.getX2();
                float f9 = linearGradient.getY2();
                if (linearGradient.isProportional()) {
                    f6 = f2 + f4 * f6;
                    f7 = f3 + f5 * f7;
                    f8 = f2 + f4 * f8;
                    f9 = f3 + f5 * f9;
                }
                this.pr.setLinearGradient((int)(65536.0f * f6), (int)(65536.0f * f7), (int)(65536.0f * f8), (int)(65536.0f * f9), SWPaint.getFractions(linearGradient), SWPaint.getARGB(linearGradient, this.compositeAlpha), SWPaint.getPiscesGradientCycleMethod(linearGradient.getSpreadMethod()), this.piscesTx);
                break;
            }
            case RADIAL_GRADIENT: {
                float f10;
                float f11;
                RadialGradient radialGradient = (RadialGradient)paint;
                if (PrismSettings.debug) {
                    System.out.println("PR.setRadialGradient: " + radialGradient.getCenterX() + ", " + radialGradient.getCenterY() + ", " + radialGradient.getFocusAngle() + ", " + radialGradient.getFocusDistance() + ", " + radialGradient.getRadius());
                }
                this.paintTx.setTransform(baseTransform);
                float f12 = radialGradient.getCenterX();
                float f13 = radialGradient.getCenterY();
                float f14 = radialGradient.getRadius();
                if (radialGradient.isProportional()) {
                    f11 = Math.min(f4, f5);
                    f10 = f2 + f4 * 0.5f;
                    float f15 = f3 + f5 * 0.5f;
                    f12 = f10 + (f12 - 0.5f) * f11;
                    f13 = f15 + (f13 - 0.5f) * f11;
                    f14 *= f11;
                    if (f4 != f5 && (double)f4 != 0.0 && (double)f5 != 0.0) {
                        this.paintTx.deriveWithTranslation(f10, f15);
                        this.paintTx.deriveWithConcatenation(f4 / f11, 0.0, 0.0, f5 / f11, 0.0, 0.0);
                        this.paintTx.deriveWithTranslation(-f10, -f15);
                    }
                }
                SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
                f11 = (float)((double)f12 + (double)(radialGradient.getFocusDistance() * f14) * Math.cos(Math.toRadians(radialGradient.getFocusAngle())));
                f10 = (float)((double)f13 + (double)(radialGradient.getFocusDistance() * f14) * Math.sin(Math.toRadians(radialGradient.getFocusAngle())));
                this.pr.setRadialGradient((int)(65536.0f * f12), (int)(65536.0f * f13), (int)(65536.0f * f11), (int)(65536.0f * f10), (int)(65536.0f * f14), SWPaint.getFractions(radialGradient), SWPaint.getARGB(radialGradient, this.compositeAlpha), SWPaint.getPiscesGradientCycleMethod(radialGradient.getSpreadMethod()), this.piscesTx);
                break;
            }
            case IMAGE_PATTERN: {
                ImagePattern imagePattern = (ImagePattern)paint;
                if (imagePattern.getImage().getPixelFormat() == PixelFormat.BYTE_ALPHA) {
                    throw new UnsupportedOperationException("Alpha image is not supported as an image pattern.");
                }
                this.computeImagePatternTransform(imagePattern, baseTransform, f2, f3, f4, f5);
                SWArgbPreTexture sWArgbPreTexture = this.context.validateImagePaintTexture(imagePattern.getImage().getWidth(), imagePattern.getImage().getHeight());
                sWArgbPreTexture.update(imagePattern.getImage());
                if (this.compositeAlpha < 1.0f) {
                    sWArgbPreTexture.applyCompositeAlpha(this.compositeAlpha);
                }
                this.pr.setTexture(1, sWArgbPreTexture.getDataNoClone(), sWArgbPreTexture.getContentWidth(), sWArgbPreTexture.getContentHeight(), sWArgbPreTexture.getPhysicalWidth(), this.piscesTx, sWArgbPreTexture.getWrapMode() == Texture.WrapMode.REPEAT, sWArgbPreTexture.hasAlpha());
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown paint type: " + (Object)((Object)paint.getType()));
            }
        }
    }

    private static int[] getARGB(Gradient gradient, float f2) {
        int n2 = gradient.getNumStops();
        int[] arrn = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            Stop stop = gradient.getStops().get(i2);
            Color color = stop.getColor();
            float f3 = 255.0f * color.getAlpha() * f2;
            arrn[i2] = (((int)f3 & 0xFF) << 24) + (((int)(f3 * color.getRed()) & 0xFF) << 16) + (((int)(f3 * color.getGreen()) & 0xFF) << 8) + ((int)(f3 * color.getBlue()) & 0xFF);
        }
        return arrn;
    }

    private static int[] getFractions(Gradient gradient) {
        int n2 = gradient.getNumStops();
        int[] arrn = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            Stop stop = gradient.getStops().get(i2);
            arrn[i2] = (int)(65536.0f * stop.getOffset());
        }
        return arrn;
    }

    private static int getPiscesGradientCycleMethod(int n2) {
        switch (n2) {
            case 0: {
                return 0;
            }
            case 1: {
                return 2;
            }
            case 2: {
                return 1;
            }
        }
        return 0;
    }

    Transform6 computeDrawTexturePaintTransform(BaseTransform baseTransform, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.paintTx.setTransform(baseTransform);
        float f10 = this.computeScale(f2, f4, f6, f8);
        float f11 = this.computeScale(f3, f5, f7, f9);
        if (f10 == 1.0f && f11 == 1.0f) {
            this.paintTx.deriveWithTranslation(-Math.min(f6, f8) + Math.min(f2, f4), -Math.min(f7, f9) + Math.min(f3, f5));
        } else {
            this.paintTx.deriveWithTranslation(Math.min(f2, f4), Math.min(f3, f5));
            this.paintTx.deriveWithTranslation(f10 >= 0.0f ? 0.0 : (double)Math.abs(f4 - f2), f11 >= 0.0f ? 0.0 : (double)Math.abs(f5 - f3));
            this.paintTx.deriveWithConcatenation(f10, 0.0, 0.0, f11, 0.0, 0.0);
            this.paintTx.deriveWithTranslation(-Math.min(f6, f8), -Math.min(f7, f9));
        }
        SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
        return this.piscesTx;
    }

    private float computeScale(float f2, float f3, float f4, float f5) {
        float f6 = f3 - f2;
        float f7 = f6 / (f5 - f4);
        if (Math.abs(f7) > 32767.0f) {
            f7 = Math.signum(f7) * 32767.0f;
        }
        return f7;
    }

    Transform6 computeSetTexturePaintTransform(Paint paint, BaseTransform baseTransform, RectBounds rectBounds, float f2, float f3, float f4, float f5) {
        this.computePaintBounds(paint, null, rectBounds, f2, f3, f4, f5);
        ImagePattern imagePattern = (ImagePattern)paint;
        this.computeImagePatternTransform(imagePattern, baseTransform, this.px, this.py, this.pw, this.ph);
        return this.piscesTx;
    }

    private void computeImagePatternTransform(ImagePattern imagePattern, BaseTransform baseTransform, float f2, float f3, float f4, float f5) {
        Image image = imagePattern.getImage();
        if (PrismSettings.debug) {
            System.out.println("PR.setTexturePaint: " + image);
            System.out.println("imagePattern: x: " + imagePattern.getX() + ", y: " + imagePattern.getY() + ", w: " + imagePattern.getWidth() + ", h: " + imagePattern.getHeight() + ", proportional: " + imagePattern.isProportional());
        }
        this.paintTx.setTransform(baseTransform);
        if (imagePattern.isProportional()) {
            this.paintTx.deriveWithConcatenation(f4 / (float)image.getWidth() * imagePattern.getWidth(), 0.0, 0.0, f5 / (float)image.getHeight() * imagePattern.getHeight(), f2 + f4 * imagePattern.getX(), f3 + f5 * imagePattern.getY());
        } else {
            this.paintTx.deriveWithConcatenation(imagePattern.getWidth() / (float)image.getWidth(), 0.0, 0.0, imagePattern.getHeight() / (float)image.getHeight(), f2 + imagePattern.getX(), f3 + imagePattern.getY());
        }
        SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
    }
}

