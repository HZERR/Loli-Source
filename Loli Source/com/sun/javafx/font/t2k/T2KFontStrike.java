/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.t2k;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.t2k.T2KFontFile;
import com.sun.javafx.font.t2k.T2KGlyph;
import com.sun.javafx.font.t2k.T2KStrikeDisposer;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import java.util.concurrent.ConcurrentHashMap;

class T2KFontStrike
extends PrismFontStrike<T2KFontFile> {
    private long pScalerContext = 0L;
    ConcurrentHashMap<Integer, Point2D> glyphPointMap;
    Affine2D invTx = null;
    boolean gdiLCDGlyphs = false;
    int gdiSize;

    T2KFontStrike(T2KFontFile t2KFontFile, float f2, BaseTransform baseTransform, int n2, FontStrikeDesc fontStrikeDesc) {
        super(t2KFontFile, f2, baseTransform, n2, fontStrikeDesc);
        int n3 = 0;
        boolean bl = false;
        float f3 = 1.0f;
        float f4 = 0.0f;
        double[] arrd = new double[4];
        if (baseTransform.isTranslateOrIdentity()) {
            arrd[0] = arrd[3] = (double)f2;
        } else {
            BaseTransform baseTransform2 = this.getTransform();
            this.invTx = new Affine2D(baseTransform2);
            try {
                this.invTx.invert();
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                this.invTx = null;
            }
            arrd[0] = baseTransform2.getMxx() * (double)f2;
            arrd[1] = baseTransform2.getMyx() * (double)f2;
            arrd[2] = baseTransform2.getMxy() * (double)f2;
            arrd[3] = baseTransform2.getMyy() * (double)f2;
        }
        float f5 = PrismFontFactory.getFontSizeLimit();
        if (Math.abs(arrd[0]) > (double)f5 || Math.abs(arrd[1]) > (double)f5 || Math.abs(arrd[2]) > (double)f5 || Math.abs(arrd[3]) > (double)f5) {
            this.drawShapes = true;
        } else if (PrismFontFactory.isWindows && this.getAAMode() == 1 && t2KFontFile.isInstalledFont() && arrd[0] > 0.0 && arrd[0] == arrd[3] && arrd[1] == 0.0 && arrd[2] == 0.0) {
            this.gdiLCDGlyphs = true;
            this.gdiSize = (int)(arrd[0] + 0.5);
        }
        int n4 = 2;
        if (this.getAAMode() == 1) {
            n4 = 4;
            bl = true;
        }
        this.pScalerContext = t2KFontFile.createScalerContext(arrd, n4, n3, bl, f3, f4);
    }

    @Override
    protected DisposerRecord createDisposer(FontStrikeDesc fontStrikeDesc) {
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        return new T2KStrikeDisposer(t2KFontFile, fontStrikeDesc, this.pScalerContext);
    }

    long getScalerContext() {
        return this.pScalerContext;
    }

    private native long getLCDGlyphFromWindows(String var1, boolean var2, boolean var3, int var4, int var5, boolean var6);

    private long getGlyphFromWindows(int n2) {
        FontResource fontResource = this.getFontResource();
        String string = fontResource.getFamilyName();
        boolean bl = fontResource.isBold();
        boolean bl2 = fontResource.isItalic();
        return this.getLCDGlyphFromWindows(string, bl, bl2, this.gdiSize, n2, true);
    }

    public Point2D getGlyphMetrics(int n2) {
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        float[] arrf = t2KFontFile.getGlyphMetrics(this.pScalerContext, n2);
        return new Point2D(arrf[0], arrf[1]);
    }

    @Override
    protected Glyph createGlyph(int n2) {
        T2KGlyph t2KGlyph;
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        if (this.drawAsShapes()) {
            float f2 = t2KFontFile.getAdvance(n2, this.getSize());
            t2KGlyph = new T2KGlyph(this, n2, f2);
        } else {
            long l2 = 0L;
            long l3 = 0L;
            if (this.gdiLCDGlyphs) {
                l2 = l3 = this.getGlyphFromWindows(n2);
            }
            if (l2 == 0L) {
                l2 = t2KFontFile.getGlyphImage(this.pScalerContext, n2);
            }
            t2KGlyph = new T2KGlyph(this, n2, l2);
            if (l3 != 0L) {
                float f3;
                float f4 = f3 = t2KFontFile.getAdvance(n2, this.getSize());
                float f5 = 0.0f;
                if (this.invTx != null) {
                    Point2D point2D = new Point2D(f3, 0.0f);
                    this.getTransform().transform(point2D, point2D);
                    f4 = point2D.x;
                    f5 = point2D.y;
                }
                t2KGlyph.setAdvance(f3, f4, f5);
            }
        }
        return t2KGlyph;
    }

    @Override
    protected Path2D createGlyphOutline(int n2) {
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        Path2D path2D = t2KFontFile.getGlyphOutline(this.pScalerContext, n2);
        if (this.invTx != null) {
            path2D.transform(this.invTx);
        }
        return path2D;
    }

    RectBounds getGlyphBounds(int n2) {
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        return t2KFontFile.getGlyphBounds(this.pScalerContext, n2);
    }

    float getGlyphUserAdvance(float f2, float f3) {
        if (this.invTx != null) {
            Point2D point2D = new Point2D(f2, f3);
            this.invTx.transform(point2D, point2D);
            return point2D.x;
        }
        return f2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    Point2D getGlyphPoint(int n2, int n3) {
        Object object;
        Point2D point2D = null;
        Integer n4 = n2 << 16 | n3;
        if (this.glyphPointMap == null) {
            object = this;
            synchronized (object) {
                if (this.glyphPointMap == null) {
                    this.glyphPointMap = new ConcurrentHashMap();
                }
            }
        } else {
            point2D = this.glyphPointMap.get(n4);
        }
        if (point2D == null && (point2D = ((T2KFontFile)(object = (T2KFontFile)this.getFontResource())).getGlyphPoint(this.pScalerContext, n2, n3)) != null) {
            this.adjustPoint(point2D);
            this.glyphPointMap.put(n4, point2D);
        }
        return point2D;
    }

    protected void adjustPoint(Point2D point2D) {
        if (this.invTx != null) {
            this.invTx.deltaTransform(point2D, point2D);
        }
    }
}

