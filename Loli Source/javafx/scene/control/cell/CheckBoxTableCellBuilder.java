/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCellBuilder;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Deprecated
public class CheckBoxTableCellBuilder<S, T, B extends CheckBoxTableCellBuilder<S, T, B>>
extends TableCellBuilder<S, T, B> {
    private int __set;
    private StringConverter<T> converter;
    private Callback<Integer, ObservableValue<Boolean>> selectedStateCallback;

    protected CheckBoxTableCellBuilder() {
    }

    public static <S, T> CheckBoxTableCellBuilder<S, T, ?> create() {
        return new CheckBoxTableCellBuilder();
    }

    public void applyTo(CheckBoxTableCell<S, T> checkBoxTableCell) {
        super.applyTo(checkBoxTableCell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            checkBoxTableCell.setConverter(this.converter);
        }
        if ((n2 & 2) != 0) {
            checkBoxTableCell.setSelectedStateCallback(this.selectedStateCallback);
        }
    }

    public B converter(StringConverter<T> stringConverter) {
        this.converter = stringConverter;
        this.__set |= 1;
        return (B)this;
    }

    public B selectedStateCallback(Callback<Integer, ObservableValue<Boolean>> callback) {
        this.selectedStateCallback = callback;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public CheckBoxTableCell<S, T> build() {
        CheckBoxTableCell checkBoxTableCell = new CheckBoxTableCell();
        this.applyTo(checkBoxTableCell);
        return checkBoxTableCell;
    }
}

