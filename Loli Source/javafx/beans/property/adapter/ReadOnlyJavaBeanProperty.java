/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property.adapter;

import javafx.beans.property.ReadOnlyProperty;

public interface ReadOnlyJavaBeanProperty<T>
extends ReadOnlyProperty<T> {
    public void fireValueChangedEvent();

    public void dispose();
}

