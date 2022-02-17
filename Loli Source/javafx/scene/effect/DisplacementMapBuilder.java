/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.Effect;
import javafx.scene.effect.FloatMap;
import javafx.util.Builder;

@Deprecated
public class DisplacementMapBuilder<B extends DisplacementMapBuilder<B>>
implements Builder<DisplacementMap> {
    private int __set;
    private Effect input;
    private FloatMap mapData;
    private double offsetX;
    private double offsetY;
    private double scaleX;
    private double scaleY;
    private boolean wrap;

    protected DisplacementMapBuilder() {
    }

    public static DisplacementMapBuilder<?> create() {
        return new DisplacementMapBuilder();
    }

    public void applyTo(DisplacementMap displacementMap) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            displacementMap.setInput(this.input);
        }
        if ((n2 & 2) != 0) {
            displacementMap.setMapData(this.mapData);
        }
        if ((n2 & 4) != 0) {
            displacementMap.setOffsetX(this.offsetX);
        }
        if ((n2 & 8) != 0) {
            displacementMap.setOffsetY(this.offsetY);
        }
        if ((n2 & 0x10) != 0) {
            displacementMap.setScaleX(this.scaleX);
        }
        if ((n2 & 0x20) != 0) {
            displacementMap.setScaleY(this.scaleY);
        }
        if ((n2 & 0x40) != 0) {
            displacementMap.setWrap(this.wrap);
        }
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set |= 1;
        return (B)this;
    }

    public B mapData(FloatMap floatMap) {
        this.mapData = floatMap;
        this.__set |= 2;
        return (B)this;
    }

    public B offsetX(double d2) {
        this.offsetX = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B offsetY(double d2) {
        this.offsetY = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B scaleX(double d2) {
        this.scaleX = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B scaleY(double d2) {
        this.scaleY = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    public B wrap(boolean bl) {
        this.wrap = bl;
        this.__set |= 0x40;
        return (B)this;
    }

    @Override
    public DisplacementMap build() {
        DisplacementMap displacementMap = new DisplacementMap();
        this.applyTo(displacementMap);
        return displacementMap;
    }
}

