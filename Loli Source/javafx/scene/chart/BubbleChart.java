/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;

public class BubbleChart<X, Y>
extends XYChart<X, Y> {
    private Legend legend = new Legend();

    public BubbleChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2) {
        this(axis, axis2, FXCollections.observableArrayList());
    }

    public BubbleChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2, @NamedArg(value="data") ObservableList<XYChart.Series<X, Y>> observableList) {
        super(axis, axis2);
        this.setLegend(this.legend);
        if (!(axis instanceof ValueAxis) || !(axis2 instanceof ValueAxis)) {
            throw new IllegalArgumentException("Axis type incorrect, X and Y should both be NumberAxis");
        }
        this.setData(observableList);
    }

    private static double getDoubleValue(Object object, double d2) {
        return !(object instanceof Number) ? d2 : ((Number)object).doubleValue();
    }

    @Override
    protected void layoutPlotChildren() {
        for (int i2 = 0; i2 < this.getDataSize(); ++i2) {
            XYChart.Series series = (XYChart.Series)this.getData().get(i2);
            Iterator iterator = this.getDisplayedDataIterator(series);
            while (iterator.hasNext()) {
                Ellipse ellipse;
                Node node;
                XYChart.Data data = iterator.next();
                double d2 = this.getXAxis().getDisplayPosition(data.getCurrentX());
                double d3 = this.getYAxis().getDisplayPosition(data.getCurrentY());
                if (Double.isNaN(d2) || Double.isNaN(d3) || (node = data.getNode()) == null || !(node instanceof StackPane)) continue;
                StackPane stackPane = (StackPane)data.getNode();
                if (stackPane.getShape() == null) {
                    ellipse = new Ellipse(BubbleChart.getDoubleValue(data.getExtraValue(), 1.0), BubbleChart.getDoubleValue(data.getExtraValue(), 1.0));
                } else if (stackPane.getShape() instanceof Ellipse) {
                    ellipse = (Ellipse)stackPane.getShape();
                } else {
                    return;
                }
                ellipse.setRadiusX(BubbleChart.getDoubleValue(data.getExtraValue(), 1.0) * (this.getXAxis() instanceof NumberAxis ? Math.abs(((NumberAxis)this.getXAxis()).getScale()) : 1.0));
                ellipse.setRadiusY(BubbleChart.getDoubleValue(data.getExtraValue(), 1.0) * (this.getYAxis() instanceof NumberAxis ? Math.abs(((NumberAxis)this.getYAxis()).getScale()) : 1.0));
                stackPane.setShape(null);
                stackPane.setShape(ellipse);
                stackPane.setScaleShape(false);
                stackPane.setCenterShape(false);
                stackPane.setCacheShape(false);
                node.setLayoutX(d2);
                node.setLayoutY(d3);
            }
        }
    }

    @Override
    protected void dataItemAdded(XYChart.Series<X, Y> series, int n2, XYChart.Data<X, Y> data) {
        Node node = this.createBubble(series, this.getData().indexOf(series), data, n2);
        if (this.shouldAnimate()) {
            node.setOpacity(0.0);
            this.getPlotChildren().add(node);
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500.0), node);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();
        } else {
            this.getPlotChildren().add(node);
        }
    }

    @Override
    protected void dataItemRemoved(XYChart.Data<X, Y> data, XYChart.Series<X, Y> series) {
        Node node = data.getNode();
        if (this.shouldAnimate()) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500.0), node);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished(actionEvent -> {
                this.getPlotChildren().remove(node);
                this.removeDataItemFromDisplay(series, data);
                node.setOpacity(1.0);
            });
            fadeTransition.play();
        } else {
            this.getPlotChildren().remove(node);
            this.removeDataItemFromDisplay(series, data);
        }
    }

    @Override
    protected void dataItemChanged(XYChart.Data<X, Y> data) {
    }

    @Override
    protected void seriesAdded(XYChart.Series<X, Y> series, int n2) {
        for (int i2 = 0; i2 < series.getData().size(); ++i2) {
            XYChart.Data data = (XYChart.Data)series.getData().get(i2);
            Node node = this.createBubble(series, n2, data, i2);
            if (this.shouldAnimate()) {
                node.setOpacity(0.0);
                this.getPlotChildren().add(node);
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(500.0), node);
                fadeTransition.setToValue(1.0);
                fadeTransition.play();
                continue;
            }
            this.getPlotChildren().add(node);
        }
    }

    @Override
    protected void seriesRemoved(XYChart.Series<X, Y> series) {
        if (this.shouldAnimate()) {
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.setOnFinished(actionEvent -> this.removeSeriesFromDisplay(series));
            for (XYChart.Data data : series.getData()) {
                Node node = data.getNode();
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(500.0), node);
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
        }
    }

    private Node createBubble(XYChart.Series<X, Y> series, int n2, XYChart.Data<X, Y> data, int n3) {
        Node node = data.getNode();
        if (node == null) {
            node = new StackPane();
            data.setNode(node);
        }
        node.getStyleClass().setAll("chart-bubble", "series" + n2, "data" + n3, series.defaultColorStyleClass);
        return node;
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
        boolean bl = axis instanceof CategoryAxis;
        boolean bl2 = axis2 instanceof CategoryAxis;
        if (arrayList != null || arrayList2 != null) {
            for (XYChart.Series series : this.getData()) {
                for (XYChart.Data data : series.getData()) {
                    if (arrayList != null) {
                        if (bl) {
                            arrayList.add(data.getXValue());
                        } else {
                            arrayList.add(axis.toRealValue(axis.toNumericValue(data.getXValue()) + BubbleChart.getDoubleValue(data.getExtraValue(), 0.0)));
                            arrayList.add(axis.toRealValue(axis.toNumericValue(data.getXValue()) - BubbleChart.getDoubleValue(data.getExtraValue(), 0.0)));
                        }
                    }
                    if (arrayList2 == null) continue;
                    if (bl2) {
                        arrayList2.add(data.getYValue());
                        continue;
                    }
                    arrayList2.add(axis2.toRealValue(axis2.toNumericValue(data.getYValue()) + BubbleChart.getDoubleValue(data.getExtraValue(), 0.0)));
                    arrayList2.add(axis2.toRealValue(axis2.toNumericValue(data.getYValue()) - BubbleChart.getDoubleValue(data.getExtraValue(), 0.0)));
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

    @Override
    protected void updateLegend() {
        this.legend.getItems().clear();
        if (this.getData() != null) {
            for (int i2 = 0; i2 < this.getData().size(); ++i2) {
                XYChart.Series series = (XYChart.Series)this.getData().get(i2);
                Legend.LegendItem legendItem = new Legend.LegendItem(series.getName());
                legendItem.getSymbol().getStyleClass().addAll("series" + i2, "chart-bubble", "bubble-legend-symbol", series.defaultColorStyleClass);
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
}

