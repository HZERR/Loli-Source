/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.TitledPaneBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Labeled;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class TitledPaneSkin
extends LabeledSkinBase<TitledPane, TitledPaneBehavior> {
    public static final Duration TRANSITION_DURATION = new Duration(350.0);
    private static final boolean CACHE_ANIMATION = PlatformUtil.isEmbedded();
    private final TitleRegion titleRegion;
    private final StackPane contentContainer;
    private Node content;
    private Timeline timeline;
    private double transitionStartValue = 0.0;
    private Rectangle clipRect = new Rectangle();
    private Pos pos;
    private HPos hpos;
    private VPos vpos;
    private DoubleProperty transition;
    private double prefHeightFromAccordion = 0.0;

    public TitledPaneSkin(TitledPane titledPane) {
        super(titledPane, new TitledPaneBehavior(titledPane));
        this.titleRegion = new TitleRegion();
        this.content = ((TitledPane)this.getSkinnable()).getContent();
        this.contentContainer = new StackPane(){
            {
                this.getStyleClass().setAll("content");
                if (TitledPaneSkin.this.content != null) {
                    this.getChildren().setAll(TitledPaneSkin.this.content);
                }
            }
        };
        this.contentContainer.setClip(this.clipRect);
        if (titledPane.isExpanded()) {
            this.setTransition(1.0);
            this.setExpanded(titledPane.isExpanded());
        } else {
            this.setTransition(0.0);
            if (this.content != null) {
                this.content.setVisible(false);
            }
        }
        this.getChildren().setAll(this.contentContainer, this.titleRegion);
        this.registerChangeListener(titledPane.contentProperty(), "CONTENT");
        this.registerChangeListener(titledPane.expandedProperty(), "EXPANDED");
        this.registerChangeListener(titledPane.collapsibleProperty(), "COLLAPSIBLE");
        this.registerChangeListener(titledPane.alignmentProperty(), "ALIGNMENT");
        this.registerChangeListener(titledPane.widthProperty(), "WIDTH");
        this.registerChangeListener(titledPane.heightProperty(), "HEIGHT");
        this.registerChangeListener(this.titleRegion.alignmentProperty(), "TITLE_REGION_ALIGNMENT");
        this.pos = titledPane.getAlignment();
        this.hpos = this.pos == null ? HPos.LEFT : this.pos.getHpos();
        this.vpos = this.pos == null ? VPos.CENTER : this.pos.getVpos();
    }

    public StackPane getContentContainer() {
        return this.contentContainer;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("CONTENT".equals(string)) {
            this.content = ((TitledPane)this.getSkinnable()).getContent();
            if (this.content == null) {
                this.contentContainer.getChildren().clear();
            } else {
                this.contentContainer.getChildren().setAll(this.content);
            }
        } else if ("EXPANDED".equals(string)) {
            this.setExpanded(((TitledPane)this.getSkinnable()).isExpanded());
        } else if ("COLLAPSIBLE".equals(string)) {
            this.titleRegion.update();
        } else if ("ALIGNMENT".equals(string)) {
            this.pos = ((TitledPane)this.getSkinnable()).getAlignment();
            this.hpos = this.pos.getHpos();
            this.vpos = this.pos.getVpos();
        } else if ("TITLE_REGION_ALIGNMENT".equals(string)) {
            this.pos = this.titleRegion.getAlignment();
            this.hpos = this.pos.getHpos();
            this.vpos = this.pos.getVpos();
        } else if ("WIDTH".equals(string)) {
            this.clipRect.setWidth(((TitledPane)this.getSkinnable()).getWidth());
        } else if ("HEIGHT".equals(string)) {
            this.clipRect.setHeight(this.contentContainer.getHeight());
        } else if ("GRAPHIC_TEXT_GAP".equals(string)) {
            this.titleRegion.requestLayout();
        }
    }

    @Override
    protected void updateChildren() {
        if (this.titleRegion != null) {
            this.titleRegion.update();
        }
    }

    private void setExpanded(boolean bl) {
        if (!((TitledPane)this.getSkinnable()).isCollapsible()) {
            this.setTransition(1.0);
            return;
        }
        if (((TitledPane)this.getSkinnable()).isAnimated()) {
            this.transitionStartValue = this.getTransition();
            this.doAnimationTransition();
        } else {
            if (bl) {
                this.setTransition(1.0);
            } else {
                this.setTransition(0.0);
            }
            if (this.content != null) {
                this.content.setVisible(bl);
            }
            ((TitledPane)this.getSkinnable()).requestLayout();
        }
    }

    private void setTransition(double d2) {
        this.transitionProperty().set(d2);
    }

    private double getTransition() {
        return this.transition == null ? 0.0 : this.transition.get();
    }

    private DoubleProperty transitionProperty() {
        if (this.transition == null) {
            this.transition = new SimpleDoubleProperty(this, "transition", 0.0){

                @Override
                protected void invalidated() {
                    TitledPaneSkin.this.contentContainer.requestLayout();
                }
            };
        }
        return this.transition;
    }

    private boolean isInsideAccordion() {
        return ((TitledPane)this.getSkinnable()).getParent() != null && ((TitledPane)this.getSkinnable()).getParent() instanceof Accordion;
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6 = this.snapSize(this.titleRegion.prefHeight(-1.0));
        this.titleRegion.resize(d4, d6);
        this.positionInArea(this.titleRegion, d2, d3, d4, d6, 0.0, HPos.LEFT, VPos.CENTER);
        double d7 = (d5 - d6) * this.getTransition();
        if (this.isInsideAccordion() && this.prefHeightFromAccordion != 0.0) {
            d7 = (this.prefHeightFromAccordion - d6) * this.getTransition();
        }
        d7 = this.snapSize(d7);
        this.contentContainer.resize(d4, d7);
        this.clipRect.setHeight(d7);
        this.positionInArea(this.contentContainer, d2, d3 += this.snapSize(d6), d4, d7, 0.0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.snapSize(this.titleRegion.prefWidth(d2));
        double d8 = this.snapSize(this.contentContainer.minWidth(d2));
        return Math.max(d7, d8) + d6 + d4;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.snapSize(this.titleRegion.prefHeight(d2));
        double d8 = this.contentContainer.minHeight(d2) * this.getTransition();
        return d7 + this.snapSize(d8) + d3 + d5;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.snapSize(this.titleRegion.prefWidth(d2));
        double d8 = this.snapSize(this.contentContainer.prefWidth(d2));
        return Math.max(d7, d8) + d6 + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.snapSize(this.titleRegion.prefHeight(d2));
        double d8 = this.contentContainer.prefHeight(d2) * this.getTransition();
        return d7 + this.snapSize(d8) + d3 + d5;
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        return Double.MAX_VALUE;
    }

    double getTitleRegionSize(double d2) {
        return this.snapSize(this.titleRegion.prefHeight(d2)) + this.snappedTopInset() + this.snappedBottomInset();
    }

    void setMaxTitledPaneHeightForAccordion(double d2) {
        this.prefHeightFromAccordion = d2;
    }

    double getTitledPaneHeightForAccordion() {
        double d2 = this.snapSize(this.titleRegion.prefHeight(-1.0));
        double d3 = (this.prefHeightFromAccordion - d2) * this.getTransition();
        return d2 + this.snapSize(d3) + this.snappedTopInset() + this.snappedBottomInset();
    }

    private void doAnimationTransition() {
        KeyFrame keyFrame;
        KeyFrame keyFrame2;
        Duration duration;
        if (this.content == null) {
            return;
        }
        if (this.timeline != null && this.timeline.getStatus() != Animation.Status.STOPPED) {
            duration = this.timeline.getCurrentTime();
            this.timeline.stop();
        } else {
            duration = TRANSITION_DURATION;
        }
        this.timeline = new Timeline();
        this.timeline.setCycleCount(1);
        if (((TitledPane)this.getSkinnable()).isExpanded()) {
            keyFrame2 = new KeyFrame(Duration.ZERO, actionEvent -> {
                if (CACHE_ANIMATION) {
                    this.content.setCache(true);
                }
                this.content.setVisible(true);
            }, new KeyValue(this.transitionProperty(), this.transitionStartValue));
            keyFrame = new KeyFrame(duration, actionEvent -> {
                if (CACHE_ANIMATION) {
                    this.content.setCache(false);
                }
            }, new KeyValue(this.transitionProperty(), 1, Interpolator.LINEAR));
        } else {
            keyFrame2 = new KeyFrame(Duration.ZERO, actionEvent -> {
                if (CACHE_ANIMATION) {
                    this.content.setCache(true);
                }
            }, new KeyValue(this.transitionProperty(), this.transitionStartValue));
            keyFrame = new KeyFrame(duration, actionEvent -> {
                this.content.setVisible(false);
                if (CACHE_ANIMATION) {
                    this.content.setCache(false);
                }
            }, new KeyValue(this.transitionProperty(), 0, Interpolator.LINEAR));
        }
        this.timeline.getKeyFrames().setAll(keyFrame2, keyFrame);
        this.timeline.play();
    }

    class TitleRegion
    extends StackPane {
        private final StackPane arrowRegion;

        public TitleRegion() {
            this.getStyleClass().setAll("title");
            this.arrowRegion = new StackPane();
            this.arrowRegion.setId("arrowRegion");
            this.arrowRegion.getStyleClass().setAll("arrow-button");
            StackPane stackPane = new StackPane();
            stackPane.setId("arrow");
            stackPane.getStyleClass().setAll("arrow");
            this.arrowRegion.getChildren().setAll(stackPane);
            stackPane.rotateProperty().bind(new DoubleBinding(){
                {
                    this.bind(TitledPaneSkin.this.transitionProperty());
                }

                @Override
                protected double computeValue() {
                    return -90.0 * (1.0 - TitledPaneSkin.this.getTransition());
                }
            });
            this.setAlignment(Pos.CENTER_LEFT);
            this.setOnMouseReleased(mouseEvent -> {
                if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                    return;
                }
                ContextMenu contextMenu = ((TitledPane)TitledPaneSkin.this.getSkinnable()).getContextMenu();
                if (contextMenu != null) {
                    contextMenu.hide();
                }
                if (((TitledPane)TitledPaneSkin.this.getSkinnable()).isCollapsible() && ((TitledPane)TitledPaneSkin.this.getSkinnable()).isFocused()) {
                    ((TitledPaneBehavior)TitledPaneSkin.this.getBehavior()).toggle();
                }
            });
            this.update();
        }

        private void update() {
            this.getChildren().clear();
            TitledPane titledPane = (TitledPane)TitledPaneSkin.this.getSkinnable();
            if (titledPane.isCollapsible()) {
                this.getChildren().add(this.arrowRegion);
            }
            if (TitledPaneSkin.this.graphic != null) {
                TitledPaneSkin.this.graphic.layoutBoundsProperty().removeListener(TitledPaneSkin.this.graphicPropertyChangedListener);
            }
            TitledPaneSkin.this.graphic = titledPane.getGraphic();
            if (TitledPaneSkin.this.isIgnoreGraphic()) {
                if (titledPane.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY) {
                    this.getChildren().clear();
                    this.getChildren().add(this.arrowRegion);
                } else {
                    this.getChildren().add(TitledPaneSkin.this.text);
                }
            } else {
                TitledPaneSkin.this.graphic.layoutBoundsProperty().addListener(TitledPaneSkin.this.graphicPropertyChangedListener);
                if (TitledPaneSkin.this.isIgnoreText()) {
                    this.getChildren().add(TitledPaneSkin.this.graphic);
                } else {
                    this.getChildren().addAll(TitledPaneSkin.this.graphic, TitledPaneSkin.this.text);
                }
            }
            this.setCursor(((TitledPane)TitledPaneSkin.this.getSkinnable()).isCollapsible() ? Cursor.HAND : Cursor.DEFAULT);
        }

        @Override
        protected double computePrefWidth(double d2) {
            double d3 = this.snappedLeftInset();
            double d4 = this.snappedRightInset();
            double d5 = 0.0;
            double d6 = this.labelPrefWidth(d2);
            if (this.arrowRegion != null) {
                d5 = this.snapSize(this.arrowRegion.prefWidth(d2));
            }
            return d3 + d5 + d6 + d4;
        }

        @Override
        protected double computePrefHeight(double d2) {
            double d3 = this.snappedTopInset();
            double d4 = this.snappedBottomInset();
            double d5 = 0.0;
            double d6 = this.labelPrefHeight(d2);
            if (this.arrowRegion != null) {
                d5 = this.snapSize(this.arrowRegion.prefHeight(d2));
            }
            return d3 + Math.max(d5, d6) + d4;
        }

        @Override
        protected void layoutChildren() {
            double d2 = this.snappedTopInset();
            double d3 = this.snappedBottomInset();
            double d4 = this.snappedLeftInset();
            double d5 = this.snappedRightInset();
            double d6 = this.getWidth() - (d4 + d5);
            double d7 = this.getHeight() - (d2 + d3);
            double d8 = this.snapSize(this.arrowRegion.prefWidth(-1.0));
            double d9 = this.snapSize(this.arrowRegion.prefHeight(-1.0));
            double d10 = this.snapSize(Math.min(d6 - d8 / 2.0, this.labelPrefWidth(-1.0)));
            double d11 = this.snapSize(this.labelPrefHeight(-1.0));
            double d12 = d4 + d8 + Utils.computeXOffset(d6 - d8, d10, TitledPaneSkin.this.hpos);
            if (HPos.CENTER == TitledPaneSkin.this.hpos) {
                d12 = d4 + Utils.computeXOffset(d6, d10, TitledPaneSkin.this.hpos);
            }
            double d13 = d2 + Utils.computeYOffset(d7, Math.max(d9, d11), TitledPaneSkin.this.vpos);
            this.arrowRegion.resize(d8, d9);
            this.positionInArea(this.arrowRegion, d4, d2, d8, d7, 0.0, HPos.CENTER, VPos.CENTER);
            TitledPaneSkin.this.layoutLabelInArea(d12, d13, d10, d7, TitledPaneSkin.this.pos);
        }

        private double labelPrefWidth(double d2) {
            Labeled labeled = (Labeled)TitledPaneSkin.this.getSkinnable();
            Font font = TitledPaneSkin.this.text.getFont();
            String string = labeled.getText();
            boolean bl = string == null || string.isEmpty();
            Insets insets = labeled.getLabelPadding();
            double d3 = insets.getLeft() + insets.getRight();
            double d4 = bl ? 0.0 : Utils.computeTextWidth(font, string, 0.0);
            Node node = labeled.getGraphic();
            if (TitledPaneSkin.this.isIgnoreGraphic()) {
                return d4 + d3;
            }
            if (TitledPaneSkin.this.isIgnoreText()) {
                return node.prefWidth(-1.0) + d3;
            }
            if (labeled.getContentDisplay() == ContentDisplay.LEFT || labeled.getContentDisplay() == ContentDisplay.RIGHT) {
                return d4 + labeled.getGraphicTextGap() + node.prefWidth(-1.0) + d3;
            }
            return Math.max(d4, node.prefWidth(-1.0)) + d3;
        }

        private double labelPrefHeight(double d2) {
            double d3;
            Labeled labeled = (Labeled)TitledPaneSkin.this.getSkinnable();
            Font font = TitledPaneSkin.this.text.getFont();
            ContentDisplay contentDisplay = labeled.getContentDisplay();
            double d4 = labeled.getGraphicTextGap();
            Insets insets = labeled.getLabelPadding();
            double d5 = this.snappedLeftInset() + this.snappedRightInset() + insets.getLeft() + insets.getRight();
            String string = labeled.getText();
            if (string != null && string.endsWith("\n")) {
                string = string.substring(0, string.length() - 1);
            }
            if (!(TitledPaneSkin.this.isIgnoreGraphic() || contentDisplay != ContentDisplay.LEFT && contentDisplay != ContentDisplay.RIGHT)) {
                d2 -= TitledPaneSkin.this.graphic.prefWidth(-1.0) + d4;
            }
            double d6 = d3 = Utils.computeTextHeight(font, string, labeled.isWrapText() ? (d2 -= d5) : 0.0, TitledPaneSkin.this.text.getBoundsType());
            if (!TitledPaneSkin.this.isIgnoreGraphic()) {
                Node node = labeled.getGraphic();
                d6 = contentDisplay == ContentDisplay.TOP || contentDisplay == ContentDisplay.BOTTOM ? node.prefHeight(-1.0) + d4 + d3 : Math.max(d3, node.prefHeight(-1.0));
            }
            return d6 + insets.getTop() + insets.getBottom();
        }
    }
}

