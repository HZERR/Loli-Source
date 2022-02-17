/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ScrollPaneBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraverseListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ScrollPaneSkin
extends BehaviorSkinBase<ScrollPane, ScrollPaneBehavior>
implements TraverseListener {
    private static final double DEFAULT_PREF_SIZE = 100.0;
    private static final double DEFAULT_MIN_SIZE = 36.0;
    private static final double DEFAULT_SB_BREADTH = 12.0;
    private static final double DEFAULT_EMBEDDED_SB_BREADTH = 8.0;
    private static final double PAN_THRESHOLD = 0.5;
    private Node scrollNode;
    private double nodeWidth;
    private double nodeHeight;
    private boolean nodeSizeInvalid = true;
    private double posX;
    private double posY;
    private boolean hsbvis;
    private boolean vsbvis;
    private double hsbHeight;
    private double vsbWidth;
    private StackPane viewRect;
    private StackPane viewContent;
    private double contentWidth;
    private double contentHeight;
    private StackPane corner;
    protected ScrollBar hsb;
    protected ScrollBar vsb;
    double pressX;
    double pressY;
    double ohvalue;
    double ovvalue;
    private Cursor saveCursor = null;
    private boolean dragDetected = false;
    private boolean touchDetected = false;
    private boolean mouseDown = false;
    Rectangle clipRect;
    private final InvalidationListener nodeListener = new InvalidationListener(){

        @Override
        public void invalidated(Observable observable) {
            if (!ScrollPaneSkin.this.nodeSizeInvalid) {
                Bounds bounds = ScrollPaneSkin.this.scrollNode.getLayoutBounds();
                double d2 = bounds.getWidth();
                double d3 = bounds.getHeight();
                if (ScrollPaneSkin.this.vsbvis != ScrollPaneSkin.this.determineVerticalSBVisible() || ScrollPaneSkin.this.hsbvis != ScrollPaneSkin.this.determineHorizontalSBVisible() || d2 != 0.0 && ScrollPaneSkin.this.nodeWidth != d2 || d3 != 0.0 && ScrollPaneSkin.this.nodeHeight != d3) {
                    ((ScrollPane)ScrollPaneSkin.this.getSkinnable()).requestLayout();
                } else if (!ScrollPaneSkin.this.dragDetected) {
                    ScrollPaneSkin.this.updateVerticalSB();
                    ScrollPaneSkin.this.updateHorizontalSB();
                }
            }
        }
    };
    private final ChangeListener<Bounds> boundsChangeListener = new ChangeListener<Bounds>(){

        @Override
        public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds bounds2) {
            double d2;
            double d3;
            double d4;
            double d5 = bounds.getHeight();
            double d6 = bounds2.getHeight();
            if (d5 > 0.0 && d5 != d6) {
                d4 = ScrollPaneSkin.this.snapPosition(ScrollPaneSkin.this.snappedTopInset() - ScrollPaneSkin.this.posY / (ScrollPaneSkin.this.vsb.getMax() - ScrollPaneSkin.this.vsb.getMin()) * (d5 - ScrollPaneSkin.this.contentHeight));
                d2 = d4 / (d3 = ScrollPaneSkin.this.snapPosition(ScrollPaneSkin.this.snappedTopInset() - ScrollPaneSkin.this.posY / (ScrollPaneSkin.this.vsb.getMax() - ScrollPaneSkin.this.vsb.getMin()) * (d6 - ScrollPaneSkin.this.contentHeight))) * ScrollPaneSkin.this.vsb.getValue();
                if (d2 < 0.0) {
                    ScrollPaneSkin.this.vsb.setValue(0.0);
                } else if (d2 < 1.0) {
                    ScrollPaneSkin.this.vsb.setValue(d2);
                } else if (d2 > 1.0) {
                    ScrollPaneSkin.this.vsb.setValue(1.0);
                }
            }
            d4 = bounds.getWidth();
            d3 = bounds2.getWidth();
            if (d4 > 0.0 && d4 != d3) {
                double d7;
                d2 = ScrollPaneSkin.this.snapPosition(ScrollPaneSkin.this.snappedLeftInset() - ScrollPaneSkin.this.posX / (ScrollPaneSkin.this.hsb.getMax() - ScrollPaneSkin.this.hsb.getMin()) * (d4 - ScrollPaneSkin.this.contentWidth));
                double d8 = d2 / (d7 = ScrollPaneSkin.this.snapPosition(ScrollPaneSkin.this.snappedLeftInset() - ScrollPaneSkin.this.posX / (ScrollPaneSkin.this.hsb.getMax() - ScrollPaneSkin.this.hsb.getMin()) * (d3 - ScrollPaneSkin.this.contentWidth))) * ScrollPaneSkin.this.hsb.getValue();
                if (d8 < 0.0) {
                    ScrollPaneSkin.this.hsb.setValue(0.0);
                } else if (d8 < 1.0) {
                    ScrollPaneSkin.this.hsb.setValue(d8);
                } else if (d8 > 1.0) {
                    ScrollPaneSkin.this.hsb.setValue(1.0);
                }
            }
        }
    };
    Timeline sbTouchTimeline;
    KeyFrame sbTouchKF1;
    KeyFrame sbTouchKF2;
    Timeline contentsToViewTimeline;
    KeyFrame contentsToViewKF1;
    KeyFrame contentsToViewKF2;
    KeyFrame contentsToViewKF3;
    private boolean tempVisibility;
    private DoubleProperty contentPosX;
    private DoubleProperty contentPosY;

    public ScrollPaneSkin(ScrollPane scrollPane) {
        super(scrollPane, new ScrollPaneBehavior(scrollPane));
        this.initialize();
        this.registerChangeListener(scrollPane.contentProperty(), "NODE");
        this.registerChangeListener(scrollPane.fitToWidthProperty(), "FIT_TO_WIDTH");
        this.registerChangeListener(scrollPane.fitToHeightProperty(), "FIT_TO_HEIGHT");
        this.registerChangeListener(scrollPane.hbarPolicyProperty(), "HBAR_POLICY");
        this.registerChangeListener(scrollPane.vbarPolicyProperty(), "VBAR_POLICY");
        this.registerChangeListener(scrollPane.hvalueProperty(), "HVALUE");
        this.registerChangeListener(scrollPane.hmaxProperty(), "HMAX");
        this.registerChangeListener(scrollPane.hminProperty(), "HMIN");
        this.registerChangeListener(scrollPane.vvalueProperty(), "VVALUE");
        this.registerChangeListener(scrollPane.vmaxProperty(), "VMAX");
        this.registerChangeListener(scrollPane.vminProperty(), "VMIN");
        this.registerChangeListener(scrollPane.prefViewportWidthProperty(), "VIEWPORT_SIZE_HINT");
        this.registerChangeListener(scrollPane.prefViewportHeightProperty(), "VIEWPORT_SIZE_HINT");
        this.registerChangeListener(scrollPane.minViewportWidthProperty(), "VIEWPORT_SIZE_HINT");
        this.registerChangeListener(scrollPane.minViewportHeightProperty(), "VIEWPORT_SIZE_HINT");
    }

    private void initialize() {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        this.scrollNode = scrollPane.getContent();
        ParentTraversalEngine parentTraversalEngine = new ParentTraversalEngine((Parent)((Object)this.getSkinnable()));
        parentTraversalEngine.addTraverseListener(this);
        ((ScrollPane)this.getSkinnable()).setImpl_traversalEngine(parentTraversalEngine);
        if (this.scrollNode != null) {
            this.scrollNode.layoutBoundsProperty().addListener(this.nodeListener);
            this.scrollNode.layoutBoundsProperty().addListener(this.boundsChangeListener);
        }
        this.viewRect = new StackPane(){

            @Override
            protected void layoutChildren() {
                ScrollPaneSkin.this.viewContent.resize(this.getWidth(), this.getHeight());
            }
        };
        this.viewRect.setManaged(false);
        this.viewRect.setCache(true);
        this.viewRect.getStyleClass().add("viewport");
        this.clipRect = new Rectangle();
        this.viewRect.setClip(this.clipRect);
        this.hsb = new ScrollBar();
        this.vsb = new ScrollBar();
        this.vsb.setOrientation(Orientation.VERTICAL);
        EventHandler<MouseEvent> eventHandler = mouseEvent -> ((ScrollPane)this.getSkinnable()).requestFocus();
        this.hsb.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.vsb.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.corner = new StackPane();
        this.corner.getStyleClass().setAll("corner");
        this.viewContent = new StackPane(){

            @Override
            public void requestLayout() {
                ScrollPaneSkin.this.nodeSizeInvalid = true;
                super.requestLayout();
                ((ScrollPane)ScrollPaneSkin.this.getSkinnable()).requestLayout();
            }

            @Override
            protected void layoutChildren() {
                if (ScrollPaneSkin.this.nodeSizeInvalid) {
                    ScrollPaneSkin.this.computeScrollNodeSize(this.getWidth(), this.getHeight());
                }
                if (ScrollPaneSkin.this.scrollNode != null && ScrollPaneSkin.this.scrollNode.isResizable()) {
                    ScrollPaneSkin.this.scrollNode.resize(this.snapSize(ScrollPaneSkin.this.nodeWidth), this.snapSize(ScrollPaneSkin.this.nodeHeight));
                    if (ScrollPaneSkin.this.vsbvis != ScrollPaneSkin.this.determineVerticalSBVisible() || ScrollPaneSkin.this.hsbvis != ScrollPaneSkin.this.determineHorizontalSBVisible()) {
                        ((ScrollPane)ScrollPaneSkin.this.getSkinnable()).requestLayout();
                    }
                }
                if (ScrollPaneSkin.this.scrollNode != null) {
                    ScrollPaneSkin.this.scrollNode.relocate(0.0, 0.0);
                }
            }
        };
        this.viewRect.getChildren().add(this.viewContent);
        if (this.scrollNode != null) {
            this.viewContent.getChildren().add(this.scrollNode);
            this.viewRect.nodeOrientationProperty().bind(this.scrollNode.nodeOrientationProperty());
        }
        this.getChildren().clear();
        this.getChildren().addAll(this.viewRect, this.vsb, this.hsb, this.corner);
        InvalidationListener invalidationListener = observable -> {
            this.posY = !IS_TOUCH_SUPPORTED ? com.sun.javafx.util.Utils.clamp(((ScrollPane)this.getSkinnable()).getVmin(), this.vsb.getValue(), ((ScrollPane)this.getSkinnable()).getVmax()) : this.vsb.getValue();
            this.updatePosY();
        };
        this.vsb.valueProperty().addListener(invalidationListener);
        InvalidationListener invalidationListener2 = observable -> {
            this.posX = !IS_TOUCH_SUPPORTED ? com.sun.javafx.util.Utils.clamp(((ScrollPane)this.getSkinnable()).getHmin(), this.hsb.getValue(), ((ScrollPane)this.getSkinnable()).getHmax()) : this.hsb.getValue();
            this.updatePosX();
        };
        this.hsb.valueProperty().addListener(invalidationListener2);
        this.viewRect.setOnMousePressed(mouseEvent -> {
            this.mouseDown = true;
            if (IS_TOUCH_SUPPORTED) {
                this.startSBReleasedAnimation();
            }
            this.pressX = mouseEvent.getX();
            this.pressY = mouseEvent.getY();
            this.ohvalue = this.hsb.getValue();
            this.ovvalue = this.vsb.getValue();
        });
        this.viewRect.setOnDragDetected(mouseEvent -> {
            if (IS_TOUCH_SUPPORTED) {
                this.startSBReleasedAnimation();
            }
            if (((ScrollPane)this.getSkinnable()).isPannable()) {
                this.dragDetected = true;
                if (this.saveCursor == null) {
                    this.saveCursor = ((ScrollPane)this.getSkinnable()).getCursor();
                    if (this.saveCursor == null) {
                        this.saveCursor = Cursor.DEFAULT;
                    }
                    ((ScrollPane)this.getSkinnable()).setCursor(Cursor.MOVE);
                    ((ScrollPane)this.getSkinnable()).requestLayout();
                }
            }
        });
        this.viewRect.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            this.mouseDown = false;
            if (this.dragDetected) {
                if (this.saveCursor != null) {
                    ((ScrollPane)this.getSkinnable()).setCursor(this.saveCursor);
                    this.saveCursor = null;
                    ((ScrollPane)this.getSkinnable()).requestLayout();
                }
                this.dragDetected = false;
            }
            if ((this.posY > ((ScrollPane)this.getSkinnable()).getVmax() || this.posY < ((ScrollPane)this.getSkinnable()).getVmin() || this.posX > ((ScrollPane)this.getSkinnable()).getHmax() || this.posX < ((ScrollPane)this.getSkinnable()).getHmin()) && !this.touchDetected) {
                this.startContentsToViewport();
            }
        });
        this.viewRect.setOnMouseDragged(mouseEvent -> {
            if (IS_TOUCH_SUPPORTED) {
                this.startSBReleasedAnimation();
            }
            if (((ScrollPane)this.getSkinnable()).isPannable() || IS_TOUCH_SUPPORTED) {
                double d2;
                double d3 = this.pressX - mouseEvent.getX();
                double d4 = this.pressY - mouseEvent.getY();
                if (this.hsb.getVisibleAmount() > 0.0 && this.hsb.getVisibleAmount() < this.hsb.getMax() && Math.abs(d3) > 0.5) {
                    if (this.isReverseNodeOrientation()) {
                        d3 = -d3;
                    }
                    d2 = this.ohvalue + d3 / (this.nodeWidth - this.viewRect.getWidth()) * (this.hsb.getMax() - this.hsb.getMin());
                    if (!IS_TOUCH_SUPPORTED) {
                        if (d2 > this.hsb.getMax()) {
                            d2 = this.hsb.getMax();
                        } else if (d2 < this.hsb.getMin()) {
                            d2 = this.hsb.getMin();
                        }
                        this.hsb.setValue(d2);
                    } else {
                        this.hsb.setValue(d2);
                    }
                }
                if (this.vsb.getVisibleAmount() > 0.0 && this.vsb.getVisibleAmount() < this.vsb.getMax() && Math.abs(d4) > 0.5) {
                    d2 = this.ovvalue + d4 / (this.nodeHeight - this.viewRect.getHeight()) * (this.vsb.getMax() - this.vsb.getMin());
                    if (!IS_TOUCH_SUPPORTED) {
                        if (d2 > this.vsb.getMax()) {
                            d2 = this.vsb.getMax();
                        } else if (d2 < this.vsb.getMin()) {
                            d2 = this.vsb.getMin();
                        }
                        this.vsb.setValue(d2);
                    } else {
                        this.vsb.setValue(d2);
                    }
                }
            }
            mouseEvent.consume();
        });
        EventDispatcher eventDispatcher = (event, eventDispatchChain) -> event;
        EventDispatcher eventDispatcher2 = this.hsb.getEventDispatcher();
        this.hsb.setEventDispatcher((event, eventDispatchChain) -> {
            if (event.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent)event).isDirect()) {
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher);
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher2);
                return eventDispatchChain.dispatchEvent(event);
            }
            return eventDispatcher2.dispatchEvent(event, eventDispatchChain);
        });
        EventDispatcher eventDispatcher3 = this.vsb.getEventDispatcher();
        this.vsb.setEventDispatcher((event, eventDispatchChain) -> {
            if (event.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent)event).isDirect()) {
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher);
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher3);
                return eventDispatchChain.dispatchEvent(event);
            }
            return eventDispatcher3.dispatchEvent(event, eventDispatchChain);
        });
        this.viewRect.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            double d2;
            double d3;
            double d4;
            if (IS_TOUCH_SUPPORTED) {
                this.startSBReleasedAnimation();
            }
            if (this.vsb.getVisibleAmount() < this.vsb.getMax()) {
                d4 = ((ScrollPane)this.getSkinnable()).getVmax() - ((ScrollPane)this.getSkinnable()).getVmin();
                d3 = this.nodeHeight > 0.0 ? d4 / this.nodeHeight : 0.0;
                d2 = this.vsb.getValue() + -scrollEvent.getDeltaY() * d3;
                if (!IS_TOUCH_SUPPORTED) {
                    if (scrollEvent.getDeltaY() > 0.0 && this.vsb.getValue() > this.vsb.getMin() || scrollEvent.getDeltaY() < 0.0 && this.vsb.getValue() < this.vsb.getMax()) {
                        this.vsb.setValue(d2);
                        scrollEvent.consume();
                    }
                } else if (!scrollEvent.isInertia() || scrollEvent.isInertia() && (this.contentsToViewTimeline == null || this.contentsToViewTimeline.getStatus() == Animation.Status.STOPPED)) {
                    this.vsb.setValue(d2);
                    if ((d2 > this.vsb.getMax() || d2 < this.vsb.getMin()) && !this.mouseDown && !this.touchDetected) {
                        this.startContentsToViewport();
                    }
                    scrollEvent.consume();
                }
            }
            if (this.hsb.getVisibleAmount() < this.hsb.getMax()) {
                d4 = ((ScrollPane)this.getSkinnable()).getHmax() - ((ScrollPane)this.getSkinnable()).getHmin();
                d3 = this.nodeWidth > 0.0 ? d4 / this.nodeWidth : 0.0;
                d2 = this.hsb.getValue() + -scrollEvent.getDeltaX() * d3;
                if (!IS_TOUCH_SUPPORTED) {
                    if (scrollEvent.getDeltaX() > 0.0 && this.hsb.getValue() > this.hsb.getMin() || scrollEvent.getDeltaX() < 0.0 && this.hsb.getValue() < this.hsb.getMax()) {
                        this.hsb.setValue(d2);
                        scrollEvent.consume();
                    }
                } else if (!scrollEvent.isInertia() || scrollEvent.isInertia() && (this.contentsToViewTimeline == null || this.contentsToViewTimeline.getStatus() == Animation.Status.STOPPED)) {
                    this.hsb.setValue(d2);
                    if ((d2 > this.hsb.getMax() || d2 < this.hsb.getMin()) && !this.mouseDown && !this.touchDetected) {
                        this.startContentsToViewport();
                    }
                    scrollEvent.consume();
                }
            }
        });
        ((ScrollPane)this.getSkinnable()).addEventHandler(TouchEvent.TOUCH_PRESSED, touchEvent -> {
            this.touchDetected = true;
            this.startSBReleasedAnimation();
            touchEvent.consume();
        });
        ((ScrollPane)this.getSkinnable()).addEventHandler(TouchEvent.TOUCH_RELEASED, touchEvent -> {
            this.touchDetected = false;
            touchEvent.consume();
        });
        this.consumeMouseEvents(false);
        this.hsb.setValue(scrollPane.getHvalue());
        this.vsb.setValue(scrollPane.getVvalue());
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("NODE".equals(string)) {
            if (this.scrollNode != ((ScrollPane)this.getSkinnable()).getContent()) {
                if (this.scrollNode != null) {
                    this.scrollNode.layoutBoundsProperty().removeListener(this.nodeListener);
                    this.scrollNode.layoutBoundsProperty().removeListener(this.boundsChangeListener);
                    this.viewContent.getChildren().remove(this.scrollNode);
                }
                this.scrollNode = ((ScrollPane)this.getSkinnable()).getContent();
                if (this.scrollNode != null) {
                    this.nodeWidth = this.snapSize(this.scrollNode.getLayoutBounds().getWidth());
                    this.nodeHeight = this.snapSize(this.scrollNode.getLayoutBounds().getHeight());
                    this.viewContent.getChildren().setAll(this.scrollNode);
                    this.scrollNode.layoutBoundsProperty().addListener(this.nodeListener);
                    this.scrollNode.layoutBoundsProperty().addListener(this.boundsChangeListener);
                }
            }
            ((ScrollPane)this.getSkinnable()).requestLayout();
        } else if ("FIT_TO_WIDTH".equals(string) || "FIT_TO_HEIGHT".equals(string)) {
            ((ScrollPane)this.getSkinnable()).requestLayout();
            this.viewRect.requestLayout();
        } else if ("HBAR_POLICY".equals(string) || "VBAR_POLICY".equals(string)) {
            ((ScrollPane)this.getSkinnable()).requestLayout();
        } else if ("HVALUE".equals(string)) {
            this.hsb.setValue(((ScrollPane)this.getSkinnable()).getHvalue());
        } else if ("HMAX".equals(string)) {
            this.hsb.setMax(((ScrollPane)this.getSkinnable()).getHmax());
        } else if ("HMIN".equals(string)) {
            this.hsb.setMin(((ScrollPane)this.getSkinnable()).getHmin());
        } else if ("VVALUE".equals(string)) {
            this.vsb.setValue(((ScrollPane)this.getSkinnable()).getVvalue());
        } else if ("VMAX".equals(string)) {
            this.vsb.setMax(((ScrollPane)this.getSkinnable()).getVmax());
        } else if ("VMIN".equals(string)) {
            this.vsb.setMin(((ScrollPane)this.getSkinnable()).getVmin());
        } else if ("VIEWPORT_SIZE_HINT".equals(string)) {
            ((ScrollPane)this.getSkinnable()).requestLayout();
        }
    }

    void scrollBoundsIntoView(Bounds bounds) {
        double d2;
        double d3 = 0.0;
        double d4 = 0.0;
        if (bounds.getMaxX() > this.contentWidth) {
            d3 = bounds.getMinX() - this.snappedLeftInset();
        }
        if (bounds.getMinX() < this.snappedLeftInset()) {
            d3 = bounds.getMaxX() - this.contentWidth - this.snappedLeftInset();
        }
        if (bounds.getMaxY() > this.snappedTopInset() + this.contentHeight) {
            d4 = bounds.getMinY() - this.snappedTopInset();
        }
        if (bounds.getMinY() < this.snappedTopInset()) {
            d4 = bounds.getMaxY() - this.contentHeight - this.snappedTopInset();
        }
        if (d3 != 0.0) {
            d2 = d3 * (this.hsb.getMax() - this.hsb.getMin()) / (this.nodeWidth - this.contentWidth);
            d2 += -1.0 * Math.signum(d2) * this.hsb.getUnitIncrement() / 5.0;
            this.hsb.setValue(this.hsb.getValue() + d2);
            ((ScrollPane)this.getSkinnable()).requestLayout();
        }
        if (d4 != 0.0) {
            d2 = d4 * (this.vsb.getMax() - this.vsb.getMin()) / (this.nodeHeight - this.contentHeight);
            d2 += -1.0 * Math.signum(d2) * this.vsb.getUnitIncrement() / 5.0;
            this.vsb.setValue(this.vsb.getValue() + d2);
            ((ScrollPane)this.getSkinnable()).requestLayout();
        }
    }

    @Override
    public void onTraverse(Node node, Bounds bounds) {
        this.scrollBoundsIntoView(bounds);
    }

    public void hsbIncrement() {
        if (this.hsb != null) {
            this.hsb.increment();
        }
    }

    public void hsbDecrement() {
        if (this.hsb != null) {
            this.hsb.decrement();
        }
    }

    public void hsbPageIncrement() {
        if (this.hsb != null) {
            this.hsb.increment();
        }
    }

    public void hsbPageDecrement() {
        if (this.hsb != null) {
            this.hsb.decrement();
        }
    }

    public void vsbIncrement() {
        if (this.vsb != null) {
            this.vsb.increment();
        }
    }

    public void vsbDecrement() {
        if (this.vsb != null) {
            this.vsb.decrement();
        }
    }

    public void vsbPageIncrement() {
        if (this.vsb != null) {
            this.vsb.increment();
        }
    }

    public void vsbPageDecrement() {
        if (this.vsb != null) {
            this.vsb.decrement();
        }
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        double d7 = this.computeVsbSizeHint(scrollPane);
        double d8 = d7 + this.snappedLeftInset() + this.snappedRightInset();
        if (scrollPane.getPrefViewportWidth() > 0.0) {
            return scrollPane.getPrefViewportWidth() + d8;
        }
        if (scrollPane.getContent() != null) {
            return scrollPane.getContent().prefWidth(d2) + d8;
        }
        return Math.max(d8, 100.0);
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        double d7 = this.computeHsbSizeHint(scrollPane);
        double d8 = d7 + this.snappedTopInset() + this.snappedBottomInset();
        if (scrollPane.getPrefViewportHeight() > 0.0) {
            return scrollPane.getPrefViewportHeight() + d8;
        }
        if (scrollPane.getContent() != null) {
            return scrollPane.getContent().prefHeight(d2) + d8;
        }
        return Math.max(d8, 100.0);
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        double d7 = this.computeVsbSizeHint(scrollPane);
        double d8 = d7 + this.snappedLeftInset() + this.snappedRightInset();
        if (scrollPane.getMinViewportWidth() > 0.0) {
            return scrollPane.getMinViewportWidth() + d8;
        }
        double d9 = this.corner.minWidth(-1.0);
        return d9 > 0.0 ? 3.0 * d9 : 36.0;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        double d7 = this.computeHsbSizeHint(scrollPane);
        double d8 = d7 + this.snappedTopInset() + this.snappedBottomInset();
        if (scrollPane.getMinViewportHeight() > 0.0) {
            return scrollPane.getMinViewportHeight() + d8;
        }
        double d9 = this.corner.minHeight(-1.0);
        return d9 > 0.0 ? 3.0 * d9 : 36.0;
    }

    private double computeHsbSizeHint(ScrollPane scrollPane) {
        return scrollPane.getHbarPolicy() == ScrollPane.ScrollBarPolicy.ALWAYS || scrollPane.getHbarPolicy() == ScrollPane.ScrollBarPolicy.AS_NEEDED && (scrollPane.getPrefViewportHeight() > 0.0 || scrollPane.getMinViewportHeight() > 0.0) ? this.hsb.prefHeight(-1.0) : 0.0;
    }

    private double computeVsbSizeHint(ScrollPane scrollPane) {
        return scrollPane.getVbarPolicy() == ScrollPane.ScrollBarPolicy.ALWAYS || scrollPane.getVbarPolicy() == ScrollPane.ScrollBarPolicy.AS_NEEDED && (scrollPane.getPrefViewportWidth() > 0.0 || scrollPane.getMinViewportWidth() > 0.0) ? this.vsb.prefWidth(-1.0) : 0.0;
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        Insets insets = scrollPane.getPadding();
        double d6 = this.snapSize(insets.getRight());
        double d7 = this.snapSize(insets.getLeft());
        double d8 = this.snapSize(insets.getTop());
        double d9 = this.snapSize(insets.getBottom());
        this.vsb.setMin(scrollPane.getVmin());
        this.vsb.setMax(scrollPane.getVmax());
        this.hsb.setMin(scrollPane.getHmin());
        this.hsb.setMax(scrollPane.getHmax());
        this.contentWidth = d4;
        this.contentHeight = d5;
        double d10 = 0.0;
        double d11 = 0.0;
        this.computeScrollNodeSize(this.contentWidth, this.contentHeight);
        this.computeScrollBarSize();
        for (int i2 = 0; i2 < 2; ++i2) {
            this.vsbvis = this.determineVerticalSBVisible();
            this.hsbvis = this.determineHorizontalSBVisible();
            if (this.vsbvis && !IS_TOUCH_SUPPORTED) {
                this.contentWidth = d4 - this.vsbWidth;
            }
            d10 = d4 + d7 + d6 - (this.vsbvis ? this.vsbWidth : 0.0);
            if (this.hsbvis && !IS_TOUCH_SUPPORTED) {
                this.contentHeight = d5 - this.hsbHeight;
            }
            d11 = d5 + d8 + d9 - (this.hsbvis ? this.hsbHeight : 0.0);
        }
        if (this.scrollNode != null && this.scrollNode.isResizable()) {
            if (this.vsbvis && this.hsbvis) {
                this.computeScrollNodeSize(this.contentWidth, this.contentHeight);
            } else if (this.hsbvis && !this.vsbvis) {
                this.computeScrollNodeSize(this.contentWidth, this.contentHeight);
                this.vsbvis = this.determineVerticalSBVisible();
                if (this.vsbvis) {
                    this.contentWidth -= this.vsbWidth;
                    d10 -= this.vsbWidth;
                    this.computeScrollNodeSize(this.contentWidth, this.contentHeight);
                }
            } else if (this.vsbvis && !this.hsbvis) {
                this.computeScrollNodeSize(this.contentWidth, this.contentHeight);
                this.hsbvis = this.determineHorizontalSBVisible();
                if (this.hsbvis) {
                    this.contentHeight -= this.hsbHeight;
                    d11 -= this.hsbHeight;
                    this.computeScrollNodeSize(this.contentWidth, this.contentHeight);
                }
            }
        }
        double d12 = this.snappedLeftInset() - d7;
        double d13 = this.snappedTopInset() - d8;
        this.vsb.setVisible(this.vsbvis);
        if (this.vsbvis) {
            this.vsb.resizeRelocate(this.snappedLeftInset() + d4 - this.vsbWidth + (d6 < 1.0 ? 0.0 : d6 - 1.0), d13, this.vsbWidth, d11);
        }
        this.updateVerticalSB();
        this.hsb.setVisible(this.hsbvis);
        if (this.hsbvis) {
            this.hsb.resizeRelocate(d12, this.snappedTopInset() + d5 - this.hsbHeight + (d9 < 1.0 ? 0.0 : d9 - 1.0), d10, this.hsbHeight);
        }
        this.updateHorizontalSB();
        this.viewRect.resizeRelocate(this.snappedLeftInset(), this.snappedTopInset(), this.snapSize(this.contentWidth), this.snapSize(this.contentHeight));
        this.resetClip();
        if (this.vsbvis && this.hsbvis) {
            this.corner.setVisible(true);
            double d14 = this.vsbWidth;
            double d15 = this.hsbHeight;
            this.corner.resizeRelocate(this.snapPosition(this.vsb.getLayoutX()), this.snapPosition(this.hsb.getLayoutY()), this.snapSize(d14), this.snapSize(d15));
        } else {
            this.corner.setVisible(false);
        }
        scrollPane.setViewportBounds(new BoundingBox(this.snapPosition(this.viewContent.getLayoutX()), this.snapPosition(this.viewContent.getLayoutY()), this.snapSize(this.contentWidth), this.snapSize(this.contentHeight)));
    }

    private void computeScrollNodeSize(double d2, double d3) {
        if (this.scrollNode != null) {
            if (this.scrollNode.isResizable()) {
                ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
                Orientation orientation = this.scrollNode.getContentBias();
                if (orientation == null) {
                    this.nodeWidth = this.snapSize(Utils.boundedSize(scrollPane.isFitToWidth() ? d2 : this.scrollNode.prefWidth(-1.0), this.scrollNode.minWidth(-1.0), this.scrollNode.maxWidth(-1.0)));
                    this.nodeHeight = this.snapSize(Utils.boundedSize(scrollPane.isFitToHeight() ? d3 : this.scrollNode.prefHeight(-1.0), this.scrollNode.minHeight(-1.0), this.scrollNode.maxHeight(-1.0)));
                } else if (orientation == Orientation.HORIZONTAL) {
                    this.nodeWidth = this.snapSize(Utils.boundedSize(scrollPane.isFitToWidth() ? d2 : this.scrollNode.prefWidth(-1.0), this.scrollNode.minWidth(-1.0), this.scrollNode.maxWidth(-1.0)));
                    this.nodeHeight = this.snapSize(Utils.boundedSize(scrollPane.isFitToHeight() ? d3 : this.scrollNode.prefHeight(this.nodeWidth), this.scrollNode.minHeight(this.nodeWidth), this.scrollNode.maxHeight(this.nodeWidth)));
                } else {
                    this.nodeHeight = this.snapSize(Utils.boundedSize(scrollPane.isFitToHeight() ? d3 : this.scrollNode.prefHeight(-1.0), this.scrollNode.minHeight(-1.0), this.scrollNode.maxHeight(-1.0)));
                    this.nodeWidth = this.snapSize(Utils.boundedSize(scrollPane.isFitToWidth() ? d2 : this.scrollNode.prefWidth(this.nodeHeight), this.scrollNode.minWidth(this.nodeHeight), this.scrollNode.maxWidth(this.nodeHeight)));
                }
            } else {
                this.nodeWidth = this.snapSize(this.scrollNode.getLayoutBounds().getWidth());
                this.nodeHeight = this.snapSize(this.scrollNode.getLayoutBounds().getHeight());
            }
            this.nodeSizeInvalid = false;
        }
    }

    private boolean isReverseNodeOrientation() {
        return this.scrollNode != null && ((ScrollPane)this.getSkinnable()).getEffectiveNodeOrientation() != this.scrollNode.getEffectiveNodeOrientation();
    }

    private boolean determineHorizontalSBVisible() {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        if (IS_TOUCH_SUPPORTED) {
            return this.tempVisibility && this.nodeWidth > this.contentWidth;
        }
        ScrollPane.ScrollBarPolicy scrollBarPolicy = scrollPane.getHbarPolicy();
        return ScrollPane.ScrollBarPolicy.NEVER == scrollBarPolicy ? false : (ScrollPane.ScrollBarPolicy.ALWAYS == scrollBarPolicy ? true : (scrollPane.isFitToWidth() && this.scrollNode != null && this.scrollNode.isResizable() ? this.nodeWidth > this.contentWidth && this.scrollNode.minWidth(-1.0) > this.contentWidth : this.nodeWidth > this.contentWidth));
    }

    private boolean determineVerticalSBVisible() {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        if (IS_TOUCH_SUPPORTED) {
            return this.tempVisibility && this.nodeHeight > this.contentHeight;
        }
        ScrollPane.ScrollBarPolicy scrollBarPolicy = scrollPane.getVbarPolicy();
        return ScrollPane.ScrollBarPolicy.NEVER == scrollBarPolicy ? false : (ScrollPane.ScrollBarPolicy.ALWAYS == scrollBarPolicy ? true : (scrollPane.isFitToHeight() && this.scrollNode != null && this.scrollNode.isResizable() ? this.nodeHeight > this.contentHeight && this.scrollNode.minHeight(-1.0) > this.contentHeight : this.nodeHeight > this.contentHeight));
    }

    private void computeScrollBarSize() {
        this.vsbWidth = this.snapSize(this.vsb.prefWidth(-1.0));
        if (this.vsbWidth == 0.0) {
            this.vsbWidth = IS_TOUCH_SUPPORTED ? 8.0 : 12.0;
        }
        this.hsbHeight = this.snapSize(this.hsb.prefHeight(-1.0));
        if (this.hsbHeight == 0.0) {
            this.hsbHeight = IS_TOUCH_SUPPORTED ? 8.0 : 12.0;
        }
    }

    private void updateHorizontalSB() {
        double d2 = this.nodeWidth * (this.hsb.getMax() - this.hsb.getMin());
        if (d2 > 0.0) {
            this.hsb.setVisibleAmount(this.contentWidth / d2);
            this.hsb.setBlockIncrement(0.9 * this.hsb.getVisibleAmount());
            this.hsb.setUnitIncrement(0.1 * this.hsb.getVisibleAmount());
        } else {
            this.hsb.setVisibleAmount(0.0);
            this.hsb.setBlockIncrement(0.0);
            this.hsb.setUnitIncrement(0.0);
        }
        if (this.hsb.isVisible()) {
            this.updatePosX();
        } else if (this.nodeWidth > this.contentWidth) {
            this.updatePosX();
        } else {
            this.viewContent.setLayoutX(0.0);
        }
    }

    private void updateVerticalSB() {
        double d2 = this.nodeHeight * (this.vsb.getMax() - this.vsb.getMin());
        if (d2 > 0.0) {
            this.vsb.setVisibleAmount(this.contentHeight / d2);
            this.vsb.setBlockIncrement(0.9 * this.vsb.getVisibleAmount());
            this.vsb.setUnitIncrement(0.1 * this.vsb.getVisibleAmount());
        } else {
            this.vsb.setVisibleAmount(0.0);
            this.vsb.setBlockIncrement(0.0);
            this.vsb.setUnitIncrement(0.0);
        }
        if (this.vsb.isVisible()) {
            this.updatePosY();
        } else if (this.nodeHeight > this.contentHeight) {
            this.updatePosY();
        } else {
            this.viewContent.setLayoutY(0.0);
        }
    }

    private double updatePosX() {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        double d2 = this.isReverseNodeOrientation() ? this.hsb.getMax() - (this.posX - this.hsb.getMin()) : this.posX;
        double d3 = Math.min(-d2 / (this.hsb.getMax() - this.hsb.getMin()) * (this.nodeWidth - this.contentWidth), 0.0);
        this.viewContent.setLayoutX(this.snapPosition(d3));
        if (!scrollPane.hvalueProperty().isBound()) {
            scrollPane.setHvalue(com.sun.javafx.util.Utils.clamp(scrollPane.getHmin(), this.posX, scrollPane.getHmax()));
        }
        return this.posX;
    }

    private double updatePosY() {
        ScrollPane scrollPane = (ScrollPane)this.getSkinnable();
        double d2 = Math.min(-this.posY / (this.vsb.getMax() - this.vsb.getMin()) * (this.nodeHeight - this.contentHeight), 0.0);
        this.viewContent.setLayoutY(this.snapPosition(d2));
        if (!scrollPane.vvalueProperty().isBound()) {
            scrollPane.setVvalue(com.sun.javafx.util.Utils.clamp(scrollPane.getVmin(), this.posY, scrollPane.getVmax()));
        }
        return this.posY;
    }

    private void resetClip() {
        this.clipRect.setWidth(this.snapSize(this.contentWidth));
        this.clipRect.setHeight(this.snapSize(this.contentHeight));
    }

    protected void startSBReleasedAnimation() {
        if (this.sbTouchTimeline == null) {
            this.sbTouchTimeline = new Timeline();
            this.sbTouchKF1 = new KeyFrame(Duration.millis(0.0), actionEvent -> {
                this.tempVisibility = true;
                if (this.touchDetected || this.mouseDown) {
                    this.sbTouchTimeline.playFromStart();
                }
            }, new KeyValue[0]);
            this.sbTouchKF2 = new KeyFrame(Duration.millis(1000.0), actionEvent -> {
                this.tempVisibility = false;
                ((ScrollPane)this.getSkinnable()).requestLayout();
            }, new KeyValue[0]);
            this.sbTouchTimeline.getKeyFrames().addAll(this.sbTouchKF1, this.sbTouchKF2);
        }
        this.sbTouchTimeline.playFromStart();
    }

    protected void startContentsToViewport() {
        double d2 = this.posX;
        double d3 = this.posY;
        this.setContentPosX(this.posX);
        this.setContentPosY(this.posY);
        if (this.posY > ((ScrollPane)this.getSkinnable()).getVmax()) {
            d3 = ((ScrollPane)this.getSkinnable()).getVmax();
        } else if (this.posY < ((ScrollPane)this.getSkinnable()).getVmin()) {
            d3 = ((ScrollPane)this.getSkinnable()).getVmin();
        }
        if (this.posX > ((ScrollPane)this.getSkinnable()).getHmax()) {
            d2 = ((ScrollPane)this.getSkinnable()).getHmax();
        } else if (this.posX < ((ScrollPane)this.getSkinnable()).getHmin()) {
            d2 = ((ScrollPane)this.getSkinnable()).getHmin();
        }
        if (!IS_TOUCH_SUPPORTED) {
            this.startSBReleasedAnimation();
        }
        if (this.contentsToViewTimeline != null) {
            this.contentsToViewTimeline.stop();
        }
        this.contentsToViewTimeline = new Timeline();
        this.contentsToViewKF1 = new KeyFrame(Duration.millis(50.0), new KeyValue[0]);
        this.contentsToViewKF2 = new KeyFrame(Duration.millis(150.0), actionEvent -> ((ScrollPane)this.getSkinnable()).requestLayout(), new KeyValue(this.contentPosX, d2), new KeyValue(this.contentPosY, d3));
        this.contentsToViewKF3 = new KeyFrame(Duration.millis(1500.0), new KeyValue[0]);
        this.contentsToViewTimeline.getKeyFrames().addAll(this.contentsToViewKF1, this.contentsToViewKF2, this.contentsToViewKF3);
        this.contentsToViewTimeline.playFromStart();
    }

    private void setContentPosX(double d2) {
        this.contentPosXProperty().set(d2);
    }

    private double getContentPosX() {
        return this.contentPosX == null ? 0.0 : this.contentPosX.get();
    }

    private DoubleProperty contentPosXProperty() {
        if (this.contentPosX == null) {
            this.contentPosX = new DoublePropertyBase(){

                @Override
                protected void invalidated() {
                    ScrollPaneSkin.this.hsb.setValue(ScrollPaneSkin.this.getContentPosX());
                    ((ScrollPane)ScrollPaneSkin.this.getSkinnable()).requestLayout();
                }

                @Override
                public Object getBean() {
                    return ScrollPaneSkin.this;
                }

                @Override
                public String getName() {
                    return "contentPosX";
                }
            };
        }
        return this.contentPosX;
    }

    private void setContentPosY(double d2) {
        this.contentPosYProperty().set(d2);
    }

    private double getContentPosY() {
        return this.contentPosY == null ? 0.0 : this.contentPosY.get();
    }

    private DoubleProperty contentPosYProperty() {
        if (this.contentPosY == null) {
            this.contentPosY = new DoublePropertyBase(){

                @Override
                protected void invalidated() {
                    ScrollPaneSkin.this.vsb.setValue(ScrollPaneSkin.this.getContentPosY());
                    ((ScrollPane)ScrollPaneSkin.this.getSkinnable()).requestLayout();
                }

                @Override
                public Object getBean() {
                    return ScrollPaneSkin.this;
                }

                @Override
                public String getName() {
                    return "contentPosY";
                }
            };
        }
        return this.contentPosY;
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case VERTICAL_SCROLLBAR: {
                return this.vsb;
            }
            case HORIZONTAL_SCROLLBAR: {
                return this.hsb;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

