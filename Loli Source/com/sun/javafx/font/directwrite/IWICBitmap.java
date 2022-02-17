/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.IWICBitmapLock;
import com.sun.javafx.font.directwrite.OS;

class IWICBitmap
extends IUnknown {
    IWICBitmap(long l2) {
        super(l2);
    }

    IWICBitmapLock Lock(int n2, int n3, int n4, int n5, int n6) {
        long l2 = OS.Lock(this.ptr, n2, n3, n4, n5, n6);
        return l2 != 0L ? new IWICBitmapLock(l2) : null;
    }
}

