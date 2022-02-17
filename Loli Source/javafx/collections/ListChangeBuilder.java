/*
 * Decompiled with CFR 0.150.
 */
package javafx.collections;

import com.sun.javafx.collections.ChangeHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

final class ListChangeBuilder<E> {
    private static final int[] EMPTY_PERM = new int[0];
    private final ObservableListBase<E> list;
    private int changeLock;
    private List<SubChange<E>> addRemoveChanges;
    private List<SubChange<E>> updateChanges;
    private SubChange<E> permutationChange;

    private void checkAddRemoveList() {
        if (this.addRemoveChanges == null) {
            this.addRemoveChanges = new ArrayList<SubChange<E>>();
        }
    }

    private void checkState() {
        if (this.changeLock == 0) {
            throw new IllegalStateException("beginChange was not called on this builder");
        }
    }

    private int findSubChange(int n2, List<SubChange<E>> list) {
        int n3 = 0;
        int n4 = list.size() - 1;
        while (n3 <= n4) {
            int n5 = (n3 + n4) / 2;
            SubChange<E> subChange = list.get(n5);
            if (n2 >= subChange.to) {
                n3 = n5 + 1;
                continue;
            }
            if (n2 < subChange.from) {
                n4 = n5 - 1;
                continue;
            }
            return n5;
        }
        return ~n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void insertUpdate(int n2) {
        SubChange<E> subChange;
        int n3 = this.findSubChange(n2, this.updateChanges);
        if (n3 >= 0) return;
        if ((n3 ^= 0xFFFFFFFF) > 0) {
            subChange = this.updateChanges.get(n3 - 1);
            if (subChange.to == n2) {
                subChange.to = n2 + 1;
                return;
            }
        }
        if (n3 < this.updateChanges.size()) {
            subChange = this.updateChanges.get(n3);
            if (subChange.from == n2 + 1) {
                subChange.from = n2;
                return;
            }
        }
        this.updateChanges.add(n3, new SubChange(n2, n2 + 1, null, EMPTY_PERM, true));
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void insertRemoved(int var1_1, E var2_2) {
        block6: {
            block4: {
                block5: {
                    var3_3 = this.findSubChange(var1_1, this.addRemoveChanges);
                    if (var3_3 >= 0) break block4;
                    if ((var3_3 ^= -1) <= 0) break block5;
                    var4_4 = this.addRemoveChanges.get(var3_3 - 1);
                    if (var4_4.to != var1_1) break block5;
                    var4_4.removed.add(var2_2);
                    --var3_3;
                    break block6;
                }
                if (var3_3 >= this.addRemoveChanges.size()) ** GOTO lbl-1000
                var4_4 = this.addRemoveChanges.get(var3_3);
                if (var4_4.from == var1_1 + 1) {
                    --var4_4.from;
                    --var4_4.to;
                    var4_4.removed.add(0, var2_2);
                } else lbl-1000:
                // 2 sources

                {
                    var5_6 = new ArrayList<E>();
                    var5_6.add(var2_2);
                    this.addRemoveChanges.add(var3_3, new SubChange<E>(var1_1, var1_1, var5_6, ListChangeBuilder.EMPTY_PERM, false));
                }
                break block6;
            }
            var4_4 = this.addRemoveChanges.get(var3_3);
            --var4_4.to;
            if (var4_4.from == var4_4.to && (var4_4.removed == null || var4_4.removed.isEmpty())) {
                this.addRemoveChanges.remove(var3_3);
            }
        }
        var4_5 = var3_3 + 1;
        while (var4_5 < this.addRemoveChanges.size()) {
            var5_6 = this.addRemoveChanges.get(var4_5);
            --var5_6.from;
            --var5_6.to;
            ++var4_5;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void insertAdd(int var1_1, int var2_2) {
        block4: {
            block3: {
                var3_3 = this.findSubChange(var1_1, this.addRemoveChanges);
                var4_4 = var2_2 - var1_1;
                if (var3_3 >= 0) break block3;
                if ((var3_3 ^= -1) <= 0) ** GOTO lbl-1000
                var5_5 = this.addRemoveChanges.get(var3_3 - 1);
                if (var5_5.to == var1_1) {
                    var5_5.to = var2_2;
                    --var3_3;
                } else lbl-1000:
                // 2 sources

                {
                    this.addRemoveChanges.add(var3_3, new SubChange<E>(var1_1, var2_2, new ArrayList<E>(), ListChangeBuilder.EMPTY_PERM, false));
                }
                break block4;
            }
            var5_5 = this.addRemoveChanges.get(var3_3);
            var5_5.to += var4_4;
        }
        var5_6 = var3_3 + 1;
        while (var5_6 < this.addRemoveChanges.size()) {
            var6_7 = this.addRemoveChanges.get(var5_6);
            var6_7.from += var4_4;
            var6_7.to += var4_4;
            ++var5_6;
        }
    }

    private int compress(List<SubChange<E>> list) {
        int n2 = 0;
        SubChange<E> subChange = list.get(0);
        int n3 = list.size();
        for (int i2 = 1; i2 < n3; ++i2) {
            SubChange<E> subChange2 = list.get(i2);
            if (subChange.to == subChange2.from) {
                subChange.to = subChange2.to;
                if (subChange.removed != null || subChange2.removed != null) {
                    if (subChange.removed == null) {
                        subChange.removed = new ArrayList();
                    }
                    subChange.removed.addAll(subChange2.removed);
                }
                list.set(i2, null);
                ++n2;
                continue;
            }
            subChange = subChange2;
        }
        return n2;
    }

    ListChangeBuilder(ObservableListBase<E> observableListBase) {
        this.list = observableListBase;
    }

    public void nextRemove(int n2, E e2) {
        SubChange<E> subChange;
        this.checkState();
        this.checkAddRemoveList();
        SubChange<E> subChange2 = subChange = this.addRemoveChanges.isEmpty() ? null : this.addRemoveChanges.get(this.addRemoveChanges.size() - 1);
        if (subChange != null && subChange.to == n2) {
            subChange.removed.add(e2);
        } else if (subChange != null && subChange.from == n2 + 1) {
            --subChange.from;
            --subChange.to;
            subChange.removed.add(0, e2);
        } else {
            this.insertRemoved(n2, e2);
        }
        if (this.updateChanges != null && !this.updateChanges.isEmpty()) {
            int n3 = this.findSubChange(n2, this.updateChanges);
            if (n3 < 0) {
                n3 ^= 0xFFFFFFFF;
            } else {
                SubChange<E> subChange3 = this.updateChanges.get(n3);
                if (subChange3.from == subChange3.to - 1) {
                    this.updateChanges.remove(n3);
                } else {
                    --subChange3.to;
                    ++n3;
                }
            }
            for (int i2 = n3; i2 < this.updateChanges.size(); ++i2) {
                --this.updateChanges.get((int)i2).from;
                --this.updateChanges.get((int)i2).to;
            }
        }
    }

    public void nextRemove(int n2, List<? extends E> list) {
        this.checkState();
        for (int i2 = 0; i2 < list.size(); ++i2) {
            this.nextRemove(n2, list.get(i2));
        }
    }

    public void nextAdd(int n2, int n3) {
        this.checkState();
        this.checkAddRemoveList();
        SubChange<E> subChange = this.addRemoveChanges.isEmpty() ? null : this.addRemoveChanges.get(this.addRemoveChanges.size() - 1);
        int n4 = n3 - n2;
        if (subChange != null && subChange.to == n2) {
            subChange.to = n3;
        } else if (subChange != null && n2 >= subChange.from && n2 < subChange.to) {
            subChange.to += n4;
        } else {
            this.insertAdd(n2, n3);
        }
        if (this.updateChanges != null && !this.updateChanges.isEmpty()) {
            int n5 = this.findSubChange(n2, this.updateChanges);
            if (n5 < 0) {
                n5 ^= 0xFFFFFFFF;
            } else {
                SubChange<E> subChange2 = this.updateChanges.get(n5);
                this.updateChanges.add(n5 + 1, new SubChange(n3, subChange2.to + n3 - n2, null, EMPTY_PERM, true));
                subChange2.to = n2;
                n5 += 2;
            }
            for (int i2 = n5; i2 < this.updateChanges.size(); ++i2) {
                this.updateChanges.get((int)i2).from += n4;
                this.updateChanges.get((int)i2).to += n4;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    public void nextPermutation(int n2, int n3, int[] arrn) {
        int n4;
        int n5;
        int n6;
        int n7;
        Cloneable cloneable;
        this.checkState();
        int n8 = n2;
        int n9 = n3;
        int[] arrn2 = arrn;
        if (this.addRemoveChanges != null && !this.addRemoveChanges.isEmpty()) {
            int subChange;
            void arrn4;
            int[] arrn3 = new int[this.list.size()];
            cloneable = new TreeSet();
            n7 = 0;
            n6 = 0;
            boolean n10 = false;
            n5 = this.addRemoveChanges.size();
            while (++arrn4 < n5) {
                int n11;
                SubChange<E> subChange2 = this.addRemoveChanges.get((int)arrn4);
                for (n11 = n7; n11 < subChange2.from; ++n11) {
                    arrn3[n11 < n2 || n11 >= n3 ? n11 : arrn[n11 - n2]] = n11 + n6;
                }
                for (n11 = subChange2.from; n11 < subChange2.to; ++n11) {
                    arrn3[n11 < n2 || n11 >= n3 ? n11 : arrn[n11 - n2]] = -1;
                }
                n7 = subChange2.to;
                n11 = subChange2.removed != null ? subChange2.removed.size() : 0;
                int n12 = subChange2.from + n6 + n11;
                for (int i2 = subChange2.from + n6; i2 < n12; ++i2) {
                    cloneable.add(i2);
                }
                n6 += n11 - (subChange2.to - subChange2.from);
            }
            int n15 = n7;
            while (++subChange < arrn3.length) {
                arrn3[subChange < n2 || subChange >= n3 ? subChange : arrn[subChange - n2]] = subChange + n6;
            }
            int[] n16 = new int[this.list.size() + n6];
            n5 = 0;
            for (n4 = 0; n4 < n16.length; ++n4) {
                if (cloneable.contains(n4)) {
                    n16[n4] = n4;
                    continue;
                }
                while (arrn3[n5] == -1) {
                    ++n5;
                }
                n16[arrn3[n5++]] = n4;
            }
            n8 = 0;
            n9 = n16.length;
            arrn2 = n16;
        }
        if (this.permutationChange != null) {
            if (n8 == this.permutationChange.from && n9 == this.permutationChange.to) {
                for (int i3 = 0; i3 < arrn2.length; ++i3) {
                    this.permutationChange.perm[i3] = arrn2[this.permutationChange.perm[i3] - n8];
                }
            } else {
                int n13 = Math.max(this.permutationChange.to, n9);
                int n14 = Math.min(this.permutationChange.from, n8);
                int[] arrn5 = new int[n13 - n14];
                for (n6 = n14; n6 < n13; ++n6) {
                    int entry;
                    arrn5[n6 - n14] = n6 < this.permutationChange.from || n6 >= this.permutationChange.to ? arrn2[n6 - n8] : ((entry = this.permutationChange.perm[n6 - this.permutationChange.from]) < n8 || entry >= n9 ? entry : arrn2[entry - n8]);
                }
                this.permutationChange.from = n14;
                this.permutationChange.to = n13;
                this.permutationChange.perm = arrn5;
            }
        } else {
            this.permutationChange = new SubChange(n8, n9, null, arrn2, false);
        }
        if (this.addRemoveChanges != null && !this.addRemoveChanges.isEmpty()) {
            TreeSet<Integer> treeSet = new TreeSet<Integer>();
            cloneable = new HashMap();
            n6 = this.addRemoveChanges.size();
            for (n7 = 0; n7 < n6; ++n7) {
                SubChange<E> i5 = this.addRemoveChanges.get(n7);
                for (n5 = i5.from; n5 < i5.to; ++n5) {
                    if (n5 < n2 || n5 >= n3) {
                        treeSet.add(n5);
                        continue;
                    }
                    treeSet.add(arrn[n5 - n2]);
                }
                if (i5.removed == null) continue;
                if (i5.from < n2 || i5.from >= n3) {
                    cloneable.put(i5.from, i5.removed);
                    continue;
                }
                cloneable.put(arrn[i5.from - n2], i5.removed);
            }
            this.addRemoveChanges.clear();
            SubChange subChange = null;
            for (Integer n13 : treeSet) {
                List list;
                if (subChange == null || subChange.to != n13) {
                    subChange = new SubChange(n13, n13 + 1, null, EMPTY_PERM, false);
                    this.addRemoveChanges.add(subChange);
                } else {
                    subChange.to = n13 + 1;
                }
                if ((list = (List)cloneable.remove(n13)) == null) continue;
                if (subChange.removed != null) {
                    subChange.removed.addAll(list);
                    continue;
                }
                subChange.removed = list;
            }
            for (Map.Entry entry : cloneable.entrySet()) {
                Integer n14 = (Integer)entry.getKey();
                n4 = this.findSubChange(n14, this.addRemoveChanges);
                assert (n4 < 0);
                this.addRemoveChanges.add(~n4, new SubChange(n14, n14, (List)entry.getValue(), new int[0], false));
            }
        }
        if (this.updateChanges != null && !this.updateChanges.isEmpty()) {
            TreeSet<Integer> treeSet = new TreeSet<Integer>();
            n7 = this.updateChanges.size();
            for (int i4 = 0; i4 < n7; ++i4) {
                void var11_34;
                SubChange<E> subChange = this.updateChanges.get(i4);
                int n15 = subChange.from;
                while (++var11_34 < subChange.to) {
                    if (var11_34 < n2 || var11_34 >= n3) {
                        treeSet.add((int)var11_34);
                        continue;
                    }
                    treeSet.add(arrn[var11_34 - n2]);
                }
            }
            this.updateChanges.clear();
            SubChange subChange = null;
            for (Integer n18 : treeSet) {
                if (subChange == null || subChange.to != n18) {
                    subChange = new SubChange(n18, n18 + 1, null, EMPTY_PERM, true);
                    this.updateChanges.add(subChange);
                    continue;
                }
                subChange.to = n18 + 1;
            }
        }
    }

    public void nextReplace(int n2, int n3, List<? extends E> list) {
        this.nextRemove(n2, list);
        this.nextAdd(n2, n3);
    }

    public void nextSet(int n2, E e2) {
        this.nextRemove(n2, e2);
        this.nextAdd(n2, n2 + 1);
    }

    public void nextUpdate(int n2) {
        SubChange<E> subChange;
        this.checkState();
        if (this.updateChanges == null) {
            this.updateChanges = new ArrayList<SubChange<E>>();
        }
        SubChange<E> subChange2 = subChange = this.updateChanges.isEmpty() ? null : this.updateChanges.get(this.updateChanges.size() - 1);
        if (subChange != null && subChange.to == n2) {
            subChange.to = n2 + 1;
        } else {
            this.insertUpdate(n2);
        }
    }

    private void commit() {
        boolean bl;
        boolean bl2 = this.addRemoveChanges != null && !this.addRemoveChanges.isEmpty();
        boolean bl3 = bl = this.updateChanges != null && !this.updateChanges.isEmpty();
        if (this.changeLock == 0 && (bl2 || bl || this.permutationChange != null)) {
            int n2 = (this.updateChanges != null ? this.updateChanges.size() : 0) + (this.addRemoveChanges != null ? this.addRemoveChanges.size() : 0) + (this.permutationChange != null ? 1 : 0);
            if (n2 == 1) {
                if (bl2) {
                    this.list.fireChange(new SingleChange<E>(ListChangeBuilder.finalizeSubChange(this.addRemoveChanges.get(0)), this.list));
                    this.addRemoveChanges.clear();
                } else if (bl) {
                    this.list.fireChange(new SingleChange<E>(ListChangeBuilder.finalizeSubChange(this.updateChanges.get(0)), this.list));
                    this.updateChanges.clear();
                } else {
                    this.list.fireChange(new SingleChange<E>(ListChangeBuilder.finalizeSubChange(this.permutationChange), this.list));
                    this.permutationChange = null;
                }
            } else {
                SubChange<E> subChange;
                int n3;
                int n4;
                int n5;
                if (bl) {
                    n5 = this.compress(this.updateChanges);
                    n2 -= n5;
                }
                if (bl2) {
                    n5 = this.compress(this.addRemoveChanges);
                    n2 -= n5;
                }
                SubChange[] arrsubChange = new SubChange[n2];
                int n6 = 0;
                if (this.permutationChange != null) {
                    arrsubChange[n6++] = this.permutationChange;
                }
                if (bl2) {
                    n4 = this.addRemoveChanges.size();
                    for (n3 = 0; n3 < n4; ++n3) {
                        subChange = this.addRemoveChanges.get(n3);
                        if (subChange == null) continue;
                        arrsubChange[n6++] = subChange;
                    }
                }
                if (bl) {
                    n4 = this.updateChanges.size();
                    for (n3 = 0; n3 < n4; ++n3) {
                        subChange = this.updateChanges.get(n3);
                        if (subChange == null) continue;
                        arrsubChange[n6++] = subChange;
                    }
                }
                this.list.fireChange(new IterableChange(ListChangeBuilder.finalizeSubChangeArray(arrsubChange), this.list));
                if (this.addRemoveChanges != null) {
                    this.addRemoveChanges.clear();
                }
                if (this.updateChanges != null) {
                    this.updateChanges.clear();
                }
                this.permutationChange = null;
            }
        }
    }

    public void beginChange() {
        ++this.changeLock;
    }

    public void endChange() {
        if (this.changeLock <= 0) {
            throw new IllegalStateException("Called endChange before beginChange");
        }
        --this.changeLock;
        this.commit();
    }

    private static <E> SubChange<E>[] finalizeSubChangeArray(SubChange<E>[] arrsubChange) {
        for (SubChange<E> subChange : arrsubChange) {
            ListChangeBuilder.finalizeSubChange(subChange);
        }
        return arrsubChange;
    }

    private static <E> SubChange<E> finalizeSubChange(SubChange<E> subChange) {
        if (subChange.perm == null) {
            subChange.perm = EMPTY_PERM;
        }
        subChange.removed = subChange.removed == null ? Collections.emptyList() : Collections.unmodifiableList(subChange.removed);
        return subChange;
    }

    private static class IterableChange<E>
    extends ListChangeListener.Change<E> {
        private SubChange[] changes;
        private int cursor = -1;

        private IterableChange(SubChange[] arrsubChange, ObservableList<E> observableList) {
            super(observableList);
            this.changes = arrsubChange;
        }

        @Override
        public boolean next() {
            if (this.cursor + 1 < this.changes.length) {
                ++this.cursor;
                return true;
            }
            return false;
        }

        @Override
        public void reset() {
            this.cursor = -1;
        }

        @Override
        public int getFrom() {
            this.checkState();
            return this.changes[this.cursor].from;
        }

        @Override
        public int getTo() {
            this.checkState();
            return this.changes[this.cursor].to;
        }

        @Override
        public List<E> getRemoved() {
            this.checkState();
            return this.changes[this.cursor].removed;
        }

        @Override
        protected int[] getPermutation() {
            this.checkState();
            return this.changes[this.cursor].perm;
        }

        @Override
        public boolean wasUpdated() {
            this.checkState();
            return this.changes[this.cursor].updated;
        }

        private void checkState() {
            if (this.cursor == -1) {
                throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ ");
            for (int i2 = 0; i2 < this.changes.length; ++i2) {
                if (this.changes[i2].perm.length != 0) {
                    stringBuilder.append(ChangeHelper.permChangeToString(this.changes[i2].perm));
                } else if (this.changes[i2].updated) {
                    stringBuilder.append(ChangeHelper.updateChangeToString(this.changes[i2].from, this.changes[i2].to));
                } else {
                    stringBuilder.append(ChangeHelper.addRemoveChangeToString(this.changes[i2].from, this.changes[i2].to, this.getList(), this.changes[i2].removed));
                }
                if (i2 == this.changes.length - 1) continue;
                stringBuilder.append(", ");
            }
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    private static class SingleChange<E>
    extends ListChangeListener.Change<E> {
        private final SubChange<E> change;
        private boolean onChange;

        public SingleChange(SubChange<E> subChange, ObservableListBase<E> observableListBase) {
            super(observableListBase);
            this.change = subChange;
        }

        @Override
        public boolean next() {
            if (this.onChange) {
                return false;
            }
            this.onChange = true;
            return true;
        }

        @Override
        public void reset() {
            this.onChange = false;
        }

        @Override
        public int getFrom() {
            this.checkState();
            return this.change.from;
        }

        @Override
        public int getTo() {
            this.checkState();
            return this.change.to;
        }

        @Override
        public List<E> getRemoved() {
            this.checkState();
            return this.change.removed;
        }

        @Override
        protected int[] getPermutation() {
            this.checkState();
            return this.change.perm;
        }

        @Override
        public boolean wasUpdated() {
            this.checkState();
            return this.change.updated;
        }

        private void checkState() {
            if (!this.onChange) {
                throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
            }
        }

        public String toString() {
            String string = this.change.perm.length != 0 ? ChangeHelper.permChangeToString(this.change.perm) : (this.change.updated ? ChangeHelper.updateChangeToString(this.change.from, this.change.to) : ChangeHelper.addRemoveChangeToString(this.change.from, this.change.to, this.getList(), this.change.removed));
            return "{ " + string + " }";
        }
    }

    private static class SubChange<E> {
        int from;
        int to;
        List<E> removed;
        int[] perm;
        boolean updated;

        public SubChange(int n2, int n3, List<E> list, int[] arrn, boolean bl) {
            this.from = n2;
            this.to = n3;
            this.removed = list;
            this.perm = arrn;
            this.updated = bl;
        }
    }
}

