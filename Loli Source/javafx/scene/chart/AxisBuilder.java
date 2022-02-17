/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Side;
import javafx.scene.chart.Axis;
import javafx.scene.layout.RegionBuilder;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

@Deprecated
public abstract class AxisBuilder<T, B extends AxisBuilder<T, B>>
extends RegionBuilder<B> {
    private int __set;
    private boolean animated;
    private boolean autoRanging;
    private String label;
    private Side side;
    private Paint tickLabelFill;
    private Font tickLabelFont;
    private double tickLabelGap;
    private double tickLabelRotation;
    private boolean tickLabelsVisible;
    private double tickLength;
    private Collection<? extends Axis.TickMark<T>> tickMarks;
    private boolean tickMarkVisible;

    protected AxisBuilder() {
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Axis<T> axis) {
        super.applyTo(axis);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    axis.setAnimated(this.animated);
                    break;
                }
                case 1: {
                    axis.setAutoRanging(this.autoRanging);
                    break;
                }
                case 2: {
                    axis.setLabel(this.label);
                    break;
                }
                case 3: {
                    axis.setSide(this.side);
                    break;
                }
                case 4: {
                    axis.setTickLabelFill(this.tickLabelFill);
                    break;
                }
                case 5: {
                    axis.setTickLabelFont(this.tickLabelFont);
                    break;
                }
                case 6: {
                    axis.setTickLabelGap(this.tickLabelGap);
                    break;
                }
                case 7: {
                    axis.setTickLabelRotation(this.tickLabelRotation);
                    break;
                }
                case 8: {
                    axis.setTickLabelsVisible(this.tickLabelsVisible);
                    break;
                }
                case 9: {
                    axis.setTickLength(this.tickLength);
                    break;
                }
                case 10: {
                    axis.getTickMarks().addAll(this.tickMarks);
                    break;
                }
                case 11: {
                    axis.setTickMarkVisible(this.tickMarkVisible);
                }
            }
        }
    }

    public B animated(boolean bl) {
        this.animated = bl;
        this.__set(0);
        return (B)this;
    }

    public B autoRanging(boolean bl) {
        this.autoRanging = bl;
        this.__set(1);
        return (B)this;
    }

    public B label(String string) {
        this.label = string;
        this.__set(2);
        return (B)this;
    }

    public B side(Side side) {
        this.side = side;
        this.__set(3);
        return (B)this;
    }

    public B tickLabelFill(Paint paint) {
        this.tickLabelFill = paint;
        this.__set(4);
        return (B)this;
    }

    public B tickLabelFont(Font font) {
        this.tickLabelFont = font;
        this.__set(5);
        return (B)this;
    }

    public B tickLabelGap(double d2) {
        this.tickLabelGap = d2;
        this.__set(6);
        return (B)this;
    }

    public B tickLabelRotation(double d2) {
        this.tickLabelRotation = d2;
        this.__set(7);
        return (B)this;
    }

    public B tickLabelsVisible(boolean bl) {
        this.tickLabelsVisible = bl;
        this.__set(8);
        return (B)this;
    }

    public B tickLength(double d2) {
        this.tickLength = d2;
        this.__set(9);
        return (B)this;
    }

    public B tickMarks(Collection<? extends Axis.TickMark<T>> collection) {
        this.tickMarks = collection;
        this.__set(10);
        return (B)this;
    }

    public B tickMarks(Axis.TickMark<T> ... arrtickMark) {
        return this.tickMarks(Arrays.asList(arrtickMark));
    }

    public B tickMarkVisible(boolean bl) {
        this.tickMarkVisible = bl;
        this.__set(11);
        return (B)this;
    }
}

