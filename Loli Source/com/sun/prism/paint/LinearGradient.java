/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.Stop;
import java.util.List;

public final class LinearGradient
extends Gradient {
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public LinearGradient(float f2, float f3, float f4, float f5, BaseTransform baseTransform, boolean bl, int n2, List<Stop> list) {
        super(Paint.Type.LINEAR_GRADIENT, baseTransform, bl, n2, list);
        this.x1 = f2;
        this.y1 = f3;
        this.x2 = f4;
        this.y2 = f5;
    }

    public float getX1() {
        return this.x1;
    }

    public float getY1() {
        return this.y1;
    }

    public float getX2() {
        return this.x2;
    }

    public float getY2() {
        return this.y2;
    }
}

