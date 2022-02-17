/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.EffectChangeListener;

public abstract class Effect {
    private com.sun.scenario.effect.Effect peer;
    private IntegerProperty effectDirty = new SimpleIntegerProperty(this, "effectDirty");

    protected Effect() {
        this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
    }

    void effectBoundsChanged() {
        this.toggleDirty(EffectDirtyBits.BOUNDS_CHANGED);
    }

    abstract com.sun.scenario.effect.Effect impl_createImpl();

    @Deprecated
    public com.sun.scenario.effect.Effect impl_getImpl() {
        if (this.peer == null) {
            this.peer = this.impl_createImpl();
        }
        return this.peer;
    }

    private void setEffectDirty(int n2) {
        this.impl_effectDirtyProperty().set(n2);
    }

    @Deprecated
    public final IntegerProperty impl_effectDirtyProperty() {
        return this.effectDirty;
    }

    @Deprecated
    public final boolean impl_isEffectDirty() {
        return this.isEffectDirty(EffectDirtyBits.EFFECT_DIRTY);
    }

    final void markDirty(EffectDirtyBits effectDirtyBits) {
        this.setEffectDirty(this.effectDirty.get() | effectDirtyBits.getMask());
    }

    private void toggleDirty(EffectDirtyBits effectDirtyBits) {
        this.setEffectDirty(this.effectDirty.get() ^ effectDirtyBits.getMask());
    }

    private boolean isEffectDirty(EffectDirtyBits effectDirtyBits) {
        return (this.effectDirty.get() & effectDirtyBits.getMask()) != 0;
    }

    private void clearEffectDirty(EffectDirtyBits effectDirtyBits) {
        this.setEffectDirty(this.effectDirty.get() & ~effectDirtyBits.getMask());
    }

    @Deprecated
    public final void impl_sync() {
        if (this.isEffectDirty(EffectDirtyBits.EFFECT_DIRTY)) {
            this.impl_update();
            this.clearEffectDirty(EffectDirtyBits.EFFECT_DIRTY);
        }
    }

    abstract void impl_update();

    abstract boolean impl_checkChainContains(Effect var1);

    boolean impl_containsCycles(Effect effect) {
        return effect != null && (effect == this || effect.impl_checkChainContains(this));
    }

    @Deprecated
    public abstract BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4);

    @Deprecated
    public abstract Effect impl_copy();

    static BaseBounds transformBounds(BaseTransform baseTransform, BaseBounds baseBounds) {
        if (baseTransform == null || baseTransform.isIdentity()) {
            return baseBounds;
        }
        BaseBounds baseBounds2 = new RectBounds();
        baseBounds2 = baseTransform.transform(baseBounds, baseBounds2);
        return baseBounds2;
    }

    static int getKernelSize(float f2, int n2) {
        int n3 = (int)Math.ceil(f2);
        if (n3 < 1) {
            n3 = 1;
        }
        n3 = (n3 - 1) * n2 + 1;
        return (n3 |= 1) / 2;
    }

    static BaseBounds getShadowBounds(BaseBounds baseBounds, BaseTransform baseTransform, float f2, float f3, BlurType blurType) {
        int n2 = 0;
        int n3 = 0;
        switch (blurType) {
            case GAUSSIAN: {
                float f4 = f2 < 1.0f ? 0.0f : (f2 - 1.0f) / 2.0f;
                float f5 = f3 < 1.0f ? 0.0f : (f3 - 1.0f) / 2.0f;
                n2 = (int)Math.ceil(f4);
                n3 = (int)Math.ceil(f5);
                break;
            }
            case ONE_PASS_BOX: {
                n2 = Effect.getKernelSize(Math.round(f2 / 3.0f), 1);
                n3 = Effect.getKernelSize(Math.round(f3 / 3.0f), 1);
                break;
            }
            case TWO_PASS_BOX: {
                n2 = Effect.getKernelSize(Math.round(f2 / 3.0f), 2);
                n3 = Effect.getKernelSize(Math.round(f3 / 3.0f), 2);
                break;
            }
            case THREE_PASS_BOX: {
                n2 = Effect.getKernelSize(Math.round(f2 / 3.0f), 3);
                n3 = Effect.getKernelSize(Math.round(f3 / 3.0f), 3);
            }
        }
        baseBounds = baseBounds.deriveWithPadding(n2, n3, 0.0f);
        return Effect.transformBounds(baseTransform, baseBounds);
    }

    static BaseBounds getInputBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor, Effect effect) {
        baseBounds = effect != null ? effect.impl_getBounds(baseBounds, baseTransform, node, boundsAccessor) : boundsAccessor.getGeomBounds(baseBounds, baseTransform, node);
        return baseBounds;
    }

    class EffectInputProperty
    extends ObjectPropertyBase<Effect> {
        private final String propertyName;
        private Effect validInput = null;
        private final EffectInputChangeListener effectChangeListener = new EffectInputChangeListener();

        public EffectInputProperty(String string) {
            this.propertyName = string;
        }

        @Override
        public void invalidated() {
            Effect effect = (Effect)super.get();
            if (Effect.this.impl_containsCycles(effect)) {
                if (this.isBound()) {
                    this.unbind();
                    this.set(this.validInput);
                    throw new IllegalArgumentException("Cycle in effect chain detected, binding was set to incorrect value, unbinding the input property");
                }
                this.set(this.validInput);
                throw new IllegalArgumentException("Cycle in effect chain detected");
            }
            this.validInput = effect;
            this.effectChangeListener.register(effect);
            Effect.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            Effect.this.effectBoundsChanged();
        }

        @Override
        public Object getBean() {
            return Effect.this;
        }

        @Override
        public String getName() {
            return this.propertyName;
        }
    }

    class EffectInputChangeListener
    extends EffectChangeListener {
        private int oldBits;

        EffectInputChangeListener() {
        }

        public void register(Effect effect) {
            super.register(effect == null ? null : effect.impl_effectDirtyProperty());
            if (effect != null) {
                this.oldBits = effect.impl_effectDirtyProperty().get();
            }
        }

        @Override
        public void invalidated(Observable observable) {
            int n2 = ((IntegerProperty)observable).get();
            int n3 = n2 ^ this.oldBits;
            this.oldBits = n2;
            if (EffectDirtyBits.isSet(n3, EffectDirtyBits.EFFECT_DIRTY) && EffectDirtyBits.isSet(n2, EffectDirtyBits.EFFECT_DIRTY)) {
                Effect.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }
            if (EffectDirtyBits.isSet(n3, EffectDirtyBits.BOUNDS_CHANGED)) {
                Effect.this.toggleDirty(EffectDirtyBits.BOUNDS_CHANGED);
            }
        }
    }
}

