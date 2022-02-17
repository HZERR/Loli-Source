/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.Cell;
import javafx.scene.control.LabeledBuilder;
import javafx.util.Builder;

@Deprecated
public class CellBuilder<T, B extends CellBuilder<T, B>>
extends LabeledBuilder<B>
implements Builder<Cell<T>> {
    private int __set;
    private boolean editable;
    private T item;

    protected CellBuilder() {
    }

    public static <T> CellBuilder<T, ?> create() {
        return new CellBuilder();
    }

    public void applyTo(Cell<T> cell) {
        super.applyTo(cell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            cell.setEditable(this.editable);
        }
        if ((n2 & 2) != 0) {
            cell.setItem(this.item);
        }
    }

    public B editable(boolean bl) {
        this.editable = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B item(T t2) {
        this.item = t2;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public Cell<T> build() {
        Cell cell = new Cell();
        this.applyTo(cell);
        return cell;
    }
}

