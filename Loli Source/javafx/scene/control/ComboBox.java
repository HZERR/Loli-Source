/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBox<T>
extends ComboBoxBase<T> {
    private ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<ObservableList<T>>(this, "items");
    private ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty<StringConverter<StringConverter<T>>>(this, "converter", ComboBox.defaultStringConverter());
    private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory = new SimpleObjectProperty<Callback<ListView<T>, ListCell<T>>>(this, "cellFactory");
    private ObjectProperty<ListCell<T>> buttonCell = new SimpleObjectProperty<ListCell<T>>(this, "buttonCell");
    private ObjectProperty<SingleSelectionModel<T>> selectionModel = new SimpleObjectProperty<SingleSelectionModel<T>>(this, "selectionModel"){
        private SingleSelectionModel<T> oldSM;
        {
            this.oldSM = null;
        }

        @Override
        protected void invalidated() {
            SingleSelectionModel singleSelectionModel;
            if (this.oldSM != null) {
                this.oldSM.selectedItemProperty().removeListener(ComboBox.this.selectedItemListener);
            }
            this.oldSM = singleSelectionModel = (SingleSelectionModel)this.get();
            if (singleSelectionModel != null) {
                singleSelectionModel.selectedItemProperty().addListener(ComboBox.this.selectedItemListener);
            }
        }
    };
    private IntegerProperty visibleRowCount = new SimpleIntegerProperty(this, "visibleRowCount", 10);
    private TextField textField;
    private ReadOnlyObjectWrapper<TextField> editor;
    private ObjectProperty<Node> placeholder;
    private ChangeListener<T> selectedItemListener = new ChangeListener<T>(){

        @Override
        public void changed(ObservableValue<? extends T> observableValue, T t2, T t3) {
            if (!ComboBox.this.wasSetAllCalled || t3 != null) {
                ComboBox.this.updateValue(t3);
            }
            ComboBox.this.wasSetAllCalled = false;
        }
    };
    private static final String DEFAULT_STYLE_CLASS = "combo-box";
    private boolean wasSetAllCalled = false;
    private int previousItemCount = -1;

    private static <T> StringConverter<T> defaultStringConverter() {
        return new StringConverter<T>(){

            @Override
            public String toString(T t2) {
                return t2 == null ? null : t2.toString();
            }

            @Override
            public T fromString(String string) {
                return string;
            }
        };
    }

    public ComboBox() {
        this(FXCollections.observableArrayList());
    }

    public ComboBox(ObservableList<T> observableList) {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setAccessibleRole(AccessibleRole.COMBO_BOX);
        this.setItems(observableList);
        this.setSelectionModel(new ComboBoxSelectionModel(this));
        this.valueProperty().addListener((observableValue, object, object2) -> {
            if (this.getItems() == null) {
                return;
            }
            SingleSelectionModel<Object> singleSelectionModel = this.getSelectionModel();
            int n2 = this.getItems().indexOf(object2);
            if (n2 == -1) {
                singleSelectionModel.setSelectedItem(object2);
            } else {
                Object t2 = singleSelectionModel.getSelectedItem();
                if (t2 == null || !t2.equals(this.getValue())) {
                    ((SelectionModel)singleSelectionModel).clearAndSelect(n2);
                }
            }
        });
        this.editableProperty().addListener(observable -> this.getSelectionModel().clearSelection());
    }

    public final void setItems(ObservableList<T> observableList) {
        this.itemsProperty().set(observableList);
    }

    public final ObservableList<T> getItems() {
        return (ObservableList)this.items.get();
    }

    public ObjectProperty<ObservableList<T>> itemsProperty() {
        return this.items;
    }

    public ObjectProperty<StringConverter<T>> converterProperty() {
        return this.converter;
    }

    public final void setConverter(StringConverter<T> stringConverter) {
        this.converterProperty().set(stringConverter);
    }

    public final StringConverter<T> getConverter() {
        return (StringConverter)this.converterProperty().get();
    }

    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> callback) {
        this.cellFactoryProperty().set(callback);
    }

    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return (Callback)this.cellFactoryProperty().get();
    }

    public ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        return this.cellFactory;
    }

    public ObjectProperty<ListCell<T>> buttonCellProperty() {
        return this.buttonCell;
    }

    public final void setButtonCell(ListCell<T> listCell) {
        this.buttonCellProperty().set(listCell);
    }

    public final ListCell<T> getButtonCell() {
        return (ListCell)this.buttonCellProperty().get();
    }

    public final void setSelectionModel(SingleSelectionModel<T> singleSelectionModel) {
        this.selectionModel.set(singleSelectionModel);
    }

    public final SingleSelectionModel<T> getSelectionModel() {
        return (SingleSelectionModel)this.selectionModel.get();
    }

    public final ObjectProperty<SingleSelectionModel<T>> selectionModelProperty() {
        return this.selectionModel;
    }

    public final void setVisibleRowCount(int n2) {
        this.visibleRowCount.set(n2);
    }

    public final int getVisibleRowCount() {
        return this.visibleRowCount.get();
    }

    public final IntegerProperty visibleRowCountProperty() {
        return this.visibleRowCount;
    }

    public final TextField getEditor() {
        return (TextField)this.editorProperty().get();
    }

    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (this.editor == null) {
            this.editor = new ReadOnlyObjectWrapper(this, "editor");
            this.textField = new ComboBoxPopupControl.FakeFocusTextField();
            this.editor.set(this.textField);
        }
        return this.editor.getReadOnlyProperty();
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

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ComboBoxListViewSkin(this);
    }

    private void updateValue(T t2) {
        if (!this.valueProperty().isBound()) {
            this.setValue(t2);
        }
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case TEXT: {
                String string = this.getAccessibleText();
                if (string != null && !string.isEmpty()) {
                    return string;
                }
                Object object = super.queryAccessibleAttribute(accessibleAttribute, arrobject);
                if (object != null) {
                    return object;
                }
                StringConverter stringConverter = this.getConverter();
                if (stringConverter == null) {
                    return this.getValue() != null ? this.getValue().toString() : "";
                }
                return stringConverter.toString(this.getValue());
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    static class ComboBoxSelectionModel<T>
    extends SingleSelectionModel<T> {
        private final ComboBox<T> comboBox;
        private final ListChangeListener<T> itemsContentObserver = new ListChangeListener<T>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends T> change) {
                int n2;
                if (comboBox.getItems() == null || comboBox.getItems().isEmpty()) {
                    this.setSelectedIndex(-1);
                } else if (this.getSelectedIndex() == -1 && this.getSelectedItem() != null && (n2 = comboBox.getItems().indexOf(this.getSelectedItem())) != -1) {
                    this.setSelectedIndex(n2);
                }
                n2 = 0;
                while (change.next()) {
                    comboBox.wasSetAllCalled = comboBox.previousItemCount == change.getRemovedSize();
                    if (change.wasReplaced() || !change.wasAdded() && !change.wasRemoved() || change.getFrom() > this.getSelectedIndex() || this.getSelectedIndex() == -1) continue;
                    n2 += change.wasAdded() ? change.getAddedSize() : -change.getRemovedSize();
                }
                if (n2 != 0) {
                    this.clearAndSelect(this.getSelectedIndex() + n2);
                }
                comboBox.previousItemCount = this.getItemCount();
            }
        };
        private final InvalidationListener itemsObserver;
        private WeakListChangeListener<T> weakItemsContentObserver = new WeakListChangeListener<T>(this.itemsContentObserver);

        public ComboBoxSelectionModel(ComboBox<T> comboBox) {
            if (comboBox == null) {
                throw new NullPointerException("ComboBox can not be null");
            }
            this.comboBox = comboBox;
            this.selectedIndexProperty().addListener(observable -> this.setSelectedItem(this.getModelItem(this.getSelectedIndex())));
            this.itemsObserver = new InvalidationListener(){
                private WeakReference<ObservableList<T>> weakItemsRef;
                {
                    this.weakItemsRef = new WeakReference(comboBox.getItems());
                }

                @Override
                public void invalidated(Observable observable) {
                    ObservableList observableList = (ObservableList)this.weakItemsRef.get();
                    this.weakItemsRef = new WeakReference(comboBox.getItems());
                    this.updateItemsObserver(observableList, comboBox.getItems());
                }
            };
            this.comboBox.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
            if (this.comboBox.getItems() != null) {
                this.comboBox.getItems().addListener(this.weakItemsContentObserver);
            }
        }

        private void updateItemsObserver(ObservableList<T> observableList, ObservableList<T> observableList2) {
            Object t2;
            if (observableList != null) {
                observableList.removeListener(this.weakItemsContentObserver);
            }
            if (observableList2 != null) {
                observableList2.addListener(this.weakItemsContentObserver);
            }
            int n2 = -1;
            if (observableList2 != null && (t2 = this.comboBox.getValue()) != null) {
                n2 = observableList2.indexOf(t2);
            }
            this.setSelectedIndex(n2);
        }

        @Override
        protected T getModelItem(int n2) {
            ObservableList<T> observableList = this.comboBox.getItems();
            if (observableList == null) {
                return null;
            }
            if (n2 < 0 || n2 >= observableList.size()) {
                return null;
            }
            return (T)observableList.get(n2);
        }

        @Override
        protected int getItemCount() {
            ObservableList<T> observableList = this.comboBox.getItems();
            return observableList == null ? 0 : observableList.size();
        }
    }
}

