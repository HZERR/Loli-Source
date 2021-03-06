/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGShape;
import javafx.scene.shape.FillRule;

public class NGPath
extends NGShape {
    private Path2D p = new Path2D();

    public void reset() {
        this.p.reset();
    }

    public void update() {
        this.geometryChanged();
    }

    private int toWindingRule(FillRule fillRule) {
        if (fillRule == FillRule.NON_ZERO) {
            return 1;
        }
        return 0;
    }

    public void setFillRule(FillRule fillRule) {
        this.p.setWindingRule(this.toWindingRule(fillRule));
    }

    public float getCurrentX() {
        return this.p.getCurrentPoint().x;
    }

    public float getCurrentY() {
        return this.p.getCurrentPoint().y;
    }

    public void addClosePath() {
        this.p.closePath();
    }

    public void addMoveTo(float f2, float f3) {
        this.p.moveTo(f2, f3);
    }

    public void addLineTo(float f2, float f3) {
        this.p.lineTo(f2, f3);
    }

    public void addQuadTo(float f2, float f3, float f4, float f5) {
        this.p.quadTo(f2, f3, f4, f5);
    }

    public void addCubicTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.p.curveTo(f2, f3, f4, f5, f6, f7);
    }

    public void addArcTo(float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        Arc2D arc2D = new Arc2D(f2, f3, f4, f5, f6, f7, 0);
        BaseTransform baseTransform = (double)f8 == 0.0 ? null : BaseTransform.getRotateInstance(f8, arc2D.getCenterX(), arc2D.getCenterY());
        PathIterator pathIterator = arc2D.getPathIterator(baseTransform);
        pathIterator.next();
        this.p.append(pathIterator, true);
    }

    public Path2D getGeometry() {
        return this.p;
    }

    @Override
    public Shape getShape() {
        return this.p;
    }

    public boolean acceptsPath2dOnUpdate() {
        return true;
    }

    public void updateWithPath2d(Path2D path2D) {
        this.p.setTo(path2D);
        this.geometryChanged();
    }
}

