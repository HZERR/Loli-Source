/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

import com.sun.prism.Image;
import com.sun.prism.paint.Paint;

public final class ImagePattern
extends Paint {
    private final Image image;
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public ImagePattern(Image image, float f2, float f3, float f4, float f5, boolean bl, boolean bl2) {
        super(Paint.Type.IMAGE_PATTERN, bl, bl2);
        if (image == null) {
            throw new IllegalArgumentException("Image must be non-null");
        }
        this.image = image;
        this.x = f2;
        this.y = f3;
        this.width = f4;
        this.height = f5;
    }

    public Image getImage() {
        return this.image;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    @Override
    public boolean isOpaque() {
        return this.image.isOpaque();
    }
}

