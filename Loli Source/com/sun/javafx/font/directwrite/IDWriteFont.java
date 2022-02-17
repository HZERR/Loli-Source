/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteFontFamily;
import com.sun.javafx.font.directwrite.IDWriteLocalizedStrings;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFont
extends IUnknown {
    IDWriteFont(long l2) {
        super(l2);
    }

    IDWriteFontFace CreateFontFace() {
        long l2 = OS.CreateFontFace(this.ptr);
        return l2 != 0L ? new IDWriteFontFace(l2) : null;
    }

    IDWriteLocalizedStrings GetFaceNames() {
        long l2 = OS.GetFaceNames(this.ptr);
        return l2 != 0L ? new IDWriteLocalizedStrings(l2) : null;
    }

    IDWriteFontFamily GetFontFamily() {
        long l2 = OS.GetFontFamily(this.ptr);
        return l2 != 0L ? new IDWriteFontFamily(l2) : null;
    }

    IDWriteLocalizedStrings GetInformationalStrings(int n2) {
        long l2 = OS.GetInformationalStrings(this.ptr, n2);
        return l2 != 0L ? new IDWriteLocalizedStrings(l2) : null;
    }

    int GetSimulations() {
        return OS.GetSimulations(this.ptr);
    }

    int GetStretch() {
        return OS.GetStretch(this.ptr);
    }

    int GetStyle() {
        return OS.GetStyle(this.ptr);
    }

    int GetWeight() {
        return OS.GetWeight(this.ptr);
    }
}

