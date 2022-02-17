/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;

@Deprecated
public class MenuBuilder<B extends MenuBuilder<B>>
extends MenuItemBuilder<B> {
    private int __set;
    private Collection<? extends MenuItem> items;
    private EventHandler<Event> onHidden;
    private EventHandler<Event> onHiding;
    private EventHandler<Event> onShowing;
    private EventHandler<Event> onShown;

    protected MenuBuilder() {
    }

    public static MenuBuilder<?> create() {
        return new MenuBuilder();
    }

    public void applyTo(Menu menu) {
        super.applyTo(menu);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            menu.getItems().addAll(this.items);
        }
        if ((n2 & 2) != 0) {
            menu.setOnHidden(this.onHidden);
        }
        if ((n2 & 4) != 0) {
            menu.setOnHiding(this.onHiding);
        }
        if ((n2 & 8) != 0) {
            menu.setOnShowing(this.onShowing);
        }
        if ((n2 & 0x10) != 0) {
            menu.setOnShown(this.onShown);
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

    public B onHidden(EventHandler<Event> eventHandler) {
        this.onHidden = eventHandler;
        this.__set |= 2;
        return (B)this;
    }

    public B onHiding(EventHandler<Event> eventHandler) {
        this.onHiding = eventHandler;
        this.__set |= 4;
        return (B)this;
    }

    public B onShowing(EventHandler<Event> eventHandler) {
        this.onShowing = eventHandler;
        this.__set |= 8;
        return (B)this;
    }

    public B onShown(EventHandler<Event> eventHandler) {
        this.onShown = eventHandler;
        this.__set |= 0x10;
        return (B)this;
    }

    @Override
    public Menu build() {
        Menu menu = new Menu();
        this.applyTo(menu);
        return menu;
    }
}

