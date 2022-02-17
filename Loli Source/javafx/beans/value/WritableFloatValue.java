/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.value;

import javafx.beans.value.WritableNumberValue;

public interface WritableFloatValue
extends WritableNumberValue {
    public float get();

    public void set(float var1);

    @Override
    public void setValue(Number var1);
}

