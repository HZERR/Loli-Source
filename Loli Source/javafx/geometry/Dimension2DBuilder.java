/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.geometry.Dimension2D;
import javafx.util.Builder;

@Deprecated
public class Dimension2DBuilder<B extends Dimension2DBuilder<B>>
implements Builder<Dimension2D> {
    private double height;
    private double width;

    protected Dimension2DBuilder() {
    }

    public static Dimension2DBuilder<?> create() {
        return new Dimension2DBuilder();
    }

    public B height(double d2) {
        this.height = d2;
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        return (B)this;
    }

    @Override
    public Dimension2D build() {
        Dimension2D dimension2D = new Dimension2D(this.width, this.height);
        return dimension2D;
    }
}

