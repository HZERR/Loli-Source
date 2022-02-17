/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.collections.ObservableList;
import javafx.scene.chart.ChartBuilder;
import javafx.scene.chart.PieChart;

@Deprecated
public class PieChartBuilder<B extends PieChartBuilder<B>>
extends ChartBuilder<B> {
    private int __set;
    private boolean clockwise;
    private ObservableList<PieChart.Data> data;
    private double labelLineLength;
    private boolean labelsVisible;
    private double startAngle;

    protected PieChartBuilder() {
    }

    public static PieChartBuilder<?> create() {
        return new PieChartBuilder();
    }

    public void applyTo(PieChart pieChart) {
        super.applyTo(pieChart);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            pieChart.setClockwise(this.clockwise);
        }
        if ((n2 & 2) != 0) {
            pieChart.setData(this.data);
        }
        if ((n2 & 4) != 0) {
            pieChart.setLabelLineLength(this.labelLineLength);
        }
        if ((n2 & 8) != 0) {
            pieChart.setLabelsVisible(this.labelsVisible);
        }
        if ((n2 & 0x10) != 0) {
            pieChart.setStartAngle(this.startAngle);
        }
    }

    public B clockwise(boolean bl) {
        this.clockwise = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B data(ObservableList<PieChart.Data> observableList) {
        this.data = observableList;
        this.__set |= 2;
        return (B)this;
    }

    public B labelLineLength(double d2) {
        this.labelLineLength = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B labelsVisible(boolean bl) {
        this.labelsVisible = bl;
        this.__set |= 8;
        return (B)this;
    }

    public B startAngle(double d2) {
        this.startAngle = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    @Override
    public PieChart build() {
        PieChart pieChart = new PieChart();
        this.applyTo(pieChart);
        return pieChart;
    }
}

