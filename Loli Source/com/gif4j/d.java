/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.m;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedList;

class d {
    private LinkedList a = new LinkedList();
    private m b;

    public d(m m2) {
        this.b = m2;
    }

    public synchronized Object a() {
        Object var1_1 = null;
        Iterator iterator = this.a.iterator();
        while (iterator.hasNext() && var1_1 == null) {
            SoftReference softReference = (SoftReference)iterator.next();
            iterator.remove();
            var1_1 = softReference.get();
            if (var1_1 == null || this.b.a(var1_1)) continue;
            this.b.b(var1_1);
            var1_1 = null;
        }
        if (var1_1 == null) {
            return this.b.a();
        }
        return var1_1;
    }

    public synchronized void a(Object object) {
        SoftReference<Object> softReference = new SoftReference<Object>(object);
        this.a.add(softReference);
    }
}

