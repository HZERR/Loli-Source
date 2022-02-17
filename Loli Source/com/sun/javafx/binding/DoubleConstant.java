/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;

public final class DoubleConstant
implements ObservableDoubleValue {
    private final double value;

    private DoubleConstant(double d2) {
        this.value = d2;
    }

    public static DoubleConstant valueOf(double d2) {
        return new DoubleConstant(d2);
    }

    @Override
    public double get() {
        return this.value;
    }

    @Override
    public Double getValue() {
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
        return (int)this.value;
    }

    @Override
    public long longValue() {
        return (long)this.value;
    }

    @Override
    public float floatValue() {
        return (float)this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }
}

