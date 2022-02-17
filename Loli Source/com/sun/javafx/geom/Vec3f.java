/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

public class Vec3f {
    public float x;
    public float y;
    public float z;

    public Vec3f() {
    }

    public Vec3f(float f2, float f3, float f4) {
        this.x = f2;
        this.y = f3;
        this.z = f4;
    }

    public Vec3f(Vec3f vec3f) {
        this.x = vec3f.x;
        this.y = vec3f.y;
        this.z = vec3f.z;
    }

    public void set(Vec3f vec3f) {
        this.x = vec3f.x;
        this.y = vec3f.y;
        this.z = vec3f.z;
    }

    public void set(float f2, float f3, float f4) {
        this.x = f2;
        this.y = f3;
        this.z = f4;
    }

    public final void mul(float f2) {
        this.x *= f2;
        this.y *= f2;
        this.z *= f2;
    }

    public void sub(Vec3f vec3f, Vec3f vec3f2) {
        this.x = vec3f.x - vec3f2.x;
        this.y = vec3f.y - vec3f2.y;
        this.z = vec3f.z - vec3f2.z;
    }

    public void sub(Vec3f vec3f) {
        this.x -= vec3f.x;
        this.y -= vec3f.y;
        this.z -= vec3f.z;
    }

    public void add(Vec3f vec3f, Vec3f vec3f2) {
        this.x = vec3f.x + vec3f2.x;
        this.y = vec3f.y + vec3f2.y;
        this.z = vec3f.z + vec3f2.z;
    }

    public void add(Vec3f vec3f) {
        this.x += vec3f.x;
        this.y += vec3f.y;
        this.z += vec3f.z;
    }

    public float length() {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public void normalize() {
        float f2 = 1.0f / this.length();
        this.x *= f2;
        this.y *= f2;
        this.z *= f2;
    }

    public void cross(Vec3f vec3f, Vec3f vec3f2) {
        float f2 = vec3f.y * vec3f2.z - vec3f.z * vec3f2.y;
        float f3 = vec3f2.x * vec3f.z - vec3f2.z * vec3f.x;
        this.z = vec3f.x * vec3f2.y - vec3f.y * vec3f2.x;
        this.x = f2;
        this.y = f3;
    }

    public float dot(Vec3f vec3f) {
        return this.x * vec3f.x + this.y * vec3f.y + this.z * vec3f.z;
    }

    public int hashCode() {
        int n2 = 7;
        n2 = 31 * n2 + Float.floatToIntBits(this.x);
        n2 = 31 * n2 + Float.floatToIntBits(this.y);
        n2 = 31 * n2 + Float.floatToIntBits(this.z);
        return n2;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Vec3f) {
            Vec3f vec3f = (Vec3f)object;
            return this.x == vec3f.x && this.y == vec3f.y && this.z == vec3f.z;
        }
        return false;
    }

    public String toString() {
        return "Vec3f[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
}

