/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.TableColumnSortTypeWrapper;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class TableColumnHeader
extends Region {
    static final double DEFAULT_COLUMN_WIDTH = 80.0;
    private boolean autoSizeComplete = false;
    private double dragOffset;
    private final TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> skin;
    private NestedTableColumnHeader nestedColumnHeader;
    private final TableColumnBase<?, ?> column;
    private TableHeaderRow tableHeaderRow;
    private NestedTableColumnHeader parentHeader;
    Label label;
    int sortPos = -1;
    private Region arrow;
    private Label sortOrderLabel;
    private HBox sortOrderDots;
    private Node sortArrow;
    private boolean isSortColumn;
    private boolean isSizeDirty = false;
    boolean isLastVisibleColumn = false;
    int columnIndex = -1;
    private int newColumnPos;
    protected final Region columnReorderLine;
    protected final MultiplePropertyChangeListenerHandler changeListenerHandler;
    private ListChangeListener<TableColumnBase<?, ?>> sortOrderListener = change -> this.updateSortPosition();
    private ListChangeListener<TableColumnBase<?, ?>> visibleLeafColumnsListener = change -> {
        this.updateColumnIndex();
        this.updateSortPosition();
    };
    private ListChangeListener<String> styleClassListener = change -> this.updateStyleClass();
    private WeakListChangeListener<TableColumnBase<?, ?>> weakSortOrderListener = new WeakListChangeListener(this.sortOrderListener);
    private final WeakListChangeListener<TableColumnBase<?, ?>> weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
    private final WeakListChangeListener<String> weakStyleClassListener = new WeakListChangeListener<String>(this.styleClassListener);
    private static final EventHandler<MouseEvent> mousePressedHandler = mouseEvent -> {
        TableColumnHeader tableColumnHeader = (TableColumnHeader)mouseEvent.getSource();
        ((Node)((Object)tableColumnHeader.getTableViewSkin().getSkinnable())).requestFocus();
        if (mouseEvent.isPrimaryButtonDown() && tableColumnHeader.isColumnReorderingEnabled()) {
            tableColumnHeader.columnReorderingStarted(mouseEvent.getX());
        }
        mouseEvent.consume();
    };
    private static final EventHandler<MouseEvent> mouseDraggedHandler = mouseEvent -> {
        TableColumnHeader tableColumnHeader = (TableColumnHeader)mouseEvent.getSource();
        if (mouseEvent.isPrimaryButtonDown() && tableColumnHeader.isColumnReorderingEnabled()) {
            tableColumnHeader.columnReordering(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        }
        mouseEvent.consume();
    };
    private static final EventHandler<MouseEvent> mouseReleasedHandler = mouseEvent -> {
        if (mouseEvent.isPopupTrigger()) {
            return;
        }
        TableColumnHeader tableColumnHeader = (TableColumnHeader)mouseEvent.getSource();
        TableColumnBase tableColumnBase = tableColumnHeader.getTableColumn();
        ContextMenu contextMenu = tableColumnBase.getContextMenu();
        if (contextMenu != null && contextMenu.isShowing()) {
            return;
        }
        if (tableColumnHeader.getTableHeaderRow().isReordering() && tableColumnHeader.isColumnReorderingEnabled()) {
            tableColumnHeader.columnReorderingComplete();
        } else if (mouseEvent.isStillSincePress()) {
            tableColumnHeader.sortColumn(mouseEvent.isShiftDown());
        }
        mouseEvent.consume();
    };
    private static final EventHandler<ContextMenuEvent> contextMenuRequestedHandler = contextMenuEvent -> {
        TableColumnHeader tableColumnHeader = (TableColumnHeader)contextMenuEvent.getSource();
        TableColumnBase tableColumnBase = tableColumnHeader.getTableColumn();
        ContextMenu contextMenu = tableColumnBase.getContextMenu();
        if (contextMenu != null) {
            contextMenu.show(tableColumnHeader, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            contextMenuEvent.consume();
        }
    };
    private DoubleProperty size;
    private static final PseudoClass PSEUDO_CLASS_LAST_VISIBLE = PseudoClass.getPseudoClass("last-visible");

    public TableColumnHeader(TableViewSkinBase tableViewSkinBase, TableColumnBase tableColumnBase) {
        this.skin = tableViewSkinBase;
        this.column = tableColumnBase;
        this.columnReorderLine = tableViewSkinBase.getColumnReorderLine();
        this.setFocusTraversable(false);
        this.updateColumnIndex();
        this.initUI();
        this.changeListenerHandler = new MultiplePropertyChangeListenerHandler(string -> {
            this.handlePropertyChanged((String)string);
            return null;
        });
        this.changeListenerHandler.registerChangeListener(this.sceneProperty(), "SCENE");
        if (this.column != null && tableViewSkinBase != null) {
            this.updateSortPosition();
            tableViewSkinBase.getSortOrder().addListener(this.weakSortOrderListener);
            tableViewSkinBase.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        }
        if (this.column != null) {
            this.changeListenerHandler.registerChangeListener(this.column.idProperty(), "TABLE_COLUMN_ID");
            this.changeListenerHandler.registerChangeListener(this.column.styleProperty(), "TABLE_COLUMN_STYLE");
            this.changeListenerHandler.registerChangeListener(this.column.widthProperty(), "TABLE_COLUMN_WIDTH");
            this.changeListenerHandler.registerChangeListener(this.column.visibleProperty(), "TABLE_COLUMN_VISIBLE");
            this.changeListenerHandler.registerChangeListener(this.column.sortNodeProperty(), "TABLE_COLUMN_SORT_NODE");
            this.changeListenerHandler.registerChangeListener(this.column.sortableProperty(), "TABLE_COLUMN_SORTABLE");
            this.changeListenerHandler.registerChangeListener(this.column.textProperty(), "TABLE_COLUMN_TEXT");
            this.changeListenerHandler.registerChangeListener(this.column.graphicProperty(), "TABLE_COLUMN_GRAPHIC");
            this.column.getStyleClass().addListener(this.weakStyleClassListener);
            this.setId(this.column.getId());
            this.setStyle(this.column.getStyle());
            this.updateStyleClass();
            this.setAccessibleRole(AccessibleRole.TABLE_COLUMN);
        }
    }

    private double getSize() {
        return this.size == null ? 20.0 : this.size.doubleValue();
    }

    private DoubleProperty sizeProperty() {
        if (this.size == null) {
            this.size = new StyleableDoubleProperty(20.0){

                @Override
                protected void invalidated() {
                    double d2 = this.get();
                    if (d2 <= 0.0) {
                        if (this.isBound()) {
                            this.unbind();
                        }
                        this.set(20.0);
                        throw new IllegalArgumentException("Size cannot be 0 or negative");
                    }
                }

                @Override
                public Object getBean() {
                    return TableColumnHeader.this;
                }

                @Override
                public String getName() {
                    return "size";
                }

                @Override
                public CssMetaData<TableColumnHeader, Number> getCssMetaData() {
                    return StyleableProperties.SIZE;
                }
            };
        }
        return this.size;
    }

    protected void handlePropertyChanged(String string) {
        if ("SCENE".equals(string)) {
            this.updateScene();
        } else if ("TABLE_COLUMN_VISIBLE".equals(string)) {
            this.setVisible(this.getTableColumn().isVisible());
        } else if ("TABLE_COLUMN_WIDTH".equals(string)) {
            this.isSizeDirty = true;
            this.requestLayout();
        } else if ("TABLE_COLUMN_ID".equals(string)) {
            this.setId(this.column.getId());
        } else if ("TABLE_COLUMN_STYLE".equals(string)) {
            this.setStyle(this.column.getStyle());
        } else if ("TABLE_COLUMN_SORT_TYPE".equals(string)) {
            this.updateSortGrid();
            if (this.arrow != null) {
                this.arrow.setRotate(TableColumnSortTypeWrapper.isAscending(this.column) ? 180.0 : 0.0);
            }
        } else if ("TABLE_COLUMN_SORT_NODE".equals(string)) {
            this.updateSortGrid();
        } else if ("TABLE_COLUMN_SORTABLE".equals(string)) {
            if (this.skin.getSortOrder().contains(this.getTableColumn())) {
                NestedTableColumnHeader nestedTableColumnHeader = this.getTableHeaderRow().getRootHeader();
                this.updateAllHeaders(nestedTableColumnHeader);
            }
        } else if ("TABLE_COLUMN_TEXT".equals(string)) {
            this.label.setText(this.column.getText());
        } else if ("TABLE_COLUMN_GRAPHIC".equals(string)) {
            this.label.setGraphic(this.column.getGraphic());
        }
    }

    protected TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> getTableViewSkin() {
        return this.skin;
    }

    NestedTableColumnHeader getNestedColumnHeader() {
        return this.nestedColumnHeader;
    }

    void setNestedColumnHeader(NestedTableColumnHeader nestedTableColumnHeader) {
        this.nestedColumnHeader = nestedTableColumnHeader;
    }

    public TableColumnBase getTableColumn() {
        return this.column;
    }

    TableHeaderRow getTableHeaderRow() {
        return this.tableHeaderRow;
    }

    void setTableHeaderRow(TableHeaderRow tableHeaderRow) {
        this.tableHeaderRow = tableHeaderRow;
    }

    NestedTableColumnHeader getParentHeader() {
        return this.parentHeader;
    }

    void setParentHeader(NestedTableColumnHeader nestedTableColumnHeader) {
        this.parentHeader = nestedTableColumnHeader;
    }

    @Override
    protected void layoutChildren() {
        if (this.isSizeDirty) {
            this.resize(this.getTableColumn().getWidth(), this.getHeight());
            this.isSizeDirty = false;
        }
        double d2 = 0.0;
        double d3 = this.snapSize(this.getWidth()) - (this.snappedLeftInset() + this.snappedRightInset());
        double d4 = this.getHeight() - (this.snappedTopInset() + this.snappedBottomInset());
        double d5 = d3;
        if (this.arrow != null) {
            this.arrow.setMaxSize(this.arrow.prefWidth(-1.0), this.arrow.prefHeight(-1.0));
        }
        if (this.sortArrow != null && this.sortArrow.isVisible()) {
            d2 = this.sortArrow.prefWidth(-1.0);
            this.sortArrow.resize(d2, this.sortArrow.prefHeight(-1.0));
            this.positionInArea(this.sortArrow, d5 -= d2, this.snappedTopInset(), d2, d4, 0.0, HPos.CENTER, VPos.CENTER);
        }
        if (this.label != null) {
            double d6 = d3 - d2;
            this.label.resizeRelocate(this.snappedLeftInset(), 0.0, d6, this.getHeight());
        }
    }

    @Override
    protected double computePrefWidth(double d2) {
        if (this.getNestedColumnHeader() != null) {
            double d3 = this.getNestedColumnHeader().prefWidth(d2);
            if (this.column != null) {
                this.column.impl_setWidth(d3);
            }
            return d3;
        }
        if (this.column != null && this.column.isVisible()) {
            return this.column.getWidth();
        }
        return 0.0;
    }

    @Override
    protected double computeMinHeight(double d2) {
        return this.label == null ? 0.0 : this.label.minHeight(d2);
    }

    @Override
    protected double computePrefHeight(double d2) {
        if (this.getTableColumn() == null) {
            return 0.0;
        }
        return Math.max(this.getSize(), this.label.prefHeight(-1.0));
    }

    private void updateAllHeaders(TableColumnHeader tableColumnHeader) {
        if (tableColumnHeader instanceof NestedTableColumnHeader) {
            ObservableList<TableColumnHeader> observableList = ((NestedTableColumnHeader)tableColumnHeader).getColumnHeaders();
            for (int i2 = 0; i2 < observableList.size(); ++i2) {
                this.updateAllHeaders((TableColumnHeader)observableList.get(i2));
            }
        } else {
            tableColumnHeader.updateSortPosition();
        }
    }

    private void updateStyleClass() {
        this.getStyleClass().setAll("column-header");
        this.getStyleClass().addAll((Collection<String>)this.column.getStyleClass());
    }

    private void updateScene() {
        if (!this.autoSizeComplete) {
            if (this.getTableColumn() == null || this.getTableColumn().getWidth() != 80.0 || this.getScene() == null) {
                return;
            }
            this.doColumnAutoSize(this.getTableColumn(), 30);
            this.autoSizeComplete = true;
        }
    }

    void dispose() {
        TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> tableViewSkinBase = this.getTableViewSkin();
        if (tableViewSkinBase != null) {
            tableViewSkinBase.getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
            tableViewSkinBase.getSortOrder().removeListener(this.weakSortOrderListener);
        }
        this.changeListenerHandler.dispose();
    }

    private boolean isSortingEnabled() {
        return true;
    }

    private boolean isColumnReorderingEnabled() {
        return !BehaviorSkinBase.IS_TOUCH_SUPPORTED && this.getTableViewSkin().getVisibleLeafColumns().size() > 1;
    }

    private void initUI() {
        if (this.column == null) {
            return;
        }
        this.setOnMousePressed(mousePressedHandler);
        this.setOnMouseDragged(mouseDraggedHandler);
        this.setOnDragDetected(mouseEvent -> mouseEvent.consume());
        this.setOnContextMenuRequested(contextMenuRequestedHandler);
        this.setOnMouseReleased(mouseReleasedHandler);
        this.label = new Label();
        this.label.setText(this.column.getText());
        this.label.setGraphic(this.column.getGraphic());
        this.label.setVisible(this.column.isVisible());
        if (this.isSortingEnabled()) {
            this.updateSortGrid();
        }
    }

    private void doColumnAutoSize(TableColumnBase<?, ?> tableColumnBase, int n2) {
        double d2 = tableColumnBase.getPrefWidth();
        if (d2 == 80.0) {
            this.getTableViewSkin().resizeColumnToFitContent(tableColumnBase, n2);
        }
    }

    private void updateSortPosition() {
        this.sortPos = !this.column.isSortable() ? -1 : this.getSortPosition();
        this.updateSortGrid();
    }

    private void updateSortGrid() {
        if (this instanceof NestedTableColumnHeader) {
            return;
        }
        this.getChildren().clear();
        this.getChildren().add(this.label);
        if (!this.isSortingEnabled()) {
            return;
        }
        boolean bl = this.isSortColumn = this.sortPos != -1;
        if (!this.isSortColumn) {
            if (this.sortArrow != null) {
                this.sortArrow.setVisible(false);
            }
            return;
        }
        int n2 = this.skin.getVisibleLeafIndex(this.getTableColumn());
        if (n2 == -1) {
            return;
        }
        int n3 = this.getVisibleSortOrderColumnCount();
        boolean bl2 = this.sortPos <= 3 && n3 > 1;
        Node node = null;
        if (this.getTableColumn().getSortNode() != null) {
            node = this.getTableColumn().getSortNode();
            this.getChildren().add(node);
        } else {
            GridPane gridPane = new GridPane();
            node = gridPane;
            gridPane.setPadding(new Insets(0.0, 3.0, 0.0, 0.0));
            this.getChildren().add(gridPane);
            if (this.arrow == null) {
                this.arrow = new Region();
                this.arrow.getStyleClass().setAll("arrow");
                this.arrow.setVisible(true);
                this.arrow.setRotate(TableColumnSortTypeWrapper.isAscending(this.column) ? 180.0 : 0.0);
                this.changeListenerHandler.registerChangeListener(TableColumnSortTypeWrapper.getSortTypeProperty(this.column), "TABLE_COLUMN_SORT_TYPE");
            }
            this.arrow.setVisible(this.isSortColumn);
            if (this.sortPos > 2) {
                if (this.sortOrderLabel == null) {
                    this.sortOrderLabel = new Label();
                    this.sortOrderLabel.getStyleClass().add("sort-order");
                }
                this.sortOrderLabel.setText("" + (this.sortPos + 1));
                this.sortOrderLabel.setVisible(n3 > 1);
                gridPane.add(this.arrow, 1, 1);
                GridPane.setHgrow(this.arrow, Priority.NEVER);
                GridPane.setVgrow(this.arrow, Priority.NEVER);
                gridPane.add(this.sortOrderLabel, 2, 1);
            } else if (bl2) {
                boolean bl3;
                if (this.sortOrderDots == null) {
                    this.sortOrderDots = new HBox(0.0);
                    this.sortOrderDots.getStyleClass().add("sort-order-dots-container");
                }
                int n4 = (bl3 = TableColumnSortTypeWrapper.isAscending(this.column)) ? 1 : 2;
                int n5 = bl3 ? 2 : 1;
                gridPane.add(this.arrow, 1, n4);
                GridPane.setHalignment(this.arrow, HPos.CENTER);
                gridPane.add(this.sortOrderDots, 1, n5);
                this.updateSortOrderDots(this.sortPos);
            } else {
                gridPane.add(this.arrow, 1, 1);
                GridPane.setHgrow(this.arrow, Priority.NEVER);
                GridPane.setVgrow(this.arrow, Priority.ALWAYS);
            }
        }
        this.sortArrow = node;
        if (this.sortArrow != null) {
            this.sortArrow.setVisible(this.isSortColumn);
        }
        this.requestLayout();
    }

    private void updateSortOrderDots(int n2) {
        double d2 = this.arrow.prefWidth(-1.0);
        this.sortOrderDots.getChildren().clear();
        for (int i2 = 0; i2 <= n2; ++i2) {
            Region region = new Region();
            region.getStyleClass().add("sort-order-dot");
            String string = TableColumnSortTypeWrapper.getSortTypeName(this.column);
            if (string != null && !string.isEmpty()) {
                region.getStyleClass().add(string.toLowerCase(Locale.ROOT));
            }
            this.sortOrderDots.getChildren().add(region);
            if (i2 >= n2) continue;
            Region region2 = new Region();
            double d3 = n2 == 1 ? 1.0 : 1.0;
            double d4 = n2 == 1 ? 1.0 : 0.0;
            region2.setPadding(new Insets(0.0, d3, 0.0, d4));
            this.sortOrderDots.getChildren().add(region2);
        }
        this.sortOrderDots.setAlignment(Pos.TOP_CENTER);
        this.sortOrderDots.setMaxWidth(d2);
    }

    void moveColumn(TableColumnBase tableColumnBase, int n2) {
        int n3;
        if (tableColumnBase == null || n2 < 0) {
            return;
        }
        ObservableList<TableColumnBase<?, ?>> observableList = this.getColumns(tableColumnBase);
        int n4 = observableList.size();
        int n5 = observableList.indexOf(tableColumnBase);
        int n6 = n3 = n2;
        for (int i2 = 0; i2 <= n6 && i2 < n4; ++i2) {
            n3 += ((TableColumnBase)observableList.get(i2)).isVisible() ? 0 : 1;
        }
        if (n3 >= n4) {
            n3 = n4 - 1;
        } else if (n3 < 0) {
            n3 = 0;
        }
        if (n3 == n5) {
            return;
        }
        ArrayList arrayList = new ArrayList(observableList);
        arrayList.remove(tableColumnBase);
        arrayList.add(n3, tableColumnBase);
        observableList.setAll(arrayList);
    }

    private ObservableList<TableColumnBase<?, ?>> getColumns(TableColumnBase tableColumnBase) {
        return tableColumnBase.getParentColumn() == null ? this.getTableViewSkin().getColumns() : tableColumnBase.getParentColumn().getColumns();
    }

    private int getIndex(TableColumnBase<?, ?> tableColumnBase) {
        if (tableColumnBase == null) {
            return -1;
        }
        ObservableList<TableColumnBase<?, ?>> observableList = this.getColumns(tableColumnBase);
        int n2 = -1;
        for (int i2 = 0; i2 < observableList.size(); ++i2) {
            TableColumnBase tableColumnBase2 = (TableColumnBase)observableList.get(i2);
            if (!tableColumnBase2.isVisible()) continue;
            ++n2;
            if (tableColumnBase.equals(tableColumnBase2)) break;
        }
        return n2;
    }

    private void updateColumnIndex() {
        TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> tableViewSkinBase = this.getTableViewSkin();
        TableColumnBase tableColumnBase = this.getTableColumn();
        this.columnIndex = tableViewSkinBase == null || tableColumnBase == null ? -1 : tableViewSkinBase.getVisibleLeafIndex(tableColumnBase);
        this.isLastVisibleColumn = this.getTableColumn() != null && this.columnIndex != -1 && this.columnIndex == this.getTableViewSkin().getVisibleLeafColumns().size() - 1;
        this.pseudoClassStateChanged(PSEUDO_CLASS_LAST_VISIBLE, this.isLastVisibleColumn);
    }

    private void sortColumn(boolean bl) {
        if (!this.isSortingEnabled()) {
            return;
        }
        if (this.column == null || this.column.getColumns().size() != 0 || this.column.getComparator() == null || !this.column.isSortable()) {
            return;
        }
        ObservableList<TableColumnBase<?, ?>> observableList = this.getTableViewSkin().getSortOrder();
        if (bl) {
            if (!this.isSortColumn) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
                observableList.add(this.column);
            } else if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
            } else {
                int n2 = observableList.indexOf(this.column);
                if (n2 != -1) {
                    observableList.remove(n2);
                }
            }
        } else if (this.isSortColumn && observableList.size() == 1) {
            if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
            } else {
                observableList.remove(this.column);
            }
        } else if (this.isSortColumn) {
            if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
            } else if (TableColumnSortTypeWrapper.isDescending(this.column)) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
            }
            ArrayList arrayList = new ArrayList(observableList);
            arrayList.remove(this.column);
            arrayList.add(0, this.column);
            observableList.setAll(this.column);
        } else {
            TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
            observableList.setAll(this.column);
        }
    }

    private int getSortPosition() {
        if (this.column == null) {
            return -1;
        }
        List<TableColumnBase> list = this.getVisibleSortOrderColumns();
        int n2 = 0;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            TableColumnBase tableColumnBase = list.get(i2);
            if (this.column.equals(tableColumnBase)) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    private List<TableColumnBase> getVisibleSortOrderColumns() {
        ObservableList<TableColumnBase<?, ?>> observableList = this.getTableViewSkin().getSortOrder();
        ArrayList<TableColumnBase> arrayList = new ArrayList<TableColumnBase>();
        for (int i2 = 0; i2 < observableList.size(); ++i2) {
            TableColumnBase tableColumnBase = (TableColumnBase)observableList.get(i2);
            if (tableColumnBase == null || !tableColumnBase.isSortable() || !tableColumnBase.isVisible()) continue;
            arrayList.add(tableColumnBase);
        }
        return arrayList;
    }

    private int getVisibleSortOrderColumnCount() {
        return this.getVisibleSortOrderColumns().size();
    }

    void columnReorderingStarted(double d2) {
        if (!this.column.impl_isReorderable()) {
            return;
        }
        this.dragOffset = d2;
        this.getTableHeaderRow().setReorderingColumn(this.column);
        this.getTableHeaderRow().setReorderingRegion(this);
    }

    void columnReordering(double d2, double d3) {
        if (!this.column.impl_isReorderable()) {
            return;
        }
        this.getTableHeaderRow().setReordering(true);
        TableColumnHeader tableColumnHeader = null;
        double d4 = this.getParentHeader().sceneToLocal(d2, d3).getX();
        double d5 = ((Node)((Object)this.getTableViewSkin().getSkinnable())).sceneToLocal(d2, d3).getX() - this.dragOffset;
        this.getTableHeaderRow().setDragHeaderX(d5);
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = 0.0;
        this.newColumnPos = 0;
        for (TableColumnHeader tableColumnHeader2 : this.getParentHeader().getColumnHeaders()) {
            if (!tableColumnHeader2.isVisible()) continue;
            double d9 = tableColumnHeader2.prefWidth(-1.0);
            d8 += d9;
            d6 = tableColumnHeader2.getBoundsInParent().getMinX();
            d7 = d6 + d9;
            if (d4 >= d6 && d4 < d7) {
                tableColumnHeader = tableColumnHeader2;
                break;
            }
            ++this.newColumnPos;
        }
        if (tableColumnHeader == null) {
            this.newColumnPos = d4 > d8 ? this.getParentHeader().getColumns().size() - 1 : 0;
            return;
        }
        double d10 = d6 + (d7 - d6) / 2.0;
        boolean bl = d4 <= d10;
        int n2 = this.getIndex(this.column);
        this.newColumnPos += this.newColumnPos > n2 && bl ? -1 : (this.newColumnPos < n2 && !bl ? 1 : 0);
        double d11 = this.getTableHeaderRow().sceneToLocal(tableColumnHeader.localToScene(tableColumnHeader.getBoundsInLocal())).getMinX();
        if ((d11 += bl ? 0.0 : tableColumnHeader.getWidth()) >= -0.5 && d11 <= ((Region)((Object)this.getTableViewSkin().getSkinnable())).getWidth()) {
            this.columnReorderLine.setTranslateX(d11);
            this.columnReorderLine.setVisible(true);
        }
        this.getTableHeaderRow().setReordering(true);
    }

    void columnReorderingComplete() {
        if (!this.column.impl_isReorderable()) {
            return;
        }
        this.moveColumn(this.getTableColumn(), this.newColumnPos);
        this.columnReorderLine.setTranslateX(0.0);
        this.columnReorderLine.setLayoutX(0.0);
        this.newColumnPos = 0;
        this.getTableHeaderRow().setReordering(false);
        this.columnReorderLine.setVisible(false);
        this.getTableHeaderRow().setReorderingColumn(null);
        this.getTableHeaderRow().setReorderingRegion(null);
        this.dragOffset = 0.0;
    }

    double getDragRectHeight() {
        return this.getHeight();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TableColumnHeader.getClassCssMetaData();
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case INDEX: {
                return this.getIndex(this.column);
            }
            case TEXT: {
                return this.column != null ? this.column.getText() : null;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    private static class StyleableProperties {
        private static final CssMetaData<TableColumnHeader, Number> SIZE = new CssMetaData<TableColumnHeader, Number>("-fx-size", SizeConverter.getInstance(), (Number)20.0){

            @Override
            public boolean isSettable(TableColumnHeader tableColumnHeader) {
                return tableColumnHeader.size == null || !tableColumnHeader.size.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TableColumnHeader tableColumnHeader) {
                return (StyleableProperty)((Object)tableColumnHeader.sizeProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Region.getClassCssMetaData());
            arrayList.add(SIZE);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

