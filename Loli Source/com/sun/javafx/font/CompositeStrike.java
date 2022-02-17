/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.CompositeStrikeDisposer;
import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismMetrics;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;

public class CompositeStrike
implements FontStrike {
    private CompositeFontResource fontResource;
    private float size;
    private int aaMode;
    BaseTransform transform;
    private FontStrike slot0Strike;
    private FontStrike[] strikeSlots;
    private FontStrikeDesc desc;
    DisposerRecord disposer;
    private PrismMetrics metrics;

    @Override
    public void clearDesc() {
        this.fontResource.getStrikeMap().remove(this.desc);
        if (this.slot0Strike != null) {
            this.slot0Strike.clearDesc();
        }
        if (this.strikeSlots != null) {
            for (int i2 = 1; i2 < this.strikeSlots.length; ++i2) {
                if (this.strikeSlots[i2] == null) continue;
                this.strikeSlots[i2].clearDesc();
            }
        }
    }

    CompositeStrike(CompositeFontResource compositeFontResource, float f2, BaseTransform baseTransform, int n2, FontStrikeDesc fontStrikeDesc) {
        this.fontResource = compositeFontResource;
        this.size = f2;
        this.transform = baseTransform.isTranslateOrIdentity() ? BaseTransform.IDENTITY_TRANSFORM : baseTransform.copy();
        this.desc = fontStrikeDesc;
        this.aaMode = n2;
        this.disposer = new CompositeStrikeDisposer(compositeFontResource, fontStrikeDesc);
    }

    @Override
    public int getAAMode() {
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        if (prismFontFactory.isLCDTextSupported()) {
            return this.aaMode;
        }
        return 0;
    }

    @Override
    public BaseTransform getTransform() {
        return this.transform;
    }

    public FontStrike getStrikeSlot(int n2) {
        Object object;
        if (n2 == 0) {
            if (this.slot0Strike == null) {
                FontResource fontResource = this.fontResource.getSlotResource(0);
                this.slot0Strike = fontResource.getStrike(this.size, this.transform, this.getAAMode());
            }
            return this.slot0Strike;
        }
        if (this.strikeSlots == null) {
            this.strikeSlots = new FontStrike[this.fontResource.getNumSlots()];
        }
        if (n2 >= this.strikeSlots.length) {
            object = new FontStrike[this.fontResource.getNumSlots()];
            System.arraycopy(this.strikeSlots, 0, object, 0, this.strikeSlots.length);
            this.strikeSlots = object;
        }
        if (this.strikeSlots[n2] == null) {
            object = this.fontResource.getSlotResource(n2);
            this.strikeSlots[n2] = object.getStrike(this.size, this.transform, this.getAAMode());
        }
        return this.strikeSlots[n2];
    }

    @Override
    public FontResource getFontResource() {
        return this.fontResource;
    }

    public int getStrikeSlotForGlyph(int n2) {
        return n2 >>> 24;
    }

    @Override
    public float getSize() {
        return this.size;
    }

    @Override
    public boolean drawAsShapes() {
        return this.getStrikeSlot(0).drawAsShapes();
    }

    @Override
    public Metrics getMetrics() {
        if (this.metrics == null) {
            PrismFontFile prismFontFile = (PrismFontFile)this.fontResource.getSlotResource(0);
            this.metrics = prismFontFile.getFontMetrics(this.size);
        }
        return this.metrics;
    }

    @Override
    public Glyph getGlyph(char c2) {
        int n2 = this.fontResource.getGlyphMapper().charToGlyph(c2);
        return this.getGlyph(n2);
    }

    @Override
    public Glyph getGlyph(int n2) {
        int n3 = n2 >>> 24;
        int n4 = n2 & 0xFFFFFF;
        return this.getStrikeSlot(n3).getGlyph(n4);
    }

    @Override
    public float getCharAdvance(char c2) {
        int n2 = this.fontResource.getGlyphMapper().charToGlyph((int)c2);
        return this.fontResource.getAdvance(n2, this.size);
    }

    @Override
    public int getQuantizedPosition(Point2D point2D) {
        return this.getStrikeSlot(0).getQuantizedPosition(point2D);
    }

    @Override
    public Shape getOutline(GlyphList glyphList, BaseTransform baseTransform) {
        Path2D path2D = new Path2D();
        this.getOutline(glyphList, baseTransform, path2D);
        return path2D;
    }

    void getOutline(GlyphList glyphList, BaseTransform baseTransform, Path2D path2D) {
        path2D.reset();
        if (glyphList == null) {
            return;
        }
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        Affine2D affine2D = new Affine2D();
        for (int i2 = 0; i2 < glyphList.getGlyphCount(); ++i2) {
            Glyph glyph;
            Shape shape;
            int n2 = glyphList.getGlyphCode(i2);
            if (n2 == 65535 || (shape = (glyph = this.getGlyph(n2)).getShape()) == null) continue;
            affine2D.setTransform(baseTransform);
            affine2D.translate(glyphList.getPosX(i2), glyphList.getPosY(i2));
            path2D.append(shape.getPathIterator(affine2D), false);
        }
    }
}

