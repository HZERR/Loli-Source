/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.shape.PathElement;

public class QuadCurveTo
extends PathElement {
    private DoubleProperty controlX = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            QuadCurveTo.this.u();
        }

        @Override
        public Object getBean() {
            return QuadCurveTo.this;
        }

        @Override
        public String getName() {
            return "controlX";
        }
    };
    private DoubleProperty controlY = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            QuadCurveTo.this.u();
        }

        @Override
        public Object getBean() {
            return QuadCurveTo.this;
        }

        @Override
        public String getName() {
            return "controlY";
        }
    };
    private DoubleProperty x;
    private DoubleProperty y;

    public QuadCurveTo() {
    }

    public QuadCurveTo(double d2, double d3, double d4, double d5) {
        this.setControlX(d2);
        this.setControlY(d3);
        this.setX(d4);
        this.setY(d5);
    }

    public final void setControlX(double d2) {
        this.controlX.set(d2);
    }

    public final double getControlX() {
        return this.controlX.get();
    }

    public final DoubleProperty controlXProperty() {
        return this.controlX;
    }

    public final void setControlY(double d2) {
        this.controlY.set(d2);
    }

    public final double getControlY() {
        return this.controlY.get();
    }

    public final DoubleProperty controlYProperty() {
        return this.controlY;
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
                    QuadCurveTo.this.u();
                }

                @Override
                public Object getBean() {
                    return QuadCurveTo.this;
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
                    QuadCurveTo.this.u();
                }

                @Override
                public Object getBean() {
                    return QuadCurveTo.this;
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
            nGPath.addQuadTo((float)this.getControlX(), (float)this.getControlY(), (float)this.getX(), (float)this.getY());
        } else {
            double d2 = nGPath.getCurrentX();
            double d3 = nGPath.getCurrentY();
            nGPath.addQuadTo((float)(this.getControlX() + d2), (float)(this.getControlY() + d3), (float)(this.getX() + d2), (float)(this.getY() + d3));
        }
    }

    @Override
    @Deprecated
    public void impl_addTo(Path2D path2D) {
        if (this.isAbsolute()) {
            path2D.quadTo((float)this.getControlX(), (float)this.getControlY(), (float)this.getX(), (float)this.getY());
        } else {
            double d2 = path2D.getCurrentX();
            double d3 = path2D.getCurrentY();
            path2D.quadTo((float)(this.getControlX() + d2), (float)(this.getControlY() + d3), (float)(this.getX() + d2), (float)(this.getY() + d3));
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("CubicCurveTo[");
        stringBuilder.append("x=").append(this.getX());
        stringBuilder.append(", y=").append(this.getY());
        stringBuilder.append(", controlX=").append(this.getControlX());
        stringBuilder.append(", controlY=").append(this.getControlY());
        return stringBuilder.append("]").toString();
    }
}

