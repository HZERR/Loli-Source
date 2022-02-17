/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.scene.chart.Axis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChartBuilder;

@Deprecated
public class ScatterChartBuilder<X, Y, B extends ScatterChartBuilder<X, Y, B>>
extends XYChartBuilder<X, Y, B> {
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected ScatterChartBuilder() {
    }

    public static <X, Y> ScatterChartBuilder<X, Y, ?> create() {
        return new ScatterChartBuilder();
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
    public ScatterChart<X, Y> build() {
        ScatterChart<X, Y> scatterChart = new ScatterChart<X, Y>(this.XAxis, this.YAxis);
        this.applyTo(scatterChart);
        return scatterChart;
    }
}

