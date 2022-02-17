/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.text.TextRun;

public class TextLine
implements com.sun.javafx.scene.text.TextLine {
    TextRun[] runs;
    RectBounds bounds;
    float lsb;
    float rsb;
    float leading;
    int start;
    int length;

    public TextLine(int n2, int n3, TextRun[] arrtextRun, float f2, float f3, float f4, float f5) {
        this.start = n2;
        this.length = n3;
        this.bounds = new RectBounds(0.0f, f3, f2, f4 + f5);
        this.leading = f5;
        this.runs = arrtextRun;
    }

    @Override
    public RectBounds getBounds() {
        return this.bounds;
    }

    public float getLeading() {
        return this.leading;
    }

    public TextRun[] getRuns() {
        return this.runs;
    }

    @Override
    public int getStart() {
        return this.start;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    public void setSideBearings(float f2, float f3) {
        this.lsb = f2;
        this.rsb = f3;
    }

    @Override
    public float getLeftSideBearing() {
        return this.lsb;
    }

    @Override
    public float getRightSideBearing() {
        return this.rsb;
    }

    public void setAlignment(float f2) {
        this.bounds.setMinX(f2);
        this.bounds.setMaxX(f2 + this.bounds.getMaxX());
    }

    public void setWidth(float f2) {
        this.bounds.setMaxX(this.bounds.getMinX() + f2);
    }
}

