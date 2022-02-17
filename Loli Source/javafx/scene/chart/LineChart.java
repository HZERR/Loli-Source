/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;

public class LineChart<X, Y>
extends XYChart<X, Y> {
    private Map<XYChart.Series<X, Y>, DoubleProperty> seriesYMultiplierMap = new HashMap<XYChart.Series<X, Y>, DoubleProperty>();
    private Legend legend = new Legend();
    private Timeline dataRemoveTimeline;
    private XYChart.Series<X, Y> seriesOfDataRemoved = null;
    private XYChart.Data<X, Y> dataItemBeingRemoved = null;
    private FadeTransition fadeSymbolTransition = null;
    private Map<XYChart.Data<X, Y>, Double> XYValueMap = new HashMap<XYChart.Data<X, Y>, Double>();
    private Timeline seriesRemoveTimeline = null;
    private BooleanProperty createSymbols = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            for (int i2 = 0; i2 < LineChart.this.getData().size(); ++i2) {
                XYChart.Series series = (XYChart.Series)LineChart.this.getData().get(i2);
                for (int i3 = 0; i3 < series.getData().size(); ++i3) {
                    XYChart.Data data = (XYChart.Data)series.getData().get(i3);
                    Node node = data.getNode();
                    if (this.get() && node == null) {
                        node = LineChart.this.createSymbol(series, LineChart.this.getData().indexOf(series), data, i3);
                        LineChart.this.getPlotChildren().add(node);
                        continue;
                    }
                    if (this.get() || node == null) continue;
                    LineChart.this.getPlotChildren().remove(node);
                    node = null;
                    data.setNode(null);
                }
            }
            LineChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return LineChart.this;
        }

        @Override
        public String getName() {
            return "createSymbols";
        }

        @Override
        public CssMetaData<LineChart<?, ?>, Boolean> getCssMetaData() {
            return StyleableProperties.CREATE_SYMBOLS;
        }
    };
    private ObjectProperty<SortingPolicy> axisSortingPolicy = new ObjectPropertyBase<SortingPolicy>(SortingPolicy.X_AXIS){

        @Override
        protected void invalidated() {
            LineChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return LineChart.this;
        }

        @Override
        public String getName() {
            return "axisSortingPolicy";
        }
    };

    public final boolean getCreateSymbols() {
        return this.createSymbols.getValue();
    }

    public final void setCreateSymbols(boolean bl) {
        this.createSymbols.setValue(bl);
    }

    public final BooleanProperty createSymbolsProperty() {
        return this.createSymbols;
    }

    public final SortingPolicy getAxisSortingPolicy() {
        return (SortingPolicy)((Object)this.axisSortingPolicy.getValue());
    }

    public final void setAxisSortingPolicy(SortingPolicy sortingPolicy) {
        this.axisSortingPolicy.setValue(sortingPolicy);
    }

    public final ObjectProperty<SortingPolicy> axisSortingPolicyProperty() {
        return this.axisSortingPolicy;
    }

    public LineChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2) {
        this(axis, axis2, FXCollections.observableArrayList());
    }

    public LineChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2, @NamedArg(value="data") ObservableList<XYChart.Series<X, Y>> observableList) {
        super(axis, axis2);
        this.setLegend(this.legend);
        this.setData(observableList);
    }

    @Override
    protected void updateAxisRange() {
        Axis axis = this.getXAxis();
        Axis axis2 = this.getYAxis();
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        if (axis.isAutoRanging()) {
            arrayList = new ArrayList();
        }
        if (axis2.isAutoRanging()) {
            arrayList2 = new ArrayList();
        }
        if (arrayList != null || arrayList2 != null) {
            for (XYChart.Series series : this.getData()) {
                for (XYChart.Data data : series.getData()) {
                    if (arrayList != null) {
                        arrayList.add(data.getXValue());
                    }
                    if (arrayList2 == null) continue;
                    arrayList2.add(data.getYValue());
                }
            }
            if (arrayList != null && (arrayList.size() != 1 || this.getXAxis().toNumericValue(arrayList.get(0)) != 0.0)) {
                axis.invalidateRange(arrayList);
            }
            if (arrayList2 != null && (arrayList2.size() != 1 || this.getYAxis().toNumericValue(arrayList2.get(0)) != 0.0)) {
                axis2.invalidateRange(arrayList2);
            }
        }
    }

    @Override
    protected void dataItemAdded(XYChart.Series<X, Y> series, int n2, XYChart.Data<X, Y> data) {
        Node node = this.createSymbol(series, this.getData().indexOf(series), data, n2);
        if (this.shouldAnimate()) {
            if (this.dataRemoveTimeline != null && this.dataRemoveTimeline.getStatus().equals((Object)Animation.Status.RUNNING) && this.seriesOfDataRemoved == series) {
                this.dataRemoveTimeline.stop();
                this.dataRemoveTimeline = null;
                this.getPlotChildren().remove(this.dataItemBeingRemoved.getNode());
                this.removeDataItemFromDisplay(this.seriesOfDataRemoved, this.dataItemBeingRemoved);
                this.seriesOfDataRemoved = null;
                this.dataItemBeingRemoved = null;
            }
            boolean bl = false;
            if (n2 > 0 && n2 < series.getData().size() - 1) {
                bl = true;
                XYChart.Data data2 = (XYChart.Data)series.getData().get(n2 - 1);
                XYChart.Data data3 = (XYChart.Data)series.getData().get(n2 + 1);
                if (data2 != null && data3 != null) {
                    double d2 = this.getXAxis().toNumericValue(data2.getXValue());
                    double d3 = this.getYAxis().toNumericValue(data2.getYValue());
                    double d4 = this.getXAxis().toNumericValue(data3.getXValue());
                    double d5 = this.getYAxis().toNumericValue(data3.getYValue());
                    double d6 = this.getXAxis().toNumericValue(data.getXValue());
                    if (d6 > d2 && d6 < d4) {
                        double d7 = (d5 - d3) / (d4 - d2) * d6 + (d4 * d3 - d5 * d2) / (d4 - d2);
                        data.setCurrentY(this.getYAxis().toRealValue(d7));
                        data.setCurrentX(this.getXAxis().toRealValue(d6));
                    } else {
                        double d8 = (d4 + d2) / 2.0;
                        double d9 = (d5 + d3) / 2.0;
                        data.setCurrentX(this.getXAxis().toRealValue(d8));
                        data.setCurrentY(this.getYAxis().toRealValue(d9));
                    }
                }
            } else if (n2 == 0 && series.getData().size() > 1) {
                bl = true;
                data.setCurrentX(((XYChart.Data)series.getData().get(1)).getXValue());
                data.setCurrentY(((XYChart.Data)series.getData().get(1)).getYValue());
            } else if (n2 == series.getData().size() - 1 && series.getData().size() > 1) {
                bl = true;
                int n3 = series.getData().size() - 2;
                data.setCurrentX(((XYChart.Data)series.getData().get(n3)).getXValue());
                data.setCurrentY(((XYChart.Data)series.getData().get(n3)).getYValue());
            } else if (node != null) {
                node.setOpacity(0.0);
                this.getPlotChildren().add(node);
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(500.0), node);
                fadeTransition.setToValue(1.0);
                fadeTransition.play();
            }
            if (bl) {
                this.animate(new KeyFrame(Duration.ZERO, actionEvent -> {
                    if (node != null && !this.getPlotChildren().contains(node)) {
                        this.getPlotChildren().add(node);
                    }
                }, new KeyValue(data.currentYProperty(), data.getCurrentY()), new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(700.0), new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
            }
        } else if (node != null) {
            this.getPlotChildren().add(node);
        }
    }

    @Override
    protected void dataItemRemoved(XYChart.Data<X, Y> data, XYChart.Series<X, Y> series) {
        Node node = data.getNode();
        if (node != null) {
            node.focusTraversableProperty().unbind();
        }
        int n2 = series.getItemIndex(data);
        if (this.shouldAnimate()) {
            this.XYValueMap.clear();
            boolean bl = false;
            int n3 = series.getDataSize();
            int n4 = series.getData().size();
            if (n2 > 0 && n2 < n3 - 1) {
                bl = true;
                XYChart.Data<X, Y> data2 = series.getItem(n2 - 1);
                XYChart.Data<X, Y> data3 = series.getItem(n2 + 1);
                double d2 = this.getXAxis().toNumericValue(data2.getXValue());
                double d3 = this.getYAxis().toNumericValue(data2.getYValue());
                double d4 = this.getXAxis().toNumericValue(data3.getXValue());
                double d5 = this.getYAxis().toNumericValue(data3.getYValue());
                double d6 = this.getXAxis().toNumericValue(data.getXValue());
                double d7 = this.getYAxis().toNumericValue(data.getYValue());
                if (d6 > d2 && d6 < d4) {
                    double d8 = (d5 - d3) / (d4 - d2) * d6 + (d4 * d3 - d5 * d2) / (d4 - d2);
                    data.setCurrentX(this.getXAxis().toRealValue(d6));
                    data.setCurrentY(this.getYAxis().toRealValue(d7));
                    data.setXValue(this.getXAxis().toRealValue(d6));
                    data.setYValue(this.getYAxis().toRealValue(d8));
                } else {
                    double d9 = (d4 + d2) / 2.0;
                    double d10 = (d5 + d3) / 2.0;
                    data.setCurrentX(this.getXAxis().toRealValue(d9));
                    data.setCurrentY(this.getYAxis().toRealValue(d10));
                }
            } else if (n2 == 0 && n4 > 1) {
                bl = true;
                data.setXValue(((XYChart.Data)series.getData().get(0)).getXValue());
                data.setYValue(((XYChart.Data)series.getData().get(0)).getYValue());
            } else if (n2 == n3 - 1 && n4 > 1) {
                bl = true;
                int n5 = n4 - 1;
                data.setXValue(((XYChart.Data)series.getData().get(n5)).getXValue());
                data.setYValue(((XYChart.Data)series.getData().get(n5)).getYValue());
            } else if (node != null) {
                this.fadeSymbolTransition = new FadeTransition(Duration.millis(500.0), node);
                this.fadeSymbolTransition.setToValue(0.0);
                this.fadeSymbolTransition.setOnFinished(actionEvent -> {
                    data.setSeries(null);
                    this.getPlotChildren().remove(node);
                    this.removeDataItemFromDisplay(series, data);
                    node.setOpacity(1.0);
                });
                this.fadeSymbolTransition.play();
            }
            if (bl) {
                this.dataRemoveTimeline = this.createDataRemoveTimeline(data, node, series);
                this.seriesOfDataRemoved = series;
                this.dataItemBeingRemoved = data;
                this.dataRemoveTimeline.play();
            }
        } else {
            data.setSeries(null);
            if (node != null) {
                this.getPlotChildren().remove(node);
            }
            this.removeDataItemFromDisplay(series, data);
        }
    }

    @Override
    protected void dataItemChanged(XYChart.Data<X, Y> data) {
    }

    @Override
    protected void seriesChanged(ListChangeListener.Change<? extends XYChart.Series> change) {
        for (int i2 = 0; i2 < this.getDataSize(); ++i2) {
            XYChart.Series series = (XYChart.Series)this.getData().get(i2);
            Node node = series.getNode();
            if (node == null) continue;
            node.getStyleClass().setAll("chart-series-line", "series" + i2, series.defaultColorStyleClass);
        }
    }

    @Override
    protected void seriesAdded(XYChart.Series<X, Y> series, int n2) {
        Path path = new Path();
        path.setStrokeLineJoin(StrokeLineJoin.BEVEL);
        series.setNode(path);
        SimpleDoubleProperty simpleDoubleProperty = new SimpleDoubleProperty(this, "seriesYMultiplier");
        this.seriesYMultiplierMap.put(series, simpleDoubleProperty);
        if (this.shouldAnimate()) {
            path.setOpacity(0.0);
            simpleDoubleProperty.setValue(0.0);
        } else {
            simpleDoubleProperty.setValue(1.0);
        }
        this.getPlotChildren().add(path);
        ArrayList<KeyFrame> arrayList = new ArrayList<KeyFrame>();
        if (this.shouldAnimate()) {
            arrayList.add(new KeyFrame(Duration.ZERO, new KeyValue(path.opacityProperty(), 0), new KeyValue(simpleDoubleProperty, 0)));
            arrayList.add(new KeyFrame(Duration.millis(200.0), new KeyValue(path.opacityProperty(), 1)));
            arrayList.add(new KeyFrame(Duration.millis(500.0), new KeyValue(simpleDoubleProperty, 1)));
        }
        for (int i2 = 0; i2 < series.getData().size(); ++i2) {
            XYChart.Data data = (XYChart.Data)series.getData().get(i2);
            Node node = this.createSymbol(series, n2, data, i2);
            if (node == null) continue;
            if (this.shouldAnimate()) {
                node.setOpacity(0.0);
            }
            this.getPlotChildren().add(node);
            if (!this.shouldAnimate()) continue;
            arrayList.add(new KeyFrame(Duration.ZERO, new KeyValue(node.opacityProperty(), 0)));
            arrayList.add(new KeyFrame(Duration.millis(200.0), new KeyValue(node.opacityProperty(), 1)));
        }
        if (this.shouldAnimate()) {
            this.animate(arrayList.toArray(new KeyFrame[arrayList.size()]));
        }
    }

    private void updateDefaultColorIndex(XYChart.Series<X, Y> series) {
        int n2 = (Integer)this.seriesColorMap.get(series);
        series.getNode().getStyleClass().remove(DEFAULT_COLOR + n2);
        for (int i2 = 0; i2 < series.getData().size(); ++i2) {
            Node node = ((XYChart.Data)series.getData().get(i2)).getNode();
            if (node == null) continue;
            node.getStyleClass().remove(DEFAULT_COLOR + n2);
        }
    }

    @Override
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
        this.updateDefaultColorIndex(series);
        this.seriesYMultiplierMap.remove(series);
        if (this.shouldAnimate()) {
            this.seriesRemoveTimeline = new Timeline(this.createSeriesRemoveTimeLine(series, 900L));
            this.seriesRemoveTimeline.play();
        } else {
            this.getPlotChildren().remove(series.getNode());
            for (XYChart.Data data : series.getData()) {
                this.getPlotChildren().remove(data.getNode());
            }
            this.removeSeriesFromDisplay(series);
        }
    }

    @Override
    protected void layoutPlotChildren() {
        ArrayList<LineTo> arrayList = new ArrayList<LineTo>(this.getDataSize());
        for (int i2 = 0; i2 < this.getDataSize(); ++i2) {
            XYChart.Series series = (XYChart.Series)this.getData().get(i2);
            DoubleProperty doubleProperty = this.seriesYMultiplierMap.get(series);
            if (!(series.getNode() instanceof Path)) continue;
            ObservableList<PathElement> observableList = ((Path)series.getNode()).getElements();
            observableList.clear();
            arrayList.clear();
            Object object = this.getDisplayedDataIterator(series);
            while (object.hasNext()) {
                XYChart.Data data = object.next();
                double d2 = this.getXAxis().getDisplayPosition(data.getCurrentX());
                double d3 = this.getYAxis().getDisplayPosition(this.getYAxis().toRealValue(this.getYAxis().toNumericValue(data.getCurrentY()) * doubleProperty.getValue()));
                if (Double.isNaN(d2) || Double.isNaN(d3)) continue;
                arrayList.add(new LineTo(d2, d3));
                Node node = data.getNode();
                if (node == null) continue;
                double d4 = node.prefWidth(-1.0);
                double d5 = node.prefHeight(-1.0);
                node.resizeRelocate(d2 - d4 / 2.0, d3 - d5 / 2.0, d4, d5);
            }
            switch (this.getAxisSortingPolicy()) {
                case X_AXIS: {
                    Collections.sort(arrayList, (lineTo, lineTo2) -> Double.compare(lineTo.getX(), lineTo2.getX()));
                    break;
                }
                case Y_AXIS: {
                    Collections.sort(arrayList, (lineTo, lineTo2) -> Double.compare(lineTo.getY(), lineTo2.getY()));
                }
            }
            if (arrayList.isEmpty()) continue;
            object = (LineTo)arrayList.get(0);
            observableList.add(new MoveTo(((LineTo)object).getX(), ((LineTo)object).getY()));
            observableList.addAll((Collection<PathElement>)arrayList);
        }
    }

    @Override
    void dataBeingRemovedIsAdded(XYChart.Data data, XYChart.Series series) {
        Node node;
        if (this.fadeSymbolTransition != null) {
            this.fadeSymbolTransition.setOnFinished(null);
            this.fadeSymbolTransition.stop();
        }
        if (this.dataRemoveTimeline != null) {
            this.dataRemoveTimeline.setOnFinished(null);
            this.dataRemoveTimeline.stop();
        }
        if ((node = data.getNode()) != null) {
            this.getPlotChildren().remove(node);
        }
        data.setSeries(null);
        this.removeDataItemFromDisplay(series, data);
        Double d2 = this.XYValueMap.get(data);
        if (d2 != null) {
            data.setYValue(d2);
            data.setCurrentY(d2);
        }
        this.XYValueMap.clear();
    }

    @Override
    void seriesBeingRemovedIsAdded(XYChart.Series<X, Y> series) {
        if (this.seriesRemoveTimeline != null) {
            this.seriesRemoveTimeline.setOnFinished(null);
            this.seriesRemoveTimeline.stop();
            this.getPlotChildren().remove(series.getNode());
            for (XYChart.Data data : series.getData()) {
                this.getPlotChildren().remove(data.getNode());
            }
            this.removeSeriesFromDisplay(series);
        }
    }

    private Timeline createDataRemoveTimeline(XYChart.Data<X, Y> data, Node node, XYChart.Series<X, Y> series) {
        Timeline timeline = new Timeline();
        this.XYValueMap.put(data, ((Number)data.getYValue()).doubleValue());
        timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY()), new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(500.0), actionEvent -> {
            if (node != null) {
                this.getPlotChildren().remove(node);
            }
            this.removeDataItemFromDisplay(series, data);
            this.XYValueMap.clear();
        }, new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
        return timeline;
    }

    private Node createSymbol(XYChart.Series<X, Y> series, int n2, XYChart.Data<X, Y> data, int n3) {
        Node node = data.getNode();
        if (node == null && this.getCreateSymbols()) {
            node = new StackPane();
            node.setAccessibleRole(AccessibleRole.TEXT);
            node.setAccessibleRoleDescription("Point");
            node.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
            data.setNode(node);
        }
        if (node != null) {
            node.getStyleClass().addAll("chart-line-symbol", "series" + n2, "data" + n3, series.defaultColorStyleClass);
        }
        return node;
    }

    @Override
    protected void updateLegend() {
        this.legend.getItems().clear();
        if (this.getData() != null) {
            for (int i2 = 0; i2 < this.getData().size(); ++i2) {
                XYChart.Series series = (XYChart.Series)this.getData().get(i2);
                Legend.LegendItem legendItem = new Legend.LegendItem(series.getName());
                legendItem.getSymbol().getStyleClass().addAll("chart-line-symbol", "series" + i2, series.defaultColorStyleClass);
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

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return LineChart.getClassCssMetaData();
    }

    public static enum SortingPolicy {
        NONE,
        X_AXIS,
        Y_AXIS;

    }

    private static class StyleableProperties {
        private static final CssMetaData<LineChart<?, ?>, Boolean> CREATE_SYMBOLS = new CssMetaData<LineChart<?, ?>, Boolean>("-fx-create-symbols", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(LineChart<?, ?> lineChart) {
                return ((LineChart)lineChart).createSymbols == null || !((LineChart)lineChart).createSymbols.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(LineChart<?, ?> lineChart) {
                return (StyleableProperty)((Object)lineChart.createSymbolsProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(XYChart.getClassCssMetaData());
            arrayList.add(CREATE_SYMBOLS);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

