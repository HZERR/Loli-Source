/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import java.util.Arrays;
import java.util.Collection;
import javafx.animation.AnimationBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Builder;

@Deprecated
public final class TimelineBuilder
extends AnimationBuilder<TimelineBuilder>
implements Builder<Timeline> {
    private boolean __set;
    private Collection<? extends KeyFrame> keyFrames;
    private double targetFramerate;

    protected TimelineBuilder() {
    }

    public static TimelineBuilder create() {
        return new TimelineBuilder();
    }

    public void applyTo(Timeline timeline) {
        super.applyTo(timeline);
        if (this.__set) {
            timeline.getKeyFrames().addAll(this.keyFrames);
        }
    }

    public TimelineBuilder keyFrames(Collection<? extends KeyFrame> collection) {
        this.keyFrames = collection;
        this.__set = true;
        return this;
    }

    public TimelineBuilder keyFrames(KeyFrame ... arrkeyFrame) {
        return this.keyFrames(Arrays.asList(arrkeyFrame));
    }

    @Override
    public TimelineBuilder targetFramerate(double d2) {
        this.targetFramerate = d2;
        return this;
    }

    @Override
    public Timeline build() {
        Timeline timeline = new Timeline(this.targetFramerate);
        this.applyTo(timeline);
        return timeline;
    }
}

