/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.collections.SetListenerHelper;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

abstract class BitSet<T>
implements ObservableSet<T> {
    private static final long[] EMPTY_SET = new long[0];
    private long[] bits = EMPTY_SET;
    private SetListenerHelper<T> listenerHelper;

    protected BitSet() {
    }

    @Override
    public int size() {
        int n2 = 0;
        if (this.bits.length > 0) {
            for (int i2 = 0; i2 < this.bits.length; ++i2) {
                long l2 = this.bits[i2];
                if (l2 == 0L) continue;
                n2 += Long.bitCount(l2);
            }
        }
        return n2;
    }

    @Override
    public boolean isEmpty() {
        if (this.bits.length > 0) {
            for (int i2 = 0; i2 < this.bits.length; ++i2) {
                long l2 = this.bits[i2];
                if (l2 == 0L) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(){
            int next = -1;
            int element = 0;
            int index = -1;

            @Override
            public boolean hasNext() {
                long l2;
                if (BitSet.this.bits == null || BitSet.this.bits.length == 0) {
                    return false;
                }
                boolean bl = false;
                do {
                    if (++this.next < 64) continue;
                    if (++this.element < BitSet.this.bits.length) {
                        this.next = 0;
                        continue;
                    }
                    return false;
                } while (!(bl = ((l2 = 1L << this.next) & BitSet.this.bits[this.element]) == l2));
                if (bl) {
                    this.index = 64 * this.element + this.next;
                }
                return bl;
            }

            @Override
            public T next() {
                try {
                    return BitSet.this.getT(this.index);
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                    throw new NoSuchElementException("[" + this.element + "][" + this.next + "]");
                }
            }

            @Override
            public void remove() {
                try {
                    Object t2 = BitSet.this.getT(this.index);
                    BitSet.this.remove(t2);
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                    throw new NoSuchElementException("[" + this.element + "][" + this.next + "]");
                }
            }
        };
    }

    @Override
    public boolean add(T t2) {
        boolean bl;
        if (t2 == null) {
            return false;
        }
        int n2 = this.getIndex(t2) / 64;
        long l2 = 1L << this.getIndex(t2) % 64;
        if (n2 >= this.bits.length) {
            long[] arrl = new long[n2 + 1];
            System.arraycopy(this.bits, 0, arrl, 0, this.bits.length);
            this.bits = arrl;
        }
        long l3 = this.bits[n2];
        this.bits[n2] = l3 | l2;
        boolean bl2 = bl = this.bits[n2] != l3;
        if (bl && SetListenerHelper.hasListeners(this.listenerHelper)) {
            this.notifyObservers(t2, false);
        }
        return bl;
    }

    @Override
    public boolean remove(Object object) {
        boolean bl;
        if (object == null) {
            return false;
        }
        T t2 = this.cast(object);
        int n2 = this.getIndex(t2) / 64;
        long l2 = 1L << this.getIndex(t2) % 64;
        if (n2 >= this.bits.length) {
            return false;
        }
        long l3 = this.bits[n2];
        this.bits[n2] = l3 & (l2 ^ 0xFFFFFFFFFFFFFFFFL);
        boolean bl2 = bl = this.bits[n2] != l3;
        if (bl) {
            if (SetListenerHelper.hasListeners(this.listenerHelper)) {
                this.notifyObservers(t2, true);
            }
            boolean bl3 = true;
            for (int i2 = 0; i2 < this.bits.length && bl3; bl3 &= this.bits[i2] == 0L, ++i2) {
            }
            if (bl3) {
                this.bits = EMPTY_SET;
            }
        }
        return bl;
    }

    @Override
    public boolean contains(Object object) {
        if (object == null) {
            return false;
        }
        T t2 = this.cast(object);
        int n2 = this.getIndex(t2) / 64;
        long l2 = 1L << this.getIndex(t2) % 64;
        return n2 < this.bits.length && (this.bits[n2] & l2) == l2;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection == null || this.getClass() != collection.getClass()) {
            return false;
        }
        BitSet bitSet = (BitSet)collection;
        if (this.bits.length == 0 && bitSet.bits.length == 0) {
            return true;
        }
        if (this.bits.length < bitSet.bits.length) {
            return false;
        }
        int n2 = bitSet.bits.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if ((this.bits[i2] & bitSet.bits[i2]) == bitSet.bits[i2]) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        int n2;
        if (collection == null || this.getClass() != collection.getClass()) {
            return false;
        }
        boolean bl = false;
        BitSet bitSet = (BitSet)collection;
        long[] arrl = this.bits;
        int n3 = arrl.length;
        long[] arrl2 = bitSet.bits;
        int n4 = arrl2.length;
        int n5 = n3 < n4 ? n4 : n3;
        long[] arrl3 = n5 > 0 ? new long[n5] : EMPTY_SET;
        for (n2 = 0; n2 < n5; ++n2) {
            if (n2 < arrl.length && n2 < arrl2.length) {
                arrl3[n2] = arrl[n2] | arrl2[n2];
                bl |= arrl3[n2] != arrl[n2];
                continue;
            }
            if (n2 < arrl.length) {
                arrl3[n2] = arrl[n2];
                bl |= false;
                continue;
            }
            arrl3[n2] = arrl2[n2];
            bl = true;
        }
        if (bl) {
            if (SetListenerHelper.hasListeners(this.listenerHelper)) {
                for (n2 = 0; n2 < n5; ++n2) {
                    long l2 = 0L;
                    if (n2 < arrl.length && n2 < arrl2.length) {
                        l2 = (arrl[n2] ^ 0xFFFFFFFFFFFFFFFFL) & arrl2[n2];
                    } else {
                        if (n2 < arrl.length) continue;
                        l2 = arrl2[n2];
                    }
                    for (int i2 = 0; i2 < 64; ++i2) {
                        long l3 = 1L << i2;
                        if ((l3 & l2) != l3) continue;
                        T t2 = this.getT(n2 * 64 + i2);
                        this.notifyObservers(t2, false);
                    }
                }
            }
            this.bits = arrl3;
        }
        return bl;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        int n2;
        if (collection == null || this.getClass() != collection.getClass()) {
            this.clear();
            return true;
        }
        boolean bl = false;
        BitSet bitSet = (BitSet)collection;
        long[] arrl = this.bits;
        int n3 = arrl.length;
        long[] arrl2 = bitSet.bits;
        int n4 = arrl2.length;
        int n5 = n3 < n4 ? n3 : n4;
        long[] arrl3 = n5 > 0 ? new long[n5] : EMPTY_SET;
        bl |= arrl.length > n5;
        boolean bl2 = true;
        for (n2 = 0; n2 < n5; ++n2) {
            arrl3[n2] = arrl[n2] & arrl2[n2];
            bl |= arrl3[n2] != arrl[n2];
            bl2 &= arrl3[n2] == 0L;
        }
        if (bl) {
            if (SetListenerHelper.hasListeners(this.listenerHelper)) {
                for (n2 = 0; n2 < arrl.length; ++n2) {
                    long l2 = 0L;
                    l2 = n2 < arrl2.length ? arrl[n2] & (arrl2[n2] ^ 0xFFFFFFFFFFFFFFFFL) : arrl[n2];
                    for (int i2 = 0; i2 < 64; ++i2) {
                        long l3 = 1L << i2;
                        if ((l3 & l2) != l3) continue;
                        T t2 = this.getT(n2 * 64 + i2);
                        this.notifyObservers(t2, true);
                    }
                }
            }
            this.bits = !bl2 ? arrl3 : EMPTY_SET;
        }
        return bl;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        int n2;
        if (collection == null || this.getClass() != collection.getClass()) {
            return false;
        }
        boolean bl = false;
        BitSet bitSet = (BitSet)collection;
        long[] arrl = this.bits;
        int n3 = arrl.length;
        long[] arrl2 = bitSet.bits;
        int n4 = arrl2.length;
        int n5 = n3 < n4 ? n3 : n4;
        long[] arrl3 = n5 > 0 ? new long[n5] : EMPTY_SET;
        boolean bl2 = true;
        for (n2 = 0; n2 < n5; ++n2) {
            arrl3[n2] = arrl[n2] & (arrl2[n2] ^ 0xFFFFFFFFFFFFFFFFL);
            bl |= arrl3[n2] != arrl[n2];
            bl2 &= arrl3[n2] == 0L;
        }
        if (bl) {
            if (SetListenerHelper.hasListeners(this.listenerHelper)) {
                for (n2 = 0; n2 < n5; ++n2) {
                    long l2 = arrl[n2] & arrl2[n2];
                    for (int i2 = 0; i2 < 64; ++i2) {
                        long l3 = 1L << i2;
                        if ((l3 & l2) != l3) continue;
                        T t2 = this.getT(n2 * 64 + i2);
                        this.notifyObservers(t2, true);
                    }
                }
            }
            this.bits = !bl2 ? arrl3 : EMPTY_SET;
        }
        return bl;
    }

    @Override
    public void clear() {
        for (int i2 = 0; i2 < this.bits.length; ++i2) {
            long l2 = this.bits[i2];
            for (int i3 = 0; i3 < 64; ++i3) {
                long l3 = 1L << i3;
                if ((l3 & l2) != l3) continue;
                T t2 = this.getT(i2 * 64 + i3);
                this.notifyObservers(t2, true);
            }
        }
        this.bits = EMPTY_SET;
    }

    @Override
    public int hashCode() {
        int n2 = 7;
        if (this.bits.length > 0) {
            for (int i2 = 0; i2 < this.bits.length; ++i2) {
                long l2 = this.bits[i2];
                n2 = 71 * n2 + (int)(l2 ^ l2 >>> 32);
            }
        }
        return n2;
    }

    @Override
    public boolean equals(Object object) {
        int n2;
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        BitSet bitSet = (BitSet)object;
        int n3 = this.bits != null ? this.bits.length : 0;
        int n4 = n2 = bitSet.bits != null ? bitSet.bits.length : 0;
        if (n3 != n2) {
            return false;
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            long l2 = this.bits[i2];
            long l3 = bitSet.bits[i2];
            if (l2 == l3) continue;
            return false;
        }
        return true;
    }

    protected abstract T getT(int var1);

    protected abstract int getIndex(T var1);

    protected abstract T cast(Object var1);

    protected long[] getBits() {
        return this.bits;
    }

    @Override
    public void addListener(SetChangeListener<? super T> setChangeListener) {
        if (setChangeListener != null) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, setChangeListener);
        }
    }

    @Override
    public void removeListener(SetChangeListener<? super T> setChangeListener) {
        if (setChangeListener != null) {
            SetListenerHelper.removeListener(this.listenerHelper, setChangeListener);
        }
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        if (invalidationListener != null) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, invalidationListener);
        }
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        if (invalidationListener != null) {
            SetListenerHelper.removeListener(this.listenerHelper, invalidationListener);
        }
    }

    private void notifyObservers(T t2, boolean bl) {
        if (t2 != null && SetListenerHelper.hasListeners(this.listenerHelper)) {
            Change change = new Change(t2, bl);
            SetListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
        }
    }

    private class Change
    extends SetChangeListener.Change<T> {
        private static final boolean ELEMENT_ADDED = false;
        private static final boolean ELEMENT_REMOVED = true;
        private final T element;
        private final boolean removed;

        public Change(T t2, boolean bl) {
            super(FXCollections.unmodifiableObservableSet(BitSet.this));
            this.element = t2;
            this.removed = bl;
        }

        @Override
        public boolean wasAdded() {
            return !this.removed;
        }

        @Override
        public boolean wasRemoved() {
            return this.removed;
        }

        @Override
        public T getElementAdded() {
            return this.removed ? null : (Object)this.element;
        }

        @Override
        public T getElementRemoved() {
            return this.removed ? (Object)this.element : null;
        }
    }
}

