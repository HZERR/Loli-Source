/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class StackedBarChart<X, Y>
extends XYChart<X, Y> {
    private Map<XYChart.Series, Map<String, List<XYChart.Data<X, Y>>>> seriesCategoryMap = new HashMap<XYChart.Series, Map<String, List<XYChart.Data<X, Y>>>>();
    private Legend legend = new Legend();
    private final Orientation orientation;
    private CategoryAxis categoryAxis;
    private ValueAxis valueAxis;
    private int seriesDefaultColorIndex = 0;
    private Map<XYChart.Series<X, Y>, String> seriesDefaultColorMap = new HashMap<XYChart.Series<X, Y>, String>();
    private ListChangeListener<String> categoriesListener = new ListChangeListener<String>(){

        @Override
        public void onChanged(ListChangeListener.Change<? extends String> change) {
            while (change.next()) {
                for (String string : change.getRemoved()) {
                    for (XYChart.Series series : StackedBarChart.this.getData()) {
                        for (XYChart.Data data : series.getData()) {
                            Orientation orientation = StackedBarChart.this.orientation;
                            StackedBarChart.this.orientation;
                            if (!string.equals(orientation == Orientation.VERTICAL ? data.getXValue() : data.getYValue())) continue;
                            boolean bl = StackedBarChart.this.getAnimated();
                            StackedBarChart.this.setAnimated(false);
                            StackedBarChart.this.dataItemRemoved(data, series);
                            StackedBarChart.this.setAnimated(bl);
                        }
                    }
                    StackedBarChart.this.requestChartLayout();
                }
            }
        }
    };
    private DoubleProperty categoryGap = new StyleableDoubleProperty(10.0){

        @Override
        protected void invalidated() {
            this.get();
            StackedBarChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return StackedBarChart.this;
        }

        @Override
        public String getName() {
            return "categoryGap";
        }

        @Override
        public CssMetaData<StackedBarChart<?, ?>, Number> getCssMetaData() {
            return StyleableProperties.CATEGORY_GAP;
        }
    };
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public double getCategoryGap() {
        return this.categoryGap.getValue();
    }

    public void setCategoryGap(double d2) {
        this.categoryGap.setValue(d2);
    }

    public DoubleProperty categoryGapProperty() {
        return this.categoryGap;
    }

    public StackedBarChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2) {
        this(axis, axis2, FXCollections.observableArrayList());
    }

    public StackedBarChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2, @NamedArg(value="data") ObservableList<XYChart.Series<X, Y>> observableList) {
        super(axis, axis2);
        this.getStyleClass().add("stacked-bar-chart");
        this.setLegend(this.legend);
        if (!(axis instanceof ValueAxis && axis2 instanceof CategoryAxis || axis2 instanceof ValueAxis && axis instanceof CategoryAxis)) {
            throw new IllegalArgumentException("Axis type incorrect, one of X,Y should be CategoryAxis and the other NumberAxis");
        }
        if (axis instanceof CategoryAxis) {
            this.categoryAxis = (CategoryAxis)axis;
            this.valueAxis = (ValueAxis)axis2;
            this.orientation = Orientation.VERTICAL;
        } else {
            this.categoryAxis = (CategoryAxis)axis2;
            this.valueAxis = (ValueAxis)axis;
            this.orientation = Orientation.HORIZONTAL;
        }
        this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, this.orientation == Orientation.HORIZONTAL);
        this.pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, this.orientation == Orientation.VERTICAL);
        this.setData(observableList);
        this.categoryAxis.getCategories().addListener(this.categoriesListener);
    }

    public StackedBarChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2, @NamedArg(value="data") ObservableList<XYChart.Series<X, Y>> observableList, @NamedArg(value="categoryGap") double d2) {
        this(axis, axis2);
        this.setData(observableList);
        this.setCategoryGap(d2);
    }

    @Override
    protected void dataItemAdded(XYChart.Series<X, Y> series, int n2, XYChart.Data<X, Y> data) {
        String string = this.orientation == Orientation.VERTICAL ? (String)data.getXValue() : (String)data.getYValue();
        Map<String, List<XYChart.Data<X, Y>>> map = this.seriesCategoryMap.get(series);
        if (map == null) {
            map = new HashMap<String, List<XYChart.Data<X, Y>>>();
            this.seriesCategoryMap.put(series, map);
        }
        List<Object> list = map.get(string) != null ? map.get(string) : new ArrayList();
        list.add(data);
        map.put(string, list);
        Node node = this.createBar(series, this.getData().indexOf(series), data, n2);
        if (this.shouldAnimate()) {
            this.animateDataAdd(data, node);
        } else {
            this.getPlotChildren().add(node);
        }
    }

    @Override
    protected void dataItemRemoved(XYChart.Data<X, Y> data, XYChart.Series<X, Y> series) {
        Node node = data.getNode();
        if (node != null) {
            node.focusTraversableProperty().unbind();
        }
        if (this.shouldAnimate()) {
            Timeline timeline = this.createDataRemoveTimeline(data, node, series);
            timeline.setOnFinished(actionEvent -> this.removeDataItemFromDisplay(series, data));
            timeline.play();
        } else {
            this.getPlotChildren().remove(node);
            this.removeDataItemFromDisplay(series, data);
        }
    }

    @Override
    protected void dataItemChanged(XYChart.Data<X, Y> data) {
        double d2;
        double d3;
        if (this.orientation == Orientation.VERTICAL) {
            d3 = ((Number)data.getYValue()).doubleValue();
            d2 = ((Number)this.getCurrentDisplayedYValue(data)).doubleValue();
        } else {
            d3 = ((Number)data.getXValue()).doubleValue();
            d2 = ((Number)this.getCurrentDisplayedXValue(data)).doubleValue();
        }
        if (d2 > 0.0 && d3 < 0.0) {
            data.getNode().getStyleClass().add("negative");
        } else if (d2 < 0.0 && d3 > 0.0) {
            data.getNode().getStyleClass().remove("negative");
        }
    }

    private void animateDataAdd(XYChart.Data<X, Y> data, Node node) {
        if (this.orientation == Orientation.VERTICAL) {
            double d2 = ((Number)data.getYValue()).doubleValue();
            if (d2 < 0.0) {
                node.getStyleClass().add("negative");
            }
            data.setYValue(this.getYAxis().toRealValue(this.getYAxis().getZeroPosition()));
            this.setCurrentDisplayedYValue(data, this.getYAxis().toRealValue(this.getYAxis().getZeroPosition()));
            this.getPlotChildren().add(node);
            data.setYValue(this.getYAxis().toRealValue(d2));
            this.animate(new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.currentDisplayedYValueProperty(data), this.getCurrentDisplayedYValue(data))), new KeyFrame(Duration.millis(700.0), new KeyValue(this.currentDisplayedYValueProperty(data), data.getYValue(), Interpolator.EASE_BOTH))));
        } else {
            double d3 = ((Number)data.getXValue()).doubleValue();
            if (d3 < 0.0) {
                node.getStyleClass().add("negative");
            }
            data.setXValue(this.getXAxis().toRealValue(this.getXAxis().getZeroPosition()));
            this.setCurrentDisplayedXValue(data, this.getXAxis().toRealValue(this.getXAxis().getZeroPosition()));
            this.getPlotChildren().add(node);
            data.setXValue(this.getXAxis().toRealValue(d3));
            this.animate(new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.currentDisplayedXValueProperty(data), this.getCurrentDisplayedXValue(data))), new KeyFrame(Duration.millis(700.0), new KeyValue(this.currentDisplayedXValueProperty(data), data.getXValue(), Interpolator.EASE_BOTH))));
        }
    }

    @Override
    protected void seriesAdded(XYChart.Series<X, Y> series, int n2) {
        String string = "default-color" + this.seriesDefaultColorIndex % 8;
        this.seriesDefaultColorMap.put(series, string);
        ++this.seriesDefaultColorIndex;
        HashMap hashMap = new HashMap();
        for (int i2 = 0; i2 < series.getData().size(); ++i2) {
            double d2;
            XYChart.Data data = (XYChart.Data)series.getData().get(i2);
            Node node = this.createBar(series, n2, data, i2);
            String string2 = this.orientation == Orientation.VERTICAL ? (String)data.getXValue() : (String)data.getYValue();
            List<XYChart.Data> list = hashMap.get(string2) != null ? (List)hashMap.get(string2) : new ArrayList();
            list.add(data);
            hashMap.put(string2, list);
            if (this.shouldAnimate()) {
                this.animateDataAdd(data, node);
                continue;
            }
            double d3 = d2 = this.orientation == Orientation.VERTICAL ? ((Number)data.getYValue()).doubleValue() : ((Number)data.getXValue()).doubleValue();
            if (d2 < 0.0) {
                node.getStyleClass().add("negative");
            }
            this.getPlotChildren().add(node);
        }
        if (hashMap.size() > 0) {
            this.seriesCategoryMap.put(series, hashMap);
        }
    }

    private Timeline createDataRemoveTimeline(XYChart.Data<X, Y> data, Node node, XYChart.Series<X, Y> series) {
        Timeline timeline = new Timeline();
        if (this.orientation == Orientation.VERTICAL) {
            data.setYValue(this.getYAxis().toRealValue(this.getYAxis().getZeroPosition()));
            timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(this.currentDisplayedYValueProperty(data), this.getCurrentDisplayedYValue(data))), new KeyFrame(Duration.millis(700.0), actionEvent -> this.getPlotChildren().remove(node), new KeyValue(this.currentDisplayedYValueProperty(data), data.getYValue(), Interpolator.EASE_BOTH)));
        } else {
            data.setXValue(this.getXAxis().toRealValue(this.getXAxis().getZeroPosition()));
            timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(this.currentDisplayedXValueProperty(data), this.getCurrentDisplayedXValue(data))), new KeyFrame(Duration.millis(700.0), actionEvent -> this.getPlotChildren().remove(node), new KeyValue(this.currentDisplayedXValueProperty(data), data.getXValue(), Interpolator.EASE_BOTH)));
        }
        return timeline;
    }

    @Override
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
        --this.seriesDefaultColorIndex;
        if (this.shouldAnimate()) {
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.setOnFinished(actionEvent -> {
                this.removeSeriesFromDisplay(series);
                this.requestChartLayout();
            });
            for (XYChart.Data data : series.getData()) {
                Node node = data.getNode();
                if (this.getSeriesSize() > 1) {
                    for (int i2 = 0; i2 < series.getData().size(); ++i2) {
                        XYChart.Data data2 = (XYChart.Data)series.getData().get(i2);
                        Timeline timeline = this.createDataRemoveTimeline(data2, node, series);
                        parallelTransition.getChildren().add(timeline);
                    }
                    continue;
                }
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(700.0), node);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setOnFinished(actionEvent -> {
                    this.getPlotChildren().remove(node);
                    node.setOpacity(1.0);
                });
                parallelTransition.getChildren().add(fadeTransition);
            }
            parallelTransition.play();
        } else {
            for (XYChart.Data data : series.getData()) {
                Node node = data.getNode();
                this.getPlotChildren().remove(node);
            }
            this.removeSeriesFromDisplay(series);
            this.requestChartLayout();
        }
    }

    @Override
    protected void updateAxisRange() {
        ArrayList<String> arrayList;
        boolean bl;
        boolean bl2 = bl = this.categoryAxis == this.getXAxis();
        if (this.categoryAxis.isAutoRanging()) {
            arrayList = new ArrayList<String>();
            for (XYChart.Series object : this.getData()) {
                for (XYChart.Data data : object.getData()) {
                    if (data == null) continue;
                    arrayList.add((String)(bl ? data.getXValue() : data.getYValue()));
                }
            }
            this.categoryAxis.invalidateRange((List<String>)arrayList);
        }
        if (this.valueAxis.isAutoRanging()) {
            arrayList = new ArrayList();
            for (String string : this.categoryAxis.getAllDataCategories()) {
                double d2 = 0.0;
                double d3 = 0.0;
                Iterator iterator = this.getDisplayedSeriesIterator();
                while (iterator.hasNext()) {
                    XYChart.Series series = iterator.next();
                    for (XYChart.Data data : this.getDataItem(series, string)) {
                        if (data == null) continue;
                        boolean bl3 = data.getNode().getStyleClass().contains("negative");
                        Number number = (Number)(bl ? data.getYValue() : data.getXValue());
                        if (!bl3) {
                            d3 += this.valueAxis.toNumericValue(number);
                            continue;
                        }
                        d2 += this.valueAxis.toNumericValue(number);
                    }
                }
                arrayList.add((String)((Object)Double.valueOf(d3)));
                arrayList.add((String)((Object)Double.valueOf(d2)));
            }
            this.valueAxis.invalidateRange(arrayList);
        }
    }

    @Override
    protected void layoutPlotChildren() {
        double d2;
        double d3 = this.categoryAxis.getCategorySpacing();
        double d4 = d2 = d3 - this.getCategoryGap();
        double d5 = -((d3 - this.getCategoryGap()) / 2.0);
        double d6 = this.valueAxis.getLowerBound();
        double d7 = this.valueAxis.getUpperBound();
        for (String string : this.categoryAxis.getCategories()) {
            double d8 = 0.0;
            double d9 = 0.0;
            Iterator iterator = this.getDisplayedSeriesIterator();
            while (iterator.hasNext()) {
                XYChart.Series series = iterator.next();
                for (XYChart.Data data : this.getDataItem(series, string)) {
                    double d10;
                    double d11;
                    double d12;
                    double d13;
                    if (data == null) continue;
                    Node node = data.getNode();
                    Object x2 = this.getCurrentDisplayedXValue(data);
                    Object y2 = this.getCurrentDisplayedYValue(data);
                    if (this.orientation == Orientation.VERTICAL) {
                        d13 = this.getXAxis().getDisplayPosition(x2);
                        d12 = this.getYAxis().toNumericValue(y2);
                    } else {
                        d13 = this.getYAxis().getDisplayPosition(y2);
                        d12 = this.getXAxis().toNumericValue(x2);
                    }
                    boolean bl = node.getStyleClass().contains("negative");
                    if (!bl) {
                        d11 = this.valueAxis.getDisplayPosition(d8);
                        d10 = this.valueAxis.getDisplayPosition(d8 + d12);
                        d8 += d12;
                    } else {
                        d11 = this.valueAxis.getDisplayPosition(d9 + d12);
                        d10 = this.valueAxis.getDisplayPosition(d9);
                        d9 += d12;
                    }
                    if (this.orientation == Orientation.VERTICAL) {
                        node.resizeRelocate(d13 + d5, d10, d4, d11 - d10);
                        continue;
                    }
                    node.resizeRelocate(d11, d13 + d5, d10 - d11, d4);
                }
            }
        }
    }

    @Override
    int getSeriesSize() {
        int n2 = 0;
        Iterator iterator = this.getDisplayedSeriesIterator();
        while (iterator.hasNext()) {
            iterator.next();
            ++n2;
        }
        return n2;
    }

    @Override
    protected void updateLegend() {
        this.legend.getItems().clear();
        if (this.getData() != null) {
            for (int i2 = 0; i2 < this.getData().size(); ++i2) {
                XYChart.Series series = (XYChart.Series)this.getData().get(i2);
                Legend.LegendItem legendItem = new Legend.LegendItem(series.getName());
                String string = this.seriesDefaultColorMap.get(series);
                legendItem.getSymbol().getStyleClass().addAll("chart-bar", "series" + i2, "bar-legend-symbol", string);
                this.legend.getItems().add(legendItem);
            }
        }
        if (this.legend.getItems().size() > 0) {
            if (this.getLegend() == null) {
                this.setLegend(this.legend);
            }
        } else {
            this.setLegend(null);
        }
    }

    private Node createBar(XYChart.Series series, int n2, XYChart.Data data, int n3) {
        Node node = data.getNode();
        if (node == null) {
            node = new StackPane();
            node.setAccessibleRole(AccessibleRole.TEXT);
            node.setAccessibleRoleDescription("Bar");
            node.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
            data.setNode(node);
        }
        String string = this.seriesDefaultColorMap.get(series);
        node.getStyleClass().setAll("chart-bar", "series" + n2, "data" + n3, string);
        return node;
    }

    private List<XYChart.Data<X, Y>> getDataItem(XYChart.Series<X, Y> series, String string) {
        Map<String, List<XYChart.Data<X, Y>>> map = this.seriesCategoryMap.get(series);
        return map != null ? (map.get(string) != null ? map.get(string) : new ArrayList()) : new ArrayList();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return StackedBarChart.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<StackedBarChart<?, ?>, Number> CATEGORY_GAP = new CssMetaData<StackedBarChart<?, ?>, Number>("-fx-category-gap", SizeConverter.getInstance(), 10.0){

            @Override
            public boolean isSettable(StackedBarChart<?, ?> stackedBarChart) {
                return ((StackedBarChart)stackedBarChart).categoryGap == null || !((StackedBarChart)stackedBarChart).categoryGap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(StackedBarChart<?, ?> stackedBarChart) {
                return (StyleableProperty)((Object)stackedBarChart.categoryGapProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(XYChart.getClassCssMetaData());
            arrayList.add(CATEGORY_GAP);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

