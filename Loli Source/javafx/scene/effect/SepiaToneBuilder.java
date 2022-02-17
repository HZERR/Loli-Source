/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Effect;
import javafx.scene.effect.SepiaTone;
import javafx.util.Builder;

@Deprecated
public class SepiaToneBuilder<B extends SepiaToneBuilder<B>>
implements Builder<SepiaTone> {
    private int __set;
    private Effect input;
    private double level;

    protected SepiaToneBuilder() {
    }

    public static SepiaToneBuilder<?> create() {
        return new SepiaToneBuilder();
    }

    public void applyTo(SepiaTone sepiaTone) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            sepiaTone.setInput(this.input);
        }
        if ((n2 & 2) != 0) {
            sepiaTone.setLevel(this.level);
        }
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set |= 1;
        return (B)this;
    }

    public B level(double d2) {
        this.level = d2;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public SepiaTone build() {
        SepiaTone sepiaTone = new SepiaTone();
        this.applyTo(sepiaTone);
        return sepiaTone;
    }
}

