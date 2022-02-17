/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBaseBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.Builder;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Deprecated
public class ComboBoxBuilder<T, B extends ComboBoxBuilder<T, B>>
extends ComboBoxBaseBuilder<T, B>
implements Builder<ComboBox<T>> {
    private int __set;
    private ListCell<T> buttonCell;
    private Callback<ListView<T>, ListCell<T>> cellFactory;
    private StringConverter<T> converter;
    private ObservableList<T> items;
    private SingleSelectionModel<T> selectionModel;
    private int visibleRowCount;

    protected ComboBoxBuilder() {
    }

    public static <T> ComboBoxBuilder<T, ?> create() {
        return new ComboBoxBuilder();
    }

    @Override
    public void applyTo(ComboBox<T> comboBox) {
        super.applyTo(comboBox);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            comboBox.setButtonCell(this.buttonCell);
        }
        if ((n2 & 2) != 0) {
            comboBox.setCellFactory(this.cellFactory);
        }
        if ((n2 & 4) != 0) {
            comboBox.setConverter(this.converter);
        }
        if ((n2 & 8) != 0) {
            comboBox.setItems(this.items);
        }
        if ((n2 & 0x10) != 0) {
            comboBox.setSelectionModel(this.selectionModel);
        }
        if ((n2 & 0x20) != 0) {
            comboBox.setVisibleRowCount(this.visibleRowCount);
        }
    }

    public B buttonCell(ListCell<T> listCell) {
        this.buttonCell = listCell;
        this.__set |= 1;
        return (B)this;
    }

    public B cellFactory(Callback<ListView<T>, ListCell<T>> callback) {
        this.cellFactory = callback;
        this.__set |= 2;
        return (B)this;
    }

    public B converter(StringConverter<T> stringConverter) {
        this.converter = stringConverter;
        this.__set |= 4;
        return (B)this;
    }

    public B items(ObservableList<T> observableList) {
        this.items = observableList;
        this.__set |= 8;
        return (B)this;
    }

    public B selectionModel(SingleSelectionModel<T> singleSelectionModel) {
        this.selectionModel = singleSelectionModel;
        this.__set |= 0x10;
        return (B)this;
    }

    public B visibleRowCount(int n2) {
        this.visibleRowCount = n2;
        this.__set |= 0x20;
        return (B)this;
    }

    @Override
    public ComboBox<T> build() {
        ComboBox comboBox = new ComboBox();
        this.applyTo(comboBox);
        return comboBox;
    }
}

