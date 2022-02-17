/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import java.util.Collections;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

public class ListCellBehavior<T>
extends CellBehaviorBase<ListCell<T>> {
    public ListCellBehavior(ListCell<T> listCell) {
        super(listCell, Collections.emptyList());
    }

    @Override
    protected MultipleSelectionModel<T> getSelectionModel() {
        return ((ListView)this.getCellContainer()).getSelectionModel();
    }

    @Override
    protected FocusModel<T> getFocusModel() {
        return ((ListView)this.getCellContainer()).getFocusModel();
    }

    @Override
    protected ListView<T> getCellContainer() {
        return ((ListCell)this.getControl()).getListView();
    }

    @Override
    protected void edit(ListCell<T> listCell) {
        int n2 = listCell == null ? -1 : listCell.getIndex();
        ((ListView)this.getCellContainer()).edit(n2);
    }
}

