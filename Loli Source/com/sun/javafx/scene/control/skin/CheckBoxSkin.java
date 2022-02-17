/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;

public class CheckBoxSkin
extends LabeledSkinBase<CheckBox, ButtonBehavior<CheckBox>> {
    private final StackPane box = new StackPane();
    private StackPane innerbox;

    public CheckBoxSkin(CheckBox checkBox) {
        super(checkBox, new ButtonBehavior<CheckBox>(checkBox));
        this.box.getStyleClass().setAll("box");
        this.innerbox = new StackPane();
        this.innerbox.getStyleClass().setAll("mark");
        this.innerbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.box.getChildren().add(this.innerbox);
        this.updateChildren();
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        if (this.box != null) {
            this.getChildren().add(this.box);
        }
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        return super.computeMinWidth(d2, d3, d4, d5, d6) + this.snapSize(this.box.minWidth(-1.0));
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        return Math.max(super.computeMinHeight(d2 - this.box.minWidth(-1.0), d3, d4, d5, d6), d3 + this.box.minHeight(-1.0) + d5);
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        return super.computePrefWidth(d2, d3, d4, d5, d6) + this.snapSize(this.box.prefWidth(-1.0));
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        return Math.max(super.computePrefHeight(d2 - this.box.prefWidth(-1.0), d3, d4, d5, d6), d3 + this.box.prefHeight(-1.0) + d5);
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        CheckBox checkBox = (CheckBox)this.getSkinnable();
        double d6 = this.snapSize(this.box.prefWidth(-1.0));
        double d7 = this.snapSize(this.box.prefHeight(-1.0));
        double d8 = Math.max(checkBox.prefWidth(-1.0), checkBox.minWidth(-1.0));
        double d9 = Math.min(d8 - d6, d4 - this.snapSize(d6));
        double d10 = Math.min(checkBox.prefHeight(d9), d5);
        double d11 = Math.max(d7, d10);
        double d12 = Utils.computeXOffset(d4, d9 + d6, checkBox.getAlignment().getHpos()) + d2;
        double d13 = Utils.computeYOffset(d5, d11, checkBox.getAlignment().getVpos()) + d2;
        this.layoutLabelInArea(d12 + d6, d13, d9, d11, checkBox.getAlignment());
        this.box.resize(d6, d7);
        this.positionInArea(this.box, d12, d13, d6, d11, 0.0, checkBox.getAlignment().getHpos(), checkBox.getAlignment().getVpos());
    }
}

