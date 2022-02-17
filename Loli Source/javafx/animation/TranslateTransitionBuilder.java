/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.TransitionBuilder;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
public final class TranslateTransitionBuilder
extends TransitionBuilder<TranslateTransitionBuilder>
implements Builder<TranslateTransition> {
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

    protected TranslateTransitionBuilder() {
    }

    public static TranslateTransitionBuilder create() {
        return new TranslateTransitionBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(TranslateTransition translateTransition) {
        super.applyTo(translateTransition);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    translateTransition.setByX(this.byX);
                    break;
                }
                case 1: {
                    translateTransition.setByY(this.byY);
                    break;
                }
                case 2: {
                    translateTransition.setByZ(this.byZ);
                    break;
                }
                case 3: {
                    translateTransition.setDuration(this.duration);
                    break;
                }
                case 4: {
                    translateTransition.setFromX(this.fromX);
                    break;
                }
                case 5: {
                    translateTransition.setFromY(this.fromY);
                    break;
                }
                case 6: {
                    translateTransition.setFromZ(this.fromZ);
                    break;
                }
                case 7: {
                    translateTransition.setNode(this.node);
                    break;
                }
                case 8: {
                    translateTransition.setToX(this.toX);
                    break;
                }
                case 9: {
                    translateTransition.setToY(this.toY);
                    break;
                }
                case 10: {
                    translateTransition.setToZ(this.toZ);
                }
            }
        }
    }

    public TranslateTransitionBuilder byX(double d2) {
        this.byX = d2;
        this.__set(0);
        return this;
    }

    public TranslateTransitionBuilder byY(double d2) {
        this.byY = d2;
        this.__set(1);
        return this;
    }

    public TranslateTransitionBuilder byZ(double d2) {
        this.byZ = d2;
        this.__set(2);
        return this;
    }

    public TranslateTransitionBuilder duration(Duration duration) {
        this.duration = duration;
        this.__set(3);
        return this;
    }

    public TranslateTransitionBuilder fromX(double d2) {
        this.fromX = d2;
        this.__set(4);
        return this;
    }

    public TranslateTransitionBuilder fromY(double d2) {
        this.fromY = d2;
        this.__set(5);
        return this;
    }

    public TranslateTransitionBuilder fromZ(double d2) {
        this.fromZ = d2;
        this.__set(6);
        return this;
    }

    public TranslateTransitionBuilder node(Node node) {
        this.node = node;
        this.__set(7);
        return this;
    }

    public TranslateTransitionBuilder toX(double d2) {
        this.toX = d2;
        this.__set(8);
        return this;
    }

    public TranslateTransitionBuilder toY(double d2) {
        this.toY = d2;
        this.__set(9);
        return this;
    }

    public TranslateTransitionBuilder toZ(double d2) {
        this.toZ = d2;
        this.__set(10);
        return this;
    }

    @Override
    public TranslateTransition build() {
        TranslateTransition translateTransition = new TranslateTransition();
        this.applyTo(translateTransition);
        return translateTransition;
    }
}

