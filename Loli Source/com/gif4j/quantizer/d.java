/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

import com.gif4j.quantizer.v;

class d
extends v {
    protected int[] a;
    protected int[] b;

    public d(int n2, double d2) {
        super(n2, d2, Integer.TYPE, Integer.TYPE);
    }

    public d(int n2) {
        this(n2, 0.3);
    }

    public d() {
        this(0, 0.3);
    }

    public d(d d2) {
        super(d2);
    }

    protected final Object a() {
        return this.a;
    }

    protected final void a(Object object) {
        this.a = (int[])object;
    }

    protected final Object b() {
        return this.b;
    }

    protected final void b(Object object) {
        this.b = (int[])object;
    }

    protected void a(boolean[] arrbl, Object object, Object object2) {
        int[] arrn = (int[])object;
        int[] arrn2 = (int[])object2;
        for (int i2 = 0; i2 < arrbl.length; ++i2) {
            if (!arrbl[i2]) continue;
            this.a(arrn[i2], arrn2[i2]);
        }
    }

    protected final int a(int n2) {
        return (n2 * 517 & Integer.MAX_VALUE) % this.g.length;
    }

    protected int a(int n2, int n3) {
        int n4 = this.h(this.a(n2));
        this.g[n4] = true;
        this.a[n4] = n2;
        this.b[n4] = n3;
        return n4;
    }

    public int b(int n2, int n3) {
        this.f(this.d + 1);
        int n4 = this.b(n2);
        if (n4 >= 0) {
            int n5 = this.b[n4];
            this.b[n4] = n3;
            return n5;
        }
        ++this.d;
        n4 = -n4 - 1;
        this.g[n4] = true;
        this.a[n4] = n2;
        this.b[n4] = n3;
        return Integer.MIN_VALUE;
    }

    protected final int b(int n2) {
        int n3 = this.a(n2);
        while (this.g[n3]) {
            if (n2 == this.a[n3]) {
                return n3;
            }
            n3 = this.g(n3);
        }
        return -n3 - 1;
    }

    public final int c(int n2) {
        int n3 = this.b(n2);
        if (n3 >= 0) {
            return this.b[n3];
        }
        return Integer.MIN_VALUE;
    }

    public Object clone() {
        return new d(this);
    }
}

