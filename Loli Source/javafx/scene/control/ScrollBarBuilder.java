/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.geometry.Orientation;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.ScrollBar;
import javafx.util.Builder;

@Deprecated
public class ScrollBarBuilder<B extends ScrollBarBuilder<B>>
extends ControlBuilder<B>
implements Builder<ScrollBar> {
    private int __set;
    private double blockIncrement;
    private double max;
    private double min;
    private Orientation orientation;
    private double unitIncrement;
    private double value;
    private double visibleAmount;

    protected ScrollBarBuilder() {
    }

    public static ScrollBarBuilder<?> create() {
        return new ScrollBarBuilder();
    }

    public void applyTo(ScrollBar scrollBar) {
        super.applyTo(scrollBar);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            scrollBar.setBlockIncrement(this.blockIncrement);
        }
        if ((n2 & 2) != 0) {
            scrollBar.setMax(this.max);
        }
        if ((n2 & 4) != 0) {
            scrollBar.setMin(this.min);
        }
        if ((n2 & 8) != 0) {
            scrollBar.setOrientation(this.orientation);
        }
        if ((n2 & 0x10) != 0) {
            scrollBar.setUnitIncrement(this.unitIncrement);
        }
        if ((n2 & 0x20) != 0) {
            scrollBar.setValue(this.value);
        }
        if ((n2 & 0x40) != 0) {
            scrollBar.setVisibleAmount(this.visibleAmount);
        }
    }

    public B blockIncrement(double d2) {
        this.blockIncrement = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B max(double d2) {
        this.max = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B min(double d2) {
        this.min = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B orientation(Orientation orientation) {
        this.orientation = orientation;
        this.__set |= 8;
        return (B)this;
    }

    public B unitIncrement(double d2) {
        this.unitIncrement = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B value(double d2) {
        this.value = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    public B visibleAmount(double d2) {
        this.visibleAmount = d2;
        this.__set |= 0x40;
        return (B)this;
    }

    @Override
    public ScrollBar build() {
        ScrollBar scrollBar = new ScrollBar();
        this.applyTo(scrollBar);
        return scrollBar;
    }
}

