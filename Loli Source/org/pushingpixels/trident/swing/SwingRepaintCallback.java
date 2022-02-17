/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.swing;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicBoolean;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;

public class SwingRepaintCallback
extends TimelineCallbackAdapter {
    private Component comp;
    private Rectangle rect;
    private AtomicBoolean repaintGuard;

    public SwingRepaintCallback(Component comp) {
        this(comp, null);
    }

    public SwingRepaintCallback(Component comp, Rectangle rect) {
        if (comp == null) {
            throw new NullPointerException("Component must be non-null");
        }
        this.comp = comp;
        if (rect != null) {
            this.rect = new Rectangle(rect);
        }
    }

    public synchronized void setAutoRepaintMode(boolean autoRepaintMode) {
        this.repaintGuard = autoRepaintMode ? null : new AtomicBoolean(false);
    }

    public synchronized void forceRepaintOnNextPulse() {
        if (this.repaintGuard == null) {
            throw new IllegalArgumentException("This method cannot be called on auto-repaint callback");
        }
        this.repaintGuard.set(true);
    }

    public synchronized void setRepaintRectangle(Rectangle rect) {
        this.rect = rect == null ? null : new Rectangle(rect);
    }

    @Override
    public synchronized void onTimelinePulse(float durationFraction, float timelinePosition) {
        this.repaintAsNecessary();
    }

    @Override
    public synchronized void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
        this.repaintAsNecessary();
    }

    private void repaintAsNecessary() {
        if (this.repaintGuard != null && !this.repaintGuard.compareAndSet(true, false)) {
            return;
        }
        if (this.rect == null) {
            this.comp.repaint();
        } else {
            this.comp.repaint(this.rect.x, this.rect.y, this.rect.width, this.rect.height);
        }
    }
}

