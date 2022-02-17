/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class JFXTextRenderer
extends IUnknown {
    JFXTextRenderer(long l2) {
        super(l2);
    }

    boolean Next() {
        return OS.JFXTextRendererNext(this.ptr);
    }

    int GetStart() {
        return OS.JFXTextRendererGetStart(this.ptr);
    }

    int GetLength() {
        return OS.JFXTextRendererGetLength(this.ptr);
    }

    int GetGlyphCount() {
        return OS.JFXTextRendererGetGlyphCount(this.ptr);
    }

    int GetTotalGlyphCount() {
        return OS.JFXTextRendererGetTotalGlyphCount(this.ptr);
    }

    IDWriteFontFace GetFontFace() {
        long l2 = OS.JFXTextRendererGetFontFace(this.ptr);
        return l2 != 0L ? new IDWriteFontFace(l2) : null;
    }

    int GetGlyphIndices(int[] arrn, int n2, int n3) {
        return OS.JFXTextRendererGetGlyphIndices(this.ptr, arrn, n2, n3);
    }

    int GetGlyphAdvances(float[] arrf, int n2) {
        return OS.JFXTextRendererGetGlyphAdvances(this.ptr, arrf, n2);
    }

    int GetGlyphOffsets(float[] arrf, int n2) {
        return OS.JFXTextRendererGetGlyphOffsets(this.ptr, arrf, n2);
    }

    int GetClusterMap(short[] arrs, int n2, int n3) {
        return OS.JFXTextRendererGetClusterMap(this.ptr, arrs, n2, n3);
    }
}

