/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGEllipse;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public class Ellipse
extends Shape {
    private final Ellipse2D shape = new Ellipse2D();
    private static final int NON_RECTILINEAR_TYPE_MASK = -80;
    private DoubleProperty centerX;
    private DoubleProperty centerY;
    private final DoubleProperty radiusX = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Ellipse.this.impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Ellipse.this;
        }

        @Override
        public String getName() {
            return "radiusX";
        }
    };
    private final DoubleProperty radiusY = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Ellipse.this.impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Ellipse.this;
        }

        @Override
        public String getName() {
            return "radiusY";
        }
    };

    public Ellipse() {
    }

    public Ellipse(double d2, double d3) {
        this.setRadiusX(d2);
        this.setRadiusY(d3);
    }

    public Ellipse(double d2, double d3, double d4, double d5) {
        this(d4, d5);
        this.setCenterX(d2);
        this.setCenterY(d3);
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
            this.centerX = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Ellipse.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Ellipse.this;
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
            this.centerY = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Ellipse.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Ellipse.this;
                }

                @Override
                public String getName() {
                    return "centerY";
                }
            };
        }
        return this.centerY;
    }

    public final void setRadiusX(double d2) {
        this.radiusX.set(d2);
    }

    public final double getRadiusX() {
        return this.radiusX.get();
    }

    public final DoubleProperty radiusXProperty() {
        return this.radiusX;
    }

    public final void setRadiusY(double d2) {
        this.radiusY.set(d2);
    }

    public final double getRadiusY() {
        return this.radiusY.get();
    }

    public final DoubleProperty radiusYProperty() {
        return this.radiusY;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGEllipse();
    }

    @Override
    StrokeLineJoin convertLineJoin(StrokeLineJoin strokeLineJoin) {
        return StrokeLineJoin.BEVEL;
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        double d2;
        double d3;
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return baseBounds.makeEmpty();
        }
        if ((baseTransform.getType() & 0xFFFFFFB0) != 0) {
            return this.computeShapeBounds(baseBounds, baseTransform, this.impl_configShape());
        }
        double d4 = this.getCenterX() - this.getRadiusX();
        double d5 = this.getCenterY() - this.getRadiusY();
        double d6 = 2.0 * this.getRadiusX();
        double d7 = 2.0 * this.getRadiusY();
        if (this.impl_mode == NGShape.Mode.FILL || this.getStrokeType() == StrokeType.INSIDE) {
            d3 = 0.0;
            d2 = 0.0;
        } else {
            d2 = this.getStrokeWidth();
            if (this.getStrokeType() == StrokeType.CENTERED) {
                d2 /= 2.0;
            }
            d3 = 0.0;
        }
        return this.computeBounds(baseBounds, baseTransform, d2, d3, d4, d5, d6, d7);
    }

    @Override
    @Deprecated
    public Ellipse2D impl_configShape() {
        this.shape.setFrame((float)(this.getCenterX() - this.getRadiusX()), (float)(this.getCenterY() - this.getRadiusY()), (float)(this.getRadiusX() * 2.0), (float)(this.getRadiusY() * 2.0));
        return this.shape;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGEllipse nGEllipse = (NGEllipse)this.impl_getPeer();
            nGEllipse.updateEllipse((float)this.getCenterX(), (float)this.getCenterY(), (float)this.getRadiusX(), (float)this.getRadiusY());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Ellipse[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("centerX=").append(this.getCenterX());
        stringBuilder.append(", centerY=").append(this.getCenterY());
        stringBuilder.append(", radiusX=").append(this.getRadiusX());
        stringBuilder.append(", radiusY=").append(this.getRadiusY());
        stringBuilder.append(", fill=").append(this.getFill());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }
}

