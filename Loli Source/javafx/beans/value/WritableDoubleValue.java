/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.value;

import javafx.beans.value.WritableNumberValue;

public interface WritableDoubleValue
extends WritableNumberValue {
    public double get();

    public void set(double var1);

    @Override
    public void setValue(Number var1);
}

