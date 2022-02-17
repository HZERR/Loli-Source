/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Shadow;
import javafx.scene.paint.Color;
import javafx.util.Builder;

@Deprecated
public class ShadowBuilder<B extends ShadowBuilder<B>>
implements Builder<Shadow> {
    private int __set;
    private BlurType blurType;
    private Color color;
    private double height;
    private Effect input;
    private double radius;
    private double width;

    protected ShadowBuilder() {
    }

    public static ShadowBuilder<?> create() {
        return new ShadowBuilder();
    }

    public void applyTo(Shadow shadow) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            shadow.setBlurType(this.blurType);
        }
        if ((n2 & 2) != 0) {
            shadow.setColor(this.color);
        }
        if ((n2 & 4) != 0) {
            shadow.setHeight(this.height);
        }
        if ((n2 & 8) != 0) {
            shadow.setInput(this.input);
        }
        if ((n2 & 0x10) != 0) {
            shadow.setRadius(this.radius);
        }
        if ((n2 & 0x20) != 0) {
            shadow.setWidth(this.width);
        }
    }

    public B blurType(BlurType blurType) {
        this.blurType = blurType;
        this.__set |= 1;
        return (B)this;
    }

    public B color(Color color) {
        this.color = color;
        this.__set |= 2;
        return (B)this;
    }

    public B height(double d2) {
        this.height = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set |= 8;
        return (B)this;
    }

    public B radius(double d2) {
        this.radius = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    @Override
    public Shadow build() {
        Shadow shadow = new Shadow();
        this.applyTo(shadow);
        return shadow;
    }
}

