/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation.shared;

import com.sun.javafx.animation.TickCalculation;
import com.sun.scenario.animation.shared.AnimationAccessor;
import com.sun.scenario.animation.shared.FiniteClipEnvelope;
import com.sun.scenario.animation.shared.InfiniteClipEnvelope;
import com.sun.scenario.animation.shared.SingleLoopClipEnvelope;
import javafx.animation.Animation;
import javafx.util.Duration;

public abstract class ClipEnvelope {
    protected static final long INDEFINITE = Long.MAX_VALUE;
    protected static final double EPSILON = 1.0E-12;
    protected Animation animation;
    protected double rate = 1.0;
    protected long cycleTicks = 0L;
    protected long deltaTicks = 0L;
    protected long ticks = 0L;
    protected double currentRate = this.rate;
    protected boolean inTimePulse = false;
    protected boolean aborted = false;

    protected ClipEnvelope(Animation animation) {
        this.animation = animation;
        if (animation != null) {
            Duration duration = animation.getCycleDuration();
            this.cycleTicks = TickCalculation.fromDuration(duration);
            this.rate = animation.getRate();
        }
    }

    public static ClipEnvelope create(Animation animation) {
        if (animation.getCycleCount() == 1 || animation.getCycleDuration().isIndefinite()) {
            return new SingleLoopClipEnvelope(animation);
        }
        if (animation.getCycleCount() == -1) {
            return new InfiniteClipEnvelope(animation);
        }
        return new FiniteClipEnvelope(animation);
    }

    public abstract ClipEnvelope setCycleDuration(Duration var1);

    public abstract void setRate(double var1);

    public abstract void setAutoReverse(boolean var1);

    public abstract ClipEnvelope setCycleCount(int var1);

    protected void updateCycleTicks(Duration duration) {
        this.cycleTicks = TickCalculation.fromDuration(duration);
    }

    public boolean wasSynched() {
        return this.cycleTicks != 0L;
    }

    public void start() {
        this.setCurrentRate(this.calculateCurrentRate());
        this.deltaTicks = this.ticks;
    }

    public abstract void timePulse(long var1);

    public abstract void jumpTo(long var1);

    public void abortCurrentPulse() {
        if (this.inTimePulse) {
            this.aborted = true;
            this.inTimePulse = false;
        }
    }

    protected abstract double calculateCurrentRate();

    protected void setCurrentRate(double d2) {
        this.currentRate = d2;
        AnimationAccessor.getDefault().setCurrentRate(this.animation, d2);
    }

    protected static long checkBounds(long l2, long l3) {
        return Math.max(0L, Math.min(l2, l3));
    }

    public double getCurrentRate() {
        return this.currentRate;
    }
}

