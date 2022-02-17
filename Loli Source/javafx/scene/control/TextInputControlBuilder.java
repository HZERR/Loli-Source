/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.ControlBuilder;
import javafx.scene.control.TextInputControl;

@Deprecated
public abstract class TextInputControlBuilder<B extends TextInputControlBuilder<B>>
extends ControlBuilder<B> {
    private int __set;
    private boolean editable;
    private String promptText;
    private String text;

    protected TextInputControlBuilder() {
    }

    public void applyTo(TextInputControl textInputControl) {
        super.applyTo(textInputControl);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            textInputControl.setEditable(this.editable);
        }
        if ((n2 & 2) != 0) {
            textInputControl.setPromptText(this.promptText);
        }
        if ((n2 & 4) != 0) {
            textInputControl.setText(this.text);
        }
    }

    public B editable(boolean bl) {
        this.editable = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B promptText(String string) {
        this.promptText = string;
        this.__set |= 2;
        return (B)this;
    }

    public B text(String string) {
        this.text = string;
        this.__set |= 4;
        return (B)this;
    }
}

