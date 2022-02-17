/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Dimension2D;
import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;

public abstract class RectangularShape
extends Shape {
    protected RectangularShape() {
    }

    public abstract float getX();

    public abstract float getY();

    public abstract float getWidth();

    public abstract float getHeight();

    public float getMinX() {
        return this.getX();
    }

    public float getMinY() {
        return this.getY();
    }

    public float getMaxX() {
        return this.getX() + this.getWidth();
    }

    public float getMaxY() {
        return this.getY() + this.getHeight();
    }

    public float getCenterX() {
        return this.getX() + this.getWidth() / 2.0f;
    }

    public float getCenterY() {
        return this.getY() + this.getHeight() / 2.0f;
    }

    public abstract boolean isEmpty();

    public abstract void setFrame(float var1, float var2, float var3, float var4);

    public void setFrame(Point2D point2D, Dimension2D dimension2D) {
        this.setFrame(point2D.x, point2D.y, dimension2D.width, dimension2D.height);
    }

    public void setFrameFromDiagonal(float f2, float f3, float f4, float f5) {
        float f6;
        if (f4 < f2) {
            f6 = f2;
            f2 = f4;
            f4 = f6;
        }
        if (f5 < f3) {
            f6 = f3;
            f3 = f5;
            f5 = f6;
        }
        this.setFrame(f2, f3, f4 - f2, f5 - f3);
    }

    public void setFrameFromDiagonal(Point2D point2D, Point2D point2D2) {
        this.setFrameFromDiagonal(point2D.x, point2D.y, point2D2.x, point2D2.y);
    }

    public void setFrameFromCenter(float f2, float f3, float f4, float f5) {
        float f6 = Math.abs(f4 - f2);
        float f7 = Math.abs(f5 - f3);
        this.setFrame(f2 - f6, f3 - f7, f6 * 2.0f, f7 * 2.0f);
    }

    public void setFrameFromCenter(Point2D point2D, Point2D point2D2) {
        this.setFrameFromCenter(point2D.x, point2D.y, point2D2.x, point2D2.y);
    }

    @Override
    public boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    @Override
    public RectBounds getBounds() {
        float f2 = this.getWidth();
        float f3 = this.getHeight();
        if (f2 < 0.0f || f3 < 0.0f) {
            return new RectBounds();
        }
        float f4 = this.getX();
        float f5 = this.getY();
        float f6 = (float)Math.floor(f4);
        float f7 = (float)Math.floor(f5);
        float f8 = (float)Math.ceil(f4 + f2);
        float f9 = (float)Math.ceil(f5 + f3);
        return new RectBounds(f6, f7, f8, f9);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f2) {
        return new FlatteningPathIterator(this.getPathIterator(baseTransform), f2);
    }

    public String toString() {
        return this.getClass().getName() + "[x=" + this.getX() + ",y=" + this.getY() + ",w=" + this.getWidth() + ",h=" + this.getHeight() + "]";
    }
}

