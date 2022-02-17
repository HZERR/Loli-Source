/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.image.Coords;

public class ViewPort {
    public float u0;
    public float v0;
    public float u1;
    public float v1;

    public ViewPort(float f2, float f3, float f4, float f5) {
        this.u0 = f2;
        this.u1 = f2 + f4;
        this.v0 = f3;
        this.v1 = f3 + f5;
    }

    public ViewPort getScaledVersion(float f2) {
        if (f2 == 1.0f) {
            return this;
        }
        float f3 = this.u0 * f2;
        float f4 = this.v0 * f2;
        float f5 = this.u1 * f2;
        float f6 = this.v1 * f2;
        return new ViewPort(f3, f4, f5 - f3, f6 - f4);
    }

    public float getRelX(float f2) {
        return (f2 - this.u0) / (this.u1 - this.u0);
    }

    public float getRelY(float f2) {
        return (f2 - this.v0) / (this.v1 - this.v0);
    }

    public Coords getClippedCoords(float f2, float f3, float f4, float f5) {
        Coords coords = new Coords(f4, f5, this);
        if (this.u1 > f2 || this.u0 < 0.0f) {
            if (this.u0 >= f2 || this.u1 <= 0.0f) {
                return null;
            }
            if (this.u1 > f2) {
                coords.x1 = f4 * this.getRelX(f2);
                coords.u1 = f2;
            }
            if (this.u0 < 0.0f) {
                coords.x0 = f4 * this.getRelX(0.0f);
                coords.u0 = 0.0f;
            }
        }
        if (this.v1 > f3 || this.v0 < 0.0f) {
            if (this.v0 >= f3 || this.v1 <= 0.0f) {
                return null;
            }
            if (this.v1 > f3) {
                coords.y1 = f5 * this.getRelY(f3);
                coords.v1 = f3;
            }
            if (this.v0 < 0.0f) {
                coords.y0 = f5 * this.getRelY(0.0f);
                coords.v0 = 0.0f;
            }
        }
        return coords;
    }
}

