/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChartBuilder;

@Deprecated
public class BarChartBuilder<X, Y, B extends BarChartBuilder<X, Y, B>>
extends XYChartBuilder<X, Y, B> {
    private int __set;
    private double barGap;
    private double categoryGap;
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected BarChartBuilder() {
    }

    public static <X, Y> BarChartBuilder<X, Y, ?> create() {
        return new BarChartBuilder();
    }

    @Override
    public void applyTo(BarChart<X, Y> barChart) {
        super.applyTo(barChart);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            barChart.setBarGap(this.barGap);
        }
        if ((n2 & 2) != 0) {
            barChart.setCategoryGap(this.categoryGap);
        }
    }

    public B barGap(double d2) {
        this.barGap = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B categoryGap(double d2) {
        this.categoryGap = d2;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public B XAxis(Axis<X> axis) {
        this.XAxis = axis;
        return (B)this;
    }

    @Override
    public B YAxis(Axis<Y> axis) {
        this.YAxis = axis;
        return (B)this;
    }

    @Override
    public BarChart<X, Y> build() {
        BarChart<X, Y> barChart = new BarChart<X, Y>(this.XAxis, this.YAxis);
        this.applyTo(barChart);
        return barChart;
    }
}

