/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.es2.GLPixelFormat;

class MacGLDrawable
extends GLDrawable {
    private static native long nCreateDrawable(long var0, long var2);

    private static native long nGetDummyDrawable(long var0);

    private static native boolean nSwapBuffers(long var0, long var2);

    MacGLDrawable(GLPixelFormat gLPixelFormat) {
        super(0L, gLPixelFormat);
        long l2 = MacGLDrawable.nGetDummyDrawable(gLPixelFormat.getNativePFInfo());
        this.setNativeDrawableInfo(l2);
    }

    MacGLDrawable(long l2, GLPixelFormat gLPixelFormat) {
        super(l2, gLPixelFormat);
        long l3 = MacGLDrawable.nCreateDrawable(l2, gLPixelFormat.getNativePFInfo());
        this.setNativeDrawableInfo(l3);
    }

    @Override
    boolean swapBuffers(GLContext gLContext) {
        return MacGLDrawable.nSwapBuffers(gLContext.getNativeCtxInfo(), this.getNativeDrawableInfo());
    }
}

