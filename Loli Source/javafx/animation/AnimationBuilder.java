/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

@Deprecated
public abstract class AnimationBuilder<B extends AnimationBuilder<B>> {
    private int __set;
    private boolean autoReverse;
    private int cycleCount;
    private Duration delay;
    private EventHandler<ActionEvent> onFinished;
    private double rate;
    private double targetFramerate;

    protected AnimationBuilder() {
    }

    public void applyTo(Animation animation) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            animation.setAutoReverse(this.autoReverse);
        }
        if ((n2 & 2) != 0) {
            animation.setCycleCount(this.cycleCount);
        }
        if ((n2 & 4) != 0) {
            animation.setDelay(this.delay);
        }
        if ((n2 & 8) != 0) {
            animation.setOnFinished(this.onFinished);
        }
        if ((n2 & 0x10) != 0) {
            animation.setRate(this.rate);
        }
    }

    public B autoReverse(boolean bl) {
        this.autoReverse = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B cycleCount(int n2) {
        this.cycleCount = n2;
        this.__set |= 2;
        return (B)this;
    }

    public B delay(Duration duration) {
        this.delay = duration;
        this.__set |= 4;
        return (B)this;
    }

    public B onFinished(EventHandler<ActionEvent> eventHandler) {
        this.onFinished = eventHandler;
        this.__set |= 8;
        return (B)this;
    }

    public B rate(double d2) {
        this.rate = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B targetFramerate(double d2) {
        this.targetFramerate = d2;
        return (B)this;
    }
}

