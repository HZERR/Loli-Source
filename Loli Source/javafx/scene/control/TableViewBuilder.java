/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
public class TableViewBuilder<S, B extends TableViewBuilder<S, B>>
extends ControlBuilder<B>
implements Builder<TableView<S>> {
    private int __set;
    private Callback<TableView.ResizeFeatures, Boolean> columnResizePolicy;
    private Collection<? extends TableColumn<S, ?>> columns;
    private boolean editable;
    private TableView.TableViewFocusModel<S> focusModel;
    private ObservableList<S> items;
    private Node placeholder;
    private Callback<TableView<S>, TableRow<S>> rowFactory;
    private TableView.TableViewSelectionModel<S> selectionModel;
    private Collection<? extends TableColumn<S, ?>> sortOrder;
    private boolean tableMenuButtonVisible;

    protected TableViewBuilder() {
    }

    public static <S> TableViewBuilder<S, ?> create() {
        return new TableViewBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(TableView<S> tableView) {
        super.applyTo(tableView);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    tableView.setColumnResizePolicy(this.columnResizePolicy);
                    break;
                }
                case 1: {
                    tableView.getColumns().addAll(this.columns);
                    break;
                }
                case 2: {
                    tableView.setEditable(this.editable);
                    break;
                }
                case 3: {
                    tableView.setFocusModel(this.focusModel);
                    break;
                }
                case 4: {
                    tableView.setItems(this.items);
                    break;
                }
                case 5: {
                    tableView.setPlaceholder(this.placeholder);
                    break;
                }
                case 6: {
                    tableView.setRowFactory(this.rowFactory);
                    break;
                }
                case 7: {
                    tableView.setSelectionModel(this.selectionModel);
                    break;
                }
                case 8: {
                    tableView.getSortOrder().addAll(this.sortOrder);
                    break;
                }
                case 9: {
                    tableView.setTableMenuButtonVisible(this.tableMenuButtonVisible);
                }
            }
        }
    }

    public B columnResizePolicy(Callback<TableView.ResizeFeatures, Boolean> callback) {
        this.columnResizePolicy = callback;
        this.__set(0);
        return (B)this;
    }

    public B columns(Collection<? extends TableColumn<S, ?>> collection) {
        this.columns = collection;
        this.__set(1);
        return (B)this;
    }

    public B columns(TableColumn<S, ?> ... arrtableColumn) {
        return this.columns(Arrays.asList(arrtableColumn));
    }

    public B editable(boolean bl) {
        this.editable = bl;
        this.__set(2);
        return (B)this;
    }

    public B focusModel(TableView.TableViewFocusModel<S> tableViewFocusModel) {
        this.focusModel = tableViewFocusModel;
        this.__set(3);
        return (B)this;
    }

    public B items(ObservableList<S> observableList) {
        this.items = observableList;
        this.__set(4);
        return (B)this;
    }

    public B placeholder(Node node) {
        this.placeholder = node;
        this.__set(5);
        return (B)this;
    }

    public B rowFactory(Callback<TableView<S>, TableRow<S>> callback) {
        this.rowFactory = callback;
        this.__set(6);
        return (B)this;
    }

    public B selectionModel(TableView.TableViewSelectionModel<S> tableViewSelectionModel) {
        this.selectionModel = tableViewSelectionModel;
        this.__set(7);
        return (B)this;
    }

    public B sortOrder(Collection<? extends TableColumn<S, ?>> collection) {
        this.sortOrder = collection;
        this.__set(8);
        return (B)this;
    }

    public B sortOrder(TableColumn<S, ?> ... arrtableColumn) {
        return this.sortOrder(Arrays.asList(arrtableColumn));
    }

    public B tableMenuButtonVisible(boolean bl) {
        this.tableMenuButtonVisible = bl;
        this.__set(9);
        return (B)this;
    }

    @Override
    public TableView<S> build() {
        TableView tableView = new TableView();
        this.applyTo(tableView);
        return tableView;
    }
}

