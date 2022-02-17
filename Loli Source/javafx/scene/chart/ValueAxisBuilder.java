/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.scene.chart.AxisBuilder;
import javafx.scene.chart.ValueAxis;
import javafx.util.StringConverter;

@Deprecated
public abstract class ValueAxisBuilder<T extends Number, B extends ValueAxisBuilder<T, B>>
extends AxisBuilder<T, B> {
    private int __set;
    private double lowerBound;
    private int minorTickCount;
    private double minorTickLength;
    private boolean minorTickVisible;
    private StringConverter<T> tickLabelFormatter;
    private double upperBound;

    protected ValueAxisBuilder() {
    }

    @Override
    public void applyTo(ValueAxis<T> valueAxis) {
        super.applyTo(valueAxis);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            valueAxis.setLowerBound(this.lowerBound);
        }
        if ((n2 & 2) != 0) {
            valueAxis.setMinorTickCount(this.minorTickCount);
        }
        if ((n2 & 4) != 0) {
            valueAxis.setMinorTickLength(this.minorTickLength);
        }
        if ((n2 & 8) != 0) {
            valueAxis.setMinorTickVisible(this.minorTickVisible);
        }
        if ((n2 & 0x10) != 0) {
            valueAxis.setTickLabelFormatter(this.tickLabelFormatter);
        }
        if ((n2 & 0x20) != 0) {
            valueAxis.setUpperBound(this.upperBound);
        }
    }

    public B lowerBound(double d2) {
        this.lowerBound = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B minorTickCount(int n2) {
        this.minorTickCount = n2;
        this.__set |= 2;
        return (B)this;
    }

    public B minorTickLength(double d2) {
        this.minorTickLength = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B minorTickVisible(boolean bl) {
        this.minorTickVisible = bl;
        this.__set |= 8;
        return (B)this;
    }

    public B tickLabelFormatter(StringConverter<T> stringConverter) {
        this.tickLabelFormatter = stringConverter;
        this.__set |= 0x10;
        return (B)this;
    }

    public B upperBound(double d2) {
        this.upperBound = d2;
        this.__set |= 0x20;
        return (B)this;
    }
}

