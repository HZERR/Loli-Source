/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteLocalizedStrings
extends IUnknown {
    IDWriteLocalizedStrings(long l2) {
        super(l2);
    }

    int FindLocaleName(String string) {
        return OS.FindLocaleName(this.ptr, (string + '\u0000').toCharArray());
    }

    int GetStringLength(int n2) {
        return OS.GetStringLength(this.ptr, n2);
    }

    String GetString(int n2, int n3) {
        char[] arrc = OS.GetString(this.ptr, n2, n3 + 1);
        return arrc != null ? new String(arrc, 0, n3) : null;
    }
}

