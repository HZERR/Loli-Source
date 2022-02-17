/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

public class PerspectiveTransform
extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty ulx;
    private DoubleProperty uly;
    private DoubleProperty urx;
    private DoubleProperty ury;
    private DoubleProperty lrx;
    private DoubleProperty lry;
    private DoubleProperty llx;
    private DoubleProperty lly;
    private float[] devcoords = new float[8];

    public PerspectiveTransform() {
    }

    public PerspectiveTransform(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        this.setUlx(d2);
        this.setUly(d3);
        this.setUrx(d4);
        this.setUry(d5);
        this.setLlx(d8);
        this.setLly(d9);
        this.setLrx(d6);
        this.setLry(d7);
    }

    private void updateXform() {
        ((com.sun.scenario.effect.PerspectiveTransform)this.impl_getImpl()).setQuadMapping((float)this.getUlx(), (float)this.getUly(), (float)this.getUrx(), (float)this.getUry(), (float)this.getLrx(), (float)this.getLry(), (float)this.getLlx(), (float)this.getLly());
    }

    @Override
    com.sun.scenario.effect.PerspectiveTransform impl_createImpl() {
        return new com.sun.scenario.effect.PerspectiveTransform();
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

    public final void setUlx(double d2) {
        this.ulxProperty().set(d2);
    }

    public final double getUlx() {
        return this.ulx == null ? 0.0 : this.ulx.get();
    }

    public final DoubleProperty ulxProperty() {
        if (this.ulx == null) {
            this.ulx = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override
                public String getName() {
                    return "ulx";
                }
            };
        }
        return this.ulx;
    }

    public final void setUly(double d2) {
        this.ulyProperty().set(d2);
    }

    public final double getUly() {
        return this.uly == null ? 0.0 : this.uly.get();
    }

    public final DoubleProperty ulyProperty() {
        if (this.uly == null) {
            this.uly = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override
                public String getName() {
                    return "uly";
                }
            };
        }
        return this.uly;
    }

    public final void setUrx(double d2) {
        this.urxProperty().set(d2);
    }

    public final double getUrx() {
        return this.urx == null ? 0.0 : this.urx.get();
    }

    public final DoubleProperty urxProperty() {
        if (this.urx == null) {
            this.urx = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override
                public String getName() {
                    return "urx";
                }
            };
        }
        return this.urx;
    }

    public final void setUry(double d2) {
        this.uryProperty().set(d2);
    }

    public final double getUry() {
        return this.ury == null ? 0.0 : this.ury.get();
    }

    public final DoubleProperty uryProperty() {
        if (this.ury == null) {
            this.ury = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override
                public String getName() {
                    return "ury";
                }
            };
        }
        return this.ury;
    }

    public final void setLrx(double d2) {
        this.lrxProperty().set(d2);
    }

    public final double getLrx() {
        return this.lrx == null ? 0.0 : this.lrx.get();
    }

    public final DoubleProperty lrxProperty() {
        if (this.lrx == null) {
            this.lrx = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override
                public String getName() {
                    return "lrx";
                }
            };
        }
        return this.lrx;
    }

    public final void setLry(double d2) {
        this.lryProperty().set(d2);
    }

    public final double getLry() {
        return this.lry == null ? 0.0 : this.lry.get();
    }

    public final DoubleProperty lryProperty() {
        if (this.lry == null) {
            this.lry = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override
                public String getName() {
                    return "lry";
                }
            };
        }
        return this.lry;
    }

    public final void setLlx(double d2) {
        this.llxProperty().set(d2);
    }

    public final double getLlx() {
        return this.llx == null ? 0.0 : this.llx.get();
    }

    public final DoubleProperty llxProperty() {
        if (this.llx == null) {
            this.llx = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override
                public String getName() {
                    return "llx";
                }
            };
        }
        return this.llx;
    }

    public final void setLly(double d2) {
        this.llyProperty().set(d2);
    }

    public final double getLly() {
        return this.lly == null ? 0.0 : this.lly.get();
    }

    public final DoubleProperty llyProperty() {
        if (this.lly == null) {
            this.lly = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override
                public String getName() {
                    return "lly";
                }
            };
        }
        return this.lly;
    }

    @Override
    void impl_update() {
        Effect effect = this.getInput();
        if (effect != null) {
            effect.impl_sync();
        }
        ((com.sun.scenario.effect.PerspectiveTransform)this.impl_getImpl()).setInput(effect == null ? null : effect.impl_getImpl());
        this.updateXform();
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        float f2;
        float f3;
        this.setupDevCoords(baseTransform);
        float f4 = f3 = this.devcoords[0];
        float f5 = f2 = this.devcoords[1];
        for (int i2 = 2; i2 < this.devcoords.length; i2 += 2) {
            if (f4 > this.devcoords[i2]) {
                f4 = this.devcoords[i2];
            } else if (f3 < this.devcoords[i2]) {
                f3 = this.devcoords[i2];
            }
            if (f5 > this.devcoords[i2 + 1]) {
                f5 = this.devcoords[i2 + 1];
                continue;
            }
            if (!(f2 < this.devcoords[i2 + 1])) continue;
            f2 = this.devcoords[i2 + 1];
        }
        return new RectBounds(f4, f5, f3, f2);
    }

    private void setupDevCoords(BaseTransform baseTransform) {
        this.devcoords[0] = (float)this.getUlx();
        this.devcoords[1] = (float)this.getUly();
        this.devcoords[2] = (float)this.getUrx();
        this.devcoords[3] = (float)this.getUry();
        this.devcoords[4] = (float)this.getLrx();
        this.devcoords[5] = (float)this.getLry();
        this.devcoords[6] = (float)this.getLlx();
        this.devcoords[7] = (float)this.getLly();
        baseTransform.transform(this.devcoords, 0, this.devcoords, 0, 4);
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        return new PerspectiveTransform(this.getUlx(), this.getUly(), this.getUrx(), this.getUry(), this.getLrx(), this.getLry(), this.getLlx(), this.getLly());
    }
}

