/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;

public class ObjectConstant<T>
implements ObservableObjectValue<T> {
    private final T value;

    private ObjectConstant(T t2) {
        this.value = t2;
    }

    public static <T> ObjectConstant<T> valueOf(T t2) {
        return new ObjectConstant<T>(t2);
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
    }

    @Override
    public void addListener(ChangeListener<? super T> changeListener) {
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
    }

    @Override
    public void removeListener(ChangeListener<? super T> changeListener) {
    }
}

