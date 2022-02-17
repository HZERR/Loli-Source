/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.util.Builder;

@Deprecated
public class BloomBuilder<B extends BloomBuilder<B>>
implements Builder<Bloom> {
    private int __set;
    private Effect input;
    private double threshold;

    protected BloomBuilder() {
    }

    public static BloomBuilder<?> create() {
        return new BloomBuilder();
    }

    public void applyTo(Bloom bloom) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            bloom.setInput(this.input);
        }
        if ((n2 & 2) != 0) {
            bloom.setThreshold(this.threshold);
        }
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set |= 1;
        return (B)this;
    }

    public B threshold(double d2) {
        this.threshold = d2;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public Bloom build() {
        Bloom bloom = new Bloom();
        this.applyTo(bloom);
        return bloom;
    }
}

