/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.animation;

import java.util.EventObject;
import org.pushingpixels.trident.Timeline;

public class StateTransitionEvent
extends EventObject {
    private Timeline.TimelineState newState;
    private Timeline.TimelineState oldState;

    public StateTransitionEvent(Object source, Timeline.TimelineState oldState, Timeline.TimelineState newState) {
        super(source);
        this.oldState = oldState;
        this.newState = newState;
    }

    public Timeline.TimelineState getOldState() {
        return this.oldState;
    }

    public Timeline.TimelineState getNewState() {
        return this.newState;
    }
}

