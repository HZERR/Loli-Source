/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

import com.gif4j.quantizer.h;

class k
extends h {
    protected int[] a;

    public k(int n2, double d2) {
        super(n2, d2, Integer.TYPE);
    }

    public k(int n2) {
        this(n2, 0.3);
    }

    public k() {
        this(0, 0.3);
    }

    public k(k k2) {
        super(k2);
    }

    protected Object a() {
        return this.a;
    }

    protected void a(Object object) {
        this.a = (int[])object;
    }

    protected void a(boolean[] arrbl, Object object) {
        int[] arrn = (int[])object;
        for (int i2 = 0; i2 < arrbl.length; ++i2) {
            if (!arrbl[i2]) continue;
            this.b(arrn[i2]);
        }
    }

    protected final int a(int n2) {
        return (n2 * 517 & Integer.MAX_VALUE) % this.g.length;
    }

    protected int b(int n2) {
        int n3 = this.h(this.a(n2));
        this.g[n3] = true;
        this.a[n3] = n2;
        return n3;
    }

    public boolean c(int n2) {
        this.f(this.d + 1);
        int n3 = -this.i(n2) - 1;
        if (n3 >= 0) {
            ++this.d;
            this.g[n3] = true;
            this.a[n3] = n2;
            return true;
        }
        return false;
    }

    protected final int i(int n2) {
        int n3 = this.a(n2);
        while (this.g[n3]) {
            if (n2 == this.a[n3]) {
                return n3;
            }
            n3 = this.g(n3);
        }
        return -n3 - 1;
    }

    public Object clone() {
        return new k(this);
    }
}

