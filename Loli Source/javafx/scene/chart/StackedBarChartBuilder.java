/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.scene.chart.Axis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChartBuilder;

@Deprecated
public class StackedBarChartBuilder<X, Y, B extends StackedBarChartBuilder<X, Y, B>>
extends XYChartBuilder<X, Y, B> {
    private boolean __set;
    private double categoryGap;
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected StackedBarChartBuilder() {
    }

    public static <X, Y> StackedBarChartBuilder<X, Y, ?> create() {
        return new StackedBarChartBuilder();
    }

    @Override
    public void applyTo(StackedBarChart<X, Y> stackedBarChart) {
        super.applyTo(stackedBarChart);
        if (this.__set) {
            stackedBarChart.setCategoryGap(this.categoryGap);
        }
    }

    public B categoryGap(double d2) {
        this.categoryGap = d2;
        this.__set = true;
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
    public StackedBarChart<X, Y> build() {
        StackedBarChart<X, Y> stackedBarChart = new StackedBarChart<X, Y>(this.XAxis, this.YAxis);
        this.applyTo(stackedBarChart);
        return stackedBarChart;
    }
}

