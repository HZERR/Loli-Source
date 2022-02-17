/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public abstract class FocusModel<T> {
    private ReadOnlyIntegerWrapper focusedIndex = new ReadOnlyIntegerWrapper(this, "focusedIndex", -1);
    private ReadOnlyObjectWrapper<T> focusedItem = new ReadOnlyObjectWrapper(this, "focusedItem");

    public FocusModel() {
        this.focusedIndexProperty().addListener(observable -> this.setFocusedItem(this.getModelItem(this.getFocusedIndex())));
    }

    public final ReadOnlyIntegerProperty focusedIndexProperty() {
        return this.focusedIndex.getReadOnlyProperty();
    }

    public final int getFocusedIndex() {
        return this.focusedIndex.get();
    }

    final void setFocusedIndex(int n2) {
        this.focusedIndex.set(n2);
    }

    public final ReadOnlyObjectProperty<T> focusedItemProperty() {
        return this.focusedItem.getReadOnlyProperty();
    }

    public final T getFocusedItem() {
        return this.focusedItemProperty().get();
    }

    final void setFocusedItem(T t2) {
        this.focusedItem.set(t2);
    }

    protected abstract int getItemCount();

    protected abstract T getModelItem(int var1);

    public boolean isFocused(int n2) {
        if (n2 < 0 || n2 >= this.getItemCount()) {
            return false;
        }
        return this.getFocusedIndex() == n2;
    }

    public void focus(int n2) {
        if (n2 < 0 || n2 >= this.getItemCount()) {
            this.setFocusedIndex(-1);
        } else {
            int n3 = this.getFocusedIndex();
            this.setFocusedIndex(n2);
            if (n3 == n2) {
                this.setFocusedItem(this.getModelItem(n2));
            }
        }
    }

    public void focusPrevious() {
        if (this.getFocusedIndex() == -1) {
            this.focus(0);
        } else if (this.getFocusedIndex() > 0) {
            this.focus(this.getFocusedIndex() - 1);
        }
    }

    public void focusNext() {
        if (this.getFocusedIndex() == -1) {
            this.focus(0);
        } else if (this.getFocusedIndex() != this.getItemCount() - 1) {
            this.focus(this.getFocusedIndex() + 1);
        }
    }
}

