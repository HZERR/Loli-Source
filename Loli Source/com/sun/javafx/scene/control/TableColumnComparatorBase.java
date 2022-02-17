/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableColumn;

public abstract class TableColumnComparatorBase<S, T>
implements Comparator<S> {
    private final List<? extends TableColumnBase> columns;

    public TableColumnComparatorBase(TableColumnBase<S, T> ... arrtableColumnBase) {
        this(Arrays.asList(arrtableColumnBase));
    }

    public TableColumnComparatorBase(List<? extends TableColumnBase> list) {
        this.columns = new ArrayList<TableColumnBase>(list);
    }

    public List<? extends TableColumnBase> getColumns() {
        return Collections.unmodifiableList(this.columns);
    }

    @Override
    public int compare(S s2, S s3) {
        for (TableColumnBase tableColumnBase : this.columns) {
            Object t2;
            Object t3;
            int n2;
            if (!this.isSortable(tableColumnBase) || (n2 = this.doCompare(tableColumnBase, t3 = tableColumnBase.getCellData(s2), t2 = tableColumnBase.getCellData(s3))) == 0) continue;
            return n2;
        }
        return 0;
    }

    public int hashCode() {
        int n2 = 7;
        n2 = 59 * n2 + (this.columns != null ? this.columns.hashCode() : 0);
        return n2;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        TableColumnComparatorBase tableColumnComparatorBase = (TableColumnComparatorBase)object;
        return this.columns == tableColumnComparatorBase.columns || this.columns != null && this.columns.equals(tableColumnComparatorBase.columns);
    }

    public String toString() {
        return "TableColumnComparatorBase [ columns: " + this.getColumns() + "] ";
    }

    public abstract boolean isSortable(TableColumnBase<S, T> var1);

    public abstract int doCompare(TableColumnBase<S, T> var1, T var2, T var3);

    public static final class TreeTableColumnComparator<S, T>
    extends TableColumnComparatorBase<S, T> {
        public TreeTableColumnComparator(TreeTableColumn<S, T> ... arrtreeTableColumn) {
            super(Arrays.asList(arrtreeTableColumn));
        }

        public TreeTableColumnComparator(List<TreeTableColumn<S, T>> list) {
            super(list);
        }

        @Override
        public boolean isSortable(TableColumnBase<S, T> tableColumnBase) {
            TreeTableColumn treeTableColumn = (TreeTableColumn)tableColumnBase;
            return treeTableColumn.getSortType() != null && treeTableColumn.isSortable();
        }

        @Override
        public int doCompare(TableColumnBase<S, T> tableColumnBase, T t2, T t3) {
            TreeTableColumn treeTableColumn = (TreeTableColumn)tableColumnBase;
            Comparator<T> comparator = treeTableColumn.getComparator();
            switch (treeTableColumn.getSortType()) {
                case ASCENDING: {
                    return comparator.compare(t2, t3);
                }
                case DESCENDING: {
                    return comparator.compare(t3, t2);
                }
            }
            return 0;
        }
    }

    public static final class TableColumnComparator<S, T>
    extends TableColumnComparatorBase<S, T> {
        public TableColumnComparator(TableColumn<S, T> ... arrtableColumn) {
            super(Arrays.asList(arrtableColumn));
        }

        public TableColumnComparator(List<TableColumn<S, T>> list) {
            super(list);
        }

        @Override
        public boolean isSortable(TableColumnBase<S, T> tableColumnBase) {
            TableColumn tableColumn = (TableColumn)tableColumnBase;
            return tableColumn.getSortType() != null && tableColumn.isSortable();
        }

        @Override
        public int doCompare(TableColumnBase<S, T> tableColumnBase, T t2, T t3) {
            TableColumn tableColumn = (TableColumn)tableColumnBase;
            Comparator<T> comparator = tableColumn.getComparator();
            switch (tableColumn.getSortType()) {
                case ASCENDING: {
                    return comparator.compare(t2, t3);
                }
                case DESCENDING: {
                    return comparator.compare(t3, t2);
                }
            }
            return 0;
        }
    }
}

