/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.util.Builder;

@Deprecated
public class GlowBuilder<B extends GlowBuilder<B>>
implements Builder<Glow> {
    private int __set;
    private Effect input;
    private double level;

    protected GlowBuilder() {
    }

    public static GlowBuilder<?> create() {
        return new GlowBuilder();
    }

    public void applyTo(Glow glow) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            glow.setInput(this.input);
        }
        if ((n2 & 2) != 0) {
            glow.setLevel(this.level);
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
    public Glow build() {
        Glow glow = new Glow();
        this.applyTo(glow);
        return glow;
    }
}

