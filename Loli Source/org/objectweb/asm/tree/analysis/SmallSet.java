/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree.analysis;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class SmallSet<E>
extends AbstractSet<E>
implements Iterator<E> {
    E e1;
    E e2;

    static final <T> Set<T> emptySet() {
        return new SmallSet<Object>(null, null);
    }

    SmallSet(E e1, E e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public Iterator<E> iterator() {
        return new SmallSet<E>(this.e1, this.e2);
    }

    @Override
    public int size() {
        return this.e1 == null ? 0 : (this.e2 == null ? 1 : 2);
    }

    @Override
    public boolean hasNext() {
        return this.e1 != null;
    }

    @Override
    public E next() {
        E e2 = this.e1;
        this.e1 = this.e2;
        this.e2 = null;
        return e2;
    }

    @Override
    public void remove() {
    }

    Set<E> union(SmallSet<E> s2) {
        if (s2.e1 == this.e1 && s2.e2 == this.e2 || s2.e1 == this.e2 && s2.e2 == this.e1) {
            return this;
        }
        if (s2.e1 == null) {
            return this;
        }
        if (this.e1 == null) {
            return s2;
        }
        if (s2.e2 == null) {
            if (this.e2 == null) {
                return new SmallSet<E>(this.e1, s2.e1);
            }
            if (s2.e1 == this.e1 || s2.e1 == this.e2) {
                return this;
            }
        }
        if (this.e2 == null && (this.e1 == s2.e1 || this.e1 == s2.e2)) {
            return s2;
        }
        HashSet<E> r2 = new HashSet<E>(4);
        r2.add(this.e1);
        if (this.e2 != null) {
            r2.add(this.e2);
        }
        r2.add(s2.e1);
        if (s2.e2 != null) {
            r2.add(s2.e2);
        }
        return r2;
    }
}

