/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import java.awt.image.BufferedImage;

class J2DTexturePool
extends BaseResourcePool<BufferedImage>
implements TextureResourcePool<BufferedImage> {
    static final J2DTexturePool instance = new J2DTexturePool();

    private static long maxVram() {
        long l2 = Runtime.getRuntime().maxMemory();
        long l3 = PrismSettings.maxVram;
        return Math.min(l2 / 4L, l3);
    }

    private static long targetVram() {
        long l2 = J2DTexturePool.maxVram();
        return Math.min(l2 / 2L, PrismSettings.targetVram);
    }

    private J2DTexturePool() {
        super(null, J2DTexturePool.targetVram(), J2DTexturePool.maxVram());
    }

    @Override
    public long used() {
        Runtime runtime = Runtime.getRuntime();
        long l2 = runtime.totalMemory() - runtime.freeMemory();
        long l3 = runtime.maxMemory() - l2;
        long l4 = this.max() - this.managed();
        return this.max() - Math.min(l3, l4);
    }

    static long size(int n2, int n3, int n4) {
        long l2 = (long)n2 * (long)n3;
        switch (n4) {
            case 5: {
                return l2 * 3L;
            }
            case 10: {
                return l2;
            }
            case 3: {
                return l2 * 4L;
            }
        }
        throw new InternalError("Unrecognized BufferedImage");
    }

    @Override
    public long size(BufferedImage bufferedImage) {
        return J2DTexturePool.size(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
    }

    @Override
    public long estimateTextureSize(int n2, int n3, PixelFormat pixelFormat) {
        int n4;
        switch (pixelFormat) {
            case BYTE_RGB: {
                n4 = 5;
                break;
            }
            case BYTE_GRAY: {
                n4 = 10;
                break;
            }
            case INT_ARGB_PRE: 
            case BYTE_BGRA_PRE: {
                n4 = 3;
                break;
            }
            default: {
                throw new InternalError("Unrecognized PixelFormat (" + (Object)((Object)pixelFormat) + ")!");
            }
        }
        return J2DTexturePool.size(n2, n3, n4);
    }

    @Override
    public long estimateRTTextureSize(int n2, int n3, boolean bl) {
        return J2DTexturePool.size(n2, n3, 3);
    }

    public String toString() {
        return "J2D Texture Pool";
    }
}

