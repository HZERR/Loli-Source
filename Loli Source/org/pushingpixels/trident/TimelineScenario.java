/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.TimelineEngine;
import org.pushingpixels.trident.callback.TimelineScenarioCallback;

public class TimelineScenario {
    private Set<TimelineScenarioActor> waitingActors = new HashSet<TimelineScenarioActor>();
    private Set<TimelineScenarioActor> runningActors = new HashSet<TimelineScenarioActor>();
    private Set<TimelineScenarioActor> doneActors = new HashSet<TimelineScenarioActor>();
    private Map<TimelineScenarioActor, Set<TimelineScenarioActor>> dependencies = new HashMap<TimelineScenarioActor, Set<TimelineScenarioActor>>();
    Chain callback = new Chain(new TimelineScenarioCallback[0]);
    TimelineScenarioState state = TimelineScenarioState.IDLE;
    TimelineScenarioState statePriorToSuspension;
    boolean isLooping;

    public void addScenarioActor(TimelineScenarioActor actor) {
        if (actor.isDone()) {
            throw new IllegalArgumentException("Already finished");
        }
        this.waitingActors.add(actor);
    }

    public void addCallback(TimelineScenarioCallback callback) {
        if (this.doneActors.size() > 0) {
            throw new IllegalArgumentException("Cannot change state of non-idle timeline scenario");
        }
        this.callback.addCallback(callback);
    }

    private void checkDependencyParam(TimelineScenarioActor actor) {
        if (!this.waitingActors.contains(actor)) {
            throw new IllegalArgumentException("Must be first added with addScenarioActor() API");
        }
    }

    public void addDependency(TimelineScenarioActor actor, TimelineScenarioActor ... waitFor) {
        this.checkDependencyParam(actor);
        for (TimelineScenarioActor wait : waitFor) {
            this.checkDependencyParam(wait);
        }
        if (!this.dependencies.containsKey(actor)) {
            this.dependencies.put(actor, new HashSet());
        }
        this.dependencies.get(actor).addAll(Arrays.asList(waitFor));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void checkDoneActors() {
        Object object = TimelineEngine.LOCK;
        synchronized (object) {
            Iterator<TimelineScenarioActor> itRunning = this.runningActors.iterator();
            while (itRunning.hasNext()) {
                TimelineScenarioActor stillRunning = itRunning.next();
                if (!stillRunning.isDone()) continue;
                itRunning.remove();
                this.doneActors.add(stillRunning);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    Set<TimelineScenarioActor> getReadyActors() {
        Object object = TimelineEngine.LOCK;
        synchronized (object) {
            if (this.state == TimelineScenarioState.SUSPENDED) {
                return new HashSet<TimelineScenarioActor>();
            }
            this.checkDoneActors();
            HashSet<TimelineScenarioActor> result = new HashSet<TimelineScenarioActor>();
            Iterator<TimelineScenarioActor> itWaiting = this.waitingActors.iterator();
            while (itWaiting.hasNext()) {
                TimelineScenarioActor waitingActor = itWaiting.next();
                boolean canRun = true;
                Set<TimelineScenarioActor> toWaitFor = this.dependencies.get(waitingActor);
                if (toWaitFor != null) {
                    for (TimelineScenarioActor actorToWaitFor : toWaitFor) {
                        if (this.doneActors.contains(actorToWaitFor)) continue;
                        canRun = false;
                        break;
                    }
                }
                if (!canRun) continue;
                this.runningActors.add(waitingActor);
                itWaiting.remove();
                result.add(waitingActor);
            }
            if (this.waitingActors.isEmpty() && this.runningActors.isEmpty()) {
                if (!this.isLooping) {
                    this.state = TimelineScenarioState.DONE;
                } else {
                    for (TimelineScenarioActor done : this.doneActors) {
                        done.resetDoneFlag();
                    }
                    this.waitingActors.addAll(this.doneActors);
                    this.doneActors.clear();
                }
            }
            return result;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void cancel() {
        Object object = TimelineEngine.LOCK;
        synchronized (object) {
            TimelineScenarioState oldState = this.state;
            if (oldState != TimelineScenarioState.PLAYING) {
                return;
            }
            this.state = TimelineScenarioState.DONE;
            for (TimelineScenarioActor waiting : this.waitingActors) {
                if (!(waiting instanceof Timeline)) continue;
                ((Timeline)waiting).cancel();
            }
            for (TimelineScenarioActor running : this.runningActors) {
                if (!(running instanceof Timeline)) continue;
                ((Timeline)running).cancel();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void suspend() {
        Object object = TimelineEngine.LOCK;
        synchronized (object) {
            TimelineScenarioState oldState = this.state;
            if (oldState != TimelineScenarioState.PLAYING) {
                return;
            }
            this.statePriorToSuspension = oldState;
            this.state = TimelineScenarioState.SUSPENDED;
            for (TimelineScenarioActor running : this.runningActors) {
                if (!(running instanceof Timeline)) continue;
                ((Timeline)running).suspend();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void resume() {
        Object object = TimelineEngine.LOCK;
        synchronized (object) {
            TimelineScenarioState oldState = this.state;
            if (oldState != TimelineScenarioState.SUSPENDED) {
                return;
            }
            this.state = this.statePriorToSuspension;
            for (TimelineScenarioActor running : this.runningActors) {
                if (!(running instanceof Timeline)) continue;
                ((Timeline)running).resume();
            }
        }
    }

    public void play() {
        this.isLooping = false;
        this.state = TimelineScenarioState.PLAYING;
        TimelineEngine.getInstance().runTimelineScenario(this, new Runnable(){

            @Override
            public void run() {
                TimelineEngine.getInstance().playScenario(TimelineScenario.this);
            }
        });
    }

    public void playLoop() {
        for (TimelineScenarioActor actor : this.waitingActors) {
            if (actor.supportsReplay()) continue;
            throw new UnsupportedOperationException("Can't loop scenario with actor(s) that don't support replay");
        }
        this.isLooping = true;
        this.state = TimelineScenarioState.PLAYING;
        TimelineEngine.getInstance().runTimelineScenario(this, new Runnable(){

            @Override
            public void run() {
                TimelineEngine.getInstance().playScenario(TimelineScenario.this);
            }
        });
    }

    public final TimelineScenarioState getState() {
        return this.state;
    }

    public static class RendezvousSequence
    extends TimelineScenario {
        private Set<TimelineScenarioActor> addedSinceLastRendezvous = new HashSet<TimelineScenarioActor>();
        private Set<TimelineScenarioActor> addedPriorToLastRendezvous = new HashSet<TimelineScenarioActor>();

        @Override
        public void addDependency(TimelineScenarioActor actor, TimelineScenarioActor ... waitFor) {
            throw new UnsupportedOperationException("Explicit dependencies not supported");
        }

        @Override
        public void addScenarioActor(TimelineScenarioActor actor) {
            super.addScenarioActor(actor);
            this.addedSinceLastRendezvous.add(actor);
        }

        public void rendezvous() {
            if (this.addedPriorToLastRendezvous.size() > 0) {
                for (TimelineScenarioActor sinceLast : this.addedSinceLastRendezvous) {
                    for (TimelineScenarioActor beforeLast : this.addedPriorToLastRendezvous) {
                        super.addDependency(sinceLast, beforeLast);
                    }
                }
            }
            this.addedPriorToLastRendezvous.clear();
            this.addedPriorToLastRendezvous.addAll(this.addedSinceLastRendezvous);
            this.addedSinceLastRendezvous.clear();
        }

        @Override
        public void play() {
            this.rendezvous();
            super.play();
        }

        @Override
        public void playLoop() {
            this.rendezvous();
            super.playLoop();
        }
    }

    public static class Sequence
    extends TimelineScenario {
        private TimelineScenarioActor lastActor;

        @Override
        public void addDependency(TimelineScenarioActor actor, TimelineScenarioActor ... waitFor) {
            throw new UnsupportedOperationException("Explicit dependencies not supported");
        }

        @Override
        public void addScenarioActor(TimelineScenarioActor actor) {
            super.addScenarioActor(actor);
            if (this.lastActor != null) {
                super.addDependency(actor, this.lastActor);
            }
            this.lastActor = actor;
        }
    }

    public static class Parallel
    extends TimelineScenario {
        @Override
        public void addDependency(TimelineScenarioActor actor, TimelineScenarioActor ... waitFor) {
            throw new UnsupportedOperationException("Explicit dependencies not supported");
        }
    }

    public static interface TimelineScenarioActor {
        public boolean isDone();

        public boolean supportsReplay();

        public void resetDoneFlag();

        public void play();
    }

    class Chain
    implements TimelineScenarioCallback {
        private List<TimelineScenarioCallback> callbacks = new ArrayList<TimelineScenarioCallback>();

        public Chain(TimelineScenarioCallback ... callbacks) {
            for (TimelineScenarioCallback callback : callbacks) {
                this.callbacks.add(callback);
            }
        }

        public void addCallback(TimelineScenarioCallback callback) {
            this.callbacks.add(callback);
        }

        @Override
        public void onTimelineScenarioDone() {
            for (TimelineScenarioCallback callback : this.callbacks) {
                callback.onTimelineScenarioDone();
            }
        }
    }

    public static enum TimelineScenarioState {
        DONE,
        PLAYING,
        IDLE,
        SUSPENDED;

    }
}

