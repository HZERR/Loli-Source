/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SliderBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.animation.Transition;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class SliderSkin
extends BehaviorSkinBase<Slider, SliderBehavior> {
    private NumberAxis tickLine = null;
    private double trackToTickGap = 2.0;
    private boolean showTickMarks;
    private double thumbWidth;
    private double thumbHeight;
    private double trackStart;
    private double trackLength;
    private double thumbTop;
    private double thumbLeft;
    private double preDragThumbPos;
    private Point2D dragStart;
    private StackPane thumb;
    private StackPane track;
    private boolean trackClicked = false;
    StringConverter<Number> stringConverterWrapper = new StringConverter<Number>(){
        Slider slider;
        {
            this.slider = (Slider)SliderSkin.this.getSkinnable();
        }

        @Override
        public String toString(Number number) {
            return number != null ? this.slider.getLabelFormatter().toString(number.doubleValue()) : "";
        }

        @Override
        public Number fromString(String string) {
            return this.slider.getLabelFormatter().fromString(string);
        }
    };

    public SliderSkin(Slider slider) {
        super(slider, new SliderBehavior(slider));
        this.initialize();
        slider.requestLayout();
        this.registerChangeListener(slider.minProperty(), "MIN");
        this.registerChangeListener(slider.maxProperty(), "MAX");
        this.registerChangeListener(slider.valueProperty(), "VALUE");
        this.registerChangeListener(slider.orientationProperty(), "ORIENTATION");
        this.registerChangeListener(slider.showTickMarksProperty(), "SHOW_TICK_MARKS");
        this.registerChangeListener(slider.showTickLabelsProperty(), "SHOW_TICK_LABELS");
        this.registerChangeListener(slider.majorTickUnitProperty(), "MAJOR_TICK_UNIT");
        this.registerChangeListener(slider.minorTickCountProperty(), "MINOR_TICK_COUNT");
        this.registerChangeListener(slider.labelFormatterProperty(), "TICK_LABEL_FORMATTER");
        this.registerChangeListener(slider.snapToTicksProperty(), "SNAP_TO_TICKS");
    }

    private void initialize() {
        this.thumb = new StackPane(){

            @Override
            public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
                switch (accessibleAttribute) {
                    case VALUE: {
                        return ((Slider)SliderSkin.this.getSkinnable()).getValue();
                    }
                }
                return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
            }
        };
        this.thumb.getStyleClass().setAll("thumb");
        this.thumb.setAccessibleRole(AccessibleRole.THUMB);
        this.track = new StackPane();
        this.track.getStyleClass().setAll("track");
        this.getChildren().clear();
        this.getChildren().addAll(this.track, this.thumb);
        this.setShowTickMarks(((Slider)this.getSkinnable()).isShowTickMarks(), ((Slider)this.getSkinnable()).isShowTickLabels());
        this.track.setOnMousePressed(mouseEvent -> {
            if (!this.thumb.isPressed()) {
                this.trackClicked = true;
                if (((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
                    ((SliderBehavior)this.getBehavior()).trackPress((MouseEvent)mouseEvent, mouseEvent.getX() / this.trackLength);
                } else {
                    ((SliderBehavior)this.getBehavior()).trackPress((MouseEvent)mouseEvent, mouseEvent.getY() / this.trackLength);
                }
                this.trackClicked = false;
            }
        });
        this.track.setOnMouseDragged(mouseEvent -> {
            if (!this.thumb.isPressed()) {
                if (((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
                    ((SliderBehavior)this.getBehavior()).trackPress((MouseEvent)mouseEvent, mouseEvent.getX() / this.trackLength);
                } else {
                    ((SliderBehavior)this.getBehavior()).trackPress((MouseEvent)mouseEvent, mouseEvent.getY() / this.trackLength);
                }
            }
        });
        this.thumb.setOnMousePressed(mouseEvent -> {
            ((SliderBehavior)this.getBehavior()).thumbPressed((MouseEvent)mouseEvent, 0.0);
            this.dragStart = this.thumb.localToParent(mouseEvent.getX(), mouseEvent.getY());
            this.preDragThumbPos = (((Slider)this.getSkinnable()).getValue() - ((Slider)this.getSkinnable()).getMin()) / (((Slider)this.getSkinnable()).getMax() - ((Slider)this.getSkinnable()).getMin());
        });
        this.thumb.setOnMouseReleased(mouseEvent -> ((SliderBehavior)this.getBehavior()).thumbReleased((MouseEvent)mouseEvent));
        this.thumb.setOnMouseDragged(mouseEvent -> {
            Point2D point2D = this.thumb.localToParent(mouseEvent.getX(), mouseEvent.getY());
            double d2 = ((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL ? point2D.getX() - this.dragStart.getX() : -(point2D.getY() - this.dragStart.getY());
            ((SliderBehavior)this.getBehavior()).thumbDragged((MouseEvent)mouseEvent, this.preDragThumbPos + d2 / this.trackLength);
        });
    }

    private void setShowTickMarks(boolean bl, boolean bl2) {
        this.showTickMarks = bl || bl2;
        Slider slider = (Slider)this.getSkinnable();
        if (this.showTickMarks) {
            if (this.tickLine == null) {
                this.tickLine = new NumberAxis();
                this.tickLine.setAutoRanging(false);
                this.tickLine.setSide(slider.getOrientation() == Orientation.VERTICAL ? Side.RIGHT : (slider.getOrientation() == null ? Side.RIGHT : Side.BOTTOM));
                this.tickLine.setUpperBound(slider.getMax());
                this.tickLine.setLowerBound(slider.getMin());
                this.tickLine.setTickUnit(slider.getMajorTickUnit());
                this.tickLine.setTickMarkVisible(bl);
                this.tickLine.setTickLabelsVisible(bl2);
                this.tickLine.setMinorTickVisible(bl);
                this.tickLine.setMinorTickCount(Math.max(slider.getMinorTickCount(), 0) + 1);
                if (slider.getLabelFormatter() != null) {
                    this.tickLine.setTickLabelFormatter(this.stringConverterWrapper);
                }
                this.getChildren().clear();
                this.getChildren().addAll(this.tickLine, this.track, this.thumb);
            } else {
                this.tickLine.setTickLabelsVisible(bl2);
                this.tickLine.setTickMarkVisible(bl);
                this.tickLine.setMinorTickVisible(bl);
            }
        } else {
            this.getChildren().clear();
            this.getChildren().addAll(this.track, this.thumb);
        }
        ((Slider)this.getSkinnable()).requestLayout();
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        Slider slider = (Slider)this.getSkinnable();
        if ("ORIENTATION".equals(string)) {
            if (this.showTickMarks && this.tickLine != null) {
                this.tickLine.setSide(slider.getOrientation() == Orientation.VERTICAL ? Side.RIGHT : (slider.getOrientation() == null ? Side.RIGHT : Side.BOTTOM));
            }
            ((Slider)this.getSkinnable()).requestLayout();
        } else if ("VALUE".equals(string)) {
            this.positionThumb(this.trackClicked);
        } else if ("MIN".equals(string)) {
            if (this.showTickMarks && this.tickLine != null) {
                this.tickLine.setLowerBound(slider.getMin());
            }
            ((Slider)this.getSkinnable()).requestLayout();
        } else if ("MAX".equals(string)) {
            if (this.showTickMarks && this.tickLine != null) {
                this.tickLine.setUpperBound(slider.getMax());
            }
            ((Slider)this.getSkinnable()).requestLayout();
        } else if ("SHOW_TICK_MARKS".equals(string) || "SHOW_TICK_LABELS".equals(string)) {
            this.setShowTickMarks(slider.isShowTickMarks(), slider.isShowTickLabels());
        } else if ("MAJOR_TICK_UNIT".equals(string)) {
            if (this.tickLine != null) {
                this.tickLine.setTickUnit(slider.getMajorTickUnit());
                ((Slider)this.getSkinnable()).requestLayout();
            }
        } else if ("MINOR_TICK_COUNT".equals(string)) {
            if (this.tickLine != null) {
                this.tickLine.setMinorTickCount(Math.max(slider.getMinorTickCount(), 0) + 1);
                ((Slider)this.getSkinnable()).requestLayout();
            }
        } else if ("TICK_LABEL_FORMATTER".equals(string)) {
            if (this.tickLine != null) {
                if (slider.getLabelFormatter() == null) {
                    this.tickLine.setTickLabelFormatter(null);
                } else {
                    this.tickLine.setTickLabelFormatter(this.stringConverterWrapper);
                    this.tickLine.requestAxisLayout();
                }
            }
        } else if ("SNAP_TO_TICKS".equals(string)) {
            slider.adjustValue(slider.getValue());
        }
    }

    void positionThumb(boolean bl) {
        double d2;
        Slider slider = (Slider)this.getSkinnable();
        if (slider.getValue() > slider.getMax()) {
            return;
        }
        boolean bl2 = slider.getOrientation() == Orientation.HORIZONTAL;
        final double d3 = bl2 ? this.trackStart + (this.trackLength * ((slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin())) - this.thumbWidth / 2.0) : this.thumbLeft;
        double d4 = d2 = bl2 ? this.thumbTop : this.snappedTopInset() + this.trackLength - this.trackLength * ((slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()));
        if (bl) {
            final double d5 = this.thumb.getLayoutX();
            final double d6 = this.thumb.getLayoutY();
            Transition transition = new Transition(){
                {
                    this.setCycleDuration(Duration.millis(200.0));
                }

                @Override
                protected void interpolate(double d22) {
                    if (!Double.isNaN(d5)) {
                        SliderSkin.this.thumb.setLayoutX(d5 + d22 * (d3 - d5));
                    }
                    if (!Double.isNaN(d6)) {
                        SliderSkin.this.thumb.setLayoutY(d6 + d22 * (d2 - d6));
                    }
                }
            };
            transition.play();
        } else {
            this.thumb.setLayoutX(d3);
            this.thumb.setLayoutY(d2);
        }
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6;
        this.thumbWidth = this.snapSize(this.thumb.prefWidth(-1.0));
        this.thumbHeight = this.snapSize(this.thumb.prefHeight(-1.0));
        this.thumb.resize(this.thumbWidth, this.thumbHeight);
        double d7 = this.track.getBackground() == null ? 0.0 : (d6 = this.track.getBackground().getFills().size() > 0 ? this.track.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius() : 0.0);
        if (((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
            double d8 = this.showTickMarks ? this.tickLine.prefHeight(-1.0) : 0.0;
            double d9 = this.snapSize(this.track.prefHeight(-1.0));
            double d10 = Math.max(d9, this.thumbHeight);
            double d11 = d10 + (this.showTickMarks ? this.trackToTickGap + d8 : 0.0);
            double d12 = d3 + (d5 - d11) / 2.0;
            this.trackLength = this.snapSize(d4 - this.thumbWidth);
            this.trackStart = this.snapPosition(d2 + this.thumbWidth / 2.0);
            double d13 = (int)(d12 + (d10 - d9) / 2.0);
            this.thumbTop = (int)(d12 + (d10 - this.thumbHeight) / 2.0);
            this.positionThumb(false);
            this.track.resizeRelocate((int)(this.trackStart - d6), d13, (int)(this.trackLength + d6 + d6), d9);
            if (this.showTickMarks) {
                this.tickLine.setLayoutX(this.trackStart);
                this.tickLine.setLayoutY(d13 + d9 + this.trackToTickGap);
                this.tickLine.resize(this.trackLength, d8);
                this.tickLine.requestAxisLayout();
            } else {
                if (this.tickLine != null) {
                    this.tickLine.resize(0.0, 0.0);
                    this.tickLine.requestAxisLayout();
                }
                this.tickLine = null;
            }
        } else {
            double d14 = this.showTickMarks ? this.tickLine.prefWidth(-1.0) : 0.0;
            double d15 = this.snapSize(this.track.prefWidth(-1.0));
            double d16 = Math.max(d15, this.thumbWidth);
            double d17 = d16 + (this.showTickMarks ? this.trackToTickGap + d14 : 0.0);
            double d18 = d2 + (d4 - d17) / 2.0;
            this.trackLength = this.snapSize(d5 - this.thumbHeight);
            this.trackStart = this.snapPosition(d3 + this.thumbHeight / 2.0);
            double d19 = (int)(d18 + (d16 - d15) / 2.0);
            this.thumbLeft = (int)(d18 + (d16 - this.thumbWidth) / 2.0);
            this.positionThumb(false);
            this.track.resizeRelocate(d19, (int)(this.trackStart - d6), d15, (int)(this.trackLength + d6 + d6));
            if (this.showTickMarks) {
                this.tickLine.setLayoutX(d19 + d15 + this.trackToTickGap);
                this.tickLine.setLayoutY(this.trackStart);
                this.tickLine.resize(d14, this.trackLength);
                this.tickLine.requestAxisLayout();
            } else {
                if (this.tickLine != null) {
                    this.tickLine.resize(0.0, 0.0);
                    this.tickLine.requestAxisLayout();
                }
                this.tickLine = null;
            }
        }
    }

    double minTrackLength() {
        return 2.0 * this.thumb.prefWidth(-1.0);
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        Slider slider = (Slider)this.getSkinnable();
        if (slider.getOrientation() == Orientation.HORIZONTAL) {
            return d6 + this.minTrackLength() + this.thumb.minWidth(-1.0) + d4;
        }
        return d6 + this.thumb.prefWidth(-1.0) + d4;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        Slider slider = (Slider)this.getSkinnable();
        if (slider.getOrientation() == Orientation.HORIZONTAL) {
            double d7 = this.showTickMarks ? this.tickLine.prefHeight(-1.0) + this.trackToTickGap : 0.0;
            return d3 + this.thumb.prefHeight(-1.0) + d7 + d5;
        }
        return d3 + this.minTrackLength() + this.thumb.prefHeight(-1.0) + d5;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        Slider slider = (Slider)this.getSkinnable();
        if (slider.getOrientation() == Orientation.HORIZONTAL) {
            if (this.showTickMarks) {
                return Math.max(140.0, this.tickLine.prefWidth(-1.0));
            }
            return 140.0;
        }
        double d7 = this.showTickMarks ? this.tickLine.prefWidth(-1.0) + this.trackToTickGap : 0.0;
        return d6 + Math.max(this.thumb.prefWidth(-1.0), this.track.prefWidth(-1.0)) + d7 + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        Slider slider = (Slider)this.getSkinnable();
        if (slider.getOrientation() == Orientation.HORIZONTAL) {
            return d3 + Math.max(this.thumb.prefHeight(-1.0), this.track.prefHeight(-1.0)) + (this.showTickMarks ? this.trackToTickGap + this.tickLine.prefHeight(-1.0) : 0.0) + d5;
        }
        if (this.showTickMarks) {
            return Math.max(140.0, this.tickLine.prefHeight(-1.0));
        }
        return 140.0;
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        if (((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
            return Double.MAX_VALUE;
        }
        return ((Slider)this.getSkinnable()).prefWidth(-1.0);
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        if (((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
            return ((Slider)this.getSkinnable()).prefHeight(d2);
        }
        return Double.MAX_VALUE;
    }
}

