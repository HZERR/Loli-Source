/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

public abstract class CharToGlyphMapper {
    public static final int HI_SURROGATE_SHIFT = 10;
    public static final int HI_SURROGATE_START = 55296;
    public static final int HI_SURROGATE_END = 56319;
    public static final int LO_SURROGATE_START = 56320;
    public static final int LO_SURROGATE_END = 57343;
    public static final int SURROGATES_START = 65536;
    public static final int MISSING_GLYPH = 0;
    public static final int INVISIBLE_GLYPH_ID = 65535;
    protected int missingGlyph = 0;

    public boolean canDisplay(char c2) {
        int n2 = this.charToGlyph(c2);
        return n2 != this.missingGlyph;
    }

    public int getMissingGlyphCode() {
        return this.missingGlyph;
    }

    public abstract int getGlyphCode(int var1);

    public int charToGlyph(char c2) {
        return this.getGlyphCode(c2);
    }

    public int charToGlyph(int n2) {
        return this.getGlyphCode(n2);
    }

    public void charsToGlyphs(int n2, int n3, char[] arrc, int[] arrn, int n4) {
        for (int i2 = 0; i2 < n3; ++i2) {
            char c2;
            int n5 = arrc[n2 + i2];
            if (n5 >= 55296 && n5 <= 56319 && i2 + 1 < n3 && (c2 = arrc[n2 + i2 + 1]) >= '\udc00' && c2 <= '\udfff') {
                n5 = (n5 - 55296 << 10) + c2 - 56320 + 65536;
                arrn[n4 + i2] = this.getGlyphCode(n5);
                arrn[n4 + ++i2] = 65535;
                continue;
            }
            arrn[n4 + i2] = this.getGlyphCode(n5);
        }
    }

    public void charsToGlyphs(int n2, int n3, char[] arrc, int[] arrn) {
        this.charsToGlyphs(n2, n3, arrc, arrn, 0);
    }

    public void charsToGlyphs(int n2, char[] arrc, int[] arrn) {
        this.charsToGlyphs(0, n2, arrc, arrn, 0);
    }
}

