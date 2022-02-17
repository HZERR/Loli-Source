/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.Ref;
import java.util.Arrays;

public final class WCTransform
extends Ref {
    private final double[] m = new double[6];

    public WCTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.m[0] = d2;
        this.m[1] = d3;
        this.m[2] = d4;
        this.m[3] = d5;
        this.m[4] = d6;
        this.m[5] = d7;
    }

    public double[] getMatrix() {
        return Arrays.copyOf(this.m, this.m.length);
    }
}

