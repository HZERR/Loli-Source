/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.binding;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

public interface Binding<T>
extends ObservableValue<T> {
    public boolean isValid();

    public void invalidate();

    public ObservableList<?> getDependencies();

    public void dispose();
}

