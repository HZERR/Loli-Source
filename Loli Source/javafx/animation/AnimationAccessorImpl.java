/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import com.sun.scenario.animation.shared.AnimationAccessor;
import javafx.animation.Animation;

final class AnimationAccessorImpl
extends AnimationAccessor {
    AnimationAccessorImpl() {
    }

    @Override
    public void setCurrentRate(Animation animation, double d2) {
        animation.impl_setCurrentRate(d2);
    }

    @Override
    public void playTo(Animation animation, long l2, long l3) {
        animation.impl_playTo(l2, l3);
    }

    @Override
    public void jumpTo(Animation animation, long l2, long l3, boolean bl) {
        animation.impl_jumpTo(l2, l3, bl);
    }

    @Override
    public void finished(Animation animation) {
        animation.impl_finished();
    }

    @Override
    public void setCurrentTicks(Animation animation, long l2) {
        animation.impl_setCurrentTicks(l2);
    }
}

