/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.value;

import javafx.beans.value.ObservableValue;

public interface ObservableNumberValue
extends ObservableValue<Number> {
    public int intValue();

    public long longValue();

    public float floatValue();

    public double doubleValue();
}

