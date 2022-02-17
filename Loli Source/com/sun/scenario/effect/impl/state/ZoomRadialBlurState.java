/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.ZoomRadialBlur;

public class ZoomRadialBlurState {
    private float dx = -1.0f;
    private float dy = -1.0f;
    private final ZoomRadialBlur effect;

    public ZoomRadialBlurState(ZoomRadialBlur zoomRadialBlur) {
        this.effect = zoomRadialBlur;
    }

    public int getRadius() {
        return this.effect.getRadius();
    }

    public void updateDeltas(float f2, float f3) {
        this.dx = f2;
        this.dy = f3;
    }

    public void invalidateDeltas() {
        this.dx = -1.0f;
        this.dy = -1.0f;
    }

    public float getDx() {
        return this.dx;
    }

    public float getDy() {
        return this.dy;
    }

    public int getNumSteps() {
        int n2 = this.getRadius();
        return n2 * 2 + 1;
    }

    public float getAlpha() {
        float f2 = this.getRadius();
        return 1.0f / (2.0f * f2 + 1.0f);
    }
}

