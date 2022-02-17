/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

public final class Transform6 {
    public int m00;
    public int m01;
    public int m10;
    public int m11;
    public int m02;
    public int m12;

    public Transform6() {
        this(65536, 0, 0, 65536, 0, 0);
    }

    public Transform6(int n2, int n3, int n4, int n5, int n6, int n7) {
        this.initialize();
        this.m00 = n2;
        this.m01 = n3;
        this.m10 = n4;
        this.m11 = n5;
        this.m02 = n6;
        this.m12 = n7;
    }

    public Transform6(Transform6 transform6) {
        this(transform6.m00, transform6.m01, transform6.m10, transform6.m11, transform6.m02, transform6.m12);
    }

    public void postMultiply(Transform6 transform6) {
        long l2 = (long)this.m00 * (long)transform6.m00 + (long)this.m01 * (long)transform6.m10 >> 16;
        long l3 = (long)this.m00 * (long)transform6.m01 + (long)this.m01 * (long)transform6.m11 >> 16;
        long l4 = (long)this.m10 * (long)transform6.m00 + (long)this.m11 * (long)transform6.m10 >> 16;
        long l5 = (long)this.m10 * (long)transform6.m01 + (long)this.m11 * (long)transform6.m11 >> 16;
        long l6 = ((long)this.m02 << 16) + (long)this.m00 * (long)transform6.m02 + (long)this.m01 * (long)transform6.m12 >> 16;
        long l7 = ((long)this.m12 << 16) + (long)this.m10 * (long)transform6.m02 + (long)this.m11 * (long)transform6.m12 >> 16;
        this.m00 = (int)l2;
        this.m01 = (int)l3;
        this.m02 = (int)l6;
        this.m10 = (int)l4;
        this.m11 = (int)l5;
        this.m12 = (int)l7;
    }

    public Transform6 inverse() {
        float f2 = (float)this.m00 / 65536.0f;
        float f3 = (float)this.m01 / 65536.0f;
        float f4 = (float)this.m02 / 65536.0f;
        float f5 = (float)this.m10 / 65536.0f;
        float f6 = (float)this.m11 / 65536.0f;
        float f7 = (float)this.m12 / 65536.0f;
        float f8 = f2 * f6 - f3 * f5;
        float f9 = f6 / f8;
        float f10 = -f3 / f8;
        float f11 = -f5 / f8;
        float f12 = f2 / f8;
        float f13 = (f3 * f7 - f4 * f6) / f8;
        float f14 = (f4 * f5 - f2 * f7) / f8;
        int n2 = (int)((double)f9 * 65536.0);
        int n3 = (int)(f10 * 65536.0f);
        int n4 = (int)(f11 * 65536.0f);
        int n5 = (int)(f12 * 65536.0f);
        int n6 = (int)(f13 * 65536.0f);
        int n7 = (int)(f14 * 65536.0f);
        return new Transform6(n2, n3, n4, n5, n6, n7);
    }

    public boolean isIdentity() {
        return this.m00 == 65536 && this.m01 == 0 && this.m10 == 0 && this.m11 == 65536 && this.m02 == 0 && this.m12 == 0;
    }

    public Transform6 setTransform(Transform6 transform6) {
        this.m00 = transform6.m00;
        this.m10 = transform6.m10;
        this.m01 = transform6.m01;
        this.m11 = transform6.m11;
        this.m02 = transform6.m02;
        this.m12 = transform6.m12;
        return this;
    }

    public String toString() {
        return "Transform6[m00=" + (double)this.m00 / 65536.0 + ", " + "m01=" + (double)this.m01 / 65536.0 + ", " + "m02=" + (double)this.m02 / 65536.0 + ", " + "m10=" + (double)this.m10 / 65536.0 + ", " + "m11=" + (double)this.m11 / 65536.0 + ", " + "m12=" + (double)this.m12 / 65536.0 + "]";
    }

    private native void initialize();
}

