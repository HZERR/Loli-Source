/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.geometry.Orientation;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.Slider;
import javafx.util.Builder;
import javafx.util.StringConverter;

@Deprecated
public class SliderBuilder<B extends SliderBuilder<B>>
extends ControlBuilder<B>
implements Builder<Slider> {
    private int __set;
    private double blockIncrement;
    private StringConverter<Double> labelFormatter;
    private double majorTickUnit;
    private double max;
    private double min;
    private int minorTickCount;
    private Orientation orientation;
    private boolean showTickLabels;
    private boolean showTickMarks;
    private boolean snapToTicks;
    private double value;
    private boolean valueChanging;

    protected SliderBuilder() {
    }

    public static SliderBuilder<?> create() {
        return new SliderBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Slider slider) {
        super.applyTo(slider);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    slider.setBlockIncrement(this.blockIncrement);
                    break;
                }
                case 1: {
                    slider.setLabelFormatter(this.labelFormatter);
                    break;
                }
                case 2: {
                    slider.setMajorTickUnit(this.majorTickUnit);
                    break;
                }
                case 3: {
                    slider.setMax(this.max);
                    break;
                }
                case 4: {
                    slider.setMin(this.min);
                    break;
                }
                case 5: {
                    slider.setMinorTickCount(this.minorTickCount);
                    break;
                }
                case 6: {
                    slider.setOrientation(this.orientation);
                    break;
                }
                case 7: {
                    slider.setShowTickLabels(this.showTickLabels);
                    break;
                }
                case 8: {
                    slider.setShowTickMarks(this.showTickMarks);
                    break;
                }
                case 9: {
                    slider.setSnapToTicks(this.snapToTicks);
                    break;
                }
                case 10: {
                    slider.setValue(this.value);
                    break;
                }
                case 11: {
                    slider.setValueChanging(this.valueChanging);
                }
            }
        }
    }

    public B blockIncrement(double d2) {
        this.blockIncrement = d2;
        this.__set(0);
        return (B)this;
    }

    public B labelFormatter(StringConverter<Double> stringConverter) {
        this.labelFormatter = stringConverter;
        this.__set(1);
        return (B)this;
    }

    public B majorTickUnit(double d2) {
        this.majorTickUnit = d2;
        this.__set(2);
        return (B)this;
    }

    public B max(double d2) {
        this.max = d2;
        this.__set(3);
        return (B)this;
    }

    public B min(double d2) {
        this.min = d2;
        this.__set(4);
        return (B)this;
    }

    public B minorTickCount(int n2) {
        this.minorTickCount = n2;
        this.__set(5);
        return (B)this;
    }

    public B orientation(Orientation orientation) {
        this.orientation = orientation;
        this.__set(6);
        return (B)this;
    }

    public B showTickLabels(boolean bl) {
        this.showTickLabels = bl;
        this.__set(7);
        return (B)this;
    }

    public B showTickMarks(boolean bl) {
        this.showTickMarks = bl;
        this.__set(8);
        return (B)this;
    }

    public B snapToTicks(boolean bl) {
        this.snapToTicks = bl;
        this.__set(9);
        return (B)this;
    }

    public B value(double d2) {
        this.value = d2;
        this.__set(10);
        return (B)this;
    }

    public B valueChanging(boolean bl) {
        this.valueChanging = bl;
        this.__set(11);
        return (B)this;
    }

    @Override
    public Slider build() {
        Slider slider = new Slider();
        this.applyTo(slider);
        return slider;
    }
}

