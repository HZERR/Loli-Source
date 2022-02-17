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
import org.pushingpixels.trident.callback.RunOnUIThread;
import org.pushingpixels.trident.callback.TimelineCallback;

@RunOnUIThread
public class AndroidRepaintCallback
implements TimelineCallback {
    private View view;
    private Rect rect;

    public AndroidRepaintCallback(View view) {
        this(view, null);
    }

    public AndroidRepaintCallback(View view, Rect rect) {
        if (view == null) {
            throw new NullPointerException("View must be non-null");
        }
        this.view = view;
        this.rect = rect;
    }

    @Override
    public void onTimelinePulse(float durationFraction, float timelinePosition) {
        if (this.rect == null) {
            this.view.invalidate();
        } else {
            this.view.invalidate(this.rect.left, this.rect.top, this.rect.right, this.rect.bottom);
        }
    }

    @Override
    public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
        if (this.rect == null) {
            this.view.invalidate();
        } else {
            this.view.invalidate(this.rect.left, this.rect.top, this.rect.right, this.rect.bottom);
        }
    }
}

