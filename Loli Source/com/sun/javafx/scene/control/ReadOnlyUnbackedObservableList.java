/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control;

import com.sun.javafx.collections.ListListenerHelper;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class ReadOnlyUnbackedObservableList<E>
implements ObservableList<E> {
    private ListListenerHelper<E> listenerHelper;

    @Override
    public abstract E get(int var1);

    @Override
    public abstract int size();

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, invalidationListener);
    }

    @Override
    public void addListener(ListChangeListener<? super E> listChangeListener) {
        this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, listChangeListener);
    }

    @Override
    public void removeListener(ListChangeListener<? super E> listChangeListener) {
        this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, listChangeListener);
    }

    public void callObservers(ListChangeListener.Change<E> change) {
        ListListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
    }

    @Override
    public int indexOf(Object object) {
        if (object == null) {
            return -1;
        }
        for (int i2 = 0; i2 < this.size(); ++i2) {
            E e2 = this.get(i2);
            if (!object.equals(e2)) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object object) {
        if (object == null) {
            return -1;
        }
        for (int i2 = this.size() - 1; i2 >= 0; --i2) {
            E e2 = this.get(i2);
            if (!object.equals(e2)) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public boolean contains(Object object) {
        return this.indexOf(object) != -1;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object obj : collection) {
            if (this.contains(obj)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new SelectionListIterator(this);
    }

    @Override
    public ListIterator<E> listIterator(int n2) {
        return new SelectionListIterator(this, n2);
    }

    @Override
    public Iterator<E> iterator() {
        return new SelectionListIterator(this);
    }

    @Override
    public List<E> subList(final int n2, final int n3) {
        if (n2 < 0 || n3 > this.size() || n2 > n3) {
            throw new IndexOutOfBoundsException();
        }
        final ReadOnlyUnbackedObservableList readOnlyUnbackedObservableList = this;
        return new ReadOnlyUnbackedObservableList<E>(){

            @Override
            public E get(int n22) {
                return readOnlyUnbackedObservableList.get(n22 + n2);
            }

            @Override
            public int size() {
                return n3 - n2;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arrobject = new Object[this.size()];
        for (int i2 = 0; i2 < this.size(); ++i2) {
            arrobject[i2] = this.get(i2);
        }
        return arrobject;
    }

    @Override
    public <T> T[] toArray(T[] arrT) {
        Object[] arrobject = this.toArray();
        int n2 = arrobject.length;
        if (arrT.length < n2) {
            return Arrays.copyOf(arrobject, n2, arrT.getClass());
        }
        System.arraycopy(arrobject, 0, arrT, 0, n2);
        if (arrT.length > n2) {
            arrT[n2] = null;
        }
        return arrT;
    }

    public String toString() {
        Iterator<E> iterator = this.iterator();
        if (!iterator.hasNext()) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        while (true) {
            E e2;
            stringBuilder.append((Object)((e2 = iterator.next()) == this ? "(this Collection)" : e2));
            if (!iterator.hasNext()) {
                return stringBuilder.append(']').toString();
            }
            stringBuilder.append(", ");
        }
    }

    @Override
    public boolean add(E e2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void add(int n2, E e2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean addAll(int n2, Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean addAll(E ... arrE) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public E set(int n2, E e2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean setAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean setAll(E ... arrE) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public E remove(int n2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void remove(int n2, int n3) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean removeAll(E ... arrE) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean retainAll(E ... arrE) {
        throw new UnsupportedOperationException("Not supported.");
    }

    private static class SelectionListIterator<E>
    implements ListIterator<E> {
        private int pos;
        private final ReadOnlyUnbackedObservableList<E> list;

        public SelectionListIterator(ReadOnlyUnbackedObservableList<E> readOnlyUnbackedObservableList) {
            this(readOnlyUnbackedObservableList, 0);
        }

        public SelectionListIterator(ReadOnlyUnbackedObservableList<E> readOnlyUnbackedObservableList, int n2) {
            this.list = readOnlyUnbackedObservableList;
            this.pos = n2;
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.list.size();
        }

        @Override
        public E next() {
            return this.list.get(this.pos++);
        }

        @Override
        public boolean hasPrevious() {
            return this.pos > 0;
        }

        @Override
        public E previous() {
            return this.list.get(this.pos--);
        }

        @Override
        public int nextIndex() {
            return this.pos + 1;
        }

        @Override
        public int previousIndex() {
            return this.pos - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void set(E e2) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void add(E e2) {
            throw new UnsupportedOperationException("Not supported.");
        }
    }
}

