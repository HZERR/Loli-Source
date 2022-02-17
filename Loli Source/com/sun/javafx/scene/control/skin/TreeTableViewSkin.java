/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.behavior.TreeTableViewBehavior;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class TreeTableViewSkin<S>
extends TableViewSkinBase<S, TreeItem<S>, TreeTableView<S>, TreeTableViewBehavior<S>, TreeTableRow<S>, TreeTableColumn<S, ?>> {
    private TreeTableViewBackingList<S> tableBackingList;
    private ObjectProperty<ObservableList<TreeItem<S>>> tableBackingListProperty;
    private TreeTableView<S> treeTableView;
    private WeakReference<TreeItem<S>> weakRootRef;
    private EventHandler<TreeItem.TreeModificationEvent<S>> rootListener = treeModificationEvent -> {
        if (treeModificationEvent.wasAdded() && treeModificationEvent.wasRemoved() && treeModificationEvent.getAddedSize() == treeModificationEvent.getRemovedSize()) {
            this.rowCountDirty = true;
            ((TreeTableView)this.getSkinnable()).requestLayout();
        } else if (treeModificationEvent.getEventType().equals(TreeItem.valueChangedEvent())) {
            this.needCellsRebuilt = true;
            ((TreeTableView)this.getSkinnable()).requestLayout();
        } else {
            for (EventType<? extends Event> eventType = treeModificationEvent.getEventType(); eventType != null; eventType = eventType.getSuperType()) {
                if (!eventType.equals(TreeItem.expandedItemCountChangeEvent())) continue;
                this.rowCountDirty = true;
                ((TreeTableView)this.getSkinnable()).requestLayout();
                break;
            }
        }
        ((TreeTableView)this.getSkinnable()).edit(-1, null);
    };
    private WeakEventHandler<TreeItem.TreeModificationEvent<S>> weakRootListener;

    public TreeTableViewSkin(TreeTableView<S> treeTableView) {
        super(treeTableView, new TreeTableViewBehavior<S>(treeTableView));
        this.treeTableView = treeTableView;
        this.tableBackingList = new TreeTableViewBackingList<S>(treeTableView);
        this.tableBackingListProperty = new SimpleObjectProperty<TreeTableViewBackingList<S>>(this.tableBackingList);
        this.flow.setFixedCellSize(treeTableView.getFixedCellSize());
        super.init(treeTableView);
        this.setRoot(((TreeTableView)this.getSkinnable()).getRoot());
        EventHandler<MouseEvent> eventHandler = mouseEvent -> {
            if (treeTableView.getEditingCell() != null) {
                treeTableView.edit(-1, null);
            }
            if (treeTableView.isFocusTraversable()) {
                treeTableView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        TreeTableViewBehavior treeTableViewBehavior = (TreeTableViewBehavior)this.getBehavior();
        treeTableViewBehavior.setOnFocusPreviousRow(() -> this.onFocusPreviousCell());
        treeTableViewBehavior.setOnFocusNextRow(() -> this.onFocusNextCell());
        treeTableViewBehavior.setOnMoveToFirstCell(() -> this.onMoveToFirstCell());
        treeTableViewBehavior.setOnMoveToLastCell(() -> this.onMoveToLastCell());
        treeTableViewBehavior.setOnScrollPageDown(bl -> this.onScrollPageDown((boolean)bl));
        treeTableViewBehavior.setOnScrollPageUp(bl -> this.onScrollPageUp((boolean)bl));
        treeTableViewBehavior.setOnSelectPreviousRow(() -> this.onSelectPreviousCell());
        treeTableViewBehavior.setOnSelectNextRow(() -> this.onSelectNextCell());
        treeTableViewBehavior.setOnSelectLeftCell(() -> this.onSelectLeftCell());
        treeTableViewBehavior.setOnSelectRightCell(() -> this.onSelectRightCell());
        this.registerChangeListener(treeTableView.rootProperty(), "ROOT");
        this.registerChangeListener(treeTableView.showRootProperty(), "SHOW_ROOT");
        this.registerChangeListener(treeTableView.rowFactoryProperty(), "ROW_FACTORY");
        this.registerChangeListener(treeTableView.expandedItemCountProperty(), "TREE_ITEM_COUNT");
        this.registerChangeListener(treeTableView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ROOT".equals(string)) {
            ((TreeTableView)this.getSkinnable()).edit(-1, null);
            this.setRoot(((TreeTableView)this.getSkinnable()).getRoot());
        } else if ("SHOW_ROOT".equals(string)) {
            if (!((TreeTableView)this.getSkinnable()).isShowRoot() && this.getRoot() != null) {
                this.getRoot().setExpanded(true);
            }
            this.updateRowCount();
        } else if ("ROW_FACTORY".equals(string)) {
            this.flow.recreateCells();
        } else if ("TREE_ITEM_COUNT".equals(string)) {
            this.rowCountDirty = true;
        } else if ("FIXED_CELL_SIZE".equals(string)) {
            this.flow.setFixedCellSize(((TreeTableView)this.getSkinnable()).getFixedCellSize());
        }
    }

    private TreeItem<S> getRoot() {
        return this.weakRootRef == null ? null : (TreeItem)this.weakRootRef.get();
    }

    private void setRoot(TreeItem<S> treeItem) {
        if (this.getRoot() != null && this.weakRootListener != null) {
            this.getRoot().removeEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        this.weakRootRef = new WeakReference<TreeItem<S>>(treeItem);
        if (this.getRoot() != null) {
            this.weakRootListener = new WeakEventHandler<TreeItem.TreeModificationEvent<S>>(this.rootListener);
            this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        this.updateRowCount();
    }

    @Override
    protected ObservableList<TreeTableColumn<S, ?>> getVisibleLeafColumns() {
        return this.treeTableView.getVisibleLeafColumns();
    }

    @Override
    protected int getVisibleLeafIndex(TreeTableColumn<S, ?> treeTableColumn) {
        return this.treeTableView.getVisibleLeafIndex(treeTableColumn);
    }

    @Override
    protected TreeTableColumn<S, ?> getVisibleLeafColumn(int n2) {
        return this.treeTableView.getVisibleLeafColumn(n2);
    }

    protected TreeTableView.TreeTableViewFocusModel<S> getFocusModel() {
        return this.treeTableView.getFocusModel();
    }

    protected TreeTablePosition<S, ?> getFocusedCell() {
        return this.treeTableView.getFocusModel().getFocusedCell();
    }

    @Override
    protected TableSelectionModel<TreeItem<S>> getSelectionModel() {
        return this.treeTableView.getSelectionModel();
    }

    @Override
    protected ObjectProperty<Callback<TreeTableView<S>, TreeTableRow<S>>> rowFactoryProperty() {
        return this.treeTableView.rowFactoryProperty();
    }

    @Override
    protected ObjectProperty<Node> placeholderProperty() {
        return this.treeTableView.placeholderProperty();
    }

    @Override
    protected ObjectProperty<ObservableList<TreeItem<S>>> itemsProperty() {
        return this.tableBackingListProperty;
    }

    @Override
    protected ObservableList<TreeTableColumn<S, ?>> getColumns() {
        return this.treeTableView.getColumns();
    }

    @Override
    protected BooleanProperty tableMenuButtonVisibleProperty() {
        return this.treeTableView.tableMenuButtonVisibleProperty();
    }

    @Override
    protected ObjectProperty<Callback<ResizeFeaturesBase, Boolean>> columnResizePolicyProperty() {
        return this.treeTableView.columnResizePolicyProperty();
    }

    @Override
    protected ObservableList<TreeTableColumn<S, ?>> getSortOrder() {
        return this.treeTableView.getSortOrder();
    }

    @Override
    protected boolean resizeColumn(TreeTableColumn<S, ?> treeTableColumn, double d2) {
        return this.treeTableView.resizeColumn(treeTableColumn, d2);
    }

    @Override
    protected void edit(int n2, TreeTableColumn<S, ?> treeTableColumn) {
        this.treeTableView.edit(n2, treeTableColumn);
    }

    @Override
    protected void resizeColumnToFitContent(TreeTableColumn<S, ?> treeTableColumn, int n2) {
        double d2;
        TreeTableRow<S> treeTableRow;
        Node node;
        TreeTableColumn<S, ?> treeTableColumn2 = treeTableColumn;
        List list = (List)this.itemsProperty().get();
        if (list == null || list.isEmpty()) {
            return;
        }
        Callback<TreeTableColumn<S, ?>, TreeTableCell<S, ?>> callback = treeTableColumn2.getCellFactory();
        if (callback == null) {
            return;
        }
        TreeTableCell<Object, ?> treeTableCell = callback.call(treeTableColumn2);
        if (treeTableCell == null) {
            return;
        }
        treeTableCell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);
        double d3 = 10.0;
        Node node2 = node = treeTableCell.getSkin() == null ? null : treeTableCell.getSkin().getNode();
        if (node instanceof Region) {
            treeTableRow = (TreeTableRow<S>)node;
            d3 = treeTableRow.snappedLeftInset() + treeTableRow.snappedRightInset();
        }
        treeTableRow = new TreeTableRow<S>();
        treeTableRow.updateTreeTableView(this.treeTableView);
        int n3 = n2 == -1 ? list.size() : Math.min(list.size(), n2);
        double d4 = 0.0;
        for (int i2 = 0; i2 < n3; ++i2) {
            treeTableRow.updateIndex(i2);
            treeTableRow.updateTreeItem(this.treeTableView.getTreeItem(i2));
            treeTableCell.updateTreeTableColumn(treeTableColumn2);
            treeTableCell.updateTreeTableView(this.treeTableView);
            treeTableCell.updateTreeTableRow(treeTableRow);
            treeTableCell.updateIndex(i2);
            if ((treeTableCell.getText() == null || treeTableCell.getText().isEmpty()) && treeTableCell.getGraphic() == null) continue;
            this.getChildren().add(treeTableCell);
            treeTableCell.applyCss();
            d2 = treeTableCell.prefWidth(-1.0);
            d4 = Math.max(d4, d2);
            this.getChildren().remove(treeTableCell);
        }
        treeTableCell.updateIndex(-1);
        TableColumnHeader tableColumnHeader = this.getTableHeaderRow().getColumnHeaderFor(treeTableColumn);
        d2 = Utils.computeTextWidth(tableColumnHeader.label.getFont(), treeTableColumn.getText(), -1.0);
        Node node3 = tableColumnHeader.label.getGraphic();
        double d5 = node3 == null ? 0.0 : node3.prefWidth(-1.0) + tableColumnHeader.label.getGraphicTextGap();
        double d6 = d2 + d5 + 10.0 + tableColumnHeader.snappedLeftInset() + tableColumnHeader.snappedRightInset();
        d4 = Math.max(d4, d6);
        d4 += d3;
        if (this.treeTableView.getColumnResizePolicy() == TreeTableView.CONSTRAINED_RESIZE_POLICY) {
            d4 = Math.max(d4, treeTableColumn2.getWidth());
        }
        treeTableColumn2.impl_setWidth(d4);
    }

    @Override
    public int getItemCount() {
        return this.treeTableView.getExpandedItemCount();
    }

    @Override
    public TreeTableRow<S> createCell() {
        TreeTableRow<Object> treeTableRow = this.treeTableView.getRowFactory() != null ? this.treeTableView.getRowFactory().call(this.treeTableView) : new TreeTableRow();
        if (treeTableRow.getDisclosureNode() == null) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().setAll("tree-disclosure-node");
            stackPane.setMouseTransparent(true);
            StackPane stackPane2 = new StackPane();
            stackPane2.getStyleClass().setAll("arrow");
            stackPane.getChildren().add(stackPane2);
            treeTableRow.setDisclosureNode(stackPane);
        }
        treeTableRow.updateTreeTableView(this.treeTableView);
        return treeTableRow;
    }

    @Override
    protected void horizontalScroll() {
        super.horizontalScroll();
        if (((TreeTableView)this.getSkinnable()).getFixedCellSize() > 0.0) {
            this.flow.requestCellLayout();
        }
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case ROW_AT_INDEX: {
                int n2 = (Integer)arrobject[0];
                return n2 < 0 ? null : this.flow.getPrivateCell(n2);
            }
            case SELECTED_ITEMS: {
                ArrayList<TreeTableRow> arrayList = new ArrayList<TreeTableRow>();
                TreeTableView.TreeTableViewSelectionModel treeTableViewSelectionModel = ((TreeTableView)this.getSkinnable()).getSelectionModel();
                for (TreeTablePosition treeTablePosition : treeTableViewSelectionModel.getSelectedCells()) {
                    TreeTableRow treeTableRow = (TreeTableRow)this.flow.getPrivateCell(treeTablePosition.getRow());
                    if (treeTableRow == null) continue;
                    arrayList.add(treeTableRow);
                }
                return FXCollections.observableArrayList(arrayList);
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    @Override
    protected void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case SHOW_ITEM: {
                Node node = (Node)arrobject[0];
                if (!(node instanceof TreeTableCell)) break;
                TreeTableCell treeTableCell = (TreeTableCell)node;
                this.flow.show(treeTableCell.getIndex());
                break;
            }
            case SET_SELECTED_ITEMS: {
                TreeTableView.TreeTableViewSelectionModel treeTableViewSelectionModel;
                ObservableList observableList = (ObservableList)arrobject[0];
                if (observableList == null || (treeTableViewSelectionModel = ((TreeTableView)this.getSkinnable()).getSelectionModel()) == null) break;
                treeTableViewSelectionModel.clearSelection();
                for (Node node : observableList) {
                    if (!(node instanceof TreeTableCell)) continue;
                    TreeTableCell treeTableCell = (TreeTableCell)node;
                    treeTableViewSelectionModel.select(treeTableCell.getIndex(), treeTableCell.getTableColumn());
                }
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }

    @Override
    protected void updateRowCount() {
        this.updatePlaceholderRegionVisibility();
        this.tableBackingList.resetSize();
        int n2 = this.flow.getCellCount();
        int n3 = this.getItemCount();
        this.flow.setCellCount(n3);
        if (this.forceCellRecreate) {
            this.needCellsRecreated = true;
            this.forceCellRecreate = false;
        } else if (n3 != n2) {
            this.needCellsRebuilt = true;
        } else {
            this.needCellsReconfigured = true;
        }
    }

    private static class TreeTableViewBackingList<S>
    extends ReadOnlyUnbackedObservableList<TreeItem<S>> {
        private final TreeTableView<S> treeTable;
        private int size = -1;

        TreeTableViewBackingList(TreeTableView<S> treeTableView) {
            this.treeTable = treeTableView;
        }

        void resetSize() {
            int n2 = this.size;
            this.size = -1;
            this.callObservers(new NonIterableChange.GenericAddRemoveChange(0, n2, FXCollections.emptyObservableList(), this));
        }

        @Override
        public TreeItem<S> get(int n2) {
            return this.treeTable.getTreeItem(n2);
        }

        @Override
        public int size() {
            if (this.size == -1) {
                this.size = this.treeTable.getExpandedItemCount();
            }
            return this.size;
        }
    }
}

