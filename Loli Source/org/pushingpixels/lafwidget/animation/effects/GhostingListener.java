/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.animation.effects;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

public class GhostingListener {
    protected ChangeListener modelListener;
    protected JComponent comp;
    protected ButtonModel buttonModel;
    static final String GHOST_LISTENER_KEY = "lafwidget.internal.ghostListenerKey";
    protected Map<AnimationFacet, Boolean> prevStateMap;
    private Timeline ghostIconRolloverTimeline;
    private Timeline ghostComponentPressedTimeline;
    private static Map<JComponent, Timeline> runningGhostRolloverTimelines = new HashMap<JComponent, Timeline>();
    private static Map<JComponent, Timeline> runningGhostPressTimelines = new HashMap<JComponent, Timeline>();

    public GhostingListener(final JComponent comp, ButtonModel buttonModel) {
        this.comp = comp;
        this.buttonModel = buttonModel;
        this.prevStateMap = new HashMap<AnimationFacet, Boolean>();
        this.prevStateMap.put(AnimationFacet.GHOSTING_ICON_ROLLOVER, buttonModel.isRollover());
        this.prevStateMap.put(AnimationFacet.GHOSTING_BUTTON_PRESS, buttonModel.isPressed());
        this.ghostIconRolloverTimeline = new Timeline(comp);
        AnimationConfigurationManager.getInstance().configureTimeline(this.ghostIconRolloverTimeline);
        this.ghostIconRolloverTimeline.addCallback(new SwingRepaintCallback(comp));
        this.ghostComponentPressedTimeline = new Timeline(comp);
        AnimationConfigurationManager.getInstance().configureTimeline(this.ghostComponentPressedTimeline);
        this.ghostComponentPressedTimeline.addCallback(new SwingRepaintCallback(comp));
        UIThreadTimelineCallbackAdapter ghostCallback = new UIThreadTimelineCallbackAdapter(){
            private boolean wasShowing = true;

            protected void repaintTopLevelWindows(float timelinePosition) {
                Window compWindow;
                if (comp == null) {
                    return;
                }
                boolean isShowing = comp.isShowing();
                if (!(!isShowing || (compWindow = SwingUtilities.getWindowAncestor(comp)).isDisplayable() && compWindow.isShowing() && compWindow.isVisible())) {
                    isShowing = false;
                }
                if (!isShowing) {
                    if (this.wasShowing) {
                        for (Window w2 : Window.getWindows()) {
                            if (!w2.isDisplayable() || !w2.isVisible() || !w2.isShowing()) continue;
                            w2.repaint();
                        }
                    }
                    this.wasShowing = false;
                    return;
                }
                Component root = SwingUtilities.getRoot(comp);
                Rectangle compRect = comp.getBounds();
                compRect.setLocation(comp.getLocationOnScreen());
                compRect.x -= compRect.width / 2;
                compRect.y -= compRect.height / 2;
                compRect.width *= 2;
                compRect.height *= 2;
                int rootRepaintX = compRect.x - root.getLocationOnScreen().x;
                int rootRepaintY = compRect.y - root.getLocationOnScreen().y;
                root.repaint(rootRepaintX, rootRepaintY, compRect.width, compRect.height);
                for (Window w3 : Window.getWindows()) {
                    if (w3 == root || !w3.isDisplayable() || !w3.isVisible() || !w3.isShowing() || !w3.getBounds().intersects(compRect)) continue;
                    int winRepaintX = compRect.x - w3.getLocationOnScreen().x;
                    int winRepaintY = compRect.y - w3.getLocationOnScreen().y;
                    w3.repaint(winRepaintX, winRepaintY, compRect.width, compRect.height);
                }
            }

            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (oldState == Timeline.TimelineState.DONE && newState == Timeline.TimelineState.IDLE) {
                    this.repaintTopLevelWindows(1.0f);
                }
            }

            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
                this.repaintTopLevelWindows(timelinePosition);
            }
        };
        this.ghostIconRolloverTimeline.addCallback(ghostCallback);
        this.ghostComponentPressedTimeline.addCallback(ghostCallback);
        this.ghostIconRolloverTimeline.addCallback(new TimelineCallbackAdapter(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (oldState != Timeline.TimelineState.DONE || newState != Timeline.TimelineState.IDLE) return;
                Class<GhostingListener> class_ = GhostingListener.class;
                synchronized (GhostingListener.class) {
                    runningGhostRolloverTimelines.remove(GhostingListener.this.ghostIconRolloverTimeline);
                    // ** MonitorExit[var5_5] (shouldn't be in output)
                    return;
                }
            }
        });
        this.ghostComponentPressedTimeline.addCallback(new TimelineCallbackAdapter(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (oldState != Timeline.TimelineState.DONE || newState != Timeline.TimelineState.IDLE) return;
                Class<GhostingListener> class_ = GhostingListener.class;
                synchronized (GhostingListener.class) {
                    runningGhostPressTimelines.remove(GhostingListener.this.ghostComponentPressedTimeline);
                    // ** MonitorExit[var5_5] (shouldn't be in output)
                    return;
                }
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    protected void trackModelChange(AnimationFacet animationFacet, boolean newState) {
        if (LafWidgetUtilities.toIgnoreAnimations(this.comp)) {
            return;
        }
        try {
            Class<GhostingListener> class_;
            if (!this.prevStateMap.containsKey(animationFacet)) return;
            boolean prevState = this.prevStateMap.get(animationFacet);
            if (prevState) return;
            if (!newState) return;
            if (animationFacet == AnimationFacet.GHOSTING_ICON_ROLLOVER) {
                class_ = GhostingListener.class;
                // MONITORENTER : org.pushingpixels.lafwidget.animation.effects.GhostingListener.class
                runningGhostRolloverTimelines.put(this.comp, this.ghostIconRolloverTimeline);
                // MONITOREXIT : class_
                this.ghostIconRolloverTimeline.play();
            }
            if (animationFacet != AnimationFacet.GHOSTING_BUTTON_PRESS) return;
            class_ = GhostingListener.class;
            // MONITORENTER : org.pushingpixels.lafwidget.animation.effects.GhostingListener.class
            runningGhostPressTimelines.put(this.comp, this.ghostComponentPressedTimeline);
            // MONITOREXIT : class_
            this.ghostComponentPressedTimeline.play();
            return;
        }
        finally {
            this.prevStateMap.put(animationFacet, newState);
        }
    }

    public void registerListeners() {
        this.modelListener = new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e2) {
                if (AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.GHOSTING_ICON_ROLLOVER, GhostingListener.this.comp)) {
                    GhostingListener.this.trackModelChange(AnimationFacet.GHOSTING_ICON_ROLLOVER, GhostingListener.this.buttonModel.isRollover());
                }
                if (AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.GHOSTING_BUTTON_PRESS, GhostingListener.this.comp)) {
                    GhostingListener.this.trackModelChange(AnimationFacet.GHOSTING_BUTTON_PRESS, GhostingListener.this.buttonModel.isPressed());
                }
            }
        };
        this.buttonModel.addChangeListener(this.modelListener);
        this.comp.putClientProperty(GHOST_LISTENER_KEY, this);
    }

    public void unregisterListeners() {
        this.buttonModel.removeChangeListener(this.modelListener);
        this.comp.putClientProperty(GHOST_LISTENER_KEY, null);
    }

    public static synchronized Map<JComponent, Timeline> getRunningGhostRolloverTimelines() {
        return Collections.unmodifiableMap(runningGhostRolloverTimelines);
    }

    public static synchronized Map<JComponent, Timeline> getRunningGhostPressTimelines() {
        return Collections.unmodifiableMap(runningGhostPressTimelines);
    }

    public Timeline getGhostComponentPressedTimeline() {
        return this.ghostComponentPressedTimeline;
    }

    public Timeline getGhostIconRolloverTimeline() {
        return this.ghostIconRolloverTimeline;
    }
}

