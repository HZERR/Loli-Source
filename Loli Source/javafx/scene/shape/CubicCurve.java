/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGCubicCurve;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

public class CubicCurve
extends Shape {
    private final CubicCurve2D shape = new CubicCurve2D();
    private DoubleProperty startX;
    private DoubleProperty startY;
    private DoubleProperty controlX1;
    private DoubleProperty controlY1;
    private DoubleProperty controlX2;
    private DoubleProperty controlY2;
    private DoubleProperty endX;
    private DoubleProperty endY;

    public CubicCurve() {
    }

    public CubicCurve(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        this.setStartX(d2);
        this.setStartY(d3);
        this.setControlX1(d4);
        this.setControlY1(d5);
        this.setControlX2(d6);
        this.setControlY2(d7);
        this.setEndX(d8);
        this.setEndY(d9);
    }

    public final void setStartX(double d2) {
        if (this.startX != null || d2 != 0.0) {
            this.startXProperty().set(d2);
        }
    }

    public final double getStartX() {
        return this.startX == null ? 0.0 : this.startX.get();
    }

    public final DoubleProperty startXProperty() {
        if (this.startX == null) {
            this.startX = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override
                public String getName() {
                    return "startX";
                }
            };
        }
        return this.startX;
    }

    public final void setStartY(double d2) {
        if (this.startY != null || d2 != 0.0) {
            this.startYProperty().set(d2);
        }
    }

    public final double getStartY() {
        return this.startY == null ? 0.0 : this.startY.get();
    }

    public final DoubleProperty startYProperty() {
        if (this.startY == null) {
            this.startY = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override
                public String getName() {
                    return "startY";
                }
            };
        }
        return this.startY;
    }

    public final void setControlX1(double d2) {
        if (this.controlX1 != null || d2 != 0.0) {
            this.controlX1Property().set(d2);
        }
    }

    public final double getControlX1() {
        return this.controlX1 == null ? 0.0 : this.controlX1.get();
    }

    public final DoubleProperty controlX1Property() {
        if (this.controlX1 == null) {
            this.controlX1 = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override
                public String getName() {
                    return "controlX1";
                }
            };
        }
        return this.controlX1;
    }

    public final void setControlY1(double d2) {
        if (this.controlY1 != null || d2 != 0.0) {
            this.controlY1Property().set(d2);
        }
    }

    public final double getControlY1() {
        return this.controlY1 == null ? 0.0 : this.controlY1.get();
    }

    public final DoubleProperty controlY1Property() {
        if (this.controlY1 == null) {
            this.controlY1 = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override
                public String getName() {
                    return "controlY1";
                }
            };
        }
        return this.controlY1;
    }

    public final void setControlX2(double d2) {
        if (this.controlX2 != null || d2 != 0.0) {
            this.controlX2Property().set(d2);
        }
    }

    public final double getControlX2() {
        return this.controlX2 == null ? 0.0 : this.controlX2.get();
    }

    public final DoubleProperty controlX2Property() {
        if (this.controlX2 == null) {
            this.controlX2 = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override
                public String getName() {
                    return "controlX2";
                }
            };
        }
        return this.controlX2;
    }

    public final void setControlY2(double d2) {
        if (this.controlY2 != null || d2 != 0.0) {
            this.controlY2Property().set(d2);
        }
    }

    public final double getControlY2() {
        return this.controlY2 == null ? 0.0 : this.controlY2.get();
    }

    public final DoubleProperty controlY2Property() {
        if (this.controlY2 == null) {
            this.controlY2 = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override
                public String getName() {
                    return "controlY2";
                }
            };
        }
        return this.controlY2;
    }

    public final void setEndX(double d2) {
        if (this.endX != null || d2 != 0.0) {
            this.endXProperty().set(d2);
        }
    }

    public final double getEndX() {
        return this.endX == null ? 0.0 : this.endX.get();
    }

    public final DoubleProperty endXProperty() {
        if (this.endX == null) {
            this.endX = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override
                public String getName() {
                    return "endX";
                }
            };
        }
        return this.endX;
    }

    public final void setEndY(double d2) {
        if (this.endY != null || d2 != 0.0) {
            this.endYProperty().set(d2);
        }
    }

    public final double getEndY() {
        return this.endY == null ? 0.0 : this.endY.get();
    }

    public final DoubleProperty endYProperty() {
        if (this.endY == null) {
            this.endY = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override
                public String getName() {
                    return "endY";
                }
            };
        }
        return this.endY;
    }

    @Override
    @Deprecated
    public CubicCurve2D impl_configShape() {
        this.shape.x1 = (float)this.getStartX();
        this.shape.y1 = (float)this.getStartY();
        this.shape.ctrlx1 = (float)this.getControlX1();
        this.shape.ctrly1 = (float)this.getControlY1();
        this.shape.ctrlx2 = (float)this.getControlX2();
        this.shape.ctrly2 = (float)this.getControlY2();
        this.shape.x2 = (float)this.getEndX();
        this.shape.y2 = (float)this.getEndY();
        return this.shape;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGCubicCurve();
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGCubicCurve nGCubicCurve = (NGCubicCurve)this.impl_getPeer();
            nGCubicCurve.updateCubicCurve((float)this.getStartX(), (float)this.getStartY(), (float)this.getEndX(), (float)this.getEndY(), (float)this.getControlX1(), (float)this.getControlY1(), (float)this.getControlX2(), (float)this.getControlY2());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("CubicCurve[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("startX=").append(this.getStartX());
        stringBuilder.append(", startY=").append(this.getStartY());
        stringBuilder.append(", controlX1=").append(this.getControlX1());
        stringBuilder.append(", controlY1=").append(this.getControlY1());
        stringBuilder.append(", controlX2=").append(this.getControlX2());
        stringBuilder.append(", controlY2=").append(this.getControlY2());
        stringBuilder.append(", endX=").append(this.getEndX());
        stringBuilder.append(", endY=").append(this.getEndY());
        stringBuilder.append(", fill=").append(this.getFill());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }
}

