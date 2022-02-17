/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import javafx.scene.control.TableCellBuilder;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

@Deprecated
public class TextFieldTableCellBuilder<S, T, B extends TextFieldTableCellBuilder<S, T, B>>
extends TableCellBuilder<S, T, B> {
    private boolean __set;
    private StringConverter<T> converter;

    protected TextFieldTableCellBuilder() {
    }

    public static <S, T> TextFieldTableCellBuilder<S, T, ?> create() {
        return new TextFieldTableCellBuilder();
    }

    public void applyTo(TextFieldTableCell<S, T> textFieldTableCell) {
        super.applyTo(textFieldTableCell);
        if (this.__set) {
            textFieldTableCell.setConverter(this.converter);
        }
    }

    public B converter(StringConverter<T> stringConverter) {
        this.converter = stringConverter;
        this.__set = true;
        return (B)this;
    }

    @Override
    public TextFieldTableCell<S, T> build() {
        TextFieldTableCell textFieldTableCell = new TextFieldTableCell();
        this.applyTo(textFieldTableCell);
        return textFieldTableCell;
    }
}

