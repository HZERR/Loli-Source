/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.charts.ChartLayoutAnimator;
import com.sun.javafx.css.converters.SizeConverter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Side;
import javafx.scene.chart.ValueAxis;
import javafx.util.Duration;
import javafx.util.StringConverter;

public final class NumberAxis
extends ValueAxis<Number> {
    private Object currentAnimationID;
    private final ChartLayoutAnimator animator = new ChartLayoutAnimator(this);
    private final StringProperty currentFormatterProperty = new SimpleStringProperty(this, "currentFormatter", "");
    private final DefaultFormatter defaultFormatter = new DefaultFormatter(this);
    private BooleanProperty forceZeroInRange = new BooleanPropertyBase(true){

        @Override
        protected void invalidated() {
            if (NumberAxis.this.isAutoRanging()) {
                NumberAxis.this.requestAxisLayout();
                NumberAxis.this.invalidateRange();
            }
        }

        @Override
        public Object getBean() {
            return NumberAxis.this;
        }

        @Override
        public String getName() {
            return "forceZeroInRange";
        }
    };
    private DoubleProperty tickUnit = new StyleableDoubleProperty(5.0){

        @Override
        protected void invalidated() {
            if (!NumberAxis.this.isAutoRanging()) {
                NumberAxis.this.invalidateRange();
                NumberAxis.this.requestAxisLayout();
            }
        }

        @Override
        public CssMetaData<NumberAxis, Number> getCssMetaData() {
            return StyleableProperties.TICK_UNIT;
        }

        @Override
        public Object getBean() {
            return NumberAxis.this;
        }

        @Override
        public String getName() {
            return "tickUnit";
        }
    };

    public final boolean isForceZeroInRange() {
        return this.forceZeroInRange.getValue();
    }

    public final void setForceZeroInRange(boolean bl) {
        this.forceZeroInRange.setValue(bl);
    }

    public final BooleanProperty forceZeroInRangeProperty() {
        return this.forceZeroInRange;
    }

    public final double getTickUnit() {
        return this.tickUnit.get();
    }

    public final void setTickUnit(double d2) {
        this.tickUnit.set(d2);
    }

    public final DoubleProperty tickUnitProperty() {
        return this.tickUnit;
    }

    public NumberAxis() {
    }

    public NumberAxis(double d2, double d3, double d4) {
        super(d2, d3);
        this.setTickUnit(d4);
    }

    public NumberAxis(String string, double d2, double d3, double d4) {
        super(d2, d3);
        this.setTickUnit(d4);
        this.setLabel(string);
    }

    @Override
    protected String getTickMarkLabel(Number number) {
        DefaultFormatter defaultFormatter = this.getTickLabelFormatter();
        if (defaultFormatter == null) {
            defaultFormatter = this.defaultFormatter;
        }
        return ((StringConverter)defaultFormatter).toString(number);
    }

    @Override
    protected Object getRange() {
        return new Object[]{this.getLowerBound(), this.getUpperBound(), this.getTickUnit(), this.getScale(), this.currentFormatterProperty.get()};
    }

    @Override
    protected void setRange(Object object, boolean bl) {
        Object[] arrobject = (Object[])object;
        double d2 = (Double)arrobject[0];
        double d3 = (Double)arrobject[1];
        double d4 = (Double)arrobject[2];
        double d5 = (Double)arrobject[3];
        String string = (String)arrobject[4];
        this.currentFormatterProperty.set(string);
        double d6 = this.getLowerBound();
        this.setLowerBound(d2);
        this.setUpperBound(d3);
        this.setTickUnit(d4);
        if (bl) {
            this.animator.stop(this.currentAnimationID);
            this.currentAnimationID = this.animator.animate(new KeyFrame(Duration.ZERO, new KeyValue(this.currentLowerBound, d6), new KeyValue(this.scalePropertyImpl(), this.getScale())), new KeyFrame(Duration.millis(700.0), new KeyValue(this.currentLowerBound, d2), new KeyValue(this.scalePropertyImpl(), d5)));
        } else {
            this.currentLowerBound.set(d2);
            this.setScale(d5);
        }
    }

    @Override
    protected List<Number> calculateTickValues(double d2, Object object) {
        Object[] arrobject = (Object[])object;
        double d3 = (Double)arrobject[0];
        double d4 = (Double)arrobject[1];
        double d5 = (Double)arrobject[2];
        ArrayList<Number> arrayList = new ArrayList<Number>();
        if (d3 == d4) {
            arrayList.add(d3);
        } else if (d5 <= 0.0) {
            arrayList.add(d3);
            arrayList.add(d4);
        } else if (d5 > 0.0) {
            arrayList.add(d3);
            if ((d4 - d3) / d5 > 2000.0) {
                System.err.println("Warning we tried to create more than 2000 major tick marks on a NumberAxis. Lower Bound=" + d3 + ", Upper Bound=" + d4 + ", Tick Unit=" + d5);
            } else if (d3 + d5 < d4) {
                double d6 = Math.rint(d5) == d5 ? Math.ceil(d3) : d3 + d5;
                int n2 = (int)Math.ceil((d4 - d6) / d5);
                for (int i2 = 0; d6 < d4 && i2 < n2; d6 += d5, ++i2) {
                    if (arrayList.contains(d6)) continue;
                    arrayList.add(d6);
                }
            }
            arrayList.add(d4);
        }
        return arrayList;
    }

    @Override
    protected List<Number> calculateMinorTickMarks() {
        ArrayList<Number> arrayList = new ArrayList<Number>();
        double d2 = this.getLowerBound();
        double d3 = this.getUpperBound();
        double d4 = this.getTickUnit();
        double d5 = d4 / (double)Math.max(1, this.getMinorTickCount());
        if (d4 > 0.0) {
            int n2;
            int n3;
            double d6;
            boolean bl;
            if ((d3 - d2) / d5 > 10000.0) {
                System.err.println("Warning we tried to create more than 10000 minor tick marks on a NumberAxis. Lower Bound=" + this.getLowerBound() + ", Upper Bound=" + this.getUpperBound() + ", Tick Unit=" + d4);
                return arrayList;
            }
            boolean bl2 = bl = Math.rint(d4) == d4;
            if (bl) {
                d6 = Math.floor(d2) + d5;
                n3 = (int)Math.ceil((Math.ceil(d2) - d6) / d5);
                for (n2 = 0; d6 < Math.ceil(d2) && n2 < n3; d6 += d5, ++n2) {
                    if (!(d6 > d2)) continue;
                    arrayList.add(d6);
                }
            }
            d6 = bl ? Math.ceil(d2) : d2;
            n3 = (int)Math.ceil((d3 - d6) / d4);
            for (n2 = 0; d6 < d3 && n2 < n3; d6 += d4, ++n2) {
                double d7 = Math.min(d6 + d4, d3);
                double d8 = d6 + d5;
                int n4 = (int)Math.ceil((d7 - d8) / d5);
                for (int i2 = 0; d8 < d7 && i2 < n4; d8 += d5, ++i2) {
                    arrayList.add(d8);
                }
            }
        }
        return arrayList;
    }

    @Override
    protected Dimension2D measureTickMarkSize(Number number, Object object) {
        Object[] arrobject = (Object[])object;
        String string = (String)arrobject[4];
        return this.measureTickMarkSize(number, this.getTickLabelRotation(), string);
    }

    private Dimension2D measureTickMarkSize(Number number, double d2, String string) {
        DefaultFormatter defaultFormatter = this.getTickLabelFormatter();
        if (defaultFormatter == null) {
            defaultFormatter = this.defaultFormatter;
        }
        String string2 = defaultFormatter instanceof DefaultFormatter ? defaultFormatter.toString(number, string) : ((StringConverter)defaultFormatter).toString(number);
        return this.measureTickMarkLabelSize(string2, d2);
    }

    @Override
    protected Object autoRange(double d2, double d3, double d4, double d5) {
        Side side = this.getEffectiveSide();
        if (this.isForceZeroInRange()) {
            if (d3 < 0.0) {
                d3 = 0.0;
            } else if (d2 > 0.0) {
                d2 = 0.0;
            }
        }
        int n2 = (int)Math.floor(d4 / d5);
        n2 = Math.max(n2, 2);
        int n3 = Math.max(this.getMinorTickCount(), 1);
        double d6 = d3 - d2;
        if (d6 != 0.0 && d6 / (double)(n2 * n3) <= Math.ulp(d2)) {
            d6 = 0.0;
        }
        double d7 = d6 == 0.0 ? (d2 == 0.0 ? 2.0 : Math.abs(d2) * 0.02) : Math.abs(d6) * 1.02;
        double d8 = (d7 - d6) / 2.0;
        double d9 = d2 - d8;
        double d10 = d3 + d8;
        if (d9 < 0.0 && d2 >= 0.0 || d9 > 0.0 && d2 <= 0.0) {
            d9 = 0.0;
        }
        if (d10 < 0.0 && d3 >= 0.0 || d10 > 0.0 && d3 <= 0.0) {
            d10 = 0.0;
        }
        double d11 = d7 / (double)n2;
        double d12 = 0.0;
        double d13 = 0.0;
        double d14 = 0.0;
        int n4 = 0;
        double d15 = Double.MAX_VALUE;
        String string = "0.00000000";
        while (d15 > d4 || n4 > 20) {
            double d16;
            int n5 = (int)Math.floor(Math.log10(d11));
            double d17 = d16 = d11 / Math.pow(10.0, n5);
            if (d16 > 5.0) {
                ++n5;
                d17 = 1.0;
            } else if (d16 > 1.0) {
                double d18 = d17 = d16 > 2.5 ? 5.0 : 2.5;
            }
            if (n5 > 1) {
                string = "#,##0";
            } else if (n5 == 1) {
                string = "0";
            } else {
                int n6;
                boolean bl = Math.rint(d17) != d17;
                StringBuilder stringBuilder = new StringBuilder("0");
                int n7 = n6 = bl ? Math.abs(n5) + 1 : Math.abs(n5);
                if (n6 > 0) {
                    stringBuilder.append(".");
                }
                for (int i2 = 0; i2 < n6; ++i2) {
                    stringBuilder.append("0");
                }
                string = stringBuilder.toString();
            }
            d12 = d17 * Math.pow(10.0, n5);
            d13 = Math.floor(d9 / d12) * d12;
            d14 = Math.ceil(d10 / d12) * d12;
            double d19 = 0.0;
            double d20 = 0.0;
            n4 = (int)Math.ceil((d14 - d13) / d12);
            double d21 = d13;
            for (int i3 = 0; d21 <= d14 && i3 < n4; d21 += d12, ++i3) {
                double d22;
                Dimension2D dimension2D = this.measureTickMarkSize(d21, this.getTickLabelRotation(), string);
                double d23 = d22 = side.isVertical() ? dimension2D.getHeight() : dimension2D.getWidth();
                if (i3 == 0) {
                    d20 = d22 / 2.0;
                    continue;
                }
                d19 = Math.max(d19, d20 + 6.0 + d22 / 2.0);
            }
            d15 = (double)(n4 - 1) * d19;
            d11 = d12;
            if (n2 == 2 && d15 > d4) break;
            if (!(d15 > d4) && n4 <= 20) continue;
            d11 *= 2.0;
        }
        double d24 = this.calculateNewScale(d4, d13, d14);
        return new Object[]{d13, d14, d12, d24, string};
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return NumberAxis.getClassCssMetaData();
    }

    public static class DefaultFormatter
    extends StringConverter<Number> {
        private DecimalFormat formatter;
        private String prefix = null;
        private String suffix = null;

        public DefaultFormatter(NumberAxis numberAxis) {
            this.formatter = numberAxis.isAutoRanging() ? new DecimalFormat((String)numberAxis.currentFormatterProperty.get()) : new DecimalFormat();
            ChangeListener<Object> changeListener = (observableValue, object, object2) -> {
                this.formatter = numberAxis.isAutoRanging() ? new DecimalFormat((String)numberAxis.currentFormatterProperty.get()) : new DecimalFormat();
            };
            numberAxis.currentFormatterProperty.addListener(changeListener);
            numberAxis.autoRangingProperty().addListener(changeListener);
        }

        public DefaultFormatter(NumberAxis numberAxis, String string, String string2) {
            this(numberAxis);
            this.prefix = string;
            this.suffix = string2;
        }

        @Override
        public String toString(Number number) {
            return this.toString(number, this.formatter);
        }

        private String toString(Number number, String string) {
            if (string == null || string.isEmpty()) {
                return this.toString(number, this.formatter);
            }
            return this.toString(number, new DecimalFormat(string));
        }

        private String toString(Number number, DecimalFormat decimalFormat) {
            if (this.prefix != null && this.suffix != null) {
                return this.prefix + decimalFormat.format(number) + this.suffix;
            }
            if (this.prefix != null) {
                return this.prefix + decimalFormat.format(number);
            }
            if (this.suffix != null) {
                return decimalFormat.format(number) + this.suffix;
            }
            return decimalFormat.format(number);
        }

        @Override
        public Number fromString(String string) {
            try {
                int n2 = this.prefix == null ? 0 : this.prefix.length();
                int n3 = this.suffix == null ? 0 : this.suffix.length();
                return this.formatter.parse(string.substring(n2, string.length() - n3));
            }
            catch (ParseException parseException) {
                return null;
            }
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<NumberAxis, Number> TICK_UNIT = new CssMetaData<NumberAxis, Number>("-fx-tick-unit", SizeConverter.getInstance(), (Number)5.0){

            @Override
            public boolean isSettable(NumberAxis numberAxis) {
                return numberAxis.tickUnit == null || !numberAxis.tickUnit.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(NumberAxis numberAxis) {
                return (StyleableProperty)((Object)numberAxis.tickUnitProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(ValueAxis.getClassCssMetaData());
            arrayList.add(TICK_UNIT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

