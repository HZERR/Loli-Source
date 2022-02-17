/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.ClipInterpolator;
import com.sun.scenario.animation.shared.InterpolationInterval;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableValue;
import javafx.util.Duration;

class SimpleClipInterpolator
extends ClipInterpolator {
    private static final KeyFrame ZERO_FRAME = new KeyFrame(Duration.ZERO, new KeyValue[0]);
    private KeyFrame startKeyFrame;
    private KeyFrame endKeyFrame;
    private long endTicks;
    private InterpolationInterval[] interval;
    private int undefinedStartValueCount;
    private long ticks;
    private boolean invalid = true;

    SimpleClipInterpolator(KeyFrame keyFrame, KeyFrame keyFrame2, long l2) {
        this.startKeyFrame = keyFrame;
        this.endKeyFrame = keyFrame2;
        this.endTicks = l2;
    }

    SimpleClipInterpolator(KeyFrame keyFrame, long l2) {
        this.startKeyFrame = ZERO_FRAME;
        this.endKeyFrame = keyFrame;
        this.endTicks = l2;
    }

    @Override
    ClipInterpolator setKeyFrames(KeyFrame[] arrkeyFrame, long[] arrl) {
        if (ClipInterpolator.getRealKeyFrameCount(arrkeyFrame) != 2) {
            return ClipInterpolator.create(arrkeyFrame, arrl);
        }
        if (arrkeyFrame.length == 1) {
            this.startKeyFrame = ZERO_FRAME;
            this.endKeyFrame = arrkeyFrame[0];
            this.endTicks = arrl[0];
        } else {
            this.startKeyFrame = arrkeyFrame[0];
            this.endKeyFrame = arrkeyFrame[1];
            this.endTicks = arrl[1];
        }
        this.invalid = true;
        return this;
    }

    @Override
    void validate(boolean bl) {
        if (this.invalid) {
            this.ticks = this.endTicks;
            HashMap hashMap = new HashMap();
            for (KeyValue keyValue : this.endKeyFrame.getValues()) {
                hashMap.put(keyValue.getTarget(), keyValue);
            }
            int n2 = hashMap.size();
            this.interval = new InterpolationInterval[n2];
            int n3 = 0;
            for (KeyValue keyValue : this.startKeyFrame.getValues()) {
                WritableValue<?> writableValue = keyValue.getTarget();
                KeyValue keyValue2 = (KeyValue)hashMap.get(writableValue);
                if (keyValue2 == null) continue;
                this.interval[n3++] = InterpolationInterval.create(keyValue2, this.ticks, keyValue, this.ticks);
                hashMap.remove(writableValue);
            }
            this.undefinedStartValueCount = hashMap.values().size();
            for (KeyValue keyValue : hashMap.values()) {
                this.interval[n3++] = InterpolationInterval.create(keyValue, this.ticks);
            }
            this.invalid = false;
        } else if (bl) {
            int n4 = this.interval.length;
            for (int i2 = n4 - this.undefinedStartValueCount; i2 < n4; ++i2) {
                this.interval[i2].recalculateStartValue();
            }
        }
    }

    @Override
    void interpolate(long l2) {
        double d2 = (double)l2 / (double)this.ticks;
        int n2 = this.interval.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.interval[i2].interpolate(d2);
        }
    }
}

