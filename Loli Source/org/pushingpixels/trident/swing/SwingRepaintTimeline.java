/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.swing;

import java.awt.Component;
import java.awt.Rectangle;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

public class SwingRepaintTimeline
extends Timeline {
    private SwingRepaintCallback repaintCallback;

    public SwingRepaintTimeline(Component mainTimelineComp) {
        this(mainTimelineComp, null);
    }

    public SwingRepaintTimeline(Component mainTimelineComp, Rectangle toRepaint) {
        super(mainTimelineComp);
        this.repaintCallback = new SwingRepaintCallback(mainTimelineComp, toRepaint);
        this.addCallback(this.repaintCallback);
    }

    public void forceRepaintOnNextPulse() {
        this.repaintCallback.forceRepaintOnNextPulse();
    }

    public void setAutoRepaintMode(boolean autoRepaintMode) {
        this.repaintCallback.setAutoRepaintMode(autoRepaintMode);
    }

    public void setRepaintRectangle(Rectangle rect) {
        this.repaintCallback.setRepaintRectangle(rect);
    }

    @Override
    public void play() {
        throw new UnsupportedOperationException("Only infinite looping is supported");
    }

    @Override
    public void playReverse() {
        throw new UnsupportedOperationException("Only infinite looping is supported");
    }

    @Override
    public void replay() {
        throw new UnsupportedOperationException("Only infinite looping is supported");
    }

    @Override
    public void replayReverse() {
        throw new UnsupportedOperationException("Only infinite looping is supported");
    }

    @Override
    public void playLoop(int loopCount, Timeline.RepeatBehavior repeatBehavior) {
        if (loopCount >= 0) {
            throw new UnsupportedOperationException("Only infinite looping is supported");
        }
        super.playLoop(loopCount, repeatBehavior);
    }
}

