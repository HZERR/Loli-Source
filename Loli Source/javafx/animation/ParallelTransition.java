/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.scenario.animation.AbstractMasterTimer;
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

public final class ParallelTransition
extends Transition {
    private static final Animation[] EMPTY_ANIMATION_ARRAY = new Animation[0];
    private static final double EPSILON = 1.0E-12;
    private Animation[] cachedChildren = EMPTY_ANIMATION_ARRAY;
    private long[] durations;
    private long[] delays;
    private double[] rates;
    private long[] offsetTicks;
    private boolean[] forceChildSync;
    private long oldTicks;
    private long cycleTime;
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
                for (int i2 = 0; i2 < ParallelTransition.this.cachedChildren.length; ++i2) {
                    Animation animation = ParallelTransition.this.cachedChildren[i2];
                    animation.clipEnvelope.setRate(ParallelTransition.this.rates[i2] * Math.signum(ParallelTransition.this.getCurrentRate()));
                }
                ParallelTransition.this.toggledRate = true;
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
                    animation.rateProperty().removeListener(ParallelTransition.this.childrenListener);
                    animation.totalDurationProperty().removeListener(ParallelTransition.this.childrenListener);
                    animation.delayProperty().removeListener(ParallelTransition.this.childrenListener);
                }
                for (Animation animation : change.getAddedSubList()) {
                    animation.parent = ParallelTransition.this;
                    animation.rateProperty().addListener(ParallelTransition.this.childrenListener);
                    animation.totalDurationProperty().addListener(ParallelTransition.this.childrenListener);
                    animation.delayProperty().addListener(ParallelTransition.this.childrenListener);
                }
            }
            ParallelTransition.this.childrenListener.invalidated(ParallelTransition.this.children);
        }
    }){

        @Override
        protected void onProposedChange(List<Animation> list, int ... arrn) {
            IllegalArgumentException illegalArgumentException = null;
            for (int i2 = 0; i2 < arrn.length; i2 += 2) {
                for (int i3 = arrn[i2]; i3 < arrn[i2 + 1]; ++i3) {
                    ParallelTransition.this.childrenSet.remove(ParallelTransition.this.children.get(i3));
                }
            }
            for (Animation animation : list) {
                if (animation == null) {
                    illegalArgumentException = new IllegalArgumentException("Child cannot be null");
                    break;
                }
                if (!ParallelTransition.this.childrenSet.add(animation)) {
                    illegalArgumentException = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                    break;
                }
                if (!ParallelTransition.checkCycle(animation, ParallelTransition.this)) continue;
                illegalArgumentException = new IllegalArgumentException("This change would create cycle");
                break;
            }
            if (illegalArgumentException != null) {
                ParallelTransition.this.childrenSet.clear();
                ParallelTransition.this.childrenSet.addAll(ParallelTransition.this.children);
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

    public ParallelTransition(Node node, Animation ... arranimation) {
        this.setInterpolator(Interpolator.LINEAR);
        this.setNode(node);
        this.getChildren().setAll(arranimation);
    }

    public ParallelTransition(Animation ... arranimation) {
        this((Node)null, arranimation);
    }

    public ParallelTransition(Node node) {
        this.setInterpolator(Interpolator.LINEAR);
        this.setNode(node);
    }

    public ParallelTransition() {
        this((Node)null);
    }

    ParallelTransition(AbstractMasterTimer abstractMasterTimer) {
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
            double d2 = Math.abs(animation.getRate());
            Duration duration2 = d2 < 1.0E-12 ? animation.getTotalDuration() : animation.getTotalDuration().divide(d2);
            Duration duration3 = duration2.add(animation.getDelay());
            if (duration3.isIndefinite()) {
                return Duration.INDEFINITE;
            }
            if (!duration3.greaterThan(duration)) continue;
            duration = duration3;
        }
        return duration;
    }

    private double calculateFraction(long l2, long l3) {
        double d2 = (double)l2 / (double)l3;
        return d2 <= 0.0 ? 0.0 : (d2 >= 1.0 ? 1.0 : d2);
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
    void impl_sync(boolean bl) {
        super.impl_sync(bl);
        if (bl && this.childrenChanged || this.durations == null) {
            this.cachedChildren = this.getChildren().toArray(EMPTY_ANIMATION_ARRAY);
            int n2 = this.cachedChildren.length;
            this.durations = new long[n2];
            this.delays = new long[n2];
            this.rates = new double[n2];
            this.offsetTicks = new long[n2];
            this.forceChildSync = new boolean[n2];
            this.cycleTime = 0L;
            int n3 = 0;
            for (Animation animation : this.cachedChildren) {
                this.rates[n3] = Math.abs(animation.getRate());
                if (this.rates[n3] < 1.0E-12) {
                    this.rates[n3] = 1.0;
                }
                this.durations[n3] = TickCalculation.fromDuration(animation.getTotalDuration(), this.rates[n3]);
                this.delays[n3] = TickCalculation.fromDuration(animation.getDelay());
                this.cycleTime = Math.max(this.cycleTime, TickCalculation.add(this.durations[n3], this.delays[n3]));
                this.forceChildSync[n3] = true;
                ++n3;
            }
            this.childrenChanged = false;
        } else if (bl) {
            int n4 = this.forceChildSync.length;
            for (int i2 = 0; i2 < n4; ++i2) {
                this.forceChildSync[i2] = true;
            }
        }
    }

    @Override
    void impl_pause() {
        super.impl_pause();
        for (Animation animation : this.cachedChildren) {
            if (animation.getStatus() != Animation.Status.RUNNING) continue;
            animation.impl_pause();
        }
    }

    @Override
    void impl_resume() {
        super.impl_resume();
        int n2 = 0;
        for (Animation animation : this.cachedChildren) {
            if (animation.getStatus() == Animation.Status.PAUSED) {
                animation.impl_resume();
                animation.clipEnvelope.setRate(this.rates[n2] * Math.signum(this.getCurrentRate()));
            }
            ++n2;
        }
    }

    @Override
    void impl_start(boolean bl) {
        super.impl_start(bl);
        this.toggledRate = false;
        this.rateProperty().addListener(this.rateListener);
        double d2 = this.getCurrentRate();
        long l2 = TickCalculation.fromDuration(this.getCurrentTime());
        if (d2 < 0.0) {
            this.jumpToEnd();
            if (l2 < this.cycleTime) {
                this.impl_jumpTo(l2, this.cycleTime, false);
            }
        } else {
            this.jumpToStart();
            if (l2 > 0L) {
                this.impl_jumpTo(l2, this.cycleTime, false);
            }
        }
    }

    @Override
    void impl_stop() {
        super.impl_stop();
        for (Animation animation : this.cachedChildren) {
            if (animation.getStatus() == Animation.Status.STOPPED) continue;
            animation.impl_stop();
        }
        if (this.childrenChanged) {
            this.setCycleDuration(this.computeCycleDuration());
        }
        this.rateProperty().removeListener(this.rateListener);
    }

    @Override
    @Deprecated
    public void impl_playTo(long l2, long l3) {
        int n2;
        this.impl_setCurrentTicks(l2);
        double d2 = this.calculateFraction(l2, l3);
        long l4 = Math.max(0L, Math.min(this.getCachedInterpolator().interpolate(0L, l3, d2), l3));
        if (this.toggledRate) {
            for (n2 = 0; n2 < this.cachedChildren.length; ++n2) {
                if (this.cachedChildren[n2].getStatus() != Animation.Status.RUNNING) continue;
                int n3 = n2;
                this.offsetTicks[n3] = (long)((double)this.offsetTicks[n3] - Math.signum(this.getCurrentRate()) * (double)(this.durations[n2] - 2L * (this.oldTicks - this.delays[n2])));
            }
            this.toggledRate = false;
        }
        if (this.getCurrentRate() > 0.0) {
            n2 = 0;
            for (Animation animation : this.cachedChildren) {
                if (l4 >= this.delays[n2] && (this.oldTicks <= this.delays[n2] || l4 < TickCalculation.add(this.delays[n2], this.durations[n2]) && animation.getStatus() == Animation.Status.STOPPED)) {
                    boolean bl;
                    boolean bl2 = bl = this.oldTicks <= this.delays[n2];
                    if (this.startChild(animation, n2)) {
                        animation.clipEnvelope.jumpTo(0L);
                    } else {
                        EventHandler<ActionEvent> eventHandler;
                        if (!bl || (eventHandler = animation.getOnFinished()) == null) continue;
                        eventHandler.handle(new ActionEvent(this, null));
                        continue;
                    }
                }
                if (l4 >= TickCalculation.add(this.durations[n2], this.delays[n2])) {
                    if (animation.getStatus() == Animation.Status.RUNNING) {
                        animation.impl_timePulse(TickCalculation.sub(this.durations[n2], this.offsetTicks[n2]));
                        this.offsetTicks[n2] = 0L;
                    }
                } else if (l4 > this.delays[n2]) {
                    animation.impl_timePulse(TickCalculation.sub(l4 - this.delays[n2], this.offsetTicks[n2]));
                }
                ++n2;
            }
        } else {
            n2 = 0;
            for (Animation animation : this.cachedChildren) {
                if (l4 < TickCalculation.add(this.durations[n2], this.delays[n2])) {
                    if (this.oldTicks >= TickCalculation.add(this.durations[n2], this.delays[n2]) || l4 >= this.delays[n2] && animation.getStatus() == Animation.Status.STOPPED) {
                        boolean bl;
                        boolean bl3 = bl = this.oldTicks >= TickCalculation.add(this.durations[n2], this.delays[n2]);
                        if (this.startChild(animation, n2)) {
                            animation.clipEnvelope.jumpTo(Math.round((double)this.durations[n2] * this.rates[n2]));
                        } else {
                            EventHandler<ActionEvent> eventHandler;
                            if (!bl || (eventHandler = animation.getOnFinished()) == null) continue;
                            eventHandler.handle(new ActionEvent(this, null));
                            continue;
                        }
                    }
                    if (l4 <= this.delays[n2]) {
                        if (animation.getStatus() == Animation.Status.RUNNING) {
                            animation.impl_timePulse(TickCalculation.sub(this.durations[n2], this.offsetTicks[n2]));
                            this.offsetTicks[n2] = 0L;
                        }
                    } else {
                        animation.impl_timePulse(TickCalculation.sub(TickCalculation.add(this.durations[n2], this.delays[n2]) - l4, this.offsetTicks[n2]));
                    }
                }
                ++n2;
            }
        }
        this.oldTicks = l4;
    }

    @Override
    @Deprecated
    public void impl_jumpTo(long l2, long l3, boolean bl) {
        this.impl_setCurrentTicks(l2);
        if (this.getStatus() == Animation.Status.STOPPED && !bl) {
            return;
        }
        this.impl_sync(false);
        double d2 = this.calculateFraction(l2, l3);
        long l4 = Math.max(0L, Math.min(this.getCachedInterpolator().interpolate(0L, l3, d2), l3));
        int n2 = 0;
        for (Animation animation : this.cachedChildren) {
            Animation.Status status = animation.getStatus();
            if (l4 <= this.delays[n2]) {
                this.offsetTicks[n2] = 0L;
                if (status != Animation.Status.STOPPED) {
                    animation.clipEnvelope.jumpTo(0L);
                    animation.impl_stop();
                } else if (TickCalculation.fromDuration(animation.getCurrentTime()) != 0L) {
                    animation.impl_jumpTo(0L, this.durations[n2], true);
                }
            } else if (l4 >= TickCalculation.add(this.durations[n2], this.delays[n2])) {
                this.offsetTicks[n2] = 0L;
                if (status != Animation.Status.STOPPED) {
                    animation.clipEnvelope.jumpTo(Math.round((double)this.durations[n2] * this.rates[n2]));
                    animation.impl_stop();
                } else if (TickCalculation.fromDuration(animation.getCurrentTime()) != this.durations[n2]) {
                    animation.impl_jumpTo(this.durations[n2], this.durations[n2], true);
                }
            } else {
                if (status == Animation.Status.STOPPED) {
                    this.startChild(animation, n2);
                    if (this.getStatus() == Animation.Status.PAUSED) {
                        animation.impl_pause();
                    }
                    this.offsetTicks[n2] = this.getCurrentRate() > 0.0 ? l4 - this.delays[n2] : TickCalculation.add(this.durations[n2], this.delays[n2]) - l4;
                } else if (status == Animation.Status.PAUSED) {
                    int n3 = n2;
                    this.offsetTicks[n3] = (long)((double)this.offsetTicks[n3] + (double)(l4 - this.oldTicks) * Math.signum(this.clipEnvelope.getCurrentRate()));
                } else {
                    int n4 = n2;
                    this.offsetTicks[n4] = this.offsetTicks[n4] + (this.getCurrentRate() > 0.0 ? l4 - this.oldTicks : this.oldTicks - l4);
                }
                animation.clipEnvelope.jumpTo(Math.round((double)TickCalculation.sub(l4, this.delays[n2]) * this.rates[n2]));
            }
            ++n2;
        }
        this.oldTicks = l4;
    }

    @Override
    protected void interpolate(double d2) {
    }

    private void jumpToEnd() {
        for (int i2 = 0; i2 < this.cachedChildren.length; ++i2) {
            if (this.forceChildSync[i2]) {
                this.cachedChildren[i2].impl_sync(true);
            }
            this.cachedChildren[i2].impl_jumpTo(this.durations[i2], this.durations[i2], true);
        }
    }

    private void jumpToStart() {
        for (int i2 = this.cachedChildren.length - 1; i2 >= 0; --i2) {
            if (this.forceChildSync[i2]) {
                this.cachedChildren[i2].impl_sync(true);
            }
            this.cachedChildren[i2].impl_jumpTo(0L, this.durations[i2], true);
        }
    }
}

