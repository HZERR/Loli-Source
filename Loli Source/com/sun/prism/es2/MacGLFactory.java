/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.ES2Pipeline;
import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.es2.GLFactory;
import com.sun.prism.es2.GLGPUInfo;
import com.sun.prism.es2.GLPixelFormat;
import com.sun.prism.es2.MacGLContext;
import com.sun.prism.es2.MacGLDrawable;
import com.sun.prism.es2.MacGLPixelFormat;
import java.util.HashMap;

class MacGLFactory
extends GLFactory {
    private GLGPUInfo[] preQualificationFilter = null;
    private GLGPUInfo[] blackList = new GLGPUInfo[]{new GLGPUInfo("ati", "radeon x1600 opengl engine"), new GLGPUInfo("ati", "radeon x1900 opengl engine"), new GLGPUInfo("intel", "gma x3100 opengl engine")};

    MacGLFactory() {
    }

    private static native long nInitialize(int[] var0);

    private static native int nGetAdapterOrdinal(long var0);

    private static native int nGetAdapterCount();

    private static native boolean nGetIsGL2(long var0);

    @Override
    GLGPUInfo[] getPreQualificationFilter() {
        return this.preQualificationFilter;
    }

    @Override
    GLGPUInfo[] getBlackList() {
        return this.blackList;
    }

    @Override
    GLContext createGLContext(long l2) {
        return new MacGLContext(l2);
    }

    @Override
    GLContext createGLContext(GLDrawable gLDrawable, GLPixelFormat gLPixelFormat, GLContext gLContext, boolean bl) {
        MacGLContext macGLContext = new MacGLContext(gLDrawable, gLPixelFormat, gLContext, bl);
        MacGLContext macGLContext2 = new MacGLContext(gLDrawable, gLPixelFormat, gLContext, bl);
        HashMap hashMap = (HashMap)ES2Pipeline.getInstance().getDeviceDetails();
        hashMap.put("contextPtr", ((GLContext)macGLContext).getNativeHandle());
        return macGLContext2;
    }

    @Override
    GLDrawable createDummyGLDrawable(GLPixelFormat gLPixelFormat) {
        return new MacGLDrawable(gLPixelFormat);
    }

    @Override
    GLDrawable createGLDrawable(long l2, GLPixelFormat gLPixelFormat) {
        return new MacGLDrawable(l2, gLPixelFormat);
    }

    @Override
    GLPixelFormat createGLPixelFormat(long l2, GLPixelFormat.Attributes attributes) {
        return new MacGLPixelFormat(l2, attributes);
    }

    @Override
    boolean initialize(Class class_, GLPixelFormat.Attributes attributes) {
        int[] arrn = new int[]{attributes.getRedSize(), attributes.getGreenSize(), attributes.getBlueSize(), attributes.getAlphaSize(), attributes.getDepthSize(), attributes.isDoubleBuffer() ? 1 : 0, attributes.isOnScreen() ? 1 : 0};
        this.nativeCtxInfo = MacGLFactory.nInitialize(arrn);
        if (this.nativeCtxInfo == 0L) {
            return false;
        }
        this.gl2 = MacGLFactory.nGetIsGL2(this.nativeCtxInfo);
        return true;
    }

    @Override
    int getAdapterCount() {
        return MacGLFactory.nGetAdapterCount();
    }

    @Override
    int getAdapterOrdinal(long l2) {
        return MacGLFactory.nGetAdapterOrdinal(l2);
    }

    @Override
    void updateDeviceDetails(HashMap hashMap) {
        hashMap.put("shareContextPtr", this.getShareContext().getNativeHandle());
    }
}

