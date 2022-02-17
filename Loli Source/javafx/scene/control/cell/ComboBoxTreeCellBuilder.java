/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TreeCellBuilder;
import javafx.scene.control.cell.ComboBoxTreeCell;
import javafx.util.StringConverter;

@Deprecated
public class ComboBoxTreeCellBuilder<T, B extends ComboBoxTreeCellBuilder<T, B>>
extends TreeCellBuilder<T, B> {
    private int __set;
    private boolean comboBoxEditable;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ComboBoxTreeCellBuilder() {
    }

    public static <T> ComboBoxTreeCellBuilder<T, ?> create() {
        return new ComboBoxTreeCellBuilder();
    }

    @Override
    public void applyTo(ComboBoxTreeCell<T> comboBoxTreeCell) {
        super.applyTo(comboBoxTreeCell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            comboBoxTreeCell.setComboBoxEditable(this.comboBoxEditable);
        }
        if ((n2 & 2) != 0) {
            comboBoxTreeCell.setConverter(this.converter);
        }
        if ((n2 & 4) != 0) {
            comboBoxTreeCell.getItems().addAll(this.items);
        }
    }

    public B comboBoxEditable(boolean bl) {
        this.comboBoxEditable = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B converter(StringConverter<T> stringConverter) {
        this.converter = stringConverter;
        this.__set |= 2;
        return (B)this;
    }

    public B items(Collection<? extends T> collection) {
        this.items = collection;
        this.__set |= 4;
        return (B)this;
    }

    public B items(T ... arrT) {
        return this.items((Collection<? extends T>)Arrays.asList(arrT));
    }

    @Override
    public ComboBoxTreeCell<T> build() {
        ComboBoxTreeCell comboBoxTreeCell = new ComboBoxTreeCell();
        this.applyTo(comboBoxTreeCell);
        return comboBoxTreeCell;
    }
}

