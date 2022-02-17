/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.IndexedCellBuilder;
import javafx.scene.control.ListCell;

@Deprecated
public class ListCellBuilder<T, B extends ListCellBuilder<T, B>>
extends IndexedCellBuilder<T, B> {
    protected ListCellBuilder() {
    }

    public static <T> ListCellBuilder<T, ?> create() {
        return new ListCellBuilder();
    }

    @Override
    public ListCell<T> build() {
        ListCell listCell = new ListCell();
        this.applyTo(listCell);
        return listCell;
    }
}

