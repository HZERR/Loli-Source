/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.Chart;
import javafx.scene.layout.Region;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public abstract class XYChart<X, Y>
extends Chart {
    private final BitSet colorBits = new BitSet(8);
    static String DEFAULT_COLOR = "default-color";
    final Map<Series<X, Y>, Integer> seriesColorMap = new HashMap<Series<X, Y>, Integer>();
    private boolean rangeValid = false;
    private final Line verticalZeroLine = new Line();
    private final Line horizontalZeroLine = new Line();
    private final Path verticalGridLines = new Path();
    private final Path horizontalGridLines = new Path();
    private final Path horizontalRowFill = new Path();
    private final Path verticalRowFill = new Path();
    private final Region plotBackground = new Region();
    private final Group plotArea = new Group(){

        @Override
        public void requestLayout() {
        }
    };
    private final Group plotContent = new Group();
    private final Rectangle plotAreaClip = new Rectangle();
    private final List<Series<X, Y>> displayedSeries = new ArrayList<Series<X, Y>>();
    private final ListChangeListener<Series<X, Y>> seriesChanged = change -> {
        ObservableList observableList = change.getList();
        while (change.next()) {
            int n2;
            if (change.wasPermutated()) {
                this.displayedSeries.sort((series, series2) -> observableList.indexOf(series2) - observableList.indexOf(series));
            }
            if (change.getRemoved().size() > 0) {
                this.updateLegend();
            }
            HashSet<Series<X, Y>> hashSet = new HashSet<Series<X, Y>>(this.displayedSeries);
            hashSet.removeAll(change.getRemoved());
            for (Series series3 : change.getAddedSubList()) {
                if (hashSet.add(series3)) continue;
                throw new IllegalArgumentException("Duplicate series added");
            }
            for (Series series3 : change.getRemoved()) {
                series3.setToRemove = true;
                this.seriesRemoved(series3);
                n2 = this.seriesColorMap.remove(series3);
                this.colorBits.clear(n2);
            }
            for (int i2 = change.getFrom(); i2 < change.getTo() && !change.wasPermutated(); ++i2) {
                Series series3;
                series3 = (Series)change.getList().get(i2);
                series3.setChart(this);
                if (series3.setToRemove) {
                    series3.setToRemove = false;
                    series3.getChart().seriesBeingRemovedIsAdded(series3);
                }
                this.displayedSeries.add(series3);
                n2 = this.colorBits.nextClearBit(0);
                this.colorBits.set(n2, true);
                series3.defaultColorStyleClass = DEFAULT_COLOR + n2 % 8;
                this.seriesColorMap.put(series3, n2 % 8);
                this.seriesAdded(series3, i2);
            }
            if (change.getFrom() < change.getTo()) {
                this.updateLegend();
            }
            this.seriesChanged(change);
        }
        this.invalidateRange();
        this.requestChartLayout();
    };
    private final Axis<X> xAxis;
    private final Axis<Y> yAxis;
    private ObjectProperty<ObservableList<Series<X, Y>>> data = new ObjectPropertyBase<ObservableList<Series<X, Y>>>(){
        private ObservableList<Series<X, Y>> old;

        @Override
        protected void invalidated() {
            ObservableList observableList = (ObservableList)this.getValue();
            int n2 = -1;
            if (this.old != null) {
                this.old.removeListener(XYChart.this.seriesChanged);
                if (observableList != null && this.old.size() > 0) {
                    n2 = ((Series)this.old.get(0)).getChart().getAnimated() ? 1 : 2;
                    ((Series)this.old.get(0)).getChart().setAnimated(false);
                }
            }
            if (observableList != null) {
                observableList.addListener(XYChart.this.seriesChanged);
            }
            if (this.old != null || observableList != null) {
                int n3;
                final List list = this.old != null ? this.old : Collections.emptyList();
                int n4 = n3 = observableList != null ? observableList.size() : 0;
                if (n3 > 0 || !list.isEmpty()) {
                    XYChart.this.seriesChanged.onChanged(new NonIterableChange<Series<X, Y>>(0, n3, observableList){

                        @Override
                        public List<Series<X, Y>> getRemoved() {
                            return list;
                        }

                        @Override
                        protected int[] getPermutation() {
                            return new int[0];
                        }
                    });
                }
            } else if (this.old != null && this.old.size() > 0) {
                XYChart.this.seriesChanged.onChanged(new NonIterableChange<Series<X, Y>>(0, 0, observableList){

                    @Override
                    public List<Series<X, Y>> getRemoved() {
                        return old;
                    }

                    @Override
                    protected int[] getPermutation() {
                        return new int[0];
                    }
                });
            }
            if (observableList != null && observableList.size() > 0 && n2 != -1) {
                ((Series)observableList.get(0)).getChart().setAnimated(n2 == 1);
            }
            this.old = observableList;
        }

        @Override
        public Object getBean() {
            return XYChart.this;
        }

        @Override
        public String getName() {
            return "data";
        }
    };
    private BooleanProperty verticalGridLinesVisible = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return XYChart.this;
        }

        @Override
        public String getName() {
            return "verticalGridLinesVisible";
        }

        @Override
        public CssMetaData<XYChart<?, ?>, Boolean> getCssMetaData() {
            return StyleableProperties.VERTICAL_GRID_LINE_VISIBLE;
        }
    };
    private BooleanProperty horizontalGridLinesVisible = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return XYChart.this;
        }

        @Override
        public String getName() {
            return "horizontalGridLinesVisible";
        }

        @Override
        public CssMetaData<XYChart<?, ?>, Boolean> getCssMetaData() {
            return StyleableProperties.HORIZONTAL_GRID_LINE_VISIBLE;
        }
    };
    private BooleanProperty alternativeColumnFillVisible = new StyleableBooleanProperty(false){

        @Override
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return XYChart.this;
        }

        @Override
        public String getName() {
            return "alternativeColumnFillVisible";
        }

        @Override
        public CssMetaData<XYChart<?, ?>, Boolean> getCssMetaData() {
            return StyleableProperties.ALTERNATIVE_COLUMN_FILL_VISIBLE;
        }
    };
    private BooleanProperty alternativeRowFillVisible = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return XYChart.this;
        }

        @Override
        public String getName() {
            return "alternativeRowFillVisible";
        }

        @Override
        public CssMetaData<XYChart<?, ?>, Boolean> getCssMetaData() {
            return StyleableProperties.ALTERNATIVE_ROW_FILL_VISIBLE;
        }
    };
    private BooleanProperty verticalZeroLineVisible = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return XYChart.this;
        }

        @Override
        public String getName() {
            return "verticalZeroLineVisible";
        }

        @Override
        public CssMetaData<XYChart<?, ?>, Boolean> getCssMetaData() {
            return StyleableProperties.VERTICAL_ZERO_LINE_VISIBLE;
        }
    };
    private BooleanProperty horizontalZeroLineVisible = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return XYChart.this;
        }

        @Override
        public String getName() {
            return "horizontalZeroLineVisible";
        }

        @Override
        public CssMetaData<XYChart<?, ?>, Boolean> getCssMetaData() {
            return StyleableProperties.HORIZONTAL_ZERO_LINE_VISIBLE;
        }
    };

    public Axis<X> getXAxis() {
        return this.xAxis;
    }

    public Axis<Y> getYAxis() {
        return this.yAxis;
    }

    public final ObservableList<Series<X, Y>> getData() {
        return (ObservableList)this.data.getValue();
    }

    public final void setData(ObservableList<Series<X, Y>> observableList) {
        this.data.setValue(observableList);
    }

    public final ObjectProperty<ObservableList<Series<X, Y>>> dataProperty() {
        return this.data;
    }

    public final boolean getVerticalGridLinesVisible() {
        return this.verticalGridLinesVisible.get();
    }

    public final void setVerticalGridLinesVisible(boolean bl) {
        this.verticalGridLinesVisible.set(bl);
    }

    public final BooleanProperty verticalGridLinesVisibleProperty() {
        return this.verticalGridLinesVisible;
    }

    public final boolean isHorizontalGridLinesVisible() {
        return this.horizontalGridLinesVisible.get();
    }

    public final void setHorizontalGridLinesVisible(boolean bl) {
        this.horizontalGridLinesVisible.set(bl);
    }

    public final BooleanProperty horizontalGridLinesVisibleProperty() {
        return this.horizontalGridLinesVisible;
    }

    public final boolean isAlternativeColumnFillVisible() {
        return this.alternativeColumnFillVisible.getValue();
    }

    public final void setAlternativeColumnFillVisible(boolean bl) {
        this.alternativeColumnFillVisible.setValue(bl);
    }

    public final BooleanProperty alternativeColumnFillVisibleProperty() {
        return this.alternativeColumnFillVisible;
    }

    public final boolean isAlternativeRowFillVisible() {
        return this.alternativeRowFillVisible.getValue();
    }

    public final void setAlternativeRowFillVisible(boolean bl) {
        this.alternativeRowFillVisible.setValue(bl);
    }

    public final BooleanProperty alternativeRowFillVisibleProperty() {
        return this.alternativeRowFillVisible;
    }

    public final boolean isVerticalZeroLineVisible() {
        return this.verticalZeroLineVisible.get();
    }

    public final void setVerticalZeroLineVisible(boolean bl) {
        this.verticalZeroLineVisible.set(bl);
    }

    public final BooleanProperty verticalZeroLineVisibleProperty() {
        return this.verticalZeroLineVisible;
    }

    public final boolean isHorizontalZeroLineVisible() {
        return this.horizontalZeroLineVisible.get();
    }

    public final void setHorizontalZeroLineVisible(boolean bl) {
        this.horizontalZeroLineVisible.set(bl);
    }

    public final BooleanProperty horizontalZeroLineVisibleProperty() {
        return this.horizontalZeroLineVisible;
    }

    protected ObservableList<Node> getPlotChildren() {
        return this.plotContent.getChildren();
    }

    public XYChart(Axis<X> axis, Axis<Y> axis2) {
        this.xAxis = axis;
        if (axis.getSide() == null) {
            axis.setSide(Side.BOTTOM);
        }
        axis.setEffectiveOrientation(Orientation.HORIZONTAL);
        this.yAxis = axis2;
        if (axis2.getSide() == null) {
            axis2.setSide(Side.LEFT);
        }
        axis2.setEffectiveOrientation(Orientation.VERTICAL);
        axis.autoRangingProperty().addListener((observableValue, bl, bl2) -> this.updateAxisRange());
        axis2.autoRangingProperty().addListener((observableValue, bl, bl2) -> this.updateAxisRange());
        this.getChartChildren().addAll(this.plotBackground, this.plotArea, axis, axis2);
        this.plotArea.setAutoSizeChildren(false);
        this.plotContent.setAutoSizeChildren(false);
        this.plotAreaClip.setSmooth(false);
        this.plotArea.setClip(this.plotAreaClip);
        this.plotArea.getChildren().addAll(this.verticalRowFill, this.horizontalRowFill, this.verticalGridLines, this.horizontalGridLines, this.verticalZeroLine, this.horizontalZeroLine, this.plotContent);
        this.plotContent.getStyleClass().setAll("plot-content");
        this.plotBackground.getStyleClass().setAll("chart-plot-background");
        this.verticalRowFill.getStyleClass().setAll("chart-alternative-column-fill");
        this.horizontalRowFill.getStyleClass().setAll("chart-alternative-row-fill");
        this.verticalGridLines.getStyleClass().setAll("chart-vertical-grid-lines");
        this.horizontalGridLines.getStyleClass().setAll("chart-horizontal-grid-lines");
        this.verticalZeroLine.getStyleClass().setAll("chart-vertical-zero-line");
        this.horizontalZeroLine.getStyleClass().setAll("chart-horizontal-zero-line");
        this.plotContent.setManaged(false);
        this.plotArea.setManaged(false);
        this.animatedProperty().addListener((observableValue, bl, bl2) -> {
            if (this.getXAxis() != null) {
                this.getXAxis().setAnimated((boolean)bl2);
            }
            if (this.getYAxis() != null) {
                this.getYAxis().setAnimated((boolean)bl2);
            }
        });
    }

    final int getDataSize() {
        ObservableList<Series<X, Y>> observableList = this.getData();
        return observableList != null ? observableList.size() : 0;
    }

    private void seriesNameChanged() {
        this.updateLegend();
        this.requestChartLayout();
    }

    private void dataItemsChanged(Series<X, Y> series, List<Data<X, Y>> list, int n2, int n3, boolean bl) {
        for (Data<X, Y> data : list) {
            this.dataItemRemoved(data, series);
        }
        for (int i2 = n2; i2 < n3; ++i2) {
            Data data = (Data)series.getData().get(i2);
            this.dataItemAdded(series, i2, data);
        }
        this.invalidateRange();
        this.requestChartLayout();
    }

    private void dataXValueChanged(Data<X, Y> data) {
        if (data.getCurrentX() != data.getXValue()) {
            this.invalidateRange();
        }
        this.dataItemChanged(data);
        if (this.shouldAnimate()) {
            this.animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(700.0), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
        } else {
            data.setCurrentX(data.getXValue());
            this.requestChartLayout();
        }
    }

    private void dataYValueChanged(Data<X, Y> data) {
        if (data.getCurrentY() != data.getYValue()) {
            this.invalidateRange();
        }
        this.dataItemChanged(data);
        if (this.shouldAnimate()) {
            this.animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY())), new KeyFrame(Duration.millis(700.0), new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH)));
        } else {
            data.setCurrentY(data.getYValue());
            this.requestChartLayout();
        }
    }

    private void dataExtraValueChanged(Data<X, Y> data) {
        if (data.getCurrentY() != data.getYValue()) {
            this.invalidateRange();
        }
        this.dataItemChanged(data);
        if (this.shouldAnimate()) {
            this.animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY())), new KeyFrame(Duration.millis(700.0), new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH)));
        } else {
            data.setCurrentY(data.getYValue());
            this.requestChartLayout();
        }
    }

    protected void updateLegend() {
    }

    void seriesBeingRemovedIsAdded(Series<X, Y> series) {
    }

    void dataBeingRemovedIsAdded(Data<X, Y> data, Series<X, Y> series) {
    }

    protected abstract void dataItemAdded(Series<X, Y> var1, int var2, Data<X, Y> var3);

    protected abstract void dataItemRemoved(Data<X, Y> var1, Series<X, Y> var2);

    protected abstract void dataItemChanged(Data<X, Y> var1);

    protected abstract void seriesAdded(Series<X, Y> var1, int var2);

    protected abstract void seriesRemoved(Series<X, Y> var1);

    protected void seriesChanged(ListChangeListener.Change<? extends Series> change) {
    }

    private void invalidateRange() {
        this.rangeValid = false;
    }

    protected void updateAxisRange() {
        Axis<X> axis = this.getXAxis();
        Axis<Y> axis2 = this.getYAxis();
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        if (axis.isAutoRanging()) {
            arrayList = new ArrayList();
        }
        if (axis2.isAutoRanging()) {
            arrayList2 = new ArrayList();
        }
        if (arrayList != null || arrayList2 != null) {
            for (Series series : this.getData()) {
                for (Data data : series.getData()) {
                    if (arrayList != null) {
                        arrayList.add(data.getXValue());
                    }
                    if (arrayList2 == null) continue;
                    arrayList2.add(data.getYValue());
                }
            }
            if (arrayList != null) {
                axis.invalidateRange(arrayList);
            }
            if (arrayList2 != null) {
                axis2.invalidateRange(arrayList2);
            }
        }
    }

    protected abstract void layoutPlotChildren();

    @Override
    protected final void layoutChartChildren(double d2, double d3, double d4, double d5) {
        double d6;
        double d7;
        double d8;
        Object object;
        int n2;
        if (this.getData() == null) {
            return;
        }
        if (!this.rangeValid) {
            this.rangeValid = true;
            if (this.getData() != null) {
                this.updateAxisRange();
            }
        }
        d2 = this.snapPosition(d2);
        d3 = this.snapPosition(d3);
        Axis<X> axis = this.getXAxis();
        ObservableList<Axis.TickMark<X>> observableList = axis.getTickMarks();
        Axis<Y> axis2 = this.getYAxis();
        ObservableList<Axis.TickMark<Y>> observableList2 = axis2.getTickMarks();
        if (axis == null || axis2 == null) {
            return;
        }
        double d9 = 0.0;
        double d10 = 30.0;
        double d11 = 0.0;
        double d12 = 0.0;
        for (int i2 = 0; i2 < 5; ++i2) {
            double d13;
            d12 = this.snapSize(d5 - d10);
            if (d12 < 0.0) {
                d12 = 0.0;
            }
            if ((d9 = this.snapSize(d4 - (d11 = axis2.prefWidth(d12)))) < 0.0) {
                d9 = 0.0;
            }
            if ((d13 = axis.prefHeight(d9)) == d10) break;
            d10 = d13;
        }
        d9 = Math.ceil(d9);
        d10 = Math.ceil(d10);
        d11 = Math.ceil(d11);
        d12 = Math.ceil(d12);
        double d14 = 0.0;
        switch (axis.getEffectiveSide()) {
            case TOP: {
                axis.setVisible(true);
                d14 = d2 + 1.0;
                d2 += d10;
                break;
            }
            case BOTTOM: {
                axis.setVisible(true);
                d14 = d2 + d12;
            }
        }
        double d15 = 0.0;
        switch (axis2.getEffectiveSide()) {
            case LEFT: {
                axis2.setVisible(true);
                d15 = d3 + 1.0;
                d3 += d11;
                break;
            }
            case RIGHT: {
                axis2.setVisible(true);
                d15 = d3 + d9;
            }
        }
        axis.resizeRelocate(d3, d14, d9, d10);
        axis2.resizeRelocate(d15, d2, d11, d12);
        axis.requestAxisLayout();
        axis.layout();
        axis2.requestAxisLayout();
        axis2.layout();
        this.layoutPlotChildren();
        double d16 = axis.getZeroPosition();
        double d17 = axis2.getZeroPosition();
        if (Double.isNaN(d16) || !this.isVerticalZeroLineVisible()) {
            this.verticalZeroLine.setVisible(false);
        } else {
            this.verticalZeroLine.setStartX(d3 + d16 + 0.5);
            this.verticalZeroLine.setStartY(d2);
            this.verticalZeroLine.setEndX(d3 + d16 + 0.5);
            this.verticalZeroLine.setEndY(d2 + d12);
            this.verticalZeroLine.setVisible(true);
        }
        if (Double.isNaN(d17) || !this.isHorizontalZeroLineVisible()) {
            this.horizontalZeroLine.setVisible(false);
        } else {
            this.horizontalZeroLine.setStartX(d3);
            this.horizontalZeroLine.setStartY(d2 + d17 + 0.5);
            this.horizontalZeroLine.setEndX(d3 + d9);
            this.horizontalZeroLine.setEndY(d2 + d17 + 0.5);
            this.horizontalZeroLine.setVisible(true);
        }
        this.plotBackground.resizeRelocate(d3, d2, d9, d12);
        this.plotAreaClip.setX(d3);
        this.plotAreaClip.setY(d2);
        this.plotAreaClip.setWidth(d9 + 1.0);
        this.plotAreaClip.setHeight(d12 + 1.0);
        this.plotContent.setLayoutX(d3);
        this.plotContent.setLayoutY(d2);
        this.plotContent.requestLayout();
        this.verticalGridLines.getElements().clear();
        if (this.getVerticalGridLinesVisible()) {
            for (n2 = 0; n2 < observableList.size(); ++n2) {
                object = (Axis.TickMark)observableList.get(n2);
                d8 = axis.getDisplayPosition(((Axis.TickMark)object).getValue());
                if (d8 == d16 && this.isVerticalZeroLineVisible() || !(d8 > 0.0) || !(d8 <= d9)) continue;
                this.verticalGridLines.getElements().add(new MoveTo(d3 + d8 + 0.5, d2));
                this.verticalGridLines.getElements().add(new LineTo(d3 + d8 + 0.5, d2 + d12));
            }
        }
        this.horizontalGridLines.getElements().clear();
        if (this.isHorizontalGridLinesVisible()) {
            for (n2 = 0; n2 < observableList2.size(); ++n2) {
                object = (Axis.TickMark)observableList2.get(n2);
                d8 = axis2.getDisplayPosition(((Axis.TickMark)object).getValue());
                if (d8 == d17 && this.isHorizontalZeroLineVisible() || !(d8 >= 0.0) || !(d8 < d12)) continue;
                this.horizontalGridLines.getElements().add(new MoveTo(d3, d2 + d8 + 0.5));
                this.horizontalGridLines.getElements().add(new LineTo(d3 + d9, d2 + d8 + 0.5));
            }
        }
        this.verticalRowFill.getElements().clear();
        if (this.isAlternativeColumnFillVisible()) {
            int n3;
            ArrayList<Double> arrayList = new ArrayList<Double>();
            object = new ArrayList();
            for (n3 = 0; n3 < observableList.size(); ++n3) {
                d7 = axis.getDisplayPosition(((Axis.TickMark)observableList.get(n3)).getValue());
                if (d7 == d16) {
                    arrayList.add(d7);
                    object.add(d7);
                    continue;
                }
                if (d7 < d16) {
                    arrayList.add(d7);
                    continue;
                }
                object.add(d7);
            }
            Collections.sort(arrayList);
            Collections.sort(object);
            for (n3 = 1; n3 < arrayList.size(); n3 += 2) {
                if (n3 + 1 >= arrayList.size()) continue;
                d7 = (Double)arrayList.get(n3);
                d6 = (Double)arrayList.get(n3 + 1);
                this.verticalRowFill.getElements().addAll(new MoveTo(d3 + d7, d2), new LineTo(d3 + d7, d2 + d12), new LineTo(d3 + d6, d2 + d12), new LineTo(d3 + d6, d2), new ClosePath());
            }
            for (n3 = 0; n3 < object.size(); n3 += 2) {
                if (n3 + 1 >= object.size()) continue;
                d7 = (Double)object.get(n3);
                d6 = (Double)object.get(n3 + 1);
                this.verticalRowFill.getElements().addAll(new MoveTo(d3 + d7, d2), new LineTo(d3 + d7, d2 + d12), new LineTo(d3 + d6, d2 + d12), new LineTo(d3 + d6, d2), new ClosePath());
            }
        }
        this.horizontalRowFill.getElements().clear();
        if (this.isAlternativeRowFillVisible()) {
            int n4;
            ArrayList<Double> arrayList = new ArrayList<Double>();
            object = new ArrayList<Double>();
            for (n4 = 0; n4 < observableList2.size(); ++n4) {
                d7 = axis2.getDisplayPosition(((Axis.TickMark)observableList2.get(n4)).getValue());
                if (d7 == d17) {
                    arrayList.add(d7);
                    object.add(d7);
                    continue;
                }
                if (d7 < d17) {
                    arrayList.add(d7);
                    continue;
                }
                object.add(d7);
            }
            Collections.sort(arrayList);
            Collections.sort(object);
            for (n4 = 1; n4 < arrayList.size(); n4 += 2) {
                if (n4 + 1 >= arrayList.size()) continue;
                d7 = (Double)arrayList.get(n4);
                d6 = (Double)arrayList.get(n4 + 1);
                this.horizontalRowFill.getElements().addAll(new MoveTo(d3, d2 + d7), new LineTo(d3 + d9, d2 + d7), new LineTo(d3 + d9, d2 + d6), new LineTo(d3, d2 + d6), new ClosePath());
            }
            for (n4 = 0; n4 < object.size(); n4 += 2) {
                if (n4 + 1 >= object.size()) continue;
                d7 = (Double)object.get(n4);
                d6 = (Double)object.get(n4 + 1);
                this.horizontalRowFill.getElements().addAll(new MoveTo(d3, d2 + d7), new LineTo(d3 + d9, d2 + d7), new LineTo(d3 + d9, d2 + d6), new LineTo(d3, d2 + d6), new ClosePath());
            }
        }
    }

    int getSeriesIndex(Series<X, Y> series) {
        return this.displayedSeries.indexOf(series);
    }

    int getSeriesSize() {
        return this.displayedSeries.size();
    }

    protected final void removeSeriesFromDisplay(Series<X, Y> series) {
        if (series != null) {
            series.setToRemove = false;
        }
        ((Series)series).setChart(null);
        this.displayedSeries.remove(series);
    }

    protected final Iterator<Series<X, Y>> getDisplayedSeriesIterator() {
        return Collections.unmodifiableList(this.displayedSeries).iterator();
    }

    final KeyFrame[] createSeriesRemoveTimeLine(Series<X, Y> series, long l2) {
        ArrayList<Node> arrayList = new ArrayList<Node>();
        arrayList.add(series.getNode());
        for (Data arrkeyValue2 : series.getData()) {
            if (arrkeyValue2.getNode() == null) continue;
            arrayList.add(arrkeyValue2.getNode());
        }
        KeyValue[] arrkeyValue3 = new KeyValue[arrayList.size()];
        KeyValue[] arrkeyValue = new KeyValue[arrayList.size()];
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            arrkeyValue3[i2] = new KeyValue(((Node)arrayList.get(i2)).opacityProperty(), 1);
            arrkeyValue[i2] = new KeyValue(((Node)arrayList.get(i2)).opacityProperty(), 0);
        }
        return new KeyFrame[]{new KeyFrame(Duration.ZERO, arrkeyValue3), new KeyFrame(Duration.millis(l2), actionEvent -> {
            this.getPlotChildren().removeAll((Collection<?>)arrayList);
            this.removeSeriesFromDisplay(series);
        }, arrkeyValue)};
    }

    protected final X getCurrentDisplayedXValue(Data<X, Y> data) {
        return data.getCurrentX();
    }

    protected final void setCurrentDisplayedXValue(Data<X, Y> data, X x2) {
        data.setCurrentX(x2);
    }

    protected final ObjectProperty<X> currentDisplayedXValueProperty(Data<X, Y> data) {
        return data.currentXProperty();
    }

    protected final Y getCurrentDisplayedYValue(Data<X, Y> data) {
        return data.getCurrentY();
    }

    protected final void setCurrentDisplayedYValue(Data<X, Y> data, Y y2) {
        data.setCurrentY(y2);
    }

    protected final ObjectProperty<Y> currentDisplayedYValueProperty(Data<X, Y> data) {
        return data.currentYProperty();
    }

    protected final Object getCurrentDisplayedExtraValue(Data<X, Y> data) {
        return data.getCurrentExtraValue();
    }

    protected final void setCurrentDisplayedExtraValue(Data<X, Y> data, Object object) {
        data.setCurrentExtraValue(object);
    }

    protected final ObjectProperty<Object> currentDisplayedExtraValueProperty(Data<X, Y> data) {
        return data.currentExtraValueProperty();
    }

    protected final Iterator<Data<X, Y>> getDisplayedDataIterator(Series<X, Y> series) {
        return Collections.unmodifiableList(((Series)series).displayedData).iterator();
    }

    protected final void removeDataItemFromDisplay(Series<X, Y> series, Data<X, Y> data) {
        ((Series)series).removeDataItemRef(data);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return XYChart.getClassCssMetaData();
    }

    public static final class Series<X, Y> {
        String defaultColorStyleClass;
        boolean setToRemove = false;
        private List<Data<X, Y>> displayedData = new ArrayList<Data<X, Y>>();
        private final ListChangeListener<Data<X, Y>> dataChangeListener = new ListChangeListener<Data<X, Y>>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends Data<X, Y>> change) {
                ObservableList observableList = change.getList();
                XYChart xYChart = this.getChart();
                while (change.next()) {
                    HashSet<Data> hashSet;
                    if (xYChart != null) {
                        if (change.wasPermutated()) {
                            displayedData.sort((data, data2) -> observableList.indexOf(data2) - observableList.indexOf(data));
                            return;
                        }
                        hashSet = new HashSet(displayedData);
                        hashSet.removeAll(change.getRemoved());
                        for (Data data3 : change.getAddedSubList()) {
                            if (hashSet.add(data3)) continue;
                            throw new IllegalArgumentException("Duplicate data added");
                        }
                        for (Data data4 : change.getRemoved()) {
                            data4.setToRemove = true;
                        }
                        if (change.getAddedSize() > 0) {
                            for (Data data5 : change.getAddedSubList()) {
                                if (!data5.setToRemove) continue;
                                if (xYChart != null) {
                                    xYChart.dataBeingRemovedIsAdded(data5, this);
                                }
                                data5.setToRemove = false;
                            }
                            for (Data data6 : change.getAddedSubList()) {
                                data6.setSeries(this);
                            }
                            if (change.getFrom() == 0) {
                                displayedData.addAll(0, change.getAddedSubList());
                            } else {
                                displayedData.addAll(displayedData.indexOf(observableList.get(change.getFrom() - 1)) + 1, change.getAddedSubList());
                            }
                        }
                        xYChart.dataItemsChanged(this, change.getRemoved(), change.getFrom(), change.getTo(), change.wasPermutated());
                        continue;
                    }
                    hashSet = new HashSet<Data>();
                    for (Data data7 : observableList) {
                        if (hashSet.add(data7)) continue;
                        throw new IllegalArgumentException("Duplicate data added");
                    }
                    for (Data data8 : change.getAddedSubList()) {
                        data8.setSeries(this);
                    }
                }
            }
        };
        private final ReadOnlyObjectWrapper<XYChart<X, Y>> chart = new ReadOnlyObjectWrapper<XYChart<X, Y>>(this, "chart"){

            @Override
            protected void invalidated() {
                if (this.get() == null) {
                    displayedData.clear();
                } else {
                    displayedData.addAll(this.getData());
                }
            }
        };
        private final StringProperty name = new StringPropertyBase(){

            @Override
            protected void invalidated() {
                this.get();
                if (this.getChart() != null) {
                    this.getChart().seriesNameChanged();
                }
            }

            @Override
            public Object getBean() {
                return this;
            }

            @Override
            public String getName() {
                return "name";
            }
        };
        private ObjectProperty<Node> node = new SimpleObjectProperty<Node>(this, "node");
        private final ObjectProperty<ObservableList<Data<X, Y>>> data = new ObjectPropertyBase<ObservableList<Data<X, Y>>>(){
            private ObservableList<Data<X, Y>> old;

            @Override
            protected void invalidated() {
                ObservableList observableList = (ObservableList)this.getValue();
                if (this.old != null) {
                    this.old.removeListener(dataChangeListener);
                }
                if (observableList != null) {
                    observableList.addListener(dataChangeListener);
                }
                if (this.old != null || observableList != null) {
                    int n2;
                    final List list = this.old != null ? this.old : Collections.emptyList();
                    int n3 = n2 = observableList != null ? observableList.size() : 0;
                    if (n2 > 0 || !list.isEmpty()) {
                        dataChangeListener.onChanged(new NonIterableChange<Data<X, Y>>(0, n2, observableList){

                            @Override
                            public List<Data<X, Y>> getRemoved() {
                                return list;
                            }

                            @Override
                            protected int[] getPermutation() {
                                return new int[0];
                            }
                        });
                    }
                } else if (this.old != null && this.old.size() > 0) {
                    dataChangeListener.onChanged(new NonIterableChange<Data<X, Y>>(0, 0, observableList){

                        @Override
                        public List<Data<X, Y>> getRemoved() {
                            return old;
                        }

                        @Override
                        protected int[] getPermutation() {
                            return new int[0];
                        }
                    });
                }
                this.old = observableList;
            }

            @Override
            public Object getBean() {
                return this;
            }

            @Override
            public String getName() {
                return "data";
            }
        };

        public final XYChart<X, Y> getChart() {
            return (XYChart)this.chart.get();
        }

        private void setChart(XYChart<X, Y> xYChart) {
            this.chart.set(xYChart);
        }

        public final ReadOnlyObjectProperty<XYChart<X, Y>> chartProperty() {
            return this.chart.getReadOnlyProperty();
        }

        public final String getName() {
            return (String)this.name.get();
        }

        public final void setName(String string) {
            this.name.set(string);
        }

        public final StringProperty nameProperty() {
            return this.name;
        }

        public final Node getNode() {
            return (Node)this.node.get();
        }

        public final void setNode(Node node) {
            this.node.set(node);
        }

        public final ObjectProperty<Node> nodeProperty() {
            return this.node;
        }

        public final ObservableList<Data<X, Y>> getData() {
            return (ObservableList)this.data.getValue();
        }

        public final void setData(ObservableList<Data<X, Y>> observableList) {
            this.data.setValue(observableList);
        }

        public final ObjectProperty<ObservableList<Data<X, Y>>> dataProperty() {
            return this.data;
        }

        public Series() {
            this(FXCollections.observableArrayList());
        }

        public Series(ObservableList<Data<X, Y>> observableList) {
            this.setData(observableList);
            for (Data data : observableList) {
                data.setSeries(this);
            }
        }

        public Series(String string, ObservableList<Data<X, Y>> observableList) {
            this(observableList);
            this.setName(string);
        }

        public String toString() {
            return "Series[" + this.getName() + "]";
        }

        private void removeDataItemRef(Data<X, Y> data) {
            if (data != null) {
                ((Data)data).setToRemove = false;
            }
            this.displayedData.remove(data);
        }

        int getItemIndex(Data<X, Y> data) {
            return this.displayedData.indexOf(data);
        }

        Data<X, Y> getItem(int n2) {
            return this.displayedData.get(n2);
        }

        int getDataSize() {
            return this.displayedData.size();
        }
    }

    public static final class Data<X, Y> {
        private boolean setToRemove = false;
        private Series<X, Y> series;
        private ObjectProperty<X> xValue = new ObjectPropertyBase<X>(this){
            final /* synthetic */ Data this$0;
            {
                this.this$0 = data;
            }

            @Override
            protected void invalidated() {
                this.get();
                if (this.this$0.series != null) {
                    XYChart xYChart = this.this$0.series.getChart();
                    if (xYChart != null) {
                        xYChart.dataXValueChanged(this.this$0);
                    }
                } else {
                    this.this$0.setCurrentX(this.get());
                }
            }

            @Override
            public Object getBean() {
                return this.this$0;
            }

            @Override
            public String getName() {
                return "XValue";
            }
        };
        private ObjectProperty<Y> yValue = new ObjectPropertyBase<Y>(this){
            final /* synthetic */ Data this$0;
            {
                this.this$0 = data;
            }

            @Override
            protected void invalidated() {
                this.get();
                if (this.this$0.series != null) {
                    XYChart xYChart = this.this$0.series.getChart();
                    if (xYChart != null) {
                        xYChart.dataYValueChanged(this.this$0);
                    }
                } else {
                    this.this$0.setCurrentY(this.get());
                }
            }

            @Override
            public Object getBean() {
                return this.this$0;
            }

            @Override
            public String getName() {
                return "YValue";
            }
        };
        private ObjectProperty<Object> extraValue = new ObjectPropertyBase<Object>(this){
            final /* synthetic */ Data this$0;
            {
                this.this$0 = data;
            }

            @Override
            protected void invalidated() {
                XYChart xYChart;
                this.get();
                if (this.this$0.series != null && (xYChart = this.this$0.series.getChart()) != null) {
                    xYChart.dataExtraValueChanged(this.this$0);
                }
            }

            @Override
            public Object getBean() {
                return this.this$0;
            }

            @Override
            public String getName() {
                return "extraValue";
            }
        };
        private ObjectProperty<Node> node = new SimpleObjectProperty<Node>(this, (Object)this, "node"){
            final /* synthetic */ Data this$0;
            {
                this.this$0 = data;
                super(object, string);
            }

            @Override
            protected void invalidated() {
                Node node = (Node)this.get();
                if (node != null) {
                    node.accessibleTextProperty().unbind();
                    node.accessibleTextProperty().bind(new StringBinding(){
                        {
                            this.bind(this$0.currentXProperty(), this$0.currentYProperty());
                        }

                        @Override
                        protected String computeValue() {
                            String string = this$0.series != null ? this$0.series.getName() : "";
                            return string + " X Axis is " + this$0.getCurrentX() + " Y Axis is " + this$0.getCurrentY();
                        }
                    });
                }
            }
        };
        private ObjectProperty<X> currentX = new SimpleObjectProperty<X>(this, "currentX");
        private ObjectProperty<Y> currentY = new SimpleObjectProperty<Y>(this, "currentY");
        private ObjectProperty<Object> currentExtraValue = new SimpleObjectProperty<Object>(this, "currentExtraValue");

        void setSeries(Series<X, Y> series) {
            this.series = series;
        }

        public final X getXValue() {
            return (X)this.xValue.get();
        }

        public final void setXValue(X x2) {
            this.xValue.set(x2);
            if (this.currentX.get() == null || this.series != null && this.series.getChart() == null) {
                this.currentX.setValue(x2);
            }
        }

        public final ObjectProperty<X> XValueProperty() {
            return this.xValue;
        }

        public final Y getYValue() {
            return (Y)this.yValue.get();
        }

        public final void setYValue(Y y2) {
            this.yValue.set(y2);
            if (this.currentY.get() == null || this.series != null && this.series.getChart() == null) {
                this.currentY.setValue(y2);
            }
        }

        public final ObjectProperty<Y> YValueProperty() {
            return this.yValue;
        }

        public final Object getExtraValue() {
            return this.extraValue.get();
        }

        public final void setExtraValue(Object object) {
            this.extraValue.set(object);
        }

        public final ObjectProperty<Object> extraValueProperty() {
            return this.extraValue;
        }

        public final Node getNode() {
            return (Node)this.node.get();
        }

        public final void setNode(Node node) {
            this.node.set(node);
        }

        public final ObjectProperty<Node> nodeProperty() {
            return this.node;
        }

        final X getCurrentX() {
            return (X)this.currentX.get();
        }

        final void setCurrentX(X x2) {
            this.currentX.set(x2);
        }

        final ObjectProperty<X> currentXProperty() {
            return this.currentX;
        }

        final Y getCurrentY() {
            return (Y)this.currentY.get();
        }

        final void setCurrentY(Y y2) {
            this.currentY.set(y2);
        }

        final ObjectProperty<Y> currentYProperty() {
            return this.currentY;
        }

        final Object getCurrentExtraValue() {
            return this.currentExtraValue.getValue();
        }

        final void setCurrentExtraValue(Object object) {
            this.currentExtraValue.setValue(object);
        }

        final ObjectProperty<Object> currentExtraValueProperty() {
            return this.currentExtraValue;
        }

        public Data() {
        }

        public Data(X x2, Y y2) {
            this.setXValue(x2);
            this.setYValue(y2);
            this.setCurrentX(x2);
            this.setCurrentY(y2);
        }

        public Data(X x2, Y y2, Object object) {
            this.setXValue(x2);
            this.setYValue(y2);
            this.setExtraValue(object);
            this.setCurrentX(x2);
            this.setCurrentY(y2);
            this.setCurrentExtraValue(object);
        }

        public String toString() {
            return "Data[" + this.getXValue() + "," + this.getYValue() + "," + this.getExtraValue() + "]";
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<XYChart<?, ?>, Boolean> HORIZONTAL_GRID_LINE_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-horizontal-grid-lines-visible", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(XYChart<?, ?> xYChart) {
                return ((XYChart)xYChart).horizontalGridLinesVisible == null || !((XYChart)xYChart).horizontalGridLinesVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> xYChart) {
                return (StyleableProperty)((Object)xYChart.horizontalGridLinesVisibleProperty());
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> HORIZONTAL_ZERO_LINE_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-horizontal-zero-line-visible", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(XYChart<?, ?> xYChart) {
                return ((XYChart)xYChart).horizontalZeroLineVisible == null || !((XYChart)xYChart).horizontalZeroLineVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> xYChart) {
                return (StyleableProperty)((Object)xYChart.horizontalZeroLineVisibleProperty());
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> ALTERNATIVE_ROW_FILL_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-alternative-row-fill-visible", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(XYChart<?, ?> xYChart) {
                return ((XYChart)xYChart).alternativeRowFillVisible == null || !((XYChart)xYChart).alternativeRowFillVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> xYChart) {
                return (StyleableProperty)((Object)xYChart.alternativeRowFillVisibleProperty());
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> VERTICAL_GRID_LINE_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-vertical-grid-lines-visible", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(XYChart<?, ?> xYChart) {
                return ((XYChart)xYChart).verticalGridLinesVisible == null || !((XYChart)xYChart).verticalGridLinesVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> xYChart) {
                return (StyleableProperty)((Object)xYChart.verticalGridLinesVisibleProperty());
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> VERTICAL_ZERO_LINE_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-vertical-zero-line-visible", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(XYChart<?, ?> xYChart) {
                return ((XYChart)xYChart).verticalZeroLineVisible == null || !((XYChart)xYChart).verticalZeroLineVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> xYChart) {
                return (StyleableProperty)((Object)xYChart.verticalZeroLineVisibleProperty());
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> ALTERNATIVE_COLUMN_FILL_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-alternative-column-fill-visible", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(XYChart<?, ?> xYChart) {
                return ((XYChart)xYChart).alternativeColumnFillVisible == null || !((XYChart)xYChart).alternativeColumnFillVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> xYChart) {
                return (StyleableProperty)((Object)xYChart.alternativeColumnFillVisibleProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Chart.getClassCssMetaData());
            arrayList.add(HORIZONTAL_GRID_LINE_VISIBLE);
            arrayList.add(HORIZONTAL_ZERO_LINE_VISIBLE);
            arrayList.add(ALTERNATIVE_ROW_FILL_VISIBLE);
            arrayList.add(VERTICAL_GRID_LINE_VISIBLE);
            arrayList.add(VERTICAL_ZERO_LINE_VISIBLE);
            arrayList.add(ALTERNATIVE_COLUMN_FILL_VISIBLE);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

