/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.freetype;

import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.freetype.FTDisposer;
import com.sun.javafx.font.freetype.FTFactory;
import com.sun.javafx.font.freetype.FTFontStrike;
import com.sun.javafx.font.freetype.FTGlyph;
import com.sun.javafx.font.freetype.FT_Bitmap;
import com.sun.javafx.font.freetype.FT_GlyphSlotRec;
import com.sun.javafx.font.freetype.FT_Glyph_Metrics;
import com.sun.javafx.font.freetype.FT_Matrix;
import com.sun.javafx.font.freetype.OSFreetype;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

class FTFontFile
extends PrismFontFile {
    private long library;
    private long face;
    private FTDisposer disposer;

    FTFontFile(String string, String string2, int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        super(string, string2, n2, bl, bl2, bl3, bl4);
        this.init();
    }

    private synchronized void init() throws Exception {
        long[] arrl = new long[1];
        int n2 = OSFreetype.FT_Init_FreeType(arrl);
        if (n2 != 0) {
            throw new Exception("FT_Init_FreeType Failed error " + n2);
        }
        this.library = arrl[0];
        if (FTFactory.LCD_SUPPORT) {
            OSFreetype.FT_Library_SetLcdFilter(this.library, 1);
        }
        String string = this.getFileName();
        int n3 = this.getFontIndex();
        byte[] arrby = (string + "\u0000").getBytes();
        n2 = OSFreetype.FT_New_Face(this.library, arrby, n3, arrl);
        if (n2 != 0) {
            throw new Exception("FT_New_Face Failed error " + n2 + " Font File " + string + " Font Index " + n3);
        }
        this.face = arrl[0];
        if (!this.isRegistered()) {
            this.disposer = new FTDisposer(this.library, this.face);
            Disposer.addRecord(this, this.disposer);
        }
    }

    @Override
    protected PrismFontStrike<?> createStrike(float f2, BaseTransform baseTransform, int n2, FontStrikeDesc fontStrikeDesc) {
        return new FTFontStrike(this, f2, baseTransform, n2, fontStrikeDesc);
    }

    @Override
    protected synchronized int[] createGlyphBoundingBox(int n2) {
        int n3 = 1;
        OSFreetype.FT_Load_Glyph(this.face, n2, n3);
        int[] arrn = new int[4];
        FT_GlyphSlotRec fT_GlyphSlotRec = OSFreetype.getGlyphSlot(this.face);
        if (fT_GlyphSlotRec != null && fT_GlyphSlotRec.metrics != null) {
            FT_Glyph_Metrics fT_Glyph_Metrics = fT_GlyphSlotRec.metrics;
            arrn[0] = (int)fT_Glyph_Metrics.horiBearingX;
            arrn[1] = (int)(fT_Glyph_Metrics.horiBearingY - fT_Glyph_Metrics.height);
            arrn[2] = (int)(fT_Glyph_Metrics.horiBearingX + fT_Glyph_Metrics.width);
            arrn[3] = (int)fT_Glyph_Metrics.horiBearingY;
        }
        return arrn;
    }

    synchronized Path2D createGlyphOutline(int n2, float f2) {
        int n3 = (int)(f2 * 64.0f);
        OSFreetype.FT_Set_Char_Size(this.face, 0L, n3, 72, 72);
        int n4 = 2058;
        OSFreetype.FT_Load_Glyph(this.face, n2, n4);
        return OSFreetype.FT_Outline_Decompose(this.face);
    }

    synchronized void initGlyph(FTGlyph fTGlyph, FTFontStrike fTFontStrike) {
        byte[] arrby;
        float f2 = fTFontStrike.getSize();
        if (f2 == 0.0f) {
            fTGlyph.buffer = new byte[0];
            fTGlyph.bitmap = new FT_Bitmap();
            return;
        }
        int n2 = (int)(f2 * 64.0f);
        OSFreetype.FT_Set_Char_Size(this.face, 0L, n2, 72, 72);
        boolean bl = fTFontStrike.getAAMode() == 1 && FTFactory.LCD_SUPPORT;
        int n3 = 14;
        FT_Matrix fT_Matrix = fTFontStrike.matrix;
        if (fT_Matrix != null) {
            OSFreetype.FT_Set_Transform(this.face, fT_Matrix, 0L, 0L);
        } else {
            n3 |= 0x800;
        }
        n3 = bl ? (n3 |= 0x30000) : (n3 |= 0);
        int n4 = fTGlyph.getGlyphCode();
        int n5 = OSFreetype.FT_Load_Glyph(this.face, n4, n3);
        if (n5 != 0) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("FT_Load_Glyph failed " + n5 + " glyph code " + n4 + " load falgs " + n3);
            }
            return;
        }
        FT_GlyphSlotRec fT_GlyphSlotRec = OSFreetype.getGlyphSlot(this.face);
        if (fT_GlyphSlotRec == null) {
            return;
        }
        FT_Bitmap fT_Bitmap = fT_GlyphSlotRec.bitmap;
        if (fT_Bitmap == null) {
            return;
        }
        byte by = fT_Bitmap.pixel_mode;
        int n6 = fT_Bitmap.width;
        int n7 = fT_Bitmap.rows;
        int n8 = fT_Bitmap.pitch;
        if (by != 2 && by != 5) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("Unexpected pixel mode: " + by + " glyph code " + n4 + " load falgs " + n3);
            }
            return;
        }
        if (n6 != 0 && n7 != 0) {
            arrby = OSFreetype.getBitmapData(this.face);
            if (arrby != null && n8 != n6) {
                byte[] arrby2 = new byte[n6 * n7];
                int n9 = 0;
                int n10 = 0;
                for (int i2 = 0; i2 < n7; ++i2) {
                    for (int i3 = 0; i3 < n6; ++i3) {
                        arrby2[n10 + i3] = arrby[n9 + i3];
                    }
                    n10 += n6;
                    n9 += n8;
                }
                arrby = arrby2;
            }
        } else {
            arrby = new byte[]{};
        }
        fTGlyph.buffer = arrby;
        fTGlyph.bitmap = fT_Bitmap;
        fTGlyph.bitmap_left = fT_GlyphSlotRec.bitmap_left;
        fTGlyph.bitmap_top = fT_GlyphSlotRec.bitmap_top;
        fTGlyph.advanceX = (float)fT_GlyphSlotRec.advance_x / 64.0f;
        fTGlyph.advanceY = (float)fT_GlyphSlotRec.advance_y / 64.0f;
        fTGlyph.userAdvance = (float)fT_GlyphSlotRec.linearHoriAdvance / 65536.0f;
        fTGlyph.lcd = bl;
    }
}

