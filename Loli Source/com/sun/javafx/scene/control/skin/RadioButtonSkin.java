/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ToggleButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;

public class RadioButtonSkin
extends LabeledSkinBase<RadioButton, ToggleButtonBehavior<RadioButton>> {
    private StackPane radio = RadioButtonSkin.createRadio();

    public RadioButtonSkin(RadioButton radioButton) {
        super(radioButton, new ToggleButtonBehavior<RadioButton>(radioButton));
        this.updateChildren();
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        if (this.radio != null) {
            this.getChildren().add(this.radio);
        }
    }

    private static StackPane createRadio() {
        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().setAll("radio");
        stackPane.setSnapToPixel(false);
        StackPane stackPane2 = new StackPane();
        stackPane2.getStyleClass().setAll("dot");
        stackPane.getChildren().clear();
        stackPane.getChildren().addAll(stackPane2);
        return stackPane;
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        return super.computeMinWidth(d2, d3, d4, d5, d6) + this.snapSize(this.radio.minWidth(-1.0));
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        return Math.max(this.snapSize(super.computeMinHeight(d2 - this.radio.minWidth(-1.0), d3, d4, d5, d6)), d3 + this.radio.minHeight(-1.0) + d5);
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        return super.computePrefWidth(d2, d3, d4, d5, d6) + this.snapSize(this.radio.prefWidth(-1.0));
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        return Math.max(this.snapSize(super.computePrefHeight(d2 - this.radio.prefWidth(-1.0), d3, d4, d5, d6)), d3 + this.radio.prefHeight(-1.0) + d5);
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        RadioButton radioButton = (RadioButton)this.getSkinnable();
        double d6 = this.radio.prefWidth(-1.0);
        double d7 = this.radio.prefHeight(-1.0);
        double d8 = Math.max(radioButton.prefWidth(-1.0), radioButton.minWidth(-1.0));
        double d9 = Math.min(d8 - d6, d4 - this.snapSize(d6));
        double d10 = Math.min(radioButton.prefHeight(d9), d5);
        double d11 = Math.max(d7, d10);
        double d12 = Utils.computeXOffset(d4, d9 + d6, radioButton.getAlignment().getHpos()) + d2;
        double d13 = Utils.computeYOffset(d5, d11, radioButton.getAlignment().getVpos()) + d3;
        this.layoutLabelInArea(d12 + d6, d13, d9, d11, radioButton.getAlignment());
        this.radio.resize(this.snapSize(d6), this.snapSize(d7));
        this.positionInArea(this.radio, d12, d13, d6, d11, 0.0, radioButton.getAlignment().getHpos(), radioButton.getAlignment().getVpos());
    }
}

