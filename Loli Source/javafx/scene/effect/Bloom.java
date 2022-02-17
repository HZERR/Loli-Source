/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.util.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

public class Bloom
extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty threshold;

    public Bloom() {
    }

    public Bloom(double d2) {
        this.setThreshold(d2);
    }

    @Override
    com.sun.scenario.effect.Bloom impl_createImpl() {
        return new com.sun.scenario.effect.Bloom();
    }

    public final void setInput(Effect effect) {
        this.inputProperty().set(effect);
    }

    public final Effect getInput() {
        return this.input == null ? null : (Effect)this.input.get();
    }

    public final ObjectProperty<Effect> inputProperty() {
        if (this.input == null) {
            this.input = new Effect.EffectInputProperty(this, "input");
        }
        return this.input;
    }

    @Override
    boolean impl_checkChainContains(Effect effect) {
        Effect effect2 = this.getInput();
        if (effect2 == null) {
            return false;
        }
        if (effect2 == effect) {
            return true;
        }
        return effect2.impl_checkChainContains(effect);
    }

    public final void setThreshold(double d2) {
        this.thresholdProperty().set(d2);
    }

    public final double getThreshold() {
        return this.threshold == null ? 0.3 : this.threshold.get();
    }

    public final DoubleProperty thresholdProperty() {
        if (this.threshold == null) {
            this.threshold = new DoublePropertyBase(0.3){

                @Override
                public void invalidated() {
                    Bloom.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return Bloom.this;
                }

                @Override
                public String getName() {
                    return "threshold";
                }
            };
        }
        return this.threshold;
    }

    @Override
    void impl_update() {
        Effect effect = this.getInput();
        if (effect != null) {
            effect.impl_sync();
        }
        com.sun.scenario.effect.Bloom bloom = (com.sun.scenario.effect.Bloom)this.impl_getImpl();
        bloom.setInput(effect == null ? null : effect.impl_getImpl());
        bloom.setThreshold((float)Utils.clamp(0.0, this.getThreshold(), 1.0));
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        return Bloom.getInputBounds(baseBounds, baseTransform, node, boundsAccessor, this.getInput());
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        Bloom bloom = new Bloom(this.getThreshold());
        bloom.setInput(this.getInput());
        return bloom;
    }
}

