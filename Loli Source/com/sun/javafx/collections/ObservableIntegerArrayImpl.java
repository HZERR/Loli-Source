/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.util.Arrays;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableIntegerArray;

public class ObservableIntegerArrayImpl
extends ObservableArrayBase<ObservableIntegerArray>
implements ObservableIntegerArray {
    private static final int[] INITIAL = new int[0];
    private int[] array = INITIAL;
    private int size = 0;
    private static final int MAX_ARRAY_SIZE = 0x7FFFFFF7;

    public ObservableIntegerArrayImpl() {
    }

    public ObservableIntegerArrayImpl(int ... arrn) {
        this.setAll(arrn);
    }

    public ObservableIntegerArrayImpl(ObservableIntegerArray observableIntegerArray) {
        this.setAll(observableIntegerArray);
    }

    @Override
    public void clear() {
        this.resize(0);
    }

    @Override
    public int size() {
        return this.size;
    }

    private void addAllInternal(ObservableIntegerArray observableIntegerArray, int n2, int n3) {
        this.growCapacity(n3);
        observableIntegerArray.copyTo(n2, this.array, this.size, n3);
        this.size += n3;
        this.fireChange(n3 != 0, this.size - n3, this.size);
    }

    private void addAllInternal(int[] arrn, int n2, int n3) {
        this.growCapacity(n3);
        System.arraycopy(arrn, n2, this.array, this.size, n3);
        this.size += n3;
        this.fireChange(n3 != 0, this.size - n3, this.size);
    }

    @Override
    public void addAll(ObservableIntegerArray observableIntegerArray) {
        this.addAllInternal(observableIntegerArray, 0, observableIntegerArray.size());
    }

    @Override
    public void addAll(int ... arrn) {
        this.addAllInternal(arrn, 0, arrn.length);
    }

    @Override
    public void addAll(ObservableIntegerArray observableIntegerArray, int n2, int n3) {
        this.rangeCheck(observableIntegerArray, n2, n3);
        this.addAllInternal(observableIntegerArray, n2, n3);
    }

    @Override
    public void addAll(int[] arrn, int n2, int n3) {
        this.rangeCheck(arrn, n2, n3);
        this.addAllInternal(arrn, n2, n3);
    }

    private void setAllInternal(ObservableIntegerArray observableIntegerArray, int n2, int n3) {
        boolean bl;
        boolean bl2 = bl = this.size() != n3;
        if (observableIntegerArray == this) {
            if (n2 == 0) {
                this.resize(n3);
            } else {
                System.arraycopy(this.array, n2, this.array, 0, n3);
                this.size = n3;
                this.fireChange(bl, 0, this.size);
            }
        } else {
            this.size = 0;
            this.ensureCapacity(n3);
            observableIntegerArray.copyTo(n2, this.array, 0, n3);
            this.size = n3;
            this.fireChange(bl, 0, this.size);
        }
    }

    private void setAllInternal(int[] arrn, int n2, int n3) {
        boolean bl = this.size() != n3;
        this.size = 0;
        this.ensureCapacity(n3);
        System.arraycopy(arrn, n2, this.array, 0, n3);
        this.size = n3;
        this.fireChange(bl, 0, this.size);
    }

    @Override
    public void setAll(ObservableIntegerArray observableIntegerArray) {
        this.setAllInternal(observableIntegerArray, 0, observableIntegerArray.size());
    }

    @Override
    public void setAll(ObservableIntegerArray observableIntegerArray, int n2, int n3) {
        this.rangeCheck(observableIntegerArray, n2, n3);
        this.setAllInternal(observableIntegerArray, n2, n3);
    }

    @Override
    public void setAll(int[] arrn, int n2, int n3) {
        this.rangeCheck(arrn, n2, n3);
        this.setAllInternal(arrn, n2, n3);
    }

    @Override
    public void setAll(int[] arrn) {
        this.setAllInternal(arrn, 0, arrn.length);
    }

    @Override
    public void set(int n2, int[] arrn, int n3, int n4) {
        this.rangeCheck(n2 + n4);
        System.arraycopy(arrn, n3, this.array, n2, n4);
        this.fireChange(false, n2, n2 + n4);
    }

    @Override
    public void set(int n2, ObservableIntegerArray observableIntegerArray, int n3, int n4) {
        this.rangeCheck(n2 + n4);
        observableIntegerArray.copyTo(n3, this.array, n2, n4);
        this.fireChange(false, n2, n2 + n4);
    }

    @Override
    public int[] toArray(int[] arrn) {
        if (arrn == null || this.size() > arrn.length) {
            arrn = new int[this.size()];
        }
        System.arraycopy(this.array, 0, arrn, 0, this.size());
        return arrn;
    }

    @Override
    public int get(int n2) {
        this.rangeCheck(n2 + 1);
        return this.array[n2];
    }

    @Override
    public void set(int n2, int n3) {
        this.rangeCheck(n2 + 1);
        this.array[n2] = n3;
        this.fireChange(false, n2, n2 + 1);
    }

    @Override
    public int[] toArray(int n2, int[] arrn, int n3) {
        this.rangeCheck(n2 + n3);
        if (arrn == null || n3 > arrn.length) {
            arrn = new int[n3];
        }
        System.arraycopy(this.array, n2, arrn, 0, n3);
        return arrn;
    }

    @Override
    public void copyTo(int n2, int[] arrn, int n3, int n4) {
        this.rangeCheck(n2 + n4);
        System.arraycopy(this.array, n2, arrn, n3, n4);
    }

    @Override
    public void copyTo(int n2, ObservableIntegerArray observableIntegerArray, int n3, int n4) {
        this.rangeCheck(n2 + n4);
        observableIntegerArray.set(n3, this.array, n2, n4);
    }

    @Override
    public void resize(int n2) {
        if (n2 < 0) {
            throw new NegativeArraySizeException("Can't resize to negative value: " + n2);
        }
        this.ensureCapacity(n2);
        int n3 = Math.min(this.size, n2);
        boolean bl = this.size != n2;
        this.size = n2;
        Arrays.fill(this.array, n3, this.size, 0);
        this.fireChange(bl, n3, n2);
    }

    private void growCapacity(int n2) {
        int n3 = this.size + n2;
        int n4 = this.array.length;
        if (n3 > this.array.length) {
            int n5 = n4 + (n4 >> 1);
            if (n5 < n3) {
                n5 = n3;
            }
            if (n5 > 0x7FFFFFF7) {
                n5 = ObservableIntegerArrayImpl.hugeCapacity(n3);
            }
            this.ensureCapacity(n5);
        } else if (n2 > 0 && n3 < 0) {
            throw new OutOfMemoryError();
        }
    }

    @Override
    public void ensureCapacity(int n2) {
        if (this.array.length < n2) {
            this.array = Arrays.copyOf(this.array, n2);
        }
    }

    private static int hugeCapacity(int n2) {
        if (n2 < 0) {
            throw new OutOfMemoryError();
        }
        return n2 > 0x7FFFFFF7 ? Integer.MAX_VALUE : 0x7FFFFFF7;
    }

    @Override
    public void trimToSize() {
        if (this.array.length != this.size) {
            int[] arrn = new int[this.size];
            System.arraycopy(this.array, 0, arrn, 0, this.size);
            this.array = arrn;
        }
    }

    private void rangeCheck(int n2) {
        if (n2 > this.size) {
            throw new ArrayIndexOutOfBoundsException(this.size);
        }
    }

    private void rangeCheck(ObservableIntegerArray observableIntegerArray, int n2, int n3) {
        if (observableIntegerArray == null) {
            throw new NullPointerException();
        }
        if (n2 < 0 || n2 + n3 > observableIntegerArray.size()) {
            throw new ArrayIndexOutOfBoundsException(observableIntegerArray.size());
        }
        if (n3 < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
        }
    }

    private void rangeCheck(int[] arrn, int n2, int n3) {
        if (arrn == null) {
            throw new NullPointerException();
        }
        if (n2 < 0 || n2 + n3 > arrn.length) {
            throw new ArrayIndexOutOfBoundsException(arrn.length);
        }
        if (n3 < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
        }
    }

    public String toString() {
        if (this.array == null) {
            return "null";
        }
        int n2 = this.size() - 1;
        if (n2 == -1) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        int n3 = 0;
        while (true) {
            stringBuilder.append(this.array[n3]);
            if (n3 == n2) {
                return stringBuilder.append(']').toString();
            }
            stringBuilder.append(", ");
            ++n3;
        }
    }
}

