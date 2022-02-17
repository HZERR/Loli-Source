/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.ListCellBehavior;
import com.sun.javafx.scene.control.skin.ListViewSkin;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ControlUtils;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.MultipleSelectionModelBase;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Skin;
import javafx.util.Callback;

@DefaultProperty(value="items")
public class ListView<T>
extends Control {
    private static final EventType<?> EDIT_ANY_EVENT = new EventType<Event>(Event.ANY, "LIST_VIEW_EDIT");
    private static final EventType<?> EDIT_START_EVENT = new EventType(ListView.editAnyEvent(), "EDIT_START");
    private static final EventType<?> EDIT_CANCEL_EVENT = new EventType(ListView.editAnyEvent(), "EDIT_CANCEL");
    private static final EventType<?> EDIT_COMMIT_EVENT = new EventType(ListView.editAnyEvent(), "EDIT_COMMIT");
    private boolean selectFirstRowByDefault = true;
    private EventHandler<EditEvent<T>> DEFAULT_EDIT_COMMIT_HANDLER = editEvent -> {
        int n2 = editEvent.getIndex();
        ObservableList<T> observableList = this.getItems();
        if (n2 < 0 || n2 >= observableList.size()) {
            return;
        }
        observableList.set(n2, editEvent.getNewValue());
    };
    private ObjectProperty<ObservableList<T>> items;
    private ObjectProperty<Node> placeholder;
    private ObjectProperty<MultipleSelectionModel<T>> selectionModel = new SimpleObjectProperty<MultipleSelectionModel<T>>(this, "selectionModel");
    private ObjectProperty<FocusModel<T>> focusModel;
    private ObjectProperty<Orientation> orientation;
    private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory;
    private DoubleProperty fixedCellSize;
    private BooleanProperty editable;
    private ReadOnlyIntegerWrapper editingIndex;
    private ObjectProperty<EventHandler<EditEvent<T>>> onEditStart;
    private ObjectProperty<EventHandler<EditEvent<T>>> onEditCommit;
    private ObjectProperty<EventHandler<EditEvent<T>>> onEditCancel;
    private ObjectProperty<EventHandler<ScrollToEvent<Integer>>> onScrollTo;
    private static final String DEFAULT_STYLE_CLASS = "list-view";
    private static final PseudoClass PSEUDO_CLASS_VERTICAL = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass PSEUDO_CLASS_HORIZONTAL = PseudoClass.getPseudoClass("horizontal");

    public static <T> EventType<EditEvent<T>> editAnyEvent() {
        return EDIT_ANY_EVENT;
    }

    public static <T> EventType<EditEvent<T>> editStartEvent() {
        return EDIT_START_EVENT;
    }

    public static <T> EventType<EditEvent<T>> editCancelEvent() {
        return EDIT_CANCEL_EVENT;
    }

    public static <T> EventType<EditEvent<T>> editCommitEvent() {
        return EDIT_COMMIT_EVENT;
    }

    public ListView() {
        this(FXCollections.observableArrayList());
    }

    public ListView(ObservableList<T> observableList) {
        this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.setAccessibleRole(AccessibleRole.LIST_VIEW);
        this.setItems(observableList);
        this.setSelectionModel(new ListViewBitSetSelectionModel(this));
        this.setFocusModel(new ListViewFocusModel(this));
        this.setOnEditCommit(this.DEFAULT_EDIT_COMMIT_HANDLER);
        this.getProperties().addListener(change -> {
            if (change.wasAdded() && "selectFirstRowByDefault".equals(change.getKey())) {
                Boolean bl = (Boolean)change.getValueAdded();
                if (bl == null) {
                    return;
                }
                this.selectFirstRowByDefault = bl;
            }
        });
    }

    public final void setItems(ObservableList<T> observableList) {
        this.itemsProperty().set(observableList);
    }

    public final ObservableList<T> getItems() {
        return this.items == null ? null : (ObservableList)this.items.get();
    }

    public final ObjectProperty<ObservableList<T>> itemsProperty() {
        if (this.items == null) {
            this.items = new SimpleObjectProperty<ObservableList<T>>(this, "items");
        }
        return this.items;
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

    public final void setSelectionModel(MultipleSelectionModel<T> multipleSelectionModel) {
        this.selectionModelProperty().set(multipleSelectionModel);
    }

    public final MultipleSelectionModel<T> getSelectionModel() {
        return this.selectionModel == null ? null : (MultipleSelectionModel)this.selectionModel.get();
    }

    public final ObjectProperty<MultipleSelectionModel<T>> selectionModelProperty() {
        return this.selectionModel;
    }

    public final void setFocusModel(FocusModel<T> focusModel) {
        this.focusModelProperty().set(focusModel);
    }

    public final FocusModel<T> getFocusModel() {
        return this.focusModel == null ? null : (FocusModel)this.focusModel.get();
    }

    public final ObjectProperty<FocusModel<T>> focusModelProperty() {
        if (this.focusModel == null) {
            this.focusModel = new SimpleObjectProperty<FocusModel<T>>(this, "focusModel");
        }
        return this.focusModel;
    }

    public final void setOrientation(Orientation orientation) {
        this.orientationProperty().set(orientation);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.VERTICAL : (Orientation)((Object)this.orientation.get());
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty<Orientation>(Orientation.VERTICAL){

                @Override
                public void invalidated() {
                    boolean bl = this.get() == Orientation.VERTICAL;
                    ListView.this.pseudoClassStateChanged(PSEUDO_CLASS_VERTICAL, bl);
                    ListView.this.pseudoClassStateChanged(PSEUDO_CLASS_HORIZONTAL, !bl);
                }

                @Override
                public CssMetaData<ListView<?>, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override
                public Object getBean() {
                    return ListView.this;
                }

                @Override
                public String getName() {
                    return "orientation";
                }
            };
        }
        return this.orientation;
    }

    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> callback) {
        this.cellFactoryProperty().set(callback);
    }

    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return this.cellFactory == null ? null : (Callback)this.cellFactory.get();
    }

    public final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        if (this.cellFactory == null) {
            this.cellFactory = new SimpleObjectProperty<Callback<ListView<T>, ListCell<T>>>(this, "cellFactory");
        }
        return this.cellFactory;
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
                public CssMetaData<ListView<?>, Number> getCssMetaData() {
                    return StyleableProperties.FIXED_CELL_SIZE;
                }

                @Override
                public Object getBean() {
                    return ListView.this;
                }

                @Override
                public String getName() {
                    return "fixedCellSize";
                }
            };
        }
        return this.fixedCellSize;
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

    private void setEditingIndex(int n2) {
        this.editingIndexPropertyImpl().set(n2);
    }

    public final int getEditingIndex() {
        return this.editingIndex == null ? -1 : this.editingIndex.get();
    }

    public final ReadOnlyIntegerProperty editingIndexProperty() {
        return this.editingIndexPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyIntegerWrapper editingIndexPropertyImpl() {
        if (this.editingIndex == null) {
            this.editingIndex = new ReadOnlyIntegerWrapper(this, "editingIndex", -1);
        }
        return this.editingIndex;
    }

    public final void setOnEditStart(EventHandler<EditEvent<T>> eventHandler) {
        this.onEditStartProperty().set(eventHandler);
    }

    public final EventHandler<EditEvent<T>> getOnEditStart() {
        return this.onEditStart == null ? null : (EventHandler)this.onEditStart.get();
    }

    public final ObjectProperty<EventHandler<EditEvent<T>>> onEditStartProperty() {
        if (this.onEditStart == null) {
            this.onEditStart = new ObjectPropertyBase<EventHandler<EditEvent<T>>>(){

                @Override
                protected void invalidated() {
                    ListView.this.setEventHandler(ListView.editStartEvent(), (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return ListView.this;
                }

                @Override
                public String getName() {
                    return "onEditStart";
                }
            };
        }
        return this.onEditStart;
    }

    public final void setOnEditCommit(EventHandler<EditEvent<T>> eventHandler) {
        this.onEditCommitProperty().set(eventHandler);
    }

    public final EventHandler<EditEvent<T>> getOnEditCommit() {
        return this.onEditCommit == null ? null : (EventHandler)this.onEditCommit.get();
    }

    public final ObjectProperty<EventHandler<EditEvent<T>>> onEditCommitProperty() {
        if (this.onEditCommit == null) {
            this.onEditCommit = new ObjectPropertyBase<EventHandler<EditEvent<T>>>(){

                @Override
                protected void invalidated() {
                    ListView.this.setEventHandler(ListView.editCommitEvent(), (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return ListView.this;
                }

                @Override
                public String getName() {
                    return "onEditCommit";
                }
            };
        }
        return this.onEditCommit;
    }

    public final void setOnEditCancel(EventHandler<EditEvent<T>> eventHandler) {
        this.onEditCancelProperty().set(eventHandler);
    }

    public final EventHandler<EditEvent<T>> getOnEditCancel() {
        return this.onEditCancel == null ? null : (EventHandler)this.onEditCancel.get();
    }

    public final ObjectProperty<EventHandler<EditEvent<T>>> onEditCancelProperty() {
        if (this.onEditCancel == null) {
            this.onEditCancel = new ObjectPropertyBase<EventHandler<EditEvent<T>>>(){

                @Override
                protected void invalidated() {
                    ListView.this.setEventHandler(ListView.editCancelEvent(), (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return ListView.this;
                }

                @Override
                public String getName() {
                    return "onEditCancel";
                }
            };
        }
        return this.onEditCancel;
    }

    public void edit(int n2) {
        if (!this.isEditable()) {
            return;
        }
        this.setEditingIndex(n2);
    }

    public void scrollTo(int n2) {
        ControlUtils.scrollToIndex(this, n2);
    }

    public void scrollTo(T t2) {
        int n2;
        if (this.getItems() != null && (n2 = this.getItems().indexOf(t2)) >= 0) {
            ControlUtils.scrollToIndex(this, n2);
        }
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
                    ListView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return ListView.this;
                }

                @Override
                public String getName() {
                    return "onScrollTo";
                }
            };
        }
        return this.onScrollTo;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ListViewSkin(this);
    }

    public void refresh() {
        this.getProperties().put("listRecreateKey", Boolean.TRUE);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return ListView.getClassCssMetaData();
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case MULTIPLE_SELECTION: {
                MultipleSelectionModel<T> multipleSelectionModel = this.getSelectionModel();
                return multipleSelectionModel != null && multipleSelectionModel.getSelectionMode() == SelectionMode.MULTIPLE;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    static /* synthetic */ EventType access$1100() {
        return EDIT_ANY_EVENT;
    }

    static class ListViewFocusModel<T>
    extends FocusModel<T> {
        private final ListView<T> listView;
        private int itemCount = 0;
        private final InvalidationListener itemsObserver;
        private final ListChangeListener<T> itemsContentListener = change -> {
            this.updateItemCount();
            while (change.next()) {
                int n2 = change.getFrom();
                if (this.getFocusedIndex() == -1 || n2 > this.getFocusedIndex()) {
                    return;
                }
                change.reset();
                boolean bl = false;
                boolean bl2 = false;
                int n3 = 0;
                int n4 = 0;
                while (change.next()) {
                    bl |= change.wasAdded();
                    bl2 |= change.wasRemoved();
                    n3 += change.getAddedSize();
                    n4 += change.getRemovedSize();
                }
                if (bl && !bl2) {
                    this.focus(Math.min(this.getItemCount() - 1, this.getFocusedIndex() + n3));
                    continue;
                }
                if (bl || !bl2) continue;
                this.focus(Math.max(0, this.getFocusedIndex() - n4));
            }
        };
        private WeakListChangeListener<T> weakItemsContentListener = new WeakListChangeListener<T>(this.itemsContentListener);

        public ListViewFocusModel(final ListView<T> listView) {
            if (listView == null) {
                throw new IllegalArgumentException("ListView can not be null");
            }
            this.listView = listView;
            this.itemsObserver = new InvalidationListener(){
                private WeakReference<ObservableList<T>> weakItemsRef;
                {
                    this.weakItemsRef = new WeakReference(listView.getItems());
                }

                @Override
                public void invalidated(Observable observable) {
                    ObservableList observableList = (ObservableList)this.weakItemsRef.get();
                    this.weakItemsRef = new WeakReference(listView.getItems());
                    this.updateItemsObserver(observableList, listView.getItems());
                }
            };
            this.listView.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
            if (listView.getItems() != null) {
                this.listView.getItems().addListener(this.weakItemsContentListener);
            }
            this.updateItemCount();
            if (this.itemCount > 0) {
                this.focus(0);
            }
        }

        private void updateItemsObserver(ObservableList<T> observableList, ObservableList<T> observableList2) {
            if (observableList != null) {
                observableList.removeListener(this.weakItemsContentListener);
            }
            if (observableList2 != null) {
                observableList2.addListener(this.weakItemsContentListener);
            }
            this.updateItemCount();
        }

        @Override
        protected int getItemCount() {
            return this.itemCount;
        }

        @Override
        protected T getModelItem(int n2) {
            if (this.isEmpty()) {
                return null;
            }
            if (n2 < 0 || n2 >= this.itemCount) {
                return null;
            }
            return (T)this.listView.getItems().get(n2);
        }

        private boolean isEmpty() {
            return this.itemCount == -1;
        }

        private void updateItemCount() {
            ObservableList<T> observableList;
            this.itemCount = this.listView == null ? -1 : ((observableList = this.listView.getItems()) == null ? -1 : observableList.size());
        }
    }

    static class ListViewBitSetSelectionModel<T>
    extends MultipleSelectionModelBase<T> {
        private final ListChangeListener<T> itemsContentObserver = new ListChangeListener<T>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends T> change) {
                this.updateItemCount();
                while (change.next()) {
                    Object t2;
                    int n2;
                    Object t3 = this.getSelectedItem();
                    int n3 = this.getSelectedIndex();
                    if (listView.getItems() == null || listView.getItems().isEmpty()) {
                        selectedItemChange = change;
                        this.clearSelection();
                        selectedItemChange = null;
                        continue;
                    }
                    if (n3 == -1 && t3 != null) {
                        n2 = listView.getItems().indexOf(t3);
                        if (n2 == -1) continue;
                        this.setSelectedIndex(n2);
                        continue;
                    }
                    if (!change.wasRemoved() || change.getRemovedSize() != 1 || change.wasAdded() || t3 == null || !t3.equals(change.getRemoved().get(0)) || this.getSelectedIndex() >= this.getItemCount() || t3.equals(t2 = this.getModelItem(n2 = n3 == 0 ? 0 : n3 - 1))) continue;
                    this.startAtomic();
                    this.clearSelection(n3);
                    this.stopAtomic();
                    this.select(t2);
                }
                this.updateSelection(change);
            }
        };
        private final InvalidationListener itemsObserver;
        private WeakListChangeListener<T> weakItemsContentObserver = new WeakListChangeListener<T>(this.itemsContentObserver);
        private final ListView<T> listView;
        private int itemCount = 0;
        private int previousModelSize = 0;

        public ListViewBitSetSelectionModel(final ListView<T> listView) {
            if (listView == null) {
                throw new IllegalArgumentException("ListView can not be null");
            }
            this.listView = listView;
            this.itemsObserver = new InvalidationListener(){
                private WeakReference<ObservableList<T>> weakItemsRef;
                {
                    this.weakItemsRef = new WeakReference(listView.getItems());
                }

                @Override
                public void invalidated(Observable observable) {
                    ObservableList observableList = (ObservableList)this.weakItemsRef.get();
                    this.weakItemsRef = new WeakReference(listView.getItems());
                    this.updateItemsObserver(observableList, listView.getItems());
                }
            };
            this.listView.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
            if (listView.getItems() != null) {
                this.listView.getItems().addListener(this.weakItemsContentObserver);
            }
            this.updateItemCount();
            this.updateDefaultSelection();
        }

        private void updateSelection(ListChangeListener.Change<? extends T> change) {
            change.reset();
            int n2 = 0;
            while (change.next()) {
                int n3;
                int n4;
                if (change.wasReplaced()) {
                    if (change.getList().isEmpty()) {
                        this.clearSelection();
                        continue;
                    }
                    n4 = this.getSelectedIndex();
                    if (this.previousModelSize == change.getRemovedSize()) {
                        this.clearSelection();
                        continue;
                    }
                    if (n4 < this.getItemCount() && n4 >= 0) {
                        this.startAtomic();
                        this.clearSelection(n4);
                        this.stopAtomic();
                        this.select(n4);
                        continue;
                    }
                    this.clearSelection();
                    continue;
                }
                if (change.wasAdded() || change.wasRemoved()) {
                    n2 += change.wasAdded() ? change.getAddedSize() : -change.getRemovedSize();
                    continue;
                }
                if (!change.wasPermutated()) continue;
                n4 = change.getTo() - change.getFrom();
                HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>(n4);
                for (int i2 = change.getFrom(); i2 < change.getTo(); ++i2) {
                    hashMap.put(i2, change.getPermutation(i2));
                }
                ArrayList<Integer> arrayList = new ArrayList<Integer>(this.getSelectedIndices());
                this.clearSelection();
                ArrayList<Integer> arrayList2 = new ArrayList<Integer>(this.getSelectedIndices().size());
                for (int i3 = 0; i3 < arrayList.size(); ++i3) {
                    n3 = (Integer)arrayList.get(i3);
                    if (!hashMap.containsKey(n3)) continue;
                    Integer n5 = (Integer)hashMap.get(n3);
                    arrayList2.add(n5);
                }
                if (arrayList2.isEmpty()) continue;
                if (arrayList2.size() == 1) {
                    this.select((Integer)arrayList2.get(0));
                    continue;
                }
                int[] arrn = new int[arrayList2.size() - 1];
                for (n3 = 0; n3 < arrayList2.size() - 1; ++n3) {
                    arrn[n3] = (Integer)arrayList2.get(n3 + 1);
                }
                this.selectIndices((Integer)arrayList2.get(0), arrn);
            }
            if (n2 != 0) {
                this.shiftSelection(change.getFrom(), n2, null);
            }
            this.previousModelSize = this.getItemCount();
        }

        @Override
        public void selectAll() {
            int n2 = ListCellBehavior.getAnchor(this.listView, -1);
            super.selectAll();
            ListCellBehavior.setAnchor(this.listView, n2, false);
        }

        @Override
        public void clearAndSelect(int n2) {
            ListCellBehavior.setAnchor(this.listView, n2, false);
            super.clearAndSelect(n2);
        }

        @Override
        protected void focus(int n2) {
            if (this.listView.getFocusModel() == null) {
                return;
            }
            this.listView.getFocusModel().focus(n2);
            this.listView.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        @Override
        protected int getFocusedIndex() {
            if (this.listView.getFocusModel() == null) {
                return -1;
            }
            return this.listView.getFocusModel().getFocusedIndex();
        }

        @Override
        protected int getItemCount() {
            return this.itemCount;
        }

        @Override
        protected T getModelItem(int n2) {
            ObservableList<T> observableList = this.listView.getItems();
            if (observableList == null) {
                return null;
            }
            if (n2 < 0 || n2 >= this.itemCount) {
                return null;
            }
            return (T)observableList.get(n2);
        }

        private void updateItemCount() {
            ObservableList<T> observableList;
            this.itemCount = this.listView == null ? -1 : ((observableList = this.listView.getItems()) == null ? -1 : observableList.size());
        }

        private void updateItemsObserver(ObservableList<T> observableList, ObservableList<T> observableList2) {
            if (observableList != null) {
                observableList.removeListener(this.weakItemsContentObserver);
            }
            if (observableList2 != null) {
                observableList2.addListener(this.weakItemsContentObserver);
            }
            this.updateItemCount();
            this.updateDefaultSelection();
        }

        private void updateDefaultSelection() {
            int n2 = -1;
            int n3 = -1;
            if (this.listView.getItems() != null) {
                Object t2 = this.getSelectedItem();
                if (t2 != null) {
                    n3 = n2 = this.listView.getItems().indexOf(t2);
                }
                if (((ListView)this.listView).selectFirstRowByDefault && n3 == -1) {
                    n3 = this.listView.getItems().size() > 0 ? 0 : -1;
                }
            }
            this.clearSelection();
            this.select(n2);
            this.focus(n3);
        }
    }

    public static class EditEvent<T>
    extends Event {
        private final T newValue;
        private final int editIndex;
        private static final long serialVersionUID = 20130724L;
        public static final EventType<?> ANY = ListView.access$1100();

        public EditEvent(ListView<T> listView, EventType<? extends EditEvent<T>> eventType, T t2, int n2) {
            super(listView, Event.NULL_SOURCE_TARGET, eventType);
            this.editIndex = n2;
            this.newValue = t2;
        }

        @Override
        public ListView<T> getSource() {
            return (ListView)super.getSource();
        }

        public int getIndex() {
            return this.editIndex;
        }

        public T getNewValue() {
            return this.newValue;
        }

        @Override
        public String toString() {
            return "ListViewEditEvent [ newValue: " + this.getNewValue() + ", ListView: " + this.getSource() + " ]";
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<ListView<?>, Orientation> ORIENTATION = new CssMetaData<ListView<?>, Orientation>("-fx-orientation", new EnumConverter<Orientation>(Orientation.class), Orientation.VERTICAL){

            @Override
            public Orientation getInitialValue(ListView<?> listView) {
                return listView.getOrientation();
            }

            @Override
            public boolean isSettable(ListView<?> listView) {
                return ((ListView)listView).orientation == null || !((ListView)listView).orientation.isBound();
            }

            @Override
            public StyleableProperty<Orientation> getStyleableProperty(ListView<?> listView) {
                return (StyleableProperty)((Object)listView.orientationProperty());
            }
        };
        private static final CssMetaData<ListView<?>, Number> FIXED_CELL_SIZE = new CssMetaData<ListView<?>, Number>("-fx-fixed-cell-size", SizeConverter.getInstance(), -1.0){

            @Override
            public Double getInitialValue(ListView<?> listView) {
                return listView.getFixedCellSize();
            }

            @Override
            public boolean isSettable(ListView<?> listView) {
                return ((ListView)listView).fixedCellSize == null || !((ListView)listView).fixedCellSize.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ListView<?> listView) {
                return (StyleableProperty)((Object)listView.fixedCellSizeProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Control.getClassCssMetaData());
            arrayList.add(ORIENTATION);
            arrayList.add(FIXED_CELL_SIZE);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

