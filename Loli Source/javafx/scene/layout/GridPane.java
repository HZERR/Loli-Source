/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
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
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Callback;

public class GridPane
extends Pane {
    public static final int REMAINING = Integer.MAX_VALUE;
    private static final String MARGIN_CONSTRAINT = "gridpane-margin";
    private static final String HALIGNMENT_CONSTRAINT = "gridpane-halignment";
    private static final String VALIGNMENT_CONSTRAINT = "gridpane-valignment";
    private static final String HGROW_CONSTRAINT = "gridpane-hgrow";
    private static final String VGROW_CONSTRAINT = "gridpane-vgrow";
    private static final String ROW_INDEX_CONSTRAINT = "gridpane-row";
    private static final String COLUMN_INDEX_CONSTRAINT = "gridpane-column";
    private static final String ROW_SPAN_CONSTRAINT = "gridpane-row-span";
    private static final String COLUMN_SPAN_CONSTRAINT = "gridpane-column-span";
    private static final String FILL_WIDTH_CONSTRAINT = "gridpane-fill-width";
    private static final String FILL_HEIGHT_CONSTRAINT = "gridpane-fill-height";
    private static final Callback<Node, Insets> marginAccessor = node -> GridPane.getMargin(node);
    private static final Color GRID_LINE_COLOR = Color.rgb(30, 30, 30);
    private static final double GRID_LINE_DASH = 3.0;
    private DoubleProperty hgap;
    private DoubleProperty vgap;
    private ObjectProperty<Pos> alignment;
    private BooleanProperty gridLinesVisible;
    private final ObservableList<RowConstraints> rowConstraints = new TrackableObservableList<RowConstraints>(){

        @Override
        protected void onChanged(ListChangeListener.Change<RowConstraints> change) {
            while (change.next()) {
                for (RowConstraints rowConstraints : change.getRemoved()) {
                    if (rowConstraints == null || GridPane.this.rowConstraints.contains(rowConstraints)) continue;
                    rowConstraints.remove(GridPane.this);
                }
                for (RowConstraints rowConstraints : change.getAddedSubList()) {
                    if (rowConstraints == null) continue;
                    rowConstraints.add(GridPane.this);
                }
            }
            GridPane.this.requestLayout();
        }
    };
    private final ObservableList<ColumnConstraints> columnConstraints = new TrackableObservableList<ColumnConstraints>(){

        @Override
        protected void onChanged(ListChangeListener.Change<ColumnConstraints> change) {
            while (change.next()) {
                for (ColumnConstraints columnConstraints : change.getRemoved()) {
                    if (columnConstraints == null || GridPane.this.columnConstraints.contains(columnConstraints)) continue;
                    columnConstraints.remove(GridPane.this);
                }
                for (ColumnConstraints columnConstraints : change.getAddedSubList()) {
                    if (columnConstraints == null) continue;
                    columnConstraints.add(GridPane.this);
                }
            }
            GridPane.this.requestLayout();
        }
    };
    private Group gridLines;
    private Orientation bias;
    private double[] rowPercentHeight;
    private double rowPercentTotal = 0.0;
    private CompositeSize rowMinHeight;
    private CompositeSize rowPrefHeight;
    private CompositeSize rowMaxHeight;
    private List<Node>[] rowBaseline;
    private double[] rowMinBaselineComplement;
    private double[] rowPrefBaselineComplement;
    private double[] rowMaxBaselineComplement;
    private Priority[] rowGrow;
    private double[] columnPercentWidth;
    private double columnPercentTotal = 0.0;
    private CompositeSize columnMinWidth;
    private CompositeSize columnPrefWidth;
    private CompositeSize columnMaxWidth;
    private Priority[] columnGrow;
    private boolean metricsDirty = true;
    private boolean performingLayout = false;
    private int numRows;
    private int numColumns;
    private CompositeSize currentHeights;
    private CompositeSize currentWidths;

    public static void setRowIndex(Node node, Integer n2) {
        if (n2 != null && n2 < 0) {
            throw new IllegalArgumentException("rowIndex must be greater or equal to 0, but was " + n2);
        }
        GridPane.setConstraint(node, ROW_INDEX_CONSTRAINT, n2);
    }

    public static Integer getRowIndex(Node node) {
        return (Integer)GridPane.getConstraint(node, ROW_INDEX_CONSTRAINT);
    }

    public static void setColumnIndex(Node node, Integer n2) {
        if (n2 != null && n2 < 0) {
            throw new IllegalArgumentException("columnIndex must be greater or equal to 0, but was " + n2);
        }
        GridPane.setConstraint(node, COLUMN_INDEX_CONSTRAINT, n2);
    }

    public static Integer getColumnIndex(Node node) {
        return (Integer)GridPane.getConstraint(node, COLUMN_INDEX_CONSTRAINT);
    }

    public static void setRowSpan(Node node, Integer n2) {
        if (n2 != null && n2 < 1) {
            throw new IllegalArgumentException("rowSpan must be greater or equal to 1, but was " + n2);
        }
        GridPane.setConstraint(node, ROW_SPAN_CONSTRAINT, n2);
    }

    public static Integer getRowSpan(Node node) {
        return (Integer)GridPane.getConstraint(node, ROW_SPAN_CONSTRAINT);
    }

    public static void setColumnSpan(Node node, Integer n2) {
        if (n2 != null && n2 < 1) {
            throw new IllegalArgumentException("columnSpan must be greater or equal to 1, but was " + n2);
        }
        GridPane.setConstraint(node, COLUMN_SPAN_CONSTRAINT, n2);
    }

    public static Integer getColumnSpan(Node node) {
        return (Integer)GridPane.getConstraint(node, COLUMN_SPAN_CONSTRAINT);
    }

    public static void setMargin(Node node, Insets insets) {
        GridPane.setConstraint(node, MARGIN_CONSTRAINT, insets);
    }

    public static Insets getMargin(Node node) {
        return (Insets)GridPane.getConstraint(node, MARGIN_CONSTRAINT);
    }

    private double getBaselineComplementForChild(Node node) {
        if (this.isNodePositionedByBaseline(node)) {
            return this.rowMinBaselineComplement[GridPane.getNodeRowIndex(node)];
        }
        return -1.0;
    }

    public static void setHalignment(Node node, HPos hPos) {
        GridPane.setConstraint(node, HALIGNMENT_CONSTRAINT, (Object)hPos);
    }

    public static HPos getHalignment(Node node) {
        return (HPos)((Object)GridPane.getConstraint(node, HALIGNMENT_CONSTRAINT));
    }

    public static void setValignment(Node node, VPos vPos) {
        GridPane.setConstraint(node, VALIGNMENT_CONSTRAINT, (Object)vPos);
    }

    public static VPos getValignment(Node node) {
        return (VPos)((Object)GridPane.getConstraint(node, VALIGNMENT_CONSTRAINT));
    }

    public static void setHgrow(Node node, Priority priority) {
        GridPane.setConstraint(node, HGROW_CONSTRAINT, (Object)priority);
    }

    public static Priority getHgrow(Node node) {
        return (Priority)((Object)GridPane.getConstraint(node, HGROW_CONSTRAINT));
    }

    public static void setVgrow(Node node, Priority priority) {
        GridPane.setConstraint(node, VGROW_CONSTRAINT, (Object)priority);
    }

    public static Priority getVgrow(Node node) {
        return (Priority)((Object)GridPane.getConstraint(node, VGROW_CONSTRAINT));
    }

    public static void setFillWidth(Node node, Boolean bl) {
        GridPane.setConstraint(node, FILL_WIDTH_CONSTRAINT, bl);
    }

    public static Boolean isFillWidth(Node node) {
        return (Boolean)GridPane.getConstraint(node, FILL_WIDTH_CONSTRAINT);
    }

    public static void setFillHeight(Node node, Boolean bl) {
        GridPane.setConstraint(node, FILL_HEIGHT_CONSTRAINT, bl);
    }

    public static Boolean isFillHeight(Node node) {
        return (Boolean)GridPane.getConstraint(node, FILL_HEIGHT_CONSTRAINT);
    }

    public static void setConstraints(Node node, int n2, int n3) {
        GridPane.setRowIndex(node, n3);
        GridPane.setColumnIndex(node, n2);
    }

    public static void setConstraints(Node node, int n2, int n3, int n4, int n5) {
        GridPane.setRowIndex(node, n3);
        GridPane.setColumnIndex(node, n2);
        GridPane.setRowSpan(node, n5);
        GridPane.setColumnSpan(node, n4);
    }

    public static void setConstraints(Node node, int n2, int n3, int n4, int n5, HPos hPos, VPos vPos) {
        GridPane.setRowIndex(node, n3);
        GridPane.setColumnIndex(node, n2);
        GridPane.setRowSpan(node, n5);
        GridPane.setColumnSpan(node, n4);
        GridPane.setHalignment(node, hPos);
        GridPane.setValignment(node, vPos);
    }

    public static void setConstraints(Node node, int n2, int n3, int n4, int n5, HPos hPos, VPos vPos, Priority priority, Priority priority2) {
        GridPane.setRowIndex(node, n3);
        GridPane.setColumnIndex(node, n2);
        GridPane.setRowSpan(node, n5);
        GridPane.setColumnSpan(node, n4);
        GridPane.setHalignment(node, hPos);
        GridPane.setValignment(node, vPos);
        GridPane.setHgrow(node, priority);
        GridPane.setVgrow(node, priority2);
    }

    public static void setConstraints(Node node, int n2, int n3, int n4, int n5, HPos hPos, VPos vPos, Priority priority, Priority priority2, Insets insets) {
        GridPane.setRowIndex(node, n3);
        GridPane.setColumnIndex(node, n2);
        GridPane.setRowSpan(node, n5);
        GridPane.setColumnSpan(node, n4);
        GridPane.setHalignment(node, hPos);
        GridPane.setValignment(node, vPos);
        GridPane.setHgrow(node, priority);
        GridPane.setVgrow(node, priority2);
        GridPane.setMargin(node, insets);
    }

    public static void clearConstraints(Node node) {
        GridPane.setRowIndex(node, null);
        GridPane.setColumnIndex(node, null);
        GridPane.setRowSpan(node, null);
        GridPane.setColumnSpan(node, null);
        GridPane.setHalignment(node, null);
        GridPane.setValignment(node, null);
        GridPane.setHgrow(node, null);
        GridPane.setVgrow(node, null);
        GridPane.setMargin(node, null);
    }

    static void createRow(int n2, int n3, Node ... arrnode) {
        for (int i2 = 0; i2 < arrnode.length; ++i2) {
            GridPane.setConstraints(arrnode[i2], n3 + i2, n2);
        }
    }

    static void createColumn(int n2, int n3, Node ... arrnode) {
        for (int i2 = 0; i2 < arrnode.length; ++i2) {
            GridPane.setConstraints(arrnode[i2], n2, n3 + i2);
        }
    }

    static int getNodeRowIndex(Node node) {
        Integer n2 = GridPane.getRowIndex(node);
        return n2 != null ? n2 : 0;
    }

    private static int getNodeRowSpan(Node node) {
        Integer n2 = GridPane.getRowSpan(node);
        return n2 != null ? n2 : 1;
    }

    static int getNodeRowEnd(Node node) {
        int n2 = GridPane.getNodeRowSpan(node);
        return n2 != Integer.MAX_VALUE ? GridPane.getNodeRowIndex(node) + n2 - 1 : Integer.MAX_VALUE;
    }

    static int getNodeColumnIndex(Node node) {
        Integer n2 = GridPane.getColumnIndex(node);
        return n2 != null ? n2 : 0;
    }

    private static int getNodeColumnSpan(Node node) {
        Integer n2 = GridPane.getColumnSpan(node);
        return n2 != null ? n2 : 1;
    }

    static int getNodeColumnEnd(Node node) {
        int n2 = GridPane.getNodeColumnSpan(node);
        return n2 != Integer.MAX_VALUE ? GridPane.getNodeColumnIndex(node) + n2 - 1 : Integer.MAX_VALUE;
    }

    private static Priority getNodeHgrow(Node node) {
        Priority priority = GridPane.getHgrow(node);
        return priority != null ? priority : Priority.NEVER;
    }

    private static Priority getNodeVgrow(Node node) {
        Priority priority = GridPane.getVgrow(node);
        return priority != null ? priority : Priority.NEVER;
    }

    private static Priority[] createPriorityArray(int n2, Priority priority) {
        Priority[] arrpriority = new Priority[n2];
        Arrays.fill((Object[])arrpriority, (Object)priority);
        return arrpriority;
    }

    public GridPane() {
        this.getChildren().addListener(observable -> this.requestLayout());
    }

    public final DoubleProperty hgapProperty() {
        if (this.hgap == null) {
            this.hgap = new StyleableDoubleProperty(0.0){

                @Override
                public void invalidated() {
                    GridPane.this.requestLayout();
                }

                @Override
                public CssMetaData<GridPane, Number> getCssMetaData() {
                    return StyleableProperties.HGAP;
                }

                @Override
                public Object getBean() {
                    return GridPane.this;
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
            this.vgap = new StyleableDoubleProperty(0.0){

                @Override
                public void invalidated() {
                    GridPane.this.requestLayout();
                }

                @Override
                public CssMetaData<GridPane, Number> getCssMetaData() {
                    return StyleableProperties.VGAP;
                }

                @Override
                public Object getBean() {
                    return GridPane.this;
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
                    GridPane.this.requestLayout();
                }

                @Override
                public CssMetaData<GridPane, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return GridPane.this;
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

    public final BooleanProperty gridLinesVisibleProperty() {
        if (this.gridLinesVisible == null) {
            this.gridLinesVisible = new StyleableBooleanProperty(){

                @Override
                protected void invalidated() {
                    if (this.get()) {
                        GridPane.this.gridLines = new Group();
                        GridPane.this.gridLines.setManaged(false);
                        GridPane.this.getChildren().add(GridPane.this.gridLines);
                    } else {
                        GridPane.this.getChildren().remove(GridPane.this.gridLines);
                        GridPane.this.gridLines = null;
                    }
                    GridPane.this.requestLayout();
                }

                @Override
                public CssMetaData<GridPane, Boolean> getCssMetaData() {
                    return StyleableProperties.GRID_LINES_VISIBLE;
                }

                @Override
                public Object getBean() {
                    return GridPane.this;
                }

                @Override
                public String getName() {
                    return "gridLinesVisible";
                }
            };
        }
        return this.gridLinesVisible;
    }

    public final void setGridLinesVisible(boolean bl) {
        this.gridLinesVisibleProperty().set(bl);
    }

    public final boolean isGridLinesVisible() {
        return this.gridLinesVisible == null ? false : this.gridLinesVisible.get();
    }

    public final ObservableList<RowConstraints> getRowConstraints() {
        return this.rowConstraints;
    }

    public final ObservableList<ColumnConstraints> getColumnConstraints() {
        return this.columnConstraints;
    }

    public void add(Node node, int n2, int n3) {
        GridPane.setConstraints(node, n2, n3);
        this.getChildren().add(node);
    }

    public void add(Node node, int n2, int n3, int n4, int n5) {
        GridPane.setConstraints(node, n2, n3, n4, n5);
        this.getChildren().add(node);
    }

    public void addRow(int n2, Node ... arrnode) {
        int n3 = 0;
        List list = this.getManagedChildren();
        int n4 = list.size();
        for (int i2 = 0; i2 < n4; ++i2) {
            Node node = (Node)list.get(i2);
            int n5 = GridPane.getNodeRowIndex(node);
            int n6 = GridPane.getNodeRowEnd(node);
            if (n2 < n5 || n2 > n6 && n6 != Integer.MAX_VALUE) continue;
            int n7 = GridPane.getNodeColumnIndex(node);
            int n8 = GridPane.getNodeColumnEnd(node);
            n3 = Math.max(n3, (n8 != Integer.MAX_VALUE ? n8 : n7) + 1);
        }
        GridPane.createRow(n2, n3, arrnode);
        this.getChildren().addAll(arrnode);
    }

    public void addColumn(int n2, Node ... arrnode) {
        int n3 = 0;
        List list = this.getManagedChildren();
        int n4 = list.size();
        for (int i2 = 0; i2 < n4; ++i2) {
            Node node = (Node)list.get(i2);
            int n5 = GridPane.getNodeColumnIndex(node);
            int n6 = GridPane.getNodeColumnEnd(node);
            if (n2 < n5 || n2 > n6 && n6 != Integer.MAX_VALUE) continue;
            int n7 = GridPane.getNodeRowIndex(node);
            int n8 = GridPane.getNodeRowEnd(node);
            n3 = Math.max(n3, (n8 != Integer.MAX_VALUE ? n8 : n7) + 1);
        }
        GridPane.createColumn(n2, n3, arrnode);
        this.getChildren().addAll(arrnode);
    }

    private int getNumberOfRows() {
        this.computeGridMetrics();
        return this.numRows;
    }

    private int getNumberOfColumns() {
        this.computeGridMetrics();
        return this.numColumns;
    }

    private boolean isNodePositionedByBaseline(Node node) {
        return this.getRowValignment(GridPane.getNodeRowIndex(node)) == VPos.BASELINE && GridPane.getValignment(node) == null || GridPane.getValignment(node) == VPos.BASELINE;
    }

    private void computeGridMetrics() {
        if (this.metricsDirty) {
            int n2;
            int n3;
            Object object;
            int n4;
            this.numRows = this.rowConstraints.size();
            this.numColumns = this.columnConstraints.size();
            List list = this.getManagedChildren();
            int n5 = list.size();
            for (n4 = 0; n4 < n5; ++n4) {
                object = (Node)list.get(n4);
                n3 = GridPane.getNodeRowIndex((Node)object);
                n2 = GridPane.getNodeColumnIndex((Node)object);
                int n6 = GridPane.getNodeRowEnd((Node)object);
                int n7 = GridPane.getNodeColumnEnd((Node)object);
                this.numRows = Math.max(this.numRows, (n6 != Integer.MAX_VALUE ? n6 : n3) + 1);
                this.numColumns = Math.max(this.numColumns, (n7 != Integer.MAX_VALUE ? n7 : n2) + 1);
            }
            this.rowPercentHeight = GridPane.createDoubleArray(this.numRows, -1.0);
            this.rowPercentTotal = 0.0;
            this.columnPercentWidth = GridPane.createDoubleArray(this.numColumns, -1.0);
            this.columnPercentTotal = 0.0;
            this.columnGrow = GridPane.createPriorityArray(this.numColumns, Priority.NEVER);
            this.rowGrow = GridPane.createPriorityArray(this.numRows, Priority.NEVER);
            this.rowMinBaselineComplement = GridPane.createDoubleArray(this.numRows, -1.0);
            this.rowPrefBaselineComplement = GridPane.createDoubleArray(this.numRows, -1.0);
            this.rowMaxBaselineComplement = GridPane.createDoubleArray(this.numRows, -1.0);
            this.rowBaseline = new List[this.numRows];
            n5 = this.numRows;
            for (n4 = 0; n4 < n5; ++n4) {
                Object object2;
                if (n4 < this.rowConstraints.size()) {
                    object = (RowConstraints)this.rowConstraints.get(n4);
                    double d2 = ((RowConstraints)object).getPercentHeight();
                    object2 = ((RowConstraints)object).getVgrow();
                    if (d2 >= 0.0) {
                        this.rowPercentHeight[n4] = d2;
                    }
                    if (object2 != null) {
                        this.rowGrow[n4] = object2;
                    }
                }
                object = new ArrayList(this.numColumns);
                n2 = list.size();
                for (n3 = 0; n3 < n2; ++n3) {
                    object2 = (Node)list.get(n3);
                    if (GridPane.getNodeRowIndex((Node)object2) != n4 || !this.isNodePositionedByBaseline((Node)object2)) continue;
                    object.add(object2);
                }
                this.rowMinBaselineComplement[n4] = GridPane.getMinBaselineComplement((List<Node>)object);
                this.rowPrefBaselineComplement[n4] = GridPane.getPrefBaselineComplement((List<Node>)object);
                this.rowMaxBaselineComplement[n4] = GridPane.getMaxBaselineComplement((List<Node>)object);
                this.rowBaseline[n4] = object;
            }
            n5 = Math.min(this.numColumns, this.columnConstraints.size());
            for (n4 = 0; n4 < n5; ++n4) {
                object = (ColumnConstraints)this.columnConstraints.get(n4);
                double d3 = ((ColumnConstraints)object).getPercentWidth();
                Priority priority = ((ColumnConstraints)object).getHgrow();
                if (d3 >= 0.0) {
                    this.columnPercentWidth[n4] = d3;
                }
                if (priority == null) continue;
                this.columnGrow[n4] = priority;
            }
            n5 = list.size();
            for (n4 = 0; n4 < n5; ++n4) {
                Priority priority;
                object = (Node)list.get(n4);
                if (GridPane.getNodeColumnSpan((Node)object) == 1) {
                    priority = GridPane.getNodeHgrow((Node)object);
                    n2 = GridPane.getNodeColumnIndex((Node)object);
                    this.columnGrow[n2] = Priority.max(this.columnGrow[n2], priority);
                }
                if (GridPane.getNodeRowSpan((Node)object) != 1) continue;
                priority = GridPane.getNodeVgrow((Node)object);
                n2 = GridPane.getNodeRowIndex((Node)object);
                this.rowGrow[n2] = Priority.max(this.rowGrow[n2], priority);
            }
            for (n4 = 0; n4 < this.rowPercentHeight.length; ++n4) {
                if (!(this.rowPercentHeight[n4] > 0.0)) continue;
                this.rowPercentTotal += this.rowPercentHeight[n4];
            }
            if (this.rowPercentTotal > 100.0) {
                double d4 = 100.0 / this.rowPercentTotal;
                for (int i2 = 0; i2 < this.rowPercentHeight.length; ++i2) {
                    if (!(this.rowPercentHeight[i2] > 0.0)) continue;
                    int n8 = i2;
                    this.rowPercentHeight[n8] = this.rowPercentHeight[n8] * d4;
                }
                this.rowPercentTotal = 100.0;
            }
            for (int i3 = 0; i3 < this.columnPercentWidth.length; ++i3) {
                if (!(this.columnPercentWidth[i3] > 0.0)) continue;
                this.columnPercentTotal += this.columnPercentWidth[i3];
            }
            if (this.columnPercentTotal > 100.0) {
                double d5 = 100.0 / this.columnPercentTotal;
                for (int i4 = 0; i4 < this.columnPercentWidth.length; ++i4) {
                    if (!(this.columnPercentWidth[i4] > 0.0)) continue;
                    int n9 = i4;
                    this.columnPercentWidth[n9] = this.columnPercentWidth[n9] * d5;
                }
                this.columnPercentTotal = 100.0;
            }
            this.bias = null;
            for (int i5 = 0; i5 < list.size(); ++i5) {
                Orientation orientation = ((Node)list.get(i5)).getContentBias();
                if (orientation == null) continue;
                this.bias = orientation;
                if (orientation == Orientation.HORIZONTAL) break;
            }
            this.metricsDirty = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected double computeMinWidth(double d2) {
        this.computeGridMetrics();
        this.performingLayout = true;
        try {
            double[] arrd = d2 == -1.0 ? null : this.computeHeightsToFit(d2).asArray();
            double d3 = this.snapSpace(this.getInsets().getLeft()) + this.computeMinWidths(arrd).computeTotalWithMultiSize() + this.snapSpace(this.getInsets().getRight());
            return d3;
        }
        finally {
            this.performingLayout = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected double computeMinHeight(double d2) {
        this.computeGridMetrics();
        this.performingLayout = true;
        try {
            double[] arrd = d2 == -1.0 ? null : this.computeWidthsToFit(d2).asArray();
            double d3 = this.snapSpace(this.getInsets().getTop()) + this.computeMinHeights(arrd).computeTotalWithMultiSize() + this.snapSpace(this.getInsets().getBottom());
            return d3;
        }
        finally {
            this.performingLayout = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected double computePrefWidth(double d2) {
        this.computeGridMetrics();
        this.performingLayout = true;
        try {
            double[] arrd = d2 == -1.0 ? null : this.computeHeightsToFit(d2).asArray();
            double d3 = this.snapSpace(this.getInsets().getLeft()) + this.computePrefWidths(arrd).computeTotalWithMultiSize() + this.snapSpace(this.getInsets().getRight());
            return d3;
        }
        finally {
            this.performingLayout = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected double computePrefHeight(double d2) {
        this.computeGridMetrics();
        this.performingLayout = true;
        try {
            double[] arrd = d2 == -1.0 ? null : this.computeWidthsToFit(d2).asArray();
            double d3 = this.snapSpace(this.getInsets().getTop()) + this.computePrefHeights(arrd).computeTotalWithMultiSize() + this.snapSpace(this.getInsets().getBottom());
            return d3;
        }
        finally {
            this.performingLayout = false;
        }
    }

    private VPos getRowValignment(int n2) {
        RowConstraints rowConstraints;
        if (n2 < this.getRowConstraints().size() && (rowConstraints = (RowConstraints)this.getRowConstraints().get(n2)).getValignment() != null) {
            return rowConstraints.getValignment();
        }
        return VPos.CENTER;
    }

    private HPos getColumnHalignment(int n2) {
        ColumnConstraints columnConstraints;
        if (n2 < this.getColumnConstraints().size() && (columnConstraints = (ColumnConstraints)this.getColumnConstraints().get(n2)).getHalignment() != null) {
            return columnConstraints.getHalignment();
        }
        return HPos.LEFT;
    }

    private double getColumnMinWidth(int n2) {
        if (n2 < this.getColumnConstraints().size()) {
            ColumnConstraints columnConstraints = (ColumnConstraints)this.getColumnConstraints().get(n2);
            return columnConstraints.getMinWidth();
        }
        return -1.0;
    }

    private double getRowMinHeight(int n2) {
        if (n2 < this.getRowConstraints().size()) {
            RowConstraints rowConstraints = (RowConstraints)this.getRowConstraints().get(n2);
            return rowConstraints.getMinHeight();
        }
        return -1.0;
    }

    private double getColumnMaxWidth(int n2) {
        if (n2 < this.getColumnConstraints().size()) {
            ColumnConstraints columnConstraints = (ColumnConstraints)this.getColumnConstraints().get(n2);
            return columnConstraints.getMaxWidth();
        }
        return -1.0;
    }

    private double getColumnPrefWidth(int n2) {
        if (n2 < this.getColumnConstraints().size()) {
            ColumnConstraints columnConstraints = (ColumnConstraints)this.getColumnConstraints().get(n2);
            return columnConstraints.getPrefWidth();
        }
        return -1.0;
    }

    private double getRowPrefHeight(int n2) {
        if (n2 < this.getRowConstraints().size()) {
            RowConstraints rowConstraints = (RowConstraints)this.getRowConstraints().get(n2);
            return rowConstraints.getPrefHeight();
        }
        return -1.0;
    }

    private double getRowMaxHeight(int n2) {
        if (n2 < this.getRowConstraints().size()) {
            RowConstraints rowConstraints = (RowConstraints)this.getRowConstraints().get(n2);
            return rowConstraints.getMaxHeight();
        }
        return -1.0;
    }

    private boolean shouldRowFillHeight(int n2) {
        if (n2 < this.getRowConstraints().size()) {
            return ((RowConstraints)this.getRowConstraints().get(n2)).isFillHeight();
        }
        return true;
    }

    private boolean shouldColumnFillWidth(int n2) {
        if (n2 < this.getColumnConstraints().size()) {
            return ((ColumnConstraints)this.getColumnConstraints().get(n2)).isFillWidth();
        }
        return true;
    }

    private double getTotalWidthOfNodeColumns(Node node, double[] arrd) {
        if (GridPane.getNodeColumnSpan(node) == 1) {
            return arrd[GridPane.getNodeColumnIndex(node)];
        }
        double d2 = 0.0;
        int n2 = this.getNodeColumnEndConvertRemaining(node);
        for (int i2 = GridPane.getNodeColumnIndex(node); i2 <= n2; ++i2) {
            d2 += arrd[i2];
        }
        return d2;
    }

    private CompositeSize computeMaxHeights() {
        if (this.rowMaxHeight == null) {
            this.rowMaxHeight = this.createCompositeRows(Double.MAX_VALUE);
            ObservableList<RowConstraints> observableList = this.getRowConstraints();
            CompositeSize compositeSize = null;
            for (int i2 = 0; i2 < observableList.size(); ++i2) {
                RowConstraints rowConstraints = (RowConstraints)observableList.get(i2);
                double d2 = this.snapSize(rowConstraints.getMaxHeight());
                if (d2 == Double.NEGATIVE_INFINITY) {
                    if (compositeSize == null) {
                        compositeSize = this.computePrefHeights(null);
                    }
                    this.rowMaxHeight.setPresetSize(i2, compositeSize.getSize(i2));
                    continue;
                }
                if (d2 == -1.0) continue;
                double d3 = this.snapSize(rowConstraints.getMinHeight());
                if (d3 >= 0.0) {
                    this.rowMaxHeight.setPresetSize(i2, GridPane.boundedSize(d3, d2, d2));
                    continue;
                }
                this.rowMaxHeight.setPresetSize(i2, d2);
            }
        }
        return this.rowMaxHeight;
    }

    private CompositeSize computePrefHeights(double[] arrd) {
        double d2;
        CompositeSize compositeSize;
        if (arrd == null) {
            if (this.rowPrefHeight != null) {
                return this.rowPrefHeight;
            }
            compositeSize = this.rowPrefHeight = this.createCompositeRows(0.0);
        } else {
            compositeSize = this.createCompositeRows(0.0);
        }
        ObservableList<RowConstraints> observableList = this.getRowConstraints();
        for (int i2 = 0; i2 < observableList.size(); ++i2) {
            RowConstraints rowConstraints = (RowConstraints)observableList.get(i2);
            double d3 = this.snapSize(rowConstraints.getPrefHeight());
            double d4 = this.snapSize(rowConstraints.getMinHeight());
            if (d3 != -1.0) {
                d2 = this.snapSize(rowConstraints.getMaxHeight());
                if (d4 >= 0.0 || d2 >= 0.0) {
                    compositeSize.setPresetSize(i2, GridPane.boundedSize(d4 < 0.0 ? 0.0 : d4, d3, d2 < 0.0 ? Double.POSITIVE_INFINITY : d2));
                    continue;
                }
                compositeSize.setPresetSize(i2, d3);
                continue;
            }
            if (!(d4 > 0.0)) continue;
            compositeSize.setSize(i2, d4);
        }
        List list = this.getManagedChildren();
        int n2 = list.size();
        for (int i3 = 0; i3 < n2; ++i3) {
            Node node = (Node)list.get(i3);
            int n3 = GridPane.getNodeRowIndex(node);
            int n4 = this.getNodeRowEndConvertRemaining(node);
            d2 = this.computeChildPrefAreaHeight(node, this.isNodePositionedByBaseline(node) ? this.rowPrefBaselineComplement[n3] : -1.0, GridPane.getMargin(node), arrd == null ? -1.0 : this.getTotalWidthOfNodeColumns(node, arrd));
            if (n3 == n4 && !compositeSize.isPreset(n3)) {
                double d5 = this.getRowMinHeight(n3);
                double d6 = this.getRowMaxHeight(n3);
                compositeSize.setMaxSize(n3, GridPane.boundedSize(d5 < 0.0 ? 0.0 : d5, d2, d6 < 0.0 ? Double.MAX_VALUE : d6));
                continue;
            }
            if (n3 == n4) continue;
            compositeSize.setMaxMultiSize(n3, n4 + 1, d2);
        }
        return compositeSize;
    }

    private CompositeSize computeMinHeights(double[] arrd) {
        CompositeSize compositeSize;
        if (arrd == null) {
            if (this.rowMinHeight != null) {
                return this.rowMinHeight;
            }
            compositeSize = this.rowMinHeight = this.createCompositeRows(0.0);
        } else {
            compositeSize = this.createCompositeRows(0.0);
        }
        ObservableList<RowConstraints> observableList = this.getRowConstraints();
        CompositeSize compositeSize2 = null;
        for (int i2 = 0; i2 < observableList.size(); ++i2) {
            double d2 = this.snapSize(((RowConstraints)observableList.get(i2)).getMinHeight());
            if (d2 == Double.NEGATIVE_INFINITY) {
                if (compositeSize2 == null) {
                    compositeSize2 = this.computePrefHeights(arrd);
                }
                compositeSize.setPresetSize(i2, compositeSize2.getSize(i2));
                continue;
            }
            if (d2 == -1.0) continue;
            compositeSize.setPresetSize(i2, d2);
        }
        List list = this.getManagedChildren();
        int n2 = list.size();
        for (int i3 = 0; i3 < n2; ++i3) {
            Node node = (Node)list.get(i3);
            int n3 = GridPane.getNodeRowIndex(node);
            int n4 = this.getNodeRowEndConvertRemaining(node);
            double d3 = this.computeChildMinAreaHeight(node, this.isNodePositionedByBaseline(node) ? this.rowMinBaselineComplement[n3] : -1.0, GridPane.getMargin(node), arrd == null ? -1.0 : this.getTotalWidthOfNodeColumns(node, arrd));
            if (n3 == n4 && !compositeSize.isPreset(n3)) {
                compositeSize.setMaxSize(n3, d3);
                continue;
            }
            if (n3 == n4) continue;
            compositeSize.setMaxMultiSize(n3, n4 + 1, d3);
        }
        return compositeSize;
    }

    private double getTotalHeightOfNodeRows(Node node, double[] arrd) {
        if (GridPane.getNodeRowSpan(node) == 1) {
            return arrd[GridPane.getNodeRowIndex(node)];
        }
        double d2 = 0.0;
        int n2 = this.getNodeRowEndConvertRemaining(node);
        for (int i2 = GridPane.getNodeRowIndex(node); i2 <= n2; ++i2) {
            d2 += arrd[i2];
        }
        return d2;
    }

    private CompositeSize computeMaxWidths() {
        if (this.columnMaxWidth == null) {
            this.columnMaxWidth = this.createCompositeColumns(Double.MAX_VALUE);
            ObservableList<ColumnConstraints> observableList = this.getColumnConstraints();
            CompositeSize compositeSize = null;
            for (int i2 = 0; i2 < observableList.size(); ++i2) {
                ColumnConstraints columnConstraints = (ColumnConstraints)observableList.get(i2);
                double d2 = this.snapSize(columnConstraints.getMaxWidth());
                if (d2 == Double.NEGATIVE_INFINITY) {
                    if (compositeSize == null) {
                        compositeSize = this.computePrefWidths(null);
                    }
                    this.columnMaxWidth.setPresetSize(i2, compositeSize.getSize(i2));
                    continue;
                }
                if (d2 == -1.0) continue;
                double d3 = this.snapSize(columnConstraints.getMinWidth());
                if (d3 >= 0.0) {
                    this.columnMaxWidth.setPresetSize(i2, GridPane.boundedSize(d3, d2, d2));
                    continue;
                }
                this.columnMaxWidth.setPresetSize(i2, d2);
            }
        }
        return this.columnMaxWidth;
    }

    private CompositeSize computePrefWidths(double[] arrd) {
        double d2;
        CompositeSize compositeSize;
        if (arrd == null) {
            if (this.columnPrefWidth != null) {
                return this.columnPrefWidth;
            }
            compositeSize = this.columnPrefWidth = this.createCompositeColumns(0.0);
        } else {
            compositeSize = this.createCompositeColumns(0.0);
        }
        ObservableList<ColumnConstraints> observableList = this.getColumnConstraints();
        for (int i2 = 0; i2 < observableList.size(); ++i2) {
            ColumnConstraints columnConstraints = (ColumnConstraints)observableList.get(i2);
            double d3 = this.snapSize(columnConstraints.getPrefWidth());
            double d4 = this.snapSize(columnConstraints.getMinWidth());
            if (d3 != -1.0) {
                d2 = this.snapSize(columnConstraints.getMaxWidth());
                if (d4 >= 0.0 || d2 >= 0.0) {
                    compositeSize.setPresetSize(i2, GridPane.boundedSize(d4 < 0.0 ? 0.0 : d4, d3, d2 < 0.0 ? Double.POSITIVE_INFINITY : d2));
                    continue;
                }
                compositeSize.setPresetSize(i2, d3);
                continue;
            }
            if (!(d4 > 0.0)) continue;
            compositeSize.setSize(i2, d4);
        }
        List list = this.getManagedChildren();
        int n2 = list.size();
        for (int i3 = 0; i3 < n2; ++i3) {
            int n3;
            Node node = (Node)list.get(i3);
            int n4 = GridPane.getNodeColumnIndex(node);
            if (n4 == (n3 = this.getNodeColumnEndConvertRemaining(node)) && !compositeSize.isPreset(n4)) {
                d2 = this.getColumnMinWidth(n4);
                double d5 = this.getColumnMaxWidth(n4);
                compositeSize.setMaxSize(n4, GridPane.boundedSize(d2 < 0.0 ? 0.0 : d2, this.computeChildPrefAreaWidth(node, this.getBaselineComplementForChild(node), GridPane.getMargin(node), arrd == null ? -1.0 : this.getTotalHeightOfNodeRows(node, arrd), false), d5 < 0.0 ? Double.MAX_VALUE : d5));
                continue;
            }
            if (n4 == n3) continue;
            compositeSize.setMaxMultiSize(n4, n3 + 1, this.computeChildPrefAreaWidth(node, this.getBaselineComplementForChild(node), GridPane.getMargin(node), arrd == null ? -1.0 : this.getTotalHeightOfNodeRows(node, arrd), false));
        }
        return compositeSize;
    }

    private CompositeSize computeMinWidths(double[] arrd) {
        CompositeSize compositeSize;
        if (arrd == null) {
            if (this.columnMinWidth != null) {
                return this.columnMinWidth;
            }
            compositeSize = this.columnMinWidth = this.createCompositeColumns(0.0);
        } else {
            compositeSize = this.createCompositeColumns(0.0);
        }
        ObservableList<ColumnConstraints> observableList = this.getColumnConstraints();
        CompositeSize compositeSize2 = null;
        for (int i2 = 0; i2 < observableList.size(); ++i2) {
            double d2 = this.snapSize(((ColumnConstraints)observableList.get(i2)).getMinWidth());
            if (d2 == Double.NEGATIVE_INFINITY) {
                if (compositeSize2 == null) {
                    compositeSize2 = this.computePrefWidths(arrd);
                }
                compositeSize.setPresetSize(i2, compositeSize2.getSize(i2));
                continue;
            }
            if (d2 == -1.0) continue;
            compositeSize.setPresetSize(i2, d2);
        }
        List list = this.getManagedChildren();
        int n2 = list.size();
        for (int i3 = 0; i3 < n2; ++i3) {
            int n3;
            Node node = (Node)list.get(i3);
            int n4 = GridPane.getNodeColumnIndex(node);
            if (n4 == (n3 = this.getNodeColumnEndConvertRemaining(node)) && !compositeSize.isPreset(n4)) {
                compositeSize.setMaxSize(n4, this.computeChildMinAreaWidth(node, this.getBaselineComplementForChild(node), GridPane.getMargin(node), arrd == null ? -1.0 : this.getTotalHeightOfNodeRows(node, arrd), false));
                continue;
            }
            if (n4 == n3) continue;
            compositeSize.setMaxMultiSize(n4, n3 + 1, this.computeChildMinAreaWidth(node, this.getBaselineComplementForChild(node), GridPane.getMargin(node), arrd == null ? -1.0 : this.getTotalHeightOfNodeRows(node, arrd), false));
        }
        return compositeSize;
    }

    private CompositeSize computeHeightsToFit(double d2) {
        assert (d2 != -1.0);
        CompositeSize compositeSize = this.rowPercentTotal == 100.0 ? this.createCompositeRows(0.0) : (CompositeSize)this.computePrefHeights(null).clone();
        this.adjustRowHeights(compositeSize, d2);
        return compositeSize;
    }

    private CompositeSize computeWidthsToFit(double d2) {
        assert (d2 != -1.0);
        CompositeSize compositeSize = this.columnPercentTotal == 100.0 ? this.createCompositeColumns(0.0) : (CompositeSize)this.computePrefWidths(null).clone();
        this.adjustColumnWidths(compositeSize, d2);
        return compositeSize;
    }

    @Override
    public Orientation getContentBias() {
        this.computeGridMetrics();
        return this.bias;
    }

    @Override
    public void requestLayout() {
        if (this.performingLayout) {
            return;
        }
        if (this.metricsDirty) {
            super.requestLayout();
            return;
        }
        this.metricsDirty = true;
        this.bias = null;
        this.rowGrow = null;
        this.rowMaxHeight = null;
        this.rowPrefHeight = null;
        this.rowMinHeight = null;
        this.columnGrow = null;
        this.columnMaxWidth = null;
        this.columnPrefWidth = null;
        this.columnMinWidth = null;
        this.rowMaxBaselineComplement = null;
        this.rowPrefBaselineComplement = null;
        this.rowMinBaselineComplement = null;
        super.requestLayout();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void layoutChildren() {
        this.performingLayout = true;
        try {
            double d2;
            double d3;
            CompositeSize compositeSize;
            CompositeSize compositeSize2;
            double d4 = this.snapSpace(this.getHgap());
            double d5 = this.snapSpace(this.getVgap());
            double d6 = this.snapSpace(this.getInsets().getTop());
            double d7 = this.snapSpace(this.getInsets().getBottom());
            double d8 = this.snapSpace(this.getInsets().getLeft());
            double d9 = this.snapSpace(this.getInsets().getRight());
            double d10 = this.getWidth();
            double d11 = this.getHeight();
            double d12 = d11 - d6 - d7;
            double d13 = d10 - d8 - d9;
            this.computeGridMetrics();
            Orientation orientation = this.getContentBias();
            if (orientation == null) {
                compositeSize2 = (CompositeSize)this.computePrefHeights(null).clone();
                compositeSize = (CompositeSize)this.computePrefWidths(null).clone();
                d3 = this.adjustRowHeights(compositeSize2, d11);
                d2 = this.adjustColumnWidths(compositeSize, d10);
            } else if (orientation == Orientation.HORIZONTAL) {
                compositeSize = (CompositeSize)this.computePrefWidths(null).clone();
                d2 = this.adjustColumnWidths(compositeSize, d10);
                compositeSize2 = this.computePrefHeights(compositeSize.asArray());
                d3 = this.adjustRowHeights(compositeSize2, d11);
            } else {
                compositeSize2 = (CompositeSize)this.computePrefHeights(null).clone();
                d3 = this.adjustRowHeights(compositeSize2, d11);
                compositeSize = this.computePrefWidths(compositeSize2.asArray());
                d2 = this.adjustColumnWidths(compositeSize, d10);
            }
            double d14 = d8 + GridPane.computeXOffset(d13, d2, this.getAlignmentInternal().getHpos());
            double d15 = d6 + GridPane.computeYOffset(d12, d3, this.getAlignmentInternal().getVpos());
            List list = this.getManagedChildren();
            double[] arrd = GridPane.createDoubleArray(this.numRows, -1.0);
            int n4 = list.size();
            for (int i2 = 0; i2 < n4; ++i2) {
                int n5;
                Node node = (Node)list.get(i2);
                int n6 = GridPane.getNodeRowIndex(node);
                int n7 = GridPane.getNodeColumnIndex(node);
                int n8 = GridPane.getNodeColumnSpan(node);
                if (n8 == Integer.MAX_VALUE) {
                    n8 = compositeSize.getLength() - n7;
                }
                if ((n5 = GridPane.getNodeRowSpan(node)) == Integer.MAX_VALUE) {
                    n5 = compositeSize2.getLength() - n6;
                }
                double d16 = d14;
                for (int i3 = 0; i3 < n7; ++i3) {
                    d16 += compositeSize.getSize(i3) + d4;
                }
                double d17 = d15;
                for (int i4 = 0; i4 < n6; ++i4) {
                    d17 += compositeSize2.getSize(i4) + d5;
                }
                double d18 = compositeSize.getSize(n7);
                for (int i5 = 2; i5 <= n8; ++i5) {
                    d18 += compositeSize.getSize(n7 + i5 - 1) + d4;
                }
                double d19 = compositeSize2.getSize(n6);
                for (int i6 = 2; i6 <= n5; ++i6) {
                    d19 += compositeSize2.getSize(n6 + i6 - 1) + d5;
                }
                HPos hPos = GridPane.getHalignment(node);
                VPos vPos = GridPane.getValignment(node);
                Boolean bl = GridPane.isFillWidth(node);
                Boolean bl2 = GridPane.isFillHeight(node);
                if (hPos == null) {
                    hPos = this.getColumnHalignment(n7);
                }
                if (vPos == null) {
                    vPos = this.getRowValignment(n6);
                }
                if (bl == null) {
                    bl = this.shouldColumnFillWidth(n7);
                }
                if (bl2 == null) {
                    bl2 = this.shouldRowFillHeight(n6);
                }
                double d20 = 0.0;
                if (vPos == VPos.BASELINE) {
                    if (arrd[n6] == -1.0) {
                        arrd[n6] = this.getAreaBaselineOffset(this.rowBaseline[n6], marginAccessor, (Integer n3) -> {
                            Node node = this.rowBaseline[n6].get((int)n3);
                            int n4 = GridPane.getNodeColumnIndex(node);
                            int n5 = GridPane.getNodeColumnSpan(node);
                            if (n5 == Integer.MAX_VALUE) {
                                n5 = compositeSize.getLength() - n4;
                            }
                            double d3 = compositeSize.getSize(n4);
                            for (int i2 = 2; i2 <= n5; ++i2) {
                                d3 += compositeSize.getSize(n4 + i2 - 1) + d4;
                            }
                            return d3;
                        }, d19, n2 -> {
                            Boolean bl = GridPane.isFillHeight(node);
                            if (bl != null) {
                                return bl;
                            }
                            return this.shouldRowFillHeight(GridPane.getNodeRowIndex(node));
                        }, this.rowMinBaselineComplement[n6]);
                    }
                    d20 = arrd[n6];
                }
                Insets insets = GridPane.getMargin(node);
                this.layoutInArea(node, d16, d17, d18, d19, d20, insets, bl, bl2, hPos, vPos);
            }
            this.layoutGridLines(compositeSize, compositeSize2, d14, d15, d3, d2);
            this.currentHeights = compositeSize2;
            this.currentWidths = compositeSize;
        }
        finally {
            this.performingLayout = false;
        }
    }

    private double adjustRowHeights(CompositeSize compositeSize, double d2) {
        double d3;
        double d4;
        assert (d2 != -1.0);
        double d5 = this.snapSpace(this.getVgap());
        double d6 = this.snapSpace(this.getInsets().getTop());
        double d7 = this.snapSpace(this.getInsets().getBottom());
        double d8 = d5 * (double)(this.getNumberOfRows() - 1);
        double d9 = d2 - d6 - d7;
        if (this.rowPercentTotal > 0.0) {
            d4 = 0.0;
            for (int i2 = 0; i2 < this.rowPercentHeight.length; ++i2) {
                if (!(this.rowPercentHeight[i2] >= 0.0)) continue;
                double d10 = (d9 - d8) * (this.rowPercentHeight[i2] / 100.0);
                double d11 = Math.floor(d10);
                d4 += d10 - d11;
                d10 = d11;
                if (d4 >= 0.5) {
                    d10 += 1.0;
                    d4 = -1.0 + d4;
                }
                compositeSize.setSize(i2, d10);
            }
        }
        d4 = compositeSize.computeTotal();
        if (this.rowPercentTotal < 100.0 && (d3 = d2 - d6 - d7 - d4) != 0.0) {
            double d12 = this.growToMultiSpanPreferredHeights(compositeSize, d3);
            d12 = this.growOrShrinkRowHeights(compositeSize, Priority.ALWAYS, d12);
            d12 = this.growOrShrinkRowHeights(compositeSize, Priority.SOMETIMES, d12);
            d4 += d3 - d12;
        }
        return d4;
    }

    private double growToMultiSpanPreferredHeights(CompositeSize compositeSize, double d2) {
        double d3;
        double d4;
        double d5;
        int n2;
        double d6;
        double d7;
        double d8;
        int n3;
        Iterator iterator;
        if (d2 <= 0.0) {
            return d2;
        }
        TreeSet<Integer> treeSet = new TreeSet<Integer>();
        TreeSet<Integer> treeSet2 = new TreeSet<Integer>();
        TreeSet<Integer> treeSet3 = new TreeSet<Integer>();
        for (Map.Entry entry : compositeSize.multiSizes()) {
            Interval interval = (Interval)entry.getKey();
            block5: for (int i2 = interval.begin; i2 < interval.end; ++i2) {
                if (!(this.rowPercentHeight[i2] < 0.0)) continue;
                switch (this.rowGrow[i2]) {
                    case ALWAYS: {
                        treeSet.add(i2);
                        continue block5;
                    }
                    case SOMETIMES: {
                        treeSet2.add(i2);
                    }
                }
            }
            if (!(this.rowPercentHeight[interval.end - 1] < 0.0)) continue;
            treeSet3.add(interval.end - 1);
        }
        double d9 = d2;
        while (treeSet.size() > 0 && d9 > (double)treeSet.size()) {
            double d10 = Math.floor(d9 / (double)treeSet.size());
            iterator = treeSet.iterator();
            while (iterator.hasNext()) {
                n3 = (Integer)iterator.next();
                d8 = this.getRowMaxHeight(n3);
                d7 = this.getRowPrefHeight(n3);
                d6 = d10;
                for (Map.Entry entry : compositeSize.multiSizes()) {
                    Interval interval = (Interval)entry.getKey();
                    if (!interval.contains(n3)) continue;
                    n2 = 0;
                    for (int i3 = interval.begin; i3 < interval.end; ++i3) {
                        if (!treeSet.contains(i3)) continue;
                        ++n2;
                    }
                    d5 = compositeSize.computeTotal(interval.begin, interval.end);
                    d6 = Math.min(Math.floor(Math.max(0.0, ((Double)entry.getValue() - d5) / (double)n2)), d6);
                }
                d4 = compositeSize.getSize(n3);
                d3 = d8 >= 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d8) : (d8 == Double.NEGATIVE_INFINITY && d7 > 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d7) : d4 + d6);
                d5 = d3 - d4;
                d9 -= d5;
                if (d5 != d6 || d5 == 0.0) {
                    iterator.remove();
                }
                compositeSize.setSize(n3, d3);
            }
        }
        while (treeSet2.size() > 0 && d9 > (double)treeSet2.size()) {
            double d11 = Math.floor(d9 / (double)treeSet2.size());
            iterator = treeSet2.iterator();
            while (iterator.hasNext()) {
                n3 = (Integer)iterator.next();
                d8 = this.getRowMaxHeight(n3);
                d7 = this.getRowPrefHeight(n3);
                d6 = d11;
                for (Map.Entry entry : compositeSize.multiSizes()) {
                    Interval interval = (Interval)entry.getKey();
                    if (!interval.contains(n3)) continue;
                    n2 = 0;
                    for (int i4 = interval.begin; i4 < interval.end; ++i4) {
                        if (!treeSet2.contains(i4)) continue;
                        ++n2;
                    }
                    d5 = compositeSize.computeTotal(interval.begin, interval.end);
                    d6 = Math.min(Math.floor(Math.max(0.0, ((Double)entry.getValue() - d5) / (double)n2)), d6);
                }
                d4 = compositeSize.getSize(n3);
                d3 = d8 >= 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d8) : (d8 == Double.NEGATIVE_INFINITY && d7 > 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d7) : d4 + d6);
                d5 = d3 - d4;
                d9 -= d5;
                if (d5 != d6 || d5 == 0.0) {
                    iterator.remove();
                }
                compositeSize.setSize(n3, d3);
            }
        }
        while (treeSet3.size() > 0 && d9 > (double)treeSet3.size()) {
            double d12 = Math.floor(d9 / (double)treeSet3.size());
            iterator = treeSet3.iterator();
            while (iterator.hasNext()) {
                n3 = (Integer)iterator.next();
                d8 = this.getRowMaxHeight(n3);
                d7 = this.getRowPrefHeight(n3);
                d6 = d12;
                for (Map.Entry entry : compositeSize.multiSizes()) {
                    Interval interval = (Interval)entry.getKey();
                    if (interval.end - 1 != n3) continue;
                    double d13 = compositeSize.computeTotal(interval.begin, interval.end);
                    d6 = Math.min(Math.max(0.0, (Double)entry.getValue() - d13), d6);
                }
                d4 = compositeSize.getSize(n3);
                d3 = d8 >= 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d8) : (d8 == Double.NEGATIVE_INFINITY && d7 > 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d7) : d4 + d6);
                d5 = d3 - d4;
                d9 -= d5;
                if (d5 != d6 || d5 == 0.0) {
                    iterator.remove();
                }
                compositeSize.setSize(n3, d3);
            }
        }
        return d9;
    }

    private double growOrShrinkRowHeights(CompositeSize compositeSize, Priority priority, double d2) {
        CompositeSize compositeSize2;
        boolean bl;
        boolean bl2 = d2 < 0.0;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i2 = 0; i2 < this.rowGrow.length; ++i2) {
            if (!(this.rowPercentHeight[i2] < 0.0) || !bl2 && this.rowGrow[i2] != priority) continue;
            arrayList.add(i2);
        }
        double d3 = d2;
        boolean bl3 = false;
        double d4 = 0.0;
        boolean bl4 = bl = d3 >= 0.0;
        CompositeSize compositeSize3 = compositeSize2 = bl2 ? this.computeMinHeights(null) : this.computeMaxHeights();
        block1: while (d3 != 0.0 && bl == bl4 && arrayList.size() > 0) {
            if (!bl3) {
                double d5 = d4 = d3 > 0.0 ? Math.floor(d3 / (double)arrayList.size()) : Math.ceil(d3 / (double)arrayList.size());
            }
            if (d4 != 0.0) {
                Iterator iterator = arrayList.iterator();
                while (iterator.hasNext()) {
                    int n2 = (Integer)iterator.next();
                    double d6 = this.snapSpace(compositeSize2.getProportionalMinOrMaxSize(n2, bl2)) - compositeSize.getSize(n2);
                    if (bl2 && d6 > 0.0 || !bl2 && d6 < 0.0) {
                        d6 = 0.0;
                    }
                    double d7 = Math.abs(d6) <= Math.abs(d4) ? d6 : d4;
                    compositeSize.addSize(n2, d7);
                    boolean bl5 = bl4 = (d3 -= d7) >= 0.0;
                    if (Math.abs(d7) < Math.abs(d4)) {
                        iterator.remove();
                    }
                    if (d3 != 0.0) continue;
                    continue block1;
                }
                continue;
            }
            d4 = (int)d3 % arrayList.size();
            if (d4 == 0.0) break;
            d4 = bl2 ? -1.0 : 1.0;
            bl3 = true;
        }
        return d3;
    }

    private double adjustColumnWidths(CompositeSize compositeSize, double d2) {
        double d3;
        double d4;
        assert (d2 != -1.0);
        double d5 = this.snapSpace(this.getHgap());
        double d6 = this.snapSpace(this.getInsets().getLeft());
        double d7 = this.snapSpace(this.getInsets().getRight());
        double d8 = d5 * (double)(this.getNumberOfColumns() - 1);
        double d9 = d2 - d6 - d7;
        if (this.columnPercentTotal > 0.0) {
            d4 = 0.0;
            for (int i2 = 0; i2 < this.columnPercentWidth.length; ++i2) {
                if (!(this.columnPercentWidth[i2] >= 0.0)) continue;
                double d10 = (d9 - d8) * (this.columnPercentWidth[i2] / 100.0);
                double d11 = Math.floor(d10);
                d4 += d10 - d11;
                d10 = d11;
                if (d4 >= 0.5) {
                    d10 += 1.0;
                    d4 = -1.0 + d4;
                }
                compositeSize.setSize(i2, d10);
            }
        }
        d4 = compositeSize.computeTotal();
        if (this.columnPercentTotal < 100.0 && (d3 = d2 - d6 - d7 - d4) != 0.0) {
            double d12 = this.growToMultiSpanPreferredWidths(compositeSize, d3);
            d12 = this.growOrShrinkColumnWidths(compositeSize, Priority.ALWAYS, d12);
            d12 = this.growOrShrinkColumnWidths(compositeSize, Priority.SOMETIMES, d12);
            d4 += d3 - d12;
        }
        return d4;
    }

    private double growToMultiSpanPreferredWidths(CompositeSize compositeSize, double d2) {
        double d3;
        double d4;
        double d5;
        int n2;
        double d6;
        double d7;
        double d8;
        int n3;
        Iterator iterator;
        if (d2 <= 0.0) {
            return d2;
        }
        TreeSet<Integer> treeSet = new TreeSet<Integer>();
        TreeSet<Integer> treeSet2 = new TreeSet<Integer>();
        TreeSet<Integer> treeSet3 = new TreeSet<Integer>();
        for (Map.Entry entry : compositeSize.multiSizes()) {
            Interval interval = (Interval)entry.getKey();
            block5: for (int i2 = interval.begin; i2 < interval.end; ++i2) {
                if (!(this.columnPercentWidth[i2] < 0.0)) continue;
                switch (this.columnGrow[i2]) {
                    case ALWAYS: {
                        treeSet.add(i2);
                        continue block5;
                    }
                    case SOMETIMES: {
                        treeSet2.add(i2);
                    }
                }
            }
            if (!(this.columnPercentWidth[interval.end - 1] < 0.0)) continue;
            treeSet3.add(interval.end - 1);
        }
        double d9 = d2;
        while (treeSet.size() > 0 && d9 > (double)treeSet.size()) {
            double d10 = Math.floor(d9 / (double)treeSet.size());
            iterator = treeSet.iterator();
            while (iterator.hasNext()) {
                n3 = (Integer)iterator.next();
                d8 = this.getColumnMaxWidth(n3);
                d7 = this.getColumnPrefWidth(n3);
                d6 = d10;
                for (Map.Entry entry : compositeSize.multiSizes()) {
                    Interval interval = (Interval)entry.getKey();
                    if (!interval.contains(n3)) continue;
                    n2 = 0;
                    for (int i3 = interval.begin; i3 < interval.end; ++i3) {
                        if (!treeSet.contains(i3)) continue;
                        ++n2;
                    }
                    d5 = compositeSize.computeTotal(interval.begin, interval.end);
                    d6 = Math.min(Math.floor(Math.max(0.0, ((Double)entry.getValue() - d5) / (double)n2)), d6);
                }
                d4 = compositeSize.getSize(n3);
                d3 = d8 >= 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d8) : (d8 == Double.NEGATIVE_INFINITY && d7 > 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d7) : d4 + d6);
                d5 = d3 - d4;
                d9 -= d5;
                if (d5 != d6 || d5 == 0.0) {
                    iterator.remove();
                }
                compositeSize.setSize(n3, d3);
            }
        }
        while (treeSet2.size() > 0 && d9 > (double)treeSet2.size()) {
            double d11 = Math.floor(d9 / (double)treeSet2.size());
            iterator = treeSet2.iterator();
            while (iterator.hasNext()) {
                n3 = (Integer)iterator.next();
                d8 = this.getColumnMaxWidth(n3);
                d7 = this.getColumnPrefWidth(n3);
                d6 = d11;
                for (Map.Entry entry : compositeSize.multiSizes()) {
                    Interval interval = (Interval)entry.getKey();
                    if (!interval.contains(n3)) continue;
                    n2 = 0;
                    for (int i4 = interval.begin; i4 < interval.end; ++i4) {
                        if (!treeSet2.contains(i4)) continue;
                        ++n2;
                    }
                    d5 = compositeSize.computeTotal(interval.begin, interval.end);
                    d6 = Math.min(Math.floor(Math.max(0.0, ((Double)entry.getValue() - d5) / (double)n2)), d6);
                }
                d4 = compositeSize.getSize(n3);
                d3 = d8 >= 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d8) : (d8 == Double.NEGATIVE_INFINITY && d7 > 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d7) : d4 + d6);
                d5 = d3 - d4;
                d9 -= d5;
                if (d5 != d6 || d5 == 0.0) {
                    iterator.remove();
                }
                compositeSize.setSize(n3, d3);
            }
        }
        while (treeSet3.size() > 0 && d9 > (double)treeSet3.size()) {
            double d12 = Math.floor(d9 / (double)treeSet3.size());
            iterator = treeSet3.iterator();
            while (iterator.hasNext()) {
                n3 = (Integer)iterator.next();
                d8 = this.getColumnMaxWidth(n3);
                d7 = this.getColumnPrefWidth(n3);
                d6 = d12;
                for (Map.Entry entry : compositeSize.multiSizes()) {
                    Interval interval = (Interval)entry.getKey();
                    if (interval.end - 1 != n3) continue;
                    double d13 = compositeSize.computeTotal(interval.begin, interval.end);
                    d6 = Math.min(Math.max(0.0, (Double)entry.getValue() - d13), d6);
                }
                d4 = compositeSize.getSize(n3);
                d3 = d8 >= 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d8) : (d8 == Double.NEGATIVE_INFINITY && d7 > 0.0 ? GridPane.boundedSize(0.0, d4 + d6, d7) : d4 + d6);
                d5 = d3 - d4;
                d9 -= d5;
                if (d5 != d6 || d5 == 0.0) {
                    iterator.remove();
                }
                compositeSize.setSize(n3, d3);
            }
        }
        return d9;
    }

    private double growOrShrinkColumnWidths(CompositeSize compositeSize, Priority priority, double d2) {
        CompositeSize compositeSize2;
        boolean bl;
        if (d2 == 0.0) {
            return 0.0;
        }
        boolean bl2 = d2 < 0.0;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i2 = 0; i2 < this.columnGrow.length; ++i2) {
            if (!(this.columnPercentWidth[i2] < 0.0) || !bl2 && this.columnGrow[i2] != priority) continue;
            arrayList.add(i2);
        }
        double d3 = d2;
        boolean bl3 = false;
        double d4 = 0.0;
        boolean bl4 = bl = d3 >= 0.0;
        CompositeSize compositeSize3 = compositeSize2 = bl2 ? this.computeMinWidths(null) : this.computeMaxWidths();
        block1: while (d3 != 0.0 && bl == bl4 && arrayList.size() > 0) {
            if (!bl3) {
                double d5 = d4 = d3 > 0.0 ? Math.floor(d3 / (double)arrayList.size()) : Math.ceil(d3 / (double)arrayList.size());
            }
            if (d4 != 0.0) {
                Iterator iterator = arrayList.iterator();
                while (iterator.hasNext()) {
                    int n2 = (Integer)iterator.next();
                    double d6 = this.snapSpace(compositeSize2.getProportionalMinOrMaxSize(n2, bl2)) - compositeSize.getSize(n2);
                    if (bl2 && d6 > 0.0 || !bl2 && d6 < 0.0) {
                        d6 = 0.0;
                    }
                    double d7 = Math.abs(d6) <= Math.abs(d4) ? d6 : d4;
                    compositeSize.addSize(n2, d7);
                    boolean bl5 = bl4 = (d3 -= d7) >= 0.0;
                    if (Math.abs(d7) < Math.abs(d4)) {
                        iterator.remove();
                    }
                    if (d3 != 0.0) continue;
                    continue block1;
                }
                continue;
            }
            d4 = (int)d3 % arrayList.size();
            if (d4 == 0.0) break;
            d4 = bl2 ? -1.0 : 1.0;
            bl3 = true;
        }
        return d3;
    }

    private void layoutGridLines(CompositeSize compositeSize, CompositeSize compositeSize2, double d2, double d3, double d4, double d5) {
        int n2;
        if (!this.isGridLinesVisible()) {
            return;
        }
        if (!this.gridLines.getChildren().isEmpty()) {
            this.gridLines.getChildren().clear();
        }
        double d6 = this.snapSpace(this.getHgap());
        double d7 = this.snapSpace(this.getVgap());
        double d8 = d2;
        double d9 = d3;
        for (n2 = 0; n2 <= compositeSize.getLength(); ++n2) {
            this.gridLines.getChildren().add(this.createGridLine(d8, d9, d8, d9 + d4));
            if (n2 > 0 && n2 < compositeSize.getLength() && this.getHgap() != 0.0) {
                this.gridLines.getChildren().add(this.createGridLine(d8 += this.getHgap(), d9, d8, d9 + d4));
            }
            if (n2 >= compositeSize.getLength()) continue;
            d8 += compositeSize.getSize(n2);
        }
        d8 = d2;
        for (n2 = 0; n2 <= compositeSize2.getLength(); ++n2) {
            this.gridLines.getChildren().add(this.createGridLine(d8, d9, d8 + d5, d9));
            if (n2 > 0 && n2 < compositeSize2.getLength() && this.getVgap() != 0.0) {
                this.gridLines.getChildren().add(this.createGridLine(d8, d9 += this.getVgap(), d8 + d5, d9));
            }
            if (n2 >= compositeSize2.getLength()) continue;
            d9 += compositeSize2.getSize(n2);
        }
    }

    private Line createGridLine(double d2, double d3, double d4, double d5) {
        Line line = new Line();
        line.setStartX(d2);
        line.setStartY(d3);
        line.setEndX(d4);
        line.setEndY(d5);
        line.setStroke(GRID_LINE_COLOR);
        line.setStrokeDashOffset(3.0);
        return line;
    }

    @Override
    public String toString() {
        return "Grid hgap=" + this.getHgap() + ", vgap=" + this.getVgap() + ", alignment=" + (Object)((Object)this.getAlignment());
    }

    private CompositeSize createCompositeRows(double d2) {
        return new CompositeSize(this.getNumberOfRows(), this.rowPercentHeight, this.rowPercentTotal, this.snapSpace(this.getVgap()), d2);
    }

    private CompositeSize createCompositeColumns(double d2) {
        return new CompositeSize(this.getNumberOfColumns(), this.columnPercentWidth, this.columnPercentTotal, this.snapSpace(this.getHgap()), d2);
    }

    private int getNodeRowEndConvertRemaining(Node node) {
        int n2 = GridPane.getNodeRowSpan(node);
        return n2 != Integer.MAX_VALUE ? GridPane.getNodeRowIndex(node) + n2 - 1 : this.getNumberOfRows() - 1;
    }

    private int getNodeColumnEndConvertRemaining(Node node) {
        int n2 = GridPane.getNodeColumnSpan(node);
        return n2 != Integer.MAX_VALUE ? GridPane.getNodeColumnIndex(node) + n2 - 1 : this.getNumberOfColumns() - 1;
    }

    double[][] getGrid() {
        if (this.currentHeights == null || this.currentWidths == null) {
            return null;
        }
        return new double[][]{this.currentWidths.asArray(), this.currentHeights.asArray()};
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return GridPane.getClassCssMetaData();
    }

    @Deprecated
    public final int impl_getRowCount() {
        int n2 = this.getRowConstraints().size();
        for (int i2 = 0; i2 < this.getChildren().size(); ++i2) {
            Node node = (Node)this.getChildren().get(i2);
            if (!node.isManaged()) continue;
            int n3 = GridPane.getNodeRowIndex(node);
            int n4 = GridPane.getNodeRowEnd(node);
            n2 = Math.max(n2, (n4 != Integer.MAX_VALUE ? n4 : n3) + 1);
        }
        return n2;
    }

    @Deprecated
    public final int impl_getColumnCount() {
        int n2 = this.getColumnConstraints().size();
        for (int i2 = 0; i2 < this.getChildren().size(); ++i2) {
            Node node = (Node)this.getChildren().get(i2);
            if (!node.isManaged()) continue;
            int n3 = GridPane.getNodeColumnIndex(node);
            int n4 = GridPane.getNodeColumnEnd(node);
            n2 = Math.max(n2, (n4 != Integer.MAX_VALUE ? n4 : n3) + 1);
        }
        return n2;
    }

    @Deprecated
    public final Bounds impl_getCellBounds(int n2, int n3) {
        double[] arrd;
        double[] arrd2;
        double d2 = this.snapSpace(this.getHgap());
        double d3 = this.snapSpace(this.getVgap());
        double d4 = this.snapSpace(this.getInsets().getTop());
        double d5 = this.snapSpace(this.getInsets().getRight());
        double d6 = this.snapSpace(this.getInsets().getBottom());
        double d7 = this.snapSpace(this.getInsets().getLeft());
        double d8 = this.snapSize(this.getHeight()) - (d4 + d6);
        double d9 = this.snapSize(this.getWidth()) - (d7 + d5);
        double[][] arrd3 = this.getGrid();
        if (arrd3 == null) {
            arrd2 = new double[]{0.0};
            n3 = 0;
            arrd = new double[]{0.0};
            n2 = 0;
        } else {
            arrd = arrd3[0];
            arrd2 = arrd3[1];
        }
        double d10 = 0.0;
        for (int i2 = 0; i2 < arrd2.length; ++i2) {
            d10 += arrd2[i2];
        }
        double d11 = d4 + Region.computeYOffset(d8, d10 += (double)(arrd2.length - 1) * d3, this.getAlignment().getVpos());
        double d12 = arrd2[n3];
        for (int i3 = 0; i3 < n3; ++i3) {
            d11 += arrd2[i3] + d3;
        }
        double d13 = 0.0;
        for (int i4 = 0; i4 < arrd.length; ++i4) {
            d13 += arrd[i4];
        }
        double d14 = d7 + Region.computeXOffset(d9, d13 += (double)(arrd.length - 1) * d2, this.getAlignment().getHpos());
        double d15 = arrd[n2];
        for (int i5 = 0; i5 < n2; ++i5) {
            d14 += arrd[i5] + d2;
        }
        return new BoundingBox(d14, d11, d15, d12);
    }

    private static final class CompositeSize
    implements Cloneable {
        double[] singleSizes;
        private SortedMap<Interval, Double> multiSizes;
        private BitSet preset;
        private final double[] fixedPercent;
        private final double totalFixedPercent;
        private final double gap;

        public CompositeSize(int n2, double[] arrd, double d2, double d3, double d4) {
            this.singleSizes = new double[n2];
            Arrays.fill(this.singleSizes, d4);
            this.fixedPercent = arrd;
            this.totalFixedPercent = d2;
            this.gap = d3;
        }

        private void setSize(int n2, double d2) {
            this.singleSizes[n2] = d2;
        }

        private void setPresetSize(int n2, double d2) {
            this.setSize(n2, d2);
            if (this.preset == null) {
                this.preset = new BitSet(this.singleSizes.length);
            }
            this.preset.set(n2);
        }

        private boolean isPreset(int n2) {
            if (this.preset == null) {
                return false;
            }
            return this.preset.get(n2);
        }

        private void addSize(int n2, double d2) {
            this.singleSizes[n2] = this.singleSizes[n2] + d2;
        }

        private double getSize(int n2) {
            return this.singleSizes[n2];
        }

        private void setMaxSize(int n2, double d2) {
            this.singleSizes[n2] = Math.max(this.singleSizes[n2], d2);
        }

        private void setMultiSize(int n2, int n3, double d2) {
            if (this.multiSizes == null) {
                this.multiSizes = new TreeMap<Interval, Double>();
            }
            Interval interval = new Interval(n2, n3);
            this.multiSizes.put(interval, d2);
        }

        private Iterable<Map.Entry<Interval, Double>> multiSizes() {
            if (this.multiSizes == null) {
                return Collections.EMPTY_LIST;
            }
            return this.multiSizes.entrySet();
        }

        private void setMaxMultiSize(int n2, int n3, double d2) {
            Interval interval;
            Double d3;
            if (this.multiSizes == null) {
                this.multiSizes = new TreeMap<Interval, Double>();
            }
            if ((d3 = (Double)this.multiSizes.get(interval = new Interval(n2, n3))) == null) {
                this.multiSizes.put(interval, d2);
            } else {
                this.multiSizes.put(interval, Math.max(d2, d3));
            }
        }

        private double getProportionalMinOrMaxSize(int n2, boolean bl) {
            double d2 = this.singleSizes[n2];
            if (!this.isPreset(n2) && this.multiSizes != null) {
                for (Interval interval : this.multiSizes.keySet()) {
                    double d3;
                    if (!interval.contains(n2)) continue;
                    double d4 = d3 = (Double)this.multiSizes.get(interval) / (double)interval.size();
                    for (int i2 = interval.begin; i2 < interval.end; ++i2) {
                        if (i2 == n2 || !(bl ? this.singleSizes[i2] > d3 : this.singleSizes[i2] < d3)) continue;
                        d4 += d3 - this.singleSizes[i2];
                    }
                    d2 = bl ? Math.max(d2, d4) : Math.min(d2, d4);
                }
            }
            return d2;
        }

        private double computeTotal(int n2, int n3) {
            double d2 = this.gap * (double)(n3 - n2 - 1);
            for (int i2 = n2; i2 < n3; ++i2) {
                d2 += this.singleSizes[i2];
            }
            return d2;
        }

        private double computeTotal() {
            return this.computeTotal(0, this.singleSizes.length);
        }

        private boolean allPreset(int n2, int n3) {
            if (this.preset == null) {
                return false;
            }
            for (int i2 = n2; i2 < n3; ++i2) {
                if (this.preset.get(i2)) continue;
                return false;
            }
            return true;
        }

        private double computeTotalWithMultiSize() {
            double d2 = this.computeTotal();
            if (this.multiSizes != null) {
                for (Map.Entry<Interval, Double> entry : this.multiSizes.entrySet()) {
                    Interval interval = entry.getKey();
                    if (this.allPreset(interval.begin, interval.end)) continue;
                    double d3 = this.computeTotal(interval.begin, interval.end);
                    if (!(entry.getValue() > d3)) continue;
                    d2 += entry.getValue() - d3;
                }
            }
            if (this.totalFixedPercent > 0.0) {
                int n2;
                double d4 = 0.0;
                for (n2 = 0; n2 < this.fixedPercent.length; ++n2) {
                    if (this.fixedPercent[n2] != 0.0) continue;
                    d2 -= this.singleSizes[n2];
                }
                for (n2 = 0; n2 < this.fixedPercent.length; ++n2) {
                    if (this.fixedPercent[n2] > 0.0) {
                        d2 = Math.max(d2, this.singleSizes[n2] * (100.0 / this.fixedPercent[n2]));
                        continue;
                    }
                    if (!(this.fixedPercent[n2] < 0.0)) continue;
                    d4 += this.singleSizes[n2];
                }
                if (this.totalFixedPercent < 100.0) {
                    d2 = Math.max(d2, d4 * 100.0 / (100.0 - this.totalFixedPercent));
                }
            }
            return d2;
        }

        private int getLength() {
            return this.singleSizes.length;
        }

        protected Object clone() {
            try {
                CompositeSize compositeSize = (CompositeSize)super.clone();
                compositeSize.singleSizes = (double[])compositeSize.singleSizes.clone();
                if (this.multiSizes != null) {
                    compositeSize.multiSizes = new TreeMap<Interval, Double>(compositeSize.multiSizes);
                }
                return compositeSize;
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                throw new RuntimeException(cloneNotSupportedException);
            }
        }

        private double[] asArray() {
            return this.singleSizes;
        }
    }

    private static final class Interval
    implements Comparable<Interval> {
        public final int begin;
        public final int end;

        public Interval(int n2, int n3) {
            this.begin = n2;
            this.end = n3;
        }

        @Override
        public int compareTo(Interval interval) {
            return this.begin != interval.begin ? this.begin - interval.begin : this.end - interval.end;
        }

        private boolean contains(int n2) {
            return this.begin <= n2 && n2 < this.end;
        }

        private int size() {
            return this.end - this.begin;
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<GridPane, Boolean> GRID_LINES_VISIBLE = new CssMetaData<GridPane, Boolean>("-fx-grid-lines-visible", BooleanConverter.getInstance(), Boolean.FALSE){

            @Override
            public boolean isSettable(GridPane gridPane) {
                return gridPane.gridLinesVisible == null || !gridPane.gridLinesVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(GridPane gridPane) {
                return (StyleableProperty)((Object)gridPane.gridLinesVisibleProperty());
            }
        };
        private static final CssMetaData<GridPane, Number> HGAP = new CssMetaData<GridPane, Number>("-fx-hgap", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(GridPane gridPane) {
                return gridPane.hgap == null || !gridPane.hgap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(GridPane gridPane) {
                return (StyleableProperty)((Object)gridPane.hgapProperty());
            }
        };
        private static final CssMetaData<GridPane, Pos> ALIGNMENT = new CssMetaData<GridPane, Pos>("-fx-alignment", new EnumConverter<Pos>(Pos.class), Pos.TOP_LEFT){

            @Override
            public boolean isSettable(GridPane gridPane) {
                return gridPane.alignment == null || !gridPane.alignment.isBound();
            }

            @Override
            public StyleableProperty<Pos> getStyleableProperty(GridPane gridPane) {
                return (StyleableProperty)((Object)gridPane.alignmentProperty());
            }
        };
        private static final CssMetaData<GridPane, Number> VGAP = new CssMetaData<GridPane, Number>("-fx-vgap", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(GridPane gridPane) {
                return gridPane.vgap == null || !gridPane.vgap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(GridPane gridPane) {
                return (StyleableProperty)((Object)gridPane.vgapProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Region.getClassCssMetaData());
            arrayList.add(GRID_LINES_VISIBLE);
            arrayList.add(HGAP);
            arrayList.add(ALIGNMENT);
            arrayList.add(VGAP);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

