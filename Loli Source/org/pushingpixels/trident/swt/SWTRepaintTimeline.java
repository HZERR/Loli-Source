/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.widgets.Control
 */
package org.pushingpixels.trident.swt;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.swt.SWTRepaintCallback;

public class SWTRepaintTimeline
extends Timeline {
    private SWTRepaintCallback repaintCallback;

    public SWTRepaintTimeline(Control mainTimelineComp) {
        this(mainTimelineComp, null);
    }

    public SWTRepaintTimeline(Control mainTimelineComp, Rectangle toRepaint) {
        super((Object)mainTimelineComp);
        this.repaintCallback = new SWTRepaintCallback(mainTimelineComp, toRepaint);
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

