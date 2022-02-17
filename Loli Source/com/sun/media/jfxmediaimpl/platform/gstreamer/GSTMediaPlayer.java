/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import com.sun.media.jfxmediaimpl.platform.gstreamer.GSTMedia;

final class GSTMediaPlayer
extends NativeMediaPlayer {
    private GSTMedia gstMedia = null;
    private float mutedVolume = 1.0f;
    private boolean muteEnabled = false;
    private AudioEqualizer audioEqualizer;
    private AudioSpectrum audioSpectrum;

    private GSTMediaPlayer(GSTMedia gSTMedia) {
        super(gSTMedia);
        this.init();
        this.gstMedia = gSTMedia;
        int n2 = this.gstInitPlayer(this.gstMedia.getNativeMediaRef());
        if (0 != n2) {
            this.dispose();
            this.throwMediaErrorException(n2, null);
        }
        long l2 = this.gstMedia.getNativeMediaRef();
        this.audioSpectrum = this.createNativeAudioSpectrum(this.gstGetAudioSpectrum(l2));
        this.audioEqualizer = this.createNativeAudioEqualizer(this.gstGetAudioEqualizer(l2));
    }

    GSTMediaPlayer(Locator locator) {
        this(new GSTMedia(locator));
    }

    @Override
    public AudioEqualizer getEqualizer() {
        return this.audioEqualizer;
    }

    @Override
    public AudioSpectrum getAudioSpectrum() {
        return this.audioSpectrum;
    }

    private void throwMediaErrorException(int n2, String string) throws MediaException {
        MediaError mediaError = MediaError.getFromCode(n2);
        throw new MediaException(string, null, mediaError);
    }

    @Override
    protected long playerGetAudioSyncDelay() throws MediaException {
        long[] arrl = new long[1];
        int n2 = this.gstGetAudioSyncDelay(this.gstMedia.getNativeMediaRef(), arrl);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
        return arrl[0];
    }

    @Override
    protected void playerSetAudioSyncDelay(long l2) throws MediaException {
        int n2 = this.gstSetAudioSyncDelay(this.gstMedia.getNativeMediaRef(), l2);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
    }

    @Override
    protected void playerPlay() throws MediaException {
        int n2 = this.gstPlay(this.gstMedia.getNativeMediaRef());
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
    }

    @Override
    protected void playerStop() throws MediaException {
        int n2 = this.gstStop(this.gstMedia.getNativeMediaRef());
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
    }

    @Override
    protected void playerPause() throws MediaException {
        int n2 = this.gstPause(this.gstMedia.getNativeMediaRef());
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
    }

    @Override
    protected void playerFinish() throws MediaException {
        int n2 = this.gstFinish(this.gstMedia.getNativeMediaRef());
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
    }

    @Override
    protected float playerGetRate() throws MediaException {
        float[] arrf = new float[1];
        int n2 = this.gstGetRate(this.gstMedia.getNativeMediaRef(), arrf);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
        return arrf[0];
    }

    @Override
    protected void playerSetRate(float f2) throws MediaException {
        int n2 = this.gstSetRate(this.gstMedia.getNativeMediaRef(), f2);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
    }

    @Override
    protected double playerGetPresentationTime() throws MediaException {
        double[] arrd = new double[1];
        int n2 = this.gstGetPresentationTime(this.gstMedia.getNativeMediaRef(), arrd);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
        return arrd[0];
    }

    @Override
    protected boolean playerGetMute() throws MediaException {
        return this.muteEnabled;
    }

    @Override
    protected synchronized void playerSetMute(boolean bl) throws MediaException {
        if (bl != this.muteEnabled) {
            if (bl) {
                float f2 = this.getVolume();
                this.playerSetVolume(0.0f);
                this.muteEnabled = true;
                this.mutedVolume = f2;
            } else {
                this.muteEnabled = false;
                this.playerSetVolume(this.mutedVolume);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected float playerGetVolume() throws MediaException {
        Object object = this;
        synchronized (object) {
            if (this.muteEnabled) {
                return this.mutedVolume;
            }
        }
        object = new float[1];
        int n2 = this.gstGetVolume(this.gstMedia.getNativeMediaRef(), (float[])object);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
        return (float)object[0];
    }

    @Override
    protected synchronized void playerSetVolume(float f2) throws MediaException {
        if (!this.muteEnabled) {
            int n2 = this.gstSetVolume(this.gstMedia.getNativeMediaRef(), f2);
            if (0 != n2) {
                this.throwMediaErrorException(n2, null);
            } else {
                this.mutedVolume = f2;
            }
        } else {
            this.mutedVolume = f2;
        }
    }

    @Override
    protected float playerGetBalance() throws MediaException {
        float[] arrf = new float[1];
        int n2 = this.gstGetBalance(this.gstMedia.getNativeMediaRef(), arrf);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
        return arrf[0];
    }

    @Override
    protected void playerSetBalance(float f2) throws MediaException {
        int n2 = this.gstSetBalance(this.gstMedia.getNativeMediaRef(), f2);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
    }

    @Override
    protected double playerGetDuration() throws MediaException {
        double[] arrd = new double[1];
        int n2 = this.gstGetDuration(this.gstMedia.getNativeMediaRef(), arrd);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
        if (arrd[0] == -1.0) {
            return Double.POSITIVE_INFINITY;
        }
        return arrd[0];
    }

    @Override
    protected void playerSeek(double d2) throws MediaException {
        int n2 = this.gstSeek(this.gstMedia.getNativeMediaRef(), d2);
        if (0 != n2) {
            this.throwMediaErrorException(n2, null);
        }
    }

    @Override
    protected void playerInit() throws MediaException {
    }

    @Override
    protected void playerDispose() {
        this.audioEqualizer = null;
        this.audioSpectrum = null;
        this.gstMedia = null;
    }

    private native int gstInitPlayer(long var1);

    private native long gstGetAudioEqualizer(long var1);

    private native long gstGetAudioSpectrum(long var1);

    private native int gstGetAudioSyncDelay(long var1, long[] var3);

    private native int gstSetAudioSyncDelay(long var1, long var3);

    private native int gstPlay(long var1);

    private native int gstPause(long var1);

    private native int gstStop(long var1);

    private native int gstFinish(long var1);

    private native int gstGetRate(long var1, float[] var3);

    private native int gstSetRate(long var1, float var3);

    private native int gstGetPresentationTime(long var1, double[] var3);

    private native int gstGetVolume(long var1, float[] var3);

    private native int gstSetVolume(long var1, float var3);

    private native int gstGetBalance(long var1, float[] var3);

    private native int gstSetBalance(long var1, float var3);

    private native int gstGetDuration(long var1, double[] var3);

    private native int gstSeek(long var1, double var3);
}

