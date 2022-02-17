/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

final class NativeAudioClip
extends AudioClip {
    private final Locator mediaSource;
    private long nativeHandle = 0L;
    private static NativeAudioClipDisposer clipDisposer = new NativeAudioClipDisposer();

    private static native boolean nacInit();

    private static native long nacLoad(Locator var0);

    private static native long nacCreate(byte[] var0, int var1, int var2, int var3, int var4, int var5);

    private static native void nacUnload(long var0);

    private static native void nacStopAll();

    public static synchronized boolean init() {
        return NativeAudioClip.nacInit();
    }

    public static AudioClip load(URI uRI) {
        NativeAudioClip nativeAudioClip = null;
        try {
            Locator locator = new Locator(uRI);
            locator.init();
            nativeAudioClip = new NativeAudioClip(locator);
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new MediaException("Non-compliant URI", uRISyntaxException);
        }
        catch (IOException iOException) {
            throw new MediaException("Cannot connect to media", iOException);
        }
        if (null == nativeAudioClip || 0L == nativeAudioClip.getNativeHandle()) {
            nativeAudioClip = null;
            throw new MediaException("Cannot create audio clip");
        }
        MediaDisposer.addResourceDisposer(nativeAudioClip, nativeAudioClip.getNativeHandle(), clipDisposer);
        return nativeAudioClip;
    }

    public static AudioClip create(byte[] arrby, int n2, int n3, int n4, int n5, int n6) {
        NativeAudioClip nativeAudioClip = new NativeAudioClip(arrby, n2, n3, n4, n5, n6);
        if (null == nativeAudioClip || 0L == nativeAudioClip.getNativeHandle()) {
            nativeAudioClip = null;
            throw new MediaException("Cannot create audio clip");
        }
        MediaDisposer.addResourceDisposer(nativeAudioClip, nativeAudioClip.getNativeHandle(), clipDisposer);
        return nativeAudioClip;
    }

    private native NativeAudioClip nacCreateSegment(long var1, double var3, double var5);

    private native NativeAudioClip nacCreateSegment(long var1, int var3, int var4);

    private native NativeAudioClip nacResample(long var1, int var3, int var4, int var5);

    private native NativeAudioClip nacAppend(long var1, long var3);

    private native boolean nacIsPlaying(long var1);

    private native void nacPlay(long var1, double var3, double var5, double var7, double var9, int var11, int var12);

    private native void nacStop(long var1);

    private NativeAudioClip(Locator locator) {
        this.mediaSource = locator;
        this.nativeHandle = NativeAudioClip.nacLoad(this.mediaSource);
    }

    private NativeAudioClip(byte[] arrby, int n2, int n3, int n4, int n5, int n6) {
        this.mediaSource = null;
        this.nativeHandle = NativeAudioClip.nacCreate(arrby, n2, n3, n4, n5, n6);
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override
    public AudioClip createSegment(double d2, double d3) {
        return this.nacCreateSegment(this.nativeHandle, d2, d3);
    }

    @Override
    public AudioClip createSegment(int n2, int n3) {
        return this.nacCreateSegment(this.nativeHandle, n2, n3);
    }

    @Override
    public AudioClip resample(int n2, int n3, int n4) {
        return this.nacResample(this.nativeHandle, n2, n3, n4);
    }

    @Override
    public AudioClip append(AudioClip audioClip) {
        if (!(audioClip instanceof NativeAudioClip)) {
            throw new IllegalArgumentException("AudioClip type mismatch, cannot append");
        }
        return this.nacAppend(this.nativeHandle, ((NativeAudioClip)audioClip).getNativeHandle());
    }

    @Override
    public AudioClip flatten() {
        return this;
    }

    @Override
    public boolean isPlaying() {
        return this.nacIsPlaying(this.nativeHandle);
    }

    @Override
    public void play() {
        this.nacPlay(this.nativeHandle, this.clipVolume, this.clipBalance, this.clipPan, this.clipRate, this.loopCount, this.clipPriority);
    }

    @Override
    public void play(double d2) {
        this.nacPlay(this.nativeHandle, d2, this.clipBalance, this.clipPan, this.clipRate, this.loopCount, this.clipPriority);
    }

    @Override
    public void play(double d2, double d3, double d4, double d5, int n2, int n3) {
        this.nacPlay(this.nativeHandle, d2, d3, d5, d4, n2, n3);
    }

    @Override
    public void stop() {
        this.nacStop(this.nativeHandle);
    }

    public static void stopAllClips() {
        NativeAudioClip.nacStopAll();
    }

    private static class NativeAudioClipDisposer
    implements MediaDisposer.ResourceDisposer {
        private NativeAudioClipDisposer() {
        }

        @Override
        public void disposeResource(Object object) {
            long l2 = (Long)object;
            if (0L != l2) {
                NativeAudioClip.nacUnload(l2);
            }
        }
    }
}

