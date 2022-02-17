/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.LineIterator;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;

public class Line2D
extends Shape {
    public float x1;
    public float y1;
    public float x2;
    public float y2;

    public Line2D() {
    }

    public Line2D(float f2, float f3, float f4, float f5) {
        this.setLine(f2, f3, f4, f5);
    }

    public Line2D(Point2D point2D, Point2D point2D2) {
        this.setLine(point2D, point2D2);
    }

    public void setLine(float f2, float f3, float f4, float f5) {
        this.x1 = f2;
        this.y1 = f3;
        this.x2 = f4;
        this.y2 = f5;
    }

    public void setLine(Point2D point2D, Point2D point2D2) {
        this.setLine(point2D.x, point2D.y, point2D2.x, point2D2.y);
    }

    public void setLine(Line2D line2D) {
        this.setLine(line2D.x1, line2D.y1, line2D.x2, line2D.y2);
    }

    @Override
    public RectBounds getBounds() {
        RectBounds rectBounds = new RectBounds();
        rectBounds.setBoundsAndSort(this.x1, this.y1, this.x2, this.y2);
        return rectBounds;
    }

    @Override
    public boolean contains(float f2, float f3) {
        return false;
    }

    @Override
    public boolean contains(float f2, float f3, float f4, float f5) {
        return false;
    }

    @Override
    public boolean contains(Point2D point2D) {
        return false;
    }

    @Override
    public boolean intersects(float f2, float f3, float f4, float f5) {
        int n2;
        int n3 = Line2D.outcode(f2, f3, f4, f5, this.x2, this.y2);
        if (n3 == 0) {
            return true;
        }
        float f6 = this.x1;
        float f7 = this.y1;
        while ((n2 = Line2D.outcode(f2, f3, f4, f5, f6, f7)) != 0) {
            if ((n2 & n3) != 0) {
                return false;
            }
            if ((n2 & 5) != 0) {
                f6 = f2;
                if ((n2 & 4) != 0) {
                    f6 += f4;
                }
                f7 = this.y1 + (f6 - this.x1) * (this.y2 - this.y1) / (this.x2 - this.x1);
                continue;
            }
            f7 = f3;
            if ((n2 & 8) != 0) {
                f7 += f5;
            }
            f6 = this.x1 + (f7 - this.y1) * (this.x2 - this.x1) / (this.y2 - this.y1);
        }
        return true;
    }

    public static int relativeCCW(float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = (f6 -= f2) * (f5 -= f3) - (f7 -= f3) * (f4 -= f2);
        if (f8 == 0.0f && (f8 = f6 * f4 + f7 * f5) > 0.0f && (f8 = (f6 -= f4) * f4 + (f7 -= f5) * f5) < 0.0f) {
            f8 = 0.0f;
        }
        return f8 < 0.0f ? -1 : (f8 > 0.0f ? 1 : 0);
    }

    public int relativeCCW(float f2, float f3) {
        return Line2D.relativeCCW(this.x1, this.y1, this.x2, this.y2, f2, f3);
    }

    public int relativeCCW(Point2D point2D) {
        return Line2D.relativeCCW(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    public static boolean linesIntersect(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        return Line2D.relativeCCW(f2, f3, f4, f5, f6, f7) * Line2D.relativeCCW(f2, f3, f4, f5, f8, f9) <= 0 && Line2D.relativeCCW(f6, f7, f8, f9, f2, f3) * Line2D.relativeCCW(f6, f7, f8, f9, f4, f5) <= 0;
    }

    public boolean intersectsLine(float f2, float f3, float f4, float f5) {
        return Line2D.linesIntersect(f2, f3, f4, f5, this.x1, this.y1, this.x2, this.y2);
    }

    public boolean intersectsLine(Line2D line2D) {
        return Line2D.linesIntersect(line2D.x1, line2D.y1, line2D.x2, line2D.y2, this.x1, this.y1, this.x2, this.y2);
    }

    public static float ptSegDistSq(float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8;
        float f9;
        float f10 = (f6 -= f2) * (f4 -= f2) + (f7 -= f3) * (f5 -= f3);
        if ((f9 = f6 * f6 + f7 * f7 - (f8 = f10 <= 0.0f ? 0.0f : ((f10 = (f6 = f4 - f6) * f4 + (f7 = f5 - f7) * f5) <= 0.0f ? 0.0f : f10 * f10 / (f4 * f4 + f5 * f5)))) < 0.0f) {
            f9 = 0.0f;
        }
        return f9;
    }

    public static float ptSegDist(float f2, float f3, float f4, float f5, float f6, float f7) {
        return (float)Math.sqrt(Line2D.ptSegDistSq(f2, f3, f4, f5, f6, f7));
    }

    public float ptSegDistSq(float f2, float f3) {
        return Line2D.ptSegDistSq(this.x1, this.y1, this.x2, this.y2, f2, f3);
    }

    public float ptSegDistSq(Point2D point2D) {
        return Line2D.ptSegDistSq(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    public double ptSegDist(float f2, float f3) {
        return Line2D.ptSegDist(this.x1, this.y1, this.x2, this.y2, f2, f3);
    }

    public float ptSegDist(Point2D point2D) {
        return Line2D.ptSegDist(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    public static float ptLineDistSq(float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8;
        float f9;
        float f10;
        if ((f10 = (f6 -= f2) * f6 + (f7 -= f3) * f7 - (f9 = (f8 = f6 * (f4 -= f2) + f7 * (f5 -= f3)) * f8 / (f4 * f4 + f5 * f5))) < 0.0f) {
            f10 = 0.0f;
        }
        return f10;
    }

    public static float ptLineDist(float f2, float f3, float f4, float f5, float f6, float f7) {
        return (float)Math.sqrt(Line2D.ptLineDistSq(f2, f3, f4, f5, f6, f7));
    }

    public float ptLineDistSq(float f2, float f3) {
        return Line2D.ptLineDistSq(this.x1, this.y1, this.x2, this.y2, f2, f3);
    }

    public float ptLineDistSq(Point2D point2D) {
        return Line2D.ptLineDistSq(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    public float ptLineDist(float f2, float f3) {
        return Line2D.ptLineDist(this.x1, this.y1, this.x2, this.y2, f2, f3);
    }

    public float ptLineDist(Point2D point2D) {
        return Line2D.ptLineDist(this.x1, this.y1, this.x2, this.y2, point2D.x, point2D.y);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new LineIterator(this, baseTransform);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f2) {
        return new LineIterator(this, baseTransform);
    }

    @Override
    public Line2D copy() {
        return new Line2D(this.x1, this.y1, this.x2, this.y2);
    }

    public int hashCode() {
        int n2 = Float.floatToIntBits(this.x1);
        n2 += Float.floatToIntBits(this.y1) * 37;
        n2 += Float.floatToIntBits(this.x2) * 43;
        return n2 += Float.floatToIntBits(this.y2) * 47;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Line2D) {
            Line2D line2D = (Line2D)object;
            return this.x1 == line2D.x1 && this.y1 == line2D.y1 && this.x2 == line2D.x2 && this.y2 == line2D.y2;
        }
        return false;
    }
}

