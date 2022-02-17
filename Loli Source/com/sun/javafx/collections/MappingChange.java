/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ChangeHelper;
import java.util.AbstractList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public final class MappingChange<E, F>
extends ListChangeListener.Change<F> {
    private final Map<E, F> map;
    private final ListChangeListener.Change<? extends E> original;
    private List<F> removed;
    public static final Map NOOP_MAP = new Map(){

        public Object map(Object object) {
            return object;
        }
    };

    public MappingChange(ListChangeListener.Change<? extends E> change, Map<E, F> map, ObservableList<F> observableList) {
        super(observableList);
        this.original = change;
        this.map = map;
    }

    @Override
    public boolean next() {
        return this.original.next();
    }

    @Override
    public void reset() {
        this.original.reset();
    }

    @Override
    public int getFrom() {
        return this.original.getFrom();
    }

    @Override
    public int getTo() {
        return this.original.getTo();
    }

    @Override
    public List<F> getRemoved() {
        if (this.removed == null) {
            this.removed = new AbstractList<F>(){

                @Override
                public F get(int n2) {
                    return MappingChange.this.map.map(MappingChange.this.original.getRemoved().get(n2));
                }

                @Override
                public int size() {
                    return MappingChange.this.original.getRemovedSize();
                }
            };
        }
        return this.removed;
    }

    @Override
    protected int[] getPermutation() {
        return new int[0];
    }

    @Override
    public boolean wasPermutated() {
        return this.original.wasPermutated();
    }

    @Override
    public boolean wasUpdated() {
        return this.original.wasUpdated();
    }

    @Override
    public int getPermutation(int n2) {
        return this.original.getPermutation(n2);
    }

    public String toString() {
        int n2 = 0;
        while (this.next()) {
            ++n2;
        }
        int n3 = 0;
        this.reset();
        while (this.next()) {
            ++n3;
        }
        this.reset();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ ");
        int n4 = 0;
        while (this.next()) {
            if (this.wasPermutated()) {
                stringBuilder.append(ChangeHelper.permChangeToString(this.getPermutation()));
            } else if (this.wasUpdated()) {
                stringBuilder.append(ChangeHelper.updateChangeToString(this.getFrom(), this.getTo()));
            } else {
                stringBuilder.append(ChangeHelper.addRemoveChangeToString(this.getFrom(), this.getTo(), this.getList(), this.getRemoved()));
            }
            if (n4 == n3) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append(" }");
        this.reset();
        n4 = n3 - n2;
        while (n4-- > 0) {
            this.next();
        }
        return stringBuilder.toString();
    }

    public static interface Map<E, F> {
        public F map(E var1);
    }
}

