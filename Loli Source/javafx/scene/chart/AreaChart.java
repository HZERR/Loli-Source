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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;

public class AreaChart<X, Y>
extends XYChart<X, Y> {
    private Map<XYChart.Series<X, Y>, DoubleProperty> seriesYMultiplierMap = new HashMap<XYChart.Series<X, Y>, DoubleProperty>();
    private Legend legend = new Legend();
    private BooleanProperty createSymbols = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            for (int i2 = 0; i2 < AreaChart.this.getData().size(); ++i2) {
                XYChart.Series series = (XYChart.Series)AreaChart.this.getData().get(i2);
                for (int i3 = 0; i3 < series.getData().size(); ++i3) {
                    XYChart.Data data = (XYChart.Data)series.getData().get(i3);
                    Node node = data.getNode();
                    if (this.get() && node == null) {
                        node = AreaChart.this.createSymbol(series, AreaChart.this.getData().indexOf(series), data, i3);
                        if (null == node) continue;
                        AreaChart.this.getPlotChildren().add(node);
                        continue;
                    }
                    if (this.get() || node == null) continue;
                    AreaChart.this.getPlotChildren().remove(node);
                    node = null;
                    data.setNode(null);
                }
            }
            AreaChart.this.requestChartLayout();
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "createSymbols";
        }

        @Override
        public CssMetaData<AreaChart<?, ?>, Boolean> getCssMetaData() {
            return StyleableProperties.CREATE_SYMBOLS;
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

    public AreaChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2) {
        this(axis, axis2, FXCollections.observableArrayList());
    }

    public AreaChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2, @NamedArg(value="data") ObservableList<XYChart.Series<X, Y>> observableList) {
        super(axis, axis2);
        this.setLegend(this.legend);
        this.setData(observableList);
    }

    private static double doubleValue(Number number) {
        return AreaChart.doubleValue(number, 0.0);
    }

    private static double doubleValue(Number number, double d2) {
        return number == null ? d2 : number.doubleValue();
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
            Object object;
            boolean bl = false;
            if (n2 > 0 && n2 < series.getData().size() - 1) {
                bl = true;
                object = (XYChart.Data)series.getData().get(n2 - 1);
                XYChart.Data data2 = (XYChart.Data)series.getData().get(n2 + 1);
                double d2 = this.getXAxis().toNumericValue(((XYChart.Data)object).getXValue());
                double d3 = this.getYAxis().toNumericValue(((XYChart.Data)object).getYValue());
                double d4 = this.getXAxis().toNumericValue(data2.getXValue());
                double d5 = this.getYAxis().toNumericValue(data2.getYValue());
                double d6 = this.getXAxis().toNumericValue(data.getXValue());
                double d7 = this.getYAxis().toNumericValue(data.getYValue());
                double d8 = (d5 - d3) / (d4 - d2) * d6 + (d4 * d3 - d5 * d2) / (d4 - d2);
                data.setCurrentY(this.getYAxis().toRealValue(d8));
                data.setCurrentX(this.getXAxis().toRealValue(d6));
            } else if (n2 == 0 && series.getData().size() > 1) {
                bl = true;
                data.setCurrentX(((XYChart.Data)series.getData().get(1)).getXValue());
                data.setCurrentY(((XYChart.Data)series.getData().get(1)).getYValue());
            } else if (n2 == series.getData().size() - 1 && series.getData().size() > 1) {
                bl = true;
                int n3 = series.getData().size() - 2;
                data.setCurrentX(((XYChart.Data)series.getData().get(n3)).getXValue());
                data.setCurrentY(((XYChart.Data)series.getData().get(n3)).getYValue());
            }
            if (node != null) {
                node.setOpacity(0.0);
                this.getPlotChildren().add(node);
                object = new FadeTransition(Duration.millis(500.0), node);
                ((FadeTransition)object).setToValue(1.0);
                ((Animation)object).play();
            }
            if (bl) {
                this.animate(new KeyFrame(Duration.ZERO, actionEvent -> {
                    if (node != null && !this.getPlotChildren().contains(node)) {
                        this.getPlotChildren().add(node);
                    }
                }, new KeyValue(data.currentYProperty(), data.getCurrentY()), new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(800.0), new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
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
                double d8 = (d5 - d3) / (d4 - d2) * d6 + (d4 * d3 - d5 * d2) / (d4 - d2);
                data.setCurrentX(this.getXAxis().toRealValue(d6));
                data.setCurrentY(this.getYAxis().toRealValue(d7));
                data.setXValue(this.getXAxis().toRealValue(d6));
                data.setYValue(this.getYAxis().toRealValue(d8));
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
                node.setOpacity(0.0);
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(500.0), node);
                fadeTransition.setToValue(0.0);
                fadeTransition.setOnFinished(actionEvent -> {
                    this.getPlotChildren().remove(node);
                    this.removeDataItemFromDisplay(series, data);
                });
                fadeTransition.play();
            }
            if (bl) {
                this.animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY()), new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(800.0), actionEvent -> {
                    data.setSeries(null);
                    this.getPlotChildren().remove(node);
                    this.removeDataItemFromDisplay(series, data);
                }, new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
            }
        } else {
            data.setSeries(null);
            this.getPlotChildren().remove(node);
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
            Path path = (Path)((Group)series.getNode()).getChildren().get(1);
            Path path2 = (Path)((Group)series.getNode()).getChildren().get(0);
            path.getStyleClass().setAll("chart-series-area-line", "series" + i2, series.defaultColorStyleClass);
            path2.getStyleClass().setAll("chart-series-area-fill", "series" + i2, series.defaultColorStyleClass);
            for (int i3 = 0; i3 < series.getData().size(); ++i3) {
                XYChart.Data data = (XYChart.Data)series.getData().get(i3);
                Node node = data.getNode();
                if (node == null) continue;
                node.getStyleClass().setAll("chart-area-symbol", "series" + i2, "data" + i3, series.defaultColorStyleClass);
            }
        }
    }

    @Override
    protected void seriesAdded(XYChart.Series<X, Y> series, int n2) {
        Path path = new Path();
        Path path2 = new Path();
        path.setStrokeLineJoin(StrokeLineJoin.BEVEL);
        Group group = new Group(path2, path);
        series.setNode(group);
        SimpleDoubleProperty simpleDoubleProperty = new SimpleDoubleProperty(this, "seriesYMultiplier");
        this.seriesYMultiplierMap.put(series, simpleDoubleProperty);
        if (this.shouldAnimate()) {
            simpleDoubleProperty.setValue(0.0);
        } else {
            simpleDoubleProperty.setValue(1.0);
        }
        this.getPlotChildren().add(group);
        ArrayList<KeyFrame> arrayList = new ArrayList<KeyFrame>();
        if (this.shouldAnimate()) {
            arrayList.add(new KeyFrame(Duration.ZERO, new KeyValue(group.opacityProperty(), 0), new KeyValue(simpleDoubleProperty, 0)));
            arrayList.add(new KeyFrame(Duration.millis(200.0), new KeyValue(group.opacityProperty(), 1)));
            arrayList.add(new KeyFrame(Duration.millis(500.0), new KeyValue(simpleDoubleProperty, 1)));
        }
        for (int i2 = 0; i2 < series.getData().size(); ++i2) {
            XYChart.Data data = (XYChart.Data)series.getData().get(i2);
            Node node = this.createSymbol(series, n2, data, i2);
            if (node == null) continue;
            if (this.shouldAnimate()) {
                node.setOpacity(0.0);
                this.getPlotChildren().add(node);
                arrayList.add(new KeyFrame(Duration.ZERO, new KeyValue(node.opacityProperty(), 0)));
                arrayList.add(new KeyFrame(Duration.millis(200.0), new KeyValue(node.opacityProperty(), 1)));
                continue;
            }
            this.getPlotChildren().add(node);
        }
        if (this.shouldAnimate()) {
            this.animate(arrayList.toArray(new KeyFrame[arrayList.size()]));
        }
    }

    private void updateDefaultColorIndex(XYChart.Series<X, Y> series) {
        int n2 = (Integer)this.seriesColorMap.get(series);
        Path path = (Path)((Group)series.getNode()).getChildren().get(1);
        Path path2 = (Path)((Group)series.getNode()).getChildren().get(0);
        if (path != null) {
            path.getStyleClass().remove(DEFAULT_COLOR + n2);
        }
        if (path2 != null) {
            path2.getStyleClass().remove(DEFAULT_COLOR + n2);
        }
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
            Timeline timeline = new Timeline(this.createSeriesRemoveTimeLine(series, 400L));
            timeline.play();
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
            double d2 = 0.0;
            ObservableList<Node> observableList = ((Group)series.getNode()).getChildren();
            ObservableList<PathElement> observableList2 = ((Path)observableList.get(1)).getElements();
            ObservableList<PathElement> observableList3 = ((Path)observableList.get(0)).getElements();
            observableList2.clear();
            observableList3.clear();
            arrayList.clear();
            Object object = this.getDisplayedDataIterator(series);
            while (object.hasNext()) {
                XYChart.Data data = object.next();
                double d3 = this.getXAxis().getDisplayPosition(data.getCurrentX());
                double d4 = this.getYAxis().getDisplayPosition(this.getYAxis().toRealValue(this.getYAxis().toNumericValue(data.getCurrentY()) * doubleProperty.getValue()));
                arrayList.add(new LineTo(d3, d4));
                if (Double.isNaN(d3) || Double.isNaN(d4)) continue;
                d2 = d3;
                Node node = data.getNode();
                if (node == null) continue;
                double d5 = node.prefWidth(-1.0);
                double d6 = node.prefHeight(-1.0);
                node.resizeRelocate(d3 - d5 / 2.0, d4 - d6 / 2.0, d5, d6);
            }
            if (arrayList.isEmpty()) continue;
            Collections.sort(arrayList, (lineTo, lineTo2) -> Double.compare(lineTo.getX(), lineTo2.getX()));
            object = (LineTo)arrayList.get(0);
            double d7 = ((LineTo)object).getY();
            double d8 = this.getYAxis().toNumericValue(this.getYAxis().getValueForDisplay(d7));
            double d9 = this.getYAxis().getZeroPosition();
            boolean bl = !Double.isNaN(d9);
            double d10 = this.getYAxis().getHeight();
            double d11 = bl ? d9 : (d8 < 0.0 ? d8 - d10 : d10);
            observableList2.add(new MoveTo(((LineTo)object).getX(), d7));
            observableList3.add(new MoveTo(((LineTo)object).getX(), d11));
            observableList2.addAll((Collection<PathElement>)arrayList);
            observableList3.addAll((Collection<PathElement>)arrayList);
            observableList3.add(new LineTo(d2, d11));
            observableList3.add(new ClosePath());
        }
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
            node.getStyleClass().setAll("chart-area-symbol", "series" + n2, "data" + n3, series.defaultColorStyleClass);
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
                legendItem.getSymbol().getStyleClass().addAll("chart-area-symbol", "series" + i2, "area-legend-symbol", series.defaultColorStyleClass);
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
        return AreaChart.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<AreaChart<?, ?>, Boolean> CREATE_SYMBOLS = new CssMetaData<AreaChart<?, ?>, Boolean>("-fx-create-symbols", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(AreaChart<?, ?> areaChart) {
                return ((AreaChart)areaChart).createSymbols == null || !((AreaChart)areaChart).createSymbols.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(AreaChart<?, ?> areaChart) {
                return (StyleableProperty)((Object)areaChart.createSymbolsProperty());
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

