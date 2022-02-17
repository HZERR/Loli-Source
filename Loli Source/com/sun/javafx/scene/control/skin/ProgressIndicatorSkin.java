/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class ProgressIndicatorSkin
extends BehaviorSkinBase<ProgressIndicator, BehaviorBase<ProgressIndicator>> {
    private ObjectProperty<Paint> progressColor = new StyleableObjectProperty<Paint>(null){

        @Override
        protected void invalidated() {
            Paint paint = (Paint)this.get();
            if (paint != null && !(paint instanceof Color)) {
                if (this.isBound()) {
                    this.unbind();
                }
                this.set(null);
                throw new IllegalArgumentException("Only Color objects are supported");
            }
            if (ProgressIndicatorSkin.this.spinner != null) {
                ProgressIndicatorSkin.this.spinner.setFillOverride(paint);
            }
            if (ProgressIndicatorSkin.this.determinateIndicator != null) {
                ProgressIndicatorSkin.this.determinateIndicator.setFillOverride(paint);
            }
        }

        @Override
        public Object getBean() {
            return ProgressIndicatorSkin.this;
        }

        @Override
        public String getName() {
            return "progressColorProperty";
        }

        @Override
        public CssMetaData<ProgressIndicator, Paint> getCssMetaData() {
            return PROGRESS_COLOR;
        }
    };
    private IntegerProperty indeterminateSegmentCount = new StyleableIntegerProperty(8){

        @Override
        protected void invalidated() {
            if (ProgressIndicatorSkin.this.spinner != null) {
                ProgressIndicatorSkin.this.spinner.rebuild();
            }
        }

        @Override
        public Object getBean() {
            return ProgressIndicatorSkin.this;
        }

        @Override
        public String getName() {
            return "indeterminateSegmentCount";
        }

        @Override
        public CssMetaData<ProgressIndicator, Number> getCssMetaData() {
            return INDETERMINATE_SEGMENT_COUNT;
        }
    };
    private final BooleanProperty spinEnabled = new StyleableBooleanProperty(false){

        @Override
        protected void invalidated() {
            if (ProgressIndicatorSkin.this.spinner != null) {
                ProgressIndicatorSkin.this.spinner.setSpinEnabled(this.get());
            }
        }

        @Override
        public CssMetaData<ProgressIndicator, Boolean> getCssMetaData() {
            return SPIN_ENABLED;
        }

        @Override
        public Object getBean() {
            return ProgressIndicatorSkin.this;
        }

        @Override
        public String getName() {
            return "spinEnabled";
        }
    };
    private static final String DONE = ControlResources.getString("ProgressIndicator.doneString");
    private static final Text doneText = new Text(DONE);
    private IndeterminateSpinner spinner;
    private DeterminateIndicator determinateIndicator;
    private ProgressIndicator control;
    protected Animation indeterminateTransition;
    protected final Duration CLIPPED_DELAY = new Duration(300.0);
    protected final Duration UNCLIPPED_DELAY = new Duration(0.0);
    private static final CssMetaData<ProgressIndicator, Paint> PROGRESS_COLOR;
    private static final CssMetaData<ProgressIndicator, Number> INDETERMINATE_SEGMENT_COUNT;
    private static final CssMetaData<ProgressIndicator, Boolean> SPIN_ENABLED;
    public static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    Paint getProgressColor() {
        return (Paint)this.progressColor.get();
    }

    public ProgressIndicatorSkin(ProgressIndicator progressIndicator) {
        super(progressIndicator, new BehaviorBase<ProgressIndicator>(progressIndicator, Collections.emptyList()));
        this.control = progressIndicator;
        this.registerChangeListener(progressIndicator.indeterminateProperty(), "INDETERMINATE");
        this.registerChangeListener(progressIndicator.progressProperty(), "PROGRESS");
        this.registerChangeListener(progressIndicator.visibleProperty(), "VISIBLE");
        this.registerChangeListener(progressIndicator.parentProperty(), "PARENT");
        this.registerChangeListener(progressIndicator.sceneProperty(), "SCENE");
        this.initialize();
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("INDETERMINATE".equals(string)) {
            this.initialize();
        } else if ("PROGRESS".equals(string)) {
            this.updateProgress();
        } else if ("VISIBLE".equals(string)) {
            this.updateAnimation();
        } else if ("PARENT".equals(string)) {
            this.updateAnimation();
        } else if ("SCENE".equals(string)) {
            this.updateAnimation();
        }
    }

    protected void initialize() {
        boolean bl = this.control.isIndeterminate();
        if (bl) {
            this.determinateIndicator = null;
            this.spinner = new IndeterminateSpinner(this.spinEnabled.get(), (Paint)this.progressColor.get());
            this.getChildren().setAll(this.spinner);
            if (this.control.impl_isTreeVisible() && this.indeterminateTransition != null) {
                this.indeterminateTransition.play();
            }
        } else {
            if (this.spinner != null) {
                if (this.indeterminateTransition != null) {
                    this.indeterminateTransition.stop();
                }
                this.spinner = null;
            }
            this.determinateIndicator = new DeterminateIndicator(this.control, this, (Paint)this.progressColor.get());
            this.getChildren().setAll(this.determinateIndicator);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.indeterminateTransition != null) {
            this.indeterminateTransition.stop();
            this.indeterminateTransition = null;
        }
        if (this.spinner != null) {
            this.spinner = null;
        }
        this.control = null;
    }

    protected void updateProgress() {
        if (this.determinateIndicator != null) {
            this.determinateIndicator.updateProgress(this.control.getProgress());
        }
    }

    protected void createIndeterminateTimeline() {
        if (this.spinner != null) {
            this.spinner.rebuildTimeline();
        }
    }

    protected void pauseTimeline(boolean bl) {
        if (((ProgressIndicator)this.getSkinnable()).isIndeterminate()) {
            if (this.indeterminateTransition == null) {
                this.createIndeterminateTimeline();
            }
            if (bl) {
                this.indeterminateTransition.pause();
            } else {
                this.indeterminateTransition.play();
            }
        }
    }

    protected void updateAnimation() {
        boolean bl;
        ProgressIndicator progressIndicator = (ProgressIndicator)this.getSkinnable();
        boolean bl2 = bl = progressIndicator.isVisible() && progressIndicator.getParent() != null && progressIndicator.getScene() != null;
        if (this.indeterminateTransition != null) {
            this.pauseTimeline(!bl);
        } else if (bl) {
            this.createIndeterminateTimeline();
        }
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        if (this.spinner != null && this.control.isIndeterminate()) {
            this.spinner.layoutChildren();
            this.spinner.resizeRelocate(0.0, 0.0, d4, d5);
        } else if (this.determinateIndicator != null) {
            this.determinateIndicator.layoutChildren();
            this.determinateIndicator.resizeRelocate(0.0, 0.0, d4, d5);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return ProgressIndicatorSkin.getClassCssMetaData();
    }

    static {
        doneText.getStyleClass().add("text");
        PROGRESS_COLOR = new CssMetaData<ProgressIndicator, Paint>("-fx-progress-color", PaintConverter.getInstance(), null){

            @Override
            public boolean isSettable(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return progressIndicatorSkin.progressColor == null || !progressIndicatorSkin.progressColor.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return (StyleableProperty)((Object)progressIndicatorSkin.progressColor);
            }
        };
        INDETERMINATE_SEGMENT_COUNT = new CssMetaData<ProgressIndicator, Number>("-fx-indeterminate-segment-count", SizeConverter.getInstance(), (Number)8){

            @Override
            public boolean isSettable(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return progressIndicatorSkin.indeterminateSegmentCount == null || !progressIndicatorSkin.indeterminateSegmentCount.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return (StyleableProperty)((Object)progressIndicatorSkin.indeterminateSegmentCount);
            }
        };
        SPIN_ENABLED = new CssMetaData<ProgressIndicator, Boolean>("-fx-spin-enabled", BooleanConverter.getInstance(), Boolean.FALSE){

            @Override
            public boolean isSettable(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return progressIndicatorSkin.spinEnabled == null || !progressIndicatorSkin.spinEnabled.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return (StyleableProperty)((Object)progressIndicatorSkin.spinEnabled);
            }
        };
        ArrayList arrayList = new ArrayList(SkinBase.getClassCssMetaData());
        arrayList.add(PROGRESS_COLOR);
        arrayList.add(INDETERMINATE_SEGMENT_COUNT);
        arrayList.add(SPIN_ENABLED);
        STYLEABLES = Collections.unmodifiableList(arrayList);
    }

    private final class IndeterminateSpinner
    extends Region {
        private IndicatorPaths pathsG;
        private final List<Double> opacities = new ArrayList<Double>();
        private boolean spinEnabled = false;
        private Paint fillOverride = null;

        private IndeterminateSpinner(boolean bl, Paint paint) {
            this.spinEnabled = bl;
            this.fillOverride = paint;
            this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            this.getStyleClass().setAll("spinner");
            this.pathsG = new IndicatorPaths();
            this.getChildren().add(this.pathsG);
            this.rebuild();
            this.rebuildTimeline();
        }

        public void setFillOverride(Paint paint) {
            this.fillOverride = paint;
            this.rebuild();
        }

        public void setSpinEnabled(boolean bl) {
            this.spinEnabled = bl;
            this.rebuildTimeline();
        }

        private void rebuildTimeline() {
            if (this.spinEnabled) {
                if (ProgressIndicatorSkin.this.indeterminateTransition == null) {
                    ProgressIndicatorSkin.this.indeterminateTransition = new Timeline();
                    ProgressIndicatorSkin.this.indeterminateTransition.setCycleCount(-1);
                    ProgressIndicatorSkin.this.indeterminateTransition.setDelay(ProgressIndicatorSkin.this.UNCLIPPED_DELAY);
                } else {
                    ProgressIndicatorSkin.this.indeterminateTransition.stop();
                    ((Timeline)ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().clear();
                }
                ObservableList<KeyFrame> observableList = FXCollections.observableArrayList();
                observableList.add(new KeyFrame(Duration.millis(1.0), new KeyValue(this.pathsG.rotateProperty(), 360)));
                observableList.add(new KeyFrame(Duration.millis(3900.0), new KeyValue(this.pathsG.rotateProperty(), 0)));
                for (int i2 = 100; i2 <= 3900; i2 += 100) {
                    observableList.add(new KeyFrame(Duration.millis(i2), actionEvent -> this.shiftColors(), new KeyValue[0]));
                }
                ((Timeline)ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().setAll((Collection<KeyFrame>)observableList);
                ProgressIndicatorSkin.this.indeterminateTransition.playFromStart();
            } else if (ProgressIndicatorSkin.this.indeterminateTransition != null) {
                ProgressIndicatorSkin.this.indeterminateTransition.stop();
                ((Timeline)ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().clear();
                ProgressIndicatorSkin.this.indeterminateTransition = null;
            }
        }

        @Override
        protected void layoutChildren() {
            double d2;
            double d3 = ProgressIndicatorSkin.this.control.getWidth() - ProgressIndicatorSkin.this.control.snappedLeftInset() - ProgressIndicatorSkin.this.control.snappedRightInset();
            double d4 = ProgressIndicatorSkin.this.control.getHeight() - ProgressIndicatorSkin.this.control.snappedTopInset() - ProgressIndicatorSkin.this.control.snappedBottomInset();
            double d5 = this.pathsG.prefWidth(-1.0);
            double d6 = this.pathsG.prefHeight(-1.0);
            double d7 = d2 = d3 / d5;
            if (d2 * d6 > d4) {
                d7 = d4 / d6;
            }
            double d8 = d5 * d7;
            double d9 = d6 * d7;
            this.pathsG.resizeRelocate((d3 - d8) / 2.0, (d4 - d9) / 2.0, d8, d9);
        }

        private void rebuild() {
            int n2 = ProgressIndicatorSkin.this.indeterminateSegmentCount.get();
            this.opacities.clear();
            this.pathsG.getChildren().clear();
            double d2 = 0.8 / (double)(n2 - 1);
            for (int i2 = 0; i2 < n2; ++i2) {
                Region region = new Region();
                region.setScaleShape(false);
                region.setCenterShape(false);
                region.getStyleClass().addAll("segment", "segment" + i2);
                if (this.fillOverride instanceof Color) {
                    Color color = (Color)this.fillOverride;
                    region.setStyle("-fx-background-color: rgba(" + (int)(255.0 * color.getRed()) + "," + (int)(255.0 * color.getGreen()) + "," + (int)(255.0 * color.getBlue()) + "," + color.getOpacity() + ");");
                } else {
                    region.setStyle(null);
                }
                this.pathsG.getChildren().add(region);
                this.opacities.add(Math.max(0.1, 1.0 - d2 * (double)i2));
            }
        }

        private void shiftColors() {
            if (this.opacities.size() <= 0) {
                return;
            }
            int n2 = ProgressIndicatorSkin.this.indeterminateSegmentCount.get();
            Collections.rotate(this.opacities, -1);
            for (int i2 = 0; i2 < n2; ++i2) {
                ((Node)this.pathsG.getChildren().get(i2)).setOpacity(this.opacities.get(i2));
            }
        }

        private class IndicatorPaths
        extends Pane {
            private IndicatorPaths() {
            }

            @Override
            protected double computePrefWidth(double d2) {
                double d3 = 0.0;
                for (Node node : this.getChildren()) {
                    if (!(node instanceof Region)) continue;
                    Region region = (Region)node;
                    if (region.getShape() != null) {
                        d3 = Math.max(d3, region.getShape().getLayoutBounds().getMaxX());
                        continue;
                    }
                    d3 = Math.max(d3, region.prefWidth(d2));
                }
                return d3;
            }

            @Override
            protected double computePrefHeight(double d2) {
                double d3 = 0.0;
                for (Node node : this.getChildren()) {
                    if (!(node instanceof Region)) continue;
                    Region region = (Region)node;
                    if (region.getShape() != null) {
                        d3 = Math.max(d3, region.getShape().getLayoutBounds().getMaxY());
                        continue;
                    }
                    d3 = Math.max(d3, region.prefHeight(d2));
                }
                return d3;
            }

            @Override
            protected void layoutChildren() {
                double d2 = this.getWidth() / this.computePrefWidth(-1.0);
                for (Node node : this.getChildren()) {
                    if (!(node instanceof Region)) continue;
                    Region region = (Region)node;
                    if (region.getShape() != null) {
                        region.resize(region.getShape().getLayoutBounds().getMaxX(), region.getShape().getLayoutBounds().getMaxY());
                        region.getTransforms().setAll(new Scale(d2, d2, 0.0, 0.0));
                        continue;
                    }
                    region.autosize();
                }
            }
        }
    }

    private class DeterminateIndicator
    extends Region {
        private double textGap = 2.0;
        private int intProgress;
        private int degProgress;
        private Text text;
        private StackPane indicator;
        private StackPane progress;
        private StackPane tick;
        private Arc arcShape;
        private Circle indicatorCircle;

        public DeterminateIndicator(ProgressIndicator progressIndicator, ProgressIndicatorSkin progressIndicatorSkin2, Paint paint) {
            this.getStyleClass().add("determinate-indicator");
            this.intProgress = (int)Math.round(progressIndicator.getProgress() * 100.0);
            this.degProgress = (int)(360.0 * progressIndicator.getProgress());
            this.getChildren().clear();
            this.text = new Text(progressIndicator.getProgress() >= 1.0 ? DONE : "" + this.intProgress + "%");
            this.text.setTextOrigin(VPos.TOP);
            this.text.getStyleClass().setAll("text", "percentage");
            this.indicator = new StackPane();
            this.indicator.setScaleShape(false);
            this.indicator.setCenterShape(false);
            this.indicator.getStyleClass().setAll("indicator");
            this.indicatorCircle = new Circle();
            this.indicator.setShape(this.indicatorCircle);
            this.arcShape = new Arc();
            this.arcShape.setType(ArcType.ROUND);
            this.arcShape.setStartAngle(90.0);
            this.progress = new StackPane();
            this.progress.getStyleClass().setAll("progress");
            this.progress.setScaleShape(false);
            this.progress.setCenterShape(false);
            this.progress.setShape(this.arcShape);
            this.progress.getChildren().clear();
            this.setFillOverride(paint);
            this.tick = new StackPane();
            this.tick.getStyleClass().setAll("tick");
            this.getChildren().setAll(this.indicator, this.progress, this.text, this.tick);
            this.updateProgress(progressIndicator.getProgress());
        }

        private void setFillOverride(Paint paint) {
            if (paint instanceof Color) {
                Color color = (Color)paint;
                this.progress.setStyle("-fx-background-color: rgba(" + (int)(255.0 * color.getRed()) + "," + (int)(255.0 * color.getGreen()) + "," + (int)(255.0 * color.getBlue()) + "," + color.getOpacity() + ");");
            } else {
                this.progress.setStyle(null);
            }
        }

        @Override
        public boolean usesMirroring() {
            return false;
        }

        private void updateProgress(double d2) {
            this.intProgress = (int)Math.round(d2 * 100.0);
            this.text.setText(d2 >= 1.0 ? DONE : "" + this.intProgress + "%");
            this.degProgress = (int)(360.0 * d2);
            this.arcShape.setLength(-this.degProgress);
            this.requestLayout();
        }

        @Override
        protected void layoutChildren() {
            double d2 = doneText.getLayoutBounds().getHeight();
            double d3 = ProgressIndicatorSkin.this.control.snappedLeftInset();
            double d4 = ProgressIndicatorSkin.this.control.snappedRightInset();
            double d5 = ProgressIndicatorSkin.this.control.snappedTopInset();
            double d6 = ProgressIndicatorSkin.this.control.snappedBottomInset();
            double d7 = ProgressIndicatorSkin.this.control.getWidth() - d3 - d4;
            double d8 = ProgressIndicatorSkin.this.control.getHeight() - d5 - d6 - this.textGap - d2;
            double d9 = d7 / 2.0;
            double d10 = d8 / 2.0;
            double d11 = Math.floor(Math.min(d9, d10));
            double d12 = this.snapPosition(d3 + d9);
            double d13 = this.snapPosition(d5 + d11);
            double d14 = this.indicator.snappedLeftInset();
            double d15 = this.indicator.snappedRightInset();
            double d16 = this.indicator.snappedTopInset();
            double d17 = this.indicator.snappedBottomInset();
            double d18 = this.snapSize(Math.min(Math.min(d11 - d14, d11 - d15), Math.min(d11 - d16, d11 - d17)));
            this.indicatorCircle.setRadius(d11);
            this.indicator.setLayoutX(d12);
            this.indicator.setLayoutY(d13);
            this.arcShape.setRadiusX(d18);
            this.arcShape.setRadiusY(d18);
            this.progress.setLayoutX(d12);
            this.progress.setLayoutY(d13);
            double d19 = this.progress.snappedLeftInset();
            double d20 = this.progress.snappedRightInset();
            double d21 = this.progress.snappedTopInset();
            double d22 = this.progress.snappedBottomInset();
            double d23 = this.snapSize(Math.min(Math.min(d18 - d19, d18 - d20), Math.min(d18 - d21, d18 - d22)));
            double d24 = Math.ceil(Math.sqrt(d23 * d23 / 2.0));
            double d25 = d23 * (Math.sqrt(2.0) / 2.0);
            this.tick.setLayoutX(d12 - d24);
            this.tick.setLayoutY(d13 - d24);
            this.tick.resize(d24 + d24, d24 + d24);
            this.tick.setVisible(ProgressIndicatorSkin.this.control.getProgress() >= 1.0);
            double d26 = this.text.getLayoutBounds().getWidth();
            double d27 = this.text.getLayoutBounds().getHeight();
            if (ProgressIndicatorSkin.this.control.getWidth() >= d26 && ProgressIndicatorSkin.this.control.getHeight() >= d27) {
                if (!this.text.isVisible()) {
                    this.text.setVisible(true);
                }
                this.text.setLayoutY(this.snapPosition(d13 + d11 + this.textGap));
                this.text.setLayoutX(this.snapPosition(d12 - d26 / 2.0));
            } else if (this.text.isVisible()) {
                this.text.setVisible(false);
            }
        }

        @Override
        protected double computePrefWidth(double d2) {
            double d3 = ProgressIndicatorSkin.this.control.snappedLeftInset();
            double d4 = ProgressIndicatorSkin.this.control.snappedRightInset();
            double d5 = this.indicator.snappedLeftInset();
            double d6 = this.indicator.snappedRightInset();
            double d7 = this.indicator.snappedTopInset();
            double d8 = this.indicator.snappedBottomInset();
            double d9 = this.snapSize(Math.max(Math.max(d5, d6), Math.max(d7, d8)));
            double d10 = this.progress.snappedLeftInset();
            double d11 = this.progress.snappedRightInset();
            double d12 = this.progress.snappedTopInset();
            double d13 = this.progress.snappedBottomInset();
            double d14 = this.snapSize(Math.max(Math.max(d10, d11), Math.max(d12, d13)));
            double d15 = this.tick.snappedLeftInset();
            double d16 = this.tick.snappedRightInset();
            double d17 = d9 + d14 + d15 + d16 + d14 + d9;
            return d3 + Math.max(d17, doneText.getLayoutBounds().getWidth()) + d4;
        }

        @Override
        protected double computePrefHeight(double d2) {
            double d3 = ProgressIndicatorSkin.this.control.snappedTopInset();
            double d4 = ProgressIndicatorSkin.this.control.snappedBottomInset();
            double d5 = this.indicator.snappedLeftInset();
            double d6 = this.indicator.snappedRightInset();
            double d7 = this.indicator.snappedTopInset();
            double d8 = this.indicator.snappedBottomInset();
            double d9 = this.snapSize(Math.max(Math.max(d5, d6), Math.max(d7, d8)));
            double d10 = this.progress.snappedLeftInset();
            double d11 = this.progress.snappedRightInset();
            double d12 = this.progress.snappedTopInset();
            double d13 = this.progress.snappedBottomInset();
            double d14 = this.snapSize(Math.max(Math.max(d10, d11), Math.max(d12, d13)));
            double d15 = this.tick.snappedTopInset();
            double d16 = this.tick.snappedBottomInset();
            double d17 = d9 + d14 + d15 + d16 + d14 + d9;
            return d3 + d17 + this.textGap + doneText.getLayoutBounds().getHeight() + d4;
        }

        @Override
        protected double computeMaxWidth(double d2) {
            return this.computePrefWidth(d2);
        }

        @Override
        protected double computeMaxHeight(double d2) {
            return this.computePrefHeight(d2);
        }
    }
}

