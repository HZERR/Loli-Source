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

public class JSWPhongLighting_POINTPeer
extends JSWEffectPeer {
    private FloatBuffer kvals;

    public JSWPhongLighting_POINTPeer(FilterContext filterContext, Renderer renderer, String string) {
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
        float[] arrf3 = this.getLightPosition();
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
        float f11 = this.getSurfaceScale();
        float f12 = (arrf[2] - arrf[0]) / (float)n12;
        float f13 = (arrf[3] - arrf[1]) / (float)n13;
        float f14 = (arrf2[2] - arrf2[0]) / (float)n12;
        float f15 = (arrf2[3] - arrf2[1]) / (float)n13;
        float f16 = arrf[1] + f13 * 0.5f;
        float f17 = arrf2[1] + f15 * 0.5f;
        for (int i2 = 0; i2 < 0 + n13; ++i2) {
            float f18 = i2;
            int n15 = i2 * n14;
            float f19 = arrf[0] + f12 * 0.5f;
            float f20 = arrf2[0] + f14 * 0.5f;
            for (int i3 = 0; i3 < 0 + n12; ++i3) {
                int n16;
                float f21;
                float f22;
                int n17;
                int n18;
                float f23 = i3;
                float f24 = f20;
                float f25 = f17;
                if (f24 >= 0.0f && f25 >= 0.0f) {
                    int n19 = (int)(f24 * (float)n9);
                    n18 = (int)(f25 * (float)n10);
                    boolean bl = n19 >= n9 || n18 >= n10;
                    n17 = bl ? 0 : arrn2[n18 * n11 + n19];
                } else {
                    n17 = 0;
                }
                float f26 = (float)(n17 >> 16 & 0xFF) / 255.0f;
                float f27 = (float)(n17 >> 8 & 0xFF) / 255.0f;
                float f28 = (float)(n17 & 0xFF) / 255.0f;
                float f29 = (float)(n17 >>> 24) / 255.0f;
                f24 = f26;
                f25 = f27;
                float f30 = f28;
                float f31 = f29;
                float f32 = 0.0f;
                float f33 = 0.0f;
                float f34 = 1.0f;
                for (n18 = 0; n18 < 8; ++n18) {
                    int n20;
                    f22 = f19 + arrf4[n18 * 4 + 0];
                    f21 = f16 + arrf4[n18 * 4 + 1];
                    if (f22 >= 0.0f && f21 >= 0.0f) {
                        int n21 = (int)(f22 * (float)n4);
                        int n22 = (int)(f21 * (float)n5);
                        boolean bl = n21 >= n4 || n22 >= n5;
                        n20 = bl ? 0 : arrn[n22 * n6 + n21];
                    } else {
                        n20 = 0;
                    }
                    f29 = (float)(n20 >>> 24) / 255.0f;
                    f32 += arrf4[n18 * 4 + 2] * f29;
                    f33 += arrf4[n18 * 4 + 3] * f29;
                }
                float f35 = f32;
                float f36 = f33;
                float f37 = f34;
                float f38 = (float)Math.sqrt(f35 * f35 + f36 * f36 + f37 * f37);
                f22 = f35 / f38;
                f21 = f36 / f38;
                float f39 = f37 / f38;
                f35 = f22;
                f36 = f21;
                f37 = f39;
                f38 = f19;
                float f40 = f16;
                if (f38 >= 0.0f && f40 >= 0.0f) {
                    int n23 = (int)(f38 * (float)n4);
                    int n24 = (int)(f40 * (float)n5);
                    boolean bl = n23 >= n4 || n24 >= n5;
                    n16 = bl ? 0 : arrn[n24 * n6 + n23];
                } else {
                    n16 = 0;
                }
                f38 = f29 = (float)(n16 >>> 24) / 255.0f;
                f40 = f23;
                float f41 = f18;
                float f42 = f11 * f38;
                float f43 = f2 - f40;
                float f44 = f3 - f41;
                float f45 = f4 - f42;
                float f46 = (float)Math.sqrt(f43 * f43 + f44 * f44 + f45 * f45);
                f22 = f43 / f46;
                f21 = f44 / f46;
                f39 = f45 / f46;
                f43 = f22;
                f44 = f21;
                f45 = f39;
                f46 = f7;
                float f47 = f8;
                float f48 = f9;
                float f49 = 0.0f;
                float f50 = 0.0f;
                float f51 = 1.0f;
                float f52 = f43 + f49;
                float f53 = f44 + f50;
                float f54 = f45 + f51;
                float f55 = (float)Math.sqrt(f52 * f52 + f53 * f53 + f54 * f54);
                f22 = f52 / f55;
                f21 = f53 / f55;
                f39 = f54 / f55;
                f52 = f22;
                f53 = f21;
                f54 = f39;
                float f56 = f35;
                float f57 = f36;
                float f58 = f37;
                float f59 = f43;
                float f60 = f44;
                float f61 = f45;
                float f62 = f56 * f59 + f57 * f60 + f58 * f61;
                f55 = f6 * f62 * f46;
                float f63 = f6 * f62 * f47;
                float f64 = f6 * f62 * f48;
                f59 = f55;
                f60 = f63;
                f61 = f64;
                float f65 = 0.0f;
                float f66 = 1.0f;
                float f67 = f59 < f65 ? f65 : (f56 = f59 > f66 ? f66 : f59);
                float f68 = f60 < f65 ? f65 : (f57 = f60 > f66 ? f66 : f60);
                f58 = f61 < f65 ? f65 : (f61 > f66 ? f66 : f61);
                f55 = f56;
                f63 = f57;
                f64 = f58;
                float f69 = 1.0f;
                f66 = f35;
                float f70 = f36;
                float f71 = f37;
                float f72 = f52;
                float f73 = f53;
                float f74 = f54;
                f71 = f66 = (f62 = f66 * f72 + f70 * f73 + f71 * f74);
                f72 = f5;
                f70 = (float)Math.pow(f71, f72);
                f59 = f10 * f70 * f46;
                f60 = f10 * f70 * f47;
                f61 = f10 * f70 * f48;
                f72 = f59;
                f73 = f60;
                f72 = f65 = (f71 = f72 > f73 ? f72 : f73);
                f73 = f61;
                f65 = f71 = f72 > f73 ? f72 : f73;
                float f75 = (f59 *= (f31 *= f69)) + (f24 *= f55) * (1.0f - (f65 *= f31));
                float f76 = (f60 *= f31) + (f25 *= f63) * (1.0f - f65);
                float f77 = (f61 *= f31) + (f30 *= f64) * (1.0f - f65);
                float f78 = f65 + f31 * (1.0f - f65);
                if (f78 < 0.0f) {
                    f78 = 0.0f;
                } else if (f78 > 1.0f) {
                    f78 = 1.0f;
                }
                if (f75 < 0.0f) {
                    f75 = 0.0f;
                } else if (f75 > f78) {
                    f75 = f78;
                }
                if (f76 < 0.0f) {
                    f76 = 0.0f;
                } else if (f76 > f78) {
                    f76 = f78;
                }
                if (f77 < 0.0f) {
                    f77 = 0.0f;
                } else if (f77 > f78) {
                    f77 = f78;
                }
                arrn3[n15 + i3] = (int)(f75 * 255.0f) << 16 | (int)(f76 * 255.0f) << 8 | (int)(f77 * 255.0f) << 0 | (int)(f78 * 255.0f) << 24;
                f19 += f12;
                f20 += f14;
            }
            f16 += f13;
            f17 += f15;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        arrimageData[1].releaseTransformedImage(heapImage2);
        return new ImageData(this.getFilterContext(), heapImage3, rectangle2);
    }
}

