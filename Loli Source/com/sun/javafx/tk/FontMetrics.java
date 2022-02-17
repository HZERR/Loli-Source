/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk;

import com.sun.javafx.tk.Toolkit;
import javafx.scene.text.Font;

public class FontMetrics {
    private float maxAscent;
    private float ascent;
    private float xheight;
    private int baseline;
    private float descent;
    private float maxDescent;
    private float leading;
    private float lineHeight;
    private Font font;

    public static FontMetrics impl_createFontMetrics(float f2, float f3, float f4, float f5, float f6, float f7, Font font) {
        return new FontMetrics(f2, f3, f4, f5, f6, f7, font);
    }

    public final float getMaxAscent() {
        return this.maxAscent;
    }

    public final float getAscent() {
        return this.ascent;
    }

    public final float getXheight() {
        return this.xheight;
    }

    public final int getBaseline() {
        return this.baseline;
    }

    public final float getDescent() {
        return this.descent;
    }

    public final float getMaxDescent() {
        return this.maxDescent;
    }

    public final float getLeading() {
        return this.leading;
    }

    public final float getLineHeight() {
        return this.lineHeight;
    }

    public final Font getFont() {
        if (this.font == null) {
            this.font = Font.getDefault();
        }
        return this.font;
    }

    public FontMetrics(float f2, float f3, float f4, float f5, float f6, float f7, Font font) {
        this.maxAscent = f2;
        this.ascent = f3;
        this.xheight = f4;
        this.descent = f5;
        this.maxDescent = f6;
        this.leading = f7;
        this.font = font;
        this.lineHeight = f2 + f6 + f7;
    }

    public float computeStringWidth(String string) {
        return Toolkit.getToolkit().getFontLoader().computeStringWidth(string, this.getFont());
    }

    public String toString() {
        return "FontMetrics: [maxAscent=" + this.getMaxAscent() + ", ascent=" + this.getAscent() + ", xheight=" + this.getXheight() + ", baseline=" + this.getBaseline() + ", descent=" + this.getDescent() + ", maxDescent=" + this.getMaxDescent() + ", leading=" + this.getLeading() + ", lineHeight=" + this.getLineHeight() + ", font=" + this.getFont() + "]";
    }
}

