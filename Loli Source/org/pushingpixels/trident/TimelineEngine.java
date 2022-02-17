/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.jcip.annotations.GuardedBy
 */
package org.pushingpixels.trident;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import net.jcip.annotations.GuardedBy;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.TimelineScenario;
import org.pushingpixels.trident.TridentConfig;
import org.pushingpixels.trident.callback.RunOnUIThread;

class TimelineEngine {
    public static boolean DEBUG_MODE = false;
    private static TimelineEngine instance;
    private Set<Timeline> runningTimelines;
    private Set<TimelineScenario> runningScenarios;
    long lastIterationTimeStamp;
    long scheduledPulseShutdown = Long.MAX_VALUE;
    boolean callbackWasIdle;
    private final Object threadSemaphore = new Object();
    @GuardedBy(value="threadSemaphore")
    TridentAnimationThread animatorThread;
    private BlockingQueue<Runnable> callbackQueue;
    @GuardedBy(value="threadSemaphore")
    private TimelineCallbackThread callbackThread;
    static final Object LOCK;

    private void checkAnimatorThread() {
        if (!this.isTimelinesEmpty()) {
            this.getAnimatorThread();
        }
    }

    private void checkCallbackThread() {
        if (!this.callbackQueue.isEmpty()) {
            this.getCallbackThread();
        }
    }

    private TimelineEngine() {
        this.runningTimelines = new HashSet<Timeline>();
        this.runningScenarios = new HashSet<TimelineScenario>();
        this.callbackQueue = new LinkedBlockingQueue<Runnable>();
        this.callbackThread = this.getCallbackThread();
    }

    public static synchronized TimelineEngine getInstance() {
        if (instance == null) {
            instance = new TimelineEngine();
        }
        return instance;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void updateTimelines() {
        Object object = LOCK;
        synchronized (object) {
            try {
                if (this.isTimelinesEmpty()) {
                    return;
                }
                long passedSinceLastIteration = System.currentTimeMillis() - this.lastIterationTimeStamp;
                if (passedSinceLastIteration < 0L) {
                    passedSinceLastIteration = 0L;
                }
                if (DEBUG_MODE) {
                    System.out.println("Elapsed since last iteration: " + passedSinceLastIteration + "ms");
                }
                Iterator<Timeline> itTimeline = this.runningTimelines.iterator();
                while (itTimeline.hasNext()) {
                    Timeline timeline = itTimeline.next();
                    if (timeline.getState() == Timeline.TimelineState.SUSPENDED) continue;
                    boolean timelineWasInReadyState = false;
                    if (timeline.getState() == Timeline.TimelineState.READY) {
                        if (timeline.timeUntilPlay - passedSinceLastIteration > 0L) {
                            timeline.timeUntilPlay -= passedSinceLastIteration;
                            continue;
                        }
                        timelineWasInReadyState = true;
                        timeline.popState();
                        this.callbackCallTimelineStateChanged(timeline, Timeline.TimelineState.READY);
                    }
                    boolean hasEnded = false;
                    if (DEBUG_MODE) {
                        System.out.println("Processing " + timeline.id + "[" + timeline.mainObject.getClass().getSimpleName() + "] from " + timeline.durationFraction + ". Callback - " + (timeline.callback == null ? "no" : "yes"));
                    }
                    switch (timeline.getState()) {
                        case PLAYING_FORWARD: {
                            int loopsToLive;
                            if (!timelineWasInReadyState) {
                                timeline.durationFraction += (float)passedSinceLastIteration / (float)timeline.duration;
                            }
                            timeline.timelinePosition = timeline.ease.map(timeline.durationFraction);
                            if (DEBUG_MODE) {
                                System.out.println("Timeline position: " + (long)(timeline.durationFraction * (float)timeline.duration) + "/" + timeline.duration + " = " + timeline.durationFraction);
                            }
                            if (!(timeline.durationFraction > 1.0f)) break;
                            timeline.durationFraction = 1.0f;
                            timeline.timelinePosition = 1.0f;
                            if (timeline.isLooping) {
                                boolean stopLoopingAnimation = timeline.toCancelAtCycleBreak;
                                loopsToLive = timeline.repeatCount;
                                if (loopsToLive > 0) {
                                    stopLoopingAnimation = stopLoopingAnimation || --loopsToLive == 0;
                                    timeline.repeatCount = loopsToLive;
                                }
                                if (stopLoopingAnimation) {
                                    hasEnded = true;
                                    itTimeline.remove();
                                    break;
                                }
                                if (timeline.repeatBehavior == Timeline.RepeatBehavior.REVERSE) {
                                    timeline.replaceState(Timeline.TimelineState.PLAYING_REVERSE);
                                    if (timeline.cycleDelay > 0L) {
                                        timeline.pushState(Timeline.TimelineState.READY);
                                        timeline.timeUntilPlay = timeline.cycleDelay;
                                    }
                                    this.callbackCallTimelineStateChanged(timeline, Timeline.TimelineState.PLAYING_FORWARD);
                                    break;
                                }
                                timeline.durationFraction = 0.0f;
                                timeline.timelinePosition = 0.0f;
                                if (timeline.cycleDelay > 0L) {
                                    timeline.pushState(Timeline.TimelineState.READY);
                                    timeline.timeUntilPlay = timeline.cycleDelay;
                                    this.callbackCallTimelineStateChanged(timeline, Timeline.TimelineState.PLAYING_FORWARD);
                                    break;
                                }
                                this.callbackCallTimelineStateChanged(timeline, Timeline.TimelineState.PLAYING_FORWARD);
                                break;
                            }
                            hasEnded = true;
                            itTimeline.remove();
                            break;
                        }
                        case PLAYING_REVERSE: {
                            int loopsToLive;
                            if (!timelineWasInReadyState) {
                                timeline.durationFraction -= (float)passedSinceLastIteration / (float)timeline.duration;
                            }
                            timeline.timelinePosition = timeline.ease.map(timeline.durationFraction);
                            if (DEBUG_MODE) {
                                System.out.println("Timeline position: " + (long)(timeline.durationFraction * (float)timeline.duration) + "/" + timeline.duration + " = " + timeline.durationFraction);
                            }
                            if (!(timeline.durationFraction < 0.0f)) break;
                            timeline.durationFraction = 0.0f;
                            timeline.timelinePosition = 0.0f;
                            if (timeline.isLooping) {
                                boolean stopLoopingAnimation = timeline.toCancelAtCycleBreak;
                                loopsToLive = timeline.repeatCount;
                                if (loopsToLive > 0) {
                                    stopLoopingAnimation = stopLoopingAnimation || --loopsToLive == 0;
                                    timeline.repeatCount = loopsToLive;
                                }
                                if (stopLoopingAnimation) {
                                    hasEnded = true;
                                    itTimeline.remove();
                                    break;
                                }
                                timeline.replaceState(Timeline.TimelineState.PLAYING_FORWARD);
                                if (timeline.cycleDelay > 0L) {
                                    timeline.pushState(Timeline.TimelineState.READY);
                                    timeline.timeUntilPlay = timeline.cycleDelay;
                                }
                                this.callbackCallTimelineStateChanged(timeline, Timeline.TimelineState.PLAYING_REVERSE);
                                break;
                            }
                            hasEnded = true;
                            itTimeline.remove();
                            break;
                        }
                        default: {
                            throw new IllegalStateException("Timeline cannot be in " + (Object)((Object)timeline.getState()) + " state");
                        }
                    }
                    if (hasEnded) {
                        if (DEBUG_MODE) {
                            System.out.println("Ending " + timeline.id + " on " + " in state " + timeline.getState().name() + " at position " + timeline.durationFraction);
                        }
                        Timeline.TimelineState oldState = timeline.getState();
                        timeline.replaceState(Timeline.TimelineState.DONE);
                        this.callbackCallTimelineStateChanged(timeline, oldState);
                        timeline.popState();
                        if (timeline.getState() != Timeline.TimelineState.IDLE) {
                            throw new IllegalStateException("Timeline should be IDLE at this point");
                        }
                        this.callbackCallTimelineStateChanged(timeline, Timeline.TimelineState.DONE);
                        continue;
                    }
                    if (DEBUG_MODE) {
                        System.out.println("Calling " + timeline.id + " on " + timeline.durationFraction);
                    }
                    this.callbackCallTimelinePulse(timeline);
                }
                if (this.runningScenarios.size() > 0) {
                    Iterator<TimelineScenario> it = this.runningScenarios.iterator();
                    while (it.hasNext()) {
                        TimelineScenario scenario = it.next();
                        if (scenario.state == TimelineScenario.TimelineScenarioState.DONE) {
                            it.remove();
                            this.callbackCallTimelineScenarioEnded(scenario);
                            continue;
                        }
                        Set<TimelineScenario.TimelineScenarioActor> readyActors = scenario.getReadyActors();
                        if (readyActors == null) continue;
                        for (TimelineScenario.TimelineScenarioActor readyActor : readyActors) {
                            readyActor.play();
                        }
                    }
                }
            }
            finally {
                this.lastIterationTimeStamp = System.currentTimeMillis();
            }
        }
    }

    private boolean isTimelinesEmpty() {
        boolean empty;
        boolean bl = empty = this.runningTimelines.size() == 0 && this.runningScenarios.size() == 0;
        if (empty) {
            if (this.scheduledPulseShutdown == Long.MAX_VALUE) {
                this.scheduledPulseShutdown = this.lastIterationTimeStamp + 60000L;
            }
        } else {
            this.scheduledPulseShutdown = Long.MAX_VALUE;
        }
        return empty;
    }

    private void callbackCallTimelineStateChanged(final Timeline timeline, final Timeline.TimelineState oldState) {
        final Timeline.TimelineState newState = timeline.getState();
        final float durationFraction = timeline.durationFraction;
        final float timelinePosition = timeline.timelinePosition;
        Runnable callbackRunnable = new Runnable(){

            @Override
            public void run() {
                boolean shouldRunOnUIThread = false;
                for (Class<?> clazz = timeline.callback.getClass(); clazz != null && !shouldRunOnUIThread; clazz = clazz.getSuperclass()) {
                    shouldRunOnUIThread = clazz.isAnnotationPresent(RunOnUIThread.class);
                }
                if (shouldRunOnUIThread && timeline.uiToolkitHandler != null) {
                    timeline.uiToolkitHandler.runOnUIThread(timeline.mainObject, new Runnable(){

                        @Override
                        public void run() {
                            timeline.callback.onTimelineStateChanged(oldState, newState, durationFraction, timelinePosition);
                        }
                    });
                } else {
                    timeline.callback.onTimelineStateChanged(oldState, newState, durationFraction, timelinePosition);
                }
            }
        };
        this.callbackQueue.offer(callbackRunnable);
        this.checkCallbackThread();
    }

    private void callbackCallTimelinePulse(final Timeline timeline) {
        final float durationFraction = timeline.durationFraction;
        final float timelinePosition = timeline.timelinePosition;
        Runnable callbackRunnable = new Runnable(){

            @Override
            public void run() {
                boolean shouldRunOnUIThread = false;
                for (Class<?> clazz = timeline.callback.getClass(); clazz != null && !shouldRunOnUIThread; clazz = clazz.getSuperclass()) {
                    shouldRunOnUIThread = clazz.isAnnotationPresent(RunOnUIThread.class);
                }
                if (shouldRunOnUIThread && timeline.uiToolkitHandler != null) {
                    timeline.uiToolkitHandler.runOnUIThread(timeline.mainObject, new Runnable(){

                        @Override
                        public void run() {
                            timeline.callback.onTimelinePulse(durationFraction, timelinePosition);
                        }
                    });
                } else {
                    timeline.callback.onTimelinePulse(durationFraction, timelinePosition);
                }
            }
        };
        this.callbackQueue.offer(callbackRunnable);
        this.checkCallbackThread();
    }

    private void callbackCallTimelineScenarioEnded(final TimelineScenario timelineScenario) {
        Runnable callbackRunnable = new Runnable(){

            @Override
            public void run() {
                timelineScenario.callback.onTimelineScenarioDone();
            }
        };
        this.callbackQueue.offer(callbackRunnable);
        this.checkCallbackThread();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Timeline getRunningTimeline(Timeline timeline) {
        Object object = LOCK;
        synchronized (object) {
            if (this.runningTimelines.contains(timeline)) {
                return timeline;
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void addTimeline(Timeline timeline) {
        Object object = LOCK;
        synchronized (object) {
            timeline.fullObjectID = new FullObjectID(timeline.mainObject, timeline.secondaryId);
            this.runningTimelines.add(timeline);
            this.checkAnimatorThread();
            if (DEBUG_MODE) {
                System.out.println("Added (" + timeline.id + ") on " + timeline.fullObjectID + "]. Fade " + timeline.getState().name() + ". Callback - " + (timeline.callback == null ? "no" : "yes"));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void play(Timeline timeline, boolean reset, long msToSkip) {
        Object object = LOCK;
        synchronized (object) {
            this.getAnimatorThread();
            Timeline existing = this.getRunningTimeline(timeline);
            if (existing == null) {
                Timeline.TimelineState oldState = timeline.getState();
                timeline.timeUntilPlay = timeline.initialDelay - msToSkip;
                if (timeline.timeUntilPlay < 0L) {
                    timeline.durationFraction = (float)(-timeline.timeUntilPlay) / (float)timeline.duration;
                    timeline.timelinePosition = timeline.ease.map(timeline.durationFraction);
                    timeline.timeUntilPlay = 0L;
                } else {
                    timeline.durationFraction = 0.0f;
                    timeline.timelinePosition = 0.0f;
                }
                timeline.pushState(Timeline.TimelineState.PLAYING_FORWARD);
                timeline.pushState(Timeline.TimelineState.READY);
                this.addTimeline(timeline);
                this.callbackCallTimelineStateChanged(timeline, oldState);
            } else {
                Timeline.TimelineState oldState = existing.getState();
                if (oldState == Timeline.TimelineState.READY) {
                    existing.popState();
                    existing.replaceState(Timeline.TimelineState.PLAYING_FORWARD);
                    existing.pushState(Timeline.TimelineState.READY);
                } else {
                    existing.replaceState(Timeline.TimelineState.PLAYING_FORWARD);
                    if (oldState != existing.getState()) {
                        this.callbackCallTimelineStateChanged(timeline, oldState);
                    }
                }
                if (reset) {
                    existing.durationFraction = 0.0f;
                    existing.timelinePosition = 0.0f;
                    this.callbackCallTimelinePulse(existing);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void playScenario(TimelineScenario scenario) {
        Object object = LOCK;
        synchronized (object) {
            this.getAnimatorThread();
            Set<TimelineScenario.TimelineScenarioActor> readyActors = scenario.getReadyActors();
            this.runningScenarios.add(scenario);
            this.checkAnimatorThread();
            for (TimelineScenario.TimelineScenarioActor readyActor : readyActors) {
                readyActor.play();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void playReverse(Timeline timeline, boolean reset, long msToSkip) {
        Object object = LOCK;
        synchronized (object) {
            this.getAnimatorThread();
            if (timeline.isLooping) {
                throw new IllegalArgumentException("Timeline must not be marked as looping");
            }
            Timeline existing = this.getRunningTimeline(timeline);
            if (existing == null) {
                Timeline.TimelineState oldState = timeline.getState();
                timeline.timeUntilPlay = timeline.initialDelay - msToSkip;
                if (timeline.timeUntilPlay < 0L) {
                    timeline.durationFraction = 1.0f - (float)(-timeline.timeUntilPlay) / (float)timeline.duration;
                    timeline.timelinePosition = timeline.ease.map(timeline.durationFraction);
                    timeline.timeUntilPlay = 0L;
                } else {
                    timeline.durationFraction = 1.0f;
                    timeline.timelinePosition = 1.0f;
                }
                timeline.pushState(Timeline.TimelineState.PLAYING_REVERSE);
                timeline.pushState(Timeline.TimelineState.READY);
                this.addTimeline(timeline);
                this.callbackCallTimelineStateChanged(timeline, oldState);
            } else {
                Timeline.TimelineState oldState = existing.getState();
                if (oldState == Timeline.TimelineState.READY) {
                    existing.popState();
                    existing.replaceState(Timeline.TimelineState.PLAYING_REVERSE);
                    existing.pushState(Timeline.TimelineState.READY);
                } else {
                    existing.replaceState(Timeline.TimelineState.PLAYING_REVERSE);
                    if (oldState != existing.getState()) {
                        this.callbackCallTimelineStateChanged(timeline, oldState);
                    }
                }
                if (reset) {
                    existing.durationFraction = 1.0f;
                    existing.timelinePosition = 1.0f;
                    this.callbackCallTimelinePulse(existing);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void playLoop(Timeline timeline, long msToSkip) {
        Object object = LOCK;
        synchronized (object) {
            this.getAnimatorThread();
            if (!timeline.isLooping) {
                throw new IllegalArgumentException("Timeline must be marked as looping");
            }
            Timeline existing = this.getRunningTimeline(timeline);
            if (existing == null) {
                Timeline.TimelineState oldState = timeline.getState();
                timeline.timeUntilPlay = timeline.initialDelay - msToSkip;
                if (timeline.timeUntilPlay < 0L) {
                    timeline.durationFraction = (float)(-timeline.timeUntilPlay) / (float)timeline.duration;
                    timeline.timelinePosition = timeline.ease.map(timeline.durationFraction);
                    timeline.timeUntilPlay = 0L;
                } else {
                    timeline.durationFraction = 0.0f;
                    timeline.timelinePosition = 0.0f;
                }
                timeline.pushState(Timeline.TimelineState.PLAYING_FORWARD);
                timeline.pushState(Timeline.TimelineState.READY);
                timeline.toCancelAtCycleBreak = false;
                this.addTimeline(timeline);
                this.callbackCallTimelineStateChanged(timeline, oldState);
            } else {
                existing.toCancelAtCycleBreak = false;
                existing.repeatCount = timeline.repeatCount;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void cancelAllTimelines() {
        Object object = LOCK;
        synchronized (object) {
            this.getAnimatorThread();
            for (Timeline timeline : this.runningTimelines) {
                Timeline.TimelineState oldState = timeline.getState();
                while (timeline.getState() != Timeline.TimelineState.IDLE) {
                    timeline.popState();
                }
                timeline.pushState(Timeline.TimelineState.CANCELLED);
                this.callbackCallTimelineStateChanged(timeline, oldState);
                timeline.popState();
                this.callbackCallTimelineStateChanged(timeline, Timeline.TimelineState.CANCELLED);
            }
            this.runningTimelines.clear();
            this.runningScenarios.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private TridentAnimationThread getAnimatorThread() {
        Object object = this.threadSemaphore;
        synchronized (object) {
            if (this.animatorThread == null) {
                this.animatorThread = new TridentAnimationThread();
                this.animatorThread.start();
            }
            return this.animatorThread;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private TimelineCallbackThread getCallbackThread() {
        Object object = this.threadSemaphore;
        synchronized (object) {
            if (this.callbackThread == null) {
                this.callbackThread = new TimelineCallbackThread();
                this.callbackThread.start();
            }
            return this.callbackThread;
        }
    }

    private void cancelTimeline(Timeline timeline) {
        this.getAnimatorThread();
        if (this.runningTimelines.contains(timeline)) {
            this.runningTimelines.remove(timeline);
            Timeline.TimelineState oldState = timeline.getState();
            while (timeline.getState() != Timeline.TimelineState.IDLE) {
                timeline.popState();
            }
            timeline.pushState(Timeline.TimelineState.CANCELLED);
            this.callbackCallTimelineStateChanged(timeline, oldState);
            timeline.popState();
            this.callbackCallTimelineStateChanged(timeline, Timeline.TimelineState.CANCELLED);
        }
    }

    private void endTimeline(Timeline timeline) {
        this.getAnimatorThread();
        if (this.runningTimelines.contains(timeline)) {
            this.runningTimelines.remove(timeline);
            Timeline.TimelineState oldState = timeline.getState();
            float endPosition = timeline.timelinePosition;
            while (timeline.getState() != Timeline.TimelineState.IDLE) {
                Timeline.TimelineState state = timeline.popState();
                if (state == Timeline.TimelineState.PLAYING_FORWARD) {
                    endPosition = 1.0f;
                }
                if (state != Timeline.TimelineState.PLAYING_REVERSE) continue;
                endPosition = 0.0f;
            }
            timeline.durationFraction = endPosition;
            timeline.timelinePosition = endPosition;
            timeline.pushState(Timeline.TimelineState.DONE);
            this.callbackCallTimelineStateChanged(timeline, oldState);
            timeline.popState();
            this.callbackCallTimelineStateChanged(timeline, Timeline.TimelineState.DONE);
        }
    }

    private void abortTimeline(Timeline timeline) {
        this.getAnimatorThread();
        if (this.runningTimelines.contains(timeline)) {
            this.runningTimelines.remove(timeline);
            while (timeline.getState() != Timeline.TimelineState.IDLE) {
                timeline.popState();
            }
        }
    }

    private void suspendTimeline(Timeline timeline) {
        this.getAnimatorThread();
        if (this.runningTimelines.contains(timeline)) {
            Timeline.TimelineState oldState = timeline.getState();
            if (oldState != Timeline.TimelineState.PLAYING_FORWARD && oldState != Timeline.TimelineState.PLAYING_REVERSE && oldState != Timeline.TimelineState.READY) {
                return;
            }
            timeline.pushState(Timeline.TimelineState.SUSPENDED);
            this.callbackCallTimelineStateChanged(timeline, oldState);
        }
    }

    private void resumeTimeline(Timeline timeline) {
        this.getAnimatorThread();
        if (this.runningTimelines.contains(timeline)) {
            Timeline.TimelineState oldState = timeline.getState();
            if (oldState != Timeline.TimelineState.SUSPENDED) {
                return;
            }
            timeline.popState();
            this.callbackCallTimelineStateChanged(timeline, oldState);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void runTimelineOperation(Timeline timeline, TimelineOperationKind operationKind, Runnable operationRunnable) {
        Object object = LOCK;
        synchronized (object) {
            this.getAnimatorThread();
            switch (operationKind) {
                case CANCEL: {
                    this.cancelTimeline(timeline);
                    return;
                }
                case END: {
                    this.endTimeline(timeline);
                    return;
                }
                case RESUME: {
                    this.resumeTimeline(timeline);
                    return;
                }
                case SUSPEND: {
                    this.suspendTimeline(timeline);
                    return;
                }
                case ABORT: {
                    this.abortTimeline(timeline);
                    return;
                }
            }
            operationRunnable.run();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void runTimelineScenario(TimelineScenario timelineScenario, Runnable timelineScenarioRunnable) {
        Object object = LOCK;
        synchronized (object) {
            this.getAnimatorThread();
            timelineScenarioRunnable.run();
        }
    }

    static {
        LOCK = new Object();
    }

    private class TimelineCallbackThread
    extends Thread {
        public TimelineCallbackThread() {
            this.setName("Trident callback thread");
            this.setDaemon(true);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            try {
                try {
                    while (true) {
                        Runnable runnable;
                        if ((runnable = (Runnable)TimelineEngine.this.callbackQueue.poll(30L, TimeUnit.SECONDS)) != null) {
                            TimelineEngine.this.callbackWasIdle = false;
                            runnable.run();
                            continue;
                        }
                        if (TimelineEngine.this.callbackWasIdle) {
                            break;
                        }
                        TimelineEngine.this.callbackWasIdle = true;
                    }
                }
                catch (Error e2) {
                    e2.printStackTrace();
                    throw e2;
                }
                catch (RuntimeException re) {
                    re.printStackTrace();
                    throw re;
                }
                catch (InterruptedException ie) {
                    // empty catch block
                }
            }
            finally {
                Object object = TimelineEngine.this.threadSemaphore;
                synchronized (object) {
                    TimelineEngine.this.callbackThread = null;
                    TimelineEngine.this.checkCallbackThread();
                }
            }
        }
    }

    class TridentAnimationThread
    extends Thread {
        public TridentAnimationThread() {
            this.setName("Trident pulse source thread");
            this.setDaemon(true);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public final void run() {
            try {
                TridentConfig.PulseSource pulseSource = TridentConfig.getInstance().getPulseSource();
                TimelineEngine.this.lastIterationTimeStamp = System.currentTimeMillis();
                while (!TimelineEngine.this.isTimelinesEmpty() || TimelineEngine.this.lastIterationTimeStamp < TimelineEngine.this.scheduledPulseShutdown) {
                    pulseSource.waitUntilNextPulse();
                    TimelineEngine.this.updateTimelines();
                }
            }
            finally {
                Object object = TimelineEngine.this.threadSemaphore;
                synchronized (object) {
                    TimelineEngine.this.animatorThread = null;
                    TimelineEngine.this.checkAnimatorThread();
                }
            }
        }

        @Override
        public void interrupt() {
            System.err.println("Interrupted");
            super.interrupt();
        }
    }

    static class FullObjectID {
        public Object mainObj;
        public Comparable subID;

        public FullObjectID(Object mainObj, Comparable subID) {
            this.mainObj = mainObj;
            this.subID = subID;
        }

        public int hashCode() {
            int result = this.mainObj.hashCode();
            if (this.subID != null) {
                result &= this.subID.hashCode();
            }
            return result;
        }

        public boolean equals(Object obj) {
            if (obj instanceof FullObjectID) {
                FullObjectID cid = (FullObjectID)obj;
                try {
                    boolean result;
                    boolean bl = result = this.mainObj == cid.mainObj;
                    result = this.subID == null ? result && cid.subID == null : result && this.subID.compareTo(cid.subID) == 0;
                    return result;
                }
                catch (Exception exc) {
                    return false;
                }
            }
            return false;
        }

        public String toString() {
            return this.mainObj.getClass().getSimpleName() + ":" + this.subID;
        }
    }

    class TimelineOperation {
        public TimelineOperationKind operationKind;
        Runnable operationRunnable;

        public TimelineOperation(TimelineOperationKind operationKind, Runnable operationRunnable) {
            this.operationKind = operationKind;
            this.operationRunnable = operationRunnable;
        }
    }

    static enum TimelineOperationKind {
        PLAY,
        CANCEL,
        RESUME,
        SUSPEND,
        ABORT,
        END;

    }
}

