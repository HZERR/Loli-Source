/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;
import com.sun.scenario.effect.impl.sw.sse.SSEEffectPeer;

public class SSEBoxShadowPeer
extends SSEEffectPeer<BoxRenderState> {
    public SSEBoxShadowPeer(FilterContext filterContext, Renderer renderer, String string) {
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
                SSEBoxShadowPeer.filterHorizontalBlack(arrn2, n13, n14, n15, arrn, n8, n9, n10, f2);
            } else if (n13 < n11 || n14 < n12) {
                SSEBoxShadowPeer.filterVerticalBlack(arrn2, n13, n14, n15, arrn, n8, n9, n10, f2);
            } else {
                float[] arrf = boxRenderState.getShadowColor().getPremultipliedRGBComponents();
                if (arrf[3] == 1.0f && arrf[0] == 0.0f && arrf[1] == 0.0f && arrf[2] == 0.0f) {
                    SSEBoxShadowPeer.filterVerticalBlack(arrn2, n13, n14, n15, arrn, n8, n9, n10, f2);
                } else {
                    SSEBoxShadowPeer.filterVertical(arrn2, n13, n14, n15, arrn, n8, n9, n10, f2, arrf);
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
        return new ImageData(this.getFilterContext(), heapImage2, rectangle3, arrimageData[0].getTransform());
    }

    private static native void filterHorizontalBlack(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float var8);

    private static native void filterVerticalBlack(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float var8);

    private static native void filterVertical(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float var8, float[] var9);
}

