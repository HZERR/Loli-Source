/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Side;
import javafx.scene.control.ButtonBaseBuilder;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.util.Builder;

@Deprecated
public class MenuButtonBuilder<B extends MenuButtonBuilder<B>>
extends ButtonBaseBuilder<B>
implements Builder<MenuButton> {
    private int __set;
    private Collection<? extends MenuItem> items;
    private Side popupSide;

    protected MenuButtonBuilder() {
    }

    public static MenuButtonBuilder<?> create() {
        return new MenuButtonBuilder();
    }

    public void applyTo(MenuButton menuButton) {
        super.applyTo(menuButton);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            menuButton.getItems().addAll(this.items);
        }
        if ((n2 & 2) != 0) {
            menuButton.setPopupSide(this.popupSide);
        }
    }

    public B items(Collection<? extends MenuItem> collection) {
        this.items = collection;
        this.__set |= 1;
        return (B)this;
    }

    public B items(MenuItem ... arrmenuItem) {
        return this.items(Arrays.asList(arrmenuItem));
    }

    public B popupSide(Side side) {
        this.popupSide = side;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public MenuButton build() {
        MenuButton menuButton = new MenuButton();
        this.applyTo(menuButton);
        return menuButton;
    }
}

