/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.Circle;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class CircleBuilder<B extends CircleBuilder<B>>
extends ShapeBuilder<B>
implements Builder<Circle> {
    private int __set;
    private double centerX;
    private double centerY;
    private double radius;

    protected CircleBuilder() {
    }

    public static CircleBuilder<?> create() {
        return new CircleBuilder();
    }

    public void applyTo(Circle circle) {
        super.applyTo(circle);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            circle.setCenterX(this.centerX);
        }
        if ((n2 & 2) != 0) {
            circle.setCenterY(this.centerY);
        }
        if ((n2 & 4) != 0) {
            circle.setRadius(this.radius);
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

    public B radius(double d2) {
        this.radius = d2;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public Circle build() {
        Circle circle = new Circle();
        this.applyTo(circle);
        return circle;
    }
}

