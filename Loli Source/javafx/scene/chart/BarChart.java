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
import javafx.animation.Animation;
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

public class BarChart<X, Y>
extends XYChart<X, Y> {
    private Map<XYChart.Series<X, Y>, Map<String, XYChart.Data<X, Y>>> seriesCategoryMap = new HashMap<XYChart.Series<X, Y>, Map<String, XYChart.Data<X, Y>>>();
    private Legend legend = new Legend();
    private final Orientation orientation;
    private CategoryAxis categoryAxis;
    private ValueAxis valueAxis;
    private Timeline dataRemoveTimeline;
    private double bottomPos = 0.0;
    private static String NEGATIVE_STYLE = "negative";
    private ParallelTransition pt;
    private Map<XYChart.Data<X, Y>, Double> XYValueMap = new HashMap<XYChart.Data<X, Y>, Double>();
    private DoubleProperty barGap = new StyleableDoubleProperty(4.0){

        @Override
        protected void invalidated() {
            this.get();
            BarChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return BarChart.this;
        }

        @Override
        public String getName() {
            return "barGap";
        }

        @Override
        public CssMetaData<BarChart<?, ?>, Number> getCssMetaData() {
            return StyleableProperties.BAR_GAP;
        }
    };
    private DoubleProperty categoryGap = new StyleableDoubleProperty(10.0){

        @Override
        protected void invalidated() {
            this.get();
            BarChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return BarChart.this;
        }

        @Override
        public String getName() {
            return "categoryGap";
        }

        @Override
        public CssMetaData<BarChart<?, ?>, Number> getCssMetaData() {
            return StyleableProperties.CATEGORY_GAP;
        }
    };
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public final double getBarGap() {
        return this.barGap.getValue();
    }

    public final void setBarGap(double d2) {
        this.barGap.setValue(d2);
    }

    public final DoubleProperty barGapProperty() {
        return this.barGap;
    }

    public final double getCategoryGap() {
        return this.categoryGap.getValue();
    }

    public final void setCategoryGap(double d2) {
        this.categoryGap.setValue(d2);
    }

    public final DoubleProperty categoryGapProperty() {
        return this.categoryGap;
    }

    public BarChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2) {
        this(axis, axis2, FXCollections.observableArrayList());
    }

    public BarChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2, @NamedArg(value="data") ObservableList<XYChart.Series<X, Y>> observableList) {
        super(axis, axis2);
        this.getStyleClass().add("bar-chart");
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
    }

    public BarChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2, @NamedArg(value="data") ObservableList<XYChart.Series<X, Y>> observableList, @NamedArg(value="categoryGap") double d2) {
        this(axis, axis2);
        this.setData(observableList);
        this.setCategoryGap(d2);
    }

    @Override
    protected void dataItemAdded(XYChart.Series<X, Y> series, int n2, XYChart.Data<X, Y> data) {
        Object object;
        String string = this.orientation == Orientation.VERTICAL ? (String)data.getXValue() : (String)data.getYValue();
        Map<String, XYChart.Data<X, Y>> map = this.seriesCategoryMap.get(series);
        if (map == null) {
            map = new HashMap<String, XYChart.Data<X, Y>>();
            this.seriesCategoryMap.put(series, map);
        }
        if (!this.categoryAxis.getCategories().contains(string)) {
            this.categoryAxis.getCategories().add(n2, string);
        } else if (map.containsKey(string)) {
            object = map.get(string);
            this.getPlotChildren().remove(((XYChart.Data)object).getNode());
            this.removeDataItemFromDisplay(series, object);
            this.requestChartLayout();
            map.remove(string);
        }
        map.put(string, data);
        object = this.createBar(series, this.getData().indexOf(series), data, n2);
        if (this.shouldAnimate()) {
            this.animateDataAdd(data, (Node)object);
        } else {
            this.getPlotChildren().add((Node)object);
        }
    }

    @Override
    protected void dataItemRemoved(XYChart.Data<X, Y> data, XYChart.Series<X, Y> series) {
        Node node = data.getNode();
        if (node != null) {
            node.focusTraversableProperty().unbind();
        }
        if (this.shouldAnimate()) {
            this.XYValueMap.clear();
            this.dataRemoveTimeline = this.createDataRemoveTimeline(data, node, series);
            this.dataRemoveTimeline.setOnFinished(actionEvent -> {
                data.setSeries(null);
                this.removeDataItemFromDisplay(series, data);
            });
            this.dataRemoveTimeline.play();
        } else {
            this.processDataRemove(series, data);
            this.removeDataItemFromDisplay(series, data);
        }
    }

    @Override
    protected void dataItemChanged(XYChart.Data<X, Y> data) {
        double d2;
        double d3;
        if (this.orientation == Orientation.VERTICAL) {
            d3 = ((Number)data.getYValue()).doubleValue();
            d2 = ((Number)data.getCurrentY()).doubleValue();
        } else {
            d3 = ((Number)data.getXValue()).doubleValue();
            d2 = ((Number)data.getCurrentX()).doubleValue();
        }
        if (d2 > 0.0 && d3 < 0.0) {
            data.getNode().getStyleClass().add(NEGATIVE_STYLE);
        } else if (d2 < 0.0 && d3 > 0.0) {
            data.getNode().getStyleClass().remove(NEGATIVE_STYLE);
        }
    }

    @Override
    protected void seriesAdded(XYChart.Series<X, Y> series, int n2) {
        HashMap<String, XYChart.Data> hashMap = new HashMap<String, XYChart.Data>();
        for (int i2 = 0; i2 < series.getData().size(); ++i2) {
            double d2;
            XYChart.Data data = (XYChart.Data)series.getData().get(i2);
            Node node = this.createBar(series, n2, data, i2);
            String string = this.orientation == Orientation.VERTICAL ? (String)data.getXValue() : (String)data.getYValue();
            hashMap.put(string, data);
            if (this.shouldAnimate()) {
                this.animateDataAdd(data, node);
                continue;
            }
            double d3 = d2 = this.orientation == Orientation.VERTICAL ? ((Number)data.getYValue()).doubleValue() : ((Number)data.getXValue()).doubleValue();
            if (d2 < 0.0) {
                node.getStyleClass().add(NEGATIVE_STYLE);
            }
            this.getPlotChildren().add(node);
        }
        if (hashMap.size() > 0) {
            this.seriesCategoryMap.put(series, hashMap);
        }
    }

    @Override
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
        this.updateDefaultColorIndex(series);
        if (this.shouldAnimate()) {
            this.pt = new ParallelTransition();
            this.pt.setOnFinished(actionEvent -> this.removeSeriesFromDisplay(series));
            boolean bl = this.getSeriesSize() <= 1;
            this.XYValueMap.clear();
            for (XYChart.Data data : series.getData()) {
                Animation animation;
                Node node = data.getNode();
                if (!bl) {
                    animation = this.createDataRemoveTimeline(data, node, series);
                    this.pt.getChildren().add(animation);
                    continue;
                }
                animation = new FadeTransition(Duration.millis(700.0), node);
                ((FadeTransition)animation).setFromValue(1.0);
                ((FadeTransition)animation).setToValue(0.0);
                animation.setOnFinished(actionEvent -> {
                    this.processDataRemove(series, data);
                    node.setOpacity(1.0);
                });
                this.pt.getChildren().add(animation);
            }
            this.pt.play();
        } else {
            for (XYChart.Data data : series.getData()) {
                Node node = data.getNode();
                this.getPlotChildren().remove(node);
                this.updateMap(series, data);
            }
            this.removeSeriesFromDisplay(series);
        }
    }

    @Override
    protected void layoutPlotChildren() {
        double d2;
        double d3 = this.categoryAxis.getCategorySpacing();
        double d4 = d3 - (this.getCategoryGap() + this.getBarGap());
        double d5 = d4 / (double)this.getSeriesSize() - this.getBarGap();
        double d6 = -((d3 - this.getCategoryGap()) / 2.0);
        double d7 = d2 = this.valueAxis.getLowerBound() > 0.0 ? this.valueAxis.getDisplayPosition(this.valueAxis.getLowerBound()) : this.valueAxis.getZeroPosition();
        if (d5 <= 0.0) {
            d5 = 1.0;
        }
        int n2 = 0;
        for (String string : this.categoryAxis.getCategories()) {
            int n3 = 0;
            Iterator iterator = this.getDisplayedSeriesIterator();
            while (iterator.hasNext()) {
                double d8;
                double d9;
                XYChart.Series series = iterator.next();
                XYChart.Data data = this.getDataItem(series, n3, n2, string);
                if (data == null) continue;
                Node node = data.getNode();
                if (this.orientation == Orientation.VERTICAL) {
                    d9 = this.getXAxis().getDisplayPosition(data.getCurrentX());
                    d8 = this.getYAxis().getDisplayPosition(data.getCurrentY());
                } else {
                    d9 = this.getYAxis().getDisplayPosition(data.getCurrentY());
                    d8 = this.getXAxis().getDisplayPosition(data.getCurrentX());
                }
                if (Double.isNaN(d9) || Double.isNaN(d8)) continue;
                double d10 = Math.min(d8, d2);
                double d11 = Math.max(d8, d2);
                this.bottomPos = d10;
                if (this.orientation == Orientation.VERTICAL) {
                    node.resizeRelocate(d9 + d6 + (d5 + this.getBarGap()) * (double)n3, d10, d5, d11 - d10);
                } else {
                    node.resizeRelocate(d10, d9 + d6 + (d5 + this.getBarGap()) * (double)n3, d11 - d10, d5);
                }
                ++n3;
            }
            ++n2;
        }
    }

    @Override
    protected void updateLegend() {
        this.legend.getItems().clear();
        if (this.getData() != null) {
            for (int i2 = 0; i2 < this.getData().size(); ++i2) {
                XYChart.Series series = (XYChart.Series)this.getData().get(i2);
                Legend.LegendItem legendItem = new Legend.LegendItem(series.getName());
                legendItem.getSymbol().getStyleClass().addAll("chart-bar", "series" + i2, "bar-legend-symbol", series.defaultColorStyleClass);
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

    private void updateMap(XYChart.Series<X, Y> series, XYChart.Data<X, Y> data) {
        String string = this.orientation == Orientation.VERTICAL ? (String)data.getXValue() : (String)data.getYValue();
        Map<String, XYChart.Data<X, Y>> map = this.seriesCategoryMap.get(series);
        if (map != null) {
            map.remove(string);
            if (map.isEmpty()) {
                this.seriesCategoryMap.remove(series);
            }
        }
        if (this.seriesCategoryMap.isEmpty() && this.categoryAxis.isAutoRanging()) {
            this.categoryAxis.getCategories().clear();
        }
    }

    private void processDataRemove(XYChart.Series<X, Y> series, XYChart.Data<X, Y> data) {
        Node node = data.getNode();
        this.getPlotChildren().remove(node);
        this.updateMap(series, data);
    }

    private void animateDataAdd(XYChart.Data<X, Y> data, Node node) {
        if (this.orientation == Orientation.VERTICAL) {
            double d2 = ((Number)data.getYValue()).doubleValue();
            if (d2 < 0.0) {
                node.getStyleClass().add(NEGATIVE_STYLE);
            }
            data.setCurrentY(this.getYAxis().toRealValue(d2 < 0.0 ? -this.bottomPos : this.bottomPos));
            this.getPlotChildren().add(node);
            data.setYValue(this.getYAxis().toRealValue(d2));
            this.animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY())), new KeyFrame(Duration.millis(700.0), new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH)));
        } else {
            double d3 = ((Number)data.getXValue()).doubleValue();
            if (d3 < 0.0) {
                node.getStyleClass().add(NEGATIVE_STYLE);
            }
            data.setCurrentX(this.getXAxis().toRealValue(d3 < 0.0 ? -this.bottomPos : this.bottomPos));
            this.getPlotChildren().add(node);
            data.setXValue(this.getXAxis().toRealValue(d3));
            this.animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(700.0), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
        }
    }

    private Timeline createDataRemoveTimeline(XYChart.Data<X, Y> data, Node node, XYChart.Series<X, Y> series) {
        Timeline timeline = new Timeline();
        if (this.orientation == Orientation.VERTICAL) {
            this.XYValueMap.put(data, ((Number)data.getYValue()).doubleValue());
            data.setYValue(this.getYAxis().toRealValue(this.bottomPos));
            timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY())), new KeyFrame(Duration.millis(700.0), actionEvent -> {
                this.processDataRemove(series, data);
                this.XYValueMap.clear();
            }, new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH)));
        } else {
            this.XYValueMap.put(data, ((Number)data.getXValue()).doubleValue());
            data.setXValue(this.getXAxis().toRealValue(this.getXAxis().getZeroPosition()));
            timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(700.0), actionEvent -> {
                this.processDataRemove(series, data);
                this.XYValueMap.clear();
            }, new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
        }
        return timeline;
    }

    @Override
    void dataBeingRemovedIsAdded(XYChart.Data<X, Y> data, XYChart.Series<X, Y> series) {
        if (this.dataRemoveTimeline != null) {
            this.dataRemoveTimeline.setOnFinished(null);
            this.dataRemoveTimeline.stop();
        }
        this.processDataRemove(series, data);
        data.setSeries(null);
        this.removeDataItemFromDisplay(series, data);
        this.restoreDataValues(data);
        this.XYValueMap.clear();
    }

    private void restoreDataValues(XYChart.Data data) {
        Double d2 = this.XYValueMap.get(data);
        if (d2 != null) {
            if (this.orientation.equals((Object)Orientation.VERTICAL)) {
                data.setYValue(d2);
                data.setCurrentY(d2);
            } else {
                data.setXValue(d2);
                data.setCurrentX(d2);
            }
        }
    }

    @Override
    void seriesBeingRemovedIsAdded(XYChart.Series<X, Y> series) {
        boolean bl;
        boolean bl2 = bl = this.pt.getChildren().size() == 1;
        if (this.pt != null) {
            if (!this.pt.getChildren().isEmpty()) {
                for (Animation object : this.pt.getChildren()) {
                    object.setOnFinished(null);
                }
            }
            for (XYChart.Data data : series.getData()) {
                this.processDataRemove(series, data);
                if (bl) continue;
                this.restoreDataValues(data);
            }
            this.XYValueMap.clear();
            this.pt.setOnFinished(null);
            this.pt.getChildren().clear();
            this.pt.stop();
            this.removeSeriesFromDisplay(series);
        }
    }

    private void updateDefaultColorIndex(XYChart.Series<X, Y> series) {
        int n2 = (Integer)this.seriesColorMap.get(series);
        for (XYChart.Data data : series.getData()) {
            Node node = data.getNode();
            if (node == null) continue;
            node.getStyleClass().remove(DEFAULT_COLOR + n2);
        }
    }

    private Node createBar(XYChart.Series<X, Y> series, int n2, XYChart.Data<X, Y> data, int n3) {
        Node node = data.getNode();
        if (node == null) {
            node = new StackPane();
            node.setAccessibleRole(AccessibleRole.TEXT);
            node.setAccessibleRoleDescription("Bar");
            node.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
            data.setNode(node);
        }
        node.getStyleClass().addAll("chart-bar", "series" + n2, "data" + n3, series.defaultColorStyleClass);
        return node;
    }

    private XYChart.Data<X, Y> getDataItem(XYChart.Series<X, Y> series, int n2, int n3, String string) {
        Map<String, XYChart.Data<X, Y>> map = this.seriesCategoryMap.get(series);
        return map != null ? map.get(string) : null;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return BarChart.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<BarChart<?, ?>, Number> BAR_GAP = new CssMetaData<BarChart<?, ?>, Number>("-fx-bar-gap", SizeConverter.getInstance(), 4.0){

            @Override
            public boolean isSettable(BarChart<?, ?> barChart) {
                return ((BarChart)barChart).barGap == null || !((BarChart)barChart).barGap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(BarChart<?, ?> barChart) {
                return (StyleableProperty)((Object)barChart.barGapProperty());
            }
        };
        private static final CssMetaData<BarChart<?, ?>, Number> CATEGORY_GAP = new CssMetaData<BarChart<?, ?>, Number>("-fx-category-gap", SizeConverter.getInstance(), 10.0){

            @Override
            public boolean isSettable(BarChart<?, ?> barChart) {
                return ((BarChart)barChart).categoryGap == null || !((BarChart)barChart).categoryGap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(BarChart<?, ?> barChart) {
                return (StyleableProperty)((Object)barChart.categoryGapProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(XYChart.getClassCssMetaData());
            arrayList.add(BAR_GAP);
            arrayList.add(CATEGORY_GAP);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

