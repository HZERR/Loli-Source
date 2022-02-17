/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.animation;

import java.awt.event.MouseEvent;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;

public interface TransitionAwareUI {
    public boolean isInside(MouseEvent var1);

    public StateTransitionTracker getTransitionTracker();
}

