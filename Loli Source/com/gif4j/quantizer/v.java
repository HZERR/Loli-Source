/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

import com.gif4j.quantizer.y;
import java.lang.reflect.Array;

abstract class v
extends y {
    public v(int n2, double d2, Class class_, Class class_2) {
        super(n2, d2, class_);
        this.b(Array.newInstance(class_2, this.g.length));
    }

    public v(v v2) {
        super(v2);
        int n2 = v2.g.length;
        Class<?> class_ = v2.b().getClass().getComponentType();
        Object object = Array.newInstance(class_, n2);
        System.arraycopy(v2.b(), 0, object, 0, n2);
        this.b(object);
    }

    protected abstract Object b();

    protected abstract void b(Object var1);

    protected abstract void a(boolean[] var1, Object var2, Object var3);

    protected void d(int n2) {
        boolean[] arrbl = this.g;
        this.g = new boolean[n2];
        Object object = this.a();
        Class<?> class_ = object.getClass().getComponentType();
        this.a(Array.newInstance(class_, n2));
        Object object2 = this.b();
        class_ = object2.getClass().getComponentType();
        this.b(Array.newInstance(class_, n2));
        this.a(arrbl, object, object2);
    }
}

