/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.impl.state.GaussianBlurState;

public class GaussianShadowState
extends GaussianBlurState {
    private Color4f shadowColor;
    private float spread;

    @Override
    void checkRadius(float f2) {
        if (f2 < 0.0f || f2 > 127.0f) {
            throw new IllegalArgumentException("Radius must be in the range [1,127]");
        }
    }

    @Override
    public Color4f getShadowColor() {
        return this.shadowColor;
    }

    public void setShadowColor(Color4f color4f) {
        if (color4f == null) {
            throw new IllegalArgumentException("Color must be non-null");
        }
        this.shadowColor = color4f;
    }

    @Override
    public float getSpread() {
        return this.spread;
    }

    public void setSpread(float f2) {
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Spread must be in the range [0,1]");
        }
        this.spread = f2;
    }

    @Override
    public boolean isNop() {
        return false;
    }

    @Override
    public boolean isShadow() {
        return true;
    }
}

