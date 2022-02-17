/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;

@Deprecated
public abstract class MultipleSelectionModelBuilder<T, B extends MultipleSelectionModelBuilder<T, B>> {
    private int __set;
    private Collection<? extends Integer> selectedIndices;
    private Collection<? extends T> selectedItems;
    private SelectionMode selectionMode;

    protected MultipleSelectionModelBuilder() {
    }

    public void applyTo(MultipleSelectionModel<T> multipleSelectionModel) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            multipleSelectionModel.getSelectedIndices().addAll(this.selectedIndices);
        }
        if ((n2 & 2) != 0) {
            multipleSelectionModel.getSelectedItems().addAll(this.selectedItems);
        }
        if ((n2 & 4) != 0) {
            multipleSelectionModel.setSelectionMode(this.selectionMode);
        }
    }

    public B selectedIndices(Collection<? extends Integer> collection) {
        this.selectedIndices = collection;
        this.__set |= 1;
        return (B)this;
    }

    public B selectedIndices(Integer ... arrinteger) {
        return this.selectedIndices(Arrays.asList(arrinteger));
    }

    public B selectedItems(Collection<? extends T> collection) {
        this.selectedItems = collection;
        this.__set |= 2;
        return (B)this;
    }

    public B selectedItems(T ... arrT) {
        return this.selectedItems((Collection<? extends T>)Arrays.asList(arrT));
    }

    public B selectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        this.__set |= 4;
        return (B)this;
    }
}

