/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import com.sun.javafx.charts.ChartLayoutAnimator;
import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public abstract class Chart
extends Region {
    private static final int MIN_WIDTH_TO_LEAVE_FOR_CHART_CONTENT = 200;
    private static final int MIN_HEIGHT_TO_LEAVE_FOR_CHART_CONTENT = 150;
    private final Label titleLabel = new Label();
    private final Pane chartContent = new Pane(){

        @Override
        protected void layoutChildren() {
            double d2 = this.snappedTopInset();
            double d3 = this.snappedLeftInset();
            double d4 = this.snappedBottomInset();
            double d5 = this.snappedRightInset();
            double d6 = this.getWidth();
            double d7 = this.getHeight();
            double d8 = this.snapSize(d6 - (d3 + d5));
            double d9 = this.snapSize(d7 - (d2 + d4));
            Chart.this.layoutChartChildren(this.snapPosition(d2), this.snapPosition(d3), d8, d9);
        }

        @Override
        public boolean usesMirroring() {
            return Chart.this.useChartContentMirroring;
        }
    };
    boolean useChartContentMirroring = true;
    private final ChartLayoutAnimator animator = new ChartLayoutAnimator(this.chartContent);
    private StringProperty title = new StringPropertyBase(){

        @Override
        protected void invalidated() {
            Chart.this.titleLabel.setText(this.get());
        }

        @Override
        public Object getBean() {
            return Chart.this;
        }

        @Override
        public String getName() {
            return "title";
        }
    };
    private ObjectProperty<Side> titleSide = new StyleableObjectProperty<Side>(Side.TOP){

        @Override
        protected void invalidated() {
            Chart.this.requestLayout();
        }

        @Override
        public CssMetaData<Chart, Side> getCssMetaData() {
            return StyleableProperties.TITLE_SIDE;
        }

        @Override
        public Object getBean() {
            return Chart.this;
        }

        @Override
        public String getName() {
            return "titleSide";
        }
    };
    private final ObjectProperty<Node> legend = new ObjectPropertyBase<Node>(){
        private Node old = null;

        @Override
        protected void invalidated() {
            Node node = (Node)this.get();
            if (this.old != null) {
                Chart.this.getChildren().remove(this.old);
            }
            if (node != null) {
                Chart.this.getChildren().add(node);
                node.setVisible(Chart.this.isLegendVisible());
            }
            this.old = node;
        }

        @Override
        public Object getBean() {
            return Chart.this;
        }

        @Override
        public String getName() {
            return "legend";
        }
    };
    private final BooleanProperty legendVisible = new StyleableBooleanProperty(true){

        @Override
        protected void invalidated() {
            Chart.this.requestLayout();
        }

        @Override
        public CssMetaData<Chart, Boolean> getCssMetaData() {
            return StyleableProperties.LEGEND_VISIBLE;
        }

        @Override
        public Object getBean() {
            return Chart.this;
        }

        @Override
        public String getName() {
            return "legendVisible";
        }
    };
    private ObjectProperty<Side> legendSide = new StyleableObjectProperty<Side>(Side.BOTTOM){

        @Override
        protected void invalidated() {
            Side side = (Side)((Object)this.get());
            Node node = Chart.this.getLegend();
            if (node instanceof Legend) {
                ((Legend)node).setVertical(Side.LEFT.equals((Object)side) || Side.RIGHT.equals((Object)side));
            }
            Chart.this.requestLayout();
        }

        @Override
        public CssMetaData<Chart, Side> getCssMetaData() {
            return StyleableProperties.LEGEND_SIDE;
        }

        @Override
        public Object getBean() {
            return Chart.this;
        }

        @Override
        public String getName() {
            return "legendSide";
        }
    };
    private BooleanProperty animated = new SimpleBooleanProperty(this, "animated", true);

    public final String getTitle() {
        return (String)this.title.get();
    }

    public final void setTitle(String string) {
        this.title.set(string);
    }

    public final StringProperty titleProperty() {
        return this.title;
    }

    public final Side getTitleSide() {
        return (Side)((Object)this.titleSide.get());
    }

    public final void setTitleSide(Side side) {
        this.titleSide.set(side);
    }

    public final ObjectProperty<Side> titleSideProperty() {
        return this.titleSide;
    }

    protected final Node getLegend() {
        return (Node)this.legend.getValue();
    }

    protected final void setLegend(Node node) {
        this.legend.setValue(node);
    }

    protected final ObjectProperty<Node> legendProperty() {
        return this.legend;
    }

    public final boolean isLegendVisible() {
        return this.legendVisible.getValue();
    }

    public final void setLegendVisible(boolean bl) {
        this.legendVisible.setValue(bl);
    }

    public final BooleanProperty legendVisibleProperty() {
        return this.legendVisible;
    }

    public final Side getLegendSide() {
        return (Side)((Object)this.legendSide.get());
    }

    public final void setLegendSide(Side side) {
        this.legendSide.set(side);
    }

    public final ObjectProperty<Side> legendSideProperty() {
        return this.legendSide;
    }

    public final boolean getAnimated() {
        return this.animated.get();
    }

    public final void setAnimated(boolean bl) {
        this.animated.set(bl);
    }

    public final BooleanProperty animatedProperty() {
        return this.animated;
    }

    protected ObservableList<Node> getChartChildren() {
        return this.chartContent.getChildren();
    }

    public Chart() {
        this.titleLabel.setAlignment(Pos.CENTER);
        this.titleLabel.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
        this.getChildren().addAll(this.titleLabel, this.chartContent);
        this.getStyleClass().add("chart");
        this.titleLabel.getStyleClass().add("chart-title");
        this.chartContent.getStyleClass().add("chart-content");
        this.chartContent.setManaged(false);
    }

    void animate(KeyFrame ... arrkeyFrame) {
        this.animator.animate(arrkeyFrame);
    }

    protected void animate(Animation animation) {
        this.animator.animate(animation);
    }

    protected void requestChartLayout() {
        this.chartContent.requestLayout();
    }

    protected final boolean shouldAnimate() {
        return this.getAnimated() && this.impl_isTreeVisible() && this.getScene() != null;
    }

    protected abstract void layoutChartChildren(double var1, double var3, double var5, double var7);

    @Override
    protected void layoutChildren() {
        double d2 = this.snappedTopInset();
        double d3 = this.snappedLeftInset();
        double d4 = this.snappedBottomInset();
        double d5 = this.snappedRightInset();
        double d6 = this.getWidth();
        double d7 = this.getHeight();
        if (this.getTitle() != null) {
            double d8;
            this.titleLabel.setVisible(true);
            if (this.getTitleSide().equals((Object)Side.TOP)) {
                d8 = this.snapSize(this.titleLabel.prefHeight(d6 - d3 - d5));
                this.titleLabel.resizeRelocate(d3, d2, d6 - d3 - d5, d8);
                d2 += d8;
            } else if (this.getTitleSide().equals((Object)Side.BOTTOM)) {
                d8 = this.snapSize(this.titleLabel.prefHeight(d6 - d3 - d5));
                this.titleLabel.resizeRelocate(d3, d7 - d4 - d8, d6 - d3 - d5, d8);
                d4 += d8;
            } else if (this.getTitleSide().equals((Object)Side.LEFT)) {
                d8 = this.snapSize(this.titleLabel.prefWidth(d7 - d2 - d4));
                this.titleLabel.resizeRelocate(d3, d2, d8, d7 - d2 - d4);
                d3 += d8;
            } else if (this.getTitleSide().equals((Object)Side.RIGHT)) {
                d8 = this.snapSize(this.titleLabel.prefWidth(d7 - d2 - d4));
                this.titleLabel.resizeRelocate(d6 - d5 - d8, d2, d8, d7 - d2 - d4);
                d5 += d8;
            }
        } else {
            this.titleLabel.setVisible(false);
        }
        Node node = this.getLegend();
        if (node != null) {
            boolean bl = this.isLegendVisible();
            if (bl) {
                if (this.getLegendSide() == Side.TOP) {
                    double d9 = this.snapSize(node.prefHeight(d6 - d3 - d5));
                    double d10 = Utils.boundedSize(this.snapSize(node.prefWidth(d9)), 0.0, d6 - d3 - d5);
                    node.resizeRelocate(d3 + (d6 - d3 - d5 - d10) / 2.0, d2, d10, d9);
                    if (d7 - d4 - d2 - d9 < 150.0) {
                        bl = false;
                    } else {
                        d2 += d9;
                    }
                } else if (this.getLegendSide() == Side.BOTTOM) {
                    double d11 = this.snapSize(node.prefHeight(d6 - d3 - d5));
                    double d12 = Utils.boundedSize(this.snapSize(node.prefWidth(d11)), 0.0, d6 - d3 - d5);
                    node.resizeRelocate(d3 + (d6 - d3 - d5 - d12) / 2.0, d7 - d4 - d11, d12, d11);
                    if (d7 - d4 - d2 - d11 < 150.0) {
                        bl = false;
                    } else {
                        d4 += d11;
                    }
                } else if (this.getLegendSide() == Side.LEFT) {
                    double d13 = this.snapSize(node.prefWidth(d7 - d2 - d4));
                    double d14 = Utils.boundedSize(this.snapSize(node.prefHeight(d13)), 0.0, d7 - d2 - d4);
                    node.resizeRelocate(d3, d2 + (d7 - d2 - d4 - d14) / 2.0, d13, d14);
                    if (d6 - d3 - d5 - d13 < 200.0) {
                        bl = false;
                    } else {
                        d3 += d13;
                    }
                } else if (this.getLegendSide() == Side.RIGHT) {
                    double d15 = this.snapSize(node.prefWidth(d7 - d2 - d4));
                    double d16 = Utils.boundedSize(this.snapSize(node.prefHeight(d15)), 0.0, d7 - d2 - d4);
                    node.resizeRelocate(d6 - d5 - d15, d2 + (d7 - d2 - d4 - d16) / 2.0, d15, d16);
                    if (d6 - d3 - d5 - d15 < 200.0) {
                        bl = false;
                    } else {
                        d5 += d15;
                    }
                }
            }
            node.setVisible(bl);
        }
        this.chartContent.resizeRelocate(d3, d2, d6 - d3 - d5, d7 - d2 - d4);
    }

    @Override
    protected double computeMinHeight(double d2) {
        return 150.0;
    }

    @Override
    protected double computeMinWidth(double d2) {
        return 200.0;
    }

    @Override
    protected double computePrefWidth(double d2) {
        return 500.0;
    }

    @Override
    protected double computePrefHeight(double d2) {
        return 400.0;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return Chart.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<Chart, Side> TITLE_SIDE = new CssMetaData<Chart, Side>("-fx-title-side", new EnumConverter<Side>(Side.class), Side.TOP){

            @Override
            public boolean isSettable(Chart chart) {
                return chart.titleSide == null || !chart.titleSide.isBound();
            }

            @Override
            public StyleableProperty<Side> getStyleableProperty(Chart chart) {
                return (StyleableProperty)((Object)chart.titleSideProperty());
            }
        };
        private static final CssMetaData<Chart, Side> LEGEND_SIDE = new CssMetaData<Chart, Side>("-fx-legend-side", new EnumConverter<Side>(Side.class), Side.BOTTOM){

            @Override
            public boolean isSettable(Chart chart) {
                return chart.legendSide == null || !chart.legendSide.isBound();
            }

            @Override
            public StyleableProperty<Side> getStyleableProperty(Chart chart) {
                return (StyleableProperty)((Object)chart.legendSideProperty());
            }
        };
        private static final CssMetaData<Chart, Boolean> LEGEND_VISIBLE = new CssMetaData<Chart, Boolean>("-fx-legend-visible", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(Chart chart) {
                return chart.legendVisible == null || !chart.legendVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Chart chart) {
                return (StyleableProperty)((Object)chart.legendVisibleProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Region.getClassCssMetaData());
            arrayList.add(TITLE_SIDE);
            arrayList.add(LEGEND_VISIBLE);
            arrayList.add(LEGEND_SIDE);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

