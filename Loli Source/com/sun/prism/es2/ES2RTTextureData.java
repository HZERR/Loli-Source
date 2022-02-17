/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2TextureData;
import com.sun.prism.impl.PrismTrace;

class ES2RTTextureData
extends ES2TextureData {
    private int fboID;
    private int dbID;
    private int rbID;

    ES2RTTextureData(ES2Context eS2Context, int n2, int n3, int n4, int n5, long l2) {
        super(eS2Context, n2, l2);
        this.fboID = n3;
        PrismTrace.rttCreated((long)n3, n4, n5, l2);
    }

    public int getFboID() {
        return this.fboID;
    }

    public int getMSAARenderBufferID() {
        return this.rbID;
    }

    void setMSAARenderBufferID(int n2) {
        assert (this.getTexID() == 0);
        this.rbID = n2;
    }

    public int getDepthBufferID() {
        return this.dbID;
    }

    void setDepthBufferID(int n2) {
        this.dbID = n2;
    }

    @Override
    void traceDispose() {
        PrismTrace.rttDisposed(this.fboID);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.fboID != 0) {
            this.context.getGLContext().deleteFBO(this.fboID);
            if (this.dbID != 0) {
                this.context.getGLContext().deleteRenderBuffer(this.dbID);
                this.dbID = 0;
            }
            if (this.rbID != 0) {
                this.context.getGLContext().deleteRenderBuffer(this.rbID);
                this.rbID = 0;
            }
            this.fboID = 0;
        }
    }
}

