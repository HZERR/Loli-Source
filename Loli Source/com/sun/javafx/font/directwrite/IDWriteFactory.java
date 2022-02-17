/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.DWRITE_GLYPH_RUN;
import com.sun.javafx.font.directwrite.DWRITE_MATRIX;
import com.sun.javafx.font.directwrite.IDWriteFontCollection;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteFontFile;
import com.sun.javafx.font.directwrite.IDWriteGlyphRunAnalysis;
import com.sun.javafx.font.directwrite.IDWriteTextAnalyzer;
import com.sun.javafx.font.directwrite.IDWriteTextFormat;
import com.sun.javafx.font.directwrite.IDWriteTextLayout;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFactory
extends IUnknown {
    IDWriteFactory(long l2) {
        super(l2);
    }

    IDWriteFontCollection GetSystemFontCollection(boolean bl) {
        long l2 = OS.GetSystemFontCollection(this.ptr, bl);
        return l2 != 0L ? new IDWriteFontCollection(l2) : null;
    }

    IDWriteTextAnalyzer CreateTextAnalyzer() {
        long l2 = OS.CreateTextAnalyzer(this.ptr);
        return l2 != 0L ? new IDWriteTextAnalyzer(l2) : null;
    }

    IDWriteTextFormat CreateTextFormat(String string, IDWriteFontCollection iDWriteFontCollection, int n2, int n3, int n4, float f2, String string2) {
        long l2 = OS.CreateTextFormat(this.ptr, (string + '\u0000').toCharArray(), iDWriteFontCollection.ptr, n2, n3, n4, f2, (string2 + '\u0000').toCharArray());
        return l2 != 0L ? new IDWriteTextFormat(l2) : null;
    }

    IDWriteTextLayout CreateTextLayout(char[] arrc, int n2, int n3, IDWriteTextFormat iDWriteTextFormat, float f2, float f3) {
        long l2 = OS.CreateTextLayout(this.ptr, arrc, n2, n3, iDWriteTextFormat.ptr, f2, f3);
        return l2 != 0L ? new IDWriteTextLayout(l2) : null;
    }

    IDWriteGlyphRunAnalysis CreateGlyphRunAnalysis(DWRITE_GLYPH_RUN dWRITE_GLYPH_RUN, float f2, DWRITE_MATRIX dWRITE_MATRIX, int n2, int n3, float f3, float f4) {
        long l2 = OS.CreateGlyphRunAnalysis(this.ptr, dWRITE_GLYPH_RUN, f2, dWRITE_MATRIX, n2, n3, f3, f4);
        return l2 != 0L ? new IDWriteGlyphRunAnalysis(l2) : null;
    }

    IDWriteFontFile CreateFontFileReference(String string) {
        long l2 = OS.CreateFontFileReference(this.ptr, (string + '\u0000').toCharArray());
        return l2 != 0L ? new IDWriteFontFile(l2) : null;
    }

    IDWriteFontFace CreateFontFace(int n2, IDWriteFontFile iDWriteFontFile, int n3, int n4) {
        long l2 = OS.CreateFontFace(this.ptr, n2, iDWriteFontFile.ptr, n3, n4);
        return l2 != 0L ? new IDWriteFontFace(l2) : null;
    }
}

