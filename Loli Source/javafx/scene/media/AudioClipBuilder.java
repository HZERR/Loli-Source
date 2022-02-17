/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.media;

import javafx.scene.media.AudioClip;
import javafx.util.Builder;

@Deprecated
public final class AudioClipBuilder
implements Builder<AudioClip> {
    private int __set;
    private double balance;
    private int cycleCount;
    private double pan;
    private int priority;
    private double rate;
    private String source;
    private double volume;

    protected AudioClipBuilder() {
    }

    public static AudioClipBuilder create() {
        return new AudioClipBuilder();
    }

    public void applyTo(AudioClip audioClip) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            audioClip.setBalance(this.balance);
        }
        if ((n2 & 2) != 0) {
            audioClip.setCycleCount(this.cycleCount);
        }
        if ((n2 & 4) != 0) {
            audioClip.setPan(this.pan);
        }
        if ((n2 & 8) != 0) {
            audioClip.setPriority(this.priority);
        }
        if ((n2 & 0x10) != 0) {
            audioClip.setRate(this.rate);
        }
        if ((n2 & 0x20) != 0) {
            audioClip.setVolume(this.volume);
        }
    }

    public AudioClipBuilder balance(double d2) {
        this.balance = d2;
        this.__set |= 1;
        return this;
    }

    public AudioClipBuilder cycleCount(int n2) {
        this.cycleCount = n2;
        this.__set |= 2;
        return this;
    }

    public AudioClipBuilder pan(double d2) {
        this.pan = d2;
        this.__set |= 4;
        return this;
    }

    public AudioClipBuilder priority(int n2) {
        this.priority = n2;
        this.__set |= 8;
        return this;
    }

    public AudioClipBuilder rate(double d2) {
        this.rate = d2;
        this.__set |= 0x10;
        return this;
    }

    public AudioClipBuilder source(String string) {
        this.source = string;
        return this;
    }

    public AudioClipBuilder volume(double d2) {
        this.volume = d2;
        this.__set |= 0x20;
        return this;
    }

    @Override
    public AudioClip build() {
        AudioClip audioClip = new AudioClip(this.source);
        this.applyTo(audioClip);
        return audioClip;
    }
}

