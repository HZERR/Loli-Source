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

public class Reflection
extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty topOffset;
    private DoubleProperty topOpacity;
    private DoubleProperty bottomOpacity;
    private DoubleProperty fraction;

    public Reflection() {
    }

    public Reflection(double d2, double d3, double d4, double d5) {
        this.setBottomOpacity(d5);
        this.setTopOffset(d2);
        this.setTopOpacity(d4);
        this.setFraction(d3);
    }

    @Override
    com.sun.scenario.effect.Reflection impl_createImpl() {
        return new com.sun.scenario.effect.Reflection();
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

    public final void setTopOffset(double d2) {
        this.topOffsetProperty().set(d2);
    }

    public final double getTopOffset() {
        return this.topOffset == null ? 0.0 : this.topOffset.get();
    }

    public final DoubleProperty topOffsetProperty() {
        if (this.topOffset == null) {
            this.topOffset = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    Reflection.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return Reflection.this;
                }

                @Override
                public String getName() {
                    return "topOffset";
                }
            };
        }
        return this.topOffset;
    }

    public final void setTopOpacity(double d2) {
        this.topOpacityProperty().set(d2);
    }

    public final double getTopOpacity() {
        return this.topOpacity == null ? 0.5 : this.topOpacity.get();
    }

    public final DoubleProperty topOpacityProperty() {
        if (this.topOpacity == null) {
            this.topOpacity = new DoublePropertyBase(0.5){

                @Override
                public void invalidated() {
                    Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return Reflection.this;
                }

                @Override
                public String getName() {
                    return "topOpacity";
                }
            };
        }
        return this.topOpacity;
    }

    public final void setBottomOpacity(double d2) {
        this.bottomOpacityProperty().set(d2);
    }

    public final double getBottomOpacity() {
        return this.bottomOpacity == null ? 0.0 : this.bottomOpacity.get();
    }

    public final DoubleProperty bottomOpacityProperty() {
        if (this.bottomOpacity == null) {
            this.bottomOpacity = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return Reflection.this;
                }

                @Override
                public String getName() {
                    return "bottomOpacity";
                }
            };
        }
        return this.bottomOpacity;
    }

    public final void setFraction(double d2) {
        this.fractionProperty().set(d2);
    }

    public final double getFraction() {
        return this.fraction == null ? 0.75 : this.fraction.get();
    }

    public final DoubleProperty fractionProperty() {
        if (this.fraction == null) {
            this.fraction = new DoublePropertyBase(0.75){

                @Override
                public void invalidated() {
                    Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    Reflection.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return Reflection.this;
                }

                @Override
                public String getName() {
                    return "fraction";
                }
            };
        }
        return this.fraction;
    }

    private float getClampedFraction() {
        return (float)Utils.clamp(0.0, this.getFraction(), 1.0);
    }

    private float getClampedBottomOpacity() {
        return (float)Utils.clamp(0.0, this.getBottomOpacity(), 1.0);
    }

    private float getClampedTopOpacity() {
        return (float)Utils.clamp(0.0, this.getTopOpacity(), 1.0);
    }

    @Override
    void impl_update() {
        Effect effect = this.getInput();
        if (effect != null) {
            effect.impl_sync();
        }
        com.sun.scenario.effect.Reflection reflection = (com.sun.scenario.effect.Reflection)this.impl_getImpl();
        reflection.setInput(effect == null ? null : effect.impl_getImpl());
        reflection.setFraction(this.getClampedFraction());
        reflection.setTopOffset((float)this.getTopOffset());
        reflection.setBottomOpacity(this.getClampedBottomOpacity());
        reflection.setTopOpacity(this.getClampedTopOpacity());
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        baseBounds = Reflection.getInputBounds(baseBounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, this.getInput());
        baseBounds.roundOut();
        float f2 = baseBounds.getMinX();
        float f3 = baseBounds.getMaxY() + (float)this.getTopOffset();
        float f4 = baseBounds.getMinZ();
        float f5 = baseBounds.getMaxX();
        float f6 = f3 + this.getClampedFraction() * baseBounds.getHeight();
        float f7 = baseBounds.getMaxZ();
        BaseBounds baseBounds2 = BaseBounds.getInstance(f2, f3, f4, f5, f6, f7);
        baseBounds2 = baseBounds2.deriveWithUnion(baseBounds);
        return Reflection.transformBounds(baseTransform, baseBounds2);
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        Reflection reflection = new Reflection(this.getTopOffset(), this.getFraction(), this.getTopOpacity(), this.getBottomOpacity());
        reflection.setInput(reflection.getInput());
        return reflection;
    }
}

