/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

public class Vec4f {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vec4f() {
    }

    public Vec4f(Vec4f vec4f) {
        this.x = vec4f.x;
        this.y = vec4f.y;
        this.z = vec4f.z;
        this.w = vec4f.w;
    }

    public Vec4f(float f2, float f3, float f4, float f5) {
        this.x = f2;
        this.y = f3;
        this.z = f4;
        this.w = f5;
    }

    public void set(Vec4f vec4f) {
        this.x = vec4f.x;
        this.y = vec4f.y;
        this.z = vec4f.z;
        this.w = vec4f.w;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }
}

