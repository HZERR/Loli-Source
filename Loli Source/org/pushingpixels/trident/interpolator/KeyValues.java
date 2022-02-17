/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.interpolator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.pushingpixels.trident.TridentConfig;
import org.pushingpixels.trident.interpolator.PropertyInterpolator;

public class KeyValues<T> {
    private final List<T> values = new ArrayList<T>();
    private final PropertyInterpolator<T> evaluator;
    private final Class<?> type;
    private T startValue;

    public static <T> KeyValues<T> create(T ... params) {
        return new KeyValues<T>(params);
    }

    public static <T> KeyValues<T> create(PropertyInterpolator evaluator, T ... params) {
        return new KeyValues<T>(evaluator, params);
    }

    private KeyValues(T ... params) {
        this(TridentConfig.getInstance().getPropertyInterpolator(params), params);
    }

    private KeyValues(PropertyInterpolator evaluator, T ... params) {
        if (params == null) {
            throw new IllegalArgumentException("params array cannot be null");
        }
        if (params.length == 0) {
            throw new IllegalArgumentException("params array must have at least one element");
        }
        if (params.length == 1) {
            this.values.add(null);
        }
        Collections.addAll(this.values, params);
        this.type = params.getClass().getComponentType();
        this.evaluator = evaluator;
    }

    int getSize() {
        return this.values.size();
    }

    Class<?> getType() {
        return this.type;
    }

    void setStartValue(T startValue) {
        if (this.isToAnimation()) {
            this.startValue = startValue;
        }
    }

    boolean isToAnimation() {
        return this.values.get(0) == null;
    }

    T getValue(int i0, int i1, float fraction) {
        T value;
        T lowerValue = this.values.get(i0);
        if (lowerValue == null) {
            lowerValue = this.startValue;
        }
        if (i0 == i1) {
            value = lowerValue;
        } else {
            T v0 = lowerValue;
            T v1 = this.values.get(i1);
            value = this.evaluator.interpolate(v0, v1, fraction);
        }
        return value;
    }
}

