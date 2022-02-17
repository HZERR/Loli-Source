/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFont;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteFontFamily;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFontCollection
extends IUnknown {
    IDWriteFontCollection(long l2) {
        super(l2);
    }

    int GetFontFamilyCount() {
        return OS.GetFontFamilyCount(this.ptr);
    }

    IDWriteFontFamily GetFontFamily(int n2) {
        long l2 = OS.GetFontFamily(this.ptr, n2);
        return l2 != 0L ? new IDWriteFontFamily(l2) : null;
    }

    int FindFamilyName(String string) {
        return OS.FindFamilyName(this.ptr, (string + '\u0000').toCharArray());
    }

    IDWriteFont GetFontFromFontFace(IDWriteFontFace iDWriteFontFace) {
        long l2 = OS.GetFontFromFontFace(this.ptr, iDWriteFontFace.ptr);
        return l2 != 0L ? new IDWriteFont(l2) : null;
    }
}

