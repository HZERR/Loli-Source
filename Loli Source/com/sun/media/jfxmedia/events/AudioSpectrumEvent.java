/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.events.PlayerEvent;

public class AudioSpectrumEvent
extends PlayerEvent {
    private AudioSpectrum source;
    private double timestamp;
    private double duration;

    public AudioSpectrumEvent(AudioSpectrum audioSpectrum, double d2, double d3) {
        this.source = audioSpectrum;
        this.timestamp = d2;
        this.duration = d3;
    }

    public final AudioSpectrum getSource() {
        return this.source;
    }

    public final double getTimestamp() {
        return this.timestamp;
    }

    public final double getDuration() {
        return this.duration;
    }
}

