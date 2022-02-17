/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCellBuilder;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Deprecated
public class CheckBoxListCellBuilder<T, B extends CheckBoxListCellBuilder<T, B>>
extends ListCellBuilder<T, B> {
    private int __set;
    private StringConverter<T> converter;
    private Callback<T, ObservableValue<Boolean>> selectedStateCallback;

    protected CheckBoxListCellBuilder() {
    }

    public static <T> CheckBoxListCellBuilder<T, ?> create() {
        return new CheckBoxListCellBuilder();
    }

    @Override
    public void applyTo(CheckBoxListCell<T> checkBoxListCell) {
        super.applyTo(checkBoxListCell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            checkBoxListCell.setConverter(this.converter);
        }
        if ((n2 & 2) != 0) {
            checkBoxListCell.setSelectedStateCallback(this.selectedStateCallback);
        }
    }

    public B converter(StringConverter<T> stringConverter) {
        this.converter = stringConverter;
        this.__set |= 1;
        return (B)this;
    }

    public B selectedStateCallback(Callback<T, ObservableValue<Boolean>> callback) {
        this.selectedStateCallback = callback;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public CheckBoxListCell<T> build() {
        CheckBoxListCell checkBoxListCell = new CheckBoxListCell();
        this.applyTo(checkBoxListCell);
        return checkBoxListCell;
    }
}

