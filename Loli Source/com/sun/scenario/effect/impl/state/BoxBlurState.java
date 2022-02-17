/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.impl.state.BoxRenderState;
import com.sun.scenario.effect.impl.state.HVSeparableKernel;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

public class BoxBlurState
extends HVSeparableKernel {
    private int hsize;
    private int vsize;
    private int blurPasses;

    public int getHsize() {
        return this.hsize;
    }

    public void setHsize(int n2) {
        if (n2 < 0 || n2 > 255) {
            throw new IllegalArgumentException("Blur size must be in the range [0,255]");
        }
        this.hsize = n2;
    }

    public int getVsize() {
        return this.vsize;
    }

    public void setVsize(int n2) {
        if (n2 < 0 || n2 > 255) {
            throw new IllegalArgumentException("Blur size must be in the range [0,255]");
        }
        this.vsize = n2;
    }

    public int getBlurPasses() {
        return this.blurPasses;
    }

    public void setBlurPasses(int n2) {
        if (n2 < 0 || n2 > 3) {
            throw new IllegalArgumentException("Number of passes must be in the range [0,3]");
        }
        this.blurPasses = n2;
    }

    public Color4f getShadowColor() {
        return null;
    }

    public float getSpread() {
        return 0.0f;
    }

    @Override
    public LinearConvolveRenderState getRenderState(BaseTransform baseTransform) {
        return new BoxRenderState(this.hsize, this.vsize, this.blurPasses, this.getSpread(), this.isShadow(), this.getShadowColor(), baseTransform);
    }

    @Override
    public boolean isNop() {
        return this.blurPasses == 0 || this.hsize <= 1 && this.vsize <= 1;
    }

    @Override
    public int getKernelSize(int n2) {
        int n3;
        int n4 = n3 = n2 == 0 ? this.hsize : this.vsize;
        if (n3 < 1) {
            n3 = 1;
        }
        n3 = (n3 - 1) * this.blurPasses + 1;
        return n3 |= 1;
    }
}

