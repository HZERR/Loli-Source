/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import javafx.scene.control.ListCellBuilder;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;

@Deprecated
public class TextFieldListCellBuilder<T, B extends TextFieldListCellBuilder<T, B>>
extends ListCellBuilder<T, B> {
    private boolean __set;
    private StringConverter<T> converter;

    protected TextFieldListCellBuilder() {
    }

    public static <T> TextFieldListCellBuilder<T, ?> create() {
        return new TextFieldListCellBuilder();
    }

    @Override
    public void applyTo(TextFieldListCell<T> textFieldListCell) {
        super.applyTo(textFieldListCell);
        if (this.__set) {
            textFieldListCell.setConverter(this.converter);
        }
    }

    public B converter(StringConverter<T> stringConverter) {
        this.converter = stringConverter;
        this.__set = true;
        return (B)this;
    }

    @Override
    public TextFieldListCell<T> build() {
        TextFieldListCell textFieldListCell = new TextFieldListCell();
        this.applyTo(textFieldListCell);
        return textFieldListCell;
    }
}

