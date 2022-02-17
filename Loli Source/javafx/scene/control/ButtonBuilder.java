/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBaseBuilder;
import javafx.util.Builder;

@Deprecated
public class ButtonBuilder<B extends ButtonBuilder<B>>
extends ButtonBaseBuilder<B>
implements Builder<Button> {
    private int __set;
    private boolean cancelButton;
    private boolean defaultButton;

    protected ButtonBuilder() {
    }

    public static ButtonBuilder<?> create() {
        return new ButtonBuilder();
    }

    public void applyTo(Button button) {
        super.applyTo(button);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            button.setCancelButton(this.cancelButton);
        }
        if ((n2 & 2) != 0) {
            button.setDefaultButton(this.defaultButton);
        }
    }

    public B cancelButton(boolean bl) {
        this.cancelButton = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B defaultButton(boolean bl) {
        this.defaultButton = bl;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public Button build() {
        Button button = new Button();
        this.applyTo(button);
        return button;
    }
}

