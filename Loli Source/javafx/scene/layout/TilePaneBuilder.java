/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.PaneBuilder;
import javafx.scene.layout.TilePane;

@Deprecated
public class TilePaneBuilder<B extends TilePaneBuilder<B>>
extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private double hgap;
    private Orientation orientation;
    private int prefColumns;
    private int prefRows;
    private double prefTileHeight;
    private double prefTileWidth;
    private Pos tileAlignment;
    private double vgap;

    protected TilePaneBuilder() {
    }

    public static TilePaneBuilder<?> create() {
        return new TilePaneBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(TilePane tilePane) {
        super.applyTo(tilePane);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    tilePane.setAlignment(this.alignment);
                    break;
                }
                case 1: {
                    tilePane.setHgap(this.hgap);
                    break;
                }
                case 2: {
                    tilePane.setOrientation(this.orientation);
                    break;
                }
                case 3: {
                    tilePane.setPrefColumns(this.prefColumns);
                    break;
                }
                case 4: {
                    tilePane.setPrefRows(this.prefRows);
                    break;
                }
                case 5: {
                    tilePane.setPrefTileHeight(this.prefTileHeight);
                    break;
                }
                case 6: {
                    tilePane.setPrefTileWidth(this.prefTileWidth);
                    break;
                }
                case 7: {
                    tilePane.setTileAlignment(this.tileAlignment);
                    break;
                }
                case 8: {
                    tilePane.setVgap(this.vgap);
                }
            }
        }
    }

    public B alignment(Pos pos) {
        this.alignment = pos;
        this.__set(0);
        return (B)this;
    }

    public B hgap(double d2) {
        this.hgap = d2;
        this.__set(1);
        return (B)this;
    }

    public B orientation(Orientation orientation) {
        this.orientation = orientation;
        this.__set(2);
        return (B)this;
    }

    public B prefColumns(int n2) {
        this.prefColumns = n2;
        this.__set(3);
        return (B)this;
    }

    public B prefRows(int n2) {
        this.prefRows = n2;
        this.__set(4);
        return (B)this;
    }

    public B prefTileHeight(double d2) {
        this.prefTileHeight = d2;
        this.__set(5);
        return (B)this;
    }

    public B prefTileWidth(double d2) {
        this.prefTileWidth = d2;
        this.__set(6);
        return (B)this;
    }

    public B tileAlignment(Pos pos) {
        this.tileAlignment = pos;
        this.__set(7);
        return (B)this;
    }

    public B vgap(double d2) {
        this.vgap = d2;
        this.__set(8);
        return (B)this;
    }

    @Override
    public TilePane build() {
        TilePane tilePane = new TilePane();
        this.applyTo(tilePane);
        return tilePane;
    }
}

