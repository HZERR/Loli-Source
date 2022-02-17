/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.menu.CheckMenuItemBase;
import com.sun.javafx.menu.CustomMenuItemBase;
import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.menu.MenuItemBase;
import com.sun.javafx.menu.RadioMenuItemBase;
import com.sun.javafx.menu.SeparatorMenuItemBase;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class GlobalMenuAdapter
extends Menu
implements MenuBase {
    private Menu menu;
    private final ObservableList<MenuItemBase> items = new TrackableObservableList<MenuItemBase>(){

        @Override
        protected void onChanged(ListChangeListener.Change<MenuItemBase> change) {
        }
    };

    public static MenuBase adapt(Menu menu) {
        return new GlobalMenuAdapter(menu);
    }

    private GlobalMenuAdapter(Menu menu) {
        super(menu.getText());
        this.menu = menu;
        GlobalMenuAdapter.bindMenuItemProperties(this, menu);
        menu.showingProperty().addListener(observable -> {
            if (menu.isShowing() && !this.isShowing()) {
                this.show();
            } else if (!menu.isShowing() && this.isShowing()) {
                this.hide();
            }
        });
        this.showingProperty().addListener(observable -> {
            if (this.isShowing() && !menu.isShowing()) {
                menu.show();
            } else if (!this.isShowing() && menu.isShowing()) {
                menu.hide();
            }
        });
        menu.getItems().addListener(new ListChangeListener<MenuItem>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends MenuItem> change) {
                while (change.next()) {
                    int n2;
                    int n3 = change.getFrom();
                    int n4 = change.getTo();
                    List<? extends MenuItem> list = change.getRemoved();
                    for (n2 = n3 + list.size() - 1; n2 >= n3; --n2) {
                        GlobalMenuAdapter.this.items.remove(n2);
                        GlobalMenuAdapter.this.getItems().remove(n2);
                    }
                    for (n2 = n3; n2 < n4; ++n2) {
                        MenuItem menuItem = (MenuItem)change.getList().get(n2);
                        GlobalMenuAdapter.this.insertItem(menuItem, n2);
                    }
                }
            }
        });
        for (MenuItem menuItem : menu.getItems()) {
            this.insertItem(menuItem, this.items.size());
        }
    }

    private void insertItem(MenuItem menuItem, int n2) {
        MenuItem menuItem2 = menuItem instanceof Menu ? new GlobalMenuAdapter((Menu)menuItem) : (menuItem instanceof CheckMenuItem ? new CheckMenuItemAdapter((CheckMenuItem)menuItem) : (menuItem instanceof RadioMenuItem ? new RadioMenuItemAdapter((RadioMenuItem)menuItem) : (menuItem instanceof SeparatorMenuItem ? new SeparatorMenuItemAdapter((SeparatorMenuItem)menuItem) : (menuItem instanceof CustomMenuItem ? new CustomMenuItemAdapter((CustomMenuItem)menuItem) : new MenuItemAdapter(menuItem)))));
        this.items.add(n2, (MenuItemBase)((Object)menuItem2));
        this.getItems().add(n2, menuItem2);
    }

    @Override
    public final ObservableList<MenuItemBase> getItemsBase() {
        return this.items;
    }

    private static void bindMenuItemProperties(MenuItem menuItem, MenuItem menuItem2) {
        menuItem.idProperty().bind(menuItem2.idProperty());
        menuItem.textProperty().bind(menuItem2.textProperty());
        menuItem.graphicProperty().bind(menuItem2.graphicProperty());
        menuItem.disableProperty().bind(menuItem2.disableProperty());
        menuItem.visibleProperty().bind(menuItem2.visibleProperty());
        menuItem.acceleratorProperty().bind(menuItem2.acceleratorProperty());
        menuItem.mnemonicParsingProperty().bind(menuItem2.mnemonicParsingProperty());
        menuItem.setOnAction(actionEvent -> menuItem2.fire());
    }

    @Override
    public void fireValidation() {
        Menu menu;
        if (this.menu.getOnMenuValidation() != null) {
            Event.fireEvent(this.menu, new Event(MENU_VALIDATION_EVENT));
        }
        if ((menu = this.menu.getParentMenu()) != null && menu.getOnMenuValidation() != null) {
            Event.fireEvent(menu, new Event(MenuItem.MENU_VALIDATION_EVENT));
        }
    }

    private static class CustomMenuItemAdapter
    extends CustomMenuItem
    implements CustomMenuItemBase {
        private CustomMenuItem menuItem;

        private CustomMenuItemAdapter(CustomMenuItem customMenuItem) {
            this.menuItem = customMenuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, customMenuItem);
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent(menu, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    private static class SeparatorMenuItemAdapter
    extends SeparatorMenuItem
    implements SeparatorMenuItemBase {
        private SeparatorMenuItem menuItem;

        private SeparatorMenuItemAdapter(SeparatorMenuItem separatorMenuItem) {
            this.menuItem = separatorMenuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, separatorMenuItem);
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent(menu, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    private static class RadioMenuItemAdapter
    extends RadioMenuItem
    implements RadioMenuItemBase {
        private RadioMenuItem menuItem;

        private RadioMenuItemAdapter(RadioMenuItem radioMenuItem) {
            super(radioMenuItem.getText());
            this.menuItem = radioMenuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, radioMenuItem);
            this.selectedProperty().bindBidirectional(radioMenuItem.selectedProperty());
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent(menu, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    private static class CheckMenuItemAdapter
    extends CheckMenuItem
    implements CheckMenuItemBase {
        private CheckMenuItem menuItem;

        private CheckMenuItemAdapter(CheckMenuItem checkMenuItem) {
            super(checkMenuItem.getText());
            this.menuItem = checkMenuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, checkMenuItem);
            this.selectedProperty().bindBidirectional(checkMenuItem.selectedProperty());
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent(menu, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    private static class MenuItemAdapter
    extends MenuItem
    implements MenuItemBase {
        private MenuItem menuItem;

        private MenuItemAdapter(MenuItem menuItem) {
            super(menuItem.getText());
            this.menuItem = menuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, menuItem);
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.menuItem.getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent(menu, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }
}

