/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.MenuButtonBehaviorBase;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MenuButtonBehavior
extends MenuButtonBehaviorBase<MenuButton> {
    protected static final List<KeyBinding> MENU_BUTTON_BINDINGS = new ArrayList<KeyBinding>();

    public MenuButtonBehavior(MenuButton menuButton) {
        super(menuButton, MENU_BUTTON_BINDINGS);
    }

    static {
        MENU_BUTTON_BINDINGS.addAll(BASE_MENU_BUTTON_BINDINGS);
        MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, "Open"));
        MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "Open"));
    }
}

