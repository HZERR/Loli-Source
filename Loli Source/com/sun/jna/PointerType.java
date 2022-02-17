/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Klass;
import com.sun.jna.NativeMapped;
import com.sun.jna.Pointer;

public abstract class PointerType
implements NativeMapped {
    private Pointer pointer;

    protected PointerType() {
        this.pointer = Pointer.NULL;
    }

    protected PointerType(Pointer p2) {
        this.pointer = p2;
    }

    @Override
    public Class<?> nativeType() {
        return Pointer.class;
    }

    @Override
    public Object toNative() {
        return this.getPointer();
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    public void setPointer(Pointer p2) {
        this.pointer = p2;
    }

    @Override
    public Object fromNative(Object nativeValue, FromNativeContext context) {
        if (nativeValue == null) {
            return null;
        }
        PointerType pt = (PointerType)Klass.newInstance(this.getClass());
        pt.pointer = (Pointer)nativeValue;
        return pt;
    }

    public int hashCode() {
        return this.pointer != null ? this.pointer.hashCode() : 0;
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (o2 instanceof PointerType) {
            Pointer p2 = ((PointerType)o2).getPointer();
            if (this.pointer == null) {
                return p2 == null;
            }
            return this.pointer.equals(p2);
        }
        return false;
    }

    public String toString() {
        return this.pointer == null ? "NULL" : this.pointer.toString() + " (" + super.toString() + ")";
    }
}

