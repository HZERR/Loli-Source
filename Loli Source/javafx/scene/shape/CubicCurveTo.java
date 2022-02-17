/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.shape.PathElement;

public class CubicCurveTo
extends PathElement {
    private DoubleProperty controlX1;
    private DoubleProperty controlY1;
    private DoubleProperty controlX2;
    private DoubleProperty controlY2;
    private DoubleProperty x;
    private DoubleProperty y;

    public CubicCurveTo() {
    }

    public CubicCurveTo(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.setControlX1(d2);
        this.setControlY1(d3);
        this.setControlX2(d4);
        this.setControlY2(d5);
        this.setX(d6);
        this.setY(d7);
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
                    CubicCurveTo.this.u();
                }

                @Override
                public Object getBean() {
                    return CubicCurveTo.this;
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
                    CubicCurveTo.this.u();
                }

                @Override
                public Object getBean() {
                    return CubicCurveTo.this;
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
                    CubicCurveTo.this.u();
                }

                @Override
                public Object getBean() {
                    return CubicCurveTo.this;
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
                    CubicCurveTo.this.u();
                }

                @Override
                public Object getBean() {
                    return CubicCurveTo.this;
                }

                @Override
                public String getName() {
                    return "controlY2";
                }
            };
        }
        return this.controlY2;
    }

    public final void setX(double d2) {
        if (this.x != null || d2 != 0.0) {
            this.xProperty().set(d2);
        }
    }

    public final double getX() {
        return this.x == null ? 0.0 : this.x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.x == null) {
            this.x = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurveTo.this.u();
                }

                @Override
                public Object getBean() {
                    return CubicCurveTo.this;
                }

                @Override
                public String getName() {
                    return "x";
                }
            };
        }
        return this.x;
    }

    public final void setY(double d2) {
        if (this.y != null || d2 != 0.0) {
            this.yProperty().set(d2);
        }
    }

    public final double getY() {
        return this.y == null ? 0.0 : this.y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.y == null) {
            this.y = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    CubicCurveTo.this.u();
                }

                @Override
                public Object getBean() {
                    return CubicCurveTo.this;
                }

                @Override
                public String getName() {
                    return "y";
                }
            };
        }
        return this.y;
    }

    @Override
    void addTo(NGPath nGPath) {
        if (this.isAbsolute()) {
            nGPath.addCubicTo((float)this.getControlX1(), (float)this.getControlY1(), (float)this.getControlX2(), (float)this.getControlY2(), (float)this.getX(), (float)this.getY());
        } else {
            double d2 = nGPath.getCurrentX();
            double d3 = nGPath.getCurrentY();
            nGPath.addCubicTo((float)(this.getControlX1() + d2), (float)(this.getControlY1() + d3), (float)(this.getControlX2() + d2), (float)(this.getControlY2() + d3), (float)(this.getX() + d2), (float)(this.getY() + d3));
        }
    }

    @Override
    @Deprecated
    public void impl_addTo(Path2D path2D) {
        if (this.isAbsolute()) {
            path2D.curveTo((float)this.getControlX1(), (float)this.getControlY1(), (float)this.getControlX2(), (float)this.getControlY2(), (float)this.getX(), (float)this.getY());
        } else {
            double d2 = path2D.getCurrentX();
            double d3 = path2D.getCurrentY();
            path2D.curveTo((float)(this.getControlX1() + d2), (float)(this.getControlY1() + d3), (float)(this.getControlX2() + d2), (float)(this.getControlY2() + d3), (float)(this.getX() + d2), (float)(this.getY() + d3));
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("CubicCurveTo[");
        stringBuilder.append("x=").append(this.getX());
        stringBuilder.append(", y=").append(this.getY());
        stringBuilder.append(", controlX1=").append(this.getControlX1());
        stringBuilder.append(", controlY1=").append(this.getControlY1());
        stringBuilder.append(", controlX2=").append(this.getControlX2());
        stringBuilder.append(", controlY2=").append(this.getControlY2());
        return stringBuilder.append("]").toString();
    }
}

