/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItemBuilder;

@Deprecated
public class CheckBoxTreeItemBuilder<T, B extends CheckBoxTreeItemBuilder<T, B>>
extends TreeItemBuilder<T, B> {
    protected CheckBoxTreeItemBuilder() {
    }

    public static <T> CheckBoxTreeItemBuilder<T, ?> create() {
        return new CheckBoxTreeItemBuilder();
    }

    @Override
    public CheckBoxTreeItem<T> build() {
        CheckBoxTreeItem checkBoxTreeItem = new CheckBoxTreeItem();
        this.applyTo(checkBoxTreeItem);
        return checkBoxTreeItem;
    }
}

