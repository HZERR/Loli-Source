/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.event.EventHandler;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
public class TreeViewBuilder<T, B extends TreeViewBuilder<T, B>>
extends ControlBuilder<B>
implements Builder<TreeView<T>> {
    private int __set;
    private Callback<TreeView<T>, TreeCell<T>> cellFactory;
    private boolean editable;
    private FocusModel<TreeItem<T>> focusModel;
    private EventHandler<TreeView.EditEvent<T>> onEditCancel;
    private EventHandler<TreeView.EditEvent<T>> onEditCommit;
    private EventHandler<TreeView.EditEvent<T>> onEditStart;
    private TreeItem<T> root;
    private MultipleSelectionModel<TreeItem<T>> selectionModel;
    private boolean showRoot;

    protected TreeViewBuilder() {
    }

    public static <T> TreeViewBuilder<T, ?> create() {
        return new TreeViewBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(TreeView<T> treeView) {
        super.applyTo(treeView);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    treeView.setCellFactory(this.cellFactory);
                    break;
                }
                case 1: {
                    treeView.setEditable(this.editable);
                    break;
                }
                case 2: {
                    treeView.setFocusModel(this.focusModel);
                    break;
                }
                case 3: {
                    treeView.setOnEditCancel(this.onEditCancel);
                    break;
                }
                case 4: {
                    treeView.setOnEditCommit(this.onEditCommit);
                    break;
                }
                case 5: {
                    treeView.setOnEditStart(this.onEditStart);
                    break;
                }
                case 6: {
                    treeView.setRoot(this.root);
                    break;
                }
                case 7: {
                    treeView.setSelectionModel(this.selectionModel);
                    break;
                }
                case 8: {
                    treeView.setShowRoot(this.showRoot);
                }
            }
        }
    }

    public B cellFactory(Callback<TreeView<T>, TreeCell<T>> callback) {
        this.cellFactory = callback;
        this.__set(0);
        return (B)this;
    }

    public B editable(boolean bl) {
        this.editable = bl;
        this.__set(1);
        return (B)this;
    }

    public B focusModel(FocusModel<TreeItem<T>> focusModel) {
        this.focusModel = focusModel;
        this.__set(2);
        return (B)this;
    }

    public B onEditCancel(EventHandler<TreeView.EditEvent<T>> eventHandler) {
        this.onEditCancel = eventHandler;
        this.__set(3);
        return (B)this;
    }

    public B onEditCommit(EventHandler<TreeView.EditEvent<T>> eventHandler) {
        this.onEditCommit = eventHandler;
        this.__set(4);
        return (B)this;
    }

    public B onEditStart(EventHandler<TreeView.EditEvent<T>> eventHandler) {
        this.onEditStart = eventHandler;
        this.__set(5);
        return (B)this;
    }

    public B root(TreeItem<T> treeItem) {
        this.root = treeItem;
        this.__set(6);
        return (B)this;
    }

    public B selectionModel(MultipleSelectionModel<TreeItem<T>> multipleSelectionModel) {
        this.selectionModel = multipleSelectionModel;
        this.__set(7);
        return (B)this;
    }

    public B showRoot(boolean bl) {
        this.showRoot = bl;
        this.__set(8);
        return (B)this;
    }

    @Override
    public TreeView<T> build() {
        TreeView treeView = new TreeView();
        this.applyTo(treeView);
        return treeView;
    }
}

