/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.GraphicsResource;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundImage;

public class CompoundTexture
extends CompoundImage
implements GraphicsResource {
    protected Texture[] texTiles;

    public CompoundTexture(Image image, int n2) {
        super(image, n2);
        this.texTiles = new Texture[this.tiles.length];
    }

    @Override
    public Texture getTile(int n2, int n3, ResourceFactory resourceFactory) {
        int n4 = n2 + n3 * this.uSections;
        Texture texture = this.texTiles[n4];
        if (texture != null) {
            texture.lock();
            if (texture.isSurfaceLost()) {
                texture = null;
                this.texTiles[n4] = null;
            }
        }
        if (texture == null) {
            this.texTiles[n4] = texture = resourceFactory.createTexture(this.tiles[n4], Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
        }
        return texture;
    }

    @Override
    public void dispose() {
        for (int i2 = 0; i2 != this.texTiles.length; ++i2) {
            if (this.texTiles[i2] == null) continue;
            this.texTiles[i2].dispose();
            this.texTiles[i2] = null;
        }
    }
}

