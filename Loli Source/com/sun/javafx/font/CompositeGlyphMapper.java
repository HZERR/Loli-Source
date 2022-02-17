/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.CompositeFontResource;
import java.util.HashMap;

public class CompositeGlyphMapper
extends CharToGlyphMapper {
    public static final int SLOTMASK = -16777216;
    public static final int GLYPHMASK = 0xFFFFFF;
    public static final int NBLOCKS = 216;
    public static final int BLOCKSZ = 256;
    public static final int MAXUNICODE = 55296;
    private static final int SIMPLE_ASCII_MASK_START = 32;
    private static final int SIMPLE_ASCII_MASK_END = 126;
    private static final int ASCII_COUNT = 95;
    private boolean asciiCacheOK;
    private char[] charToGlyph;
    CompositeFontResource font;
    CharToGlyphMapper[] slotMappers;
    HashMap<Integer, Integer> glyphMap;

    public CompositeGlyphMapper(CompositeFontResource compositeFontResource) {
        this.font = compositeFontResource;
        this.missingGlyph = 0;
        this.glyphMap = new HashMap();
        this.slotMappers = new CharToGlyphMapper[compositeFontResource.getNumSlots()];
        this.asciiCacheOK = true;
    }

    private final CharToGlyphMapper getSlotMapper(int n2) {
        Object object;
        if (n2 >= this.slotMappers.length) {
            object = new CharToGlyphMapper[this.font.getNumSlots()];
            System.arraycopy(this.slotMappers, 0, object, 0, this.slotMappers.length);
            this.slotMappers = object;
        }
        if ((object = this.slotMappers[n2]) == null) {
            this.slotMappers[n2] = object = this.font.getSlotResource(n2).getGlyphMapper();
        }
        return object;
    }

    @Override
    public int getMissingGlyphCode() {
        return this.missingGlyph;
    }

    public final int compositeGlyphCode(int n2, int n3) {
        return n2 << 24 | n3 & 0xFFFFFF;
    }

    private final int convertToGlyph(int n2) {
        for (int i2 = 0; i2 < this.font.getNumSlots(); ++i2) {
            CharToGlyphMapper charToGlyphMapper = this.getSlotMapper(i2);
            int n3 = charToGlyphMapper.charToGlyph(n2);
            if (n3 == charToGlyphMapper.getMissingGlyphCode()) continue;
            n3 = this.compositeGlyphCode(i2, n3);
            this.glyphMap.put(n2, n3);
            return n3;
        }
        this.glyphMap.put(n2, this.missingGlyph);
        return this.missingGlyph;
    }

    private int getAsciiGlyphCode(int n2) {
        if (!this.asciiCacheOK || n2 > 126 || n2 < 32) {
            return -1;
        }
        if (this.charToGlyph == null) {
            char[] arrc = new char[95];
            CharToGlyphMapper charToGlyphMapper = this.getSlotMapper(0);
            int n3 = charToGlyphMapper.getMissingGlyphCode();
            for (int i2 = 0; i2 < 95; ++i2) {
                int n4 = charToGlyphMapper.charToGlyph(32 + i2);
                if (n4 == n3) {
                    this.charToGlyph = null;
                    this.asciiCacheOK = false;
                    return -1;
                }
                arrc[i2] = (char)n4;
            }
            this.charToGlyph = arrc;
        }
        int n5 = n2 - 32;
        return this.charToGlyph[n5];
    }

    @Override
    public int getGlyphCode(int n2) {
        int n3 = this.getAsciiGlyphCode(n2);
        if (n3 >= 0) {
            return n3;
        }
        Integer n4 = this.glyphMap.get(n2);
        if (n4 != null) {
            return n4;
        }
        return this.convertToGlyph(n2);
    }
}

