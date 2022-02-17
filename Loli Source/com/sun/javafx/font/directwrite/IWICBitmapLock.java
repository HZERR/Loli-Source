/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IWICBitmapLock
extends IUnknown {
    IWICBitmapLock(long l2) {
        super(l2);
    }

    byte[] GetDataPointer() {
        return OS.GetDataPointer(this.ptr);
    }

    int GetStride() {
        return OS.GetStride(this.ptr);
    }
}

