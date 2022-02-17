/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.util.Builder;

@Deprecated
public class InnerShadowBuilder<B extends InnerShadowBuilder<B>>
implements Builder<InnerShadow> {
    private int __set;
    private BlurType blurType;
    private double choke;
    private Color color;
    private double height;
    private Effect input;
    private double offsetX;
    private double offsetY;
    private double radius;
    private double width;

    protected InnerShadowBuilder() {
    }

    public static InnerShadowBuilder<?> create() {
        return new InnerShadowBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(InnerShadow innerShadow) {
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    innerShadow.setBlurType(this.blurType);
                    break;
                }
                case 1: {
                    innerShadow.setChoke(this.choke);
                    break;
                }
                case 2: {
                    innerShadow.setColor(this.color);
                    break;
                }
                case 3: {
                    innerShadow.setHeight(this.height);
                    break;
                }
                case 4: {
                    innerShadow.setInput(this.input);
                    break;
                }
                case 5: {
                    innerShadow.setOffsetX(this.offsetX);
                    break;
                }
                case 6: {
                    innerShadow.setOffsetY(this.offsetY);
                    break;
                }
                case 7: {
                    innerShadow.setRadius(this.radius);
                    break;
                }
                case 8: {
                    innerShadow.setWidth(this.width);
                }
            }
        }
    }

    public B blurType(BlurType blurType) {
        this.blurType = blurType;
        this.__set(0);
        return (B)this;
    }

    public B choke(double d2) {
        this.choke = d2;
        this.__set(1);
        return (B)this;
    }

    public B color(Color color) {
        this.color = color;
        this.__set(2);
        return (B)this;
    }

    public B height(double d2) {
        this.height = d2;
        this.__set(3);
        return (B)this;
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set(4);
        return (B)this;
    }

    public B offsetX(double d2) {
        this.offsetX = d2;
        this.__set(5);
        return (B)this;
    }

    public B offsetY(double d2) {
        this.offsetY = d2;
        this.__set(6);
        return (B)this;
    }

    public B radius(double d2) {
        this.radius = d2;
        this.__set(7);
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        this.__set(8);
        return (B)this;
    }

    @Override
    public InnerShadow build() {
        InnerShadow innerShadow = new InnerShadow();
        this.applyTo(innerShadow);
        return innerShadow;
    }
}

