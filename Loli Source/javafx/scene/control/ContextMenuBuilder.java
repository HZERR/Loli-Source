/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControlBuilder;

@Deprecated
public class ContextMenuBuilder<B extends ContextMenuBuilder<B>>
extends PopupControlBuilder<B> {
    private int __set;
    private boolean impl_showRelativeToWindow;
    private Collection<? extends MenuItem> items;
    private EventHandler<ActionEvent> onAction;

    protected ContextMenuBuilder() {
    }

    public static ContextMenuBuilder<?> create() {
        return new ContextMenuBuilder();
    }

    public void applyTo(ContextMenu contextMenu) {
        super.applyTo(contextMenu);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            contextMenu.setImpl_showRelativeToWindow(this.impl_showRelativeToWindow);
        }
        if ((n2 & 2) != 0) {
            contextMenu.getItems().addAll(this.items);
        }
        if ((n2 & 4) != 0) {
            contextMenu.setOnAction(this.onAction);
        }
    }

    @Deprecated
    public B impl_showRelativeToWindow(boolean bl) {
        this.impl_showRelativeToWindow = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B items(Collection<? extends MenuItem> collection) {
        this.items = collection;
        this.__set |= 2;
        return (B)this;
    }

    public B items(MenuItem ... arrmenuItem) {
        return this.items(Arrays.asList(arrmenuItem));
    }

    public B onAction(EventHandler<ActionEvent> eventHandler) {
        this.onAction = eventHandler;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public ContextMenu build() {
        ContextMenu contextMenu = new ContextMenu();
        this.applyTo(contextMenu);
        return contextMenu;
    }
}

