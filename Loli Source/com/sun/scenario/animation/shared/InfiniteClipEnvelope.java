/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.AnimationAccessor;
import com.sun.scenario.animation.shared.ClipEnvelope;
import javafx.animation.Animation;
import javafx.util.Duration;

public class InfiniteClipEnvelope
extends ClipEnvelope {
    private boolean autoReverse;
    private long pos;

    protected InfiniteClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            this.autoReverse = animation.isAutoReverse();
        }
    }

    @Override
    public void setAutoReverse(boolean bl) {
        this.autoReverse = bl;
    }

    @Override
    protected double calculateCurrentRate() {
        return !this.autoReverse ? this.rate : (this.ticks % (2L * this.cycleTicks) < this.cycleTicks ? this.rate : -this.rate);
    }

    @Override
    public ClipEnvelope setCycleDuration(Duration duration) {
        if (duration.isIndefinite()) {
            return InfiniteClipEnvelope.create(this.animation);
        }
        this.updateCycleTicks(duration);
        return this;
    }

    @Override
    public ClipEnvelope setCycleCount(int n2) {
        return n2 != -1 ? InfiniteClipEnvelope.create(this.animation) : this;
    }

    @Override
    public void setRate(double d2) {
        Animation.Status status = this.animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            if (status == Animation.Status.RUNNING) {
                this.setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12 ? d2 : -d2);
            }
            this.deltaTicks = this.ticks - Math.round((double)(this.ticks - this.deltaTicks) * Math.abs(d2 / this.rate));
            if (d2 * this.rate < 0.0) {
                long l2 = 2L * this.cycleTicks - this.pos;
                this.deltaTicks += l2;
                this.ticks += l2;
            }
            this.abortCurrentPulse();
        }
        this.rate = d2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void timePulse(long l2) {
        if (this.cycleTicks == 0L) {
            return;
        }
        this.aborted = false;
        this.inTimePulse = true;
        try {
            long l3;
            long l4 = this.ticks;
            this.ticks = Math.max(0L, this.deltaTicks + Math.round((double)l2 * Math.abs(this.rate)));
            long l5 = this.ticks - l4;
            if (l5 == 0L) {
                return;
            }
            long l6 = l3 = this.currentRate > 0.0 ? this.cycleTicks - this.pos : this.pos;
            while (l5 >= l3) {
                if (l3 > 0L) {
                    this.pos = this.currentRate > 0.0 ? this.cycleTicks : 0L;
                    l5 -= l3;
                    AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
                    if (this.aborted) {
                        return;
                    }
                }
                if (this.autoReverse) {
                    this.setCurrentRate(-this.currentRate);
                } else {
                    this.pos = this.currentRate > 0.0 ? 0L : this.cycleTicks;
                    AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
                }
                l3 = this.cycleTicks;
            }
            if (l5 > 0L) {
                this.pos += this.currentRate > 0.0 ? l5 : -l5;
                AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
            }
        }
        finally {
            this.inTimePulse = false;
        }
    }

    @Override
    public void jumpTo(long l2) {
        if (this.cycleTicks == 0L) {
            return;
        }
        long l3 = this.ticks;
        this.ticks = Math.max(0L, l2) % (2L * this.cycleTicks);
        long l4 = this.ticks - l3;
        if (l4 != 0L) {
            this.deltaTicks += l4;
            if (this.autoReverse) {
                if (this.ticks > this.cycleTicks) {
                    this.pos = 2L * this.cycleTicks - this.ticks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(-this.rate);
                    }
                } else {
                    this.pos = this.ticks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(this.rate);
                    }
                }
            } else {
                this.pos = this.ticks % this.cycleTicks;
                if (this.pos == 0L) {
                    this.pos = this.ticks;
                }
            }
            AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
            this.abortCurrentPulse();
        }
    }
}

