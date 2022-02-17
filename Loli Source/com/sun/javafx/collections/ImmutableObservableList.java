/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ImmutableObservableList<E>
extends AbstractList<E>
implements ObservableList<E> {
    private final E[] elements;

    public ImmutableObservableList(E ... arrE) {
        this.elements = arrE == null || arrE.length == 0 ? null : Arrays.copyOf(arrE, arrE.length);
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
    }

    @Override
    public void addListener(ListChangeListener<? super E> listChangeListener) {
    }

    @Override
    public void removeListener(ListChangeListener<? super E> listChangeListener) {
    }

    @Override
    public boolean addAll(E ... arrE) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setAll(E ... arrE) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(E ... arrE) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(E ... arrE) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(int n2, int n3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int n2) {
        if (n2 < 0 || n2 >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        return this.elements[n2];
    }

    @Override
    public int size() {
        return this.elements == null ? 0 : this.elements.length;
    }
}

