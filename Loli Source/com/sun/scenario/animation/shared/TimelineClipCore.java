/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation.shared;

import com.sun.javafx.animation.TickCalculation;
import com.sun.scenario.animation.shared.AnimationAccessor;
import com.sun.scenario.animation.shared.ClipInterpolator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class TimelineClipCore {
    private static final int UNDEFINED_KEYFRAME = -1;
    private static final Comparator<KeyFrame> KEY_FRAME_COMPARATOR = (keyFrame, keyFrame2) -> keyFrame.getTime().compareTo(keyFrame2.getTime());
    Timeline timeline;
    private KeyFrame[] keyFrames = new KeyFrame[0];
    private long[] keyFrameTicks = new long[0];
    private boolean canSkipFrames = true;
    private ClipInterpolator clipInterpolator;
    private boolean aborted = false;
    private int lastKF = -1;
    private long curTicks = 0L;

    public TimelineClipCore(Timeline timeline) {
        this.timeline = timeline;
        this.clipInterpolator = ClipInterpolator.create(this.keyFrames, this.keyFrameTicks);
    }

    public Duration setKeyFrames(Collection<KeyFrame> collection) {
        int n2 = collection.size();
        KeyFrame[] arrkeyFrame = new KeyFrame[n2];
        collection.toArray(arrkeyFrame);
        Arrays.sort(arrkeyFrame, KEY_FRAME_COMPARATOR);
        this.canSkipFrames = true;
        this.keyFrames = arrkeyFrame;
        this.keyFrameTicks = new long[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.keyFrameTicks[i2] = TickCalculation.fromDuration(this.keyFrames[i2].getTime());
            if (!this.canSkipFrames || this.keyFrames[i2].getOnFinished() == null) continue;
            this.canSkipFrames = false;
        }
        this.clipInterpolator = this.clipInterpolator.setKeyFrames(arrkeyFrame, this.keyFrameTicks);
        return n2 == 0 ? Duration.ZERO : arrkeyFrame[n2 - 1].getTime();
    }

    public void notifyCurrentRateChanged() {
        if (this.timeline.getStatus() != Animation.Status.RUNNING) {
            this.clearLastKeyFrame();
        }
    }

    public void abort() {
        this.aborted = true;
    }

    private void clearLastKeyFrame() {
        this.lastKF = -1;
    }

    public void jumpTo(long l2, boolean bl) {
        this.lastKF = -1;
        this.curTicks = l2;
        if (this.timeline.getStatus() != Animation.Status.STOPPED || bl) {
            if (bl) {
                this.clipInterpolator.validate(false);
            }
            this.clipInterpolator.interpolate(l2);
        }
    }

    public void start(boolean bl) {
        this.clearLastKeyFrame();
        this.clipInterpolator.validate(bl);
        if (this.curTicks > 0L) {
            this.clipInterpolator.interpolate(this.curTicks);
        }
    }

    public void playTo(long l2) {
        boolean bl;
        if (this.canSkipFrames) {
            this.clearLastKeyFrame();
            this.setTime(l2);
            this.clipInterpolator.interpolate(l2);
            return;
        }
        this.aborted = false;
        boolean bl2 = bl = this.curTicks <= l2;
        if (bl) {
            int n2 = this.lastKF == -1 ? 0 : (this.keyFrameTicks[this.lastKF] <= this.curTicks ? this.lastKF + 1 : this.lastKF);
            int n3 = this.keyFrames.length;
            for (int i2 = n2; i2 < n3; ++i2) {
                long l3 = this.keyFrameTicks[i2];
                if (l3 > l2) {
                    this.lastKF = i2 - 1;
                } else {
                    if (l3 < this.curTicks) continue;
                    this.visitKeyFrame(i2, l3);
                    if (!this.aborted) {
                        continue;
                    }
                }
                break;
            }
        } else {
            int n4;
            for (int i3 = n4 = this.lastKF == -1 ? this.keyFrames.length - 1 : (this.keyFrameTicks[this.lastKF] >= this.curTicks ? this.lastKF - 1 : this.lastKF); i3 >= 0; --i3) {
                long l4 = this.keyFrameTicks[i3];
                if (l4 < l2) {
                    this.lastKF = i3 + 1;
                } else {
                    if (l4 > this.curTicks) continue;
                    this.visitKeyFrame(i3, l4);
                    if (!this.aborted) {
                        continue;
                    }
                }
                break;
            }
        }
        if (!(this.aborted || this.lastKF != -1 && this.keyFrameTicks[this.lastKF] == l2 && this.keyFrames[this.lastKF].getOnFinished() != null)) {
            this.setTime(l2);
            this.clipInterpolator.interpolate(l2);
        }
    }

    private void setTime(long l2) {
        this.curTicks = l2;
        AnimationAccessor.getDefault().setCurrentTicks(this.timeline, l2);
    }

    private void visitKeyFrame(int n2, long l2) {
        if (n2 != this.lastKF) {
            this.lastKF = n2;
            KeyFrame keyFrame = this.keyFrames[n2];
            EventHandler<ActionEvent> eventHandler = keyFrame.getOnFinished();
            if (eventHandler != null) {
                this.setTime(l2);
                this.clipInterpolator.interpolate(l2);
                try {
                    eventHandler.handle(new ActionEvent(keyFrame, null));
                }
                catch (Throwable throwable) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), throwable);
                }
            }
        }
    }
}

