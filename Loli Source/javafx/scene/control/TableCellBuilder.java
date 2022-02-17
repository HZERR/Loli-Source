/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.CellBuilder;
import javafx.scene.control.TableCell;

@Deprecated
public class TableCellBuilder<S, T, B extends TableCellBuilder<S, T, B>>
extends CellBuilder<T, B> {
    protected TableCellBuilder() {
    }

    public static <S, T> TableCellBuilder<S, T, ?> create() {
        return new TableCellBuilder();
    }

    @Override
    public TableCell<S, T> build() {
        TableCell tableCell = new TableCell();
        this.applyTo(tableCell);
        return tableCell;
    }
}

