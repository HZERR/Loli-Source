/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.StrokeTransition;
import javafx.animation.TransitionBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
public final class StrokeTransitionBuilder
extends TransitionBuilder<StrokeTransitionBuilder>
implements Builder<StrokeTransition> {
    private int __set;
    private Duration duration;
    private Color fromValue;
    private Shape shape;
    private Color toValue;

    protected StrokeTransitionBuilder() {
    }

    public static StrokeTransitionBuilder create() {
        return new StrokeTransitionBuilder();
    }

    public void applyTo(StrokeTransition strokeTransition) {
        super.applyTo(strokeTransition);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            strokeTransition.setDuration(this.duration);
        }
        if ((n2 & 2) != 0) {
            strokeTransition.setFromValue(this.fromValue);
        }
        if ((n2 & 4) != 0) {
            strokeTransition.setShape(this.shape);
        }
        if ((n2 & 8) != 0) {
            strokeTransition.setToValue(this.toValue);
        }
    }

    public StrokeTransitionBuilder duration(Duration duration) {
        this.duration = duration;
        this.__set |= 1;
        return this;
    }

    public StrokeTransitionBuilder fromValue(Color color) {
        this.fromValue = color;
        this.__set |= 2;
        return this;
    }

    public StrokeTransitionBuilder shape(Shape shape) {
        this.shape = shape;
        this.__set |= 4;
        return this;
    }

    public StrokeTransitionBuilder toValue(Color color) {
        this.toValue = color;
        this.__set |= 8;
        return this;
    }

    @Override
    public StrokeTransition build() {
        StrokeTransition strokeTransition = new StrokeTransition();
        this.applyTo(strokeTransition);
        return strokeTransition;
    }
}

