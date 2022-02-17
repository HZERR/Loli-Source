/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.util.Builder;

@Deprecated
public class TreeItemBuilder<T, B extends TreeItemBuilder<T, B>>
implements Builder<TreeItem<T>> {
    private int __set;
    private Collection<? extends TreeItem<T>> children;
    private boolean expanded;
    private Node graphic;
    private T value;

    protected TreeItemBuilder() {
    }

    public static <T> TreeItemBuilder<T, ?> create() {
        return new TreeItemBuilder();
    }

    public void applyTo(TreeItem<T> treeItem) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            treeItem.getChildren().addAll(this.children);
        }
        if ((n2 & 2) != 0) {
            treeItem.setExpanded(this.expanded);
        }
        if ((n2 & 4) != 0) {
            treeItem.setGraphic(this.graphic);
        }
        if ((n2 & 8) != 0) {
            treeItem.setValue(this.value);
        }
    }

    public B children(Collection<? extends TreeItem<T>> collection) {
        this.children = collection;
        this.__set |= 1;
        return (B)this;
    }

    public B children(TreeItem<T> ... arrtreeItem) {
        return this.children(Arrays.asList(arrtreeItem));
    }

    public B expanded(boolean bl) {
        this.expanded = bl;
        this.__set |= 2;
        return (B)this;
    }

    public B graphic(Node node) {
        this.graphic = node;
        this.__set |= 4;
        return (B)this;
    }

    public B value(T t2) {
        this.value = t2;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public TreeItem<T> build() {
        TreeItem treeItem = new TreeItem();
        this.applyTo(treeItem);
        return treeItem;
    }
}

