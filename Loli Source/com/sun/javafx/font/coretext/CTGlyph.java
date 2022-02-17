/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.coretext.CGAffineTransform;
import com.sun.javafx.font.coretext.CGRect;
import com.sun.javafx.font.coretext.CGSize;
import com.sun.javafx.font.coretext.CTFontFile;
import com.sun.javafx.font.coretext.CTFontStrike;
import com.sun.javafx.font.coretext.OS;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

class CTGlyph
implements Glyph {
    private CTFontStrike strike;
    private int glyphCode;
    private CGRect bounds;
    private double xAdvance;
    private double yAdvance;
    private boolean drawShapes;
    private static boolean LCD_CONTEXT = true;
    private static boolean CACHE_CONTEXT = true;
    private static long cachedContextRef;
    private static final int BITMAP_WIDTH = 256;
    private static final int BITMAP_HEIGHT = 256;
    private static final int MAX_SIZE = 320;
    private static final long GRAY_COLORSPACE;
    private static final long RGB_COLORSPACE;

    CTGlyph(CTFontStrike cTFontStrike, int n2, boolean bl) {
        this.strike = cTFontStrike;
        this.glyphCode = n2;
        this.drawShapes = bl;
    }

    @Override
    public int getGlyphCode() {
        return this.glyphCode;
    }

    @Override
    public RectBounds getBBox() {
        CGRect cGRect = this.strike.getBBox(this.glyphCode);
        if (cGRect == null) {
            return new RectBounds();
        }
        return new RectBounds((float)cGRect.origin.x, (float)cGRect.origin.y, (float)(cGRect.origin.x + cGRect.size.width), (float)(cGRect.origin.y + cGRect.size.height));
    }

    private void checkBounds() {
        if (this.bounds != null) {
            return;
        }
        this.bounds = new CGRect();
        if (this.strike.getSize() == 0.0f) {
            return;
        }
        long l2 = this.strike.getFontRef();
        if (l2 == 0L) {
            return;
        }
        int n2 = 0;
        CGSize cGSize = new CGSize();
        OS.CTFontGetAdvancesForGlyphs(l2, n2, (short)this.glyphCode, cGSize);
        this.xAdvance = cGSize.width;
        this.yAdvance = -cGSize.height;
        if (this.drawShapes) {
            return;
        }
        CTFontFile cTFontFile = (CTFontFile)this.strike.getFontResource();
        float[] arrf = new float[4];
        cTFontFile.getGlyphBoundingBox((short)this.glyphCode, this.strike.getSize(), arrf);
        this.bounds.origin.x = arrf[0];
        this.bounds.origin.y = arrf[1];
        this.bounds.size.width = arrf[2] - arrf[0];
        this.bounds.size.height = arrf[3] - arrf[1];
        if (this.strike.matrix != null) {
            OS.CGRectApplyAffineTransform(this.bounds, this.strike.matrix);
        }
        if (this.bounds.size.width < 0.0 || this.bounds.size.height < 0.0 || this.bounds.size.width > 320.0 || this.bounds.size.height > 320.0) {
            this.bounds.size.height = 0.0;
            this.bounds.size.width = 0.0;
            this.bounds.origin.y = 0.0;
            this.bounds.origin.x = 0.0;
        } else {
            this.bounds.origin.x = (int)Math.floor(this.bounds.origin.x) - 1;
            this.bounds.origin.y = (int)Math.floor(this.bounds.origin.y) - 1;
            this.bounds.size.width = (int)Math.ceil(this.bounds.size.width) + 1 + 1 + 1;
            this.bounds.size.height = (int)Math.ceil(this.bounds.size.height) + 1 + 1 + 1;
        }
    }

    @Override
    public Shape getShape() {
        return this.strike.createGlyphOutline(this.glyphCode);
    }

    private long createContext(boolean bl, int n2, int n3) {
        int n4;
        int n5;
        long l2;
        int n6 = 8;
        if (bl) {
            l2 = RGB_COLORSPACE;
            n5 = n2 * 4;
            n4 = OS.kCGBitmapByteOrder32Host | 2;
        } else {
            l2 = GRAY_COLORSPACE;
            n5 = n2;
            n4 = 0;
        }
        long l3 = OS.CGBitmapContextCreate(0L, n2, n3, n6, n5, l2, n4);
        boolean bl2 = this.strike.isSubPixelGlyph();
        OS.CGContextSetAllowsFontSmoothing(l3, bl);
        OS.CGContextSetAllowsAntialiasing(l3, true);
        OS.CGContextSetAllowsFontSubpixelPositioning(l3, bl2);
        OS.CGContextSetAllowsFontSubpixelQuantization(l3, bl2);
        return l3;
    }

    private long getCachedContext(boolean bl) {
        if (cachedContextRef == 0L) {
            cachedContextRef = this.createContext(bl, 256, 256);
        }
        return cachedContextRef;
    }

    private synchronized byte[] getImage(double d2, double d3, int n2, int n3, int n4) {
        byte[] arrby;
        long l2;
        if (n2 == 0 || n3 == 0) {
            return new byte[0];
        }
        long l3 = this.strike.getFontRef();
        boolean bl = this.isLCDGlyph();
        boolean bl2 = LCD_CONTEXT || bl;
        CGAffineTransform cGAffineTransform = this.strike.matrix;
        boolean bl3 = CACHE_CONTEXT & 256 >= n2 & 256 >= n3;
        long l4 = l2 = bl3 ? this.getCachedContext(bl2) : this.createContext(bl2, n2, n3);
        if (l2 == 0L) {
            return new byte[0];
        }
        OS.CGContextSetRGBFillColor(l2, 1.0, 1.0, 1.0, 1.0);
        CGRect cGRect = new CGRect();
        cGRect.size.width = n2;
        cGRect.size.height = n3;
        OS.CGContextFillRect(l2, cGRect);
        double d4 = 0.0;
        double d5 = 0.0;
        if (cGAffineTransform != null) {
            OS.CGContextTranslateCTM(l2, -d2, -d3);
        } else {
            d4 = d2 - (double)this.strike.getSubPixelPosition(n4);
            d5 = d3;
        }
        OS.CGContextSetRGBFillColor(l2, 0.0, 0.0, 0.0, 1.0);
        OS.CTFontDrawGlyphs(l3, (short)this.glyphCode, -d4, -d5, l2);
        if (cGAffineTransform != null) {
            OS.CGContextTranslateCTM(l2, d2, d3);
        }
        if ((arrby = bl ? OS.CGBitmapContextGetData(l2, n2, n3, 24) : OS.CGBitmapContextGetData(l2, n2, n3, 8)) == null) {
            this.bounds = new CGRect();
            arrby = new byte[]{};
        }
        if (!bl3) {
            OS.CGContextRelease(l2);
        }
        return arrby;
    }

    @Override
    public byte[] getPixelData() {
        return this.getPixelData(0);
    }

    @Override
    public byte[] getPixelData(int n2) {
        this.checkBounds();
        return this.getImage(this.bounds.origin.x, this.bounds.origin.y, (int)this.bounds.size.width, (int)this.bounds.size.height, n2);
    }

    @Override
    public float getAdvance() {
        this.checkBounds();
        return (float)this.xAdvance;
    }

    @Override
    public float getPixelXAdvance() {
        this.checkBounds();
        return (float)this.xAdvance;
    }

    @Override
    public float getPixelYAdvance() {
        this.checkBounds();
        return (float)this.yAdvance;
    }

    @Override
    public int getWidth() {
        this.checkBounds();
        int n2 = (int)this.bounds.size.width;
        return this.isLCDGlyph() ? n2 * 3 : n2;
    }

    @Override
    public int getHeight() {
        this.checkBounds();
        return (int)this.bounds.size.height;
    }

    @Override
    public int getOriginX() {
        this.checkBounds();
        return (int)this.bounds.origin.x;
    }

    @Override
    public int getOriginY() {
        this.checkBounds();
        int n2 = (int)this.bounds.size.height;
        int n3 = (int)this.bounds.origin.y;
        return -n2 - n3;
    }

    @Override
    public boolean isLCDGlyph() {
        return this.strike.getAAMode() == 1;
    }

    static {
        GRAY_COLORSPACE = OS.CGColorSpaceCreateDeviceGray();
        RGB_COLORSPACE = OS.CGColorSpaceCreateDeviceRGB();
    }
}

