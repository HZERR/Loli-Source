/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.animation;

import java.awt.Component;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

public class IconGlowTracker {
    private Timeline iconGlowTimeline;
    private Component component;

    public IconGlowTracker(Component component) {
        this.component = component;
        this.iconGlowTimeline = new Timeline(this.component);
        AnimationConfigurationManager.getInstance().configureTimeline(this.iconGlowTimeline);
        this.iconGlowTimeline.setDuration(10L * this.iconGlowTimeline.getDuration());
        this.iconGlowTimeline.addCallback(new SwingRepaintCallback(component));
        this.iconGlowTimeline.setName("Icon glow");
    }

    public void play() {
        this.iconGlowTimeline.playLoop(Timeline.RepeatBehavior.REVERSE);
    }

    public void play(int fullLoopCount) {
        this.iconGlowTimeline.playLoop(fullLoopCount, Timeline.RepeatBehavior.REVERSE);
    }

    public boolean isPlaying() {
        return this.iconGlowTimeline.getState() != Timeline.TimelineState.IDLE;
    }

    public void cancel() {
        if (this.iconGlowTimeline.getState() != Timeline.TimelineState.IDLE) {
            this.iconGlowTimeline.cancelAtCycleBreak();
        }
    }

    public float getIconGlowPosition() {
        return this.iconGlowTimeline.getTimelinePosition();
    }
}

