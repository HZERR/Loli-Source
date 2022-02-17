/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.geometry.VPos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Builder;

@Deprecated
public class RowConstraintsBuilder<B extends RowConstraintsBuilder<B>>
implements Builder<RowConstraints> {
    private int __set;
    private boolean fillHeight;
    private double maxHeight;
    private double minHeight;
    private double percentHeight;
    private double prefHeight;
    private VPos valignment;
    private Priority vgrow;

    protected RowConstraintsBuilder() {
    }

    public static RowConstraintsBuilder<?> create() {
        return new RowConstraintsBuilder();
    }

    public void applyTo(RowConstraints rowConstraints) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            rowConstraints.setFillHeight(this.fillHeight);
        }
        if ((n2 & 2) != 0) {
            rowConstraints.setMaxHeight(this.maxHeight);
        }
        if ((n2 & 4) != 0) {
            rowConstraints.setMinHeight(this.minHeight);
        }
        if ((n2 & 8) != 0) {
            rowConstraints.setPercentHeight(this.percentHeight);
        }
        if ((n2 & 0x10) != 0) {
            rowConstraints.setPrefHeight(this.prefHeight);
        }
        if ((n2 & 0x20) != 0) {
            rowConstraints.setValignment(this.valignment);
        }
        if ((n2 & 0x40) != 0) {
            rowConstraints.setVgrow(this.vgrow);
        }
    }

    public B fillHeight(boolean bl) {
        this.fillHeight = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B maxHeight(double d2) {
        this.maxHeight = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B minHeight(double d2) {
        this.minHeight = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B percentHeight(double d2) {
        this.percentHeight = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B prefHeight(double d2) {
        this.prefHeight = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B valignment(VPos vPos) {
        this.valignment = vPos;
        this.__set |= 0x20;
        return (B)this;
    }

    public B vgrow(Priority priority) {
        this.vgrow = priority;
        this.__set |= 0x40;
        return (B)this;
    }

    @Override
    public RowConstraints build() {
        RowConstraints rowConstraints = new RowConstraints();
        this.applyTo(rowConstraints);
        return rowConstraints;
    }
}

