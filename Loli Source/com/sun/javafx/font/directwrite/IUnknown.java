/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.OS;

class IUnknown {
    long ptr;

    IUnknown(long l2) {
        this.ptr = l2;
    }

    int AddRef() {
        return OS.AddRef(this.ptr);
    }

    int Release() {
        int n2 = 0;
        if (this.ptr != 0L) {
            n2 = OS.Release(this.ptr);
            this.ptr = 0L;
        }
        return n2;
    }
}

