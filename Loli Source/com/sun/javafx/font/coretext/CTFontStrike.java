/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.coretext.CGAffineTransform;
import com.sun.javafx.font.coretext.CGRect;
import com.sun.javafx.font.coretext.CTFontFile;
import com.sun.javafx.font.coretext.CTGlyph;
import com.sun.javafx.font.coretext.CTStrikeDisposer;
import com.sun.javafx.font.coretext.OS;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.transform.BaseTransform;

class CTFontStrike
extends PrismFontStrike<CTFontFile> {
    private long fontRef;
    CGAffineTransform matrix;
    static final float SUBPIXEL4_SIZE = 12.0f;
    static final float SUBPIXEL3_SIZE = 18.0f;
    static final float SUBPIXEL2_SIZE = 34.0f;
    private static final boolean SUBPIXEL;

    CTFontStrike(CTFontFile cTFontFile, float f2, BaseTransform baseTransform, int n2, FontStrikeDesc fontStrikeDesc) {
        super(cTFontFile, f2, baseTransform, n2, fontStrikeDesc);
        float f3 = PrismFontFactory.getFontSizeLimit();
        if (baseTransform.isTranslateOrIdentity()) {
            this.drawShapes = f2 > f3;
        } else {
            BaseTransform baseTransform2 = this.getTransform();
            this.matrix = new CGAffineTransform();
            this.matrix.a = baseTransform2.getMxx();
            this.matrix.b = -baseTransform2.getMyx();
            this.matrix.c = -baseTransform2.getMxy();
            this.matrix.d = baseTransform2.getMyy();
            if (Math.abs(this.matrix.a * (double)f2) > (double)f3 || Math.abs(this.matrix.b * (double)f2) > (double)f3 || Math.abs(this.matrix.c * (double)f2) > (double)f3 || Math.abs(this.matrix.d * (double)f2) > (double)f3) {
                this.drawShapes = true;
            }
        }
        long l2 = OS.CFStringCreate(cTFontFile.getPSName());
        if (l2 != 0L) {
            this.fontRef = OS.CTFontCreateWithName(l2, f2, this.matrix);
            OS.CFRelease(l2);
        }
        if (this.fontRef == 0L && PrismFontFactory.debugFonts) {
            System.err.println("Failed to create CTFont for " + this);
        }
    }

    long getFontRef() {
        return this.fontRef;
    }

    @Override
    protected DisposerRecord createDisposer(FontStrikeDesc fontStrikeDesc) {
        CTFontFile cTFontFile = (CTFontFile)this.getFontResource();
        return new CTStrikeDisposer(cTFontFile, fontStrikeDesc, this.fontRef);
    }

    @Override
    protected Glyph createGlyph(int n2) {
        return new CTGlyph(this, n2, this.drawShapes);
    }

    @Override
    public int getQuantizedPosition(Point2D point2D) {
        if (SUBPIXEL && this.matrix == null) {
            if (this.getSize() < 12.0f) {
                float f2 = point2D.x;
                point2D.x = (int)point2D.x;
                f2 -= point2D.x;
                point2D.y = Math.round(point2D.y);
                if (f2 >= 0.75f) {
                    return 3;
                }
                if (f2 >= 0.5f) {
                    return 2;
                }
                if (f2 >= 0.25f) {
                    return 1;
                }
                return 0;
            }
            if (this.getAAMode() == 0) {
                if (this.getSize() < 18.0f) {
                    float f3 = point2D.x;
                    point2D.x = (int)point2D.x;
                    f3 -= point2D.x;
                    point2D.y = Math.round(point2D.y);
                    if (f3 >= 0.66f) {
                        return 2;
                    }
                    if (f3 >= 0.33f) {
                        return 1;
                    }
                    return 0;
                }
                if (this.getSize() < 34.0f) {
                    float f4 = point2D.x;
                    point2D.x = (int)point2D.x;
                    f4 -= point2D.x;
                    point2D.y = Math.round(point2D.y);
                    if (f4 >= 0.5f) {
                        return 1;
                    }
                }
                return 0;
            }
        }
        return super.getQuantizedPosition(point2D);
    }

    float getSubPixelPosition(int n2) {
        if (n2 == 0) {
            return 0.0f;
        }
        float f2 = this.getSize();
        if (f2 < 12.0f) {
            if (n2 == 3) {
                return 0.75f;
            }
            if (n2 == 2) {
                return 0.5f;
            }
            if (n2 == 1) {
                return 0.25f;
            }
            return 0.0f;
        }
        if (this.getAAMode() == 1) {
            return 0.0f;
        }
        if (f2 < 18.0f) {
            if (n2 == 2) {
                return 0.66f;
            }
            if (n2 == 1) {
                return 0.33f;
            }
            return 0.0f;
        }
        if (f2 < 34.0f && n2 == 1) {
            return 0.5f;
        }
        return 0.0f;
    }

    boolean isSubPixelGlyph() {
        return SUBPIXEL && this.matrix == null;
    }

    @Override
    protected Path2D createGlyphOutline(int n2) {
        CTFontFile cTFontFile = (CTFontFile)this.getFontResource();
        return cTFontFile.getGlyphOutline(n2, this.getSize());
    }

    CGRect getBBox(int n2) {
        CTFontFile cTFontFile = (CTFontFile)this.getFontResource();
        return cTFontFile.getBBox(n2, this.getSize());
    }

    static {
        int n2 = PrismFontFactory.getFontFactory().getSubPixelMode();
        SUBPIXEL = (n2 & 1) != 0;
    }
}

