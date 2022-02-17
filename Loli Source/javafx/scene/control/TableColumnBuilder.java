/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
public class TableColumnBuilder<S, T, B extends TableColumnBuilder<S, T, B>>
implements Builder<TableColumn<S, T>> {
    private int __set;
    private Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory;
    private Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> cellValueFactory;
    private Collection<? extends TableColumn<S, ?>> columns;
    private Comparator<T> comparator;
    private ContextMenu contextMenu;
    private boolean editable;
    private Node graphic;
    private String id;
    private double maxWidth;
    private double minWidth;
    private EventHandler<TableColumn.CellEditEvent<S, T>> onEditCancel;
    private EventHandler<TableColumn.CellEditEvent<S, T>> onEditCommit;
    private EventHandler<TableColumn.CellEditEvent<S, T>> onEditStart;
    private double prefWidth;
    private boolean resizable;
    private boolean sortable;
    private Node sortNode;
    private TableColumn.SortType sortType;
    private String style;
    private Collection<? extends String> styleClass;
    private String text;
    private Object userData;
    private boolean visible;

    protected TableColumnBuilder() {
    }

    public static <S, T> TableColumnBuilder<S, T, ?> create() {
        return new TableColumnBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(TableColumn<S, T> tableColumn) {
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    tableColumn.setCellFactory(this.cellFactory);
                    break;
                }
                case 1: {
                    tableColumn.setCellValueFactory(this.cellValueFactory);
                    break;
                }
                case 2: {
                    tableColumn.getColumns().addAll(this.columns);
                    break;
                }
                case 3: {
                    tableColumn.setComparator(this.comparator);
                    break;
                }
                case 4: {
                    tableColumn.setContextMenu(this.contextMenu);
                    break;
                }
                case 5: {
                    tableColumn.setEditable(this.editable);
                    break;
                }
                case 6: {
                    tableColumn.setGraphic(this.graphic);
                    break;
                }
                case 7: {
                    tableColumn.setId(this.id);
                    break;
                }
                case 8: {
                    tableColumn.setMaxWidth(this.maxWidth);
                    break;
                }
                case 9: {
                    tableColumn.setMinWidth(this.minWidth);
                    break;
                }
                case 10: {
                    tableColumn.setOnEditCancel(this.onEditCancel);
                    break;
                }
                case 11: {
                    tableColumn.setOnEditCommit(this.onEditCommit);
                    break;
                }
                case 12: {
                    tableColumn.setOnEditStart(this.onEditStart);
                    break;
                }
                case 13: {
                    tableColumn.setPrefWidth(this.prefWidth);
                    break;
                }
                case 14: {
                    tableColumn.setResizable(this.resizable);
                    break;
                }
                case 15: {
                    tableColumn.setSortable(this.sortable);
                    break;
                }
                case 16: {
                    tableColumn.setSortNode(this.sortNode);
                    break;
                }
                case 17: {
                    tableColumn.setSortType(this.sortType);
                    break;
                }
                case 18: {
                    tableColumn.setStyle(this.style);
                    break;
                }
                case 19: {
                    tableColumn.getStyleClass().addAll(this.styleClass);
                    break;
                }
                case 20: {
                    tableColumn.setText(this.text);
                    break;
                }
                case 21: {
                    tableColumn.setUserData(this.userData);
                    break;
                }
                case 22: {
                    tableColumn.setVisible(this.visible);
                }
            }
        }
    }

    public B cellFactory(Callback<TableColumn<S, T>, TableCell<S, T>> callback) {
        this.cellFactory = callback;
        this.__set(0);
        return (B)this;
    }

    public B cellValueFactory(Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> callback) {
        this.cellValueFactory = callback;
        this.__set(1);
        return (B)this;
    }

    public B columns(Collection<? extends TableColumn<S, ?>> collection) {
        this.columns = collection;
        this.__set(2);
        return (B)this;
    }

    public B columns(TableColumn<S, ?> ... arrtableColumn) {
        return this.columns(Arrays.asList(arrtableColumn));
    }

    public B comparator(Comparator<T> comparator) {
        this.comparator = comparator;
        this.__set(3);
        return (B)this;
    }

    public B contextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
        this.__set(4);
        return (B)this;
    }

    public B editable(boolean bl) {
        this.editable = bl;
        this.__set(5);
        return (B)this;
    }

    public B graphic(Node node) {
        this.graphic = node;
        this.__set(6);
        return (B)this;
    }

    public B id(String string) {
        this.id = string;
        this.__set(7);
        return (B)this;
    }

    public B maxWidth(double d2) {
        this.maxWidth = d2;
        this.__set(8);
        return (B)this;
    }

    public B minWidth(double d2) {
        this.minWidth = d2;
        this.__set(9);
        return (B)this;
    }

    public B onEditCancel(EventHandler<TableColumn.CellEditEvent<S, T>> eventHandler) {
        this.onEditCancel = eventHandler;
        this.__set(10);
        return (B)this;
    }

    public B onEditCommit(EventHandler<TableColumn.CellEditEvent<S, T>> eventHandler) {
        this.onEditCommit = eventHandler;
        this.__set(11);
        return (B)this;
    }

    public B onEditStart(EventHandler<TableColumn.CellEditEvent<S, T>> eventHandler) {
        this.onEditStart = eventHandler;
        this.__set(12);
        return (B)this;
    }

    public B prefWidth(double d2) {
        this.prefWidth = d2;
        this.__set(13);
        return (B)this;
    }

    public B resizable(boolean bl) {
        this.resizable = bl;
        this.__set(14);
        return (B)this;
    }

    public B sortable(boolean bl) {
        this.sortable = bl;
        this.__set(15);
        return (B)this;
    }

    public B sortNode(Node node) {
        this.sortNode = node;
        this.__set(16);
        return (B)this;
    }

    public B sortType(TableColumn.SortType sortType) {
        this.sortType = sortType;
        this.__set(17);
        return (B)this;
    }

    public B style(String string) {
        this.style = string;
        this.__set(18);
        return (B)this;
    }

    public B styleClass(Collection<? extends String> collection) {
        this.styleClass = collection;
        this.__set(19);
        return (B)this;
    }

    public B styleClass(String ... arrstring) {
        return this.styleClass(Arrays.asList(arrstring));
    }

    public B text(String string) {
        this.text = string;
        this.__set(20);
        return (B)this;
    }

    public B userData(Object object) {
        this.userData = object;
        this.__set(21);
        return (B)this;
    }

    public B visible(boolean bl) {
        this.visible = bl;
        this.__set(22);
        return (B)this;
    }

    @Override
    public TableColumn<S, T> build() {
        TableColumn tableColumn = new TableColumn();
        this.applyTo(tableColumn);
        return tableColumn;
    }
}

