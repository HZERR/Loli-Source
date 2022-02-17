/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.geometry.Point2D;
import javafx.util.Builder;

@Deprecated
public class Point2DBuilder<B extends Point2DBuilder<B>>
implements Builder<Point2D> {
    private double x;
    private double y;

    protected Point2DBuilder() {
    }

    public static Point2DBuilder<?> create() {
        return new Point2DBuilder();
    }

    public B x(double d2) {
        this.x = d2;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        return (B)this;
    }

    @Override
    public Point2D build() {
        Point2D point2D = new Point2D(this.x, this.y);
        return point2D;
    }
}

