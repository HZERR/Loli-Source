/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ElementObserver;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SortHelper;
import com.sun.javafx.collections.SortableList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public final class ObservableSequentialListWrapper<E>
extends ModifiableObservableListBase<E>
implements ObservableList<E>,
SortableList<E> {
    private final List<E> backingList;
    private final ElementObserver elementObserver;
    private SortHelper helper;

    public ObservableSequentialListWrapper(List<E> list) {
        this.backingList = list;
        this.elementObserver = null;
    }

    public ObservableSequentialListWrapper(List<E> list, Callback<E, Observable[]> callback) {
        this.backingList = list;
        this.elementObserver = new ElementObserver<E>(callback, new Callback<E, InvalidationListener>(){

            @Override
            public InvalidationListener call(final E e2) {
                return new InvalidationListener(){

                    @Override
                    public void invalidated(Observable observable) {
                        ObservableSequentialListWrapper.this.beginChange();
                        int n2 = 0;
                        Iterator iterator = ObservableSequentialListWrapper.this.backingList.iterator();
                        while (iterator.hasNext()) {
                            if (iterator.next() == e2) {
                                ObservableSequentialListWrapper.this.nextUpdate(n2);
                            }
                            ++n2;
                        }
                        ObservableSequentialListWrapper.this.endChange();
                    }
                };
            }
        }, this);
        for (E e2 : this.backingList) {
            this.elementObserver.attachListener(e2);
        }
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
    public int indexOf(Object object) {
        return this.backingList.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return this.backingList.lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator(final int n2) {
        return new ListIterator<E>(){
            private final ListIterator<E> backingIt;
            private E lastReturned;
            {
                this.backingIt = ObservableSequentialListWrapper.this.backingList.listIterator(n2);
            }

            @Override
            public boolean hasNext() {
                return this.backingIt.hasNext();
            }

            @Override
            public E next() {
                this.lastReturned = this.backingIt.next();
                return this.lastReturned;
            }

            @Override
            public boolean hasPrevious() {
                return this.backingIt.hasPrevious();
            }

            @Override
            public E previous() {
                this.lastReturned = this.backingIt.previous();
                return this.lastReturned;
            }

            @Override
            public int nextIndex() {
                return this.backingIt.nextIndex();
            }

            @Override
            public int previousIndex() {
                return this.backingIt.previousIndex();
            }

            @Override
            public void remove() {
                ObservableSequentialListWrapper.this.beginChange();
                int n22 = this.previousIndex();
                this.backingIt.remove();
                ObservableSequentialListWrapper.this.nextRemove(n22, this.lastReturned);
                ObservableSequentialListWrapper.this.endChange();
            }

            @Override
            public void set(E e2) {
                ObservableSequentialListWrapper.this.beginChange();
                int n22 = this.previousIndex();
                this.backingIt.set(e2);
                ObservableSequentialListWrapper.this.nextSet(n22, this.lastReturned);
                ObservableSequentialListWrapper.this.endChange();
            }

            @Override
            public void add(E e2) {
                ObservableSequentialListWrapper.this.beginChange();
                int n22 = this.nextIndex();
                this.backingIt.add(e2);
                ObservableSequentialListWrapper.this.nextAdd(n22, n22 + 1);
                ObservableSequentialListWrapper.this.endChange();
            }
        };
    }

    @Override
    public Iterator<E> iterator() {
        return this.listIterator();
    }

    @Override
    public E get(int n2) {
        try {
            return this.backingList.listIterator(n2).next();
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n2);
        }
    }

    @Override
    public boolean addAll(int n2, Collection<? extends E> collection) {
        try {
            this.beginChange();
            boolean bl = false;
            ListIterator<E> listIterator = this.listIterator(n2);
            Iterator<E> iterator = collection.iterator();
            while (iterator.hasNext()) {
                listIterator.add(iterator.next());
                bl = true;
            }
            this.endChange();
            return bl;
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n2);
        }
    }

    @Override
    public int size() {
        return this.backingList.size();
    }

    @Override
    protected void doAdd(int n2, E e2) {
        try {
            this.backingList.listIterator(n2).add(e2);
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n2);
        }
    }

    @Override
    protected E doSet(int n2, E e2) {
        try {
            ListIterator<E> listIterator = this.backingList.listIterator(n2);
            E e3 = listIterator.next();
            listIterator.set(e2);
            return e3;
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n2);
        }
    }

    @Override
    protected E doRemove(int n2) {
        try {
            ListIterator<E> listIterator = this.backingList.listIterator(n2);
            E e2 = listIterator.next();
            listIterator.remove();
            return e2;
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n2);
        }
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

