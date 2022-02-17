/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.TableRowBehaviorBase;
import javafx.collections.ObservableList;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;

public class TreeTableRowBehavior<T>
extends TableRowBehaviorBase<TreeTableRow<T>> {
    public TreeTableRowBehavior(TreeTableRow<T> treeTableRow) {
        super(treeTableRow);
    }

    @Override
    protected TableSelectionModel<TreeItem<T>> getSelectionModel() {
        return ((TreeTableView)this.getCellContainer()).getSelectionModel();
    }

    protected TableFocusModel<TreeItem<T>, ?> getFocusModel() {
        return ((TreeTableView)this.getCellContainer()).getFocusModel();
    }

    @Override
    protected TreeTableView<T> getCellContainer() {
        return ((TreeTableRow)this.getControl()).getTreeTableView();
    }

    @Override
    protected TablePositionBase<?> getFocusedCell() {
        return ((TreeTableView)this.getCellContainer()).getFocusModel().getFocusedCell();
    }

    @Override
    protected ObservableList getVisibleLeafColumns() {
        return ((TreeTableView)this.getCellContainer()).getVisibleLeafColumns();
    }

    @Override
    protected void edit(TreeTableRow<T> treeTableRow) {
    }

    @Override
    protected void handleClicks(MouseButton mouseButton, int n2, boolean bl) {
        TreeItem treeItem = ((TreeTableRow)this.getControl()).getTreeItem();
        if (mouseButton == MouseButton.PRIMARY) {
            if (n2 == 1 && bl) {
                this.edit((TreeTableRow)this.getControl());
            } else if (n2 == 1) {
                this.edit((TreeTableRow<T>)null);
            } else if (n2 == 2 && treeItem.isLeaf()) {
                this.edit((TreeTableRow)this.getControl());
            } else if (n2 % 2 == 0) {
                treeItem.setExpanded(!treeItem.isExpanded());
            }
        }
    }
}

