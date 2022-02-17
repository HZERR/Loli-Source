/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.IndexedCellBuilder;
import javafx.scene.control.TableRow;

@Deprecated
public class TableRowBuilder<T, B extends TableRowBuilder<T, B>>
extends IndexedCellBuilder<T, B> {
    protected TableRowBuilder() {
    }

    public static <T> TableRowBuilder<T, ?> create() {
        return new TableRowBuilder();
    }

    @Override
    public TableRow<T> build() {
        TableRow tableRow = new TableRow();
        this.applyTo(tableRow);
        return tableRow;
    }
}

