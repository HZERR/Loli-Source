/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.javafx.scene.control.behavior.MenuButtonBehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.LabeledImpl;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.Collection;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class MenuButtonSkinBase<C extends MenuButton, B extends MenuButtonBehaviorBase<C>>
extends BehaviorSkinBase<C, B> {
    protected final LabeledImpl label;
    protected final StackPane arrow;
    protected final StackPane arrowButton;
    protected ContextMenu popup;
    protected boolean behaveLikeButton = false;
    private ListChangeListener<MenuItem> itemsChangedListener;

    public MenuButtonSkinBase(C c2, B b2) {
        super(c2, b2);
        if (((Node)c2).getOnMousePressed() == null) {
            ((Node)c2).addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> ((MenuButtonBehaviorBase)this.getBehavior()).mousePressed((MouseEvent)mouseEvent, this.behaveLikeButton));
        }
        if (((Node)c2).getOnMouseReleased() == null) {
            ((Node)c2).addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> ((MenuButtonBehaviorBase)this.getBehavior()).mouseReleased((MouseEvent)mouseEvent, this.behaveLikeButton));
        }
        this.label = new MenuLabeledImpl((MenuButton)this.getSkinnable());
        this.label.setMnemonicParsing(((Labeled)c2).isMnemonicParsing());
        this.label.setLabelFor((Node)c2);
        this.arrow = new StackPane();
        this.arrow.getStyleClass().setAll("arrow");
        this.arrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.arrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.arrowButton = new StackPane();
        this.arrowButton.getStyleClass().setAll("arrow-button");
        this.arrowButton.getChildren().add(this.arrow);
        this.popup = new ContextMenu();
        this.popup.getItems().clear();
        this.popup.getItems().addAll((Collection<MenuItem>)((MenuButton)this.getSkinnable()).getItems());
        this.getChildren().clear();
        this.getChildren().addAll(this.label, this.arrowButton);
        ((MenuButton)this.getSkinnable()).requestLayout();
        this.itemsChangedListener = change -> {
            while (change.next()) {
                this.popup.getItems().removeAll(change.getRemoved());
                this.popup.getItems().addAll(change.getFrom(), change.getAddedSubList());
            }
        };
        ((MenuButton)c2).getItems().addListener(this.itemsChangedListener);
        if (((MenuButton)this.getSkinnable()).getScene() != null) {
            ControlAcceleratorSupport.addAcceleratorsIntoScene(((MenuButton)this.getSkinnable()).getItems(), (Node)((Object)this.getSkinnable()));
        }
        ((Node)c2).sceneProperty().addListener((observableValue, scene, scene2) -> {
            if (this.getSkinnable() != null && ((MenuButton)this.getSkinnable()).getScene() != null) {
                ControlAcceleratorSupport.addAcceleratorsIntoScene(((MenuButton)this.getSkinnable()).getItems(), (Node)((Object)this.getSkinnable()));
            }
        });
        this.registerChangeListener(((MenuButton)c2).showingProperty(), "SHOWING");
        this.registerChangeListener(((Node)c2).focusedProperty(), "FOCUSED");
        this.registerChangeListener(((Labeled)c2).mnemonicParsingProperty(), "MNEMONIC_PARSING");
        this.registerChangeListener(this.popup.showingProperty(), "POPUP_VISIBLE");
    }

    @Override
    public void dispose() {
        ((MenuButton)this.getSkinnable()).getItems().removeListener(this.itemsChangedListener);
        super.dispose();
        if (this.popup != null) {
            if (this.popup.getSkin() != null && this.popup.getSkin().getNode() != null) {
                ContextMenuContent contextMenuContent = (ContextMenuContent)this.popup.getSkin().getNode();
                contextMenuContent.dispose();
                contextMenuContent = null;
            }
            this.popup.setSkin(null);
            this.popup = null;
        }
    }

    private void show() {
        if (!this.popup.isShowing()) {
            this.popup.show((Node)((Object)this.getSkinnable()), ((MenuButton)this.getSkinnable()).getPopupSide(), 0.0, 0.0);
        }
    }

    private void hide() {
        if (this.popup.isShowing()) {
            this.popup.hide();
        }
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("SHOWING".equals(string)) {
            if (((MenuButton)this.getSkinnable()).isShowing()) {
                this.show();
            } else {
                this.hide();
            }
        } else if ("FOCUSED".equals(string)) {
            if (!((MenuButton)this.getSkinnable()).isFocused() && ((MenuButton)this.getSkinnable()).isShowing()) {
                this.hide();
            }
            if (!((MenuButton)this.getSkinnable()).isFocused() && this.popup.isShowing()) {
                this.hide();
            }
        } else if ("POPUP_VISIBLE".equals(string)) {
            if (!this.popup.isShowing() && ((MenuButton)this.getSkinnable()).isShowing()) {
                ((MenuButton)this.getSkinnable()).hide();
            }
            if (this.popup.isShowing()) {
                Utils.addMnemonics(this.popup, ((MenuButton)this.getSkinnable()).getScene(), ((MenuButton)this.getSkinnable()).impl_isShowMnemonics());
            } else {
                Utils.removeMnemonics(this.popup, ((MenuButton)this.getSkinnable()).getScene());
            }
        } else if ("MNEMONIC_PARSING".equals(string)) {
            this.label.setMnemonicParsing(((MenuButton)this.getSkinnable()).isMnemonicParsing());
            ((MenuButton)this.getSkinnable()).requestLayout();
        }
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        return d6 + this.label.minWidth(d2) + this.snapSize(this.arrowButton.minWidth(d2)) + d4;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        return d3 + Math.max(this.label.minHeight(d2), this.snapSize(this.arrowButton.minHeight(-1.0))) + d5;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        return d6 + this.label.prefWidth(d2) + this.snapSize(this.arrowButton.prefWidth(d2)) + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        return d3 + Math.max(this.label.prefHeight(d2), this.snapSize(this.arrowButton.prefHeight(-1.0))) + d5;
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        return ((MenuButton)this.getSkinnable()).prefWidth(d2);
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        return ((MenuButton)this.getSkinnable()).prefHeight(d2);
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6 = this.snapSize(this.arrowButton.prefWidth(-1.0));
        this.label.resizeRelocate(d2, d3, d4 - d6, d5);
        this.arrowButton.resizeRelocate(d2 + (d4 - d6), d3, d6, d5);
    }

    private class MenuLabeledImpl
    extends LabeledImpl {
        MenuButton button;

        public MenuLabeledImpl(MenuButton menuButton) {
            super(menuButton);
            this.button = menuButton;
            this.addEventHandler(ActionEvent.ACTION, actionEvent -> {
                this.button.fireEvent(new ActionEvent());
                actionEvent.consume();
            });
        }
    }
}

