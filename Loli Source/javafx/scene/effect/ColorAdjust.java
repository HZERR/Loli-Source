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

public class ColorAdjust
extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty hue;
    private DoubleProperty saturation;
    private DoubleProperty brightness;
    private DoubleProperty contrast;

    public ColorAdjust() {
    }

    public ColorAdjust(double d2, double d3, double d4, double d5) {
        this.setBrightness(d4);
        this.setContrast(d5);
        this.setHue(d2);
        this.setSaturation(d3);
    }

    @Override
    com.sun.scenario.effect.ColorAdjust impl_createImpl() {
        return new com.sun.scenario.effect.ColorAdjust();
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

    public final void setHue(double d2) {
        this.hueProperty().set(d2);
    }

    public final double getHue() {
        return this.hue == null ? 0.0 : this.hue.get();
    }

    public final DoubleProperty hueProperty() {
        if (this.hue == null) {
            this.hue = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorAdjust.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return ColorAdjust.this;
                }

                @Override
                public String getName() {
                    return "hue";
                }
            };
        }
        return this.hue;
    }

    public final void setSaturation(double d2) {
        this.saturationProperty().set(d2);
    }

    public final double getSaturation() {
        return this.saturation == null ? 0.0 : this.saturation.get();
    }

    public final DoubleProperty saturationProperty() {
        if (this.saturation == null) {
            this.saturation = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorAdjust.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return ColorAdjust.this;
                }

                @Override
                public String getName() {
                    return "saturation";
                }
            };
        }
        return this.saturation;
    }

    public final void setBrightness(double d2) {
        this.brightnessProperty().set(d2);
    }

    public final double getBrightness() {
        return this.brightness == null ? 0.0 : this.brightness.get();
    }

    public final DoubleProperty brightnessProperty() {
        if (this.brightness == null) {
            this.brightness = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorAdjust.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return ColorAdjust.this;
                }

                @Override
                public String getName() {
                    return "brightness";
                }
            };
        }
        return this.brightness;
    }

    public final void setContrast(double d2) {
        this.contrastProperty().set(d2);
    }

    public final double getContrast() {
        return this.contrast == null ? 0.0 : this.contrast.get();
    }

    public final DoubleProperty contrastProperty() {
        if (this.contrast == null) {
            this.contrast = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorAdjust.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return ColorAdjust.this;
                }

                @Override
                public String getName() {
                    return "contrast";
                }
            };
        }
        return this.contrast;
    }

    @Override
    void impl_update() {
        Effect effect = this.getInput();
        if (effect != null) {
            effect.impl_sync();
        }
        com.sun.scenario.effect.ColorAdjust colorAdjust = (com.sun.scenario.effect.ColorAdjust)this.impl_getImpl();
        colorAdjust.setInput(effect == null ? null : effect.impl_getImpl());
        colorAdjust.setHue((float)Utils.clamp(-1.0, this.getHue(), 1.0));
        colorAdjust.setSaturation((float)Utils.clamp(-1.0, this.getSaturation(), 1.0));
        colorAdjust.setBrightness((float)Utils.clamp(-1.0, this.getBrightness(), 1.0));
        colorAdjust.setContrast((float)Utils.clamp(-1.0, this.getContrast(), 1.0));
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        return ColorAdjust.getInputBounds(baseBounds, baseTransform, node, boundsAccessor, this.getInput());
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        ColorAdjust colorAdjust = new ColorAdjust(this.getHue(), this.getSaturation(), this.getBrightness(), this.getContrast());
        colorAdjust.setInput(colorAdjust.getInput());
        return colorAdjust;
    }
}

