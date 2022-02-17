/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx;

import java.util.AbstractList;
import java.util.RandomAccess;

public class UnmodifiableArrayList<T>
extends AbstractList<T>
implements RandomAccess {
    private T[] elements;
    private final int size;

    public UnmodifiableArrayList(T[] arrT, int n2) {
        assert (arrT != null ? n2 <= arrT.length : n2 == 0);
        this.size = n2;
        this.elements = arrT;
    }

    @Override
    public T get(int n2) {
        return this.elements[n2];
    }

    @Override
    public int size() {
        return this.size;
    }
}

