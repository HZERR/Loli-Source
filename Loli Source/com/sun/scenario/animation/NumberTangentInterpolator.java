/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation;

import com.sun.javafx.animation.TickCalculation;
import javafx.animation.Interpolator;
import javafx.util.Duration;

public class NumberTangentInterpolator
extends Interpolator {
    private final double inValue;
    private final double outValue;
    private final long inTicks;
    private final long outTicks;

    public double getInValue() {
        return this.inValue;
    }

    public double getOutValue() {
        return this.outValue;
    }

    public double getInTicks() {
        return this.inTicks;
    }

    public double getOutTicks() {
        return this.outTicks;
    }

    public NumberTangentInterpolator(Duration duration, double d2, Duration duration2, double d3) {
        this.inTicks = TickCalculation.fromDuration(duration);
        this.inValue = d2;
        this.outTicks = TickCalculation.fromDuration(duration2);
        this.outValue = d3;
    }

    public NumberTangentInterpolator(Duration duration, double d2) {
        this.outTicks = this.inTicks = TickCalculation.fromDuration(duration);
        this.inValue = this.outValue = d2;
    }

    public String toString() {
        return "NumberTangentInterpolator [inValue=" + this.inValue + ", inDuration=" + TickCalculation.toDuration(this.inTicks) + ", outValue=" + this.outValue + ", outDuration=" + TickCalculation.toDuration(this.outTicks) + "]";
    }

    public int hashCode() {
        int n2 = 7;
        n2 = 59 * n2 + (int)(Double.doubleToLongBits(this.inValue) ^ Double.doubleToLongBits(this.inValue) >>> 32);
        n2 = 59 * n2 + (int)(Double.doubleToLongBits(this.outValue) ^ Double.doubleToLongBits(this.outValue) >>> 32);
        n2 = 59 * n2 + (int)(this.inTicks ^ this.inTicks >>> 32);
        n2 = 59 * n2 + (int)(this.outTicks ^ this.outTicks >>> 32);
        return n2;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        NumberTangentInterpolator numberTangentInterpolator = (NumberTangentInterpolator)object;
        if (Double.doubleToLongBits(this.inValue) != Double.doubleToLongBits(numberTangentInterpolator.inValue)) {
            return false;
        }
        if (Double.doubleToLongBits(this.outValue) != Double.doubleToLongBits(numberTangentInterpolator.outValue)) {
            return false;
        }
        if (this.inTicks != numberTangentInterpolator.inTicks) {
            return false;
        }
        return this.outTicks == numberTangentInterpolator.outTicks;
    }

    @Override
    protected double curve(double d2) {
        return d2;
    }
}

