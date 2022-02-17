/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.ptr;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

public class ByteByReference
extends ByReference {
    public ByteByReference() {
        this(0);
    }

    public ByteByReference(byte value) {
        super(1);
        this.setValue(value);
    }

    public void setValue(byte value) {
        this.getPointer().setByte(0L, value);
    }

    public byte getValue() {
        return this.getPointer().getByte(0L);
    }

    @Override
    public String toString() {
        return String.format("byte@0x%1$x=0x%2$x (%2$d)", Pointer.nativeValue(this.getPointer()), this.getValue());
    }
}

