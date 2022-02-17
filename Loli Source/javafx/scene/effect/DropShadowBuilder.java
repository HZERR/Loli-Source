/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.util.Builder;

@Deprecated
public class DropShadowBuilder<B extends DropShadowBuilder<B>>
implements Builder<DropShadow> {
    private int __set;
    private BlurType blurType;
    private Color color;
    private double height;
    private Effect input;
    private double offsetX;
    private double offsetY;
    private double radius;
    private double spread;
    private double width;

    protected DropShadowBuilder() {
    }

    public static DropShadowBuilder<?> create() {
        return new DropShadowBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(DropShadow dropShadow) {
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    dropShadow.setBlurType(this.blurType);
                    break;
                }
                case 1: {
                    dropShadow.setColor(this.color);
                    break;
                }
                case 2: {
                    dropShadow.setHeight(this.height);
                    break;
                }
                case 3: {
                    dropShadow.setInput(this.input);
                    break;
                }
                case 4: {
                    dropShadow.setOffsetX(this.offsetX);
                    break;
                }
                case 5: {
                    dropShadow.setOffsetY(this.offsetY);
                    break;
                }
                case 6: {
                    dropShadow.setRadius(this.radius);
                    break;
                }
                case 7: {
                    dropShadow.setSpread(this.spread);
                    break;
                }
                case 8: {
                    dropShadow.setWidth(this.width);
                }
            }
        }
    }

    public B blurType(BlurType blurType) {
        this.blurType = blurType;
        this.__set(0);
        return (B)this;
    }

    public B color(Color color) {
        this.color = color;
        this.__set(1);
        return (B)this;
    }

    public B height(double d2) {
        this.height = d2;
        this.__set(2);
        return (B)this;
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set(3);
        return (B)this;
    }

    public B offsetX(double d2) {
        this.offsetX = d2;
        this.__set(4);
        return (B)this;
    }

    public B offsetY(double d2) {
        this.offsetY = d2;
        this.__set(5);
        return (B)this;
    }

    public B radius(double d2) {
        this.radius = d2;
        this.__set(6);
        return (B)this;
    }

    public B spread(double d2) {
        this.spread = d2;
        this.__set(7);
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        this.__set(8);
        return (B)this;
    }

    @Override
    public DropShadow build() {
        DropShadow dropShadow = new DropShadow();
        this.applyTo(dropShadow);
        return dropShadow;
    }
}

