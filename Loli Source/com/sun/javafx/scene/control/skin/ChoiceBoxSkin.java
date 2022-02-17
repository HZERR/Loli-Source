/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ChoiceBoxBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class ChoiceBoxSkin<T>
extends BehaviorSkinBase<ChoiceBox<T>, ChoiceBoxBehavior<T>> {
    private ObservableList<T> choiceBoxItems;
    private ContextMenu popup;
    private StackPane openButton;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private SelectionModel<T> selectionModel;
    private Label label;
    private final ListChangeListener<T> choiceBoxItemsListener = new ListChangeListener<T>(){

        @Override
        public void onChanged(ListChangeListener.Change<? extends T> change) {
            while (change.next()) {
                int n2;
                if (change.getRemovedSize() > 0 || change.wasPermutated()) {
                    ChoiceBoxSkin.this.toggleGroup.getToggles().clear();
                    ChoiceBoxSkin.this.popup.getItems().clear();
                    n2 = 0;
                    for (Object e2 : change.getList()) {
                        ChoiceBoxSkin.this.addPopupItem(e2, n2);
                        ++n2;
                    }
                    continue;
                }
                for (n2 = change.getFrom(); n2 < change.getTo(); ++n2) {
                    Object e3 = change.getList().get(n2);
                    ChoiceBoxSkin.this.addPopupItem(e3, n2);
                }
            }
            ChoiceBoxSkin.this.updateSelection();
            ((ChoiceBox)ChoiceBoxSkin.this.getSkinnable()).requestLayout();
        }
    };
    private final WeakListChangeListener<T> weakChoiceBoxItemsListener = new WeakListChangeListener<T>(this.choiceBoxItemsListener);
    private final InvalidationListener itemsObserver;
    private InvalidationListener selectionChangeListener = observable -> this.updateSelection();

    public ChoiceBoxSkin(ChoiceBox<T> choiceBox) {
        super(choiceBox, new ChoiceBoxBehavior<T>(choiceBox));
        this.initialize();
        this.itemsObserver = observable -> this.updateChoiceBoxItems();
        choiceBox.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
        choiceBox.requestLayout();
        this.registerChangeListener(choiceBox.selectionModelProperty(), "SELECTION_MODEL");
        this.registerChangeListener(choiceBox.showingProperty(), "SHOWING");
        this.registerChangeListener(choiceBox.itemsProperty(), "ITEMS");
        this.registerChangeListener(choiceBox.getSelectionModel().selectedItemProperty(), "SELECTION_CHANGED");
        this.registerChangeListener(choiceBox.converterProperty(), "CONVERTER");
    }

    private void initialize() {
        this.updateChoiceBoxItems();
        this.label = new Label();
        this.label.setMnemonicParsing(false);
        this.openButton = new StackPane();
        this.openButton.getStyleClass().setAll("open-button");
        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().setAll("arrow");
        this.openButton.getChildren().clear();
        this.openButton.getChildren().addAll(stackPane);
        this.popup = new ContextMenu();
        this.popup.showingProperty().addListener((observableValue, bl, bl2) -> {
            if (!bl2.booleanValue()) {
                ((ChoiceBox)this.getSkinnable()).hide();
            }
        });
        this.popup.setId("choice-box-popup-menu");
        this.getChildren().setAll(this.label, this.openButton);
        this.updatePopupItems();
        this.updateSelectionModel();
        this.updateSelection();
        if (this.selectionModel != null && this.selectionModel.getSelectedIndex() == -1) {
            this.label.setText("");
        }
    }

    private void updateChoiceBoxItems() {
        if (this.choiceBoxItems != null) {
            this.choiceBoxItems.removeListener(this.weakChoiceBoxItemsListener);
        }
        this.choiceBoxItems = ((ChoiceBox)this.getSkinnable()).getItems();
        if (this.choiceBoxItems != null) {
            this.choiceBoxItems.addListener(this.weakChoiceBoxItemsListener);
        }
    }

    String getChoiceBoxSelectedText() {
        return this.label.getText();
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ITEMS".equals(string)) {
            this.updateChoiceBoxItems();
            this.updatePopupItems();
            this.updateSelectionModel();
            this.updateSelection();
            if (this.selectionModel != null && this.selectionModel.getSelectedIndex() == -1) {
                this.label.setText("");
            }
        } else if ("SELECTION_MODEL".equals(string)) {
            this.updateSelectionModel();
        } else if ("SELECTION_CHANGED".equals(string)) {
            MenuItem menuItem;
            int n2;
            if (((ChoiceBox)this.getSkinnable()).getSelectionModel() != null && (n2 = ((ChoiceBox)this.getSkinnable()).getSelectionModel().getSelectedIndex()) != -1 && (menuItem = (MenuItem)this.popup.getItems().get(n2)) instanceof RadioMenuItem) {
                ((RadioMenuItem)menuItem).setSelected(true);
            }
        } else if ("SHOWING".equals(string)) {
            if (((ChoiceBox)this.getSkinnable()).isShowing()) {
                ContextMenuContent contextMenuContent;
                boolean bl;
                MenuItem menuItem = null;
                SingleSelectionModel singleSelectionModel = ((ChoiceBox)this.getSkinnable()).getSelectionModel();
                if (singleSelectionModel == null) {
                    return;
                }
                long l2 = singleSelectionModel.getSelectedIndex();
                int n3 = this.choiceBoxItems.size();
                boolean bl2 = bl = l2 >= 0L && l2 < (long)n3;
                if (bl) {
                    menuItem = (MenuItem)this.popup.getItems().get((int)l2);
                    if (menuItem != null && menuItem instanceof RadioMenuItem) {
                        ((RadioMenuItem)menuItem).setSelected(true);
                    }
                } else if (n3 > 0) {
                    menuItem = (MenuItem)this.popup.getItems().get(0);
                }
                ((ChoiceBox)this.getSkinnable()).autosize();
                double d2 = 0.0;
                if (this.popup.getSkin() != null && (contextMenuContent = (ContextMenuContent)this.popup.getSkin().getNode()) != null && l2 != -1L) {
                    d2 = -contextMenuContent.getMenuYOffset((int)l2);
                }
                this.popup.show((Node)((Object)this.getSkinnable()), Side.BOTTOM, 2.0, d2);
            } else {
                this.popup.hide();
            }
        } else if ("CONVERTER".equals(string)) {
            this.updateChoiceBoxItems();
            this.updatePopupItems();
        }
    }

    private void addPopupItem(T t2, int n2) {
        MenuItem menuItem = null;
        if (t2 instanceof Separator) {
            menuItem = new SeparatorMenuItem();
        } else if (t2 instanceof SeparatorMenuItem) {
            menuItem = (SeparatorMenuItem)t2;
        } else {
            StringConverter<T> stringConverter = ((ChoiceBox)this.getSkinnable()).getConverter();
            String string = stringConverter == null ? (t2 == null ? "" : t2.toString()) : stringConverter.toString(t2);
            RadioMenuItem radioMenuItem = new RadioMenuItem(string);
            radioMenuItem.setId("choice-box-menu-item");
            radioMenuItem.setToggleGroup(this.toggleGroup);
            radioMenuItem.setOnAction(actionEvent -> {
                if (this.selectionModel == null) {
                    return;
                }
                int n2 = ((ChoiceBox)this.getSkinnable()).getItems().indexOf(t2);
                this.selectionModel.select(n2);
                radioMenuItem.setSelected(true);
            });
            menuItem = radioMenuItem;
        }
        menuItem.setMnemonicParsing(false);
        this.popup.getItems().add(n2, menuItem);
    }

    private void updatePopupItems() {
        this.toggleGroup.getToggles().clear();
        this.popup.getItems().clear();
        this.toggleGroup.selectToggle(null);
        for (int i2 = 0; i2 < this.choiceBoxItems.size(); ++i2) {
            Object e2 = this.choiceBoxItems.get(i2);
            this.addPopupItem(e2, i2);
        }
    }

    private void updateSelectionModel() {
        if (this.selectionModel != null) {
            this.selectionModel.selectedIndexProperty().removeListener(this.selectionChangeListener);
        }
        this.selectionModel = ((ChoiceBox)this.getSkinnable()).getSelectionModel();
        if (this.selectionModel != null) {
            this.selectionModel.selectedIndexProperty().addListener(this.selectionChangeListener);
        }
    }

    private void updateSelection() {
        if (this.selectionModel == null || this.selectionModel.isEmpty()) {
            this.toggleGroup.selectToggle(null);
            this.label.setText("");
        } else {
            int n2 = this.selectionModel.getSelectedIndex();
            if (n2 == -1 || n2 > this.popup.getItems().size()) {
                this.label.setText("");
                return;
            }
            if (n2 < this.popup.getItems().size()) {
                MenuItem menuItem = (MenuItem)this.popup.getItems().get(n2);
                if (menuItem instanceof RadioMenuItem) {
                    ((RadioMenuItem)menuItem).setSelected(true);
                    this.toggleGroup.selectToggle(null);
                }
                this.label.setText(((MenuItem)this.popup.getItems().get(n2)).getText());
            }
        }
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6 = this.openButton.prefWidth(-1.0);
        ChoiceBox choiceBox = (ChoiceBox)this.getSkinnable();
        this.label.resizeRelocate(d2, d3, d4, d5);
        this.openButton.resize(d6, this.openButton.prefHeight(-1.0));
        this.positionInArea(this.openButton, d2 + d4 - d6, d3, d6, d5, 0.0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.label.minWidth(-1.0) + this.openButton.minWidth(-1.0);
        double d8 = this.popup.minWidth(-1.0);
        return d6 + Math.max(d7, d8) + d4;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.label.minHeight(-1.0);
        double d8 = this.openButton.minHeight(-1.0);
        return d3 + Math.max(d7, d8) + d5;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.label.prefWidth(-1.0) + this.openButton.prefWidth(-1.0);
        double d8 = this.popup.prefWidth(-1.0);
        if (d8 <= 0.0 && this.popup.getItems().size() > 0) {
            d8 = new Text(((MenuItem)this.popup.getItems().get(0)).getText()).prefWidth(-1.0);
        }
        return this.popup.getItems().size() == 0 ? 50.0 : d6 + Math.max(d7, d8) + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.label.prefHeight(-1.0);
        double d8 = this.openButton.prefHeight(-1.0);
        return d3 + Math.max(d7, d8) + d5;
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        return ((ChoiceBox)this.getSkinnable()).prefHeight(d2);
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        return ((ChoiceBox)this.getSkinnable()).prefWidth(d2);
    }
}

