/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGLine;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.css.StyleableProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

public class Line
extends Shape {
    private final Line2D shape = new Line2D();
    private final DoubleProperty startX;
    private final DoubleProperty startY;
    private final DoubleProperty endX;
    private final DoubleProperty endY;

    public Line() {
        ((StyleableProperty)((Object)this.fillProperty())).applyStyle(null, null);
        ((StyleableProperty)((Object)this.strokeProperty())).applyStyle(null, Color.BLACK);
        this.startX = new DoublePropertyBase(){

            @Override
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override
            public Object getBean() {
                return Line.this;
            }

            @Override
            public String getName() {
                return "startX";
            }
        };
        this.startY = new DoublePropertyBase(){

            @Override
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override
            public Object getBean() {
                return Line.this;
            }

            @Override
            public String getName() {
                return "startY";
            }
        };
        this.endX = new DoublePropertyBase(){

            @Override
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override
            public Object getBean() {
                return Line.this;
            }

            @Override
            public String getName() {
                return "endX";
            }
        };
        this.endY = new DoublePropertyBase(){

            @Override
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override
            public Object getBean() {
                return Line.this;
            }

            @Override
            public String getName() {
                return "endY";
            }
        };
    }

    public Line(double d2, double d3, double d4, double d5) {
        ((StyleableProperty)((Object)this.fillProperty())).applyStyle(null, null);
        ((StyleableProperty)((Object)this.strokeProperty())).applyStyle(null, Color.BLACK);
        this.startX = new /* invalid duplicate definition of identical inner class */;
        this.startY = new /* invalid duplicate definition of identical inner class */;
        this.endX = new /* invalid duplicate definition of identical inner class */;
        this.endY = new /* invalid duplicate definition of identical inner class */;
        this.setStartX(d2);
        this.setStartY(d3);
        this.setEndX(d4);
        this.setEndY(d5);
    }

    public final void setStartX(double d2) {
        this.startX.set(d2);
    }

    public final double getStartX() {
        return this.startX.get();
    }

    public final DoubleProperty startXProperty() {
        return this.startX;
    }

    public final void setStartY(double d2) {
        this.startY.set(d2);
    }

    public final double getStartY() {
        return this.startY.get();
    }

    public final DoubleProperty startYProperty() {
        return this.startY;
    }

    public final void setEndX(double d2) {
        this.endX.set(d2);
    }

    public final double getEndX() {
        return this.endX.get();
    }

    public final DoubleProperty endXProperty() {
        return this.endX;
    }

    public final void setEndY(double d2) {
        this.endY.set(d2);
    }

    public final double getEndY() {
        return this.endY.get();
    }

    public final DoubleProperty endYProperty() {
        return this.endY;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGLine();
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        double d2;
        double d3;
        if (this.impl_mode == NGShape.Mode.FILL || this.impl_mode == NGShape.Mode.EMPTY || this.getStrokeType() == StrokeType.INSIDE) {
            return baseBounds.makeEmpty();
        }
        double d4 = this.getStartX();
        double d5 = this.getEndX();
        double d6 = this.getStartY();
        double d7 = this.getEndY();
        double d8 = this.getStrokeWidth();
        if (this.getStrokeType() == StrokeType.CENTERED) {
            d8 /= 2.0;
        }
        if (baseTransform.isTranslateOrIdentity()) {
            double d9;
            double d10;
            double d11;
            d8 = Math.max(d8, 0.5);
            if (baseTransform.getType() == 1) {
                d11 = baseTransform.getMxt();
                double d12 = baseTransform.getMyt();
                d4 += d11;
                d6 += d12;
                d5 += d11;
                d7 += d12;
            }
            if (d6 == d7 && d4 != d5) {
                d10 = d8;
                d9 = this.getStrokeLineCap() == StrokeLineCap.BUTT ? 0.0 : d8;
            } else if (d4 == d5 && d6 != d7) {
                d9 = d8;
                d10 = this.getStrokeLineCap() == StrokeLineCap.BUTT ? 0.0 : d8;
            } else {
                if (this.getStrokeLineCap() == StrokeLineCap.SQUARE) {
                    d8 *= Math.sqrt(2.0);
                }
                d9 = d10 = d8;
            }
            if (d4 > d5) {
                d11 = d4;
                d4 = d5;
                d5 = d11;
            }
            if (d6 > d7) {
                d11 = d6;
                d6 = d7;
                d7 = d11;
            }
            baseBounds = baseBounds.deriveWithNewBounds((float)(d4 -= d9), (float)(d6 -= d10), 0.0f, (float)(d5 += d9), (float)(d7 += d10), 0.0f);
            return baseBounds;
        }
        double d13 = d5 - d4;
        double d14 = d7 - d6;
        double d15 = Math.sqrt(d13 * d13 + d14 * d14);
        if (d15 == 0.0) {
            d13 = d8;
            d14 = 0.0;
        } else {
            d13 = d8 * d13 / d15;
            d14 = d8 * d14 / d15;
        }
        if (this.getStrokeLineCap() != StrokeLineCap.BUTT) {
            d3 = d13;
            d2 = d14;
        } else {
            d2 = 0.0;
            d3 = 0.0;
        }
        double[] arrd = new double[]{d4 - d14 - d3, d6 + d13 - d2, d4 + d14 - d3, d6 - d13 - d2, d5 + d14 + d3, d7 - d13 + d2, d5 - d14 + d3, d7 + d13 + d2};
        baseTransform.transform(arrd, 0, arrd, 0, 4);
        d4 = Math.min(Math.min(arrd[0], arrd[2]), Math.min(arrd[4], arrd[6]));
        d6 = Math.min(Math.min(arrd[1], arrd[3]), Math.min(arrd[5], arrd[7]));
        d5 = Math.max(Math.max(arrd[0], arrd[2]), Math.max(arrd[4], arrd[6]));
        d7 = Math.max(Math.max(arrd[1], arrd[3]), Math.max(arrd[5], arrd[7]));
        baseBounds = baseBounds.deriveWithNewBounds((float)(d4 -= 0.5), (float)(d6 -= 0.5), 0.0f, (float)(d5 += 0.5), (float)(d7 += 0.5), 0.0f);
        return baseBounds;
    }

    @Override
    @Deprecated
    public Line2D impl_configShape() {
        this.shape.setLine((float)this.getStartX(), (float)this.getStartY(), (float)this.getEndX(), (float)this.getEndY());
        return this.shape;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGLine nGLine = (NGLine)this.impl_getPeer();
            nGLine.updateLine((float)this.getStartX(), (float)this.getStartY(), (float)this.getEndX(), (float)this.getEndY());
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
        StringBuilder stringBuilder = new StringBuilder("Line[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("startX=").append(this.getStartX());
        stringBuilder.append(", startY=").append(this.getStartY());
        stringBuilder.append(", endX=").append(this.getEndX());
        stringBuilder.append(", endY=").append(this.getEndY());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }
}

