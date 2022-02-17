/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.VirtualContainerBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.Skinnable;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public abstract class TableViewSkinBase<M, S, C extends Control, B extends BehaviorBase<C>, I extends IndexedCell<M>, TC extends TableColumnBase<S, ?>>
extends VirtualContainerBase<C, B, I> {
    public static final String REFRESH = "tableRefreshKey";
    public static final String RECREATE = "tableRecreateKey";
    private boolean contentWidthDirty = true;
    private Region columnReorderLine;
    private Region columnReorderOverlay;
    private TableHeaderRow tableHeaderRow;
    private Callback<C, I> rowFactory;
    private StackPane placeholderRegion;
    private Label placeholderLabel;
    private static final String EMPTY_TABLE_TEXT = ControlResources.getString("TableView.noContent");
    private static final String NO_COLUMNS_TEXT = ControlResources.getString("TableView.noColumns");
    private int visibleColCount;
    protected boolean needCellsRebuilt = true;
    protected boolean needCellsRecreated = true;
    protected boolean needCellsReconfigured = false;
    private int itemCount = -1;
    protected boolean forceCellRecreate = false;
    private static final boolean IS_PANNABLE = AccessController.doPrivileged(() -> Boolean.getBoolean("com.sun.javafx.scene.control.skin.TableViewSkin.pannable"));
    private MapChangeListener<Object, Object> propertiesMapListener = change -> {
        if (!change.wasAdded()) {
            return;
        }
        if ("tableRefreshKey".equals(change.getKey())) {
            this.refreshView();
            ((Node)((Object)this.getSkinnable())).getProperties().remove("tableRefreshKey");
        } else if ("tableRecreateKey".equals(change.getKey())) {
            this.forceCellRecreate = true;
            this.refreshView();
            ((Node)((Object)this.getSkinnable())).getProperties().remove("tableRecreateKey");
        }
    };
    private ListChangeListener<S> rowCountListener = change -> {
        while (change.next()) {
            if (change.wasReplaced()) {
                this.itemCount = 0;
                break;
            }
            if (change.getRemovedSize() != this.itemCount) continue;
            this.itemCount = 0;
            break;
        }
        if (this.getSkinnable() instanceof TableView) {
            this.edit(-1, null);
        }
        this.rowCountDirty = true;
        ((Parent)((Object)this.getSkinnable())).requestLayout();
    };
    private ListChangeListener<TC> visibleLeafColumnsListener = change -> {
        this.updateVisibleColumnCount();
        while (change.next()) {
            this.updateVisibleLeafColumnWidthListeners(change.getAddedSubList(), change.getRemoved());
        }
    };
    private InvalidationListener widthListener = observable -> {
        this.needCellsReconfigured = true;
        if (this.getSkinnable() != null) {
            ((Parent)((Object)this.getSkinnable())).requestLayout();
        }
    };
    private InvalidationListener itemsChangeListener;
    private WeakListChangeListener<S> weakRowCountListener = new WeakListChangeListener<S>(this.rowCountListener);
    private WeakListChangeListener<TC> weakVisibleLeafColumnsListener = new WeakListChangeListener<TC>(this.visibleLeafColumnsListener);
    private WeakInvalidationListener weakWidthListener = new WeakInvalidationListener(this.widthListener);
    private WeakInvalidationListener weakItemsChangeListener;
    private static final double GOLDEN_RATIO_MULTIPLIER = 0.618033987;

    public TableViewSkinBase(C c2, B b2) {
        super(c2, b2);
    }

    protected void init(C c2) {
        this.flow.setPannable(IS_PANNABLE);
        this.flow.setCreateCell(virtualFlow -> this.createCell());
        InvalidationListener invalidationListener = observable -> this.horizontalScroll();
        this.flow.getHbar().valueProperty().addListener(invalidationListener);
        this.flow.getHbar().setUnitIncrement(15.0);
        this.flow.getHbar().setBlockIncrement(80.0);
        this.columnReorderLine = new Region();
        this.columnReorderLine.getStyleClass().setAll("column-resize-line");
        this.columnReorderLine.setManaged(false);
        this.columnReorderLine.setVisible(false);
        this.columnReorderOverlay = new Region();
        this.columnReorderOverlay.getStyleClass().setAll("column-overlay");
        this.columnReorderOverlay.setVisible(false);
        this.columnReorderOverlay.setManaged(false);
        this.tableHeaderRow = this.createTableHeaderRow();
        this.tableHeaderRow.setFocusTraversable(false);
        this.getChildren().addAll(this.tableHeaderRow, this.flow, this.columnReorderOverlay, this.columnReorderLine);
        this.updateVisibleColumnCount();
        this.updateVisibleLeafColumnWidthListeners(this.getVisibleLeafColumns(), FXCollections.emptyObservableList());
        this.tableHeaderRow.reorderingProperty().addListener(observable -> ((Parent)((Object)this.getSkinnable())).requestLayout());
        this.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        this.updateTableItems(null, (ObservableList)this.itemsProperty().get());
        this.itemsChangeListener = new InvalidationListener(){
            private WeakReference<ObservableList<S>> weakItemsRef;
            {
                this.weakItemsRef = new WeakReference(TableViewSkinBase.this.itemsProperty().get());
            }

            @Override
            public void invalidated(Observable observable) {
                ObservableList observableList = (ObservableList)this.weakItemsRef.get();
                this.weakItemsRef = new WeakReference(TableViewSkinBase.this.itemsProperty().get());
                TableViewSkinBase.this.updateTableItems(observableList, (ObservableList)TableViewSkinBase.this.itemsProperty().get());
            }
        };
        this.weakItemsChangeListener = new WeakInvalidationListener(this.itemsChangeListener);
        this.itemsProperty().addListener(this.weakItemsChangeListener);
        ObservableMap<Object, Object> observableMap = ((Node)c2).getProperties();
        observableMap.remove(REFRESH);
        observableMap.remove(RECREATE);
        observableMap.addListener(this.propertiesMapListener);
        ((Node)c2).addEventHandler(ScrollToEvent.scrollToColumn(), scrollToEvent -> this.scrollHorizontally((TableColumnBase)scrollToEvent.getScrollTarget()));
        InvalidationListener invalidationListener2 = observable -> {
            this.contentWidthDirty = true;
            ((Parent)((Object)this.getSkinnable())).requestLayout();
        };
        this.flow.widthProperty().addListener(invalidationListener2);
        this.flow.getVbar().widthProperty().addListener(invalidationListener2);
        this.registerChangeListener(this.rowFactoryProperty(), "ROW_FACTORY");
        this.registerChangeListener(this.placeholderProperty(), "PLACEHOLDER");
        this.registerChangeListener(((Region)c2).widthProperty(), "WIDTH");
        this.registerChangeListener(this.flow.getVbar().visibleProperty(), "VBAR_VISIBLE");
    }

    protected abstract TableSelectionModel<S> getSelectionModel();

    protected abstract TableFocusModel<S, TC> getFocusModel();

    protected abstract TablePositionBase<? extends TC> getFocusedCell();

    protected abstract ObservableList<? extends TC> getVisibleLeafColumns();

    protected abstract int getVisibleLeafIndex(TC var1);

    protected abstract TC getVisibleLeafColumn(int var1);

    protected abstract ObservableList<TC> getColumns();

    protected abstract ObservableList<TC> getSortOrder();

    protected abstract ObjectProperty<ObservableList<S>> itemsProperty();

    protected abstract ObjectProperty<Callback<C, I>> rowFactoryProperty();

    protected abstract ObjectProperty<Node> placeholderProperty();

    protected abstract BooleanProperty tableMenuButtonVisibleProperty();

    protected abstract ObjectProperty<Callback<ResizeFeaturesBase, Boolean>> columnResizePolicyProperty();

    protected abstract boolean resizeColumn(TC var1, double var2);

    protected abstract void resizeColumnToFitContent(TC var1, int var2);

    protected abstract void edit(int var1, TC var2);

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ROW_FACTORY".equals(string)) {
            Callback<C, I> callback = this.rowFactory;
            this.rowFactory = (Callback)this.rowFactoryProperty().get();
            if (callback != this.rowFactory) {
                this.needCellsRebuilt = true;
                ((Parent)((Object)this.getSkinnable())).requestLayout();
            }
        } else if ("PLACEHOLDER".equals(string)) {
            this.updatePlaceholderRegionVisibility();
        } else if ("VBAR_VISIBLE".equals(string)) {
            this.updateContentWidth();
        }
    }

    @Override
    public void dispose() {
        this.getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
        this.itemsProperty().removeListener(this.weakItemsChangeListener);
        ((Node)((Object)this.getSkinnable())).getProperties().removeListener(this.propertiesMapListener);
        this.updateTableItems((ObservableList)this.itemsProperty().get(), null);
        super.dispose();
    }

    protected TableHeaderRow createTableHeaderRow() {
        return new TableHeaderRow(this);
    }

    public TableHeaderRow getTableHeaderRow() {
        return this.tableHeaderRow;
    }

    public Region getColumnReorderLine() {
        return this.columnReorderLine;
    }

    public int onScrollPageDown(boolean bl) {
        int n2;
        boolean bl2;
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return -1;
        }
        int n3 = this.getItemCount();
        Object t2 = this.flow.getLastVisibleCellWithinViewPort();
        if (t2 == null) {
            return -1;
        }
        int n4 = ((IndexedCell)t2).getIndex();
        int n5 = n4 = n4 >= n3 ? n3 - 1 : n4;
        if (bl) {
            bl2 = ((Node)t2).isFocused() || this.isCellFocused(n4);
        } else {
            boolean bl3 = bl2 = ((Cell)t2).isSelected() || this.isCellSelected(n4);
        }
        if (bl2 && (n2 = (int)(this.isLeadIndex(bl, n4) ? 1 : 0)) != 0) {
            this.flow.showAsFirst(t2);
            Object t3 = this.flow.getLastVisibleCellWithinViewPort();
            t2 = t3 == null ? t2 : t3;
        }
        n2 = (n2 = ((IndexedCell)t2).getIndex()) >= n3 ? n3 - 1 : n2;
        this.flow.show(n2);
        return n2;
    }

    public int onScrollPageUp(boolean bl) {
        int n2;
        Object t2 = this.flow.getFirstVisibleCellWithinViewPort();
        if (t2 == null) {
            return -1;
        }
        int n3 = ((IndexedCell)t2).getIndex();
        boolean bl2 = false;
        if (bl) {
            bl2 = ((Node)t2).isFocused() || this.isCellFocused(n3);
        } else {
            boolean bl3 = bl2 = ((Cell)t2).isSelected() || this.isCellSelected(n3);
        }
        if (bl2 && (n2 = (int)(this.isLeadIndex(bl, n3) ? 1 : 0)) != 0) {
            this.flow.showAsLast(t2);
            Object t3 = this.flow.getFirstVisibleCellWithinViewPort();
            t2 = t3 == null ? t2 : t3;
        }
        n2 = ((IndexedCell)t2).getIndex();
        this.flow.show(n2);
        return n2;
    }

    private boolean isLeadIndex(boolean bl, int n2) {
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        return bl && tableFocusModel.getFocusedIndex() == n2 || !bl && tableSelectionModel.getSelectedIndex() == n2;
    }

    boolean isColumnPartiallyOrFullyVisible(TC TC) {
        Object object;
        if (TC == null || !((TableColumnBase)TC).isVisible()) {
            return false;
        }
        double d2 = this.flow.getHbar().getValue();
        double d3 = 0.0;
        ObservableList<TC> observableList = this.getVisibleLeafColumns();
        int n2 = observableList.size();
        for (int i2 = 0; i2 < n2 && !(object = (TableColumnBase)observableList.get(i2)).equals(TC); ++i2) {
            d3 += ((TableColumnBase)object).getWidth();
        }
        double d4 = d3 + ((TableColumnBase)TC).getWidth();
        object = ((Region)((Object)this.getSkinnable())).getPadding();
        double d5 = ((Region)((Object)this.getSkinnable())).getWidth() - ((Insets)object).getLeft() + ((Insets)object).getRight();
        return (d3 >= d2 || d4 > d2) && (d3 < d5 + d2 || d4 <= d5 + d2);
    }

    protected void horizontalScroll() {
        this.tableHeaderRow.updateScrollX();
    }

    @Override
    protected void updateRowCount() {
        int n2;
        this.updatePlaceholderRegionVisibility();
        int n3 = this.itemCount;
        this.itemCount = n2 = this.getItemCount();
        this.flow.setCellCount(n2);
        if (this.forceCellRecreate) {
            this.needCellsRecreated = true;
            this.forceCellRecreate = false;
        } else if (n2 != n3) {
            this.needCellsRebuilt = true;
        } else {
            this.needCellsReconfigured = true;
        }
    }

    protected void onFocusPreviousCell() {
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        this.flow.show(tableFocusModel.getFocusedIndex());
    }

    protected void onFocusNextCell() {
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        this.flow.show(tableFocusModel.getFocusedIndex());
    }

    protected void onSelectPreviousCell() {
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        this.flow.show(tableSelectionModel.getSelectedIndex());
    }

    protected void onSelectNextCell() {
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        this.flow.show(tableSelectionModel.getSelectedIndex());
    }

    protected void onSelectLeftCell() {
        this.scrollHorizontally();
    }

    protected void onSelectRightCell() {
        this.scrollHorizontally();
    }

    protected void onMoveToFirstCell() {
        this.flow.show(0);
        this.flow.setPosition(0.0);
    }

    protected void onMoveToLastCell() {
        int n2 = this.getItemCount();
        this.flow.show(n2);
        this.flow.setPosition(1.0);
    }

    public void updateTableItems(ObservableList<S> observableList, ObservableList<S> observableList2) {
        if (observableList != null) {
            observableList.removeListener(this.weakRowCountListener);
        }
        if (observableList2 != null) {
            observableList2.addListener(this.weakRowCountListener);
        }
        this.rowCountDirty = true;
        ((Parent)((Object)this.getSkinnable())).requestLayout();
    }

    private void checkContentWidthState() {
        if (this.contentWidthDirty || this.getItemCount() == 0) {
            this.updateContentWidth();
            this.contentWidthDirty = false;
        }
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        return 400.0;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.computePrefHeight(-1.0, d3, d4, d5, d6);
        ObservableList<TC> observableList = this.getVisibleLeafColumns();
        if (observableList == null || observableList.isEmpty()) {
            return d7 * 0.618033987;
        }
        double d8 = d6 + d4;
        int n2 = observableList.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            TableColumnBase tableColumnBase = (TableColumnBase)observableList.get(i2);
            d8 += Math.max(tableColumnBase.getPrefWidth(), tableColumnBase.getMinWidth());
        }
        return Math.max(d8, d7 * 0.618033987);
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        Skinnable skinnable = this.getSkinnable();
        if (skinnable == null) {
            return;
        }
        super.layoutChildren(d2, d3, d4, d5);
        if (this.needCellsRecreated) {
            this.flow.recreateCells();
        } else if (this.needCellsRebuilt) {
            this.flow.rebuildCells();
        } else if (this.needCellsReconfigured) {
            this.flow.reconfigureCells();
        }
        this.needCellsRebuilt = false;
        this.needCellsRecreated = false;
        this.needCellsReconfigured = false;
        double d6 = ((Node)((Object)skinnable)).getLayoutBounds().getHeight() / 2.0;
        double d7 = this.tableHeaderRow.prefHeight(-1.0);
        this.layoutInArea(this.tableHeaderRow, d2, d3, d4, d7, d6, HPos.CENTER, VPos.CENTER);
        d3 += d7;
        double d8 = Math.floor(d5 - d7);
        if (this.getItemCount() == 0 || this.visibleColCount == 0) {
            this.layoutInArea(this.placeholderRegion, d2, d3, d4, d8, d6, HPos.CENTER, VPos.CENTER);
        } else {
            this.layoutInArea(this.flow, d2, d3, d4, d8, d6, HPos.CENTER, VPos.CENTER);
        }
        if (this.tableHeaderRow.getReorderingRegion() != null) {
            TableColumnHeader tableColumnHeader = this.tableHeaderRow.getReorderingRegion();
            TableColumnBase tableColumnBase = tableColumnHeader.getTableColumn();
            if (tableColumnBase != null) {
                TableColumnHeader tableColumnHeader2 = this.tableHeaderRow.getReorderingRegion();
                double d9 = this.tableHeaderRow.sceneToLocal(tableColumnHeader2.localToScene(tableColumnHeader2.getBoundsInLocal())).getMinX();
                double d10 = tableColumnHeader.getWidth();
                if (d9 < 0.0) {
                    d10 += d9;
                }
                double d11 = d9 = d9 < 0.0 ? 0.0 : d9;
                if (d9 + d10 > d4) {
                    d10 = d4 - d9;
                    if (this.flow.getVbar().isVisible()) {
                        d10 -= this.flow.getVbar().getWidth() - 1.0;
                    }
                }
                double d12 = d8;
                if (this.flow.getHbar().isVisible()) {
                    d12 -= this.flow.getHbar().getHeight();
                }
                this.columnReorderOverlay.resize(d10, d12);
                this.columnReorderOverlay.setLayoutX(d9);
                this.columnReorderOverlay.setLayoutY(this.tableHeaderRow.getHeight());
            }
            double d13 = this.columnReorderLine.snappedLeftInset() + this.columnReorderLine.snappedRightInset();
            double d14 = d5 - (this.flow.getHbar().isVisible() ? this.flow.getHbar().getHeight() - 1.0 : 0.0);
            this.columnReorderLine.resizeRelocate(0.0, this.columnReorderLine.snappedTopInset(), d13, d14);
        }
        this.columnReorderLine.setVisible(this.tableHeaderRow.isReordering());
        this.columnReorderOverlay.setVisible(this.tableHeaderRow.isReordering());
        this.checkContentWidthState();
    }

    private void updateVisibleColumnCount() {
        this.visibleColCount = this.getVisibleLeafColumns().size();
        this.updatePlaceholderRegionVisibility();
        this.needCellsRebuilt = true;
        ((Parent)((Object)this.getSkinnable())).requestLayout();
    }

    private void updateVisibleLeafColumnWidthListeners(List<? extends TC> list, List<? extends TC> list2) {
        TableColumnBase tableColumnBase;
        int n2;
        int n3 = list2.size();
        for (n2 = 0; n2 < n3; ++n2) {
            tableColumnBase = (TableColumnBase)list2.get(n2);
            tableColumnBase.widthProperty().removeListener(this.weakWidthListener);
        }
        n3 = list.size();
        for (n2 = 0; n2 < n3; ++n2) {
            tableColumnBase = (TableColumnBase)list.get(n2);
            tableColumnBase.widthProperty().addListener(this.weakWidthListener);
        }
        this.needCellsRebuilt = true;
        ((Parent)((Object)this.getSkinnable())).requestLayout();
    }

    protected final void updatePlaceholderRegionVisibility() {
        boolean bl;
        boolean bl2 = bl = this.visibleColCount == 0 || this.getItemCount() == 0;
        if (bl) {
            Node node;
            if (this.placeholderRegion == null) {
                this.placeholderRegion = new StackPane();
                this.placeholderRegion.getStyleClass().setAll("placeholder");
                this.getChildren().add(this.placeholderRegion);
            }
            if ((node = (Node)this.placeholderProperty().get()) == null) {
                if (this.placeholderLabel == null) {
                    this.placeholderLabel = new Label();
                }
                String string = this.visibleColCount == 0 ? NO_COLUMNS_TEXT : EMPTY_TABLE_TEXT;
                this.placeholderLabel.setText(string);
                this.placeholderRegion.getChildren().setAll(this.placeholderLabel);
            } else {
                this.placeholderRegion.getChildren().setAll(node);
            }
        }
        this.flow.setVisible(!bl);
        if (this.placeholderRegion != null) {
            this.placeholderRegion.setVisible(bl);
        }
    }

    private void updateContentWidth() {
        double d2 = this.flow.getWidth();
        if (this.flow.getVbar().isVisible()) {
            d2 -= this.flow.getVbar().getWidth();
        }
        if (d2 <= 0.0) {
            Skinnable skinnable = this.getSkinnable();
            d2 = ((Region)((Object)skinnable)).getWidth() - (this.snappedLeftInset() + this.snappedRightInset());
        }
        d2 = Math.max(0.0, d2);
        ((Node)((Object)this.getSkinnable())).getProperties().put("TableView.contentWidth", Math.floor(d2));
    }

    private void refreshView() {
        this.rowCountDirty = true;
        Skinnable skinnable = this.getSkinnable();
        if (skinnable != null) {
            ((Parent)((Object)skinnable)).requestLayout();
        }
    }

    protected void scrollHorizontally() {
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TC TC = this.getFocusedCell().getTableColumn();
        this.scrollHorizontally(TC);
    }

    protected void scrollHorizontally(TC TC) {
        double d2;
        TableColumnBase tableColumnBase;
        if (TC == null || !((TableColumnBase)TC).isVisible()) {
            return;
        }
        Skinnable skinnable = this.getSkinnable();
        TableColumnHeader tableColumnHeader = this.tableHeaderRow.getColumnHeaderFor((TableColumnBase<?, ?>)TC);
        if (tableColumnHeader == null || tableColumnHeader.getWidth() <= 0.0) {
            Platform.runLater(() -> this.scrollHorizontally(TC));
            return;
        }
        double d3 = 0.0;
        Iterator iterator = this.getVisibleLeafColumns().iterator();
        while (iterator.hasNext() && !(tableColumnBase = (TableColumnBase)iterator.next()).equals(TC)) {
            d3 += tableColumnBase.getWidth();
        }
        double d4 = d3 + ((TableColumnBase)TC).getWidth();
        double d5 = ((Region)((Object)skinnable)).getWidth() - this.snappedLeftInset() - this.snappedRightInset();
        double d6 = this.flow.getHbar().getValue();
        double d7 = this.flow.getHbar().getMax();
        if (d3 < d6 && d3 >= 0.0) {
            d2 = d3;
        } else {
            double d8 = d3 < 0.0 || d4 > d5 ? d3 - d6 : 0.0;
            d2 = d6 + d8 > d7 ? d7 : d6 + d8;
        }
        this.flow.getHbar().setValue(d2);
    }

    private boolean isCellSelected(int n2) {
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return false;
        }
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            return false;
        }
        int n3 = this.getVisibleLeafColumns().size();
        for (int i2 = 0; i2 < n3; ++i2) {
            if (!tableSelectionModel.isSelected(n2, (TableColumnBase<S, ?>)this.getVisibleLeafColumn(i2))) continue;
            return true;
        }
        return false;
    }

    private boolean isCellFocused(int n2) {
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return false;
        }
        int n3 = this.getVisibleLeafColumns().size();
        for (int i2 = 0; i2 < n3; ++i2) {
            if (!tableFocusModel.isFocused(n2, this.getVisibleLeafColumn(i2))) continue;
            return true;
        }
        return false;
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
                int n2 = tableFocusModel.getFocusedIndex();
                if (n2 == -1) {
                    if (this.placeholderRegion != null && this.placeholderRegion.isVisible()) {
                        return this.placeholderRegion.getChildren().get(0);
                    }
                    if (this.getItemCount() > 0) {
                        n2 = 0;
                    } else {
                        return null;
                    }
                }
                return this.flow.getPrivateCell(n2);
            }
            case CELL_AT_ROW_COLUMN: {
                int n3 = (Integer)arrobject[0];
                return this.flow.getPrivateCell(n3);
            }
            case COLUMN_AT_INDEX: {
                int n4 = (Integer)arrobject[0];
                TC TC = this.getVisibleLeafColumn(n4);
                return this.getTableHeaderRow().getColumnHeaderFor((TableColumnBase<?, ?>)TC);
            }
            case HEADER: {
                return this.getTableHeaderRow();
            }
            case VERTICAL_SCROLLBAR: {
                return this.flow.getVbar();
            }
            case HORIZONTAL_SCROLLBAR: {
                return this.flow.getHbar();
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

