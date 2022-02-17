/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class TilePane
extends Pane {
    private static final String MARGIN_CONSTRAINT = "tilepane-margin";
    private static final String ALIGNMENT_CONSTRAINT = "tilepane-alignment";
    private static final Callback<Node, Insets> marginAccessor = node -> TilePane.getMargin(node);
    private double _tileWidth = -1.0;
    private double _tileHeight = -1.0;
    private ObjectProperty<Orientation> orientation;
    private IntegerProperty prefRows;
    private IntegerProperty prefColumns;
    private DoubleProperty prefTileWidth;
    private DoubleProperty prefTileHeight;
    private TileSizeProperty tileWidth;
    private TileSizeProperty tileHeight;
    private DoubleProperty hgap;
    private DoubleProperty vgap;
    private ObjectProperty<Pos> alignment;
    private ObjectProperty<Pos> tileAlignment;
    private int actualRows = 0;
    private int actualColumns = 0;

    public static void setAlignment(Node node, Pos pos) {
        TilePane.setConstraint(node, ALIGNMENT_CONSTRAINT, (Object)pos);
    }

    public static Pos getAlignment(Node node) {
        return (Pos)((Object)TilePane.getConstraint(node, ALIGNMENT_CONSTRAINT));
    }

    public static void setMargin(Node node, Insets insets) {
        TilePane.setConstraint(node, MARGIN_CONSTRAINT, insets);
    }

    public static Insets getMargin(Node node) {
        return (Insets)TilePane.getConstraint(node, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node node) {
        TilePane.setAlignment(node, null);
        TilePane.setMargin(node, null);
    }

    public TilePane() {
    }

    public TilePane(Orientation orientation) {
        this.setOrientation(orientation);
    }

    public TilePane(double d2, double d3) {
        this.setHgap(d2);
        this.setVgap(d3);
    }

    public TilePane(Orientation orientation, double d2, double d3) {
        this();
        this.setOrientation(orientation);
        this.setHgap(d2);
        this.setVgap(d3);
    }

    public TilePane(Node ... arrnode) {
        this.getChildren().addAll(arrnode);
    }

    public TilePane(Orientation orientation, Node ... arrnode) {
        this.setOrientation(orientation);
        this.getChildren().addAll(arrnode);
    }

    public TilePane(double d2, double d3, Node ... arrnode) {
        this.setHgap(d2);
        this.setVgap(d3);
        this.getChildren().addAll(arrnode);
    }

    public TilePane(Orientation orientation, double d2, double d3, Node ... arrnode) {
        this();
        this.setOrientation(orientation);
        this.setHgap(d2);
        this.setVgap(d3);
        this.getChildren().addAll(arrnode);
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty((Object)Orientation.HORIZONTAL){

                @Override
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override
                public CssMetaData<TilePane, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override
                public Object getBean() {
                    return TilePane.this;
                }

                @Override
                public String getName() {
                    return "orientation";
                }
            };
        }
        return this.orientation;
    }

    public final void setOrientation(Orientation orientation) {
        this.orientationProperty().set(orientation);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.HORIZONTAL : (Orientation)((Object)this.orientation.get());
    }

    public final IntegerProperty prefRowsProperty() {
        if (this.prefRows == null) {
            this.prefRows = new StyleableIntegerProperty(5){

                @Override
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override
                public CssMetaData<TilePane, Number> getCssMetaData() {
                    return StyleableProperties.PREF_ROWS;
                }

                @Override
                public Object getBean() {
                    return TilePane.this;
                }

                @Override
                public String getName() {
                    return "prefRows";
                }
            };
        }
        return this.prefRows;
    }

    public final void setPrefRows(int n2) {
        this.prefRowsProperty().set(n2);
    }

    public final int getPrefRows() {
        return this.prefRows == null ? 5 : this.prefRows.get();
    }

    public final IntegerProperty prefColumnsProperty() {
        if (this.prefColumns == null) {
            this.prefColumns = new StyleableIntegerProperty(5){

                @Override
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override
                public CssMetaData<TilePane, Number> getCssMetaData() {
                    return StyleableProperties.PREF_COLUMNS;
                }

                @Override
                public Object getBean() {
                    return TilePane.this;
                }

                @Override
                public String getName() {
                    return "prefColumns";
                }
            };
        }
        return this.prefColumns;
    }

    public final void setPrefColumns(int n2) {
        this.prefColumnsProperty().set(n2);
    }

    public final int getPrefColumns() {
        return this.prefColumns == null ? 5 : this.prefColumns.get();
    }

    public final DoubleProperty prefTileWidthProperty() {
        if (this.prefTileWidth == null) {
            this.prefTileWidth = new StyleableDoubleProperty(-1.0){

                @Override
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override
                public CssMetaData<TilePane, Number> getCssMetaData() {
                    return StyleableProperties.PREF_TILE_WIDTH;
                }

                @Override
                public Object getBean() {
                    return TilePane.this;
                }

                @Override
                public String getName() {
                    return "prefTileWidth";
                }
            };
        }
        return this.prefTileWidth;
    }

    public final void setPrefTileWidth(double d2) {
        this.prefTileWidthProperty().set(d2);
    }

    public final double getPrefTileWidth() {
        return this.prefTileWidth == null ? -1.0 : this.prefTileWidth.get();
    }

    public final DoubleProperty prefTileHeightProperty() {
        if (this.prefTileHeight == null) {
            this.prefTileHeight = new StyleableDoubleProperty(-1.0){

                @Override
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override
                public CssMetaData<TilePane, Number> getCssMetaData() {
                    return StyleableProperties.PREF_TILE_HEIGHT;
                }

                @Override
                public Object getBean() {
                    return TilePane.this;
                }

                @Override
                public String getName() {
                    return "prefTileHeight";
                }
            };
        }
        return this.prefTileHeight;
    }

    public final void setPrefTileHeight(double d2) {
        this.prefTileHeightProperty().set(d2);
    }

    public final double getPrefTileHeight() {
        return this.prefTileHeight == null ? -1.0 : this.prefTileHeight.get();
    }

    public final ReadOnlyDoubleProperty tileWidthProperty() {
        if (this.tileWidth == null) {
            this.tileWidth = new TileSizeProperty("tileWidth", this._tileWidth){

                @Override
                public double compute() {
                    return TilePane.this.computeTileWidth();
                }
            };
        }
        return this.tileWidth;
    }

    private void invalidateTileWidth() {
        if (this.tileWidth != null) {
            this.tileWidth.invalidate();
        } else {
            this._tileWidth = -1.0;
        }
    }

    public final double getTileWidth() {
        if (this.tileWidth != null) {
            return this.tileWidth.get();
        }
        if (this._tileWidth == -1.0) {
            this._tileWidth = this.computeTileWidth();
        }
        return this._tileWidth;
    }

    public final ReadOnlyDoubleProperty tileHeightProperty() {
        if (this.tileHeight == null) {
            this.tileHeight = new TileSizeProperty("tileHeight", this._tileHeight){

                @Override
                public double compute() {
                    return TilePane.this.computeTileHeight();
                }
            };
        }
        return this.tileHeight;
    }

    private void invalidateTileHeight() {
        if (this.tileHeight != null) {
            this.tileHeight.invalidate();
        } else {
            this._tileHeight = -1.0;
        }
    }

    public final double getTileHeight() {
        if (this.tileHeight != null) {
            return this.tileHeight.get();
        }
        if (this._tileHeight == -1.0) {
            this._tileHeight = this.computeTileHeight();
        }
        return this._tileHeight;
    }

    public final DoubleProperty hgapProperty() {
        if (this.hgap == null) {
            this.hgap = new StyleableDoubleProperty(){

                @Override
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override
                public CssMetaData<TilePane, Number> getCssMetaData() {
                    return StyleableProperties.HGAP;
                }

                @Override
                public Object getBean() {
                    return TilePane.this;
                }

                @Override
                public String getName() {
                    return "hgap";
                }
            };
        }
        return this.hgap;
    }

    public final void setHgap(double d2) {
        this.hgapProperty().set(d2);
    }

    public final double getHgap() {
        return this.hgap == null ? 0.0 : this.hgap.get();
    }

    public final DoubleProperty vgapProperty() {
        if (this.vgap == null) {
            this.vgap = new StyleableDoubleProperty(){

                @Override
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override
                public CssMetaData<TilePane, Number> getCssMetaData() {
                    return StyleableProperties.VGAP;
                }

                @Override
                public Object getBean() {
                    return TilePane.this;
                }

                @Override
                public String getName() {
                    return "vgap";
                }
            };
        }
        return this.vgap;
    }

    public final void setVgap(double d2) {
        this.vgapProperty().set(d2);
    }

    public final double getVgap() {
        return this.vgap == null ? 0.0 : this.vgap.get();
    }

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT){

                @Override
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override
                public CssMetaData<TilePane, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return TilePane.this;
                }

                @Override
                public String getName() {
                    return "alignment";
                }
            };
        }
        return this.alignment;
    }

    public final void setAlignment(Pos pos) {
        this.alignmentProperty().set(pos);
    }

    public final Pos getAlignment() {
        return this.alignment == null ? Pos.TOP_LEFT : (Pos)((Object)this.alignment.get());
    }

    private Pos getAlignmentInternal() {
        Pos pos = this.getAlignment();
        return pos == null ? Pos.TOP_LEFT : pos;
    }

    public final ObjectProperty<Pos> tileAlignmentProperty() {
        if (this.tileAlignment == null) {
            this.tileAlignment = new StyleableObjectProperty<Pos>(Pos.CENTER){

                @Override
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override
                public CssMetaData<TilePane, Pos> getCssMetaData() {
                    return StyleableProperties.TILE_ALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return TilePane.this;
                }

                @Override
                public String getName() {
                    return "tileAlignment";
                }
            };
        }
        return this.tileAlignment;
    }

    public final void setTileAlignment(Pos pos) {
        this.tileAlignmentProperty().set(pos);
    }

    public final Pos getTileAlignment() {
        return this.tileAlignment == null ? Pos.CENTER : (Pos)((Object)this.tileAlignment.get());
    }

    private Pos getTileAlignmentInternal() {
        Pos pos = this.getTileAlignment();
        return pos == null ? Pos.CENTER : pos;
    }

    @Override
    public Orientation getContentBias() {
        return this.getOrientation();
    }

    @Override
    public void requestLayout() {
        this.invalidateTileWidth();
        this.invalidateTileHeight();
        super.requestLayout();
    }

    @Override
    protected double computeMinWidth(double d2) {
        if (this.getContentBias() == Orientation.HORIZONTAL) {
            return this.getInsets().getLeft() + this.getTileWidth() + this.getInsets().getRight();
        }
        return this.computePrefWidth(d2);
    }

    @Override
    protected double computeMinHeight(double d2) {
        if (this.getContentBias() == Orientation.VERTICAL) {
            return this.getInsets().getTop() + this.getTileHeight() + this.getInsets().getBottom();
        }
        return this.computePrefHeight(d2);
    }

    @Override
    protected double computePrefWidth(double d2) {
        List list = this.getManagedChildren();
        Insets insets = this.getInsets();
        int n2 = 0;
        if (d2 != -1.0) {
            int n3 = this.computeRows(d2 - this.snapSpace(insets.getTop()) - this.snapSpace(insets.getBottom()), this.getTileHeight());
            n2 = this.computeOther(list.size(), n3);
        } else {
            n2 = this.getOrientation() == Orientation.HORIZONTAL ? this.getPrefColumns() : this.computeOther(list.size(), this.getPrefRows());
        }
        return this.snapSpace(insets.getLeft()) + this.computeContentWidth(n2, this.getTileWidth()) + this.snapSpace(insets.getRight());
    }

    @Override
    protected double computePrefHeight(double d2) {
        List list = this.getManagedChildren();
        Insets insets = this.getInsets();
        int n2 = 0;
        if (d2 != -1.0) {
            int n3 = this.computeColumns(d2 - this.snapSpace(insets.getLeft()) - this.snapSpace(insets.getRight()), this.getTileWidth());
            n2 = this.computeOther(list.size(), n3);
        } else {
            n2 = this.getOrientation() == Orientation.HORIZONTAL ? this.computeOther(list.size(), this.getPrefColumns()) : this.getPrefRows();
        }
        return this.snapSpace(insets.getTop()) + this.computeContentHeight(n2, this.getTileHeight()) + this.snapSpace(insets.getBottom());
    }

    private double computeTileWidth() {
        List<Node> list = this.getManagedChildren();
        double d2 = this.getPrefTileWidth();
        if (d2 == -1.0) {
            double d3 = -1.0;
            boolean bl = false;
            int n2 = list.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node = (Node)list.get(i2);
                if (node.getContentBias() != Orientation.VERTICAL) continue;
                bl = true;
                break;
            }
            if (bl) {
                d3 = this.computeMaxPrefAreaHeight(list, marginAccessor, -1.0, this.getTileAlignmentInternal().getVpos());
            }
            return this.snapSize(this.computeMaxPrefAreaWidth(list, marginAccessor, d3, true));
        }
        return this.snapSize(d2);
    }

    private double computeTileHeight() {
        List<Node> list = this.getManagedChildren();
        double d2 = this.getPrefTileHeight();
        if (d2 == -1.0) {
            double d3 = -1.0;
            boolean bl = false;
            int n2 = list.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node = (Node)list.get(i2);
                if (node.getContentBias() != Orientation.HORIZONTAL) continue;
                bl = true;
                break;
            }
            if (bl) {
                d3 = this.computeMaxPrefAreaWidth(list, marginAccessor);
            }
            return this.snapSize(this.computeMaxPrefAreaHeight(list, marginAccessor, d3, this.getTileAlignmentInternal().getVpos()));
        }
        return this.snapSize(d2);
    }

    private int computeOther(int n2, int n3) {
        double d2 = (double)n2 / (double)Math.max(1, n3);
        return (int)Math.ceil(d2);
    }

    private int computeColumns(double d2, double d3) {
        return Math.max(1, (int)((d2 + this.snapSpace(this.getHgap())) / (d3 + this.snapSpace(this.getHgap()))));
    }

    private int computeRows(double d2, double d3) {
        return Math.max(1, (int)((d2 + this.snapSpace(this.getVgap())) / (d3 + this.snapSpace(this.getVgap()))));
    }

    private double computeContentWidth(int n2, double d2) {
        if (n2 == 0) {
            return 0.0;
        }
        return (double)n2 * d2 + (double)(n2 - 1) * this.snapSpace(this.getHgap());
    }

    private double computeContentHeight(int n2, double d2) {
        if (n2 == 0) {
            return 0.0;
        }
        return (double)n2 * d2 + (double)(n2 - 1) * this.snapSpace(this.getVgap());
    }

    @Override
    protected void layoutChildren() {
        List<Node> list = this.getManagedChildren();
        HPos hPos = this.getAlignmentInternal().getHpos();
        VPos vPos = this.getAlignmentInternal().getVpos();
        double d2 = this.getWidth();
        double d3 = this.getHeight();
        double d4 = this.snapSpace(this.getInsets().getTop());
        double d5 = this.snapSpace(this.getInsets().getLeft());
        double d6 = this.snapSpace(this.getInsets().getBottom());
        double d7 = this.snapSpace(this.getInsets().getRight());
        double d8 = this.snapSpace(this.getVgap());
        double d9 = this.snapSpace(this.getHgap());
        double d10 = d2 - d5 - d7;
        double d11 = d3 - d4 - d6;
        double d12 = this.getTileWidth() > d10 ? d10 : this.getTileWidth();
        double d13 = this.getTileHeight() > d11 ? d11 : this.getTileHeight();
        int n3 = 0;
        int n4 = 0;
        if (this.getOrientation() == Orientation.HORIZONTAL) {
            this.actualColumns = this.computeColumns(d10, d12);
            this.actualRows = this.computeOther(list.size(), this.actualColumns);
            n3 = hPos != HPos.LEFT ? this.actualColumns - (this.actualColumns * this.actualRows - list.size()) : 0;
        } else {
            this.actualRows = this.computeRows(d11, d13);
            this.actualColumns = this.computeOther(list.size(), this.actualRows);
            n4 = vPos != VPos.TOP ? this.actualRows - (this.actualColumns * this.actualRows - list.size()) : 0;
        }
        double d14 = d5 + TilePane.computeXOffset(d10, this.computeContentWidth(this.actualColumns, d12), hPos);
        double d15 = d4 + TilePane.computeYOffset(d11, this.computeContentHeight(this.actualRows, d13), vPos);
        double d16 = n3 > 0 ? d5 + TilePane.computeXOffset(d10, this.computeContentWidth(n3, d12), hPos) : d14;
        double d17 = n4 > 0 ? d4 + TilePane.computeYOffset(d11, this.computeContentHeight(n4, d13), vPos) : d15;
        double d18 = this.getTileAlignmentInternal().getVpos() == VPos.BASELINE ? this.getAreaBaselineOffset(list, marginAccessor, n2 -> d12, d13, false) : -1.0;
        int n5 = 0;
        int n6 = 0;
        int n7 = list.size();
        for (int i2 = 0; i2 < n7; ++i2) {
            Node node = list.get(i2);
            double d19 = n5 == this.actualRows - 1 ? d16 : d14;
            double d20 = n6 == this.actualColumns - 1 ? d17 : d15;
            double d21 = d19 + (double)n6 * (d12 + d9);
            double d22 = d20 + (double)n5 * (d13 + d8);
            Pos pos = TilePane.getAlignment(node);
            this.layoutInArea(node, d21, d22, d12, d13, d18, TilePane.getMargin(node), pos != null ? pos.getHpos() : this.getTileAlignmentInternal().getHpos(), pos != null ? pos.getVpos() : this.getTileAlignmentInternal().getVpos());
            if (this.getOrientation() == Orientation.HORIZONTAL) {
                if (++n6 != this.actualColumns) continue;
                n6 = 0;
                ++n5;
                continue;
            }
            if (++n5 != this.actualRows) continue;
            n5 = 0;
            ++n6;
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TilePane.getClassCssMetaData();
    }

    private abstract class TileSizeProperty
    extends ReadOnlyDoubleProperty {
        private final String name;
        private ExpressionHelper<Number> helper;
        private double value;
        private boolean valid;

        TileSizeProperty(String string, double d2) {
            this.name = string;
            this.value = d2;
            this.valid = d2 != -1.0;
        }

        @Override
        public Object getBean() {
            return TilePane.this;
        }

        @Override
        public String getName() {
            return this.name;
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
        public void addListener(ChangeListener<? super Number> changeListener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
        }

        @Override
        public void removeListener(ChangeListener<? super Number> changeListener) {
            this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
        }

        @Override
        public double get() {
            if (!this.valid) {
                this.value = this.compute();
                this.valid = true;
            }
            return this.value;
        }

        public void invalidate() {
            if (this.valid) {
                this.valid = false;
                ExpressionHelper.fireValueChangedEvent(this.helper);
            }
        }

        public abstract double compute();
    }

    private static class StyleableProperties {
        private static final CssMetaData<TilePane, Pos> ALIGNMENT = new CssMetaData<TilePane, Pos>("-fx-alignment", new EnumConverter<Pos>(Pos.class), Pos.TOP_LEFT){

            @Override
            public boolean isSettable(TilePane tilePane) {
                return tilePane.alignment == null || !tilePane.alignment.isBound();
            }

            @Override
            public StyleableProperty<Pos> getStyleableProperty(TilePane tilePane) {
                return (StyleableProperty)((Object)tilePane.alignmentProperty());
            }
        };
        private static final CssMetaData<TilePane, Number> PREF_COLUMNS = new CssMetaData<TilePane, Number>("-fx-pref-columns", SizeConverter.getInstance(), (Number)5.0){

            @Override
            public boolean isSettable(TilePane tilePane) {
                return tilePane.prefColumns == null || !tilePane.prefColumns.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TilePane tilePane) {
                return (StyleableProperty)((Object)tilePane.prefColumnsProperty());
            }
        };
        private static final CssMetaData<TilePane, Number> HGAP = new CssMetaData<TilePane, Number>("-fx-hgap", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(TilePane tilePane) {
                return tilePane.hgap == null || !tilePane.hgap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TilePane tilePane) {
                return (StyleableProperty)((Object)tilePane.hgapProperty());
            }
        };
        private static final CssMetaData<TilePane, Number> PREF_ROWS = new CssMetaData<TilePane, Number>("-fx-pref-rows", SizeConverter.getInstance(), (Number)5.0){

            @Override
            public boolean isSettable(TilePane tilePane) {
                return tilePane.prefRows == null || !tilePane.prefRows.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TilePane tilePane) {
                return (StyleableProperty)((Object)tilePane.prefRowsProperty());
            }
        };
        private static final CssMetaData<TilePane, Pos> TILE_ALIGNMENT = new CssMetaData<TilePane, Pos>("-fx-tile-alignment", new EnumConverter<Pos>(Pos.class), Pos.CENTER){

            @Override
            public boolean isSettable(TilePane tilePane) {
                return tilePane.tileAlignment == null || !tilePane.tileAlignment.isBound();
            }

            @Override
            public StyleableProperty<Pos> getStyleableProperty(TilePane tilePane) {
                return (StyleableProperty)((Object)tilePane.tileAlignmentProperty());
            }
        };
        private static final CssMetaData<TilePane, Number> PREF_TILE_WIDTH = new CssMetaData<TilePane, Number>("-fx-pref-tile-width", SizeConverter.getInstance(), (Number)-1.0){

            @Override
            public boolean isSettable(TilePane tilePane) {
                return tilePane.prefTileWidth == null || !tilePane.prefTileWidth.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TilePane tilePane) {
                return (StyleableProperty)((Object)tilePane.prefTileWidthProperty());
            }
        };
        private static final CssMetaData<TilePane, Number> PREF_TILE_HEIGHT = new CssMetaData<TilePane, Number>("-fx-pref-tile-height", SizeConverter.getInstance(), (Number)-1.0){

            @Override
            public boolean isSettable(TilePane tilePane) {
                return tilePane.prefTileHeight == null || !tilePane.prefTileHeight.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TilePane tilePane) {
                return (StyleableProperty)((Object)tilePane.prefTileHeightProperty());
            }
        };
        private static final CssMetaData<TilePane, Orientation> ORIENTATION = new CssMetaData<TilePane, Orientation>("-fx-orientation", new EnumConverter<Orientation>(Orientation.class), Orientation.HORIZONTAL){

            @Override
            public Orientation getInitialValue(TilePane tilePane) {
                return tilePane.getOrientation();
            }

            @Override
            public boolean isSettable(TilePane tilePane) {
                return tilePane.orientation == null || !tilePane.orientation.isBound();
            }

            @Override
            public StyleableProperty<Orientation> getStyleableProperty(TilePane tilePane) {
                return (StyleableProperty)((Object)tilePane.orientationProperty());
            }
        };
        private static final CssMetaData<TilePane, Number> VGAP = new CssMetaData<TilePane, Number>("-fx-vgap", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(TilePane tilePane) {
                return tilePane.vgap == null || !tilePane.vgap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TilePane tilePane) {
                return (StyleableProperty)((Object)tilePane.vgapProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Region.getClassCssMetaData());
            arrayList.add(ALIGNMENT);
            arrayList.add(HGAP);
            arrayList.add(ORIENTATION);
            arrayList.add(PREF_COLUMNS);
            arrayList.add(PREF_ROWS);
            arrayList.add(PREF_TILE_WIDTH);
            arrayList.add(PREF_TILE_HEIGHT);
            arrayList.add(TILE_ALIGNMENT);
            arrayList.add(VGAP);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

