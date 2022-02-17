/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;

public final class IntegerConstant
implements ObservableIntegerValue {
    private final int value;

    private IntegerConstant(int n2) {
        this.value = n2;
    }

    public static IntegerConstant valueOf(int n2) {
        return new IntegerConstant(n2);
    }

    @Override
    public int get() {
        return this.value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
    }

    @Override
    public void addListener(ChangeListener<? super Number> changeListener) {
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
    }

    @Override
    public void removeListener(ChangeListener<? super Number> changeListener) {
    }

    @Override
    public int intValue() {
        return this.value;
    }

    @Override
    public long longValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }
}

