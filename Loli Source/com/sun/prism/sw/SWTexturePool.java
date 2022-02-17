/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWTexture;

class SWTexturePool
extends BaseResourcePool<SWTexture>
implements TextureResourcePool<SWTexture> {
    static final SWTexturePool instance = new SWTexturePool();

    private static long maxVram() {
        long l2 = Runtime.getRuntime().maxMemory();
        long l3 = PrismSettings.maxVram;
        return Math.min(l2 / 4L, l3);
    }

    private static long targetVram() {
        long l2 = SWTexturePool.maxVram();
        return Math.min(l2 / 2L, PrismSettings.targetVram);
    }

    private SWTexturePool() {
        super(null, SWTexturePool.targetVram(), SWTexturePool.maxVram());
    }

    @Override
    public long used() {
        return 0L;
    }

    @Override
    public long size(SWTexture sWTexture) {
        long l2 = sWTexture.getPhysicalWidth();
        l2 *= (long)sWTexture.getPhysicalHeight();
        if (sWTexture instanceof SWArgbPreTexture) {
            l2 *= 4L;
        }
        return l2;
    }

    @Override
    public long estimateTextureSize(int n2, int n3, PixelFormat pixelFormat) {
        switch (pixelFormat) {
            case BYTE_ALPHA: {
                return (long)n2 * (long)n3;
            }
        }
        return (long)n2 * (long)n3 * 4L;
    }

    @Override
    public long estimateRTTextureSize(int n2, int n3, boolean bl) {
        return (long)n2 * (long)n3 * 4L;
    }
}

