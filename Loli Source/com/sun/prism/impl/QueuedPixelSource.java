/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.prism.PixelSource;
import com.sun.prism.impl.BufferUtil;
import java.lang.ref.WeakReference;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class QueuedPixelSource
implements PixelSource {
    private volatile Pixels beingConsumed;
    private volatile Pixels enqueued;
    private final List<WeakReference<Pixels>> saved = new ArrayList<WeakReference<Pixels>>(3);
    private final boolean useDirectBuffers;

    public QueuedPixelSource(boolean bl) {
        this.useDirectBuffers = bl;
    }

    @Override
    public synchronized Pixels getLatestPixels() {
        if (this.beingConsumed != null) {
            throw new IllegalStateException("already consuming pixels: " + this.beingConsumed);
        }
        if (this.enqueued != null) {
            this.beingConsumed = this.enqueued;
            this.enqueued = null;
        }
        return this.beingConsumed;
    }

    @Override
    public synchronized void doneWithPixels(Pixels pixels) {
        if (this.beingConsumed != pixels) {
            throw new IllegalStateException("wrong pixels buffer: " + pixels + " != " + this.beingConsumed);
        }
        this.beingConsumed = null;
    }

    @Override
    public synchronized void skipLatestPixels() {
        if (this.beingConsumed != null) {
            throw new IllegalStateException("cannot skip while processing: " + this.beingConsumed);
        }
        this.enqueued = null;
    }

    private boolean usesSameBuffer(Pixels pixels, Pixels pixels2) {
        if (pixels == pixels2) {
            return true;
        }
        if (pixels == null || pixels2 == null) {
            return false;
        }
        return pixels.getPixels() == pixels2.getPixels();
    }

    public synchronized Pixels getUnusedPixels(int n2, int n3, float f2) {
        WeakReference<Pixels> weakReference;
        int n4 = 0;
        IntBuffer intBuffer = null;
        while (n4 < this.saved.size()) {
            weakReference = this.saved.get(n4);
            Pixels pixels = (Pixels)weakReference.get();
            if (pixels == null) {
                this.saved.remove(n4);
                continue;
            }
            if (this.usesSameBuffer(pixels, this.beingConsumed) || this.usesSameBuffer(pixels, this.enqueued)) {
                ++n4;
                continue;
            }
            if (pixels.getWidthUnsafe() == n2 && pixels.getHeightUnsafe() == n3 && pixels.getScaleUnsafe() == f2) {
                return pixels;
            }
            this.saved.remove(n4);
            intBuffer = (IntBuffer)pixels.getPixels();
            if (intBuffer.capacity() >= n2 * n3) break;
            intBuffer = null;
        }
        if (intBuffer == null) {
            int n5 = n2 * n3;
            intBuffer = this.useDirectBuffers ? BufferUtil.newIntBuffer(n5) : IntBuffer.allocate(n5);
        }
        weakReference = Application.GetApplication().createPixels(n2, n3, intBuffer, f2);
        this.saved.add(new WeakReference<Object>(weakReference));
        return weakReference;
    }

    public synchronized void enqueuePixels(Pixels pixels) {
        this.enqueued = pixels;
    }
}

