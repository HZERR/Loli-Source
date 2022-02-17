/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.binding.DoubleExpression;
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
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;

public class StackedAreaChart<X, Y>
extends XYChart<X, Y> {
    private Map<XYChart.Series<X, Y>, DoubleProperty> seriesYMultiplierMap = new HashMap<XYChart.Series<X, Y>, DoubleProperty>();
    private Legend legend = new Legend();
    private BooleanProperty createSymbols = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            for (int i2 = 0; i2 < StackedAreaChart.this.getData().size(); ++i2) {
                XYChart.Series series = (XYChart.Series)StackedAreaChart.this.getData().get(i2);
                for (int i3 = 0; i3 < series.getData().size(); ++i3) {
                    XYChart.Data data = (XYChart.Data)series.getData().get(i3);
                    Node node = data.getNode();
                    if (this.get() && node == null) {
                        node = StackedAreaChart.this.createSymbol(series, StackedAreaChart.this.getData().indexOf(series), data, i3);
                        if (null == node) continue;
                        StackedAreaChart.this.getPlotChildren().add(node);
                        continue;
                    }
                    if (this.get() || node == null) continue;
                    StackedAreaChart.this.getPlotChildren().remove(node);
                    node = null;
                    data.setNode(null);
                }
            }
            StackedAreaChart.this.requestChartLayout();
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
        public CssMetaData<StackedAreaChart<?, ?>, Boolean> getCssMetaData() {
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

    public StackedAreaChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2) {
        this(axis, axis2, FXCollections.observableArrayList());
    }

    public StackedAreaChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2, @NamedArg(value="data") ObservableList<XYChart.Series<X, Y>> observableList) {
        super(axis, axis2);
        if (!(axis2 instanceof ValueAxis)) {
            throw new IllegalArgumentException("Axis type incorrect, yAxis must be of ValueAxis type.");
        }
        this.setLegend(this.legend);
        this.setData(observableList);
    }

    private static double doubleValue(Number number) {
        return StackedAreaChart.doubleValue(number, 0.0);
    }

    private static double doubleValue(Number number, double d2) {
        return number == null ? d2 : number.doubleValue();
    }

    @Override
    protected void dataItemAdded(XYChart.Series<X, Y> series, int n2, XYChart.Data<X, Y> data) {
        Node node = this.createSymbol(series, this.getData().indexOf(series), data, n2);
        if (this.shouldAnimate()) {
            boolean bl = false;
            if (n2 > 0 && n2 < series.getData().size() - 1) {
                bl = true;
                XYChart.Data data2 = (XYChart.Data)series.getData().get(n2 - 1);
                XYChart.Data data3 = (XYChart.Data)series.getData().get(n2 + 1);
                double d2 = this.getXAxis().toNumericValue(data2.getXValue());
                double d3 = this.getYAxis().toNumericValue(data2.getYValue());
                double d4 = this.getXAxis().toNumericValue(data3.getXValue());
                double d5 = this.getYAxis().toNumericValue(data3.getYValue());
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
                    node.setOpacity(1.0);
                });
                fadeTransition.play();
            }
            if (bl) {
                this.animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY()), new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(800.0), actionEvent -> {
                    this.getPlotChildren().remove(node);
                    this.removeDataItemFromDisplay(series, data);
                }, new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
            }
        } else {
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
        path2.setStrokeLineJoin(StrokeLineJoin.BEVEL);
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

    @Override
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
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
    protected void updateAxisRange() {
        Axis axis = this.getXAxis();
        Axis axis2 = this.getYAxis();
        if (axis.isAutoRanging()) {
            ArrayList arrayList = new ArrayList();
            for (XYChart.Series object : this.getData()) {
                for (XYChart.Data data : object.getData()) {
                    arrayList.add(data.getXValue());
                }
            }
            axis.invalidateRange(arrayList);
        }
        if (axis2.isAutoRanging()) {
            double d2 = Double.MAX_VALUE;
            Iterator iterator = this.getDisplayedSeriesIterator();
            boolean bl = true;
            TreeMap<Double, Double> treeMap = new TreeMap<Double, Double>();
            TreeMap<Double, Double> treeMap2 = new TreeMap<Double, Double>();
            TreeMap<Double, Double> treeMap3 = new TreeMap<Double, Double>();
            while (iterator.hasNext()) {
                treeMap3.clear();
                XYChart.Series series = iterator.next();
                for (XYChart.Data data : series.getData()) {
                    if (data == null) continue;
                    double d3 = axis.toNumericValue(data.getXValue());
                    double d4 = axis2.toNumericValue(data.getYValue());
                    treeMap3.put(d3, d4);
                    if (bl) {
                        treeMap.put(d3, d4);
                        d2 = Math.min(d2, d4);
                        continue;
                    }
                    if (treeMap2.containsKey(d3)) {
                        treeMap.put(d3, (Double)treeMap2.get(d3) + d4);
                        continue;
                    }
                    Map.Entry entry = treeMap2.higherEntry(d3);
                    Map.Entry entry2 = treeMap2.lowerEntry(d3);
                    if (entry != null && entry2 != null) {
                        treeMap.put(d3, (d3 - entry2.getKey()) / (entry.getKey() - entry2.getKey()) * ((Double)entry2.getValue() + (Double)entry.getValue()) + d4);
                        continue;
                    }
                    if (entry != null) {
                        treeMap.put(d3, (Double)entry.getValue() + d4);
                        continue;
                    }
                    if (entry2 != null) {
                        treeMap.put(d3, (Double)entry2.getValue() + d4);
                        continue;
                    }
                    treeMap.put(d3, d4);
                }
                for (Map.Entry entry : treeMap2.entrySet()) {
                    if (treeMap.keySet().contains(entry.getKey())) continue;
                    Double d5 = (Double)entry.getKey();
                    Double d6 = (Double)entry.getValue();
                    Map.Entry entry3 = treeMap3.higherEntry(d5);
                    Map.Entry entry4 = treeMap3.lowerEntry(d5);
                    if (entry3 != null && entry4 != null) {
                        treeMap.put(d5, (d5 - entry4.getKey()) / (entry3.getKey() - entry4.getKey()) * ((Double)entry4.getValue() + (Double)entry3.getValue()) + d6);
                        continue;
                    }
                    if (entry3 != null) {
                        treeMap.put(d5, (Double)entry3.getValue() + d6);
                        continue;
                    }
                    if (entry4 != null) {
                        treeMap.put(d5, (Double)entry4.getValue() + d6);
                        continue;
                    }
                    treeMap.put(d5, d6);
                }
                treeMap2.clear();
                treeMap2.putAll(treeMap);
                treeMap.clear();
                bl = d2 == Double.MAX_VALUE;
            }
            if (d2 != Double.MAX_VALUE) {
                axis2.invalidateRange(Arrays.asList(axis2.toRealValue(d2), axis2.toRealValue((Double)Collections.max(treeMap2.values()))));
            }
        }
    }

    @Override
    protected void layoutPlotChildren() {
        ArrayList<DataPointInfo<X, Y>> arrayList = new ArrayList<DataPointInfo<X, Y>>();
        ArrayList<DataPointInfo<X, Y>> arrayList2 = new ArrayList<DataPointInfo<X, Y>>();
        for (int i2 = 0; i2 < this.getDataSize(); ++i2) {
            Object object;
            Object object2;
            XYChart.Series series = (XYChart.Series)this.getData().get(i2);
            arrayList2.clear();
            for (DataPointInfo dataPointInfo : arrayList) {
                dataPointInfo.partOf = PartOf.PREVIOUS;
                arrayList2.add(dataPointInfo);
            }
            arrayList.clear();
            Object object4 = this.getDisplayedDataIterator(series);
            while (object4.hasNext()) {
                XYChart.Data data = (XYChart.Data)object4.next();
                object2 = new DataPointInfo(data, data.getXValue(), data.getYValue(), PartOf.CURRENT);
                arrayList2.add((DataPointInfo<X, Y>)object2);
            }
            object4 = this.seriesYMultiplierMap.get(series);
            Path object32 = (Path)((Group)series.getNode()).getChildren().get(1);
            object2 = (Path)((Group)series.getNode()).getChildren().get(0);
            object32.getElements().clear();
            ((Path)object2).getElements().clear();
            int n2 = 0;
            this.sortAggregateList(arrayList2);
            Axis axis = this.getYAxis();
            Axis axis2 = this.getXAxis();
            boolean bl = false;
            boolean bl2 = false;
            int n3 = this.findNextCurrent(arrayList2, -1);
            int n4 = this.findPreviousCurrent(arrayList2, arrayList2.size());
            double d2 = axis.getZeroPosition();
            if (Double.isNaN(d2)) {
                ValueAxis valueAxis = (ValueAxis)axis;
                d2 = valueAxis.getLowerBound() > 0.0 ? valueAxis.getDisplayPosition(valueAxis.getLowerBound()) : valueAxis.getDisplayPosition(valueAxis.getUpperBound());
            }
            for (DataPointInfo<X, Y> dataPointInfo : arrayList2) {
                double d3;
                DataPointInfo<X, Y> dataPointInfo2;
                DataPointInfo<X, Y> dataPointInfo3;
                double d4;
                double d5;
                int n5;
                int n6;
                if (n2 == n4) {
                    bl2 = true;
                }
                if (n2 == n3) {
                    bl = true;
                }
                object = dataPointInfo.dataItem;
                if (dataPointInfo.partOf.equals((Object)PartOf.CURRENT)) {
                    n6 = this.findPreviousPrevious(arrayList2, n2);
                    n5 = this.findNextPrevious(arrayList2, n2);
                    if (n6 == -1 || n5 == -1 && !arrayList2.get((int)n6).x.equals(dataPointInfo.x)) {
                        if (bl) {
                            XYChart.Data data = new XYChart.Data(dataPointInfo.x, 0);
                            this.addDropDown(arrayList, data, data.getXValue(), data.getYValue(), axis2.getDisplayPosition(data.getCurrentX()), d2);
                        }
                        d5 = axis2.getDisplayPosition(((XYChart.Data)object).getCurrentX());
                        d4 = axis.getDisplayPosition(axis.toRealValue(axis.toNumericValue(((XYChart.Data)object).getCurrentY()) * ((DoubleExpression)object4).getValue()));
                        this.addPoint(arrayList, (XYChart.Data<X, Y>)object, ((XYChart.Data)object).getXValue(), ((XYChart.Data)object).getYValue(), d5, d4, PartOf.CURRENT, false, !bl);
                        if (n2 == n4) {
                            XYChart.Data data = new XYChart.Data(dataPointInfo.x, 0);
                            this.addDropDown(arrayList, data, data.getXValue(), data.getYValue(), axis2.getDisplayPosition(data.getCurrentX()), d2);
                        }
                    } else {
                        dataPointInfo3 = arrayList2.get(n6);
                        if (dataPointInfo3.x.equals(dataPointInfo.x)) {
                            if (dataPointInfo3.dropDown) {
                                n6 = this.findPreviousPrevious(arrayList2, n6);
                                dataPointInfo3 = arrayList2.get(n6);
                            }
                            if (dataPointInfo3.x.equals(dataPointInfo.x)) {
                                d5 = axis2.getDisplayPosition(((XYChart.Data)object).getCurrentX());
                                d4 = axis.toNumericValue(((XYChart.Data)object).getCurrentY()) + axis.toNumericValue(dataPointInfo3.y);
                                double d6 = axis.getDisplayPosition(axis.toRealValue(d4 * ((DoubleExpression)object4).getValue()));
                                this.addPoint(arrayList, (XYChart.Data<X, Y>)object, dataPointInfo.x, axis.toRealValue(d4), d5, d6, PartOf.CURRENT, false, !bl);
                            }
                            if (bl2) {
                                this.addDropDown(arrayList, (XYChart.Data<X, Y>)object, dataPointInfo3.x, dataPointInfo3.y, dataPointInfo3.displayX, dataPointInfo3.displayY);
                            }
                        } else {
                            dataPointInfo2 = n5 == -1 ? null : arrayList2.get(n5);
                            dataPointInfo3 = n6 == -1 ? null : arrayList2.get(n6);
                            d5 = axis.toNumericValue(((XYChart.Data)object).getCurrentY());
                            if (dataPointInfo3 != null && dataPointInfo2 != null) {
                                d4 = axis2.getDisplayPosition(((XYChart.Data)object).getCurrentX());
                                double d7 = this.interpolate(dataPointInfo3.displayX, dataPointInfo3.displayY, dataPointInfo2.displayX, dataPointInfo2.displayY, d4);
                                d3 = this.interpolate(axis2.toNumericValue(dataPointInfo3.x), axis.toNumericValue(dataPointInfo3.y), axis2.toNumericValue(dataPointInfo2.x), axis.toNumericValue(dataPointInfo2.y), axis2.toNumericValue(dataPointInfo.x));
                                if (bl) {
                                    XYChart.Data data = new XYChart.Data(dataPointInfo.x, d3);
                                    this.addDropDown(arrayList, data, dataPointInfo.x, axis.toRealValue(d3), d4, d7);
                                }
                                double d8 = axis.getDisplayPosition(axis.toRealValue((d5 + d3) * ((DoubleExpression)object4).getValue()));
                                this.addPoint(arrayList, (XYChart.Data<X, Y>)object, dataPointInfo.x, axis.toRealValue(d5 + d3), d4, d8, PartOf.CURRENT, false, !bl);
                                if (n2 == n4) {
                                    XYChart.Data data = new XYChart.Data(dataPointInfo.x, d3);
                                    this.addDropDown(arrayList, data, dataPointInfo.x, axis.toRealValue(d3), d4, d7);
                                }
                            }
                        }
                    }
                } else {
                    n6 = this.findPreviousCurrent(arrayList2, n2);
                    n5 = this.findNextCurrent(arrayList2, n2);
                    if (dataPointInfo.dropDown) {
                        if (axis2.toNumericValue(dataPointInfo.x) <= axis2.toNumericValue(arrayList2.get((int)n3).x) || axis2.toNumericValue(dataPointInfo.x) > axis2.toNumericValue(arrayList2.get((int)n4).x)) {
                            this.addDropDown(arrayList, (XYChart.Data<X, Y>)object, dataPointInfo.x, dataPointInfo.y, dataPointInfo.displayX, dataPointInfo.displayY);
                        }
                    } else if (n6 == -1 || n5 == -1) {
                        this.addPoint(arrayList, (XYChart.Data<X, Y>)object, dataPointInfo.x, dataPointInfo.y, dataPointInfo.displayX, dataPointInfo.displayY, PartOf.CURRENT, true, false);
                    } else {
                        dataPointInfo2 = arrayList2.get(n5);
                        if (!dataPointInfo2.x.equals(dataPointInfo.x)) {
                            dataPointInfo3 = arrayList2.get(n6);
                            d5 = axis2.getDisplayPosition(((XYChart.Data)object).getCurrentX());
                            d4 = this.interpolate(axis2.toNumericValue(dataPointInfo3.x), axis.toNumericValue(dataPointInfo3.y), axis2.toNumericValue(dataPointInfo2.x), axis.toNumericValue(dataPointInfo2.y), axis2.toNumericValue(dataPointInfo.x));
                            double d9 = axis.toNumericValue(dataPointInfo.y) + d4;
                            d3 = axis.getDisplayPosition(axis.toRealValue(d9 * ((DoubleExpression)object4).getValue()));
                            this.addPoint(arrayList, new XYChart.Data(dataPointInfo.x, d4), dataPointInfo.x, axis.toRealValue(d9), d5, d3, PartOf.CURRENT, true, true);
                        }
                    }
                }
                ++n2;
                if (bl) {
                    bl = false;
                }
                if (!bl2) continue;
                bl2 = false;
            }
            if (!arrayList.isEmpty()) {
                object32.getElements().add(new MoveTo(((DataPointInfo)arrayList.get((int)0)).displayX, ((DataPointInfo)arrayList.get((int)0)).displayY));
                ((Path)object2).getElements().add(new MoveTo(((DataPointInfo)arrayList.get((int)0)).displayX, ((DataPointInfo)arrayList.get((int)0)).displayY));
            }
            for (DataPointInfo<X, Y> dataPointInfo : arrayList) {
                if (dataPointInfo.lineTo) {
                    object32.getElements().add(new LineTo(dataPointInfo.displayX, dataPointInfo.displayY));
                } else {
                    object32.getElements().add(new MoveTo(dataPointInfo.displayX, dataPointInfo.displayY));
                }
                ((Path)object2).getElements().add(new LineTo(dataPointInfo.displayX, dataPointInfo.displayY));
                if (dataPointInfo.skipSymbol || (object = dataPointInfo.dataItem.getNode()) == null) continue;
                double d10 = ((Node)object).prefWidth(-1.0);
                double d11 = ((Node)object).prefHeight(-1.0);
                ((Node)object).resizeRelocate(dataPointInfo.displayX - d10 / 2.0, dataPointInfo.displayY - d11 / 2.0, d10, d11);
            }
            for (int i3 = arrayList2.size() - 1; i3 > 0; --i3) {
                DataPointInfo<X, Y> dataPointInfo;
                dataPointInfo = arrayList2.get(i3);
                if (!PartOf.PREVIOUS.equals((Object)dataPointInfo.partOf)) continue;
                ((Path)object2).getElements().add(new LineTo(dataPointInfo.displayX, dataPointInfo.displayY));
            }
            if (((Path)object2).getElements().isEmpty()) continue;
            ((Path)object2).getElements().add(new ClosePath());
        }
    }

    private void addDropDown(ArrayList<DataPointInfo<X, Y>> arrayList, XYChart.Data<X, Y> data, X x2, Y y2, double d2, double d3) {
        DataPointInfo<X, Y> dataPointInfo = new DataPointInfo<X, Y>(true);
        dataPointInfo.setValues(data, x2, y2, d2, d3, PartOf.CURRENT, true, false);
        arrayList.add(dataPointInfo);
    }

    private void addPoint(ArrayList<DataPointInfo<X, Y>> arrayList, XYChart.Data<X, Y> data, X x2, Y y2, double d2, double d3, PartOf partOf, boolean bl, boolean bl2) {
        DataPointInfo<X, Y> dataPointInfo = new DataPointInfo<X, Y>();
        dataPointInfo.setValues(data, x2, y2, d2, d3, partOf, bl, bl2);
        arrayList.add(dataPointInfo);
    }

    private int findNextCurrent(ArrayList<DataPointInfo<X, Y>> arrayList, int n2) {
        for (int i2 = n2 + 1; i2 < arrayList.size(); ++i2) {
            if (!arrayList.get((int)i2).partOf.equals((Object)PartOf.CURRENT)) continue;
            return i2;
        }
        return -1;
    }

    private int findPreviousCurrent(ArrayList<DataPointInfo<X, Y>> arrayList, int n2) {
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            if (!arrayList.get((int)i2).partOf.equals((Object)PartOf.CURRENT)) continue;
            return i2;
        }
        return -1;
    }

    private int findPreviousPrevious(ArrayList<DataPointInfo<X, Y>> arrayList, int n2) {
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            if (!arrayList.get((int)i2).partOf.equals((Object)PartOf.PREVIOUS)) continue;
            return i2;
        }
        return -1;
    }

    private int findNextPrevious(ArrayList<DataPointInfo<X, Y>> arrayList, int n2) {
        for (int i2 = n2 + 1; i2 < arrayList.size(); ++i2) {
            if (!arrayList.get((int)i2).partOf.equals((Object)PartOf.PREVIOUS)) continue;
            return i2;
        }
        return -1;
    }

    private void sortAggregateList(ArrayList<DataPointInfo<X, Y>> arrayList) {
        Collections.sort(arrayList, (dataPointInfo, dataPointInfo2) -> {
            double d2;
            XYChart.Data data = dataPointInfo.dataItem;
            XYChart.Data data2 = dataPointInfo2.dataItem;
            double d3 = this.getXAxis().toNumericValue(data.getXValue());
            return d3 < (d2 = this.getXAxis().toNumericValue(data2.getXValue())) ? -1 : (d3 == d2 ? 0 : 1);
        });
    }

    private double interpolate(double d2, double d3, double d4, double d5, double d6) {
        return (d5 - d3) / (d4 - d2) * (d6 - d2) + d3;
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
        return StackedAreaChart.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<StackedAreaChart<?, ?>, Boolean> CREATE_SYMBOLS = new CssMetaData<StackedAreaChart<?, ?>, Boolean>("-fx-create-symbols", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(StackedAreaChart<?, ?> stackedAreaChart) {
                return ((StackedAreaChart)stackedAreaChart).createSymbols == null || !((StackedAreaChart)stackedAreaChart).createSymbols.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(StackedAreaChart<?, ?> stackedAreaChart) {
                return (StyleableProperty)((Object)stackedAreaChart.createSymbolsProperty());
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

    private static enum PartOf {
        CURRENT,
        PREVIOUS;

    }

    static final class DataPointInfo<X, Y> {
        X x;
        Y y;
        double displayX;
        double displayY;
        XYChart.Data<X, Y> dataItem;
        PartOf partOf;
        boolean skipSymbol = false;
        boolean lineTo = false;
        boolean dropDown = false;

        DataPointInfo() {
        }

        DataPointInfo(XYChart.Data<X, Y> data, X x2, Y y2, PartOf partOf) {
            this.dataItem = data;
            this.x = x2;
            this.y = y2;
            this.partOf = partOf;
        }

        DataPointInfo(boolean bl) {
            this.dropDown = bl;
        }

        void setValues(XYChart.Data<X, Y> data, X x2, Y y2, double d2, double d3, PartOf partOf, boolean bl, boolean bl2) {
            this.dataItem = data;
            this.x = x2;
            this.y = y2;
            this.displayX = d2;
            this.displayY = d3;
            this.partOf = partOf;
            this.skipSymbol = bl;
            this.lineTo = bl2;
        }

        public final X getX() {
            return this.x;
        }

        public final Y getY() {
            return this.y;
        }
    }
}

