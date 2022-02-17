/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TreeCellBuilder;
import javafx.scene.control.cell.ChoiceBoxTreeCell;
import javafx.util.StringConverter;

@Deprecated
public class ChoiceBoxTreeCellBuilder<T, B extends ChoiceBoxTreeCellBuilder<T, B>>
extends TreeCellBuilder<T, B> {
    private int __set;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ChoiceBoxTreeCellBuilder() {
    }

    public static <T> ChoiceBoxTreeCellBuilder<T, ?> create() {
        return new ChoiceBoxTreeCellBuilder();
    }

    @Override
    public void applyTo(ChoiceBoxTreeCell<T> choiceBoxTreeCell) {
        super.applyTo(choiceBoxTreeCell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            choiceBoxTreeCell.setConverter(this.converter);
        }
        if ((n2 & 2) != 0) {
            choiceBoxTreeCell.getItems().addAll(this.items);
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
    public ChoiceBoxTreeCell<T> build() {
        ChoiceBoxTreeCell choiceBoxTreeCell = new ChoiceBoxTreeCell();
        this.applyTo(choiceBoxTreeCell);
        return choiceBoxTreeCell;
    }
}

