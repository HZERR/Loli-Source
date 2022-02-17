/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.DateCellBehavior;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import javafx.scene.control.DateCell;
import javafx.scene.text.Text;

public class DateCellSkin
extends CellSkinBase<DateCell, DateCellBehavior> {
    public DateCellSkin(DateCell dateCell) {
        super(dateCell, new DateCellBehavior(dateCell));
        dateCell.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        Text text = (Text)((DateCell)this.getSkinnable()).getProperties().get("DateCell.secondaryText");
        if (text != null) {
            text.setManaged(false);
            this.getChildren().add(text);
        }
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        super.layoutChildren(d2, d3, d4, d5);
        Text text = (Text)((DateCell)this.getSkinnable()).getProperties().get("DateCell.secondaryText");
        if (text != null) {
            double d6 = d2 + d4 - this.rightLabelPadding() - text.getLayoutBounds().getWidth();
            double d7 = d3 + d5 - this.bottomLabelPadding() - text.getLayoutBounds().getHeight();
            text.relocate(this.snapPosition(d6), this.snapPosition(d7));
        }
    }

    private double cellSize() {
        double d2 = this.getCellSize();
        Text text = (Text)((DateCell)this.getSkinnable()).getProperties().get("DateCell.secondaryText");
        if (text != null && d2 == 24.0) {
            d2 = 36.0;
        }
        return d2;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = super.computePrefWidth(d2, d3, d4, d5, d6);
        return this.snapSize(Math.max(d7, this.cellSize()));
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = super.computePrefHeight(d2, d3, d4, d5, d6);
        return this.snapSize(Math.max(d7, this.cellSize()));
    }
}

