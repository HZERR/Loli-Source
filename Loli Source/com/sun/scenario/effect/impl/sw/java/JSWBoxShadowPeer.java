/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWBoxShadowPeer
extends JSWEffectPeer<BoxRenderState> {
    public JSWBoxShadowPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    public ImageData filter(Effect effect, BoxRenderState boxRenderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        boolean bl;
        int n2;
        this.setRenderState(boxRenderState);
        boolean bl2 = this.getPass() == 0;
        int n3 = bl2 ? boxRenderState.getBoxPixelSize(0) - 1 : 0;
        int n4 = n2 = bl2 ? 0 : boxRenderState.getBoxPixelSize(1) - 1;
        if (n3 < 0) {
            n3 = 0;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        int n5 = boxRenderState.getBlurPasses();
        float f2 = boxRenderState.getSpread();
        if (bl2 && (n5 < 1 || n3 < 1 && n2 < 1)) {
            arrimageData[0].addref();
            return arrimageData[0];
        }
        int n6 = n3 * n5 + 1 & 0xFFFFFFFE;
        int n7 = n2 * n5 + 1 & 0xFFFFFFFE;
        HeapImage heapImage = (HeapImage)arrimageData[0].getUntransformedImage();
        Rectangle rectangle2 = arrimageData[0].getUntransformedBounds();
        HeapImage heapImage2 = heapImage;
        int n8 = rectangle2.width;
        int n9 = rectangle2.height;
        int n10 = heapImage2.getScanlineStride();
        int[] arrn = heapImage2.getPixelArray();
        int n11 = n8 + n6;
        int n12 = n9 + n7;
        boolean bl3 = bl = !bl2;
        while (bl || n8 < n11 || n9 < n12) {
            int n13 = n8 + n3;
            int n14 = n9 + n2;
            if (n13 > n11) {
                n13 = n11;
            }
            if (n14 > n12) {
                n14 = n12;
            }
            HeapImage heapImage3 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n13, n14));
            int n15 = heapImage3.getScanlineStride();
            int[] arrn2 = heapImage3.getPixelArray();
            if (n5 == 0) {
                f2 = 0.0f;
            }
            if (bl2) {
                this.filterHorizontalBlack(arrn2, n13, n14, n15, arrn, n8, n9, n10, f2);
            } else if (n13 < n11 || n14 < n12) {
                this.filterVerticalBlack(arrn2, n13, n14, n15, arrn, n8, n9, n10, f2);
            } else {
                float[] arrf = boxRenderState.getShadowColor().getPremultipliedRGBComponents();
                if (arrf[3] == 1.0f && arrf[0] == 0.0f && arrf[1] == 0.0f && arrf[2] == 0.0f) {
                    this.filterVerticalBlack(arrn2, n13, n14, n15, arrn, n8, n9, n10, f2);
                } else {
                    this.filterVertical(arrn2, n13, n14, n15, arrn, n8, n9, n10, f2, arrf);
                }
            }
            if (heapImage2 != heapImage) {
                this.getRenderer().releaseCompatibleImage(heapImage2);
            }
            --n5;
            bl = false;
            heapImage2 = heapImage3;
            n8 = n13;
            n9 = n14;
            arrn = arrn2;
            n10 = n15;
        }
        Rectangle rectangle3 = new Rectangle(rectangle2.x - n6 / 2, rectangle2.y - n7 / 2, n8, n9);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle3);
    }

    protected void filterHorizontalBlack(int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5, int n6, int n7, float f2) {
        int n8 = n2 - n5 + 1;
        int n9 = n8 * 255;
        n9 = (int)((float)n9 + (float)(255 - n9) * f2);
        int n10 = Integer.MAX_VALUE / n9;
        int n11 = n9 / 255;
        int n12 = 0;
        int n13 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n14 = 0;
            for (int i3 = 0; i3 < n2; ++i3) {
                int n15 = i3 >= n8 ? arrn2[n12 + i3 - n8] : 0;
                n14 -= n15 >>> 24;
                int n16 = n15 = i3 < n5 ? arrn2[n12 + i3] : 0;
                arrn[n13 + i3] = (n14 += n15 >>> 24) < n11 ? 0 : (n14 >= n9 ? -16777216 : n14 * n10 >> 23 << 24);
            }
            n12 += n7;
            n13 += n4;
        }
    }

    protected void filterVerticalBlack(int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5, int n6, int n7, float f2) {
        int n8 = n3 - n6 + 1;
        int n9 = n8 * 255;
        n9 = (int)((float)n9 + (float)(255 - n9) * f2);
        int n10 = Integer.MAX_VALUE / n9;
        int n11 = n9 / 255;
        int n12 = n8 * n7;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n13 = 0;
            int n14 = i2;
            int n15 = i2;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n16 = n14 >= n12 ? arrn2[n14 - n12] : 0;
                n13 -= n16 >>> 24;
                int n17 = n16 = i3 < n6 ? arrn2[n14] : 0;
                arrn[n15] = (n13 += n16 >>> 24) < n11 ? 0 : (n13 >= n9 ? -16777216 : n13 * n10 >> 23 << 24);
                n14 += n7;
                n15 += n4;
            }
        }
    }

    protected void filterVertical(int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5, int n6, int n7, float f2, float[] arrf) {
        int n8 = n3 - n6 + 1;
        int n9 = n8 * 255;
        n9 = (int)((float)n9 + (float)(255 - n9) * f2);
        int n10 = Integer.MAX_VALUE / n9;
        int n11 = (int)((float)n10 * arrf[0]);
        int n12 = (int)((float)n10 * arrf[1]);
        int n13 = (int)((float)n10 * arrf[2]);
        n10 = (int)((float)n10 * arrf[3]);
        int n14 = n9 / 255;
        int n15 = n8 * n7;
        int n16 = (int)(arrf[0] * 255.0f) << 16 | (int)(arrf[1] * 255.0f) << 8 | (int)(arrf[2] * 255.0f) | (int)(arrf[3] * 255.0f) << 24;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n17 = 0;
            int n18 = i2;
            int n19 = i2;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n20 = n18 >= n15 ? arrn2[n18 - n15] : 0;
                n17 -= n20 >>> 24;
                int n21 = n20 = i3 < n6 ? arrn2[n18] : 0;
                arrn[n19] = (n17 += n20 >>> 24) < n14 ? 0 : (n17 >= n9 ? n16 : n17 * n10 >> 23 << 24 | n17 * n11 >> 23 << 16 | n17 * n12 >> 23 << 8 | n17 * n13 >> 23);
                n18 += n7;
                n19 += n4;
            }
        }
    }
}

