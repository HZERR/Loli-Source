/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.ScaleTransition;
import javafx.animation.TransitionBuilder;
import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
public final class ScaleTransitionBuilder
extends TransitionBuilder<ScaleTransitionBuilder>
implements Builder<ScaleTransition> {
    private int __set;
    private double byX;
    private double byY;
    private double byZ;
    private Duration duration;
    private double fromX;
    private double fromY;
    private double fromZ;
    private Node node;
    private double toX;
    private double toY;
    private double toZ;

    protected ScaleTransitionBuilder() {
    }

    public static ScaleTransitionBuilder create() {
        return new ScaleTransitionBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(ScaleTransition scaleTransition) {
        super.applyTo(scaleTransition);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    scaleTransition.setByX(this.byX);
                    break;
                }
                case 1: {
                    scaleTransition.setByY(this.byY);
                    break;
                }
                case 2: {
                    scaleTransition.setByZ(this.byZ);
                    break;
                }
                case 3: {
                    scaleTransition.setDuration(this.duration);
                    break;
                }
                case 4: {
                    scaleTransition.setFromX(this.fromX);
                    break;
                }
                case 5: {
                    scaleTransition.setFromY(this.fromY);
                    break;
                }
                case 6: {
                    scaleTransition.setFromZ(this.fromZ);
                    break;
                }
                case 7: {
                    scaleTransition.setNode(this.node);
                    break;
                }
                case 8: {
                    scaleTransition.setToX(this.toX);
                    break;
                }
                case 9: {
                    scaleTransition.setToY(this.toY);
                    break;
                }
                case 10: {
                    scaleTransition.setToZ(this.toZ);
                }
            }
        }
    }

    public ScaleTransitionBuilder byX(double d2) {
        this.byX = d2;
        this.__set(0);
        return this;
    }

    public ScaleTransitionBuilder byY(double d2) {
        this.byY = d2;
        this.__set(1);
        return this;
    }

    public ScaleTransitionBuilder byZ(double d2) {
        this.byZ = d2;
        this.__set(2);
        return this;
    }

    public ScaleTransitionBuilder duration(Duration duration) {
        this.duration = duration;
        this.__set(3);
        return this;
    }

    public ScaleTransitionBuilder fromX(double d2) {
        this.fromX = d2;
        this.__set(4);
        return this;
    }

    public ScaleTransitionBuilder fromY(double d2) {
        this.fromY = d2;
        this.__set(5);
        return this;
    }

    public ScaleTransitionBuilder fromZ(double d2) {
        this.fromZ = d2;
        this.__set(6);
        return this;
    }

    public ScaleTransitionBuilder node(Node node) {
        this.node = node;
        this.__set(7);
        return this;
    }

    public ScaleTransitionBuilder toX(double d2) {
        this.toX = d2;
        this.__set(8);
        return this;
    }

    public ScaleTransitionBuilder toY(double d2) {
        this.toY = d2;
        this.__set(9);
        return this;
    }

    public ScaleTransitionBuilder toZ(double d2) {
        this.toZ = d2;
        this.__set(10);
        return this;
    }

    @Override
    public ScaleTransition build() {
        ScaleTransition scaleTransition = new ScaleTransition();
        this.applyTo(scaleTransition);
        return scaleTransition;
    }
}

