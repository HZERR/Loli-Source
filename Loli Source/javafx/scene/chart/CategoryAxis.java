/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.charts.ChartLayoutAnimator;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Side;
import javafx.scene.chart.Axis;
import javafx.util.Duration;

public final class CategoryAxis
extends Axis<String> {
    private List<String> allDataCategories = new ArrayList<String>();
    private boolean changeIsLocal = false;
    private final DoubleProperty firstCategoryPos = new SimpleDoubleProperty(this, "firstCategoryPos", 0.0);
    private Object currentAnimationID;
    private final ChartLayoutAnimator animator = new ChartLayoutAnimator(this);
    private ListChangeListener<String> itemsListener = change -> {
        while (change.next()) {
            if (!change.getAddedSubList().isEmpty()) {
                for (String string : change.getAddedSubList()) {
                    this.checkAndRemoveDuplicates(string);
                }
            }
            if (!this.isAutoRanging()) {
                this.allDataCategories.clear();
                this.allDataCategories.addAll(this.getCategories());
                this.rangeValid = false;
            }
            this.requestAxisLayout();
        }
    };
    private DoubleProperty startMargin = new StyleableDoubleProperty(5.0){

        @Override
        protected void invalidated() {
            CategoryAxis.this.requestAxisLayout();
        }

        @Override
        public CssMetaData<CategoryAxis, Number> getCssMetaData() {
            return StyleableProperties.START_MARGIN;
        }

        @Override
        public Object getBean() {
            return CategoryAxis.this;
        }

        @Override
        public String getName() {
            return "startMargin";
        }
    };
    private DoubleProperty endMargin = new StyleableDoubleProperty(5.0){

        @Override
        protected void invalidated() {
            CategoryAxis.this.requestAxisLayout();
        }

        @Override
        public CssMetaData<CategoryAxis, Number> getCssMetaData() {
            return StyleableProperties.END_MARGIN;
        }

        @Override
        public Object getBean() {
            return CategoryAxis.this;
        }

        @Override
        public String getName() {
            return "endMargin";
        }
    };
    private BooleanProperty gapStartAndEnd = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            CategoryAxis.this.requestAxisLayout();
        }

        @Override
        public CssMetaData<CategoryAxis, Boolean> getCssMetaData() {
            return StyleableProperties.GAP_START_AND_END;
        }

        @Override
        public Object getBean() {
            return CategoryAxis.this;
        }

        @Override
        public String getName() {
            return "gapStartAndEnd";
        }
    };
    private ObjectProperty<ObservableList<String>> categories = new ObjectPropertyBase<ObservableList<String>>(){
        ObservableList<String> old;

        @Override
        protected void invalidated() {
            if (CategoryAxis.this.getDuplicate() != null) {
                throw new IllegalArgumentException("Duplicate category added; " + CategoryAxis.this.getDuplicate() + " already present");
            }
            ObservableList observableList = (ObservableList)this.get();
            if (this.old != observableList) {
                if (this.old != null) {
                    this.old.removeListener(CategoryAxis.this.itemsListener);
                }
                if (observableList != null) {
                    observableList.addListener(CategoryAxis.this.itemsListener);
                }
                this.old = observableList;
            }
        }

        @Override
        public Object getBean() {
            return CategoryAxis.this;
        }

        @Override
        public String getName() {
            return "categories";
        }
    };
    private final ReadOnlyDoubleWrapper categorySpacing = new ReadOnlyDoubleWrapper(this, "categorySpacing", 1.0);

    public final double getStartMargin() {
        return this.startMargin.getValue();
    }

    public final void setStartMargin(double d2) {
        this.startMargin.setValue(d2);
    }

    public final DoubleProperty startMarginProperty() {
        return this.startMargin;
    }

    public final double getEndMargin() {
        return this.endMargin.getValue();
    }

    public final void setEndMargin(double d2) {
        this.endMargin.setValue(d2);
    }

    public final DoubleProperty endMarginProperty() {
        return this.endMargin;
    }

    public final boolean isGapStartAndEnd() {
        return this.gapStartAndEnd.getValue();
    }

    public final void setGapStartAndEnd(boolean bl) {
        this.gapStartAndEnd.setValue(bl);
    }

    public final BooleanProperty gapStartAndEndProperty() {
        return this.gapStartAndEnd;
    }

    public final void setCategories(ObservableList<String> observableList) {
        this.categories.set(observableList);
        if (!this.changeIsLocal) {
            this.setAutoRanging(false);
            this.allDataCategories.clear();
            this.allDataCategories.addAll(this.getCategories());
        }
        this.requestAxisLayout();
    }

    private void checkAndRemoveDuplicates(String string) {
        if (this.getDuplicate() != null) {
            this.getCategories().remove(string);
            throw new IllegalArgumentException("Duplicate category ; " + string + " already present");
        }
    }

    private String getDuplicate() {
        if (this.getCategories() != null) {
            for (int i2 = 0; i2 < this.getCategories().size(); ++i2) {
                for (int i3 = 0; i3 < this.getCategories().size(); ++i3) {
                    if (!((String)this.getCategories().get(i2)).equals(this.getCategories().get(i3)) || i2 == i3) continue;
                    return (String)this.getCategories().get(i2);
                }
            }
        }
        return null;
    }

    public final ObservableList<String> getCategories() {
        return (ObservableList)this.categories.get();
    }

    public final double getCategorySpacing() {
        return this.categorySpacing.get();
    }

    public final ReadOnlyDoubleProperty categorySpacingProperty() {
        return this.categorySpacing.getReadOnlyProperty();
    }

    public CategoryAxis() {
        this.changeIsLocal = true;
        this.setCategories(FXCollections.observableArrayList());
        this.changeIsLocal = false;
    }

    public CategoryAxis(ObservableList<String> observableList) {
        this.setCategories(observableList);
    }

    private double calculateNewSpacing(double d2, List<String> list) {
        Side side = this.getEffectiveSide();
        double d3 = 1.0;
        if (list != null) {
            double d4 = this.isGapStartAndEnd() ? list.size() : list.size() - 1;
            double d5 = d3 = d4 == 0.0 ? 1.0 : (d2 - this.getStartMargin() - this.getEndMargin()) / d4;
        }
        if (!this.isAutoRanging()) {
            this.categorySpacing.set(d3);
        }
        return d3;
    }

    private double calculateNewFirstPos(double d2, double d3) {
        Side side = this.getEffectiveSide();
        double d4 = 1.0;
        double d5 = this.isGapStartAndEnd() ? d3 / 2.0 : 0.0;
        d4 = side.isHorizontal() ? 0.0 + this.getStartMargin() + d5 : d2 - this.getStartMargin() - d5;
        if (!this.isAutoRanging()) {
            this.firstCategoryPos.set(d4);
        }
        return d4;
    }

    @Override
    protected Object getRange() {
        return new Object[]{this.getCategories(), this.categorySpacing.get(), this.firstCategoryPos.get(), this.getEffectiveTickLabelRotation()};
    }

    @Override
    protected void setRange(Object object, boolean bl) {
        Object[] arrobject = (Object[])object;
        List list = (List)arrobject[0];
        double d2 = (Double)arrobject[1];
        double d3 = (Double)arrobject[2];
        this.setEffectiveTickLabelRotation((Double)arrobject[3]);
        this.changeIsLocal = true;
        this.setCategories(FXCollections.observableArrayList(list));
        this.changeIsLocal = false;
        if (bl) {
            this.animator.stop(this.currentAnimationID);
            this.currentAnimationID = this.animator.animate(new KeyFrame(Duration.ZERO, new KeyValue(this.firstCategoryPos, this.firstCategoryPos.get()), new KeyValue(this.categorySpacing, this.categorySpacing.get())), new KeyFrame(Duration.millis(1000.0), new KeyValue(this.firstCategoryPos, d3), new KeyValue(this.categorySpacing, d2)));
        } else {
            this.categorySpacing.set(d2);
            this.firstCategoryPos.set(d3);
        }
    }

    @Override
    protected Object autoRange(double d2) {
        double d3;
        Side side = this.getEffectiveSide();
        double d4 = this.calculateNewSpacing(d2, this.allDataCategories);
        double d5 = this.calculateNewFirstPos(d2, d4);
        double d6 = this.getTickLabelRotation();
        if (d2 >= 0.0 && (d3 = this.calculateRequiredSize(side.isVertical(), d6)) > d2) {
            d6 = 90.0;
        }
        return new Object[]{this.allDataCategories, d4, d5, d6};
    }

    private double calculateRequiredSize(boolean bl, double d2) {
        double d3 = Double.MAX_VALUE;
        double d4 = 0.0;
        double d5 = 0.0;
        boolean bl2 = true;
        for (String string : this.allDataCategories) {
            double d6;
            Dimension2D dimension2D = this.measureTickMarkSize(string, d2);
            double d7 = d6 = bl || d2 != 0.0 ? dimension2D.getHeight() : dimension2D.getWidth();
            if (bl2) {
                bl2 = false;
                d5 = d6 / 2.0;
                continue;
            }
            d4 = Math.max(d4, d5 + 6.0 + d6 / 2.0);
        }
        return this.getStartMargin() + d4 * (double)this.allDataCategories.size() + this.getEndMargin();
    }

    @Override
    protected List<String> calculateTickValues(double d2, Object object) {
        Object[] arrobject = (Object[])object;
        return (List)arrobject[0];
    }

    @Override
    protected String getTickMarkLabel(String string) {
        return string;
    }

    @Override
    protected Dimension2D measureTickMarkSize(String string, Object object) {
        Object[] arrobject = (Object[])object;
        double d2 = (Double)arrobject[3];
        return this.measureTickMarkSize(string, d2);
    }

    @Override
    public void invalidateRange(List<String> list) {
        super.invalidateRange(list);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(this.allDataCategories);
        for (String string : this.allDataCategories) {
            if (list.contains(string)) continue;
            arrayList.remove(string);
        }
        for (int i2 = 0; i2 < list.size(); ++i2) {
            int n2 = arrayList.size();
            if (arrayList.contains(list.get(i2))) continue;
            arrayList.add(i2 > n2 ? n2 : i2, list.get(i2));
        }
        this.allDataCategories.clear();
        this.allDataCategories.addAll(arrayList);
    }

    final List<String> getAllDataCategories() {
        return this.allDataCategories;
    }

    @Override
    public double getDisplayPosition(String string) {
        ObservableList<String> observableList = this.getCategories();
        if (!observableList.contains(string)) {
            return Double.NaN;
        }
        if (this.getEffectiveSide().isHorizontal()) {
            return this.firstCategoryPos.get() + (double)observableList.indexOf(string) * this.categorySpacing.get();
        }
        return this.firstCategoryPos.get() + (double)observableList.indexOf(string) * this.categorySpacing.get() * -1.0;
    }

    @Override
    public String getValueForDisplay(double d2) {
        if (this.getEffectiveSide().isHorizontal()) {
            if (d2 < 0.0 || d2 > this.getWidth()) {
                return null;
            }
            double d3 = (d2 - this.firstCategoryPos.get()) / this.categorySpacing.get();
            return this.toRealValue(d3);
        }
        if (d2 < 0.0 || d2 > this.getHeight()) {
            return null;
        }
        double d4 = (d2 - this.firstCategoryPos.get()) / (this.categorySpacing.get() * -1.0);
        return this.toRealValue(d4);
    }

    @Override
    public boolean isValueOnAxis(String string) {
        return this.getCategories().indexOf("" + string) != -1;
    }

    @Override
    public double toNumericValue(String string) {
        return this.getCategories().indexOf(string);
    }

    @Override
    public String toRealValue(double d2) {
        int n2 = (int)Math.round(d2);
        ObservableList<String> observableList = this.getCategories();
        if (n2 >= 0 && n2 < observableList.size()) {
            return (String)this.getCategories().get(n2);
        }
        return null;
    }

    @Override
    public double getZeroPosition() {
        return Double.NaN;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return CategoryAxis.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<CategoryAxis, Number> START_MARGIN = new CssMetaData<CategoryAxis, Number>("-fx-start-margin", SizeConverter.getInstance(), (Number)5.0){

            @Override
            public boolean isSettable(CategoryAxis categoryAxis) {
                return categoryAxis.startMargin == null || !categoryAxis.startMargin.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(CategoryAxis categoryAxis) {
                return (StyleableProperty)((Object)categoryAxis.startMarginProperty());
            }
        };
        private static final CssMetaData<CategoryAxis, Number> END_MARGIN = new CssMetaData<CategoryAxis, Number>("-fx-end-margin", SizeConverter.getInstance(), (Number)5.0){

            @Override
            public boolean isSettable(CategoryAxis categoryAxis) {
                return categoryAxis.endMargin == null || !categoryAxis.endMargin.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(CategoryAxis categoryAxis) {
                return (StyleableProperty)((Object)categoryAxis.endMarginProperty());
            }
        };
        private static final CssMetaData<CategoryAxis, Boolean> GAP_START_AND_END = new CssMetaData<CategoryAxis, Boolean>("-fx-gap-start-and-end", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(CategoryAxis categoryAxis) {
                return categoryAxis.gapStartAndEnd == null || !categoryAxis.gapStartAndEnd.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(CategoryAxis categoryAxis) {
                return (StyleableProperty)((Object)categoryAxis.gapStartAndEndProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Axis.getClassCssMetaData());
            arrayList.add(START_MARGIN);
            arrayList.add(END_MARGIN);
            arrayList.add(GAP_START_AND_END);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

