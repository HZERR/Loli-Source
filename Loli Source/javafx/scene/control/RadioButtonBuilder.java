/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButtonBuilder;

@Deprecated
public class RadioButtonBuilder<B extends RadioButtonBuilder<B>>
extends ToggleButtonBuilder<B> {
    protected RadioButtonBuilder() {
    }

    public static RadioButtonBuilder<?> create() {
        return new RadioButtonBuilder();
    }

    @Override
    public RadioButton build() {
        RadioButton radioButton = new RadioButton();
        this.applyTo(radioButton);
        return radioButton;
    }
}

