/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.geometry.Rectangle2D;
import javafx.util.Builder;

@Deprecated
public class Rectangle2DBuilder<B extends Rectangle2DBuilder<B>>
implements Builder<Rectangle2D> {
    private double height;
    private double minX;
    private double minY;
    private double width;

    protected Rectangle2DBuilder() {
    }

    public static Rectangle2DBuilder<?> create() {
        return new Rectangle2DBuilder();
    }

    public B height(double d2) {
        this.height = d2;
        return (B)this;
    }

    public B minX(double d2) {
        this.minX = d2;
        return (B)this;
    }

    public B minY(double d2) {
        this.minY = d2;
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        return (B)this;
    }

    @Override
    public Rectangle2D build() {
        Rectangle2D rectangle2D = new Rectangle2D(this.minX, this.minY, this.width, this.height);
        return rectangle2D;
    }
}

