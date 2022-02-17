/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ControlUtils;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

abstract class MultipleSelectionModelBase<T>
extends MultipleSelectionModel<T> {
    final BitSet selectedIndices;
    final BitSetReadOnlyUnbackedObservableList selectedIndicesSeq;
    private final ReadOnlyUnbackedObservableList<T> selectedItemsSeq;
    ListChangeListener.Change selectedItemChange;
    private int atomicityCount = 0;

    public MultipleSelectionModelBase() {
        this.selectedIndexProperty().addListener(observable -> this.setSelectedItem(this.getModelItem(this.getSelectedIndex())));
        this.selectedIndices = new BitSet();
        this.selectedIndicesSeq = new BitSetReadOnlyUnbackedObservableList(this.selectedIndices);
        final MappingChange.Map<Integer, Object> map = n2 -> this.getModelItem((int)n2);
        this.selectedIndicesSeq.addListener(new ListChangeListener<Integer>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends Integer> change) {
                boolean bl = false;
                while (change.next() && !bl) {
                    bl = change.wasAdded() || change.wasRemoved();
                }
                if (bl) {
                    if (MultipleSelectionModelBase.this.selectedItemChange != null) {
                        MultipleSelectionModelBase.this.selectedItemsSeq.callObservers(MultipleSelectionModelBase.this.selectedItemChange);
                    } else {
                        change.reset();
                        MultipleSelectionModelBase.this.selectedItemsSeq.callObservers(new MappingChange(change, map, MultipleSelectionModelBase.this.selectedItemsSeq));
                    }
                }
                change.reset();
            }
        });
        this.selectedItemsSeq = new ReadOnlyUnbackedObservableList<T>(){

            @Override
            public T get(int n2) {
                int n3 = MultipleSelectionModelBase.this.selectedIndicesSeq.get(n2);
                return MultipleSelectionModelBase.this.getModelItem(n3);
            }

            @Override
            public int size() {
                return MultipleSelectionModelBase.this.selectedIndices.cardinality();
            }
        };
    }

    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return this.selectedIndicesSeq;
    }

    @Override
    public ObservableList<T> getSelectedItems() {
        return this.selectedItemsSeq;
    }

    boolean isAtomic() {
        return this.atomicityCount > 0;
    }

    void startAtomic() {
        ++this.atomicityCount;
    }

    void stopAtomic() {
        this.atomicityCount = Math.max(0, --this.atomicityCount);
    }

    protected abstract int getItemCount();

    protected abstract T getModelItem(int var1);

    protected abstract void focus(int var1);

    protected abstract int getFocusedIndex();

    void shiftSelection(int n2, int n3, Callback<ShiftParams, Void> callback) {
        int n4;
        if (n2 < 0) {
            return;
        }
        if (n3 == 0) {
            return;
        }
        int n5 = this.selectedIndices.cardinality();
        if (n5 == 0) {
            return;
        }
        int n6 = this.selectedIndices.size();
        int[] arrn = new int[n6];
        int n7 = 0;
        boolean bl2 = false;
        if (n3 > 0) {
            for (n4 = n6 - 1; n4 >= n2 && n4 >= 0; --n4) {
                boolean bl = this.selectedIndices.get(n4);
                if (callback == null) {
                    this.selectedIndices.clear(n4);
                    this.selectedIndices.set(n4 + n3, bl);
                } else {
                    callback.call(new ShiftParams(n4, n4 + n3, bl));
                }
                if (!bl) continue;
                arrn[n7++] = n4 + 1;
                bl2 = true;
            }
            this.selectedIndices.clear(n2);
        } else if (n3 < 0) {
            for (n4 = n2; n4 < n6; ++n4) {
                if (n4 + n3 < 0 || n4 + 1 + n3 < n2) continue;
                boolean bl = this.selectedIndices.get(n4 + 1);
                if (callback == null) {
                    this.selectedIndices.clear(n4 + 1);
                    this.selectedIndices.set(n4 + 1 + n3, bl);
                } else {
                    callback.call(new ShiftParams(n4 + 1, n4 + 1 + n3, bl));
                }
                if (!bl) continue;
                arrn[n7++] = n4;
                bl2 = true;
            }
        }
        n4 = this.getSelectedIndex();
        if (n4 >= n2 && n4 > -1) {
            int n8 = Math.max(0, n4 + n3);
            this.setSelectedIndex(n8);
            if (bl2) {
                this.selectedIndices.set(n8, true);
            } else {
                this.select(n8);
            }
        }
        if (bl2) {
            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimplePermutationChange<Integer>(0, n5, arrn, this.selectedIndicesSeq));
        }
    }

    @Override
    public void clearAndSelect(int n2) {
        ListChangeListener.Change<Integer> change;
        if (n2 < 0 || n2 >= this.getItemCount()) {
            this.clearSelection();
            return;
        }
        boolean bl = this.isSelected(n2);
        if (bl && this.getSelectedIndices().size() == 1 && this.getSelectedItem() == this.getModelItem(n2)) {
            return;
        }
        BitSet bitSet = new BitSet();
        bitSet.or(this.selectedIndices);
        bitSet.clear(n2);
        BitSetReadOnlyUnbackedObservableList bitSetReadOnlyUnbackedObservableList = new BitSetReadOnlyUnbackedObservableList(bitSet);
        this.startAtomic();
        this.clearSelection();
        this.select(n2);
        this.stopAtomic();
        if (bl) {
            change = ControlUtils.buildClearAndSelectChange(this.selectedIndicesSeq, bitSetReadOnlyUnbackedObservableList, n2);
        } else {
            int n3 = this.selectedIndicesSeq.indexOf(n2);
            change = new NonIterableChange.GenericAddRemoveChange<Integer>(n3, n3 + 1, bitSetReadOnlyUnbackedObservableList, this.selectedIndicesSeq);
        }
        this.selectedIndicesSeq.callObservers(change);
    }

    @Override
    public void select(int n2) {
        if (n2 == -1) {
            this.clearSelection();
            return;
        }
        if (n2 < 0 || n2 >= this.getItemCount()) {
            return;
        }
        boolean bl = n2 == this.getSelectedIndex();
        Object t2 = this.getSelectedItem();
        T t3 = this.getModelItem(n2);
        boolean bl2 = t3 != null && t3.equals(t2);
        boolean bl3 = bl && !bl2;
        this.startAtomic();
        if (!this.selectedIndices.get(n2)) {
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
                this.quietClearSelection();
            }
            this.selectedIndices.set(n2);
        }
        this.setSelectedIndex(n2);
        this.focus(n2);
        this.stopAtomic();
        if (!this.isAtomic()) {
            int n3 = Math.max(0, this.selectedIndicesSeq.indexOf(n2));
            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimpleAddChange<Integer>(n3, n3 + 1, this.selectedIndicesSeq));
        }
        if (bl3) {
            this.setSelectedItem(t3);
        }
    }

    @Override
    public void select(T t2) {
        if (t2 == null && this.getSelectionMode() == SelectionMode.SINGLE) {
            this.clearSelection();
            return;
        }
        Object object = null;
        int n2 = this.getItemCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            object = this.getModelItem(i2);
            if (object == null || !object.equals(t2)) continue;
            if (this.isSelected(i2)) {
                return;
            }
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
                this.quietClearSelection();
            }
            this.select(i2);
            return;
        }
        this.setSelectedIndex(-1);
        this.setSelectedItem(t2);
    }

    @Override
    public void selectIndices(int n2, int ... arrn) {
        if (arrn == null || arrn.length == 0) {
            this.select(n2);
            return;
        }
        int n3 = this.getItemCount();
        if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
            for (int i2 = arrn.length - 1; i2 >= 0; --i2) {
                int n4 = arrn[i2];
                if (n4 < 0 || n4 >= n3) continue;
                this.selectedIndices.set(n4);
                this.select(n4);
                break;
            }
            if (this.selectedIndices.isEmpty() && n2 > 0 && n2 < n3) {
                this.selectedIndices.set(n2);
                this.select(n2);
            }
            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimpleAddChange<Integer>(0, 1, this.selectedIndicesSeq));
        } else {
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            int n5 = -1;
            if (n2 >= 0 && n2 < n3) {
                n5 = n2;
                if (!this.selectedIndices.get(n2)) {
                    this.selectedIndices.set(n2);
                    arrayList.add(n2);
                }
            }
            for (int i3 = 0; i3 < arrn.length; ++i3) {
                int n6 = arrn[i3];
                if (n6 < 0 || n6 >= n3) continue;
                n5 = n6;
                if (this.selectedIndices.get(n6)) continue;
                this.selectedIndices.set(n6);
                arrayList.add(n6);
            }
            if (n5 != -1) {
                this.setSelectedIndex(n5);
                this.focus(n5);
                this.setSelectedItem(this.getModelItem(n5));
            }
            Collections.sort(arrayList);
            ListChangeListener.Change<Integer> change = MultipleSelectionModelBase.createRangeChange(this.selectedIndicesSeq, arrayList, false);
            this.selectedIndicesSeq.callObservers(change);
        }
    }

    static ListChangeListener.Change<Integer> createRangeChange(final ObservableList<Integer> observableList, final List<Integer> list, final boolean bl) {
        ListChangeListener.Change<Integer> change = new ListChangeListener.Change<Integer>(observableList){
            private final int[] EMPTY_PERM;
            private final int addedSize;
            private boolean invalid;
            private int pos;
            private int from;
            private int to;
            {
                super(observableList3);
                this.EMPTY_PERM = new int[0];
                this.addedSize = list.size();
                this.invalid = true;
                this.from = this.pos = 0;
                this.to = this.pos;
            }

            @Override
            public int getFrom() {
                this.checkState();
                return this.from;
            }

            @Override
            public int getTo() {
                this.checkState();
                return this.to;
            }

            @Override
            public List<Integer> getRemoved() {
                this.checkState();
                return Collections.emptyList();
            }

            @Override
            protected int[] getPermutation() {
                this.checkState();
                return this.EMPTY_PERM;
            }

            @Override
            public int getAddedSize() {
                return this.to - this.from;
            }

            @Override
            public boolean next() {
                if (this.pos >= this.addedSize) {
                    return false;
                }
                int n2 = (Integer)list.get(this.pos++);
                this.from = observableList.indexOf(n2);
                this.to = this.from + 1;
                int n3 = n2;
                while (this.pos < this.addedSize) {
                    int n4 = n3;
                    n3 = (Integer)list.get(this.pos++);
                    ++this.to;
                    if (!bl || n4 == n3 - 1) continue;
                    break;
                }
                if (this.invalid) {
                    this.invalid = false;
                    return true;
                }
                return bl && this.pos < this.addedSize;
            }

            @Override
            public void reset() {
                this.invalid = true;
                this.pos = 0;
                this.to = 0;
                this.from = 0;
            }

            private void checkState() {
                if (this.invalid) {
                    throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
                }
            }
        };
        return change;
    }

    @Override
    public void selectAll() {
        if (this.getSelectionMode() == SelectionMode.SINGLE) {
            return;
        }
        if (this.getItemCount() <= 0) {
            return;
        }
        int n2 = this.getItemCount();
        int n3 = this.getFocusedIndex();
        this.clearSelection();
        this.selectedIndices.set(0, n2, true);
        this.selectedIndicesSeq.callObservers(new NonIterableChange.SimpleAddChange<Integer>(0, n2, this.selectedIndicesSeq));
        if (n3 == -1) {
            this.setSelectedIndex(n2 - 1);
            this.focus(n2 - 1);
        } else {
            this.setSelectedIndex(n3);
            this.focus(n3);
        }
    }

    @Override
    public void selectFirst() {
        if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
        }
        if (this.getItemCount() > 0) {
            this.select(0);
        }
    }

    @Override
    public void selectLast() {
        int n2;
        if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
        }
        if ((n2 = this.getItemCount()) > 0 && this.getSelectedIndex() < n2 - 1) {
            this.select(n2 - 1);
        }
    }

    @Override
    public void clearSelection(int n2) {
        if (n2 < 0) {
            return;
        }
        boolean bl = this.selectedIndices.isEmpty();
        this.selectedIndices.clear(n2);
        if (!bl && this.selectedIndices.isEmpty()) {
            this.clearSelection();
        }
        if (!this.isAtomic()) {
            this.selectedIndicesSeq.callObservers(new NonIterableChange.GenericAddRemoveChange<Integer>(n2, n2, Collections.singletonList(n2), this.selectedIndicesSeq));
        }
    }

    @Override
    public void clearSelection() {
        BitSetReadOnlyUnbackedObservableList bitSetReadOnlyUnbackedObservableList = new BitSetReadOnlyUnbackedObservableList((BitSet)this.selectedIndices.clone());
        this.quietClearSelection();
        if (!this.isAtomic()) {
            this.setSelectedIndex(-1);
            this.focus(-1);
            this.selectedIndicesSeq.callObservers(new NonIterableChange.GenericAddRemoveChange<Integer>(0, 0, bitSetReadOnlyUnbackedObservableList, this.selectedIndicesSeq));
        }
    }

    private void quietClearSelection() {
        this.selectedIndices.clear();
    }

    @Override
    public boolean isSelected(int n2) {
        if (n2 >= 0 && n2 < this.selectedIndices.length()) {
            return this.selectedIndices.get(n2);
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.selectedIndices.isEmpty();
    }

    @Override
    public void selectPrevious() {
        int n2 = this.getFocusedIndex();
        if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
        }
        if (n2 == -1) {
            this.select(this.getItemCount() - 1);
        } else if (n2 > 0) {
            this.select(n2 - 1);
        }
    }

    @Override
    public void selectNext() {
        int n2 = this.getFocusedIndex();
        if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
        }
        if (n2 == -1) {
            this.select(0);
        } else if (n2 != this.getItemCount() - 1) {
            this.select(n2 + 1);
        }
    }

    class BitSetReadOnlyUnbackedObservableList
    extends ReadOnlyUnbackedObservableList<Integer> {
        private final BitSet bitset;
        private int lastGetIndex = -1;
        private int lastGetValue = -1;

        public BitSetReadOnlyUnbackedObservableList(BitSet bitSet) {
            this.bitset = bitSet;
        }

        @Override
        public Integer get(int n2) {
            int n3 = MultipleSelectionModelBase.this.getItemCount();
            if (n2 < 0 || n2 >= n3) {
                return -1;
            }
            if (n2 == this.lastGetIndex + 1 && this.lastGetValue < n3) {
                ++this.lastGetIndex;
                this.lastGetValue = this.bitset.nextSetBit(this.lastGetValue + 1);
                return this.lastGetValue;
            }
            if (n2 == this.lastGetIndex - 1 && this.lastGetValue > 0) {
                --this.lastGetIndex;
                this.lastGetValue = this.bitset.previousSetBit(this.lastGetValue - 1);
                return this.lastGetValue;
            }
            this.lastGetIndex = 0;
            this.lastGetValue = this.bitset.nextSetBit(0);
            while (this.lastGetValue >= 0 || this.lastGetIndex == n2) {
                if (this.lastGetIndex == n2) {
                    return this.lastGetValue;
                }
                ++this.lastGetIndex;
                this.lastGetValue = this.bitset.nextSetBit(this.lastGetValue + 1);
            }
            return -1;
        }

        @Override
        public int size() {
            return this.bitset.cardinality();
        }

        @Override
        public boolean contains(Object object) {
            if (object instanceof Number) {
                Number number = (Number)object;
                int n2 = number.intValue();
                return n2 >= 0 && n2 < this.bitset.length() && this.bitset.get(n2);
            }
            return false;
        }

        public void reset() {
            this.lastGetIndex = -1;
            this.lastGetValue = -1;
        }
    }

    static class ShiftParams {
        private final int clearIndex;
        private final int setIndex;
        private final boolean selected;

        ShiftParams(int n2, int n3, boolean bl) {
            this.clearIndex = n2;
            this.setIndex = n3;
            this.selected = bl;
        }

        public final int getClearIndex() {
            return this.clearIndex;
        }

        public final int getSetIndex() {
            return this.setIndex;
        }

        public final boolean isSelected() {
            return this.selected;
        }
    }
}

