/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.IndexedCell;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public abstract class TableCellSkinBase<C extends IndexedCell, B extends CellBehaviorBase<C>>
extends CellSkinBase<C, B> {
    static final String DEFER_TO_PARENT_PREF_WIDTH = "deferToParentPrefWidth";
    boolean isDeferToParentForPrefWidth = false;
    private InvalidationListener columnWidthListener = observable -> ((IndexedCell)this.getSkinnable()).requestLayout();
    private WeakInvalidationListener weakColumnWidthListener = new WeakInvalidationListener(this.columnWidthListener);

    protected abstract ReadOnlyDoubleProperty columnWidthProperty();

    protected abstract BooleanProperty columnVisibleProperty();

    public TableCellSkinBase(C c2, B b2) {
        super(c2, b2);
    }

    protected void init(C c2) {
        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(((Region)c2).widthProperty());
        rectangle.heightProperty().bind(((Region)c2).heightProperty());
        ((IndexedCell)this.getSkinnable()).setClip(rectangle);
        ReadOnlyDoubleProperty readOnlyDoubleProperty = this.columnWidthProperty();
        if (readOnlyDoubleProperty != null) {
            readOnlyDoubleProperty.addListener(this.weakColumnWidthListener);
        }
        this.registerChangeListener(((Node)c2).visibleProperty(), "VISIBLE");
        if (((Node)c2).getProperties().containsKey("deferToParentPrefWidth")) {
            this.isDeferToParentForPrefWidth = true;
        }
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("VISIBLE".equals(string)) {
            ((IndexedCell)this.getSkinnable()).setVisible(this.columnVisibleProperty().get());
        }
    }

    @Override
    public void dispose() {
        ReadOnlyDoubleProperty readOnlyDoubleProperty = this.columnWidthProperty();
        if (readOnlyDoubleProperty != null) {
            readOnlyDoubleProperty.removeListener(this.weakColumnWidthListener);
        }
        super.dispose();
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        this.layoutLabelInArea(d2, d3, d4, d5 - ((IndexedCell)this.getSkinnable()).getPadding().getBottom());
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        if (this.isDeferToParentForPrefWidth) {
            return super.computePrefWidth(d2, d3, d4, d5, d6);
        }
        return this.columnWidthProperty().get();
    }
}

