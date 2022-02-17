/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItemBuilder;

@Deprecated
public class CheckMenuItemBuilder<B extends CheckMenuItemBuilder<B>>
extends MenuItemBuilder<B> {
    private boolean __set;
    private boolean selected;

    protected CheckMenuItemBuilder() {
    }

    public static CheckMenuItemBuilder<?> create() {
        return new CheckMenuItemBuilder();
    }

    public void applyTo(CheckMenuItem checkMenuItem) {
        super.applyTo(checkMenuItem);
        if (this.__set) {
            checkMenuItem.setSelected(this.selected);
        }
    }

    public B selected(boolean bl) {
        this.selected = bl;
        this.__set = true;
        return (B)this;
    }

    @Override
    public CheckMenuItem build() {
        CheckMenuItem checkMenuItem = new CheckMenuItem();
        this.applyTo(checkMenuItem);
        return checkMenuItem;
    }
}

