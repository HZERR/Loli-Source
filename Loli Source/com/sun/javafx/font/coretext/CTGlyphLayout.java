/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.coretext.CTFontStrike;
import com.sun.javafx.font.coretext.OS;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.TextRun;

class CTGlyphLayout
extends GlyphLayout {
    CTGlyphLayout() {
    }

    private long createCTLine(long l2, char[] arrc, boolean bl, int n2, int n3) {
        long l3 = OS.kCFAllocatorDefault();
        long l4 = OS.CFStringCreateWithCharacters(l3, arrc, n2, n3);
        long l5 = 0L;
        if (l4 != 0L) {
            long l6 = OS.CFDictionaryCreateMutable(l3, 4L, OS.kCFTypeDictionaryKeyCallBacks(), OS.kCFTypeDictionaryValueCallBacks());
            if (l6 != 0L) {
                long l7;
                OS.CFDictionaryAddValue(l6, OS.kCTFontAttributeName(), l2);
                if (bl && (l7 = OS.CTParagraphStyleCreate(1)) != 0L) {
                    OS.CFDictionaryAddValue(l6, OS.kCTParagraphStyleAttributeName(), l7);
                    OS.CFRelease(l7);
                }
                if ((l7 = OS.CFAttributedStringCreate(l3, l4, l6)) != 0L) {
                    l5 = OS.CTLineCreateWithAttributedString(l7);
                    OS.CFRelease(l7);
                }
                OS.CFRelease(l6);
            }
            OS.CFRelease(l4);
        }
        return l5;
    }

    private int getFontSlot(long l2, CompositeFontResource compositeFontResource, String string, int n2) {
        long l3 = OS.CTRunGetAttributes(l2);
        if (l3 == 0L) {
            return -1;
        }
        long l4 = OS.CFDictionaryGetValue(l3, OS.kCTFontAttributeName());
        if (l4 == 0L) {
            return -1;
        }
        String string2 = OS.CTFontCopyAttributeDisplayName(l4);
        if (string2 == null) {
            return -1;
        }
        if (!string2.equalsIgnoreCase(string)) {
            if (compositeFontResource == null) {
                return -1;
            }
            n2 = compositeFontResource.getSlotForFont(string2);
            if (PrismFontFactory.debugFonts) {
                System.err.println("\tFallback font= " + string2 + " slot=" + n2);
            }
        }
        return n2;
    }

    @Override
    public void layout(TextRun textRun, PGFont pGFont, FontStrike fontStrike, char[] arrc) {
        int n2 = 0;
        CompositeFontResource compositeFontResource = null;
        if (fontStrike instanceof CompositeStrike) {
            compositeFontResource = (CompositeFontResource)fontStrike.getFontResource();
            n2 = this.getInitialSlot(compositeFontResource);
            fontStrike = ((CompositeStrike)fontStrike).getStrikeSlot(n2);
        }
        float f2 = fontStrike.getSize();
        String string = fontStrike.getFontResource().getFullName();
        long l2 = ((CTFontStrike)fontStrike).getFontRef();
        if (l2 == 0L) {
            return;
        }
        boolean bl = (textRun.getLevel() & 1) != 0;
        long l3 = this.createCTLine(l2, arrc, bl, textRun.getStart(), textRun.getLength());
        if (l3 == 0L) {
            return;
        }
        long l4 = OS.CTLineGetGlyphRuns(l3);
        if (l4 != 0L) {
            int n3 = (int)OS.CTLineGetGlyphCount(l3);
            int[] arrn = new int[n3];
            float[] arrf = new float[n3 * 2 + 2];
            int[] arrn2 = new int[n3];
            long l5 = OS.CFArrayGetCount(l4);
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            while ((long)n7 < l5) {
                long l6 = OS.CFArrayGetValueAtIndex(l4, n7);
                if (l6 != 0L) {
                    int n8 = this.getFontSlot(l6, compositeFontResource, string, n2);
                    n4 = n8 != -1 ? (n4 += OS.CTRunGetGlyphs(l6, n8 << 24, n4, arrn)) : (int)((long)n4 + OS.CTRunGetGlyphCount(l6));
                    if (f2 > 0.0f) {
                        n5 += OS.CTRunGetPositions(l6, n5, arrf);
                    }
                    n6 += OS.CTRunGetStringIndices(l6, n6, arrn2);
                }
                ++n7;
            }
            if (f2 > 0.0f) {
                arrf[n5] = (float)OS.CTLineGetTypographicBounds(l3);
            }
            textRun.shape(n3, arrn, arrf, arrn2);
        }
        OS.CFRelease(l3);
    }
}

