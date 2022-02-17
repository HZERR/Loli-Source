/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;

public class NGCircle
extends NGShape {
    static final float HALF_SQRT_HALF = 0.353f;
    private Ellipse2D ellipse = new Ellipse2D();
    private float cx;
    private float cy;

    public void updateCircle(float f2, float f3, float f4) {
        this.ellipse.x = f2 - f4;
        this.ellipse.y = f3 - f4;
        this.ellipse.height = this.ellipse.width = f4 * 2.0f;
        this.cx = f2;
        this.cy = f3;
        this.geometryChanged();
    }

    @Override
    public Shape getShape() {
        return this.ellipse;
    }

    @Override
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override
    protected boolean hasOpaqueRegion() {
        return super.hasOpaqueRegion() && this.ellipse.width > 0.0f;
    }

    @Override
    protected RectBounds computeOpaqueRegion(RectBounds rectBounds) {
        float f2 = this.ellipse.width * 0.353f;
        return (RectBounds)rectBounds.deriveWithNewBounds(this.cx - f2, this.cy - f2, 0.0f, this.cx + f2, this.cy + f2, 0.0f);
    }

    @Override
    protected ShapeRep createShapeRep(Graphics graphics) {
        return graphics.getResourceFactory().createEllipseRep();
    }
}

