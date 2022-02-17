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

public class SepiaTone
extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty level;

    public SepiaTone() {
    }

    public SepiaTone(double d2) {
        this.setLevel(d2);
    }

    @Override
    com.sun.scenario.effect.SepiaTone impl_createImpl() {
        return new com.sun.scenario.effect.SepiaTone();
    }

    public final void setInput(Effect effect) {
        this.inputProperty().set(effect);
    }

    public final Effect getInput() {
        return this.input == null ? null : (Effect)this.input.get();
    }

    public final ObjectProperty<Effect> inputProperty() {
        if (this.input == null) {
            this.input = new Effect.EffectInputProperty("input");
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

    public final void setLevel(double d2) {
        this.levelProperty().set(d2);
    }

    public final double getLevel() {
        return this.level == null ? 1.0 : this.level.get();
    }

    public final DoubleProperty levelProperty() {
        if (this.level == null) {
            this.level = new DoublePropertyBase(1.0){

                @Override
                public void invalidated() {
                    SepiaTone.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return SepiaTone.this;
                }

                @Override
                public String getName() {
                    return "level";
                }
            };
        }
        return this.level;
    }

    @Override
    void impl_update() {
        Effect effect = this.getInput();
        if (effect != null) {
            effect.impl_sync();
        }
        com.sun.scenario.effect.SepiaTone sepiaTone = (com.sun.scenario.effect.SepiaTone)this.impl_getImpl();
        sepiaTone.setInput(effect == null ? null : effect.impl_getImpl());
        sepiaTone.setLevel((float)Utils.clamp(0.0, this.getLevel(), 1.0));
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        return SepiaTone.getInputBounds(baseBounds, baseTransform, node, boundsAccessor, this.getInput());
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        SepiaTone sepiaTone = new SepiaTone(this.getLevel());
        sepiaTone.setInput(this.getInput());
        return sepiaTone;
    }
}

