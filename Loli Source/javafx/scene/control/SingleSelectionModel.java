/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.SelectionModel;

public abstract class SingleSelectionModel<T>
extends SelectionModel<T> {
    @Override
    public void clearSelection() {
        this.updateSelectedIndex(-1);
    }

    @Override
    public void clearSelection(int n2) {
        if (this.getSelectedIndex() == n2) {
            this.clearSelection();
        }
    }

    @Override
    public boolean isEmpty() {
        return this.getItemCount() == 0 || this.getSelectedIndex() == -1;
    }

    @Override
    public boolean isSelected(int n2) {
        return this.getSelectedIndex() == n2;
    }

    @Override
    public void clearAndSelect(int n2) {
        this.select(n2);
    }

    @Override
    public void select(T t2) {
        if (t2 == null) {
            this.setSelectedIndex(-1);
            this.setSelectedItem(null);
            return;
        }
        int n2 = this.getItemCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            T t3 = this.getModelItem(i2);
            if (t3 == null || !t3.equals(t2)) continue;
            this.select(i2);
            return;
        }
        this.setSelectedItem(t2);
    }

    @Override
    public void select(int n2) {
        if (n2 == -1) {
            this.clearSelection();
            return;
        }
        int n3 = this.getItemCount();
        if (n3 == 0 || n2 < 0 || n2 >= n3) {
            return;
        }
        this.updateSelectedIndex(n2);
    }

    @Override
    public void selectPrevious() {
        if (this.getSelectedIndex() == 0) {
            return;
        }
        this.select(this.getSelectedIndex() - 1);
    }

    @Override
    public void selectNext() {
        this.select(this.getSelectedIndex() + 1);
    }

    @Override
    public void selectFirst() {
        if (this.getItemCount() > 0) {
            this.select(0);
        }
    }

    @Override
    public void selectLast() {
        int n2 = this.getItemCount();
        if (n2 > 0 && this.getSelectedIndex() < n2 - 1) {
            this.select(n2 - 1);
        }
    }

    protected abstract T getModelItem(int var1);

    protected abstract int getItemCount();

    private void updateSelectedIndex(int n2) {
        int n3 = this.getSelectedIndex();
        Object t2 = this.getSelectedItem();
        this.setSelectedIndex(n2);
        if (n3 != -1 || t2 == null || n2 != -1) {
            this.setSelectedItem(this.getModelItem(this.getSelectedIndex()));
        }
    }
}

