/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;
import com.sun.javafx.font.directwrite.RECT;

class IDWriteGlyphRunAnalysis
extends IUnknown {
    IDWriteGlyphRunAnalysis(long l2) {
        super(l2);
    }

    byte[] CreateAlphaTexture(int n2, RECT rECT) {
        return OS.CreateAlphaTexture(this.ptr, n2, rECT);
    }

    RECT GetAlphaTextureBounds(int n2) {
        return OS.GetAlphaTextureBounds(this.ptr, n2);
    }
}

