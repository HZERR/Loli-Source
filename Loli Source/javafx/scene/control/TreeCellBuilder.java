/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.IndexedCellBuilder;
import javafx.scene.control.TreeCell;

@Deprecated
public class TreeCellBuilder<T, B extends TreeCellBuilder<T, B>>
extends IndexedCellBuilder<T, B> {
    private boolean __set;
    private Node disclosureNode;

    protected TreeCellBuilder() {
    }

    public static <T> TreeCellBuilder<T, ?> create() {
        return new TreeCellBuilder();
    }

    @Override
    public void applyTo(TreeCell<T> treeCell) {
        super.applyTo(treeCell);
        if (this.__set) {
            treeCell.setDisclosureNode(this.disclosureNode);
        }
    }

    public B disclosureNode(Node node) {
        this.disclosureNode = node;
        this.__set = true;
        return (B)this;
    }

    @Override
    public TreeCell<T> build() {
        TreeCell treeCell = new TreeCell();
        this.applyTo(treeCell);
        return treeCell;
    }
}

