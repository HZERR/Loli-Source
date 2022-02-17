/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.util.Builder;

@Deprecated
public class BoxBlurBuilder<B extends BoxBlurBuilder<B>>
implements Builder<BoxBlur> {
    private int __set;
    private double height;
    private Effect input;
    private int iterations;
    private double width;

    protected BoxBlurBuilder() {
    }

    public static BoxBlurBuilder<?> create() {
        return new BoxBlurBuilder();
    }

    public void applyTo(BoxBlur boxBlur) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            boxBlur.setHeight(this.height);
        }
        if ((n2 & 2) != 0) {
            boxBlur.setInput(this.input);
        }
        if ((n2 & 4) != 0) {
            boxBlur.setIterations(this.iterations);
        }
        if ((n2 & 8) != 0) {
            boxBlur.setWidth(this.width);
        }
    }

    public B height(double d2) {
        this.height = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set |= 2;
        return (B)this;
    }

    public B iterations(int n2) {
        this.iterations = n2;
        this.__set |= 4;
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public BoxBlur build() {
        BoxBlur boxBlur = new BoxBlur();
        this.applyTo(boxBlur);
        return boxBlur;
    }
}

