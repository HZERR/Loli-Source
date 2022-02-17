/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TreeTableCellBehavior;
import com.sun.javafx.scene.control.skin.TableCellSkinBase;
import com.sun.javafx.scene.control.skin.TableRowSkinBase;
import com.sun.javafx.scene.control.skin.TreeTableRowSkin;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

public class TreeTableCellSkin<S, T>
extends TableCellSkinBase<TreeTableCell<S, T>, TreeTableCellBehavior<S, T>> {
    private final TreeTableCell<S, T> treeTableCell;
    private final TreeTableColumn<S, T> tableColumn;

    public TreeTableCellSkin(TreeTableCell<S, T> treeTableCell) {
        super(treeTableCell, new TreeTableCellBehavior<S, T>(treeTableCell));
        this.treeTableCell = treeTableCell;
        this.tableColumn = treeTableCell.getTableColumn();
        super.init(treeTableCell);
    }

    @Override
    protected BooleanProperty columnVisibleProperty() {
        return this.tableColumn.visibleProperty();
    }

    @Override
    protected ReadOnlyDoubleProperty columnWidthProperty() {
        return this.tableColumn.widthProperty();
    }

    @Override
    protected double leftLabelPadding() {
        double d2 = super.leftLabelPadding();
        double d3 = this.getCellSize();
        TreeTableCell treeTableCell = (TreeTableCell)this.getSkinnable();
        TreeTableColumn treeTableColumn = treeTableCell.getTableColumn();
        if (treeTableColumn == null) {
            return d2;
        }
        TreeTableView treeTableView = treeTableCell.getTreeTableView();
        if (treeTableView == null) {
            return d2;
        }
        int n2 = treeTableView.getVisibleLeafIndex(treeTableColumn);
        TreeTableColumn treeTableColumn2 = treeTableView.getTreeColumn();
        if (treeTableColumn2 == null && n2 != 0 || treeTableColumn2 != null && !treeTableColumn.equals(treeTableColumn2)) {
            return d2;
        }
        TreeTableRow treeTableRow = treeTableCell.getTreeTableRow();
        if (treeTableRow == null) {
            return d2;
        }
        TreeItem treeItem = treeTableRow.getTreeItem();
        if (treeItem == null) {
            return d2;
        }
        int n3 = treeTableView.getTreeItemLevel(treeItem);
        if (!treeTableView.isShowRoot()) {
            --n3;
        }
        double d4 = 10.0;
        if (treeTableRow.getSkin() instanceof TreeTableRowSkin) {
            d4 = ((TreeTableRowSkin)treeTableRow.getSkin()).getIndentationPerLevel();
        }
        d2 += (double)n3 * d4;
        Map<Control, Double> map = TableRowSkinBase.maxDisclosureWidthMap;
        d2 += map.containsKey(treeTableView) ? map.get(treeTableView) : 0.0;
        Node node = treeItem.getGraphic();
        return d2 += node == null ? 0.0 : node.prefWidth(d3);
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        if (this.isDeferToParentForPrefWidth) {
            return super.computePrefWidth(d2, d3, d4, d5, d6);
        }
        return this.columnWidthProperty().get();
    }
}

