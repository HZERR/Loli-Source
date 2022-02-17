/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPolyline;
import com.sun.javafx.sg.prism.NGShape;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleableProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class Polyline
extends Shape {
    private final Path2D shape = new Path2D();
    private final ObservableList<Double> points;

    public Polyline() {
        ((StyleableProperty)((Object)this.fillProperty())).applyStyle(null, null);
        ((StyleableProperty)((Object)this.strokeProperty())).applyStyle(null, Color.BLACK);
        this.points = new TrackableObservableList<Double>(){

            @Override
            protected void onChanged(ListChangeListener.Change<Double> change) {
                Polyline.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Polyline.this.impl_geomChanged();
            }
        };
    }

    public Polyline(double ... arrd) {
        ((StyleableProperty)((Object)this.fillProperty())).applyStyle(null, null);
        ((StyleableProperty)((Object)this.strokeProperty())).applyStyle(null, Color.BLACK);
        this.points = new /* invalid duplicate definition of identical inner class */;
        if (arrd != null) {
            for (double d2 : arrd) {
                this.getPoints().add(d2);
            }
        }
    }

    public final ObservableList<Double> getPoints() {
        return this.points;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGPolyline();
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        if (this.impl_mode == NGShape.Mode.EMPTY || this.getPoints().size() <= 1) {
            return baseBounds.makeEmpty();
        }
        if (this.getPoints().size() == 2) {
            if (this.impl_mode == NGShape.Mode.FILL || this.getStrokeType() == StrokeType.INSIDE) {
                return baseBounds.makeEmpty();
            }
            double d2 = this.getStrokeWidth();
            if (this.getStrokeType() == StrokeType.CENTERED) {
                d2 /= 2.0;
            }
            return this.computeBounds(baseBounds, baseTransform, d2, 0.5, (Double)this.getPoints().get(0), (Double)this.getPoints().get(1), 0.0, 0.0);
        }
        return this.computeShapeBounds(baseBounds, baseTransform, this.impl_configShape());
    }

    @Override
    @Deprecated
    public Path2D impl_configShape() {
        double d2 = (Double)this.getPoints().get(0);
        double d3 = (Double)this.getPoints().get(1);
        this.shape.reset();
        this.shape.moveTo((float)d2, (float)d3);
        int n2 = this.getPoints().size() & 0xFFFFFFFE;
        for (int i2 = 2; i2 < n2; i2 += 2) {
            d2 = (Double)this.getPoints().get(i2);
            d3 = (Double)this.getPoints().get(i2 + 1);
            this.shape.lineTo((float)d2, (float)d3);
        }
        return this.shape;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            int n2 = this.getPoints().size() & 0xFFFFFFFE;
            float[] arrf = new float[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                arrf[i2] = (float)((Double)this.getPoints().get(i2)).doubleValue();
            }
            NGPolyline nGPolyline = (NGPolyline)this.impl_getPeer();
            nGPolyline.updatePolyline(arrf);
        }
    }

    @Override
    @Deprecated
    protected Paint impl_cssGetFillInitialValue() {
        return null;
    }

    @Override
    @Deprecated
    protected Paint impl_cssGetStrokeInitialValue() {
        return Color.BLACK;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Polyline[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("points=").append(this.getPoints());
        stringBuilder.append(", fill=").append(this.getFill());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }
}

