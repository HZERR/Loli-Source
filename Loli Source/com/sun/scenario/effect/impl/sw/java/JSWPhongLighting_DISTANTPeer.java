/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.PhongLighting;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import java.nio.FloatBuffer;

public class JSWPhongLighting_DISTANTPeer
extends JSWEffectPeer {
    private FloatBuffer kvals;

    public JSWPhongLighting_DISTANTPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final PhongLighting getEffect() {
        return (PhongLighting)super.getEffect();
    }

    private float getSurfaceScale() {
        return this.getEffect().getSurfaceScale();
    }

    private float getDiffuseConstant() {
        return this.getEffect().getDiffuseConstant();
    }

    private float getSpecularConstant() {
        return this.getEffect().getSpecularConstant();
    }

    private float getSpecularExponent() {
        return this.getEffect().getSpecularExponent();
    }

    private float[] getNormalizedLightPosition() {
        return this.getEffect().getLight().getNormalizedLightPosition();
    }

    private float[] getLightPosition() {
        PointLight pointLight = (PointLight)this.getEffect().getLight();
        return new float[]{pointLight.getX(), pointLight.getY(), pointLight.getZ()};
    }

    private float[] getLightColor() {
        return this.getEffect().getLight().getColor().getPremultipliedRGBComponents();
    }

    private float getLightSpecularExponent() {
        return ((SpotLight)this.getEffect().getLight()).getSpecularExponent();
    }

    private float[] getNormalizedLightDirection() {
        return ((SpotLight)this.getEffect().getLight()).getNormalizedLightDirection();
    }

    private FloatBuffer getKvals() {
        Rectangle rectangle = this.getInputNativeBounds(0);
        float f2 = 1.0f / (float)rectangle.width;
        float f3 = 1.0f / (float)rectangle.height;
        float[] arrf = new float[]{-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f};
        float[] arrf2 = new float[]{-1.0f, -2.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f};
        if (this.kvals == null) {
            this.kvals = BufferUtil.newFloatBuffer(32);
        }
        this.kvals.clear();
        int n2 = 0;
        float f4 = -this.getSurfaceScale() * 0.25f;
        for (int i2 = -1; i2 <= 1; ++i2) {
            for (int i3 = -1; i3 <= 1; ++i3) {
                if (i2 != 0 || i3 != 0) {
                    this.kvals.put((float)i3 * f2);
                    this.kvals.put((float)i2 * f3);
                    this.kvals.put(arrf[n2] * f4);
                    this.kvals.put(arrf2[n2] * f4);
                }
                ++n2;
            }
        }
        this.kvals.rewind();
        return this.kvals;
    }

    private int getKvalsArrayLength() {
        return 8;
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
        HeapImage heapImage2 = (HeapImage)arrimageData[1].getTransformedImage(rectangle2);
        int n7 = 0;
        int n8 = 0;
        int n9 = heapImage2.getPhysicalWidth();
        int n10 = heapImage2.getPhysicalHeight();
        int n11 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        Rectangle rectangle5 = new Rectangle(n7, n8, n9, n10);
        Rectangle rectangle6 = arrimageData[1].getTransformedBounds(rectangle2);
        BaseTransform baseTransform3 = BaseTransform.IDENTITY_TRANSFORM;
        this.setInputBounds(1, rectangle6);
        this.setInputNativeBounds(1, rectangle5);
        float[] arrf = new float[4];
        this.getTextureCoordinates(0, arrf, rectangle4.x, rectangle4.y, n4, n5, rectangle2, baseTransform2);
        float[] arrf2 = new float[4];
        this.getTextureCoordinates(1, arrf2, rectangle6.x, rectangle6.y, n9, n10, rectangle2, baseTransform3);
        int n12 = rectangle2.width;
        int n13 = rectangle2.height;
        HeapImage heapImage3 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n12, n13));
        this.setDestNativeBounds(heapImage3.getPhysicalWidth(), heapImage3.getPhysicalHeight());
        int n14 = heapImage3.getScanlineStride();
        int[] arrn3 = heapImage3.getPixelArray();
        float[] arrf3 = this.getNormalizedLightPosition();
        float f2 = arrf3[0];
        float f3 = arrf3[1];
        float f4 = arrf3[2];
        float f5 = this.getSpecularExponent();
        FloatBuffer floatBuffer = this.getKvals();
        float[] arrf4 = new float[floatBuffer.capacity()];
        floatBuffer.get(arrf4);
        float f6 = this.getDiffuseConstant();
        float[] arrf5 = this.getLightColor();
        float f7 = arrf5[0];
        float f8 = arrf5[1];
        float f9 = arrf5[2];
        float f10 = this.getSpecularConstant();
        float f11 = (arrf[2] - arrf[0]) / (float)n12;
        float f12 = (arrf[3] - arrf[1]) / (float)n13;
        float f13 = (arrf2[2] - arrf2[0]) / (float)n12;
        float f14 = (arrf2[3] - arrf2[1]) / (float)n13;
        float f15 = arrf[1] + f12 * 0.5f;
        float f16 = arrf2[1] + f14 * 0.5f;
        for (int i2 = 0; i2 < 0 + n13; ++i2) {
            float f17 = i2;
            int n15 = i2 * n14;
            float f18 = arrf[0] + f11 * 0.5f;
            float f19 = arrf2[0] + f13 * 0.5f;
            for (int i3 = 0; i3 < 0 + n12; ++i3) {
                float f20;
                float f21;
                int n16;
                int n17;
                float f22 = i3;
                float f23 = f19;
                float f24 = f16;
                if (f23 >= 0.0f && f24 >= 0.0f) {
                    int n18 = (int)(f23 * (float)n9);
                    n17 = (int)(f24 * (float)n10);
                    boolean bl = n18 >= n9 || n17 >= n10;
                    n16 = bl ? 0 : arrn2[n17 * n11 + n18];
                } else {
                    n16 = 0;
                }
                float f25 = (float)(n16 >> 16 & 0xFF) / 255.0f;
                float f26 = (float)(n16 >> 8 & 0xFF) / 255.0f;
                float f27 = (float)(n16 & 0xFF) / 255.0f;
                float f28 = (float)(n16 >>> 24) / 255.0f;
                f23 = f25;
                f24 = f26;
                float f29 = f27;
                float f30 = f28;
                float f31 = 0.0f;
                float f32 = 0.0f;
                float f33 = 1.0f;
                for (n17 = 0; n17 < 8; ++n17) {
                    int n19;
                    f21 = f18 + arrf4[n17 * 4 + 0];
                    f20 = f15 + arrf4[n17 * 4 + 1];
                    if (f21 >= 0.0f && f20 >= 0.0f) {
                        int n20 = (int)(f21 * (float)n4);
                        int n21 = (int)(f20 * (float)n5);
                        boolean bl = n20 >= n4 || n21 >= n5;
                        n19 = bl ? 0 : arrn[n21 * n6 + n20];
                    } else {
                        n19 = 0;
                    }
                    f28 = (float)(n19 >>> 24) / 255.0f;
                    f31 += arrf4[n17 * 4 + 2] * f28;
                    f32 += arrf4[n17 * 4 + 3] * f28;
                }
                float f34 = f31;
                float f35 = f32;
                float f36 = f33;
                float f37 = (float)Math.sqrt(f34 * f34 + f35 * f35 + f36 * f36);
                f21 = f34 / f37;
                f20 = f35 / f37;
                float f38 = f36 / f37;
                f34 = f21;
                f35 = f20;
                f36 = f38;
                f37 = f2;
                float f39 = f3;
                float f40 = f4;
                float f41 = f7;
                float f42 = f8;
                float f43 = f9;
                float f44 = 0.0f;
                float f45 = 0.0f;
                float f46 = 1.0f;
                float f47 = f37 + f44;
                float f48 = f39 + f45;
                float f49 = f40 + f46;
                float f50 = (float)Math.sqrt(f47 * f47 + f48 * f48 + f49 * f49);
                f21 = f47 / f50;
                f20 = f48 / f50;
                f38 = f49 / f50;
                f47 = f21;
                f48 = f20;
                f49 = f38;
                float f51 = f34;
                float f52 = f35;
                float f53 = f36;
                float f54 = f37;
                float f55 = f39;
                float f56 = f40;
                float f57 = f51 * f54 + f52 * f55 + f53 * f56;
                f50 = f6 * f57 * f41;
                float f58 = f6 * f57 * f42;
                float f59 = f6 * f57 * f43;
                f54 = f50;
                f55 = f58;
                f56 = f59;
                float f60 = 0.0f;
                float f61 = 1.0f;
                float f62 = f54 < f60 ? f60 : (f51 = f54 > f61 ? f61 : f54);
                float f63 = f55 < f60 ? f60 : (f52 = f55 > f61 ? f61 : f55);
                f53 = f56 < f60 ? f60 : (f56 > f61 ? f61 : f56);
                f50 = f51;
                f58 = f52;
                f59 = f53;
                float f64 = 1.0f;
                f61 = f34;
                float f65 = f35;
                float f66 = f36;
                float f67 = f47;
                float f68 = f48;
                float f69 = f49;
                f66 = f61 = (f57 = f61 * f67 + f65 * f68 + f66 * f69);
                f67 = f5;
                f65 = (float)Math.pow(f66, f67);
                f54 = f10 * f65 * f41;
                f55 = f10 * f65 * f42;
                f56 = f10 * f65 * f43;
                f67 = f54;
                f68 = f55;
                f67 = f60 = (f66 = f67 > f68 ? f67 : f68);
                f68 = f56;
                f60 = f66 = f67 > f68 ? f67 : f68;
                float f70 = (f54 *= (f30 *= f64)) + (f23 *= f50) * (1.0f - (f60 *= f30));
                float f71 = (f55 *= f30) + (f24 *= f58) * (1.0f - f60);
                float f72 = (f56 *= f30) + (f29 *= f59) * (1.0f - f60);
                float f73 = f60 + f30 * (1.0f - f60);
                if (f73 < 0.0f) {
                    f73 = 0.0f;
                } else if (f73 > 1.0f) {
                    f73 = 1.0f;
                }
                if (f70 < 0.0f) {
                    f70 = 0.0f;
                } else if (f70 > f73) {
                    f70 = f73;
                }
                if (f71 < 0.0f) {
                    f71 = 0.0f;
                } else if (f71 > f73) {
                    f71 = f73;
                }
                if (f72 < 0.0f) {
                    f72 = 0.0f;
                } else if (f72 > f73) {
                    f72 = f73;
                }
                arrn3[n15 + i3] = (int)(f70 * 255.0f) << 16 | (int)(f71 * 255.0f) << 8 | (int)(f72 * 255.0f) << 0 | (int)(f73 * 255.0f) << 24;
                f18 += f11;
                f19 += f13;
            }
            f15 += f12;
            f16 += f14;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        arrimageData[1].releaseTransformedImage(heapImage2);
        return new ImageData(this.getFilterContext(), heapImage3, rectangle2);
    }
}

