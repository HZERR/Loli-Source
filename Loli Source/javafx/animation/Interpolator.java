/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import com.sun.scenario.animation.NumberTangentInterpolator;
import com.sun.scenario.animation.SplineInterpolator;
import javafx.animation.Interpolatable;
import javafx.util.Duration;

public abstract class Interpolator {
    private static final double EPSILON = 1.0E-12;
    public static final Interpolator DISCRETE = new Interpolator(){

        @Override
        protected double curve(double d2) {
            return Math.abs(d2 - 1.0) < 1.0E-12 ? 1.0 : 0.0;
        }

        public String toString() {
            return "Interpolator.DISCRETE";
        }
    };
    public static final Interpolator LINEAR = new Interpolator(){

        @Override
        protected double curve(double d2) {
            return d2;
        }

        public String toString() {
            return "Interpolator.LINEAR";
        }
    };
    public static final Interpolator EASE_BOTH = new Interpolator(){

        @Override
        protected double curve(double d2) {
            return Interpolator.clamp(d2 < 0.2 ? 3.125 * d2 * d2 : (d2 > 0.8 ? -3.125 * d2 * d2 + 6.25 * d2 - 2.125 : 1.25 * d2 - 0.125));
        }

        public String toString() {
            return "Interpolator.EASE_BOTH";
        }
    };
    public static final Interpolator EASE_IN = new Interpolator(){
        private static final double S1 = 2.7777777777777777;
        private static final double S3 = 1.1111111111111112;
        private static final double S4 = 0.1111111111111111;

        @Override
        protected double curve(double d2) {
            return Interpolator.clamp(d2 < 0.2 ? 2.7777777777777777 * d2 * d2 : 1.1111111111111112 * d2 - 0.1111111111111111);
        }

        public String toString() {
            return "Interpolator.EASE_IN";
        }
    };
    public static final Interpolator EASE_OUT = new Interpolator(){
        private static final double S1 = -2.7777777777777777;
        private static final double S2 = 5.555555555555555;
        private static final double S3 = -1.7777777777777777;
        private static final double S4 = 1.1111111111111112;

        @Override
        protected double curve(double d2) {
            return Interpolator.clamp(d2 > 0.8 ? -2.7777777777777777 * d2 * d2 + 5.555555555555555 * d2 + -1.7777777777777777 : 1.1111111111111112 * d2);
        }

        public String toString() {
            return "Interpolator.EASE_OUT";
        }
    };

    protected Interpolator() {
    }

    public static Interpolator SPLINE(double d2, double d3, double d4, double d5) {
        return new SplineInterpolator(d2, d3, d4, d5);
    }

    public static Interpolator TANGENT(Duration duration, double d2, Duration duration2, double d3) {
        return new NumberTangentInterpolator(duration, d2, duration2, d3);
    }

    public static Interpolator TANGENT(Duration duration, double d2) {
        return new NumberTangentInterpolator(duration, d2);
    }

    public Object interpolate(Object object, Object object2, double d2) {
        if (object instanceof Number && object2 instanceof Number) {
            double d3 = ((Number)object).doubleValue();
            double d4 = ((Number)object2).doubleValue();
            double d5 = d3 + (d4 - d3) * this.curve(d2);
            if (object instanceof Double || object2 instanceof Double) {
                return d5;
            }
            if (object instanceof Float || object2 instanceof Float) {
                return Float.valueOf((float)d5);
            }
            if (object instanceof Long || object2 instanceof Long) {
                return Math.round(d5);
            }
            return (int)Math.round(d5);
        }
        if (object instanceof Interpolatable && object2 instanceof Interpolatable) {
            return ((Interpolatable)object).interpolate(object2, this.curve(d2));
        }
        return this.curve(d2) == 1.0 ? object2 : object;
    }

    public boolean interpolate(boolean bl, boolean bl2, double d2) {
        return Math.abs(this.curve(d2) - 1.0) < 1.0E-12 ? bl2 : bl;
    }

    public double interpolate(double d2, double d3, double d4) {
        return d2 + (d3 - d2) * this.curve(d4);
    }

    public int interpolate(int n2, int n3, double d2) {
        return n2 + (int)Math.round((double)(n3 - n2) * this.curve(d2));
    }

    public long interpolate(long l2, long l3, double d2) {
        return l2 + Math.round((double)(l3 - l2) * this.curve(d2));
    }

    private static double clamp(double d2) {
        return d2 < 0.0 ? 0.0 : (d2 > 1.0 ? 1.0 : d2);
    }

    protected abstract double curve(double var1);
}

