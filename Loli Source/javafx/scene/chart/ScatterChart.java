/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import java.util.Collection;
import java.util.Iterator;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ScatterChart<X, Y>
extends XYChart<X, Y> {
    private Legend legend = new Legend();

    public ScatterChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2) {
        this(axis, axis2, FXCollections.observableArrayList());
    }

    public ScatterChart(@NamedArg(value="xAxis") Axis<X> axis, @NamedArg(value="yAxis") Axis<Y> axis2, @NamedArg(value="data") ObservableList<XYChart.Series<X, Y>> observableList) {
        super(axis, axis2);
        this.setLegend(this.legend);
        this.setData(observableList);
    }

    @Override
    protected void dataItemAdded(XYChart.Series<X, Y> series, int n2, XYChart.Data<X, Y> data) {
        Node node = data.getNode();
        if (node == null) {
            node = new StackPane();
            node.setAccessibleRole(AccessibleRole.TEXT);
            node.setAccessibleRoleDescription("Point");
            node.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
            data.setNode(node);
        }
        node.getStyleClass().setAll("chart-symbol", "series" + this.getData().indexOf(series), "data" + n2, series.defaultColorStyleClass);
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
        if (node != null) {
            node.focusTraversableProperty().unbind();
        }
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
            this.dataItemAdded(series, i2, (XYChart.Data)series.getData().get(i2));
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

    @Override
    protected void layoutPlotChildren() {
        for (int i2 = 0; i2 < this.getDataSize(); ++i2) {
            XYChart.Series series = (XYChart.Series)this.getData().get(i2);
            Iterator iterator = this.getDisplayedDataIterator(series);
            while (iterator.hasNext()) {
                Node node;
                XYChart.Data data = iterator.next();
                double d2 = this.getXAxis().getDisplayPosition(data.getCurrentX());
                double d3 = this.getYAxis().getDisplayPosition(data.getCurrentY());
                if (Double.isNaN(d2) || Double.isNaN(d3) || (node = data.getNode()) == null) continue;
                double d4 = node.prefWidth(-1.0);
                double d5 = node.prefHeight(-1.0);
                node.resizeRelocate(d2 - d4 / 2.0, d3 - d5 / 2.0, d4, d5);
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
                if (!series.getData().isEmpty() && ((XYChart.Data)series.getData().get(0)).getNode() != null) {
                    legendItem.getSymbol().getStyleClass().addAll((Collection<String>)((XYChart.Data)series.getData().get(0)).getNode().getStyleClass());
                }
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

