/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
public class ListViewBuilder<T, B extends ListViewBuilder<T, B>>
extends ControlBuilder<B>
implements Builder<ListView<T>> {
    private int __set;
    private Callback<ListView<T>, ListCell<T>> cellFactory;
    private boolean editable;
    private FocusModel<T> focusModel;
    private ObservableList<T> items;
    private EventHandler<ListView.EditEvent<T>> onEditCancel;
    private EventHandler<ListView.EditEvent<T>> onEditCommit;
    private EventHandler<ListView.EditEvent<T>> onEditStart;
    private Orientation orientation;
    private MultipleSelectionModel<T> selectionModel;

    protected ListViewBuilder() {
    }

    public static <T> ListViewBuilder<T, ?> create() {
        return new ListViewBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(ListView<T> listView) {
        super.applyTo(listView);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    listView.setCellFactory(this.cellFactory);
                    break;
                }
                case 1: {
                    listView.setEditable(this.editable);
                    break;
                }
                case 2: {
                    listView.setFocusModel(this.focusModel);
                    break;
                }
                case 3: {
                    listView.setItems(this.items);
                    break;
                }
                case 4: {
                    listView.setOnEditCancel(this.onEditCancel);
                    break;
                }
                case 5: {
                    listView.setOnEditCommit(this.onEditCommit);
                    break;
                }
                case 6: {
                    listView.setOnEditStart(this.onEditStart);
                    break;
                }
                case 7: {
                    listView.setOrientation(this.orientation);
                    break;
                }
                case 8: {
                    listView.setSelectionModel(this.selectionModel);
                }
            }
        }
    }

    public B cellFactory(Callback<ListView<T>, ListCell<T>> callback) {
        this.cellFactory = callback;
        this.__set(0);
        return (B)this;
    }

    public B editable(boolean bl) {
        this.editable = bl;
        this.__set(1);
        return (B)this;
    }

    public B focusModel(FocusModel<T> focusModel) {
        this.focusModel = focusModel;
        this.__set(2);
        return (B)this;
    }

    public B items(ObservableList<T> observableList) {
        this.items = observableList;
        this.__set(3);
        return (B)this;
    }

    public B onEditCancel(EventHandler<ListView.EditEvent<T>> eventHandler) {
        this.onEditCancel = eventHandler;
        this.__set(4);
        return (B)this;
    }

    public B onEditCommit(EventHandler<ListView.EditEvent<T>> eventHandler) {
        this.onEditCommit = eventHandler;
        this.__set(5);
        return (B)this;
    }

    public B onEditStart(EventHandler<ListView.EditEvent<T>> eventHandler) {
        this.onEditStart = eventHandler;
        this.__set(6);
        return (B)this;
    }

    public B orientation(Orientation orientation) {
        this.orientation = orientation;
        this.__set(7);
        return (B)this;
    }

    public B selectionModel(MultipleSelectionModel<T> multipleSelectionModel) {
        this.selectionModel = multipleSelectionModel;
        this.__set(8);
        return (B)this;
    }

    @Override
    public ListView<T> build() {
        ListView listView = new ListView();
        this.applyTo(listView);
        return listView;
    }
}

