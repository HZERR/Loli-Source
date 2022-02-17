/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.css.PseudoClass;
import javafx.scene.control.Cell;

public class IndexedCell<T>
extends Cell<T> {
    private ReadOnlyIntegerWrapper index = new ReadOnlyIntegerWrapper(this, "index", -1){

        @Override
        protected void invalidated() {
            boolean bl = this.get() % 2 == 0;
            IndexedCell.this.pseudoClassStateChanged(PSEUDO_CLASS_EVEN, bl);
            IndexedCell.this.pseudoClassStateChanged(PSEUDO_CLASS_ODD, !bl);
        }
    };
    private static final String DEFAULT_STYLE_CLASS = "indexed-cell";
    private static final PseudoClass PSEUDO_CLASS_ODD = PseudoClass.getPseudoClass("odd");
    private static final PseudoClass PSEUDO_CLASS_EVEN = PseudoClass.getPseudoClass("even");

    public IndexedCell() {
        this.getStyleClass().addAll(DEFAULT_STYLE_CLASS);
    }

    public final int getIndex() {
        return this.index.get();
    }

    public final ReadOnlyIntegerProperty indexProperty() {
        return this.index.getReadOnlyProperty();
    }

    public void updateIndex(int n2) {
        int n3 = this.index.get();
        this.index.set(n2);
        this.indexChanged(n3, n2);
    }

    void indexChanged(int n2, int n3) {
    }
}

