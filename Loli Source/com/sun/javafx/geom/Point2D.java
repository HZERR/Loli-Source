/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

public class Point2D {
    public float x;
    public float y;

    public Point2D() {
    }

    public Point2D(float f2, float f3) {
        this.x = f2;
        this.y = f3;
    }

    public void setLocation(float f2, float f3) {
        this.x = f2;
        this.y = f3;
    }

    public void setLocation(Point2D point2D) {
        this.setLocation(point2D.x, point2D.y);
    }

    public static float distanceSq(float f2, float f3, float f4, float f5) {
        return (f2 -= f4) * f2 + (f3 -= f5) * f3;
    }

    public static float distance(float f2, float f3, float f4, float f5) {
        return (float)Math.sqrt((f2 -= f4) * f2 + (f3 -= f5) * f3);
    }

    public float distanceSq(float f2, float f3) {
        return (f2 -= this.x) * f2 + (f3 -= this.y) * f3;
    }

    public float distanceSq(Point2D point2D) {
        float f2 = point2D.x - this.x;
        float f3 = point2D.y - this.y;
        return f2 * f2 + f3 * f3;
    }

    public float distance(float f2, float f3) {
        return (float)Math.sqrt((f2 -= this.x) * f2 + (f3 -= this.y) * f3);
    }

    public float distance(Point2D point2D) {
        float f2 = point2D.x - this.x;
        float f3 = point2D.y - this.y;
        return (float)Math.sqrt(f2 * f2 + f3 * f3);
    }

    public int hashCode() {
        int n2 = Float.floatToIntBits(this.x);
        return n2 ^= Float.floatToIntBits(this.y) * 31;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Point2D) {
            Point2D point2D = (Point2D)object;
            return this.x == point2D.x && this.y == point2D.y;
        }
        return false;
    }

    public String toString() {
        return "Point2D[" + this.x + ", " + this.y + "]";
    }
}

