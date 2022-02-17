/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ListViewBehavior;
import com.sun.javafx.scene.control.skin.VirtualContainerBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class ListViewSkin<T>
extends VirtualContainerBase<ListView<T>, ListViewBehavior<T>, ListCell<T>> {
    public static final String RECREATE = "listRecreateKey";
    private StackPane placeholderRegion;
    private Node placeholderNode;
    private static final String EMPTY_LIST_TEXT = ControlResources.getString("ListView.noContent");
    private static final boolean IS_PANNABLE = AccessController.doPrivileged(() -> Boolean.getBoolean("com.sun.javafx.scene.control.skin.ListViewSkin.pannable"));
    private ObservableList<T> listViewItems;
    private final InvalidationListener itemsChangeListener = observable -> this.updateListViewItems();
    private MapChangeListener<Object, Object> propertiesMapListener = change -> {
        if (!change.wasAdded()) {
            return;
        }
        if (RECREATE.equals(change.getKey())) {
            this.needCellsRebuilt = true;
            ((ListView)this.getSkinnable()).requestLayout();
            ((ListView)this.getSkinnable()).getProperties().remove(RECREATE);
        }
    };
    private final ListChangeListener<T> listViewItemsListener = new ListChangeListener<T>(){

        @Override
        public void onChanged(ListChangeListener.Change<? extends T> change) {
            while (change.next()) {
                if (change.wasReplaced()) {
                    for (int i2 = change.getFrom(); i2 < change.getTo(); ++i2) {
                        ListViewSkin.this.flow.setCellDirty(i2);
                    }
                    break;
                }
                if (change.getRemovedSize() != ListViewSkin.this.itemCount) continue;
                ListViewSkin.this.itemCount = 0;
                break;
            }
            ((ListView)ListViewSkin.this.getSkinnable()).edit(-1);
            ListViewSkin.this.rowCountDirty = true;
            ((ListView)ListViewSkin.this.getSkinnable()).requestLayout();
        }
    };
    private final WeakListChangeListener<T> weakListViewItemsListener = new WeakListChangeListener<T>(this.listViewItemsListener);
    private int itemCount = -1;
    private boolean needCellsRebuilt = true;
    private boolean needCellsReconfigured = false;

    public ListViewSkin(ListView<T> listView) {
        super(listView, new ListViewBehavior<T>(listView));
        this.updateListViewItems();
        this.flow.setId("virtual-flow");
        this.flow.setPannable(IS_PANNABLE);
        this.flow.setVertical(((ListView)this.getSkinnable()).getOrientation() == Orientation.VERTICAL);
        this.flow.setCreateCell(virtualFlow -> this.createCell());
        this.flow.setFixedCellSize(listView.getFixedCellSize());
        this.getChildren().add(this.flow);
        EventHandler<MouseEvent> eventHandler = mouseEvent -> {
            if (listView.getEditingIndex() > -1) {
                listView.edit(-1);
            }
            if (listView.isFocusTraversable()) {
                listView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.updateRowCount();
        listView.itemsProperty().addListener(new WeakInvalidationListener(this.itemsChangeListener));
        ObservableMap<Object, Object> observableMap = listView.getProperties();
        observableMap.remove(RECREATE);
        observableMap.addListener(this.propertiesMapListener);
        ((ListViewBehavior)this.getBehavior()).setOnFocusPreviousRow(() -> this.onFocusPreviousCell());
        ((ListViewBehavior)this.getBehavior()).setOnFocusNextRow(() -> this.onFocusNextCell());
        ((ListViewBehavior)this.getBehavior()).setOnMoveToFirstCell(() -> this.onMoveToFirstCell());
        ((ListViewBehavior)this.getBehavior()).setOnMoveToLastCell(() -> this.onMoveToLastCell());
        ((ListViewBehavior)this.getBehavior()).setOnScrollPageDown(bl -> this.onScrollPageDown((boolean)bl));
        ((ListViewBehavior)this.getBehavior()).setOnScrollPageUp(bl -> this.onScrollPageUp((boolean)bl));
        ((ListViewBehavior)this.getBehavior()).setOnSelectPreviousRow(() -> this.onSelectPreviousCell());
        ((ListViewBehavior)this.getBehavior()).setOnSelectNextRow(() -> this.onSelectNextCell());
        this.registerChangeListener(listView.itemsProperty(), "ITEMS");
        this.registerChangeListener(listView.orientationProperty(), "ORIENTATION");
        this.registerChangeListener(listView.cellFactoryProperty(), "CELL_FACTORY");
        this.registerChangeListener(listView.parentProperty(), "PARENT");
        this.registerChangeListener(listView.placeholderProperty(), "PLACEHOLDER");
        this.registerChangeListener(listView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ITEMS".equals(string)) {
            this.updateListViewItems();
        } else if ("ORIENTATION".equals(string)) {
            this.flow.setVertical(((ListView)this.getSkinnable()).getOrientation() == Orientation.VERTICAL);
        } else if ("CELL_FACTORY".equals(string)) {
            this.flow.recreateCells();
        } else if ("PARENT".equals(string)) {
            if (((ListView)this.getSkinnable()).getParent() != null && ((ListView)this.getSkinnable()).isVisible()) {
                ((ListView)this.getSkinnable()).requestLayout();
            }
        } else if ("PLACEHOLDER".equals(string)) {
            this.updatePlaceholderRegionVisibility();
        } else if ("FIXED_CELL_SIZE".equals(string)) {
            this.flow.setFixedCellSize(((ListView)this.getSkinnable()).getFixedCellSize());
        }
    }

    public void updateListViewItems() {
        if (this.listViewItems != null) {
            this.listViewItems.removeListener(this.weakListViewItemsListener);
        }
        this.listViewItems = ((ListView)this.getSkinnable()).getItems();
        if (this.listViewItems != null) {
            this.listViewItems.addListener(this.weakListViewItemsListener);
        }
        this.rowCountDirty = true;
        ((ListView)this.getSkinnable()).requestLayout();
    }

    @Override
    public int getItemCount() {
        return this.itemCount;
    }

    @Override
    protected void updateRowCount() {
        int n2;
        if (this.flow == null) {
            return;
        }
        int n3 = this.itemCount;
        this.itemCount = n2 = this.listViewItems == null ? 0 : this.listViewItems.size();
        this.flow.setCellCount(n2);
        this.updatePlaceholderRegionVisibility();
        if (n2 != n3) {
            this.needCellsRebuilt = true;
        } else {
            this.needCellsReconfigured = true;
        }
    }

    protected final void updatePlaceholderRegionVisibility() {
        boolean bl;
        boolean bl2 = bl = this.getItemCount() == 0;
        if (bl) {
            this.placeholderNode = ((ListView)this.getSkinnable()).getPlaceholder();
            if (this.placeholderNode == null && EMPTY_LIST_TEXT != null && !EMPTY_LIST_TEXT.isEmpty()) {
                this.placeholderNode = new Label();
                ((Label)this.placeholderNode).setText(EMPTY_LIST_TEXT);
            }
            if (this.placeholderNode != null) {
                if (this.placeholderRegion == null) {
                    this.placeholderRegion = new StackPane();
                    this.placeholderRegion.getStyleClass().setAll("placeholder");
                    this.getChildren().add(this.placeholderRegion);
                }
                this.placeholderRegion.getChildren().setAll(this.placeholderNode);
            }
        }
        this.flow.setVisible(!bl);
        if (this.placeholderRegion != null) {
            this.placeholderRegion.setVisible(bl);
        }
    }

    @Override
    public ListCell<T> createCell() {
        ListCell<T> listCell = ((ListView)this.getSkinnable()).getCellFactory() != null ? ((ListView)this.getSkinnable()).getCellFactory().call(this.getSkinnable()) : ListViewSkin.createDefaultCellImpl();
        listCell.updateListView((ListView)this.getSkinnable());
        return listCell;
    }

    private static <T> ListCell<T> createDefaultCellImpl() {
        return new ListCell<T>(){

            @Override
            public void updateItem(T t2, boolean bl) {
                super.updateItem(t2, bl);
                if (bl) {
                    this.setText(null);
                    this.setGraphic(null);
                } else if (t2 instanceof Node) {
                    this.setText(null);
                    Node node = this.getGraphic();
                    Node node2 = (Node)t2;
                    if (node == null || !node.equals(node2)) {
                        this.setGraphic(node2);
                    }
                } else {
                    this.setText(t2 == null ? "null" : t2.toString());
                    this.setGraphic(null);
                }
            }
        };
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        super.layoutChildren(d2, d3, d4, d5);
        if (this.needCellsRebuilt) {
            this.flow.rebuildCells();
        } else if (this.needCellsReconfigured) {
            this.flow.reconfigureCells();
        }
        this.needCellsRebuilt = false;
        this.needCellsReconfigured = false;
        if (this.getItemCount() == 0) {
            if (this.placeholderRegion != null) {
                this.placeholderRegion.setVisible(d4 > 0.0 && d5 > 0.0);
                this.placeholderRegion.resizeRelocate(d2, d3, d4, d5);
            }
        } else {
            this.flow.resizeRelocate(d2, d3, d4, d5);
        }
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        this.checkState();
        if (this.getItemCount() == 0) {
            if (this.placeholderRegion == null) {
                this.updatePlaceholderRegionVisibility();
            }
            if (this.placeholderRegion != null) {
                return this.placeholderRegion.prefWidth(d2) + d6 + d4;
            }
        }
        return this.computePrefHeight(-1.0, d3, d4, d5, d6) * 0.618033987;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        return 400.0;
    }

    private void onFocusPreviousCell() {
        FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        this.flow.show(focusModel.getFocusedIndex());
    }

    private void onFocusNextCell() {
        FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        this.flow.show(focusModel.getFocusedIndex());
    }

    private void onSelectPreviousCell() {
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        int n2 = multipleSelectionModel.getSelectedIndex();
        this.flow.show(n2);
        Object t2 = this.flow.getFirstVisibleCell();
        if (t2 == null || n2 < ((IndexedCell)t2).getIndex()) {
            this.flow.setPosition((double)n2 / (double)this.getItemCount());
        }
    }

    private void onSelectNextCell() {
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        int n2 = multipleSelectionModel.getSelectedIndex();
        this.flow.show(n2);
        ListCell listCell = (ListCell)this.flow.getLastVisibleCell();
        if (listCell == null || listCell.getIndex() < n2) {
            this.flow.setPosition((double)n2 / (double)this.getItemCount());
        }
    }

    private void onMoveToFirstCell() {
        this.flow.show(0);
        this.flow.setPosition(0.0);
    }

    private void onMoveToLastCell() {
        int n2 = this.getItemCount() - 1;
        this.flow.show(n2);
        this.flow.setPosition(1.0);
    }

    private int onScrollPageDown(boolean bl) {
        int n2;
        ListCell listCell = (ListCell)this.flow.getLastVisibleCellWithinViewPort();
        if (listCell == null) {
            return -1;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
        FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
        if (multipleSelectionModel == null || focusModel == null) {
            return -1;
        }
        int n3 = listCell.getIndex();
        boolean bl2 = false;
        if (bl) {
            bl2 = listCell.isFocused() || focusModel.isFocused(n3);
        } else {
            boolean bl3 = bl2 = listCell.isSelected() || multipleSelectionModel.isSelected(n3);
        }
        if (bl2) {
            int n4 = n2 = bl && focusModel.getFocusedIndex() == n3 || !bl && multipleSelectionModel.getSelectedIndex() == n3 ? 1 : 0;
            if (n2 != 0) {
                this.flow.showAsFirst(listCell);
                ListCell listCell2 = (ListCell)this.flow.getLastVisibleCellWithinViewPort();
                listCell = listCell2 == null ? listCell : listCell2;
            }
        }
        n2 = listCell.getIndex();
        this.flow.show(listCell);
        return n2;
    }

    private int onScrollPageUp(boolean bl) {
        int n2;
        ListCell listCell = (ListCell)this.flow.getFirstVisibleCellWithinViewPort();
        if (listCell == null) {
            return -1;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
        FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
        if (multipleSelectionModel == null || focusModel == null) {
            return -1;
        }
        int n3 = listCell.getIndex();
        boolean bl2 = false;
        if (bl) {
            bl2 = listCell.isFocused() || focusModel.isFocused(n3);
        } else {
            boolean bl3 = bl2 = listCell.isSelected() || multipleSelectionModel.isSelected(n3);
        }
        if (bl2) {
            int n4 = n2 = bl && focusModel.getFocusedIndex() == n3 || !bl && multipleSelectionModel.getSelectedIndex() == n3 ? 1 : 0;
            if (n2 != 0) {
                this.flow.showAsLast(listCell);
                ListCell listCell2 = (ListCell)this.flow.getFirstVisibleCellWithinViewPort();
                listCell = listCell2 == null ? listCell : listCell2;
            }
        }
        n2 = listCell.getIndex();
        this.flow.show(listCell);
        return n2;
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
                int n2 = focusModel.getFocusedIndex();
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
            case ITEM_COUNT: {
                return this.getItemCount();
            }
            case ITEM_AT_INDEX: {
                Integer n3 = (Integer)arrobject[0];
                if (n3 == null) {
                    return null;
                }
                if (0 <= n3 && n3 < this.getItemCount()) {
                    return this.flow.getPrivateCell(n3);
                }
                return null;
            }
            case SELECTED_ITEMS: {
                MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
                ObservableList<Integer> observableList = multipleSelectionModel.getSelectedIndices();
                ArrayList<ListCell> arrayList = new ArrayList<ListCell>(observableList.size());
                Iterator iterator = observableList.iterator();
                while (iterator.hasNext()) {
                    int n4 = (Integer)iterator.next();
                    ListCell listCell = (ListCell)this.flow.getPrivateCell(n4);
                    if (listCell == null) continue;
                    arrayList.add(listCell);
                }
                return FXCollections.observableArrayList(arrayList);
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

    @Override
    protected void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case SHOW_ITEM: {
                Node node = (Node)arrobject[0];
                if (!(node instanceof ListCell)) break;
                ListCell listCell = (ListCell)node;
                this.flow.show(listCell.getIndex());
                break;
            }
            case SET_SELECTED_ITEMS: {
                MultipleSelectionModel multipleSelectionModel;
                ObservableList observableList = (ObservableList)arrobject[0];
                if (observableList == null || (multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel()) == null) break;
                multipleSelectionModel.clearSelection();
                for (Node node : observableList) {
                    if (!(node instanceof ListCell)) continue;
                    ListCell listCell = (ListCell)node;
                    multipleSelectionModel.select(listCell.getIndex());
                }
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }
}

