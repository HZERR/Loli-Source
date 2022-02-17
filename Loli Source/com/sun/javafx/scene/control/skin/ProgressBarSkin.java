/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.ProgressIndicatorSkin;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Transition;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ProgressBarSkin
extends ProgressIndicatorSkin {
    private DoubleProperty indeterminateBarLength = null;
    private BooleanProperty indeterminateBarEscape = null;
    private BooleanProperty indeterminateBarFlip = null;
    private DoubleProperty indeterminateBarAnimationTime = null;
    private StackPane bar;
    private StackPane track;
    private Region clipRegion;
    private double barWidth;
    boolean wasIndeterminate = false;

    private DoubleProperty indeterminateBarLengthProperty() {
        if (this.indeterminateBarLength == null) {
            this.indeterminateBarLength = new StyleableDoubleProperty(60.0){

                @Override
                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                @Override
                public String getName() {
                    return "indeterminateBarLength";
                }

                @Override
                public CssMetaData<ProgressBar, Number> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_LENGTH;
                }
            };
        }
        return this.indeterminateBarLength;
    }

    private Double getIndeterminateBarLength() {
        return this.indeterminateBarLength == null ? 60.0 : this.indeterminateBarLength.get();
    }

    private BooleanProperty indeterminateBarEscapeProperty() {
        if (this.indeterminateBarEscape == null) {
            this.indeterminateBarEscape = new StyleableBooleanProperty(true){

                @Override
                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                @Override
                public String getName() {
                    return "indeterminateBarEscape";
                }

                @Override
                public CssMetaData<ProgressBar, Boolean> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_ESCAPE;
                }
            };
        }
        return this.indeterminateBarEscape;
    }

    private Boolean getIndeterminateBarEscape() {
        return this.indeterminateBarEscape == null ? true : this.indeterminateBarEscape.get();
    }

    private BooleanProperty indeterminateBarFlipProperty() {
        if (this.indeterminateBarFlip == null) {
            this.indeterminateBarFlip = new StyleableBooleanProperty(true){

                @Override
                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                @Override
                public String getName() {
                    return "indeterminateBarFlip";
                }

                @Override
                public CssMetaData<ProgressBar, Boolean> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_FLIP;
                }
            };
        }
        return this.indeterminateBarFlip;
    }

    private Boolean getIndeterminateBarFlip() {
        return this.indeterminateBarFlip == null ? true : this.indeterminateBarFlip.get();
    }

    private DoubleProperty indeterminateBarAnimationTimeProperty() {
        if (this.indeterminateBarAnimationTime == null) {
            this.indeterminateBarAnimationTime = new StyleableDoubleProperty(2.0){

                @Override
                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                @Override
                public String getName() {
                    return "indeterminateBarAnimationTime";
                }

                @Override
                public CssMetaData<ProgressBar, Number> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_ANIMATION_TIME;
                }
            };
        }
        return this.indeterminateBarAnimationTime;
    }

    private double getIndeterminateBarAnimationTime() {
        return this.indeterminateBarAnimationTime == null ? 2.0 : this.indeterminateBarAnimationTime.get();
    }

    public ProgressBarSkin(ProgressBar progressBar) {
        super(progressBar);
        this.barWidth = (double)((int)(progressBar.getWidth() - this.snappedLeftInset() - this.snappedRightInset()) * 2) * Math.min(1.0, Math.max(0.0, progressBar.getProgress())) / 2.0;
        InvalidationListener invalidationListener = observable -> this.updateProgress();
        progressBar.widthProperty().addListener(invalidationListener);
        this.initialize();
        ((ProgressIndicator)this.getSkinnable()).requestLayout();
    }

    @Override
    protected void initialize() {
        this.track = new StackPane();
        this.track.getStyleClass().setAll("track");
        this.bar = new StackPane();
        this.bar.getStyleClass().setAll("bar");
        this.getChildren().setAll(this.track, this.bar);
        this.clipRegion = new Region();
        this.bar.backgroundProperty().addListener((observableValue, background, background2) -> {
            if (background2 != null && !background2.getFills().isEmpty()) {
                BackgroundFill[] arrbackgroundFill = new BackgroundFill[background2.getFills().size()];
                for (int i2 = 0; i2 < background2.getFills().size(); ++i2) {
                    BackgroundFill backgroundFill = background2.getFills().get(i2);
                    arrbackgroundFill[i2] = new BackgroundFill(Color.BLACK, backgroundFill.getRadii(), backgroundFill.getInsets());
                }
                this.clipRegion.setBackground(new Background(arrbackgroundFill));
            }
        });
    }

    @Override
    protected void createIndeterminateTimeline() {
        if (this.indeterminateTransition != null) {
            this.indeterminateTransition.stop();
        }
        ProgressIndicator progressIndicator = (ProgressIndicator)this.getSkinnable();
        double d2 = progressIndicator.getWidth() - (this.snappedLeftInset() + this.snappedRightInset());
        double d3 = this.getIndeterminateBarEscape() != false ? -this.getIndeterminateBarLength().doubleValue() : 0.0;
        double d4 = this.getIndeterminateBarEscape() != false ? d2 : d2 - this.getIndeterminateBarLength();
        this.indeterminateTransition = new IndeterminateTransition(d3, d4, this);
        this.indeterminateTransition.setCycleCount(-1);
        this.clipRegion.translateXProperty().bind(new When(this.bar.scaleXProperty().isEqualTo(-1.0, 1.0E-100)).then(this.bar.translateXProperty().subtract(d2).add(this.indeterminateBarLengthProperty())).otherwise(this.bar.translateXProperty().negate()));
    }

    @Override
    protected void updateProgress() {
        ProgressIndicator progressIndicator = (ProgressIndicator)this.getSkinnable();
        boolean bl = progressIndicator.isIndeterminate();
        if (!bl || !this.wasIndeterminate) {
            this.barWidth = (double)((int)(progressIndicator.getWidth() - this.snappedLeftInset() - this.snappedRightInset()) * 2) * Math.min(1.0, Math.max(0.0, progressIndicator.getProgress())) / 2.0;
            ((ProgressIndicator)this.getSkinnable()).requestLayout();
        }
        this.wasIndeterminate = bl;
    }

    @Override
    public double computeBaselineOffset(double d2, double d3, double d4, double d5) {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        return Math.max(100.0, d6 + this.bar.prefWidth(((ProgressIndicator)this.getSkinnable()).getWidth()) + d4);
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        return d3 + this.bar.prefHeight(d2) + d5;
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        return ((ProgressIndicator)this.getSkinnable()).prefWidth(d2);
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        return ((ProgressIndicator)this.getSkinnable()).prefHeight(d2);
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        ProgressIndicator progressIndicator = (ProgressIndicator)this.getSkinnable();
        boolean bl = progressIndicator.isIndeterminate();
        this.clipRegion.resizeRelocate(0.0, 0.0, d4, d5);
        this.track.resizeRelocate(d2, d3, d4, d5);
        this.bar.resizeRelocate(d2, d3, bl ? this.getIndeterminateBarLength() : this.barWidth, d5);
        this.track.setVisible(true);
        if (bl) {
            this.createIndeterminateTimeline();
            if (((ProgressIndicator)this.getSkinnable()).impl_isTreeVisible()) {
                this.indeterminateTransition.play();
            }
            this.bar.setClip(this.clipRegion);
        } else if (this.indeterminateTransition != null) {
            this.indeterminateTransition.stop();
            this.indeterminateTransition = null;
            this.bar.setClip(null);
            this.bar.setScaleX(1.0);
            this.bar.setTranslateX(0.0);
            this.clipRegion.translateXProperty().unbind();
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return ProgressBarSkin.getClassCssMetaData();
    }

    private static class IndeterminateTransition
    extends Transition {
        private final WeakReference<ProgressBarSkin> skin;
        private final double startX;
        private final double endX;
        private final boolean flip;

        public IndeterminateTransition(double d2, double d3, ProgressBarSkin progressBarSkin) {
            this.startX = d2;
            this.endX = d3;
            this.skin = new WeakReference<ProgressBarSkin>(progressBarSkin);
            this.flip = progressBarSkin.getIndeterminateBarFlip();
            progressBarSkin.getIndeterminateBarEscape();
            this.setCycleDuration(Duration.seconds(progressBarSkin.getIndeterminateBarAnimationTime() * (double)(this.flip ? 2 : 1)));
        }

        @Override
        protected void interpolate(double d2) {
            ProgressBarSkin progressBarSkin = (ProgressBarSkin)this.skin.get();
            if (progressBarSkin == null) {
                this.stop();
            } else if (d2 <= 0.5 || !this.flip) {
                progressBarSkin.bar.setScaleX(-1.0);
                progressBarSkin.bar.setTranslateX(this.startX + (double)(this.flip ? 2 : 1) * d2 * (this.endX - this.startX));
            } else {
                progressBarSkin.bar.setScaleX(1.0);
                progressBarSkin.bar.setTranslateX(this.startX + 2.0 * (1.0 - d2) * (this.endX - this.startX));
            }
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<ProgressBar, Number> INDETERMINATE_BAR_LENGTH = new CssMetaData<ProgressBar, Number>("-fx-indeterminate-bar-length", SizeConverter.getInstance(), (Number)60.0){

            @Override
            public boolean isSettable(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return progressBarSkin.indeterminateBarLength == null || !progressBarSkin.indeterminateBarLength.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return (StyleableProperty)((Object)progressBarSkin.indeterminateBarLengthProperty());
            }
        };
        private static final CssMetaData<ProgressBar, Boolean> INDETERMINATE_BAR_ESCAPE = new CssMetaData<ProgressBar, Boolean>("-fx-indeterminate-bar-escape", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return progressBarSkin.indeterminateBarEscape == null || !progressBarSkin.indeterminateBarEscape.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return (StyleableProperty)((Object)progressBarSkin.indeterminateBarEscapeProperty());
            }
        };
        private static final CssMetaData<ProgressBar, Boolean> INDETERMINATE_BAR_FLIP = new CssMetaData<ProgressBar, Boolean>("-fx-indeterminate-bar-flip", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return progressBarSkin.indeterminateBarFlip == null || !progressBarSkin.indeterminateBarFlip.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return (StyleableProperty)((Object)progressBarSkin.indeterminateBarFlipProperty());
            }
        };
        private static final CssMetaData<ProgressBar, Number> INDETERMINATE_BAR_ANIMATION_TIME = new CssMetaData<ProgressBar, Number>("-fx-indeterminate-bar-animation-time", SizeConverter.getInstance(), (Number)2.0){

            @Override
            public boolean isSettable(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return progressBarSkin.indeterminateBarAnimationTime == null || !progressBarSkin.indeterminateBarAnimationTime.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return (StyleableProperty)((Object)progressBarSkin.indeterminateBarAnimationTimeProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(SkinBase.getClassCssMetaData());
            arrayList.add(INDETERMINATE_BAR_LENGTH);
            arrayList.add(INDETERMINATE_BAR_ESCAPE);
            arrayList.add(INDETERMINATE_BAR_FLIP);
            arrayList.add(INDETERMINATE_BAR_ANIMATION_TIME);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

