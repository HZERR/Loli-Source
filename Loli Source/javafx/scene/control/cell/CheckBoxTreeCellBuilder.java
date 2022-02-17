/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeCellBuilder;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Deprecated
public class CheckBoxTreeCellBuilder<T, B extends CheckBoxTreeCellBuilder<T, B>>
extends TreeCellBuilder<T, B> {
    private int __set;
    private StringConverter<TreeItem<T>> converter;
    private Callback<TreeItem<T>, ObservableValue<Boolean>> selectedStateCallback;

    protected CheckBoxTreeCellBuilder() {
    }

    public static <T> CheckBoxTreeCellBuilder<T, ?> create() {
        return new CheckBoxTreeCellBuilder();
    }

    @Override
    public void applyTo(CheckBoxTreeCell<T> checkBoxTreeCell) {
        super.applyTo(checkBoxTreeCell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            checkBoxTreeCell.setConverter(this.converter);
        }
        if ((n2 & 2) != 0) {
            checkBoxTreeCell.setSelectedStateCallback(this.selectedStateCallback);
        }
    }

    public B converter(StringConverter<TreeItem<T>> stringConverter) {
        this.converter = stringConverter;
        this.__set |= 1;
        return (B)this;
    }

    public B selectedStateCallback(Callback<TreeItem<T>, ObservableValue<Boolean>> callback) {
        this.selectedStateCallback = callback;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public CheckBoxTreeCell<T> build() {
        CheckBoxTreeCell checkBoxTreeCell = new CheckBoxTreeCell();
        this.applyTo(checkBoxTreeCell);
        return checkBoxTreeCell;
    }
}

