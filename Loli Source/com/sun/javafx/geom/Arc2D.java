/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.ArcIterator;
import com.sun.javafx.geom.Dimension2D;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RectangularShape;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;

public class Arc2D
extends RectangularShape {
    public static final int OPEN = 0;
    public static final int CHORD = 1;
    public static final int PIE = 2;
    private int type;
    public float x;
    public float y;
    public float width;
    public float height;
    public float start;
    public float extent;

    public Arc2D() {
        this(0);
    }

    public Arc2D(int n2) {
        this.setArcType(n2);
    }

    public Arc2D(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
        this(n2);
        this.x = f2;
        this.y = f3;
        this.width = f4;
        this.height = f5;
        this.start = f6;
        this.extent = f7;
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

    public void setArc(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
        this.setArcType(n2);
        this.x = f2;
        this.y = f3;
        this.width = f4;
        this.height = f5;
        this.start = f6;
        this.extent = f7;
    }

    public int getArcType() {
        return this.type;
    }

    public Point2D getStartPoint() {
        double d2 = Math.toRadians(-this.start);
        double d3 = (double)this.x + (Math.cos(d2) * 0.5 + 0.5) * (double)this.width;
        double d4 = (double)this.y + (Math.sin(d2) * 0.5 + 0.5) * (double)this.height;
        return new Point2D((float)d3, (float)d4);
    }

    public Point2D getEndPoint() {
        double d2 = Math.toRadians(-this.start - this.extent);
        double d3 = (double)this.x + (Math.cos(d2) * 0.5 + 0.5) * (double)this.width;
        double d4 = (double)this.y + (Math.sin(d2) * 0.5 + 0.5) * (double)this.height;
        return new Point2D((float)d3, (float)d4);
    }

    public void setArc(Point2D point2D, Dimension2D dimension2D, float f2, float f3, int n2) {
        this.setArc(point2D.x, point2D.y, dimension2D.width, dimension2D.height, f2, f3, n2);
    }

    public void setArc(Arc2D arc2D) {
        this.setArc(arc2D.x, arc2D.y, arc2D.width, arc2D.height, arc2D.start, arc2D.extent, arc2D.type);
    }

    public void setArcByCenter(float f2, float f3, float f4, float f5, float f6, int n2) {
        this.setArc(f2 - f4, f3 - f4, f4 * 2.0f, f4 * 2.0f, f5, f6, n2);
    }

    public void setArcByTangent(Point2D point2D, Point2D point2D2, Point2D point2D3, float f2) {
        double d2 = Math.atan2(point2D.y - point2D2.y, point2D.x - point2D2.x);
        double d3 = Math.atan2(point2D3.y - point2D2.y, point2D3.x - point2D2.x);
        double d4 = d3 - d2;
        if (d4 > Math.PI) {
            d3 -= Math.PI * 2;
        } else if (d4 < -Math.PI) {
            d3 += Math.PI * 2;
        }
        double d5 = (d2 + d3) / 2.0;
        double d6 = Math.abs(d3 - d5);
        double d7 = (double)f2 / Math.sin(d6);
        double d8 = (double)point2D2.x + d7 * Math.cos(d5);
        double d9 = (double)point2D2.y + d7 * Math.sin(d5);
        if (d2 < d3) {
            d2 -= 1.5707963267948966;
            d3 += 1.5707963267948966;
        } else {
            d2 += 1.5707963267948966;
            d3 -= 1.5707963267948966;
        }
        d2 = Math.toDegrees(-d2);
        d3 = Math.toDegrees(-d3);
        d4 = d3 - d2;
        d4 = d4 < 0.0 ? (d4 += 360.0) : (d4 -= 360.0);
        this.setArcByCenter((float)d8, (float)d9, f2, (float)d2, (float)d4, this.type);
    }

    public void setAngleStart(Point2D point2D) {
        double d2 = this.height * (point2D.x - this.getCenterX());
        double d3 = this.width * (point2D.y - this.getCenterY());
        this.start = (float)(-Math.toDegrees(Math.atan2(d3, d2)));
    }

    public void setAngles(float f2, float f3, float f4, float f5) {
        double d2 = this.getCenterX();
        double d3 = this.getCenterY();
        double d4 = this.width;
        double d5 = this.height;
        double d6 = Math.atan2(d4 * (d3 - (double)f3), d5 * ((double)f2 - d2));
        double d7 = Math.atan2(d4 * (d3 - (double)f5), d5 * ((double)f4 - d2));
        if ((d7 -= d6) <= 0.0) {
            d7 += Math.PI * 2;
        }
        this.start = (float)Math.toDegrees(d6);
        this.extent = (float)Math.toDegrees(d7);
    }

    public void setAngles(Point2D point2D, Point2D point2D2) {
        this.setAngles(point2D.x, point2D.y, point2D2.x, point2D2.y);
    }

    public void setArcType(int n2) {
        if (n2 < 0 || n2 > 2) {
            throw new IllegalArgumentException("invalid type for Arc: " + n2);
        }
        this.type = n2;
    }

    @Override
    public void setFrame(float f2, float f3, float f4, float f5) {
        this.setArc(f2, f3, f4, f5, this.start, this.extent, this.type);
    }

    @Override
    public RectBounds getBounds() {
        double d2;
        double d3;
        double d4;
        double d5;
        if (this.isEmpty()) {
            return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
        }
        if (this.getArcType() == 2) {
            d5 = 0.0;
            d4 = 0.0;
            d3 = 0.0;
            d2 = 0.0;
        } else {
            d3 = 1.0;
            d2 = 1.0;
            d5 = -1.0;
            d4 = -1.0;
        }
        double d6 = 0.0;
        for (int i2 = 0; i2 < 6; ++i2) {
            if (i2 < 4) {
                if (!this.containsAngle((float)(d6 += 90.0))) {
                    continue;
                }
            } else {
                d6 = i2 == 4 ? (double)this.start : (d6 += (double)this.extent);
            }
            double d7 = Math.toRadians(-d6);
            double d8 = Math.cos(d7);
            double d9 = Math.sin(d7);
            d2 = Math.min(d2, d8);
            d3 = Math.min(d3, d9);
            d4 = Math.max(d4, d8);
            d5 = Math.max(d5, d9);
        }
        double d10 = this.width;
        double d11 = this.height;
        d4 = (double)this.x + (d4 * 0.5 + 0.5) * d10;
        d5 = (double)this.y + (d5 * 0.5 + 0.5) * d11;
        d2 = (double)this.x + (d2 * 0.5 + 0.5) * d10;
        d3 = (double)this.y + (d3 * 0.5 + 0.5) * d11;
        return new RectBounds((float)d2, (float)d3, (float)d4, (float)d5);
    }

    static float normalizeDegrees(double d2) {
        if (d2 > 180.0) {
            if (d2 <= 540.0) {
                d2 -= 360.0;
            } else if ((d2 = Math.IEEEremainder(d2, 360.0)) == -180.0) {
                d2 = 180.0;
            }
        } else if (d2 <= -180.0) {
            if (d2 > -540.0) {
                d2 += 360.0;
            } else if ((d2 = Math.IEEEremainder(d2, 360.0)) == -180.0) {
                d2 = 180.0;
            }
        }
        return (float)d2;
    }

    public boolean containsAngle(float f2) {
        boolean bl;
        double d2 = this.extent;
        boolean bl2 = bl = d2 < 0.0;
        if (bl) {
            d2 = -d2;
        }
        if (d2 >= 360.0) {
            return true;
        }
        f2 = Arc2D.normalizeDegrees(f2) - Arc2D.normalizeDegrees(this.start);
        if (bl) {
            f2 = -f2;
        }
        if ((double)f2 < 0.0) {
            f2 = (float)((double)f2 + 360.0);
        }
        return (double)f2 >= 0.0 && (double)f2 < d2;
    }

    @Override
    public boolean contains(float f2, float f3) {
        double d2;
        double d3;
        double d4;
        boolean bl;
        double d5 = this.width;
        if (d5 <= 0.0) {
            return false;
        }
        double d6 = (double)(f2 - this.x) / d5 - 0.5;
        double d7 = this.height;
        if (d7 <= 0.0) {
            return false;
        }
        double d8 = (double)(f3 - this.y) / d7 - 0.5;
        double d9 = d6 * d6 + d8 * d8;
        if (d9 >= 0.25) {
            return false;
        }
        double d10 = Math.abs(this.extent);
        if (d10 >= 360.0) {
            return true;
        }
        boolean bl2 = this.containsAngle((float)(-Math.toDegrees(Math.atan2(d8, d6))));
        if (this.type == 2) {
            return bl2;
        }
        if (bl2) {
            if (d10 >= 180.0) {
                return true;
            }
        } else if (d10 <= 180.0) {
            return false;
        }
        double d11 = Math.toRadians(-this.start);
        double d12 = Math.cos(d11);
        boolean bl3 = bl = Line2D.relativeCCW((float)d12, (float)(d4 = Math.sin(d11)), (float)(d3 = Math.cos(d11 += Math.toRadians(-this.extent))), (float)(d2 = Math.sin(d11)), (float)(2.0 * d6), (float)(2.0 * d8)) * Line2D.relativeCCW((float)d12, (float)d4, (float)d3, (float)d2, 0.0f, 0.0f) >= 0;
        return bl2 ? !bl : bl;
    }

    @Override
    public boolean intersects(float f2, float f3, float f4, float f5) {
        float f6 = this.width;
        float f7 = this.height;
        if (f4 <= 0.0f || f5 <= 0.0f || f6 <= 0.0f || f7 <= 0.0f) {
            return false;
        }
        float f8 = this.extent;
        if (f8 == 0.0f) {
            return false;
        }
        float f9 = this.x;
        float f10 = this.y;
        float f11 = f9 + f6;
        float f12 = f10 + f7;
        float f13 = f2 + f4;
        float f14 = f3 + f5;
        if (f2 >= f11 || f3 >= f12 || f13 <= f9 || f14 <= f10) {
            return false;
        }
        float f15 = this.getCenterX();
        float f16 = this.getCenterY();
        double d2 = Math.toRadians(-this.start);
        float f17 = (float)((double)this.x + (Math.cos(d2) * 0.5 + 0.5) * (double)this.width);
        float f18 = (float)((double)this.y + (Math.sin(d2) * 0.5 + 0.5) * (double)this.height);
        double d3 = Math.toRadians(-this.start - this.extent);
        float f19 = (float)((double)this.x + (Math.cos(d3) * 0.5 + 0.5) * (double)this.width);
        float f20 = (float)((double)this.y + (Math.sin(d3) * 0.5 + 0.5) * (double)this.height);
        if (f16 >= f3 && f16 <= f14 && (f17 < f13 && f19 < f13 && f15 < f13 && f11 > f2 && this.containsAngle(0.0f) || f17 > f2 && f19 > f2 && f15 > f2 && f9 < f13 && this.containsAngle(180.0f))) {
            return true;
        }
        if (f15 >= f2 && f15 <= f13 && (f18 > f3 && f20 > f3 && f16 > f3 && f10 < f14 && this.containsAngle(90.0f) || f18 < f14 && f20 < f14 && f16 < f14 && f12 > f3 && this.containsAngle(270.0f))) {
            return true;
        }
        if (this.type == 2 || Math.abs(f8) > 180.0f ? Shape.intersectsLine(f2, f3, f4, f5, f15, f16, f17, f18) || Shape.intersectsLine(f2, f3, f4, f5, f15, f16, f19, f20) : Shape.intersectsLine(f2, f3, f4, f5, f17, f18, f19, f20)) {
            return true;
        }
        return this.contains(f2, f3) || this.contains(f2 + f4, f3) || this.contains(f2, f3 + f5) || this.contains(f2 + f4, f3 + f5);
    }

    @Override
    public boolean contains(float f2, float f3, float f4, float f5) {
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        if (!(this.contains(f2, f3) && this.contains(f2 + f4, f3) && this.contains(f2, f3 + f5) && this.contains(f2 + f4, f3 + f5))) {
            return false;
        }
        if (this.type != 2 || (double)Math.abs(this.extent) <= 180.0) {
            return true;
        }
        float f11 = this.getWidth() / 2.0f;
        float f12 = f2 + f11;
        if (Shape.intersectsLine(f2, f3, f4, f5, f12, f10 = f3 + (f9 = this.getHeight() / 2.0f), f8 = (float)((double)f12 + (double)f11 * Math.cos(f7 = (float)Math.toRadians(-this.start))), f6 = (float)((double)f10 + (double)f9 * Math.sin(f7)))) {
            return false;
        }
        f8 = (float)((double)f12 + (double)f11 * Math.cos(f7 += (float)Math.toRadians(-this.extent)));
        return !Shape.intersectsLine(f2, f3, f4, f5, f12, f10, f8, f6 = (float)((double)f10 + (double)f9 * Math.sin(f7)));
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new ArcIterator(this, baseTransform);
    }

    @Override
    public Arc2D copy() {
        return new Arc2D(this.x, this.y, this.width, this.height, this.start, this.extent, this.type);
    }

    public int hashCode() {
        int n2 = Float.floatToIntBits(this.x);
        n2 += Float.floatToIntBits(this.y) * 37;
        n2 += Float.floatToIntBits(this.width) * 43;
        n2 += Float.floatToIntBits(this.height) * 47;
        n2 += Float.floatToIntBits(this.start) * 53;
        n2 += Float.floatToIntBits(this.extent) * 59;
        return n2 += this.getArcType() * 61;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Arc2D) {
            Arc2D arc2D = (Arc2D)object;
            return this.x == arc2D.x && this.y == arc2D.y && this.width == arc2D.width && this.height == arc2D.height && this.start == arc2D.start && this.extent == arc2D.extent && this.type == arc2D.type;
        }
        return false;
    }
}

