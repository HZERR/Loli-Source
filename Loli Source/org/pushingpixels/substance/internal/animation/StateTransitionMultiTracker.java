/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.animation;

import java.util.HashMap;
import java.util.Map;
import org.pushingpixels.substance.internal.animation.StateTransitionEvent;
import org.pushingpixels.substance.internal.animation.StateTransitionListener;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;

public final class StateTransitionMultiTracker<T> {
    private Map<Comparable<T>, StateTransitionTracker> trackerMap = new HashMap<Comparable<T>, StateTransitionTracker>();
    private boolean isInCleaning;

    public synchronized void clear() {
        this.isInCleaning = true;
        for (StateTransitionTracker tracker : this.trackerMap.values()) {
            tracker.endTransition();
        }
        this.trackerMap.clear();
        this.isInCleaning = false;
    }

    public synchronized int size() {
        return this.trackerMap.size();
    }

    public synchronized StateTransitionTracker getTracker(Comparable<T> id) {
        return this.trackerMap.get(id);
    }

    public synchronized void addTracker(final Comparable<T> id, final StateTransitionTracker tracker) {
        this.trackerMap.put(id, tracker);
        StateTransitionListener listener = new StateTransitionListener(){

            @Override
            public void onModelStateTransition(StateTransitionEvent stateTransitionEvent) {
                if (StateTransitionMultiTracker.this.isInCleaning) {
                    return;
                }
                if (!tracker.hasRunningTimelines()) {
                    StateTransitionMultiTracker.this.trackerMap.remove(id);
                    tracker.unregisterModelListeners();
                    tracker.removeStateTransitionListener(this);
                }
            }

            @Override
            public void onFocusStateTransition(StateTransitionEvent stateTransitionEvent) {
                if (StateTransitionMultiTracker.this.isInCleaning) {
                    return;
                }
                if (!tracker.hasRunningTimelines()) {
                    StateTransitionMultiTracker.this.trackerMap.remove(id);
                    tracker.unregisterModelListeners();
                    tracker.removeStateTransitionListener(this);
                }
            }
        };
        tracker.addStateTransitionListener(listener);
    }
}

