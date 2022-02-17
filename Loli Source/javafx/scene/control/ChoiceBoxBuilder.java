/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.Builder;
import javafx.util.StringConverter;

@Deprecated
public class ChoiceBoxBuilder<T, B extends ChoiceBoxBuilder<T, B>>
extends ControlBuilder<B>
implements Builder<ChoiceBox<T>> {
    private int __set;
    private StringConverter<T> converter;
    private ObservableList<T> items;
    private SingleSelectionModel<T> selectionModel;
    private T value;

    protected ChoiceBoxBuilder() {
    }

    public static <T> ChoiceBoxBuilder<T, ?> create() {
        return new ChoiceBoxBuilder();
    }

    public void applyTo(ChoiceBox<T> choiceBox) {
        super.applyTo(choiceBox);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            choiceBox.setConverter(this.converter);
        }
        if ((n2 & 2) != 0) {
            choiceBox.setItems(this.items);
        }
        if ((n2 & 4) != 0) {
            choiceBox.setSelectionModel(this.selectionModel);
        }
        if ((n2 & 8) != 0) {
            choiceBox.setValue(this.value);
        }
    }

    public B converter(StringConverter<T> stringConverter) {
        this.converter = stringConverter;
        this.__set |= 1;
        return (B)this;
    }

    public B items(ObservableList<T> observableList) {
        this.items = observableList;
        this.__set |= 2;
        return (B)this;
    }

    public B selectionModel(SingleSelectionModel<T> singleSelectionModel) {
        this.selectionModel = singleSelectionModel;
        this.__set |= 4;
        return (B)this;
    }

    public B value(T t2) {
        this.value = t2;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public ChoiceBox<T> build() {
        ChoiceBox choiceBox = new ChoiceBox();
        this.applyTo(choiceBox);
        return choiceBox;
    }
}

