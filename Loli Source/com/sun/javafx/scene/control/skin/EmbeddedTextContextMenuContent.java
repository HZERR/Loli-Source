/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class EmbeddedTextContextMenuContent
extends StackPane {
    private ContextMenu contextMenu;
    private StackPane pointer;
    private HBox menuBox;

    public EmbeddedTextContextMenuContent(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
        this.menuBox = new HBox();
        this.pointer = new StackPane();
        this.pointer.getStyleClass().add("pointer");
        this.updateMenuItemContainer();
        this.getChildren().addAll(this.pointer, this.menuBox);
        this.contextMenu.ownerNodeProperty().addListener(observable -> {
            if (this.contextMenu.getOwnerNode() instanceof TextArea) {
                TextAreaSkin textAreaSkin = (TextAreaSkin)((TextArea)this.contextMenu.getOwnerNode()).getSkin();
                ((TextArea)textAreaSkin.getSkinnable()).getProperties().addListener(new InvalidationListener(){

                    @Override
                    public void invalidated(Observable observable) {
                        EmbeddedTextContextMenuContent.this.requestLayout();
                    }
                });
            } else if (this.contextMenu.getOwnerNode() instanceof TextField) {
                TextFieldSkin textFieldSkin = (TextFieldSkin)((TextField)this.contextMenu.getOwnerNode()).getSkin();
                ((TextField)textFieldSkin.getSkinnable()).getProperties().addListener(new InvalidationListener(){

                    @Override
                    public void invalidated(Observable observable) {
                        EmbeddedTextContextMenuContent.this.requestLayout();
                    }
                });
            }
        });
        this.contextMenu.getItems().addListener(change -> this.updateMenuItemContainer());
    }

    private void updateMenuItemContainer() {
        this.menuBox.getChildren().clear();
        for (MenuItem menuItem : this.contextMenu.getItems()) {
            MenuItemContainer menuItemContainer = new MenuItemContainer(menuItem);
            menuItemContainer.visibleProperty().bind(menuItem.visibleProperty());
            this.menuBox.getChildren().add(menuItemContainer);
        }
    }

    private void hideAllMenus(MenuItem menuItem) {
        Menu menu;
        this.contextMenu.hide();
        while ((menu = menuItem.getParentMenu()) != null) {
            menu.hide();
            menuItem = menu;
        }
        if (menu == null && menuItem.getParentPopup() != null) {
            menuItem.getParentPopup().hide();
        }
    }

    @Override
    protected double computePrefHeight(double d2) {
        double d3 = this.snapSize(this.pointer.prefHeight(d2));
        double d4 = this.snapSize(this.menuBox.prefHeight(d2));
        return this.snappedTopInset() + d3 + d4 + this.snappedBottomInset();
    }

    @Override
    protected double computePrefWidth(double d2) {
        double d3 = this.snapSize(this.menuBox.prefWidth(d2));
        return this.snappedLeftInset() + d3 + this.snappedRightInset();
    }

    @Override
    protected void layoutChildren() {
        double d2 = this.snappedLeftInset();
        double d3 = this.snappedRightInset();
        double d4 = this.snappedTopInset();
        double d5 = this.getWidth() - (d2 + d3);
        double d6 = this.snapSize(Utils.boundedSize(this.pointer.prefWidth(-1.0), this.pointer.minWidth(-1.0), this.pointer.maxWidth(-1.0)));
        double d7 = this.snapSize(Utils.boundedSize(this.pointer.prefWidth(-1.0), this.pointer.minWidth(-1.0), this.pointer.maxWidth(-1.0)));
        double d8 = this.snapSize(Utils.boundedSize(this.menuBox.prefWidth(-1.0), this.menuBox.minWidth(-1.0), this.menuBox.maxWidth(-1.0)));
        double d9 = this.snapSize(Utils.boundedSize(this.menuBox.prefWidth(-1.0), this.menuBox.minWidth(-1.0), this.menuBox.maxWidth(-1.0)));
        double d10 = 0.0;
        double d11 = 0.0;
        double d12 = 0.0;
        ObservableMap<Object, Object> observableMap = null;
        if (this.contextMenu.getOwnerNode() instanceof TextArea) {
            observableMap = ((TextArea)this.contextMenu.getOwnerNode()).getProperties();
        } else if (this.contextMenu.getOwnerNode() instanceof TextField) {
            observableMap = ((TextField)this.contextMenu.getOwnerNode()).getProperties();
        }
        if (observableMap != null) {
            if (observableMap.containsKey("CONTEXT_MENU_SCENE_X")) {
                d10 = Double.valueOf(observableMap.get("CONTEXT_MENU_SCENE_X").toString());
                observableMap.remove("CONTEXT_MENU_SCENE_X");
            }
            if (observableMap.containsKey("CONTEXT_MENU_SCREEN_X")) {
                d11 = Double.valueOf(observableMap.get("CONTEXT_MENU_SCREEN_X").toString());
                observableMap.remove("CONTEXT_MENU_SCREEN_X");
            }
        }
        d12 = d10 == 0.0 ? d5 / 2.0 : d11 - d10 - this.contextMenu.getX() + d10;
        this.pointer.resize(d6, d7);
        this.positionInArea(this.pointer, d12, d4, d6, d7, 0.0, HPos.CENTER, VPos.CENTER);
        this.menuBox.resize(d8, d9);
        this.positionInArea(this.menuBox, d2, d4 + d7, d8, d9, 0.0, HPos.CENTER, VPos.CENTER);
    }

    class MenuItemContainer
    extends Button {
        private MenuItem item;

        public MenuItemContainer(MenuItem menuItem) {
            this.getStyleClass().addAll((Collection<String>)menuItem.getStyleClass());
            this.setId(menuItem.getId());
            this.item = menuItem;
            this.setText(menuItem.getText());
            this.setStyle(menuItem.getStyle());
            this.textProperty().bind(menuItem.textProperty());
        }

        public MenuItem getItem() {
            return this.item;
        }

        @Override
        public void fire() {
            Event.fireEvent(this.item, new ActionEvent());
            if (!Boolean.TRUE.equals((Boolean)this.item.getProperties().get("refreshMenu"))) {
                EmbeddedTextContextMenuContent.this.hideAllMenus(this.item);
            }
        }
    }
}

