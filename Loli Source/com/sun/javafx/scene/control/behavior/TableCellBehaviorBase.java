/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import java.util.Collections;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.MouseButton;

public abstract class TableCellBehaviorBase<S, T, TC extends TableColumnBase<S, ?>, C extends IndexedCell<T>>
extends CellBehaviorBase<C> {
    public TableCellBehaviorBase(C c2) {
        super(c2, Collections.emptyList());
    }

    protected abstract TableColumnBase<S, T> getTableColumn();

    protected abstract int getItemCount();

    @Override
    protected abstract TableSelectionModel<S> getSelectionModel();

    protected abstract TableFocusModel<S, TC> getFocusModel();

    protected abstract TablePositionBase getFocusedCell();

    protected abstract boolean isTableRowSelected();

    protected abstract int getVisibleLeafIndex(TableColumnBase<S, T> var1);

    protected abstract void focus(int var1, TableColumnBase<S, T> var2);

    @Override
    protected void doSelect(double d2, double d3, MouseButton mouseButton, int n2, boolean bl, boolean bl2) {
        IndexedCell indexedCell = (IndexedCell)this.getControl();
        if (!indexedCell.contains(d2, d3)) {
            return;
        }
        Control control = this.getCellContainer();
        if (control == null) {
            return;
        }
        int n3 = this.getItemCount();
        if (indexedCell.getIndex() >= n3) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = this.getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        boolean bl3 = this.isSelected();
        int n4 = indexedCell.getIndex();
        int n5 = this.getColumn();
        TableColumnBase<S, T> tableColumnBase = this.getTableColumn();
        FocusModel focusModel = this.getFocusModel();
        if (focusModel == null) {
            return;
        }
        TablePositionBase tablePositionBase = this.getFocusedCell();
        if (this.handleDisclosureNode(d2, d3)) {
            return;
        }
        if (bl) {
            if (!TableCellBehaviorBase.hasNonDefaultAnchor(control)) {
                TableCellBehaviorBase.setAnchor(control, tablePositionBase, false);
            }
        } else {
            TableCellBehaviorBase.removeAnchor(control);
        }
        if (mouseButton == MouseButton.PRIMARY || mouseButton == MouseButton.SECONDARY && !bl3) {
            if (multipleSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
                this.simpleSelect(mouseButton, n2, bl2);
            } else if (bl2) {
                if (bl3) {
                    ((TableSelectionModel)multipleSelectionModel).clearSelection(n4, tableColumnBase);
                    ((TableFocusModel)focusModel).focus(n4, tableColumnBase);
                } else {
                    ((TableSelectionModel)multipleSelectionModel).select(n4, tableColumnBase);
                }
            } else if (bl) {
                TableColumnBase<S, T> tableColumnBase2;
                TablePositionBase tablePositionBase2 = TableCellBehaviorBase.getAnchor(control, tablePositionBase);
                int n6 = tablePositionBase2.getRow();
                boolean bl4 = n6 < n4;
                ((TableSelectionModel)multipleSelectionModel).clearSelection();
                int n7 = Math.min(n6, n4);
                int n8 = Math.max(n6, n4);
                TableColumnBase<S, T> tableColumnBase3 = tablePositionBase2.getColumn() < n5 ? tablePositionBase2.getTableColumn() : tableColumnBase;
                TableColumnBase<S, T> tableColumnBase4 = tableColumnBase2 = tablePositionBase2.getColumn() >= n5 ? tablePositionBase2.getTableColumn() : tableColumnBase;
                if (bl4) {
                    ((TableSelectionModel)multipleSelectionModel).selectRange(n7, tableColumnBase3, n8, tableColumnBase2);
                } else {
                    ((TableSelectionModel)multipleSelectionModel).selectRange(n8, tableColumnBase3, n7, tableColumnBase2);
                }
            } else {
                this.simpleSelect(mouseButton, n2, bl2);
            }
        }
    }

    @Override
    protected void simpleSelect(MouseButton mouseButton, int n2, boolean bl) {
        TableColumnBase<S, T> tableColumnBase;
        int n3;
        MultipleSelectionModel multipleSelectionModel = this.getSelectionModel();
        boolean bl2 = ((TableSelectionModel)multipleSelectionModel).isSelected(n3 = ((IndexedCell)this.getControl()).getIndex(), tableColumnBase = this.getTableColumn());
        if (bl2 && bl) {
            ((TableSelectionModel)multipleSelectionModel).clearSelection(n3, tableColumnBase);
            ((TableFocusModel)this.getFocusModel()).focus(n3, tableColumnBase);
            bl2 = false;
        } else {
            ((TableSelectionModel)multipleSelectionModel).clearAndSelect(n3, tableColumnBase);
        }
        this.handleClicks(mouseButton, n2, bl2);
    }

    private int getColumn() {
        if (((TableSelectionModel)this.getSelectionModel()).isCellSelectionEnabled()) {
            TableColumnBase<S, T> tableColumnBase = this.getTableColumn();
            return this.getVisibleLeafIndex(tableColumnBase);
        }
        return -1;
    }

    @Override
    protected boolean isSelected() {
        MultipleSelectionModel multipleSelectionModel = this.getSelectionModel();
        if (multipleSelectionModel == null) {
            return false;
        }
        if (((TableSelectionModel)multipleSelectionModel).isCellSelectionEnabled()) {
            IndexedCell indexedCell = (IndexedCell)this.getControl();
            return indexedCell.isSelected();
        }
        return this.isTableRowSelected();
    }
}

