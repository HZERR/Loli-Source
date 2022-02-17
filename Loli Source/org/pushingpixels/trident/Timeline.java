/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.pushingpixels.trident.TimelineEngine;
import org.pushingpixels.trident.TimelinePropertyBuilder;
import org.pushingpixels.trident.TimelineScenario;
import org.pushingpixels.trident.TridentConfig;
import org.pushingpixels.trident.UIToolkitHandler;
import org.pushingpixels.trident.callback.RunOnUIThread;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;
import org.pushingpixels.trident.ease.Linear;
import org.pushingpixels.trident.ease.TimelineEase;
import org.pushingpixels.trident.interpolator.KeyFrames;

public class Timeline
implements TimelineScenario.TimelineScenarioActor {
    Object mainObject;
    Comparable<?> secondaryId;
    TimelineEngine.FullObjectID fullObjectID;
    long duration;
    long initialDelay;
    long cycleDelay;
    boolean isLooping;
    int repeatCount;
    RepeatBehavior repeatBehavior;
    UIToolkitHandler uiToolkitHandler;
    Chain callback;
    String name;
    List<TimelinePropertyBuilder.AbstractFieldInfo> propertiesToInterpolate;
    static long counter;
    protected long id;
    float durationFraction;
    float timelinePosition;
    long timeUntilPlay;
    boolean toCancelAtCycleBreak;
    Stack<TimelineState> stateStack;
    TimelineEase ease;
    private int doneCount;

    public Timeline() {
        this(null);
    }

    public Timeline(Object mainTimelineObject) {
        this.mainObject = mainTimelineObject;
        for (UIToolkitHandler uiToolkitHandler : TridentConfig.getInstance().getUIToolkitHandlers()) {
            if (!uiToolkitHandler.isHandlerFor(mainTimelineObject)) continue;
            this.uiToolkitHandler = uiToolkitHandler;
            break;
        }
        Setter setterCallback = this.uiToolkitHandler != null ? new UISetter() : new Setter();
        this.callback = new Chain(setterCallback);
        this.duration = 500L;
        this.propertiesToInterpolate = new ArrayList<TimelinePropertyBuilder.AbstractFieldInfo>();
        this.id = Timeline.getId();
        this.stateStack = new Stack();
        this.stateStack.push(TimelineState.IDLE);
        this.doneCount = 0;
        this.ease = new Linear();
    }

    public final void setSecondaryID(Comparable<?> secondaryId) {
        if (this.getState() != TimelineState.IDLE) {
            throw new IllegalArgumentException("Cannot change state of non-idle timeline [" + this.toString() + "]");
        }
        this.secondaryId = secondaryId;
    }

    public final void setDuration(long durationMs) {
        if (this.getState() != TimelineState.IDLE) {
            throw new IllegalArgumentException("Cannot change state of non-idle timeline [" + this.toString() + "]");
        }
        this.duration = durationMs;
    }

    public final void setInitialDelay(long initialDelay) {
        if (this.getState() != TimelineState.IDLE) {
            throw new IllegalArgumentException("Cannot change state of non-idle timeline [" + this.toString() + "]");
        }
        this.initialDelay = initialDelay;
    }

    public final void setCycleDelay(long cycleDelay) {
        if (this.getState() != TimelineState.IDLE) {
            throw new IllegalArgumentException("Cannot change state of non-idle timeline [" + this.toString() + "]");
        }
        this.cycleDelay = cycleDelay;
    }

    public final void addCallback(TimelineCallback callback) {
        if (this.getState() != TimelineState.IDLE) {
            throw new IllegalArgumentException("Cannot change state of non-idle timeline [" + this.toString() + "]");
        }
        this.callback.addCallback(callback);
    }

    public final void removeCallback(TimelineCallback callback) {
        if (this.getState() != TimelineState.IDLE) {
            throw new IllegalArgumentException("Cannot change state of non-idle timeline [" + this.toString() + "]");
        }
        this.callback.removeCallback(callback);
    }

    public static <T> TimelinePropertyBuilder<T> property(String propertyName) {
        return new TimelinePropertyBuilder(propertyName);
    }

    public final <T> void addPropertyToInterpolate(TimelinePropertyBuilder<T> propertyBuilder) {
        this.propertiesToInterpolate.add(propertyBuilder.getFieldInfo(this));
    }

    public final <T> void addPropertyToInterpolate(String propName, KeyFrames<T> keyFrames) {
        this.addPropertyToInterpolate(Timeline.property(propName).goingThrough(keyFrames));
    }

    public final <T> void addPropertyToInterpolate(String propName, T from, T to) {
        this.addPropertyToInterpolate(Timeline.property(propName).from(from).to(to));
    }

    @Override
    public void play() {
        this.playSkipping(0L);
    }

    public void playSkipping(final long msToSkip) {
        if (this.initialDelay + this.duration < msToSkip) {
            throw new IllegalArgumentException("Required skip longer than initial delay + duration");
        }
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.PLAY, new Runnable(){

            @Override
            public void run() {
                Timeline.this.isLooping = false;
                TimelineEngine.getInstance().play(Timeline.this, false, msToSkip);
            }
        });
    }

    public void playReverse() {
        this.playReverseSkipping(0L);
    }

    public void playReverseSkipping(final long msToSkip) {
        if (this.initialDelay + this.duration < msToSkip) {
            throw new IllegalArgumentException("Required skip longer than initial delay + duration");
        }
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.PLAY, new Runnable(){

            @Override
            public void run() {
                Timeline.this.isLooping = false;
                TimelineEngine.getInstance().playReverse(Timeline.this, false, msToSkip);
            }
        });
    }

    public void replay() {
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.PLAY, new Runnable(){

            @Override
            public void run() {
                Timeline.this.isLooping = false;
                TimelineEngine.getInstance().play(Timeline.this, true, 0L);
            }
        });
    }

    public void replayReverse() {
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.PLAY, new Runnable(){

            @Override
            public void run() {
                Timeline.this.isLooping = false;
                TimelineEngine.getInstance().playReverse(Timeline.this, true, 0L);
            }
        });
    }

    public void playLoop(RepeatBehavior repeatBehavior) {
        this.playLoop(-1, repeatBehavior);
    }

    public void playLoopSkipping(RepeatBehavior repeatBehavior, long msToSkip) {
        this.playLoopSkipping(-1, repeatBehavior, msToSkip);
    }

    public void playLoop(int loopCount, RepeatBehavior repeatBehavior) {
        this.playLoopSkipping(loopCount, repeatBehavior, 0L);
    }

    public void playLoopSkipping(final int loopCount, final RepeatBehavior repeatBehavior, final long msToSkip) {
        if (this.initialDelay + this.duration < msToSkip) {
            throw new IllegalArgumentException("Required skip longer than initial delay + duration");
        }
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.PLAY, new Runnable(){

            @Override
            public void run() {
                Timeline.this.isLooping = true;
                Timeline.this.repeatCount = loopCount;
                Timeline.this.repeatBehavior = repeatBehavior;
                TimelineEngine.getInstance().playLoop(Timeline.this, msToSkip);
            }
        });
    }

    public void cancel() {
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.CANCEL, null);
    }

    public void end() {
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.END, null);
    }

    public void abort() {
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.ABORT, null);
    }

    public void suspend() {
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.SUSPEND, null);
    }

    public void resume() {
        TimelineEngine.getInstance().runTimelineOperation(this, TimelineEngine.TimelineOperationKind.RESUME, null);
    }

    public void cancelAtCycleBreak() {
        if (!this.isLooping) {
            throw new IllegalArgumentException("Can only be called on looping timelines");
        }
        this.toCancelAtCycleBreak = true;
    }

    protected static synchronized long getId() {
        return counter++;
    }

    public final float getTimelinePosition() {
        return this.timelinePosition;
    }

    public final float getDurationFraction() {
        return this.durationFraction;
    }

    public final TimelineState getState() {
        return this.stateStack.peek();
    }

    public final void setEase(TimelineEase ease) {
        if (this.getState() != TimelineState.IDLE) {
            throw new IllegalArgumentException("Cannot change state of non-idle timeline");
        }
        this.ease = ease;
    }

    @Override
    public boolean isDone() {
        return this.doneCount > 0;
    }

    @Override
    public boolean supportsReplay() {
        return true;
    }

    @Override
    public void resetDoneFlag() {
        this.doneCount = 0;
    }

    public String toString() {
        StringBuffer res = new StringBuffer();
        if (this.name != null) {
            res.append(this.name);
        }
        if (this.mainObject != null) {
            res.append(":" + this.mainObject.getClass().getName());
        }
        if (this.secondaryId != null) {
            res.append(":" + this.secondaryId.toString());
        }
        res.append(" " + this.getState().name());
        res.append(":" + this.timelinePosition);
        return res.toString();
    }

    void replaceState(TimelineState state) {
        this.stateStack.pop();
        this.pushState(state);
    }

    void pushState(TimelineState state) {
        if (state == TimelineState.DONE) {
            ++this.doneCount;
        }
        this.stateStack.add(state);
    }

    TimelineState popState() {
        return this.stateStack.pop();
    }

    public final long getDuration() {
        return this.duration;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getMainObject() {
        return this.mainObject;
    }

    class Chain
    implements TimelineCallback {
        private List<TimelineCallback> callbacks = new ArrayList<TimelineCallback>();

        public Chain(TimelineCallback ... callbacks) {
            for (TimelineCallback callback : callbacks) {
                this.callbacks.add(callback);
            }
        }

        public void addCallback(TimelineCallback callback) {
            this.callbacks.add(callback);
        }

        public void removeCallback(TimelineCallback callback) {
            this.callbacks.remove(callback);
        }

        @Override
        public void onTimelineStateChanged(final TimelineState oldState, final TimelineState newState, final float durationFraction, final float timelinePosition) {
            if (Timeline.this.uiToolkitHandler != null && !Timeline.this.uiToolkitHandler.isInReadyState(Timeline.this.mainObject)) {
                return;
            }
            for (int i2 = this.callbacks.size() - 1; i2 >= 0; --i2) {
                final TimelineCallback callback = this.callbacks.get(i2);
                boolean shouldRunOnUIThread = false;
                for (Class<?> clazz = callback.getClass(); clazz != null && !shouldRunOnUIThread; clazz = clazz.getSuperclass()) {
                    shouldRunOnUIThread = clazz.isAnnotationPresent(RunOnUIThread.class);
                }
                if (shouldRunOnUIThread && Timeline.this.uiToolkitHandler != null) {
                    Timeline.this.uiToolkitHandler.runOnUIThread(Timeline.this.mainObject, new Runnable(){

                        @Override
                        public void run() {
                            callback.onTimelineStateChanged(oldState, newState, durationFraction, timelinePosition);
                        }
                    });
                    continue;
                }
                callback.onTimelineStateChanged(oldState, newState, durationFraction, timelinePosition);
            }
        }

        @Override
        public void onTimelinePulse(final float durationFraction, final float timelinePosition) {
            if (Timeline.this.uiToolkitHandler != null && !Timeline.this.uiToolkitHandler.isInReadyState(Timeline.this.mainObject)) {
                return;
            }
            for (int i2 = this.callbacks.size() - 1; i2 >= 0; --i2) {
                final TimelineCallback callback = this.callbacks.get(i2);
                boolean shouldRunOnUIThread = false;
                for (Class<?> clazz = callback.getClass(); clazz != null && !shouldRunOnUIThread; clazz = clazz.getSuperclass()) {
                    shouldRunOnUIThread = clazz.isAnnotationPresent(RunOnUIThread.class);
                }
                if (shouldRunOnUIThread && Timeline.this.uiToolkitHandler != null) {
                    Timeline.this.uiToolkitHandler.runOnUIThread(Timeline.this.mainObject, new Runnable(){

                        @Override
                        public void run() {
                            if (Timeline.this.getState() == TimelineState.CANCELLED) {
                                return;
                            }
                            callback.onTimelinePulse(durationFraction, timelinePosition);
                        }
                    });
                    continue;
                }
                callback.onTimelinePulse(durationFraction, timelinePosition);
            }
        }
    }

    @RunOnUIThread
    private class UISetter
    extends Setter {
        private UISetter() {
        }
    }

    private class Setter
    extends TimelineCallbackAdapter {
        private Setter() {
        }

        @Override
        public void onTimelineStateChanged(TimelineState oldState, TimelineState newState, float durationFraction, float timelinePosition) {
            if (newState == TimelineState.READY) {
                for (TimelinePropertyBuilder.AbstractFieldInfo fInfo : Timeline.this.propertiesToInterpolate) {
                    if (Timeline.this.uiToolkitHandler != null && !Timeline.this.uiToolkitHandler.isInReadyState(fInfo.object)) continue;
                    fInfo.onStart();
                }
            }
            if (oldState.isActive || newState.isActive) {
                for (TimelinePropertyBuilder.AbstractFieldInfo fInfo : Timeline.this.propertiesToInterpolate) {
                    if (Timeline.this.uiToolkitHandler != null && !Timeline.this.uiToolkitHandler.isInReadyState(fInfo.object)) continue;
                    fInfo.updateFieldValue(timelinePosition);
                }
            }
        }

        @Override
        public void onTimelinePulse(float durationFraction, float timelinePosition) {
            for (TimelinePropertyBuilder.AbstractFieldInfo fInfo : Timeline.this.propertiesToInterpolate) {
                if (Timeline.this.uiToolkitHandler != null && !Timeline.this.uiToolkitHandler.isInReadyState(fInfo.object)) continue;
                fInfo.updateFieldValue(timelinePosition);
            }
        }
    }

    public static enum TimelineState {
        IDLE(false),
        READY(false),
        PLAYING_FORWARD(true),
        PLAYING_REVERSE(true),
        SUSPENDED(false),
        CANCELLED(false),
        DONE(false);

        private boolean isActive;

        private TimelineState(boolean isActive) {
            this.isActive = isActive;
        }
    }

    public static enum RepeatBehavior {
        LOOP,
        REVERSE;

    }
}

