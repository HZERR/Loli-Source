/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Matrix3f;

public class Quat4f {
    static final double EPS2 = 1.0E-30;
    public float x;
    public float y;
    public float z;
    public float w;

    public Quat4f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    public Quat4f(float f2, float f3, float f4, float f5) {
        float f6 = (float)(1.0 / Math.sqrt(f2 * f2 + f3 * f3 + f4 * f4 + f5 * f5));
        this.x = f2 * f6;
        this.y = f3 * f6;
        this.z = f4 * f6;
        this.w = f5 * f6;
    }

    public Quat4f(float[] arrf) {
        float f2 = (float)(1.0 / Math.sqrt(arrf[0] * arrf[0] + arrf[1] * arrf[1] + arrf[2] * arrf[2] + arrf[3] * arrf[3]));
        this.x = arrf[0] * f2;
        this.y = arrf[1] * f2;
        this.z = arrf[2] * f2;
        this.w = arrf[3] * f2;
    }

    public Quat4f(Quat4f quat4f) {
        this.x = quat4f.x;
        this.y = quat4f.y;
        this.z = quat4f.z;
        this.w = quat4f.w;
    }

    public final void normalize() {
        float f2 = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
        if (f2 > 0.0f) {
            f2 = 1.0f / (float)Math.sqrt(f2);
            this.x *= f2;
            this.y *= f2;
            this.z *= f2;
            this.w *= f2;
        } else {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
            this.w = 0.0f;
        }
    }

    public final void set(Matrix3f matrix3f) {
        float f2 = 0.25f * (matrix3f.m00 + matrix3f.m11 + matrix3f.m22 + 1.0f);
        if (f2 >= 0.0f) {
            if ((double)f2 >= 1.0E-30) {
                this.w = (float)Math.sqrt(f2);
                f2 = 0.25f / this.w;
                this.x = (matrix3f.m21 - matrix3f.m12) * f2;
                this.y = (matrix3f.m02 - matrix3f.m20) * f2;
                this.z = (matrix3f.m10 - matrix3f.m01) * f2;
                return;
            }
        } else {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        this.w = 0.0f;
        f2 = -0.5f * (matrix3f.m11 + matrix3f.m22);
        if (f2 >= 0.0f) {
            if ((double)f2 >= 1.0E-30) {
                this.x = (float)Math.sqrt(f2);
                f2 = 0.5f / this.x;
                this.y = matrix3f.m10 * f2;
                this.z = matrix3f.m20 * f2;
                return;
            }
        } else {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        this.x = 0.0f;
        f2 = 0.5f * (1.0f - matrix3f.m22);
        if ((double)f2 >= 1.0E-30) {
            this.y = (float)Math.sqrt(f2);
            this.z = matrix3f.m21 / (2.0f * this.y);
            return;
        }
        this.y = 0.0f;
        this.z = 1.0f;
    }

    public final void set(float[][] arrf) {
        float f2 = 0.25f * (arrf[0][0] + arrf[1][1] + arrf[2][2] + 1.0f);
        if (f2 >= 0.0f) {
            if ((double)f2 >= 1.0E-30) {
                this.w = (float)Math.sqrt(f2);
                f2 = 0.25f / this.w;
                this.x = (arrf[2][1] - arrf[1][2]) * f2;
                this.y = (arrf[0][2] - arrf[2][0]) * f2;
                this.z = (arrf[1][0] - arrf[0][1]) * f2;
                return;
            }
        } else {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        this.w = 0.0f;
        f2 = -0.5f * (arrf[1][1] + arrf[2][2]);
        if (f2 >= 0.0f) {
            if ((double)f2 >= 1.0E-30) {
                this.x = (float)Math.sqrt(f2);
                f2 = 0.5f / this.x;
                this.y = arrf[1][0] * f2;
                this.z = arrf[2][0] * f2;
                return;
            }
        } else {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        this.x = 0.0f;
        f2 = 0.5f * (1.0f - arrf[2][2]);
        if ((double)f2 >= 1.0E-30) {
            this.y = (float)Math.sqrt(f2);
            this.z = arrf[2][1] / (2.0f * this.y);
            return;
        }
        this.y = 0.0f;
        this.z = 1.0f;
    }

    public final void scale(float f2) {
        this.x *= f2;
        this.y *= f2;
        this.z *= f2;
        this.w *= f2;
    }

    public String toString() {
        return "Quat4f[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }
}

