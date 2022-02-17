/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class RectangleBuilder<B extends RectangleBuilder<B>>
extends ShapeBuilder<B>
implements Builder<Rectangle> {
    private int __set;
    private double arcHeight;
    private double arcWidth;
    private double height;
    private double width;
    private double x;
    private double y;

    protected RectangleBuilder() {
    }

    public static RectangleBuilder<?> create() {
        return new RectangleBuilder();
    }

    public void applyTo(Rectangle rectangle) {
        super.applyTo(rectangle);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            rectangle.setArcHeight(this.arcHeight);
        }
        if ((n2 & 2) != 0) {
            rectangle.setArcWidth(this.arcWidth);
        }
        if ((n2 & 4) != 0) {
            rectangle.setHeight(this.height);
        }
        if ((n2 & 8) != 0) {
            rectangle.setWidth(this.width);
        }
        if ((n2 & 0x10) != 0) {
            rectangle.setX(this.x);
        }
        if ((n2 & 0x20) != 0) {
            rectangle.setY(this.y);
        }
    }

    public B arcHeight(double d2) {
        this.arcHeight = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B arcWidth(double d2) {
        this.arcWidth = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B height(double d2) {
        this.height = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    @Override
    public Rectangle build() {
        Rectangle rectangle = new Rectangle();
        this.applyTo(rectangle);
        return rectangle;
    }
}

