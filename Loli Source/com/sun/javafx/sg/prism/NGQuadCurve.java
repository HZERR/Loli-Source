/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.QuadCurve2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.sg.prism.NGShape;

public class NGQuadCurve
extends NGShape {
    private QuadCurve2D curve = new QuadCurve2D();

    @Override
    public final Shape getShape() {
        return this.curve;
    }

    public void updateQuadCurve(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.curve.x1 = f2;
        this.curve.y1 = f3;
        this.curve.x2 = f4;
        this.curve.y2 = f5;
        this.curve.ctrlx = f6;
        this.curve.ctrly = f7;
        this.geometryChanged();
    }
}

