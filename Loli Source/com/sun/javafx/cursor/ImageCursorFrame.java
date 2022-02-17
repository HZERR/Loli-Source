/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.cursor;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.CursorType;

public final class ImageCursorFrame
extends CursorFrame {
    private final Object platformImage;
    private final double width;
    private final double height;
    private final double hotspotX;
    private final double hotspotY;

    public ImageCursorFrame(Object object, double d2, double d3, double d4, double d5) {
        this.platformImage = object;
        this.width = d2;
        this.height = d3;
        this.hotspotX = d4;
        this.hotspotY = d5;
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.IMAGE;
    }

    public Object getPlatformImage() {
        return this.platformImage;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getHotspotX() {
        return this.hotspotX;
    }

    public double getHotspotY() {
        return this.hotspotY;
    }
}

