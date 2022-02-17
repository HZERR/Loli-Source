/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.coretext.CGAffineTransform;
import com.sun.javafx.font.coretext.CGRect;
import com.sun.javafx.font.coretext.CTFontStrike;
import com.sun.javafx.font.coretext.OS;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

class CTFontFile
extends PrismFontFile {
    private static final CGAffineTransform tx = new CGAffineTransform();

    CTFontFile(String string, String string2, int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        super(string, string2, n2, bl, bl2, bl3, bl4);
    }

    public static boolean registerFont(String string) {
        if (string == null) {
            return false;
        }
        long l2 = OS.kCFAllocatorDefault();
        boolean bl = false;
        long l3 = OS.CFStringCreate(string);
        if (l3 != 0L) {
            int n2 = 0;
            long l4 = OS.CFURLCreateWithFileSystemPath(l2, l3, n2, false);
            if (l4 != 0L) {
                int n3 = 1;
                bl = OS.CTFontManagerRegisterFontsForURL(l4, n3, 0L);
                OS.CFRelease(l4);
            }
            OS.CFRelease(l3);
        }
        return bl;
    }

    CGRect getBBox(int n2, float f2) {
        CTFontStrike cTFontStrike = (CTFontStrike)this.getStrike(f2, BaseTransform.IDENTITY_TRANSFORM);
        long l2 = cTFontStrike.getFontRef();
        if (l2 == 0L) {
            return null;
        }
        long l3 = OS.CTFontCreatePathForGlyph(l2, (short)n2, tx);
        if (l3 == 0L) {
            return null;
        }
        CGRect cGRect = OS.CGPathGetPathBoundingBox(l3);
        OS.CGPathRelease(l3);
        return cGRect;
    }

    Path2D getGlyphOutline(int n2, float f2) {
        CTFontStrike cTFontStrike = (CTFontStrike)this.getStrike(f2, BaseTransform.IDENTITY_TRANSFORM);
        long l2 = cTFontStrike.getFontRef();
        if (l2 == 0L) {
            return null;
        }
        long l3 = OS.CTFontCreatePathForGlyph(l2, (short)n2, tx);
        if (l3 == 0L) {
            return null;
        }
        Path2D path2D = OS.CGPathApply(l3);
        OS.CGPathRelease(l3);
        return path2D;
    }

    @Override
    protected int[] createGlyphBoundingBox(int n2) {
        short s2;
        float f2 = 12.0f;
        CTFontStrike cTFontStrike = (CTFontStrike)this.getStrike(f2, BaseTransform.IDENTITY_TRANSFORM);
        long l2 = cTFontStrike.getFontRef();
        if (l2 == 0L) {
            return null;
        }
        int[] arrn = new int[4];
        if (!this.isCFF() && OS.CTFontGetBoundingRectForGlyphUsingTables(l2, (short)n2, s2 = this.getIndexToLocFormat(), arrn)) {
            return arrn;
        }
        long l3 = OS.CTFontCreatePathForGlyph(l2, (short)n2, null);
        if (l3 == 0L) {
            return null;
        }
        CGRect cGRect = OS.CGPathGetPathBoundingBox(l3);
        OS.CGPathRelease(l3);
        float f3 = (float)this.getUnitsPerEm() / f2;
        arrn[0] = (int)Math.round(cGRect.origin.x * (double)f3);
        arrn[1] = (int)Math.round(cGRect.origin.y * (double)f3);
        arrn[2] = (int)Math.round((cGRect.origin.x + cGRect.size.width) * (double)f3);
        arrn[3] = (int)Math.round((cGRect.origin.y + cGRect.size.height) * (double)f3);
        return arrn;
    }

    @Override
    protected PrismFontStrike<CTFontFile> createStrike(float f2, BaseTransform baseTransform, int n2, FontStrikeDesc fontStrikeDesc) {
        return new CTFontStrike(this, f2, baseTransform, n2, fontStrikeDesc);
    }

    static {
        CTFontFile.tx.a = 1.0;
        CTFontFile.tx.d = -1.0;
    }
}

