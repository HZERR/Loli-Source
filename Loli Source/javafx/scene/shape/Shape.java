/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.Area;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public abstract class Shape
extends Node {
    @Deprecated
    protected NGShape.Mode impl_mode = NGShape.Mode.FILL;
    private ObjectProperty<Paint> fill;
    Paint old_fill;
    private ObjectProperty<Paint> stroke;
    private final AbstractNotifyListener platformImageChangeListener = new AbstractNotifyListener(){

        @Override
        public void invalidated(Observable observable) {
            Shape.this.impl_markDirty(DirtyBits.SHAPE_FILL);
            Shape.this.impl_markDirty(DirtyBits.SHAPE_STROKE);
            Shape.this.impl_geomChanged();
            Shape.this.checkModeChanged();
        }
    };
    Paint old_stroke;
    private BooleanProperty smooth;
    private static final double MIN_STROKE_WIDTH = 0.0;
    private static final double MIN_STROKE_MITER_LIMIT = 1.0;
    private Reference<Runnable> shapeChangeListener;
    private boolean strokeAttributesDirty = true;
    private StrokeAttributes strokeAttributes;
    private static final StrokeType DEFAULT_STROKE_TYPE = StrokeType.CENTERED;
    private static final double DEFAULT_STROKE_WIDTH = 1.0;
    private static final StrokeLineJoin DEFAULT_STROKE_LINE_JOIN = StrokeLineJoin.MITER;
    private static final StrokeLineCap DEFAULT_STROKE_LINE_CAP = StrokeLineCap.SQUARE;
    private static final double DEFAULT_STROKE_MITER_LIMIT = 10.0;
    private static final double DEFAULT_STROKE_DASH_OFFSET = 0.0;
    private static final float[] DEFAULT_PG_STROKE_DASH_ARRAY = new float[0];

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        throw new AssertionError((Object)"Subclasses of Shape must implement impl_createPGNode");
    }

    StrokeLineJoin convertLineJoin(StrokeLineJoin strokeLineJoin) {
        return strokeLineJoin;
    }

    public final void setStrokeType(StrokeType strokeType) {
        this.strokeTypeProperty().set(strokeType);
    }

    public final StrokeType getStrokeType() {
        return this.strokeAttributes == null ? DEFAULT_STROKE_TYPE : this.strokeAttributes.getType();
    }

    public final ObjectProperty<StrokeType> strokeTypeProperty() {
        return this.getStrokeAttributes().typeProperty();
    }

    public final void setStrokeWidth(double d2) {
        this.strokeWidthProperty().set(d2);
    }

    public final double getStrokeWidth() {
        return this.strokeAttributes == null ? 1.0 : this.strokeAttributes.getWidth();
    }

    public final DoubleProperty strokeWidthProperty() {
        return this.getStrokeAttributes().widthProperty();
    }

    public final void setStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        this.strokeLineJoinProperty().set(strokeLineJoin);
    }

    public final StrokeLineJoin getStrokeLineJoin() {
        return this.strokeAttributes == null ? DEFAULT_STROKE_LINE_JOIN : this.strokeAttributes.getLineJoin();
    }

    public final ObjectProperty<StrokeLineJoin> strokeLineJoinProperty() {
        return this.getStrokeAttributes().lineJoinProperty();
    }

    public final void setStrokeLineCap(StrokeLineCap strokeLineCap) {
        this.strokeLineCapProperty().set(strokeLineCap);
    }

    public final StrokeLineCap getStrokeLineCap() {
        return this.strokeAttributes == null ? DEFAULT_STROKE_LINE_CAP : this.strokeAttributes.getLineCap();
    }

    public final ObjectProperty<StrokeLineCap> strokeLineCapProperty() {
        return this.getStrokeAttributes().lineCapProperty();
    }

    public final void setStrokeMiterLimit(double d2) {
        this.strokeMiterLimitProperty().set(d2);
    }

    public final double getStrokeMiterLimit() {
        return this.strokeAttributes == null ? 10.0 : this.strokeAttributes.getMiterLimit();
    }

    public final DoubleProperty strokeMiterLimitProperty() {
        return this.getStrokeAttributes().miterLimitProperty();
    }

    public final void setStrokeDashOffset(double d2) {
        this.strokeDashOffsetProperty().set(d2);
    }

    public final double getStrokeDashOffset() {
        return this.strokeAttributes == null ? 0.0 : this.strokeAttributes.getDashOffset();
    }

    public final DoubleProperty strokeDashOffsetProperty() {
        return this.getStrokeAttributes().dashOffsetProperty();
    }

    public final ObservableList<Double> getStrokeDashArray() {
        return this.getStrokeAttributes().dashArrayProperty();
    }

    private NGShape.Mode computeMode() {
        if (this.getFill() != null && this.getStroke() != null) {
            return NGShape.Mode.STROKE_FILL;
        }
        if (this.getFill() != null) {
            return NGShape.Mode.FILL;
        }
        if (this.getStroke() != null) {
            return NGShape.Mode.STROKE;
        }
        return NGShape.Mode.EMPTY;
    }

    private void checkModeChanged() {
        NGShape.Mode mode = this.computeMode();
        if (this.impl_mode != mode) {
            this.impl_mode = mode;
            this.impl_markDirty(DirtyBits.SHAPE_MODE);
            this.impl_geomChanged();
        }
    }

    public final void setFill(Paint paint) {
        this.fillProperty().set(paint);
    }

    public final Paint getFill() {
        return this.fill == null ? Color.BLACK : (Paint)this.fill.get();
    }

    public final ObjectProperty<Paint> fillProperty() {
        if (this.fill == null) {
            this.fill = new StyleableObjectProperty<Paint>((Paint)Color.BLACK){
                boolean needsListener;
                {
                    this.needsListener = false;
                }

                @Override
                public void invalidated() {
                    Paint paint = (Paint)this.get();
                    if (this.needsListener) {
                        Toolkit.getPaintAccessor().removeListener(Shape.this.old_fill, Shape.this.platformImageChangeListener);
                    }
                    this.needsListener = paint != null && Toolkit.getPaintAccessor().isMutable(paint);
                    Shape.this.old_fill = paint;
                    if (this.needsListener) {
                        Toolkit.getPaintAccessor().addListener(paint, Shape.this.platformImageChangeListener);
                    }
                    Shape.this.impl_markDirty(DirtyBits.SHAPE_FILL);
                    Shape.this.checkModeChanged();
                }

                @Override
                public CssMetaData<Shape, Paint> getCssMetaData() {
                    return StyleableProperties.FILL;
                }

                @Override
                public Object getBean() {
                    return Shape.this;
                }

                @Override
                public String getName() {
                    return "fill";
                }
            };
        }
        return this.fill;
    }

    public final void setStroke(Paint paint) {
        this.strokeProperty().set(paint);
    }

    public final Paint getStroke() {
        return this.stroke == null ? null : (Paint)this.stroke.get();
    }

    public final ObjectProperty<Paint> strokeProperty() {
        if (this.stroke == null) {
            this.stroke = new StyleableObjectProperty<Paint>(){
                boolean needsListener = false;

                @Override
                public void invalidated() {
                    Paint paint = (Paint)this.get();
                    if (this.needsListener) {
                        Toolkit.getPaintAccessor().removeListener(Shape.this.old_stroke, Shape.this.platformImageChangeListener);
                    }
                    this.needsListener = paint != null && Toolkit.getPaintAccessor().isMutable(paint);
                    Shape.this.old_stroke = paint;
                    if (this.needsListener) {
                        Toolkit.getPaintAccessor().addListener(paint, Shape.this.platformImageChangeListener);
                    }
                    Shape.this.impl_markDirty(DirtyBits.SHAPE_STROKE);
                    Shape.this.checkModeChanged();
                }

                @Override
                public CssMetaData<Shape, Paint> getCssMetaData() {
                    return StyleableProperties.STROKE;
                }

                @Override
                public Object getBean() {
                    return Shape.this;
                }

                @Override
                public String getName() {
                    return "stroke";
                }
            };
        }
        return this.stroke;
    }

    public final void setSmooth(boolean bl) {
        this.smoothProperty().set(bl);
    }

    public final boolean isSmooth() {
        return this.smooth == null ? true : this.smooth.get();
    }

    public final BooleanProperty smoothProperty() {
        if (this.smooth == null) {
            this.smooth = new StyleableBooleanProperty(true){

                @Override
                public void invalidated() {
                    Shape.this.impl_markDirty(DirtyBits.NODE_SMOOTH);
                }

                @Override
                public CssMetaData<Shape, Boolean> getCssMetaData() {
                    return StyleableProperties.SMOOTH;
                }

                @Override
                public Object getBean() {
                    return Shape.this;
                }

                @Override
                public String getName() {
                    return "smooth";
                }
            };
        }
        return this.smooth;
    }

    @Deprecated
    protected Paint impl_cssGetFillInitialValue() {
        return Color.BLACK;
    }

    @Deprecated
    protected Paint impl_cssGetStrokeInitialValue() {
        return null;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return Shape.getClassCssMetaData();
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        return this.computeShapeBounds(baseBounds, baseTransform, this.impl_configShape());
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        return this.computeShapeContains(d2, d3, this.impl_configShape());
    }

    @Deprecated
    public abstract com.sun.javafx.geom.Shape impl_configShape();

    private void updatePGShape() {
        Object object;
        NGShape nGShape = (NGShape)this.impl_getPeer();
        if (this.strokeAttributesDirty && this.getStroke() != null) {
            object = this.hasStrokeDashArray() ? Shape.toPGDashArray(this.getStrokeDashArray()) : DEFAULT_PG_STROKE_DASH_ARRAY;
            nGShape.setDrawStroke((float)Utils.clampMin(this.getStrokeWidth(), 0.0), this.getStrokeType(), this.getStrokeLineCap(), this.convertLineJoin(this.getStrokeLineJoin()), (float)Utils.clampMin(this.getStrokeMiterLimit(), 1.0), (float[])object, (float)this.getStrokeDashOffset());
            this.strokeAttributesDirty = false;
        }
        if (this.impl_isDirty(DirtyBits.SHAPE_MODE)) {
            nGShape.setMode(this.impl_mode);
        }
        if (this.impl_isDirty(DirtyBits.SHAPE_FILL)) {
            object = this.getFill();
            nGShape.setFillPaint(object == null ? null : Toolkit.getPaintAccessor().getPlatformPaint((Paint)object));
        }
        if (this.impl_isDirty(DirtyBits.SHAPE_STROKE)) {
            object = this.getStroke();
            nGShape.setDrawPaint(object == null ? null : Toolkit.getPaintAccessor().getPlatformPaint((Paint)object));
        }
        if (this.impl_isDirty(DirtyBits.NODE_SMOOTH)) {
            nGShape.setSmooth(this.isSmooth());
        }
    }

    @Override
    @Deprecated
    protected void impl_markDirty(DirtyBits dirtyBits) {
        Runnable runnable;
        Runnable runnable2 = runnable = this.shapeChangeListener != null ? this.shapeChangeListener.get() : null;
        if (runnable != null && this.impl_isDirtyEmpty()) {
            runnable.run();
        }
        super.impl_markDirty(dirtyBits);
    }

    @Deprecated
    public void impl_setShapeChangeListener(Runnable runnable) {
        if (this.shapeChangeListener != null) {
            this.shapeChangeListener.clear();
        }
        this.shapeChangeListener = runnable != null ? new WeakReference<Runnable>(runnable) : null;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        this.updatePGShape();
    }

    BaseBounds computeBounds(BaseBounds baseBounds, BaseTransform baseTransform, double d2, double d3, double d4, double d5, double d6, double d7) {
        if (d6 < 0.0 || d7 < 0.0) {
            return baseBounds.makeEmpty();
        }
        double d8 = d4;
        double d9 = d5;
        double d10 = d6;
        double d11 = d7;
        double d12 = d3;
        if (baseTransform.isTranslateOrIdentity()) {
            d10 += d8;
            d11 += d9;
            if (baseTransform.getType() == 1) {
                double d13 = baseTransform.getMxt();
                double d14 = baseTransform.getMyt();
                d8 += d13;
                d9 += d14;
                d10 += d13;
                d11 += d14;
            }
            d12 += d2;
        } else {
            double d15 = baseTransform.getMxx();
            double d16 = baseTransform.getMxy();
            double d17 = baseTransform.getMyx();
            double d18 = baseTransform.getMyy();
            double d19 = (d8 -= d2) * d15 + (d9 -= d2) * d16 + baseTransform.getMxt();
            double d20 = d8 * d17 + d9 * d18 + baseTransform.getMyt();
            d8 = Math.min(Math.min(0.0, d15 *= (d10 += d2 * 2.0)), Math.min(d16 *= (d11 += d2 * 2.0), d15 + d16)) + d19;
            d9 = Math.min(Math.min(0.0, d17 *= d10), Math.min(d18 *= d11, d17 + d18)) + d20;
            d10 = Math.max(Math.max(0.0, d15), Math.max(d16, d15 + d16)) + d19;
            d11 = Math.max(Math.max(0.0, d17), Math.max(d18, d17 + d18)) + d20;
        }
        baseBounds = baseBounds.deriveWithNewBounds((float)(d8 -= d12), (float)(d9 -= d12), 0.0f, (float)(d10 += d12), (float)(d11 += d12), 0.0f);
        return baseBounds;
    }

    BaseBounds computeShapeBounds(BaseBounds baseBounds, BaseTransform baseTransform, com.sun.javafx.geom.Shape shape) {
        boolean bl;
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return baseBounds.makeEmpty();
        }
        float[] arrf = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
        boolean bl2 = this.impl_mode != NGShape.Mode.STROKE;
        boolean bl3 = bl = this.impl_mode != NGShape.Mode.FILL;
        if (bl && this.getStrokeType() == StrokeType.INSIDE) {
            bl2 = true;
            bl = false;
        }
        if (bl) {
            StrokeType strokeType = this.getStrokeType();
            double d2 = Utils.clampMin(this.getStrokeWidth(), 0.0);
            StrokeLineCap strokeLineCap = this.getStrokeLineCap();
            StrokeLineJoin strokeLineJoin = this.convertLineJoin(this.getStrokeLineJoin());
            float f2 = (float)Utils.clampMin(this.getStrokeMiterLimit(), 1.0);
            Toolkit.getToolkit().accumulateStrokeBounds(shape, arrf, strokeType, d2, strokeLineCap, strokeLineJoin, f2, baseTransform);
            arrf[0] = (float)((double)arrf[0] - 0.5);
            arrf[1] = (float)((double)arrf[1] - 0.5);
            arrf[2] = (float)((double)arrf[2] + 0.5);
            arrf[3] = (float)((double)arrf[3] + 0.5);
        } else if (bl2) {
            com.sun.javafx.geom.Shape.accumulate(arrf, shape, baseTransform);
        }
        if (arrf[2] < arrf[0] || arrf[3] < arrf[1]) {
            return baseBounds.makeEmpty();
        }
        baseBounds = baseBounds.deriveWithNewBounds(arrf[0], arrf[1], 0.0f, arrf[2], arrf[3], 0.0f);
        return baseBounds;
    }

    boolean computeShapeContains(double d2, double d3, com.sun.javafx.geom.Shape shape) {
        boolean bl;
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return false;
        }
        boolean bl2 = this.impl_mode != NGShape.Mode.STROKE;
        boolean bl3 = bl = this.impl_mode != NGShape.Mode.FILL;
        if (bl && bl2 && this.getStrokeType() == StrokeType.INSIDE) {
            bl = false;
        }
        if (bl2 && shape.contains((float)d2, (float)d3)) {
            return true;
        }
        if (bl) {
            StrokeType strokeType = this.getStrokeType();
            double d4 = Utils.clampMin(this.getStrokeWidth(), 0.0);
            StrokeLineCap strokeLineCap = this.getStrokeLineCap();
            StrokeLineJoin strokeLineJoin = this.convertLineJoin(this.getStrokeLineJoin());
            float f2 = (float)Utils.clampMin(this.getStrokeMiterLimit(), 1.0);
            return Toolkit.getToolkit().strokeContains(shape, d2, d3, strokeType, d4, strokeLineCap, strokeLineJoin, f2);
        }
        return false;
    }

    private StrokeAttributes getStrokeAttributes() {
        if (this.strokeAttributes == null) {
            this.strokeAttributes = new StrokeAttributes();
        }
        return this.strokeAttributes;
    }

    private boolean hasStrokeDashArray() {
        return this.strokeAttributes != null && this.strokeAttributes.hasDashArray();
    }

    private static float[] toPGDashArray(List<Double> list) {
        int n2 = list.size();
        float[] arrf = new float[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrf[i2] = list.get(i2).floatValue();
        }
        return arrf;
    }

    @Override
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm mXNodeAlgorithm, MXNodeAlgorithmContext mXNodeAlgorithmContext) {
        return mXNodeAlgorithm.processLeafNode(this, mXNodeAlgorithmContext);
    }

    public static Shape union(Shape shape, Shape shape2) {
        Area area = shape.getTransformedArea();
        area.add(shape2.getTransformedArea());
        return Shape.createFromGeomShape(area);
    }

    public static Shape subtract(Shape shape, Shape shape2) {
        Area area = shape.getTransformedArea();
        area.subtract(shape2.getTransformedArea());
        return Shape.createFromGeomShape(area);
    }

    public static Shape intersect(Shape shape, Shape shape2) {
        Area area = shape.getTransformedArea();
        area.intersect(shape2.getTransformedArea());
        return Shape.createFromGeomShape(area);
    }

    private Area getTransformedArea() {
        return this.getTransformedArea(Shape.calculateNodeToSceneTransform(this));
    }

    private Area getTransformedArea(BaseTransform baseTransform) {
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return new Area();
        }
        com.sun.javafx.geom.Shape shape = this.impl_configShape();
        if (this.impl_mode == NGShape.Mode.FILL || this.impl_mode == NGShape.Mode.STROKE_FILL && this.getStrokeType() == StrokeType.INSIDE) {
            return Shape.createTransformedArea(shape, baseTransform);
        }
        StrokeType strokeType = this.getStrokeType();
        double d2 = Utils.clampMin(this.getStrokeWidth(), 0.0);
        StrokeLineCap strokeLineCap = this.getStrokeLineCap();
        StrokeLineJoin strokeLineJoin = this.convertLineJoin(this.getStrokeLineJoin());
        float f2 = (float)Utils.clampMin(this.getStrokeMiterLimit(), 1.0);
        float[] arrf = this.hasStrokeDashArray() ? Shape.toPGDashArray(this.getStrokeDashArray()) : DEFAULT_PG_STROKE_DASH_ARRAY;
        com.sun.javafx.geom.Shape shape2 = Toolkit.getToolkit().createStrokedShape(shape, strokeType, d2, strokeLineCap, strokeLineJoin, f2, arrf, (float)this.getStrokeDashOffset());
        if (this.impl_mode == NGShape.Mode.STROKE) {
            return Shape.createTransformedArea(shape2, baseTransform);
        }
        Area area = new Area(shape);
        area.add(new Area(shape2));
        return Shape.createTransformedArea(area, baseTransform);
    }

    private static BaseTransform calculateNodeToSceneTransform(Node node) {
        Affine3D affine3D = new Affine3D();
        do {
            affine3D.preConcatenate(node.impl_getLeafTransform());
        } while ((node = node.getParent()) != null);
        return affine3D;
    }

    private static Area createTransformedArea(com.sun.javafx.geom.Shape shape, BaseTransform baseTransform) {
        return baseTransform.isIdentity() ? new Area(shape) : new Area(shape.getPathIterator(baseTransform));
    }

    private static Path createFromGeomShape(com.sun.javafx.geom.Shape shape) {
        Path path = new Path();
        ObservableList<PathElement> observableList = path.getElements();
        PathIterator pathIterator = shape.getPathIterator(null);
        float[] arrf = new float[6];
        while (!pathIterator.isDone()) {
            int n2 = pathIterator.currentSegment(arrf);
            switch (n2) {
                case 0: {
                    observableList.add(new MoveTo(arrf[0], arrf[1]));
                    break;
                }
                case 1: {
                    observableList.add(new LineTo(arrf[0], arrf[1]));
                    break;
                }
                case 2: {
                    observableList.add(new QuadCurveTo(arrf[0], arrf[1], arrf[2], arrf[3]));
                    break;
                }
                case 3: {
                    observableList.add(new CubicCurveTo(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5]));
                    break;
                }
                case 4: {
                    observableList.add(new ClosePath());
                }
            }
            pathIterator.next();
        }
        path.setFillRule(pathIterator.getWindingRule() == 0 ? FillRule.EVEN_ODD : FillRule.NON_ZERO);
        path.setFill(Color.BLACK);
        path.setStroke(null);
        return path;
    }

    private final class StrokeAttributes {
        private ObjectProperty<StrokeType> type;
        private DoubleProperty width;
        private ObjectProperty<StrokeLineJoin> lineJoin;
        private ObjectProperty<StrokeLineCap> lineCap;
        private DoubleProperty miterLimit;
        private DoubleProperty dashOffset;
        private ObservableList<Double> dashArray;
        private ObjectProperty<Number[]> cssDashArray = null;

        private StrokeAttributes() {
        }

        public final StrokeType getType() {
            return this.type == null ? DEFAULT_STROKE_TYPE : (StrokeType)((Object)this.type.get());
        }

        public final ObjectProperty<StrokeType> typeProperty() {
            if (this.type == null) {
                this.type = new StyleableObjectProperty<StrokeType>(DEFAULT_STROKE_TYPE){

                    @Override
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_TYPE);
                    }

                    @Override
                    public CssMetaData<Shape, StrokeType> getCssMetaData() {
                        return StyleableProperties.STROKE_TYPE;
                    }

                    @Override
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override
                    public String getName() {
                        return "strokeType";
                    }
                };
            }
            return this.type;
        }

        public double getWidth() {
            return this.width == null ? 1.0 : this.width.get();
        }

        public final DoubleProperty widthProperty() {
            if (this.width == null) {
                this.width = new StyleableDoubleProperty(1.0){

                    @Override
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_WIDTH);
                    }

                    @Override
                    public CssMetaData<Shape, Number> getCssMetaData() {
                        return StyleableProperties.STROKE_WIDTH;
                    }

                    @Override
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override
                    public String getName() {
                        return "strokeWidth";
                    }
                };
            }
            return this.width;
        }

        public StrokeLineJoin getLineJoin() {
            return this.lineJoin == null ? DEFAULT_STROKE_LINE_JOIN : (StrokeLineJoin)((Object)this.lineJoin.get());
        }

        public final ObjectProperty<StrokeLineJoin> lineJoinProperty() {
            if (this.lineJoin == null) {
                this.lineJoin = new StyleableObjectProperty<StrokeLineJoin>(DEFAULT_STROKE_LINE_JOIN){

                    @Override
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_LINE_JOIN);
                    }

                    @Override
                    public CssMetaData<Shape, StrokeLineJoin> getCssMetaData() {
                        return StyleableProperties.STROKE_LINE_JOIN;
                    }

                    @Override
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override
                    public String getName() {
                        return "strokeLineJoin";
                    }
                };
            }
            return this.lineJoin;
        }

        public StrokeLineCap getLineCap() {
            return this.lineCap == null ? DEFAULT_STROKE_LINE_CAP : (StrokeLineCap)((Object)this.lineCap.get());
        }

        public final ObjectProperty<StrokeLineCap> lineCapProperty() {
            if (this.lineCap == null) {
                this.lineCap = new StyleableObjectProperty<StrokeLineCap>(DEFAULT_STROKE_LINE_CAP){

                    @Override
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_LINE_CAP);
                    }

                    @Override
                    public CssMetaData<Shape, StrokeLineCap> getCssMetaData() {
                        return StyleableProperties.STROKE_LINE_CAP;
                    }

                    @Override
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override
                    public String getName() {
                        return "strokeLineCap";
                    }
                };
            }
            return this.lineCap;
        }

        public double getMiterLimit() {
            return this.miterLimit == null ? 10.0 : this.miterLimit.get();
        }

        public final DoubleProperty miterLimitProperty() {
            if (this.miterLimit == null) {
                this.miterLimit = new StyleableDoubleProperty(10.0){

                    @Override
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_MITER_LIMIT);
                    }

                    @Override
                    public CssMetaData<Shape, Number> getCssMetaData() {
                        return StyleableProperties.STROKE_MITER_LIMIT;
                    }

                    @Override
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override
                    public String getName() {
                        return "strokeMiterLimit";
                    }
                };
            }
            return this.miterLimit;
        }

        public double getDashOffset() {
            return this.dashOffset == null ? 0.0 : this.dashOffset.get();
        }

        public final DoubleProperty dashOffsetProperty() {
            if (this.dashOffset == null) {
                this.dashOffset = new StyleableDoubleProperty(0.0){

                    @Override
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_DASH_OFFSET);
                    }

                    @Override
                    public CssMetaData<Shape, Number> getCssMetaData() {
                        return StyleableProperties.STROKE_DASH_OFFSET;
                    }

                    @Override
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override
                    public String getName() {
                        return "strokeDashOffset";
                    }
                };
            }
            return this.dashOffset;
        }

        public ObservableList<Double> dashArrayProperty() {
            if (this.dashArray == null) {
                this.dashArray = new TrackableObservableList<Double>(){

                    @Override
                    protected void onChanged(ListChangeListener.Change<Double> change) {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_DASH_ARRAY);
                    }
                };
            }
            return this.dashArray;
        }

        private ObjectProperty<Number[]> cssDashArrayProperty() {
            if (this.cssDashArray == null) {
                this.cssDashArray = new StyleableObjectProperty<Number[]>(){

                    @Override
                    public void set(Number[] arrnumber) {
                        ObservableList<Double> observableList = StrokeAttributes.this.dashArrayProperty();
                        observableList.clear();
                        if (arrnumber != null && arrnumber.length > 0) {
                            for (int i2 = 0; i2 < arrnumber.length; ++i2) {
                                observableList.add(arrnumber[i2].doubleValue());
                            }
                        }
                    }

                    @Override
                    public Double[] get() {
                        ObservableList<Double> observableList = StrokeAttributes.this.dashArrayProperty();
                        return observableList.toArray(new Double[observableList.size()]);
                    }

                    @Override
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override
                    public String getName() {
                        return "cssDashArray";
                    }

                    @Override
                    public CssMetaData<Shape, Number[]> getCssMetaData() {
                        return StyleableProperties.STROKE_DASH_ARRAY;
                    }
                };
            }
            return this.cssDashArray;
        }

        public boolean canSetType() {
            return this.type == null || !this.type.isBound();
        }

        public boolean canSetWidth() {
            return this.width == null || !this.width.isBound();
        }

        public boolean canSetLineJoin() {
            return this.lineJoin == null || !this.lineJoin.isBound();
        }

        public boolean canSetLineCap() {
            return this.lineCap == null || !this.lineCap.isBound();
        }

        public boolean canSetMiterLimit() {
            return this.miterLimit == null || !this.miterLimit.isBound();
        }

        public boolean canSetDashOffset() {
            return this.dashOffset == null || !this.dashOffset.isBound();
        }

        public boolean hasDashArray() {
            return this.dashArray != null;
        }

        private void invalidated(CssMetaData<Shape, ?> cssMetaData) {
            Shape.this.impl_markDirty(DirtyBits.SHAPE_STROKEATTRS);
            Shape.this.strokeAttributesDirty = true;
            if (cssMetaData != StyleableProperties.STROKE_DASH_OFFSET) {
                Shape.this.impl_geomChanged();
            }
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<Shape, Paint> FILL = new CssMetaData<Shape, Paint>("-fx-fill", PaintConverter.getInstance(), (Paint)Color.BLACK){

            @Override
            public boolean isSettable(Shape shape) {
                return shape.fill == null || !shape.fill.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.fillProperty());
            }

            @Override
            public Paint getInitialValue(Shape shape) {
                return shape.impl_cssGetFillInitialValue();
            }
        };
        private static final CssMetaData<Shape, Boolean> SMOOTH = new CssMetaData<Shape, Boolean>("-fx-smooth", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(Shape shape) {
                return shape.smooth == null || !shape.smooth.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.smoothProperty());
            }
        };
        private static final CssMetaData<Shape, Paint> STROKE = new CssMetaData<Shape, Paint>("-fx-stroke", PaintConverter.getInstance()){

            @Override
            public boolean isSettable(Shape shape) {
                return shape.stroke == null || !shape.stroke.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.strokeProperty());
            }

            @Override
            public Paint getInitialValue(Shape shape) {
                return shape.impl_cssGetStrokeInitialValue();
            }
        };
        private static final CssMetaData<Shape, Number[]> STROKE_DASH_ARRAY = new CssMetaData<Shape, Number[]>("-fx-stroke-dash-array", (StyleConverter)SizeConverter.SequenceConverter.getInstance(), (Number[])new Double[0]){

            @Override
            public boolean isSettable(Shape shape) {
                return true;
            }

            @Override
            public StyleableProperty<Number[]> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.getStrokeAttributes().cssDashArrayProperty());
            }
        };
        private static final CssMetaData<Shape, Number> STROKE_DASH_OFFSET = new CssMetaData<Shape, Number>("-fx-stroke-dash-offset", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(Shape shape) {
                return shape.strokeAttributes == null || shape.strokeAttributes.canSetDashOffset();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.strokeDashOffsetProperty());
            }
        };
        private static final CssMetaData<Shape, StrokeLineCap> STROKE_LINE_CAP = new CssMetaData<Shape, StrokeLineCap>("-fx-stroke-line-cap", new EnumConverter<StrokeLineCap>(StrokeLineCap.class), StrokeLineCap.SQUARE){

            @Override
            public boolean isSettable(Shape shape) {
                return shape.strokeAttributes == null || shape.strokeAttributes.canSetLineCap();
            }

            @Override
            public StyleableProperty<StrokeLineCap> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.strokeLineCapProperty());
            }
        };
        private static final CssMetaData<Shape, StrokeLineJoin> STROKE_LINE_JOIN = new CssMetaData<Shape, StrokeLineJoin>("-fx-stroke-line-join", new EnumConverter<StrokeLineJoin>(StrokeLineJoin.class), StrokeLineJoin.MITER){

            @Override
            public boolean isSettable(Shape shape) {
                return shape.strokeAttributes == null || shape.strokeAttributes.canSetLineJoin();
            }

            @Override
            public StyleableProperty<StrokeLineJoin> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.strokeLineJoinProperty());
            }
        };
        private static final CssMetaData<Shape, StrokeType> STROKE_TYPE = new CssMetaData<Shape, StrokeType>("-fx-stroke-type", new EnumConverter<StrokeType>(StrokeType.class), StrokeType.CENTERED){

            @Override
            public boolean isSettable(Shape shape) {
                return shape.strokeAttributes == null || shape.strokeAttributes.canSetType();
            }

            @Override
            public StyleableProperty<StrokeType> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.strokeTypeProperty());
            }
        };
        private static final CssMetaData<Shape, Number> STROKE_MITER_LIMIT = new CssMetaData<Shape, Number>("-fx-stroke-miter-limit", SizeConverter.getInstance(), (Number)10.0){

            @Override
            public boolean isSettable(Shape shape) {
                return shape.strokeAttributes == null || shape.strokeAttributes.canSetMiterLimit();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.strokeMiterLimitProperty());
            }
        };
        private static final CssMetaData<Shape, Number> STROKE_WIDTH = new CssMetaData<Shape, Number>("-fx-stroke-width", SizeConverter.getInstance(), (Number)1.0){

            @Override
            public boolean isSettable(Shape shape) {
                return shape.strokeAttributes == null || shape.strokeAttributes.canSetWidth();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Shape shape) {
                return (StyleableProperty)((Object)shape.strokeWidthProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Node.getClassCssMetaData());
            arrayList.add(FILL);
            arrayList.add(SMOOTH);
            arrayList.add(STROKE);
            arrayList.add(STROKE_DASH_ARRAY);
            arrayList.add(STROKE_DASH_OFFSET);
            arrayList.add(STROKE_LINE_CAP);
            arrayList.add(STROKE_LINE_JOIN);
            arrayList.add(STROKE_TYPE);
            arrayList.add(STROKE_MITER_LIMIT);
            arrayList.add(STROKE_WIDTH);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

