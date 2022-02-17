/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.util.Builder;

@Deprecated
public class GaussianBlurBuilder<B extends GaussianBlurBuilder<B>>
implements Builder<GaussianBlur> {
    private int __set;
    private Effect input;
    private double radius;

    protected GaussianBlurBuilder() {
    }

    public static GaussianBlurBuilder<?> create() {
        return new GaussianBlurBuilder();
    }

    public void applyTo(GaussianBlur gaussianBlur) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            gaussianBlur.setInput(this.input);
        }
        if ((n2 & 2) != 0) {
            gaussianBlur.setRadius(this.radius);
        }
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set |= 1;
        return (B)this;
    }

    public B radius(double d2) {
        this.radius = d2;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public GaussianBlur build() {
        GaussianBlur gaussianBlur = new GaussianBlur();
        this.applyTo(gaussianBlur);
        return gaussianBlur;
    }
}

