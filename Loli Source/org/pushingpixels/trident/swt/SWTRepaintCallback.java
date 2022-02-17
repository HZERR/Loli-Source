/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.widgets.Control
 */
package org.pushingpixels.trident.swt;

import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.RunOnUIThread;
import org.pushingpixels.trident.callback.TimelineCallback;

@RunOnUIThread
public class SWTRepaintCallback
implements TimelineCallback {
    private Control control;
    private Rectangle rect;
    private AtomicBoolean repaintGuard;

    public SWTRepaintCallback(Control control) {
        this(control, null);
    }

    public SWTRepaintCallback(Control control, Rectangle rect) {
        if (control == null) {
            throw new NullPointerException("Control must be non-null");
        }
        this.control = control;
        if (rect != null) {
            this.rect = new Rectangle(rect.x, rect.y, rect.width, rect.height);
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
        this.rect = rect == null ? null : new Rectangle(rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public void onTimelinePulse(float durationFraction, float timelinePosition) {
        this.redrawAsNecessary();
    }

    @Override
    public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
        this.redrawAsNecessary();
    }

    private void redrawAsNecessary() {
        if (this.control.isDisposed()) {
            return;
        }
        if (this.repaintGuard != null && !this.repaintGuard.compareAndSet(true, false)) {
            return;
        }
        if (this.rect == null) {
            this.control.redraw();
        } else {
            this.control.redraw(this.rect.x, this.rect.y, this.rect.width, this.rect.height, true);
        }
    }
}

