/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

public class Vec4d {
    public double x;
    public double y;
    public double z;
    public double w;

    public Vec4d() {
    }

    public Vec4d(Vec4d vec4d) {
        this.x = vec4d.x;
        this.y = vec4d.y;
        this.z = vec4d.z;
        this.w = vec4d.w;
    }

    public Vec4d(double d2, double d3, double d4, double d5) {
        this.x = d2;
        this.y = d3;
        this.z = d4;
        this.w = d5;
    }

    public void set(Vec4d vec4d) {
        this.x = vec4d.x;
        this.y = vec4d.y;
        this.z = vec4d.z;
        this.w = vec4d.w;
    }
}

