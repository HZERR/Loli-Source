/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.View
 */
package org.pushingpixels.trident.android;

import android.graphics.Rect;
import android.view.View;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.android.AndroidRepaintCallback;

public class AndroidRepaintTimeline
extends Timeline {
    public AndroidRepaintTimeline(View mainTimelineView) {
        this(mainTimelineView, null);
    }

    public AndroidRepaintTimeline(View mainTimelineView, Rect toRepaint) {
        super((Object)mainTimelineView);
        this.addCallback(new AndroidRepaintCallback(mainTimelineView, toRepaint));
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

