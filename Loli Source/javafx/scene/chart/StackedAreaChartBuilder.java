/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.scene.chart.Axis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChartBuilder;

@Deprecated
public class StackedAreaChartBuilder<X, Y, B extends StackedAreaChartBuilder<X, Y, B>>
extends XYChartBuilder<X, Y, B> {
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected StackedAreaChartBuilder() {
    }

    public static <X, Y> StackedAreaChartBuilder<X, Y, ?> create() {
        return new StackedAreaChartBuilder();
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
    public StackedAreaChart<X, Y> build() {
        StackedAreaChart<X, Y> stackedAreaChart = new StackedAreaChart<X, Y>(this.XAxis, this.YAxis);
        this.applyTo(stackedAreaChart);
        return stackedAreaChart;
    }
}

