/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.util.Builder;

@Deprecated
public class TabBuilder<B extends TabBuilder<B>>
implements Builder<Tab> {
    private int __set;
    private boolean closable;
    private Node content;
    private ContextMenu contextMenu;
    private boolean disable;
    private Node graphic;
    private String id;
    private EventHandler<Event> onClosed;
    private EventHandler<Event> onSelectionChanged;
    private String style;
    private Collection<? extends String> styleClass;
    private String text;
    private Tooltip tooltip;
    private Object userData;

    protected TabBuilder() {
    }

    public static TabBuilder<?> create() {
        return new TabBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Tab tab) {
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    tab.setClosable(this.closable);
                    break;
                }
                case 1: {
                    tab.setContent(this.content);
                    break;
                }
                case 2: {
                    tab.setContextMenu(this.contextMenu);
                    break;
                }
                case 3: {
                    tab.setDisable(this.disable);
                    break;
                }
                case 4: {
                    tab.setGraphic(this.graphic);
                    break;
                }
                case 5: {
                    tab.setId(this.id);
                    break;
                }
                case 6: {
                    tab.setOnClosed(this.onClosed);
                    break;
                }
                case 7: {
                    tab.setOnSelectionChanged(this.onSelectionChanged);
                    break;
                }
                case 8: {
                    tab.setStyle(this.style);
                    break;
                }
                case 9: {
                    tab.getStyleClass().addAll(this.styleClass);
                    break;
                }
                case 10: {
                    tab.setText(this.text);
                    break;
                }
                case 11: {
                    tab.setTooltip(this.tooltip);
                    break;
                }
                case 12: {
                    tab.setUserData(this.userData);
                }
            }
        }
    }

    public B closable(boolean bl) {
        this.closable = bl;
        this.__set(0);
        return (B)this;
    }

    public B content(Node node) {
        this.content = node;
        this.__set(1);
        return (B)this;
    }

    public B contextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
        this.__set(2);
        return (B)this;
    }

    public B disable(boolean bl) {
        this.disable = bl;
        this.__set(3);
        return (B)this;
    }

    public B graphic(Node node) {
        this.graphic = node;
        this.__set(4);
        return (B)this;
    }

    public B id(String string) {
        this.id = string;
        this.__set(5);
        return (B)this;
    }

    public B onClosed(EventHandler<Event> eventHandler) {
        this.onClosed = eventHandler;
        this.__set(6);
        return (B)this;
    }

    public B onSelectionChanged(EventHandler<Event> eventHandler) {
        this.onSelectionChanged = eventHandler;
        this.__set(7);
        return (B)this;
    }

    public B style(String string) {
        this.style = string;
        this.__set(8);
        return (B)this;
    }

    public B styleClass(Collection<? extends String> collection) {
        this.styleClass = collection;
        this.__set(9);
        return (B)this;
    }

    public B styleClass(String ... arrstring) {
        return this.styleClass(Arrays.asList(arrstring));
    }

    public B text(String string) {
        this.text = string;
        this.__set(10);
        return (B)this;
    }

    public B tooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        this.__set(11);
        return (B)this;
    }

    public B userData(Object object) {
        this.userData = object;
        this.__set(12);
        return (B)this;
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        this.applyTo(tab);
        return tab;
    }
}

