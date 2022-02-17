/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RectangularShape;
import com.sun.javafx.geom.RoundRectIterator;
import com.sun.javafx.geom.transform.BaseTransform;

public class RoundRectangle2D
extends RectangularShape {
    public float x;
    public float y;
    public float width;
    public float height;
    public float arcWidth;
    public float arcHeight;

    public RoundRectangle2D() {
    }

    public RoundRectangle2D(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.setRoundRect(f2, f3, f4, f5, f6, f7);
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

    public void setRoundRect(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.x = f2;
        this.y = f3;
        this.width = f4;
        this.height = f5;
        this.arcWidth = f6;
        this.arcHeight = f7;
    }

    @Override
    public RectBounds getBounds() {
        return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
    }

    public void setRoundRect(RoundRectangle2D roundRectangle2D) {
        this.setRoundRect(roundRectangle2D.x, roundRectangle2D.y, roundRectangle2D.width, roundRectangle2D.height, roundRectangle2D.arcWidth, roundRectangle2D.arcHeight);
    }

    @Override
    public void setFrame(float f2, float f3, float f4, float f5) {
        this.setRoundRect(f2, f3, f4, f5, this.arcWidth, this.arcHeight);
    }

    @Override
    public boolean contains(float f2, float f3) {
        float f4;
        float f5;
        if (this.isEmpty()) {
            return false;
        }
        float f6 = this.x;
        float f7 = this.y;
        float f8 = f6 + this.width;
        float f9 = f7 + this.height;
        if (f2 < f6 || f3 < f7 || f2 >= f8 || f3 >= f9) {
            return false;
        }
        float f10 = Math.min(this.width, Math.abs(this.arcWidth)) / 2.0f;
        float f11 = Math.min(this.height, Math.abs(this.arcHeight)) / 2.0f;
        f6 += f10;
        if (f2 >= f5) {
            float f12;
            f6 = f8 - f10;
            if (f2 < f12) {
                return true;
            }
        }
        f7 += f11;
        if (f3 >= f4) {
            float f13;
            f7 = f9 - f11;
            if (f3 < f13) {
                return true;
            }
        }
        return (double)((f2 = (f2 - f6) / f10) * f2 + (f3 = (f3 - f7) / f11) * f3) <= 1.0;
    }

    private int classify(float f2, float f3, float f4, float f5) {
        if (f2 < f3) {
            return 0;
        }
        if (f2 < f3 + f5) {
            return 1;
        }
        if (f2 < f4 - f5) {
            return 2;
        }
        if (f2 < f4) {
            return 3;
        }
        return 4;
    }

    @Override
    public boolean intersects(float f2, float f3, float f4, float f5) {
        if (this.isEmpty() || f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        float f6 = this.x;
        float f7 = this.y;
        float f8 = f6 + this.width;
        float f9 = f7 + this.height;
        if (f2 + f4 <= f6 || f2 >= f8 || f3 + f5 <= f7 || f3 >= f9) {
            return false;
        }
        float f10 = Math.min(this.width, Math.abs(this.arcWidth)) / 2.0f;
        float f11 = Math.min(this.height, Math.abs(this.arcHeight)) / 2.0f;
        int n2 = this.classify(f2, f6, f8, f10);
        int n3 = this.classify(f2 + f4, f6, f8, f10);
        int n4 = this.classify(f3, f7, f9, f11);
        int n5 = this.classify(f3 + f5, f7, f9, f11);
        if (n2 == 2 || n3 == 2 || n4 == 2 || n5 == 2) {
            return true;
        }
        if (n2 < 2 && n3 > 2 || n4 < 2 && n5 > 2) {
            return true;
        }
        f2 = n3 == 1 ? (f2 = f2 + f4 - (f6 + f10)) : (f2 = f2 - (f8 - f10));
        f3 = n5 == 1 ? (f3 = f3 + f5 - (f7 + f11)) : (f3 = f3 - (f9 - f11));
        return (f2 /= f10) * f2 + (f3 /= f11) * f3 <= 1.0f;
    }

    @Override
    public boolean contains(float f2, float f3, float f4, float f5) {
        if (this.isEmpty() || f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        return this.contains(f2, f3) && this.contains(f2 + f4, f3) && this.contains(f2, f3 + f5) && this.contains(f2 + f4, f3 + f5);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new RoundRectIterator(this, baseTransform);
    }

    @Override
    public RoundRectangle2D copy() {
        return new RoundRectangle2D(this.x, this.y, this.width, this.height, this.arcWidth, this.arcHeight);
    }

    public int hashCode() {
        int n2 = Float.floatToIntBits(this.x);
        n2 += Float.floatToIntBits(this.y) * 37;
        n2 += Float.floatToIntBits(this.width) * 43;
        n2 += Float.floatToIntBits(this.height) * 47;
        n2 += Float.floatToIntBits(this.arcWidth) * 53;
        return n2 += Float.floatToIntBits(this.arcHeight) * 59;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof RoundRectangle2D) {
            RoundRectangle2D roundRectangle2D = (RoundRectangle2D)object;
            return this.x == roundRectangle2D.x && this.y == roundRectangle2D.y && this.width == roundRectangle2D.width && this.height == roundRectangle2D.height && this.arcWidth == roundRectangle2D.arcWidth && this.arcHeight == roundRectangle2D.arcHeight;
        }
        return false;
    }
}

