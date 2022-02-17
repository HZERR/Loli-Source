/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.scene.control.behavior.TableRowBehavior;
import com.sun.javafx.scene.control.skin.TableRowSkinBase;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class TableRowSkin<T>
extends TableRowSkinBase<T, TableRow<T>, CellBehaviorBase<TableRow<T>>, TableCell<T, ?>> {
    private TableView<T> tableView;
    private TableViewSkin<T> tableViewSkin;

    public TableRowSkin(TableRow<T> tableRow) {
        super(tableRow, new TableRowBehavior<T>(tableRow));
        this.tableView = tableRow.getTableView();
        this.updateTableViewSkin();
        super.init(tableRow);
        this.registerChangeListener(tableRow.tableViewProperty(), "TABLE_VIEW");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("TABLE_VIEW".equals(string)) {
            this.updateTableViewSkin();
            int n2 = this.cells.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node = (Node)this.cells.get(i2);
                if (!(node instanceof TableCell)) continue;
                ((TableCell)node).updateTableView(((TableRow)this.getSkinnable()).getTableView());
            }
            this.tableView = ((TableRow)this.getSkinnable()).getTableView();
        }
    }

    @Override
    protected TableCell<T, ?> getCell(TableColumnBase tableColumnBase) {
        TableColumn tableColumn = (TableColumn)tableColumnBase;
        TableCell tableCell = tableColumn.getCellFactory().call(tableColumn);
        tableCell.updateTableColumn(tableColumn);
        tableCell.updateTableView(tableColumn.getTableView());
        tableCell.updateTableRow((TableRow)this.getSkinnable());
        return tableCell;
    }

    @Override
    protected ObservableList<TableColumn<T, ?>> getVisibleLeafColumns() {
        return this.tableView.getVisibleLeafColumns();
    }

    @Override
    protected void updateCell(TableCell<T, ?> tableCell, TableRow<T> tableRow) {
        tableCell.updateTableRow(tableRow);
    }

    @Override
    protected DoubleProperty fixedCellSizeProperty() {
        return this.tableView.fixedCellSizeProperty();
    }

    @Override
    protected boolean isColumnPartiallyOrFullyVisible(TableColumnBase tableColumnBase) {
        return this.tableViewSkin == null ? false : this.tableViewSkin.isColumnPartiallyOrFullyVisible((TableColumn)tableColumnBase);
    }

    @Override
    protected TableColumn<T, ?> getTableColumnBase(TableCell<T, ?> tableCell) {
        return tableCell.getTableColumn();
    }

    @Override
    protected ObjectProperty<Node> graphicProperty() {
        return null;
    }

    @Override
    protected Control getVirtualFlowOwner() {
        return ((TableRow)this.getSkinnable()).getTableView();
    }

    private void updateTableViewSkin() {
        TableView tableView = ((TableRow)this.getSkinnable()).getTableView();
        if (tableView.getSkin() instanceof TableViewSkin) {
            this.tableViewSkin = (TableViewSkin)tableView.getSkin();
        }
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case SELECTED_ITEMS: {
                ArrayList<TableCell> arrayList = new ArrayList<TableCell>();
                int n2 = ((TableRow)this.getSkinnable()).getIndex();
                Iterator iterator = this.tableView.getSelectionModel().getSelectedCells().iterator();
                if (iterator.hasNext()) {
                    TablePosition tablePosition = (TablePosition)iterator.next();
                    if (tablePosition.getRow() == n2) {
                        TableCell tableCell;
                        TableColumn<T, ?> tableColumn = tablePosition.getTableColumn();
                        if (tableColumn == null) {
                            tableColumn = this.tableView.getVisibleLeafColumn(0);
                        }
                        if ((tableCell = (TableCell)((Reference)this.cellsMap.get(tableColumn)).get()) != null) {
                            arrayList.add(tableCell);
                        }
                    }
                    return FXCollections.observableArrayList(arrayList);
                }
            }
            case CELL_AT_ROW_COLUMN: {
                int n3 = (Integer)arrobject[1];
                TableColumn<T, ?> tableColumn = this.tableView.getVisibleLeafColumn(n3);
                if (this.cellsMap.containsKey(tableColumn)) {
                    return ((Reference)this.cellsMap.get(tableColumn)).get();
                }
                return null;
            }
            case FOCUS_ITEM: {
                TableView.TableViewFocusModel<T> tableViewFocusModel = this.tableView.getFocusModel();
                TablePosition tablePosition = tableViewFocusModel.getFocusedCell();
                TableColumn<T, ?> tableColumn = tablePosition.getTableColumn();
                if (tableColumn == null) {
                    tableColumn = this.tableView.getVisibleLeafColumn(0);
                }
                if (this.cellsMap.containsKey(tableColumn)) {
                    return ((Reference)this.cellsMap.get(tableColumn)).get();
                }
                return null;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

