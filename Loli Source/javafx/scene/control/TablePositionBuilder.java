/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.util.Builder;

@Deprecated
public class TablePositionBuilder<S, T, B extends TablePositionBuilder<S, T, B>>
implements Builder<TablePosition<S, T>> {
    private int row;
    private TableColumn<S, T> tableColumn;
    private TableView<S> tableView;

    protected TablePositionBuilder() {
    }

    public static <S, T> TablePositionBuilder<S, T, ?> create() {
        return new TablePositionBuilder();
    }

    public B row(int n2) {
        this.row = n2;
        return (B)this;
    }

    public B tableColumn(TableColumn<S, T> tableColumn) {
        this.tableColumn = tableColumn;
        return (B)this;
    }

    public B tableView(TableView<S> tableView) {
        this.tableView = tableView;
        return (B)this;
    }

    @Override
    public TablePosition<S, T> build() {
        TablePosition<S, T> tablePosition = new TablePosition<S, T>(this.tableView, this.row, this.tableColumn);
        return tablePosition;
    }
}

