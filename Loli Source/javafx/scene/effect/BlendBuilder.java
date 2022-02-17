/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.util.Builder;

@Deprecated
public class BlendBuilder<B extends BlendBuilder<B>>
implements Builder<Blend> {
    private int __set;
    private Effect bottomInput;
    private BlendMode mode;
    private double opacity;
    private Effect topInput;

    protected BlendBuilder() {
    }

    public static BlendBuilder<?> create() {
        return new BlendBuilder();
    }

    public void applyTo(Blend blend) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            blend.setBottomInput(this.bottomInput);
        }
        if ((n2 & 2) != 0) {
            blend.setMode(this.mode);
        }
        if ((n2 & 4) != 0) {
            blend.setOpacity(this.opacity);
        }
        if ((n2 & 8) != 0) {
            blend.setTopInput(this.topInput);
        }
    }

    public B bottomInput(Effect effect) {
        this.bottomInput = effect;
        this.__set |= 1;
        return (B)this;
    }

    public B mode(BlendMode blendMode) {
        this.mode = blendMode;
        this.__set |= 2;
        return (B)this;
    }

    public B opacity(double d2) {
        this.opacity = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B topInput(Effect effect) {
        this.topInput = effect;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public Blend build() {
        Blend blend = new Blend();
        this.applyTo(blend);
        return blend;
    }
}

