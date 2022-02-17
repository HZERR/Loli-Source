/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGCircle;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public class Circle
extends Shape {
    private final Ellipse2D shape = new Ellipse2D();
    private DoubleProperty centerX;
    private DoubleProperty centerY;
    private final DoubleProperty radius = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            Circle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Circle.this.impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Circle.this;
        }

        @Override
        public String getName() {
            return "radius";
        }
    };

    public Circle(double d2) {
        this.setRadius(d2);
    }

    public Circle(double d2, Paint paint) {
        this.setRadius(d2);
        this.setFill(paint);
    }

    public Circle() {
    }

    public Circle(double d2, double d3, double d4) {
        this.setCenterX(d2);
        this.setCenterY(d3);
        this.setRadius(d4);
    }

    public Circle(double d2, double d3, double d4, Paint paint) {
        this.setCenterX(d2);
        this.setCenterY(d3);
        this.setRadius(d4);
        this.setFill(paint);
    }

    public final void setCenterX(double d2) {
        if (this.centerX != null || d2 != 0.0) {
            this.centerXProperty().set(d2);
        }
    }

    public final double getCenterX() {
        return this.centerX == null ? 0.0 : this.centerX.get();
    }

    public final DoubleProperty centerXProperty() {
        if (this.centerX == null) {
            this.centerX = new DoublePropertyBase(0.0){

                @Override
                public void invalidated() {
                    Circle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Circle.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Circle.this;
                }

                @Override
                public String getName() {
                    return "centerX";
                }
            };
        }
        return this.centerX;
    }

    public final void setCenterY(double d2) {
        if (this.centerY != null || d2 != 0.0) {
            this.centerYProperty().set(d2);
        }
    }

    public final double getCenterY() {
        return this.centerY == null ? 0.0 : this.centerY.get();
    }

    public final DoubleProperty centerYProperty() {
        if (this.centerY == null) {
            this.centerY = new DoublePropertyBase(0.0){

                @Override
                public void invalidated() {
                    Circle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Circle.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Circle.this;
                }

                @Override
                public String getName() {
                    return "centerY";
                }
            };
        }
        return this.centerY;
    }

    public final void setRadius(double d2) {
        this.radius.set(d2);
    }

    public final double getRadius() {
        return this.radius.get();
    }

    public final DoubleProperty radiusProperty() {
        return this.radius;
    }

    @Override
    StrokeLineJoin convertLineJoin(StrokeLineJoin strokeLineJoin) {
        return StrokeLineJoin.BEVEL;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGCircle();
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return baseBounds.makeEmpty();
        }
        double d2 = this.getCenterX();
        double d3 = this.getCenterY();
        if ((baseTransform.getType() & 0xFFFFFFE6) == 0) {
            double d4 = d2 * baseTransform.getMxx() + d3 * baseTransform.getMxy() + baseTransform.getMxt();
            double d5 = d2 * baseTransform.getMyx() + d3 * baseTransform.getMyy() + baseTransform.getMyt();
            double d6 = this.getRadius();
            if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
                double d7 = this.getStrokeWidth();
                if (this.getStrokeType() == StrokeType.CENTERED) {
                    d7 /= 2.0;
                }
                d6 += d7;
            }
            return baseBounds.deriveWithNewBounds((float)(d4 - d6), (float)(d5 - d6), 0.0f, (float)(d4 + d6), (float)(d5 + d6), 0.0f);
        }
        if ((baseTransform.getType() & 0xFFFFFFB8) == 0) {
            double d8;
            double d9 = this.getRadius();
            double d10 = this.getCenterX() - d9;
            double d11 = this.getCenterY() - d9;
            double d12 = d8 = 2.0 * d9;
            double d13 = this.impl_mode == NGShape.Mode.FILL || this.getStrokeType() == StrokeType.INSIDE ? 0.0 : this.getStrokeWidth();
            return this.computeBounds(baseBounds, baseTransform, d13, 0.0, d10, d11, d8, d12);
        }
        return this.computeShapeBounds(baseBounds, baseTransform, this.impl_configShape());
    }

    @Override
    @Deprecated
    public Ellipse2D impl_configShape() {
        double d2 = this.getRadius();
        this.shape.setFrame((float)(this.getCenterX() - d2), (float)(this.getCenterY() - d2), (float)(d2 * 2.0), (float)(d2 * 2.0));
        return this.shape;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGCircle nGCircle = (NGCircle)this.impl_getPeer();
            nGCircle.updateCircle((float)this.getCenterX(), (float)this.getCenterY(), (float)this.getRadius());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Circle[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("centerX=").append(this.getCenterX());
        stringBuilder.append(", centerY=").append(this.getCenterY());
        stringBuilder.append(", radius=").append(this.getRadius());
        stringBuilder.append(", fill=").append(this.getFill());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }
}

