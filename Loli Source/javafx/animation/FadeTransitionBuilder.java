/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.FadeTransition;
import javafx.animation.TransitionBuilder;
import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
public final class FadeTransitionBuilder
extends TransitionBuilder<FadeTransitionBuilder>
implements Builder<FadeTransition> {
    private int __set;
    private double byValue;
    private Duration duration;
    private double fromValue;
    private Node node;
    private double toValue;

    protected FadeTransitionBuilder() {
    }

    public static FadeTransitionBuilder create() {
        return new FadeTransitionBuilder();
    }

    public void applyTo(FadeTransition fadeTransition) {
        super.applyTo(fadeTransition);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            fadeTransition.setByValue(this.byValue);
        }
        if ((n2 & 2) != 0) {
            fadeTransition.setDuration(this.duration);
        }
        if ((n2 & 4) != 0) {
            fadeTransition.setFromValue(this.fromValue);
        }
        if ((n2 & 8) != 0) {
            fadeTransition.setNode(this.node);
        }
        if ((n2 & 0x10) != 0) {
            fadeTransition.setToValue(this.toValue);
        }
    }

    public FadeTransitionBuilder byValue(double d2) {
        this.byValue = d2;
        this.__set |= 1;
        return this;
    }

    public FadeTransitionBuilder duration(Duration duration) {
        this.duration = duration;
        this.__set |= 2;
        return this;
    }

    public FadeTransitionBuilder fromValue(double d2) {
        this.fromValue = d2;
        this.__set |= 4;
        return this;
    }

    public FadeTransitionBuilder node(Node node) {
        this.node = node;
        this.__set |= 8;
        return this;
    }

    public FadeTransitionBuilder toValue(double d2) {
        this.toValue = d2;
        this.__set |= 0x10;
        return this;
    }

    @Override
    public FadeTransition build() {
        FadeTransition fadeTransition = new FadeTransition();
        this.applyTo(fadeTransition);
        return fadeTransition;
    }
}

