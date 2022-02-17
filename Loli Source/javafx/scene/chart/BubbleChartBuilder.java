/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.scene.chart.Axis;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.XYChartBuilder;

@Deprecated
public class BubbleChartBuilder<X, Y, B extends BubbleChartBuilder<X, Y, B>>
extends XYChartBuilder<X, Y, B> {
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected BubbleChartBuilder() {
    }

    public static <X, Y> BubbleChartBuilder<X, Y, ?> create() {
        return new BubbleChartBuilder();
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
    public BubbleChart<X, Y> build() {
        BubbleChart<X, Y> bubbleChart = new BubbleChart<X, Y>(this.XAxis, this.YAxis);
        this.applyTo(bubbleChart);
        return bubbleChart;
    }
}

