/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BufferUtil;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

class PaintHelper {
    static final int MULTI_MAX_FRACTIONS = 12;
    private static final int MULTI_TEXTURE_SIZE = 16;
    private static final int MULTI_CACHE_SIZE = 256;
    private static final int GTEX_CLR_TABLE_SIZE = 101;
    private static final int GTEX_CLR_TABLE_MIRRORED_SIZE = 201;
    private static final float FULL_TEXEL_Y = 0.00390625f;
    private static final float HALF_TEXEL_Y = 0.001953125f;
    private static final FloatBuffer stopVals = BufferUtil.newFloatBuffer(48);
    private static final ByteBuffer bgraColors = BufferUtil.newByteBuffer(64);
    private static final Image colorsImg = Image.fromByteBgraPreData(bgraColors, 16, 1);
    private static final int[] previousColors = new int[16];
    private static final byte[] gtexColors = new byte[804];
    private static final Image gtexImg = Image.fromByteBgraPreData(ByteBuffer.wrap(gtexColors), 201, 1);
    private static long cacheOffset = -1L;
    private static Texture gradientCacheTexture = null;
    private static Texture gtexCacheTexture = null;
    private static final Affine2D scratchXform2D = new Affine2D();
    private static final Affine3D scratchXform3D = new Affine3D();
    private static Color PINK = new Color(1.0f, 0.078431375f, 0.5764706f, 1.0f);

    PaintHelper() {
    }

    private static float len(float f2, float f3) {
        return f2 == 0.0f ? Math.abs(f3) : (f3 == 0.0f ? Math.abs(f2) : (float)Math.sqrt(f2 * f2 + f3 * f3));
    }

    static void initGradientTextures(ShaderGraphics shaderGraphics) {
        gradientCacheTexture = shaderGraphics.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE, 16, 256);
        gradientCacheTexture.setLinearFiltering(true);
        gradientCacheTexture.contentsUseful();
        gradientCacheTexture.makePermanent();
        gtexCacheTexture = shaderGraphics.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED, 201, 256);
        gtexCacheTexture.setLinearFiltering(true);
        gtexCacheTexture.contentsUseful();
        gtexCacheTexture.makePermanent();
    }

    static Texture getGradientTexture(ShaderGraphics shaderGraphics, Gradient gradient) {
        if (gradientCacheTexture == null) {
            PaintHelper.initGradientTextures(shaderGraphics);
        }
        gradientCacheTexture.lock();
        return gradientCacheTexture;
    }

    static Texture getWrapGradientTexture(ShaderGraphics shaderGraphics) {
        if (gtexCacheTexture == null) {
            PaintHelper.initGradientTextures(shaderGraphics);
        }
        gtexCacheTexture.lock();
        return gtexCacheTexture;
    }

    private static void stopsToImage(List<Stop> list, int n2) {
        if (n2 > 12) {
            throw new RuntimeException("Maximum number of gradient stops exceeded (paint uses " + n2 + " stops, but max is " + 12 + ")");
        }
        bgraColors.clear();
        Color color = null;
        for (int i2 = 0; i2 < 16; ++i2) {
            Color color2;
            if (i2 < n2) {
                color = color2 = list.get(i2).getColor();
            } else {
                color2 = color;
            }
            color2.putBgraPreBytes(bgraColors);
            int n3 = color2.getIntArgbPre();
            if (n3 == previousColors[i2]) continue;
            PaintHelper.previousColors[i2] = n3;
        }
        bgraColors.rewind();
    }

    private static void insertInterpColor(byte[] arrby, int n2, Color color, Color color2, float f2) {
        float f3 = 255.0f - (f2 *= 255.0f);
        arrby[(n2 *= 4) + 0] = (byte)(color.getBluePremult() * f3 + color2.getBluePremult() * f2 + 0.5f);
        arrby[n2 + 1] = (byte)(color.getGreenPremult() * f3 + color2.getGreenPremult() * f2 + 0.5f);
        arrby[n2 + 2] = (byte)(color.getRedPremult() * f3 + color2.getRedPremult() * f2 + 0.5f);
        arrby[n2 + 3] = (byte)(color.getAlpha() * f3 + color2.getAlpha() * f2 + 0.5f);
    }

    private static void stopsToGtexImage(List<Stop> list, int n2) {
        int n3;
        int n4;
        Color color = list.get(0).getColor();
        float f2 = list.get(0).getOffset();
        int n5 = (int)(f2 * 100.0f + 0.5f);
        PaintHelper.insertInterpColor(gtexColors, 0, color, color, 0.0f);
        for (n4 = 1; n4 < n2; ++n4) {
            Color color2 = list.get(n4).getColor();
            f2 = list.get(n4).getOffset();
            n3 = (int)(f2 * 100.0f + 0.5f);
            if (n3 == n5) {
                PaintHelper.insertInterpColor(gtexColors, n3, color, color2, 0.5f);
            } else {
                for (int i2 = n5 + 1; i2 <= n3; ++i2) {
                    float f3 = i2 - n5;
                    PaintHelper.insertInterpColor(gtexColors, i2, color, color2, f3 /= (float)(n3 - n5));
                }
            }
            n5 = n3;
            color = color2;
        }
        for (n4 = 1; n4 < 101; ++n4) {
            int n6 = (100 + n4) * 4;
            n3 = (100 - n4) * 4;
            PaintHelper.gtexColors[n6 + 0] = gtexColors[n3 + 0];
            PaintHelper.gtexColors[n6 + 1] = gtexColors[n3 + 1];
            PaintHelper.gtexColors[n6 + 2] = gtexColors[n3 + 2];
            PaintHelper.gtexColors[n6 + 3] = gtexColors[n3 + 3];
        }
    }

    public static int initGradient(Gradient gradient) {
        long l2 = gradient.getGradientOffset();
        if (l2 >= 0L && l2 > cacheOffset - 256L) {
            return (int)(l2 % 256L);
        }
        List<Stop> list = gradient.getStops();
        int n2 = gradient.getNumStops();
        PaintHelper.stopsToImage(list, n2);
        PaintHelper.stopsToGtexImage(list, n2);
        long l3 = ++cacheOffset;
        gradient.setGradientOffset(l3);
        int n3 = (int)(l3 % 256L);
        gradientCacheTexture.update(colorsImg, 0, n3);
        gtexCacheTexture.update(gtexImg, 0, n3);
        return n3;
    }

    private static void setMultiGradient(Shader shader, Gradient gradient) {
        List<Stop> list = gradient.getStops();
        int n2 = gradient.getNumStops();
        stopVals.clear();
        for (int i2 = 0; i2 < 12; ++i2) {
            stopVals.put(i2 < n2 ? list.get(i2).getOffset() : 0.0f);
            stopVals.put(i2 < n2 - 1 ? 1.0f / (list.get(i2 + 1).getOffset() - list.get(i2).getOffset()) : 0.0f);
            stopVals.put(0.0f);
            stopVals.put(0.0f);
        }
        stopVals.rewind();
        shader.setConstants("fractions", stopVals, 0, 12);
        float f2 = PaintHelper.initGradient(gradient);
        shader.setConstant("offset", f2 / 256.0f + 0.001953125f);
    }

    private static void setTextureGradient(Shader shader, Gradient gradient) {
        float f2 = (float)PaintHelper.initGradient(gradient) + 0.5f;
        float f3 = 0.5f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        switch (gradient.getSpreadMethod()) {
            case 0: {
                f5 = 100.0f;
                break;
            }
            case 2: {
                f4 = 100.0f;
                break;
            }
            case 1: {
                f4 = 200.0f;
            }
        }
        float f6 = 1.0f / (float)gtexCacheTexture.getPhysicalWidth();
        float f7 = 1.0f / (float)gtexCacheTexture.getPhysicalHeight();
        shader.setConstant("content", f3 *= f6, f2 *= f7, f4 *= f6, f5 *= f6);
    }

    static void setLinearGradient(ShaderGraphics shaderGraphics, Shader shader, LinearGradient linearGradient, float f2, float f3, float f4, float f5) {
        BaseTransform baseTransform = linearGradient.getGradientTransformNoClone();
        Affine3D affine3D = scratchXform3D;
        shaderGraphics.getPaintShaderTransform(affine3D);
        if (baseTransform != null) {
            affine3D.concatenate(baseTransform);
        }
        float f6 = f2 + linearGradient.getX1() * f4;
        float f7 = f3 + linearGradient.getY1() * f5;
        float f8 = f2 + linearGradient.getX2() * f4;
        float f9 = f3 + linearGradient.getY2() * f5;
        float f10 = f6;
        float f11 = f7;
        affine3D.translate(f10, f11);
        f10 = f8 - f10;
        f11 = f9 - f11;
        double d2 = PaintHelper.len(f10, f11);
        affine3D.rotate(Math.atan2(f11, f10));
        affine3D.scale(d2, 1.0);
        if (!affine3D.is2D()) {
            BaseTransform baseTransform2;
            try {
                baseTransform2 = affine3D.createInverse();
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                affine3D.setToScale(0.0, 0.0, 0.0);
                baseTransform2 = affine3D;
            }
            NGCamera nGCamera = shaderGraphics.getCameraNoClone();
            Vec3d vec3d = new Vec3d();
            PickRay pickRay = new PickRay();
            PickRay pickRay2 = PaintHelper.project(0.0f, 0.0f, nGCamera, baseTransform2, pickRay, vec3d, null);
            PickRay pickRay3 = PaintHelper.project(1.0f, 0.0f, nGCamera, baseTransform2, pickRay, vec3d, null);
            PickRay pickRay4 = PaintHelper.project(0.0f, 1.0f, nGCamera, baseTransform2, pickRay, vec3d, null);
            double d3 = pickRay3.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d4 = pickRay4.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d5 = pickRay2.getDirectionNoClone().x;
            double d6 = pickRay3.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d7 = pickRay4.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d8 = pickRay2.getDirectionNoClone().z;
            shader.setConstant("gradParams", (float)(d3 *= -pickRay2.getOriginNoClone().z), (float)(d4 *= -pickRay2.getOriginNoClone().z), (float)(d5 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().x);
            shader.setConstant("perspVec", (float)d6, (float)d7, (float)d8);
        } else {
            try {
                affine3D.invert();
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                affine3D.setToScale(0.0, 0.0, 0.0);
            }
            double d9 = (float)affine3D.getMxx();
            double d10 = (float)affine3D.getMxy();
            double d11 = (float)affine3D.getMxt();
            shader.setConstant("gradParams", (float)d9, (float)d10, (float)d11, 0.0f);
            shader.setConstant("perspVec", 0.0f, 0.0f, 1.0f);
        }
        PaintHelper.setMultiGradient(shader, linearGradient);
    }

    static AffineBase getLinearGradientTx(LinearGradient linearGradient, Shader shader, BaseTransform baseTransform, float f2, float f3, float f4, float f5) {
        AffineBase affineBase;
        BaseTransform baseTransform2;
        float f6 = linearGradient.getX1();
        float f7 = linearGradient.getY1();
        float f8 = linearGradient.getX2();
        float f9 = linearGradient.getY2();
        if (linearGradient.isProportional()) {
            f6 = f2 + f6 * f4;
            f7 = f3 + f7 * f5;
            f8 = f2 + f8 * f4;
            f9 = f3 + f9 * f5;
        }
        float f10 = f8 - f6;
        float f11 = f9 - f7;
        float f12 = PaintHelper.len(f10, f11);
        if (linearGradient.getSpreadMethod() == 1) {
            f12 *= 2.0f;
        }
        if ((baseTransform2 = linearGradient.getGradientTransformNoClone()).isIdentity() && baseTransform.isIdentity()) {
            Affine2D affine2D = scratchXform2D;
            affine2D.setToTranslation(f6, f7);
            affine2D.rotate(f10, f11);
            affine2D.scale(f12, 1.0);
            affineBase = affine2D;
        } else {
            Affine3D affine3D = scratchXform3D;
            affine3D.setTransform(baseTransform);
            affine3D.concatenate(baseTransform2);
            affine3D.translate(f6, f7);
            affine3D.rotate(Math.atan2(f11, f10));
            affine3D.scale(f12, 1.0);
            affineBase = affine3D;
        }
        try {
            affineBase.invert();
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            scratchXform2D.setToScale(0.0, 0.0);
            affineBase = scratchXform2D;
        }
        PaintHelper.setTextureGradient(shader, linearGradient);
        return affineBase;
    }

    static void setRadialGradient(ShaderGraphics shaderGraphics, Shader shader, RadialGradient radialGradient, float f2, float f3, float f4, float f5) {
        BaseTransform baseTransform;
        float f6;
        Affine3D affine3D = scratchXform3D;
        shaderGraphics.getPaintShaderTransform(affine3D);
        float f7 = radialGradient.getRadius();
        float f8 = radialGradient.getCenterX();
        float f9 = radialGradient.getCenterY();
        float f10 = radialGradient.getFocusAngle();
        float f11 = radialGradient.getFocusDistance();
        if (f11 < 0.0f) {
            f11 = -f11;
            f10 += 180.0f;
        }
        f10 = (float)Math.toRadians(f10);
        if (radialGradient.isProportional()) {
            float f12 = f2 + f4 / 2.0f;
            float f13 = f3 + f5 / 2.0f;
            f6 = Math.min(f4, f5);
            f8 = (f8 - 0.5f) * f6 + f12;
            f9 = (f9 - 0.5f) * f6 + f13;
            if (f4 != f5 && f4 != 0.0f && f5 != 0.0f) {
                affine3D.translate(f12, f13);
                affine3D.scale(f4 / f6, f5 / f6);
                affine3D.translate(-f12, -f13);
            }
            f7 *= f6;
        }
        if ((baseTransform = radialGradient.getGradientTransformNoClone()) != null) {
            affine3D.concatenate(baseTransform);
        }
        affine3D.translate(f8, f9);
        affine3D.rotate(f10);
        affine3D.scale(f7, f7);
        try {
            affine3D.invert();
        }
        catch (Exception exception) {
            affine3D.setToScale(0.0, 0.0, 0.0);
        }
        if (!affine3D.is2D()) {
            NGCamera nGCamera = shaderGraphics.getCameraNoClone();
            Vec3d vec3d = new Vec3d();
            PickRay pickRay = new PickRay();
            PickRay pickRay2 = PaintHelper.project(0.0f, 0.0f, nGCamera, affine3D, pickRay, vec3d, null);
            PickRay pickRay3 = PaintHelper.project(1.0f, 0.0f, nGCamera, affine3D, pickRay, vec3d, null);
            PickRay pickRay4 = PaintHelper.project(0.0f, 1.0f, nGCamera, affine3D, pickRay, vec3d, null);
            double d2 = pickRay3.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d3 = pickRay4.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d4 = pickRay2.getDirectionNoClone().x;
            double d5 = pickRay3.getDirectionNoClone().y - pickRay2.getDirectionNoClone().y;
            double d6 = pickRay4.getDirectionNoClone().y - pickRay2.getDirectionNoClone().y;
            double d7 = pickRay2.getDirectionNoClone().y;
            double d8 = pickRay3.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d9 = pickRay4.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d10 = pickRay2.getDirectionNoClone().z;
            shader.setConstant("perspVec", (float)d8, (float)d9, (float)d10);
            shader.setConstant("m0", (float)(d2 *= -pickRay2.getOriginNoClone().z), (float)(d3 *= -pickRay2.getOriginNoClone().z), (float)(d4 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().x);
            shader.setConstant("m1", (float)(d5 *= -pickRay2.getOriginNoClone().z), (float)(d6 *= -pickRay2.getOriginNoClone().z), (float)(d7 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().y);
        } else {
            float f14 = (float)affine3D.getMxx();
            f6 = (float)affine3D.getMxy();
            float f15 = (float)affine3D.getMxt();
            shader.setConstant("m0", f14, f6, f15, 0.0f);
            float f16 = (float)affine3D.getMyx();
            float f17 = (float)affine3D.getMyy();
            float f18 = (float)affine3D.getMyt();
            shader.setConstant("m1", f16, f17, f18, 0.0f);
            shader.setConstant("perspVec", 0.0f, 0.0f, 1.0f);
        }
        f11 = Math.min(f11, 0.99f);
        float f19 = 1.0f - f11 * f11;
        float f20 = 1.0f / f19;
        shader.setConstant("precalc", f11, f19, f20);
        PaintHelper.setMultiGradient(shader, radialGradient);
    }

    static AffineBase getRadialGradientTx(RadialGradient radialGradient, Shader shader, BaseTransform baseTransform, float f2, float f3, float f4, float f5) {
        BaseTransform baseTransform2;
        float f6;
        Affine3D affine3D = scratchXform3D;
        affine3D.setTransform(baseTransform);
        float f7 = radialGradient.getRadius();
        float f8 = radialGradient.getCenterX();
        float f9 = radialGradient.getCenterY();
        float f10 = radialGradient.getFocusAngle();
        float f11 = radialGradient.getFocusDistance();
        if (f11 < 0.0f) {
            f11 = -f11;
            f10 += 180.0f;
        }
        f10 = (float)Math.toRadians(f10);
        if (radialGradient.isProportional()) {
            float f12 = f2 + f4 / 2.0f;
            float f13 = f3 + f5 / 2.0f;
            f6 = Math.min(f4, f5);
            f8 = (f8 - 0.5f) * f6 + f12;
            f9 = (f9 - 0.5f) * f6 + f13;
            if (f4 != f5 && f4 != 0.0f && f5 != 0.0f) {
                affine3D.translate(f12, f13);
                affine3D.scale(f4 / f6, f5 / f6);
                affine3D.translate(-f12, -f13);
            }
            f7 *= f6;
        }
        if (radialGradient.getSpreadMethod() == 1) {
            f7 *= 2.0f;
        }
        if ((baseTransform2 = radialGradient.getGradientTransformNoClone()) != null) {
            affine3D.concatenate(baseTransform2);
        }
        affine3D.translate(f8, f9);
        affine3D.rotate(f10);
        affine3D.scale(f7, f7);
        try {
            affine3D.invert();
        }
        catch (Exception exception) {
            affine3D.setToScale(0.0, 0.0, 0.0);
        }
        f11 = Math.min(f11, 0.99f);
        float f14 = 1.0f - f11 * f11;
        f6 = 1.0f / f14;
        shader.setConstant("precalc", f11, f14, f6);
        PaintHelper.setTextureGradient(shader, radialGradient);
        return affine3D;
    }

    static void setImagePattern(ShaderGraphics shaderGraphics, Shader shader, ImagePattern imagePattern, float f2, float f3, float f4, float f5) {
        float f6 = f2 + imagePattern.getX() * f4;
        float f7 = f3 + imagePattern.getY() * f5;
        float f8 = f6 + imagePattern.getWidth() * f4;
        float f9 = f7 + imagePattern.getHeight() * f5;
        ResourceFactory resourceFactory = shaderGraphics.getResourceFactory();
        Image image = imagePattern.getImage();
        Texture texture = resourceFactory.getCachedTexture(image, Texture.WrapMode.REPEAT);
        float f10 = texture.getContentX();
        float f11 = texture.getContentY();
        float f12 = texture.getContentWidth();
        float f13 = texture.getContentHeight();
        float f14 = texture.getPhysicalWidth();
        float f15 = texture.getPhysicalHeight();
        texture.unlock();
        Affine3D affine3D = scratchXform3D;
        shaderGraphics.getPaintShaderTransform(affine3D);
        affine3D.translate(f6, f7);
        affine3D.scale(f8 - f6, f9 - f7);
        if (f12 < f14) {
            affine3D.translate(0.5 / (double)f12, 0.0);
            f10 += 0.5f;
        }
        if (f13 < f15) {
            affine3D.translate(0.0, 0.5 / (double)f13);
            f11 += 0.5f;
        }
        try {
            affine3D.invert();
        }
        catch (Exception exception) {
            affine3D.setToScale(0.0, 0.0, 0.0);
        }
        if (!affine3D.is2D()) {
            NGCamera nGCamera = shaderGraphics.getCameraNoClone();
            Vec3d vec3d = new Vec3d();
            PickRay pickRay = new PickRay();
            PickRay pickRay2 = PaintHelper.project(0.0f, 0.0f, nGCamera, affine3D, pickRay, vec3d, null);
            PickRay pickRay3 = PaintHelper.project(1.0f, 0.0f, nGCamera, affine3D, pickRay, vec3d, null);
            PickRay pickRay4 = PaintHelper.project(0.0f, 1.0f, nGCamera, affine3D, pickRay, vec3d, null);
            double d2 = pickRay3.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d3 = pickRay4.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d4 = pickRay2.getDirectionNoClone().x;
            double d5 = pickRay3.getDirectionNoClone().y - pickRay2.getDirectionNoClone().y;
            double d6 = pickRay4.getDirectionNoClone().y - pickRay2.getDirectionNoClone().y;
            double d7 = pickRay2.getDirectionNoClone().y;
            double d8 = pickRay3.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d9 = pickRay4.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d10 = pickRay2.getDirectionNoClone().z;
            shader.setConstant("perspVec", (float)d8, (float)d9, (float)d10);
            shader.setConstant("xParams", (float)(d2 *= -pickRay2.getOriginNoClone().z), (float)(d3 *= -pickRay2.getOriginNoClone().z), (float)(d4 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().x);
            shader.setConstant("yParams", (float)(d5 *= -pickRay2.getOriginNoClone().z), (float)(d6 *= -pickRay2.getOriginNoClone().z), (float)(d7 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().y);
        } else {
            float f16 = (float)affine3D.getMxx();
            float f17 = (float)affine3D.getMxy();
            float f18 = (float)affine3D.getMxt();
            shader.setConstant("xParams", f16, f17, f18, 0.0f);
            float f19 = (float)affine3D.getMyx();
            float f20 = (float)affine3D.getMyy();
            float f21 = (float)affine3D.getMyt();
            shader.setConstant("yParams", f19, f20, f21, 0.0f);
            shader.setConstant("perspVec", 0.0f, 0.0f, 1.0f);
        }
        shader.setConstant("content", f10 /= f14, f11 /= f15, f12 /= f14, f13 /= f15);
    }

    static AffineBase getImagePatternTx(ShaderGraphics shaderGraphics, ImagePattern imagePattern, Shader shader, BaseTransform baseTransform, float f2, float f3, float f4, float f5) {
        AffineBase affineBase;
        float f6 = imagePattern.getX();
        float f7 = imagePattern.getY();
        float f8 = imagePattern.getWidth();
        float f9 = imagePattern.getHeight();
        if (imagePattern.isProportional()) {
            f6 = f2 + f6 * f4;
            f7 = f3 + f7 * f5;
            f8 *= f4;
            f9 *= f5;
        }
        ResourceFactory resourceFactory = shaderGraphics.getResourceFactory();
        Image image = imagePattern.getImage();
        Texture texture = resourceFactory.getCachedTexture(image, Texture.WrapMode.REPEAT);
        float f10 = texture.getContentX();
        float f11 = texture.getContentY();
        float f12 = texture.getContentWidth();
        float f13 = texture.getContentHeight();
        float f14 = texture.getPhysicalWidth();
        float f15 = texture.getPhysicalHeight();
        texture.unlock();
        if (baseTransform.isIdentity()) {
            Affine2D affine2D = scratchXform2D;
            affine2D.setToTranslation(f6, f7);
            affine2D.scale(f8, f9);
            affineBase = affine2D;
        } else {
            Affine3D affine3D = scratchXform3D;
            affine3D.setTransform(baseTransform);
            affine3D.translate(f6, f7);
            affine3D.scale(f8, f9);
            affineBase = affine3D;
        }
        if (f12 < f14) {
            affineBase.translate(0.5 / (double)f12, 0.0);
            f10 += 0.5f;
        }
        if (f13 < f15) {
            affineBase.translate(0.0, 0.5 / (double)f13);
            f11 += 0.5f;
        }
        try {
            affineBase.invert();
        }
        catch (Exception exception) {
            affineBase = scratchXform2D;
            scratchXform2D.setToScale(0.0, 0.0);
        }
        shader.setConstant("content", f10 /= f14, f11 /= f15, f12 /= f14, f13 /= f15);
        return affineBase;
    }

    static PickRay project(float f2, float f3, NGCamera nGCamera, BaseTransform baseTransform, PickRay pickRay, Vec3d vec3d, Point2D point2D) {
        pickRay = nGCamera.computePickRay(f2, f3, pickRay);
        return pickRay.project(baseTransform, nGCamera instanceof NGPerspectiveCamera, vec3d, point2D);
    }
}

