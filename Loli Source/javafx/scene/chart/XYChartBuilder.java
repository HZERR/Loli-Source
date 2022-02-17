/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ChartBuilder;
import javafx.scene.chart.XYChart;

@Deprecated
public abstract class XYChartBuilder<X, Y, B extends XYChartBuilder<X, Y, B>>
extends ChartBuilder<B> {
    private int __set;
    private boolean alternativeColumnFillVisible;
    private boolean alternativeRowFillVisible;
    private ObservableList<XYChart.Series<X, Y>> data;
    private boolean horizontalGridLinesVisible;
    private boolean horizontalZeroLineVisible;
    private boolean verticalGridLinesVisible;
    private boolean verticalZeroLineVisible;
    private Axis<X> XAxis;
    private Axis<Y> YAxis;

    protected XYChartBuilder() {
    }

    public void applyTo(XYChart<X, Y> xYChart) {
        super.applyTo(xYChart);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            xYChart.setAlternativeColumnFillVisible(this.alternativeColumnFillVisible);
        }
        if ((n2 & 2) != 0) {
            xYChart.setAlternativeRowFillVisible(this.alternativeRowFillVisible);
        }
        if ((n2 & 4) != 0) {
            xYChart.setData(this.data);
        }
        if ((n2 & 8) != 0) {
            xYChart.setHorizontalGridLinesVisible(this.horizontalGridLinesVisible);
        }
        if ((n2 & 0x10) != 0) {
            xYChart.setHorizontalZeroLineVisible(this.horizontalZeroLineVisible);
        }
        if ((n2 & 0x20) != 0) {
            xYChart.setVerticalGridLinesVisible(this.verticalGridLinesVisible);
        }
        if ((n2 & 0x40) != 0) {
            xYChart.setVerticalZeroLineVisible(this.verticalZeroLineVisible);
        }
    }

    public B alternativeColumnFillVisible(boolean bl) {
        this.alternativeColumnFillVisible = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B alternativeRowFillVisible(boolean bl) {
        this.alternativeRowFillVisible = bl;
        this.__set |= 2;
        return (B)this;
    }

    public B data(ObservableList<XYChart.Series<X, Y>> observableList) {
        this.data = observableList;
        this.__set |= 4;
        return (B)this;
    }

    public B horizontalGridLinesVisible(boolean bl) {
        this.horizontalGridLinesVisible = bl;
        this.__set |= 8;
        return (B)this;
    }

    public B horizontalZeroLineVisible(boolean bl) {
        this.horizontalZeroLineVisible = bl;
        this.__set |= 0x10;
        return (B)this;
    }

    public B verticalGridLinesVisible(boolean bl) {
        this.verticalGridLinesVisible = bl;
        this.__set |= 0x20;
        return (B)this;
    }

    public B verticalZeroLineVisible(boolean bl) {
        this.verticalZeroLineVisible = bl;
        this.__set |= 0x40;
        return (B)this;
    }

    public B XAxis(Axis<X> axis) {
        this.XAxis = axis;
        return (B)this;
    }

    public B YAxis(Axis<Y> axis) {
        this.YAxis = axis;
        return (B)this;
    }
}

