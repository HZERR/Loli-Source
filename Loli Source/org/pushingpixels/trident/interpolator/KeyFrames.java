/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.interpolator;

import org.pushingpixels.trident.ease.TimelineEase;
import org.pushingpixels.trident.interpolator.KeyInterpolators;
import org.pushingpixels.trident.interpolator.KeyTimes;
import org.pushingpixels.trident.interpolator.KeyValues;

public class KeyFrames<T> {
    private KeyValues<T> keyValues;
    private KeyTimes keyTimes;
    private KeyInterpolators interpolators;

    public KeyFrames(KeyValues<T> keyValues) {
        this.init(keyValues, null, new TimelineEase[]{null});
    }

    public KeyFrames(KeyValues<T> keyValues, KeyTimes keyTimes) {
        this.init(keyValues, keyTimes, new TimelineEase[]{null});
    }

    public KeyFrames(KeyValues<T> keyValues, KeyTimes keyTimes, TimelineEase ... interpolators) {
        this.init(keyValues, keyTimes, interpolators);
    }

    public KeyFrames(KeyValues<T> keyValues, TimelineEase ... interpolators) {
        this.init(keyValues, null, interpolators);
    }

    private void init(KeyValues<T> keyValues, KeyTimes keyTimes, TimelineEase ... interpolators) {
        int numFrames = keyValues.getSize();
        if (keyTimes == null) {
            float timeVal;
            float[] keyTimesArray = new float[numFrames];
            keyTimesArray[0] = timeVal = 0.0f;
            for (int i2 = 1; i2 < numFrames - 1; ++i2) {
                keyTimesArray[i2] = timeVal += 1.0f / (float)(numFrames - 1);
            }
            keyTimesArray[numFrames - 1] = 1.0f;
            this.keyTimes = new KeyTimes(keyTimesArray);
        } else {
            this.keyTimes = keyTimes;
        }
        this.keyValues = keyValues;
        if (numFrames != this.keyTimes.getSize()) {
            throw new IllegalArgumentException("keyValues and keyTimes must be of equal size");
        }
        if (interpolators != null && interpolators.length != numFrames - 1 && interpolators.length != 1) {
            throw new IllegalArgumentException("interpolators must be either null (implying interpolation for all intervals), a single interpolator (which will be used for all intervals), or a number of interpolators equal to one less than the number of times.");
        }
        this.interpolators = new KeyInterpolators(numFrames - 1, interpolators);
    }

    public Class getType() {
        return this.keyValues.getType();
    }

    KeyValues getKeyValues() {
        return this.keyValues;
    }

    KeyTimes getKeyTimes() {
        return this.keyTimes;
    }

    public int getInterval(float fraction) {
        return this.keyTimes.getInterval(fraction);
    }

    public Object getValue(float fraction) {
        float t1;
        float t0;
        float t2;
        int interval = this.getInterval(fraction);
        float interpolatedT = this.interpolators.interpolate(interval, t2 = (fraction - (t0 = this.keyTimes.getTime(interval))) / ((t1 = this.keyTimes.getTime(interval + 1)) - t0));
        if (interpolatedT < 0.0f) {
            interpolatedT = 0.0f;
        } else if (interpolatedT > 1.0f) {
            interpolatedT = 1.0f;
        }
        return this.keyValues.getValue(interval, interval + 1, interpolatedT);
    }
}

