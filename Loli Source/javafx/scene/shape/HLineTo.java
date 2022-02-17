/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.shape.PathElement;

public class HLineTo
extends PathElement {
    private DoubleProperty x = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            HLineTo.this.u();
        }

        @Override
        public Object getBean() {
            return HLineTo.this;
        }

        @Override
        public String getName() {
            return "x";
        }
    };

    public HLineTo() {
    }

    public HLineTo(double d2) {
        this.setX(d2);
    }

    public final void setX(double d2) {
        this.x.set(d2);
    }

    public final double getX() {
        return this.x.get();
    }

    public final DoubleProperty xProperty() {
        return this.x;
    }

    @Override
    void addTo(NGPath nGPath) {
        if (this.isAbsolute()) {
            nGPath.addLineTo((float)this.getX(), nGPath.getCurrentY());
        } else {
            nGPath.addLineTo((float)((double)nGPath.getCurrentX() + this.getX()), nGPath.getCurrentY());
        }
    }

    @Override
    @Deprecated
    public void impl_addTo(Path2D path2D) {
        if (this.isAbsolute()) {
            path2D.lineTo((float)this.getX(), path2D.getCurrentY());
        } else {
            path2D.lineTo((float)((double)path2D.getCurrentX() + this.getX()), path2D.getCurrentY());
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("HLineTo[");
        stringBuilder.append("x=").append(this.getX());
        return stringBuilder.append("]").toString();
    }
}

