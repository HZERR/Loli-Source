/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.es2.GLPixelFormat;

class MacGLContext
extends GLContext {
    private static native long nInitialize(long var0, long var2, long var4, boolean var6);

    private static native long nGetNativeHandle(long var0);

    private static native void nMakeCurrent(long var0, long var2);

    MacGLContext(long l2) {
        this.nativeCtxInfo = l2;
    }

    MacGLContext(GLDrawable gLDrawable, GLPixelFormat gLPixelFormat, GLContext gLContext, boolean bl) {
        int[] arrn = new int[7];
        GLPixelFormat.Attributes attributes = gLPixelFormat.getAttributes();
        arrn[0] = attributes.getRedSize();
        arrn[1] = attributes.getGreenSize();
        arrn[2] = attributes.getBlueSize();
        arrn[3] = attributes.getAlphaSize();
        arrn[4] = attributes.getDepthSize();
        arrn[5] = attributes.isDoubleBuffer() ? 1 : 0;
        arrn[6] = attributes.isOnScreen() ? 1 : 0;
        this.nativeCtxInfo = MacGLContext.nInitialize(gLDrawable.getNativeDrawableInfo(), gLPixelFormat.getNativePFInfo(), gLContext.getNativeHandle(), bl);
    }

    @Override
    long getNativeHandle() {
        return MacGLContext.nGetNativeHandle(this.nativeCtxInfo);
    }

    @Override
    void makeCurrent(GLDrawable gLDrawable) {
        MacGLContext.nMakeCurrent(this.nativeCtxInfo, gLDrawable.getNativeDrawableInfo());
    }
}

