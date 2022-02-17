/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.AnimationBuilder;
import javafx.animation.Interpolator;
import javafx.animation.Transition;

@Deprecated
public abstract class TransitionBuilder<B extends TransitionBuilder<B>>
extends AnimationBuilder<B> {
    private boolean __set;
    private Interpolator interpolator;
    private double targetFramerate;

    protected TransitionBuilder() {
    }

    public void applyTo(Transition transition) {
        super.applyTo(transition);
        if (this.__set) {
            transition.setInterpolator(this.interpolator);
        }
    }

    public B interpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        this.__set = true;
        return (B)this;
    }

    @Override
    public B targetFramerate(double d2) {
        this.targetFramerate = d2;
        return (B)this;
    }
}

