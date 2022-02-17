/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxisBuilder;

@Deprecated
public final class NumberAxisBuilder
extends ValueAxisBuilder<Number, NumberAxisBuilder> {
    private int __set;
    private boolean forceZeroInRange;
    private double tickUnit;

    protected NumberAxisBuilder() {
    }

    public static NumberAxisBuilder create() {
        return new NumberAxisBuilder();
    }

    public void applyTo(NumberAxis numberAxis) {
        super.applyTo(numberAxis);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            numberAxis.setForceZeroInRange(this.forceZeroInRange);
        }
        if ((n2 & 2) != 0) {
            numberAxis.setTickUnit(this.tickUnit);
        }
    }

    public NumberAxisBuilder forceZeroInRange(boolean bl) {
        this.forceZeroInRange = bl;
        this.__set |= 1;
        return this;
    }

    public NumberAxisBuilder tickUnit(double d2) {
        this.tickUnit = d2;
        this.__set |= 2;
        return this;
    }

    @Override
    public NumberAxis build() {
        NumberAxis numberAxis = new NumberAxis();
        this.applyTo(numberAxis);
        return numberAxis;
    }
}

