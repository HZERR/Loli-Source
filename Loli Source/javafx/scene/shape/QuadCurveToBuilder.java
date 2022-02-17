/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.PathElementBuilder;
import javafx.scene.shape.QuadCurveTo;
import javafx.util.Builder;

@Deprecated
public class QuadCurveToBuilder<B extends QuadCurveToBuilder<B>>
extends PathElementBuilder<B>
implements Builder<QuadCurveTo> {
    private int __set;
    private double controlX;
    private double controlY;
    private double x;
    private double y;

    protected QuadCurveToBuilder() {
    }

    public static QuadCurveToBuilder<?> create() {
        return new QuadCurveToBuilder();
    }

    public void applyTo(QuadCurveTo quadCurveTo) {
        super.applyTo(quadCurveTo);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            quadCurveTo.setControlX(this.controlX);
        }
        if ((n2 & 2) != 0) {
            quadCurveTo.setControlY(this.controlY);
        }
        if ((n2 & 4) != 0) {
            quadCurveTo.setX(this.x);
        }
        if ((n2 & 8) != 0) {
            quadCurveTo.setY(this.y);
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

    public B x(double d2) {
        this.x = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public QuadCurveTo build() {
        QuadCurveTo quadCurveTo = new QuadCurveTo();
        this.applyTo(quadCurveTo);
        return quadCurveTo;
    }
}

