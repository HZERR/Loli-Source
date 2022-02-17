/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TableCellBuilder;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.util.StringConverter;

@Deprecated
public class ChoiceBoxTableCellBuilder<S, T, B extends ChoiceBoxTableCellBuilder<S, T, B>>
extends TableCellBuilder<S, T, B> {
    private int __set;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ChoiceBoxTableCellBuilder() {
    }

    public static <S, T> ChoiceBoxTableCellBuilder<S, T, ?> create() {
        return new ChoiceBoxTableCellBuilder();
    }

    public void applyTo(ChoiceBoxTableCell<S, T> choiceBoxTableCell) {
        super.applyTo(choiceBoxTableCell);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            choiceBoxTableCell.setConverter(this.converter);
        }
        if ((n2 & 2) != 0) {
            choiceBoxTableCell.getItems().addAll(this.items);
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
    public ChoiceBoxTableCell<S, T> build() {
        ChoiceBoxTableCell choiceBoxTableCell = new ChoiceBoxTableCell();
        this.applyTo(choiceBoxTableCell);
        return choiceBoxTableCell;
    }
}

