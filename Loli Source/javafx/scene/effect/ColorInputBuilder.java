/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.ColorInput;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

@Deprecated
public class ColorInputBuilder<B extends ColorInputBuilder<B>>
implements Builder<ColorInput> {
    private int __set;
    private double height;
    private Paint paint;
    private double width;
    private double x;
    private double y;

    protected ColorInputBuilder() {
    }

    public static ColorInputBuilder<?> create() {
        return new ColorInputBuilder();
    }

    public void applyTo(ColorInput colorInput) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            colorInput.setHeight(this.height);
        }
        if ((n2 & 2) != 0) {
            colorInput.setPaint(this.paint);
        }
        if ((n2 & 4) != 0) {
            colorInput.setWidth(this.width);
        }
        if ((n2 & 8) != 0) {
            colorInput.setX(this.x);
        }
        if ((n2 & 0x10) != 0) {
            colorInput.setY(this.y);
        }
    }

    public B height(double d2) {
        this.height = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B paint(Paint paint) {
        this.paint = paint;
        this.__set |= 2;
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    @Override
    public ColorInput build() {
        ColorInput colorInput = new ColorInput();
        this.applyTo(colorInput);
        return colorInput;
    }
}

