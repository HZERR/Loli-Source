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

public class JSWPhongLighting_SPOTPeer
extends JSWEffectPeer {
    private FloatBuffer kvals;

    public JSWPhongLighting_SPOTPeer(FilterContext filterContext, Renderer renderer, String string) {
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
        float f5 = this.getLightSpecularExponent();
        float f6 = this.getSpecularExponent();
        FloatBuffer floatBuffer = this.getKvals();
        float[] arrf4 = new float[floatBuffer.capacity()];
        floatBuffer.get(arrf4);
        float f7 = this.getDiffuseConstant();
        float[] arrf5 = this.getLightColor();
        float f8 = arrf5[0];
        float f9 = arrf5[1];
        float f10 = arrf5[2];
        float[] arrf6 = this.getNormalizedLightDirection();
        float f11 = arrf6[0];
        float f12 = arrf6[1];
        float f13 = arrf6[2];
        float f14 = this.getSpecularConstant();
        float f15 = this.getSurfaceScale();
        float f16 = (arrf[2] - arrf[0]) / (float)n12;
        float f17 = (arrf[3] - arrf[1]) / (float)n13;
        float f18 = (arrf2[2] - arrf2[0]) / (float)n12;
        float f19 = (arrf2[3] - arrf2[1]) / (float)n13;
        float f20 = arrf[1] + f17 * 0.5f;
        float f21 = arrf2[1] + f19 * 0.5f;
        for (int i2 = 0; i2 < 0 + n13; ++i2) {
            float f22 = i2;
            int n15 = i2 * n14;
            float f23 = arrf[0] + f16 * 0.5f;
            float f24 = arrf2[0] + f18 * 0.5f;
            for (int i3 = 0; i3 < 0 + n12; ++i3) {
                int n16;
                float f25;
                float f26;
                int n17;
                int n18;
                float f27 = i3;
                float f28 = f24;
                float f29 = f21;
                if (f28 >= 0.0f && f29 >= 0.0f) {
                    int n19 = (int)(f28 * (float)n9);
                    n18 = (int)(f29 * (float)n10);
                    boolean bl = n19 >= n9 || n18 >= n10;
                    n17 = bl ? 0 : arrn2[n18 * n11 + n19];
                } else {
                    n17 = 0;
                }
                float f30 = (float)(n17 >> 16 & 0xFF) / 255.0f;
                float f31 = (float)(n17 >> 8 & 0xFF) / 255.0f;
                float f32 = (float)(n17 & 0xFF) / 255.0f;
                float f33 = (float)(n17 >>> 24) / 255.0f;
                f28 = f30;
                f29 = f31;
                float f34 = f32;
                float f35 = f33;
                float f36 = 0.0f;
                float f37 = 0.0f;
                float f38 = 1.0f;
                for (n18 = 0; n18 < 8; ++n18) {
                    int n20;
                    f26 = f23 + arrf4[n18 * 4 + 0];
                    f25 = f20 + arrf4[n18 * 4 + 1];
                    if (f26 >= 0.0f && f25 >= 0.0f) {
                        int n21 = (int)(f26 * (float)n4);
                        int n22 = (int)(f25 * (float)n5);
                        boolean bl = n21 >= n4 || n22 >= n5;
                        n20 = bl ? 0 : arrn[n22 * n6 + n21];
                    } else {
                        n20 = 0;
                    }
                    f33 = (float)(n20 >>> 24) / 255.0f;
                    f36 += arrf4[n18 * 4 + 2] * f33;
                    f37 += arrf4[n18 * 4 + 3] * f33;
                }
                float f39 = f36;
                float f40 = f37;
                float f41 = f38;
                float f42 = (float)Math.sqrt(f39 * f39 + f40 * f40 + f41 * f41);
                f26 = f39 / f42;
                f25 = f40 / f42;
                float f43 = f41 / f42;
                f39 = f26;
                f40 = f25;
                f41 = f43;
                f42 = f23;
                float f44 = f20;
                if (f42 >= 0.0f && f44 >= 0.0f) {
                    int n23 = (int)(f42 * (float)n4);
                    int n24 = (int)(f44 * (float)n5);
                    boolean bl = n23 >= n4 || n24 >= n5;
                    n16 = bl ? 0 : arrn[n24 * n6 + n23];
                } else {
                    n16 = 0;
                }
                f42 = f33 = (float)(n16 >>> 24) / 255.0f;
                f44 = f27;
                float f45 = f22;
                float f46 = f15 * f42;
                float f47 = f2 - f44;
                float f48 = f3 - f45;
                float f49 = f4 - f46;
                float f50 = (float)Math.sqrt(f47 * f47 + f48 * f48 + f49 * f49);
                f26 = f47 / f50;
                f25 = f48 / f50;
                f43 = f49 / f50;
                f47 = f26;
                f48 = f25;
                f49 = f43;
                float f51 = f47;
                float f52 = f48;
                float f53 = f49;
                float f54 = f11;
                float f55 = f12;
                float f56 = f13;
                f53 = f51 = (f50 = f51 * f54 + f52 * f55 + f53 * f56);
                f54 = 0.0f;
                f51 = f52 = f53 < f54 ? f53 : f54;
                f54 = -f51;
                f55 = f5;
                f53 = (float)Math.pow(f54, f55);
                f54 = f8 * f53;
                f55 = f9 * f53;
                f56 = f10 * f53;
                float f57 = 0.0f;
                float f58 = 0.0f;
                float f59 = 1.0f;
                float f60 = f47 + f57;
                float f61 = f48 + f58;
                float f62 = f49 + f59;
                float f63 = (float)Math.sqrt(f60 * f60 + f61 * f61 + f62 * f62);
                f26 = f60 / f63;
                f25 = f61 / f63;
                f43 = f62 / f63;
                f60 = f26;
                f61 = f25;
                f62 = f43;
                float f64 = f39;
                float f65 = f40;
                float f66 = f41;
                float f67 = f47;
                float f68 = f48;
                float f69 = f49;
                f50 = f64 * f67 + f65 * f68 + f66 * f69;
                f63 = f7 * f50 * f54;
                float f70 = f7 * f50 * f55;
                float f71 = f7 * f50 * f56;
                f67 = f63;
                f68 = f70;
                f69 = f71;
                float f72 = 0.0f;
                float f73 = 1.0f;
                float f74 = f67 < f72 ? f72 : (f64 = f67 > f73 ? f73 : f67);
                float f75 = f68 < f72 ? f72 : (f65 = f68 > f73 ? f73 : f68);
                f66 = f69 < f72 ? f72 : (f69 > f73 ? f73 : f69);
                f63 = f64;
                f70 = f65;
                f71 = f66;
                float f76 = 1.0f;
                f73 = f39;
                float f77 = f40;
                float f78 = f41;
                float f79 = f60;
                float f80 = f61;
                float f81 = f62;
                f77 = f73 = (f50 = f73 * f79 + f77 * f80 + f78 * f81);
                f78 = f6;
                f53 = (float)Math.pow(f77, f78);
                f67 = f14 * f53 * f54;
                f68 = f14 * f53 * f55;
                f69 = f14 * f53 * f56;
                f78 = f67;
                f79 = f68;
                f78 = f72 = (f77 = f78 > f79 ? f78 : f79);
                f79 = f69;
                f72 = f77 = f78 > f79 ? f78 : f79;
                float f82 = (f67 *= (f35 *= f76)) + (f28 *= f63) * (1.0f - (f72 *= f35));
                float f83 = (f68 *= f35) + (f29 *= f70) * (1.0f - f72);
                float f84 = (f69 *= f35) + (f34 *= f71) * (1.0f - f72);
                float f85 = f72 + f35 * (1.0f - f72);
                if (f85 < 0.0f) {
                    f85 = 0.0f;
                } else if (f85 > 1.0f) {
                    f85 = 1.0f;
                }
                if (f82 < 0.0f) {
                    f82 = 0.0f;
                } else if (f82 > f85) {
                    f82 = f85;
                }
                if (f83 < 0.0f) {
                    f83 = 0.0f;
                } else if (f83 > f85) {
                    f83 = f85;
                }
                if (f84 < 0.0f) {
                    f84 = 0.0f;
                } else if (f84 > f85) {
                    f84 = f85;
                }
                arrn3[n15 + i3] = (int)(f82 * 255.0f) << 16 | (int)(f83 * 255.0f) << 8 | (int)(f84 * 255.0f) << 0 | (int)(f85 * 255.0f) << 24;
                f23 += f16;
                f24 += f18;
            }
            f20 += f17;
            f21 += f19;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        arrimageData[1].releaseTransformedImage(heapImage2);
        return new ImageData(this.getFilterContext(), heapImage3, rectangle2);
    }
}

