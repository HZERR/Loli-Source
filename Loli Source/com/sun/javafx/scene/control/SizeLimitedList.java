/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control;

import java.util.LinkedList;
import java.util.List;

public class SizeLimitedList<E> {
    private final int maxSize;
    private final List<E> backingList;

    public SizeLimitedList(int n2) {
        this.maxSize = n2;
        this.backingList = new LinkedList();
    }

    public E get(int n2) {
        return this.backingList.get(n2);
    }

    public void add(E e2) {
        this.backingList.add(0, e2);
        if (this.backingList.size() > this.maxSize) {
            this.backingList.remove(this.maxSize);
        }
    }

    public int size() {
        return this.backingList.size();
    }

    public boolean contains(E e2) {
        return this.backingList.contains(e2);
    }
}

