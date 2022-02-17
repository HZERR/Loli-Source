/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.scenario.animation.AbstractMasterTimer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

public final class SequentialTransition
extends Transition {
    private static final Animation[] EMPTY_ANIMATION_ARRAY = new Animation[0];
    private static final int BEFORE = -1;
    private static final double EPSILON = 1.0E-12;
    private Animation[] cachedChildren = EMPTY_ANIMATION_ARRAY;
    private long[] startTimes;
    private long[] durations;
    private long[] delays;
    private double[] rates;
    private boolean[] forceChildSync;
    private int end;
    private int curIndex = -1;
    private long oldTicks = 0L;
    private long offsetTicks;
    private boolean childrenChanged = true;
    private boolean toggledRate;
    private final InvalidationListener childrenListener = observable -> {
        this.childrenChanged = true;
        if (this.getStatus() == Animation.Status.STOPPED) {
            this.setCycleDuration(this.computeCycleDuration());
        }
    };
    private final ChangeListener<Number> rateListener = new ChangeListener<Number>(){

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            if (number.doubleValue() * number2.doubleValue() < 0.0) {
                for (int i2 = 0; i2 < SequentialTransition.this.cachedChildren.length; ++i2) {
                    Animation animation = SequentialTransition.this.cachedChildren[i2];
                    animation.clipEnvelope.setRate(SequentialTransition.this.rates[i2] * Math.signum(SequentialTransition.this.getCurrentRate()));
                }
                SequentialTransition.this.toggledRate = true;
            }
        }
    };
    private ObjectProperty<Node> node;
    private static final Node DEFAULT_NODE = null;
    private final Set<Animation> childrenSet = new HashSet<Animation>();
    private final ObservableList<Animation> children = new VetoableListDecorator<Animation>((ObservableList)new TrackableObservableList<Animation>(){

        @Override
        protected void onChanged(ListChangeListener.Change<Animation> change) {
            while (change.next()) {
                for (Animation animation : change.getRemoved()) {
                    animation.parent = null;
                    animation.rateProperty().removeListener(SequentialTransition.this.childrenListener);
                    animation.totalDurationProperty().removeListener(SequentialTransition.this.childrenListener);
                    animation.delayProperty().removeListener(SequentialTransition.this.childrenListener);
                }
                for (Animation animation : change.getAddedSubList()) {
                    animation.parent = SequentialTransition.this;
                    animation.rateProperty().addListener(SequentialTransition.this.childrenListener);
                    animation.totalDurationProperty().addListener(SequentialTransition.this.childrenListener);
                    animation.delayProperty().addListener(SequentialTransition.this.childrenListener);
                }
            }
            SequentialTransition.this.childrenListener.invalidated(SequentialTransition.this.children);
        }
    }){

        @Override
        protected void onProposedChange(List<Animation> list, int ... arrn) {
            IllegalArgumentException illegalArgumentException = null;
            for (int i2 = 0; i2 < arrn.length; i2 += 2) {
                for (int i3 = arrn[i2]; i3 < arrn[i2 + 1]; ++i3) {
                    SequentialTransition.this.childrenSet.remove(SequentialTransition.this.children.get(i3));
                }
            }
            for (Animation animation : list) {
                if (animation == null) {
                    illegalArgumentException = new IllegalArgumentException("Child cannot be null");
                    break;
                }
                if (!SequentialTransition.this.childrenSet.add(animation)) {
                    illegalArgumentException = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                    break;
                }
                if (!SequentialTransition.checkCycle(animation, SequentialTransition.this)) continue;
                illegalArgumentException = new IllegalArgumentException("This change would create cycle");
                break;
            }
            if (illegalArgumentException != null) {
                SequentialTransition.this.childrenSet.clear();
                SequentialTransition.this.childrenSet.addAll(SequentialTransition.this.children);
                throw illegalArgumentException;
            }
        }
    };

    public final void setNode(Node node) {
        if (this.node != null || node != null) {
            this.nodeProperty().set(node);
        }
    }

    public final Node getNode() {
        return this.node == null ? DEFAULT_NODE : (Node)this.node.get();
    }

    public final ObjectProperty<Node> nodeProperty() {
        if (this.node == null) {
            this.node = new SimpleObjectProperty<Node>(this, "node", DEFAULT_NODE);
        }
        return this.node;
    }

    private static boolean checkCycle(Animation animation, Animation animation2) {
        Animation animation3 = animation2;
        while (animation3 != animation) {
            if (animation3.parent != null) {
                animation3 = animation3.parent;
                continue;
            }
            return false;
        }
        return true;
    }

    public final ObservableList<Animation> getChildren() {
        return this.children;
    }

    public SequentialTransition(Node node, Animation ... arranimation) {
        this.setInterpolator(Interpolator.LINEAR);
        this.setNode(node);
        this.getChildren().setAll(arranimation);
    }

    public SequentialTransition(Animation ... arranimation) {
        this((Node)null, arranimation);
    }

    public SequentialTransition(Node node) {
        this.setInterpolator(Interpolator.LINEAR);
        this.setNode(node);
    }

    public SequentialTransition() {
        this((Node)null);
    }

    SequentialTransition(AbstractMasterTimer abstractMasterTimer) {
        super(abstractMasterTimer);
        this.setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected Node getParentTargetNode() {
        Node node = this.getNode();
        return node != null ? node : (this.parent != null && this.parent instanceof Transition ? ((Transition)this.parent).getParentTargetNode() : null);
    }

    private Duration computeCycleDuration() {
        Duration duration = Duration.ZERO;
        for (Animation animation : this.getChildren()) {
            duration = duration.add(animation.getDelay());
            double d2 = Math.abs(animation.getRate());
            if (!(duration = duration.add(d2 < 1.0E-12 ? animation.getTotalDuration() : animation.getTotalDuration().divide(d2))).isIndefinite()) continue;
            break;
        }
        return duration;
    }

    private double calculateFraction(long l2, long l3) {
        double d2 = (double)l2 / (double)l3;
        return d2 <= 0.0 ? 0.0 : (d2 >= 1.0 ? 1.0 : d2);
    }

    private int findNewIndex(long l2) {
        int n2;
        if (this.curIndex != -1 && this.curIndex != this.end && this.startTimes[this.curIndex] <= l2 && l2 <= this.startTimes[this.curIndex + 1]) {
            return this.curIndex;
        }
        boolean bl = this.curIndex == -1 || this.curIndex == this.end;
        int n3 = bl || l2 < this.oldTicks ? 0 : this.curIndex + 1;
        int n4 = Arrays.binarySearch(this.startTimes, n3, n2 = bl || this.oldTicks < l2 ? this.end : this.curIndex, l2);
        return n4 < 0 ? -n4 - 2 : (n4 > 0 ? n4 - 1 : 0);
    }

    @Override
    void impl_sync(boolean bl) {
        super.impl_sync(bl);
        if (bl && this.childrenChanged || this.startTimes == null) {
            this.cachedChildren = this.getChildren().toArray(EMPTY_ANIMATION_ARRAY);
            this.end = this.cachedChildren.length;
            this.startTimes = new long[this.end + 1];
            this.durations = new long[this.end];
            this.delays = new long[this.end];
            this.rates = new double[this.end];
            this.forceChildSync = new boolean[this.end];
            long l2 = 0L;
            int n2 = 0;
            for (Animation animation : this.cachedChildren) {
                this.startTimes[n2] = l2;
                this.rates[n2] = Math.abs(animation.getRate());
                if (this.rates[n2] < 1.0E-12) {
                    this.rates[n2] = 1.0;
                }
                this.durations[n2] = TickCalculation.fromDuration(animation.getTotalDuration(), this.rates[n2]);
                this.delays[n2] = TickCalculation.fromDuration(animation.getDelay());
                l2 = this.durations[n2] == Long.MAX_VALUE || this.delays[n2] == Long.MAX_VALUE || l2 == Long.MAX_VALUE ? Long.MAX_VALUE : TickCalculation.add(l2, TickCalculation.add(this.durations[n2], this.delays[n2]));
                this.forceChildSync[n2] = true;
                ++n2;
            }
            this.startTimes[this.end] = l2;
            this.childrenChanged = false;
        } else if (bl) {
            int n3 = this.forceChildSync.length;
            for (int i2 = 0; i2 < n3; ++i2) {
                this.forceChildSync[i2] = true;
            }
        }
    }

    @Override
    void impl_start(boolean bl) {
        super.impl_start(bl);
        this.toggledRate = false;
        this.rateProperty().addListener(this.rateListener);
        this.offsetTicks = 0L;
        double d2 = this.getCurrentRate();
        long l2 = TickCalculation.fromDuration(this.getCurrentTime());
        if (d2 < 0.0) {
            this.jumpToEnd();
            this.curIndex = this.end;
            if (l2 < this.startTimes[this.end]) {
                this.impl_jumpTo(l2, this.startTimes[this.end], false);
            }
        } else {
            this.jumpToBefore();
            this.curIndex = -1;
            if (l2 > 0L) {
                this.impl_jumpTo(l2, this.startTimes[this.end], false);
            }
        }
    }

    @Override
    void impl_pause() {
        Animation animation;
        super.impl_pause();
        if (this.curIndex != -1 && this.curIndex != this.end && (animation = this.cachedChildren[this.curIndex]).getStatus() == Animation.Status.RUNNING) {
            animation.impl_pause();
        }
    }

    @Override
    void impl_resume() {
        Animation animation;
        super.impl_resume();
        if (this.curIndex != -1 && this.curIndex != this.end && (animation = this.cachedChildren[this.curIndex]).getStatus() == Animation.Status.PAUSED) {
            animation.impl_resume();
            animation.clipEnvelope.setRate(this.rates[this.curIndex] * Math.signum(this.getCurrentRate()));
        }
    }

    @Override
    void impl_stop() {
        Animation animation;
        super.impl_stop();
        if (this.curIndex != -1 && this.curIndex != this.end && (animation = this.cachedChildren[this.curIndex]).getStatus() != Animation.Status.STOPPED) {
            animation.impl_stop();
        }
        if (this.childrenChanged) {
            this.setCycleDuration(this.computeCycleDuration());
        }
        this.rateProperty().removeListener(this.rateListener);
    }

    private boolean startChild(Animation animation, int n2) {
        boolean bl = this.forceChildSync[n2];
        if (animation.impl_startable(bl)) {
            animation.clipEnvelope.setRate(this.rates[n2] * Math.signum(this.getCurrentRate()));
            animation.impl_start(bl);
            this.forceChildSync[n2] = false;
            return true;
        }
        return false;
    }

    @Override
    void impl_playTo(long l2, long l3) {
        Animation animation;
        this.impl_setCurrentTicks(l2);
        double d2 = this.calculateFraction(l2, l3);
        long l4 = Math.max(0L, Math.min(this.getCachedInterpolator().interpolate(0L, l3, d2), l3));
        int n2 = this.findNewIndex(l4);
        Animation animation2 = animation = this.curIndex == -1 || this.curIndex == this.end ? null : this.cachedChildren[this.curIndex];
        if (this.toggledRate) {
            if (animation != null && animation.getStatus() == Animation.Status.RUNNING) {
                this.offsetTicks = (long)((double)this.offsetTicks - Math.signum(this.getCurrentRate()) * (double)(this.durations[this.curIndex] - 2L * (this.oldTicks - this.delays[this.curIndex] - this.startTimes[this.curIndex])));
            }
            this.toggledRate = false;
        }
        if (this.curIndex == n2) {
            if (this.getCurrentRate() > 0.0) {
                long l5 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
                if (l4 >= l5) {
                    if (this.oldTicks <= l5 || animation.getStatus() == Animation.Status.STOPPED) {
                        boolean bl;
                        boolean bl2 = bl = this.oldTicks <= l5;
                        if (bl) {
                            animation.clipEnvelope.jumpTo(0L);
                        }
                        if (!this.startChild(animation, this.curIndex)) {
                            EventHandler<ActionEvent> eventHandler;
                            if (bl && (eventHandler = animation.getOnFinished()) != null) {
                                eventHandler.handle(new ActionEvent(this, null));
                            }
                            this.oldTicks = l4;
                            return;
                        }
                    }
                    if (l4 >= this.startTimes[this.curIndex + 1]) {
                        animation.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
                        if (l4 == l3) {
                            this.curIndex = this.end;
                        }
                    } else {
                        long l6 = TickCalculation.sub(l4 - l5, this.offsetTicks);
                        animation.impl_timePulse(l6);
                    }
                }
            } else {
                long l7 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
                if (this.oldTicks >= this.startTimes[this.curIndex + 1] || this.oldTicks >= l7 && animation.getStatus() == Animation.Status.STOPPED) {
                    boolean bl;
                    boolean bl3 = bl = this.oldTicks >= this.startTimes[this.curIndex + 1];
                    if (bl) {
                        animation.clipEnvelope.jumpTo(Math.round((double)this.durations[this.curIndex] * this.rates[this.curIndex]));
                    }
                    if (!this.startChild(animation, this.curIndex)) {
                        EventHandler<ActionEvent> eventHandler;
                        if (bl && (eventHandler = animation.getOnFinished()) != null) {
                            eventHandler.handle(new ActionEvent(this, null));
                        }
                        this.oldTicks = l4;
                        return;
                    }
                }
                if (l4 <= l7) {
                    animation.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
                    if (l4 == 0L) {
                        this.curIndex = -1;
                    }
                } else {
                    long l8 = TickCalculation.sub(this.startTimes[this.curIndex + 1] - l4, this.offsetTicks);
                    animation.impl_timePulse(l8);
                }
            }
        } else if (this.curIndex < n2) {
            EventHandler<ActionEvent> eventHandler;
            if (animation != null) {
                long l9 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
                if (this.oldTicks <= l9 || animation.getStatus() == Animation.Status.STOPPED && this.oldTicks != this.startTimes[this.curIndex + 1]) {
                    EventHandler<ActionEvent> eventHandler2;
                    boolean bl;
                    boolean bl4 = bl = this.oldTicks <= l9;
                    if (bl) {
                        animation.clipEnvelope.jumpTo(0L);
                    }
                    if (!this.startChild(animation, this.curIndex) && bl && (eventHandler2 = animation.getOnFinished()) != null) {
                        eventHandler2.handle(new ActionEvent(this, null));
                    }
                }
                if (animation.getStatus() == Animation.Status.RUNNING) {
                    animation.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
                }
                this.oldTicks = this.startTimes[this.curIndex + 1];
            }
            this.offsetTicks = 0L;
            ++this.curIndex;
            while (this.curIndex < n2) {
                Animation animation3 = this.cachedChildren[this.curIndex];
                animation3.clipEnvelope.jumpTo(0L);
                if (this.startChild(animation3, this.curIndex)) {
                    animation3.impl_timePulse(this.durations[this.curIndex]);
                } else {
                    eventHandler = animation3.getOnFinished();
                    if (eventHandler != null) {
                        eventHandler.handle(new ActionEvent(this, null));
                    }
                }
                this.oldTicks = this.startTimes[this.curIndex + 1];
                ++this.curIndex;
            }
            Animation animation4 = this.cachedChildren[this.curIndex];
            animation4.clipEnvelope.jumpTo(0L);
            if (this.startChild(animation4, this.curIndex)) {
                if (l4 >= this.startTimes[this.curIndex + 1]) {
                    animation4.impl_timePulse(this.durations[this.curIndex]);
                    if (l4 == l3) {
                        this.curIndex = this.end;
                    }
                } else {
                    long l10 = TickCalculation.sub(l4, TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]));
                    animation4.impl_timePulse(l10);
                }
            } else {
                eventHandler = animation4.getOnFinished();
                if (eventHandler != null) {
                    eventHandler.handle(new ActionEvent(this, null));
                }
            }
        } else {
            EventHandler<ActionEvent> eventHandler;
            if (animation != null) {
                long l11 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
                if (this.oldTicks >= this.startTimes[this.curIndex + 1] || this.oldTicks > l11 && animation.getStatus() == Animation.Status.STOPPED) {
                    EventHandler<ActionEvent> eventHandler3;
                    boolean bl;
                    boolean bl5 = bl = this.oldTicks >= this.startTimes[this.curIndex + 1];
                    if (bl) {
                        animation.clipEnvelope.jumpTo(Math.round((double)this.durations[this.curIndex] * this.rates[this.curIndex]));
                    }
                    if (!this.startChild(animation, this.curIndex) && bl && (eventHandler3 = animation.getOnFinished()) != null) {
                        eventHandler3.handle(new ActionEvent(this, null));
                    }
                }
                if (animation.getStatus() == Animation.Status.RUNNING) {
                    animation.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
                }
                this.oldTicks = this.startTimes[this.curIndex];
            }
            this.offsetTicks = 0L;
            --this.curIndex;
            while (this.curIndex > n2) {
                Animation animation5 = this.cachedChildren[this.curIndex];
                animation5.clipEnvelope.jumpTo(Math.round((double)this.durations[this.curIndex] * this.rates[this.curIndex]));
                if (this.startChild(animation5, this.curIndex)) {
                    animation5.impl_timePulse(this.durations[this.curIndex]);
                } else {
                    eventHandler = animation5.getOnFinished();
                    if (eventHandler != null) {
                        eventHandler.handle(new ActionEvent(this, null));
                    }
                }
                this.oldTicks = this.startTimes[this.curIndex];
                --this.curIndex;
            }
            Animation animation6 = this.cachedChildren[this.curIndex];
            animation6.clipEnvelope.jumpTo(Math.round((double)this.durations[this.curIndex] * this.rates[this.curIndex]));
            if (this.startChild(animation6, this.curIndex)) {
                if (l4 <= TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex])) {
                    animation6.impl_timePulse(this.durations[this.curIndex]);
                    if (l4 == 0L) {
                        this.curIndex = -1;
                    }
                } else {
                    long l12 = TickCalculation.sub(this.startTimes[this.curIndex + 1], l4);
                    animation6.impl_timePulse(l12);
                }
            } else {
                eventHandler = animation6.getOnFinished();
                if (eventHandler != null) {
                    eventHandler.handle(new ActionEvent(this, null));
                }
            }
        }
        this.oldTicks = l4;
    }

    @Override
    void impl_jumpTo(long l2, long l3, boolean bl) {
        this.impl_setCurrentTicks(l2);
        Animation.Status status = this.getStatus();
        if (status == Animation.Status.STOPPED && !bl) {
            return;
        }
        this.impl_sync(false);
        double d2 = this.calculateFraction(l2, l3);
        long l4 = Math.max(0L, Math.min(this.getCachedInterpolator().interpolate(0L, l3, d2), l3));
        int n2 = this.curIndex;
        this.curIndex = this.findNewIndex(l4);
        Animation animation = this.cachedChildren[this.curIndex];
        double d3 = this.getCurrentRate();
        long l5 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
        if (this.curIndex != n2 && status != Animation.Status.STOPPED) {
            Animation animation2;
            if (n2 != -1 && n2 != this.end && (animation2 = this.cachedChildren[n2]).getStatus() != Animation.Status.STOPPED) {
                this.cachedChildren[n2].impl_stop();
            }
            if (this.curIndex < n2) {
                int n3;
                int n4 = n3 = n2 == this.end ? this.end - 1 : n2;
                while (n3 > this.curIndex) {
                    this.cachedChildren[n3].impl_jumpTo(0L, this.durations[n3], true);
                    --n3;
                }
            } else {
                int n5;
                int n6 = n5 = n2 == -1 ? 0 : n2;
                while (n5 < this.curIndex) {
                    this.cachedChildren[n5].impl_jumpTo(this.durations[n5], this.durations[n5], true);
                    ++n5;
                }
            }
            if (l4 >= l5) {
                this.startChild(animation, this.curIndex);
                if (status == Animation.Status.PAUSED) {
                    animation.impl_pause();
                }
            }
        }
        this.offsetTicks = n2 == this.curIndex ? (d3 == 0.0 ? (long)((double)this.offsetTicks + (double)(l4 - this.oldTicks) * Math.signum(this.clipEnvelope.getCurrentRate())) : (this.offsetTicks += d3 > 0.0 ? l4 - this.oldTicks : this.oldTicks - l4)) : (d3 == 0.0 ? (this.clipEnvelope.getCurrentRate() > 0.0 ? Math.max(0L, l4 - l5) : this.startTimes[this.curIndex] + this.durations[this.curIndex] - l4) : (d3 > 0.0 ? Math.max(0L, l4 - l5) : this.startTimes[this.curIndex + 1] - l4));
        animation.clipEnvelope.jumpTo(Math.round((double)TickCalculation.sub(l4, l5) * this.rates[this.curIndex]));
        this.oldTicks = l4;
    }

    private void jumpToEnd() {
        for (int i2 = 0; i2 < this.end; ++i2) {
            if (this.forceChildSync[i2]) {
                this.cachedChildren[i2].impl_sync(true);
            }
            this.cachedChildren[i2].impl_jumpTo(this.durations[i2], this.durations[i2], true);
        }
    }

    private void jumpToBefore() {
        for (int i2 = this.end - 1; i2 >= 0; --i2) {
            if (this.forceChildSync[i2]) {
                this.cachedChildren[i2].impl_sync(true);
            }
            this.cachedChildren[i2].impl_jumpTo(0L, this.durations[i2], true);
        }
    }

    @Override
    protected void interpolate(double d2) {
    }
}

