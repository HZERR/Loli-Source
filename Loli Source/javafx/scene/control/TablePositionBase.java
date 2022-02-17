/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.lang.ref.WeakReference;
import javafx.scene.control.TableColumnBase;

public abstract class TablePositionBase<TC extends TableColumnBase> {
    private final int row;
    private final WeakReference<TC> tableColumnRef;

    protected TablePositionBase(int n2, TC TC) {
        this.row = n2;
        this.tableColumnRef = new WeakReference<TC>(TC);
    }

    public int getRow() {
        return this.row;
    }

    public abstract int getColumn();

    public TC getTableColumn() {
        return (TC)((TableColumnBase)this.tableColumnRef.get());
    }

    public boolean equals(Object object) {
        TC TC;
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        TablePositionBase tablePositionBase = (TablePositionBase)object;
        if (this.row != tablePositionBase.row) {
            return false;
        }
        TC TC2 = this.getTableColumn();
        return TC2 == (TC = tablePositionBase.getTableColumn()) || TC2 != null && TC2.equals(TC);
    }

    public int hashCode() {
        int n2 = 5;
        n2 = 79 * n2 + this.row;
        TC TC = this.getTableColumn();
        n2 = 79 * n2 + (TC != null ? TC.hashCode() : 0);
        return n2;
    }
}

