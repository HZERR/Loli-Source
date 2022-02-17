/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.PathElementBuilder;
import javafx.util.Builder;

@Deprecated
public class CubicCurveToBuilder<B extends CubicCurveToBuilder<B>>
extends PathElementBuilder<B>
implements Builder<CubicCurveTo> {
    private int __set;
    private double controlX1;
    private double controlX2;
    private double controlY1;
    private double controlY2;
    private double x;
    private double y;

    protected CubicCurveToBuilder() {
    }

    public static CubicCurveToBuilder<?> create() {
        return new CubicCurveToBuilder();
    }

    public void applyTo(CubicCurveTo cubicCurveTo) {
        super.applyTo(cubicCurveTo);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            cubicCurveTo.setControlX1(this.controlX1);
        }
        if ((n2 & 2) != 0) {
            cubicCurveTo.setControlX2(this.controlX2);
        }
        if ((n2 & 4) != 0) {
            cubicCurveTo.setControlY1(this.controlY1);
        }
        if ((n2 & 8) != 0) {
            cubicCurveTo.setControlY2(this.controlY2);
        }
        if ((n2 & 0x10) != 0) {
            cubicCurveTo.setX(this.x);
        }
        if ((n2 & 0x20) != 0) {
            cubicCurveTo.setY(this.y);
        }
    }

    public B controlX1(double d2) {
        this.controlX1 = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B controlX2(double d2) {
        this.controlX2 = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B controlY1(double d2) {
        this.controlY1 = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B controlY2(double d2) {
        this.controlY2 = d2;
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
    public CubicCurveTo build() {
        CubicCurveTo cubicCurveTo = new CubicCurveTo();
        this.applyTo(cubicCurveTo);
        return cubicCurveTo;
    }
}

