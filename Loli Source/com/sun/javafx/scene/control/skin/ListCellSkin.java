/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ListCellBehavior;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class ListCellSkin<T>
extends CellSkinBase<ListCell<T>, ListCellBehavior<T>> {
    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;

    public ListCellSkin(ListCell<T> listCell) {
        super(listCell, new ListCellBehavior<T>(listCell));
        this.fixedCellSize = listCell.getListView().getFixedCellSize();
        this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        this.registerChangeListener(listCell.getListView().fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("FIXED_CELL_SIZE".equals(string)) {
            this.fixedCellSize = ((ListCell)this.getSkinnable()).getListView().getFixedCellSize();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        }
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = super.computePrefWidth(d2, d3, d4, d5, d6);
        ListView listView = ((ListCell)this.getSkinnable()).getListView();
        return listView == null ? 0.0 : (listView.getOrientation() == Orientation.VERTICAL ? d7 : Math.max(d7, this.getCellSize()));
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        double d7 = this.getCellSize();
        double d8 = d7 == 24.0 ? super.computePrefHeight(d2, d3, d4, d5, d6) : d7;
        return d8;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMinHeight(d2, d3, d4, d5, d6);
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMaxHeight(d2, d3, d4, d5, d6);
    }
}

