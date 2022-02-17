/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class ArcBuilder<B extends ArcBuilder<B>>
extends ShapeBuilder<B>
implements Builder<Arc> {
    private int __set;
    private double centerX;
    private double centerY;
    private double length;
    private double radiusX;
    private double radiusY;
    private double startAngle;
    private ArcType type;

    protected ArcBuilder() {
    }

    public static ArcBuilder<?> create() {
        return new ArcBuilder();
    }

    public void applyTo(Arc arc) {
        super.applyTo(arc);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            arc.setCenterX(this.centerX);
        }
        if ((n2 & 2) != 0) {
            arc.setCenterY(this.centerY);
        }
        if ((n2 & 4) != 0) {
            arc.setLength(this.length);
        }
        if ((n2 & 8) != 0) {
            arc.setRadiusX(this.radiusX);
        }
        if ((n2 & 0x10) != 0) {
            arc.setRadiusY(this.radiusY);
        }
        if ((n2 & 0x20) != 0) {
            arc.setStartAngle(this.startAngle);
        }
        if ((n2 & 0x40) != 0) {
            arc.setType(this.type);
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

    public B length(double d2) {
        this.length = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B radiusX(double d2) {
        this.radiusX = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B radiusY(double d2) {
        this.radiusY = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B startAngle(double d2) {
        this.startAngle = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    public B type(ArcType arcType) {
        this.type = arcType;
        this.__set |= 0x40;
        return (B)this;
    }

    @Override
    public Arc build() {
        Arc arc = new Arc();
        this.applyTo(arc);
        return arc;
    }
}

