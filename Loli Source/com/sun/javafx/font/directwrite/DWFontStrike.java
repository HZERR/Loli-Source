/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.directwrite.DWFontFile;
import com.sun.javafx.font.directwrite.DWGlyph;
import com.sun.javafx.font.directwrite.DWRITE_MATRIX;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

class DWFontStrike
extends PrismFontStrike<DWFontFile> {
    DWRITE_MATRIX matrix;
    static final boolean SUBPIXEL_ON;
    static final boolean SUBPIXEL_Y;
    static final boolean SUBPIXEL_NATIVE;

    DWFontStrike(DWFontFile dWFontFile, float f2, BaseTransform baseTransform, int n2, FontStrikeDesc fontStrikeDesc) {
        super(dWFontFile, f2, baseTransform, n2, fontStrikeDesc);
        float f3 = PrismFontFactory.getFontSizeLimit();
        if (baseTransform.isTranslateOrIdentity()) {
            this.drawShapes = f2 > f3;
        } else {
            BaseTransform baseTransform2 = this.getTransform();
            this.matrix = new DWRITE_MATRIX();
            this.matrix.m11 = (float)baseTransform2.getMxx();
            this.matrix.m12 = (float)baseTransform2.getMyx();
            this.matrix.m21 = (float)baseTransform2.getMxy();
            this.matrix.m22 = (float)baseTransform2.getMyy();
            if (Math.abs(this.matrix.m11 * f2) > f3 || Math.abs(this.matrix.m12 * f2) > f3 || Math.abs(this.matrix.m21 * f2) > f3 || Math.abs(this.matrix.m22 * f2) > f3) {
                this.drawShapes = true;
            }
        }
    }

    @Override
    protected DisposerRecord createDisposer(FontStrikeDesc fontStrikeDesc) {
        return null;
    }

    @Override
    public int getQuantizedPosition(Point2D point2D) {
        if (SUBPIXEL_ON && (this.matrix == null || SUBPIXEL_NATIVE) && (this.getAAMode() == 0 || SUBPIXEL_NATIVE)) {
            float f2 = point2D.x;
            point2D.x = (int)point2D.x;
            f2 -= point2D.x;
            int n2 = 0;
            if (f2 >= 0.66f) {
                n2 = 2;
            } else if (f2 >= 0.33f) {
                n2 = 1;
            }
            if (SUBPIXEL_Y) {
                f2 = point2D.y;
                point2D.y = (int)point2D.y;
                if ((f2 -= point2D.y) >= 0.66f) {
                    n2 += 6;
                } else if (f2 >= 0.33f) {
                    n2 += 3;
                }
            } else {
                point2D.y = Math.round(point2D.y);
            }
            return n2;
        }
        return super.getQuantizedPosition(point2D);
    }

    IDWriteFontFace getFontFace() {
        DWFontFile dWFontFile = (DWFontFile)this.getFontResource();
        return dWFontFile.getFontFace();
    }

    RectBounds getBBox(int n2) {
        DWFontFile dWFontFile = (DWFontFile)this.getFontResource();
        return dWFontFile.getBBox(n2, this.getSize());
    }

    int getUpem() {
        return ((DWFontFile)this.getFontResource()).getUnitsPerEm();
    }

    @Override
    protected Path2D createGlyphOutline(int n2) {
        DWFontFile dWFontFile = (DWFontFile)this.getFontResource();
        return dWFontFile.getGlyphOutline(n2, this.getSize());
    }

    @Override
    protected Glyph createGlyph(int n2) {
        return new DWGlyph(this, n2, this.drawShapes);
    }

    static {
        int n2 = PrismFontFactory.getFontFactory().getSubPixelMode();
        SUBPIXEL_ON = (n2 & 1) != 0;
        SUBPIXEL_Y = (n2 & 2) != 0;
        SUBPIXEL_NATIVE = (n2 & 4) != 0;
    }
}

