/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.CellBuilder;
import javafx.scene.control.IndexedCell;

@Deprecated
public class IndexedCellBuilder<T, B extends IndexedCellBuilder<T, B>>
extends CellBuilder<T, B> {
    protected IndexedCellBuilder() {
    }

    public static <T> IndexedCellBuilder<T, ?> create() {
        return new IndexedCellBuilder();
    }

    @Override
    public IndexedCell<T> build() {
        IndexedCell indexedCell = new IndexedCell();
        this.applyTo(indexedCell);
        return indexedCell;
    }
}

