/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextFieldBuilder;

@Deprecated
public class PasswordFieldBuilder<B extends PasswordFieldBuilder<B>>
extends TextFieldBuilder<B> {
    private boolean __set;
    private String promptText;

    protected PasswordFieldBuilder() {
    }

    public static PasswordFieldBuilder<?> create() {
        return new PasswordFieldBuilder();
    }

    public void applyTo(PasswordField passwordField) {
        super.applyTo(passwordField);
        if (this.__set) {
            passwordField.setPromptText(this.promptText);
        }
    }

    @Override
    public B promptText(String string) {
        this.promptText = string;
        this.__set = true;
        return (B)this;
    }

    @Override
    public PasswordField build() {
        PasswordField passwordField = new PasswordField();
        this.applyTo(passwordField);
        return passwordField;
    }
}

