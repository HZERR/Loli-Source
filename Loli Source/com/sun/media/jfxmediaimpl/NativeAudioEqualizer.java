/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.EqualizerBand;

final class NativeAudioEqualizer
implements AudioEqualizer {
    private final long nativeRef;

    NativeAudioEqualizer(long l2) {
        if (l2 == 0L) {
            throw new IllegalArgumentException("Invalid native media reference");
        }
        this.nativeRef = l2;
    }

    @Override
    public boolean getEnabled() {
        return this.nativeGetEnabled(this.nativeRef);
    }

    @Override
    public void setEnabled(boolean bl) {
        this.nativeSetEnabled(this.nativeRef, bl);
    }

    @Override
    public EqualizerBand addBand(double d2, double d3, double d4) {
        return this.nativeGetNumBands(this.nativeRef) >= 64 && d4 >= -24.0 && d4 <= 12.0 ? null : this.nativeAddBand(this.nativeRef, d2, d3, d4);
    }

    @Override
    public boolean removeBand(double d2) {
        return d2 > 0.0 ? this.nativeRemoveBand(this.nativeRef, d2) : false;
    }

    private native boolean nativeGetEnabled(long var1);

    private native void nativeSetEnabled(long var1, boolean var3);

    private native int nativeGetNumBands(long var1);

    private native EqualizerBand nativeAddBand(long var1, double var3, double var5, double var7);

    private native boolean nativeRemoveBand(long var1, double var3);
}

