/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.util.Utils;
import javafx.scene.control.ScrollBar;

public class VirtualScrollBar
extends ScrollBar {
    private final VirtualFlow flow;
    private boolean virtual;
    private boolean adjusting;

    public VirtualScrollBar(VirtualFlow virtualFlow) {
        this.flow = virtualFlow;
        super.valueProperty().addListener(observable -> {
            if (this.isVirtual() && !this.adjusting) {
                virtualFlow.setPosition(this.getValue());
            }
        });
    }

    public void setVirtual(boolean bl) {
        this.virtual = bl;
    }

    public boolean isVirtual() {
        return this.virtual;
    }

    @Override
    public void decrement() {
        if (this.isVirtual()) {
            this.flow.adjustPixels(-10.0);
        } else {
            super.decrement();
        }
    }

    @Override
    public void increment() {
        if (this.isVirtual()) {
            this.flow.adjustPixels(10.0);
        } else {
            super.increment();
        }
    }

    @Override
    public void adjustValue(double d2) {
        if (this.isVirtual()) {
            this.adjusting = true;
            double d3 = this.flow.getPosition();
            double d4 = (this.getMax() - this.getMin()) * Utils.clamp(0.0, d2, 1.0) + this.getMin();
            if (d4 < d3) {
                Object t2 = this.flow.getFirstVisibleCell();
                if (t2 == null) {
                    return;
                }
                this.flow.showAsLast(t2);
            } else if (d4 > d3) {
                Object t3 = this.flow.getLastVisibleCell();
                if (t3 == null) {
                    return;
                }
                this.flow.showAsFirst(t3);
            }
            this.adjusting = false;
        } else {
            super.adjustValue(d2);
        }
    }
}

