/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;

public abstract class MultipleSelectionModel<T>
extends SelectionModel<T> {
    private ObjectProperty<SelectionMode> selectionMode;

    public final void setSelectionMode(SelectionMode selectionMode) {
        this.selectionModeProperty().set(selectionMode);
    }

    public final SelectionMode getSelectionMode() {
        return this.selectionMode == null ? SelectionMode.SINGLE : (SelectionMode)((Object)this.selectionMode.get());
    }

    public final ObjectProperty<SelectionMode> selectionModeProperty() {
        if (this.selectionMode == null) {
            this.selectionMode = new ObjectPropertyBase<SelectionMode>(SelectionMode.SINGLE){

                @Override
                protected void invalidated() {
                    if (MultipleSelectionModel.this.getSelectionMode() == SelectionMode.SINGLE && !MultipleSelectionModel.this.isEmpty()) {
                        int n2 = MultipleSelectionModel.this.getSelectedIndex();
                        MultipleSelectionModel.this.clearSelection();
                        MultipleSelectionModel.this.select(n2);
                    }
                }

                @Override
                public Object getBean() {
                    return MultipleSelectionModel.this;
                }

                @Override
                public String getName() {
                    return "selectionMode";
                }
            };
        }
        return this.selectionMode;
    }

    public abstract ObservableList<Integer> getSelectedIndices();

    public abstract ObservableList<T> getSelectedItems();

    public abstract void selectIndices(int var1, int ... var2);

    public void selectRange(int n2, int n3) {
        if (n2 == n3) {
            return;
        }
        boolean bl = n2 < n3;
        int n4 = bl ? n2 : n3;
        int n5 = bl ? n3 : n2;
        int n6 = n5 - n4 - 1;
        int[] arrn = new int[n6];
        int n7 = bl ? n4 : n5;
        int n8 = bl ? n7++ : n7--;
        for (int i2 = 0; i2 < n6; ++i2) {
            arrn[i2] = bl ? n7++ : n7--;
        }
        this.selectIndices(n8, arrn);
    }

    public abstract void selectAll();

    @Override
    public abstract void selectFirst();

    @Override
    public abstract void selectLast();
}

