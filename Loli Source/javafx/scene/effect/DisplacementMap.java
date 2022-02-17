/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.EffectChangeListener;
import javafx.scene.effect.FloatMap;

public class DisplacementMap
extends Effect {
    private ObjectProperty<Effect> input;
    private final FloatMap defaultMap = new FloatMap(1, 1);
    private ObjectProperty<FloatMap> mapData;
    private final MapDataChangeListener mapDataChangeListener = new MapDataChangeListener();
    private DoubleProperty scaleX;
    private DoubleProperty scaleY;
    private DoubleProperty offsetX;
    private DoubleProperty offsetY;
    private BooleanProperty wrap;

    @Override
    com.sun.scenario.effect.DisplacementMap impl_createImpl() {
        return new com.sun.scenario.effect.DisplacementMap(new com.sun.scenario.effect.FloatMap(1, 1), com.sun.scenario.effect.Effect.DefaultInput);
    }

    public DisplacementMap() {
        this.setMapData(new FloatMap(1, 1));
    }

    public DisplacementMap(FloatMap floatMap) {
        this.setMapData(floatMap);
    }

    public DisplacementMap(FloatMap floatMap, double d2, double d3, double d4, double d5) {
        this.setMapData(floatMap);
        this.setOffsetX(d2);
        this.setOffsetY(d3);
        this.setScaleX(d4);
        this.setScaleY(d5);
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

    public final void setMapData(FloatMap floatMap) {
        this.mapDataProperty().set(floatMap);
    }

    public final FloatMap getMapData() {
        return this.mapData == null ? null : (FloatMap)this.mapData.get();
    }

    public final ObjectProperty<FloatMap> mapDataProperty() {
        if (this.mapData == null) {
            this.mapData = new ObjectPropertyBase<FloatMap>(){

                @Override
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    DisplacementMap.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override
                public String getName() {
                    return "mapData";
                }
            };
        }
        return this.mapData;
    }

    public final void setScaleX(double d2) {
        this.scaleXProperty().set(d2);
    }

    public final double getScaleX() {
        return this.scaleX == null ? 1.0 : this.scaleX.get();
    }

    public final DoubleProperty scaleXProperty() {
        if (this.scaleX == null) {
            this.scaleX = new DoublePropertyBase(1.0){

                @Override
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override
                public String getName() {
                    return "scaleX";
                }
            };
        }
        return this.scaleX;
    }

    public final void setScaleY(double d2) {
        this.scaleYProperty().set(d2);
    }

    public final double getScaleY() {
        return this.scaleY == null ? 1.0 : this.scaleY.get();
    }

    public final DoubleProperty scaleYProperty() {
        if (this.scaleY == null) {
            this.scaleY = new DoublePropertyBase(1.0){

                @Override
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override
                public String getName() {
                    return "scaleY";
                }
            };
        }
        return this.scaleY;
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
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return DisplacementMap.this;
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
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override
                public String getName() {
                    return "offsetY";
                }
            };
        }
        return this.offsetY;
    }

    public final void setWrap(boolean bl) {
        this.wrapProperty().set(bl);
    }

    public final boolean isWrap() {
        return this.wrap == null ? false : this.wrap.get();
    }

    public final BooleanProperty wrapProperty() {
        if (this.wrap == null) {
            this.wrap = new BooleanPropertyBase(){

                @Override
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override
                public String getName() {
                    return "wrap";
                }
            };
        }
        return this.wrap;
    }

    @Override
    void impl_update() {
        Effect effect = this.getInput();
        if (effect != null) {
            effect.impl_sync();
        }
        com.sun.scenario.effect.DisplacementMap displacementMap = (com.sun.scenario.effect.DisplacementMap)this.impl_getImpl();
        displacementMap.setContentInput(effect == null ? null : effect.impl_getImpl());
        FloatMap floatMap = this.getMapData();
        this.mapDataChangeListener.register(floatMap);
        if (floatMap != null) {
            floatMap.impl_sync();
            displacementMap.setMapData(floatMap.getImpl());
        } else {
            this.defaultMap.impl_sync();
            displacementMap.setMapData(this.defaultMap.getImpl());
        }
        displacementMap.setScaleX((float)this.getScaleX());
        displacementMap.setScaleY((float)this.getScaleY());
        displacementMap.setOffsetX((float)this.getOffsetX());
        displacementMap.setOffsetY((float)this.getOffsetY());
        displacementMap.setWrap(this.isWrap());
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        baseBounds = DisplacementMap.getInputBounds(baseBounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, this.getInput());
        return DisplacementMap.transformBounds(baseTransform, baseBounds);
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        DisplacementMap displacementMap = new DisplacementMap(this.getMapData().impl_copy(), this.getOffsetX(), this.getOffsetY(), this.getScaleX(), this.getScaleY());
        displacementMap.setInput(this.getInput());
        return displacementMap;
    }

    private class MapDataChangeListener
    extends EffectChangeListener {
        FloatMap mapData;

        private MapDataChangeListener() {
        }

        public void register(FloatMap floatMap) {
            this.mapData = floatMap;
            super.register(this.mapData == null ? null : this.mapData.effectDirtyProperty());
        }

        @Override
        public void invalidated(Observable observable) {
            if (this.mapData.impl_isEffectDirty()) {
                DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                DisplacementMap.this.effectBoundsChanged();
            }
        }
    }
}

