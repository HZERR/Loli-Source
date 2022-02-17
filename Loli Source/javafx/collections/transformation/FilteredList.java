/*
 * Decompiled with CFR 0.150.
 */
package javafx.collections.transformation;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SortHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.function.Predicate;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

public final class FilteredList<E>
extends TransformationList<E, E> {
    private int[] filtered;
    private int size;
    private SortHelper helper;
    private static final Predicate ALWAYS_TRUE = object -> true;
    private ObjectProperty<Predicate<? super E>> predicate;

    public FilteredList(@NamedArg(value="source") ObservableList<E> observableList, @NamedArg(value="predicate") Predicate<? super E> predicate) {
        super(observableList);
        this.filtered = new int[observableList.size() * 3 / 2 + 1];
        if (predicate != null) {
            this.setPredicate(predicate);
        } else {
            this.size = 0;
            while (this.size < observableList.size()) {
                this.filtered[this.size] = this.size;
                ++this.size;
            }
        }
    }

    public FilteredList(@NamedArg(value="source") ObservableList<E> observableList) {
        this(observableList, null);
    }

    public final ObjectProperty<Predicate<? super E>> predicateProperty() {
        if (this.predicate == null) {
            this.predicate = new ObjectPropertyBase<Predicate<? super E>>(){

                @Override
                protected void invalidated() {
                    FilteredList.this.refilter();
                }

                @Override
                public Object getBean() {
                    return FilteredList.this;
                }

                @Override
                public String getName() {
                    return "predicate";
                }
            };
        }
        return this.predicate;
    }

    public final Predicate<? super E> getPredicate() {
        return this.predicate == null ? null : (Predicate)this.predicate.get();
    }

    public final void setPredicate(Predicate<? super E> predicate) {
        this.predicateProperty().set(predicate);
    }

    private Predicate<? super E> getPredicateImpl() {
        if (this.getPredicate() != null) {
            return this.getPredicate();
        }
        return ALWAYS_TRUE;
    }

    @Override
    protected void sourceChanged(ListChangeListener.Change<? extends E> change) {
        this.beginChange();
        while (change.next()) {
            if (change.wasPermutated()) {
                this.permutate(change);
                continue;
            }
            if (change.wasUpdated()) {
                this.update(change);
                continue;
            }
            this.addRemove(change);
        }
        this.endChange();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public E get(int n2) {
        if (n2 >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        return this.getSource().get(this.filtered[n2]);
    }

    @Override
    public int getSourceIndex(int n2) {
        if (n2 >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        return this.filtered[n2];
    }

    private SortHelper getSortHelper() {
        if (this.helper == null) {
            this.helper = new SortHelper();
        }
        return this.helper;
    }

    private int findPosition(int n2) {
        if (this.filtered.length == 0) {
            return 0;
        }
        if (n2 == 0) {
            return 0;
        }
        int n3 = Arrays.binarySearch(this.filtered, 0, this.size, n2);
        if (n3 < 0) {
            n3 ^= 0xFFFFFFFF;
        }
        return n3;
    }

    private void ensureSize(int n2) {
        if (this.filtered.length < n2) {
            int[] arrn = new int[n2 * 3 / 2 + 1];
            System.arraycopy(this.filtered, 0, arrn, 0, this.size);
            this.filtered = arrn;
        }
    }

    private void updateIndexes(int n2, int n3) {
        int n4 = n2;
        while (n4 < this.size) {
            int n5 = n4++;
            this.filtered[n5] = this.filtered[n5] + n3;
        }
    }

    private void permutate(ListChangeListener.Change<? extends E> change) {
        int n2 = this.findPosition(change.getFrom());
        int n3 = this.findPosition(change.getTo());
        if (n3 > n2) {
            for (int i2 = n2; i2 < n3; ++i2) {
                this.filtered[i2] = change.getPermutation(this.filtered[i2]);
            }
            int[] arrn = this.getSortHelper().sort(this.filtered, n2, n3);
            this.nextPermutation(n2, n3, arrn);
        }
    }

    private void addRemove(ListChangeListener.Change<? extends E> change) {
        int n2;
        Predicate<E> predicate = this.getPredicateImpl();
        this.ensureSize(this.getSource().size());
        int n3 = this.findPosition(change.getFrom());
        int n4 = this.findPosition(change.getFrom() + change.getRemovedSize());
        for (n2 = n3; n2 < n4; ++n2) {
            this.nextRemove(n3, change.getRemoved().get(this.filtered[n2] - change.getFrom()));
        }
        this.updateIndexes(n4, change.getAddedSize() - change.getRemovedSize());
        n2 = n3;
        int n5 = change.getFrom();
        ListIterator listIterator = this.getSource().listIterator(n5);
        while (n2 < n4 && listIterator.nextIndex() < change.getTo()) {
            if (!predicate.test(listIterator.next())) continue;
            this.filtered[n2] = listIterator.previousIndex();
            this.nextAdd(n2, n2 + 1);
            ++n2;
        }
        if (n2 < n4) {
            System.arraycopy(this.filtered, n4, this.filtered, n2, this.size - n4);
            this.size -= n4 - n2;
        } else {
            while (listIterator.nextIndex() < change.getTo()) {
                if (predicate.test(listIterator.next())) {
                    System.arraycopy(this.filtered, n2, this.filtered, n2 + 1, this.size - n2);
                    this.filtered[n2] = listIterator.previousIndex();
                    this.nextAdd(n2, n2 + 1);
                    ++n2;
                    ++this.size;
                }
                ++n5;
            }
        }
    }

    private void update(ListChangeListener.Change<? extends E> change) {
        int n2;
        Predicate<E> predicate = this.getPredicateImpl();
        this.ensureSize(this.getSource().size());
        int n3 = change.getTo();
        int n4 = this.findPosition(n2);
        int n5 = this.findPosition(n3);
        ListIterator listIterator = this.getSource().listIterator(n2);
        int n6 = n4;
        for (n2 = change.getFrom(); n6 < n5 || n2 < n3; ++n2) {
            Object e2 = listIterator.next();
            if (n6 < this.size && this.filtered[n6] == n2) {
                if (!predicate.test(e2)) {
                    this.nextRemove(n6, e2);
                    System.arraycopy(this.filtered, n6 + 1, this.filtered, n6, this.size - n6 - 1);
                    --this.size;
                    --n5;
                    continue;
                }
                this.nextUpdate(n6);
                ++n6;
                continue;
            }
            if (!predicate.test(e2)) continue;
            this.nextAdd(n6, n6 + 1);
            System.arraycopy(this.filtered, n6, this.filtered, n6 + 1, this.size - n6);
            this.filtered[n6] = n2;
            ++this.size;
            ++n6;
            ++n5;
        }
    }

    private void refilter() {
        this.ensureSize(this.getSource().size());
        ArrayList arrayList = null;
        if (this.hasListeners()) {
            arrayList = new ArrayList(this);
        }
        this.size = 0;
        int n2 = 0;
        Predicate<E> predicate = this.getPredicateImpl();
        for (Object e2 : this.getSource()) {
            if (predicate.test(e2)) {
                this.filtered[this.size++] = n2;
            }
            ++n2;
        }
        if (this.hasListeners()) {
            this.fireChange(new NonIterableChange.GenericAddRemoveChange(0, this.size, arrayList, this));
        }
    }
}

