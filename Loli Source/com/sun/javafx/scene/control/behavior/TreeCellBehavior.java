/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import java.util.Collections;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;

public class TreeCellBehavior<T>
extends CellBehaviorBase<TreeCell<T>> {
    public TreeCellBehavior(TreeCell<T> treeCell) {
        super(treeCell, Collections.emptyList());
    }

    @Override
    protected MultipleSelectionModel<TreeItem<T>> getSelectionModel() {
        return ((TreeView)this.getCellContainer()).getSelectionModel();
    }

    @Override
    protected FocusModel<TreeItem<T>> getFocusModel() {
        return ((TreeView)this.getCellContainer()).getFocusModel();
    }

    @Override
    protected TreeView<T> getCellContainer() {
        return ((TreeCell)this.getControl()).getTreeView();
    }

    @Override
    protected void edit(TreeCell<T> treeCell) {
        TreeItem<T> treeItem = treeCell == null ? null : treeCell.getTreeItem();
        ((TreeView)this.getCellContainer()).edit(treeItem);
    }

    @Override
    protected void handleClicks(MouseButton mouseButton, int n2, boolean bl) {
        TreeItem treeItem = ((TreeCell)this.getControl()).getTreeItem();
        if (mouseButton == MouseButton.PRIMARY) {
            if (n2 == 1 && bl) {
                this.edit((TreeCell)this.getControl());
            } else if (n2 == 1) {
                this.edit((TreeCell<T>)null);
            } else if (n2 == 2 && treeItem.isLeaf()) {
                this.edit((TreeCell)this.getControl());
            } else if (n2 % 2 == 0) {
                treeItem.setExpanded(!treeItem.isExpanded());
            }
        }
    }

    @Override
    protected boolean handleDisclosureNode(double d2, double d3) {
        TreeCell treeCell = (TreeCell)this.getControl();
        Node node = treeCell.getDisclosureNode();
        if (node != null && node.getBoundsInParent().contains(d2, d3)) {
            if (treeCell.getTreeItem() != null) {
                treeCell.getTreeItem().setExpanded(!treeCell.getTreeItem().isExpanded());
            }
            return true;
        }
        return false;
    }
}

