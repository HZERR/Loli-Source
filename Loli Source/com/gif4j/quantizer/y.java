/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

import java.lang.reflect.Array;

abstract class y {
    protected double c;
    protected int d;
    protected int e;
    protected int f;
    protected boolean[] g;

    public y(int n2, double d2, Class class_) {
        if (d2 <= 0.0 || d2 >= 1.0) {
            throw new IllegalArgumentException("fill value out of range");
        }
        this.c = d2;
        int n3 = Math.max((int)((double)n2 / this.c), 31);
        n3 += (n3 + 1) % 2;
        this.e = (int)((double)n3 * this.c);
        this.f = n3 / 2;
        this.g = new boolean[n3];
        this.a(Array.newInstance(class_, n3));
    }

    public y(y y2) {
        this.c = y2.c;
        this.d = y2.d;
        this.e = y2.e;
        this.f = y2.f;
        int n2 = y2.g.length;
        this.g = new boolean[n2];
        System.arraycopy(y2.g, 0, this.g, 0, this.g.length);
        Class<?> class_ = y2.a().getClass().getComponentType();
        Object object = Array.newInstance(class_, n2);
        System.arraycopy(y2.a(), 0, object, 0, n2);
        this.a(object);
    }

    protected abstract Object a();

    protected abstract void a(Object var1);

    protected abstract void d(int var1);

    protected void e(int n2) {
        int n3 = this.g.length;
        int n4 = this.e;
        while (n4 < n2) {
            n3 = n3 * 2 + 1;
            n4 = (int)((double)n3 * this.c);
        }
        this.e = n4;
        this.f = n3 / 2;
        this.d(n3);
    }

    public final void f(int n2) {
        if (n2 > this.e) {
            this.e(n2);
        }
    }

    protected final int g(int n2) {
        return (n2 + this.f) % this.g.length;
    }

    protected final int h(int n2) {
        while (this.g[n2]) {
            n2 = this.g(n2);
        }
        return n2;
    }
}

