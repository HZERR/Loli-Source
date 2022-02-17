/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.ShapeConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGRegion;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.TempState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundConverter;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderConverter;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.util.Callback;
import sun.util.logging.PlatformLogger;

public class Region
extends Parent {
    public static final double USE_PREF_SIZE = Double.NEGATIVE_INFINITY;
    public static final double USE_COMPUTED_SIZE = -1.0;
    static Vec2d TEMP_VEC2D = new Vec2d();
    private InvalidationListener imageChangeListener = observable -> {
        ReadOnlyObjectPropertyBase readOnlyObjectPropertyBase = (ReadOnlyObjectPropertyBase)observable;
        Image image = (Image)readOnlyObjectPropertyBase.getBean();
        Toolkit.ImageAccessor imageAccessor = Toolkit.getImageAccessor();
        if (image.getProgress() == 1.0 && !imageAccessor.isAnimation(image)) {
            this.removeImageListener(image);
        }
        this.impl_markDirty(DirtyBits.NODE_CONTENTS);
    };
    private BooleanProperty snapToPixel;
    private boolean _snapToPixel = true;
    private ObjectProperty<Insets> padding = new StyleableObjectProperty<Insets>(Insets.EMPTY){
        private Insets lastValidValue;
        {
            this.lastValidValue = Insets.EMPTY;
        }

        @Override
        public Object getBean() {
            return Region.this;
        }

        @Override
        public String getName() {
            return "padding";
        }

        @Override
        public CssMetaData<Region, Insets> getCssMetaData() {
            return StyleableProperties.PADDING;
        }

        @Override
        public void invalidated() {
            Insets insets = (Insets)this.get();
            if (insets == null) {
                if (this.isBound()) {
                    this.unbind();
                }
                this.set(this.lastValidValue);
                throw new NullPointerException("cannot set padding to null");
            }
            if (!insets.equals(this.lastValidValue)) {
                this.lastValidValue = insets;
                Region.this.insets.fireValueChanged();
            }
        }
    };
    private final ObjectProperty<Background> background = new StyleableObjectProperty<Background>(null){
        private Background old;
        {
            this.old = null;
        }

        @Override
        public Object getBean() {
            return Region.this;
        }

        @Override
        public String getName() {
            return "background";
        }

        @Override
        public CssMetaData<Region, Background> getCssMetaData() {
            return StyleableProperties.BACKGROUND;
        }

        @Override
        protected void invalidated() {
            Background background = (Background)this.get();
            if (this.old != null ? !this.old.equals(background) : background != null) {
                if (this.old == null || background == null || !this.old.getOutsets().equals(background.getOutsets())) {
                    Region.this.impl_geomChanged();
                    Region.this.insets.fireValueChanged();
                }
                if (background != null) {
                    for (BackgroundImage backgroundImage : background.getImages()) {
                        Image image = backgroundImage.image;
                        Toolkit.ImageAccessor imageAccessor = Toolkit.getImageAccessor();
                        if (!imageAccessor.isAnimation(image) && !(image.getProgress() < 1.0)) continue;
                        Region.this.addImageListener(image);
                    }
                }
                if (this.old != null) {
                    for (BackgroundImage backgroundImage : this.old.getImages()) {
                        Region.this.removeImageListener(backgroundImage.image);
                    }
                }
                Region.this.impl_markDirty(DirtyBits.SHAPE_FILL);
                Region.this.cornersValid = false;
                this.old = background;
            }
        }
    };
    private final ObjectProperty<Border> border = new StyleableObjectProperty<Border>(null){
        private Border old;
        {
            this.old = null;
        }

        @Override
        public Object getBean() {
            return Region.this;
        }

        @Override
        public String getName() {
            return "border";
        }

        @Override
        public CssMetaData<Region, Border> getCssMetaData() {
            return StyleableProperties.BORDER;
        }

        @Override
        protected void invalidated() {
            Border border = (Border)this.get();
            if (this.old != null ? !this.old.equals(border) : border != null) {
                if (this.old == null || border == null || !this.old.getOutsets().equals(border.getOutsets())) {
                    Region.this.impl_geomChanged();
                }
                if (this.old == null || border == null || !this.old.getInsets().equals(border.getInsets())) {
                    Region.this.insets.fireValueChanged();
                }
                if (border != null) {
                    for (BorderImage borderImage : border.getImages()) {
                        Image image = borderImage.image;
                        Toolkit.ImageAccessor imageAccessor = Toolkit.getImageAccessor();
                        if (!imageAccessor.isAnimation(image) && !(image.getProgress() < 1.0)) continue;
                        Region.this.addImageListener(image);
                    }
                }
                if (this.old != null) {
                    for (BorderImage borderImage : this.old.getImages()) {
                        Region.this.removeImageListener(borderImage.image);
                    }
                }
                Region.this.impl_markDirty(DirtyBits.SHAPE_STROKE);
                Region.this.cornersValid = false;
                this.old = border;
            }
        }
    };
    private ObjectProperty<Insets> opaqueInsets;
    private final InsetsProperty insets = new InsetsProperty();
    private double snappedTopInset = 0.0;
    private double snappedRightInset = 0.0;
    private double snappedBottomInset = 0.0;
    private double snappedLeftInset = 0.0;
    private ReadOnlyDoubleWrapper width;
    private double _width;
    private ReadOnlyDoubleWrapper height;
    private double _height;
    private DoubleProperty minWidth;
    private double _minWidth = -1.0;
    private DoubleProperty minHeight;
    private double _minHeight = -1.0;
    private DoubleProperty prefWidth;
    private double _prefWidth = -1.0;
    private DoubleProperty prefHeight;
    private double _prefHeight = -1.0;
    private DoubleProperty maxWidth;
    private double _maxWidth = -1.0;
    private DoubleProperty maxHeight;
    private double _maxHeight = -1.0;
    private ObjectProperty<javafx.scene.shape.Shape> shape = null;
    private javafx.scene.shape.Shape _shape;
    private BooleanProperty scaleShape = null;
    private BooleanProperty centerShape = null;
    private BooleanProperty cacheShape = null;
    private boolean cornersValid;
    private List<CornerRadii> normalizedFillCorners;
    private List<CornerRadii> normalizedStrokeCorners;
    private Bounds boundingBox;

    static double boundedSize(double d2, double d3, double d4) {
        double d5 = d3 >= d2 ? d3 : d2;
        double d6 = d2 >= d4 ? d2 : d4;
        return d5 <= d6 ? d5 : d6;
    }

    double adjustWidthByMargin(double d2, Insets insets) {
        if (insets == null || insets == Insets.EMPTY) {
            return d2;
        }
        boolean bl = this.isSnapToPixel();
        return d2 - Region.snapSpace(insets.getLeft(), bl) - Region.snapSpace(insets.getRight(), bl);
    }

    double adjustHeightByMargin(double d2, Insets insets) {
        if (insets == null || insets == Insets.EMPTY) {
            return d2;
        }
        boolean bl = this.isSnapToPixel();
        return d2 - Region.snapSpace(insets.getTop(), bl) - Region.snapSpace(insets.getBottom(), bl);
    }

    private static double snapSpace(double d2, boolean bl) {
        return bl ? (double)Math.round(d2) : d2;
    }

    private static double snapSize(double d2, boolean bl) {
        return bl ? Math.ceil(d2) : d2;
    }

    private static double snapPosition(double d2, boolean bl) {
        return bl ? (double)Math.round(d2) : d2;
    }

    private static double snapPortion(double d2, boolean bl) {
        if (bl) {
            return d2 == 0.0 ? 0.0 : (d2 > 0.0 ? Math.max(1.0, Math.floor(d2)) : Math.min(-1.0, Math.ceil(d2)));
        }
        return d2;
    }

    double getAreaBaselineOffset(List<Node> list, Callback<Node, Insets> callback, Function<Integer, Double> function, double d2, boolean bl) {
        return Region.getAreaBaselineOffset(list, callback, function, d2, bl, this.isSnapToPixel());
    }

    static double getAreaBaselineOffset(List<Node> list, Callback<Node, Insets> callback, Function<Integer, Double> function, double d2, boolean bl, boolean bl2) {
        return Region.getAreaBaselineOffset(list, callback, function, d2, bl, Region.getMinBaselineComplement(list), bl2);
    }

    double getAreaBaselineOffset(List<Node> list, Callback<Node, Insets> callback, Function<Integer, Double> function, double d2, boolean bl, double d3) {
        return Region.getAreaBaselineOffset(list, callback, function, d2, bl, d3, this.isSnapToPixel());
    }

    static double getAreaBaselineOffset(List<Node> list, Callback<Node, Insets> callback, Function<Integer, Double> function, double d2, boolean bl, double d3, boolean bl2) {
        return Region.getAreaBaselineOffset(list, callback, function, d2, n2 -> bl, d3, bl2);
    }

    double getAreaBaselineOffset(List<Node> list, Callback<Node, Insets> callback, Function<Integer, Double> function, double d2, Function<Integer, Boolean> function2, double d3) {
        return Region.getAreaBaselineOffset(list, callback, function, d2, function2, d3, this.isSnapToPixel());
    }

    static double getAreaBaselineOffset(List<Node> list, Callback<Node, Insets> callback, Function<Integer, Double> function, double d2, Function<Integer, Boolean> function2, double d3, boolean bl) {
        double d4 = 0.0;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            Node node = list.get(i2);
            Insets insets = callback.call(node);
            double d5 = insets != null ? Region.snapSpace(insets.getTop(), bl) : 0.0;
            double d6 = insets != null ? Region.snapSpace(insets.getBottom(), bl) : 0.0;
            double d7 = node.getBaselineOffset();
            if (d7 == Double.NEGATIVE_INFINITY) {
                double d8 = -1.0;
                if (node.getContentBias() == Orientation.HORIZONTAL) {
                    d8 = function.apply(i2);
                }
                if (function2.apply(i2).booleanValue()) {
                    d4 = Math.max(d4, d5 + Region.boundedSize(node.minHeight(d8), d2 - d3 - d5 - d6, node.maxHeight(d8)));
                    continue;
                }
                d4 = Math.max(d4, d5 + Region.boundedSize(node.minHeight(d8), node.prefHeight(d8), Math.min(node.maxHeight(d8), d2 - d3 - d5 - d6)));
                continue;
            }
            d4 = Math.max(d4, d5 + d7);
        }
        return d4;
    }

    static double getMinBaselineComplement(List<Node> list) {
        return Region.getBaselineComplement(list, true, false);
    }

    static double getPrefBaselineComplement(List<Node> list) {
        return Region.getBaselineComplement(list, false, false);
    }

    static double getMaxBaselineComplement(List<Node> list) {
        return Region.getBaselineComplement(list, false, true);
    }

    private static double getBaselineComplement(List<Node> list, boolean bl, boolean bl2) {
        double d2 = 0.0;
        for (Node node : list) {
            double d3 = node.getBaselineOffset();
            if (d3 == Double.NEGATIVE_INFINITY) continue;
            if (node.isResizable()) {
                d2 = Math.max(d2, (bl ? node.minHeight(-1.0) : (bl2 ? node.maxHeight(-1.0) : node.prefHeight(-1.0))) - d3);
                continue;
            }
            d2 = Math.max(d2, node.getLayoutBounds().getHeight() - d3);
        }
        return d2;
    }

    static double computeXOffset(double d2, double d3, HPos hPos) {
        switch (hPos) {
            case LEFT: {
                return 0.0;
            }
            case CENTER: {
                return (d2 - d3) / 2.0;
            }
            case RIGHT: {
                return d2 - d3;
            }
        }
        throw new AssertionError((Object)"Unhandled hPos");
    }

    static double computeYOffset(double d2, double d3, VPos vPos) {
        switch (vPos) {
            case BASELINE: 
            case TOP: {
                return 0.0;
            }
            case CENTER: {
                return (d2 - d3) / 2.0;
            }
            case BOTTOM: {
                return d2 - d3;
            }
        }
        throw new AssertionError((Object)"Unhandled vPos");
    }

    static double[] createDoubleArray(int n2, double d2) {
        double[] arrd = new double[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrd[i2] = d2;
        }
        return arrd;
    }

    public Region() {
        this.setPickOnBounds(true);
    }

    public final boolean isSnapToPixel() {
        return this._snapToPixel;
    }

    public final void setSnapToPixel(boolean bl) {
        if (this.snapToPixel == null) {
            if (this._snapToPixel != bl) {
                this._snapToPixel = bl;
                this.updateSnappedInsets();
                this.requestParentLayout();
            }
        } else {
            this.snapToPixel.set(bl);
        }
    }

    public final BooleanProperty snapToPixelProperty() {
        if (this.snapToPixel == null) {
            this.snapToPixel = new StyleableBooleanProperty(this._snapToPixel){

                @Override
                public Object getBean() {
                    return Region.this;
                }

                @Override
                public String getName() {
                    return "snapToPixel";
                }

                @Override
                public CssMetaData<Region, Boolean> getCssMetaData() {
                    return StyleableProperties.SNAP_TO_PIXEL;
                }

                @Override
                public void invalidated() {
                    boolean bl = this.get();
                    if (Region.this._snapToPixel != bl) {
                        Region.this._snapToPixel = bl;
                        Region.this.updateSnappedInsets();
                        Region.this.requestParentLayout();
                    }
                }
            };
        }
        return this.snapToPixel;
    }

    public final void setPadding(Insets insets) {
        this.padding.set(insets);
    }

    public final Insets getPadding() {
        return (Insets)this.padding.get();
    }

    public final ObjectProperty<Insets> paddingProperty() {
        return this.padding;
    }

    public final void setBackground(Background background) {
        this.background.set(background);
    }

    public final Background getBackground() {
        return (Background)this.background.get();
    }

    public final ObjectProperty<Background> backgroundProperty() {
        return this.background;
    }

    public final void setBorder(Border border) {
        this.border.set(border);
    }

    public final Border getBorder() {
        return (Border)this.border.get();
    }

    public final ObjectProperty<Border> borderProperty() {
        return this.border;
    }

    void addImageListener(Image image) {
        Toolkit.ImageAccessor imageAccessor = Toolkit.getImageAccessor();
        imageAccessor.getImageProperty(image).addListener(this.imageChangeListener);
    }

    void removeImageListener(Image image) {
        Toolkit.ImageAccessor imageAccessor = Toolkit.getImageAccessor();
        imageAccessor.getImageProperty(image).removeListener(this.imageChangeListener);
    }

    public final ObjectProperty<Insets> opaqueInsetsProperty() {
        if (this.opaqueInsets == null) {
            this.opaqueInsets = new StyleableObjectProperty<Insets>(){

                @Override
                public Object getBean() {
                    return Region.this;
                }

                @Override
                public String getName() {
                    return "opaqueInsets";
                }

                @Override
                public CssMetaData<Region, Insets> getCssMetaData() {
                    return StyleableProperties.OPAQUE_INSETS;
                }

                @Override
                protected void invalidated() {
                    Region.this.impl_markDirty(DirtyBits.SHAPE_FILL);
                }
            };
        }
        return this.opaqueInsets;
    }

    public final void setOpaqueInsets(Insets insets) {
        this.opaqueInsetsProperty().set(insets);
    }

    public final Insets getOpaqueInsets() {
        return this.opaqueInsets == null ? null : (Insets)this.opaqueInsets.get();
    }

    public final Insets getInsets() {
        return this.insets.get();
    }

    public final ReadOnlyObjectProperty<Insets> insetsProperty() {
        return this.insets;
    }

    private void updateSnappedInsets() {
        Insets insets = this.getInsets();
        if (this._snapToPixel) {
            this.snappedTopInset = Math.ceil(insets.getTop());
            this.snappedRightInset = Math.ceil(insets.getRight());
            this.snappedBottomInset = Math.ceil(insets.getBottom());
            this.snappedLeftInset = Math.ceil(insets.getLeft());
        } else {
            this.snappedTopInset = insets.getTop();
            this.snappedRightInset = insets.getRight();
            this.snappedBottomInset = insets.getBottom();
            this.snappedLeftInset = insets.getLeft();
        }
    }

    protected void setWidth(double d2) {
        if (this.width == null) {
            this.widthChanged(d2);
        } else {
            this.width.set(d2);
        }
    }

    private void widthChanged(double d2) {
        if (d2 != this._width) {
            this._width = d2;
            this.cornersValid = false;
            this.boundingBox = null;
            this.impl_layoutBoundsChanged();
            this.impl_geomChanged();
            this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            this.setNeedsLayout(true);
            this.requestParentLayout();
        }
    }

    public final double getWidth() {
        return this.width == null ? this._width : this.width.get();
    }

    public final ReadOnlyDoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new ReadOnlyDoubleWrapper(this._width){

                @Override
                protected void invalidated() {
                    Region.this.widthChanged(this.get());
                }

                @Override
                public Object getBean() {
                    return Region.this;
                }

                @Override
                public String getName() {
                    return "width";
                }
            };
        }
        return this.width.getReadOnlyProperty();
    }

    protected void setHeight(double d2) {
        if (this.height == null) {
            this.heightChanged(d2);
        } else {
            this.height.set(d2);
        }
    }

    private void heightChanged(double d2) {
        if (this._height != d2) {
            this._height = d2;
            this.cornersValid = false;
            this.boundingBox = null;
            this.impl_geomChanged();
            this.impl_layoutBoundsChanged();
            this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            this.setNeedsLayout(true);
            this.requestParentLayout();
        }
    }

    public final double getHeight() {
        return this.height == null ? this._height : this.height.get();
    }

    public final ReadOnlyDoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new ReadOnlyDoubleWrapper(this._height){

                @Override
                protected void invalidated() {
                    Region.this.heightChanged(this.get());
                }

                @Override
                public Object getBean() {
                    return Region.this;
                }

                @Override
                public String getName() {
                    return "height";
                }
            };
        }
        return this.height.getReadOnlyProperty();
    }

    public final void setMinWidth(double d2) {
        if (this.minWidth == null) {
            this._minWidth = d2;
            this.requestParentLayout();
        } else {
            this.minWidth.set(d2);
        }
    }

    public final double getMinWidth() {
        return this.minWidth == null ? this._minWidth : this.minWidth.get();
    }

    public final DoubleProperty minWidthProperty() {
        if (this.minWidth == null) {
            this.minWidth = new MinPrefMaxProperty("minWidth", this._minWidth, StyleableProperties.MIN_WIDTH);
        }
        return this.minWidth;
    }

    public final void setMinHeight(double d2) {
        if (this.minHeight == null) {
            this._minHeight = d2;
            this.requestParentLayout();
        } else {
            this.minHeight.set(d2);
        }
    }

    public final double getMinHeight() {
        return this.minHeight == null ? this._minHeight : this.minHeight.get();
    }

    public final DoubleProperty minHeightProperty() {
        if (this.minHeight == null) {
            this.minHeight = new MinPrefMaxProperty("minHeight", this._minHeight, StyleableProperties.MIN_HEIGHT);
        }
        return this.minHeight;
    }

    public void setMinSize(double d2, double d3) {
        this.setMinWidth(d2);
        this.setMinHeight(d3);
    }

    public final void setPrefWidth(double d2) {
        if (this.prefWidth == null) {
            this._prefWidth = d2;
            this.requestParentLayout();
        } else {
            this.prefWidth.set(d2);
        }
    }

    public final double getPrefWidth() {
        return this.prefWidth == null ? this._prefWidth : this.prefWidth.get();
    }

    public final DoubleProperty prefWidthProperty() {
        if (this.prefWidth == null) {
            this.prefWidth = new MinPrefMaxProperty("prefWidth", this._prefWidth, StyleableProperties.PREF_WIDTH);
        }
        return this.prefWidth;
    }

    public final void setPrefHeight(double d2) {
        if (this.prefHeight == null) {
            this._prefHeight = d2;
            this.requestParentLayout();
        } else {
            this.prefHeight.set(d2);
        }
    }

    public final double getPrefHeight() {
        return this.prefHeight == null ? this._prefHeight : this.prefHeight.get();
    }

    public final DoubleProperty prefHeightProperty() {
        if (this.prefHeight == null) {
            this.prefHeight = new MinPrefMaxProperty("prefHeight", this._prefHeight, StyleableProperties.PREF_HEIGHT);
        }
        return this.prefHeight;
    }

    public void setPrefSize(double d2, double d3) {
        this.setPrefWidth(d2);
        this.setPrefHeight(d3);
    }

    public final void setMaxWidth(double d2) {
        if (this.maxWidth == null) {
            this._maxWidth = d2;
            this.requestParentLayout();
        } else {
            this.maxWidth.set(d2);
        }
    }

    public final double getMaxWidth() {
        return this.maxWidth == null ? this._maxWidth : this.maxWidth.get();
    }

    public final DoubleProperty maxWidthProperty() {
        if (this.maxWidth == null) {
            this.maxWidth = new MinPrefMaxProperty("maxWidth", this._maxWidth, StyleableProperties.MAX_WIDTH);
        }
        return this.maxWidth;
    }

    public final void setMaxHeight(double d2) {
        if (this.maxHeight == null) {
            this._maxHeight = d2;
            this.requestParentLayout();
        } else {
            this.maxHeight.set(d2);
        }
    }

    public final double getMaxHeight() {
        return this.maxHeight == null ? this._maxHeight : this.maxHeight.get();
    }

    public final DoubleProperty maxHeightProperty() {
        if (this.maxHeight == null) {
            this.maxHeight = new MinPrefMaxProperty("maxHeight", this._maxHeight, StyleableProperties.MAX_HEIGHT);
        }
        return this.maxHeight;
    }

    public void setMaxSize(double d2, double d3) {
        this.setMaxWidth(d2);
        this.setMaxHeight(d3);
    }

    public final javafx.scene.shape.Shape getShape() {
        return this.shape == null ? this._shape : (javafx.scene.shape.Shape)this.shape.get();
    }

    public final void setShape(javafx.scene.shape.Shape shape) {
        this.shapeProperty().set(shape);
    }

    public final ObjectProperty<javafx.scene.shape.Shape> shapeProperty() {
        if (this.shape == null) {
            this.shape = new ShapeProperty();
        }
        return this.shape;
    }

    public final void setScaleShape(boolean bl) {
        this.scaleShapeProperty().set(bl);
    }

    public final boolean isScaleShape() {
        return this.scaleShape == null ? true : this.scaleShape.get();
    }

    public final BooleanProperty scaleShapeProperty() {
        if (this.scaleShape == null) {
            this.scaleShape = new StyleableBooleanProperty(true){

                @Override
                public Object getBean() {
                    return Region.this;
                }

                @Override
                public String getName() {
                    return "scaleShape";
                }

                @Override
                public CssMetaData<Region, Boolean> getCssMetaData() {
                    return StyleableProperties.SCALE_SHAPE;
                }

                @Override
                public void invalidated() {
                    Region.this.impl_geomChanged();
                    Region.this.impl_markDirty(DirtyBits.REGION_SHAPE);
                }
            };
        }
        return this.scaleShape;
    }

    public final void setCenterShape(boolean bl) {
        this.centerShapeProperty().set(bl);
    }

    public final boolean isCenterShape() {
        return this.centerShape == null ? true : this.centerShape.get();
    }

    public final BooleanProperty centerShapeProperty() {
        if (this.centerShape == null) {
            this.centerShape = new StyleableBooleanProperty(true){

                @Override
                public Object getBean() {
                    return Region.this;
                }

                @Override
                public String getName() {
                    return "centerShape";
                }

                @Override
                public CssMetaData<Region, Boolean> getCssMetaData() {
                    return StyleableProperties.POSITION_SHAPE;
                }

                @Override
                public void invalidated() {
                    Region.this.impl_geomChanged();
                    Region.this.impl_markDirty(DirtyBits.REGION_SHAPE);
                }
            };
        }
        return this.centerShape;
    }

    public final void setCacheShape(boolean bl) {
        this.cacheShapeProperty().set(bl);
    }

    public final boolean isCacheShape() {
        return this.cacheShape == null ? true : this.cacheShape.get();
    }

    public final BooleanProperty cacheShapeProperty() {
        if (this.cacheShape == null) {
            this.cacheShape = new StyleableBooleanProperty(true){

                @Override
                public Object getBean() {
                    return Region.this;
                }

                @Override
                public String getName() {
                    return "cacheShape";
                }

                @Override
                public CssMetaData<Region, Boolean> getCssMetaData() {
                    return StyleableProperties.CACHE_SHAPE;
                }
            };
        }
        return this.cacheShape;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double d2, double d3) {
        this.setWidth(d2);
        this.setHeight(d3);
        PlatformLogger platformLogger = Logging.getLayoutLogger();
        if (platformLogger.isLoggable(PlatformLogger.Level.FINER)) {
            platformLogger.finer(this.toString() + " resized to " + d2 + " x " + d3);
        }
    }

    @Override
    public final double minWidth(double d2) {
        double d3 = this.getMinWidth();
        if (d3 == -1.0) {
            return super.minWidth(d2);
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefWidth(d2);
        }
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public final double minHeight(double d2) {
        double d3 = this.getMinHeight();
        if (d3 == -1.0) {
            return super.minHeight(d2);
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefHeight(d2);
        }
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public final double prefWidth(double d2) {
        double d3 = this.getPrefWidth();
        if (d3 == -1.0) {
            return super.prefWidth(d2);
        }
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public final double prefHeight(double d2) {
        double d3 = this.getPrefHeight();
        if (d3 == -1.0) {
            return super.prefHeight(d2);
        }
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public final double maxWidth(double d2) {
        double d3 = this.getMaxWidth();
        if (d3 == -1.0) {
            return this.computeMaxWidth(d2);
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefWidth(d2);
        }
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public final double maxHeight(double d2) {
        double d3 = this.getMaxHeight();
        if (d3 == -1.0) {
            return this.computeMaxHeight(d2);
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefHeight(d2);
        }
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    protected double computeMinWidth(double d2) {
        return this.getInsets().getLeft() + this.getInsets().getRight();
    }

    @Override
    protected double computeMinHeight(double d2) {
        return this.getInsets().getTop() + this.getInsets().getBottom();
    }

    @Override
    protected double computePrefWidth(double d2) {
        double d3 = super.computePrefWidth(d2);
        return this.getInsets().getLeft() + d3 + this.getInsets().getRight();
    }

    @Override
    protected double computePrefHeight(double d2) {
        double d3 = super.computePrefHeight(d2);
        return this.getInsets().getTop() + d3 + this.getInsets().getBottom();
    }

    protected double computeMaxWidth(double d2) {
        return Double.MAX_VALUE;
    }

    protected double computeMaxHeight(double d2) {
        return Double.MAX_VALUE;
    }

    protected double snapSpace(double d2) {
        return Region.snapSpace(d2, this.isSnapToPixel());
    }

    protected double snapSize(double d2) {
        return Region.snapSize(d2, this.isSnapToPixel());
    }

    protected double snapPosition(double d2) {
        return Region.snapPosition(d2, this.isSnapToPixel());
    }

    double snapPortion(double d2) {
        return Region.snapPortion(d2, this.isSnapToPixel());
    }

    public final double snappedTopInset() {
        return this.snappedTopInset;
    }

    public final double snappedBottomInset() {
        return this.snappedBottomInset;
    }

    public final double snappedLeftInset() {
        return this.snappedLeftInset;
    }

    public final double snappedRightInset() {
        return this.snappedRightInset;
    }

    double computeChildMinAreaWidth(Node node, Insets insets) {
        return this.computeChildMinAreaWidth(node, -1.0, insets, -1.0, false);
    }

    double computeChildMinAreaWidth(Node node, double d2, Insets insets, double d3, boolean bl) {
        boolean bl2 = this.isSnapToPixel();
        double d4 = insets != null ? Region.snapSpace(insets.getLeft(), bl2) : 0.0;
        double d5 = insets != null ? Region.snapSpace(insets.getRight(), bl2) : 0.0;
        double d6 = -1.0;
        if (d3 != -1.0 && node.isResizable() && node.getContentBias() == Orientation.VERTICAL) {
            double d7 = insets != null ? Region.snapSpace(insets.getTop(), bl2) : 0.0;
            double d8 = insets != null ? Region.snapSpace(insets.getBottom(), bl2) : 0.0;
            double d9 = node.getBaselineOffset();
            double d10 = d9 == Double.NEGATIVE_INFINITY && d2 != -1.0 ? d3 - d7 - d8 - d2 : d3 - d7 - d8;
            d6 = bl ? this.snapSize(Region.boundedSize(node.minHeight(-1.0), d10, node.maxHeight(-1.0))) : this.snapSize(Region.boundedSize(node.minHeight(-1.0), node.prefHeight(-1.0), Math.min(node.maxHeight(-1.0), d10)));
        }
        return d4 + this.snapSize(node.minWidth(d6)) + d5;
    }

    double computeChildMinAreaHeight(Node node, Insets insets) {
        return this.computeChildMinAreaHeight(node, -1.0, insets, -1.0);
    }

    double computeChildMinAreaHeight(Node node, double d2, Insets insets, double d3) {
        double d4;
        boolean bl = this.isSnapToPixel();
        double d5 = insets != null ? Region.snapSpace(insets.getTop(), bl) : 0.0;
        double d6 = insets != null ? Region.snapSpace(insets.getBottom(), bl) : 0.0;
        double d7 = -1.0;
        if (node.isResizable() && node.getContentBias() == Orientation.HORIZONTAL) {
            d4 = insets != null ? Region.snapSpace(insets.getLeft(), bl) : 0.0;
            double d8 = insets != null ? Region.snapSpace(insets.getRight(), bl) : 0.0;
            d7 = this.snapSize(d3 != -1.0 ? Region.boundedSize(node.minWidth(-1.0), d3 - d4 - d8, node.maxWidth(-1.0)) : node.maxWidth(-1.0));
        }
        if (d2 != -1.0) {
            d4 = node.getBaselineOffset();
            if (node.isResizable() && d4 == Double.NEGATIVE_INFINITY) {
                return d5 + this.snapSize(node.minHeight(d7)) + d6 + d2;
            }
            return d4 + d2;
        }
        return d5 + this.snapSize(node.minHeight(d7)) + d6;
    }

    double computeChildPrefAreaWidth(Node node, Insets insets) {
        return this.computeChildPrefAreaWidth(node, -1.0, insets, -1.0, false);
    }

    double computeChildPrefAreaWidth(Node node, double d2, Insets insets, double d3, boolean bl) {
        boolean bl2 = this.isSnapToPixel();
        double d4 = insets != null ? Region.snapSpace(insets.getLeft(), bl2) : 0.0;
        double d5 = insets != null ? Region.snapSpace(insets.getRight(), bl2) : 0.0;
        double d6 = -1.0;
        if (d3 != -1.0 && node.isResizable() && node.getContentBias() == Orientation.VERTICAL) {
            double d7 = insets != null ? Region.snapSpace(insets.getTop(), bl2) : 0.0;
            double d8 = insets != null ? Region.snapSpace(insets.getBottom(), bl2) : 0.0;
            double d9 = node.getBaselineOffset();
            double d10 = d9 == Double.NEGATIVE_INFINITY && d2 != -1.0 ? d3 - d7 - d8 - d2 : d3 - d7 - d8;
            d6 = bl ? this.snapSize(Region.boundedSize(node.minHeight(-1.0), d10, node.maxHeight(-1.0))) : this.snapSize(Region.boundedSize(node.minHeight(-1.0), node.prefHeight(-1.0), Math.min(node.maxHeight(-1.0), d10)));
        }
        return d4 + this.snapSize(Region.boundedSize(node.minWidth(d6), node.prefWidth(d6), node.maxWidth(d6))) + d5;
    }

    double computeChildPrefAreaHeight(Node node, Insets insets) {
        return this.computeChildPrefAreaHeight(node, -1.0, insets, -1.0);
    }

    double computeChildPrefAreaHeight(Node node, double d2, Insets insets, double d3) {
        double d4;
        boolean bl = this.isSnapToPixel();
        double d5 = insets != null ? Region.snapSpace(insets.getTop(), bl) : 0.0;
        double d6 = insets != null ? Region.snapSpace(insets.getBottom(), bl) : 0.0;
        double d7 = -1.0;
        if (node.isResizable() && node.getContentBias() == Orientation.HORIZONTAL) {
            d4 = insets != null ? Region.snapSpace(insets.getLeft(), bl) : 0.0;
            double d8 = insets != null ? Region.snapSpace(insets.getRight(), bl) : 0.0;
            d7 = this.snapSize(Region.boundedSize(node.minWidth(-1.0), d3 != -1.0 ? d3 - d4 - d8 : node.prefWidth(-1.0), node.maxWidth(-1.0)));
        }
        if (d2 != -1.0) {
            d4 = node.getBaselineOffset();
            if (node.isResizable() && d4 == Double.NEGATIVE_INFINITY) {
                return d5 + this.snapSize(Region.boundedSize(node.minHeight(d7), node.prefHeight(d7), node.maxHeight(d7))) + d6 + d2;
            }
            return d5 + d4 + d2 + d6;
        }
        return d5 + this.snapSize(Region.boundedSize(node.minHeight(d7), node.prefHeight(d7), node.maxHeight(d7))) + d6;
    }

    double computeChildMaxAreaWidth(Node node, double d2, Insets insets, double d3, boolean bl) {
        double d4 = node.maxWidth(-1.0);
        if (d4 == Double.MAX_VALUE) {
            return d4;
        }
        boolean bl2 = this.isSnapToPixel();
        double d5 = insets != null ? Region.snapSpace(insets.getLeft(), bl2) : 0.0;
        double d6 = insets != null ? Region.snapSpace(insets.getRight(), bl2) : 0.0;
        double d7 = -1.0;
        if (d3 != -1.0 && node.isResizable() && node.getContentBias() == Orientation.VERTICAL) {
            double d8 = insets != null ? Region.snapSpace(insets.getTop(), bl2) : 0.0;
            double d9 = insets != null ? Region.snapSpace(insets.getBottom(), bl2) : 0.0;
            double d10 = node.getBaselineOffset();
            double d11 = d10 == Double.NEGATIVE_INFINITY && d2 != -1.0 ? d3 - d8 - d9 - d2 : d3 - d8 - d9;
            d7 = bl ? this.snapSize(Region.boundedSize(node.minHeight(-1.0), d11, node.maxHeight(-1.0))) : this.snapSize(Region.boundedSize(node.minHeight(-1.0), node.prefHeight(-1.0), Math.min(node.maxHeight(-1.0), d11)));
            d4 = node.maxWidth(d7);
        }
        return d5 + this.snapSize(Region.boundedSize(node.minWidth(d7), d4, Double.MAX_VALUE)) + d6;
    }

    double computeChildMaxAreaHeight(Node node, double d2, Insets insets, double d3) {
        double d4;
        double d5 = node.maxHeight(-1.0);
        if (d5 == Double.MAX_VALUE) {
            return d5;
        }
        boolean bl = this.isSnapToPixel();
        double d6 = insets != null ? Region.snapSpace(insets.getTop(), bl) : 0.0;
        double d7 = insets != null ? Region.snapSpace(insets.getBottom(), bl) : 0.0;
        double d8 = -1.0;
        if (node.isResizable() && node.getContentBias() == Orientation.HORIZONTAL) {
            d4 = insets != null ? Region.snapSpace(insets.getLeft(), bl) : 0.0;
            double d9 = insets != null ? Region.snapSpace(insets.getRight(), bl) : 0.0;
            d8 = this.snapSize(d3 != -1.0 ? Region.boundedSize(node.minWidth(-1.0), d3 - d4 - d9, node.maxWidth(-1.0)) : node.minWidth(-1.0));
            d5 = node.maxHeight(d8);
        }
        if (d2 != -1.0) {
            d4 = node.getBaselineOffset();
            if (node.isResizable() && d4 == Double.NEGATIVE_INFINITY) {
                return d6 + this.snapSize(Region.boundedSize(node.minHeight(d8), node.maxHeight(d8), Double.MAX_VALUE)) + d7 + d2;
            }
            return d6 + d4 + d2 + d7;
        }
        return d6 + this.snapSize(Region.boundedSize(node.minHeight(d8), d5, Double.MAX_VALUE)) + d7;
    }

    double computeMaxMinAreaWidth(List<Node> list, Callback<Node, Insets> callback) {
        return this.getMaxAreaWidth(list, callback, new double[]{-1.0}, false, true);
    }

    double computeMaxMinAreaWidth(List<Node> list, Callback<Node, Insets> callback, double d2, boolean bl) {
        return this.getMaxAreaWidth(list, callback, new double[]{d2}, bl, true);
    }

    double computeMaxMinAreaWidth(List<Node> list, Callback<Node, Insets> callback, double[] arrd, boolean bl) {
        return this.getMaxAreaWidth(list, callback, arrd, bl, true);
    }

    double computeMaxMinAreaHeight(List<Node> list, Callback<Node, Insets> callback, VPos vPos) {
        return this.getMaxAreaHeight(list, callback, null, vPos, true);
    }

    double computeMaxMinAreaHeight(List<Node> list, Callback<Node, Insets> callback, VPos vPos, double d2) {
        return this.getMaxAreaHeight(list, callback, new double[]{d2}, vPos, true);
    }

    double computeMaxMinAreaHeight(List<Node> list, Callback<Node, Insets> callback, double[] arrd, VPos vPos) {
        return this.getMaxAreaHeight(list, callback, arrd, vPos, true);
    }

    double computeMaxPrefAreaWidth(List<Node> list, Callback<Node, Insets> callback) {
        return this.getMaxAreaWidth(list, callback, new double[]{-1.0}, false, false);
    }

    double computeMaxPrefAreaWidth(List<Node> list, Callback<Node, Insets> callback, double d2, boolean bl) {
        return this.getMaxAreaWidth(list, callback, new double[]{d2}, bl, false);
    }

    double computeMaxPrefAreaWidth(List<Node> list, Callback<Node, Insets> callback, double[] arrd, boolean bl) {
        return this.getMaxAreaWidth(list, callback, arrd, bl, false);
    }

    double computeMaxPrefAreaHeight(List<Node> list, Callback<Node, Insets> callback, VPos vPos) {
        return this.getMaxAreaHeight(list, callback, null, vPos, false);
    }

    double computeMaxPrefAreaHeight(List<Node> list, Callback<Node, Insets> callback, double d2, VPos vPos) {
        return this.getMaxAreaHeight(list, callback, new double[]{d2}, vPos, false);
    }

    double computeMaxPrefAreaHeight(List<Node> list, Callback<Node, Insets> callback, double[] arrd, VPos vPos) {
        return this.getMaxAreaHeight(list, callback, arrd, vPos, false);
    }

    static Vec2d boundedNodeSizeWithBias(Node node, double d2, double d3, boolean bl, boolean bl2, Vec2d vec2d) {
        if (vec2d == null) {
            vec2d = new Vec2d();
        }
        Orientation orientation = node.getContentBias();
        double d4 = 0.0;
        double d5 = 0.0;
        if (orientation == null) {
            d4 = Region.boundedSize(node.minWidth(-1.0), bl ? d2 : Math.min(d2, node.prefWidth(-1.0)), node.maxWidth(-1.0));
            d5 = Region.boundedSize(node.minHeight(-1.0), bl2 ? d3 : Math.min(d3, node.prefHeight(-1.0)), node.maxHeight(-1.0));
        } else if (orientation == Orientation.HORIZONTAL) {
            d4 = Region.boundedSize(node.minWidth(-1.0), bl ? d2 : Math.min(d2, node.prefWidth(-1.0)), node.maxWidth(-1.0));
            d5 = Region.boundedSize(node.minHeight(d4), bl2 ? d3 : Math.min(d3, node.prefHeight(d4)), node.maxHeight(d4));
        } else {
            d5 = Region.boundedSize(node.minHeight(-1.0), bl2 ? d3 : Math.min(d3, node.prefHeight(-1.0)), node.maxHeight(-1.0));
            d4 = Region.boundedSize(node.minWidth(d5), bl ? d2 : Math.min(d2, node.prefWidth(d5)), node.maxWidth(d5));
        }
        vec2d.set(d4, d5);
        return vec2d;
    }

    private double getMaxAreaHeight(List<Node> list, Callback<Node, Insets> callback, double[] arrd, VPos vPos, boolean bl) {
        double d2;
        double d3 = arrd == null ? -1.0 : (d2 = arrd.length == 1 ? arrd[0] : Double.NaN);
        if (vPos == VPos.BASELINE) {
            double d4 = 0.0;
            double d5 = 0.0;
            int n2 = list.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                double d6;
                Node node = list.get(i2);
                double d7 = Double.isNaN(d2) ? arrd[i2] : d2;
                Insets insets = callback.call(node);
                double d8 = insets != null ? this.snapSpace(insets.getTop()) : 0.0;
                double d9 = insets != null ? this.snapSpace(insets.getBottom()) : 0.0;
                double d10 = node.getBaselineOffset();
                double d11 = d6 = bl ? this.snapSize(node.minHeight(d7)) : this.snapSize(node.prefHeight(d7));
                if (d10 == Double.NEGATIVE_INFINITY) {
                    d4 = Math.max(d4, d6 + d8);
                    continue;
                }
                d4 = Math.max(d4, d10 + d8);
                d5 = Math.max(d5, this.snapSpace(bl ? this.snapSize(node.minHeight(d7)) : this.snapSize(node.prefHeight(d7))) - d10 + d9);
            }
            return d4 + d5;
        }
        double d12 = 0.0;
        int n3 = list.size();
        for (int i3 = 0; i3 < n3; ++i3) {
            Node node = list.get(i3);
            Insets insets = callback.call(node);
            double d13 = Double.isNaN(d2) ? arrd[i3] : d2;
            d12 = Math.max(d12, bl ? this.computeChildMinAreaHeight(node, -1.0, insets, d13) : this.computeChildPrefAreaHeight(node, -1.0, insets, d13));
        }
        return d12;
    }

    private double getMaxAreaWidth(List<Node> list, Callback<Node, Insets> callback, double[] arrd, boolean bl, boolean bl2) {
        double d2 = arrd == null ? -1.0 : (arrd.length == 1 ? arrd[0] : Double.NaN);
        double d3 = 0.0;
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = list.get(i2);
            Insets insets = callback.call(node);
            double d4 = Double.isNaN(d2) ? arrd[i2] : d2;
            d3 = Math.max(d3, bl2 ? this.computeChildMinAreaWidth(list.get(i2), -1.0, insets, d4, bl) : this.computeChildPrefAreaWidth(node, -1.0, insets, d4, bl));
        }
        return d3;
    }

    protected void positionInArea(Node node, double d2, double d3, double d4, double d5, double d6, HPos hPos, VPos vPos) {
        Region.positionInArea(node, d2, d3, d4, d5, d6, Insets.EMPTY, hPos, vPos, this.isSnapToPixel());
    }

    public static void positionInArea(Node node, double d2, double d3, double d4, double d5, double d6, Insets insets, HPos hPos, VPos vPos, boolean bl) {
        Insets insets2 = insets != null ? insets : Insets.EMPTY;
        Region.position(node, d2, d3, d4, d5, d6, Region.snapSpace(insets2.getTop(), bl), Region.snapSpace(insets2.getRight(), bl), Region.snapSpace(insets2.getBottom(), bl), Region.snapSpace(insets2.getLeft(), bl), hPos, vPos, bl);
    }

    protected void layoutInArea(Node node, double d2, double d3, double d4, double d5, double d6, HPos hPos, VPos vPos) {
        this.layoutInArea(node, d2, d3, d4, d5, d6, Insets.EMPTY, hPos, vPos);
    }

    protected void layoutInArea(Node node, double d2, double d3, double d4, double d5, double d6, Insets insets, HPos hPos, VPos vPos) {
        this.layoutInArea(node, d2, d3, d4, d5, d6, insets, true, true, hPos, vPos);
    }

    protected void layoutInArea(Node node, double d2, double d3, double d4, double d5, double d6, Insets insets, boolean bl, boolean bl2, HPos hPos, VPos vPos) {
        Region.layoutInArea(node, d2, d3, d4, d5, d6, insets, bl, bl2, hPos, vPos, this.isSnapToPixel());
    }

    public static void layoutInArea(Node node, double d2, double d3, double d4, double d5, double d6, Insets insets, boolean bl, boolean bl2, HPos hPos, VPos vPos, boolean bl3) {
        Insets insets2 = insets != null ? insets : Insets.EMPTY;
        double d7 = Region.snapSpace(insets2.getTop(), bl3);
        double d8 = Region.snapSpace(insets2.getBottom(), bl3);
        double d9 = Region.snapSpace(insets2.getLeft(), bl3);
        double d10 = Region.snapSpace(insets2.getRight(), bl3);
        if (vPos == VPos.BASELINE) {
            double d11 = node.getBaselineOffset();
            if (d11 == Double.NEGATIVE_INFINITY) {
                if (node.isResizable()) {
                    d8 += Region.snapSpace(d5 - d6, bl3);
                } else {
                    d7 = Region.snapSpace(d6 - node.getLayoutBounds().getHeight(), bl3);
                }
            } else {
                d7 = Region.snapSpace(d6 - d11, bl3);
            }
        }
        if (node.isResizable()) {
            Vec2d vec2d = Region.boundedNodeSizeWithBias(node, d4 - d9 - d10, d5 - d7 - d8, bl, bl2, TEMP_VEC2D);
            node.resize(Region.snapSize(vec2d.x, bl3), Region.snapSize(vec2d.y, bl3));
        }
        Region.position(node, d2, d3, d4, d5, d6, d7, d10, d8, d9, hPos, vPos, bl3);
    }

    private static void position(Node node, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, HPos hPos, VPos vPos, boolean bl) {
        double d11;
        double d12 = d10 + Region.computeXOffset(d4 - d10 - d8, node.getLayoutBounds().getWidth(), hPos);
        double d13 = vPos == VPos.BASELINE ? ((d11 = node.getBaselineOffset()) == Double.NEGATIVE_INFINITY ? d6 - node.getLayoutBounds().getHeight() : d6 - d11) : d7 + Region.computeYOffset(d5 - d7 - d9, node.getLayoutBounds().getHeight(), vPos);
        d11 = Region.snapPosition(d2 + d12, bl);
        double d14 = Region.snapPosition(d3 + d13, bl);
        node.relocate(d11, d14);
    }

    @Override
    public void impl_updatePeer() {
        boolean bl;
        boolean bl2;
        super.impl_updatePeer();
        if (this._shape != null) {
            this._shape.impl_syncPeer();
        }
        NGRegion nGRegion = (NGRegion)this.impl_getPeer();
        if (!this.cornersValid) {
            this.validateCorners();
        }
        if (bl2 = this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            nGRegion.setSize((float)this.getWidth(), (float)this.getHeight());
        }
        if (bl = this.impl_isDirty(DirtyBits.REGION_SHAPE)) {
            nGRegion.updateShape(this._shape, this.isScaleShape(), this.isCenterShape(), this.isCacheShape());
        }
        nGRegion.updateFillCorners(this.normalizedFillCorners);
        boolean bl3 = this.impl_isDirty(DirtyBits.SHAPE_FILL);
        Background background = this.getBackground();
        if (bl3) {
            nGRegion.updateBackground(background);
        }
        if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            nGRegion.imagesUpdated();
        }
        nGRegion.updateStrokeCorners(this.normalizedStrokeCorners);
        if (this.impl_isDirty(DirtyBits.SHAPE_STROKE)) {
            nGRegion.updateBorder(this.getBorder());
        }
        if (bl2 || bl3 || bl) {
            Insets insets = this.getOpaqueInsets();
            if (this._shape != null) {
                if (insets != null) {
                    nGRegion.setOpaqueInsets((float)insets.getTop(), (float)insets.getRight(), (float)insets.getBottom(), (float)insets.getLeft());
                } else {
                    nGRegion.setOpaqueInsets(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
                }
            } else if (background == null || background.isEmpty()) {
                nGRegion.setOpaqueInsets(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
            } else {
                double[] arrd = new double[4];
                background.computeOpaqueInsets(this.getWidth(), this.getHeight(), arrd);
                if (insets != null) {
                    double d2 = Double.isNaN(arrd[0]) ? insets.getTop() : (arrd[0] = Double.isNaN(insets.getTop()) ? arrd[0] : Math.min(arrd[0], insets.getTop()));
                    double d3 = Double.isNaN(arrd[1]) ? insets.getRight() : (arrd[1] = Double.isNaN(insets.getRight()) ? arrd[1] : Math.min(arrd[1], insets.getRight()));
                    double d4 = Double.isNaN(arrd[2]) ? insets.getBottom() : (arrd[2] = Double.isNaN(insets.getBottom()) ? arrd[2] : Math.min(arrd[2], insets.getBottom()));
                    arrd[3] = Double.isNaN(arrd[3]) ? insets.getLeft() : (Double.isNaN(insets.getLeft()) ? arrd[3] : Math.min(arrd[3], insets.getLeft()));
                }
                nGRegion.setOpaqueInsets((float)arrd[0], (float)arrd[1], (float)arrd[2], (float)arrd[3]);
            }
        }
    }

    @Override
    public NGNode impl_createPeer() {
        return new NGRegion();
    }

    private boolean shapeContains(Shape shape, double d2, double d3, double d4, double d5, double d6, double d7) {
        double d8 = d2;
        double d9 = d3;
        RectBounds rectBounds = shape.getBounds();
        if (this.isScaleShape()) {
            d8 -= d7;
            d9 -= d4;
            d8 *= (double)rectBounds.getWidth() / (this.getWidth() - d7 - d5);
            d9 *= (double)rectBounds.getHeight() / (this.getHeight() - d4 - d6);
            if (this.isCenterShape()) {
                d8 += (double)rectBounds.getMinX();
                d9 += (double)rectBounds.getMinY();
            }
        } else if (this.isCenterShape()) {
            double d10 = rectBounds.getWidth();
            double d11 = rectBounds.getHeight();
            double d12 = d10 / (d10 - d7 - d5);
            double d13 = d11 / (d11 - d4 - d6);
            d8 = d12 * (d8 - (d7 + (this.getWidth() - d10) / 2.0)) + (double)rectBounds.getMinX();
            d9 = d13 * (d9 - (d4 + (this.getHeight() - d11) / 2.0)) + (double)rectBounds.getMinY();
        } else if (d4 != 0.0 || d5 != 0.0 || d6 != 0.0 || d7 != 0.0) {
            double d14 = (double)rectBounds.getWidth() / ((double)rectBounds.getWidth() - d7 - d5);
            double d15 = (double)rectBounds.getHeight() / ((double)rectBounds.getHeight() - d4 - d6);
            d8 = d14 * (d8 - d7 - (double)rectBounds.getMinX()) + (double)rectBounds.getMinX();
            d9 = d15 * (d9 - d4 - (double)rectBounds.getMinY()) + (double)rectBounds.getMinY();
        }
        return shape.contains((float)d8, (float)d9);
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        int n2;
        Object object;
        double d4 = this.getWidth();
        double d5 = this.getHeight();
        Background background = this.getBackground();
        if (this._shape != null) {
            if (background != null && !background.getFills().isEmpty()) {
                List<BackgroundFill> list = background.getFills();
                double d6 = Double.MAX_VALUE;
                double d7 = Double.MAX_VALUE;
                double d8 = Double.MAX_VALUE;
                double d9 = Double.MAX_VALUE;
                int n3 = list.size();
                for (int i2 = 0; i2 < n3; ++i2) {
                    BackgroundFill backgroundFill = list.get(0);
                    d6 = Math.min(d6, backgroundFill.getInsets().getTop());
                    d7 = Math.min(d7, backgroundFill.getInsets().getLeft());
                    d8 = Math.min(d8, backgroundFill.getInsets().getBottom());
                    d9 = Math.min(d9, backgroundFill.getInsets().getRight());
                }
                return this.shapeContains(this._shape.impl_configShape(), d2, d3, d6, d7, d8, d9);
            }
            return false;
        }
        if (background != null) {
            object = background.getFills();
            n2 = object.size();
            for (int i3 = 0; i3 < n2; ++i3) {
                BackgroundFill backgroundFill = (BackgroundFill)object.get(i3);
                if (!this.contains(d2, d3, 0.0, 0.0, d4, d5, backgroundFill.getInsets(), this.getNormalizedFillCorner(i3))) continue;
                return true;
            }
        }
        if ((object = this.getBorder()) != null) {
            List<BorderStroke> list = ((Border)object).getStrokes();
            int n4 = list.size();
            for (n2 = 0; n2 < n4; ++n2) {
                BorderStroke borderStroke = list.get(n2);
                if (!this.contains(d2, d3, 0.0, 0.0, d4, d5, borderStroke.getWidths(), false, borderStroke.getInsets(), this.getNormalizedStrokeCorner(n2))) continue;
                return true;
            }
            List<BorderImage> list2 = ((Border)object).getImages();
            int n5 = list2.size();
            for (n4 = 0; n4 < n5; ++n4) {
                BorderImage borderImage = list2.get(n4);
                if (!this.contains(d2, d3, 0.0, 0.0, d4, d5, borderImage.getWidths(), borderImage.isFilled(), borderImage.getInsets(), CornerRadii.EMPTY)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean contains(double d2, double d3, double d4, double d5, double d6, double d7, BorderWidths borderWidths, boolean bl, Insets insets, CornerRadii cornerRadii) {
        if (bl) {
            if (this.contains(d2, d3, d4, d5, d6, d7, insets, cornerRadii)) {
                return true;
            }
        } else {
            boolean bl2 = this.contains(d2, d3, d4, d5, d6, d7, insets, cornerRadii);
            if (bl2) {
                boolean bl3;
                boolean bl4 = bl3 = !this.contains(d2, d3, d4 + (borderWidths.isLeftAsPercentage() ? this.getWidth() * borderWidths.getLeft() : borderWidths.getLeft()), d5 + (borderWidths.isTopAsPercentage() ? this.getHeight() * borderWidths.getTop() : borderWidths.getTop()), d6 - (borderWidths.isRightAsPercentage() ? this.getWidth() * borderWidths.getRight() : borderWidths.getRight()), d7 - (borderWidths.isBottomAsPercentage() ? this.getHeight() * borderWidths.getBottom() : borderWidths.getBottom()), insets, cornerRadii);
                if (bl3) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean contains(double d2, double d3, double d4, double d5, double d6, double d7, Insets insets, CornerRadii cornerRadii) {
        double d8 = d4 + insets.getLeft();
        double d9 = d5 + insets.getTop();
        double d10 = d6 - insets.getRight();
        double d11 = d7 - insets.getBottom();
        if (d2 >= d8 && d3 >= d9 && d2 <= d10 && d3 <= d11) {
            double d12;
            double d13;
            double d14;
            double d15;
            double d16 = cornerRadii.getTopLeftHorizontalRadius();
            if (cornerRadii.isUniform() && d16 == 0.0) {
                return true;
            }
            double d17 = cornerRadii.getTopLeftVerticalRadius();
            double d18 = cornerRadii.getTopRightHorizontalRadius();
            double d19 = cornerRadii.getTopRightVerticalRadius();
            double d20 = cornerRadii.getBottomLeftHorizontalRadius();
            double d21 = cornerRadii.getBottomLeftVerticalRadius();
            double d22 = cornerRadii.getBottomRightHorizontalRadius();
            double d23 = cornerRadii.getBottomRightVerticalRadius();
            if (d2 <= d8 + d16 && d3 <= d9 + d17) {
                d15 = d8 + d16;
                d14 = d9 + d17;
                d13 = d16;
                d12 = d17;
            } else if (d2 >= d10 - d18 && d3 <= d9 + d19) {
                d15 = d10 - d18;
                d14 = d9 + d19;
                d13 = d18;
                d12 = d19;
            } else if (d2 >= d10 - d22 && d3 >= d11 - d23) {
                d15 = d10 - d22;
                d14 = d11 - d23;
                d13 = d22;
                d12 = d23;
            } else if (d2 <= d8 + d20 && d3 >= d11 - d21) {
                d15 = d8 + d20;
                d14 = d11 - d21;
                d13 = d20;
                d12 = d21;
            } else {
                return true;
            }
            double d24 = d2 - d15;
            double d25 = d3 - d14;
            double d26 = d24 * d24 / (d13 * d13) + d25 * d25 / (d12 * d12);
            if (d26 - 1.0E-7 <= 1.0) {
                return true;
            }
        }
        return false;
    }

    private CornerRadii getNormalizedFillCorner(int n2) {
        if (!this.cornersValid) {
            this.validateCorners();
        }
        return this.normalizedFillCorners == null ? this.getBackground().getFills().get(n2).getRadii() : this.normalizedFillCorners.get(n2);
    }

    private CornerRadii getNormalizedStrokeCorner(int n2) {
        if (!this.cornersValid) {
            this.validateCorners();
        }
        return this.normalizedStrokeCorners == null ? this.getBorder().getStrokes().get(n2).getRadii() : this.normalizedStrokeCorners.get(n2);
    }

    private void validateCorners() {
        int n2;
        Object object;
        CornerRadii cornerRadii;
        Object object2;
        double d2 = this.getWidth();
        double d3 = this.getHeight();
        List<Object> list = null;
        List<Object> list2 = null;
        Background background = this.getBackground();
        List<BackgroundFill> list3 = background == null ? Collections.EMPTY_LIST : background.getFills();
        for (int i2 = 0; i2 < list3.size(); ++i2) {
            object2 = list3.get(i2);
            CornerRadii cornerRadii2 = ((BackgroundFill)object2).getRadii();
            if (cornerRadii2 == (cornerRadii = Region.normalize(cornerRadii2, (Insets)(object = ((BackgroundFill)object2).getInsets()), d2, d3))) continue;
            if (list == null) {
                list = Arrays.asList(new CornerRadii[list3.size()]);
            }
            list.set(i2, cornerRadii);
        }
        Border border = this.getBorder();
        object2 = border == null ? Collections.EMPTY_LIST : border.getStrokes();
        for (n2 = 0; n2 < object2.size(); ++n2) {
            Insets insets;
            CornerRadii cornerRadii3;
            object = (BorderStroke)object2.get(n2);
            cornerRadii = ((BorderStroke)object).getRadii();
            if (cornerRadii == (cornerRadii3 = Region.normalize(cornerRadii, insets = ((BorderStroke)object).getInsets(), d2, d3))) continue;
            if (list2 == null) {
                list2 = Arrays.asList(new CornerRadii[object2.size()]);
            }
            list2.set(n2, cornerRadii3);
        }
        if (list != null) {
            for (n2 = 0; n2 < list3.size(); ++n2) {
                if (list.get(n2) != null) continue;
                list.set(n2, list3.get(n2).getRadii());
            }
            list = Collections.unmodifiableList(list);
        }
        if (list2 != null) {
            for (n2 = 0; n2 < object2.size(); ++n2) {
                if (list2.get(n2) != null) continue;
                list2.set(n2, ((BorderStroke)object2.get(n2)).getRadii());
            }
            list2 = Collections.unmodifiableList(list2);
        }
        this.normalizedFillCorners = list;
        this.normalizedStrokeCorners = list2;
        this.cornersValid = true;
    }

    private static CornerRadii normalize(CornerRadii cornerRadii, Insets insets, double d2, double d3) {
        d3 -= insets.getTop() + insets.getBottom();
        if ((d2 -= insets.getLeft() + insets.getRight()) <= 0.0 || d3 <= 0.0) {
            return CornerRadii.EMPTY;
        }
        double d4 = cornerRadii.getTopLeftVerticalRadius();
        double d5 = cornerRadii.getTopLeftHorizontalRadius();
        double d6 = cornerRadii.getTopRightVerticalRadius();
        double d7 = cornerRadii.getTopRightHorizontalRadius();
        double d8 = cornerRadii.getBottomRightVerticalRadius();
        double d9 = cornerRadii.getBottomRightHorizontalRadius();
        double d10 = cornerRadii.getBottomLeftVerticalRadius();
        double d11 = cornerRadii.getBottomLeftHorizontalRadius();
        if (cornerRadii.hasPercentBasedRadii) {
            if (cornerRadii.isTopLeftVerticalRadiusAsPercentage()) {
                d4 *= d3;
            }
            if (cornerRadii.isTopLeftHorizontalRadiusAsPercentage()) {
                d5 *= d2;
            }
            if (cornerRadii.isTopRightVerticalRadiusAsPercentage()) {
                d6 *= d3;
            }
            if (cornerRadii.isTopRightHorizontalRadiusAsPercentage()) {
                d7 *= d2;
            }
            if (cornerRadii.isBottomRightVerticalRadiusAsPercentage()) {
                d8 *= d3;
            }
            if (cornerRadii.isBottomRightHorizontalRadiusAsPercentage()) {
                d9 *= d2;
            }
            if (cornerRadii.isBottomLeftVerticalRadiusAsPercentage()) {
                d10 *= d3;
            }
            if (cornerRadii.isBottomLeftHorizontalRadiusAsPercentage()) {
                d11 *= d2;
            }
        }
        double d12 = 1.0;
        if (d5 + d7 > d2) {
            d12 = Math.min(d12, d2 / (d5 + d7));
        }
        if (d11 + d9 > d2) {
            d12 = Math.min(d12, d2 / (d11 + d9));
        }
        if (d4 + d10 > d3) {
            d12 = Math.min(d12, d3 / (d4 + d10));
        }
        if (d6 + d8 > d3) {
            d12 = Math.min(d12, d3 / (d6 + d8));
        }
        if (d12 < 1.0) {
            d4 *= d12;
            d5 *= d12;
            d6 *= d12;
            d7 *= d12;
            d8 *= d12;
            d9 *= d12;
            d10 *= d12;
            d11 *= d12;
        }
        if (cornerRadii.hasPercentBasedRadii || d12 < 1.0) {
            return new CornerRadii(d5, d4, d6, d7, d9, d8, d10, d11, false, false, false, false, false, false, false, false);
        }
        return cornerRadii;
    }

    @Override
    @Deprecated
    protected void impl_pickNodeLocal(PickRay pickRay, PickResultChooser pickResultChooser) {
        double d2 = this.impl_intersectsBounds(pickRay);
        if (!Double.isNaN(d2)) {
            ObservableList<Node> observableList = this.getChildren();
            for (int i2 = observableList.size() - 1; i2 >= 0; --i2) {
                ((Node)observableList.get(i2)).impl_pickNode(pickRay, pickResultChooser);
                if (!pickResultChooser.isClosed()) continue;
                return;
            }
            this.impl_intersects(pickRay, pickResultChooser);
        }
    }

    @Override
    @Deprecated
    protected final Bounds impl_computeLayoutBounds() {
        if (this.boundingBox == null) {
            this.boundingBox = new BoundingBox(0.0, 0.0, 0.0, this.getWidth(), this.getHeight(), 0.0);
        }
        return this.boundingBox;
    }

    @Override
    @Deprecated
    protected final void impl_notifyLayoutBoundsChanged() {
    }

    private BaseBounds computeShapeBounds(BaseBounds baseBounds) {
        Object object;
        Shape shape = this._shape.impl_configShape();
        float[] arrf = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
        Background background = this.getBackground();
        if (background != null) {
            object = shape.getBounds();
            Insets insets = background.getOutsets();
            arrf[0] = ((RectBounds)object).getMinX() - (float)insets.getLeft();
            arrf[1] = ((RectBounds)object).getMinY() - (float)insets.getTop();
            arrf[2] = ((RectBounds)object).getMaxX() + (float)insets.getBottom();
            arrf[3] = ((RectBounds)object).getMaxY() + (float)insets.getRight();
        }
        if ((object = this.getBorder()) != null && ((Border)object).getStrokes().size() > 0) {
            for (BorderStroke borderStroke : ((Border)object).getStrokes()) {
                BorderStrokeStyle borderStrokeStyle = borderStroke.getTopStyle() != null ? borderStroke.getTopStyle() : (borderStroke.getLeftStyle() != null ? borderStroke.getLeftStyle() : (borderStroke.getBottomStyle() != null ? borderStroke.getBottomStyle() : (borderStroke.getRightStyle() != null ? borderStroke.getRightStyle() : null)));
                if (borderStrokeStyle == null || borderStrokeStyle == BorderStrokeStyle.NONE) continue;
                StrokeType strokeType = borderStrokeStyle.getType();
                double d2 = Math.max(borderStroke.getWidths().top, 0.0);
                StrokeLineCap strokeLineCap = borderStrokeStyle.getLineCap();
                StrokeLineJoin strokeLineJoin = borderStrokeStyle.getLineJoin();
                float f2 = (float)Math.max(borderStrokeStyle.getMiterLimit(), 1.0);
                Toolkit.getToolkit().accumulateStrokeBounds(shape, arrf, strokeType, d2, strokeLineCap, strokeLineJoin, f2, BaseTransform.IDENTITY_TRANSFORM);
            }
        }
        if (arrf[2] < arrf[0] || arrf[3] < arrf[1]) {
            return baseBounds.makeEmpty();
        }
        return baseBounds.deriveWithNewBounds(arrf[0], arrf[1], 0.0f, arrf[2], arrf[3], 0.0f);
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        Object object;
        Object object2;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = this.getWidth();
        double d5 = this.getHeight();
        if (this._shape != null && !this.isScaleShape()) {
            object2 = this.computeShapeBounds(baseBounds);
            double d6 = ((BaseBounds)object2).getWidth();
            double d7 = ((BaseBounds)object2).getHeight();
            if (this.isCenterShape()) {
                d2 = (d4 - d6) / 2.0;
                d3 = (d5 - d7) / 2.0;
                d4 = d2 + d6;
                d5 = d3 + d7;
            } else {
                d2 = ((BaseBounds)object2).getMinX();
                d3 = ((BaseBounds)object2).getMinY();
                d4 = ((BaseBounds)object2).getMaxX();
                d5 = ((BaseBounds)object2).getMaxY();
            }
        } else {
            object2 = this.getBackground();
            Border border = this.getBorder();
            object = object2 == null ? Insets.EMPTY : ((Background)object2).getOutsets();
            Insets insets = border == null ? Insets.EMPTY : border.getOutsets();
            d2 -= Math.max(((Insets)object).getLeft(), insets.getLeft());
            d3 -= Math.max(((Insets)object).getTop(), insets.getTop());
            d4 += Math.max(((Insets)object).getRight(), insets.getRight());
            d5 += Math.max(((Insets)object).getBottom(), insets.getBottom());
        }
        object2 = super.impl_computeGeomBounds(baseBounds, baseTransform);
        if (((BaseBounds)object2).isEmpty()) {
            baseBounds = baseBounds.deriveWithNewBounds((float)d2, (float)d3, 0.0f, (float)d4, (float)d5, 0.0f);
            baseBounds = baseTransform.transform(baseBounds, baseBounds);
            return baseBounds;
        }
        BaseBounds baseBounds2 = TempState.getInstance().bounds;
        baseBounds2 = baseBounds2.deriveWithNewBounds((float)d2, (float)d3, 0.0f, (float)d4, (float)d5, 0.0f);
        object = baseTransform.transform(baseBounds2, baseBounds2);
        object2 = ((BaseBounds)object2).deriveWithUnion((BaseBounds)object);
        return object2;
    }

    public String getUserAgentStylesheet() {
        return null;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return Region.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<Region, Insets> PADDING = new CssMetaData<Region, Insets>("-fx-padding", InsetsConverter.getInstance(), Insets.EMPTY){

            @Override
            public boolean isSettable(Region region) {
                return region.padding == null || !region.padding.isBound();
            }

            @Override
            public StyleableProperty<Insets> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.paddingProperty());
            }
        };
        private static final CssMetaData<Region, Insets> OPAQUE_INSETS = new CssMetaData<Region, Insets>("-fx-opaque-insets", InsetsConverter.getInstance(), null){

            @Override
            public boolean isSettable(Region region) {
                return region.opaqueInsets == null || !region.opaqueInsets.isBound();
            }

            @Override
            public StyleableProperty<Insets> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.opaqueInsetsProperty());
            }
        };
        private static final CssMetaData<Region, Background> BACKGROUND = new CssMetaData<Region, Background>("-fx-region-background", BackgroundConverter.INSTANCE, null, false, Background.getClassCssMetaData()){

            @Override
            public boolean isSettable(Region region) {
                return !region.background.isBound();
            }

            @Override
            public StyleableProperty<Background> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.background);
            }
        };
        private static final CssMetaData<Region, Border> BORDER = new CssMetaData<Region, Border>("-fx-region-border", (StyleConverter)BorderConverter.getInstance(), null, false, Border.getClassCssMetaData()){

            @Override
            public boolean isSettable(Region region) {
                return !region.border.isBound();
            }

            @Override
            public StyleableProperty<Border> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.border);
            }
        };
        private static final CssMetaData<Region, javafx.scene.shape.Shape> SHAPE = new CssMetaData<Region, javafx.scene.shape.Shape>("-fx-shape", ShapeConverter.getInstance()){

            @Override
            public boolean isSettable(Region region) {
                return region.shape == null || !region.shape.isBound();
            }

            @Override
            public StyleableProperty<javafx.scene.shape.Shape> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.shapeProperty());
            }
        };
        private static final CssMetaData<Region, Boolean> SCALE_SHAPE = new CssMetaData<Region, Boolean>("-fx-scale-shape", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(Region region) {
                return region.scaleShape == null || !region.scaleShape.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.scaleShapeProperty());
            }
        };
        private static final CssMetaData<Region, Boolean> POSITION_SHAPE = new CssMetaData<Region, Boolean>("-fx-position-shape", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(Region region) {
                return region.centerShape == null || !region.centerShape.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.centerShapeProperty());
            }
        };
        private static final CssMetaData<Region, Boolean> CACHE_SHAPE = new CssMetaData<Region, Boolean>("-fx-cache-shape", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(Region region) {
                return region.cacheShape == null || !region.cacheShape.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.cacheShapeProperty());
            }
        };
        private static final CssMetaData<Region, Boolean> SNAP_TO_PIXEL = new CssMetaData<Region, Boolean>("-fx-snap-to-pixel", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(Region region) {
                return region.snapToPixel == null || !region.snapToPixel.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.snapToPixelProperty());
            }
        };
        private static final CssMetaData<Region, Number> MIN_HEIGHT = new CssMetaData<Region, Number>("-fx-min-height", SizeConverter.getInstance(), (Number)-1.0){

            @Override
            public boolean isSettable(Region region) {
                return region.minHeight == null || !region.minHeight.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.minHeightProperty());
            }
        };
        private static final CssMetaData<Region, Number> PREF_HEIGHT = new CssMetaData<Region, Number>("-fx-pref-height", SizeConverter.getInstance(), (Number)-1.0){

            @Override
            public boolean isSettable(Region region) {
                return region.prefHeight == null || !region.prefHeight.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.prefHeightProperty());
            }
        };
        private static final CssMetaData<Region, Number> MAX_HEIGHT = new CssMetaData<Region, Number>("-fx-max-height", SizeConverter.getInstance(), (Number)-1.0){

            @Override
            public boolean isSettable(Region region) {
                return region.maxHeight == null || !region.maxHeight.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.maxHeightProperty());
            }
        };
        private static final CssMetaData<Region, Number> MIN_WIDTH = new CssMetaData<Region, Number>("-fx-min-width", SizeConverter.getInstance(), (Number)-1.0){

            @Override
            public boolean isSettable(Region region) {
                return region.minWidth == null || !region.minWidth.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.minWidthProperty());
            }
        };
        private static final CssMetaData<Region, Number> PREF_WIDTH = new CssMetaData<Region, Number>("-fx-pref-width", SizeConverter.getInstance(), (Number)-1.0){

            @Override
            public boolean isSettable(Region region) {
                return region.prefWidth == null || !region.prefWidth.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.prefWidthProperty());
            }
        };
        private static final CssMetaData<Region, Number> MAX_WIDTH = new CssMetaData<Region, Number>("-fx-max-width", SizeConverter.getInstance(), (Number)-1.0){

            @Override
            public boolean isSettable(Region region) {
                return region.maxWidth == null || !region.maxWidth.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Region region) {
                return (StyleableProperty)((Object)region.maxWidthProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Parent.getClassCssMetaData());
            arrayList.add(PADDING);
            arrayList.add(BACKGROUND);
            arrayList.add(BORDER);
            arrayList.add(OPAQUE_INSETS);
            arrayList.add(SHAPE);
            arrayList.add(SCALE_SHAPE);
            arrayList.add(POSITION_SHAPE);
            arrayList.add(SNAP_TO_PIXEL);
            arrayList.add(MIN_WIDTH);
            arrayList.add(PREF_WIDTH);
            arrayList.add(MAX_WIDTH);
            arrayList.add(MIN_HEIGHT);
            arrayList.add(PREF_HEIGHT);
            arrayList.add(MAX_HEIGHT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    private final class ShapeProperty
    extends StyleableObjectProperty<javafx.scene.shape.Shape>
    implements Runnable {
        private ShapeProperty() {
        }

        @Override
        public Object getBean() {
            return Region.this;
        }

        @Override
        public String getName() {
            return "shape";
        }

        @Override
        public CssMetaData<Region, javafx.scene.shape.Shape> getCssMetaData() {
            return StyleableProperties.SHAPE;
        }

        @Override
        protected void invalidated() {
            javafx.scene.shape.Shape shape = (javafx.scene.shape.Shape)this.get();
            if (Region.this._shape != shape) {
                if (Region.this._shape != null) {
                    Region.this._shape.impl_setShapeChangeListener(null);
                }
                if (shape != null) {
                    shape.impl_setShapeChangeListener(this);
                }
                this.run();
                if (Region.this._shape == null || shape == null) {
                    Region.this.insets.fireValueChanged();
                }
                Region.this._shape = shape;
            }
        }

        @Override
        public void run() {
            Region.this.impl_geomChanged();
            Region.this.impl_markDirty(DirtyBits.REGION_SHAPE);
        }
    }

    private final class MinPrefMaxProperty
    extends StyleableDoubleProperty {
        private final String name;
        private final CssMetaData<? extends Styleable, Number> cssMetaData;

        MinPrefMaxProperty(String string, double d2, CssMetaData<? extends Styleable, Number> cssMetaData) {
            super(d2);
            this.name = string;
            this.cssMetaData = cssMetaData;
        }

        @Override
        public void invalidated() {
            Region.this.requestParentLayout();
        }

        @Override
        public Object getBean() {
            return Region.this;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public CssMetaData<? extends Styleable, Number> getCssMetaData() {
            return this.cssMetaData;
        }
    }

    private final class InsetsProperty
    extends ReadOnlyObjectProperty<Insets> {
        private Insets cache = null;
        private ExpressionHelper<Insets> helper = null;

        private InsetsProperty() {
        }

        @Override
        public Object getBean() {
            return Region.this;
        }

        @Override
        public String getName() {
            return "insets";
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, invalidationListener);
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            this.helper = ExpressionHelper.removeListener(this.helper, invalidationListener);
        }

        @Override
        public void addListener(ChangeListener<? super Insets> changeListener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
        }

        @Override
        public void removeListener(ChangeListener<? super Insets> changeListener) {
            this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
        }

        void fireValueChanged() {
            this.cache = null;
            Region.this.updateSnappedInsets();
            Region.this.requestLayout();
            ExpressionHelper.fireValueChangedEvent(this.helper);
        }

        @Override
        public Insets get() {
            if (Region.this._shape != null) {
                return Region.this.getPadding();
            }
            Border border = Region.this.getBorder();
            if (border == null || Insets.EMPTY.equals(border.getInsets())) {
                return Region.this.getPadding();
            }
            if (this.cache == null) {
                Insets insets = border.getInsets();
                Insets insets2 = Region.this.getPadding();
                this.cache = new Insets(insets.getTop() + insets2.getTop(), insets.getRight() + insets2.getRight(), insets.getBottom() + insets2.getBottom(), insets.getLeft() + insets2.getLeft());
            }
            return this.cache;
        }
    }
}

