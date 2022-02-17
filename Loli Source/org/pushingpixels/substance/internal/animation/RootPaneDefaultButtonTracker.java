/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.animation;

import java.awt.Component;
import java.awt.Container;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

public class RootPaneDefaultButtonTracker
extends UIThreadTimelineCallbackAdapter {
    private static WeakHashMap<JButton, RootPaneDefaultButtonTracker> trackers = new WeakHashMap();
    private WeakReference<JButton> buttonRef;
    private Timeline timeline;

    private RootPaneDefaultButtonTracker(JButton jbutton) {
        this.buttonRef = new WeakReference<JButton>(jbutton);
        this.timeline = new Timeline(this);
        this.timeline.addCallback(this);
        trackers.put(jbutton, this);
    }

    private static boolean isInFocusedWindow(Component component) {
        if (component == null) {
            return false;
        }
        if (component.isFocusOwner()) {
            return true;
        }
        if (component instanceof Container) {
            for (Component comp : ((Container)component).getComponents()) {
                if (!RootPaneDefaultButtonTracker.isInFocusedWindow(comp)) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean hasGlassPane(Component component) {
        if (component == null) {
            return false;
        }
        Component glassPane = null;
        if (component instanceof JDialog) {
            glassPane = ((JDialog)component).getGlassPane();
        }
        if (component instanceof JFrame) {
            glassPane = ((JFrame)component).getGlassPane();
        }
        return glassPane != null && glassPane.isVisible();
    }

    @Override
    public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
        this.onTimelineEvent();
    }

    @Override
    public void onTimelinePulse(float durationFraction, float timelinePosition) {
        this.onTimelineEvent();
    }

    void onTimelineEvent() {
        JButton jButton = (JButton)this.buttonRef.get();
        if (jButton == null) {
            return;
        }
        if (!jButton.isDisplayable()) {
            this.timeline.abort();
            return;
        }
        if (RootPaneDefaultButtonTracker.hasGlassPane(jButton.getTopLevelAncestor())) {
            return;
        }
        if (!RootPaneDefaultButtonTracker.isPulsating(jButton)) {
            RootPaneDefaultButtonTracker tracker = trackers.get(jButton);
            tracker.stopTimer();
            tracker.buttonRef.clear();
            trackers.remove(jButton);
        } else if (!RootPaneDefaultButtonTracker.isInFocusedWindow(jButton.getTopLevelAncestor())) {
            RootPaneDefaultButtonTracker.update(jButton);
        } else if (!jButton.isEnabled()) {
            if (this.timeline.getState() != Timeline.TimelineState.SUSPENDED) {
                this.timeline.suspend();
            }
        } else if (this.timeline.getState() == Timeline.TimelineState.SUSPENDED) {
            this.timeline.resume();
        }
        if (SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            jButton.repaint();
        }
    }

    private void startTimer() {
        this.timeline.playLoop(Timeline.RepeatBehavior.REVERSE);
    }

    private void stopTimer() {
        this.timeline.cancel();
    }

    private boolean isRunning() {
        Timeline.TimelineState state = this.timeline.getState();
        return state == Timeline.TimelineState.PLAYING_FORWARD || state == Timeline.TimelineState.PLAYING_REVERSE;
    }

    public static void update(JButton jButton) {
        if (jButton == null) {
            return;
        }
        boolean hasFocus = RootPaneDefaultButtonTracker.isInFocusedWindow(jButton.getTopLevelAncestor());
        RootPaneDefaultButtonTracker tracker = trackers.get(jButton);
        if (!hasFocus) {
            if (tracker == null) {
                return;
            }
        } else {
            if (tracker != null) {
                tracker.startTimer();
                return;
            }
            tracker = new RootPaneDefaultButtonTracker(jButton);
            tracker.startTimer();
            trackers.put(jButton, tracker);
        }
    }

    public static float getTimelinePosition(JButton jButton) {
        RootPaneDefaultButtonTracker tracker = trackers.get(jButton);
        if (tracker == null) {
            return 0.0f;
        }
        return tracker.timeline.getTimelinePosition();
    }

    public static boolean isAnimating(JButton jButton) {
        RootPaneDefaultButtonTracker tracker = trackers.get(jButton);
        if (tracker == null) {
            return false;
        }
        return tracker.isRunning();
    }

    static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("PulseTracker: \n");
        sb.append("\t" + trackers.size() + " trackers");
        return sb.toString();
    }

    public static boolean isPulsating(JButton jButton) {
        boolean isDefault = jButton.isDefaultButton();
        return isDefault;
    }

    public static void stopAllTimers() {
        for (RootPaneDefaultButtonTracker tracker : trackers.values()) {
            tracker.stopTimer();
        }
    }
}

