/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.animation;

import javafx.util.Duration;

public class TickCalculation {
    public static final int TICKS_PER_SECOND = 6000;
    private static final double TICKS_PER_MILI = 6.0;
    private static final double TICKS_PER_NANO = 6.0E-6;

    private TickCalculation() {
    }

    public static long add(long l2, long l3) {
        assert (l2 >= 0L);
        if (l2 == Long.MAX_VALUE || l3 == Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        if (l3 == Long.MIN_VALUE) {
            return 0L;
        }
        if (l3 >= 0L) {
            long l4 = l2 + l3;
            return l4 < 0L ? Long.MAX_VALUE : l4;
        }
        return Math.max(0L, l2 + l3);
    }

    public static long sub(long l2, long l3) {
        assert (l2 >= 0L);
        if (l2 == Long.MAX_VALUE || l3 == Long.MIN_VALUE) {
            return Long.MAX_VALUE;
        }
        if (l3 == Long.MAX_VALUE) {
            return 0L;
        }
        if (l3 >= 0L) {
            return Math.max(0L, l2 - l3);
        }
        long l4 = l2 - l3;
        return l4 < 0L ? Long.MAX_VALUE : l4;
    }

    public static long fromMillis(double d2) {
        return Math.round(6.0 * d2);
    }

    public static long fromNano(long l2) {
        return Math.round(6.0E-6 * (double)l2);
    }

    public static long fromDuration(Duration duration) {
        return TickCalculation.fromMillis(duration.toMillis());
    }

    public static long fromDuration(Duration duration, double d2) {
        return Math.round(6.0 * duration.toMillis() / Math.abs(d2));
    }

    public static Duration toDuration(long l2) {
        return Duration.millis(TickCalculation.toMillis(l2));
    }

    public static double toMillis(long l2) {
        return (double)l2 / 6.0;
    }
}

