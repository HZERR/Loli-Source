/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Renderer;
import com.sun.pisces.PiscesRenderer;

final class DirectRTPiscesAlphaConsumer
implements AlphaConsumer {
    private byte[] alpha_map;
    private int outpix_xmin;
    private int outpix_ymin;
    private int w;
    private int h;
    private int rowNum;
    private PiscesRenderer pr;

    DirectRTPiscesAlphaConsumer() {
    }

    void initConsumer(Renderer renderer, PiscesRenderer piscesRenderer) {
        this.outpix_xmin = renderer.getOutpixMinX();
        this.outpix_ymin = renderer.getOutpixMinY();
        this.w = renderer.getOutpixMaxX() - this.outpix_xmin;
        if (this.w < 0) {
            this.w = 0;
        }
        this.h = renderer.getOutpixMaxY() - this.outpix_ymin;
        if (this.h < 0) {
            this.h = 0;
        }
        this.rowNum = 0;
        this.pr = piscesRenderer;
    }

    @Override
    public int getOriginX() {
        return this.outpix_xmin;
    }

    @Override
    public int getOriginY() {
        return this.outpix_ymin;
    }

    @Override
    public int getWidth() {
        return this.w;
    }

    @Override
    public int getHeight() {
        return this.h;
    }

    @Override
    public void setMaxAlpha(int n2) {
        if (this.alpha_map == null || this.alpha_map.length != n2 + 1) {
            this.alpha_map = new byte[n2 + 1];
            for (int i2 = 0; i2 <= n2; ++i2) {
                this.alpha_map[i2] = (byte)((i2 * 255 + n2 / 2) / n2);
            }
        }
    }

    @Override
    public void setAndClearRelativeAlphas(int[] arrn, int n2, int n3, int n4) {
        this.pr.emitAndClearAlphaRow(this.alpha_map, arrn, n2, n3, n4, this.rowNum);
        ++this.rowNum;
    }
}

