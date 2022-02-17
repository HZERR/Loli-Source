/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TableCellBuilder;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.StringConverter;

@Deprecated
public class ComboBoxTableCellBuilder<S, T, B extends ComboBoxTableCellBuilder<S, T, B>>
extends TableCellBuilder<S, T, B> {
    private int __set;
    private boolean comboBoxEditable;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ComboBoxTableCellBuilder() {
    }

    public static <S, T> ComboBoxTableCellBuilder<S, T, ?> create() {
        return new ComboBoxTableCellBuilder();
    }

    public void applyTo(ComboBoxTableCell<S, T> comboBoxTableCell) {
        super.applyTo(comboBoxTableCell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            comboBoxTableCell.setComboBoxEditable(this.comboBoxEditable);
        }
        if ((n2 & 2) != 0) {
            comboBoxTableCell.setConverter(this.converter);
        }
        if ((n2 & 4) != 0) {
            comboBoxTableCell.getItems().addAll(this.items);
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
    public ComboBoxTableCell<S, T> build() {
        ComboBoxTableCell comboBoxTableCell = new ComboBoxTableCell();
        this.applyTo(comboBoxTableCell);
        return comboBoxTableCell;
    }
}

