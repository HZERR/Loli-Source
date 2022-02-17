/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.stage;

import com.sun.javafx.stage.WindowPeerListener;
import com.sun.javafx.tk.FocusCause;
import javafx.stage.PopupWindow;

public class PopupWindowPeerListener
extends WindowPeerListener {
    private final PopupWindow popupWindow;

    public PopupWindowPeerListener(PopupWindow popupWindow) {
        super(popupWindow);
        this.popupWindow = popupWindow;
    }

    @Override
    public void changedFocused(boolean bl, FocusCause focusCause) {
        this.popupWindow.setFocused(bl);
    }

    @Override
    public void closing() {
    }

    @Override
    public void changedLocation(float f2, float f3) {
    }

    @Override
    public void changedIconified(boolean bl) {
    }

    @Override
    public void changedMaximized(boolean bl) {
    }

    @Override
    public void changedResizable(boolean bl) {
    }

    @Override
    public void changedFullscreen(boolean bl) {
    }

    @Override
    public void focusUngrab() {
    }
}

