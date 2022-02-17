/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.ColorAdjust;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWColorAdjustPeer
extends JSWEffectPeer {
    public JSWColorAdjustPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final ColorAdjust getEffect() {
        return (ColorAdjust)super.getEffect();
    }

    private float getHue() {
        return this.getEffect().getHue() / 2.0f;
    }

    private float getSaturation() {
        return this.getEffect().getSaturation() + 1.0f;
    }

    private float getBrightness() {
        return this.getEffect().getBrightness() + 1.0f;
    }

    private float getContrast() {
        float f2 = this.getEffect().getContrast();
        if (f2 > 0.0f) {
            f2 *= 3.0f;
        }
        return f2 + 1.0f;
    }

    @Override
    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setEffect(effect);
        Rectangle rectangle2 = this.getResultBounds(baseTransform, rectangle, arrimageData);
        this.setDestBounds(rectangle2);
        HeapImage heapImage = (HeapImage)arrimageData[0].getTransformedImage(rectangle2);
        int n2 = 0;
        int n3 = 0;
        int n4 = heapImage.getPhysicalWidth();
        int n5 = heapImage.getPhysicalHeight();
        int n6 = heapImage.getScanlineStride();
        int[] arrn = heapImage.getPixelArray();
        Rectangle rectangle3 = new Rectangle(n2, n3, n4, n5);
        Rectangle rectangle4 = arrimageData[0].getTransformedBounds(rectangle2);
        BaseTransform baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
        this.setInputBounds(0, rectangle4);
        this.setInputNativeBounds(0, rectangle3);
        float[] arrf = new float[4];
        this.getTextureCoordinates(0, arrf, rectangle4.x, rectangle4.y, n4, n5, rectangle2, baseTransform2);
        int n7 = rectangle2.width;
        int n8 = rectangle2.height;
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n7, n8));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n9 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        float f2 = this.getSaturation();
        float f3 = this.getBrightness();
        float f4 = this.getContrast();
        float f5 = this.getHue();
        float f6 = (arrf[2] - arrf[0]) / (float)n7;
        float f7 = (arrf[3] - arrf[1]) / (float)n8;
        float f8 = arrf[1] + f7 * 0.5f;
        for (int i2 = 0; i2 < 0 + n8; ++i2) {
            float f9 = i2;
            int n10 = i2 * n9;
            float f10 = arrf[0] + f6 * 0.5f;
            for (int i3 = 0; i3 < 0 + n7; ++i3) {
                float f11;
                float f12;
                float f13;
                float f14;
                int n11;
                float f15 = i3;
                float f16 = f10;
                float f17 = f8;
                if (f16 >= 0.0f && f17 >= 0.0f) {
                    int n12 = (int)(f16 * (float)n4);
                    int n13 = (int)(f17 * (float)n5);
                    boolean bl = n12 >= n4 || n13 >= n5;
                    n11 = bl ? 0 : arrn[n13 * n6 + n12];
                } else {
                    n11 = 0;
                }
                float f18 = (float)(n11 >> 16 & 0xFF) / 255.0f;
                float f19 = (float)(n11 >> 8 & 0xFF) / 255.0f;
                float f20 = (float)(n11 & 0xFF) / 255.0f;
                float f21 = (float)(n11 >>> 24) / 255.0f;
                f16 = f18;
                f17 = f19;
                float f22 = f20;
                float f23 = f21;
                if (f23 > 0.0f) {
                    f16 /= f23;
                    f17 /= f23;
                    f22 /= f23;
                }
                f16 = (f16 - 0.5f) * f4 + 0.5f;
                f17 = (f17 - 0.5f) * f4 + 0.5f;
                f22 = (f22 - 0.5f) * f4 + 0.5f;
                float f24 = f16;
                float f25 = f17;
                float f26 = f22;
                float f27 = f24;
                float f28 = f25;
                f28 = f27 = (f14 = f27 > f28 ? f27 : f28);
                float f29 = f26;
                f27 = f14 = f28 > f29 ? f28 : f29;
                f29 = f24;
                float f30 = f25;
                f30 = f29 = (f28 = f29 < f30 ? f29 : f30);
                float f31 = f26;
                f28 = f30 < f31 ? f30 : f31;
                f29 = f28;
                if (f27 > f29) {
                    f30 = (f27 - f24) / (f27 - f29);
                    f31 = (f27 - f25) / (f27 - f29);
                    f13 = (f27 - f26) / (f27 - f29);
                    f12 = f24 == f27 ? f13 - f31 : (f25 == f27 ? 2.0f + f30 - f13 : 4.0f + f31 - f30);
                    if ((f12 /= 6.0f) < 0.0f) {
                        f12 += 1.0f;
                    }
                    f11 = (f27 - f29) / f27;
                } else {
                    f12 = 0.0f;
                    f11 = 0.0f;
                }
                float f32 = f27;
                float f33 = f12;
                float f34 = f11;
                float f35 = f32;
                f24 = f33;
                f25 = f34;
                f26 = f35;
                f24 += f5;
                if (f24 < 0.0f) {
                    f24 += 1.0f;
                } else if (f24 > 1.0f) {
                    f24 -= 1.0f;
                }
                if (f2 > 1.0f) {
                    f12 = f2 - 1.0f;
                    f25 += (1.0f - f25) * f12;
                } else {
                    f25 *= f2;
                }
                if (f3 > 1.0f) {
                    f12 = f3 - 1.0f;
                    f25 *= 1.0f - f12;
                    f26 += (1.0f - f26) * f12;
                } else {
                    f26 *= f3;
                }
                f32 = f25;
                f14 = f26;
                f27 = 0.0f;
                f28 = 1.0f;
                float f36 = f32 < f27 ? f27 : (f12 = f32 > f28 ? f28 : f32);
                f11 = f14 < f27 ? f27 : (f14 > f28 ? f28 : f14);
                f25 = f12;
                f26 = f11;
                f28 = f24;
                f29 = f25;
                f30 = f26;
                f31 = 0.0f;
                f13 = 0.0f;
                float f37 = 0.0f;
                float f38 = f28;
                float f39 = f29;
                float f40 = f30;
                float f41 = f38;
                float f42 = (float)Math.floor(f41);
                f41 = f38 = (f38 - f42) * 6.0f;
                f42 = (float)Math.floor(f41);
                f41 = f38 - f42;
                float f43 = f40 * (1.0f - f39);
                float f44 = f40 * (1.0f - f39 * f41);
                float f45 = f40 * (1.0f - f39 * (1.0f - f41));
                float f46 = f38;
                f38 = f42 = (float)Math.floor(f46);
                if (f38 < 1.0f) {
                    f31 = f40;
                    f13 = f45;
                    f37 = f43;
                } else if (f38 < 2.0f) {
                    f31 = f44;
                    f13 = f40;
                    f37 = f43;
                } else if (f38 < 3.0f) {
                    f31 = f43;
                    f13 = f40;
                    f37 = f45;
                } else if (f38 < 4.0f) {
                    f31 = f43;
                    f13 = f44;
                    f37 = f40;
                } else if (f38 < 5.0f) {
                    f31 = f45;
                    f13 = f43;
                    f37 = f40;
                } else {
                    f31 = f40;
                    f13 = f43;
                    f37 = f44;
                }
                f32 = f31;
                f14 = f13;
                f27 = f37;
                float f47 = f23 * f32;
                float f48 = f23 * f14;
                float f49 = f23 * f27;
                float f50 = f23;
                if (f50 < 0.0f) {
                    f50 = 0.0f;
                } else if (f50 > 1.0f) {
                    f50 = 1.0f;
                }
                if (f47 < 0.0f) {
                    f47 = 0.0f;
                } else if (f47 > f50) {
                    f47 = f50;
                }
                if (f48 < 0.0f) {
                    f48 = 0.0f;
                } else if (f48 > f50) {
                    f48 = f50;
                }
                if (f49 < 0.0f) {
                    f49 = 0.0f;
                } else if (f49 > f50) {
                    f49 = f50;
                }
                arrn2[n10 + i3] = (int)(f47 * 255.0f) << 16 | (int)(f48 * 255.0f) << 8 | (int)(f49 * 255.0f) << 0 | (int)(f50 * 255.0f) << 24;
                f10 += f6;
            }
            f8 += f7;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

