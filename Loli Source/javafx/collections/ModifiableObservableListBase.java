/*
 * Decompiled with CFR 0.150.
 */
package javafx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.collections.ObservableListBase;

public abstract class ModifiableObservableListBase<E>
extends ObservableListBase<E> {
    @Override
    public boolean setAll(Collection<? extends E> collection) {
        this.beginChange();
        try {
            this.clear();
            this.addAll(collection);
        }
        finally {
            this.endChange();
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        this.beginChange();
        try {
            boolean bl;
            boolean bl2 = bl = super.addAll(collection);
            return bl2;
        }
        finally {
            this.endChange();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean addAll(int n2, Collection<? extends E> collection) {
        this.beginChange();
        try {
            boolean bl;
            boolean bl2 = bl = super.addAll(n2, collection);
            return bl2;
        }
        finally {
            this.endChange();
        }
    }

    @Override
    protected void removeRange(int n2, int n3) {
        this.beginChange();
        try {
            super.removeRange(n2, n3);
        }
        finally {
            this.endChange();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        this.beginChange();
        try {
            boolean bl;
            boolean bl2 = bl = super.removeAll(collection);
            return bl2;
        }
        finally {
            this.endChange();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        this.beginChange();
        try {
            boolean bl;
            boolean bl2 = bl = super.retainAll(collection);
            return bl2;
        }
        finally {
            this.endChange();
        }
    }

    @Override
    public void add(int n2, E e2) {
        this.doAdd(n2, e2);
        this.beginChange();
        this.nextAdd(n2, n2 + 1);
        ++this.modCount;
        this.endChange();
    }

    @Override
    public E set(int n2, E e2) {
        E e3 = this.doSet(n2, e2);
        this.beginChange();
        this.nextSet(n2, e3);
        this.endChange();
        return e3;
    }

    @Override
    public boolean remove(Object object) {
        int n2 = this.indexOf(object);
        if (n2 != -1) {
            this.remove(n2);
            return true;
        }
        return false;
    }

    @Override
    public E remove(int n2) {
        E e2 = this.doRemove(n2);
        this.beginChange();
        this.nextRemove(n2, e2);
        ++this.modCount;
        this.endChange();
        return e2;
    }

    @Override
    public List<E> subList(int n2, int n3) {
        return new SubObservableList(super.subList(n2, n3));
    }

    @Override
    public abstract E get(int var1);

    @Override
    public abstract int size();

    protected abstract void doAdd(int var1, E var2);

    protected abstract E doSet(int var1, E var2);

    protected abstract E doRemove(int var1);

    private class SubObservableList
    implements List<E> {
        private List<E> sublist;

        public SubObservableList(List<E> list) {
            this.sublist = list;
        }

        @Override
        public int size() {
            return this.sublist.size();
        }

        @Override
        public boolean isEmpty() {
            return this.sublist.isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return this.sublist.contains(object);
        }

        @Override
        public Iterator<E> iterator() {
            return this.sublist.iterator();
        }

        @Override
        public Object[] toArray() {
            return this.sublist.toArray();
        }

        @Override
        public <T> T[] toArray(T[] arrT) {
            return this.sublist.toArray(arrT);
        }

        @Override
        public boolean add(E e2) {
            return this.sublist.add(e2);
        }

        @Override
        public boolean remove(Object object) {
            return this.sublist.remove(object);
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return this.sublist.containsAll(collection);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean addAll(Collection<? extends E> collection) {
            ModifiableObservableListBase.this.beginChange();
            try {
                boolean bl;
                boolean bl2 = bl = this.sublist.addAll(collection);
                return bl2;
            }
            finally {
                ModifiableObservableListBase.this.endChange();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean addAll(int n2, Collection<? extends E> collection) {
            ModifiableObservableListBase.this.beginChange();
            try {
                boolean bl;
                boolean bl2 = bl = this.sublist.addAll(n2, collection);
                return bl2;
            }
            finally {
                ModifiableObservableListBase.this.endChange();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean removeAll(Collection<?> collection) {
            ModifiableObservableListBase.this.beginChange();
            try {
                boolean bl;
                boolean bl2 = bl = this.sublist.removeAll(collection);
                return bl2;
            }
            finally {
                ModifiableObservableListBase.this.endChange();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean retainAll(Collection<?> collection) {
            ModifiableObservableListBase.this.beginChange();
            try {
                boolean bl;
                boolean bl2 = bl = this.sublist.retainAll(collection);
                return bl2;
            }
            finally {
                ModifiableObservableListBase.this.endChange();
            }
        }

        @Override
        public void clear() {
            ModifiableObservableListBase.this.beginChange();
            try {
                this.sublist.clear();
            }
            finally {
                ModifiableObservableListBase.this.endChange();
            }
        }

        @Override
        public E get(int n2) {
            return this.sublist.get(n2);
        }

        @Override
        public E set(int n2, E e2) {
            return this.sublist.set(n2, e2);
        }

        @Override
        public void add(int n2, E e2) {
            this.sublist.add(n2, e2);
        }

        @Override
        public E remove(int n2) {
            return this.sublist.remove(n2);
        }

        @Override
        public int indexOf(Object object) {
            return this.sublist.indexOf(object);
        }

        @Override
        public int lastIndexOf(Object object) {
            return this.sublist.lastIndexOf(object);
        }

        @Override
        public ListIterator<E> listIterator() {
            return this.sublist.listIterator();
        }

        @Override
        public ListIterator<E> listIterator(int n2) {
            return this.sublist.listIterator(n2);
        }

        @Override
        public List<E> subList(int n2, int n3) {
            return new SubObservableList(this.sublist.subList(n2, n3));
        }

        @Override
        public boolean equals(Object object) {
            return this.sublist.equals(object);
        }

        @Override
        public int hashCode() {
            return this.sublist.hashCode();
        }

        public String toString() {
            return this.sublist.toString();
        }
    }
}

