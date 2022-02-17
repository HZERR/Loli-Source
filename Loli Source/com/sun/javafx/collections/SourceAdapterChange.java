/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class SourceAdapterChange<E>
extends ListChangeListener.Change<E> {
    private final ListChangeListener.Change<? extends E> change;
    private int[] perm;

    public SourceAdapterChange(ObservableList<E> observableList, ListChangeListener.Change<? extends E> change) {
        super(observableList);
        this.change = change;
    }

    @Override
    public boolean next() {
        this.perm = null;
        return this.change.next();
    }

    @Override
    public void reset() {
        this.change.reset();
    }

    @Override
    public int getTo() {
        return this.change.getTo();
    }

    @Override
    public List<E> getRemoved() {
        return this.change.getRemoved();
    }

    @Override
    public int getFrom() {
        return this.change.getFrom();
    }

    @Override
    public boolean wasUpdated() {
        return this.change.wasUpdated();
    }

    @Override
    protected int[] getPermutation() {
        if (this.perm == null) {
            if (this.change.wasPermutated()) {
                int n2 = this.change.getFrom();
                int n3 = this.change.getTo() - n2;
                this.perm = new int[n3];
                for (int i2 = 0; i2 < n3; ++i2) {
                    this.perm[i2] = this.change.getPermutation(n2 + i2);
                }
            } else {
                this.perm = new int[0];
            }
        }
        return this.perm;
    }

    public String toString() {
        return this.change.toString();
    }
}

