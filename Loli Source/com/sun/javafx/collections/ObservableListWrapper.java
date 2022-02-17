/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ElementObserver;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SortHelper;
import com.sun.javafx.collections.SortableList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class ObservableListWrapper<E>
extends ModifiableObservableListBase<E>
implements ObservableList<E>,
SortableList<E>,
RandomAccess {
    private final List<E> backingList;
    private final ElementObserver elementObserver;
    private SortHelper helper;

    public ObservableListWrapper(List<E> list) {
        this.backingList = list;
        this.elementObserver = null;
    }

    public ObservableListWrapper(List<E> list, Callback<E, Observable[]> callback) {
        this.backingList = list;
        this.elementObserver = new ElementObserver<E>(callback, new Callback<E, InvalidationListener>(){

            @Override
            public InvalidationListener call(final E e2) {
                return new InvalidationListener(){

                    @Override
                    public void invalidated(Observable observable) {
                        ObservableListWrapper.this.beginChange();
                        int n2 = ObservableListWrapper.this.size();
                        for (int i2 = 0; i2 < n2; ++i2) {
                            if (ObservableListWrapper.this.get(i2) != e2) continue;
                            ObservableListWrapper.this.nextUpdate(i2);
                        }
                        ObservableListWrapper.this.endChange();
                    }
                };
            }
        }, this);
        int n2 = this.backingList.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.elementObserver.attachListener(this.backingList.get(i2));
        }
    }

    @Override
    public E get(int n2) {
        return this.backingList.get(n2);
    }

    @Override
    public int size() {
        return this.backingList.size();
    }

    @Override
    protected void doAdd(int n2, E e2) {
        if (this.elementObserver != null) {
            this.elementObserver.attachListener(e2);
        }
        this.backingList.add(n2, e2);
    }

    @Override
    protected E doSet(int n2, E e2) {
        E e3 = this.backingList.set(n2, e2);
        if (this.elementObserver != null) {
            this.elementObserver.detachListener(e3);
            this.elementObserver.attachListener(e2);
        }
        return e3;
    }

    @Override
    protected E doRemove(int n2) {
        E e2 = this.backingList.remove(n2);
        if (this.elementObserver != null) {
            this.elementObserver.detachListener(e2);
        }
        return e2;
    }

    @Override
    public int indexOf(Object object) {
        return this.backingList.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return this.backingList.lastIndexOf(object);
    }

    @Override
    public boolean contains(Object object) {
        return this.backingList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return this.backingList.containsAll(collection);
    }

    @Override
    public void clear() {
        if (this.elementObserver != null) {
            int n2 = this.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                this.elementObserver.detachListener(this.get(i2));
            }
        }
        if (this.hasListeners()) {
            this.beginChange();
            this.nextRemove(0, this);
        }
        this.backingList.clear();
        ++this.modCount;
        if (this.hasListeners()) {
            this.endChange();
        }
    }

    @Override
    public void remove(int n2, int n3) {
        this.beginChange();
        for (int i2 = n2; i2 < n3; ++i2) {
            this.remove(n2);
        }
        this.endChange();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        int n2;
        this.beginChange();
        BitSet bitSet = new BitSet(collection.size());
        for (n2 = 0; n2 < this.size(); ++n2) {
            if (!collection.contains(this.get(n2))) continue;
            bitSet.set(n2);
        }
        if (!bitSet.isEmpty()) {
            n2 = this.size();
            while ((n2 = bitSet.previousSetBit(n2 - 1)) >= 0) {
                this.remove(n2);
            }
        }
        this.endChange();
        return !bitSet.isEmpty();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        int n2;
        this.beginChange();
        BitSet bitSet = new BitSet(collection.size());
        for (n2 = 0; n2 < this.size(); ++n2) {
            if (collection.contains(this.get(n2))) continue;
            bitSet.set(n2);
        }
        if (!bitSet.isEmpty()) {
            n2 = this.size();
            while ((n2 = bitSet.previousSetBit(n2 - 1)) >= 0) {
                this.remove(n2);
            }
        }
        this.endChange();
        return !bitSet.isEmpty();
    }

    @Override
    public void sort() {
        if (this.backingList.isEmpty()) {
            return;
        }
        int[] arrn = this.getSortHelper().sort(this.backingList);
        this.fireChange(new NonIterableChange.SimplePermutationChange(0, this.size(), arrn, this));
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        if (this.backingList.isEmpty()) {
            return;
        }
        int[] arrn = this.getSortHelper().sort(this.backingList, comparator);
        this.fireChange(new NonIterableChange.SimplePermutationChange(0, this.size(), arrn, this));
    }

    private SortHelper getSortHelper() {
        if (this.helper == null) {
            this.helper = new SortHelper();
        }
        return this.helper;
    }
}

