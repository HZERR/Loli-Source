/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ComboBoxMode;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public abstract class ComboBoxBaseSkin<T>
extends BehaviorSkinBase<ComboBoxBase<T>, ComboBoxBaseBehavior<T>> {
    private Node displayNode;
    protected StackPane arrowButton;
    protected Region arrow;
    private ComboBoxMode mode = ComboBoxMode.COMBOBOX;

    protected final ComboBoxMode getMode() {
        return this.mode;
    }

    protected final void setMode(ComboBoxMode comboBoxMode) {
        this.mode = comboBoxMode;
    }

    public ComboBoxBaseSkin(ComboBoxBase<T> comboBoxBase, ComboBoxBaseBehavior<T> comboBoxBaseBehavior) {
        super(comboBoxBase, comboBoxBaseBehavior);
        this.arrow = new Region();
        this.arrow.setFocusTraversable(false);
        this.arrow.getStyleClass().setAll("arrow");
        this.arrow.setId("arrow");
        this.arrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.arrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.arrow.setMouseTransparent(true);
        this.arrowButton = new StackPane();
        this.arrowButton.setFocusTraversable(false);
        this.arrowButton.setId("arrow-button");
        this.arrowButton.getStyleClass().setAll("arrow-button");
        this.arrowButton.getChildren().add(this.arrow);
        if (comboBoxBase.isEditable()) {
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> ((ComboBoxBaseBehavior)this.getBehavior()).mouseEntered((MouseEvent)mouseEvent));
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                ((ComboBoxBaseBehavior)this.getBehavior()).mousePressed((MouseEvent)mouseEvent);
                mouseEvent.consume();
            });
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                ((ComboBoxBaseBehavior)this.getBehavior()).mouseReleased((MouseEvent)mouseEvent);
                mouseEvent.consume();
            });
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> ((ComboBoxBaseBehavior)this.getBehavior()).mouseExited((MouseEvent)mouseEvent));
        }
        this.getChildren().add(this.arrowButton);
        ((ComboBoxBase)this.getSkinnable()).focusedProperty().addListener((observableValue, bl, bl2) -> {
            if (!bl2.booleanValue()) {
                this.focusLost();
            }
        });
        this.registerChangeListener(comboBoxBase.editableProperty(), "EDITABLE");
        this.registerChangeListener(comboBoxBase.showingProperty(), "SHOWING");
        this.registerChangeListener(comboBoxBase.focusedProperty(), "FOCUSED");
        this.registerChangeListener(comboBoxBase.valueProperty(), "VALUE");
    }

    protected void focusLost() {
        ((ComboBoxBase)this.getSkinnable()).hide();
    }

    public abstract Node getDisplayNode();

    public abstract void show();

    public abstract void hide();

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("SHOWING".equals(string)) {
            if (((ComboBoxBase)this.getSkinnable()).isShowing()) {
                this.show();
            } else {
                this.hide();
            }
        } else if ("EDITABLE".equals(string)) {
            this.updateDisplayArea();
        } else if ("VALUE".equals(string)) {
            this.updateDisplayArea();
        }
    }

    protected void updateDisplayArea() {
        ObservableList<Node> observableList = this.getChildren();
        Node node = this.displayNode;
        this.displayNode = this.getDisplayNode();
        if (node != null && node != this.displayNode) {
            observableList.remove(node);
        }
        if (this.displayNode != null && !observableList.contains(this.displayNode)) {
            observableList.add(this.displayNode);
            this.displayNode.applyCss();
        }
    }

    private boolean isButton() {
        return this.getMode() == ComboBoxMode.BUTTON;
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6;
        if (this.displayNode == null) {
            this.updateDisplayArea();
        }
        double d7 = this.snapSize(this.arrow.prefWidth(-1.0));
        double d8 = d6 = this.isButton() ? 0.0 : this.arrowButton.snappedLeftInset() + d7 + this.arrowButton.snappedRightInset();
        if (this.displayNode != null) {
            this.displayNode.resizeRelocate(d2, d3, d4 - d6, d5);
        }
        this.arrowButton.setVisible(!this.isButton());
        if (!this.isButton()) {
            this.arrowButton.resize(d6, d5);
            this.positionInArea(this.arrowButton, d2 + d4 - d6, d3, d6, d5, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        if (this.displayNode == null) {
            this.updateDisplayArea();
        }
        double d7 = this.snapSize(this.arrow.prefWidth(-1.0));
        double d8 = this.isButton() ? 0.0 : this.arrowButton.snappedLeftInset() + d7 + this.arrowButton.snappedRightInset();
        double d9 = this.displayNode == null ? 0.0 : this.displayNode.prefWidth(d2);
        double d10 = d9 + d8;
        return d6 + d10 + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7;
        if (this.displayNode == null) {
            this.updateDisplayArea();
        }
        if (this.displayNode == null) {
            double d8 = this.isButton() ? 0.0 : this.arrowButton.snappedTopInset() + this.arrow.prefHeight(-1.0) + this.arrowButton.snappedBottomInset();
            d7 = Math.max(21.0, d8);
        } else {
            d7 = this.displayNode.prefHeight(d2);
        }
        return d3 + d7 + d5;
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        return ((ComboBoxBase)this.getSkinnable()).prefWidth(d2);
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        return ((ComboBoxBase)this.getSkinnable()).prefHeight(d2);
    }

    @Override
    protected double computeBaselineOffset(double d2, double d3, double d4, double d5) {
        if (this.displayNode == null) {
            this.updateDisplayArea();
        }
        if (this.displayNode != null) {
            return this.displayNode.getLayoutBounds().getMinY() + this.displayNode.getLayoutY() + this.displayNode.getBaselineOffset();
        }
        return super.computeBaselineOffset(d2, d3, d4, d5);
    }
}

