/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.scene.control.behavior.TableCellBehavior;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public abstract class TableRowBehaviorBase<T extends Cell>
extends CellBehaviorBase<T> {
    public TableRowBehaviorBase(T t2) {
        super(t2, Collections.emptyList());
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (!this.isClickPositionValid(mouseEvent.getX(), mouseEvent.getY())) {
            return;
        }
        super.mousePressed(mouseEvent);
    }

    @Override
    protected abstract TableSelectionModel<?> getSelectionModel();

    protected abstract TablePositionBase<?> getFocusedCell();

    protected abstract ObservableList getVisibleLeafColumns();

    @Override
    protected void doSelect(double d2, double d3, MouseButton mouseButton, int n2, boolean bl, boolean bl2) {
        Control control = this.getCellContainer();
        if (control == null) {
            return;
        }
        if (this.handleDisclosureNode(d2, d3)) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = this.getSelectionModel();
        if (multipleSelectionModel == null || ((TableSelectionModel)multipleSelectionModel).isCellSelectionEnabled()) {
            return;
        }
        int n3 = this.getIndex();
        boolean bl3 = ((TableSelectionModel)multipleSelectionModel).isSelected(n3);
        if (n2 == 1) {
            if (!this.isClickPositionValid(d2, d3)) {
                return;
            }
            if (bl3 && bl2) {
                ((TableSelectionModel)multipleSelectionModel).clearSelection(n3);
            } else if (bl2) {
                ((TableSelectionModel)multipleSelectionModel).select(this.getIndex());
            } else if (bl) {
                TablePositionBase<?> tablePositionBase = TableCellBehavior.getAnchor(control, this.getFocusedCell());
                int n4 = tablePositionBase.getRow();
                this.selectRows(n4, n3);
            } else {
                this.simpleSelect(mouseButton, n2, bl2);
            }
        } else {
            this.simpleSelect(mouseButton, n2, bl2);
        }
    }

    @Override
    protected boolean isClickPositionValid(double d2, double d3) {
        ObservableList observableList = this.getVisibleLeafColumns();
        double d4 = 0.0;
        for (int i2 = 0; i2 < observableList.size(); ++i2) {
            d4 += ((TableColumnBase)observableList.get(i2)).getWidth();
        }
        return d2 > d4;
    }
}

