/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLPixelFormat;

abstract class GLDrawable {
    private final long nativeWindow;
    private final GLPixelFormat pixelFormat;
    long nativeDrawableInfo;

    GLDrawable(long l2, GLPixelFormat gLPixelFormat) {
        this.nativeWindow = l2;
        this.pixelFormat = gLPixelFormat;
    }

    GLPixelFormat getPixelFormat() {
        return this.pixelFormat;
    }

    long getNativeWindow() {
        return this.nativeWindow;
    }

    void setNativeDrawableInfo(long l2) {
        this.nativeDrawableInfo = l2;
    }

    long getNativeDrawableInfo() {
        return this.nativeDrawableInfo;
    }

    abstract boolean swapBuffers(GLContext var1);
}

