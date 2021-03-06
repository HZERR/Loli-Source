/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.sg.prism.NGShape;

public class NGCubicCurve
extends NGShape {
    private CubicCurve2D curve = new CubicCurve2D();

    @Override
    public final Shape getShape() {
        return this.curve;
    }

    public void updateCubicCurve(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.curve.x1 = f2;
        this.curve.y1 = f3;
        this.curve.x2 = f4;
        this.curve.y2 = f5;
        this.curve.ctrlx1 = f6;
        this.curve.ctrly1 = f7;
        this.curve.ctrlx2 = f8;
        this.curve.ctrly2 = f9;
        this.geometryChanged();
    }
}

