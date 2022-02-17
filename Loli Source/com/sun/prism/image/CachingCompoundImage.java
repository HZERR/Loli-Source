/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundImage;

public class CachingCompoundImage
extends CompoundImage {
    public CachingCompoundImage(Image image, int n2) {
        super(image, n2);
    }

    @Override
    public Texture getTile(int n2, int n3, ResourceFactory resourceFactory) {
        return resourceFactory.getCachedTexture(this.tiles[n2 + n3 * this.uSections], Texture.WrapMode.CLAMP_TO_EDGE);
    }
}

