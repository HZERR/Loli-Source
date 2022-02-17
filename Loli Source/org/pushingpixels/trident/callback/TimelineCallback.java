/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.callback;

import org.pushingpixels.trident.Timeline;

public interface TimelineCallback {
    public void onTimelineStateChanged(Timeline.TimelineState var1, Timeline.TimelineState var2, float var3, float var4);

    public void onTimelinePulse(float var1, float var2);
}

