/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.util.Builder;

@Deprecated
public class MenuBarBuilder<B extends MenuBarBuilder<B>>
extends ControlBuilder<B>
implements Builder<MenuBar> {
    private int __set;
    private Collection<? extends Menu> menus;
    private boolean useSystemMenuBar;

    protected MenuBarBuilder() {
    }

    public static MenuBarBuilder<?> create() {
        return new MenuBarBuilder();
    }

    public void applyTo(MenuBar menuBar) {
        super.applyTo(menuBar);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            menuBar.getMenus().addAll(this.menus);
        }
        if ((n2 & 2) != 0) {
            menuBar.setUseSystemMenuBar(this.useSystemMenuBar);
        }
    }

    public B menus(Collection<? extends Menu> collection) {
        this.menus = collection;
        this.__set |= 1;
        return (B)this;
    }

    public B menus(Menu ... arrmenu) {
        return this.menus(Arrays.asList(arrmenu));
    }

    public B useSystemMenuBar(boolean bl) {
        this.useSystemMenuBar = bl;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public MenuBar build() {
        MenuBar menuBar = new MenuBar();
        this.applyTo(menuBar);
        return menuBar;
    }
}

