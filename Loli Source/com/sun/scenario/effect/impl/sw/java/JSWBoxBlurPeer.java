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

public class JSWBoxBlurPeer
extends JSWEffectPeer<BoxRenderState> {
    public JSWBoxBlurPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    public ImageData filter(Effect effect, BoxRenderState boxRenderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setRenderState(boxRenderState);
        boolean bl = this.getPass() == 0;
        int n2 = bl ? boxRenderState.getBoxPixelSize(0) - 1 : 0;
        int n3 = bl ? 0 : boxRenderState.getBoxPixelSize(1) - 1;
        int n4 = boxRenderState.getBlurPasses();
        if (n4 < 1 || n2 < 1 && n3 < 1) {
            arrimageData[0].addref();
            return arrimageData[0];
        }
        int n5 = n2 * n4 + 1 & 0xFFFFFFFE;
        int n6 = n3 * n4 + 1 & 0xFFFFFFFE;
        HeapImage heapImage = (HeapImage)arrimageData[0].getUntransformedImage();
        Rectangle rectangle2 = arrimageData[0].getUntransformedBounds();
        HeapImage heapImage2 = heapImage;
        int n7 = rectangle2.width;
        int n8 = rectangle2.height;
        int n9 = heapImage2.getScanlineStride();
        int[] arrn = heapImage2.getPixelArray();
        int n10 = n7 + n5;
        int n11 = n8 + n6;
        while (n7 < n10 || n8 < n11) {
            int n12 = n7 + n2;
            int n13 = n8 + n3;
            if (n12 > n10) {
                n12 = n10;
            }
            if (n13 > n11) {
                n13 = n11;
            }
            HeapImage heapImage3 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n12, n13));
            int n14 = heapImage3.getScanlineStride();
            int[] arrn2 = heapImage3.getPixelArray();
            if (bl) {
                this.filterHorizontal(arrn2, n12, n13, n14, arrn, n7, n8, n9);
            } else {
                this.filterVertical(arrn2, n12, n13, n14, arrn, n7, n8, n9);
            }
            if (heapImage2 != heapImage) {
                this.getRenderer().releaseCompatibleImage(heapImage2);
            }
            heapImage2 = heapImage3;
            n7 = n12;
            n8 = n13;
            arrn = arrn2;
            n9 = n14;
        }
        Rectangle rectangle3 = new Rectangle(rectangle2.x - n5 / 2, rectangle2.y - n6 / 2, n7, n8);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle3);
    }

    protected void filterHorizontal(int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5, int n6, int n7) {
        int n8 = n2 - n5 + 1;
        int n9 = Integer.MAX_VALUE / (n8 * 255);
        int n10 = 0;
        int n11 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n12 = 0;
            int n13 = 0;
            int n14 = 0;
            int n15 = 0;
            for (int i3 = 0; i3 < n2; ++i3) {
                int n16 = i3 >= n8 ? arrn2[n10 + i3 - n8] : 0;
                n12 -= n16 >>> 24;
                n13 -= n16 >> 16 & 0xFF;
                n14 -= n16 >> 8 & 0xFF;
                n15 -= n16 & 0xFF;
                n16 = i3 < n5 ? arrn2[n10 + i3] : 0;
                arrn[n11 + i3] = ((n12 += n16 >>> 24) * n9 >> 23 << 24) + ((n13 += n16 >> 16 & 0xFF) * n9 >> 23 << 16) + ((n14 += n16 >> 8 & 0xFF) * n9 >> 23 << 8) + ((n15 += n16 & 0xFF) * n9 >> 23);
            }
            n10 += n7;
            n11 += n4;
        }
    }

    protected void filterVertical(int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5, int n6, int n7) {
        int n8 = n3 - n6 + 1;
        int n9 = Integer.MAX_VALUE / (n8 * 255);
        int n10 = n8 * n7;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n11 = 0;
            int n12 = 0;
            int n13 = 0;
            int n14 = 0;
            int n15 = i2;
            int n16 = i2;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n17 = n15 >= n10 ? arrn2[n15 - n10] : 0;
                n11 -= n17 >>> 24;
                n12 -= n17 >> 16 & 0xFF;
                n13 -= n17 >> 8 & 0xFF;
                n14 -= n17 & 0xFF;
                n17 = i3 < n6 ? arrn2[n15] : 0;
                arrn[n16] = ((n11 += n17 >>> 24) * n9 >> 23 << 24) + ((n12 += n17 >> 16 & 0xFF) * n9 >> 23 << 16) + ((n13 += n17 >> 8 & 0xFF) * n9 >> 23 << 8) + ((n14 += n17 & 0xFF) * n9 >> 23);
                n15 += n7;
                n16 += n4;
            }
        }
    }
}

