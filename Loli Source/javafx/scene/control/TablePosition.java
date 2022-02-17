/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;

public class TablePosition<S, T>
extends TablePositionBase<TableColumn<S, T>> {
    private final WeakReference<TableView<S>> controlRef;
    private final WeakReference<S> itemRef;
    int fixedColumnIndex = -1;

    public TablePosition(@NamedArg(value="tableView") TableView<S> tableView, @NamedArg(value="row") int n2, @NamedArg(value="tableColumn") TableColumn<S, T> tableColumn) {
        super(n2, tableColumn);
        this.controlRef = new WeakReference<TableView<S>>(tableView);
        ObservableList<S> observableList = tableView.getItems();
        this.itemRef = new WeakReference<Object>((observableList != null && n2 >= 0 && n2 < observableList.size() ? (Object)observableList.get(n2) : null));
    }

    @Override
    public int getColumn() {
        if (this.fixedColumnIndex > -1) {
            return this.fixedColumnIndex;
        }
        TableView<S> tableView = this.getTableView();
        TableColumnBase tableColumnBase = this.getTableColumn();
        return tableView == null || tableColumnBase == null ? -1 : tableView.getVisibleLeafIndex((TableColumn<S, ?>)tableColumnBase);
    }

    public final TableView<S> getTableView() {
        return (TableView)this.controlRef.get();
    }

    @Override
    public final TableColumn<S, T> getTableColumn() {
        return (TableColumn)super.getTableColumn();
    }

    final S getItem() {
        return this.itemRef == null ? null : (S)this.itemRef.get();
    }

    public String toString() {
        return "TablePosition [ row: " + this.getRow() + ", column: " + this.getTableColumn() + ", " + "tableView: " + this.getTableView() + " ]";
    }
}

