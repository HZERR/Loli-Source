/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class QuadCurveBuilder<B extends QuadCurveBuilder<B>>
extends ShapeBuilder<B>
implements Builder<QuadCurve> {
    private int __set;
    private double controlX;
    private double controlY;
    private double endX;
    private double endY;
    private double startX;
    private double startY;

    protected QuadCurveBuilder() {
    }

    public static QuadCurveBuilder<?> create() {
        return new QuadCurveBuilder();
    }

    public void applyTo(QuadCurve quadCurve) {
        super.applyTo(quadCurve);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            quadCurve.setControlX(this.controlX);
        }
        if ((n2 & 2) != 0) {
            quadCurve.setControlY(this.controlY);
        }
        if ((n2 & 4) != 0) {
            quadCurve.setEndX(this.endX);
        }
        if ((n2 & 8) != 0) {
            quadCurve.setEndY(this.endY);
        }
        if ((n2 & 0x10) != 0) {
            quadCurve.setStartX(this.startX);
        }
        if ((n2 & 0x20) != 0) {
            quadCurve.setStartY(this.startY);
        }
    }

    public B controlX(double d2) {
        this.controlX = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B controlY(double d2) {
        this.controlY = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B endX(double d2) {
        this.endX = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B endY(double d2) {
        this.endY = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B startX(double d2) {
        this.startX = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B startY(double d2) {
        this.startY = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    @Override
    public QuadCurve build() {
        QuadCurve quadCurve = new QuadCurve();
        this.applyTo(quadCurve);
        return quadCurve;
    }
}

