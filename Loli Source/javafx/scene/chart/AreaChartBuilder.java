/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChartBuilder;

@Deprecated
public class AreaChartBuilder<X, Y, B extends AreaChartBuilder<X, Y, B>>
extends XYChartBuilder<X, Y, B> {
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected AreaChartBuilder() {
    }

    public static <X, Y> AreaChartBuilder<X, Y, ?> create() {
        return new AreaChartBuilder();
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
    public AreaChart<X, Y> build() {
        AreaChart<X, Y> areaChart = new AreaChart<X, Y>(this.XAxis, this.YAxis);
        this.applyTo(areaChart);
        return areaChart;
    }
}

