/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollToEvent;

public abstract class VirtualContainerBase<C extends Control, B extends BehaviorBase<C>, I extends IndexedCell>
extends BehaviorSkinBase<C, B> {
    protected boolean rowCountDirty;
    protected final VirtualFlow<I> flow = this.createVirtualFlow();

    public VirtualContainerBase(C c2, B b2) {
        super(c2, b2);
        ((Node)c2).addEventHandler(ScrollToEvent.scrollToTopIndex(), scrollToEvent -> {
            if (this.rowCountDirty) {
                this.updateRowCount();
                this.rowCountDirty = false;
            }
            this.flow.scrollTo((Integer)scrollToEvent.getScrollTarget());
        });
    }

    public abstract I createCell();

    protected VirtualFlow<I> createVirtualFlow() {
        return new VirtualFlow();
    }

    public abstract int getItemCount();

    protected abstract void updateRowCount();

    double getMaxCellWidth(int n2) {
        return this.snappedLeftInset() + this.flow.getMaxCellWidth(n2) + this.snappedRightInset();
    }

    double getVirtualFlowPreferredHeight(int n2) {
        double d2 = 1.0;
        for (int i2 = 0; i2 < n2 && i2 < this.getItemCount(); ++i2) {
            d2 += this.flow.getCellLength(i2);
        }
        return d2 + this.snappedTopInset() + this.snappedBottomInset();
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        this.checkState();
    }

    protected void checkState() {
        if (this.rowCountDirty) {
            this.updateRowCount();
            this.rowCountDirty = false;
        }
    }
}

