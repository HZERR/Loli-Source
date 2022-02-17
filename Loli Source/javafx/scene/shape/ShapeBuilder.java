/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.NodeBuilder;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

@Deprecated
public abstract class ShapeBuilder<B extends ShapeBuilder<B>>
extends NodeBuilder<B> {
    private int __set;
    private Paint fill;
    private boolean smooth;
    private Paint stroke;
    private Collection<? extends Double> strokeDashArray;
    private double strokeDashOffset;
    private StrokeLineCap strokeLineCap;
    private StrokeLineJoin strokeLineJoin;
    private double strokeMiterLimit;
    private StrokeType strokeType;
    private double strokeWidth;

    protected ShapeBuilder() {
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Shape shape) {
        super.applyTo(shape);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    shape.setFill(this.fill);
                    break;
                }
                case 1: {
                    shape.setSmooth(this.smooth);
                    break;
                }
                case 2: {
                    shape.setStroke(this.stroke);
                    break;
                }
                case 3: {
                    shape.getStrokeDashArray().addAll(this.strokeDashArray);
                    break;
                }
                case 4: {
                    shape.setStrokeDashOffset(this.strokeDashOffset);
                    break;
                }
                case 5: {
                    shape.setStrokeLineCap(this.strokeLineCap);
                    break;
                }
                case 6: {
                    shape.setStrokeLineJoin(this.strokeLineJoin);
                    break;
                }
                case 7: {
                    shape.setStrokeMiterLimit(this.strokeMiterLimit);
                    break;
                }
                case 8: {
                    shape.setStrokeType(this.strokeType);
                    break;
                }
                case 9: {
                    shape.setStrokeWidth(this.strokeWidth);
                }
            }
        }
    }

    public B fill(Paint paint) {
        this.fill = paint;
        this.__set(0);
        return (B)this;
    }

    public B smooth(boolean bl) {
        this.smooth = bl;
        this.__set(1);
        return (B)this;
    }

    public B stroke(Paint paint) {
        this.stroke = paint;
        this.__set(2);
        return (B)this;
    }

    public B strokeDashArray(Collection<? extends Double> collection) {
        this.strokeDashArray = collection;
        this.__set(3);
        return (B)this;
    }

    public B strokeDashArray(Double ... arrdouble) {
        return this.strokeDashArray(Arrays.asList(arrdouble));
    }

    public B strokeDashOffset(double d2) {
        this.strokeDashOffset = d2;
        this.__set(4);
        return (B)this;
    }

    public B strokeLineCap(StrokeLineCap strokeLineCap) {
        this.strokeLineCap = strokeLineCap;
        this.__set(5);
        return (B)this;
    }

    public B strokeLineJoin(StrokeLineJoin strokeLineJoin) {
        this.strokeLineJoin = strokeLineJoin;
        this.__set(6);
        return (B)this;
    }

    public B strokeMiterLimit(double d2) {
        this.strokeMiterLimit = d2;
        this.__set(7);
        return (B)this;
    }

    public B strokeType(StrokeType strokeType) {
        this.strokeType = strokeType;
        this.__set(8);
        return (B)this;
    }

    public B strokeWidth(double d2) {
        this.strokeWidth = d2;
        this.__set(9);
        return (B)this;
    }
}

