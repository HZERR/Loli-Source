/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.EllipseIterator;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RectangularShape;
import com.sun.javafx.geom.transform.BaseTransform;

public class Ellipse2D
extends RectangularShape {
    public float x;
    public float y;
    public float width;
    public float height;

    public Ellipse2D() {
    }

    public Ellipse2D(float f2, float f3, float f4, float f5) {
        this.setFrame(f2, f3, f4, f5);
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public boolean isEmpty() {
        return this.width <= 0.0f || this.height <= 0.0f;
    }

    @Override
    public void setFrame(float f2, float f3, float f4, float f5) {
        this.x = f2;
        this.y = f3;
        this.width = f4;
        this.height = f5;
    }

    @Override
    public RectBounds getBounds() {
        return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
    }

    @Override
    public boolean contains(float f2, float f3) {
        float f4 = this.width;
        if (f4 <= 0.0f) {
            return false;
        }
        float f5 = (f2 - this.x) / f4 - 0.5f;
        float f6 = this.height;
        if (f6 <= 0.0f) {
            return false;
        }
        float f7 = (f3 - this.y) / f6 - 0.5f;
        return f5 * f5 + f7 * f7 < 0.25f;
    }

    @Override
    public boolean intersects(float f2, float f3, float f4, float f5) {
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        float f6 = this.width;
        if (f6 <= 0.0f) {
            return false;
        }
        float f7 = (f2 - this.x) / f6 - 0.5f;
        float f8 = f7 + f4 / f6;
        float f9 = this.height;
        if (f9 <= 0.0f) {
            return false;
        }
        float f10 = (f3 - this.y) / f9 - 0.5f;
        float f11 = f10 + f5 / f9;
        float f12 = f7 > 0.0f ? f7 : (f8 < 0.0f ? f8 : 0.0f);
        float f13 = f10 > 0.0f ? f10 : (f11 < 0.0f ? f11 : 0.0f);
        return f12 * f12 + f13 * f13 < 0.25f;
    }

    @Override
    public boolean contains(float f2, float f3, float f4, float f5) {
        return this.contains(f2, f3) && this.contains(f2 + f4, f3) && this.contains(f2, f3 + f5) && this.contains(f2 + f4, f3 + f5);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new EllipseIterator(this, baseTransform);
    }

    @Override
    public Ellipse2D copy() {
        return new Ellipse2D(this.x, this.y, this.width, this.height);
    }

    public int hashCode() {
        int n2 = Float.floatToIntBits(this.x);
        n2 += Float.floatToIntBits(this.y) * 37;
        n2 += Float.floatToIntBits(this.width) * 43;
        return n2 += Float.floatToIntBits(this.height) * 47;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Ellipse2D) {
            Ellipse2D ellipse2D = (Ellipse2D)object;
            return this.x == ellipse2D.x && this.y == ellipse2D.y && this.width == ellipse2D.width && this.height == ellipse2D.height;
        }
        return false;
    }
}

