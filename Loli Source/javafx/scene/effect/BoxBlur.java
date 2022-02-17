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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

public class BoxBlur
extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty width;
    private DoubleProperty height;
    private IntegerProperty iterations;

    public BoxBlur() {
    }

    public BoxBlur(double d2, double d3, int n2) {
        this.setWidth(d2);
        this.setHeight(d3);
        this.setIterations(n2);
    }

    @Override
    com.sun.scenario.effect.BoxBlur impl_createImpl() {
        return new com.sun.scenario.effect.BoxBlur();
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

    public final void setWidth(double d2) {
        this.widthProperty().set(d2);
    }

    public final double getWidth() {
        return this.width == null ? 5.0 : this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new DoublePropertyBase(5.0){

                @Override
                public void invalidated() {
                    BoxBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    BoxBlur.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return BoxBlur.this;
                }

                @Override
                public String getName() {
                    return "width";
                }
            };
        }
        return this.width;
    }

    public final void setHeight(double d2) {
        this.heightProperty().set(d2);
    }

    public final double getHeight() {
        return this.height == null ? 5.0 : this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new DoublePropertyBase(5.0){

                @Override
                public void invalidated() {
                    BoxBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    BoxBlur.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return BoxBlur.this;
                }

                @Override
                public String getName() {
                    return "height";
                }
            };
        }
        return this.height;
    }

    public final void setIterations(int n2) {
        this.iterationsProperty().set(n2);
    }

    public final int getIterations() {
        return this.iterations == null ? 1 : this.iterations.get();
    }

    public final IntegerProperty iterationsProperty() {
        if (this.iterations == null) {
            this.iterations = new IntegerPropertyBase(1){

                @Override
                public void invalidated() {
                    BoxBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    BoxBlur.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return BoxBlur.this;
                }

                @Override
                public String getName() {
                    return "iterations";
                }
            };
        }
        return this.iterations;
    }

    private int getClampedWidth() {
        return Utils.clamp(0, (int)this.getWidth(), 255);
    }

    private int getClampedHeight() {
        return Utils.clamp(0, (int)this.getHeight(), 255);
    }

    private int getClampedIterations() {
        return Utils.clamp(0, this.getIterations(), 3);
    }

    @Override
    void impl_update() {
        Effect effect = this.getInput();
        if (effect != null) {
            effect.impl_sync();
        }
        com.sun.scenario.effect.BoxBlur boxBlur = (com.sun.scenario.effect.BoxBlur)this.impl_getImpl();
        boxBlur.setInput(effect == null ? null : effect.impl_getImpl());
        boxBlur.setHorizontalSize(this.getClampedWidth());
        boxBlur.setVerticalSize(this.getClampedHeight());
        boxBlur.setPasses(this.getClampedIterations());
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        baseBounds = BoxBlur.getInputBounds(baseBounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, this.getInput());
        int n2 = this.getClampedIterations();
        int n3 = BoxBlur.getKernelSize(this.getClampedWidth(), n2);
        int n4 = BoxBlur.getKernelSize(this.getClampedHeight(), n2);
        baseBounds = baseBounds.deriveWithPadding(n3, n4, 0.0f);
        return BoxBlur.transformBounds(baseTransform, baseBounds);
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        BoxBlur boxBlur = new BoxBlur(this.getWidth(), this.getHeight(), this.getIterations());
        boxBlur.setInput(this.getInput());
        return boxBlur;
    }
}

