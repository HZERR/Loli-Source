/*
 * Decompiled with CFR 0.150.
 */
package javafx.stage;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.PopupWindow;
import javafx.stage.WindowBuilder;

@Deprecated
public abstract class PopupWindowBuilder<B extends PopupWindowBuilder<B>>
extends WindowBuilder<B> {
    private int __set;
    private boolean autoFix;
    private boolean autoHide;
    private boolean consumeAutoHidingEvents;
    private boolean hideOnEscape;
    private EventHandler<Event> onAutoHide;

    protected PopupWindowBuilder() {
    }

    public void applyTo(PopupWindow popupWindow) {
        super.applyTo(popupWindow);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            popupWindow.setAutoFix(this.autoFix);
        }
        if ((n2 & 2) != 0) {
            popupWindow.setAutoHide(this.autoHide);
        }
        if ((n2 & 4) != 0) {
            popupWindow.setConsumeAutoHidingEvents(this.consumeAutoHidingEvents);
        }
        if ((n2 & 8) != 0) {
            popupWindow.setHideOnEscape(this.hideOnEscape);
        }
        if ((n2 & 0x10) != 0) {
            popupWindow.setOnAutoHide(this.onAutoHide);
        }
    }

    public B autoFix(boolean bl) {
        this.autoFix = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B autoHide(boolean bl) {
        this.autoHide = bl;
        this.__set |= 2;
        return (B)this;
    }

    public B consumeAutoHidingEvents(boolean bl) {
        this.consumeAutoHidingEvents = bl;
        this.__set |= 4;
        return (B)this;
    }

    public B hideOnEscape(boolean bl) {
        this.hideOnEscape = bl;
        this.__set |= 8;
        return (B)this;
    }

    public B onAutoHide(EventHandler<Event> eventHandler) {
        this.onAutoHide = eventHandler;
        this.__set |= 0x10;
        return (B)this;
    }
}

