/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.PaneBuilder;

@Deprecated
public class FlowPaneBuilder<B extends FlowPaneBuilder<B>>
extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private HPos columnHalignment;
    private double hgap;
    private Orientation orientation;
    private double prefWrapLength;
    private VPos rowValignment;
    private double vgap;

    protected FlowPaneBuilder() {
    }

    public static FlowPaneBuilder<?> create() {
        return new FlowPaneBuilder();
    }

    public void applyTo(FlowPane flowPane) {
        super.applyTo(flowPane);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            flowPane.setAlignment(this.alignment);
        }
        if ((n2 & 2) != 0) {
            flowPane.setColumnHalignment(this.columnHalignment);
        }
        if ((n2 & 4) != 0) {
            flowPane.setHgap(this.hgap);
        }
        if ((n2 & 8) != 0) {
            flowPane.setOrientation(this.orientation);
        }
        if ((n2 & 0x10) != 0) {
            flowPane.setPrefWrapLength(this.prefWrapLength);
        }
        if ((n2 & 0x20) != 0) {
            flowPane.setRowValignment(this.rowValignment);
        }
        if ((n2 & 0x40) != 0) {
            flowPane.setVgap(this.vgap);
        }
    }

    public B alignment(Pos pos) {
        this.alignment = pos;
        this.__set |= 1;
        return (B)this;
    }

    public B columnHalignment(HPos hPos) {
        this.columnHalignment = hPos;
        this.__set |= 2;
        return (B)this;
    }

    public B hgap(double d2) {
        this.hgap = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B orientation(Orientation orientation) {
        this.orientation = orientation;
        this.__set |= 8;
        return (B)this;
    }

    public B prefWrapLength(double d2) {
        this.prefWrapLength = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B rowValignment(VPos vPos) {
        this.rowValignment = vPos;
        this.__set |= 0x20;
        return (B)this;
    }

    public B vgap(double d2) {
        this.vgap = d2;
        this.__set |= 0x40;
        return (B)this;
    }

    @Override
    public FlowPane build() {
        FlowPane flowPane = new FlowPane();
        this.applyTo(flowPane);
        return flowPane;
    }
}

