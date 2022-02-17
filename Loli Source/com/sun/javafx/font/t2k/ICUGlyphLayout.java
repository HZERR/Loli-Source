/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.t2k;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.t2k.T2KFontFile;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.text.ScriptMapper;
import com.sun.javafx.text.TextRun;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.Bidi;

class ICUGlyphLayout
extends GlyphLayout {
    private float[] _mat = new float[4];
    private long textPtr;
    private static final int CANONICAL_MASK = 448;

    private static native void initIDs();

    private static native long createTextPtr(char[] var0);

    private static native void freeTextPtr(long var0);

    private static native void nativeLayout(T2KFontFile var0, FontStrike var1, float var2, float[] var3, int var4, long var5, int var7, int var8, int var9, int var10, int var11, int var12, int var13, TextRun var14, int var15, int var16, long var17);

    @Override
    public int breakRuns(PrismTextLayout prismTextLayout, char[] arrc, int n2) {
        int n3;
        int n4;
        int n5 = arrc.length;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = true;
        boolean bl6 = true;
        if ((n2 & 2) != 0) {
            bl5 = (n2 & 0x10) != 0;
            bl6 = (n2 & 8) != 0;
        }
        TextRun textRun = null;
        Bidi bidi = null;
        byte by = 0;
        int n10 = n5;
        int n11 = 0;
        int n12 = 0;
        TextSpan textSpan = null;
        int n13 = n5;
        PGFont pGFont = null;
        TextSpan[] arrtextSpan = prismTextLayout.getTextSpans();
        if (arrtextSpan != null) {
            if (arrtextSpan.length > 0) {
                textSpan = arrtextSpan[n12];
                n13 = textSpan.getText().length();
                pGFont = (PGFont)textSpan.getFont();
                if (pGFont == null) {
                    n2 |= 0x20;
                }
            }
        } else {
            pGFont = prismTextLayout.getFont();
        }
        CharToGlyphMapper charToGlyphMapper = null;
        if (pGFont != null) {
            FontResource fontResource = pGFont.getFontResource();
            n4 = pGFont.getFeatures();
            boolean bl7 = bl4 = (n4 & (n3 = fontResource.getFeatures())) != 0;
            if (bl5 && fontResource instanceof CompositeFontResource) {
                charToGlyphMapper = fontResource.getGlyphMapper();
            }
        }
        if (bl6 && n5 > 0) {
            int n14 = prismTextLayout.getDirection();
            bidi = new Bidi(arrc, 0, null, 0, n5, n14);
            by = (byte)bidi.getLevelAt(bidi.getRunStart(n11));
            n10 = bidi.getRunLimit(n11);
            if ((by & 1) != 0) {
                n2 |= 8;
            }
        }
        int n15 = 0;
        n4 = 0;
        while (n4 < n5) {
            int n16 = n3 = arrc[n4];
            boolean bl8 = n3 == 9 || n3 == 10 || n3 == 13;
            boolean bl9 = n4 >= n13;
            boolean bl10 = n4 >= n10;
            boolean bl11 = false;
            boolean bl12 = false;
            boolean bl13 = false;
            if (bl5 && !bl8 && !bl9 && !bl10) {
                if (Character.isHighSurrogate((char)n3) && n4 + 1 < n13 && Character.isLowSurrogate(arrc[n4 + 1])) {
                    n16 = Character.toCodePoint((char)n3, arrc[++n4]);
                }
                if (bl) {
                    int n17;
                    if (charToGlyphMapper != null && !Character.isWhitespace(n16) && n8 != (n9 = (n17 = charToGlyphMapper.charToGlyph(n16)) != 0 ? n17 >>> 24 : -1)) {
                        bl11 = true;
                    }
                    n7 = ScriptMapper.getScript(n16);
                    if (n6 > 1 && n7 > 1 && n7 != n6) {
                        bl12 = true;
                    }
                    if (!bl3 && ((n17 = 1 << Character.getType(n16)) & 0x1C0) != 0) {
                        bl3 = true;
                    }
                } else {
                    boolean bl14 = bl2 = bl4 || ScriptMapper.isComplexCharCode(n16);
                    if (bl2) {
                        bl13 = true;
                    }
                }
            }
            if (bl8 || bl9 || bl10 || bl11 || bl12 || bl13) {
                int n18;
                if (n4 != n15) {
                    textRun = new TextRun(n15, n4 - n15, by, bl, n6, textSpan, n8, bl3);
                    prismTextLayout.addTextRun(textRun);
                    if (bl) {
                        n2 |= 0x10;
                    }
                    bl3 = false;
                    n15 = n4;
                }
                if (bl8) {
                    if (n3 == 13 && ++n4 < n13 && arrc[n4] == '\n') {
                        ++n4;
                    }
                    textRun = new TextRun(n15, n4 - n15, by, false, 0, textSpan, 0, false);
                    if (n3 == 9) {
                        textRun.setTab();
                        n2 |= 4;
                    } else {
                        textRun.setLinebreak();
                    }
                    prismTextLayout.addTextRun(textRun);
                    n15 = n4;
                    if (n4 == n5) break;
                    bl9 = n4 >= n13;
                    bl10 = n4 >= n10;
                    bl3 = false;
                }
                if (bl9) {
                    textSpan = arrtextSpan[++n12];
                    n13 += textSpan.getText().length();
                    pGFont = (PGFont)textSpan.getFont();
                    charToGlyphMapper = null;
                    if (pGFont == null) {
                        n2 |= 0x20;
                    } else {
                        int n19;
                        FontResource fontResource = pGFont.getFontResource();
                        n18 = pGFont.getFeatures();
                        boolean bl15 = bl4 = (n18 & (n19 = fontResource.getFeatures())) != 0;
                        if (bl5 && fontResource instanceof CompositeFontResource) {
                            charToGlyphMapper = fontResource.getGlyphMapper();
                        }
                    }
                }
                if (bl10) {
                    by = (byte)bidi.getLevelAt(bidi.getRunStart(++n11));
                    n10 = bidi.getRunLimit(n11);
                    if ((by & 1) != 0) {
                        n2 |= 8;
                    }
                }
                if (bl5) {
                    if (bl8 || bl9 || bl10) {
                        n3 = arrc[n4];
                        if (Character.isHighSurrogate((char)n3) && n4 + 1 < n13 && Character.isLowSurrogate(arrc[n4 + 1])) {
                            n16 = Character.toCodePoint((char)n3, arrc[++n4]);
                        }
                        bl13 = true;
                        boolean bl16 = bl2 = bl4 || ScriptMapper.isComplexCharCode(n16);
                    }
                    if (bl12) {
                        boolean bl17 = bl2 = bl4 || ScriptMapper.isComplexCharCode(n16);
                        if (bl2) {
                            n6 = n7;
                        } else {
                            bl13 = true;
                        }
                    }
                    if (bl11) {
                        n8 = n9;
                    }
                    if (bl13) {
                        if (bl2) {
                            bl = true;
                            int n20 = 1 << Character.getType(n16);
                            bl3 = (n20 & 0x1C0) != 0;
                            n6 = ScriptMapper.getScript(n16);
                            n8 = 0;
                            if (charToGlyphMapper != null && !Character.isWhitespace(n16)) {
                                n18 = charToGlyphMapper.charToGlyph(n16);
                                n8 = n18 != 0 ? n18 >>> 24 : -1;
                            }
                        } else {
                            bl = false;
                            bl3 = false;
                            n6 = 0;
                            n8 = 0;
                        }
                    }
                }
            }
            if (bl8) continue;
            ++n4;
        }
        if (n15 < n5) {
            prismTextLayout.addTextRun(new TextRun(n15, n5 - n15, by, bl, n6, textSpan, n8, bl3));
            if (bl) {
                n2 |= 0x10;
            }
        } else if (textRun == null || textRun.isLinebreak()) {
            textRun = new TextRun(n15, 0, 0, false, 0, textSpan, 0, false);
            prismTextLayout.addTextRun(textRun);
        }
        if (bidi != null && !bidi.baseIsLeftToRight()) {
            n2 |= 0x100;
        }
        return n2 |= 2;
    }

    @Override
    public void layout(TextRun textRun, PGFont pGFont, FontStrike fontStrike, char[] arrc) {
        int n2 = textRun.getLength();
        T2KFontFile t2KFontFile = null;
        if (fontStrike instanceof CompositeStrike) {
            int n3 = textRun.getSlot();
            if (n3 != -1) {
                CompositeStrike compositeStrike = (CompositeStrike)fontStrike;
                t2KFontFile = (T2KFontFile)compositeStrike.getStrikeSlot(n3).getFontResource();
            }
        } else if (fontStrike.getFontResource() instanceof T2KFontFile) {
            t2KFontFile = (T2KFontFile)fontStrike.getFontResource();
        }
        if (t2KFontFile != null) {
            FontStrike fontStrike2;
            if (this.textPtr == 0L) {
                this.textPtr = ICUGlyphLayout.createTextPtr(arrc);
            }
            BaseTransform baseTransform = fontStrike.getTransform();
            float f2 = fontStrike.getSize();
            this._mat[0] = (float)baseTransform.getMxx() * f2;
            this._mat[1] = (float)baseTransform.getMxy() * f2;
            this._mat[2] = (float)baseTransform.getMyx() * f2;
            this._mat[3] = (float)baseTransform.getMyy() * f2;
            int n4 = -1;
            boolean bl = textRun.isLeftToRight();
            int n5 = bl ? 1 : 2;
            int n6 = pGFont.getFeatures();
            int n7 = t2KFontFile.getFeatures();
            int n8 = n6 & n7;
            int n9 = textRun.getScript();
            if (n8 != 0 && n9 == 0) {
                n9 = 25;
            }
            if (textRun.isCanonical()) {
                n8 |= 0x40000000;
            }
            if ((fontStrike2 = t2KFontFile.getStrike(f2, baseTransform)).getAAMode() == 1) {
                n5 |= 0x10;
            }
            if (textRun.isNoLinkBefore()) {
                n5 |= 4;
            }
            if (textRun.isNoLinkAfter()) {
                n5 |= 8;
            }
            ICUGlyphLayout.nativeLayout(t2KFontFile, fontStrike2, f2, this._mat, textRun.getSlot() << 24, this.textPtr, textRun.getStart(), n2, arrc.length, n9, n4, n5, n8, textRun, t2KFontFile.getUnitsPerEm(), t2KFontFile.getNumHMetrics(), t2KFontFile.getLayoutTableCache());
        } else {
            float f3 = 0.0f;
            float f4 = 0.0f;
            int[] arrn = new int[n2];
            float[] arrf = new float[n2 * 2 + 2];
            Glyph glyph = fontStrike.getGlyph(0);
            float f5 = glyph.getPixelXAdvance();
            float f6 = glyph.getPixelYAdvance();
            for (int i2 = 0; i2 < n2; ++i2) {
                arrf[i2 * 2] = f3;
                arrf[i2 * 2 + 1] = f4;
                f3 += f5;
                f4 += f6;
            }
            arrf[n2 * 2] = f3;
            arrf[n2 * 2 + 1] = f4;
            textRun.shape(n2, arrn, arrf, null);
        }
    }

    @Override
    public void dispose() {
        if (this.textPtr != 0L) {
            ICUGlyphLayout.freeTextPtr(this.textPtr);
            this.textPtr = 0L;
        }
        super.dispose();
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>(){

            @Override
            public Void run() {
                NativeLibLoader.loadLibrary("javafx_font_t2k");
                return null;
            }
        });
        ICUGlyphLayout.initIDs();
    }
}

