/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxListViewBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.ListViewSkin;
import com.sun.javafx.scene.control.skin.VirtualContainerBase;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBoxListViewSkin<T>
extends ComboBoxPopupControl<T> {
    private static final String COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY = "comboBoxRowsToMeasureWidth";
    private final ComboBox<T> comboBox;
    private ObservableList<T> comboBoxItems;
    private ListCell<T> buttonCell;
    private Callback<ListView<T>, ListCell<T>> cellFactory;
    private final ListView<T> listView;
    private ObservableList<T> listViewItems;
    private boolean listSelectionLock = false;
    private boolean listViewSelectionDirty = false;
    private boolean itemCountDirty;
    private final ListChangeListener<T> listViewItemsListener = new ListChangeListener<T>(){

        @Override
        public void onChanged(ListChangeListener.Change<? extends T> change) {
            ComboBoxListViewSkin.this.itemCountDirty = true;
            ((ComboBoxBase)ComboBoxListViewSkin.this.getSkinnable()).requestLayout();
        }
    };
    private final InvalidationListener itemsObserver;
    private final WeakListChangeListener<T> weakListViewItemsListener = new WeakListChangeListener<T>(this.listViewItemsListener);
    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");
    private static final PseudoClass PSEUDO_CLASS_EMPTY = PseudoClass.getPseudoClass("empty");
    private static final PseudoClass PSEUDO_CLASS_FILLED = PseudoClass.getPseudoClass("filled");

    public ComboBoxListViewSkin(ComboBox<T> comboBox) {
        super(comboBox, new ComboBoxListViewBehavior<T>(comboBox));
        this.comboBox = comboBox;
        this.updateComboBoxItems();
        this.itemsObserver = observable -> {
            this.updateComboBoxItems();
            this.updateListViewItems();
        };
        this.comboBox.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
        this.listView = this.createListView();
        this.listView.setManaged(false);
        this.getChildren().add(this.listView);
        this.updateListViewItems();
        this.updateCellFactory();
        this.updateButtonCell();
        this.updateValue();
        this.registerChangeListener(comboBox.itemsProperty(), "ITEMS");
        this.registerChangeListener(comboBox.promptTextProperty(), "PROMPT_TEXT");
        this.registerChangeListener(comboBox.cellFactoryProperty(), "CELL_FACTORY");
        this.registerChangeListener(comboBox.visibleRowCountProperty(), "VISIBLE_ROW_COUNT");
        this.registerChangeListener(comboBox.converterProperty(), "CONVERTER");
        this.registerChangeListener(comboBox.buttonCellProperty(), "BUTTON_CELL");
        this.registerChangeListener(comboBox.valueProperty(), "VALUE");
        this.registerChangeListener(comboBox.editableProperty(), "EDITABLE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ITEMS".equals(string)) {
            this.updateComboBoxItems();
            this.updateListViewItems();
        } else if ("PROMPT_TEXT".equals(string)) {
            this.updateDisplayNode();
        } else if ("CELL_FACTORY".equals(string)) {
            this.updateCellFactory();
        } else if ("VISIBLE_ROW_COUNT".equals(string)) {
            if (this.listView == null) {
                return;
            }
            this.listView.requestLayout();
        } else if ("CONVERTER".equals(string)) {
            this.updateListViewItems();
        } else if ("EDITOR".equals(string)) {
            this.getEditableInputNode();
        } else if ("BUTTON_CELL".equals(string)) {
            this.updateButtonCell();
        } else if ("VALUE".equals(string)) {
            this.updateValue();
            this.comboBox.fireEvent(new ActionEvent());
        } else if ("EDITABLE".equals(string)) {
            this.updateEditable();
        }
    }

    @Override
    protected TextField getEditor() {
        return ((ComboBoxBase)this.getSkinnable()).isEditable() ? ((ComboBox)this.getSkinnable()).getEditor() : null;
    }

    @Override
    protected StringConverter<T> getConverter() {
        return ((ComboBox)this.getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {
        Control control = this.comboBox.isEditable() ? this.getEditableInputNode() : this.buttonCell;
        this.updateDisplayNode();
        return control;
    }

    public void updateComboBoxItems() {
        this.comboBoxItems = this.comboBox.getItems();
        this.comboBoxItems = this.comboBoxItems == null ? FXCollections.emptyObservableList() : this.comboBoxItems;
    }

    public void updateListViewItems() {
        if (this.listViewItems != null) {
            this.listViewItems.removeListener(this.weakListViewItemsListener);
        }
        this.listViewItems = this.comboBoxItems;
        this.listView.setItems(this.listViewItems);
        if (this.listViewItems != null) {
            this.listViewItems.addListener(this.weakListViewItemsListener);
        }
        this.itemCountDirty = true;
        ((ComboBoxBase)this.getSkinnable()).requestLayout();
    }

    @Override
    public Node getPopupContent() {
        return this.listView;
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        this.reconfigurePopup();
        return 50.0;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = super.computePrefWidth(d2, d3, d4, d5, d6);
        double d8 = this.listView.prefWidth(d2);
        double d9 = Math.max(d7, d8);
        this.reconfigurePopup();
        return d9;
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        this.reconfigurePopup();
        return super.computeMaxWidth(d2, d3, d4, d5, d6);
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        this.reconfigurePopup();
        return super.computeMinHeight(d2, d3, d4, d5, d6);
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        this.reconfigurePopup();
        return super.computePrefHeight(d2, d3, d4, d5, d6);
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        this.reconfigurePopup();
        return super.computeMaxHeight(d2, d3, d4, d5, d6);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        if (this.listViewSelectionDirty) {
            try {
                this.listSelectionLock = true;
                Object t2 = this.comboBox.getSelectionModel().getSelectedItem();
                this.listView.getSelectionModel().clearSelection();
                this.listView.getSelectionModel().select(t2);
            }
            finally {
                this.listSelectionLock = false;
                this.listViewSelectionDirty = false;
            }
        }
        super.layoutChildren(d2, d3, d4, d5);
    }

    protected boolean isHideOnClickEnabled() {
        return true;
    }

    private void updateValue() {
        Object t2 = this.comboBox.getValue();
        MultipleSelectionModel multipleSelectionModel = this.listView.getSelectionModel();
        if (t2 == null) {
            multipleSelectionModel.clearSelection();
        } else {
            int n2 = this.getIndexOfComboBoxValueInItemsList();
            if (n2 == -1) {
                this.listSelectionLock = true;
                multipleSelectionModel.clearSelection();
                this.listSelectionLock = false;
            } else {
                int n3 = this.comboBox.getSelectionModel().getSelectedIndex();
                if (n3 >= 0 && n3 < this.comboBoxItems.size()) {
                    Object e2 = this.comboBoxItems.get(n3);
                    if (e2 != null && e2.equals(t2)) {
                        multipleSelectionModel.select(n3);
                    } else {
                        multipleSelectionModel.select(t2);
                    }
                } else {
                    int n4 = this.comboBoxItems.indexOf(t2);
                    if (n4 == -1) {
                        this.updateDisplayNode();
                    } else {
                        multipleSelectionModel.select(n4);
                    }
                }
            }
        }
    }

    @Override
    protected void updateDisplayNode() {
        if (this.getEditor() != null) {
            super.updateDisplayNode();
        } else {
            Object t2 = this.comboBox.getValue();
            int n2 = this.getIndexOfComboBoxValueInItemsList();
            if (n2 > -1) {
                this.buttonCell.setItem(null);
                this.buttonCell.updateIndex(n2);
            } else {
                this.buttonCell.updateIndex(-1);
                boolean bl = this.updateDisplayText(this.buttonCell, t2, false);
                this.buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_EMPTY, bl);
                this.buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_FILLED, !bl);
                this.buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
            }
        }
    }

    private boolean updateDisplayText(ListCell<T> listCell, T t2, boolean bl) {
        if (bl) {
            if (listCell == null) {
                return true;
            }
            listCell.setGraphic(null);
            listCell.setText(null);
            return true;
        }
        if (t2 instanceof Node) {
            Node node = listCell.getGraphic();
            Node node2 = (Node)t2;
            if (node == null || !node.equals(node2)) {
                listCell.setText(null);
                listCell.setGraphic(node2);
            }
            return node2 == null;
        }
        StringConverter<T> stringConverter = this.comboBox.getConverter();
        String string = t2 == null ? this.comboBox.getPromptText() : (stringConverter == null ? t2.toString() : stringConverter.toString(t2));
        listCell.setText(string);
        listCell.setGraphic(null);
        return string == null || string.isEmpty();
    }

    private int getIndexOfComboBoxValueInItemsList() {
        Object t2 = this.comboBox.getValue();
        int n2 = this.comboBoxItems.indexOf(t2);
        return n2;
    }

    private void updateButtonCell() {
        this.buttonCell = this.comboBox.getButtonCell() != null ? this.comboBox.getButtonCell() : this.getDefaultCellFactory().call(this.listView);
        this.buttonCell.setMouseTransparent(true);
        this.buttonCell.updateListView(this.listView);
        this.updateDisplayArea();
        this.buttonCell.setAccessibleRole(AccessibleRole.NODE);
    }

    private void updateCellFactory() {
        Callback<ListView<T>, ListCell<T>> callback = this.comboBox.getCellFactory();
        this.cellFactory = callback != null ? callback : this.getDefaultCellFactory();
        this.listView.setCellFactory(this.cellFactory);
    }

    private Callback<ListView<T>, ListCell<T>> getDefaultCellFactory() {
        return new Callback<ListView<T>, ListCell<T>>(){

            @Override
            public ListCell<T> call(ListView<T> listView) {
                return new ListCell<T>(){

                    @Override
                    public void updateItem(T t2, boolean bl) {
                        super.updateItem(t2, bl);
                        ComboBoxListViewSkin.this.updateDisplayText(this, t2, bl);
                    }
                };
            }
        };
    }

    private ListView<T> createListView() {
        ListView listView = new ListView<T>(){
            {
                this.getProperties().put("selectFirstRowByDefault", false);
            }

            @Override
            protected double computeMinHeight(double d2) {
                return 30.0;
            }

            @Override
            protected double computePrefWidth(double d2) {
                double d3;
                if (this.getSkin() instanceof ListViewSkin) {
                    ListViewSkin listViewSkin = (ListViewSkin)this.getSkin();
                    if (ComboBoxListViewSkin.this.itemCountDirty) {
                        listViewSkin.updateRowCount();
                        ComboBoxListViewSkin.this.itemCountDirty = false;
                    }
                    int n2 = -1;
                    if (ComboBoxListViewSkin.this.comboBox.getProperties().containsKey(ComboBoxListViewSkin.COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY)) {
                        n2 = (Integer)ComboBoxListViewSkin.this.comboBox.getProperties().get(ComboBoxListViewSkin.COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY);
                    }
                    d3 = Math.max(ComboBoxListViewSkin.this.comboBox.getWidth(), listViewSkin.getMaxCellWidth(n2) + 30.0);
                } else {
                    d3 = Math.max(100.0, ComboBoxListViewSkin.this.comboBox.getWidth());
                }
                if (this.getItems().isEmpty() && this.getPlaceholder() != null) {
                    d3 = Math.max(super.computePrefWidth(d2), d3);
                }
                return Math.max(50.0, d3);
            }

            @Override
            protected double computePrefHeight(double d2) {
                return ComboBoxListViewSkin.this.getListViewPrefHeight();
            }
        };
        listView.setId("list-view");
        listView.placeholderProperty().bind(this.comboBox.placeholderProperty());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setFocusTraversable(false);
        listView.getSelectionModel().selectedIndexProperty().addListener(observable -> {
            if (this.listSelectionLock) {
                return;
            }
            int n2 = this.listView.getSelectionModel().getSelectedIndex();
            this.comboBox.getSelectionModel().select(n2);
            this.updateDisplayNode();
            this.comboBox.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
        });
        this.comboBox.getSelectionModel().selectedItemProperty().addListener(observable -> {
            this.listViewSelectionDirty = true;
        });
        listView.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            ObservableList<String> observableList;
            EventTarget eventTarget = mouseEvent.getTarget();
            if (eventTarget instanceof Parent && ((observableList = ((Parent)eventTarget).getStyleClass()).contains("thumb") || observableList.contains("track") || observableList.contains("decrement-arrow") || observableList.contains("increment-arrow"))) {
                return;
            }
            if (this.isHideOnClickEnabled()) {
                this.comboBox.hide();
            }
        });
        listView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.ESCAPE) {
                this.comboBox.hide();
            }
        });
        return listView;
    }

    private double getListViewPrefHeight() {
        double d2;
        if (this.listView.getSkin() instanceof VirtualContainerBase) {
            int n2 = this.comboBox.getVisibleRowCount();
            VirtualContainerBase virtualContainerBase = (VirtualContainerBase)this.listView.getSkin();
            d2 = virtualContainerBase.getVirtualFlowPreferredHeight(n2);
        } else {
            double d3 = this.comboBoxItems.size() * 25;
            d2 = Math.min(d3, 200.0);
        }
        return d2;
    }

    public ListView<T> getListView() {
        return this.listView;
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                if (this.comboBox.isShowing()) {
                    return this.listView.queryAccessibleAttribute(accessibleAttribute, arrobject);
                }
                return null;
            }
            case TEXT: {
                String string;
                String string2 = this.comboBox.getAccessibleText();
                if (string2 != null && !string2.isEmpty()) {
                    return string2;
                }
                String string3 = string = this.comboBox.isEditable() ? this.getEditor().getText() : this.buttonCell.getText();
                if (string == null || string.isEmpty()) {
                    string = this.comboBox.getPromptText();
                }
                return string;
            }
            case SELECTION_START: {
                return this.getEditor().getSelection().getStart();
            }
            case SELECTION_END: {
                return this.getEditor().getSelection().getEnd();
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

