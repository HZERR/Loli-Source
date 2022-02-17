/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DropShadow;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.InnerShadow;

class EffectUtil {
    private static final int TEX_SIZE = 256;
    private static Texture itex;
    private static Texture dtex;

    static boolean renderEffectForRectangularNode(NGNode nGNode, Graphics graphics, Effect effect, float f2, boolean bl, float f3, float f4, float f5, float f6) {
        DropShadow dropShadow;
        float f7;
        if (!graphics.getTransformNoClone().is2D() && graphics.isDepthBuffer() && graphics.isDepthTest()) {
            return false;
        }
        if (effect instanceof InnerShadow && !bl) {
            InnerShadow innerShadow = (InnerShadow)effect;
            float f8 = innerShadow.getRadius();
            if (f8 > 0.0f && f8 < f5 / 2.0f && f8 < f6 / 2.0f && innerShadow.getChoke() == 0.0f && innerShadow.getShadowSourceInput() == null && innerShadow.getContentInput() == null) {
                nGNode.renderContent(graphics);
                EffectUtil.renderRectInnerShadow(graphics, innerShadow, f2, f3, f4, f5, f6);
                return true;
            }
        } else if (effect instanceof DropShadow && (f7 = (dropShadow = (DropShadow)effect).getRadius()) > 0.0f && f7 < f5 / 2.0f && f7 < f6 / 2.0f && dropShadow.getSpread() == 0.0f && dropShadow.getShadowSourceInput() == null && dropShadow.getContentInput() == null) {
            EffectUtil.renderRectDropShadow(graphics, dropShadow, f2, f3, f4, f5, f6);
            nGNode.renderContent(graphics);
            return true;
        }
        return false;
    }

    static void renderRectInnerShadow(Graphics graphics, InnerShadow innerShadow, float f2, float f3, float f4, float f5, float f6) {
        if (itex == null) {
            byte[] arrby = new byte[65536];
            EffectUtil.fillGaussian(arrby, 256, 128.0f, innerShadow.getChoke(), true);
            Image image = Image.fromByteAlphaData(arrby, 256, 256);
            itex = graphics.getResourceFactory().createTexture(image, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
            assert (itex.getWrapMode() == Texture.WrapMode.CLAMP_TO_EDGE);
            itex.contentsUseful();
            itex.makePermanent();
        }
        float f7 = innerShadow.getRadius();
        int n2 = itex.getPhysicalWidth();
        int n3 = itex.getContentX();
        int n4 = n3 + itex.getContentWidth();
        float f8 = ((float)n3 + 0.5f) / (float)n2;
        float f9 = ((float)n4 - 0.5f) / (float)n2;
        float f10 = f3;
        float f11 = f4;
        float f12 = f3 + f5;
        float f13 = f4 + f6;
        float f14 = f10 + (float)innerShadow.getOffsetX();
        float f15 = f11 + (float)innerShadow.getOffsetY();
        float f16 = f14 + f5;
        float f17 = f15 + f6;
        graphics.setPaint(EffectUtil.toPrismColor(innerShadow.getColor(), f2));
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f10, f11, f12, f15 - f7, f8, f8, f8, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f14 - f7, f15 - f7, f14 + f7, f15 + f7, f8, f8, f9, f9);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f14 + f7, f15 - f7, f16 - f7, f15 + f7, f9, f8, f9, f9);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f16 - f7, f15 - f7, f16 + f7, f15 + f7, f9, f8, f8, f9);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f10, f15 - f7, f14 - f7, f17 + f7, f8, f8, f8, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f14 - f7, f15 + f7, f14 + f7, f17 - f7, f8, f9, f9, f9);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f16 - f7, f15 + f7, f16 + f7, f17 - f7, f9, f9, f8, f9);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f16 + f7, f15 - f7, f12, f17 + f7, f8, f8, f8, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f14 - f7, f17 - f7, f14 + f7, f17 + f7, f8, f9, f9, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f14 + f7, f17 - f7, f16 - f7, f17 + f7, f9, f9, f9, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f16 - f7, f17 - f7, f16 + f7, f17 + f7, f9, f9, f8, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f10, f11, f12, f13, f10, f17 + f7, f12, f13, f8, f8, f8, f8);
    }

    static void drawClippedTexture(Graphics graphics, Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        if (f6 >= f8 || f7 >= f9 || f2 >= f4 || f3 >= f5) {
            return;
        }
        if (f8 > f2 && f6 < f4) {
            if (f6 < f2) {
                f10 += (f12 - f10) * (f2 - f6) / (f8 - f6);
                f6 = f2;
            }
            if (f8 > f4) {
                f12 -= (f12 - f10) * (f8 - f4) / (f8 - f6);
                f8 = f4;
            }
        } else {
            return;
        }
        if (f9 > f3 && f7 < f5) {
            if (f7 < f3) {
                f11 += (f13 - f11) * (f3 - f7) / (f9 - f7);
                f7 = f3;
            }
            if (f9 > f5) {
                f13 -= (f13 - f11) * (f9 - f5) / (f9 - f7);
                f9 = f5;
            }
        } else {
            return;
        }
        graphics.drawTextureRaw(texture, f6, f7, f8, f9, f10, f11, f12, f13);
    }

    static void renderRectDropShadow(Graphics graphics, DropShadow dropShadow, float f2, float f3, float f4, float f5, float f6) {
        if (dtex == null) {
            byte[] arrby = new byte[65536];
            EffectUtil.fillGaussian(arrby, 256, 128.0f, dropShadow.getSpread(), false);
            Image image = Image.fromByteAlphaData(arrby, 256, 256);
            dtex = graphics.getResourceFactory().createTexture(image, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
            assert (dtex.getWrapMode() == Texture.WrapMode.CLAMP_TO_EDGE);
            dtex.contentsUseful();
            dtex.makePermanent();
        }
        float f7 = dropShadow.getRadius();
        int n2 = dtex.getPhysicalWidth();
        int n3 = dtex.getContentX();
        int n4 = n3 + dtex.getContentWidth();
        float f8 = ((float)n3 + 0.5f) / (float)n2;
        float f9 = ((float)n4 - 0.5f) / (float)n2;
        float f10 = f3 + (float)dropShadow.getOffsetX();
        float f11 = f4 + (float)dropShadow.getOffsetY();
        float f12 = f10 + f5;
        float f13 = f11 + f6;
        graphics.setPaint(EffectUtil.toPrismColor(dropShadow.getColor(), f2));
        graphics.drawTextureRaw(dtex, f10 - f7, f11 - f7, f10 + f7, f11 + f7, f8, f8, f9, f9);
        graphics.drawTextureRaw(dtex, f12 - f7, f11 - f7, f12 + f7, f11 + f7, f9, f8, f8, f9);
        graphics.drawTextureRaw(dtex, f12 - f7, f13 - f7, f12 + f7, f13 + f7, f9, f9, f8, f8);
        graphics.drawTextureRaw(dtex, f10 - f7, f13 - f7, f10 + f7, f13 + f7, f8, f9, f9, f8);
        graphics.drawTextureRaw(dtex, f10 + f7, f11 + f7, f12 - f7, f13 - f7, f9, f9, f9, f9);
        graphics.drawTextureRaw(dtex, f10 - f7, f11 + f7, f10 + f7, f13 - f7, f8, f9, f9, f9);
        graphics.drawTextureRaw(dtex, f12 - f7, f11 + f7, f12 + f7, f13 - f7, f9, f9, f8, f9);
        graphics.drawTextureRaw(dtex, f10 + f7, f11 - f7, f12 - f7, f11 + f7, f9, f8, f9, f9);
        graphics.drawTextureRaw(dtex, f10 + f7, f13 - f7, f12 - f7, f13 + f7, f9, f9, f9, f8);
    }

    private static void fillGaussian(byte[] arrby, int n2, float f2, float f3, boolean bl) {
        int n3;
        int n4;
        float f4 = f2 / 3.0f;
        float f5 = 2.0f * f4 * f4;
        if (f5 < Float.MIN_VALUE) {
            f5 = Float.MIN_VALUE;
        }
        float[] arrf = new float[n2];
        int n5 = (n2 + 1) / 2;
        float f6 = 0.0f;
        for (n4 = 0; n4 < arrf.length; ++n4) {
            n3 = n5 - n4;
            arrf[n4] = f6 += (float)Math.exp((float)(-(n3 * n3)) / f5);
        }
        n4 = 0;
        while (n4 < arrf.length) {
            int n6 = n4++;
            arrf[n6] = arrf[n6] / f6;
        }
        for (n4 = 0; n4 < n2; ++n4) {
            for (n3 = 0; n3 < n2; ++n3) {
                int n7;
                float f7 = arrf[n4] * arrf[n3];
                if (bl) {
                    f7 = 1.0f - f7;
                }
                if ((n7 = (int)(f7 * 255.0f)) < 0) {
                    n7 = 0;
                } else if (n7 > 255) {
                    n7 = 255;
                }
                arrby[n4 * n2 + n3] = (byte)n7;
            }
        }
    }

    private static Color toPrismColor(Color4f color4f, float f2) {
        float f3 = color4f.getRed();
        float f4 = color4f.getGreen();
        float f5 = color4f.getBlue();
        float f6 = color4f.getAlpha() * f2;
        return new Color(f3, f4, f5, f6);
    }

    private EffectUtil() {
    }
}

