/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.AnimationAccessor;
import com.sun.scenario.animation.shared.ClipEnvelope;
import javafx.animation.Animation;
import javafx.util.Duration;

public class FiniteClipEnvelope
extends ClipEnvelope {
    private boolean autoReverse;
    private int cycleCount;
    private long totalTicks;
    private long pos;

    protected FiniteClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            this.autoReverse = animation.isAutoReverse();
            this.cycleCount = animation.getCycleCount();
        }
        this.updateTotalTicks();
    }

    @Override
    public void setAutoReverse(boolean bl) {
        this.autoReverse = bl;
    }

    @Override
    protected double calculateCurrentRate() {
        return !this.autoReverse ? this.rate : (this.ticks % (2L * this.cycleTicks) < this.cycleTicks == this.rate > 0.0 ? this.rate : -this.rate);
    }

    @Override
    public ClipEnvelope setCycleDuration(Duration duration) {
        if (duration.isIndefinite()) {
            return FiniteClipEnvelope.create(this.animation);
        }
        this.updateCycleTicks(duration);
        this.updateTotalTicks();
        return this;
    }

    @Override
    public ClipEnvelope setCycleCount(int n2) {
        if (n2 == 1 || n2 == -1) {
            return FiniteClipEnvelope.create(this.animation);
        }
        this.cycleCount = n2;
        this.updateTotalTicks();
        return this;
    }

    @Override
    public void setRate(double d2) {
        boolean bl = d2 * this.rate < 0.0;
        long l2 = bl ? this.totalTicks - this.ticks : this.ticks;
        Animation.Status status = this.animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            if (status == Animation.Status.RUNNING) {
                this.setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12 ? d2 : -d2);
            }
            this.deltaTicks = l2 - Math.round((double)(this.ticks - this.deltaTicks) * Math.abs(d2 / this.rate));
            this.abortCurrentPulse();
        }
        this.ticks = l2;
        this.rate = d2;
    }

    private void updateTotalTicks() {
        this.totalTicks = (long)this.cycleCount * this.cycleTicks;
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
            this.ticks = ClipEnvelope.checkBounds(this.deltaTicks + Math.round((double)l2 * Math.abs(this.rate)), this.totalTicks);
            boolean bl = this.ticks >= this.totalTicks;
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
                if (!bl || l5 > 0L) {
                    if (this.autoReverse) {
                        this.setCurrentRate(-this.currentRate);
                    } else {
                        this.pos = this.currentRate > 0.0 ? 0L : this.cycleTicks;
                        AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
                    }
                }
                l3 = this.cycleTicks;
            }
            if (l5 > 0L && !bl) {
                this.pos += this.currentRate > 0.0 ? l5 : -l5;
                AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
            }
            if (bl && !this.aborted) {
                AnimationAccessor.getDefault().finished(this.animation);
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
        if (this.rate < 0.0) {
            l2 = this.totalTicks - l2;
        }
        this.ticks = ClipEnvelope.checkBounds(l2, this.totalTicks);
        long l4 = this.ticks - l3;
        if (l4 != 0L) {
            this.deltaTicks += l4;
            if (this.autoReverse) {
                boolean bl = this.ticks % (2L * this.cycleTicks) < this.cycleTicks;
                if (bl == this.rate > 0.0) {
                    this.pos = this.ticks % this.cycleTicks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(Math.abs(this.rate));
                    }
                } else {
                    this.pos = this.cycleTicks - this.ticks % this.cycleTicks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(-Math.abs(this.rate));
                    }
                }
            } else {
                this.pos = this.ticks % this.cycleTicks;
                if (this.rate < 0.0) {
                    this.pos = this.cycleTicks - this.pos;
                }
                if (this.pos == 0L && this.ticks > 0L) {
                    this.pos = this.cycleTicks;
                }
            }
            AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
            this.abortCurrentPulse();
        }
    }
}

