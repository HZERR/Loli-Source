/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.ease;

import org.pushingpixels.trident.ease.TimelineEase;

public class Sine
implements TimelineEase {
    @Override
    public float map(float durationFraction) {
        return (float)Math.sin((double)durationFraction * Math.PI / 2.0);
    }
}

