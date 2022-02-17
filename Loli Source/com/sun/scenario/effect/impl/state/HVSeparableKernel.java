/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

public abstract class HVSeparableKernel
extends LinearConvolveKernel {
    @Override
    public final Rectangle getResultBounds(Rectangle rectangle, int n2) {
        int n3 = this.getKernelSize(n2);
        Rectangle rectangle2 = new Rectangle(rectangle);
        if (n2 == 0) {
            rectangle2.grow(n3 / 2, 0);
        } else {
            rectangle2.grow(0, n3 / 2);
        }
        return rectangle2;
    }
}

