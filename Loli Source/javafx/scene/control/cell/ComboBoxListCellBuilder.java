/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.ListCellBuilder;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.util.StringConverter;

@Deprecated
public class ComboBoxListCellBuilder<T, B extends ComboBoxListCellBuilder<T, B>>
extends ListCellBuilder<T, B> {
    private int __set;
    private boolean comboBoxEditable;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ComboBoxListCellBuilder() {
    }

    public static <T> ComboBoxListCellBuilder<T, ?> create() {
        return new ComboBoxListCellBuilder();
    }

    @Override
    public void applyTo(ComboBoxListCell<T> comboBoxListCell) {
        super.applyTo(comboBoxListCell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            comboBoxListCell.setComboBoxEditable(this.comboBoxEditable);
        }
        if ((n2 & 2) != 0) {
            comboBoxListCell.setConverter(this.converter);
        }
        if ((n2 & 4) != 0) {
            comboBoxListCell.getItems().addAll(this.items);
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
    public ComboBoxListCell<T> build() {
        ComboBoxListCell comboBoxListCell = new ComboBoxListCell();
        this.applyTo(comboBoxListCell);
        return comboBoxListCell;
    }
}

