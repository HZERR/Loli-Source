/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.Ellipse;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class EllipseBuilder<B extends EllipseBuilder<B>>
extends ShapeBuilder<B>
implements Builder<Ellipse> {
    private int __set;
    private double centerX;
    private double centerY;
    private double radiusX;
    private double radiusY;

    protected EllipseBuilder() {
    }

    public static EllipseBuilder<?> create() {
        return new EllipseBuilder();
    }

    public void applyTo(Ellipse ellipse) {
        super.applyTo(ellipse);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            ellipse.setCenterX(this.centerX);
        }
        if ((n2 & 2) != 0) {
            ellipse.setCenterY(this.centerY);
        }
        if ((n2 & 4) != 0) {
            ellipse.setRadiusX(this.radiusX);
        }
        if ((n2 & 8) != 0) {
            ellipse.setRadiusY(this.radiusY);
        }
    }

    public B centerX(double d2) {
        this.centerX = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B centerY(double d2) {
        this.centerY = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B radiusX(double d2) {
        this.radiusX = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B radiusY(double d2) {
        this.radiusY = d2;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public Ellipse build() {
        Ellipse ellipse = new Ellipse();
        this.applyTo(ellipse);
        return ellipse;
    }
}

