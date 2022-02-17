/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.PaneBuilder;
import javafx.scene.layout.RowConstraints;

@Deprecated
public class GridPaneBuilder<B extends GridPaneBuilder<B>>
extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private Collection<? extends ColumnConstraints> columnConstraints;
    private boolean gridLinesVisible;
    private double hgap;
    private Collection<? extends RowConstraints> rowConstraints;
    private double vgap;

    protected GridPaneBuilder() {
    }

    public static GridPaneBuilder<?> create() {
        return new GridPaneBuilder();
    }

    public void applyTo(GridPane gridPane) {
        super.applyTo(gridPane);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            gridPane.setAlignment(this.alignment);
        }
        if ((n2 & 2) != 0) {
            gridPane.getColumnConstraints().addAll(this.columnConstraints);
        }
        if ((n2 & 4) != 0) {
            gridPane.setGridLinesVisible(this.gridLinesVisible);
        }
        if ((n2 & 8) != 0) {
            gridPane.setHgap(this.hgap);
        }
        if ((n2 & 0x10) != 0) {
            gridPane.getRowConstraints().addAll(this.rowConstraints);
        }
        if ((n2 & 0x20) != 0) {
            gridPane.setVgap(this.vgap);
        }
    }

    public B alignment(Pos pos) {
        this.alignment = pos;
        this.__set |= 1;
        return (B)this;
    }

    public B columnConstraints(Collection<? extends ColumnConstraints> collection) {
        this.columnConstraints = collection;
        this.__set |= 2;
        return (B)this;
    }

    public B columnConstraints(ColumnConstraints ... arrcolumnConstraints) {
        return this.columnConstraints(Arrays.asList(arrcolumnConstraints));
    }

    public B gridLinesVisible(boolean bl) {
        this.gridLinesVisible = bl;
        this.__set |= 4;
        return (B)this;
    }

    public B hgap(double d2) {
        this.hgap = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B rowConstraints(Collection<? extends RowConstraints> collection) {
        this.rowConstraints = collection;
        this.__set |= 0x10;
        return (B)this;
    }

    public B rowConstraints(RowConstraints ... arrrowConstraints) {
        return this.rowConstraints(Arrays.asList(arrrowConstraints));
    }

    public B vgap(double d2) {
        this.vgap = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    @Override
    public GridPane build() {
        GridPane gridPane = new GridPane();
        this.applyTo(gridPane);
        return gridPane;
    }
}

