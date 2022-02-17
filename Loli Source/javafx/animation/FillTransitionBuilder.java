/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.FillTransition;
import javafx.animation.TransitionBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
public final class FillTransitionBuilder
extends TransitionBuilder<FillTransitionBuilder>
implements Builder<FillTransition> {
    private int __set;
    private Duration duration;
    private Color fromValue;
    private Shape shape;
    private Color toValue;

    protected FillTransitionBuilder() {
    }

    public static FillTransitionBuilder create() {
        return new FillTransitionBuilder();
    }

    public void applyTo(FillTransition fillTransition) {
        super.applyTo(fillTransition);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            fillTransition.setDuration(this.duration);
        }
        if ((n2 & 2) != 0) {
            fillTransition.setFromValue(this.fromValue);
        }
        if ((n2 & 4) != 0) {
            fillTransition.setShape(this.shape);
        }
        if ((n2 & 8) != 0) {
            fillTransition.setToValue(this.toValue);
        }
    }

    public FillTransitionBuilder duration(Duration duration) {
        this.duration = duration;
        this.__set |= 1;
        return this;
    }

    public FillTransitionBuilder fromValue(Color color) {
        this.fromValue = color;
        this.__set |= 2;
        return this;
    }

    public FillTransitionBuilder shape(Shape shape) {
        this.shape = shape;
        this.__set |= 4;
        return this;
    }

    public FillTransitionBuilder toValue(Color color) {
        this.toValue = color;
        this.__set |= 8;
        return this;
    }

    @Override
    public FillTransition build() {
        FillTransition fillTransition = new FillTransition();
        this.applyTo(fillTransition);
        return fillTransition;
    }
}

