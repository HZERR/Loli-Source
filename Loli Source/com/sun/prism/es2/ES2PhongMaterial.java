/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.javafx.PlatformUtil;
import com.sun.prism.Image;
import com.sun.prism.PhongMaterial;
import com.sun.prism.Texture;
import com.sun.prism.TextureMap;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;
import com.sun.prism.paint.Color;
import sun.util.logging.PlatformLogger;

class ES2PhongMaterial
extends BaseGraphicsResource
implements PhongMaterial {
    static int count = 0;
    private final ES2Context context;
    private final long nativeHandle;
    TextureMap[] maps = new TextureMap[MAX_MAP_TYPE];
    Color diffuseColor = Color.WHITE;
    Color specularColor = Color.WHITE;
    boolean specularColorSet = false;

    private ES2PhongMaterial(ES2Context eS2Context, long l2, Disposer.Record record) {
        super(record);
        this.context = eS2Context;
        this.nativeHandle = l2;
        ++count;
    }

    static ES2PhongMaterial create(ES2Context eS2Context) {
        long l2 = eS2Context.createES2PhongMaterial();
        return new ES2PhongMaterial(eS2Context, l2, new ES2PhongMaterialDisposerRecord(eS2Context, l2));
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override
    public void setDiffuseColor(float f2, float f3, float f4, float f5) {
        this.diffuseColor = new Color(f2, f3, f4, f5);
    }

    @Override
    public void setSpecularColor(boolean bl, float f2, float f3, float f4, float f5) {
        this.specularColorSet = bl;
        this.specularColor = new Color(f2, f3, f4, f5);
    }

    @Override
    public void setTextureMap(TextureMap textureMap) {
        this.maps[textureMap.getType().ordinal()] = textureMap;
    }

    private Texture setupTexture(TextureMap textureMap, boolean bl) {
        Image image = textureMap.getImage();
        Texture texture = image == null ? null : this.context.getResourceFactory().getCachedTexture(image, Texture.WrapMode.REPEAT, bl);
        return texture;
    }

    @Override
    public void lockTextureMaps() {
        for (int i2 = 0; i2 < MAX_MAP_TYPE; ++i2) {
            Texture texture = this.maps[i2].getTexture();
            if (!this.maps[i2].isDirty() && texture != null) {
                texture.lock();
                if (!texture.isSurfaceLost()) continue;
            }
            boolean bl = !PlatformUtil.isEmbedded() && (i2 == PhongMaterial.DIFFUSE || i2 == PhongMaterial.SELF_ILLUM);
            texture = this.setupTexture(this.maps[i2], bl);
            this.maps[i2].setTexture(texture);
            this.maps[i2].setDirty(false);
            if (this.maps[i2].getImage() == null || texture != null) continue;
            String string = PhongMaterial.class.getName();
            PlatformLogger.getLogger(string).warning("Warning: Low on texture resources. Cannot create texture.");
        }
    }

    @Override
    public void unlockTextureMaps() {
        for (int i2 = 0; i2 < MAX_MAP_TYPE; ++i2) {
            Texture texture = this.maps[i2].getTexture();
            if (texture == null) continue;
            texture.unlock();
        }
    }

    @Override
    public void dispose() {
        this.disposerRecord.dispose();
        --count;
    }

    public int getCount() {
        return count;
    }

    static class ES2PhongMaterialDisposerRecord
    implements Disposer.Record {
        private final ES2Context context;
        private long nativeHandle;

        ES2PhongMaterialDisposerRecord(ES2Context eS2Context, long l2) {
            this.context = eS2Context;
            this.nativeHandle = l2;
        }

        void traceDispose() {
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                this.traceDispose();
                this.context.releaseES2PhongMaterial(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

