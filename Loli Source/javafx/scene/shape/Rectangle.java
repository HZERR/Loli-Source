/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.javafx.sg.prism.NGShape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public class Rectangle
extends Shape {
    private final RoundRectangle2D shape = new RoundRectangle2D();
    private static final int NON_RECTILINEAR_TYPE_MASK = -80;
    private DoubleProperty x;
    private DoubleProperty y;
    private final DoubleProperty width = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Rectangle.this.impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Rectangle.this;
        }

        @Override
        public String getName() {
            return "width";
        }
    };
    private final DoubleProperty height = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Rectangle.this.impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Rectangle.this;
        }

        @Override
        public String getName() {
            return "height";
        }
    };
    private DoubleProperty arcWidth;
    private DoubleProperty arcHeight;

    public Rectangle() {
    }

    public Rectangle(double d2, double d3) {
        this.setWidth(d2);
        this.setHeight(d3);
    }

    public Rectangle(double d2, double d3, Paint paint) {
        this.setWidth(d2);
        this.setHeight(d3);
        this.setFill(paint);
    }

    public Rectangle(double d2, double d3, double d4, double d5) {
        this(d4, d5);
        this.setX(d2);
        this.setY(d3);
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
                    Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Rectangle.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Rectangle.this;
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
                    Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Rectangle.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Rectangle.this;
                }

                @Override
                public String getName() {
                    return "y";
                }
            };
        }
        return this.y;
    }

    public final void setWidth(double d2) {
        this.width.set(d2);
    }

    public final double getWidth() {
        return this.width.get();
    }

    public final DoubleProperty widthProperty() {
        return this.width;
    }

    public final void setHeight(double d2) {
        this.height.set(d2);
    }

    public final double getHeight() {
        return this.height.get();
    }

    public final DoubleProperty heightProperty() {
        return this.height;
    }

    public final void setArcWidth(double d2) {
        if (this.arcWidth != null || d2 != 0.0) {
            this.arcWidthProperty().set(d2);
        }
    }

    public final double getArcWidth() {
        return this.arcWidth == null ? 0.0 : this.arcWidth.get();
    }

    public final DoubleProperty arcWidthProperty() {
        if (this.arcWidth == null) {
            this.arcWidth = new StyleableDoubleProperty(){

                @Override
                public void invalidated() {
                    Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                }

                @Override
                public CssMetaData<Rectangle, Number> getCssMetaData() {
                    return StyleableProperties.ARC_WIDTH;
                }

                @Override
                public Object getBean() {
                    return Rectangle.this;
                }

                @Override
                public String getName() {
                    return "arcWidth";
                }
            };
        }
        return this.arcWidth;
    }

    public final void setArcHeight(double d2) {
        if (this.arcHeight != null || d2 != 0.0) {
            this.arcHeightProperty().set(d2);
        }
    }

    public final double getArcHeight() {
        return this.arcHeight == null ? 0.0 : this.arcHeight.get();
    }

    public final DoubleProperty arcHeightProperty() {
        if (this.arcHeight == null) {
            this.arcHeight = new StyleableDoubleProperty(){

                @Override
                public void invalidated() {
                    Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                }

                @Override
                public CssMetaData<Rectangle, Number> getCssMetaData() {
                    return StyleableProperties.ARC_HEIGHT;
                }

                @Override
                public Object getBean() {
                    return Rectangle.this;
                }

                @Override
                public String getName() {
                    return "arcHeight";
                }
            };
        }
        return this.arcHeight;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGRectangle();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return Rectangle.getClassCssMetaData();
    }

    @Override
    StrokeLineJoin convertLineJoin(StrokeLineJoin strokeLineJoin) {
        if (this.getArcWidth() > 0.0 && this.getArcHeight() > 0.0) {
            return StrokeLineJoin.BEVEL;
        }
        return strokeLineJoin;
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        double d2;
        double d3;
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return baseBounds.makeEmpty();
        }
        if (this.getArcWidth() > 0.0 && this.getArcHeight() > 0.0 && (baseTransform.getType() & 0xFFFFFFB0) != 0) {
            return this.computeShapeBounds(baseBounds, baseTransform, this.impl_configShape());
        }
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
        return this.computeBounds(baseBounds, baseTransform, d2, d3, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    @Deprecated
    public RoundRectangle2D impl_configShape() {
        if (this.getArcWidth() > 0.0 && this.getArcHeight() > 0.0) {
            this.shape.setRoundRect((float)this.getX(), (float)this.getY(), (float)this.getWidth(), (float)this.getHeight(), (float)this.getArcWidth(), (float)this.getArcHeight());
        } else {
            this.shape.setRoundRect((float)this.getX(), (float)this.getY(), (float)this.getWidth(), (float)this.getHeight(), 0.0f, 0.0f);
        }
        return this.shape;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGRectangle nGRectangle = (NGRectangle)this.impl_getPeer();
            nGRectangle.updateRectangle((float)this.getX(), (float)this.getY(), (float)this.getWidth(), (float)this.getHeight(), (float)this.getArcWidth(), (float)this.getArcHeight());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Rectangle[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("x=").append(this.getX());
        stringBuilder.append(", y=").append(this.getY());
        stringBuilder.append(", width=").append(this.getWidth());
        stringBuilder.append(", height=").append(this.getHeight());
        stringBuilder.append(", fill=").append(this.getFill());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }

    private static class StyleableProperties {
        private static final CssMetaData<Rectangle, Number> ARC_HEIGHT = new CssMetaData<Rectangle, Number>("-fx-arc-height", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(Rectangle rectangle) {
                return rectangle.arcHeight == null || !rectangle.arcHeight.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Rectangle rectangle) {
                return (StyleableProperty)((Object)rectangle.arcHeightProperty());
            }
        };
        private static final CssMetaData<Rectangle, Number> ARC_WIDTH = new CssMetaData<Rectangle, Number>("-fx-arc-width", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(Rectangle rectangle) {
                return rectangle.arcWidth == null || !rectangle.arcWidth.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Rectangle rectangle) {
                return (StyleableProperty)((Object)rectangle.arcWidthProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Shape.getClassCssMetaData());
            arrayList.add(ARC_HEIGHT);
            arrayList.add(ARC_WIDTH);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

