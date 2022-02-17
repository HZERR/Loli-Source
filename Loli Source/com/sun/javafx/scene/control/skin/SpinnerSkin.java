/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SpinnerBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class SpinnerSkin<T>
extends BehaviorSkinBase<Spinner<T>, SpinnerBehavior<T>> {
    private TextField textField;
    private Region incrementArrow;
    private StackPane incrementArrowButton;
    private Region decrementArrow;
    private StackPane decrementArrowButton;
    private static final int ARROWS_ON_RIGHT_VERTICAL = 0;
    private static final int ARROWS_ON_LEFT_VERTICAL = 1;
    private static final int ARROWS_ON_RIGHT_HORIZONTAL = 2;
    private static final int ARROWS_ON_LEFT_HORIZONTAL = 3;
    private static final int SPLIT_ARROWS_VERTICAL = 4;
    private static final int SPLIT_ARROWS_HORIZONTAL = 5;
    private int layoutMode = 0;
    private static PseudoClass CONTAINS_FOCUS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("contains-focus");

    public SpinnerSkin(Spinner<T> spinner) {
        super(spinner, new SpinnerBehavior<T>(spinner));
        this.textField = spinner.getEditor();
        this.getChildren().add(this.textField);
        this.updateStyleClass();
        spinner.getStyleClass().addListener(change -> this.updateStyleClass());
        this.incrementArrow = new Region();
        this.incrementArrow.setFocusTraversable(false);
        this.incrementArrow.getStyleClass().setAll("increment-arrow");
        this.incrementArrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.incrementArrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.incrementArrow.setMouseTransparent(true);
        this.incrementArrowButton = new StackPane(){

            @Override
            public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
                switch (accessibleAction) {
                    case FIRE: {
                        ((Spinner)SpinnerSkin.this.getSkinnable()).increment();
                    }
                }
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        };
        this.incrementArrowButton.setAccessibleRole(AccessibleRole.INCREMENT_BUTTON);
        this.incrementArrowButton.setFocusTraversable(false);
        this.incrementArrowButton.getStyleClass().setAll("increment-arrow-button");
        this.incrementArrowButton.getChildren().add(this.incrementArrow);
        this.incrementArrowButton.setOnMousePressed(mouseEvent -> {
            ((Spinner)this.getSkinnable()).requestFocus();
            ((SpinnerBehavior)this.getBehavior()).startSpinning(true);
        });
        this.incrementArrowButton.setOnMouseReleased(mouseEvent -> ((SpinnerBehavior)this.getBehavior()).stopSpinning());
        this.decrementArrow = new Region();
        this.decrementArrow.setFocusTraversable(false);
        this.decrementArrow.getStyleClass().setAll("decrement-arrow");
        this.decrementArrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.decrementArrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.decrementArrow.setMouseTransparent(true);
        this.decrementArrowButton = new StackPane(){

            @Override
            public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
                switch (accessibleAction) {
                    case FIRE: {
                        ((Spinner)SpinnerSkin.this.getSkinnable()).decrement();
                    }
                }
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        };
        this.decrementArrowButton.setAccessibleRole(AccessibleRole.DECREMENT_BUTTON);
        this.decrementArrowButton.setFocusTraversable(false);
        this.decrementArrowButton.getStyleClass().setAll("decrement-arrow-button");
        this.decrementArrowButton.getChildren().add(this.decrementArrow);
        this.decrementArrowButton.setOnMousePressed(mouseEvent -> {
            ((Spinner)this.getSkinnable()).requestFocus();
            ((SpinnerBehavior)this.getBehavior()).startSpinning(false);
        });
        this.decrementArrowButton.setOnMouseReleased(mouseEvent -> ((SpinnerBehavior)this.getBehavior()).stopSpinning());
        this.getChildren().addAll(this.incrementArrowButton, this.decrementArrowButton);
        spinner.focusedProperty().addListener((observableValue, bl, bl2) -> ((ComboBoxPopupControl.FakeFocusTextField)this.textField).setFakeFocus((boolean)bl2));
        spinner.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (spinner.isEditable()) {
                if (keyEvent.getTarget().equals(this.textField)) {
                    return;
                }
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    return;
                }
                this.textField.fireEvent(keyEvent.copyFor(this.textField, this.textField));
                keyEvent.consume();
            }
        });
        this.textField.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (!spinner.isEditable()) {
                spinner.fireEvent(keyEvent.copyFor(spinner, spinner));
                keyEvent.consume();
            }
        });
        this.textField.focusedProperty().addListener((observableValue, bl, bl2) -> {
            spinner.getProperties().put("FOCUSED", bl2);
            if (!bl2.booleanValue()) {
                this.pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, false);
            } else {
                this.pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, true);
            }
        });
        this.textField.focusTraversableProperty().bind(spinner.editableProperty());
        spinner.setImpl_traversalEngine(new ParentTraversalEngine(spinner, new Algorithm(){

            @Override
            public Node select(Node node, Direction direction, TraversalContext traversalContext) {
                return null;
            }

            @Override
            public Node selectFirst(TraversalContext traversalContext) {
                return null;
            }

            @Override
            public Node selectLast(TraversalContext traversalContext) {
                return null;
            }
        }));
    }

    private void updateStyleClass() {
        ObservableList<String> observableList = ((Spinner)this.getSkinnable()).getStyleClass();
        this.layoutMode = observableList.contains("arrows-on-left-vertical") ? 1 : (observableList.contains("arrows-on-left-horizontal") ? 3 : (observableList.contains("arrows-on-right-horizontal") ? 2 : (observableList.contains("split-arrows-vertical") ? 4 : (observableList.contains("split-arrows-horizontal") ? 5 : 0))));
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6 = this.incrementArrowButton.snappedLeftInset() + this.snapSize(this.incrementArrow.prefWidth(-1.0)) + this.incrementArrowButton.snappedRightInset();
        double d7 = this.decrementArrowButton.snappedLeftInset() + this.snapSize(this.decrementArrow.prefWidth(-1.0)) + this.decrementArrowButton.snappedRightInset();
        double d8 = Math.max(d6, d7);
        if (this.layoutMode == 0 || this.layoutMode == 1) {
            double d9 = this.layoutMode == 0 ? d2 : d2 + d8;
            double d10 = this.layoutMode == 0 ? d2 + d4 - d8 : d2;
            double d11 = Math.floor(d5 / 2.0);
            this.textField.resizeRelocate(d9, d3, d4 - d8, d5);
            this.incrementArrowButton.resize(d8, d11);
            this.positionInArea(this.incrementArrowButton, d10, d3, d8, d11, 0.0, HPos.CENTER, VPos.CENTER);
            this.decrementArrowButton.resize(d8, d11);
            this.positionInArea(this.decrementArrowButton, d10, d3 + d11, d8, d5 - d11, 0.0, HPos.CENTER, VPos.BOTTOM);
        } else if (this.layoutMode == 2 || this.layoutMode == 3) {
            double d12 = d6 + d7;
            double d13 = this.layoutMode == 2 ? d2 : d2 + d12;
            double d14 = this.layoutMode == 2 ? d2 + d4 - d12 : d2;
            this.textField.resizeRelocate(d13, d3, d4 - d12, d5);
            this.decrementArrowButton.resize(d7, d5);
            this.positionInArea(this.decrementArrowButton, d14, d3, d7, d5, 0.0, HPos.CENTER, VPos.CENTER);
            this.incrementArrowButton.resize(d6, d5);
            this.positionInArea(this.incrementArrowButton, d14 + d7, d3, d6, d5, 0.0, HPos.CENTER, VPos.CENTER);
        } else if (this.layoutMode == 4) {
            double d15 = this.incrementArrowButton.snappedTopInset() + this.snapSize(this.incrementArrow.prefHeight(-1.0)) + this.incrementArrowButton.snappedBottomInset();
            double d16 = this.decrementArrowButton.snappedTopInset() + this.snapSize(this.decrementArrow.prefHeight(-1.0)) + this.decrementArrowButton.snappedBottomInset();
            double d17 = Math.max(d15, d16);
            this.incrementArrowButton.resize(d4, d17);
            this.positionInArea(this.incrementArrowButton, d2, d3, d4, d17, 0.0, HPos.CENTER, VPos.CENTER);
            this.textField.resizeRelocate(d2, d3 + d17, d4, d5 - 2.0 * d17);
            this.decrementArrowButton.resize(d4, d17);
            this.positionInArea(this.decrementArrowButton, d2, d5 - d17, d4, d17, 0.0, HPos.CENTER, VPos.CENTER);
        } else if (this.layoutMode == 5) {
            this.decrementArrowButton.resize(d8, d5);
            this.positionInArea(this.decrementArrowButton, d2, d3, d8, d5, 0.0, HPos.CENTER, VPos.CENTER);
            this.textField.resizeRelocate(d2 + d8, d3, d4 - 2.0 * d8, d5);
            this.incrementArrowButton.resize(d8, d5);
            this.positionInArea(this.incrementArrowButton, d4 - d8, d3, d8, d5, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        return this.computePrefHeight(d2, d3, d4, d5, d6);
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.textField.prefWidth(d2);
        return d6 + d7 + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.textField.prefHeight(d2);
        double d8 = this.layoutMode == 4 ? d3 + this.incrementArrowButton.prefHeight(d2) + d7 + this.decrementArrowButton.prefHeight(d2) + d5 : d3 + d7 + d5;
        return d8;
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        return ((Spinner)this.getSkinnable()).prefWidth(d2);
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        return ((Spinner)this.getSkinnable()).prefHeight(d2);
    }

    @Override
    protected double computeBaselineOffset(double d2, double d3, double d4, double d5) {
        return this.textField.getLayoutBounds().getMinY() + this.textField.getLayoutY() + this.textField.getBaselineOffset();
    }
}

