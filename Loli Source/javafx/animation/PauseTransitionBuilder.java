/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.PauseTransition;
import javafx.animation.TransitionBuilder;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
public final class PauseTransitionBuilder
extends TransitionBuilder<PauseTransitionBuilder>
implements Builder<PauseTransition> {
    private boolean __set;
    private Duration duration;

    protected PauseTransitionBuilder() {
    }

    public static PauseTransitionBuilder create() {
        return new PauseTransitionBuilder();
    }

    public void applyTo(PauseTransition pauseTransition) {
        super.applyTo(pauseTransition);
        if (this.__set) {
            pauseTransition.setDuration(this.duration);
        }
    }

    public PauseTransitionBuilder duration(Duration duration) {
        this.duration = duration;
        this.__set = true;
        return this;
    }

    @Override
    public PauseTransition build() {
        PauseTransition pauseTransition = new PauseTransition();
        this.applyTo(pauseTransition);
        return pauseTransition;
    }
}

