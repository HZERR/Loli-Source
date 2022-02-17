/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.util.Builder;

@Deprecated
public class MenuItemBuilder<B extends MenuItemBuilder<B>>
implements Builder<MenuItem> {
    private int __set;
    private KeyCombination accelerator;
    private boolean disable;
    private Node graphic;
    private String id;
    private boolean mnemonicParsing;
    private EventHandler<ActionEvent> onAction;
    private EventHandler<Event> onMenuValidation;
    private String style;
    private Collection<? extends String> styleClass;
    private String text;
    private Object userData;
    private boolean visible;

    protected MenuItemBuilder() {
    }

    public static MenuItemBuilder<?> create() {
        return new MenuItemBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(MenuItem menuItem) {
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    menuItem.setAccelerator(this.accelerator);
                    break;
                }
                case 1: {
                    menuItem.setDisable(this.disable);
                    break;
                }
                case 2: {
                    menuItem.setGraphic(this.graphic);
                    break;
                }
                case 3: {
                    menuItem.setId(this.id);
                    break;
                }
                case 4: {
                    menuItem.setMnemonicParsing(this.mnemonicParsing);
                    break;
                }
                case 5: {
                    menuItem.setOnAction(this.onAction);
                    break;
                }
                case 6: {
                    menuItem.setOnMenuValidation(this.onMenuValidation);
                    break;
                }
                case 7: {
                    menuItem.setStyle(this.style);
                    break;
                }
                case 8: {
                    menuItem.getStyleClass().addAll(this.styleClass);
                    break;
                }
                case 9: {
                    menuItem.setText(this.text);
                    break;
                }
                case 10: {
                    menuItem.setUserData(this.userData);
                    break;
                }
                case 11: {
                    menuItem.setVisible(this.visible);
                }
            }
        }
    }

    public B accelerator(KeyCombination keyCombination) {
        this.accelerator = keyCombination;
        this.__set(0);
        return (B)this;
    }

    public B disable(boolean bl) {
        this.disable = bl;
        this.__set(1);
        return (B)this;
    }

    public B graphic(Node node) {
        this.graphic = node;
        this.__set(2);
        return (B)this;
    }

    public B id(String string) {
        this.id = string;
        this.__set(3);
        return (B)this;
    }

    public B mnemonicParsing(boolean bl) {
        this.mnemonicParsing = bl;
        this.__set(4);
        return (B)this;
    }

    public B onAction(EventHandler<ActionEvent> eventHandler) {
        this.onAction = eventHandler;
        this.__set(5);
        return (B)this;
    }

    public B onMenuValidation(EventHandler<Event> eventHandler) {
        this.onMenuValidation = eventHandler;
        this.__set(6);
        return (B)this;
    }

    public B style(String string) {
        this.style = string;
        this.__set(7);
        return (B)this;
    }

    public B styleClass(Collection<? extends String> collection) {
        this.styleClass = collection;
        this.__set(8);
        return (B)this;
    }

    public B styleClass(String ... arrstring) {
        return this.styleClass(Arrays.asList(arrstring));
    }

    public B text(String string) {
        this.text = string;
        this.__set(9);
        return (B)this;
    }

    public B userData(Object object) {
        this.userData = object;
        this.__set(10);
        return (B)this;
    }

    public B visible(boolean bl) {
        this.visible = bl;
        this.__set(11);
        return (B)this;
    }

    @Override
    public MenuItem build() {
        MenuItem menuItem = new MenuItem();
        this.applyTo(menuItem);
        return menuItem;
    }
}

