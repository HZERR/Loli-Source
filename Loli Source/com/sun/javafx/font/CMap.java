/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontFileReader;
import com.sun.javafx.font.PrismFontFile;

abstract class CMap {
    static final char noSuchChar = '\ufffd';
    static final int SHORTMASK = 65535;
    static final int INTMASK = -1;
    private static final int MAX_CODE_POINTS = 0x10FFFF;
    public static final NullCMapClass theNullCmap = new NullCMapClass();

    CMap() {
    }

    static CMap initialize(PrismFontFile prismFontFile) {
        CMap cMap = null;
        int n2 = -1;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        boolean bl2 = false;
        FontFileReader.Buffer buffer = prismFontFile.readTable(1668112752);
        int n7 = buffer.getShort(2);
        block5: for (int i2 = 0; i2 < n7; ++i2) {
            buffer.position(i2 * 8 + 4);
            short s2 = buffer.getShort();
            if (s2 == 0) {
                bl = true;
                n2 = buffer.getShort();
                n6 = buffer.getInt();
                continue;
            }
            if (s2 != 3) continue;
            bl2 = true;
            n2 = buffer.getShort();
            int n8 = buffer.getInt();
            switch (n2) {
                case 0: {
                    n3 = n8;
                    continue block5;
                }
                case 1: {
                    n4 = n8;
                    continue block5;
                }
                case 10: {
                    n5 = n8;
                }
            }
        }
        if (bl2) {
            if (n5 != 0) {
                cMap = CMap.createCMap(buffer, n5);
            } else if (n3 != 0) {
                cMap = CMap.createCMap(buffer, n3);
            } else if (n4 != 0) {
                cMap = CMap.createCMap(buffer, n4);
            }
        } else {
            cMap = bl && n6 != 0 ? CMap.createCMap(buffer, n6) : CMap.createCMap(buffer, buffer.getInt(8));
        }
        return cMap;
    }

    static CMap createCMap(FontFileReader.Buffer buffer, int n2) {
        char c2 = buffer.getChar(n2);
        switch (c2) {
            case '\u0000': {
                return new CMapFormat0(buffer, n2);
            }
            case '\u0002': {
                return new CMapFormat2(buffer, n2);
            }
            case '\u0004': {
                return new CMapFormat4(buffer, n2);
            }
            case '\u0006': {
                return new CMapFormat6(buffer, n2);
            }
            case '\b': {
                return new CMapFormat8(buffer, n2);
            }
            case '\n': {
                return new CMapFormat10(buffer, n2);
            }
            case '\f': {
                return new CMapFormat12(buffer, n2);
            }
        }
        throw new RuntimeException("Cmap format unimplemented: " + buffer.getChar(n2));
    }

    abstract char getGlyph(int var1);

    final int getControlCodeGlyph(int n2, boolean bl) {
        if (n2 < 16) {
            switch (n2) {
                case 9: 
                case 10: 
                case 13: {
                    return 65535;
                }
            }
        } else if (n2 >= 8204) {
            if (n2 <= 8207 || n2 >= 8232 && n2 <= 8238 || n2 >= 8298 && n2 <= 8303) {
                return 65535;
            }
            if (bl && n2 >= 65535) {
                return 0;
            }
        }
        return -1;
    }

    static class NullCMapClass
    extends CMap {
        NullCMapClass() {
        }

        @Override
        char getGlyph(int n2) {
            return '\u0000';
        }
    }

    static class CMapFormat12
    extends CMap {
        int numGroups;
        int highBit = 0;
        int power;
        int extra;
        long[] startCharCode;
        long[] endCharCode;
        int[] startGlyphID;

        CMapFormat12(FontFileReader.Buffer buffer, int n2) {
            int n3;
            this.numGroups = buffer.getInt(n2 + 12);
            if (this.numGroups <= 0 || this.numGroups > 0x10FFFF || n2 > buffer.capacity() - this.numGroups * 12 - 12 - 4) {
                throw new RuntimeException("Invalid cmap subtable");
            }
            this.startCharCode = new long[this.numGroups];
            this.endCharCode = new long[this.numGroups];
            this.startGlyphID = new int[this.numGroups];
            buffer.position(n2 + 16);
            for (n3 = 0; n3 < this.numGroups; ++n3) {
                this.startCharCode[n3] = buffer.getInt() & 0xFFFFFFFF;
                this.endCharCode[n3] = buffer.getInt() & 0xFFFFFFFF;
                this.startGlyphID[n3] = buffer.getInt() & 0xFFFFFFFF;
            }
            n3 = this.numGroups;
            if (n3 >= 65536) {
                n3 >>= 16;
                this.highBit += 16;
            }
            if (n3 >= 256) {
                n3 >>= 8;
                this.highBit += 8;
            }
            if (n3 >= 16) {
                n3 >>= 4;
                this.highBit += 4;
            }
            if (n3 >= 4) {
                n3 >>= 2;
                this.highBit += 2;
            }
            if (n3 >= 2) {
                n3 >>= 1;
                ++this.highBit;
            }
            this.power = 1 << this.highBit;
            this.extra = this.numGroups - this.power;
        }

        @Override
        char getGlyph(int n2) {
            int n3 = this.getControlCodeGlyph(n2, false);
            if (n3 >= 0) {
                return (char)n3;
            }
            int n4 = this.power;
            int n5 = 0;
            if (this.startCharCode[this.extra] <= (long)n2) {
                n5 = this.extra;
            }
            while (n4 > 1) {
                if (this.startCharCode[n5 + (n4 >>= 1)] > (long)n2) continue;
                n5 += n4;
            }
            if (this.startCharCode[n5] <= (long)n2 && this.endCharCode[n5] >= (long)n2) {
                return (char)((long)this.startGlyphID[n5] + ((long)n2 - this.startCharCode[n5]));
            }
            return '\u0000';
        }
    }

    static class CMapFormat10
    extends CMap {
        long startCharCode;
        int numChars;
        char[] glyphIdArray;

        CMapFormat10(FontFileReader.Buffer buffer, int n2) {
            buffer.position(n2 + 12);
            this.startCharCode = buffer.getInt() & 0xFFFFFFFF;
            this.numChars = buffer.getInt() & 0xFFFFFFFF;
            if (this.numChars <= 0 || this.numChars > 0x10FFFF || n2 > buffer.capacity() - this.numChars * 2 - 12 - 8) {
                throw new RuntimeException("Invalid cmap subtable");
            }
            this.glyphIdArray = new char[this.numChars];
            for (int i2 = 0; i2 < this.numChars; ++i2) {
                this.glyphIdArray[i2] = buffer.getChar();
            }
        }

        @Override
        char getGlyph(int n2) {
            int n3 = (int)((long)n2 - this.startCharCode);
            if (n3 < 0 || n3 >= this.numChars) {
                return '\u0000';
            }
            return this.glyphIdArray[n3];
        }
    }

    static class CMapFormat8
    extends CMap {
        CMapFormat8(FontFileReader.Buffer buffer, int n2) {
        }

        @Override
        char getGlyph(int n2) {
            return '\u0000';
        }
    }

    static class CMapFormat6
    extends CMap {
        char firstCode;
        char entryCount;
        char[] glyphIdArray;

        CMapFormat6(FontFileReader.Buffer buffer, int n2) {
            buffer.position(n2 + 6);
            this.firstCode = buffer.getChar();
            this.entryCount = buffer.getChar();
            this.glyphIdArray = new char[this.entryCount];
            for (int i2 = 0; i2 < this.entryCount; ++i2) {
                this.glyphIdArray[i2] = buffer.getChar();
            }
        }

        @Override
        char getGlyph(int n2) {
            int n3 = this.getControlCodeGlyph(n2, true);
            if (n3 >= 0) {
                return (char)n3;
            }
            if ((n2 -= this.firstCode) < 0 || n2 >= this.entryCount) {
                return '\u0000';
            }
            return this.glyphIdArray[n2];
        }
    }

    static class CMapFormat2
    extends CMap {
        char[] subHeaderKey = new char[256];
        char[] firstCodeArray;
        char[] entryCountArray;
        short[] idDeltaArray;
        char[] idRangeOffSetArray;
        char[] glyphIndexArray;

        CMapFormat2(FontFileReader.Buffer buffer, int n2) {
            int n3;
            int n4;
            char c2 = buffer.getChar(n2 + 2);
            buffer.position(n2 + 6);
            char c3 = '\u0000';
            for (n4 = 0; n4 < 256; ++n4) {
                this.subHeaderKey[n4] = buffer.getChar();
                if (this.subHeaderKey[n4] <= c3) continue;
                c3 = this.subHeaderKey[n4];
            }
            n4 = (c3 >> 3) + 1;
            this.firstCodeArray = new char[n4];
            this.entryCountArray = new char[n4];
            this.idDeltaArray = new short[n4];
            this.idRangeOffSetArray = new char[n4];
            for (n3 = 0; n3 < n4; ++n3) {
                this.firstCodeArray[n3] = buffer.getChar();
                this.entryCountArray[n3] = buffer.getChar();
                this.idDeltaArray[n3] = (short)buffer.getChar();
                this.idRangeOffSetArray[n3] = buffer.getChar();
            }
            n3 = (c2 - 518 - n4 * 8) / 2;
            this.glyphIndexArray = new char[n3];
            for (int i2 = 0; i2 < n3; ++i2) {
                this.glyphIndexArray[i2] = buffer.getChar();
            }
        }

        @Override
        char getGlyph(int n2) {
            int n3;
            int n4;
            char c2;
            char c3;
            int n5 = this.getControlCodeGlyph(n2, true);
            if (n5 >= 0) {
                return (char)n5;
            }
            char c4 = (char)(n2 >> 8);
            char c5 = (char)(n2 & 0xFF);
            int n6 = this.subHeaderKey[c4] >> 3;
            if (n6 != 0) {
                c3 = c5;
            } else {
                c3 = c4;
                if (c3 == '\u0000') {
                    c3 = c5;
                }
            }
            char c6 = this.firstCodeArray[n6];
            if (c3 < c6) {
                return '\u0000';
            }
            if ((c3 = (char)(c3 - c6)) < this.entryCountArray[n6] && (c2 = this.glyphIndexArray[(n4 = (this.idRangeOffSetArray[n6] - (n3 = (this.idRangeOffSetArray.length - n6) * 8 - 6)) / 2) + c3]) != '\u0000') {
                c2 = (char)(c2 + this.idDeltaArray[n6]);
                return c2;
            }
            return '\u0000';
        }
    }

    static class CMapFormat0
    extends CMap {
        byte[] cmap;

        CMapFormat0(FontFileReader.Buffer buffer, int n2) {
            char c2 = buffer.getChar(n2 + 2);
            this.cmap = new byte[c2 - 6];
            buffer.get(n2 + 6, this.cmap, 0, c2 - 6);
        }

        @Override
        char getGlyph(int n2) {
            if (n2 < 256) {
                if (n2 < 16) {
                    switch (n2) {
                        case 9: 
                        case 10: 
                        case 13: {
                            return '\uffff';
                        }
                    }
                }
                return (char)(0xFF & this.cmap[n2]);
            }
            return '\u0000';
        }
    }

    static class CMapFormat4
    extends CMap {
        int segCount;
        int entrySelector;
        int rangeShift;
        char[] endCount;
        char[] startCount;
        short[] idDelta;
        char[] idRangeOffset;
        char[] glyphIds;

        CMapFormat4(FontFileReader.Buffer buffer, int n2) {
            int n3;
            int n4;
            buffer.position(n2);
            buffer.getChar();
            int n5 = buffer.getChar();
            if (n2 + n5 > buffer.capacity()) {
                n5 = buffer.capacity() - n2;
            }
            buffer.getChar();
            this.segCount = buffer.getChar() / 2;
            buffer.getChar();
            this.entrySelector = buffer.getChar();
            this.rangeShift = buffer.getChar() / 2;
            this.startCount = new char[this.segCount];
            this.endCount = new char[this.segCount];
            this.idDelta = new short[this.segCount];
            this.idRangeOffset = new char[this.segCount];
            for (n4 = 0; n4 < this.segCount; ++n4) {
                this.endCount[n4] = buffer.getChar();
            }
            buffer.getChar();
            for (n4 = 0; n4 < this.segCount; ++n4) {
                this.startCount[n4] = buffer.getChar();
            }
            for (n4 = 0; n4 < this.segCount; ++n4) {
                this.idDelta[n4] = (short)buffer.getChar();
            }
            for (n4 = 0; n4 < this.segCount; ++n4) {
                n3 = buffer.getChar();
                this.idRangeOffset[n4] = (char)(n3 >> 1 & 0xFFFF);
            }
            n4 = (this.segCount * 8 + 16) / 2;
            buffer.position(n4 * 2 + n2);
            n3 = n5 / 2 - n4;
            this.glyphIds = new char[n3];
            for (int i2 = 0; i2 < n3; ++i2) {
                this.glyphIds[i2] = buffer.getChar();
            }
        }

        @Override
        char getGlyph(int n2) {
            int n3 = 0;
            char c2 = '\u0000';
            int n4 = this.getControlCodeGlyph(n2, true);
            if (n4 >= 0) {
                return (char)n4;
            }
            int n5 = 0;
            int n6 = this.startCount.length;
            n3 = this.startCount.length >> 1;
            while (n5 < n6) {
                if (this.endCount[n3] < n2) {
                    n5 = n3 + 1;
                } else {
                    n6 = n3;
                }
                n3 = n5 + n6 >> 1;
            }
            if (n2 >= this.startCount[n3] && n2 <= this.endCount[n3]) {
                char c3 = this.idRangeOffset[n3];
                if (c3 == '\u0000') {
                    c2 = (char)(n2 + this.idDelta[n3]);
                } else {
                    int n7 = c3 - this.segCount + n3 + (n2 - this.startCount[n3]);
                    c2 = this.glyphIds[n7];
                    if (c2 != '\u0000') {
                        c2 = (char)(c2 + this.idDelta[n3]);
                    }
                }
            }
            return c2;
        }
    }
}

