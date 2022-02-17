/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ControlBuilder;

@Deprecated
public abstract class ComboBoxBaseBuilder<T, B extends ComboBoxBaseBuilder<T, B>>
extends ControlBuilder<B> {
    private int __set;
    private boolean editable;
    private EventHandler<ActionEvent> onAction;
    private EventHandler<Event> onHidden;
    private EventHandler<Event> onHiding;
    private EventHandler<Event> onShowing;
    private EventHandler<Event> onShown;
    private String promptText;
    private T value;

    protected ComboBoxBaseBuilder() {
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(ComboBoxBase<T> comboBoxBase) {
        super.applyTo(comboBoxBase);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    comboBoxBase.setEditable(this.editable);
                    break;
                }
                case 1: {
                    comboBoxBase.setOnAction(this.onAction);
                    break;
                }
                case 2: {
                    comboBoxBase.setOnHidden(this.onHidden);
                    break;
                }
                case 3: {
                    comboBoxBase.setOnHiding(this.onHiding);
                    break;
                }
                case 4: {
                    comboBoxBase.setOnShowing(this.onShowing);
                    break;
                }
                case 5: {
                    comboBoxBase.setOnShown(this.onShown);
                    break;
                }
                case 6: {
                    comboBoxBase.setPromptText(this.promptText);
                    break;
                }
                case 7: {
                    comboBoxBase.setValue(this.value);
                }
            }
        }
    }

    public B editable(boolean bl) {
        this.editable = bl;
        this.__set(0);
        return (B)this;
    }

    public B onAction(EventHandler<ActionEvent> eventHandler) {
        this.onAction = eventHandler;
        this.__set(1);
        return (B)this;
    }

    public B onHidden(EventHandler<Event> eventHandler) {
        this.onHidden = eventHandler;
        this.__set(2);
        return (B)this;
    }

    public B onHiding(EventHandler<Event> eventHandler) {
        this.onHiding = eventHandler;
        this.__set(3);
        return (B)this;
    }

    public B onShowing(EventHandler<Event> eventHandler) {
        this.onShowing = eventHandler;
        this.__set(4);
        return (B)this;
    }

    public B onShown(EventHandler<Event> eventHandler) {
        this.onShown = eventHandler;
        this.__set(5);
        return (B)this;
    }

    public B promptText(String string) {
        this.promptText = string;
        this.__set(6);
        return (B)this;
    }

    public B value(T t2) {
        this.value = t2;
        this.__set(7);
        return (B)this;
    }
}

