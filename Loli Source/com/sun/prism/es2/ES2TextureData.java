/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.Texture;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.GLContext;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.PrismTrace;

class ES2TextureData
implements Disposer.Record {
    protected final ES2Context context;
    private int texID;
    private long size;
    private boolean lastAssociatedFilterMode = true;
    private Texture.WrapMode lastAssociatedWrapMode = Texture.WrapMode.REPEAT;

    protected ES2TextureData(ES2Context eS2Context, int n2, long l2) {
        this.context = eS2Context;
        this.texID = n2;
        this.size = l2;
    }

    ES2TextureData(ES2Context eS2Context, int n2, int n3, int n4, long l2) {
        this.context = eS2Context;
        this.texID = n2;
        this.size = l2;
        PrismTrace.textureCreated((long)n2, n3, n4, l2);
    }

    public int getTexID() {
        return this.texID;
    }

    public long getSize() {
        return this.size;
    }

    public boolean isFiltered() {
        return this.lastAssociatedFilterMode;
    }

    public void setFiltered(boolean bl) {
        this.lastAssociatedFilterMode = bl;
    }

    public Texture.WrapMode getWrapMode() {
        return this.lastAssociatedWrapMode;
    }

    public void setWrapMode(Texture.WrapMode wrapMode) {
        this.lastAssociatedWrapMode = wrapMode;
    }

    void traceDispose() {
        PrismTrace.textureDisposed(this.texID);
    }

    @Override
    public void dispose() {
        if (this.texID != 0) {
            this.traceDispose();
            GLContext gLContext = this.context.getGLContext();
            for (int i2 = 0; i2 < gLContext.getNumBoundTexture(); ++i2) {
                if (this.texID != gLContext.getBoundTexture(i2)) continue;
                this.context.flushVertexBuffer();
                gLContext.updateActiveTextureUnit(i2);
                gLContext.setBoundTexture(0);
            }
            gLContext.deleteTexture(this.texID);
            this.texID = 0;
        }
    }
}

