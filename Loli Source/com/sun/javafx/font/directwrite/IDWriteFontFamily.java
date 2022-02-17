/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFont;
import com.sun.javafx.font.directwrite.IDWriteFontList;
import com.sun.javafx.font.directwrite.IDWriteLocalizedStrings;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFontFamily
extends IDWriteFontList {
    IDWriteFontFamily(long l2) {
        super(l2);
    }

    IDWriteLocalizedStrings GetFamilyNames() {
        long l2 = OS.GetFamilyNames(this.ptr);
        return l2 != 0L ? new IDWriteLocalizedStrings(l2) : null;
    }

    IDWriteFont GetFirstMatchingFont(int n2, int n3, int n4) {
        long l2 = OS.GetFirstMatchingFont(this.ptr, n2, n3, n4);
        return l2 != 0L ? new IDWriteFont(l2) : null;
    }
}

