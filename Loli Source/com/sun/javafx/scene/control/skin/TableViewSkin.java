/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TableViewBehavior;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class TableViewSkin<T>
extends TableViewSkinBase<T, T, TableView<T>, TableViewBehavior<T>, TableRow<T>, TableColumn<T, ?>> {
    private final TableView<T> tableView;

    public TableViewSkin(TableView<T> tableView) {
        super(tableView, new TableViewBehavior<T>(tableView));
        this.tableView = tableView;
        this.flow.setFixedCellSize(tableView.getFixedCellSize());
        super.init(tableView);
        EventHandler<MouseEvent> eventHandler = mouseEvent -> {
            if (tableView.getEditingCell() != null) {
                tableView.edit(-1, null);
            }
            if (tableView.isFocusTraversable()) {
                tableView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        TableViewBehavior tableViewBehavior = (TableViewBehavior)this.getBehavior();
        tableViewBehavior.setOnFocusPreviousRow(() -> this.onFocusPreviousCell());
        tableViewBehavior.setOnFocusNextRow(() -> this.onFocusNextCell());
        tableViewBehavior.setOnMoveToFirstCell(() -> this.onMoveToFirstCell());
        tableViewBehavior.setOnMoveToLastCell(() -> this.onMoveToLastCell());
        tableViewBehavior.setOnScrollPageDown(bl -> this.onScrollPageDown((boolean)bl));
        tableViewBehavior.setOnScrollPageUp(bl -> this.onScrollPageUp((boolean)bl));
        tableViewBehavior.setOnSelectPreviousRow(() -> this.onSelectPreviousCell());
        tableViewBehavior.setOnSelectNextRow(() -> this.onSelectNextCell());
        tableViewBehavior.setOnSelectLeftCell(() -> this.onSelectLeftCell());
        tableViewBehavior.setOnSelectRightCell(() -> this.onSelectRightCell());
        this.registerChangeListener(tableView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("FIXED_CELL_SIZE".equals(string)) {
            this.flow.setFixedCellSize(((TableView)this.getSkinnable()).getFixedCellSize());
        }
    }

    @Override
    protected ObservableList<TableColumn<T, ?>> getVisibleLeafColumns() {
        return this.tableView.getVisibleLeafColumns();
    }

    @Override
    protected int getVisibleLeafIndex(TableColumn<T, ?> tableColumn) {
        return this.tableView.getVisibleLeafIndex(tableColumn);
    }

    @Override
    protected TableColumn<T, ?> getVisibleLeafColumn(int n2) {
        return this.tableView.getVisibleLeafColumn(n2);
    }

    protected TableView.TableViewFocusModel<T> getFocusModel() {
        return this.tableView.getFocusModel();
    }

    protected TablePosition<T, ?> getFocusedCell() {
        return this.tableView.getFocusModel().getFocusedCell();
    }

    @Override
    protected TableSelectionModel<T> getSelectionModel() {
        return this.tableView.getSelectionModel();
    }

    @Override
    protected ObjectProperty<Callback<TableView<T>, TableRow<T>>> rowFactoryProperty() {
        return this.tableView.rowFactoryProperty();
    }

    @Override
    protected ObjectProperty<Node> placeholderProperty() {
        return this.tableView.placeholderProperty();
    }

    @Override
    protected ObjectProperty<ObservableList<T>> itemsProperty() {
        return this.tableView.itemsProperty();
    }

    @Override
    protected ObservableList<TableColumn<T, ?>> getColumns() {
        return this.tableView.getColumns();
    }

    @Override
    protected BooleanProperty tableMenuButtonVisibleProperty() {
        return this.tableView.tableMenuButtonVisibleProperty();
    }

    @Override
    protected ObjectProperty<Callback<ResizeFeaturesBase, Boolean>> columnResizePolicyProperty() {
        return this.tableView.columnResizePolicyProperty();
    }

    @Override
    protected ObservableList<TableColumn<T, ?>> getSortOrder() {
        return this.tableView.getSortOrder();
    }

    @Override
    protected boolean resizeColumn(TableColumn<T, ?> tableColumn, double d2) {
        return this.tableView.resizeColumn(tableColumn, d2);
    }

    @Override
    protected void edit(int n2, TableColumn<T, ?> tableColumn) {
        this.tableView.edit(n2, tableColumn);
    }

    @Override
    protected void resizeColumnToFitContent(TableColumn<T, ?> tableColumn, int n2) {
        Node node;
        if (!tableColumn.isResizable()) {
            return;
        }
        List list = (List)this.itemsProperty().get();
        if (list == null || list.isEmpty()) {
            return;
        }
        Callback<TableColumn<T, ?>, TableCell<T, ?>> callback = tableColumn.getCellFactory();
        if (callback == null) {
            return;
        }
        TableCell<T, ?> tableCell = callback.call(tableColumn);
        if (tableCell == null) {
            return;
        }
        tableCell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);
        double d2 = 10.0;
        Node node2 = node = tableCell.getSkin() == null ? null : tableCell.getSkin().getNode();
        if (node instanceof Region) {
            Region region = (Region)node;
            d2 = region.snappedLeftInset() + region.snappedRightInset();
        }
        int n3 = n2 == -1 ? list.size() : Math.min(list.size(), n2);
        double d3 = 0.0;
        for (int i2 = 0; i2 < n3; ++i2) {
            tableCell.updateTableColumn(tableColumn);
            tableCell.updateTableView(this.tableView);
            tableCell.updateIndex(i2);
            if ((tableCell.getText() == null || tableCell.getText().isEmpty()) && tableCell.getGraphic() == null) continue;
            this.getChildren().add(tableCell);
            tableCell.applyCss();
            d3 = Math.max(d3, tableCell.prefWidth(-1.0));
            this.getChildren().remove(tableCell);
        }
        tableCell.updateIndex(-1);
        TableColumnHeader tableColumnHeader = this.getTableHeaderRow().getColumnHeaderFor(tableColumn);
        double d4 = Utils.computeTextWidth(tableColumnHeader.label.getFont(), tableColumn.getText(), -1.0);
        Node node3 = tableColumnHeader.label.getGraphic();
        double d5 = node3 == null ? 0.0 : node3.prefWidth(-1.0) + tableColumnHeader.label.getGraphicTextGap();
        double d6 = d4 + d5 + 10.0 + tableColumnHeader.snappedLeftInset() + tableColumnHeader.snappedRightInset();
        d3 = Math.max(d3, d6);
        d3 += d2;
        if (this.tableView.getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY) {
            d3 = Math.max(d3, tableColumn.getWidth());
        }
        tableColumn.impl_setWidth(d3);
    }

    @Override
    public int getItemCount() {
        return this.tableView.getItems() == null ? 0 : this.tableView.getItems().size();
    }

    @Override
    public TableRow<T> createCell() {
        TableRow<T> tableRow = this.tableView.getRowFactory() != null ? this.tableView.getRowFactory().call(this.tableView) : new TableRow<T>();
        tableRow.updateTableView(this.tableView);
        return tableRow;
    }

    @Override
    protected void horizontalScroll() {
        super.horizontalScroll();
        if (((TableView)this.getSkinnable()).getFixedCellSize() > 0.0) {
            this.flow.requestCellLayout();
        }
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case SELECTED_ITEMS: {
                ArrayList<TableRow> arrayList = new ArrayList<TableRow>();
                TableView.TableViewSelectionModel tableViewSelectionModel = ((TableView)this.getSkinnable()).getSelectionModel();
                for (TablePosition tablePosition : tableViewSelectionModel.getSelectedCells()) {
                    TableRow tableRow = (TableRow)this.flow.getPrivateCell(tablePosition.getRow());
                    if (tableRow == null) continue;
                    arrayList.add(tableRow);
                }
                return FXCollections.observableArrayList(arrayList);
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    @Override
    protected void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case SHOW_ITEM: {
                Node node = (Node)arrobject[0];
                if (!(node instanceof TableCell)) break;
                TableCell tableCell = (TableCell)node;
                this.flow.show(tableCell.getIndex());
                break;
            }
            case SET_SELECTED_ITEMS: {
                TableView.TableViewSelectionModel tableViewSelectionModel;
                ObservableList observableList = (ObservableList)arrobject[0];
                if (observableList == null || (tableViewSelectionModel = ((TableView)this.getSkinnable()).getSelectionModel()) == null) break;
                tableViewSelectionModel.clearSelection();
                for (Node node : observableList) {
                    if (!(node instanceof TableCell)) continue;
                    TableCell tableCell = (TableCell)node;
                    ((TableSelectionModel)tableViewSelectionModel).select(tableCell.getIndex(), tableCell.getTableColumn());
                }
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }
}

