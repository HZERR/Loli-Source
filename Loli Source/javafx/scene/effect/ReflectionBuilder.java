/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Effect;
import javafx.scene.effect.Reflection;
import javafx.util.Builder;

@Deprecated
public class ReflectionBuilder<B extends ReflectionBuilder<B>>
implements Builder<Reflection> {
    private int __set;
    private double bottomOpacity;
    private double fraction;
    private Effect input;
    private double topOffset;
    private double topOpacity;

    protected ReflectionBuilder() {
    }

    public static ReflectionBuilder<?> create() {
        return new ReflectionBuilder();
    }

    public void applyTo(Reflection reflection) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            reflection.setBottomOpacity(this.bottomOpacity);
        }
        if ((n2 & 2) != 0) {
            reflection.setFraction(this.fraction);
        }
        if ((n2 & 4) != 0) {
            reflection.setInput(this.input);
        }
        if ((n2 & 8) != 0) {
            reflection.setTopOffset(this.topOffset);
        }
        if ((n2 & 0x10) != 0) {
            reflection.setTopOpacity(this.topOpacity);
        }
    }

    public B bottomOpacity(double d2) {
        this.bottomOpacity = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B fraction(double d2) {
        this.fraction = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set |= 4;
        return (B)this;
    }

    public B topOffset(double d2) {
        this.topOffset = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B topOpacity(double d2) {
        this.topOpacity = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    @Override
    public Reflection build() {
        Reflection reflection = new Reflection();
        this.applyTo(reflection);
        return reflection;
    }
}

