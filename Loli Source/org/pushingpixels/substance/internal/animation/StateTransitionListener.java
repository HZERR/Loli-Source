/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.animation;

import java.util.EventListener;
import org.pushingpixels.substance.internal.animation.StateTransitionEvent;

public interface StateTransitionListener
extends EventListener {
    public void onModelStateTransition(StateTransitionEvent var1);

    public void onFocusStateTransition(StateTransitionEvent var1);
}

