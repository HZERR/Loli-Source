/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import javafx.scene.control.TreeCellBuilder;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;

@Deprecated
public class TextFieldTreeCellBuilder<T, B extends TextFieldTreeCellBuilder<T, B>>
extends TreeCellBuilder<T, B> {
    private boolean __set;
    private StringConverter<T> converter;

    protected TextFieldTreeCellBuilder() {
    }

    public static <T> TextFieldTreeCellBuilder<T, ?> create() {
        return new TextFieldTreeCellBuilder();
    }

    @Override
    public void applyTo(TextFieldTreeCell<T> textFieldTreeCell) {
        super.applyTo(textFieldTreeCell);
        if (this.__set) {
            textFieldTreeCell.setConverter(this.converter);
        }
    }

    public B converter(StringConverter<T> stringConverter) {
        this.converter = stringConverter;
        this.__set = true;
        return (B)this;
    }

    @Override
    public TextFieldTreeCell<T> build() {
        TextFieldTreeCell textFieldTreeCell = new TextFieldTreeCell();
        this.applyTo(textFieldTreeCell);
        return textFieldTreeCell;
    }
}

