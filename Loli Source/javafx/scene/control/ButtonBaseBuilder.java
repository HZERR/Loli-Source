/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.LabeledBuilder;

@Deprecated
public abstract class ButtonBaseBuilder<B extends ButtonBaseBuilder<B>>
extends LabeledBuilder<B> {
    private boolean __set;
    private EventHandler<ActionEvent> onAction;

    protected ButtonBaseBuilder() {
    }

    public void applyTo(ButtonBase buttonBase) {
        super.applyTo(buttonBase);
        if (this.__set) {
            buttonBase.setOnAction(this.onAction);
        }
    }

    public B onAction(EventHandler<ActionEvent> eventHandler) {
        this.onAction = eventHandler;
        this.__set = true;
        return (B)this;
    }
}

