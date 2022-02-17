/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.interpolator;

import java.util.ArrayList;

public class KeyTimes {
    private ArrayList<Float> times = new ArrayList();

    public KeyTimes(float ... times) {
        if (times[0] != 0.0f) {
            throw new IllegalArgumentException("First time value must be zero");
        }
        if (times[times.length - 1] != 1.0f) {
            throw new IllegalArgumentException("Last time value must be one");
        }
        float prevTime = 0.0f;
        for (float time : times) {
            if (time < prevTime) {
                throw new IllegalArgumentException("Time values must be in increasing order");
            }
            this.times.add(Float.valueOf(time));
            prevTime = time;
        }
    }

    ArrayList getTimes() {
        return this.times;
    }

    int getSize() {
        return this.times.size();
    }

    int getInterval(float fraction) {
        int prevIndex = 0;
        int i2 = 1;
        while (i2 < this.times.size()) {
            float time = this.times.get(i2).floatValue();
            if (time >= fraction) {
                return prevIndex;
            }
            prevIndex = i2++;
        }
        return prevIndex;
    }

    float getTime(int index) {
        return this.times.get(index).floatValue();
    }
}

