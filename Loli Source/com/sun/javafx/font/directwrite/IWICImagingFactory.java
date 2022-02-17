/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.IWICBitmap;
import com.sun.javafx.font.directwrite.OS;

class IWICImagingFactory
extends IUnknown {
    IWICImagingFactory(long l2) {
        super(l2);
    }

    IWICBitmap CreateBitmap(int n2, int n3, int n4, int n5) {
        long l2 = OS.CreateBitmap(this.ptr, n2, n3, n4, n5);
        return l2 != 0L ? new IWICBitmap(l2) : null;
    }
}

