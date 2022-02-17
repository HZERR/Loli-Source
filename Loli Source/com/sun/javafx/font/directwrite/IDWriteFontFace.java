/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.DWRITE_GLYPH_METRICS;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;
import com.sun.javafx.geom.Path2D;

class IDWriteFontFace
extends IUnknown {
    IDWriteFontFace(long l2) {
        super(l2);
    }

    DWRITE_GLYPH_METRICS GetDesignGlyphMetrics(short s2, boolean bl) {
        return OS.GetDesignGlyphMetrics(this.ptr, s2, bl);
    }

    Path2D GetGlyphRunOutline(float f2, short s2, boolean bl) {
        return OS.GetGlyphRunOutline(this.ptr, f2, s2, bl);
    }
}

