/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.ease;

import org.pushingpixels.trident.ease.TimelineEase;

public class Linear
implements TimelineEase {
    @Override
    public float map(float durationFraction) {
        return durationFraction;
    }
}

