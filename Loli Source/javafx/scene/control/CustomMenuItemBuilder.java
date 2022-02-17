/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItemBuilder;

@Deprecated
public class CustomMenuItemBuilder<B extends CustomMenuItemBuilder<B>>
extends MenuItemBuilder<B> {
    private int __set;
    private Node content;
    private boolean hideOnClick;

    protected CustomMenuItemBuilder() {
    }

    public static CustomMenuItemBuilder<?> create() {
        return new CustomMenuItemBuilder();
    }

    public void applyTo(CustomMenuItem customMenuItem) {
        super.applyTo(customMenuItem);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            customMenuItem.setContent(this.content);
        }
        if ((n2 & 2) != 0) {
            customMenuItem.setHideOnClick(this.hideOnClick);
        }
    }

    public B content(Node node) {
        this.content = node;
        this.__set |= 1;
        return (B)this;
    }

    public B hideOnClick(boolean bl) {
        this.hideOnClick = bl;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public CustomMenuItem build() {
        CustomMenuItem customMenuItem = new CustomMenuItem();
        this.applyTo(customMenuItem);
        return customMenuItem;
    }
}

