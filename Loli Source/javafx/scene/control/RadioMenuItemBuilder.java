/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

@Deprecated
public class RadioMenuItemBuilder<B extends RadioMenuItemBuilder<B>>
extends MenuItemBuilder<B> {
    private int __set;
    private boolean selected;
    private String text;
    private ToggleGroup toggleGroup;

    protected RadioMenuItemBuilder() {
    }

    public static RadioMenuItemBuilder<?> create() {
        return new RadioMenuItemBuilder();
    }

    public void applyTo(RadioMenuItem radioMenuItem) {
        super.applyTo(radioMenuItem);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            radioMenuItem.setSelected(this.selected);
        }
        if ((n2 & 2) != 0) {
            radioMenuItem.setToggleGroup(this.toggleGroup);
        }
    }

    public B selected(boolean bl) {
        this.selected = bl;
        this.__set |= 1;
        return (B)this;
    }

    @Override
    public B text(String string) {
        this.text = string;
        return (B)this;
    }

    public B toggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup = toggleGroup;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public RadioMenuItem build() {
        RadioMenuItem radioMenuItem = new RadioMenuItem(this.text);
        this.applyTo(radioMenuItem);
        return radioMenuItem;
    }
}

