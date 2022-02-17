/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.DWRITE_SCRIPT_ANALYSIS;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.JFXTextAnalysisSink;
import com.sun.javafx.font.directwrite.OS;

class IDWriteTextAnalyzer
extends IUnknown {
    IDWriteTextAnalyzer(long l2) {
        super(l2);
    }

    int AnalyzeScript(JFXTextAnalysisSink jFXTextAnalysisSink, int n2, int n3, JFXTextAnalysisSink jFXTextAnalysisSink2) {
        return OS.AnalyzeScript(this.ptr, jFXTextAnalysisSink.ptr, n2, n3, jFXTextAnalysisSink2.ptr);
    }

    int GetGlyphs(char[] arrc, int n2, int n3, IDWriteFontFace iDWriteFontFace, boolean bl, boolean bl2, DWRITE_SCRIPT_ANALYSIS dWRITE_SCRIPT_ANALYSIS, String string, long l2, long[] arrl, int[] arrn, int n4, int n5, short[] arrs, short[] arrs2, short[] arrs3, short[] arrs4, int[] arrn2) {
        return OS.GetGlyphs(this.ptr, arrc, n2, n3, iDWriteFontFace.ptr, bl, bl2, dWRITE_SCRIPT_ANALYSIS, string != null ? (string + '\u0000').toCharArray() : (char[])null, l2, arrl, arrn, n4, n5, arrs, arrs2, arrs3, arrs4, arrn2);
    }

    int GetGlyphPlacements(char[] arrc, short[] arrs, short[] arrs2, int n2, int n3, short[] arrs3, short[] arrs4, int n4, IDWriteFontFace iDWriteFontFace, float f2, boolean bl, boolean bl2, DWRITE_SCRIPT_ANALYSIS dWRITE_SCRIPT_ANALYSIS, String string, long[] arrl, int[] arrn, int n5, float[] arrf, float[] arrf2) {
        return OS.GetGlyphPlacements(this.ptr, arrc, arrs, arrs2, n2, n3, arrs3, arrs4, n4, iDWriteFontFace.ptr, f2, bl, bl2, dWRITE_SCRIPT_ANALYSIS, string != null ? (string + '\u0000').toCharArray() : (char[])null, arrl, arrn, n5, arrf, arrf2);
    }
}

