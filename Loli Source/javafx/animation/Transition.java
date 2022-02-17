/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import com.sun.scenario.animation.AbstractMasterTimer;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public abstract class Transition
extends Animation {
    private ObjectProperty<Interpolator> interpolator;
    private static final Interpolator DEFAULT_INTERPOLATOR = Interpolator.EASE_BOTH;
    private Interpolator cachedInterpolator;

    public final void setInterpolator(Interpolator interpolator) {
        if (this.interpolator != null || !DEFAULT_INTERPOLATOR.equals(interpolator)) {
            this.interpolatorProperty().set(interpolator);
        }
    }

    public final Interpolator getInterpolator() {
        return this.interpolator == null ? DEFAULT_INTERPOLATOR : (Interpolator)this.interpolator.get();
    }

    public final ObjectProperty<Interpolator> interpolatorProperty() {
        if (this.interpolator == null) {
            this.interpolator = new SimpleObjectProperty<Interpolator>(this, "interpolator", DEFAULT_INTERPOLATOR);
        }
        return this.interpolator;
    }

    protected Interpolator getCachedInterpolator() {
        return this.cachedInterpolator;
    }

    public Transition(double d2) {
        super(d2);
    }

    public Transition() {
    }

    Transition(AbstractMasterTimer abstractMasterTimer) {
        super(abstractMasterTimer);
    }

    protected Node getParentTargetNode() {
        return this.parent != null && this.parent instanceof Transition ? ((Transition)this.parent).getParentTargetNode() : null;
    }

    protected abstract void interpolate(double var1);

    private double calculateFraction(long l2, long l3) {
        double d2 = l3 <= 0L ? 1.0 : (double)l2 / (double)l3;
        return this.cachedInterpolator.interpolate(0.0, 1.0, d2);
    }

    @Override
    boolean impl_startable(boolean bl) {
        return super.impl_startable(bl) && (this.getInterpolator() != null || !bl && this.cachedInterpolator != null);
    }

    @Override
    void impl_sync(boolean bl) {
        super.impl_sync(bl);
        if (bl || this.cachedInterpolator == null) {
            this.cachedInterpolator = this.getInterpolator();
        }
    }

    @Override
    void impl_playTo(long l2, long l3) {
        this.impl_setCurrentTicks(l2);
        this.interpolate(this.calculateFraction(l2, l3));
    }

    @Override
    void impl_jumpTo(long l2, long l3, boolean bl) {
        this.impl_setCurrentTicks(l2);
        if (this.getStatus() != Animation.Status.STOPPED || bl) {
            this.impl_sync(false);
            this.interpolate(this.calculateFraction(l2, l3));
        }
    }
}

