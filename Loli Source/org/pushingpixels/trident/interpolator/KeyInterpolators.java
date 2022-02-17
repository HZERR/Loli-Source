/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.interpolator;

import java.util.ArrayList;
import org.pushingpixels.trident.ease.Linear;
import org.pushingpixels.trident.ease.TimelineEase;

class KeyInterpolators {
    private ArrayList<TimelineEase> interpolators = new ArrayList();

    KeyInterpolators(int numIntervals, TimelineEase ... interpolators) {
        if (interpolators == null || interpolators[0] == null) {
            for (int i2 = 0; i2 < numIntervals; ++i2) {
                this.interpolators.add(new Linear());
            }
        } else if (interpolators.length < numIntervals) {
            for (int i3 = 0; i3 < numIntervals; ++i3) {
                this.interpolators.add(interpolators[0]);
            }
        } else {
            for (int i4 = 0; i4 < numIntervals; ++i4) {
                this.interpolators.add(interpolators[i4]);
            }
        }
    }

    float interpolate(int interval, float fraction) {
        return this.interpolators.get(interval).map(fraction);
    }
}

