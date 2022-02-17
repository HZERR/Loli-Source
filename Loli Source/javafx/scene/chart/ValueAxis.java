/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Side;
import javafx.scene.chart.Axis;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.StringConverter;

public abstract class ValueAxis<T extends Number>
extends Axis<T> {
    private final Path minorTickPath = new Path();
    private double offset;
    double dataMinValue;
    double dataMaxValue;
    private List<T> minorTickMarkValues = null;
    protected final DoubleProperty currentLowerBound = new SimpleDoubleProperty(this, "currentLowerBound");
    private BooleanProperty minorTickVisible = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            ValueAxis.this.minorTickPath.setVisible(this.get());
            ValueAxis.this.requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return ValueAxis.this;
        }

        @Override
        public String getName() {
            return "minorTickVisible";
        }

        @Override
        public CssMetaData<ValueAxis<? extends Number>, Boolean> getCssMetaData() {
            return StyleableProperties.MINOR_TICK_VISIBLE;
        }
    };
    private ReadOnlyDoubleWrapper scale = new ReadOnlyDoubleWrapper(this, "scale", 0.0){

        @Override
        protected void invalidated() {
            ValueAxis.this.requestAxisLayout();
            ValueAxis.this.measureInvalid = true;
        }
    };
    private DoubleProperty upperBound = new DoublePropertyBase(100.0){

        @Override
        protected void invalidated() {
            if (!ValueAxis.this.isAutoRanging()) {
                ValueAxis.this.invalidateRange();
                ValueAxis.this.requestAxisLayout();
            }
        }

        @Override
        public Object getBean() {
            return ValueAxis.this;
        }

        @Override
        public String getName() {
            return "upperBound";
        }
    };
    private DoubleProperty lowerBound = new DoublePropertyBase(0.0){

        @Override
        protected void invalidated() {
            if (!ValueAxis.this.isAutoRanging()) {
                ValueAxis.this.invalidateRange();
                ValueAxis.this.requestAxisLayout();
            }
        }

        @Override
        public Object getBean() {
            return ValueAxis.this;
        }

        @Override
        public String getName() {
            return "lowerBound";
        }
    };
    private final ObjectProperty<StringConverter<T>> tickLabelFormatter = new ObjectPropertyBase<StringConverter<T>>(null){

        @Override
        protected void invalidated() {
            ValueAxis.this.invalidateRange();
            ValueAxis.this.requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return ValueAxis.this;
        }

        @Override
        public String getName() {
            return "tickLabelFormatter";
        }
    };
    private DoubleProperty minorTickLength = new StyleableDoubleProperty(5.0){

        @Override
        protected void invalidated() {
            ValueAxis.this.requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return ValueAxis.this;
        }

        @Override
        public String getName() {
            return "minorTickLength";
        }

        @Override
        public CssMetaData<ValueAxis<? extends Number>, Number> getCssMetaData() {
            return StyleableProperties.MINOR_TICK_LENGTH;
        }
    };
    private IntegerProperty minorTickCount = new StyleableIntegerProperty(5){

        @Override
        protected void invalidated() {
            ValueAxis.this.invalidateRange();
            ValueAxis.this.requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return ValueAxis.this;
        }

        @Override
        public String getName() {
            return "minorTickCount";
        }

        @Override
        public CssMetaData<ValueAxis<? extends Number>, Number> getCssMetaData() {
            return StyleableProperties.MINOR_TICK_COUNT;
        }
    };

    public final boolean isMinorTickVisible() {
        return this.minorTickVisible.get();
    }

    public final void setMinorTickVisible(boolean bl) {
        this.minorTickVisible.set(bl);
    }

    public final BooleanProperty minorTickVisibleProperty() {
        return this.minorTickVisible;
    }

    public final double getScale() {
        return this.scale.get();
    }

    protected final void setScale(double d2) {
        this.scale.set(d2);
    }

    public final ReadOnlyDoubleProperty scaleProperty() {
        return this.scale.getReadOnlyProperty();
    }

    ReadOnlyDoubleWrapper scalePropertyImpl() {
        return this.scale;
    }

    public final double getUpperBound() {
        return this.upperBound.get();
    }

    public final void setUpperBound(double d2) {
        this.upperBound.set(d2);
    }

    public final DoubleProperty upperBoundProperty() {
        return this.upperBound;
    }

    public final double getLowerBound() {
        return this.lowerBound.get();
    }

    public final void setLowerBound(double d2) {
        this.lowerBound.set(d2);
    }

    public final DoubleProperty lowerBoundProperty() {
        return this.lowerBound;
    }

    public final StringConverter<T> getTickLabelFormatter() {
        return (StringConverter)this.tickLabelFormatter.getValue();
    }

    public final void setTickLabelFormatter(StringConverter<T> stringConverter) {
        this.tickLabelFormatter.setValue(stringConverter);
    }

    public final ObjectProperty<StringConverter<T>> tickLabelFormatterProperty() {
        return this.tickLabelFormatter;
    }

    public final double getMinorTickLength() {
        return this.minorTickLength.get();
    }

    public final void setMinorTickLength(double d2) {
        this.minorTickLength.set(d2);
    }

    public final DoubleProperty minorTickLengthProperty() {
        return this.minorTickLength;
    }

    public final int getMinorTickCount() {
        return this.minorTickCount.get();
    }

    public final void setMinorTickCount(int n2) {
        this.minorTickCount.set(n2);
    }

    public final IntegerProperty minorTickCountProperty() {
        return this.minorTickCount;
    }

    public ValueAxis() {
        this.minorTickPath.getStyleClass().add("axis-minor-tick-mark");
        this.getChildren().add(this.minorTickPath);
    }

    public ValueAxis(double d2, double d3) {
        this();
        this.setAutoRanging(false);
        this.setLowerBound(d2);
        this.setUpperBound(d3);
    }

    @Override
    protected final Object autoRange(double d2) {
        if (this.isAutoRanging()) {
            double d3 = this.getTickLabelFont().getSize() * 2.0;
            return this.autoRange(this.dataMinValue, this.dataMaxValue, d2, d3);
        }
        return this.getRange();
    }

    protected final double calculateNewScale(double d2, double d3, double d4) {
        double d5 = 1.0;
        Side side = this.getEffectiveSide();
        if (side.isVertical()) {
            this.offset = d2;
            d5 = d4 - d3 == 0.0 ? -d2 : -(d2 / (d4 - d3));
        } else {
            this.offset = 0.0;
            d5 = d4 - d3 == 0.0 ? d2 : d2 / (d4 - d3);
        }
        return d5;
    }

    protected Object autoRange(double d2, double d3, double d4, double d5) {
        return null;
    }

    protected abstract List<T> calculateMinorTickMarks();

    @Override
    protected void tickMarksUpdated() {
        super.tickMarksUpdated();
        this.minorTickMarkValues = this.calculateMinorTickMarks();
    }

    @Override
    protected void layoutChildren() {
        block11: {
            double d2;
            Side side = this.getEffectiveSide();
            double d3 = d2 = side.isVertical() ? this.getHeight() : this.getWidth();
            if (!this.isAutoRanging()) {
                this.setScale(this.calculateNewScale(d2, this.getLowerBound(), this.getUpperBound()));
                this.currentLowerBound.set(this.getLowerBound());
            }
            super.layoutChildren();
            this.minorTickPath.getElements().clear();
            double d4 = Math.max(0.0, this.getMinorTickLength());
            if (!(d4 > 0.0) || !(d2 > (double)(2 * this.getTickMarks().size()))) break block11;
            int n2 = (int)Math.ceil((double)(2 * this.minorTickMarkValues.size()) / (d2 - (double)(2 * this.getTickMarks().size())));
            if (Side.LEFT.equals((Object)side)) {
                this.minorTickPath.setLayoutX(-0.5);
                this.minorTickPath.setLayoutY(0.5);
                for (int i2 = 0; i2 < this.minorTickMarkValues.size(); i2 += n2) {
                    Number number = (Number)this.minorTickMarkValues.get(i2);
                    double d5 = this.getDisplayPosition((T)number);
                    if (!(d5 >= 0.0) || !(d5 <= d2)) continue;
                    this.minorTickPath.getElements().addAll(new MoveTo(this.getWidth() - d4, d5), new LineTo(this.getWidth() - 1.0, d5));
                }
            } else if (Side.RIGHT.equals((Object)side)) {
                this.minorTickPath.setLayoutX(0.5);
                this.minorTickPath.setLayoutY(0.5);
                for (int i3 = 0; i3 < this.minorTickMarkValues.size(); i3 += n2) {
                    Number number = (Number)this.minorTickMarkValues.get(i3);
                    double d6 = this.getDisplayPosition((T)number);
                    if (!(d6 >= 0.0) || !(d6 <= d2)) continue;
                    this.minorTickPath.getElements().addAll(new MoveTo(1.0, d6), new LineTo(d4, d6));
                }
            } else if (Side.TOP.equals((Object)side)) {
                this.minorTickPath.setLayoutX(0.5);
                this.minorTickPath.setLayoutY(-0.5);
                for (int i4 = 0; i4 < this.minorTickMarkValues.size(); i4 += n2) {
                    Number number = (Number)this.minorTickMarkValues.get(i4);
                    double d7 = this.getDisplayPosition((T)number);
                    if (!(d7 >= 0.0) || !(d7 <= d2)) continue;
                    this.minorTickPath.getElements().addAll(new MoveTo(d7, this.getHeight() - 1.0), new LineTo(d7, this.getHeight() - d4));
                }
            } else {
                this.minorTickPath.setLayoutX(0.5);
                this.minorTickPath.setLayoutY(0.5);
                for (int i5 = 0; i5 < this.minorTickMarkValues.size(); i5 += n2) {
                    Number number = (Number)this.minorTickMarkValues.get(i5);
                    double d8 = this.getDisplayPosition((T)number);
                    if (!(d8 >= 0.0) || !(d8 <= d2)) continue;
                    this.minorTickPath.getElements().addAll(new MoveTo(d8, 1.0), new LineTo(d8, d4));
                }
            }
        }
    }

    @Override
    public void invalidateRange(List<T> list) {
        if (list.isEmpty()) {
            this.dataMaxValue = this.getUpperBound();
            this.dataMinValue = this.getLowerBound();
        } else {
            this.dataMinValue = Double.MAX_VALUE;
            this.dataMaxValue = -1.7976931348623157E308;
        }
        for (Number number : list) {
            this.dataMinValue = Math.min(this.dataMinValue, number.doubleValue());
            this.dataMaxValue = Math.max(this.dataMaxValue, number.doubleValue());
        }
        super.invalidateRange(list);
    }

    @Override
    public double getDisplayPosition(T t2) {
        return this.offset + (((Number)t2).doubleValue() - this.currentLowerBound.get()) * this.getScale();
    }

    @Override
    public T getValueForDisplay(double d2) {
        return (T)this.toRealValue((d2 - this.offset) / this.getScale() + this.currentLowerBound.get());
    }

    @Override
    public double getZeroPosition() {
        if (0.0 < this.getLowerBound() || 0.0 > this.getUpperBound()) {
            return Double.NaN;
        }
        return this.getDisplayPosition(0.0);
    }

    @Override
    public boolean isValueOnAxis(T t2) {
        double d2 = ((Number)t2).doubleValue();
        return d2 >= this.getLowerBound() && d2 <= this.getUpperBound();
    }

    @Override
    public double toNumericValue(T t2) {
        return t2 == null ? Double.NaN : ((Number)t2).doubleValue();
    }

    @Override
    public T toRealValue(double d2) {
        return (T)new Double(d2);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return ValueAxis.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<ValueAxis<? extends Number>, Number> MINOR_TICK_LENGTH = new CssMetaData<ValueAxis<? extends Number>, Number>("-fx-minor-tick-length", SizeConverter.getInstance(), (Number)5.0){

            @Override
            public boolean isSettable(ValueAxis<? extends Number> valueAxis) {
                return ((ValueAxis)valueAxis).minorTickLength == null || !((ValueAxis)valueAxis).minorTickLength.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ValueAxis<? extends Number> valueAxis) {
                return (StyleableProperty)((Object)valueAxis.minorTickLengthProperty());
            }
        };
        private static final CssMetaData<ValueAxis<? extends Number>, Number> MINOR_TICK_COUNT = new CssMetaData<ValueAxis<? extends Number>, Number>("-fx-minor-tick-count", SizeConverter.getInstance(), (Number)5){

            @Override
            public boolean isSettable(ValueAxis<? extends Number> valueAxis) {
                return ((ValueAxis)valueAxis).minorTickCount == null || !((ValueAxis)valueAxis).minorTickCount.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ValueAxis<? extends Number> valueAxis) {
                return (StyleableProperty)((Object)valueAxis.minorTickCountProperty());
            }
        };
        private static final CssMetaData<ValueAxis<? extends Number>, Boolean> MINOR_TICK_VISIBLE = new CssMetaData<ValueAxis<? extends Number>, Boolean>("-fx-minor-tick-visible", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(ValueAxis<? extends Number> valueAxis) {
                return ((ValueAxis)valueAxis).minorTickVisible == null || !((ValueAxis)valueAxis).minorTickVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(ValueAxis<? extends Number> valueAxis) {
                return (StyleableProperty)((Object)valueAxis.minorTickVisibleProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Axis.getClassCssMetaData());
            arrayList.add(MINOR_TICK_COUNT);
            arrayList.add(MINOR_TICK_LENGTH);
            arrayList.add(MINOR_TICK_COUNT);
            arrayList.add(MINOR_TICK_VISIBLE);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

