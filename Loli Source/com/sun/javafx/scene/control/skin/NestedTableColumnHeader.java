/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class NestedTableColumnHeader
extends TableColumnHeader {
    private static final int DRAG_RECT_WIDTH = 4;
    private static final String TABLE_COLUMN_KEY = "TableColumn";
    private static final String TABLE_COLUMN_HEADER_KEY = "TableColumnHeader";
    private ObservableList<? extends TableColumnBase> columns;
    private TableColumnHeader label;
    private ObservableList<TableColumnHeader> columnHeaders;
    private double lastX = 0.0;
    private double dragAnchorX = 0.0;
    private Map<TableColumnBase<?, ?>, Rectangle> dragRects = new WeakHashMap();
    boolean updateColumns = true;
    private final ListChangeListener<TableColumnBase> columnsListener = change -> this.setHeadersNeedUpdate();
    private final WeakListChangeListener weakColumnsListener = new WeakListChangeListener<TableColumnBase>(this.columnsListener);
    private static final EventHandler<MouseEvent> rectMousePressed = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent mouseEvent) {
            Rectangle rectangle = (Rectangle)mouseEvent.getSource();
            TableColumnBase tableColumnBase = (TableColumnBase)rectangle.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader nestedTableColumnHeader = (NestedTableColumnHeader)rectangle.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY);
            if (!nestedTableColumnHeader.isColumnResizingEnabled()) {
                return;
            }
            if (mouseEvent.getClickCount() == 2 && mouseEvent.isPrimaryButtonDown()) {
                nestedTableColumnHeader.getTableViewSkin().resizeColumnToFitContent(tableColumnBase, -1);
            } else {
                Rectangle rectangle2 = (Rectangle)mouseEvent.getSource();
                double d2 = nestedTableColumnHeader.getTableHeaderRow().sceneToLocal(rectangle2.localToScene(rectangle2.getBoundsInLocal())).getMinX() + 2.0;
                nestedTableColumnHeader.dragAnchorX = mouseEvent.getSceneX();
                nestedTableColumnHeader.columnResizingStarted(d2);
            }
            mouseEvent.consume();
        }
    };
    private static final EventHandler<MouseEvent> rectMouseDragged = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent mouseEvent) {
            Rectangle rectangle = (Rectangle)mouseEvent.getSource();
            TableColumnBase tableColumnBase = (TableColumnBase)rectangle.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader nestedTableColumnHeader = (NestedTableColumnHeader)rectangle.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY);
            if (!nestedTableColumnHeader.isColumnResizingEnabled()) {
                return;
            }
            nestedTableColumnHeader.columnResizing(tableColumnBase, mouseEvent);
            mouseEvent.consume();
        }
    };
    private static final EventHandler<MouseEvent> rectMouseReleased = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent mouseEvent) {
            Rectangle rectangle = (Rectangle)mouseEvent.getSource();
            TableColumnBase tableColumnBase = (TableColumnBase)rectangle.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader nestedTableColumnHeader = (NestedTableColumnHeader)rectangle.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY);
            if (!nestedTableColumnHeader.isColumnResizingEnabled()) {
                return;
            }
            nestedTableColumnHeader.columnResizingComplete(tableColumnBase, mouseEvent);
            mouseEvent.consume();
        }
    };
    private static final EventHandler<MouseEvent> rectCursorChangeListener = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent mouseEvent) {
            Rectangle rectangle = (Rectangle)mouseEvent.getSource();
            TableColumnBase tableColumnBase = (TableColumnBase)rectangle.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader nestedTableColumnHeader = (NestedTableColumnHeader)rectangle.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY);
            if (nestedTableColumnHeader.getCursor() == null) {
                rectangle.setCursor(nestedTableColumnHeader.isColumnResizingEnabled() && rectangle.isHover() && tableColumnBase.isResizable() ? Cursor.H_RESIZE : null);
            }
        }
    };

    public NestedTableColumnHeader(TableViewSkinBase tableViewSkinBase, TableColumnBase tableColumnBase) {
        super(tableViewSkinBase, tableColumnBase);
        this.getStyleClass().setAll("nested-column-header");
        this.setFocusTraversable(false);
        this.label = new TableColumnHeader(tableViewSkinBase, this.getTableColumn());
        this.label.setTableHeaderRow(this.getTableHeaderRow());
        this.label.setParentHeader(this.getParentHeader());
        this.label.setNestedColumnHeader(this);
        if (this.getTableColumn() != null) {
            this.changeListenerHandler.registerChangeListener(this.getTableColumn().textProperty(), "TABLE_COLUMN_TEXT");
        }
        this.changeListenerHandler.registerChangeListener(tableViewSkinBase.columnResizePolicyProperty(), "TABLE_VIEW_COLUMN_RESIZE_POLICY");
    }

    @Override
    protected void handlePropertyChanged(String string) {
        super.handlePropertyChanged(string);
        if ("TABLE_VIEW_COLUMN_RESIZE_POLICY".equals(string)) {
            this.updateContent();
        } else if ("TABLE_COLUMN_TEXT".equals(string)) {
            this.label.setVisible(this.getTableColumn().getText() != null && !this.getTableColumn().getText().isEmpty());
        }
    }

    @Override
    public void setTableHeaderRow(TableHeaderRow tableHeaderRow) {
        super.setTableHeaderRow(tableHeaderRow);
        this.label.setTableHeaderRow(tableHeaderRow);
        for (TableColumnHeader tableColumnHeader : this.getColumnHeaders()) {
            tableColumnHeader.setTableHeaderRow(tableHeaderRow);
        }
    }

    @Override
    public void setParentHeader(NestedTableColumnHeader nestedTableColumnHeader) {
        super.setParentHeader(nestedTableColumnHeader);
        this.label.setParentHeader(nestedTableColumnHeader);
    }

    ObservableList<? extends TableColumnBase> getColumns() {
        return this.columns;
    }

    void setColumns(ObservableList<? extends TableColumnBase> observableList) {
        if (this.columns != null) {
            this.columns.removeListener(this.weakColumnsListener);
        }
        this.columns = observableList;
        if (this.columns != null) {
            this.columns.addListener(this.weakColumnsListener);
        }
    }

    void updateTableColumnHeaders() {
        int n2;
        Object object;
        if (this.getTableColumn() == null && this.getTableViewSkin() != null) {
            this.setColumns(this.getTableViewSkin().getColumns());
        } else if (this.getTableColumn() != null) {
            this.setColumns(this.getTableColumn().getColumns());
        }
        if (this.getColumns().isEmpty()) {
            for (int i2 = 0; i2 < this.getColumnHeaders().size(); ++i2) {
                TableColumnHeader observableList2 = (TableColumnHeader)this.getColumnHeaders().get(i2);
                observableList2.dispose();
            }
            object = this.getParentHeader();
            if (object != null) {
                ObservableList<TableColumnHeader> observableList = ((NestedTableColumnHeader)object).getColumnHeaders();
                n2 = observableList.indexOf(this);
                if (n2 >= 0 && n2 < observableList.size()) {
                    observableList.set(n2, this.createColumnHeader(this.getTableColumn()));
                }
            } else {
                this.getColumnHeaders().clear();
            }
        } else {
            object = new ArrayList<TableColumnHeader>(this.getColumnHeaders());
            ArrayList<TableColumnHeader> arrayList = new ArrayList<TableColumnHeader>();
            for (n2 = 0; n2 < this.getColumns().size(); ++n2) {
                TableColumnBase tableColumnBase = (TableColumnBase)this.getColumns().get(n2);
                if (tableColumnBase == null || !tableColumnBase.isVisible()) continue;
                boolean bl = false;
                for (int i2 = 0; i2 < object.size(); ++i2) {
                    TableColumnHeader tableColumnHeader = (TableColumnHeader)object.get(i2);
                    if (tableColumnBase != tableColumnHeader.getTableColumn()) continue;
                    arrayList.add(tableColumnHeader);
                    bl = true;
                    break;
                }
                if (bl) continue;
                arrayList.add(this.createColumnHeader(tableColumnBase));
            }
            this.getColumnHeaders().setAll((Collection<TableColumnHeader>)arrayList);
            object.removeAll(arrayList);
            for (n2 = 0; n2 < object.size(); ++n2) {
                ((TableColumnHeader)object.get(n2)).dispose();
            }
        }
        this.updateContent();
        for (TableColumnHeader tableColumnHeader : this.getColumnHeaders()) {
            tableColumnHeader.applyCss();
        }
    }

    @Override
    void dispose() {
        super.dispose();
        if (this.label != null) {
            this.label.dispose();
        }
        if (this.getColumns() != null) {
            this.getColumns().removeListener(this.weakColumnsListener);
        }
        for (int i2 = 0; i2 < this.getColumnHeaders().size(); ++i2) {
            TableColumnHeader node = (TableColumnHeader)this.getColumnHeaders().get(i2);
            node.dispose();
        }
        for (Rectangle rectangle : this.dragRects.values()) {
            if (rectangle == null) continue;
            rectangle.visibleProperty().unbind();
        }
        this.dragRects.clear();
        this.getChildren().clear();
        this.changeListenerHandler.dispose();
    }

    public ObservableList<TableColumnHeader> getColumnHeaders() {
        if (this.columnHeaders == null) {
            this.columnHeaders = FXCollections.observableArrayList();
        }
        return this.columnHeaders;
    }

    @Override
    protected void layoutChildren() {
        double d2 = this.getWidth() - this.snappedLeftInset() - this.snappedRightInset();
        double d3 = this.getHeight() - this.snappedTopInset() - this.snappedBottomInset();
        int n2 = (int)this.label.prefHeight(-1.0);
        if (this.label.isVisible()) {
            this.label.resize(d2, n2);
            this.label.relocate(this.snappedLeftInset(), this.snappedTopInset());
        }
        double d4 = this.snappedLeftInset();
        boolean bl = false;
        int n3 = this.getColumnHeaders().size();
        for (int i2 = 0; i2 < n3; ++i2) {
            TableColumnHeader tableColumnHeader = (TableColumnHeader)this.getColumnHeaders().get(i2);
            if (!tableColumnHeader.isVisible()) continue;
            double d5 = this.snapSize(tableColumnHeader.prefWidth(-1.0));
            tableColumnHeader.resize(d5, this.snapSize(d3 - (double)n2));
            tableColumnHeader.relocate(d4, (double)n2 + this.snappedTopInset());
            d4 += d5;
            Rectangle rectangle = this.dragRects.get(tableColumnHeader.getTableColumn());
            if (rectangle == null) continue;
            rectangle.setHeight(tableColumnHeader.getDragRectHeight());
            rectangle.relocate(d4 - 2.0, this.snappedTopInset() + (double)n2);
        }
    }

    @Override
    double getDragRectHeight() {
        return this.label.prefHeight(-1.0);
    }

    @Override
    protected double computePrefWidth(double d2) {
        this.checkState();
        double d3 = 0.0;
        if (this.getColumns() != null) {
            for (TableColumnHeader tableColumnHeader : this.getColumnHeaders()) {
                if (!tableColumnHeader.isVisible()) continue;
                d3 += this.snapSize(tableColumnHeader.computePrefWidth(d2));
            }
        }
        return d3;
    }

    @Override
    protected double computePrefHeight(double d2) {
        this.checkState();
        double d3 = 0.0;
        if (this.getColumnHeaders() != null) {
            for (TableColumnHeader tableColumnHeader : this.getColumnHeaders()) {
                d3 = Math.max(d3, tableColumnHeader.prefHeight(-1.0));
            }
        }
        return d3 + this.label.prefHeight(-1.0) + this.snappedTopInset() + this.snappedBottomInset();
    }

    protected TableColumnHeader createTableColumnHeader(TableColumnBase tableColumnBase) {
        return tableColumnBase.getColumns().isEmpty() ? new TableColumnHeader(this.getTableViewSkin(), tableColumnBase) : new NestedTableColumnHeader(this.getTableViewSkin(), tableColumnBase);
    }

    protected void setHeadersNeedUpdate() {
        this.updateColumns = true;
        for (int i2 = 0; i2 < this.getColumnHeaders().size(); ++i2) {
            TableColumnHeader tableColumnHeader = (TableColumnHeader)this.getColumnHeaders().get(i2);
            if (!(tableColumnHeader instanceof NestedTableColumnHeader)) continue;
            ((NestedTableColumnHeader)tableColumnHeader).setHeadersNeedUpdate();
        }
        this.requestLayout();
    }

    private void updateContent() {
        ArrayList<Node> arrayList = new ArrayList<Node>();
        arrayList.add(this.label);
        arrayList.addAll(this.getColumnHeaders());
        if (this.isColumnResizingEnabled()) {
            this.rebuildDragRects();
            arrayList.addAll(this.dragRects.values());
        }
        this.getChildren().setAll((Collection<Node>)arrayList);
    }

    private void rebuildDragRects() {
        boolean bl;
        if (!this.isColumnResizingEnabled()) {
            return;
        }
        this.getChildren().removeAll(this.dragRects.values());
        for (Rectangle object2 : this.dragRects.values()) {
            object2.visibleProperty().unbind();
        }
        this.dragRects.clear();
        ObservableList<? extends TableColumnBase> observableList = this.getColumns();
        if (observableList == null) {
            return;
        }
        TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> tableViewSkinBase = this.getTableViewSkin();
        Callback callback = (Callback)tableViewSkinBase.columnResizePolicyProperty().get();
        boolean bl2 = tableViewSkinBase instanceof TableViewSkin ? TableView.CONSTRAINED_RESIZE_POLICY.equals(callback) : (bl = tableViewSkinBase instanceof TreeTableViewSkin ? TreeTableView.CONSTRAINED_RESIZE_POLICY.equals(callback) : false);
        if (bl && tableViewSkinBase.getVisibleLeafColumns().size() == 1) {
            return;
        }
        for (int i2 = 0; !(i2 >= observableList.size() || bl && i2 == this.getColumns().size() - 1); ++i2) {
            TableColumnBase tableColumnBase = (TableColumnBase)observableList.get(i2);
            Rectangle rectangle = new Rectangle();
            rectangle.getProperties().put(TABLE_COLUMN_KEY, tableColumnBase);
            rectangle.getProperties().put(TABLE_COLUMN_HEADER_KEY, this);
            rectangle.setWidth(4.0);
            rectangle.setHeight(this.getHeight() - this.label.getHeight());
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.visibleProperty().bind(tableColumnBase.visibleProperty());
            rectangle.setOnMousePressed(rectMousePressed);
            rectangle.setOnMouseDragged(rectMouseDragged);
            rectangle.setOnMouseReleased(rectMouseReleased);
            rectangle.setOnMouseEntered(rectCursorChangeListener);
            rectangle.setOnMouseExited(rectCursorChangeListener);
            this.dragRects.put(tableColumnBase, rectangle);
        }
    }

    private void checkState() {
        if (this.updateColumns) {
            this.updateTableColumnHeaders();
            this.updateColumns = false;
        }
    }

    private TableColumnHeader createColumnHeader(TableColumnBase tableColumnBase) {
        TableColumnHeader tableColumnHeader = this.createTableColumnHeader(tableColumnBase);
        tableColumnHeader.setTableHeaderRow(this.getTableHeaderRow());
        tableColumnHeader.setParentHeader(this);
        return tableColumnHeader;
    }

    private boolean isColumnResizingEnabled() {
        return true;
    }

    private void columnResizingStarted(double d2) {
        this.setCursor(Cursor.H_RESIZE);
        this.columnReorderLine.setLayoutX(d2);
    }

    private void columnResizing(TableColumnBase tableColumnBase, MouseEvent mouseEvent) {
        double d2 = mouseEvent.getSceneX() - this.dragAnchorX;
        if (this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            d2 = -d2;
        }
        double d3 = d2 - this.lastX;
        boolean bl = this.getTableViewSkin().resizeColumn(tableColumnBase, d3);
        if (bl) {
            this.lastX = d2;
        }
    }

    private void columnResizingComplete(TableColumnBase tableColumnBase, MouseEvent mouseEvent) {
        this.setCursor(null);
        this.columnReorderLine.setTranslateX(0.0);
        this.columnReorderLine.setLayoutX(0.0);
        this.lastX = 0.0;
    }
}

