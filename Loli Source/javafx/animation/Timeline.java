/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.animation.shared.TimelineClipCore;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Duration;

public final class Timeline
extends Animation {
    final TimelineClipCore clipCore;
    private final ObservableList<KeyFrame> keyFrames = new TrackableObservableList<KeyFrame>(){

        @Override
        protected void onChanged(ListChangeListener.Change<KeyFrame> change) {
            while (change.next()) {
                String string;
                if (change.wasPermutated()) continue;
                for (KeyFrame keyFrame : change.getRemoved()) {
                    string = keyFrame.getName();
                    if (string == null) continue;
                    Timeline.this.getCuePoints().remove(string);
                }
                for (KeyFrame keyFrame : change.getAddedSubList()) {
                    string = keyFrame.getName();
                    if (string == null) continue;
                    Timeline.this.getCuePoints().put(string, keyFrame.getTime());
                }
                Duration duration = Timeline.this.clipCore.setKeyFrames(Timeline.this.getKeyFrames());
                Timeline.this.setCycleDuration(duration);
            }
        }
    };

    public final ObservableList<KeyFrame> getKeyFrames() {
        return this.keyFrames;
    }

    public Timeline(double d2, KeyFrame ... arrkeyFrame) {
        super(d2);
        this.clipCore = new TimelineClipCore(this);
        this.getKeyFrames().setAll(arrkeyFrame);
    }

    public Timeline(KeyFrame ... arrkeyFrame) {
        this.clipCore = new TimelineClipCore(this);
        this.getKeyFrames().setAll(arrkeyFrame);
    }

    public Timeline(double d2) {
        super(d2);
        this.clipCore = new TimelineClipCore(this);
    }

    public Timeline() {
        this.clipCore = new TimelineClipCore(this);
    }

    Timeline(AbstractMasterTimer abstractMasterTimer) {
        super(abstractMasterTimer);
        this.clipCore = new TimelineClipCore(this);
    }

    @Override
    void impl_playTo(long l2, long l3) {
        this.clipCore.playTo(l2);
    }

    @Override
    void impl_jumpTo(long l2, long l3, boolean bl) {
        this.impl_sync(false);
        this.impl_setCurrentTicks(l2);
        this.clipCore.jumpTo(l2, bl);
    }

    @Override
    void impl_setCurrentRate(double d2) {
        super.impl_setCurrentRate(d2);
        this.clipCore.notifyCurrentRateChanged();
    }

    @Override
    void impl_start(boolean bl) {
        super.impl_start(bl);
        this.clipCore.start(bl);
    }

    @Override
    public void stop() {
        if (this.parent != null) {
            throw new IllegalStateException("Cannot stop when embedded in another animation");
        }
        if (this.getStatus() == Animation.Status.RUNNING) {
            this.clipCore.abort();
        }
        super.stop();
    }
}

