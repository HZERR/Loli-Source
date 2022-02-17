/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.GLPixelFormat;

class MacGLPixelFormat
extends GLPixelFormat {
    private static native long nCreatePixelFormat(long var0, int[] var2);

    MacGLPixelFormat(long l2, GLPixelFormat.Attributes attributes) {
        super(l2, attributes);
        int[] arrn = new int[]{attributes.getRedSize(), attributes.getGreenSize(), attributes.getBlueSize(), attributes.getAlphaSize(), attributes.getDepthSize(), attributes.isDoubleBuffer() ? 1 : 0, attributes.isOnScreen() ? 1 : 0};
        long l3 = MacGLPixelFormat.nCreatePixelFormat(l2, arrn);
        this.setNativePFInfo(l3);
    }
}

