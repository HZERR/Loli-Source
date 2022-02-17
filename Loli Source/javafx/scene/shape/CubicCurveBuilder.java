/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class CubicCurveBuilder<B extends CubicCurveBuilder<B>>
extends ShapeBuilder<B>
implements Builder<CubicCurve> {
    private int __set;
    private double controlX1;
    private double controlX2;
    private double controlY1;
    private double controlY2;
    private double endX;
    private double endY;
    private double startX;
    private double startY;

    protected CubicCurveBuilder() {
    }

    public static CubicCurveBuilder<?> create() {
        return new CubicCurveBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(CubicCurve cubicCurve) {
        super.applyTo(cubicCurve);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    cubicCurve.setControlX1(this.controlX1);
                    break;
                }
                case 1: {
                    cubicCurve.setControlX2(this.controlX2);
                    break;
                }
                case 2: {
                    cubicCurve.setControlY1(this.controlY1);
                    break;
                }
                case 3: {
                    cubicCurve.setControlY2(this.controlY2);
                    break;
                }
                case 4: {
                    cubicCurve.setEndX(this.endX);
                    break;
                }
                case 5: {
                    cubicCurve.setEndY(this.endY);
                    break;
                }
                case 6: {
                    cubicCurve.setStartX(this.startX);
                    break;
                }
                case 7: {
                    cubicCurve.setStartY(this.startY);
                }
            }
        }
    }

    public B controlX1(double d2) {
        this.controlX1 = d2;
        this.__set(0);
        return (B)this;
    }

    public B controlX2(double d2) {
        this.controlX2 = d2;
        this.__set(1);
        return (B)this;
    }

    public B controlY1(double d2) {
        this.controlY1 = d2;
        this.__set(2);
        return (B)this;
    }

    public B controlY2(double d2) {
        this.controlY2 = d2;
        this.__set(3);
        return (B)this;
    }

    public B endX(double d2) {
        this.endX = d2;
        this.__set(4);
        return (B)this;
    }

    public B endY(double d2) {
        this.endY = d2;
        this.__set(5);
        return (B)this;
    }

    public B startX(double d2) {
        this.startX = d2;
        this.__set(6);
        return (B)this;
    }

    public B startY(double d2) {
        this.startY = d2;
        this.__set(7);
        return (B)this;
    }

    @Override
    public CubicCurve build() {
        CubicCurve cubicCurve = new CubicCurve();
        this.applyTo(cubicCurve);
        return cubicCurve;
    }
}

