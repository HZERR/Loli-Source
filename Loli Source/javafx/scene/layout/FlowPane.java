/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
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

public class FlowPane
extends Pane {
    private static final String MARGIN_CONSTRAINT = "flowpane-margin";
    private static final Callback<Node, Insets> marginAccessor = node -> FlowPane.getMargin(node);
    private ObjectProperty<Orientation> orientation;
    private DoubleProperty hgap;
    private DoubleProperty vgap;
    private DoubleProperty prefWrapLength;
    private ObjectProperty<Pos> alignment;
    private ObjectProperty<HPos> columnHalignment;
    private ObjectProperty<VPos> rowValignment;
    private List<Run> runs = null;
    private double lastMaxRunLength = -1.0;
    boolean computingRuns = false;

    public static void setMargin(Node node, Insets insets) {
        FlowPane.setConstraint(node, MARGIN_CONSTRAINT, insets);
    }

    public static Insets getMargin(Node node) {
        return (Insets)FlowPane.getConstraint(node, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node node) {
        FlowPane.setMargin(node, null);
    }

    public FlowPane() {
    }

    public FlowPane(Orientation orientation) {
        this();
        this.setOrientation(orientation);
    }

    public FlowPane(double d2, double d3) {
        this();
        this.setHgap(d2);
        this.setVgap(d3);
    }

    public FlowPane(Orientation orientation, double d2, double d3) {
        this();
        this.setOrientation(orientation);
        this.setHgap(d2);
        this.setVgap(d3);
    }

    public FlowPane(Node ... arrnode) {
        this.getChildren().addAll(arrnode);
    }

    public FlowPane(Orientation orientation, Node ... arrnode) {
        this();
        this.setOrientation(orientation);
        this.getChildren().addAll(arrnode);
    }

    public FlowPane(double d2, double d3, Node ... arrnode) {
        this();
        this.setHgap(d2);
        this.setVgap(d3);
        this.getChildren().addAll(arrnode);
    }

    public FlowPane(Orientation orientation, double d2, double d3, Node ... arrnode) {
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
                    FlowPane.this.requestLayout();
                }

                @Override
                public CssMetaData<FlowPane, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override
                public Object getBean() {
                    return FlowPane.this;
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

    public final DoubleProperty hgapProperty() {
        if (this.hgap == null) {
            this.hgap = new StyleableDoubleProperty(){

                @Override
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override
                public CssMetaData<FlowPane, Number> getCssMetaData() {
                    return StyleableProperties.HGAP;
                }

                @Override
                public Object getBean() {
                    return FlowPane.this;
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
                    FlowPane.this.requestLayout();
                }

                @Override
                public CssMetaData<FlowPane, Number> getCssMetaData() {
                    return StyleableProperties.VGAP;
                }

                @Override
                public Object getBean() {
                    return FlowPane.this;
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

    public final DoubleProperty prefWrapLengthProperty() {
        if (this.prefWrapLength == null) {
            this.prefWrapLength = new DoublePropertyBase(400.0){

                @Override
                protected void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override
                public String getName() {
                    return "prefWrapLength";
                }
            };
        }
        return this.prefWrapLength;
    }

    public final void setPrefWrapLength(double d2) {
        this.prefWrapLengthProperty().set(d2);
    }

    public final double getPrefWrapLength() {
        return this.prefWrapLength == null ? 400.0 : this.prefWrapLength.get();
    }

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT){

                @Override
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override
                public CssMetaData<FlowPane, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return FlowPane.this;
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

    public final ObjectProperty<HPos> columnHalignmentProperty() {
        if (this.columnHalignment == null) {
            this.columnHalignment = new StyleableObjectProperty<HPos>(HPos.LEFT){

                @Override
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override
                public CssMetaData<FlowPane, HPos> getCssMetaData() {
                    return StyleableProperties.COLUMN_HALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override
                public String getName() {
                    return "columnHalignment";
                }
            };
        }
        return this.columnHalignment;
    }

    public final void setColumnHalignment(HPos hPos) {
        this.columnHalignmentProperty().set(hPos);
    }

    public final HPos getColumnHalignment() {
        return this.columnHalignment == null ? HPos.LEFT : (HPos)((Object)this.columnHalignment.get());
    }

    private HPos getColumnHalignmentInternal() {
        HPos hPos = this.getColumnHalignment();
        return hPos == null ? HPos.LEFT : hPos;
    }

    public final ObjectProperty<VPos> rowValignmentProperty() {
        if (this.rowValignment == null) {
            this.rowValignment = new StyleableObjectProperty<VPos>(VPos.CENTER){

                @Override
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override
                public CssMetaData<FlowPane, VPos> getCssMetaData() {
                    return StyleableProperties.ROW_VALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override
                public String getName() {
                    return "rowValignment";
                }
            };
        }
        return this.rowValignment;
    }

    public final void setRowValignment(VPos vPos) {
        this.rowValignmentProperty().set(vPos);
    }

    public final VPos getRowValignment() {
        return this.rowValignment == null ? VPos.CENTER : (VPos)((Object)this.rowValignment.get());
    }

    private VPos getRowValignmentInternal() {
        VPos vPos = this.getRowValignment();
        return vPos == null ? VPos.CENTER : vPos;
    }

    @Override
    public Orientation getContentBias() {
        return this.getOrientation();
    }

    @Override
    protected double computeMinWidth(double d2) {
        if (this.getContentBias() == Orientation.HORIZONTAL) {
            double d3 = 0.0;
            ObservableList<Node> observableList = this.getChildren();
            int n2 = observableList.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node = (Node)observableList.get(i2);
                if (!node.isManaged()) continue;
                d3 = Math.max(d3, node.prefWidth(-1.0));
            }
            Insets insets = this.getInsets();
            return insets.getLeft() + this.snapSize(d3) + insets.getRight();
        }
        return this.computePrefWidth(d2);
    }

    @Override
    protected double computeMinHeight(double d2) {
        if (this.getContentBias() == Orientation.VERTICAL) {
            double d3 = 0.0;
            ObservableList<Node> observableList = this.getChildren();
            int n2 = observableList.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node = (Node)observableList.get(i2);
                if (!node.isManaged()) continue;
                d3 = Math.max(d3, node.prefHeight(-1.0));
            }
            Insets insets = this.getInsets();
            return insets.getTop() + this.snapSize(d3) + insets.getBottom();
        }
        return this.computePrefHeight(d2);
    }

    @Override
    protected double computePrefWidth(double d2) {
        Insets insets = this.getInsets();
        if (this.getOrientation() == Orientation.HORIZONTAL) {
            double d3 = this.getPrefWrapLength();
            List<Run> list = this.getRuns(d3);
            double d4 = this.computeContentWidth(list);
            d4 = this.getPrefWrapLength() > d4 ? this.getPrefWrapLength() : d4;
            return insets.getLeft() + this.snapSize(d4) + insets.getRight();
        }
        double d5 = d2 != -1.0 ? d2 - insets.getTop() - insets.getBottom() : this.getPrefWrapLength();
        List<Run> list = this.getRuns(d5);
        return insets.getLeft() + this.computeContentWidth(list) + insets.getRight();
    }

    @Override
    protected double computePrefHeight(double d2) {
        Insets insets = this.getInsets();
        if (this.getOrientation() == Orientation.HORIZONTAL) {
            double d3 = d2 != -1.0 ? d2 - insets.getLeft() - insets.getRight() : this.getPrefWrapLength();
            List<Run> list = this.getRuns(d3);
            return insets.getTop() + this.computeContentHeight(list) + insets.getBottom();
        }
        double d4 = this.getPrefWrapLength();
        List<Run> list = this.getRuns(d4);
        double d5 = this.computeContentHeight(list);
        d5 = this.getPrefWrapLength() > d5 ? this.getPrefWrapLength() : d5;
        return insets.getTop() + this.snapSize(d5) + insets.getBottom();
    }

    @Override
    public void requestLayout() {
        if (!this.computingRuns) {
            this.runs = null;
        }
        super.requestLayout();
    }

    private List<Run> getRuns(double d2) {
        if (this.runs == null || d2 != this.lastMaxRunLength) {
            this.computingRuns = true;
            this.lastMaxRunLength = d2;
            this.runs = new ArrayList<Run>();
            double d3 = 0.0;
            double d4 = 0.0;
            Run run = new Run();
            double d5 = this.snapSpace(this.getVgap());
            double d6 = this.snapSpace(this.getHgap());
            ObservableList<Node> observableList = this.getChildren();
            int n2 = observableList.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                double d7;
                Node node = (Node)observableList.get(i2);
                if (!node.isManaged()) continue;
                LayoutRect layoutRect = new LayoutRect();
                layoutRect.node = node;
                Insets insets = FlowPane.getMargin(node);
                layoutRect.width = this.computeChildPrefAreaWidth(node, insets);
                layoutRect.height = this.computeChildPrefAreaHeight(node, insets);
                double d8 = d7 = this.getOrientation() == Orientation.HORIZONTAL ? layoutRect.width : layoutRect.height;
                if (d3 + d7 > d2 && d3 > 0.0) {
                    this.normalizeRun(run, d4);
                    d4 = this.getOrientation() == Orientation.HORIZONTAL ? (d4 += run.height + d5) : (d4 += run.width + d6);
                    this.runs.add(run);
                    d3 = 0.0;
                    run = new Run();
                }
                if (this.getOrientation() == Orientation.HORIZONTAL) {
                    layoutRect.x = d3;
                    d3 += layoutRect.width + d6;
                } else {
                    layoutRect.y = d3;
                    d3 += layoutRect.height + d5;
                }
                run.rects.add(layoutRect);
            }
            this.normalizeRun(run, d4);
            this.runs.add(run);
            this.computingRuns = false;
        }
        return this.runs;
    }

    private void normalizeRun(Run run, double d2) {
        if (this.getOrientation() == Orientation.HORIZONTAL) {
            ArrayList<Node> arrayList = new ArrayList<Node>();
            run.width = (double)(run.rects.size() - 1) * this.snapSpace(this.getHgap());
            int n3 = run.rects.size();
            for (int i2 = 0; i2 < n3; ++i2) {
                LayoutRect layoutRect = run.rects.get(i2);
                arrayList.add(layoutRect.node);
                run.width += layoutRect.width;
                layoutRect.y = d2;
            }
            run.height = this.computeMaxPrefAreaHeight(arrayList, marginAccessor, this.getRowValignment());
            run.baselineOffset = this.getRowValignment() == VPos.BASELINE ? this.getAreaBaselineOffset(arrayList, marginAccessor, n2 -> run.rects.get((int)n2.intValue()).width, run.height, true) : 0.0;
        } else {
            run.height = (double)(run.rects.size() - 1) * this.snapSpace(this.getVgap());
            double d3 = 0.0;
            int n4 = run.rects.size();
            for (int i3 = 0; i3 < n4; ++i3) {
                LayoutRect layoutRect = run.rects.get(i3);
                run.height += layoutRect.height;
                layoutRect.x = d2;
                d3 = Math.max(d3, layoutRect.width);
            }
            run.width = d3;
            run.baselineOffset = run.height;
        }
    }

    private double computeContentWidth(List<Run> list) {
        double d2 = this.getOrientation() == Orientation.HORIZONTAL ? 0.0 : (double)(list.size() - 1) * this.snapSpace(this.getHgap());
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Run run = list.get(i2);
            if (this.getOrientation() == Orientation.HORIZONTAL) {
                d2 = Math.max(d2, run.width);
                continue;
            }
            d2 += run.width;
        }
        return d2;
    }

    private double computeContentHeight(List<Run> list) {
        double d2 = this.getOrientation() == Orientation.VERTICAL ? 0.0 : (double)(list.size() - 1) * this.snapSpace(this.getVgap());
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Run run = list.get(i2);
            if (this.getOrientation() == Orientation.VERTICAL) {
                d2 = Math.max(d2, run.height);
                continue;
            }
            d2 += run.height;
        }
        return d2;
    }

    @Override
    protected void layoutChildren() {
        Insets insets = this.getInsets();
        double d2 = this.getWidth();
        double d3 = this.getHeight();
        double d4 = insets.getTop();
        double d5 = insets.getLeft();
        double d6 = insets.getBottom();
        double d7 = insets.getRight();
        double d8 = d2 - d5 - d7;
        double d9 = d3 - d4 - d6;
        List<Run> list = this.getRuns(this.getOrientation() == Orientation.HORIZONTAL ? d8 : d9);
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Run run = list.get(i2);
            double d10 = d5 + FlowPane.computeXOffset(d8, this.getOrientation() == Orientation.HORIZONTAL ? run.width : this.computeContentWidth(list), this.getAlignmentInternal().getHpos());
            double d11 = d4 + FlowPane.computeYOffset(d9, this.getOrientation() == Orientation.VERTICAL ? run.height : this.computeContentHeight(list), this.getAlignmentInternal().getVpos());
            for (int i3 = 0; i3 < run.rects.size(); ++i3) {
                LayoutRect layoutRect = run.rects.get(i3);
                double d12 = d10 + layoutRect.x;
                double d13 = d11 + layoutRect.y;
                this.layoutInArea(layoutRect.node, d12, d13, this.getOrientation() == Orientation.HORIZONTAL ? layoutRect.width : run.width, this.getOrientation() == Orientation.VERTICAL ? layoutRect.height : run.height, run.baselineOffset, FlowPane.getMargin(layoutRect.node), this.getColumnHalignmentInternal(), this.getRowValignmentInternal());
            }
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return FlowPane.getClassCssMetaData();
    }

    private static class Run {
        ArrayList<LayoutRect> rects = new ArrayList();
        double width;
        double height;
        double baselineOffset;

        private Run() {
        }
    }

    private static class LayoutRect {
        public Node node;
        double x;
        double y;
        double width;
        double height;

        private LayoutRect() {
        }

        public String toString() {
            return "LayoutRect node id=" + this.node.getId() + " " + this.x + "," + this.y + " " + this.width + "x" + this.height;
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<FlowPane, Pos> ALIGNMENT = new CssMetaData<FlowPane, Pos>("-fx-alignment", new EnumConverter<Pos>(Pos.class), Pos.TOP_LEFT){

            @Override
            public boolean isSettable(FlowPane flowPane) {
                return flowPane.alignment == null || !flowPane.alignment.isBound();
            }

            @Override
            public StyleableProperty<Pos> getStyleableProperty(FlowPane flowPane) {
                return (StyleableProperty)((Object)flowPane.alignmentProperty());
            }
        };
        private static final CssMetaData<FlowPane, HPos> COLUMN_HALIGNMENT = new CssMetaData<FlowPane, HPos>("-fx-column-halignment", new EnumConverter<HPos>(HPos.class), HPos.LEFT){

            @Override
            public boolean isSettable(FlowPane flowPane) {
                return flowPane.columnHalignment == null || !flowPane.columnHalignment.isBound();
            }

            @Override
            public StyleableProperty<HPos> getStyleableProperty(FlowPane flowPane) {
                return (StyleableProperty)((Object)flowPane.columnHalignmentProperty());
            }
        };
        private static final CssMetaData<FlowPane, Number> HGAP = new CssMetaData<FlowPane, Number>("-fx-hgap", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(FlowPane flowPane) {
                return flowPane.hgap == null || !flowPane.hgap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(FlowPane flowPane) {
                return (StyleableProperty)((Object)flowPane.hgapProperty());
            }
        };
        private static final CssMetaData<FlowPane, VPos> ROW_VALIGNMENT = new CssMetaData<FlowPane, VPos>("-fx-row-valignment", new EnumConverter<VPos>(VPos.class), VPos.CENTER){

            @Override
            public boolean isSettable(FlowPane flowPane) {
                return flowPane.rowValignment == null || !flowPane.rowValignment.isBound();
            }

            @Override
            public StyleableProperty<VPos> getStyleableProperty(FlowPane flowPane) {
                return (StyleableProperty)((Object)flowPane.rowValignmentProperty());
            }
        };
        private static final CssMetaData<FlowPane, Orientation> ORIENTATION = new CssMetaData<FlowPane, Orientation>("-fx-orientation", new EnumConverter<Orientation>(Orientation.class), Orientation.HORIZONTAL){

            @Override
            public Orientation getInitialValue(FlowPane flowPane) {
                return flowPane.getOrientation();
            }

            @Override
            public boolean isSettable(FlowPane flowPane) {
                return flowPane.orientation == null || !flowPane.orientation.isBound();
            }

            @Override
            public StyleableProperty<Orientation> getStyleableProperty(FlowPane flowPane) {
                return (StyleableProperty)((Object)flowPane.orientationProperty());
            }
        };
        private static final CssMetaData<FlowPane, Number> VGAP = new CssMetaData<FlowPane, Number>("-fx-vgap", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(FlowPane flowPane) {
                return flowPane.vgap == null || !flowPane.vgap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(FlowPane flowPane) {
                return (StyleableProperty)((Object)flowPane.vgapProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Region.getClassCssMetaData());
            arrayList.add(ALIGNMENT);
            arrayList.add(COLUMN_HALIGNMENT);
            arrayList.add(HGAP);
            arrayList.add(ROW_VALIGNMENT);
            arrayList.add(ORIENTATION);
            arrayList.add(VGAP);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

