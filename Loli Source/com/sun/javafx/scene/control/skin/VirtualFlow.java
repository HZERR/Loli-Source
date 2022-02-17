/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.Logging;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.control.skin.VirtualScrollBar;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;
import sun.util.logging.PlatformLogger;

public class VirtualFlow<T extends IndexedCell>
extends Region {
    private static final int MIN_SCROLLING_LINES_PER_PAGE = 8;
    private boolean touchDetected = false;
    private boolean mouseDown = false;
    private BooleanProperty vertical;
    private boolean pannable = true;
    private int cellCount;
    private double position;
    private double fixedCellSize = 0.0;
    private boolean fixedCellSizeEnabled = false;
    private Callback<VirtualFlow, T> createCell;
    private double maxPrefBreadth;
    private double viewportBreadth;
    private double viewportLength;
    double lastWidth = -1.0;
    double lastHeight = -1.0;
    int lastCellCount = 0;
    boolean lastVertical;
    double lastPosition;
    double lastCellBreadth = -1.0;
    double lastCellLength = -1.0;
    final ArrayLinkedList<T> cells = new ArrayLinkedList();
    final ArrayLinkedList<T> pile = new ArrayLinkedList();
    T accumCell;
    Group accumCellParent;
    final Group sheet;
    final ObservableList<Node> sheetChildren;
    private VirtualScrollBar hbar = new VirtualScrollBar(this);
    private VirtualScrollBar vbar = new VirtualScrollBar(this);
    ClippedContainer clipView;
    StackPane corner;
    private double lastX;
    private double lastY;
    private boolean isPanning = false;
    private final List<T> privateCells = new ArrayList<T>();
    private static final String NEW_CELL = "newcell";
    private boolean needsReconfigureCells = false;
    private boolean needsRecreateCells = false;
    private boolean needsRebuildCells = false;
    private boolean needsCellsLayout = false;
    private boolean sizeChanged = false;
    private final BitSet dirtyCells = new BitSet();
    private static final double GOLDEN_RATIO_MULTIPLIER = 0.618033987;
    Timeline sbTouchTimeline;
    KeyFrame sbTouchKF1;
    KeyFrame sbTouchKF2;
    private boolean needBreadthBar;
    private boolean needLengthBar;
    private boolean tempVisibility = false;

    public final void setVertical(boolean bl) {
        this.verticalProperty().set(bl);
    }

    public final boolean isVertical() {
        return this.vertical == null ? true : this.vertical.get();
    }

    public final BooleanProperty verticalProperty() {
        if (this.vertical == null) {
            this.vertical = new BooleanPropertyBase(true){

                @Override
                protected void invalidated() {
                    VirtualFlow.this.pile.clear();
                    VirtualFlow.this.sheetChildren.clear();
                    VirtualFlow.this.cells.clear();
                    VirtualFlow.this.lastHeight = -1.0;
                    VirtualFlow.this.lastWidth = -1.0;
                    VirtualFlow.this.setMaxPrefBreadth(-1.0);
                    VirtualFlow.this.setViewportBreadth(0.0);
                    VirtualFlow.this.setViewportLength(0.0);
                    VirtualFlow.this.lastPosition = 0.0;
                    VirtualFlow.this.hbar.setValue(0.0);
                    VirtualFlow.this.vbar.setValue(0.0);
                    VirtualFlow.this.setPosition(0.0);
                    VirtualFlow.this.setNeedsLayout(true);
                    VirtualFlow.this.requestLayout();
                }

                @Override
                public Object getBean() {
                    return VirtualFlow.this;
                }

                @Override
                public String getName() {
                    return "vertical";
                }
            };
        }
        return this.vertical;
    }

    public boolean isPannable() {
        return this.pannable;
    }

    public void setPannable(boolean bl) {
        this.pannable = bl;
    }

    public int getCellCount() {
        return this.cellCount;
    }

    public void setCellCount(int n2) {
        Parent parent;
        boolean bl;
        int n3 = this.cellCount;
        this.cellCount = n2;
        boolean bl2 = bl = n3 != this.cellCount;
        if (bl) {
            parent = this.isVertical() ? this.vbar : this.hbar;
            ((ScrollBar)parent).setMax(n2);
        }
        if (bl) {
            this.layoutChildren();
            this.sheetChildren.clear();
            parent = this.getParent();
            if (parent != null) {
                parent.requestLayout();
            }
        }
    }

    public double getPosition() {
        return this.position;
    }

    public void setPosition(double d2) {
        boolean bl = this.position != d2;
        this.position = com.sun.javafx.util.Utils.clamp(0.0, d2, 1.0);
        if (bl) {
            this.requestLayout();
        }
    }

    public void setFixedCellSize(double d2) {
        this.fixedCellSize = d2;
        this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        this.needsCellsLayout = true;
        this.layoutChildren();
    }

    public Callback<VirtualFlow, T> getCreateCell() {
        return this.createCell;
    }

    public void setCreateCell(Callback<VirtualFlow, T> callback) {
        this.createCell = callback;
        if (this.createCell != null) {
            this.accumCell = null;
            this.setNeedsLayout(true);
            this.recreateCells();
            if (this.getParent() != null) {
                this.getParent().requestLayout();
            }
        }
    }

    protected final void setMaxPrefBreadth(double d2) {
        this.maxPrefBreadth = d2;
    }

    protected final double getMaxPrefBreadth() {
        return this.maxPrefBreadth;
    }

    protected final void setViewportBreadth(double d2) {
        this.viewportBreadth = d2;
    }

    protected final double getViewportBreadth() {
        return this.viewportBreadth;
    }

    void setViewportLength(double d2) {
        this.viewportLength = d2;
    }

    protected double getViewportLength() {
        return this.viewportLength;
    }

    protected List<T> getCells() {
        return this.cells;
    }

    protected final VirtualScrollBar getHbar() {
        return this.hbar;
    }

    protected final VirtualScrollBar getVbar() {
        return this.vbar;
    }

    public VirtualFlow() {
        this.getStyleClass().add("virtual-flow");
        this.setId("virtual-flow");
        this.sheet = new Group();
        this.sheet.getStyleClass().add("sheet");
        this.sheet.setAutoSizeChildren(false);
        this.sheetChildren = this.sheet.getChildren();
        this.clipView = new ClippedContainer(this);
        this.clipView.setNode(this.sheet);
        this.getChildren().add(this.clipView);
        this.accumCellParent = new Group();
        this.accumCellParent.setVisible(false);
        this.getChildren().add(this.accumCellParent);
        EventDispatcher eventDispatcher = (event, eventDispatchChain) -> event;
        EventDispatcher eventDispatcher2 = this.hbar.getEventDispatcher();
        this.hbar.setEventDispatcher((event, eventDispatchChain) -> {
            if (event.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent)event).isDirect()) {
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher);
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher2);
                return eventDispatchChain.dispatchEvent(event);
            }
            return eventDispatcher2.dispatchEvent(event, eventDispatchChain);
        });
        EventDispatcher eventDispatcher3 = this.vbar.getEventDispatcher();
        this.vbar.setEventDispatcher((event, eventDispatchChain) -> {
            if (event.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent)event).isDirect()) {
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher);
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher3);
                return eventDispatchChain.dispatchEvent(event);
            }
            return eventDispatcher3.dispatchEvent(event, eventDispatchChain);
        });
        this.setOnScroll((EventHandler<? super ScrollEvent>)new EventHandler<ScrollEvent>(){

            @Override
            public void handle(ScrollEvent scrollEvent) {
                VirtualScrollBar virtualScrollBar;
                double d2;
                if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && !VirtualFlow.this.touchDetected && !VirtualFlow.this.mouseDown) {
                    VirtualFlow.this.startSBReleasedAnimation();
                }
                double d3 = 0.0;
                if (VirtualFlow.this.isVertical()) {
                    switch (scrollEvent.getTextDeltaYUnits()) {
                        case PAGES: {
                            d3 = scrollEvent.getTextDeltaY() * VirtualFlow.this.lastHeight;
                            break;
                        }
                        case LINES: {
                            if (VirtualFlow.this.fixedCellSizeEnabled) {
                                d2 = VirtualFlow.this.fixedCellSize;
                            } else {
                                IndexedCell indexedCell = (IndexedCell)VirtualFlow.this.cells.getLast();
                                d2 = (VirtualFlow.this.getCellPosition(indexedCell) + VirtualFlow.this.getCellLength(indexedCell) - VirtualFlow.this.getCellPosition((IndexedCell)VirtualFlow.this.cells.getFirst())) / (double)VirtualFlow.this.cells.size();
                            }
                            if (VirtualFlow.this.lastHeight / d2 < 8.0) {
                                d2 = VirtualFlow.this.lastHeight / 8.0;
                            }
                            d3 = scrollEvent.getTextDeltaY() * d2;
                            break;
                        }
                        case NONE: {
                            d3 = scrollEvent.getDeltaY();
                        }
                    }
                } else {
                    switch (scrollEvent.getTextDeltaXUnits()) {
                        case CHARACTERS: 
                        case NONE: {
                            d2 = scrollEvent.getDeltaX();
                            double d4 = scrollEvent.getDeltaY();
                            double d5 = d3 = Math.abs(d2) > Math.abs(d4) ? d2 : d4;
                        }
                    }
                }
                if (d3 != 0.0 && (d2 = VirtualFlow.this.adjustPixels(-d3)) != 0.0) {
                    scrollEvent.consume();
                }
                VirtualScrollBar virtualScrollBar2 = virtualScrollBar = VirtualFlow.this.isVertical() ? VirtualFlow.this.hbar : VirtualFlow.this.vbar;
                if (VirtualFlow.this.needBreadthBar) {
                    double d6;
                    double d7 = d6 = VirtualFlow.this.isVertical() ? scrollEvent.getDeltaX() : scrollEvent.getDeltaY();
                    if (d6 != 0.0) {
                        double d8 = virtualScrollBar.getValue() - d6;
                        if (d8 < virtualScrollBar.getMin()) {
                            virtualScrollBar.setValue(virtualScrollBar.getMin());
                        } else if (d8 > virtualScrollBar.getMax()) {
                            virtualScrollBar.setValue(virtualScrollBar.getMax());
                        } else {
                            virtualScrollBar.setValue(d8);
                        }
                        scrollEvent.consume();
                    }
                }
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent mouseEvent) {
                VirtualFlow.this.mouseDown = true;
                if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                    VirtualFlow.this.scrollBarOn();
                }
                if (VirtualFlow.this.isFocusTraversable()) {
                    boolean bl = true;
                    Node node = VirtualFlow.this.getScene().getFocusOwner();
                    if (node != null) {
                        for (Parent parent = node.getParent(); parent != null; parent = parent.getParent()) {
                            if (!parent.equals(VirtualFlow.this)) continue;
                            bl = false;
                            break;
                        }
                    }
                    if (bl) {
                        VirtualFlow.this.requestFocus();
                    }
                }
                VirtualFlow.this.lastX = mouseEvent.getX();
                VirtualFlow.this.lastY = mouseEvent.getY();
                VirtualFlow.this.isPanning = !VirtualFlow.this.vbar.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY()) && !VirtualFlow.this.hbar.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY());
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            this.mouseDown = false;
            if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                this.startSBReleasedAnimation();
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            VirtualScrollBar virtualScrollBar;
            if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                this.scrollBarOn();
            }
            if (!this.isPanning || !this.isPannable()) {
                return;
            }
            double d2 = this.lastX - mouseEvent.getX();
            double d3 = this.lastY - mouseEvent.getY();
            double d4 = this.isVertical() ? d3 : d2;
            double d5 = this.adjustPixels(d4);
            if (d5 != 0.0) {
                if (this.isVertical()) {
                    this.lastY = mouseEvent.getY();
                } else {
                    this.lastX = mouseEvent.getX();
                }
            }
            double d6 = this.isVertical() ? d2 : d3;
            VirtualScrollBar virtualScrollBar2 = virtualScrollBar = this.isVertical() ? this.hbar : this.vbar;
            if (virtualScrollBar.isVisible()) {
                double d7 = virtualScrollBar.getValue() + d6;
                if (d7 < virtualScrollBar.getMin()) {
                    virtualScrollBar.setValue(virtualScrollBar.getMin());
                } else if (d7 > virtualScrollBar.getMax()) {
                    virtualScrollBar.setValue(virtualScrollBar.getMax());
                } else {
                    virtualScrollBar.setValue(d7);
                    if (this.isVertical()) {
                        this.lastX = mouseEvent.getX();
                    } else {
                        this.lastY = mouseEvent.getY();
                    }
                }
            }
        });
        this.vbar.setOrientation(Orientation.VERTICAL);
        this.vbar.addEventHandler(MouseEvent.ANY, mouseEvent -> mouseEvent.consume());
        this.getChildren().add(this.vbar);
        this.hbar.setOrientation(Orientation.HORIZONTAL);
        this.hbar.addEventHandler(MouseEvent.ANY, mouseEvent -> mouseEvent.consume());
        this.getChildren().add(this.hbar);
        this.corner = new StackPane();
        this.corner.getStyleClass().setAll("corner");
        this.getChildren().add(this.corner);
        InvalidationListener invalidationListener = observable -> this.updateHbar();
        this.verticalProperty().addListener(invalidationListener);
        this.hbar.valueProperty().addListener(invalidationListener);
        this.hbar.visibleProperty().addListener(invalidationListener);
        ChangeListener<Number> changeListener = (observableValue, number, number2) -> this.clipView.setClipY(this.isVertical() ? 0.0 : this.vbar.getValue());
        this.vbar.valueProperty().addListener(changeListener);
        super.heightProperty().addListener((observableValue, number, number2) -> {
            if (number.doubleValue() == 0.0 && number2.doubleValue() > 0.0) {
                this.recreateCells();
            }
        });
        this.setOnTouchPressed(touchEvent -> {
            this.touchDetected = true;
            this.scrollBarOn();
        });
        this.setOnTouchReleased(touchEvent -> {
            this.touchDetected = false;
            this.startSBReleasedAnimation();
        });
        this.setImpl_traversalEngine(new ParentTraversalEngine(this, new Algorithm(){

            Node selectNextAfterIndex(int n2, TraversalContext traversalContext) {
                Object t2;
                while ((t2 = VirtualFlow.this.getVisibleCell(++n2)) != null) {
                    if (((Node)t2).isFocusTraversable()) {
                        return t2;
                    }
                    Node node = traversalContext.selectFirstInParent((Parent)t2);
                    if (node == null) continue;
                    return node;
                }
                return null;
            }

            Node selectPreviousBeforeIndex(int n2, TraversalContext traversalContext) {
                Object t2;
                while ((t2 = VirtualFlow.this.getVisibleCell(--n2)) != null) {
                    Node node = traversalContext.selectLastInParent((Parent)t2);
                    if (node != null) {
                        return node;
                    }
                    if (!((Node)t2).isFocusTraversable()) continue;
                    return t2;
                }
                return null;
            }

            @Override
            public Node select(Node node, Direction direction, TraversalContext traversalContext) {
                IndexedCell indexedCell;
                if (VirtualFlow.this.cells.isEmpty()) {
                    return null;
                }
                if (VirtualFlow.this.cells.contains(node)) {
                    indexedCell = (IndexedCell)node;
                } else {
                    indexedCell = this.findOwnerCell(node);
                    Node node2 = traversalContext.selectInSubtree(indexedCell, node, direction);
                    if (node2 != null) {
                        return node2;
                    }
                    if (direction == Direction.NEXT) {
                        direction = Direction.NEXT_IN_LINE;
                    }
                }
                int n2 = indexedCell.getIndex();
                switch (direction) {
                    case PREVIOUS: {
                        return this.selectPreviousBeforeIndex(n2, traversalContext);
                    }
                    case NEXT: {
                        Node node3 = traversalContext.selectFirstInParent(indexedCell);
                        if (node3 != null) {
                            return node3;
                        }
                    }
                    case NEXT_IN_LINE: {
                        return this.selectNextAfterIndex(n2, traversalContext);
                    }
                }
                return null;
            }

            private T findOwnerCell(Node node) {
                Parent parent = node.getParent();
                while (!VirtualFlow.this.cells.contains(parent)) {
                    parent = parent.getParent();
                }
                return (IndexedCell)parent;
            }

            @Override
            public Node selectFirst(TraversalContext traversalContext) {
                IndexedCell indexedCell = (IndexedCell)VirtualFlow.this.cells.getFirst();
                if (indexedCell == null) {
                    return null;
                }
                if (indexedCell.isFocusTraversable()) {
                    return indexedCell;
                }
                Node node = traversalContext.selectFirstInParent(indexedCell);
                if (node != null) {
                    return node;
                }
                return this.selectNextAfterIndex(indexedCell.getIndex(), traversalContext);
            }

            @Override
            public Node selectLast(TraversalContext traversalContext) {
                IndexedCell indexedCell = (IndexedCell)VirtualFlow.this.cells.getLast();
                if (indexedCell == null) {
                    return null;
                }
                Node node = traversalContext.selectLastInParent(indexedCell);
                if (node != null) {
                    return node;
                }
                if (indexedCell.isFocusTraversable()) {
                    return indexedCell;
                }
                return this.selectPreviousBeforeIndex(indexedCell.getIndex(), traversalContext);
            }
        }));
    }

    void updateHbar() {
        if (!this.isVisible() || this.getScene() == null) {
            return;
        }
        if (this.isVertical()) {
            if (this.hbar.isVisible()) {
                this.clipView.setClipX(this.hbar.getValue());
            } else {
                this.clipView.setClipX(0.0);
                this.hbar.setValue(0.0);
            }
        }
    }

    @Override
    public void requestLayout() {
        this.setNeedsLayout(true);
    }

    @Override
    protected void layoutChildren() {
        double d2;
        int n2;
        boolean bl;
        double d3;
        int n3;
        int n4;
        if (this.needsRecreateCells) {
            this.lastWidth = -1.0;
            this.lastHeight = -1.0;
            this.releaseCell(this.accumCell);
            this.sheet.getChildren().clear();
            n4 = this.cells.size();
            for (n3 = 0; n3 < n4; ++n3) {
                ((IndexedCell)this.cells.get(n3)).updateIndex(-1);
            }
            this.cells.clear();
            this.pile.clear();
            this.releaseAllPrivateCells();
        } else if (this.needsRebuildCells) {
            this.lastWidth = -1.0;
            this.lastHeight = -1.0;
            this.releaseCell(this.accumCell);
            for (n3 = 0; n3 < this.cells.size(); ++n3) {
                ((IndexedCell)this.cells.get(n3)).updateIndex(-1);
            }
            this.addAllToPile();
            this.releaseAllPrivateCells();
        } else if (this.needsReconfigureCells) {
            this.setMaxPrefBreadth(-1.0);
            this.lastWidth = -1.0;
            this.lastHeight = -1.0;
        }
        if (!this.dirtyCells.isEmpty()) {
            n4 = this.cells.size();
            while ((n3 = this.dirtyCells.nextSetBit(0)) != -1 && n3 < n4) {
                IndexedCell indexedCell = (IndexedCell)this.cells.get(n3);
                if (indexedCell != null) {
                    indexedCell.requestLayout();
                }
                this.dirtyCells.clear(n3);
            }
            this.setMaxPrefBreadth(-1.0);
            this.lastWidth = -1.0;
            this.lastHeight = -1.0;
        }
        n3 = this.sizeChanged;
        n4 = this.needsRebuildCells || this.needsRecreateCells || this.sizeChanged ? 1 : 0;
        this.needsRecreateCells = false;
        this.needsReconfigureCells = false;
        this.needsRebuildCells = false;
        this.sizeChanged = false;
        if (this.needsCellsLayout) {
            int n5 = this.cells.size();
            for (int i2 = 0; i2 < n5; ++i2) {
                Cell cell = (Cell)this.cells.get(i2);
                if (cell == null) continue;
                cell.requestLayout();
            }
            this.needsCellsLayout = false;
            return;
        }
        double d4 = this.getWidth();
        double d5 = this.getHeight();
        boolean bl2 = this.isVertical();
        double d6 = this.getPosition();
        if (d4 <= 0.0 || d5 <= 0.0) {
            this.addAllToPile();
            this.lastWidth = d4;
            this.lastHeight = d5;
            this.hbar.setVisible(false);
            this.vbar.setVisible(false);
            this.corner.setVisible(false);
            return;
        }
        boolean bl3 = false;
        boolean bl4 = false;
        if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && (this.tempVisibility && (!this.hbar.isVisible() || !this.vbar.isVisible()) || !this.tempVisibility && (this.hbar.isVisible() || this.vbar.isVisible()))) {
            bl4 = true;
        }
        if (!bl3) {
            Cell cell;
            for (int i3 = 0; i3 < this.cells.size() && !(bl3 = (cell = (Cell)this.cells.get(i3)).isNeedsLayout()); ++i3) {
            }
        }
        T t2 = this.getFirstVisibleCell();
        if (!bl3 && !bl4) {
            boolean bl5 = false;
            if (t2 != null) {
                double d7 = this.getCellBreadth((Cell)t2);
                d3 = this.getCellLength(t2);
                bl5 = d7 != this.lastCellBreadth || d3 != this.lastCellLength;
                this.lastCellBreadth = d7;
                this.lastCellLength = d3;
            }
            if (d4 == this.lastWidth && d5 == this.lastHeight && this.cellCount == this.lastCellCount && bl2 == this.lastVertical && d6 == this.lastPosition && !bl5) {
                return;
            }
        }
        boolean bl6 = false;
        boolean bl7 = bl = bl3 || bl2 != this.lastVertical || this.cells.isEmpty() || this.getMaxPrefBreadth() == -1.0 || d6 != this.lastPosition || this.cellCount != this.lastCellCount || n3 != 0 || bl2 && d5 < this.lastHeight || !bl2 && d4 < this.lastWidth;
        if (!bl) {
            double d8 = this.getMaxPrefBreadth();
            boolean bl8 = false;
            for (n2 = 0; n2 < this.cells.size(); ++n2) {
                d2 = this.getCellBreadth((Cell)this.cells.get(n2));
                if (d8 == d2) {
                    bl8 = true;
                    continue;
                }
                if (!(d2 > d8)) continue;
                bl = true;
                break;
            }
            if (!bl8) {
                bl = true;
            }
        }
        if (!bl && (bl2 && d5 > this.lastHeight || !bl2 && d4 > this.lastWidth)) {
            bl6 = true;
        }
        this.initViewport();
        int n6 = this.computeCurrentIndex();
        if (this.lastCellCount != this.cellCount) {
            if (d6 != 0.0 && d6 != 1.0) {
                if (n6 >= this.cellCount) {
                    this.setPosition(1.0);
                } else if (t2 != null) {
                    d3 = this.getCellPosition(t2);
                    n2 = this.getCellIndex(t2);
                    this.adjustPositionToIndex(n2);
                    d2 = -this.computeOffsetForCell(n2);
                    this.adjustByPixelAmount(d2 - d3);
                }
            }
            n6 = this.computeCurrentIndex();
        }
        if (bl) {
            this.setMaxPrefBreadth(-1.0);
            this.addAllToPile();
            d3 = -this.computeViewportOffset(this.getPosition());
            this.addLeadingCells(n6, d3);
            this.addTrailingCells(true);
        } else if (bl6) {
            this.addTrailingCells(true);
        }
        this.computeBarVisiblity();
        this.updateScrollBarsAndCells(n4 != 0);
        this.lastWidth = this.getWidth();
        this.lastHeight = this.getHeight();
        this.lastCellCount = this.getCellCount();
        this.lastVertical = this.isVertical();
        this.lastPosition = this.getPosition();
        this.cleanPile();
    }

    protected void addLeadingCells(int n2, double d2) {
        double d3 = d2;
        int n3 = n2;
        boolean bl = true;
        IndexedCell indexedCell = null;
        if (n3 == this.cellCount && d3 == this.getViewportLength()) {
            --n3;
            bl = false;
        }
        while (n3 >= 0 && (d3 > 0.0 || bl)) {
            indexedCell = (IndexedCell)this.getAvailableCell(n3);
            this.setCellIndex(indexedCell, n3);
            this.resizeCellSize(indexedCell);
            this.cells.addFirst(indexedCell);
            if (bl) {
                bl = false;
            } else {
                d3 -= this.getCellLength(indexedCell);
            }
            this.positionCell(indexedCell, d3);
            this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth(indexedCell)));
            indexedCell.setVisible(true);
            --n3;
        }
        if (this.cells.size() > 0) {
            indexedCell = (IndexedCell)this.cells.getFirst();
            int n4 = this.getCellIndex(indexedCell);
            double d4 = this.getCellPosition(indexedCell);
            if (n4 == 0 && d4 > 0.0) {
                this.setPosition(0.0);
                d3 = 0.0;
                for (int i2 = 0; i2 < this.cells.size(); ++i2) {
                    indexedCell = (IndexedCell)this.cells.get(i2);
                    this.positionCell(indexedCell, d3);
                    d3 += this.getCellLength(indexedCell);
                }
            }
        } else {
            this.vbar.setValue(0.0);
            this.hbar.setValue(0.0);
        }
    }

    protected boolean addTrailingCells(boolean bl) {
        Object object;
        if (this.cells.isEmpty()) {
            return false;
        }
        IndexedCell indexedCell = (IndexedCell)this.cells.getLast();
        double d2 = this.getCellPosition(indexedCell) + this.getCellLength(indexedCell);
        int n2 = this.getCellIndex(indexedCell) + 1;
        boolean bl2 = n2 <= this.cellCount;
        double d3 = this.getViewportLength();
        if (d2 < 0.0 && !bl) {
            return false;
        }
        double d4 = d3 - d2;
        while (d2 < d3) {
            if (n2 >= this.cellCount) {
                if (d2 < d3) {
                    bl2 = false;
                }
                if (!bl) {
                    return bl2;
                }
                if ((double)n2 > d4) {
                    object = Logging.getControlsLogger();
                    if (((PlatformLogger)object).isLoggable(PlatformLogger.Level.INFO)) {
                        if (indexedCell != null) {
                            ((PlatformLogger)object).info("index exceeds maxCellCount. Check size calculations for " + indexedCell.getClass());
                        } else {
                            ((PlatformLogger)object).info("index exceeds maxCellCount");
                        }
                    }
                    return bl2;
                }
            }
            object = this.getAvailableCell(n2);
            this.setCellIndex(object, n2);
            this.resizeCellSize(object);
            this.cells.addLast(object);
            this.positionCell(object, d2);
            this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth((Cell)object)));
            d2 += this.getCellLength(object);
            ((Node)object).setVisible(true);
            ++n2;
        }
        object = (IndexedCell)this.cells.getFirst();
        n2 = this.getCellIndex(object);
        T t2 = this.getLastVisibleCell();
        double d5 = this.getCellPosition(object);
        double d6 = this.getCellPosition(t2) + this.getCellLength(t2);
        if ((n2 != 0 || n2 == 0 && d5 < 0.0) && bl && t2 != null && this.getCellIndex(t2) == this.cellCount - 1 && d6 < d3) {
            double d7;
            double d8 = d3 - d6;
            for (double d9 = d6; d9 < d3 && n2 != 0 && -d5 < d8; d9 += d7) {
                T t3 = this.getAvailableCell(--n2);
                this.setCellIndex(t3, n2);
                this.resizeCellSize(t3);
                this.cells.addFirst(t3);
                d7 = this.getCellLength(t3);
                this.positionCell(t3, d5 -= d7);
                this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth((Cell)t3)));
                ((Node)t3).setVisible(true);
            }
            object = (IndexedCell)this.cells.getFirst();
            d5 = this.getCellPosition(object);
            double d10 = d3 - d6;
            if (this.getCellIndex(object) == 0 && d10 > -d5) {
                d10 = -d5;
            }
            for (int i2 = 0; i2 < this.cells.size(); ++i2) {
                IndexedCell indexedCell2 = (IndexedCell)this.cells.get(i2);
                this.positionCell(indexedCell2, this.getCellPosition(indexedCell2) + d10);
            }
            d5 = this.getCellPosition(object);
            if (this.getCellIndex(object) == 0 && d5 == 0.0) {
                this.setPosition(0.0);
            } else if (this.getPosition() != 1.0) {
                this.setPosition(1.0);
            }
        }
        return bl2;
    }

    private boolean computeBarVisiblity() {
        if (this.cells.isEmpty()) {
            this.needLengthBar = false;
            this.needBreadthBar = false;
            return true;
        }
        boolean bl = this.isVertical();
        boolean bl2 = false;
        VirtualScrollBar virtualScrollBar = bl ? this.hbar : this.vbar;
        VirtualScrollBar virtualScrollBar2 = bl ? this.vbar : this.hbar;
        double d2 = this.getViewportBreadth();
        int n2 = this.cells.size();
        for (int i2 = 0; i2 < 2; ++i2) {
            boolean bl3;
            boolean bl4;
            boolean bl5 = bl4 = this.getPosition() > 0.0 || this.cellCount > n2 || this.cellCount == n2 && this.getCellPosition((IndexedCell)this.cells.getLast()) + this.getCellLength((IndexedCell)this.cells.getLast()) > this.getViewportLength() || this.cellCount == n2 - 1 && bl2 && this.needBreadthBar;
            if (bl4 ^ this.needLengthBar) {
                this.needLengthBar = bl4;
                bl2 = true;
            }
            boolean bl6 = bl3 = this.maxPrefBreadth > d2;
            if (!(bl3 ^ this.needBreadthBar)) continue;
            this.needBreadthBar = bl3;
            bl2 = true;
        }
        if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
            this.updateViewportDimensions();
            virtualScrollBar.setVisible(this.needBreadthBar);
            virtualScrollBar2.setVisible(this.needLengthBar);
        } else {
            virtualScrollBar.setVisible(this.needBreadthBar && this.tempVisibility);
            virtualScrollBar2.setVisible(this.needLengthBar && this.tempVisibility);
        }
        return bl2;
    }

    private void updateViewportDimensions() {
        boolean bl = this.isVertical();
        double d2 = this.snapSize(bl ? this.hbar.prefHeight(-1.0) : this.vbar.prefWidth(-1.0));
        double d3 = this.snapSize(bl ? this.vbar.prefWidth(-1.0) : this.hbar.prefHeight(-1.0));
        this.setViewportBreadth((bl ? this.getWidth() : this.getHeight()) - (this.needLengthBar ? d3 : 0.0));
        this.setViewportLength((bl ? this.getHeight() : this.getWidth()) - (this.needBreadthBar ? d2 : 0.0));
    }

    private void initViewport() {
        boolean bl = this.isVertical();
        this.updateViewportDimensions();
        VirtualScrollBar virtualScrollBar = bl ? this.hbar : this.vbar;
        VirtualScrollBar virtualScrollBar2 = bl ? this.vbar : this.hbar;
        virtualScrollBar.setVirtual(false);
        virtualScrollBar2.setVirtual(true);
    }

    @Override
    protected void setWidth(double d2) {
        if (d2 != this.lastWidth) {
            super.setWidth(d2);
            this.sizeChanged = true;
            this.setNeedsLayout(true);
            this.requestLayout();
        }
    }

    @Override
    protected void setHeight(double d2) {
        if (d2 != this.lastHeight) {
            super.setHeight(d2);
            this.sizeChanged = true;
            this.setNeedsLayout(true);
            this.requestLayout();
        }
    }

    private void updateScrollBarsAndCells(boolean bl) {
        double d2;
        double d3;
        boolean bl2 = this.isVertical();
        VirtualScrollBar virtualScrollBar = bl2 ? this.hbar : this.vbar;
        VirtualScrollBar virtualScrollBar2 = bl2 ? this.vbar : this.hbar;
        this.fitCells();
        if (!this.cells.isEmpty()) {
            IndexedCell indexedCell;
            int n2;
            d3 = -this.computeViewportOffset(this.getPosition());
            int n3 = this.computeCurrentIndex() - ((IndexedCell)this.cells.getFirst()).getIndex();
            int n4 = this.cells.size();
            d2 = d3;
            for (n2 = n3 - 1; n2 >= 0 && n2 < n4; --n2) {
                indexedCell = (IndexedCell)this.cells.get(n2);
                this.positionCell(indexedCell, d2 -= this.getCellLength(indexedCell));
            }
            d2 = d3;
            for (n2 = n3; n2 >= 0 && n2 < n4; ++n2) {
                indexedCell = (IndexedCell)this.cells.get(n2);
                this.positionCell(indexedCell, d2);
                d2 += this.getCellLength(indexedCell);
            }
        }
        this.corner.setVisible(virtualScrollBar.isVisible() && virtualScrollBar2.isVisible());
        d3 = 0.0;
        double d4 = (bl2 ? this.getHeight() : this.getWidth()) - (virtualScrollBar.isVisible() ? virtualScrollBar.prefHeight(-1.0) : 0.0);
        d2 = this.getViewportBreadth();
        double d5 = this.getViewportLength();
        if (virtualScrollBar.isVisible()) {
            double d6;
            if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                if (bl2) {
                    this.hbar.resizeRelocate(0.0, d5, d2, this.hbar.prefHeight(d2));
                } else {
                    this.vbar.resizeRelocate(d5, 0.0, this.vbar.prefWidth(d2), d2);
                }
            } else if (bl2) {
                this.hbar.resizeRelocate(0.0, d5 - this.hbar.getHeight(), d2, this.hbar.prefHeight(d2));
            } else {
                this.vbar.resizeRelocate(d5 - this.vbar.getWidth(), 0.0, this.vbar.prefWidth(d2), d2);
            }
            if (this.getMaxPrefBreadth() != -1.0 && (d6 = Math.max(1.0, this.getMaxPrefBreadth() - d2)) != virtualScrollBar.getMax()) {
                boolean bl3;
                virtualScrollBar.setMax(d6);
                double d7 = virtualScrollBar.getValue();
                boolean bl4 = bl3 = d7 != 0.0 && d6 == d7;
                if (bl3 || d7 > d6) {
                    virtualScrollBar.setValue(d6);
                }
                virtualScrollBar.setVisibleAmount(d2 / this.getMaxPrefBreadth() * d6);
            }
        }
        if (bl && (virtualScrollBar2.isVisible() || BehaviorSkinBase.IS_TOUCH_SUPPORTED)) {
            int n5 = 0;
            int n6 = this.cells.size();
            for (int i2 = 0; i2 < n6; ++i2) {
                IndexedCell indexedCell = (IndexedCell)this.cells.get(i2);
                if (indexedCell == null || indexedCell.isEmpty()) continue;
                if ((d3 += bl2 ? indexedCell.getHeight() : indexedCell.getWidth()) > d4) break;
                ++n5;
            }
            virtualScrollBar2.setMax(1.0);
            if (n5 == 0 && this.cellCount == 1) {
                virtualScrollBar2.setVisibleAmount(d4 / d3);
            } else {
                virtualScrollBar2.setVisibleAmount((float)n5 / (float)this.cellCount);
            }
        }
        if (virtualScrollBar2.isVisible()) {
            if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                if (bl2) {
                    this.vbar.resizeRelocate(d2, 0.0, this.vbar.prefWidth(d5), d5);
                } else {
                    this.hbar.resizeRelocate(0.0, d2, d5, this.hbar.prefHeight(-1.0));
                }
            } else if (bl2) {
                this.vbar.resizeRelocate(d2 - this.vbar.getWidth(), 0.0, this.vbar.prefWidth(d5), d5);
            } else {
                this.hbar.resizeRelocate(0.0, d2 - this.hbar.getHeight(), d5, this.hbar.prefHeight(-1.0));
            }
        }
        if (this.corner.isVisible()) {
            if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                this.corner.resize(this.vbar.getWidth(), this.hbar.getHeight());
                this.corner.relocate(this.hbar.getLayoutX() + this.hbar.getWidth(), this.vbar.getLayoutY() + this.vbar.getHeight());
            } else {
                this.corner.resize(this.vbar.getWidth(), this.hbar.getHeight());
                this.corner.relocate(this.hbar.getLayoutX() + (this.hbar.getWidth() - this.vbar.getWidth()), this.vbar.getLayoutY() + (this.vbar.getHeight() - this.hbar.getHeight()));
                this.hbar.resize(this.hbar.getWidth() - this.vbar.getWidth(), this.hbar.getHeight());
                this.vbar.resize(this.vbar.getWidth(), this.vbar.getHeight() - this.hbar.getHeight());
            }
        }
        this.clipView.resize(this.snapSize(bl2 ? d2 : d5), this.snapSize(bl2 ? d5 : d2));
        if (this.getPosition() != virtualScrollBar2.getValue()) {
            virtualScrollBar2.setValue(this.getPosition());
        }
    }

    private void fitCells() {
        double d2 = Math.max(this.getMaxPrefBreadth(), this.getViewportBreadth());
        boolean bl = this.isVertical();
        for (int i2 = 0; i2 < this.cells.size(); ++i2) {
            Cell cell = (Cell)this.cells.get(i2);
            if (bl) {
                cell.resize(d2, cell.prefHeight(d2));
                continue;
            }
            cell.resize(cell.prefWidth(d2), d2);
        }
    }

    private void cull() {
        double d2 = this.getViewportLength();
        for (int i2 = this.cells.size() - 1; i2 >= 0; --i2) {
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i2);
            double d3 = this.getCellLength(indexedCell);
            double d4 = this.getCellPosition(indexedCell);
            double d5 = d4 + d3;
            if (!(d4 >= d2) && !(d5 < 0.0)) continue;
            this.addToPile((IndexedCell)this.cells.remove(i2));
        }
    }

    protected int getCellIndex(T t2) {
        return ((IndexedCell)t2).getIndex();
    }

    public T getCell(int n2) {
        Callback<VirtualFlow, T> callback;
        T t2;
        if (!this.cells.isEmpty() && (t2 = this.getVisibleCell(n2)) != null) {
            return t2;
        }
        for (int i2 = 0; i2 < this.pile.size(); ++i2) {
            IndexedCell indexedCell = (IndexedCell)this.pile.get(i2);
            if (this.getCellIndex(indexedCell) != n2) continue;
            return (T)indexedCell;
        }
        if (this.pile.size() > 0) {
            return (T)((IndexedCell)this.pile.get(0));
        }
        if (this.accumCell == null && (callback = this.getCreateCell()) != null) {
            this.accumCell = (IndexedCell)callback.call(this);
            ((Node)this.accumCell).getProperties().put(NEW_CELL, null);
            this.accumCellParent.getChildren().setAll(this.accumCell);
            ((Node)this.accumCell).setAccessibleRole(AccessibleRole.NODE);
            ((Parent)this.accumCell).getChildrenUnmodifiable().addListener(observable -> {
                for (Node node : ((Parent)this.accumCell).getChildrenUnmodifiable()) {
                    node.setAccessibleRole(AccessibleRole.NODE);
                }
            });
        }
        this.setCellIndex(this.accumCell, n2);
        this.resizeCellSize(this.accumCell);
        return this.accumCell;
    }

    private void releaseCell(T t2) {
        if (this.accumCell != null && t2 == this.accumCell) {
            ((IndexedCell)this.accumCell).updateIndex(-1);
        }
    }

    T getPrivateCell(int n2) {
        Callback<VirtualFlow, T> callback;
        Parent parent = null;
        if (!this.cells.isEmpty() && (parent = (Parent)this.getVisibleCell(n2)) != null) {
            parent.layout();
            return (T)parent;
        }
        if (parent == null) {
            for (int i2 = 0; i2 < this.sheetChildren.size(); ++i2) {
                IndexedCell indexedCell = (IndexedCell)this.sheetChildren.get(i2);
                if (this.getCellIndex(indexedCell) != n2) continue;
                return (T)indexedCell;
            }
        }
        if (parent == null && (callback = this.getCreateCell()) != null) {
            parent = (IndexedCell)callback.call(this);
        }
        if (parent != null) {
            this.setCellIndex(parent, n2);
            this.resizeCellSize(parent);
            parent.setVisible(false);
            this.sheetChildren.add(parent);
            this.privateCells.add(parent);
        }
        return (T)parent;
    }

    private void releaseAllPrivateCells() {
        this.sheetChildren.removeAll((Collection<?>)this.privateCells);
    }

    protected double getCellLength(int n2) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        T t2 = this.getCell(n2);
        double d2 = this.getCellLength(t2);
        this.releaseCell(t2);
        return d2;
    }

    protected double getCellBreadth(int n2) {
        T t2 = this.getCell(n2);
        double d2 = this.getCellBreadth((Cell)t2);
        this.releaseCell(t2);
        return d2;
    }

    protected double getCellLength(T t2) {
        if (t2 == null) {
            return 0.0;
        }
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return this.isVertical() ? ((Node)t2).getLayoutBounds().getHeight() : ((Node)t2).getLayoutBounds().getWidth();
    }

    protected double getCellBreadth(Cell cell) {
        return this.isVertical() ? cell.prefWidth(-1.0) : cell.prefHeight(-1.0);
    }

    protected double getCellPosition(T t2) {
        if (t2 == null) {
            return 0.0;
        }
        return this.isVertical() ? ((Node)t2).getLayoutY() : ((Node)t2).getLayoutX();
    }

    protected void positionCell(T t2, double d2) {
        if (this.isVertical()) {
            ((Node)t2).setLayoutX(0.0);
            ((Node)t2).setLayoutY(this.snapSize(d2));
        } else {
            ((Node)t2).setLayoutX(this.snapSize(d2));
            ((Node)t2).setLayoutY(0.0);
        }
    }

    protected void resizeCellSize(T t2) {
        if (t2 == null) {
            return;
        }
        if (this.isVertical()) {
            double d2 = Math.max(this.getMaxPrefBreadth(), this.getViewportBreadth());
            ((Region)t2).resize(d2, this.fixedCellSizeEnabled ? this.fixedCellSize : Utils.boundedSize(((Region)t2).prefHeight(d2), ((Region)t2).minHeight(d2), ((Region)t2).maxHeight(d2)));
        } else {
            double d3 = Math.max(this.getMaxPrefBreadth(), this.getViewportBreadth());
            ((Region)t2).resize(this.fixedCellSizeEnabled ? this.fixedCellSize : Utils.boundedSize(((Region)t2).prefWidth(d3), ((Region)t2).minWidth(d3), ((Region)t2).maxWidth(d3)), d3);
        }
    }

    protected void setCellIndex(T t2, int n2) {
        assert (t2 != null);
        ((IndexedCell)t2).updateIndex(n2);
        if (((Parent)t2).isNeedsLayout() && ((Node)t2).getScene() != null || ((Node)t2).getProperties().containsKey(NEW_CELL)) {
            ((Node)t2).applyCss();
            ((Node)t2).getProperties().remove(NEW_CELL);
        }
    }

    protected T getAvailableCell(int n2) {
        int n3;
        Node node = null;
        int n4 = this.pile.size();
        for (n3 = 0; n3 < n4; ++n3) {
            IndexedCell indexedCell = (IndexedCell)this.pile.get(n3);
            assert (indexedCell != null);
            if (this.getCellIndex(indexedCell) == n2) {
                node = indexedCell;
                this.pile.remove(n3);
                break;
            }
            node = null;
        }
        if (node == null) {
            if (this.pile.size() > 0) {
                n3 = (n2 & 1) == 0 ? 1 : 0;
                int n5 = this.pile.size();
                for (n4 = 0; n4 < n5; ++n4) {
                    IndexedCell indexedCell = (IndexedCell)this.pile.get(n4);
                    int n6 = this.getCellIndex(indexedCell);
                    if ((n6 & 1) == 0 && n3 != 0) {
                        node = indexedCell;
                        this.pile.remove(n4);
                        break;
                    }
                    if ((n6 & 1) != 1 || n3 != 0) continue;
                    node = indexedCell;
                    this.pile.remove(n4);
                    break;
                }
                if (node == null) {
                    node = (IndexedCell)this.pile.removeFirst();
                }
            } else {
                node = (IndexedCell)this.getCreateCell().call(this);
                node.getProperties().put(NEW_CELL, null);
            }
        }
        if (node.getParent() == null) {
            this.sheetChildren.add(node);
        }
        return (T)node;
    }

    protected void addAllToPile() {
        int n2 = this.cells.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.addToPile((IndexedCell)this.cells.removeFirst());
        }
    }

    private void addToPile(T t2) {
        assert (t2 != null);
        this.pile.addLast(t2);
    }

    private void cleanPile() {
        boolean bl = false;
        int n2 = this.pile.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            IndexedCell indexedCell = (IndexedCell)this.pile.get(i2);
            bl = bl || this.doesCellContainFocus(indexedCell);
            indexedCell.setVisible(false);
        }
        if (bl) {
            this.requestFocus();
        }
    }

    private boolean doesCellContainFocus(Cell<?> cell) {
        Node node;
        Scene scene = cell.getScene();
        Node node2 = node = scene == null ? null : scene.getFocusOwner();
        if (node != null) {
            if (cell.equals(node)) {
                return true;
            }
            for (Parent parent = node.getParent(); parent != null && !(parent instanceof VirtualFlow); parent = parent.getParent()) {
                if (!cell.equals(parent)) continue;
                return true;
            }
        }
        return false;
    }

    public T getVisibleCell(int n2) {
        IndexedCell indexedCell;
        if (this.cells.isEmpty()) {
            return null;
        }
        IndexedCell indexedCell2 = (IndexedCell)this.cells.getLast();
        int n3 = this.getCellIndex(indexedCell2);
        if (n2 == n3) {
            return (T)indexedCell2;
        }
        IndexedCell indexedCell3 = (IndexedCell)this.cells.getFirst();
        int n4 = this.getCellIndex(indexedCell3);
        if (n2 == n4) {
            return (T)indexedCell3;
        }
        if (n2 > n4 && n2 < n3 && this.getCellIndex(indexedCell = (IndexedCell)this.cells.get(n2 - n4)) == n2) {
            return (T)indexedCell;
        }
        return null;
    }

    public T getLastVisibleCell() {
        if (this.cells.isEmpty() || this.getViewportLength() <= 0.0) {
            return null;
        }
        for (int i2 = this.cells.size() - 1; i2 >= 0; --i2) {
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i2);
            if (indexedCell.isEmpty()) continue;
            return (T)indexedCell;
        }
        return null;
    }

    public T getFirstVisibleCell() {
        if (this.cells.isEmpty() || this.getViewportLength() <= 0.0) {
            return null;
        }
        IndexedCell indexedCell = (IndexedCell)this.cells.getFirst();
        return (T)(indexedCell.isEmpty() ? null : indexedCell);
    }

    public T getLastVisibleCellWithinViewPort() {
        if (this.cells.isEmpty() || this.getViewportLength() <= 0.0) {
            return null;
        }
        double d2 = this.getViewportLength();
        for (int i2 = this.cells.size() - 1; i2 >= 0; --i2) {
            double d3;
            double d4;
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i2);
            if (indexedCell.isEmpty() || !((d4 = (d3 = this.getCellPosition(indexedCell)) + this.getCellLength(indexedCell)) <= d2 + 2.0)) continue;
            return (T)indexedCell;
        }
        return null;
    }

    public T getFirstVisibleCellWithinViewPort() {
        if (this.cells.isEmpty() || this.getViewportLength() <= 0.0) {
            return null;
        }
        for (int i2 = 0; i2 < this.cells.size(); ++i2) {
            double d2;
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i2);
            if (indexedCell.isEmpty() || !((d2 = this.getCellPosition(indexedCell)) >= 0.0)) continue;
            return (T)indexedCell;
        }
        return null;
    }

    public void showAsFirst(T t2) {
        if (t2 != null) {
            this.adjustPixels(this.getCellPosition(t2));
        }
    }

    public void showAsLast(T t2) {
        if (t2 != null) {
            this.adjustPixels(this.getCellPosition(t2) + this.getCellLength(t2) - this.getViewportLength());
        }
    }

    public void show(T t2) {
        if (t2 != null) {
            double d2 = this.getCellPosition(t2);
            double d3 = this.getCellLength(t2);
            double d4 = d2 + d3;
            double d5 = this.getViewportLength();
            if (d2 < 0.0) {
                this.adjustPixels(d2);
            } else if (d4 > d5) {
                this.adjustPixels(d4 - d5);
            }
        }
    }

    public void show(int n2) {
        T t2 = this.getVisibleCell(n2);
        if (t2 != null) {
            this.show(t2);
        } else {
            T t3 = this.getVisibleCell(n2 - 1);
            if (t3 != null) {
                t2 = this.getAvailableCell(n2);
                this.setCellIndex(t2, n2);
                this.resizeCellSize(t2);
                this.cells.addLast(t2);
                this.positionCell(t2, this.getCellPosition(t3) + this.getCellLength(t3));
                this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth((Cell)t2)));
                ((Node)t2).setVisible(true);
                this.show(t2);
                return;
            }
            T t4 = this.getVisibleCell(n2 + 1);
            if (t4 != null) {
                t2 = this.getAvailableCell(n2);
                this.setCellIndex(t2, n2);
                this.resizeCellSize(t2);
                this.cells.addFirst(t2);
                this.positionCell(t2, this.getCellPosition(t4) - this.getCellLength(t2));
                this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth((Cell)t2)));
                ((Node)t2).setVisible(true);
                this.show(t2);
                return;
            }
            this.adjustPositionToIndex(n2);
            this.addAllToPile();
            this.requestLayout();
        }
    }

    public void scrollTo(int n2) {
        boolean bl = false;
        if (n2 >= this.cellCount - 1) {
            this.setPosition(1.0);
            bl = true;
        } else if (n2 < 0) {
            this.setPosition(0.0);
            bl = true;
        }
        if (!bl) {
            this.adjustPositionToIndex(n2);
            double d2 = -this.computeOffsetForCell(n2);
            this.adjustByPixelAmount(d2);
        }
        this.requestLayout();
    }

    public void scrollToOffset(int n2) {
        this.adjustPixels((double)n2 * this.getCellLength(0));
    }

    public double adjustPixels(double d2) {
        if (d2 == 0.0) {
            return 0.0;
        }
        boolean bl = this.isVertical();
        if (bl && (!this.tempVisibility ? !this.vbar.isVisible() : !this.needLengthBar) || !bl && (this.tempVisibility ? !this.needLengthBar : !this.hbar.isVisible())) {
            return 0.0;
        }
        double d3 = this.getPosition();
        if (d3 == 0.0 && d2 < 0.0) {
            return 0.0;
        }
        if (d3 == 1.0 && d2 > 0.0) {
            return 0.0;
        }
        this.adjustByPixelAmount(d2);
        if (d3 == this.getPosition()) {
            return 0.0;
        }
        if (this.cells.size() > 0) {
            double d4;
            int n2;
            for (int i2 = 0; i2 < this.cells.size(); ++i2) {
                IndexedCell indexedCell = (IndexedCell)this.cells.get(i2);
                assert (indexedCell != null);
                this.positionCell(indexedCell, this.getCellPosition(indexedCell) - d2);
            }
            IndexedCell indexedCell = (IndexedCell)this.cells.getFirst();
            double d5 = indexedCell == null ? 0.0 : this.getCellPosition(indexedCell);
            for (n2 = 0; n2 < this.cells.size(); ++n2) {
                IndexedCell indexedCell2 = (IndexedCell)this.cells.get(n2);
                assert (indexedCell2 != null);
                double d6 = this.getCellPosition(indexedCell2);
                if (d6 != d5) {
                    this.positionCell(indexedCell2, d5);
                }
                d5 += this.getCellLength(indexedCell2);
            }
            this.cull();
            indexedCell = (IndexedCell)this.cells.getFirst();
            if (indexedCell != null) {
                n2 = this.getCellIndex(indexedCell);
                d4 = this.getCellLength(n2 - 1);
                this.addLeadingCells(n2 - 1, this.getCellPosition(indexedCell) - d4);
            } else {
                n2 = this.computeCurrentIndex();
                d4 = -this.computeViewportOffset(this.getPosition());
                this.addLeadingCells(n2, d4);
            }
            if (!this.addTrailingCells(false)) {
                double d7;
                T t2 = this.getLastVisibleCell();
                d4 = this.getCellLength(t2);
                double d8 = this.getCellPosition(t2) + d4;
                if (d8 < (d7 = this.getViewportLength())) {
                    int n3;
                    double d9 = d7 - d8;
                    for (n3 = 0; n3 < this.cells.size(); ++n3) {
                        IndexedCell indexedCell3 = (IndexedCell)this.cells.get(n3);
                        this.positionCell(indexedCell3, this.getCellPosition(indexedCell3) + d9);
                    }
                    this.setPosition(1.0);
                    indexedCell = (IndexedCell)this.cells.getFirst();
                    n3 = this.getCellIndex(indexedCell);
                    double d10 = this.getCellLength(n3 - 1);
                    this.addLeadingCells(n3 - 1, this.getCellPosition(indexedCell) - d10);
                }
            }
        }
        this.cull();
        this.updateScrollBarsAndCells(false);
        this.lastPosition = this.getPosition();
        return d2;
    }

    public void reconfigureCells() {
        this.needsReconfigureCells = true;
        this.requestLayout();
    }

    public void recreateCells() {
        this.needsRecreateCells = true;
        this.requestLayout();
    }

    public void rebuildCells() {
        this.needsRebuildCells = true;
        this.requestLayout();
    }

    public void requestCellLayout() {
        this.needsCellsLayout = true;
        this.requestLayout();
    }

    public void setCellDirty(int n2) {
        this.dirtyCells.set(n2);
        this.requestLayout();
    }

    private double getPrefBreadth(double d2) {
        double d3 = this.getMaxCellWidth(10);
        if (d2 > -1.0) {
            double d4 = this.getPrefLength();
            d3 = Math.max(d3, d4 * 0.618033987);
        }
        return d3;
    }

    private double getPrefLength() {
        double d2 = 0.0;
        int n2 = Math.min(10, this.cellCount);
        for (int i2 = 0; i2 < n2; ++i2) {
            d2 += this.getCellLength(i2);
        }
        return d2;
    }

    @Override
    protected double computePrefWidth(double d2) {
        double d3 = this.isVertical() ? this.getPrefBreadth(d2) : this.getPrefLength();
        return d3 + this.vbar.prefWidth(-1.0);
    }

    @Override
    protected double computePrefHeight(double d2) {
        double d3 = this.isVertical() ? this.getPrefLength() : this.getPrefBreadth(d2);
        return d3 + this.hbar.prefHeight(-1.0);
    }

    double getMaxCellWidth(int n2) {
        double d2 = 0.0;
        int n3 = Math.max(1, n2 == -1 ? this.cellCount : n2);
        for (int i2 = 0; i2 < n3; ++i2) {
            d2 = Math.max(d2, this.getCellBreadth(i2));
        }
        return d2;
    }

    private double computeViewportOffset(double d2) {
        double d3 = com.sun.javafx.util.Utils.clamp(0.0, d2, 1.0);
        double d4 = d3 * (double)this.getCellCount();
        int n2 = (int)d4;
        double d5 = d4 - (double)n2;
        double d6 = this.getCellLength(n2);
        double d7 = d6 * d5;
        double d8 = this.getViewportLength() * d3;
        return d7 - d8;
    }

    private void adjustPositionToIndex(int n2) {
        int n3 = this.getCellCount();
        if (n3 <= 0) {
            this.setPosition(0.0);
        } else {
            this.setPosition((double)n2 / (double)n3);
        }
    }

    private void adjustByPixelAmount(double d2) {
        double d3;
        if (d2 == 0.0) {
            return;
        }
        boolean bl = d2 > 0.0;
        int n2 = this.getCellCount();
        double d4 = this.getPosition() * (double)n2;
        int n3 = (int)d4;
        if (bl && n3 == n2) {
            return;
        }
        double d5 = this.getCellLength(n3);
        double d6 = d4 - (double)n3;
        double d7 = d5 * d6;
        double d8 = 1.0 / (double)n2;
        double d9 = this.computeOffsetForCell(n3);
        double d10 = d5 + this.computeOffsetForCell(n3 + 1);
        double d11 = d10 - d9;
        double d12 = d8 * (double)n3;
        for (d3 = bl ? d2 + d7 - this.getViewportLength() * this.getPosition() - d9 : -d2 + d10 - (d7 - this.getViewportLength() * this.getPosition()); d3 > d11 && (bl && n3 < n2 - 1 || !bl && n3 > 0); d3 -= d11) {
            n3 = bl ? ++n3 : --n3;
            d5 = this.getCellLength(n3);
            d9 = this.computeOffsetForCell(n3);
            d10 = d5 + this.computeOffsetForCell(n3 + 1);
            d11 = d10 - d9;
            d12 = d8 * (double)n3;
        }
        if (d3 > d11) {
            this.setPosition(bl ? 1.0 : 0.0);
        } else if (bl) {
            double d13 = d8 / Math.abs(d10 - d9);
            this.setPosition(d12 + d13 * d3);
        } else {
            double d14 = d8 / Math.abs(d10 - d9);
            this.setPosition(d12 + d8 - d14 * d3);
        }
    }

    private int computeCurrentIndex() {
        return (int)(this.getPosition() * (double)this.getCellCount());
    }

    private double computeOffsetForCell(int n2) {
        double d2 = this.getCellCount();
        double d3 = com.sun.javafx.util.Utils.clamp(0.0, (double)n2, d2) / d2;
        return -(this.getViewportLength() * d3);
    }

    protected void startSBReleasedAnimation() {
        if (this.sbTouchTimeline == null) {
            this.sbTouchTimeline = new Timeline();
            this.sbTouchKF1 = new KeyFrame(Duration.millis(0.0), actionEvent -> {
                this.tempVisibility = true;
                this.requestLayout();
            }, new KeyValue[0]);
            this.sbTouchKF2 = new KeyFrame(Duration.millis(1000.0), actionEvent -> {
                if (!this.touchDetected && !this.mouseDown) {
                    this.tempVisibility = false;
                    this.requestLayout();
                }
            }, new KeyValue[0]);
            this.sbTouchTimeline.getKeyFrames().addAll(this.sbTouchKF1, this.sbTouchKF2);
        }
        this.sbTouchTimeline.playFromStart();
    }

    protected void scrollBarOn() {
        this.tempVisibility = true;
        this.requestLayout();
    }

    public static class ArrayLinkedList<T>
    extends AbstractList<T> {
        private final ArrayList<T> array = new ArrayList(50);
        private int firstIndex = -1;
        private int lastIndex = -1;

        public ArrayLinkedList() {
            for (int i2 = 0; i2 < 50; ++i2) {
                this.array.add(null);
            }
        }

        public T getFirst() {
            return this.firstIndex == -1 ? null : (T)this.array.get(this.firstIndex);
        }

        public T getLast() {
            return this.lastIndex == -1 ? null : (T)this.array.get(this.lastIndex);
        }

        public void addFirst(T t2) {
            if (this.firstIndex == -1) {
                this.firstIndex = this.lastIndex = this.array.size() / 2;
                this.array.set(this.firstIndex, t2);
            } else if (this.firstIndex == 0) {
                this.array.add(0, t2);
                ++this.lastIndex;
            } else {
                this.array.set(--this.firstIndex, t2);
            }
        }

        public void addLast(T t2) {
            if (this.firstIndex == -1) {
                this.firstIndex = this.lastIndex = this.array.size() / 2;
                this.array.set(this.lastIndex, t2);
            } else if (this.lastIndex == this.array.size() - 1) {
                this.array.add(++this.lastIndex, t2);
            } else {
                this.array.set(++this.lastIndex, t2);
            }
        }

        @Override
        public int size() {
            return this.firstIndex == -1 ? 0 : this.lastIndex - this.firstIndex + 1;
        }

        @Override
        public boolean isEmpty() {
            return this.firstIndex == -1;
        }

        @Override
        public T get(int n2) {
            if (n2 > this.lastIndex - this.firstIndex || n2 < 0) {
                return null;
            }
            return this.array.get(this.firstIndex + n2);
        }

        @Override
        public void clear() {
            for (int i2 = 0; i2 < this.array.size(); ++i2) {
                this.array.set(i2, null);
            }
            this.lastIndex = -1;
            this.firstIndex = -1;
        }

        public T removeFirst() {
            if (this.isEmpty()) {
                return null;
            }
            return this.remove(0);
        }

        public T removeLast() {
            if (this.isEmpty()) {
                return null;
            }
            return this.remove(this.lastIndex - this.firstIndex);
        }

        @Override
        public T remove(int n2) {
            if (n2 > this.lastIndex - this.firstIndex || n2 < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (n2 == 0) {
                T t2 = this.array.get(this.firstIndex);
                this.array.set(this.firstIndex, null);
                if (this.firstIndex == this.lastIndex) {
                    this.lastIndex = -1;
                    this.firstIndex = -1;
                } else {
                    ++this.firstIndex;
                }
                return t2;
            }
            if (n2 == this.lastIndex - this.firstIndex) {
                T t3 = this.array.get(this.lastIndex);
                this.array.set(this.lastIndex--, null);
                return t3;
            }
            T t4 = this.array.get(this.firstIndex + n2);
            this.array.set(this.firstIndex + n2, null);
            for (int i2 = this.firstIndex + n2 + 1; i2 <= this.lastIndex; ++i2) {
                this.array.set(i2 - 1, this.array.get(i2));
            }
            this.array.set(this.lastIndex--, null);
            return t4;
        }
    }

    static class ClippedContainer
    extends Region {
        private Node node;
        private final Rectangle clipRect;

        public Node getNode() {
            return this.node;
        }

        public void setNode(Node node) {
            this.node = node;
            this.getChildren().clear();
            this.getChildren().add(this.node);
        }

        public void setClipX(double d2) {
            this.setLayoutX(-d2);
            this.clipRect.setLayoutX(d2);
        }

        public void setClipY(double d2) {
            this.setLayoutY(-d2);
            this.clipRect.setLayoutY(d2);
        }

        public ClippedContainer(VirtualFlow<?> virtualFlow) {
            if (virtualFlow == null) {
                throw new IllegalArgumentException("VirtualFlow can not be null");
            }
            this.getStyleClass().add("clipped-container");
            this.clipRect = new Rectangle();
            this.clipRect.setSmooth(false);
            this.setClip(this.clipRect);
            super.widthProperty().addListener(observable -> this.clipRect.setWidth(this.getWidth()));
            super.heightProperty().addListener(observable -> this.clipRect.setHeight(this.getHeight()));
        }
    }
}

