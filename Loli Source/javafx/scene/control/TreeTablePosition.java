/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class TreeTablePosition<S, T>
extends TablePositionBase<TreeTableColumn<S, T>> {
    private final WeakReference<TreeTableView<S>> controlRef;
    private final WeakReference<TreeItem<S>> treeItemRef;
    int fixedColumnIndex = -1;

    public TreeTablePosition(@NamedArg(value="treeTableView") TreeTableView<S> treeTableView, @NamedArg(value="row") int n2, @NamedArg(value="tableColumn") TreeTableColumn<S, T> treeTableColumn) {
        super(n2, treeTableColumn);
        this.controlRef = new WeakReference<TreeTableView<S>>(treeTableView);
        this.treeItemRef = new WeakReference<TreeItem<S>>(treeTableView.getTreeItem(n2));
    }

    @Override
    public int getColumn() {
        if (this.fixedColumnIndex > -1) {
            return this.fixedColumnIndex;
        }
        TreeTableView<S> treeTableView = this.getTreeTableView();
        TableColumnBase tableColumnBase = this.getTableColumn();
        return treeTableView == null || tableColumnBase == null ? -1 : treeTableView.getVisibleLeafIndex((TreeTableColumn<S, ?>)tableColumnBase);
    }

    public final TreeTableView<S> getTreeTableView() {
        return (TreeTableView)this.controlRef.get();
    }

    @Override
    public final TreeTableColumn<S, T> getTableColumn() {
        return (TreeTableColumn)super.getTableColumn();
    }

    public final TreeItem<S> getTreeItem() {
        return (TreeItem)this.treeItemRef.get();
    }
}

