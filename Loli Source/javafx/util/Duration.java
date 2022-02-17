/*
 * Decompiled with CFR 0.150.
 */
package javafx.util;

import java.io.Serializable;
import javafx.beans.NamedArg;

public class Duration
implements Comparable<Duration>,
Serializable {
    public static final Duration ZERO = new Duration(0.0);
    public static final Duration ONE = new Duration(1.0);
    public static final Duration INDEFINITE = new Duration(Double.POSITIVE_INFINITY);
    public static final Duration UNKNOWN = new Duration(Double.NaN);
    private final double millis;

    public static Duration valueOf(String string) {
        int n2 = -1;
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (Character.isDigit(c2) || c2 == '.' || c2 == '-') continue;
            n2 = i2;
            break;
        }
        if (n2 == -1) {
            throw new IllegalArgumentException("The time parameter must have a suffix of [ms|s|m|h]");
        }
        double d2 = Double.parseDouble(string.substring(0, n2));
        String string2 = string.substring(n2);
        if ("ms".equals(string2)) {
            return Duration.millis(d2);
        }
        if ("s".equals(string2)) {
            return Duration.seconds(d2);
        }
        if ("m".equals(string2)) {
            return Duration.minutes(d2);
        }
        if ("h".equals(string2)) {
            return Duration.hours(d2);
        }
        throw new IllegalArgumentException("The time parameter must have a suffix of [ms|s|m|h]");
    }

    public static Duration millis(double d2) {
        if (d2 == 0.0) {
            return ZERO;
        }
        if (d2 == 1.0) {
            return ONE;
        }
        if (d2 == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        }
        if (Double.isNaN(d2)) {
            return UNKNOWN;
        }
        return new Duration(d2);
    }

    public static Duration seconds(double d2) {
        if (d2 == 0.0) {
            return ZERO;
        }
        if (d2 == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        }
        if (Double.isNaN(d2)) {
            return UNKNOWN;
        }
        return new Duration(d2 * 1000.0);
    }

    public static Duration minutes(double d2) {
        if (d2 == 0.0) {
            return ZERO;
        }
        if (d2 == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        }
        if (Double.isNaN(d2)) {
            return UNKNOWN;
        }
        return new Duration(d2 * 60000.0);
    }

    public static Duration hours(double d2) {
        if (d2 == 0.0) {
            return ZERO;
        }
        if (d2 == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        }
        if (Double.isNaN(d2)) {
            return UNKNOWN;
        }
        return new Duration(d2 * 3600000.0);
    }

    public Duration(@NamedArg(value="millis") double d2) {
        this.millis = d2;
    }

    public double toMillis() {
        return this.millis;
    }

    public double toSeconds() {
        return this.millis / 1000.0;
    }

    public double toMinutes() {
        return this.millis / 60000.0;
    }

    public double toHours() {
        return this.millis / 3600000.0;
    }

    public Duration add(Duration duration) {
        return Duration.millis(this.millis + duration.millis);
    }

    public Duration subtract(Duration duration) {
        return Duration.millis(this.millis - duration.millis);
    }

    @Deprecated
    public Duration multiply(Duration duration) {
        return Duration.millis(this.millis * duration.millis);
    }

    public Duration multiply(double d2) {
        return Duration.millis(this.millis * d2);
    }

    public Duration divide(double d2) {
        return Duration.millis(this.millis / d2);
    }

    @Deprecated
    public Duration divide(Duration duration) {
        return Duration.millis(this.millis / duration.millis);
    }

    public Duration negate() {
        return Duration.millis(-this.millis);
    }

    public boolean isIndefinite() {
        return this.millis == Double.POSITIVE_INFINITY;
    }

    public boolean isUnknown() {
        return Double.isNaN(this.millis);
    }

    public boolean lessThan(Duration duration) {
        return this.millis < duration.millis;
    }

    public boolean lessThanOrEqualTo(Duration duration) {
        return this.millis <= duration.millis;
    }

    public boolean greaterThan(Duration duration) {
        return this.millis > duration.millis;
    }

    public boolean greaterThanOrEqualTo(Duration duration) {
        return this.millis >= duration.millis;
    }

    public String toString() {
        return this.isIndefinite() ? "INDEFINITE" : (this.isUnknown() ? "UNKNOWN" : this.millis + " ms");
    }

    @Override
    public int compareTo(Duration duration) {
        return Double.compare(this.millis, duration.millis);
    }

    public boolean equals(Object object) {
        return object == this || object instanceof Duration && this.millis == ((Duration)object).millis;
    }

    public int hashCode() {
        long l2 = Double.doubleToLongBits(this.millis);
        return (int)(l2 ^ l2 >>> 32);
    }
}

