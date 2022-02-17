/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.geometry.Side;
import javafx.scene.chart.Chart;
import javafx.scene.layout.RegionBuilder;

@Deprecated
public abstract class ChartBuilder<B extends ChartBuilder<B>>
extends RegionBuilder<B> {
    private int __set;
    private boolean animated;
    private Side legendSide;
    private boolean legendVisible;
    private String title;
    private Side titleSide;

    protected ChartBuilder() {
    }

    public void applyTo(Chart chart) {
        super.applyTo(chart);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            chart.setAnimated(this.animated);
        }
        if ((n2 & 2) != 0) {
            chart.setLegendSide(this.legendSide);
        }
        if ((n2 & 4) != 0) {
            chart.setLegendVisible(this.legendVisible);
        }
        if ((n2 & 8) != 0) {
            chart.setTitle(this.title);
        }
        if ((n2 & 0x10) != 0) {
            chart.setTitleSide(this.titleSide);
        }
    }

    public B animated(boolean bl) {
        this.animated = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B legendSide(Side side) {
        this.legendSide = side;
        this.__set |= 2;
        return (B)this;
    }

    public B legendVisible(boolean bl) {
        this.legendVisible = bl;
        this.__set |= 4;
        return (B)this;
    }

    public B title(String string) {
        this.title = string;
        this.__set |= 8;
        return (B)this;
    }

    public B titleSide(Side side) {
        this.titleSide = side;
        this.__set |= 0x10;
        return (B)this;
    }
}

