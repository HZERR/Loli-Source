/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.events.PlayerEvent;

public class BufferProgressEvent
extends PlayerEvent {
    private double duration;
    private long start;
    private long stop;
    private long position;

    public BufferProgressEvent(double d2, long l2, long l3, long l4) {
        this.duration = d2;
        this.start = l2;
        this.stop = l3;
        this.position = l4;
    }

    public double getDuration() {
        return this.duration;
    }

    public long getBufferStart() {
        return this.start;
    }

    public long getBufferStop() {
        return this.stop;
    }

    public long getBufferPosition() {
        return this.position;
    }
}

