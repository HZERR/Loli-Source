/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.menu.CheckMenuItemBase;
import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.menu.MenuItemBase;
import com.sun.javafx.menu.RadioMenuItemBase;
import com.sun.javafx.menu.SeparatorMenuItemBase;
import com.sun.javafx.tk.TKSystemMenu;
import com.sun.javafx.tk.quantum.GlassMenuEventHandler;
import com.sun.javafx.tk.quantum.PixelUtils;
import com.sun.prism.Image;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

class GlassSystemMenu
implements TKSystemMenu {
    private List<MenuBase> systemMenus = null;
    private MenuBar glassSystemMenuBar = null;
    private InvalidationListener visibilityListener = observable -> {
        if (this.systemMenus != null) {
            this.setMenus(this.systemMenus);
        }
    };

    GlassSystemMenu() {
    }

    protected void createMenuBar() {
        if (this.glassSystemMenuBar == null) {
            Application application = Application.GetApplication();
            this.glassSystemMenuBar = application.createMenuBar();
            application.installDefaultMenus(this.glassSystemMenuBar);
            if (this.systemMenus != null) {
                this.setMenus(this.systemMenus);
            }
        }
    }

    protected MenuBar getMenuBar() {
        return this.glassSystemMenuBar;
    }

    @Override
    public boolean isSupported() {
        return Application.GetApplication().supportsSystemMenu();
    }

    @Override
    public void setMenus(List<MenuBase> list) {
        this.systemMenus = list;
        if (this.glassSystemMenuBar != null) {
            List<Menu> list2 = this.glassSystemMenuBar.getMenus();
            int n2 = list2.size();
            for (int i2 = n2 - 1; i2 >= 1; --i2) {
                Menu object = list2.get(i2);
                this.clearMenu(object);
                this.glassSystemMenuBar.remove(i2);
            }
            for (MenuBase menuBase : list) {
                this.addMenu(null, menuBase);
            }
        }
    }

    private void clearMenu(Menu menu) {
        for (int i2 = menu.getItems().size() - 1; i2 >= 0; --i2) {
            Object object = menu.getItems().get(i2);
            if (object instanceof MenuItem) {
                ((MenuItem)object).setCallback(null);
                continue;
            }
            if (!(object instanceof Menu)) continue;
            this.clearMenu((Menu)object);
        }
        menu.setEventHandler(null);
    }

    private void addMenu(Menu menu, MenuBase menuBase) {
        if (menu != null) {
            this.insertMenu(menu, menuBase, menu.getItems().size());
        } else {
            this.insertMenu(menu, menuBase, this.glassSystemMenuBar.getMenus().size());
        }
    }

    private void insertMenu(Menu menu, MenuBase menuBase, int n2) {
        Application application = Application.GetApplication();
        Menu menu2 = application.createMenu(this.parseText(menuBase), !menuBase.isDisable());
        menu2.setEventHandler(new GlassMenuEventHandler(menuBase));
        menuBase.visibleProperty().removeListener(this.visibilityListener);
        menuBase.visibleProperty().addListener(this.visibilityListener);
        if (!menuBase.isVisible()) {
            return;
        }
        ObservableList<MenuItemBase> observableList = menuBase.getItemsBase();
        observableList.addListener(change -> {
            while (change.next()) {
                Object object;
                int n2;
                int n3 = change.getFrom();
                int n4 = change.getTo();
                List list = change.getRemoved();
                for (n2 = n3 + list.size() - 1; n2 >= n3; --n2) {
                    object = menu2.getItems();
                    if (n2 < 0 || object.size() <= n2) continue;
                    menu2.remove(n2);
                }
                for (n2 = n3; n2 < n4; ++n2) {
                    object = (MenuItemBase)change.getList().get(n2);
                    if (object instanceof MenuBase) {
                        this.insertMenu(menu2, (MenuBase)object, n2);
                        continue;
                    }
                    this.insertMenuItem(menu2, (MenuItemBase)object, n2);
                }
            }
        });
        for (MenuItemBase menuItemBase : observableList) {
            if (menuItemBase instanceof MenuBase) {
                this.addMenu(menu2, (MenuBase)menuItemBase);
                continue;
            }
            this.addMenuItem(menu2, menuItemBase);
        }
        menu2.setPixels(this.getPixels(menuBase));
        this.setMenuBindings(menu2, menuBase);
        if (menu != null) {
            menu.insert(menu2, n2);
        } else {
            this.glassSystemMenuBar.insert(menu2, n2);
        }
    }

    private void setMenuBindings(Menu menu, MenuBase menuBase) {
        menuBase.textProperty().addListener(observable -> menu.setTitle(this.parseText(menuBase)));
        menuBase.disableProperty().addListener(observable -> menu.setEnabled(!menuBase.isDisable()));
        menuBase.mnemonicParsingProperty().addListener(observable -> menu.setTitle(this.parseText(menuBase)));
    }

    private void addMenuItem(Menu menu, MenuItemBase menuItemBase) {
        this.insertMenuItem(menu, menuItemBase, menu.getItems().size());
    }

    private void insertMenuItem(final Menu menu, final MenuItemBase menuItemBase, int n2) {
        Application application = Application.GetApplication();
        menuItemBase.visibleProperty().removeListener(this.visibilityListener);
        menuItemBase.visibleProperty().addListener(this.visibilityListener);
        if (!menuItemBase.isVisible()) {
            return;
        }
        if (menuItemBase instanceof SeparatorMenuItemBase) {
            if (menuItemBase.isVisible()) {
                menu.insert(MenuItem.Separator, n2);
            }
        } else {
            MenuItem.Callback callback = new MenuItem.Callback(){

                @Override
                public void action() {
                    if (menuItemBase instanceof CheckMenuItemBase) {
                        CheckMenuItemBase checkMenuItemBase;
                        checkMenuItemBase.setSelected(!(checkMenuItemBase = (CheckMenuItemBase)menuItemBase).isSelected());
                    } else if (menuItemBase instanceof RadioMenuItemBase) {
                        RadioMenuItemBase radioMenuItemBase = (RadioMenuItemBase)menuItemBase;
                        radioMenuItemBase.setSelected(true);
                    }
                    menuItemBase.fire();
                }

                @Override
                public void validate() {
                    Menu.EventHandler eventHandler = menu.getEventHandler();
                    GlassMenuEventHandler glassMenuEventHandler = (GlassMenuEventHandler)eventHandler;
                    if (glassMenuEventHandler.isMenuOpen()) {
                        return;
                    }
                    menuItemBase.fireValidation();
                }
            };
            MenuItem menuItem = application.createMenuItem(this.parseText(menuItemBase), callback);
            menuItemBase.textProperty().addListener(observable -> menuItem.setTitle(this.parseText(menuItemBase)));
            menuItem.setPixels(this.getPixels(menuItemBase));
            menuItemBase.graphicProperty().addListener(observable -> menuItem.setPixels(this.getPixels(menuItemBase)));
            menuItem.setEnabled(!menuItemBase.isDisable());
            menuItemBase.disableProperty().addListener(observable -> menuItem.setEnabled(!menuItemBase.isDisable()));
            this.setShortcut(menuItem, menuItemBase);
            menuItemBase.acceleratorProperty().addListener(observable -> this.setShortcut(menuItem, menuItemBase));
            menuItemBase.mnemonicParsingProperty().addListener(observable -> menuItem.setTitle(this.parseText(menuItemBase)));
            if (menuItemBase instanceof CheckMenuItemBase) {
                CheckMenuItemBase checkMenuItemBase = (CheckMenuItemBase)menuItemBase;
                menuItem.setChecked(checkMenuItemBase.isSelected());
                checkMenuItemBase.selectedProperty().addListener(observable -> menuItem.setChecked(checkMenuItemBase.isSelected()));
            } else if (menuItemBase instanceof RadioMenuItemBase) {
                RadioMenuItemBase radioMenuItemBase = (RadioMenuItemBase)menuItemBase;
                menuItem.setChecked(radioMenuItemBase.isSelected());
                radioMenuItemBase.selectedProperty().addListener(observable -> menuItem.setChecked(radioMenuItemBase.isSelected()));
            }
            menu.insert(menuItem, n2);
        }
    }

    private String parseText(MenuItemBase menuItemBase) {
        String string = menuItemBase.getText();
        if (string == null) {
            return "";
        }
        if (!string.isEmpty() && menuItemBase.isMnemonicParsing()) {
            return string.replaceFirst("_([^_])", "$1");
        }
        return string;
    }

    private Pixels getPixels(MenuItemBase menuItemBase) {
        if (menuItemBase.getGraphic() instanceof ImageView) {
            ImageView imageView = (ImageView)menuItemBase.getGraphic();
            javafx.scene.image.Image image = imageView.getImage();
            if (image == null) {
                return null;
            }
            String string = image.impl_getUrl();
            if (string == null || PixelUtils.supportedFormatType(string)) {
                Image image2 = (Image)image.impl_getPlatformImage();
                return image2 == null ? null : PixelUtils.imageToPixels(image2);
            }
        }
        return null;
    }

    private void setShortcut(MenuItem menuItem, MenuItemBase menuItemBase) {
        KeyCombination keyCombination = menuItemBase.getAccelerator();
        if (keyCombination == null) {
            menuItem.setShortcut(0, 0);
        } else if (keyCombination instanceof KeyCodeCombination) {
            KeyCodeCombination keyCodeCombination = (KeyCodeCombination)keyCombination;
            KeyCode keyCode = keyCodeCombination.getCode();
            assert (PlatformUtil.isMac() || PlatformUtil.isLinux());
            int n2 = this.glassModifiers(keyCodeCombination);
            if (PlatformUtil.isMac()) {
                int n3 = keyCode.isLetterKey() ? (int)keyCode.impl_getChar().toUpperCase().charAt(0) : keyCode.impl_getCode();
                menuItem.setShortcut(n3, n2);
            } else if (PlatformUtil.isLinux()) {
                String string = keyCode.impl_getChar().toLowerCase();
                if ((n2 & 4) != 0) {
                    menuItem.setShortcut(string.charAt(0), n2);
                } else {
                    menuItem.setShortcut(0, 0);
                }
            } else {
                menuItem.setShortcut(0, 0);
            }
        } else if (keyCombination instanceof KeyCharacterCombination) {
            KeyCharacterCombination keyCharacterCombination = (KeyCharacterCombination)keyCombination;
            String string = keyCharacterCombination.getCharacter();
            menuItem.setShortcut(string.charAt(0), this.glassModifiers(keyCharacterCombination));
        }
    }

    private int glassModifiers(KeyCombination keyCombination) {
        KeyCode keyCode;
        int n2;
        int n3 = 0;
        if (keyCombination.getShift() == KeyCombination.ModifierValue.DOWN) {
            ++n3;
        }
        if (keyCombination.getControl() == KeyCombination.ModifierValue.DOWN) {
            n3 += 4;
        }
        if (keyCombination.getAlt() == KeyCombination.ModifierValue.DOWN) {
            n3 += 8;
        }
        if (keyCombination.getShortcut() == KeyCombination.ModifierValue.DOWN) {
            if (PlatformUtil.isLinux()) {
                n3 += 4;
            } else if (PlatformUtil.isMac()) {
                n3 += 16;
            }
        }
        if (keyCombination.getMeta() == KeyCombination.ModifierValue.DOWN) {
            if (PlatformUtil.isLinux()) {
                n3 += 16;
            } else if (PlatformUtil.isMac()) {
                n3 += 16;
            }
        }
        if (keyCombination instanceof KeyCodeCombination && ((n2 = (keyCode = ((KeyCodeCombination)keyCombination).getCode()).impl_getCode()) >= KeyCode.F1.impl_getCode() && n2 <= KeyCode.F12.impl_getCode() || n2 >= KeyCode.F13.impl_getCode() && n2 <= KeyCode.F24.impl_getCode())) {
            n3 += 2;
        }
        return n3;
    }
}

