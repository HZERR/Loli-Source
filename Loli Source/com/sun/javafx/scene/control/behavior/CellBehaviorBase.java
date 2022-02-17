/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public abstract class CellBehaviorBase<T extends Cell>
extends BehaviorBase<T> {
    private static final String ANCHOR_PROPERTY_KEY = "anchor";
    private static final String IS_DEFAULT_ANCHOR_KEY = "isDefaultAnchor";
    private boolean latePress = false;

    public static <T> T getAnchor(Control control, T t2) {
        return (T)(CellBehaviorBase.hasNonDefaultAnchor(control) ? control.getProperties().get(ANCHOR_PROPERTY_KEY) : t2);
    }

    public static <T> void setAnchor(Control control, T t2, boolean bl) {
        if (control != null && t2 == null) {
            CellBehaviorBase.removeAnchor(control);
        } else {
            control.getProperties().put(ANCHOR_PROPERTY_KEY, t2);
            control.getProperties().put(IS_DEFAULT_ANCHOR_KEY, bl);
        }
    }

    public static boolean hasNonDefaultAnchor(Control control) {
        Boolean bl = (Boolean)control.getProperties().remove(IS_DEFAULT_ANCHOR_KEY);
        return (bl == null || bl == false) && CellBehaviorBase.hasAnchor(control);
    }

    public static boolean hasDefaultAnchor(Control control) {
        Boolean bl = (Boolean)control.getProperties().remove(IS_DEFAULT_ANCHOR_KEY);
        return bl != null && bl == true && CellBehaviorBase.hasAnchor(control);
    }

    private static boolean hasAnchor(Control control) {
        return control.getProperties().get(ANCHOR_PROPERTY_KEY) != null;
    }

    public static void removeAnchor(Control control) {
        control.getProperties().remove(ANCHOR_PROPERTY_KEY);
        control.getProperties().remove(IS_DEFAULT_ANCHOR_KEY);
    }

    public CellBehaviorBase(T t2, List<KeyBinding> list) {
        super(t2, list);
    }

    protected abstract Control getCellContainer();

    protected abstract MultipleSelectionModel<?> getSelectionModel();

    protected abstract FocusModel<?> getFocusModel();

    protected abstract void edit(T var1);

    protected boolean handleDisclosureNode(double d2, double d3) {
        return false;
    }

    protected boolean isClickPositionValid(double d2, double d3) {
        return true;
    }

    protected int getIndex() {
        return this.getControl() instanceof IndexedCell ? ((IndexedCell)this.getControl()).getIndex() : -1;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isSynthesized()) {
            this.latePress = true;
        } else {
            this.latePress = this.isSelected();
            if (!this.latePress) {
                this.doSelect(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getButton(), mouseEvent.getClickCount(), mouseEvent.isShiftDown(), mouseEvent.isShortcutDown());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (this.latePress) {
            this.latePress = false;
            this.doSelect(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getButton(), mouseEvent.getClickCount(), mouseEvent.isShiftDown(), mouseEvent.isShortcutDown());
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        this.latePress = false;
    }

    protected void doSelect(double d2, double d3, MouseButton mouseButton, int n2, boolean bl, boolean bl2) {
        Cell cell = (Cell)this.getControl();
        Control control = this.getCellContainer();
        if (cell.isEmpty() || !cell.contains(d2, d3)) {
            return;
        }
        int n3 = this.getIndex();
        boolean bl3 = cell.isSelected();
        MultipleSelectionModel<?> multipleSelectionModel = this.getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel<?> focusModel = this.getFocusModel();
        if (focusModel == null) {
            return;
        }
        if (this.handleDisclosureNode(d2, d3)) {
            return;
        }
        if (!this.isClickPositionValid(d2, d3)) {
            return;
        }
        if (bl) {
            if (!CellBehaviorBase.hasNonDefaultAnchor(control)) {
                CellBehaviorBase.setAnchor(control, focusModel.getFocusedIndex(), false);
            }
        } else {
            CellBehaviorBase.removeAnchor(control);
        }
        if (mouseButton == MouseButton.PRIMARY || mouseButton == MouseButton.SECONDARY && !bl3) {
            if (multipleSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
                this.simpleSelect(mouseButton, n2, bl2);
            } else if (bl2) {
                if (bl3) {
                    multipleSelectionModel.clearSelection(n3);
                    focusModel.focus(n3);
                } else {
                    multipleSelectionModel.select(n3);
                }
            } else if (bl && n2 == 1) {
                int n4 = CellBehaviorBase.getAnchor(control, focusModel.getFocusedIndex());
                this.selectRows(n4, n3);
                focusModel.focus(n3);
            } else {
                this.simpleSelect(mouseButton, n2, bl2);
            }
        }
    }

    protected void simpleSelect(MouseButton mouseButton, int n2, boolean bl) {
        int n3 = this.getIndex();
        MultipleSelectionModel<?> multipleSelectionModel = this.getSelectionModel();
        boolean bl2 = multipleSelectionModel.isSelected(n3);
        if (bl2 && bl) {
            multipleSelectionModel.clearSelection(n3);
            this.getFocusModel().focus(n3);
            bl2 = false;
        } else {
            multipleSelectionModel.clearAndSelect(n3);
        }
        this.handleClicks(mouseButton, n2, bl2);
    }

    protected void handleClicks(MouseButton mouseButton, int n2, boolean bl) {
        if (mouseButton == MouseButton.PRIMARY) {
            if (n2 == 1 && bl) {
                this.edit((Cell)this.getControl());
            } else if (n2 == 1) {
                this.edit(null);
            } else if (n2 == 2 && ((Cell)this.getControl()).isEditable()) {
                this.edit((Cell)this.getControl());
            }
        }
    }

    void selectRows(int n2, int n3) {
        boolean bl = n2 < n3;
        int n4 = Math.min(n2, n3);
        int n5 = Math.max(n2, n3);
        ArrayList<Integer> arrayList = new ArrayList<Integer>(this.getSelectionModel().getSelectedIndices());
        int n6 = arrayList.size();
        for (int i2 = 0; i2 < n6; ++i2) {
            int n7 = (Integer)arrayList.get(i2);
            if (n7 >= n4 && n7 <= n5) continue;
            this.getSelectionModel().clearSelection(n7);
        }
        if (n4 == n5) {
            this.getSelectionModel().select(n4);
        } else if (bl) {
            this.getSelectionModel().selectRange(n4, n5 + 1);
        } else {
            this.getSelectionModel().selectRange(n5, n4 - 1);
        }
    }

    protected boolean isSelected() {
        return ((Cell)this.getControl()).isSelected();
    }
}

