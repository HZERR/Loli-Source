/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.FloatMap;
import javafx.util.Builder;

@Deprecated
public class FloatMapBuilder<B extends FloatMapBuilder<B>>
implements Builder<FloatMap> {
    private int __set;
    private int height;
    private int width;

    protected FloatMapBuilder() {
    }

    public static FloatMapBuilder<?> create() {
        return new FloatMapBuilder();
    }

    public void applyTo(FloatMap floatMap) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            floatMap.setHeight(this.height);
        }
        if ((n2 & 2) != 0) {
            floatMap.setWidth(this.width);
        }
    }

    public B height(int n2) {
        this.height = n2;
        this.__set |= 1;
        return (B)this;
    }

    public B width(int n2) {
        this.width = n2;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public FloatMap build() {
        FloatMap floatMap = new FloatMap();
        this.applyTo(floatMap);
        return floatMap;
    }
}

