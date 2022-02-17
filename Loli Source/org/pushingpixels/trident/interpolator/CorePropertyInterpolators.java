/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.interpolator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.pushingpixels.trident.interpolator.PropertyInterpolator;
import org.pushingpixels.trident.interpolator.PropertyInterpolatorSource;

public class CorePropertyInterpolators
implements PropertyInterpolatorSource {
    private Set<PropertyInterpolator> interpolators = new HashSet<PropertyInterpolator>();

    public CorePropertyInterpolators() {
        this.interpolators.add(new IntegerPropertyInterpolator());
        this.interpolators.add(new FloatPropertyInterpolator());
        this.interpolators.add(new DoublePropertyInterpolator());
        this.interpolators.add(new LongPropertyInterpolator());
    }

    @Override
    public Set<PropertyInterpolator> getPropertyInterpolators() {
        return Collections.unmodifiableSet(this.interpolators);
    }

    private static class LongPropertyInterpolator
    implements PropertyInterpolator<Long> {
        private LongPropertyInterpolator() {
        }

        @Override
        public Class getBasePropertyClass() {
            return Long.class;
        }

        @Override
        public Long interpolate(Long from, Long to, float timelinePosition) {
            return (long)((float)from.longValue() + (float)(to - from) * timelinePosition);
        }
    }

    private static class IntegerPropertyInterpolator
    implements PropertyInterpolator<Integer> {
        private IntegerPropertyInterpolator() {
        }

        @Override
        public Class getBasePropertyClass() {
            return Integer.class;
        }

        @Override
        public Integer interpolate(Integer from, Integer to, float timelinePosition) {
            return (int)((float)from.intValue() + (float)(to - from) * timelinePosition);
        }
    }

    private static class DoublePropertyInterpolator
    implements PropertyInterpolator<Double> {
        private DoublePropertyInterpolator() {
        }

        @Override
        public Class getBasePropertyClass() {
            return Double.class;
        }

        @Override
        public Double interpolate(Double from, Double to, float timelinePosition) {
            return from + (to - from) * (double)timelinePosition;
        }
    }

    private static class FloatPropertyInterpolator
    implements PropertyInterpolator<Float> {
        private FloatPropertyInterpolator() {
        }

        @Override
        public Class getBasePropertyClass() {
            return Float.class;
        }

        @Override
        public Float interpolate(Float from, Float to, float timelinePosition) {
            return Float.valueOf(from.floatValue() + (to.floatValue() - from.floatValue()) * timelinePosition);
        }
    }
}

