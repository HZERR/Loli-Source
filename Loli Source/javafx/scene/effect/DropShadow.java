/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

public class DropShadow
extends Effect {
    private boolean changeIsLocal;
    private ObjectProperty<Effect> input;
    private DoubleProperty radius;
    private DoubleProperty width;
    private DoubleProperty height;
    private ObjectProperty<BlurType> blurType;
    private DoubleProperty spread;
    private ObjectProperty<Color> color;
    private DoubleProperty offsetX;
    private DoubleProperty offsetY;

    public DropShadow() {
    }

    public DropShadow(double d2, Color color) {
        this.setRadius(d2);
        this.setColor(color);
    }

    public DropShadow(double d2, double d3, double d4, Color color) {
        this.setRadius(d2);
        this.setOffsetX(d3);
        this.setOffsetY(d4);
        this.setColor(color);
    }

    public DropShadow(BlurType blurType, Color color, double d2, double d3, double d4, double d5) {
        this.setBlurType(blurType);
        this.setColor(color);
        this.setRadius(d2);
        this.setSpread(d3);
        this.setOffsetX(d4);
        this.setOffsetY(d5);
    }

    @Override
    com.sun.scenario.effect.DropShadow impl_createImpl() {
        return new com.sun.scenario.effect.DropShadow();
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
                    double d2 = DropShadow.this.getRadius();
                    if (!DropShadow.this.changeIsLocal) {
                        DropShadow.this.changeIsLocal = true;
                        DropShadow.this.updateRadius(d2);
                        DropShadow.this.changeIsLocal = false;
                        DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                        DropShadow.this.effectBoundsChanged();
                    }
                }

                @Override
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override
                public String getName() {
                    return "radius";
                }
            };
        }
        return this.radius;
    }

    private void updateRadius(double d2) {
        double d3 = d2 * 2.0 + 1.0;
        if (this.width != null && this.width.isBound()) {
            if (this.height == null || !this.height.isBound()) {
                this.setHeight(d3 * 2.0 - this.getWidth());
            }
        } else if (this.height != null && this.height.isBound()) {
            this.setWidth(d3 * 2.0 - this.getHeight());
        } else {
            this.setWidth(d3);
            this.setHeight(d3);
        }
    }

    public final void setWidth(double d2) {
        this.widthProperty().set(d2);
    }

    public final double getWidth() {
        return this.width == null ? 21.0 : this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new DoublePropertyBase(21.0){

                @Override
                public void invalidated() {
                    double d2 = DropShadow.this.getWidth();
                    if (!DropShadow.this.changeIsLocal) {
                        DropShadow.this.changeIsLocal = true;
                        DropShadow.this.updateWidth(d2);
                        DropShadow.this.changeIsLocal = false;
                        DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                        DropShadow.this.effectBoundsChanged();
                    }
                }

                @Override
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override
                public String getName() {
                    return "width";
                }
            };
        }
        return this.width;
    }

    private void updateWidth(double d2) {
        if (this.radius == null || !this.radius.isBound()) {
            double d3 = (d2 + this.getHeight()) / 2.0;
            if ((d3 = (d3 - 1.0) / 2.0) < 0.0) {
                d3 = 0.0;
            }
            this.setRadius(d3);
        } else if (this.height == null || !this.height.isBound()) {
            double d4 = this.getRadius() * 2.0 + 1.0;
            this.setHeight(d4 * 2.0 - d2);
        }
    }

    public final void setHeight(double d2) {
        this.heightProperty().set(d2);
    }

    public final double getHeight() {
        return this.height == null ? 21.0 : this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new DoublePropertyBase(21.0){

                @Override
                public void invalidated() {
                    double d2 = DropShadow.this.getHeight();
                    if (!DropShadow.this.changeIsLocal) {
                        DropShadow.this.changeIsLocal = true;
                        DropShadow.this.updateHeight(d2);
                        DropShadow.this.changeIsLocal = false;
                        DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                        DropShadow.this.effectBoundsChanged();
                    }
                }

                @Override
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override
                public String getName() {
                    return "height";
                }
            };
        }
        return this.height;
    }

    private void updateHeight(double d2) {
        if (this.radius == null || !this.radius.isBound()) {
            double d3 = (this.getWidth() + d2) / 2.0;
            if ((d3 = (d3 - 1.0) / 2.0) < 0.0) {
                d3 = 0.0;
            }
            this.setRadius(d3);
        } else if (this.width == null || !this.width.isBound()) {
            double d4 = this.getRadius() * 2.0 + 1.0;
            this.setWidth(d4 * 2.0 - d2);
        }
    }

    public final void setBlurType(BlurType blurType) {
        this.blurTypeProperty().set(blurType);
    }

    public final BlurType getBlurType() {
        return this.blurType == null ? BlurType.THREE_PASS_BOX : (BlurType)((Object)this.blurType.get());
    }

    public final ObjectProperty<BlurType> blurTypeProperty() {
        if (this.blurType == null) {
            this.blurType = new ObjectPropertyBase<BlurType>(BlurType.THREE_PASS_BOX){

                @Override
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    DropShadow.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override
                public String getName() {
                    return "blurType";
                }
            };
        }
        return this.blurType;
    }

    public final void setSpread(double d2) {
        this.spreadProperty().set(d2);
    }

    public final double getSpread() {
        return this.spread == null ? 0.0 : this.spread.get();
    }

    public final DoubleProperty spreadProperty() {
        if (this.spread == null) {
            this.spread = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override
                public String getName() {
                    return "spread";
                }
            };
        }
        return this.spread;
    }

    public final void setColor(Color color) {
        this.colorProperty().set(color);
    }

    public final Color getColor() {
        return this.color == null ? Color.BLACK : (Color)this.color.get();
    }

    public final ObjectProperty<Color> colorProperty() {
        if (this.color == null) {
            this.color = new ObjectPropertyBase<Color>(Color.BLACK){

                @Override
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override
                public String getName() {
                    return "color";
                }
            };
        }
        return this.color;
    }

    public final void setOffsetX(double d2) {
        this.offsetXProperty().set(d2);
    }

    public final double getOffsetX() {
        return this.offsetX == null ? 0.0 : this.offsetX.get();
    }

    public final DoubleProperty offsetXProperty() {
        if (this.offsetX == null) {
            this.offsetX = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    DropShadow.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override
                public String getName() {
                    return "offsetX";
                }
            };
        }
        return this.offsetX;
    }

    public final void setOffsetY(double d2) {
        this.offsetYProperty().set(d2);
    }

    public final double getOffsetY() {
        return this.offsetY == null ? 0.0 : this.offsetY.get();
    }

    public final DoubleProperty offsetYProperty() {
        if (this.offsetY == null) {
            this.offsetY = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    DropShadow.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override
                public String getName() {
                    return "offsetY";
                }
            };
        }
        return this.offsetY;
    }

    private float getClampedWidth() {
        return (float)Utils.clamp(0.0, this.getWidth(), 255.0);
    }

    private float getClampedHeight() {
        return (float)Utils.clamp(0.0, this.getHeight(), 255.0);
    }

    private float getClampedSpread() {
        return (float)Utils.clamp(0.0, this.getSpread(), 1.0);
    }

    private Color getColorInternal() {
        Color color = this.getColor();
        return color == null ? Color.BLACK : color;
    }

    private BlurType getBlurTypeInternal() {
        BlurType blurType = this.getBlurType();
        return blurType == null ? BlurType.THREE_PASS_BOX : blurType;
    }

    @Override
    void impl_update() {
        Effect effect = this.getInput();
        if (effect != null) {
            effect.impl_sync();
        }
        com.sun.scenario.effect.DropShadow dropShadow = (com.sun.scenario.effect.DropShadow)this.impl_getImpl();
        dropShadow.setShadowSourceInput(effect == null ? null : effect.impl_getImpl());
        dropShadow.setContentInput(effect == null ? null : effect.impl_getImpl());
        dropShadow.setGaussianWidth(this.getClampedWidth());
        dropShadow.setGaussianHeight(this.getClampedHeight());
        dropShadow.setSpread(this.getClampedSpread());
        dropShadow.setShadowMode(Toolkit.getToolkit().toShadowMode(this.getBlurTypeInternal()));
        dropShadow.setColor(Toolkit.getToolkit().toColor4f(this.getColorInternal()));
        dropShadow.setOffsetX((int)this.getOffsetX());
        dropShadow.setOffsetY((int)this.getOffsetY());
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        baseBounds = DropShadow.getInputBounds(baseBounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, this.getInput());
        int n2 = (int)this.getOffsetX();
        int n3 = (int)this.getOffsetY();
        BaseBounds baseBounds2 = BaseBounds.getInstance(baseBounds.getMinX() + (float)n2, baseBounds.getMinY() + (float)n3, baseBounds.getMinZ(), baseBounds.getMaxX() + (float)n2, baseBounds.getMaxY() + (float)n3, baseBounds.getMaxZ());
        baseBounds2 = DropShadow.getShadowBounds(baseBounds2, baseTransform, this.getClampedWidth(), this.getClampedHeight(), this.getBlurTypeInternal());
        BaseBounds baseBounds3 = DropShadow.transformBounds(baseTransform, baseBounds);
        BaseBounds baseBounds4 = baseBounds3.deriveWithUnion(baseBounds2);
        return baseBounds4;
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        DropShadow dropShadow = new DropShadow(this.getBlurType(), this.getColor(), this.getRadius(), this.getSpread(), this.getOffsetX(), this.getOffsetY());
        dropShadow.setInput(this.getInput());
        dropShadow.setWidth(this.getWidth());
        dropShadow.setHeight(this.getHeight());
        return dropShadow;
    }
}

