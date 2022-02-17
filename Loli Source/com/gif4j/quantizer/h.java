/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

import com.gif4j.quantizer.y;
import java.lang.reflect.Array;

abstract class h
extends y {
    public h(int n2, double d2, Class class_) {
        super(n2, d2, class_);
    }

    public h(h h2) {
        super(h2);
    }

    protected abstract void a(boolean[] var1, Object var2);

    protected void d(int n2) {
        boolean[] arrbl = this.g;
        this.g = new boolean[n2];
        Object object = this.a();
        Class<?> class_ = object.getClass().getComponentType();
        this.a(Array.newInstance(class_, n2));
        this.a(arrbl, object);
    }
}

