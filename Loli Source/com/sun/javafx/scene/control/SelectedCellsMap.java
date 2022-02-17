/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control;

import java.util.BitSet;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TablePositionBase;

public abstract class SelectedCellsMap<T extends TablePositionBase> {
    private final ObservableList<T> selectedCells = FXCollections.observableArrayList();
    private final ObservableList<T> sortedSelectedCells = new SortedList<T>(this.selectedCells, (tablePositionBase, tablePositionBase2) -> {
        int n2 = tablePositionBase.getRow() - tablePositionBase2.getRow();
        return n2 == 0 ? tablePositionBase.getColumn() - tablePositionBase2.getColumn() : n2;
    });
    private final Map<Integer, BitSet> selectedCellBitSetMap;

    public SelectedCellsMap(ListChangeListener<T> listChangeListener) {
        this.sortedSelectedCells.addListener(listChangeListener);
        this.selectedCellBitSetMap = new TreeMap<Integer, BitSet>((n2, n3) -> n2.compareTo((Integer)n3));
    }

    public abstract boolean isCellSelectionEnabled();

    public int size() {
        return this.selectedCells.size();
    }

    public T get(int n2) {
        if (n2 < 0) {
            return null;
        }
        return (T)((TablePositionBase)this.sortedSelectedCells.get(n2));
    }

    public void add(T t2) {
        BitSet bitSet;
        int n2 = ((TablePositionBase)t2).getRow();
        int n3 = ((TablePositionBase)t2).getColumn();
        boolean bl = false;
        if (!this.selectedCellBitSetMap.containsKey(n2)) {
            bitSet = new BitSet();
            this.selectedCellBitSetMap.put(n2, bitSet);
            bl = true;
        } else {
            bitSet = this.selectedCellBitSetMap.get(n2);
        }
        boolean bl2 = this.isCellSelectionEnabled();
        if (bl2) {
            if (n3 >= 0) {
                boolean bl3 = bitSet.get(n3);
                if (!bl3) {
                    bitSet.set(n3);
                    this.selectedCells.add(t2);
                }
            } else if (!this.selectedCells.contains(t2)) {
                this.selectedCells.add(t2);
            }
        } else if (bl) {
            if (n3 >= 0) {
                bitSet.set(n3);
            }
            this.selectedCells.add(t2);
        }
    }

    public void addAll(Collection<T> collection) {
        for (TablePositionBase tablePositionBase : collection) {
            BitSet bitSet;
            int n2 = tablePositionBase.getRow();
            int n3 = tablePositionBase.getColumn();
            if (!this.selectedCellBitSetMap.containsKey(n2)) {
                bitSet = new BitSet();
                this.selectedCellBitSetMap.put(n2, bitSet);
            } else {
                bitSet = this.selectedCellBitSetMap.get(n2);
            }
            if (n3 < 0) continue;
            bitSet.set(n3);
        }
        this.selectedCells.addAll(collection);
    }

    public void setAll(Collection<T> collection) {
        this.selectedCellBitSetMap.clear();
        for (TablePositionBase tablePositionBase : collection) {
            BitSet bitSet;
            int n2 = tablePositionBase.getRow();
            int n3 = tablePositionBase.getColumn();
            if (!this.selectedCellBitSetMap.containsKey(n2)) {
                bitSet = new BitSet();
                this.selectedCellBitSetMap.put(n2, bitSet);
            } else {
                bitSet = this.selectedCellBitSetMap.get(n2);
            }
            if (n3 < 0) continue;
            bitSet.set(n3);
        }
        this.selectedCells.setAll(collection);
    }

    public void remove(T t2) {
        int n2 = ((TablePositionBase)t2).getRow();
        int n3 = ((TablePositionBase)t2).getColumn();
        if (this.selectedCellBitSetMap.containsKey(n2)) {
            BitSet bitSet = this.selectedCellBitSetMap.get(n2);
            if (n3 >= 0) {
                bitSet.clear(n3);
            }
            if (bitSet.isEmpty()) {
                this.selectedCellBitSetMap.remove(n2);
            }
        }
        this.selectedCells.remove(t2);
    }

    public void clear() {
        this.selectedCellBitSetMap.clear();
        this.selectedCells.clear();
    }

    public boolean isSelected(int n2, int n3) {
        if (n3 < 0) {
            return this.selectedCellBitSetMap.containsKey(n2);
        }
        return this.selectedCellBitSetMap.containsKey(n2) ? this.selectedCellBitSetMap.get(n2).get(n3) : false;
    }

    public int indexOf(T t2) {
        return this.sortedSelectedCells.indexOf(t2);
    }

    public boolean isEmpty() {
        return this.selectedCells.isEmpty();
    }

    public ObservableList<T> getSelectedCells() {
        return this.selectedCells;
    }
}

