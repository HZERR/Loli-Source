/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.GeneralClipInterpolator;
import com.sun.scenario.animation.shared.SimpleClipInterpolator;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public abstract class ClipInterpolator {
    static ClipInterpolator create(KeyFrame[] arrkeyFrame, long[] arrl) {
        return ClipInterpolator.getRealKeyFrameCount(arrkeyFrame) == 2 ? (arrkeyFrame.length == 1 ? new SimpleClipInterpolator(arrkeyFrame[0], arrl[0]) : new SimpleClipInterpolator(arrkeyFrame[0], arrkeyFrame[1], arrl[1])) : new GeneralClipInterpolator(arrkeyFrame, arrl);
    }

    static int getRealKeyFrameCount(KeyFrame[] arrkeyFrame) {
        int n2 = arrkeyFrame.length;
        return n2 == 0 ? 0 : (arrkeyFrame[0].getTime().greaterThan(Duration.ZERO) ? n2 + 1 : n2);
    }

    abstract ClipInterpolator setKeyFrames(KeyFrame[] var1, long[] var2);

    abstract void interpolate(long var1);

    abstract void validate(boolean var1);
}

