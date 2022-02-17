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

public class MotionBlur
extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty radius;
    private DoubleProperty angle;

    public MotionBlur() {
    }

    public MotionBlur(double d2, double d3) {
        this.setAngle(d2);
        this.setRadius(d3);
    }

    @Override
    com.sun.scenario.effect.MotionBlur impl_createImpl() {
        return new com.sun.scenario.effect.MotionBlur();
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

    public final void setRadius(double d2) {
        this.radiusProperty().set(d2);
    }

    public final double getRadius() {
        return this.radius == null ? 10.0 : this.radius.get();
    }

    public final DoubleProperty radiusProperty() {
        if (this.radius == null) {
            this.radius = new DoublePropertyBase(10.0){

                @Override
                public void invalidated() {
                    MotionBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    MotionBlur.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return MotionBlur.this;
                }

                @Override
                public String getName() {
                    return "radius";
                }
            };
        }
        return this.radius;
    }

    public final void setAngle(double d2) {
        this.angleProperty().set(d2);
    }

    public final double getAngle() {
        return this.angle == null ? 0.0 : this.angle.get();
    }

    public final DoubleProperty angleProperty() {
        if (this.angle == null) {
            this.angle = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    MotionBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    MotionBlur.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return MotionBlur.this;
                }

                @Override
                public String getName() {
                    return "angle";
                }
            };
        }
        return this.angle;
    }

    private float getClampedRadius() {
        return (float)Utils.clamp(0.0, this.getRadius(), 63.0);
    }

    @Override
    void impl_update() {
        Effect effect = this.getInput();
        if (effect != null) {
            effect.impl_sync();
        }
        com.sun.scenario.effect.MotionBlur motionBlur = (com.sun.scenario.effect.MotionBlur)this.impl_getImpl();
        motionBlur.setInput(effect == null ? null : effect.impl_getImpl());
        motionBlur.setRadius(this.getClampedRadius());
        motionBlur.setAngle((float)Math.toRadians(this.getAngle()));
    }

    private int getHPad() {
        return (int)Math.ceil(Math.abs(Math.cos(Math.toRadians(this.getAngle()))) * (double)this.getClampedRadius());
    }

    private int getVPad() {
        return (int)Math.ceil(Math.abs(Math.sin(Math.toRadians(this.getAngle()))) * (double)this.getClampedRadius());
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        baseBounds = MotionBlur.getInputBounds(baseBounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, this.getInput());
        int n2 = this.getHPad();
        int n3 = this.getVPad();
        baseBounds = baseBounds.deriveWithPadding(n2, n3, 0.0f);
        return MotionBlur.transformBounds(baseTransform, baseBounds);
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        MotionBlur motionBlur = new MotionBlur(this.getAngle(), this.getRadius());
        motionBlur.setInput(motionBlur.getInput());
        return motionBlur;
    }
}

