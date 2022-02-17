/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.ListCellBuilder;
import javafx.scene.control.cell.ChoiceBoxListCell;
import javafx.util.StringConverter;

@Deprecated
public class ChoiceBoxListCellBuilder<T, B extends ChoiceBoxListCellBuilder<T, B>>
extends ListCellBuilder<T, B> {
    private int __set;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ChoiceBoxListCellBuilder() {
    }

    public static <T> ChoiceBoxListCellBuilder<T, ?> create() {
        return new ChoiceBoxListCellBuilder();
    }

    @Override
    public void applyTo(ChoiceBoxListCell<T> choiceBoxListCell) {
        super.applyTo(choiceBoxListCell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            choiceBoxListCell.setConverter(this.converter);
        }
        if ((n2 & 2) != 0) {
            choiceBoxListCell.getItems().addAll(this.items);
        }
    }

    public B converter(StringConverter<T> stringConverter) {
        this.converter = stringConverter;
        this.__set |= 1;
        return (B)this;
    }

    public B items(Collection<? extends T> collection) {
        this.items = collection;
        this.__set |= 2;
        return (B)this;
    }

    public B items(T ... arrT) {
        return this.items((Collection<? extends T>)Arrays.asList(arrT));
    }

    @Override
    public ChoiceBoxListCell<T> build() {
        ChoiceBoxListCell choiceBoxListCell = new ChoiceBoxListCell();
        this.applyTo(choiceBoxListCell);
        return choiceBoxListCell;
    }
}

