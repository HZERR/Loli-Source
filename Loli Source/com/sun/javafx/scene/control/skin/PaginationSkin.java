/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.PaginationBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class PaginationSkin
extends BehaviorSkinBase<Pagination, PaginationBehavior> {
    private static final Duration DURATION = new Duration(125.0);
    private static final double SWIPE_THRESHOLD = 0.3;
    private static final double TOUCH_THRESHOLD = 15.0;
    private Pagination pagination;
    private StackPane currentStackPane;
    private StackPane nextStackPane;
    private Timeline timeline;
    private Rectangle clipRect;
    private NavigationControl navigation;
    private int fromIndex;
    private int previousIndex;
    private int currentIndex;
    private int toIndex;
    private int pageCount;
    private int maxPageIndicatorCount;
    private boolean animate = true;
    private double startTouchPos;
    private double lastTouchPos;
    private long startTouchTime;
    private long lastTouchTime;
    private double touchVelocity;
    private boolean touchThresholdBroken;
    private int touchEventId = -1;
    private boolean nextPageReached = false;
    private boolean setInitialDirection = false;
    private int direction;
    private static final Interpolator interpolator = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);
    private int currentAnimatedIndex;
    private boolean hasPendingAnimation = false;
    private EventHandler<ActionEvent> swipeAnimationEndEventHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(ActionEvent actionEvent) {
            PaginationSkin.this.swapPanes();
            PaginationSkin.this.timeline = null;
            if (PaginationSkin.this.hasPendingAnimation) {
                PaginationSkin.this.animateSwitchPage();
                PaginationSkin.this.hasPendingAnimation = false;
            }
        }
    };
    private EventHandler<ActionEvent> clampAnimationEndEventHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(ActionEvent actionEvent) {
            PaginationSkin.this.currentStackPane.setTranslateX(0.0);
            PaginationSkin.this.nextStackPane.setTranslateX(0.0);
            PaginationSkin.this.nextStackPane.setVisible(false);
            PaginationSkin.this.timeline = null;
        }
    };
    private final DoubleProperty arrowButtonGap = new StyleableDoubleProperty(60.0){

        @Override
        public Object getBean() {
            return PaginationSkin.this;
        }

        @Override
        public String getName() {
            return "arrowButtonGap";
        }

        @Override
        public CssMetaData<Pagination, Number> getCssMetaData() {
            return StyleableProperties.ARROW_BUTTON_GAP;
        }
    };
    private BooleanProperty arrowsVisible;
    private BooleanProperty pageInformationVisible;
    private ObjectProperty<Side> pageInformationAlignment;
    private BooleanProperty tooltipVisible;
    private static final Boolean DEFAULT_ARROW_VISIBLE = Boolean.FALSE;
    private static final Boolean DEFAULT_PAGE_INFORMATION_VISIBLE = Boolean.FALSE;
    private static final Side DEFAULT_PAGE_INFORMATION_ALIGNMENT = Side.BOTTOM;
    private static final Boolean DEFAULT_TOOLTIP_VISIBLE = Boolean.FALSE;

    public PaginationSkin(Pagination pagination) {
        super(pagination, new PaginationBehavior(pagination));
        this.clipRect = new Rectangle();
        ((Pagination)this.getSkinnable()).setClip(this.clipRect);
        this.pagination = pagination;
        this.currentStackPane = new StackPane();
        this.currentStackPane.getStyleClass().add("page");
        this.nextStackPane = new StackPane();
        this.nextStackPane.getStyleClass().add("page");
        this.nextStackPane.setVisible(false);
        this.resetIndexes(true);
        this.navigation = new NavigationControl();
        this.getChildren().addAll(this.currentStackPane, this.nextStackPane, this.navigation);
        pagination.maxPageIndicatorCountProperty().addListener(observable -> this.resetIndiciesAndNav());
        this.registerChangeListener(pagination.widthProperty(), "WIDTH");
        this.registerChangeListener(pagination.heightProperty(), "HEIGHT");
        this.registerChangeListener(pagination.pageCountProperty(), "PAGE_COUNT");
        this.registerChangeListener(pagination.pageFactoryProperty(), "PAGE_FACTORY");
        this.initializeSwipeAndTouchHandlers();
    }

    protected void resetIndiciesAndNav() {
        this.resetIndexes(false);
        this.navigation.initializePageIndicators();
        this.navigation.updatePageIndicators();
    }

    public void selectNext() {
        if (this.getCurrentPageIndex() < this.getPageCount() - 1) {
            this.pagination.setCurrentPageIndex(this.getCurrentPageIndex() + 1);
        }
    }

    public void selectPrevious() {
        if (this.getCurrentPageIndex() > 0) {
            this.pagination.setCurrentPageIndex(this.getCurrentPageIndex() - 1);
        }
    }

    private void initializeSwipeAndTouchHandlers() {
        Pagination pagination = (Pagination)this.getSkinnable();
        ((Pagination)this.getSkinnable()).addEventHandler(TouchEvent.TOUCH_PRESSED, touchEvent -> {
            if (this.touchEventId == -1) {
                this.touchEventId = touchEvent.getTouchPoint().getId();
            }
            if (this.touchEventId != touchEvent.getTouchPoint().getId()) {
                return;
            }
            this.lastTouchPos = this.startTouchPos = touchEvent.getTouchPoint().getX();
            this.lastTouchTime = this.startTouchTime = System.currentTimeMillis();
            this.touchThresholdBroken = false;
            touchEvent.consume();
        });
        ((Pagination)this.getSkinnable()).addEventHandler(TouchEvent.TOUCH_MOVED, touchEvent -> {
            if (this.touchEventId != touchEvent.getTouchPoint().getId()) {
                return;
            }
            double d2 = touchEvent.getTouchPoint().getX() - this.lastTouchPos;
            long l2 = System.currentTimeMillis() - this.lastTouchTime;
            this.touchVelocity = d2 / (double)l2;
            this.lastTouchPos = touchEvent.getTouchPoint().getX();
            this.lastTouchTime = System.currentTimeMillis();
            double d3 = touchEvent.getTouchPoint().getX() - this.startTouchPos;
            if (!this.touchThresholdBroken && Math.abs(d3) > 15.0) {
                this.touchThresholdBroken = true;
            }
            if (this.touchThresholdBroken) {
                double d4 = pagination.getWidth() - (this.snappedLeftInset() + this.snappedRightInset());
                if (!this.setInitialDirection) {
                    this.setInitialDirection = true;
                    int n2 = this.direction = d3 < 0.0 ? 1 : -1;
                }
                if (d3 < 0.0) {
                    double d5;
                    double d6;
                    if (this.direction == -1) {
                        this.nextStackPane.getChildren().clear();
                        this.direction = 1;
                    }
                    if (Math.abs(d3) <= d4) {
                        d6 = d3;
                        d5 = d4 + d3;
                        this.nextPageReached = false;
                    } else {
                        d6 = -d4;
                        d5 = 0.0;
                        this.nextPageReached = true;
                    }
                    this.currentStackPane.setTranslateX(d6);
                    if (this.getCurrentPageIndex() < this.getPageCount() - 1) {
                        this.createPage(this.nextStackPane, this.currentIndex + 1);
                        this.nextStackPane.setVisible(true);
                        this.nextStackPane.setTranslateX(d5);
                    } else {
                        this.currentStackPane.setTranslateX(0.0);
                    }
                } else {
                    double d7;
                    double d8;
                    if (this.direction == 1) {
                        this.nextStackPane.getChildren().clear();
                        this.direction = -1;
                    }
                    if (Math.abs(d3) <= d4) {
                        d8 = d3;
                        d7 = -d4 + d3;
                        this.nextPageReached = false;
                    } else {
                        d8 = d4;
                        d7 = 0.0;
                        this.nextPageReached = true;
                    }
                    this.currentStackPane.setTranslateX(d8);
                    if (this.getCurrentPageIndex() != 0) {
                        this.createPage(this.nextStackPane, this.currentIndex - 1);
                        this.nextStackPane.setVisible(true);
                        this.nextStackPane.setTranslateX(d7);
                    } else {
                        this.currentStackPane.setTranslateX(0.0);
                    }
                }
            }
            touchEvent.consume();
        });
        ((Pagination)this.getSkinnable()).addEventHandler(TouchEvent.TOUCH_RELEASED, touchEvent -> {
            if (this.touchEventId != touchEvent.getTouchPoint().getId()) {
                return;
            }
            this.touchEventId = -1;
            this.setInitialDirection = false;
            if (this.touchThresholdBroken) {
                double d2 = touchEvent.getTouchPoint().getX() - this.startTouchPos;
                long l2 = System.currentTimeMillis() - this.startTouchTime;
                boolean bl = l2 < 300L;
                double d3 = bl ? d2 / (double)l2 : this.touchVelocity;
                double d4 = d3 * 500.0;
                double d5 = pagination.getWidth() - (this.snappedLeftInset() + this.snappedRightInset());
                double d6 = Math.abs(d4 / d5);
                double d7 = Math.abs(d2 / d5);
                if (d6 > 0.3 || d7 > 0.3) {
                    if (this.startTouchPos > touchEvent.getTouchPoint().getX()) {
                        this.selectNext();
                    } else {
                        this.selectPrevious();
                    }
                } else {
                    this.animateClamping(this.startTouchPos > touchEvent.getTouchPoint().getSceneX());
                }
            }
            touchEvent.consume();
        });
    }

    private void resetIndexes(boolean bl) {
        boolean bl2;
        this.maxPageIndicatorCount = this.getMaxPageIndicatorCount();
        this.pageCount = this.getPageCount();
        if (this.pageCount > this.maxPageIndicatorCount) {
            this.pageCount = this.maxPageIndicatorCount;
        }
        this.fromIndex = 0;
        this.previousIndex = 0;
        this.currentIndex = bl ? this.getCurrentPageIndex() : 0;
        this.toIndex = this.pageCount - 1;
        if (this.pageCount == Integer.MAX_VALUE && this.maxPageIndicatorCount == Integer.MAX_VALUE) {
            this.toIndex = 0;
        }
        if (bl2 = this.animate) {
            this.animate = false;
        }
        this.currentStackPane.getChildren().clear();
        this.nextStackPane.getChildren().clear();
        this.pagination.setCurrentPageIndex(this.currentIndex);
        this.createPage(this.currentStackPane, this.currentIndex);
        if (bl2) {
            this.animate = true;
        }
    }

    private boolean createPage(StackPane stackPane, int n2) {
        if (this.pagination.getPageFactory() != null && stackPane.getChildren().isEmpty()) {
            Node node = this.pagination.getPageFactory().call(n2);
            if (node != null) {
                stackPane.getChildren().setAll(node);
                return true;
            }
            boolean bl = this.animate;
            if (bl) {
                this.animate = false;
            }
            if (this.pagination.getPageFactory().call(this.previousIndex) != null) {
                this.pagination.setCurrentPageIndex(this.previousIndex);
            } else {
                this.pagination.setCurrentPageIndex(0);
            }
            if (bl) {
                this.animate = true;
            }
            return false;
        }
        return false;
    }

    private int getPageCount() {
        if (((Pagination)this.getSkinnable()).getPageCount() < 1) {
            return 1;
        }
        return ((Pagination)this.getSkinnable()).getPageCount();
    }

    private int getMaxPageIndicatorCount() {
        return ((Pagination)this.getSkinnable()).getMaxPageIndicatorCount();
    }

    private int getCurrentPageIndex() {
        return ((Pagination)this.getSkinnable()).getCurrentPageIndex();
    }

    private void animateSwitchPage() {
        if (this.timeline != null) {
            this.timeline.setRate(8.0);
            this.hasPendingAnimation = true;
            return;
        }
        if (!this.nextStackPane.isVisible() && !this.createPage(this.nextStackPane, this.currentAnimatedIndex)) {
            return;
        }
        if (this.nextPageReached) {
            this.swapPanes();
            this.nextPageReached = false;
            return;
        }
        this.nextStackPane.setCache(true);
        this.currentStackPane.setCache(true);
        Platform.runLater(() -> {
            boolean bl;
            boolean bl2 = bl = this.nextStackPane.getTranslateX() != 0.0;
            if (this.currentAnimatedIndex > this.previousIndex) {
                if (!bl) {
                    this.nextStackPane.setTranslateX(this.currentStackPane.getWidth());
                }
                this.nextStackPane.setVisible(true);
                this.timeline = new Timeline();
                KeyFrame keyFrame = new KeyFrame(Duration.millis(0.0), new KeyValue(this.currentStackPane.translateXProperty(), bl ? this.currentStackPane.getTranslateX() : 0.0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), bl ? this.nextStackPane.getTranslateX() : this.currentStackPane.getWidth(), interpolator));
                KeyFrame keyFrame2 = new KeyFrame(DURATION, this.swipeAnimationEndEventHandler, new KeyValue(this.currentStackPane.translateXProperty(), -this.currentStackPane.getWidth(), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), 0, interpolator));
                this.timeline.getKeyFrames().setAll(keyFrame, keyFrame2);
                this.timeline.play();
            } else {
                if (!bl) {
                    this.nextStackPane.setTranslateX(-this.currentStackPane.getWidth());
                }
                this.nextStackPane.setVisible(true);
                this.timeline = new Timeline();
                KeyFrame keyFrame = new KeyFrame(Duration.millis(0.0), new KeyValue(this.currentStackPane.translateXProperty(), bl ? this.currentStackPane.getTranslateX() : 0.0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), bl ? this.nextStackPane.getTranslateX() : -this.currentStackPane.getWidth(), interpolator));
                KeyFrame keyFrame3 = new KeyFrame(DURATION, this.swipeAnimationEndEventHandler, new KeyValue(this.currentStackPane.translateXProperty(), this.currentStackPane.getWidth(), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), 0, interpolator));
                this.timeline.getKeyFrames().setAll(keyFrame, keyFrame3);
                this.timeline.play();
            }
        });
    }

    private void swapPanes() {
        StackPane stackPane = this.currentStackPane;
        this.currentStackPane = this.nextStackPane;
        this.nextStackPane = stackPane;
        this.currentStackPane.setTranslateX(0.0);
        this.currentStackPane.setCache(false);
        this.nextStackPane.setTranslateX(0.0);
        this.nextStackPane.setCache(false);
        this.nextStackPane.setVisible(false);
        this.nextStackPane.getChildren().clear();
    }

    private void animateClamping(boolean bl) {
        if (bl) {
            this.timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.millis(0.0), new KeyValue(this.currentStackPane.translateXProperty(), this.currentStackPane.getTranslateX(), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), this.nextStackPane.getTranslateX(), interpolator));
            KeyFrame keyFrame2 = new KeyFrame(DURATION, this.clampAnimationEndEventHandler, new KeyValue(this.currentStackPane.translateXProperty(), 0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), this.currentStackPane.getWidth(), interpolator));
            this.timeline.getKeyFrames().setAll(keyFrame, keyFrame2);
            this.timeline.play();
        } else {
            this.timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.millis(0.0), new KeyValue(this.currentStackPane.translateXProperty(), this.currentStackPane.getTranslateX(), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), this.nextStackPane.getTranslateX(), interpolator));
            KeyFrame keyFrame3 = new KeyFrame(DURATION, this.clampAnimationEndEventHandler, new KeyValue(this.currentStackPane.translateXProperty(), 0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), -this.currentStackPane.getWidth(), interpolator));
            this.timeline.getKeyFrames().setAll(keyFrame, keyFrame3);
            this.timeline.play();
        }
    }

    private DoubleProperty arrowButtonGapProperty() {
        return this.arrowButtonGap;
    }

    public final void setArrowsVisible(boolean bl) {
        this.arrowsVisibleProperty().set(bl);
    }

    public final boolean isArrowsVisible() {
        return this.arrowsVisible == null ? DEFAULT_ARROW_VISIBLE.booleanValue() : this.arrowsVisible.get();
    }

    public final BooleanProperty arrowsVisibleProperty() {
        if (this.arrowsVisible == null) {
            this.arrowsVisible = new StyleableBooleanProperty(DEFAULT_ARROW_VISIBLE){

                @Override
                protected void invalidated() {
                    ((Pagination)PaginationSkin.this.getSkinnable()).requestLayout();
                }

                @Override
                public CssMetaData<Pagination, Boolean> getCssMetaData() {
                    return StyleableProperties.ARROWS_VISIBLE;
                }

                @Override
                public Object getBean() {
                    return PaginationSkin.this;
                }

                @Override
                public String getName() {
                    return "arrowVisible";
                }
            };
        }
        return this.arrowsVisible;
    }

    public final void setPageInformationVisible(boolean bl) {
        this.pageInformationVisibleProperty().set(bl);
    }

    public final boolean isPageInformationVisible() {
        return this.pageInformationVisible == null ? DEFAULT_PAGE_INFORMATION_VISIBLE.booleanValue() : this.pageInformationVisible.get();
    }

    public final BooleanProperty pageInformationVisibleProperty() {
        if (this.pageInformationVisible == null) {
            this.pageInformationVisible = new StyleableBooleanProperty(DEFAULT_PAGE_INFORMATION_VISIBLE){

                @Override
                protected void invalidated() {
                    ((Pagination)PaginationSkin.this.getSkinnable()).requestLayout();
                }

                @Override
                public CssMetaData<Pagination, Boolean> getCssMetaData() {
                    return StyleableProperties.PAGE_INFORMATION_VISIBLE;
                }

                @Override
                public Object getBean() {
                    return PaginationSkin.this;
                }

                @Override
                public String getName() {
                    return "pageInformationVisible";
                }
            };
        }
        return this.pageInformationVisible;
    }

    public final void setPageInformationAlignment(Side side) {
        this.pageInformationAlignmentProperty().set(side);
    }

    public final Side getPageInformationAlignment() {
        return this.pageInformationAlignment == null ? DEFAULT_PAGE_INFORMATION_ALIGNMENT : (Side)((Object)this.pageInformationAlignment.get());
    }

    public final ObjectProperty<Side> pageInformationAlignmentProperty() {
        if (this.pageInformationAlignment == null) {
            this.pageInformationAlignment = new StyleableObjectProperty<Side>(Side.BOTTOM){

                @Override
                protected void invalidated() {
                    ((Pagination)PaginationSkin.this.getSkinnable()).requestLayout();
                }

                @Override
                public CssMetaData<Pagination, Side> getCssMetaData() {
                    return StyleableProperties.PAGE_INFORMATION_ALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return PaginationSkin.this;
                }

                @Override
                public String getName() {
                    return "pageInformationAlignment";
                }
            };
        }
        return this.pageInformationAlignment;
    }

    public final void setTooltipVisible(boolean bl) {
        this.tooltipVisibleProperty().set(bl);
    }

    public final boolean isTooltipVisible() {
        return this.tooltipVisible == null ? DEFAULT_TOOLTIP_VISIBLE.booleanValue() : this.tooltipVisible.get();
    }

    public final BooleanProperty tooltipVisibleProperty() {
        if (this.tooltipVisible == null) {
            this.tooltipVisible = new StyleableBooleanProperty(DEFAULT_TOOLTIP_VISIBLE){

                @Override
                protected void invalidated() {
                    ((Pagination)PaginationSkin.this.getSkinnable()).requestLayout();
                }

                @Override
                public CssMetaData<Pagination, Boolean> getCssMetaData() {
                    return StyleableProperties.TOOLTIP_VISIBLE;
                }

                @Override
                public Object getBean() {
                    return PaginationSkin.this;
                }

                @Override
                public String getName() {
                    return "tooltipVisible";
                }
            };
        }
        return this.tooltipVisible;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("PAGE_FACTORY".equals(string)) {
            if (this.animate && this.timeline != null) {
                this.timeline.setRate(8.0);
                this.timeline.setOnFinished(actionEvent -> this.resetIndiciesAndNav());
                return;
            }
            this.resetIndiciesAndNav();
        } else if ("PAGE_COUNT".equals(string)) {
            this.resetIndiciesAndNav();
        } else if ("WIDTH".equals(string)) {
            this.clipRect.setWidth(((Pagination)this.getSkinnable()).getWidth());
        } else if ("HEIGHT".equals(string)) {
            this.clipRect.setHeight(((Pagination)this.getSkinnable()).getHeight());
        }
        ((Pagination)this.getSkinnable()).requestLayout();
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.navigation.isVisible() ? this.snapSize(this.navigation.minWidth(d2)) : 0.0;
        return d6 + Math.max(this.currentStackPane.minWidth(d2), d7) + d4;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.navigation.isVisible() ? this.snapSize(this.navigation.minHeight(d2)) : 0.0;
        return d3 + this.currentStackPane.minHeight(d2) + d7 + d5;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.navigation.isVisible() ? this.snapSize(this.navigation.prefWidth(d2)) : 0.0;
        return d6 + Math.max(this.currentStackPane.prefWidth(d2), d7) + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.navigation.isVisible() ? this.snapSize(this.navigation.prefHeight(d2)) : 0.0;
        return d3 + this.currentStackPane.prefHeight(d2) + d7 + d5;
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6 = this.navigation.isVisible() ? this.snapSize(this.navigation.prefHeight(-1.0)) : 0.0;
        double d7 = this.snapSize(d5 - d6);
        this.layoutInArea(this.currentStackPane, d2, d3, d4, d7, 0.0, HPos.CENTER, VPos.CENTER);
        this.layoutInArea(this.nextStackPane, d2, d3, d4, d7, 0.0, HPos.CENTER, VPos.CENTER);
        this.layoutInArea(this.navigation, d2, d7, d4, d6, 0.0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                return this.navigation.indicatorButtons.getSelectedToggle();
            }
            case ITEM_COUNT: {
                return this.navigation.indicatorButtons.getToggles().size();
            }
            case ITEM_AT_INDEX: {
                Integer n2 = (Integer)arrobject[0];
                if (n2 == null) {
                    return null;
                }
                return this.navigation.indicatorButtons.getToggles().get(n2);
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return PaginationSkin.getClassCssMetaData();
    }

    static /* synthetic */ Boolean access$2900() {
        return DEFAULT_ARROW_VISIBLE;
    }

    static /* synthetic */ Boolean access$3100() {
        return DEFAULT_PAGE_INFORMATION_VISIBLE;
    }

    static /* synthetic */ Side access$3300() {
        return DEFAULT_PAGE_INFORMATION_ALIGNMENT;
    }

    static /* synthetic */ Boolean access$3500() {
        return DEFAULT_TOOLTIP_VISIBLE;
    }

    private static class StyleableProperties {
        private static final CssMetaData<Pagination, Boolean> ARROWS_VISIBLE = new CssMetaData<Pagination, Boolean>("-fx-arrows-visible", BooleanConverter.getInstance(), PaginationSkin.access$2900()){

            @Override
            public boolean isSettable(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return paginationSkin.arrowsVisible == null || !paginationSkin.arrowsVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return (StyleableProperty)((Object)paginationSkin.arrowsVisibleProperty());
            }
        };
        private static final CssMetaData<Pagination, Boolean> PAGE_INFORMATION_VISIBLE = new CssMetaData<Pagination, Boolean>("-fx-page-information-visible", BooleanConverter.getInstance(), PaginationSkin.access$3100()){

            @Override
            public boolean isSettable(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return paginationSkin.pageInformationVisible == null || !paginationSkin.pageInformationVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return (StyleableProperty)((Object)paginationSkin.pageInformationVisibleProperty());
            }
        };
        private static final CssMetaData<Pagination, Side> PAGE_INFORMATION_ALIGNMENT = new CssMetaData<Pagination, Side>("-fx-page-information-alignment", new EnumConverter<Side>(Side.class), PaginationSkin.access$3300()){

            @Override
            public boolean isSettable(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return paginationSkin.pageInformationAlignment == null || !paginationSkin.pageInformationAlignment.isBound();
            }

            @Override
            public StyleableProperty<Side> getStyleableProperty(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return (StyleableProperty)((Object)paginationSkin.pageInformationAlignmentProperty());
            }
        };
        private static final CssMetaData<Pagination, Boolean> TOOLTIP_VISIBLE = new CssMetaData<Pagination, Boolean>("-fx-tooltip-visible", BooleanConverter.getInstance(), PaginationSkin.access$3500()){

            @Override
            public boolean isSettable(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return paginationSkin.tooltipVisible == null || !paginationSkin.tooltipVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return (StyleableProperty)((Object)paginationSkin.tooltipVisibleProperty());
            }
        };
        private static final CssMetaData<Pagination, Number> ARROW_BUTTON_GAP = new CssMetaData<Pagination, Number>("-fx-arrow-button-gap", SizeConverter.getInstance(), (Number)4){

            @Override
            public boolean isSettable(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return paginationSkin.arrowButtonGap == null || !paginationSkin.arrowButtonGap.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Pagination pagination) {
                PaginationSkin paginationSkin = (PaginationSkin)pagination.getSkin();
                return (StyleableProperty)((Object)paginationSkin.arrowButtonGapProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(SkinBase.getClassCssMetaData());
            arrayList.add(ARROWS_VISIBLE);
            arrayList.add(PAGE_INFORMATION_VISIBLE);
            arrayList.add(PAGE_INFORMATION_ALIGNMENT);
            arrayList.add(TOOLTIP_VISIBLE);
            arrayList.add(ARROW_BUTTON_GAP);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    class IndicatorButton
    extends ToggleButton {
        private final ListChangeListener<String> updateSkinIndicatorType = change -> this.setIndicatorType();
        private final ChangeListener<Boolean> updateTooltipVisibility = (observableValue, bl, bl2) -> this.setTooltipVisible((boolean)bl2);
        private int pageNumber;

        public IndicatorButton(int n2) {
            this.pageNumber = n2;
            this.setFocusTraversable(false);
            this.setIndicatorType();
            this.setTooltipVisible(PaginationSkin.this.isTooltipVisible());
            ((Pagination)PaginationSkin.this.getSkinnable()).getStyleClass().addListener(this.updateSkinIndicatorType);
            this.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent actionEvent) {
                    int n2 = PaginationSkin.this.getCurrentPageIndex();
                    if (n2 != IndicatorButton.this.pageNumber) {
                        PaginationSkin.this.pagination.setCurrentPageIndex(IndicatorButton.this.pageNumber);
                        IndicatorButton.this.requestLayout();
                    }
                }
            });
            PaginationSkin.this.tooltipVisibleProperty().addListener(this.updateTooltipVisibility);
            this.prefHeightProperty().bind(this.minHeightProperty());
            this.setAccessibleRole(AccessibleRole.PAGE_ITEM);
        }

        private void setIndicatorType() {
            if (((Pagination)PaginationSkin.this.getSkinnable()).getStyleClass().contains("bullet")) {
                this.getStyleClass().remove("number-button");
                this.getStyleClass().add("bullet-button");
                this.setText(null);
                this.prefWidthProperty().bind(this.minWidthProperty());
            } else {
                this.getStyleClass().remove("bullet-button");
                this.getStyleClass().add("number-button");
                this.setText(Integer.toString(this.pageNumber + 1));
                this.prefWidthProperty().unbind();
            }
        }

        private void setTooltipVisible(boolean bl) {
            if (bl) {
                this.setTooltip(new Tooltip(Integer.toString(this.pageNumber + 1)));
            } else {
                this.setTooltip(null);
            }
        }

        public int getPageNumber() {
            return this.pageNumber;
        }

        @Override
        public void fire() {
            if (this.getToggleGroup() == null || !this.isSelected()) {
                super.fire();
            }
        }

        public void release() {
            ((Pagination)PaginationSkin.this.getSkinnable()).getStyleClass().removeListener(this.updateSkinIndicatorType);
            PaginationSkin.this.tooltipVisibleProperty().removeListener(this.updateTooltipVisibility);
        }

        @Override
        public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
            switch (accessibleAttribute) {
                case TEXT: {
                    return this.getText();
                }
                case SELECTED: {
                    return this.isSelected();
                }
            }
            return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
        }

        @Override
        public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
            switch (accessibleAction) {
                case REQUEST_FOCUS: {
                    ((Pagination)PaginationSkin.this.getSkinnable()).setCurrentPageIndex(this.pageNumber);
                    break;
                }
                default: {
                    super.executeAccessibleAction(accessibleAction, new Object[0]);
                }
            }
        }
    }

    class NavigationControl
    extends StackPane {
        private HBox controlBox;
        private Button leftArrowButton;
        private StackPane leftArrow;
        private Button rightArrowButton;
        private StackPane rightArrow;
        private ToggleGroup indicatorButtons;
        private Label pageInformation;
        private double previousWidth = -1.0;
        private double minButtonSize = -1.0;
        private int previousIndicatorCount = 0;

        public NavigationControl() {
            this.getStyleClass().setAll("pagination-control");
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> ((PaginationBehavior)PaginationSkin.this.getBehavior()).mousePressed((MouseEvent)mouseEvent));
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> ((PaginationBehavior)PaginationSkin.this.getBehavior()).mouseReleased((MouseEvent)mouseEvent));
            this.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> ((PaginationBehavior)PaginationSkin.this.getBehavior()).mouseEntered((MouseEvent)mouseEvent));
            this.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> ((PaginationBehavior)PaginationSkin.this.getBehavior()).mouseExited((MouseEvent)mouseEvent));
            this.controlBox = new HBox();
            this.controlBox.getStyleClass().add("control-box");
            this.leftArrowButton = new Button();
            this.leftArrowButton.setAccessibleText(ControlResources.getString("Accessibility.title.Pagination.PreviousButton"));
            this.minButtonSize = this.leftArrowButton.getFont().getSize() * 2.0;
            this.leftArrowButton.fontProperty().addListener((observableValue, font, font2) -> {
                this.minButtonSize = font2.getSize() * 2.0;
                for (Node node : this.controlBox.getChildren()) {
                    ((Control)node).setMinSize(this.minButtonSize, this.minButtonSize);
                }
                this.requestLayout();
            });
            this.leftArrowButton.setMinSize(this.minButtonSize, this.minButtonSize);
            this.leftArrowButton.prefWidthProperty().bind(this.leftArrowButton.minWidthProperty());
            this.leftArrowButton.prefHeightProperty().bind(this.leftArrowButton.minHeightProperty());
            this.leftArrowButton.getStyleClass().add("left-arrow-button");
            this.leftArrowButton.setFocusTraversable(false);
            HBox.setMargin(this.leftArrowButton, new Insets(0.0, this.snapSize(PaginationSkin.this.arrowButtonGap.get()), 0.0, 0.0));
            this.leftArrow = new StackPane();
            this.leftArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
            this.leftArrowButton.setGraphic(this.leftArrow);
            this.leftArrow.getStyleClass().add("left-arrow");
            this.rightArrowButton = new Button();
            this.rightArrowButton.setAccessibleText(ControlResources.getString("Accessibility.title.Pagination.NextButton"));
            this.rightArrowButton.setMinSize(this.minButtonSize, this.minButtonSize);
            this.rightArrowButton.prefWidthProperty().bind(this.rightArrowButton.minWidthProperty());
            this.rightArrowButton.prefHeightProperty().bind(this.rightArrowButton.minHeightProperty());
            this.rightArrowButton.getStyleClass().add("right-arrow-button");
            this.rightArrowButton.setFocusTraversable(false);
            HBox.setMargin(this.rightArrowButton, new Insets(0.0, 0.0, 0.0, this.snapSize(PaginationSkin.this.arrowButtonGap.get())));
            this.rightArrow = new StackPane();
            this.rightArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
            this.rightArrowButton.setGraphic(this.rightArrow);
            this.rightArrow.getStyleClass().add("right-arrow");
            this.indicatorButtons = new ToggleGroup();
            this.pageInformation = new Label();
            this.pageInformation.getStyleClass().add("page-information");
            this.getChildren().addAll(this.controlBox, this.pageInformation);
            this.initializeNavigationHandlers();
            this.initializePageIndicators();
            this.updatePageIndex();
            PaginationSkin.this.arrowButtonGap.addListener((observableValue, number, number2) -> {
                if (number2.doubleValue() == 0.0) {
                    HBox.setMargin(this.leftArrowButton, null);
                    HBox.setMargin(this.rightArrowButton, null);
                } else {
                    HBox.setMargin(this.leftArrowButton, new Insets(0.0, this.snapSize(number2.doubleValue()), 0.0, 0.0));
                    HBox.setMargin(this.rightArrowButton, new Insets(0.0, 0.0, 0.0, this.snapSize(number2.doubleValue())));
                }
            });
        }

        private void initializeNavigationHandlers() {
            this.leftArrowButton.setOnAction(actionEvent -> {
                PaginationSkin.this.selectPrevious();
                this.requestLayout();
            });
            this.rightArrowButton.setOnAction(actionEvent -> {
                PaginationSkin.this.selectNext();
                this.requestLayout();
            });
            PaginationSkin.this.pagination.currentPageIndexProperty().addListener((observableValue, number, number2) -> {
                PaginationSkin.this.previousIndex = number.intValue();
                PaginationSkin.this.currentIndex = number2.intValue();
                this.updatePageIndex();
                if (PaginationSkin.this.animate) {
                    PaginationSkin.this.currentAnimatedIndex = PaginationSkin.this.currentIndex;
                    PaginationSkin.this.animateSwitchPage();
                } else {
                    PaginationSkin.this.createPage(PaginationSkin.this.currentStackPane, PaginationSkin.this.currentIndex);
                }
            });
        }

        private void initializePageIndicators() {
            this.previousIndicatorCount = 0;
            this.controlBox.getChildren().clear();
            this.clearIndicatorButtons();
            this.controlBox.getChildren().add(this.leftArrowButton);
            for (int i2 = PaginationSkin.this.fromIndex; i2 <= PaginationSkin.this.toIndex; ++i2) {
                IndicatorButton indicatorButton = new IndicatorButton(i2);
                indicatorButton.setMinSize(this.minButtonSize, this.minButtonSize);
                indicatorButton.setToggleGroup(this.indicatorButtons);
                this.controlBox.getChildren().add(indicatorButton);
            }
            this.controlBox.getChildren().add(this.rightArrowButton);
        }

        private void clearIndicatorButtons() {
            for (Toggle toggle : this.indicatorButtons.getToggles()) {
                if (!(toggle instanceof IndicatorButton)) continue;
                IndicatorButton indicatorButton = (IndicatorButton)toggle;
                indicatorButton.release();
            }
            this.indicatorButtons.getToggles().clear();
        }

        private void updatePageIndicators() {
            for (int i2 = 0; i2 < this.indicatorButtons.getToggles().size(); ++i2) {
                IndicatorButton indicatorButton = (IndicatorButton)this.indicatorButtons.getToggles().get(i2);
                if (indicatorButton.getPageNumber() != PaginationSkin.this.currentIndex) continue;
                indicatorButton.setSelected(true);
                this.updatePageInformation();
                break;
            }
            ((Pagination)PaginationSkin.this.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        private void updatePageIndex() {
            if (PaginationSkin.this.pageCount == PaginationSkin.this.maxPageIndicatorCount && this.changePageSet()) {
                this.initializePageIndicators();
            }
            this.updatePageIndicators();
            this.requestLayout();
        }

        private void updatePageInformation() {
            String string = Integer.toString(PaginationSkin.this.currentIndex + 1);
            String string2 = PaginationSkin.this.getPageCount() == Integer.MAX_VALUE ? "..." : Integer.toString(PaginationSkin.this.getPageCount());
            this.pageInformation.setText(string + "/" + string2);
        }

        private void layoutPageIndicators() {
            int n2;
            double d2 = this.snappedLeftInset();
            double d3 = this.snappedRightInset();
            double d4 = this.snapSize(this.getWidth()) - (d2 + d3);
            double d5 = this.controlBox.snappedLeftInset();
            double d6 = this.controlBox.snappedRightInset();
            double d7 = this.snapSize(Utils.boundedSize(this.leftArrowButton.prefWidth(-1.0), this.leftArrowButton.minWidth(-1.0), this.leftArrowButton.maxWidth(-1.0)));
            double d8 = this.snapSize(Utils.boundedSize(this.rightArrowButton.prefWidth(-1.0), this.rightArrowButton.minWidth(-1.0), this.rightArrowButton.maxWidth(-1.0)));
            double d9 = this.snapSize(this.controlBox.getSpacing());
            double d10 = d4 - (d5 + d7 + 2.0 * PaginationSkin.this.arrowButtonGap.get() + d9 + d8 + d6);
            if (PaginationSkin.this.isPageInformationVisible() && (Side.LEFT.equals((Object)PaginationSkin.this.getPageInformationAlignment()) || Side.RIGHT.equals((Object)PaginationSkin.this.getPageInformationAlignment()))) {
                d10 -= this.snapSize(this.pageInformation.prefWidth(-1.0));
            }
            double d11 = 0.0;
            int n3 = 0;
            for (n2 = 0; n2 < PaginationSkin.this.getMaxPageIndicatorCount(); ++n2) {
                int n4 = n2 < this.indicatorButtons.getToggles().size() ? n2 : this.indicatorButtons.getToggles().size() - 1;
                double d12 = this.minButtonSize;
                if (n4 != -1) {
                    IndicatorButton indicatorButton = (IndicatorButton)this.indicatorButtons.getToggles().get(n4);
                    d12 = this.snapSize(Utils.boundedSize(indicatorButton.prefWidth(-1.0), indicatorButton.minWidth(-1.0), indicatorButton.maxWidth(-1.0)));
                }
                if ((d11 += d12 + d9) > d10) break;
                ++n3;
            }
            if (n3 == 0) {
                n3 = 1;
            }
            if (n3 != this.previousIndicatorCount) {
                if (n3 < PaginationSkin.this.getMaxPageIndicatorCount()) {
                    PaginationSkin.this.maxPageIndicatorCount = n3;
                } else {
                    PaginationSkin.this.maxPageIndicatorCount = PaginationSkin.this.getMaxPageIndicatorCount();
                }
                if (PaginationSkin.this.pageCount > PaginationSkin.this.maxPageIndicatorCount) {
                    PaginationSkin.this.pageCount = PaginationSkin.this.maxPageIndicatorCount;
                    n2 = PaginationSkin.this.maxPageIndicatorCount - 1;
                } else if (n3 > PaginationSkin.this.getPageCount()) {
                    PaginationSkin.this.pageCount = PaginationSkin.this.getPageCount();
                    n2 = PaginationSkin.this.getPageCount() - 1;
                } else {
                    PaginationSkin.this.pageCount = n3;
                    n2 = n3 - 1;
                }
                if (PaginationSkin.this.currentIndex >= PaginationSkin.this.toIndex) {
                    PaginationSkin.this.toIndex = PaginationSkin.this.currentIndex;
                    PaginationSkin.this.fromIndex = PaginationSkin.this.toIndex - n2;
                } else if (PaginationSkin.this.currentIndex <= PaginationSkin.this.fromIndex) {
                    PaginationSkin.this.fromIndex = PaginationSkin.this.currentIndex;
                    PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + n2;
                } else {
                    PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + n2;
                }
                if (PaginationSkin.this.toIndex > PaginationSkin.this.getPageCount() - 1) {
                    PaginationSkin.this.toIndex = PaginationSkin.this.getPageCount() - 1;
                }
                if (PaginationSkin.this.fromIndex < 0) {
                    PaginationSkin.this.fromIndex = 0;
                    PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + n2;
                }
                this.initializePageIndicators();
                this.updatePageIndicators();
                this.previousIndicatorCount = n3;
            }
        }

        private boolean changePageSet() {
            int n2 = this.indexToIndicatorButtonsIndex(PaginationSkin.this.currentIndex);
            int n3 = PaginationSkin.this.maxPageIndicatorCount - 1;
            if (PaginationSkin.this.previousIndex < PaginationSkin.this.currentIndex && n2 == 0 && n3 != 0 && n2 % n3 == 0) {
                PaginationSkin.this.fromIndex = PaginationSkin.this.currentIndex;
                PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + n3;
            } else if (PaginationSkin.this.currentIndex < PaginationSkin.this.previousIndex && n2 == n3 && n3 != 0 && n2 % n3 == 0) {
                PaginationSkin.this.toIndex = PaginationSkin.this.currentIndex;
                PaginationSkin.this.fromIndex = PaginationSkin.this.toIndex - n3;
            } else if (PaginationSkin.this.currentIndex < PaginationSkin.this.fromIndex || PaginationSkin.this.currentIndex > PaginationSkin.this.toIndex) {
                PaginationSkin.this.fromIndex = PaginationSkin.this.currentIndex - n2;
                PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + n3;
            } else {
                return false;
            }
            if (PaginationSkin.this.toIndex > PaginationSkin.this.getPageCount() - 1) {
                if (PaginationSkin.this.fromIndex > PaginationSkin.this.getPageCount() - 1) {
                    return false;
                }
                PaginationSkin.this.toIndex = PaginationSkin.this.getPageCount() - 1;
            }
            if (PaginationSkin.this.fromIndex < 0) {
                PaginationSkin.this.fromIndex = 0;
                PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + n3;
            }
            return true;
        }

        private int indexToIndicatorButtonsIndex(int n2) {
            if (n2 >= PaginationSkin.this.fromIndex && n2 <= PaginationSkin.this.toIndex) {
                return n2 - PaginationSkin.this.fromIndex;
            }
            int n3 = 0;
            int n4 = PaginationSkin.this.fromIndex;
            int n5 = PaginationSkin.this.toIndex;
            if (PaginationSkin.this.currentIndex > PaginationSkin.this.previousIndex) {
                while (n4 < PaginationSkin.this.getPageCount() && n5 < PaginationSkin.this.getPageCount()) {
                    if (n2 >= (n4 += n3) && n2 <= (n5 += n3)) {
                        if (n2 == n4) {
                            return 0;
                        }
                        if (n2 == n5) {
                            return PaginationSkin.this.maxPageIndicatorCount - 1;
                        }
                        return n2 - n4;
                    }
                    n3 += PaginationSkin.this.maxPageIndicatorCount;
                }
            } else {
                while (n4 > 0 && n5 > 0) {
                    if (n2 >= (n4 -= n3) && n2 <= (n5 -= n3)) {
                        if (n2 == n4) {
                            return 0;
                        }
                        if (n2 == n5) {
                            return PaginationSkin.this.maxPageIndicatorCount - 1;
                        }
                        return n2 - n4;
                    }
                    n3 += PaginationSkin.this.maxPageIndicatorCount;
                }
            }
            return PaginationSkin.this.maxPageIndicatorCount - 1;
        }

        private Pos sideToPos(Side side) {
            if (Side.TOP.equals((Object)side)) {
                return Pos.TOP_CENTER;
            }
            if (Side.RIGHT.equals((Object)side)) {
                return Pos.CENTER_RIGHT;
            }
            if (Side.BOTTOM.equals((Object)side)) {
                return Pos.BOTTOM_CENTER;
            }
            return Pos.CENTER_LEFT;
        }

        @Override
        protected double computeMinWidth(double d2) {
            double d3 = this.snappedLeftInset();
            double d4 = this.snappedRightInset();
            double d5 = this.snapSize(Utils.boundedSize(this.leftArrowButton.prefWidth(-1.0), this.leftArrowButton.minWidth(-1.0), this.leftArrowButton.maxWidth(-1.0)));
            double d6 = this.snapSize(Utils.boundedSize(this.rightArrowButton.prefWidth(-1.0), this.rightArrowButton.minWidth(-1.0), this.rightArrowButton.maxWidth(-1.0)));
            double d7 = this.snapSize(this.controlBox.getSpacing());
            double d8 = 0.0;
            Side side = PaginationSkin.this.getPageInformationAlignment();
            if (Side.LEFT.equals((Object)side) || Side.RIGHT.equals((Object)side)) {
                d8 = this.snapSize(this.pageInformation.prefWidth(-1.0));
            }
            double d9 = PaginationSkin.this.arrowButtonGap.get();
            return d3 + d5 + 2.0 * d9 + this.minButtonSize + 2.0 * d7 + d6 + d4 + d8;
        }

        @Override
        protected double computeMinHeight(double d2) {
            return this.computePrefHeight(d2);
        }

        @Override
        protected double computePrefWidth(double d2) {
            double d3 = this.snappedLeftInset();
            double d4 = this.snappedRightInset();
            double d5 = this.snapSize(this.controlBox.prefWidth(d2));
            double d6 = 0.0;
            Side side = PaginationSkin.this.getPageInformationAlignment();
            if (Side.LEFT.equals((Object)side) || Side.RIGHT.equals((Object)side)) {
                d6 = this.snapSize(this.pageInformation.prefWidth(-1.0));
            }
            return d3 + d5 + d4 + d6;
        }

        @Override
        protected double computePrefHeight(double d2) {
            double d3 = this.snappedTopInset();
            double d4 = this.snappedBottomInset();
            double d5 = this.snapSize(this.controlBox.prefHeight(d2));
            double d6 = 0.0;
            Side side = PaginationSkin.this.getPageInformationAlignment();
            if (Side.TOP.equals((Object)side) || Side.BOTTOM.equals((Object)side)) {
                d6 = this.snapSize(this.pageInformation.prefHeight(-1.0));
            }
            return d3 + d5 + d6 + d4;
        }

        @Override
        protected void layoutChildren() {
            double d2 = this.snappedTopInset();
            double d3 = this.snappedBottomInset();
            double d4 = this.snappedLeftInset();
            double d5 = this.snappedRightInset();
            double d6 = this.snapSize(this.getWidth()) - (d4 + d5);
            double d7 = this.snapSize(this.getHeight()) - (d2 + d3);
            double d8 = this.snapSize(this.controlBox.prefWidth(-1.0));
            double d9 = this.snapSize(this.controlBox.prefHeight(-1.0));
            double d10 = this.snapSize(this.pageInformation.prefWidth(-1.0));
            double d11 = this.snapSize(this.pageInformation.prefHeight(-1.0));
            this.leftArrowButton.setDisable(false);
            this.rightArrowButton.setDisable(false);
            if (PaginationSkin.this.currentIndex == 0) {
                this.leftArrowButton.setDisable(true);
            }
            if (PaginationSkin.this.currentIndex == PaginationSkin.this.getPageCount() - 1) {
                this.rightArrowButton.setDisable(true);
            }
            this.applyCss();
            this.leftArrowButton.setVisible(PaginationSkin.this.isArrowsVisible());
            this.rightArrowButton.setVisible(PaginationSkin.this.isArrowsVisible());
            this.pageInformation.setVisible(PaginationSkin.this.isPageInformationVisible());
            this.layoutPageIndicators();
            this.previousWidth = this.getWidth();
            HPos hPos = this.controlBox.getAlignment().getHpos();
            VPos vPos = this.controlBox.getAlignment().getVpos();
            double d12 = d4 + Utils.computeXOffset(d6, d8, hPos);
            double d13 = d2 + Utils.computeYOffset(d7, d9, vPos);
            if (PaginationSkin.this.isPageInformationVisible()) {
                Pos pos = this.sideToPos(PaginationSkin.this.getPageInformationAlignment());
                HPos hPos2 = pos.getHpos();
                VPos vPos2 = pos.getVpos();
                double d14 = d4 + Utils.computeXOffset(d6, d10, hPos2);
                double d15 = d2 + Utils.computeYOffset(d7, d11, vPos2);
                if (Side.TOP.equals((Object)PaginationSkin.this.getPageInformationAlignment())) {
                    d15 = d2;
                    d13 = d2 + d11;
                } else if (Side.RIGHT.equals((Object)PaginationSkin.this.getPageInformationAlignment())) {
                    d14 = d6 - d5 - d10;
                } else if (Side.BOTTOM.equals((Object)PaginationSkin.this.getPageInformationAlignment())) {
                    d13 = d2;
                    d15 = d2 + d9;
                } else if (Side.LEFT.equals((Object)PaginationSkin.this.getPageInformationAlignment())) {
                    d14 = d4;
                }
                this.layoutInArea(this.pageInformation, d14, d15, d10, d11, 0.0, hPos2, vPos2);
            }
            this.layoutInArea(this.controlBox, d12, d13, d8, d9, 0.0, hPos, vPos);
        }
    }
}

