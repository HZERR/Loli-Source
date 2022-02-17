/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.freetype;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.freetype.FTFontFile;
import com.sun.javafx.font.freetype.FTGlyph;
import com.sun.javafx.font.freetype.FT_Matrix;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

class FTFontStrike
extends PrismFontStrike<FTFontFile> {
    FT_Matrix matrix;

    protected FTFontStrike(FTFontFile fTFontFile, float f2, BaseTransform baseTransform, int n2, FontStrikeDesc fontStrikeDesc) {
        super(fTFontFile, f2, baseTransform, n2, fontStrikeDesc);
        float f3 = PrismFontFactory.getFontSizeLimit();
        if (baseTransform.isTranslateOrIdentity()) {
            this.drawShapes = f2 > f3;
        } else {
            BaseTransform baseTransform2 = this.getTransform();
            this.matrix = new FT_Matrix();
            this.matrix.xx = (int)(baseTransform2.getMxx() * 65536.0);
            this.matrix.yx = (int)(-baseTransform2.getMyx() * 65536.0);
            this.matrix.xy = (int)(-baseTransform2.getMxy() * 65536.0);
            this.matrix.yy = (int)(baseTransform2.getMyy() * 65536.0);
            if (Math.abs(baseTransform2.getMxx() * (double)f2) > (double)f3 || Math.abs(baseTransform2.getMyx() * (double)f2) > (double)f3 || Math.abs(baseTransform2.getMxy() * (double)f2) > (double)f3 || Math.abs(baseTransform2.getMyy() * (double)f2) > (double)f3) {
                this.drawShapes = true;
            }
        }
    }

    @Override
    protected DisposerRecord createDisposer(FontStrikeDesc fontStrikeDesc) {
        return null;
    }

    @Override
    protected Glyph createGlyph(int n2) {
        return new FTGlyph(this, n2, this.drawShapes);
    }

    @Override
    protected Path2D createGlyphOutline(int n2) {
        FTFontFile fTFontFile = (FTFontFile)this.getFontResource();
        return fTFontFile.createGlyphOutline(n2, this.getSize());
    }

    void initGlyph(FTGlyph fTGlyph) {
        FTFontFile fTFontFile = (FTFontFile)this.getFontResource();
        fTFontFile.initGlyph(fTGlyph, this);
    }
}

