/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControlBuilder;
import javafx.util.Builder;

@Deprecated
public class TextFieldBuilder<B extends TextFieldBuilder<B>>
extends TextInputControlBuilder<B>
implements Builder<TextField> {
    private int __set;
    private Pos alignment;
    private EventHandler<ActionEvent> onAction;
    private int prefColumnCount;
    private String promptText;

    protected TextFieldBuilder() {
    }

    public static TextFieldBuilder<?> create() {
        return new TextFieldBuilder();
    }

    public void applyTo(TextField textField) {
        super.applyTo(textField);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            textField.setAlignment(this.alignment);
        }
        if ((n2 & 2) != 0) {
            textField.setOnAction(this.onAction);
        }
        if ((n2 & 4) != 0) {
            textField.setPrefColumnCount(this.prefColumnCount);
        }
        if ((n2 & 8) != 0) {
            textField.setPromptText(this.promptText);
        }
    }

    public B alignment(Pos pos) {
        this.alignment = pos;
        this.__set |= 1;
        return (B)this;
    }

    public B onAction(EventHandler<ActionEvent> eventHandler) {
        this.onAction = eventHandler;
        this.__set |= 2;
        return (B)this;
    }

    public B prefColumnCount(int n2) {
        this.prefColumnCount = n2;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public B promptText(String string) {
        this.promptText = string;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public TextField build() {
        TextField textField = new TextField();
        this.applyTo(textField);
        return textField;
    }
}

