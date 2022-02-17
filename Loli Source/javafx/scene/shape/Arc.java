/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGArc;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Shape;

public class Arc
extends Shape {
    private final Arc2D shape = new Arc2D();
    private DoubleProperty centerX;
    private DoubleProperty centerY;
    private final DoubleProperty radiusX = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Arc.this.impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Arc.this;
        }

        @Override
        public String getName() {
            return "radiusX";
        }
    };
    private final DoubleProperty radiusY = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Arc.this.impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Arc.this;
        }

        @Override
        public String getName() {
            return "radiusY";
        }
    };
    private DoubleProperty startAngle;
    private final DoubleProperty length = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Arc.this.impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Arc.this;
        }

        @Override
        public String getName() {
            return "length";
        }
    };
    private ObjectProperty<ArcType> type;

    public Arc() {
    }

    public Arc(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.setCenterX(d2);
        this.setCenterY(d3);
        this.setRadiusX(d4);
        this.setRadiusY(d5);
        this.setStartAngle(d6);
        this.setLength(d7);
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
                    Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Arc.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Arc.this;
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
                    Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Arc.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Arc.this;
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

    public final void setStartAngle(double d2) {
        if (this.startAngle != null || d2 != 0.0) {
            this.startAngleProperty().set(d2);
        }
    }

    public final double getStartAngle() {
        return this.startAngle == null ? 0.0 : this.startAngle.get();
    }

    public final DoubleProperty startAngleProperty() {
        if (this.startAngle == null) {
            this.startAngle = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Arc.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Arc.this;
                }

                @Override
                public String getName() {
                    return "startAngle";
                }
            };
        }
        return this.startAngle;
    }

    public final void setLength(double d2) {
        this.length.set(d2);
    }

    public final double getLength() {
        return this.length.get();
    }

    public final DoubleProperty lengthProperty() {
        return this.length;
    }

    public final void setType(ArcType arcType) {
        if (this.type != null || arcType != ArcType.OPEN) {
            this.typeProperty().set(arcType);
        }
    }

    public final ArcType getType() {
        return this.type == null ? ArcType.OPEN : (ArcType)((Object)this.type.get());
    }

    public final ObjectProperty<ArcType> typeProperty() {
        if (this.type == null) {
            this.type = new ObjectPropertyBase<ArcType>(ArcType.OPEN){

                @Override
                public void invalidated() {
                    Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Arc.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Arc.this;
                }

                @Override
                public String getName() {
                    return "type";
                }
            };
        }
        return this.type;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGArc();
    }

    @Override
    @Deprecated
    public Arc2D impl_configShape() {
        int n2;
        switch (this.getTypeInternal()) {
            case OPEN: {
                n2 = 0;
                break;
            }
            case CHORD: {
                n2 = 1;
                break;
            }
            default: {
                n2 = 2;
            }
        }
        this.shape.setArc((float)(this.getCenterX() - this.getRadiusX()), (float)(this.getCenterY() - this.getRadiusY()), (float)(this.getRadiusX() * 2.0), (float)(this.getRadiusY() * 2.0), (float)this.getStartAngle(), (float)this.getLength(), n2);
        return this.shape;
    }

    private final ArcType getTypeInternal() {
        ArcType arcType = this.getType();
        return arcType == null ? ArcType.OPEN : arcType;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGArc nGArc = (NGArc)this.impl_getPeer();
            nGArc.updateArc((float)this.getCenterX(), (float)this.getCenterY(), (float)this.getRadiusX(), (float)this.getRadiusY(), (float)this.getStartAngle(), (float)this.getLength(), this.getTypeInternal());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Arc[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("centerX=").append(this.getCenterX());
        stringBuilder.append(", centerY=").append(this.getCenterY());
        stringBuilder.append(", radiusX=").append(this.getRadiusX());
        stringBuilder.append(", radiusY=").append(this.getRadiusY());
        stringBuilder.append(", startAngle=").append(this.getStartAngle());
        stringBuilder.append(", length=").append(this.getLength());
        stringBuilder.append(", type=").append((Object)this.getType());
        stringBuilder.append(", fill=").append(this.getFill());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }
}

