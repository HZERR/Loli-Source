/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.animation;

import java.awt.Container;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonModel;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.UiThreadingViolationException;
import org.pushingpixels.substance.api.renderers.SubstanceRenderer;
import org.pushingpixels.substance.internal.animation.IconGlowTracker;
import org.pushingpixels.substance.internal.animation.StateTransitionEvent;
import org.pushingpixels.substance.internal.animation.StateTransitionListener;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

public class StateTransitionTracker {
    JComponent component;
    private ButtonModel model;
    private ChangeListener modelChangeListener;
    private Timeline transitionTimeline;
    private float transitionPosition;
    private FocusListener focusListener;
    private Timeline focusTimeline;
    private Timeline focusLoopTimeline;
    private IconGlowTracker iconGlowTracker;
    private RepaintCallback repaintCallback;
    private boolean isAutoTrackingModelChanges;
    private EventListenerList eventListenerList;
    private String name;
    private ModelStateInfo modelStateInfo;

    public StateTransitionTracker(final JComponent component, ButtonModel model) {
        this.component = component;
        this.model = model;
        this.modelStateInfo = new ModelStateInfo();
        this.modelStateInfo.currState = ComponentState.getState(model, component);
        this.modelStateInfo.currStateNoSelection = ComponentState.getState(model, component, true);
        this.modelStateInfo.clear();
        this.repaintCallback = new RepaintCallback(){

            @Override
            public SwingRepaintCallback getRepaintCallback() {
                return new SwingRepaintCallback(component);
            }
        };
        this.isAutoTrackingModelChanges = true;
        this.eventListenerList = new EventListenerList();
        this.focusTimeline = new Timeline(this.component);
        AnimationConfigurationManager.getInstance().configureTimeline(this.focusTimeline);
        this.focusTimeline.addCallback(this.repaintCallback.getRepaintCallback());
        this.focusTimeline.addCallback(new TimelineCallbackAdapter(){

            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                StateTransitionTracker.this.fireFocusStateTransitionEvent(oldState, newState);
            }
        });
        this.focusLoopTimeline = new Timeline(this.component);
        AnimationConfigurationManager.getInstance().configureTimeline(this.focusLoopTimeline);
        this.focusLoopTimeline.addCallback(this.repaintCallback.getRepaintCallback());
        this.iconGlowTracker = new IconGlowTracker(this.component);
        this.name = "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setRepaintCallback(RepaintCallback repaintCallback) {
        this.repaintCallback = repaintCallback;
    }

    public void registerFocusListeners() {
        this.focusListener = new FocusListener(){

            @Override
            public void focusGained(FocusEvent e2) {
                StateTransitionTracker.this.setFocusState(true);
            }

            @Override
            public void focusLost(FocusEvent e2) {
                StateTransitionTracker.this.setFocusState(false);
            }
        };
        this.component.addFocusListener(this.focusListener);
    }

    public void registerModelListeners() {
        this.modelChangeListener = new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e2) {
                if (StateTransitionTracker.this.isAutoTrackingModelChanges) {
                    StateTransitionTracker.this.onModelStateChanged();
                }
            }
        };
        this.model.addChangeListener(this.modelChangeListener);
    }

    public void unregisterFocusListeners() {
        this.component.removeFocusListener(this.focusListener);
        this.focusListener = null;
    }

    public void unregisterModelListeners() {
        this.model.removeChangeListener(this.modelChangeListener);
        this.modelChangeListener = null;
    }

    public void setTransitionPosition(float transitionPosition) {
        this.transitionPosition = transitionPosition;
    }

    public void setModel(ButtonModel model) {
        this.model.removeChangeListener(this.modelChangeListener);
        if (this.transitionTimeline != null) {
            this.transitionTimeline.abort();
            this.transitionPosition = 0.0f;
        }
        this.modelStateInfo.currState = ComponentState.getState(model, this.component);
        this.modelStateInfo.currStateNoSelection = ComponentState.getState(model, this.component, true);
        this.modelStateInfo.clear();
        this.model = model;
        this.model.addChangeListener(this.modelChangeListener);
        this.component.repaint();
    }

    public ButtonModel getModel() {
        return this.model;
    }

    public void turnOffModelChangeTracking() {
        this.isAutoTrackingModelChanges = false;
    }

    public void onModelStateChanged() {
        StateContributionInfo currRange;
        ComponentState state;
        StateContributionInfo currRange2;
        this.isAutoTrackingModelChanges = true;
        ComponentState newState = ComponentState.getState(this.model, this.component);
        ComponentState newStateNoSelection = ComponentState.getState(this.model, this.component, true);
        boolean isInRenderer = this.component.getClass().isAnnotationPresent(SubstanceRenderer.class);
        if (!isInRenderer) {
            for (Container parent = this.component.getParent(); parent != null; parent = parent.getParent()) {
                if (!CellRendererPane.class.isInstance(parent) && !ListCellRenderer.class.isInstance(parent) && !TreeCellRenderer.class.isInstance(parent) && !TableCellRenderer.class.isInstance(parent)) continue;
                isInRenderer = true;
                break;
            }
        }
        if (isInRenderer || this.component.getParent() == null) {
            this.modelStateInfo.currState = newState;
            this.modelStateInfo.currStateNoSelection = newStateNoSelection;
            this.modelStateInfo.clear();
            return;
        }
        if (this.modelStateInfo.currState == newState) {
            return;
        }
        if (this.transitionTimeline != null) {
            this.transitionTimeline.abort();
        }
        this.transitionTimeline = new Timeline(this);
        this.transitionTimeline.setName("Model transitions");
        this.transitionTimeline.addCallback(this.repaintCallback.getRepaintCallback());
        AnimationConfigurationManager.getInstance().configureTimeline(this.transitionTimeline);
        if (!this.modelStateInfo.currState.isFacetActive(ComponentStateFacet.SELECTION) && newState.isFacetActive(ComponentStateFacet.SELECTION)) {
            this.transitionTimeline.setDuration(this.transitionTimeline.getDuration() / 2L);
        }
        long fullDuration = this.transitionTimeline.getDuration();
        if (this.modelStateInfo.stateContributionMap.containsKey(newState)) {
            this.transitionPosition = ((StateContributionInfo)this.modelStateInfo.stateContributionMap.get(newState)).getContribution();
            this.transitionTimeline.addPropertyToInterpolate("transitionPosition", Float.valueOf(this.transitionPosition), Float.valueOf(1.0f));
            this.transitionTimeline.setDuration((long)((float)fullDuration * (1.0f - this.transitionPosition)));
        } else {
            this.transitionPosition = 0.0f;
            this.transitionTimeline.addPropertyToInterpolate("transitionPosition", Float.valueOf(0.0f), Float.valueOf(1.0f));
        }
        HashMap<ComponentState, StateContributionInfo> newContributionMap = new HashMap<ComponentState, StateContributionInfo>();
        if (this.modelStateInfo.stateContributionMap.containsKey(newState)) {
            for (Map.Entry existing : this.modelStateInfo.stateContributionMap.entrySet()) {
                currRange2 = (StateContributionInfo)existing.getValue();
                state = (ComponentState)existing.getKey();
                float newEnd = state == newState ? 1.0f : 0.0f;
                newContributionMap.put(state, new StateContributionInfo(currRange2.curr, newEnd));
            }
        } else {
            for (Map.Entry existing : this.modelStateInfo.stateContributionMap.entrySet()) {
                currRange2 = (StateContributionInfo)existing.getValue();
                state = (ComponentState)existing.getKey();
                newContributionMap.put(state, new StateContributionInfo(currRange2.curr, 0.0f));
            }
            newContributionMap.put(newState, new StateContributionInfo(0.0f, 1.0f));
        }
        this.modelStateInfo.stateContributionMap = newContributionMap;
        HashMap<ComponentState, StateContributionInfo> newNoSelectionContributionMap = new HashMap<ComponentState, StateContributionInfo>();
        if (this.modelStateInfo.stateNoSelectionContributionMap.containsKey(newStateNoSelection)) {
            for (Map.Entry existing : this.modelStateInfo.stateNoSelectionContributionMap.entrySet()) {
                currRange = (StateContributionInfo)existing.getValue();
                ComponentState state2 = (ComponentState)existing.getKey();
                float newEnd = state2 == newStateNoSelection ? 1.0f : 0.0f;
                newNoSelectionContributionMap.put(state2, new StateContributionInfo(currRange.curr, newEnd));
            }
        } else {
            for (Map.Entry existing : this.modelStateInfo.stateNoSelectionContributionMap.entrySet()) {
                currRange = (StateContributionInfo)existing.getValue();
                ComponentState state3 = (ComponentState)existing.getKey();
                newNoSelectionContributionMap.put(state3, new StateContributionInfo(currRange.curr, 0.0f));
            }
            newNoSelectionContributionMap.put(newStateNoSelection, new StateContributionInfo(0.0f, 1.0f));
        }
        this.modelStateInfo.stateNoSelectionContributionMap = newNoSelectionContributionMap;
        this.modelStateInfo.sync();
        this.transitionTimeline.addCallback(new TimelineCallbackAdapter(){

            @Override
            public void onTimelineStateChanged(final Timeline.TimelineState oldState, final Timeline.TimelineState newState, final float durationFraction, final float timelinePosition) {
                if (newState == Timeline.TimelineState.DONE) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            StateTransitionTracker.this.modelStateInfo.clear();
                            StateTransitionTracker.this.repaintCallback.getRepaintCallback().onTimelineStateChanged(oldState, newState, durationFraction, timelinePosition);
                        }
                    });
                }
            }
        });
        this.transitionTimeline.addCallback(new TimelineCallbackAdapter(){

            @Override
            public void onTimelineStateChanged(final Timeline.TimelineState oldState, final Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        StateTransitionTracker.this.fireModelStateTransitionEvent(oldState, newState);
                    }
                });
            }
        });
        this.transitionTimeline.addCallback(new TimelineCallbackAdapter(){

            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        if (StateTransitionTracker.this.component instanceof JMenuItem) {
                            if (SubstanceCoreUtilities.isCoveredByLightweightPopups(StateTransitionTracker.this.component)) {
                                StateTransitionTracker.this.component.putClientProperty("substancelaf.internal.paint.isCoveredByLightweightPopups", Boolean.TRUE);
                            } else {
                                StateTransitionTracker.this.component.putClientProperty("substancelaf.internal.paint.isCoveredByLightweightPopups", null);
                            }
                        }
                    }
                });
            }

            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        if (StateTransitionTracker.this.component instanceof JMenuItem) {
                            if (SubstanceCoreUtilities.isCoveredByLightweightPopups(StateTransitionTracker.this.component)) {
                                StateTransitionTracker.this.component.putClientProperty("substancelaf.internal.paint.isCoveredByLightweightPopups", Boolean.TRUE);
                            } else {
                                StateTransitionTracker.this.component.putClientProperty("substancelaf.internal.paint.isCoveredByLightweightPopups", null);
                            }
                        }
                    }
                });
            }
        });
        this.transitionTimeline.addCallback(new TimelineCallbackAdapter(){

            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                this.updateActiveStates(timelinePosition);
            }

            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
                this.updateActiveStates(timelinePosition);
            }

            private void updateActiveStates(final float timelinePosition) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        for (StateContributionInfo pair : StateTransitionTracker.this.modelStateInfo.stateContributionMap.values()) {
                            pair.updateContribution(timelinePosition);
                        }
                        for (StateContributionInfo pair : StateTransitionTracker.this.modelStateInfo.stateNoSelectionContributionMap.values()) {
                            pair.updateContribution(timelinePosition);
                        }
                        StateTransitionTracker.this.modelStateInfo.sync();
                    }
                });
            }
        });
        this.modelStateInfo.currState = newState;
        this.modelStateInfo.currStateNoSelection = newStateNoSelection;
        this.transitionTimeline.play();
        if (AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.ICON_GLOW, this.component)) {
            boolean wasRollover = false;
            for (Map.Entry activeEntry : this.modelStateInfo.stateContributionMap.entrySet()) {
                ComponentState activeState = (ComponentState)activeEntry.getKey();
                if (activeState == this.modelStateInfo.currState || !activeState.isFacetActive(ComponentStateFacet.ROLLOVER)) continue;
                wasRollover = true;
                break;
            }
            boolean isRollover = this.modelStateInfo.currState.isFacetActive(ComponentStateFacet.ROLLOVER);
            if (wasRollover && !isRollover) {
                this.iconGlowTracker.cancel();
            }
            if (!wasRollover && isRollover) {
                this.iconGlowTracker.play();
            }
        }
    }

    public float getFocusStrength(boolean hasFocus) {
        if (this.focusTimeline == null) {
            return 0.0f;
        }
        Timeline.TimelineState focusTimelineState = this.focusTimeline.getState();
        if (focusTimelineState == Timeline.TimelineState.READY || focusTimelineState == Timeline.TimelineState.PLAYING_FORWARD || focusTimelineState == Timeline.TimelineState.PLAYING_REVERSE) {
            return this.focusTimeline.getTimelinePosition();
        }
        return hasFocus ? 1.0f : 0.0f;
    }

    public float getFocusLoopPosition() {
        if (this.focusLoopTimeline == null) {
            return 0.0f;
        }
        return this.focusLoopTimeline.getTimelinePosition();
    }

    public float getIconGlowPosition() {
        return this.iconGlowTracker.getIconGlowPosition();
    }

    public float getFacetStrength(ComponentStateFacet stateFacet) {
        float result = 0.0f;
        for (Map.Entry activeEntry : this.modelStateInfo.stateContributionMap.entrySet()) {
            ComponentState activeState = (ComponentState)activeEntry.getKey();
            if (!activeState.isFacetActive(stateFacet)) continue;
            result += ((StateContributionInfo)activeEntry.getValue()).getContribution();
        }
        return result;
    }

    public float getActiveStrength() {
        return this.modelStateInfo.getActiveStrength();
    }

    public void addStateTransitionListener(StateTransitionListener stateTransitionListener) {
        this.eventListenerList.add(StateTransitionListener.class, stateTransitionListener);
    }

    public void removeStateTransitionListener(StateTransitionListener stateTransitionListener) {
        this.eventListenerList.remove(StateTransitionListener.class, stateTransitionListener);
    }

    private void fireModelStateTransitionEvent(Timeline.TimelineState oldState, Timeline.TimelineState newState) {
        if (this.eventListenerList.getListenerCount() == 0) {
            return;
        }
        StateTransitionListener[] listeners = (StateTransitionListener[])this.eventListenerList.getListeners(StateTransitionListener.class);
        if (listeners == null || listeners.length == 0) {
            return;
        }
        StateTransitionEvent event = new StateTransitionEvent(this, oldState, newState);
        for (StateTransitionListener listener : listeners) {
            listener.onModelStateTransition(event);
        }
    }

    private void fireFocusStateTransitionEvent(Timeline.TimelineState oldState, Timeline.TimelineState newState) {
        if (this.eventListenerList.getListenerCount() == 0) {
            return;
        }
        StateTransitionListener[] listeners = (StateTransitionListener[])this.eventListenerList.getListeners(StateTransitionListener.class);
        if (listeners == null || listeners.length == 0) {
            return;
        }
        StateTransitionEvent event = new StateTransitionEvent(this, oldState, newState);
        for (StateTransitionListener listener : listeners) {
            listener.onFocusStateTransition(event);
        }
    }

    public void endTransition() {
        if (this.transitionTimeline != null) {
            this.transitionTimeline.end();
        }
    }

    public void setFocusState(boolean hasFocus) {
        if (hasFocus) {
            this.focusTimeline.play();
            if (AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.FOCUS_LOOP_ANIMATION, this.component)) {
                this.focusLoopTimeline.playLoop(Timeline.RepeatBehavior.LOOP);
            }
        } else {
            this.focusTimeline.playReverse();
            if (AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.FOCUS_LOOP_ANIMATION, this.component)) {
                this.focusLoopTimeline.cancel();
            }
        }
    }

    public boolean hasRunningTimelines() {
        Timeline.TimelineState modelTransitionTimelineState;
        Timeline.TimelineState focusLoopTimelineState;
        Timeline.TimelineState focusTimelineState;
        if (this.focusTimeline != null && (focusTimelineState = this.focusTimeline.getState()) != Timeline.TimelineState.IDLE) {
            return true;
        }
        if (this.focusLoopTimeline != null && (focusLoopTimelineState = this.focusLoopTimeline.getState()) != Timeline.TimelineState.IDLE) {
            return true;
        }
        if (this.iconGlowTracker.isPlaying()) {
            return true;
        }
        return this.transitionTimeline != null && (modelTransitionTimelineState = this.transitionTimeline.getState()) != Timeline.TimelineState.IDLE;
    }

    public IconGlowTracker getIconGlowTracker() {
        return this.iconGlowTracker;
    }

    public ModelStateInfo getModelStateInfo() {
        return this.modelStateInfo;
    }

    public static class ModelStateInfo {
        private Map<ComponentState, StateContributionInfo> stateContributionMap = new HashMap<ComponentState, StateContributionInfo>();
        private Map<ComponentState, StateContributionInfo> stateNoSelectionContributionMap = new HashMap<ComponentState, StateContributionInfo>();
        ComponentState currState;
        ComponentState currStateNoSelection;
        float activeStrength = 0.0f;

        public ComponentState getCurrModelState() {
            return this.currState;
        }

        public ComponentState getCurrModelStateNoSelection() {
            return this.currStateNoSelection;
        }

        public Map<ComponentState, StateContributionInfo> getStateContributionMap() {
            return this.stateContributionMap;
        }

        public Map<ComponentState, StateContributionInfo> getStateNoSelectionContributionMap() {
            return this.stateNoSelectionContributionMap;
        }

        void sync() {
            this.activeStrength = 0.0f;
            for (Map.Entry<ComponentState, StateContributionInfo> activeEntry : this.stateContributionMap.entrySet()) {
                ComponentState activeState = activeEntry.getKey();
                if (!activeState.isActive()) continue;
                this.activeStrength += activeEntry.getValue().getContribution();
            }
        }

        float getActiveStrength() {
            return this.activeStrength;
        }

        void clear() {
            if ((SubstanceCoreUtilities.reallyPrintThreadingExceptions() || SubstanceCoreUtilities.reallyThrowThreadingExceptions()) && !SwingUtilities.isEventDispatchThread()) {
                UiThreadingViolationException uiThreadingViolationError = new UiThreadingViolationException("State tracking must be done on Event Dispatch Thread");
                if (SubstanceCoreUtilities.reallyPrintThreadingExceptions()) {
                    uiThreadingViolationError.printStackTrace(System.err);
                }
                if (SubstanceCoreUtilities.reallyThrowThreadingExceptions()) {
                    throw uiThreadingViolationError;
                }
            }
            this.stateContributionMap.clear();
            this.stateContributionMap.put(this.currState, new StateContributionInfo(1.0f, 1.0f));
            this.stateNoSelectionContributionMap.clear();
            this.stateNoSelectionContributionMap.put(this.currStateNoSelection, new StateContributionInfo(1.0f, 1.0f));
            this.sync();
        }
    }

    public static class StateContributionInfo {
        float start;
        float end;
        float curr;

        public StateContributionInfo(float start, float end) {
            this.start = start;
            this.end = end;
            this.curr = start;
        }

        public float getContribution() {
            return this.curr;
        }

        void updateContribution(float timelinePosition) {
            this.curr = this.start + timelinePosition * (this.end - this.start);
        }
    }

    public static interface RepaintCallback {
        public TimelineCallback getRepaintCallback();
    }
}

