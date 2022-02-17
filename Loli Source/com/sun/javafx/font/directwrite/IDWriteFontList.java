/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFont;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFontList
extends IUnknown {
    IDWriteFontList(long l2) {
        super(l2);
    }

    int GetFontCount() {
        return OS.GetFontCount(this.ptr);
    }

    IDWriteFont GetFont(int n2) {
        long l2 = OS.GetFont(this.ptr, n2);
        return l2 != 0L ? new IDWriteFont(l2) : null;
    }
}

