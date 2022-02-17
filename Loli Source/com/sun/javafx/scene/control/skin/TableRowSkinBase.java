/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TableColumnBase;
import javafx.util.Duration;

public abstract class TableRowSkinBase<T, C extends IndexedCell, B extends CellBehaviorBase<C>, R extends IndexedCell>
extends CellSkinBase<C, B> {
    private static boolean IS_STUB_TOOLKIT = Toolkit.getToolkit().toString().contains("StubToolkit");
    private static boolean DO_ANIMATIONS = !IS_STUB_TOOLKIT && !PlatformUtil.isEmbedded();
    private static final Duration FADE_DURATION = Duration.millis(200.0);
    static final Map<Control, Double> maxDisclosureWidthMap = new WeakHashMap<Control, Double>();
    private static final int DEFAULT_FULL_REFRESH_COUNTER = 100;
    protected WeakHashMap<TableColumnBase, Reference<R>> cellsMap;
    protected final List<R> cells = new ArrayList<R>();
    private int fullRefreshCounter = 100;
    protected boolean isDirty = false;
    protected boolean updateCells = false;
    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;
    private ListChangeListener<TableColumnBase> visibleLeafColumnsListener = change -> {
        this.isDirty = true;
        ((IndexedCell)this.getSkinnable()).requestLayout();
    };
    private WeakListChangeListener<TableColumnBase> weakVisibleLeafColumnsListener = new WeakListChangeListener<TableColumnBase>(this.visibleLeafColumnsListener);

    public TableRowSkinBase(C c2, B b2) {
        super(c2, b2);
    }

    protected void init(C c2) {
        ((IndexedCell)this.getSkinnable()).setPickOnBounds(false);
        this.recreateCells();
        this.updateCells(true);
        this.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        ((Cell)c2).itemProperty().addListener(observable -> this.requestCellUpdate());
        this.registerChangeListener(((IndexedCell)c2).indexProperty(), "INDEX");
        if (this.fixedCellSizeProperty() != null) {
            this.registerChangeListener(this.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
            this.fixedCellSize = this.fixedCellSizeProperty().get();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        }
    }

    protected abstract ObjectProperty<Node> graphicProperty();

    protected abstract Control getVirtualFlowOwner();

    protected abstract ObservableList<? extends TableColumnBase> getVisibleLeafColumns();

    protected abstract void updateCell(R var1, C var2);

    protected abstract DoubleProperty fixedCellSizeProperty();

    protected abstract boolean isColumnPartiallyOrFullyVisible(TableColumnBase var1);

    protected abstract R getCell(TableColumnBase var1);

    protected abstract TableColumnBase<T, ?> getTableColumnBase(R var1);

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("INDEX".equals(string)) {
            if (((IndexedCell)this.getSkinnable()).isEmpty()) {
                this.requestCellUpdate();
            }
        } else if ("FIXED_CELL_SIZE".equals(string)) {
            this.fixedCellSize = this.fixedCellSizeProperty().get();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        }
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6;
        this.checkState();
        if (this.cellsMap.isEmpty()) {
            return;
        }
        ObservableList<TableColumnBase> observableList = this.getVisibleLeafColumns();
        if (observableList.isEmpty()) {
            super.layoutChildren(d2, d3, d4, d5);
            return;
        }
        IndexedCell indexedCell = (IndexedCell)this.getSkinnable();
        double d7 = 0.0;
        double d8 = 0.0;
        double d9 = 0.0;
        boolean bl = this.isIndentationRequired();
        boolean bl2 = this.isDisclosureNodeVisible();
        int n2 = 0;
        Node node = null;
        if (bl) {
            double d10;
            TableColumnBase tableColumnBase = this.getTreeColumn();
            n2 = tableColumnBase == null ? 0 : observableList.indexOf(tableColumnBase);
            n2 = n2 < 0 ? 0 : n2;
            int n3 = this.getIndentationLevel(indexedCell);
            if (!this.isShowRoot()) {
                --n3;
            }
            d6 = this.getIndentationPerLevel();
            d7 = (double)n3 * d6;
            Control control = this.getVirtualFlowOwner();
            d8 = d10 = maxDisclosureWidthMap.containsKey(control) ? maxDisclosureWidthMap.get(control) : 0.0;
            node = this.getDisclosureNode();
            if (node != null) {
                node.setVisible(bl2);
                if (bl2 && (d8 = node.prefWidth(d5)) > d10) {
                    maxDisclosureWidthMap.put(control, d8);
                    VirtualFlow<C> virtualFlow = this.getVirtualFlow();
                    int n4 = ((IndexedCell)this.getSkinnable()).getIndex();
                    for (int i2 = 0; i2 < virtualFlow.cells.size(); ++i2) {
                        IndexedCell indexedCell2 = (IndexedCell)virtualFlow.cells.get(i2);
                        if (indexedCell2 == null || indexedCell2.isEmpty()) continue;
                        indexedCell2.requestLayout();
                        indexedCell2.layout();
                    }
                }
            }
        }
        double d11 = this.snappedTopInset() + this.snappedBottomInset();
        double d12 = this.snappedLeftInset() + this.snappedRightInset();
        double d13 = indexedCell.getHeight();
        int n5 = indexedCell.getIndex();
        if (n5 < 0) {
            return;
        }
        int n6 = this.cells.size();
        for (int i3 = 0; i3 < n6; ++i3) {
            double d14;
            IndexedCell indexedCell3 = (IndexedCell)this.cells.get(i3);
            TableColumnBase<T, ?> tableColumnBase = this.getTableColumnBase(indexedCell3);
            boolean bl3 = true;
            if (this.fixedCellSizeEnabled) {
                bl3 = this.isColumnPartiallyOrFullyVisible(tableColumnBase);
                d6 = this.fixedCellSize;
            } else {
                d6 = Math.max(d13, indexedCell3.prefHeight(-1.0));
                d6 = this.snapSize(d6) - this.snapSize(d11);
            }
            if (bl3) {
                if (this.fixedCellSizeEnabled && indexedCell3.getParent() == null) {
                    this.getChildren().add(indexedCell3);
                }
                d14 = this.snapSize(indexedCell3.prefWidth(-1.0)) - this.snapSize(d12);
                boolean bl4 = d5 <= 24.0;
                StyleOrigin styleOrigin = ((StyleableObjectProperty)indexedCell3.alignmentProperty()).getStyleOrigin();
                if (!bl4 && styleOrigin == null) {
                    indexedCell3.setAlignment(Pos.TOP_LEFT);
                }
                if (bl && i3 == n2) {
                    ObjectProperty<Node> objectProperty;
                    Node node2;
                    if (bl2) {
                        double d15 = node.prefHeight(d8);
                        if (d14 > 0.0 && d14 < d8 + d7) {
                            this.fadeOut(node);
                        } else {
                            this.fadeIn(node);
                            node.resize(d8, d15);
                            node.relocate(d2 + d7, bl4 ? d5 / 2.0 - d15 / 2.0 : d3 + indexedCell3.getPadding().getTop());
                            node.toFront();
                        }
                    }
                    Node node3 = node2 = (objectProperty = this.graphicProperty()) == null ? null : (Node)objectProperty.get();
                    if (node2 != null) {
                        d9 = node2.prefWidth(-1.0) + 3.0;
                        double d16 = node2.prefHeight(d9);
                        if (d14 > 0.0 && d14 < d8 + d7 + d9) {
                            this.fadeOut(node2);
                        } else {
                            this.fadeIn(node2);
                            node2.relocate(d2 + d7 + d8, bl4 ? d5 / 2.0 - d16 / 2.0 : d3 + indexedCell3.getPadding().getTop());
                            node2.toFront();
                        }
                    }
                }
                indexedCell3.resize(d14, d6);
                indexedCell3.relocate(d2, this.snappedTopInset());
                indexedCell3.requestLayout();
            } else {
                if (this.fixedCellSizeEnabled) {
                    this.getChildren().remove(indexedCell3);
                }
                d14 = this.snapSize(indexedCell3.prefWidth(-1.0)) - this.snapSize(d12);
            }
            d2 += d14;
        }
    }

    protected int getIndentationLevel(C c2) {
        return 0;
    }

    protected double getIndentationPerLevel() {
        return 0.0;
    }

    protected boolean isIndentationRequired() {
        return false;
    }

    protected TableColumnBase getTreeColumn() {
        return null;
    }

    protected Node getDisclosureNode() {
        return null;
    }

    protected boolean isDisclosureNodeVisible() {
        return false;
    }

    protected boolean isShowRoot() {
        return true;
    }

    protected TableColumnBase<T, ?> getVisibleLeafColumn(int n2) {
        ObservableList<TableColumnBase> observableList = this.getVisibleLeafColumns();
        if (n2 < 0 || n2 >= observableList.size()) {
            return null;
        }
        return (TableColumnBase)observableList.get(n2);
    }

    protected void updateCells(boolean bl) {
        if (bl) {
            if (this.fullRefreshCounter == 0) {
                this.recreateCells();
            }
            --this.fullRefreshCounter;
        }
        boolean bl2 = this.cells.isEmpty();
        this.cells.clear();
        IndexedCell indexedCell = (IndexedCell)this.getSkinnable();
        int n2 = indexedCell.getIndex();
        ObservableList<TableColumnBase> observableList = this.getVisibleLeafColumns();
        int n3 = observableList.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            TableColumnBase tableColumnBase = (TableColumnBase)observableList.get(i2);
            IndexedCell indexedCell2 = null;
            if (this.cellsMap.containsKey(tableColumnBase) && (indexedCell2 = (IndexedCell)this.cellsMap.get(tableColumnBase).get()) == null) {
                this.cellsMap.remove(tableColumnBase);
            }
            if (indexedCell2 == null) {
                indexedCell2 = (IndexedCell)this.createCell(tableColumnBase);
            }
            this.updateCell(indexedCell2, indexedCell);
            indexedCell2.updateIndex(n2);
            this.cells.add(indexedCell2);
        }
        if (!this.fixedCellSizeEnabled && (bl || bl2)) {
            this.getChildren().setAll((Collection<Node>)this.cells);
        }
    }

    private VirtualFlow<C> getVirtualFlow() {
        for (Object object = this.getSkinnable(); object != null; object = ((Node)object).getParent()) {
            if (!(object instanceof VirtualFlow)) continue;
            return (VirtualFlow)object;
        }
        return null;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        ObservableList<TableColumnBase> observableList = this.getVisibleLeafColumns();
        int n2 = observableList.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            d7 += ((TableColumnBase)observableList.get(i2)).getWidth();
        }
        return d7;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        this.checkState();
        if (this.getCellSize() < 24.0) {
            return this.getCellSize();
        }
        double d7 = 0.0;
        int n2 = this.cells.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i2);
            d7 = Math.max(d7, indexedCell.prefHeight(-1.0));
        }
        double d8 = Math.max(d7, Math.max(this.getCellSize(), ((IndexedCell)this.getSkinnable()).minHeight(-1.0)));
        return d8;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        this.checkState();
        if (this.getCellSize() < 24.0) {
            return this.getCellSize();
        }
        double d7 = 0.0;
        int n2 = this.cells.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i2);
            d7 = Math.max(d7, indexedCell.minHeight(-1.0));
        }
        return d7;
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMaxHeight(d2, d3, d4, d5, d6);
    }

    protected final void checkState() {
        if (this.isDirty) {
            this.updateCells(true);
            this.isDirty = false;
        } else if (this.updateCells) {
            this.updateCells(false);
            this.updateCells = false;
        }
    }

    private void requestCellUpdate() {
        this.updateCells = true;
        ((IndexedCell)this.getSkinnable()).requestLayout();
        int n2 = ((IndexedCell)this.getSkinnable()).getIndex();
        int n3 = this.cells.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            ((IndexedCell)this.cells.get(i2)).updateIndex(n2);
        }
    }

    private void recreateCells() {
        Collection<Object> collection;
        if (this.cellsMap != null) {
            collection = this.cellsMap.values();
            for (Object object : collection) {
                IndexedCell indexedCell = (IndexedCell)((Reference)object).get();
                if (indexedCell == null) continue;
                indexedCell.updateIndex(-1);
                indexedCell.getSkin().dispose();
                indexedCell.setSkin(null);
            }
            this.cellsMap.clear();
        }
        collection = this.getVisibleLeafColumns();
        this.cellsMap = new WeakHashMap(collection.size());
        this.fullRefreshCounter = 100;
        this.getChildren().clear();
        for (Object object : collection) {
            if (this.cellsMap.containsKey(object)) continue;
            this.createCell((TableColumnBase)object);
        }
    }

    private R createCell(TableColumnBase tableColumnBase) {
        R r2 = this.getCell(tableColumnBase);
        this.cellsMap.put(tableColumnBase, new WeakReference<R>(r2));
        return r2;
    }

    private void fadeOut(Node node) {
        if (node.getOpacity() < 1.0) {
            return;
        }
        if (!DO_ANIMATIONS) {
            node.setOpacity(0.0);
            return;
        }
        FadeTransition fadeTransition = new FadeTransition(FADE_DURATION, node);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    private void fadeIn(Node node) {
        if (node.getOpacity() > 0.0) {
            return;
        }
        if (!DO_ANIMATIONS) {
            node.setOpacity(1.0);
            return;
        }
        FadeTransition fadeTransition = new FadeTransition(FADE_DURATION, node);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }
}

