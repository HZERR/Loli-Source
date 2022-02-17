/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChartBuilder;

@Deprecated
public class LineChartBuilder<X, Y, B extends LineChartBuilder<X, Y, B>>
extends XYChartBuilder<X, Y, B> {
    private boolean __set;
    private boolean createSymbols;
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected LineChartBuilder() {
    }

    public static <X, Y> LineChartBuilder<X, Y, ?> create() {
        return new LineChartBuilder();
    }

    @Override
    public void applyTo(LineChart<X, Y> lineChart) {
        super.applyTo(lineChart);
        if (this.__set) {
            lineChart.setCreateSymbols(this.createSymbols);
        }
    }

    public B createSymbols(boolean bl) {
        this.createSymbols = bl;
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
    public LineChart<X, Y> build() {
        LineChart<X, Y> lineChart = new LineChart<X, Y>(this.XAxis, this.YAxis);
        this.applyTo(lineChart);
        return lineChart;
    }
}

