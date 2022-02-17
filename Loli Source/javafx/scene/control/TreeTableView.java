/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.SelectedCellsMap;
import com.sun.javafx.scene.control.TableColumnComparatorBase;
import com.sun.javafx.scene.control.behavior.TableCellBehaviorBase;
import com.sun.javafx.scene.control.behavior.TreeTableCellBehavior;
import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ControlUtils;
import javafx.scene.control.MultipleSelectionModelBase;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Skin;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableUtil;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeSortMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeUtil;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

@DefaultProperty(value="root")
public class TreeTableView<S>
extends Control {
    private static final EventType<?> EDIT_ANY_EVENT = new EventType<Event>(Event.ANY, "TREE_TABLE_VIEW_EDIT");
    private static final EventType<?> EDIT_START_EVENT = new EventType(TreeTableView.editAnyEvent(), "EDIT_START");
    private static final EventType<?> EDIT_CANCEL_EVENT = new EventType(TreeTableView.editAnyEvent(), "EDIT_CANCEL");
    private static final EventType<?> EDIT_COMMIT_EVENT = new EventType(TreeTableView.editAnyEvent(), "EDIT_COMMIT");
    public static final Callback<ResizeFeatures, Boolean> UNCONSTRAINED_RESIZE_POLICY = new Callback<ResizeFeatures, Boolean>(){

        public String toString() {
            return "unconstrained-resize";
        }

        @Override
        public Boolean call(ResizeFeatures resizeFeatures) {
            double d2 = TableUtil.resize(resizeFeatures.getColumn(), resizeFeatures.getDelta());
            return Double.compare(d2, 0.0) == 0;
        }
    };
    public static final Callback<ResizeFeatures, Boolean> CONSTRAINED_RESIZE_POLICY = new Callback<ResizeFeatures, Boolean>(){
        private boolean isFirstRun = true;

        public String toString() {
            return "constrained-resize";
        }

        @Override
        public Boolean call(ResizeFeatures resizeFeatures) {
            TreeTableView treeTableView = resizeFeatures.getTable();
            ObservableList observableList = treeTableView.getVisibleLeafColumns();
            Boolean bl = TableUtil.constrainedResize(resizeFeatures, this.isFirstRun, treeTableView.contentWidth, observableList);
            this.isFirstRun = !this.isFirstRun ? false : bl == false;
            return bl;
        }
    };
    public static final Callback<TreeTableView, Boolean> DEFAULT_SORT_POLICY = new Callback<TreeTableView, Boolean>(){

        @Override
        public Boolean call(TreeTableView treeTableView) {
            try {
                TreeItem treeItem = treeTableView.getRoot();
                if (treeItem == null) {
                    return false;
                }
                TreeSortMode treeSortMode = treeTableView.getSortMode();
                if (treeSortMode == null) {
                    return false;
                }
                treeItem.lastSortMode = treeSortMode;
                treeItem.lastComparator = treeTableView.getComparator();
                treeItem.sort();
                return true;
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                return false;
            }
        }
    };
    private boolean expandedItemCountDirty = true;
    private Map<Integer, SoftReference<TreeItem<S>>> treeItemCacheMap = new HashMap<Integer, SoftReference<TreeItem<S>>>();
    private final ObservableList<TreeTableColumn<S, ?>> columns = FXCollections.observableArrayList();
    private final ObservableList<TreeTableColumn<S, ?>> visibleLeafColumns = FXCollections.observableArrayList();
    private final ObservableList<TreeTableColumn<S, ?>> unmodifiableVisibleLeafColumns = FXCollections.unmodifiableObservableList(this.visibleLeafColumns);
    private ObservableList<TreeTableColumn<S, ?>> sortOrder = FXCollections.observableArrayList();
    double contentWidth;
    private boolean isInited = false;
    private final EventHandler<TreeItem.TreeModificationEvent<S>> rootEvent = treeModificationEvent -> {
        boolean bl = false;
        for (EventType<? extends Event> eventType = treeModificationEvent.getEventType(); eventType != null; eventType = eventType.getSuperType()) {
            if (!eventType.equals(TreeItem.expandedItemCountChangeEvent())) continue;
            bl = true;
            break;
        }
        if (bl) {
            this.expandedItemCountDirty = true;
            this.requestLayout();
        }
    };
    private final ListChangeListener<TreeTableColumn<S, ?>> columnsObserver = new ListChangeListener<TreeTableColumn<S, ?>>(){

        @Override
        public void onChanged(ListChangeListener.Change<? extends TreeTableColumn<S, ?>> change) {
            Object object2;
            TreeTableColumn treeTableColumn22;
            ArrayList<Object> arrayList;
            ObservableList observableList = TreeTableView.this.getColumns();
            while (change.next()) {
                if (!change.wasAdded()) continue;
                arrayList = new ArrayList<Object>();
                for (TreeTableColumn treeTableColumn22 : change.getAddedSubList()) {
                    if (treeTableColumn22 == null) continue;
                    int n2 = 0;
                    for (TreeTableColumn treeTableColumn : observableList) {
                        if (treeTableColumn22 != treeTableColumn) continue;
                        ++n2;
                    }
                    if (n2 <= true) continue;
                    arrayList.add(treeTableColumn22);
                }
                if (arrayList.isEmpty()) continue;
                object2 = "";
                for (TreeTableColumn treeTableColumn4 : arrayList) {
                    object2 = object2 + "'" + treeTableColumn4.getText() + "', ";
                }
                throw new IllegalStateException("Duplicate TreeTableColumns detected in TreeTableView columns list with titles " + object2);
            }
            change.reset();
            TreeTableView.this.updateVisibleLeafColumns();
            arrayList = new ArrayList();
            while (change.next()) {
                object2 = change.getRemoved();
                treeTableColumn22 = change.getAddedSubList();
                if (change.wasRemoved()) {
                    arrayList.addAll((Collection<Object>)object2);
                    Iterator iterator = object2.iterator();
                    while (iterator.hasNext()) {
                        TreeTableColumn treeTableColumn = (TreeTableColumn)iterator.next();
                        treeTableColumn.setTreeTableView(null);
                    }
                }
                if (change.wasAdded()) {
                    arrayList.removeAll((Collection<?>)((Object)treeTableColumn22));
                    Iterator list = treeTableColumn22.iterator();
                    while (list.hasNext()) {
                        TreeTableColumn treeTableColumn = (TreeTableColumn)list.next();
                        treeTableColumn.setTreeTableView(TreeTableView.this);
                    }
                }
                TableUtil.removeColumnsListener(object2, TreeTableView.this.weakColumnsObserver);
                TableUtil.addColumnsListener((List<? extends TableColumnBase>)((Object)treeTableColumn22), TreeTableView.this.weakColumnsObserver);
                TableUtil.removeTableColumnListener(change.getRemoved(), TreeTableView.this.weakColumnVisibleObserver, TreeTableView.this.weakColumnSortableObserver, TreeTableView.this.weakColumnSortTypeObserver, TreeTableView.this.weakColumnComparatorObserver);
                TableUtil.addTableColumnListener(change.getAddedSubList(), TreeTableView.this.weakColumnVisibleObserver, TreeTableView.this.weakColumnSortableObserver, TreeTableView.this.weakColumnSortTypeObserver, TreeTableView.this.weakColumnComparatorObserver);
            }
            TreeTableView.this.sortOrder.removeAll((Collection<?>)arrayList);
            object2 = TreeTableView.this.getFocusModel();
            treeTableColumn22 = TreeTableView.this.getSelectionModel();
            change.reset();
            while (change.next()) {
                if (!change.wasRemoved()) continue;
                List list = change.getRemoved();
                if (object2 != null) {
                    boolean bl;
                    TreeTablePosition treeTablePosition = ((TreeTableViewFocusModel)object2).getFocusedCell();
                    boolean bl2 = false;
                    for (TreeTableColumn treeTableColumn : list) {
                        bl = treeTablePosition != null && treeTablePosition.getTableColumn() == treeTableColumn;
                        if (!bl) continue;
                        break;
                    }
                    if (bl) {
                        int n2 = TreeTableView.this.lastKnownColumnIndex.getOrDefault(treeTablePosition.getTableColumn(), 0);
                        int n3 = n2 == 0 ? 0 : Math.min(TreeTableView.this.getVisibleLeafColumns().size() - 1, n2 - 1);
                        ((TreeTableViewFocusModel)object2).focus(treeTablePosition.getRow(), TreeTableView.this.getVisibleLeafColumn(n3));
                    }
                }
                if (treeTableColumn22 == null) continue;
                ArrayList arrayList2 = new ArrayList(((TreeTableViewSelectionModel)((Object)treeTableColumn22)).getSelectedCells());
                for (Object object : arrayList2) {
                    int n4;
                    boolean bl = false;
                    for (TreeTableColumn treeTableColumn : list) {
                        bl = object != null && ((TreeTablePosition)object).getTableColumn() == treeTableColumn;
                        if (!bl) continue;
                        break;
                    }
                    if (!bl || (n4 = TreeTableView.this.lastKnownColumnIndex.getOrDefault(((TreeTablePosition)object).getTableColumn(), -1).intValue()) == -1) continue;
                    if (treeTableColumn22 instanceof TreeTableViewArrayListSelectionModel) {
                        TreeTablePosition treeTablePosition = new TreeTablePosition(TreeTableView.this, ((TablePositionBase)object).getRow(), ((TreeTablePosition)object).getTableColumn());
                        treeTablePosition.fixedColumnIndex = n4;
                        ((TreeTableViewArrayListSelectionModel)((Object)treeTableColumn22)).clearSelection(treeTablePosition);
                        continue;
                    }
                    ((TableSelectionModel)((Object)treeTableColumn22)).clearSelection(((TablePositionBase)object).getRow(), ((TreeTablePosition)object).getTableColumn());
                }
            }
            TreeTableView.this.lastKnownColumnIndex.clear();
            for (TreeTableColumn treeTableColumn : TreeTableView.this.getColumns()) {
                int n5 = TreeTableView.this.getVisibleLeafIndex(treeTableColumn);
                if (n5 <= -1) continue;
                TreeTableView.this.lastKnownColumnIndex.put(treeTableColumn, n5);
            }
        }
    };
    private final WeakHashMap<TreeTableColumn<S, ?>, Integer> lastKnownColumnIndex = new WeakHashMap();
    private final InvalidationListener columnVisibleObserver = observable -> this.updateVisibleLeafColumns();
    private final InvalidationListener columnSortableObserver = observable -> {
        TreeTableColumn treeTableColumn = (TreeTableColumn)((BooleanProperty)observable).getBean();
        if (!this.getSortOrder().contains(treeTableColumn)) {
            return;
        }
        this.doSort(TableUtil.SortEventType.COLUMN_SORTABLE_CHANGE, treeTableColumn);
    };
    private final InvalidationListener columnSortTypeObserver = observable -> {
        TreeTableColumn treeTableColumn = (TreeTableColumn)((ObjectProperty)observable).getBean();
        if (!this.getSortOrder().contains(treeTableColumn)) {
            return;
        }
        this.doSort(TableUtil.SortEventType.COLUMN_SORT_TYPE_CHANGE, treeTableColumn);
    };
    private final InvalidationListener columnComparatorObserver = observable -> {
        TreeTableColumn treeTableColumn = (TreeTableColumn)((SimpleObjectProperty)observable).getBean();
        if (!this.getSortOrder().contains(treeTableColumn)) {
            return;
        }
        this.doSort(TableUtil.SortEventType.COLUMN_COMPARATOR_CHANGE, treeTableColumn);
    };
    private final InvalidationListener cellSelectionModelInvalidationListener = observable -> {
        boolean bl = ((BooleanProperty)observable).get();
        this.pseudoClassStateChanged(PSEUDO_CLASS_CELL_SELECTION, bl);
        this.pseudoClassStateChanged(PSEUDO_CLASS_ROW_SELECTION, !bl);
    };
    private WeakEventHandler<TreeItem.TreeModificationEvent<S>> weakRootEventListener;
    private final WeakInvalidationListener weakColumnVisibleObserver = new WeakInvalidationListener(this.columnVisibleObserver);
    private final WeakInvalidationListener weakColumnSortableObserver = new WeakInvalidationListener(this.columnSortableObserver);
    private final WeakInvalidationListener weakColumnSortTypeObserver = new WeakInvalidationListener(this.columnSortTypeObserver);
    private final WeakInvalidationListener weakColumnComparatorObserver = new WeakInvalidationListener(this.columnComparatorObserver);
    private final WeakListChangeListener<TreeTableColumn<S, ?>> weakColumnsObserver = new WeakListChangeListener(this.columnsObserver);
    private final WeakInvalidationListener weakCellSelectionModelInvalidationListener = new WeakInvalidationListener(this.cellSelectionModelInvalidationListener);
    private ObjectProperty<TreeItem<S>> root = new SimpleObjectProperty<TreeItem<S>>(this, "root"){
        private WeakReference<TreeItem<S>> weakOldItem;

        @Override
        protected void invalidated() {
            TreeItem treeItem;
            TreeItem treeItem2;
            TreeItem treeItem3 = treeItem2 = this.weakOldItem == null ? null : (TreeItem)this.weakOldItem.get();
            if (treeItem2 != null && TreeTableView.this.weakRootEventListener != null) {
                treeItem2.removeEventHandler(TreeItem.treeNotificationEvent(), TreeTableView.this.weakRootEventListener);
            }
            if ((treeItem = TreeTableView.this.getRoot()) != null) {
                TreeTableView.this.weakRootEventListener = new WeakEventHandler(TreeTableView.this.rootEvent);
                TreeTableView.this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), TreeTableView.this.weakRootEventListener);
                this.weakOldItem = new WeakReference(treeItem);
            }
            TreeTableView.this.getSortOrder().clear();
            TreeTableView.this.expandedItemCountDirty = true;
            TreeTableView.this.updateRootExpanded();
        }
    };
    private BooleanProperty showRoot;
    private ObjectProperty<TreeTableColumn<S, ?>> treeColumn;
    private ObjectProperty<TreeTableViewSelectionModel<S>> selectionModel;
    private ObjectProperty<TreeTableViewFocusModel<S>> focusModel;
    private ReadOnlyIntegerWrapper expandedItemCount = new ReadOnlyIntegerWrapper(this, "expandedItemCount", 0);
    private BooleanProperty editable;
    private ReadOnlyObjectWrapper<TreeTablePosition<S, ?>> editingCell;
    private BooleanProperty tableMenuButtonVisible;
    private ObjectProperty<Callback<ResizeFeatures, Boolean>> columnResizePolicy;
    private ObjectProperty<Callback<TreeTableView<S>, TreeTableRow<S>>> rowFactory;
    private ObjectProperty<Node> placeholder;
    private DoubleProperty fixedCellSize;
    private ObjectProperty<TreeSortMode> sortMode;
    private ReadOnlyObjectWrapper<Comparator<TreeItem<S>>> comparator;
    private ObjectProperty<Callback<TreeTableView<S>, Boolean>> sortPolicy;
    private ObjectProperty<EventHandler<SortEvent<TreeTableView<S>>>> onSort;
    private ObjectProperty<EventHandler<ScrollToEvent<Integer>>> onScrollTo;
    private ObjectProperty<EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>>> onScrollToColumn;
    private boolean sortLock = false;
    private TableUtil.SortEventType lastSortEventType = null;
    private Object[] lastSortEventSupportInfo = null;
    private static final String DEFAULT_STYLE_CLASS = "tree-table-view";
    private static final PseudoClass PSEUDO_CLASS_CELL_SELECTION = PseudoClass.getPseudoClass("cell-selection");
    private static final PseudoClass PSEUDO_CLASS_ROW_SELECTION = PseudoClass.getPseudoClass("row-selection");

    public TreeTableView() {
        this(null);
    }

    public TreeTableView(TreeItem<S> treeItem) {
        this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.setAccessibleRole(AccessibleRole.TREE_TABLE_VIEW);
        this.setRoot(treeItem);
        this.updateExpandedItemCount(treeItem);
        this.setSelectionModel(new TreeTableViewArrayListSelectionModel(this));
        this.setFocusModel(new TreeTableViewFocusModel(this));
        this.getColumns().addListener(this.weakColumnsObserver);
        this.getSortOrder().addListener(change -> this.doSort(TableUtil.SortEventType.SORT_ORDER_CHANGE, change));
        this.getProperties().addListener(change -> {
            if (change.wasAdded() && "TableView.contentWidth".equals(change.getKey())) {
                if (change.getValueAdded() instanceof Number) {
                    this.setContentWidth((Double)change.getValueAdded());
                }
                this.getProperties().remove("TableView.contentWidth");
            }
        });
        this.isInited = true;
    }

    public static <S> EventType<EditEvent<S>> editAnyEvent() {
        return EDIT_ANY_EVENT;
    }

    public static <S> EventType<EditEvent<S>> editStartEvent() {
        return EDIT_START_EVENT;
    }

    public static <S> EventType<EditEvent<S>> editCancelEvent() {
        return EDIT_CANCEL_EVENT;
    }

    public static <S> EventType<EditEvent<S>> editCommitEvent() {
        return EDIT_COMMIT_EVENT;
    }

    @Deprecated
    public static int getNodeLevel(TreeItem<?> treeItem) {
        return TreeView.getNodeLevel(treeItem);
    }

    public final void setRoot(TreeItem<S> treeItem) {
        this.rootProperty().set(treeItem);
    }

    public final TreeItem<S> getRoot() {
        return this.root == null ? null : (TreeItem)this.root.get();
    }

    public final ObjectProperty<TreeItem<S>> rootProperty() {
        return this.root;
    }

    public final void setShowRoot(boolean bl) {
        this.showRootProperty().set(bl);
    }

    public final boolean isShowRoot() {
        return this.showRoot == null ? true : this.showRoot.get();
    }

    public final BooleanProperty showRootProperty() {
        if (this.showRoot == null) {
            this.showRoot = new SimpleBooleanProperty(this, "showRoot", true){

                @Override
                protected void invalidated() {
                    TreeTableView.this.updateRootExpanded();
                    TreeTableView.this.updateExpandedItemCount(TreeTableView.this.getRoot());
                }
            };
        }
        return this.showRoot;
    }

    public final ObjectProperty<TreeTableColumn<S, ?>> treeColumnProperty() {
        if (this.treeColumn == null) {
            this.treeColumn = new SimpleObjectProperty<Object>(this, "treeColumn", null);
        }
        return this.treeColumn;
    }

    public final void setTreeColumn(TreeTableColumn<S, ?> treeTableColumn) {
        this.treeColumnProperty().set(treeTableColumn);
    }

    public final TreeTableColumn<S, ?> getTreeColumn() {
        return this.treeColumn == null ? null : (TreeTableColumn)this.treeColumn.get();
    }

    public final void setSelectionModel(TreeTableViewSelectionModel<S> treeTableViewSelectionModel) {
        this.selectionModelProperty().set(treeTableViewSelectionModel);
    }

    public final TreeTableViewSelectionModel<S> getSelectionModel() {
        return this.selectionModel == null ? null : (TreeTableViewSelectionModel)this.selectionModel.get();
    }

    public final ObjectProperty<TreeTableViewSelectionModel<S>> selectionModelProperty() {
        if (this.selectionModel == null) {
            this.selectionModel = new SimpleObjectProperty<TreeTableViewSelectionModel<S>>(this, "selectionModel"){
                TreeTableViewSelectionModel<S> oldValue;
                {
                    this.oldValue = null;
                }

                @Override
                protected void invalidated() {
                    if (this.oldValue != null) {
                        this.oldValue.cellSelectionEnabledProperty().removeListener(TreeTableView.this.weakCellSelectionModelInvalidationListener);
                    }
                    this.oldValue = (TreeTableViewSelectionModel)this.get();
                    if (this.oldValue != null) {
                        this.oldValue.cellSelectionEnabledProperty().addListener(TreeTableView.this.weakCellSelectionModelInvalidationListener);
                        TreeTableView.this.weakCellSelectionModelInvalidationListener.invalidated(this.oldValue.cellSelectionEnabledProperty());
                    }
                }
            };
        }
        return this.selectionModel;
    }

    public final void setFocusModel(TreeTableViewFocusModel<S> treeTableViewFocusModel) {
        this.focusModelProperty().set(treeTableViewFocusModel);
    }

    public final TreeTableViewFocusModel<S> getFocusModel() {
        return this.focusModel == null ? null : (TreeTableViewFocusModel)this.focusModel.get();
    }

    public final ObjectProperty<TreeTableViewFocusModel<S>> focusModelProperty() {
        if (this.focusModel == null) {
            this.focusModel = new SimpleObjectProperty<TreeTableViewFocusModel<S>>(this, "focusModel");
        }
        return this.focusModel;
    }

    public final ReadOnlyIntegerProperty expandedItemCountProperty() {
        return this.expandedItemCount.getReadOnlyProperty();
    }

    private void setExpandedItemCount(int n2) {
        this.expandedItemCount.set(n2);
    }

    public final int getExpandedItemCount() {
        if (this.expandedItemCountDirty) {
            this.updateExpandedItemCount(this.getRoot());
        }
        return this.expandedItemCount.get();
    }

    public final void setEditable(boolean bl) {
        this.editableProperty().set(bl);
    }

    public final boolean isEditable() {
        return this.editable == null ? false : this.editable.get();
    }

    public final BooleanProperty editableProperty() {
        if (this.editable == null) {
            this.editable = new SimpleBooleanProperty(this, "editable", false);
        }
        return this.editable;
    }

    private void setEditingCell(TreeTablePosition<S, ?> treeTablePosition) {
        this.editingCellPropertyImpl().set(treeTablePosition);
    }

    public final TreeTablePosition<S, ?> getEditingCell() {
        return this.editingCell == null ? null : (TreeTablePosition)this.editingCell.get();
    }

    public final ReadOnlyObjectProperty<TreeTablePosition<S, ?>> editingCellProperty() {
        return this.editingCellPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TreeTablePosition<S, ?>> editingCellPropertyImpl() {
        if (this.editingCell == null) {
            this.editingCell = new ReadOnlyObjectWrapper(this, "editingCell");
        }
        return this.editingCell;
    }

    public final BooleanProperty tableMenuButtonVisibleProperty() {
        if (this.tableMenuButtonVisible == null) {
            this.tableMenuButtonVisible = new SimpleBooleanProperty(this, "tableMenuButtonVisible");
        }
        return this.tableMenuButtonVisible;
    }

    public final void setTableMenuButtonVisible(boolean bl) {
        this.tableMenuButtonVisibleProperty().set(bl);
    }

    public final boolean isTableMenuButtonVisible() {
        return this.tableMenuButtonVisible == null ? false : this.tableMenuButtonVisible.get();
    }

    public final void setColumnResizePolicy(Callback<ResizeFeatures, Boolean> callback) {
        this.columnResizePolicyProperty().set(callback);
    }

    public final Callback<ResizeFeatures, Boolean> getColumnResizePolicy() {
        return this.columnResizePolicy == null ? UNCONSTRAINED_RESIZE_POLICY : (Callback)this.columnResizePolicy.get();
    }

    public final ObjectProperty<Callback<ResizeFeatures, Boolean>> columnResizePolicyProperty() {
        if (this.columnResizePolicy == null) {
            this.columnResizePolicy = new SimpleObjectProperty<Callback<ResizeFeatures, Boolean>>((Object)this, "columnResizePolicy", UNCONSTRAINED_RESIZE_POLICY){
                private Callback<ResizeFeatures, Boolean> oldPolicy;

                @Override
                protected void invalidated() {
                    if (TreeTableView.this.isInited) {
                        PseudoClass pseudoClass;
                        ((Callback)this.get()).call(new ResizeFeatures(TreeTableView.this, null, 0.0));
                        if (this.oldPolicy != null) {
                            pseudoClass = PseudoClass.getPseudoClass(this.oldPolicy.toString());
                            TreeTableView.this.pseudoClassStateChanged(pseudoClass, false);
                        }
                        if (this.get() != null) {
                            pseudoClass = PseudoClass.getPseudoClass(((Callback)this.get()).toString());
                            TreeTableView.this.pseudoClassStateChanged(pseudoClass, true);
                        }
                        this.oldPolicy = (Callback)this.get();
                    }
                }
            };
        }
        return this.columnResizePolicy;
    }

    public final ObjectProperty<Callback<TreeTableView<S>, TreeTableRow<S>>> rowFactoryProperty() {
        if (this.rowFactory == null) {
            this.rowFactory = new SimpleObjectProperty<Callback<TreeTableView<S>, TreeTableRow<S>>>(this, "rowFactory");
        }
        return this.rowFactory;
    }

    public final void setRowFactory(Callback<TreeTableView<S>, TreeTableRow<S>> callback) {
        this.rowFactoryProperty().set(callback);
    }

    public final Callback<TreeTableView<S>, TreeTableRow<S>> getRowFactory() {
        return this.rowFactory == null ? null : (Callback)this.rowFactory.get();
    }

    public final ObjectProperty<Node> placeholderProperty() {
        if (this.placeholder == null) {
            this.placeholder = new SimpleObjectProperty<Node>(this, "placeholder");
        }
        return this.placeholder;
    }

    public final void setPlaceholder(Node node) {
        this.placeholderProperty().set(node);
    }

    public final Node getPlaceholder() {
        return this.placeholder == null ? null : (Node)this.placeholder.get();
    }

    public final void setFixedCellSize(double d2) {
        this.fixedCellSizeProperty().set(d2);
    }

    public final double getFixedCellSize() {
        return this.fixedCellSize == null ? -1.0 : this.fixedCellSize.get();
    }

    public final DoubleProperty fixedCellSizeProperty() {
        if (this.fixedCellSize == null) {
            this.fixedCellSize = new StyleableDoubleProperty(-1.0){

                @Override
                public CssMetaData<TreeTableView<?>, Number> getCssMetaData() {
                    return StyleableProperties.FIXED_CELL_SIZE;
                }

                @Override
                public Object getBean() {
                    return TreeTableView.this;
                }

                @Override
                public String getName() {
                    return "fixedCellSize";
                }
            };
        }
        return this.fixedCellSize;
    }

    public final ObjectProperty<TreeSortMode> sortModeProperty() {
        if (this.sortMode == null) {
            this.sortMode = new SimpleObjectProperty<TreeSortMode>(this, "sortMode", TreeSortMode.ALL_DESCENDANTS);
        }
        return this.sortMode;
    }

    public final void setSortMode(TreeSortMode treeSortMode) {
        this.sortModeProperty().set(treeSortMode);
    }

    public final TreeSortMode getSortMode() {
        return this.sortMode == null ? TreeSortMode.ALL_DESCENDANTS : (TreeSortMode)((Object)this.sortMode.get());
    }

    private void setComparator(Comparator<TreeItem<S>> comparator) {
        this.comparatorPropertyImpl().set(comparator);
    }

    public final Comparator<TreeItem<S>> getComparator() {
        return this.comparator == null ? null : (Comparator)this.comparator.get();
    }

    public final ReadOnlyObjectProperty<Comparator<TreeItem<S>>> comparatorProperty() {
        return this.comparatorPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Comparator<TreeItem<S>>> comparatorPropertyImpl() {
        if (this.comparator == null) {
            this.comparator = new ReadOnlyObjectWrapper(this, "comparator");
        }
        return this.comparator;
    }

    public final void setSortPolicy(Callback<TreeTableView<S>, Boolean> callback) {
        this.sortPolicyProperty().set(callback);
    }

    public final Callback<TreeTableView<S>, Boolean> getSortPolicy() {
        return this.sortPolicy == null ? DEFAULT_SORT_POLICY : (Callback)this.sortPolicy.get();
    }

    public final ObjectProperty<Callback<TreeTableView<S>, Boolean>> sortPolicyProperty() {
        if (this.sortPolicy == null) {
            this.sortPolicy = new SimpleObjectProperty<Callback<TreeTableView<S>, Boolean>>(this, "sortPolicy", DEFAULT_SORT_POLICY){

                @Override
                protected void invalidated() {
                    TreeTableView.this.sort();
                }
            };
        }
        return this.sortPolicy;
    }

    public void setOnSort(EventHandler<SortEvent<TreeTableView<S>>> eventHandler) {
        this.onSortProperty().set(eventHandler);
    }

    public EventHandler<SortEvent<TreeTableView<S>>> getOnSort() {
        if (this.onSort != null) {
            return (EventHandler)this.onSort.get();
        }
        return null;
    }

    public ObjectProperty<EventHandler<SortEvent<TreeTableView<S>>>> onSortProperty() {
        if (this.onSort == null) {
            this.onSort = new ObjectPropertyBase<EventHandler<SortEvent<TreeTableView<S>>>>(){

                @Override
                protected void invalidated() {
                    EventType eventType = SortEvent.sortEvent();
                    EventHandler eventHandler = (EventHandler)this.get();
                    TreeTableView.this.setEventHandler(eventType, eventHandler);
                }

                @Override
                public Object getBean() {
                    return TreeTableView.this;
                }

                @Override
                public String getName() {
                    return "onSort";
                }
            };
        }
        return this.onSort;
    }

    @Override
    protected void layoutChildren() {
        if (this.expandedItemCountDirty) {
            this.updateExpandedItemCount(this.getRoot());
        }
        super.layoutChildren();
    }

    public void scrollTo(int n2) {
        ControlUtils.scrollToIndex(this, n2);
    }

    public void setOnScrollTo(EventHandler<ScrollToEvent<Integer>> eventHandler) {
        this.onScrollToProperty().set(eventHandler);
    }

    public EventHandler<ScrollToEvent<Integer>> getOnScrollTo() {
        if (this.onScrollTo != null) {
            return (EventHandler)this.onScrollTo.get();
        }
        return null;
    }

    public ObjectProperty<EventHandler<ScrollToEvent<Integer>>> onScrollToProperty() {
        if (this.onScrollTo == null) {
            this.onScrollTo = new ObjectPropertyBase<EventHandler<ScrollToEvent<Integer>>>(){

                @Override
                protected void invalidated() {
                    TreeTableView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return TreeTableView.this;
                }

                @Override
                public String getName() {
                    return "onScrollTo";
                }
            };
        }
        return this.onScrollTo;
    }

    public void scrollToColumn(TreeTableColumn<S, ?> treeTableColumn) {
        ControlUtils.scrollToColumn(this, treeTableColumn);
    }

    public void scrollToColumnIndex(int n2) {
        if (this.getColumns() != null) {
            ControlUtils.scrollToColumn(this, (TableColumnBase)this.getColumns().get(n2));
        }
    }

    public void setOnScrollToColumn(EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>> eventHandler) {
        this.onScrollToColumnProperty().set(eventHandler);
    }

    public EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>> getOnScrollToColumn() {
        if (this.onScrollToColumn != null) {
            return (EventHandler)this.onScrollToColumn.get();
        }
        return null;
    }

    public ObjectProperty<EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>>> onScrollToColumnProperty() {
        if (this.onScrollToColumn == null) {
            this.onScrollToColumn = new ObjectPropertyBase<EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>>>(){

                @Override
                protected void invalidated() {
                    EventType eventType = ScrollToEvent.scrollToColumn();
                    TreeTableView.this.setEventHandler(eventType, (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return TreeTableView.this;
                }

                @Override
                public String getName() {
                    return "onScrollToColumn";
                }
            };
        }
        return this.onScrollToColumn;
    }

    public int getRow(TreeItem<S> treeItem) {
        return TreeUtil.getRow(treeItem, this.getRoot(), this.expandedItemCountDirty, this.isShowRoot());
    }

    public TreeItem<S> getTreeItem(int n2) {
        Object object;
        TreeItem treeItem;
        int n3;
        if (n2 < 0) {
            return null;
        }
        int n4 = n3 = this.isShowRoot() ? n2 : n2 + 1;
        if (this.expandedItemCountDirty) {
            this.updateExpandedItemCount(this.getRoot());
        } else if (this.treeItemCacheMap.containsKey(n3) && (treeItem = (TreeItem)((SoftReference)(object = this.treeItemCacheMap.get(n3))).get()) != null) {
            return treeItem;
        }
        object = TreeUtil.getItem(this.getRoot(), n3, this.expandedItemCountDirty);
        this.treeItemCacheMap.put(n3, new SoftReference<Object>(object));
        return object;
    }

    public int getTreeItemLevel(TreeItem<?> treeItem) {
        TreeItem<S> treeItem2 = this.getRoot();
        if (treeItem == null) {
            return -1;
        }
        if (treeItem == treeItem2) {
            return 0;
        }
        int n2 = 0;
        for (TreeItem<?> treeItem3 = treeItem.getParent(); treeItem3 != null; treeItem3 = treeItem3.getParent()) {
            ++n2;
            if (treeItem3 == treeItem2) break;
        }
        return n2;
    }

    public final ObservableList<TreeTableColumn<S, ?>> getColumns() {
        return this.columns;
    }

    public final ObservableList<TreeTableColumn<S, ?>> getSortOrder() {
        return this.sortOrder;
    }

    public boolean resizeColumn(TreeTableColumn<S, ?> treeTableColumn, double d2) {
        if (treeTableColumn == null || Double.compare(d2, 0.0) == 0) {
            return false;
        }
        boolean bl = this.getColumnResizePolicy().call(new ResizeFeatures<S>(this, treeTableColumn, d2));
        return bl;
    }

    public void edit(int n2, TreeTableColumn<S, ?> treeTableColumn) {
        if (!this.isEditable() || treeTableColumn != null && !treeTableColumn.isEditable()) {
            return;
        }
        if (n2 < 0 && treeTableColumn == null) {
            this.setEditingCell(null);
        } else {
            this.setEditingCell(new TreeTablePosition(this, n2, treeTableColumn));
        }
    }

    public ObservableList<TreeTableColumn<S, ?>> getVisibleLeafColumns() {
        return this.unmodifiableVisibleLeafColumns;
    }

    public int getVisibleLeafIndex(TreeTableColumn<S, ?> treeTableColumn) {
        return this.getVisibleLeafColumns().indexOf(treeTableColumn);
    }

    public TreeTableColumn<S, ?> getVisibleLeafColumn(int n2) {
        if (n2 < 0 || n2 >= this.visibleLeafColumns.size()) {
            return null;
        }
        return (TreeTableColumn)this.visibleLeafColumns.get(n2);
    }

    public void sort() {
        ObservableList observableList = this.getSortOrder();
        Comparator<TreeItem<S>> comparator = this.getComparator();
        this.setComparator(observableList.isEmpty() ? null : new TableColumnComparatorBase.TreeTableColumnComparator(observableList));
        SortEvent<TreeTableView> sortEvent = new SortEvent<TreeTableView>(this, this);
        this.fireEvent(sortEvent);
        if (sortEvent.isConsumed()) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.getSelectionModel().getSelectedCells());
        int n2 = arrayList.size();
        this.getSelectionModel().startAtomic();
        Callback<TreeTableView<S>, Boolean> callback = this.getSortPolicy();
        if (callback == null) {
            return;
        }
        Boolean bl = callback.call(this);
        this.getSelectionModel().stopAtomic();
        if (bl == null || !bl.booleanValue()) {
            this.sortLock = true;
            TableUtil.handleSortFailure(observableList, this.lastSortEventType, this.lastSortEventSupportInfo);
            this.setComparator(comparator);
            this.sortLock = false;
        } else if (this.getSelectionModel() instanceof TreeTableViewArrayListSelectionModel) {
            TreeTableViewArrayListSelectionModel treeTableViewArrayListSelectionModel = (TreeTableViewArrayListSelectionModel)this.getSelectionModel();
            ObservableList observableList2 = treeTableViewArrayListSelectionModel.getSelectedCells();
            ArrayList<TreeTablePosition> arrayList2 = new ArrayList<TreeTablePosition>();
            for (int i2 = 0; i2 < n2; ++i2) {
                TreeTablePosition treeTablePosition = (TreeTablePosition)arrayList.get(i2);
                if (observableList2.contains(treeTablePosition)) continue;
                arrayList2.add(treeTablePosition);
            }
            if (!arrayList2.isEmpty()) {
                NonIterableChange.GenericAddRemoveChange genericAddRemoveChange = new NonIterableChange.GenericAddRemoveChange(0, n2, arrayList2, observableList2);
                treeTableViewArrayListSelectionModel.handleSelectedCellsListChangeEvent(genericAddRemoveChange);
            }
        }
    }

    public void refresh() {
        this.getProperties().put("tableRecreateKey", Boolean.TRUE);
    }

    private void doSort(TableUtil.SortEventType sortEventType, Object ... arrobject) {
        if (this.sortLock) {
            return;
        }
        this.lastSortEventType = sortEventType;
        this.lastSortEventSupportInfo = arrobject;
        this.sort();
        this.lastSortEventType = null;
        this.lastSortEventSupportInfo = null;
    }

    private void updateExpandedItemCount(TreeItem<S> treeItem) {
        this.setExpandedItemCount(TreeUtil.updateExpandedItemCount(treeItem, this.expandedItemCountDirty, this.isShowRoot()));
        if (this.expandedItemCountDirty) {
            this.treeItemCacheMap.clear();
        }
        this.expandedItemCountDirty = false;
    }

    private void updateRootExpanded() {
        if (!this.isShowRoot() && this.getRoot() != null && !this.getRoot().isExpanded()) {
            this.getRoot().setExpanded(true);
        }
    }

    private void setContentWidth(double d2) {
        this.contentWidth = d2;
        if (this.isInited) {
            this.getColumnResizePolicy().call(new ResizeFeatures(this, null, 0.0));
        }
    }

    private void updateVisibleLeafColumns() {
        ArrayList arrayList = new ArrayList();
        this.buildVisibleLeafColumns(this.getColumns(), arrayList);
        this.visibleLeafColumns.setAll(arrayList);
        this.getColumnResizePolicy().call(new ResizeFeatures(this, null, 0.0));
    }

    private void buildVisibleLeafColumns(List<TreeTableColumn<S, ?>> list, List<TreeTableColumn<S, ?>> list2) {
        for (TreeTableColumn<S, ?> treeTableColumn : list) {
            boolean bl;
            if (treeTableColumn == null) continue;
            boolean bl2 = bl = !treeTableColumn.getColumns().isEmpty();
            if (bl) {
                this.buildVisibleLeafColumns(treeTableColumn.getColumns(), list2);
                continue;
            }
            if (!treeTableColumn.isVisible()) continue;
            list2.add(treeTableColumn);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return TreeTableView.getClassCssMetaData();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TreeTableViewSkin(this);
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case ROW_COUNT: {
                return this.getExpandedItemCount();
            }
            case COLUMN_COUNT: {
                return this.getVisibleLeafColumns().size();
            }
            case SELECTED_ITEMS: {
                ObservableList observableList = (ObservableList)super.queryAccessibleAttribute(accessibleAttribute, arrobject);
                ArrayList arrayList = new ArrayList();
                for (TreeTableRow treeTableRow : observableList) {
                    ObservableList observableList2 = (ObservableList)treeTableRow.queryAccessibleAttribute(accessibleAttribute, arrobject);
                    if (observableList2 == null) continue;
                    arrayList.addAll(observableList2);
                }
                return FXCollections.observableArrayList(arrayList);
            }
            case FOCUS_ITEM: {
                Node node = (Node)super.queryAccessibleAttribute(accessibleAttribute, arrobject);
                if (node == null) {
                    return null;
                }
                Node node2 = (Node)node.queryAccessibleAttribute(accessibleAttribute, arrobject);
                return node2 != null ? node2 : node;
            }
            case CELL_AT_ROW_COLUMN: {
                TreeTableRow treeTableRow = (TreeTableRow)super.queryAccessibleAttribute(accessibleAttribute, arrobject);
                return treeTableRow != null ? treeTableRow.queryAccessibleAttribute(accessibleAttribute, arrobject) : null;
            }
            case MULTIPLE_SELECTION: {
                TreeTableViewSelectionModel<S> treeTableViewSelectionModel = this.getSelectionModel();
                return treeTableViewSelectionModel != null && treeTableViewSelectionModel.getSelectionMode() == SelectionMode.MULTIPLE;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    static /* synthetic */ EventType access$2300() {
        return EDIT_ANY_EVENT;
    }

    public static class TreeTableViewFocusModel<S>
    extends TableFocusModel<TreeItem<S>, TreeTableColumn<S, ?>> {
        private final TreeTableView<S> treeTableView;
        private final TreeTablePosition EMPTY_CELL;
        private final ChangeListener<TreeItem<S>> rootPropertyListener = (observableValue, treeItem, treeItem2) -> this.updateTreeEventListener((TreeItem<S>)treeItem, (TreeItem<S>)treeItem2);
        private final WeakChangeListener<TreeItem<S>> weakRootPropertyListener = new WeakChangeListener<TreeItem<S>>(this.rootPropertyListener);
        private EventHandler<TreeItem.TreeModificationEvent<S>> treeItemListener = new EventHandler<TreeItem.TreeModificationEvent<S>>(){

            @Override
            public void handle(TreeItem.TreeModificationEvent<S> treeModificationEvent) {
                TreeTablePosition treeTablePosition;
                int n2;
                if (this.getFocusedIndex() == -1) {
                    return;
                }
                int n3 = 0;
                if (treeModificationEvent.getChange() != null) {
                    treeModificationEvent.getChange().next();
                }
                do {
                    int n4 = treeTableView.getRow(treeModificationEvent.getTreeItem());
                    if (treeModificationEvent.wasExpanded()) {
                        if (n4 >= this.getFocusedIndex()) continue;
                        n3 += treeModificationEvent.getTreeItem().getExpandedDescendentCount(false) - 1;
                        continue;
                    }
                    if (treeModificationEvent.wasCollapsed()) {
                        if (n4 >= this.getFocusedIndex()) continue;
                        n3 += -treeModificationEvent.getTreeItem().previousExpandedDescendentCount + 1;
                        continue;
                    }
                    if (treeModificationEvent.wasAdded()) {
                        TreeItem treeItem = treeModificationEvent.getTreeItem();
                        if (!treeItem.isExpanded()) continue;
                        for (int i2 = 0; i2 < treeModificationEvent.getAddedChildren().size(); ++i2) {
                            TreeItem treeItem2 = treeModificationEvent.getAddedChildren().get(i2);
                            n4 = treeTableView.getRow(treeItem2);
                            if (treeItem2 == null || n4 > this.getFocusedIndex()) continue;
                            n3 += treeItem2.getExpandedDescendentCount(false);
                        }
                    } else {
                        if (!treeModificationEvent.wasRemoved()) continue;
                        n4 += treeModificationEvent.getFrom() + 1;
                        for (int i3 = 0; i3 < treeModificationEvent.getRemovedChildren().size(); ++i3) {
                            TreeItem treeItem = treeModificationEvent.getRemovedChildren().get(i3);
                            if (treeItem == null || !treeItem.equals(this.getFocusedItem())) continue;
                            this.focus(Math.max(0, this.getFocusedIndex() - 1));
                            return;
                        }
                        if (n4 > this.getFocusedIndex()) continue;
                        n3 += treeModificationEvent.getTreeItem().isExpanded() ? -treeModificationEvent.getRemovedSize() : 0;
                    }
                } while (treeModificationEvent.getChange() != null && treeModificationEvent.getChange().next());
                if (n3 != 0 && (n2 = (treeTablePosition = this.getFocusedCell()).getRow() + n3) >= 0) {
                    Platform.runLater(() -> this.focus(n2, treeTablePosition.getTableColumn()));
                }
            }
        };
        private WeakEventHandler<TreeItem.TreeModificationEvent<S>> weakTreeItemListener;
        private ReadOnlyObjectWrapper<TreeTablePosition<S, ?>> focusedCell;

        public TreeTableViewFocusModel(TreeTableView<S> treeTableView) {
            if (treeTableView == null) {
                throw new NullPointerException("TableView can not be null");
            }
            this.treeTableView = treeTableView;
            this.EMPTY_CELL = new TreeTablePosition(treeTableView, -1, null);
            this.treeTableView.rootProperty().addListener(this.weakRootPropertyListener);
            this.updateTreeEventListener(null, treeTableView.getRoot());
            int n2 = this.getItemCount() > 0 ? 0 : -1;
            TreeTablePosition treeTablePosition = new TreeTablePosition(treeTableView, n2, null);
            this.setFocusedCell(treeTablePosition);
            treeTableView.showRootProperty().addListener(observable -> {
                if (this.isFocused(0)) {
                    this.focus(-1);
                    this.focus(0);
                }
            });
        }

        private void updateTreeEventListener(TreeItem<S> treeItem, TreeItem<S> treeItem2) {
            if (treeItem != null && this.weakTreeItemListener != null) {
                treeItem.removeEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
            if (treeItem2 != null) {
                this.weakTreeItemListener = new WeakEventHandler<TreeItem.TreeModificationEvent<S>>(this.treeItemListener);
                treeItem2.addEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
        }

        @Override
        protected int getItemCount() {
            return this.treeTableView.getExpandedItemCount();
        }

        @Override
        protected TreeItem<S> getModelItem(int n2) {
            if (n2 < 0 || n2 >= this.getItemCount()) {
                return null;
            }
            return this.treeTableView.getTreeItem(n2);
        }

        public final ReadOnlyObjectProperty<TreeTablePosition<S, ?>> focusedCellProperty() {
            return this.focusedCellPropertyImpl().getReadOnlyProperty();
        }

        private void setFocusedCell(TreeTablePosition<S, ?> treeTablePosition) {
            this.focusedCellPropertyImpl().set(treeTablePosition);
        }

        public final TreeTablePosition<S, ?> getFocusedCell() {
            return this.focusedCell == null ? this.EMPTY_CELL : (TreeTablePosition)this.focusedCell.get();
        }

        private ReadOnlyObjectWrapper<TreeTablePosition<S, ?>> focusedCellPropertyImpl() {
            if (this.focusedCell == null) {
                this.focusedCell = new ReadOnlyObjectWrapper<TreeTablePosition<S, ?>>(this.EMPTY_CELL){
                    private TreeTablePosition<S, ?> old;

                    @Override
                    protected void invalidated() {
                        if (this.get() == null) {
                            return;
                        }
                        if (this.old == null || !this.old.equals(this.get())) {
                            this.setFocusedIndex(((TreeTablePosition)this.get()).getRow());
                            this.setFocusedItem(this.getModelItem(((TreeTablePosition)this.getValue()).getRow()));
                            this.old = (TreeTablePosition)this.get();
                        }
                    }

                    @Override
                    public Object getBean() {
                        return this;
                    }

                    @Override
                    public String getName() {
                        return "focusedCell";
                    }
                };
            }
            return this.focusedCell;
        }

        @Override
        public void focus(int n2, TreeTableColumn<S, ?> treeTableColumn) {
            if (n2 < 0 || n2 >= this.getItemCount()) {
                this.setFocusedCell(this.EMPTY_CELL);
            } else {
                TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
                TreeTablePosition treeTablePosition2 = new TreeTablePosition(this.treeTableView, n2, treeTableColumn);
                this.setFocusedCell(treeTablePosition2);
                if (treeTablePosition2.equals(treeTablePosition)) {
                    this.setFocusedIndex(n2);
                    this.setFocusedItem(this.getModelItem(n2));
                }
            }
        }

        public void focus(TreeTablePosition<S, ?> treeTablePosition) {
            if (treeTablePosition == null) {
                return;
            }
            this.focus(treeTablePosition.getRow(), (TreeTableColumn<S, ?>)treeTablePosition.getTableColumn());
        }

        @Override
        public boolean isFocused(int n2, TreeTableColumn<S, ?> treeTableColumn) {
            if (n2 < 0 || n2 >= this.getItemCount()) {
                return false;
            }
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            boolean bl = treeTableColumn == null || treeTableColumn.equals(treeTablePosition.getTableColumn());
            return treeTablePosition.getRow() == n2 && bl;
        }

        @Override
        public void focus(int n2) {
            if (((TreeTableView)this.treeTableView).expandedItemCountDirty) {
                ((TreeTableView)this.treeTableView).updateExpandedItemCount(this.treeTableView.getRoot());
            }
            if (n2 < 0 || n2 >= this.getItemCount()) {
                this.setFocusedCell(this.EMPTY_CELL);
            } else {
                this.setFocusedCell(new TreeTablePosition(this.treeTableView, n2, null));
            }
        }

        @Override
        public void focusAboveCell() {
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (this.getFocusedIndex() == -1) {
                this.focus(this.getItemCount() - 1, (TreeTableColumn<S, ?>)treeTablePosition.getTableColumn());
            } else if (this.getFocusedIndex() > 0) {
                this.focus(this.getFocusedIndex() - 1, (TreeTableColumn<S, ?>)treeTablePosition.getTableColumn());
            }
        }

        @Override
        public void focusBelowCell() {
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (this.getFocusedIndex() == -1) {
                this.focus(0, (TreeTableColumn<S, ?>)treeTablePosition.getTableColumn());
            } else if (this.getFocusedIndex() != this.getItemCount() - 1) {
                this.focus(this.getFocusedIndex() + 1, (TreeTableColumn<S, ?>)treeTablePosition.getTableColumn());
            }
        }

        @Override
        public void focusLeftCell() {
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (treeTablePosition.getColumn() <= 0) {
                return;
            }
            this.focus(treeTablePosition.getRow(), this.getTableColumn((TreeTableColumn<S, ?>)treeTablePosition.getTableColumn(), -1));
        }

        @Override
        public void focusRightCell() {
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (treeTablePosition.getColumn() == this.getColumnCount() - 1) {
                return;
            }
            this.focus(treeTablePosition.getRow(), this.getTableColumn((TreeTableColumn<S, ?>)treeTablePosition.getTableColumn(), 1));
        }

        @Override
        public void focusPrevious() {
            if (this.getFocusedIndex() == -1) {
                this.focus(0);
            } else if (this.getFocusedIndex() > 0) {
                this.focusAboveCell();
            }
        }

        @Override
        public void focusNext() {
            if (this.getFocusedIndex() == -1) {
                this.focus(0);
            } else if (this.getFocusedIndex() != this.getItemCount() - 1) {
                this.focusBelowCell();
            }
        }

        private int getColumnCount() {
            return this.treeTableView.getVisibleLeafColumns().size();
        }

        private TreeTableColumn<S, ?> getTableColumn(TreeTableColumn<S, ?> treeTableColumn, int n2) {
            int n3 = this.treeTableView.getVisibleLeafIndex(treeTableColumn);
            int n4 = n3 + n2;
            return this.treeTableView.getVisibleLeafColumn(n4);
        }
    }

    static class TreeTableViewArrayListSelectionModel<S>
    extends TreeTableViewSelectionModel<S> {
        private final MappingChange.Map<TreeTablePosition<S, ?>, TreeItem<S>> cellToItemsMap = treeTablePosition -> this.getModelItem(treeTablePosition.getRow());
        private final MappingChange.Map<TreeTablePosition<S, ?>, Integer> cellToIndicesMap = treeTablePosition -> treeTablePosition.getRow();
        private TreeTableView<S> treeTableView = null;
        private ChangeListener<TreeItem<S>> rootPropertyListener = (observableValue, treeItem, treeItem2) -> {
            this.updateDefaultSelection();
            this.updateTreeEventListener((TreeItem<S>)treeItem, (TreeItem<S>)treeItem2);
        };
        private EventHandler<TreeItem.TreeModificationEvent<S>> treeItemListener = new EventHandler<TreeItem.TreeModificationEvent<S>>(){

            @Override
            public void handle(TreeItem.TreeModificationEvent<S> treeModificationEvent) {
                if (this.getSelectedIndex() == -1 && this.getSelectedItem() == null) {
                    return;
                }
                TreeItem treeItem = treeModificationEvent.getTreeItem();
                if (treeItem == null) {
                    return;
                }
                int n2 = this.getSelectedIndex();
                TreeItem treeItem2 = (TreeItem)this.getSelectedItem();
                treeTableView.expandedItemCountDirty = true;
                int n3 = treeTableView.getRow(treeItem);
                int n4 = 0;
                ListChangeListener.Change change = treeModificationEvent.getChange();
                if (change != null) {
                    change.next();
                }
                do {
                    int n5;
                    int n6;
                    int n7;
                    int n8;
                    int n9 = change == null ? 0 : change.getAddedSize();
                    int n10 = n8 = change == null ? 0 : change.getRemovedSize();
                    if (treeModificationEvent.wasExpanded()) {
                        n4 += treeItem.getExpandedDescendentCount(false) - 1;
                        ++n3;
                        continue;
                    }
                    if (treeModificationEvent.wasCollapsed()) {
                        treeItem.getExpandedDescendentCount(false);
                        int n11 = treeItem.previousExpandedDescendentCount;
                        int n12 = this.getSelectedIndex();
                        boolean bl = n12 >= n3 + 1 && n12 < n3 + n11;
                        boolean bl2 = false;
                        n7 = this.isCellSelectionEnabled();
                        ObservableList observableList = this.getTreeTableView().getVisibleLeafColumns();
                        if (n7 == 0) {
                            this.startAtomic();
                        }
                        n6 = n3 + 1;
                        int n13 = n3 + n11;
                        ArrayList<Integer> arrayList = new ArrayList<Integer>();
                        TreeTableColumn treeTableColumn = null;
                        for (int i2 = n6; i2 < n13; ++i2) {
                            if (n7 != 0) {
                                for (int i3 = 0; i3 < observableList.size(); ++i3) {
                                    TreeTableColumn treeTableColumn2 = (TreeTableColumn)observableList.get(i3);
                                    if (!this.isSelected(i2, treeTableColumn2)) continue;
                                    bl2 = true;
                                    this.clearSelection(i2, treeTableColumn2);
                                    treeTableColumn = treeTableColumn2;
                                }
                                continue;
                            }
                            if (!this.isSelected(i2)) continue;
                            bl2 = true;
                            this.clearSelection(i2);
                            arrayList.add(i2);
                        }
                        if (n7 == 0) {
                            this.stopAtomic();
                        }
                        if (bl && bl2) {
                            this.select(n3, treeTableColumn);
                        } else if (n7 == 0) {
                            NonIterableChange.GenericAddRemoveChange<Integer> genericAddRemoveChange = new NonIterableChange.GenericAddRemoveChange<Integer>(n6, n6, arrayList, selectedIndicesSeq);
                            selectedIndicesSeq.callObservers(genericAddRemoveChange);
                        }
                        n4 += -n11 + 1;
                        ++n3;
                        continue;
                    }
                    if (treeModificationEvent.wasPermutated()) {
                        this.quietClearSelection();
                        this.select(treeItem2);
                        continue;
                    }
                    if (treeModificationEvent.wasAdded()) {
                        boolean bl;
                        n4 += treeItem.isExpanded() ? n9 : 0;
                        n3 = treeTableView.getRow(treeModificationEvent.getChange().getAddedSubList().get(0));
                        TreeTablePosition treeTablePosition = TreeTableCellBehavior.getAnchor(treeTableView, null);
                        if (treeTablePosition == null || !(bl = this.isSelected(treeTablePosition.getRow(), treeTablePosition.getTableColumn()))) continue;
                        TreeTablePosition treeTablePosition2 = new TreeTablePosition(treeTableView, treeTablePosition.getRow() + n4, treeTablePosition.getTableColumn());
                        TreeTableCellBehavior.setAnchor(treeTableView, treeTablePosition2, false);
                        continue;
                    }
                    if (!treeModificationEvent.wasRemoved()) continue;
                    n4 += treeItem.isExpanded() ? -n8 : 0;
                    n3 += treeModificationEvent.getFrom() + 1;
                    ObservableList observableList = this.getSelectedIndices();
                    ObservableList observableList2 = this.getSelectedItems();
                    TreeItem treeItem3 = (TreeItem)this.getSelectedItem();
                    List list = treeModificationEvent.getChange().getRemoved();
                    for (n7 = 0; n7 < observableList.size() && !observableList2.isEmpty() && (n5 = ((Integer)observableList.get(n7)).intValue()) <= observableList2.size(); ++n7) {
                        Object object;
                        if (list.size() != 1 || observableList2.size() != 1 || treeItem3 == null || !treeItem3.equals(list.get(0)) || n2 >= this.getItemCount() || treeItem3.equals(object = this.getModelItem(n6 = n2 == 0 ? 0 : n2 - 1))) continue;
                        this.clearAndSelect(n6);
                    }
                } while (treeModificationEvent.getChange() != null && treeModificationEvent.getChange().next());
                this.shiftSelection(n3, n4, new Callback<MultipleSelectionModelBase.ShiftParams, Void>(){

                    @Override
                    public Void call(MultipleSelectionModelBase.ShiftParams shiftParams) {
                        this.startAtomic();
                        int n2 = shiftParams.getClearIndex();
                        TreeTablePosition treeTablePosition = null;
                        if (n2 > -1) {
                            for (int i2 = 0; i2 < selectedCellsMap.size(); ++i2) {
                                TreeTablePosition treeTablePosition2 = (TreeTablePosition)selectedCellsMap.get(i2);
                                if (treeTablePosition2.getRow() != n2) continue;
                                treeTablePosition = treeTablePosition2;
                                selectedCellsMap.remove(treeTablePosition2);
                                break;
                            }
                        }
                        if (treeTablePosition != null && shiftParams.isSelected()) {
                            TreeTablePosition treeTablePosition3 = new TreeTablePosition(treeTableView, shiftParams.getSetIndex(), treeTablePosition.getTableColumn());
                            selectedCellsMap.add(treeTablePosition3);
                        }
                        this.stopAtomic();
                        return null;
                    }
                });
            }
        };
        private WeakChangeListener<TreeItem<S>> weakRootPropertyListener = new WeakChangeListener<TreeItem<S>>(this.rootPropertyListener);
        private WeakEventHandler<TreeItem.TreeModificationEvent<S>> weakTreeItemListener;
        private final SelectedCellsMap<TreeTablePosition<S, ?>> selectedCellsMap;
        private final ReadOnlyUnbackedObservableList<TreeItem<S>> selectedItems;
        private final ReadOnlyUnbackedObservableList<TreeTablePosition<S, ?>> selectedCellsSeq;

        public TreeTableViewArrayListSelectionModel(TreeTableView<S> treeTableView) {
            super(treeTableView);
            this.treeTableView = treeTableView;
            this.treeTableView.rootProperty().addListener(this.weakRootPropertyListener);
            this.treeTableView.showRootProperty().addListener(observable -> this.shiftSelection(0, treeTableView.isShowRoot() ? 1 : -1, null));
            this.updateTreeEventListener(null, treeTableView.getRoot());
            this.selectedCellsMap = new SelectedCellsMap<TreeTablePosition<S, ?>>(change -> this.handleSelectedCellsListChangeEvent(change)){

                @Override
                public boolean isCellSelectionEnabled() {
                    return this.isCellSelectionEnabled();
                }
            };
            this.selectedItems = new ReadOnlyUnbackedObservableList<TreeItem<S>>(){

                @Override
                public TreeItem<S> get(int n2) {
                    return this.getModelItem((Integer)this.getSelectedIndices().get(n2));
                }

                @Override
                public int size() {
                    return this.getSelectedIndices().size();
                }
            };
            this.selectedCellsSeq = new ReadOnlyUnbackedObservableList<TreeTablePosition<S, ?>>(){

                @Override
                public TreeTablePosition<S, ?> get(int n2) {
                    return (TreeTablePosition)selectedCellsMap.get(n2);
                }

                @Override
                public int size() {
                    return selectedCellsMap.size();
                }
            };
            this.updateDefaultSelection();
            this.cellSelectionEnabledProperty().addListener(observable -> {
                this.updateDefaultSelection();
                TableCellBehaviorBase.setAnchor(treeTableView, this.getFocusedCell(), true);
            });
        }

        private void updateTreeEventListener(TreeItem<S> treeItem, TreeItem<S> treeItem2) {
            if (treeItem != null && this.weakTreeItemListener != null) {
                treeItem.removeEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
            if (treeItem2 != null) {
                this.weakTreeItemListener = new WeakEventHandler<TreeItem.TreeModificationEvent<S>>(this.treeItemListener);
                treeItem2.addEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
        }

        @Override
        public ObservableList<TreeItem<S>> getSelectedItems() {
            return this.selectedItems;
        }

        @Override
        public ObservableList<TreeTablePosition<S, ?>> getSelectedCells() {
            return this.selectedCellsSeq;
        }

        @Override
        public void clearAndSelect(int n2) {
            this.clearAndSelect(n2, (TableColumnBase<TreeItem<S>, ?>)null);
        }

        @Override
        public void clearAndSelect(int n2, TableColumnBase<TreeItem<S>, ?> tableColumnBase) {
            ListChangeListener.Change<TreeTablePosition<S, ?>> change;
            if (n2 < 0 || n2 >= this.getItemCount()) {
                return;
            }
            TreeTablePosition treeTablePosition = new TreeTablePosition(this.getTreeTableView(), n2, (TreeTableColumn)tableColumnBase);
            boolean bl = this.isCellSelectionEnabled();
            TreeTableCellBehavior.setAnchor(this.treeTableView, treeTablePosition, false);
            if (bl && tableColumnBase == null) {
                return;
            }
            boolean bl2 = this.isSelected(n2, tableColumnBase);
            ArrayList arrayList = new ArrayList(this.selectedCellsMap.getSelectedCells());
            if (bl2 && arrayList.size() == 1) {
                change = (TreeTablePosition)this.getSelectedCells().get(0);
                if (this.getSelectedItem() == this.getModelItem(n2) && ((TablePositionBase)((Object)change)).getRow() == n2 && ((TreeTablePosition)((Object)change)).getTableColumn() == tableColumnBase) {
                    return;
                }
            }
            this.startAtomic();
            this.clearSelection();
            this.select(n2, tableColumnBase);
            this.stopAtomic();
            if (bl) {
                arrayList.remove(treeTablePosition);
            } else {
                for (TreeTablePosition treeTablePosition2 : arrayList) {
                    if (treeTablePosition2.getRow() != n2) continue;
                    arrayList.remove(treeTablePosition2);
                    break;
                }
            }
            if (bl2) {
                change = ControlUtils.buildClearAndSelectChange(this.selectedCellsSeq, arrayList, n2);
            } else {
                int n3 = this.selectedCellsSeq.indexOf(treeTablePosition);
                change = new NonIterableChange.GenericAddRemoveChange(n3, n3 + 1, arrayList, this.selectedCellsSeq);
            }
            this.handleSelectedCellsListChangeEvent(change);
        }

        @Override
        public void select(int n2) {
            this.select(n2, (TableColumnBase<TreeItem<S>, ?>)null);
        }

        @Override
        public void select(int n2, TableColumnBase<TreeItem<S>, ?> tableColumnBase) {
            if (n2 < 0 || n2 >= this.getRowCount()) {
                return;
            }
            if (this.isCellSelectionEnabled() && tableColumnBase == null) {
                ObservableList observableList = this.getTreeTableView().getVisibleLeafColumns();
                for (int i2 = 0; i2 < observableList.size(); ++i2) {
                    this.select(n2, (TableColumnBase)observableList.get(i2));
                }
                return;
            }
            TreeTablePosition treeTablePosition = new TreeTablePosition(this.getTreeTableView(), n2, (TreeTableColumn)tableColumnBase);
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
                this.startAtomic();
                this.quietClearSelection();
                this.stopAtomic();
            }
            if (TreeTableCellBehavior.hasDefaultAnchor(this.treeTableView)) {
                TreeTableCellBehavior.removeAnchor(this.treeTableView);
            }
            this.selectedCellsMap.add(treeTablePosition);
            this.updateSelectedIndex(n2);
            this.focus(n2, (TreeTableColumn)tableColumnBase);
        }

        @Override
        public void select(TreeItem<S> treeItem) {
            if (treeItem == null && this.getSelectionMode() == SelectionMode.SINGLE) {
                this.clearSelection();
                return;
            }
            int n2 = this.treeTableView.getRow(treeItem);
            if (n2 > -1) {
                if (this.isSelected(n2)) {
                    return;
                }
                if (this.getSelectionMode() == SelectionMode.SINGLE) {
                    this.quietClearSelection();
                }
                this.select(n2);
            } else {
                this.setSelectedIndex(-1);
                this.setSelectedItem(treeItem);
            }
        }

        @Override
        public void selectIndices(int n2, int ... arrn) {
            if (arrn == null) {
                this.select(n2);
                return;
            }
            int n3 = this.getRowCount();
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
                this.quietClearSelection();
                for (int i2 = arrn.length - 1; i2 >= 0; --i2) {
                    int n4 = arrn[i2];
                    if (n4 < 0 || n4 >= n3) continue;
                    this.select(n4);
                    break;
                }
                if (this.selectedCellsMap.isEmpty() && n2 > 0 && n2 < n3) {
                    this.select(n2);
                }
            } else {
                int n5;
                int n6 = -1;
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                if (n2 >= 0 && n2 < n3) {
                    if (this.isCellSelectionEnabled()) {
                        ObservableList observableList = this.getTreeTableView().getVisibleLeafColumns();
                        for (n5 = 0; n5 < observableList.size(); ++n5) {
                            if (this.selectedCellsMap.isSelected(n2, n5)) continue;
                            linkedHashSet.add(new TreeTablePosition(this.getTreeTableView(), n2, (TreeTableColumn)observableList.get(n5)));
                        }
                    } else {
                        boolean bl = this.selectedCellsMap.isSelected(n2, -1);
                        if (!bl) {
                            linkedHashSet.add(new TreeTablePosition(this.getTreeTableView(), n2, null));
                        }
                    }
                    n6 = n2;
                }
                for (int i3 = 0; i3 < arrn.length; ++i3) {
                    n5 = arrn[i3];
                    if (n5 < 0 || n5 >= n3) continue;
                    n6 = n5;
                    if (this.isCellSelectionEnabled()) {
                        ObservableList observableList = this.getTreeTableView().getVisibleLeafColumns();
                        for (int i4 = 0; i4 < observableList.size(); ++i4) {
                            if (this.selectedCellsMap.isSelected(n5, i4)) continue;
                            linkedHashSet.add(new TreeTablePosition(this.getTreeTableView(), n5, (TreeTableColumn)observableList.get(i4)));
                            n6 = n5;
                        }
                        continue;
                    }
                    if (this.selectedCellsMap.isSelected(n5, -1)) continue;
                    linkedHashSet.add(new TreeTablePosition(this.getTreeTableView(), n5, null));
                }
                this.selectedCellsMap.addAll(linkedHashSet);
                if (n6 != -1) {
                    this.select(n6);
                }
            }
        }

        @Override
        public void selectAll() {
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
                return;
            }
            if (this.isCellSelectionEnabled()) {
                ArrayList arrayList = new ArrayList();
                TreeTablePosition treeTablePosition = null;
                for (int i2 = 0; i2 < this.getTreeTableView().getVisibleLeafColumns().size(); ++i2) {
                    TreeTableColumn treeTableColumn = (TreeTableColumn)this.getTreeTableView().getVisibleLeafColumns().get(i2);
                    for (int i3 = 0; i3 < this.getRowCount(); ++i3) {
                        treeTablePosition = new TreeTablePosition(this.getTreeTableView(), i3, treeTableColumn);
                        arrayList.add(treeTablePosition);
                    }
                }
                this.selectedCellsMap.setAll(arrayList);
                if (treeTablePosition != null) {
                    this.select(treeTablePosition.getRow(), treeTablePosition.getTableColumn());
                    this.focus(treeTablePosition.getRow(), (TreeTableColumn<S, ?>)treeTablePosition.getTableColumn());
                }
            } else {
                int n2;
                ArrayList arrayList = new ArrayList();
                for (n2 = 0; n2 < this.getRowCount(); ++n2) {
                    arrayList.add(new TreeTablePosition(this.getTreeTableView(), n2, null));
                }
                this.selectedCellsMap.setAll(arrayList);
                n2 = this.getFocusedIndex();
                if (n2 == -1) {
                    int n3 = this.getItemCount();
                    if (n3 > 0) {
                        this.select(n3 - 1);
                        this.focus((TreeTablePosition)arrayList.get(arrayList.size() - 1));
                    }
                } else {
                    this.select(n2);
                    this.focus(n2);
                }
            }
        }

        @Override
        public void selectRange(int n2, TableColumnBase<TreeItem<S>, ?> tableColumnBase, int n3, TableColumnBase<TreeItem<S>, ?> tableColumnBase2) {
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
                this.quietClearSelection();
                this.select(n3, tableColumnBase2);
                return;
            }
            this.startAtomic();
            int n4 = this.getItemCount();
            boolean bl = this.isCellSelectionEnabled();
            int n5 = this.treeTableView.getVisibleLeafIndex((TreeTableColumn)tableColumnBase);
            int n6 = this.treeTableView.getVisibleLeafIndex((TreeTableColumn)tableColumnBase2);
            int n7 = Math.min(n5, n6);
            int n8 = Math.max(n5, n6);
            int n9 = Math.min(n2, n3);
            int n10 = Math.max(n2, n3);
            ArrayList arrayList = new ArrayList();
            for (int i2 = n9; i2 <= n10; ++i2) {
                if (i2 < 0 || i2 >= n4) continue;
                if (!bl) {
                    arrayList.add(new TreeTablePosition(this.treeTableView, i2, (TreeTableColumn)tableColumnBase));
                    continue;
                }
                for (int i3 = n7; i3 <= n8; ++i3) {
                    TreeTableColumn<S, ?> treeTableColumn = this.treeTableView.getVisibleLeafColumn(i3);
                    if (treeTableColumn == null && bl) continue;
                    arrayList.add(new TreeTablePosition(this.treeTableView, i2, treeTableColumn));
                }
            }
            arrayList.removeAll(this.getSelectedCells());
            this.selectedCellsMap.addAll(arrayList);
            this.stopAtomic();
            this.updateSelectedIndex(n3);
            this.focus(n3, (TreeTableColumn)tableColumnBase2);
            TreeTableColumn treeTableColumn = (TreeTableColumn)tableColumnBase;
            TreeTableColumn treeTableColumn2 = bl ? (TreeTableColumn)tableColumnBase2 : treeTableColumn;
            int n11 = this.selectedCellsMap.indexOf(new TreeTablePosition(this.treeTableView, n2, treeTableColumn));
            int n12 = this.selectedCellsMap.indexOf(new TreeTablePosition(this.treeTableView, n3, treeTableColumn2));
            if (n11 > -1 && n12 > -1) {
                int n13 = Math.min(n11, n12);
                int n14 = Math.max(n11, n12);
                NonIterableChange.SimpleAddChange simpleAddChange = new NonIterableChange.SimpleAddChange(n13, n14 + 1, this.selectedCellsSeq);
                this.handleSelectedCellsListChangeEvent(simpleAddChange);
            }
        }

        @Override
        public void clearSelection(int n2) {
            this.clearSelection(n2, (TableColumnBase<TreeItem<S>, ?>)null);
        }

        @Override
        public void clearSelection(int n2, TableColumnBase<TreeItem<S>, ?> tableColumnBase) {
            this.clearSelection(new TreeTablePosition(this.getTreeTableView(), n2, (TreeTableColumn)tableColumnBase));
        }

        private void clearSelection(TreeTablePosition<S, ?> treeTablePosition) {
            boolean bl = this.isCellSelectionEnabled();
            int n2 = treeTablePosition.getRow();
            for (TreeTablePosition treeTablePosition2 : this.getSelectedCells()) {
                if (!bl) {
                    if (treeTablePosition2.getRow() != n2) continue;
                    this.selectedCellsMap.remove(treeTablePosition2);
                    break;
                }
                if (!treeTablePosition2.equals(treeTablePosition)) continue;
                this.selectedCellsMap.remove(treeTablePosition);
                break;
            }
            if (this.isEmpty() && !this.isAtomic()) {
                this.updateSelectedIndex(-1);
                this.selectedCellsMap.clear();
            }
        }

        @Override
        public void clearSelection() {
            final ArrayList arrayList = new ArrayList(this.getSelectedCells());
            this.quietClearSelection();
            if (!this.isAtomic()) {
                this.updateSelectedIndex(-1);
                this.focus(-1);
                NonIterableChange nonIterableChange = new NonIterableChange<TreeTablePosition<S, ?>>(0, 0, this.selectedCellsSeq){

                    @Override
                    public List<TreeTablePosition<S, ?>> getRemoved() {
                        return arrayList;
                    }
                };
                this.handleSelectedCellsListChangeEvent(nonIterableChange);
            }
        }

        private void quietClearSelection() {
            this.startAtomic();
            this.selectedCellsMap.clear();
            this.stopAtomic();
        }

        @Override
        public boolean isSelected(int n2) {
            return this.isSelected(n2, (TableColumnBase<TreeItem<S>, ?>)null);
        }

        @Override
        public boolean isSelected(int n2, TableColumnBase<TreeItem<S>, ?> tableColumnBase) {
            boolean bl = this.isCellSelectionEnabled();
            if (bl && tableColumnBase == null) {
                return false;
            }
            int n3 = !bl || tableColumnBase == null ? -1 : this.treeTableView.getVisibleLeafIndex((TreeTableColumn)tableColumnBase);
            return this.selectedCellsMap.isSelected(n2, n3);
        }

        @Override
        public boolean isEmpty() {
            return this.selectedCellsMap.isEmpty();
        }

        @Override
        public void selectPrevious() {
            if (this.isCellSelectionEnabled()) {
                TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
                if (treeTablePosition.getColumn() - 1 >= 0) {
                    this.select(treeTablePosition.getRow(), (TableColumnBase<TreeItem<S>, ?>)this.getTableColumn((TreeTableColumn<S, ?>)treeTablePosition.getTableColumn(), -1));
                } else if (treeTablePosition.getRow() < this.getRowCount() - 1) {
                    this.select(treeTablePosition.getRow() - 1, (TableColumnBase<TreeItem<S>, ?>)this.getTableColumn(this.getTreeTableView().getVisibleLeafColumns().size() - 1));
                }
            } else {
                int n2 = this.getFocusedIndex();
                if (n2 == -1) {
                    this.select(this.getRowCount() - 1);
                } else if (n2 > 0) {
                    this.select(n2 - 1);
                }
            }
        }

        @Override
        public void selectNext() {
            if (this.isCellSelectionEnabled()) {
                TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
                if (treeTablePosition.getColumn() + 1 < this.getTreeTableView().getVisibleLeafColumns().size()) {
                    this.select(treeTablePosition.getRow(), (TableColumnBase<TreeItem<S>, ?>)this.getTableColumn((TreeTableColumn<S, ?>)treeTablePosition.getTableColumn(), 1));
                } else if (treeTablePosition.getRow() < this.getRowCount() - 1) {
                    this.select(treeTablePosition.getRow() + 1, (TableColumnBase<TreeItem<S>, ?>)this.getTableColumn(0));
                }
            } else {
                int n2 = this.getFocusedIndex();
                if (n2 == -1) {
                    this.select(0);
                } else if (n2 < this.getRowCount() - 1) {
                    this.select(n2 + 1);
                }
            }
        }

        @Override
        public void selectAboveCell() {
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (treeTablePosition.getRow() == -1) {
                this.select(this.getRowCount() - 1);
            } else if (treeTablePosition.getRow() > 0) {
                this.select(treeTablePosition.getRow() - 1, treeTablePosition.getTableColumn());
            }
        }

        @Override
        public void selectBelowCell() {
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (treeTablePosition.getRow() == -1) {
                this.select(0);
            } else if (treeTablePosition.getRow() < this.getRowCount() - 1) {
                this.select(treeTablePosition.getRow() + 1, treeTablePosition.getTableColumn());
            }
        }

        @Override
        public void selectFirst() {
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
                this.quietClearSelection();
            }
            if (this.getRowCount() > 0) {
                if (this.isCellSelectionEnabled()) {
                    this.select(0, treeTablePosition.getTableColumn());
                } else {
                    this.select(0);
                }
            }
        }

        @Override
        public void selectLast() {
            int n2;
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
                this.quietClearSelection();
            }
            if ((n2 = this.getRowCount()) > 0 && this.getSelectedIndex() < n2 - 1) {
                if (this.isCellSelectionEnabled()) {
                    this.select(n2 - 1, treeTablePosition.getTableColumn());
                } else {
                    this.select(n2 - 1);
                }
            }
        }

        @Override
        public void selectLeftCell() {
            if (!this.isCellSelectionEnabled()) {
                return;
            }
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (treeTablePosition.getColumn() - 1 >= 0) {
                this.select(treeTablePosition.getRow(), (TableColumnBase<TreeItem<S>, ?>)this.getTableColumn((TreeTableColumn<S, ?>)treeTablePosition.getTableColumn(), -1));
            }
        }

        @Override
        public void selectRightCell() {
            if (!this.isCellSelectionEnabled()) {
                return;
            }
            TreeTablePosition<S, ?> treeTablePosition = this.getFocusedCell();
            if (treeTablePosition.getColumn() + 1 < this.getTreeTableView().getVisibleLeafColumns().size()) {
                this.select(treeTablePosition.getRow(), (TableColumnBase<TreeItem<S>, ?>)this.getTableColumn((TreeTableColumn<S, ?>)treeTablePosition.getTableColumn(), 1));
            }
        }

        private void updateDefaultSelection() {
            this.clearSelection();
            int n2 = this.getItemCount() > 0 ? 0 : -1;
            this.focus(n2, this.isCellSelectionEnabled() ? this.getTableColumn(0) : null);
        }

        private TreeTableColumn<S, ?> getTableColumn(int n2) {
            return this.getTreeTableView().getVisibleLeafColumn(n2);
        }

        private TreeTableColumn<S, ?> getTableColumn(TreeTableColumn<S, ?> treeTableColumn, int n2) {
            int n3 = this.getTreeTableView().getVisibleLeafIndex(treeTableColumn);
            int n4 = n3 + n2;
            return this.getTreeTableView().getVisibleLeafColumn(n4);
        }

        private void updateSelectedIndex(int n2) {
            this.setSelectedIndex(n2);
            this.setSelectedItem(this.getModelItem(n2));
        }

        @Override
        public void focus(int n2) {
            this.focus(n2, null);
        }

        private void focus(int n2, TreeTableColumn<S, ?> treeTableColumn) {
            this.focus(new TreeTablePosition(this.getTreeTableView(), n2, treeTableColumn));
        }

        private void focus(TreeTablePosition<S, ?> treeTablePosition) {
            if (this.getTreeTableView().getFocusModel() == null) {
                return;
            }
            this.getTreeTableView().getFocusModel().focus(treeTablePosition.getRow(), treeTablePosition.getTableColumn());
            this.getTreeTableView().notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        @Override
        public int getFocusedIndex() {
            return this.getFocusedCell().getRow();
        }

        private TreeTablePosition<S, ?> getFocusedCell() {
            if (this.treeTableView.getFocusModel() == null) {
                return new TreeTablePosition(this.treeTableView, -1, null);
            }
            return this.treeTableView.getFocusModel().getFocusedCell();
        }

        private int getRowCount() {
            return this.treeTableView.getExpandedItemCount();
        }

        private void handleSelectedCellsListChangeEvent(ListChangeListener.Change<? extends TreeTablePosition<S, ?>> change) {
            boolean bl;
            ArrayList<Integer> arrayList;
            ArrayList<Integer> arrayList2;
            block15: {
                int n2;
                int n3;
                arrayList2 = new ArrayList<Integer>();
                arrayList = new ArrayList<Integer>();
                while (change.next()) {
                    TreeTablePosition<S, ?> treeTablePosition;
                    List<TreeTablePosition<S, ?>> list;
                    if (change.wasRemoved()) {
                        list = change.getRemoved();
                        for (n3 = 0; n3 < list.size(); ++n3) {
                            treeTablePosition = list.get(n3);
                            n2 = treeTablePosition.getRow();
                            if (!this.selectedIndices.get(n2)) continue;
                            this.selectedIndices.clear(n2);
                            arrayList.add(n2);
                        }
                    }
                    if (!change.wasAdded()) continue;
                    list = change.getAddedSubList();
                    for (n3 = 0; n3 < list.size(); ++n3) {
                        treeTablePosition = list.get(n3);
                        n2 = treeTablePosition.getRow();
                        if (this.selectedIndices.get(n2)) continue;
                        this.selectedIndices.set(n2);
                        arrayList2.add(n2);
                    }
                }
                change.reset();
                this.selectedIndicesSeq.reset();
                if (this.isAtomic()) {
                    return;
                }
                change.next();
                if (change.wasReplaced()) {
                    int n4;
                    n3 = change.getRemovedSize();
                    if (n3 != (n4 = change.getAddedSize())) {
                        bl = true;
                    } else {
                        for (n2 = 0; n2 < change.getRemovedSize(); ++n2) {
                            TreeTablePosition<S, ?> treeTablePosition = change.getRemoved().get(n2);
                            TreeItem<S> treeItem = treeTablePosition.getTreeItem();
                            boolean bl2 = false;
                            for (int i2 = 0; i2 < change.getAddedSize(); ++i2) {
                                TreeTablePosition<S, ?> treeTablePosition2 = change.getAddedSubList().get(i2);
                                TreeItem<S> treeItem2 = treeTablePosition2.getTreeItem();
                                if (treeItem == null || !treeItem.equals(treeItem2)) continue;
                                bl2 = true;
                                break;
                            }
                            if (bl2) continue;
                            bl = true;
                            break block15;
                        }
                        bl = false;
                    }
                } else {
                    bl = true;
                }
            }
            if (bl) {
                this.selectedItems.callObservers(new MappingChange(change, this.cellToItemsMap, this.selectedItems));
            }
            change.reset();
            if (this.selectedItems.isEmpty() && this.getSelectedItem() != null) {
                this.setSelectedItem(null);
            }
            ReadOnlyUnbackedObservableList readOnlyUnbackedObservableList = (ReadOnlyUnbackedObservableList)this.getSelectedIndices();
            if (!arrayList2.isEmpty() && arrayList.isEmpty()) {
                ListChangeListener.Change<Integer> change2 = TreeTableViewArrayListSelectionModel.createRangeChange(readOnlyUnbackedObservableList, arrayList2, false);
                readOnlyUnbackedObservableList.callObservers(change2);
            } else {
                readOnlyUnbackedObservableList.callObservers(new MappingChange(change, this.cellToIndicesMap, readOnlyUnbackedObservableList));
                change.reset();
            }
            this.selectedCellsSeq.callObservers(new MappingChange(change, MappingChange.NOOP_MAP, this.selectedCellsSeq));
            change.reset();
        }
    }

    public static abstract class TreeTableViewSelectionModel<S>
    extends TableSelectionModel<TreeItem<S>> {
        private final TreeTableView<S> treeTableView;

        public TreeTableViewSelectionModel(TreeTableView<S> treeTableView) {
            if (treeTableView == null) {
                throw new NullPointerException("TreeTableView can not be null");
            }
            this.treeTableView = treeTableView;
        }

        public abstract ObservableList<TreeTablePosition<S, ?>> getSelectedCells();

        public TreeTableView<S> getTreeTableView() {
            return this.treeTableView;
        }

        @Override
        public TreeItem<S> getModelItem(int n2) {
            return this.treeTableView.getTreeItem(n2);
        }

        @Override
        protected int getItemCount() {
            return this.treeTableView.getExpandedItemCount();
        }

        @Override
        public void focus(int n2) {
            this.focus(n2, null);
        }

        @Override
        public int getFocusedIndex() {
            return this.getFocusedCell().getRow();
        }

        @Override
        public void selectRange(int n2, TableColumnBase<TreeItem<S>, ?> tableColumnBase, int n3, TableColumnBase<TreeItem<S>, ?> tableColumnBase2) {
            int n4 = this.treeTableView.getVisibleLeafIndex((TreeTableColumn)tableColumnBase);
            int n5 = this.treeTableView.getVisibleLeafIndex((TreeTableColumn)tableColumnBase2);
            for (int i2 = n2; i2 <= n3; ++i2) {
                for (int i3 = n4; i3 <= n5; ++i3) {
                    this.select(i2, this.treeTableView.getVisibleLeafColumn(i3));
                }
            }
        }

        private void focus(int n2, TreeTableColumn<S, ?> treeTableColumn) {
            this.focus(new TreeTablePosition(this.getTreeTableView(), n2, treeTableColumn));
        }

        private void focus(TreeTablePosition<S, ?> treeTablePosition) {
            if (this.getTreeTableView().getFocusModel() == null) {
                return;
            }
            this.getTreeTableView().getFocusModel().focus(treeTablePosition.getRow(), (TreeTableColumn<S, ?>)treeTablePosition.getTableColumn());
        }

        private TreeTablePosition<S, ?> getFocusedCell() {
            if (this.treeTableView.getFocusModel() == null) {
                return new TreeTablePosition(this.treeTableView, -1, null);
            }
            return this.treeTableView.getFocusModel().getFocusedCell();
        }
    }

    public static class EditEvent<S>
    extends Event {
        private static final long serialVersionUID = -4437033058917528976L;
        public static final EventType<?> ANY = TreeTableView.access$2300();
        private final S oldValue;
        private final S newValue;
        private final transient TreeItem<S> treeItem;

        public EditEvent(TreeTableView<S> treeTableView, EventType<? extends EditEvent> eventType, TreeItem<S> treeItem, S s2, S s3) {
            super(treeTableView, Event.NULL_SOURCE_TARGET, eventType);
            this.oldValue = s2;
            this.newValue = s3;
            this.treeItem = treeItem;
        }

        @Override
        public TreeTableView<S> getSource() {
            return (TreeTableView)super.getSource();
        }

        public TreeItem<S> getTreeItem() {
            return this.treeItem;
        }

        public S getNewValue() {
            return this.newValue;
        }

        public S getOldValue() {
            return this.oldValue;
        }
    }

    public static class ResizeFeatures<S>
    extends ResizeFeaturesBase<TreeItem<S>> {
        private TreeTableView<S> treeTable;

        public ResizeFeatures(TreeTableView<S> treeTableView, TreeTableColumn<S, ?> treeTableColumn, Double d2) {
            super(treeTableColumn, d2);
            this.treeTable = treeTableView;
        }

        @Override
        public TreeTableColumn<S, ?> getColumn() {
            return (TreeTableColumn)super.getColumn();
        }

        public TreeTableView<S> getTable() {
            return this.treeTable;
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<TreeTableView<?>, Number> FIXED_CELL_SIZE = new CssMetaData<TreeTableView<?>, Number>("-fx-fixed-cell-size", SizeConverter.getInstance(), -1.0){

            @Override
            public Double getInitialValue(TreeTableView<?> treeTableView) {
                return treeTableView.getFixedCellSize();
            }

            @Override
            public boolean isSettable(TreeTableView<?> treeTableView) {
                return ((TreeTableView)treeTableView).fixedCellSize == null || !((TreeTableView)treeTableView).fixedCellSize.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TreeTableView<?> treeTableView) {
                return (StyleableProperty)((Object)treeTableView.fixedCellSizeProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Control.getClassCssMetaData());
            arrayList.add(FIXED_CELL_SIZE);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

