/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.value;

import javafx.beans.value.WritableValue;

public interface WritableBooleanValue
extends WritableValue<Boolean> {
    public boolean get();

    public void set(boolean var1);

    @Override
    public void setValue(Boolean var1);
}

