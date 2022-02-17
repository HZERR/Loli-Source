/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.geometry.Insets;
import javafx.scene.ParentBuilder;
import javafx.scene.layout.Region;
import javafx.util.Builder;

@Deprecated
public class RegionBuilder<B extends RegionBuilder<B>>
extends ParentBuilder<B>
implements Builder<Region> {
    private int __set;
    private double maxHeight;
    private double maxWidth;
    private double minHeight;
    private double minWidth;
    private Insets padding;
    private double prefHeight;
    private double prefWidth;
    private boolean snapToPixel;

    protected RegionBuilder() {
    }

    public static RegionBuilder<?> create() {
        return new RegionBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Region region) {
        super.applyTo(region);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    region.setMaxHeight(this.maxHeight);
                    break;
                }
                case 1: {
                    region.setMaxWidth(this.maxWidth);
                    break;
                }
                case 2: {
                    region.setMinHeight(this.minHeight);
                    break;
                }
                case 3: {
                    region.setMinWidth(this.minWidth);
                    break;
                }
                case 4: {
                    region.setPadding(this.padding);
                    break;
                }
                case 5: {
                    region.setPrefHeight(this.prefHeight);
                    break;
                }
                case 6: {
                    region.setPrefWidth(this.prefWidth);
                    break;
                }
                case 7: {
                    region.setSnapToPixel(this.snapToPixel);
                }
            }
        }
    }

    public B maxHeight(double d2) {
        this.maxHeight = d2;
        this.__set(0);
        return (B)this;
    }

    public B maxWidth(double d2) {
        this.maxWidth = d2;
        this.__set(1);
        return (B)this;
    }

    public B minHeight(double d2) {
        this.minHeight = d2;
        this.__set(2);
        return (B)this;
    }

    public B minWidth(double d2) {
        this.minWidth = d2;
        this.__set(3);
        return (B)this;
    }

    public B padding(Insets insets) {
        this.padding = insets;
        this.__set(4);
        return (B)this;
    }

    public B prefHeight(double d2) {
        this.prefHeight = d2;
        this.__set(5);
        return (B)this;
    }

    public B prefWidth(double d2) {
        this.prefWidth = d2;
        this.__set(6);
        return (B)this;
    }

    public B snapToPixel(boolean bl) {
        this.snapToPixel = bl;
        this.__set(7);
        return (B)this;
    }

    @Override
    public Region build() {
        Region region = new Region();
        this.applyTo(region);
        return region;
    }
}

