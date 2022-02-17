/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.MenuBarSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Control;
import javafx.scene.control.Menu;
import javafx.scene.control.Skin;

@DefaultProperty(value="menus")
public class MenuBar
extends Control {
    private ObservableList<Menu> menus = FXCollections.observableArrayList();
    private BooleanProperty useSystemMenuBar;
    private static final String DEFAULT_STYLE_CLASS = "menu-bar";

    public MenuBar() {
        this(null);
    }

    public MenuBar(Menu ... arrmenu) {
        this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.setAccessibleRole(AccessibleRole.MENU_BAR);
        if (arrmenu != null) {
            this.getMenus().addAll(arrmenu);
        }
        ((StyleableProperty)((Object)this.focusTraversableProperty())).applyStyle(null, Boolean.FALSE);
    }

    public final BooleanProperty useSystemMenuBarProperty() {
        if (this.useSystemMenuBar == null) {
            this.useSystemMenuBar = new StyleableBooleanProperty(){

                @Override
                public CssMetaData<MenuBar, Boolean> getCssMetaData() {
                    return StyleableProperties.USE_SYSTEM_MENU_BAR;
                }

                @Override
                public Object getBean() {
                    return MenuBar.this;
                }

                @Override
                public String getName() {
                    return "useSystemMenuBar";
                }
            };
        }
        return this.useSystemMenuBar;
    }

    public final void setUseSystemMenuBar(boolean bl) {
        this.useSystemMenuBarProperty().setValue(bl);
    }

    public final boolean isUseSystemMenuBar() {
        return this.useSystemMenuBar == null ? false : this.useSystemMenuBar.getValue();
    }

    public final ObservableList<Menu> getMenus() {
        return this.menus;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MenuBarSkin(this);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return MenuBar.getClassCssMetaData();
    }

    @Override
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }

    private static class StyleableProperties {
        private static final CssMetaData<MenuBar, Boolean> USE_SYSTEM_MENU_BAR = new CssMetaData<MenuBar, Boolean>("-fx-use-system-menu-bar", BooleanConverter.getInstance(), Boolean.valueOf(false)){

            @Override
            public boolean isSettable(MenuBar menuBar) {
                return menuBar.useSystemMenuBar == null || !menuBar.useSystemMenuBar.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(MenuBar menuBar) {
                return (StyleableProperty)((Object)menuBar.useSystemMenuBarProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Control.getClassCssMetaData());
            arrayList.add(USE_SYSTEM_MENU_BAR);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

