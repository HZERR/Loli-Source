/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

public class Vec2f {
    public float x;
    public float y;

    public Vec2f() {
    }

    public Vec2f(float f2, float f3) {
        this.x = f2;
        this.y = f3;
    }

    public Vec2f(Vec2f vec2f) {
        this.x = vec2f.x;
        this.y = vec2f.y;
    }

    public void set(Vec2f vec2f) {
        this.x = vec2f.x;
        this.y = vec2f.y;
    }

    public void set(float f2, float f3) {
        this.x = f2;
        this.y = f3;
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

    public float distanceSq(Vec2f vec2f) {
        float f2 = vec2f.x - this.x;
        float f3 = vec2f.y - this.y;
        return f2 * f2 + f3 * f3;
    }

    public float distance(float f2, float f3) {
        return (float)Math.sqrt((f2 -= this.x) * f2 + (f3 -= this.y) * f3);
    }

    public float distance(Vec2f vec2f) {
        float f2 = vec2f.x - this.x;
        float f3 = vec2f.y - this.y;
        return (float)Math.sqrt(f2 * f2 + f3 * f3);
    }

    public int hashCode() {
        int n2 = 7;
        n2 = 31 * n2 + Float.floatToIntBits(this.x);
        n2 = 31 * n2 + Float.floatToIntBits(this.y);
        return n2;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Vec2f) {
            Vec2f vec2f = (Vec2f)object;
            return this.x == vec2f.x && this.y == vec2f.y;
        }
        return false;
    }

    public String toString() {
        return "Vec2f[" + this.x + ", " + this.y + "]";
    }
}

