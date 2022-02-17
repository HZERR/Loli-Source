/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.util.Arrays;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableFloatArray;

public final class ObservableFloatArrayImpl
extends ObservableArrayBase<ObservableFloatArray>
implements ObservableFloatArray {
    private static final float[] INITIAL = new float[0];
    private float[] array = INITIAL;
    private int size = 0;
    private static final int MAX_ARRAY_SIZE = 0x7FFFFFF7;

    public ObservableFloatArrayImpl() {
    }

    public ObservableFloatArrayImpl(float ... arrf) {
        this.setAll(arrf);
    }

    public ObservableFloatArrayImpl(ObservableFloatArray observableFloatArray) {
        this.setAll(observableFloatArray);
    }

    @Override
    public void clear() {
        this.resize(0);
    }

    @Override
    public int size() {
        return this.size;
    }

    private void addAllInternal(ObservableFloatArray observableFloatArray, int n2, int n3) {
        this.growCapacity(n3);
        observableFloatArray.copyTo(n2, this.array, this.size, n3);
        this.size += n3;
        this.fireChange(n3 != 0, this.size - n3, this.size);
    }

    private void addAllInternal(float[] arrf, int n2, int n3) {
        this.growCapacity(n3);
        System.arraycopy(arrf, n2, this.array, this.size, n3);
        this.size += n3;
        this.fireChange(n3 != 0, this.size - n3, this.size);
    }

    @Override
    public void addAll(ObservableFloatArray observableFloatArray) {
        this.addAllInternal(observableFloatArray, 0, observableFloatArray.size());
    }

    @Override
    public void addAll(float ... arrf) {
        this.addAllInternal(arrf, 0, arrf.length);
    }

    @Override
    public void addAll(ObservableFloatArray observableFloatArray, int n2, int n3) {
        this.rangeCheck(observableFloatArray, n2, n3);
        this.addAllInternal(observableFloatArray, n2, n3);
    }

    @Override
    public void addAll(float[] arrf, int n2, int n3) {
        this.rangeCheck(arrf, n2, n3);
        this.addAllInternal(arrf, n2, n3);
    }

    private void setAllInternal(ObservableFloatArray observableFloatArray, int n2, int n3) {
        boolean bl;
        boolean bl2 = bl = this.size() != n3;
        if (observableFloatArray == this) {
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
            observableFloatArray.copyTo(n2, this.array, 0, n3);
            this.size = n3;
            this.fireChange(bl, 0, this.size);
        }
    }

    private void setAllInternal(float[] arrf, int n2, int n3) {
        boolean bl = this.size() != n3;
        this.size = 0;
        this.ensureCapacity(n3);
        System.arraycopy(arrf, n2, this.array, 0, n3);
        this.size = n3;
        this.fireChange(bl, 0, this.size);
    }

    @Override
    public void setAll(ObservableFloatArray observableFloatArray) {
        this.setAllInternal(observableFloatArray, 0, observableFloatArray.size());
    }

    @Override
    public void setAll(ObservableFloatArray observableFloatArray, int n2, int n3) {
        this.rangeCheck(observableFloatArray, n2, n3);
        this.setAllInternal(observableFloatArray, n2, n3);
    }

    @Override
    public void setAll(float[] arrf, int n2, int n3) {
        this.rangeCheck(arrf, n2, n3);
        this.setAllInternal(arrf, n2, n3);
    }

    @Override
    public void setAll(float[] arrf) {
        this.setAllInternal(arrf, 0, arrf.length);
    }

    @Override
    public void set(int n2, float[] arrf, int n3, int n4) {
        this.rangeCheck(n2 + n4);
        System.arraycopy(arrf, n3, this.array, n2, n4);
        this.fireChange(false, n2, n2 + n4);
    }

    @Override
    public void set(int n2, ObservableFloatArray observableFloatArray, int n3, int n4) {
        this.rangeCheck(n2 + n4);
        observableFloatArray.copyTo(n3, this.array, n2, n4);
        this.fireChange(false, n2, n2 + n4);
    }

    @Override
    public float[] toArray(float[] arrf) {
        if (arrf == null || this.size() > arrf.length) {
            arrf = new float[this.size()];
        }
        System.arraycopy(this.array, 0, arrf, 0, this.size());
        return arrf;
    }

    @Override
    public float get(int n2) {
        this.rangeCheck(n2 + 1);
        return this.array[n2];
    }

    @Override
    public void set(int n2, float f2) {
        this.rangeCheck(n2 + 1);
        this.array[n2] = f2;
        this.fireChange(false, n2, n2 + 1);
    }

    @Override
    public float[] toArray(int n2, float[] arrf, int n3) {
        this.rangeCheck(n2 + n3);
        if (arrf == null || n3 > arrf.length) {
            arrf = new float[n3];
        }
        System.arraycopy(this.array, n2, arrf, 0, n3);
        return arrf;
    }

    @Override
    public void copyTo(int n2, float[] arrf, int n3, int n4) {
        this.rangeCheck(n2 + n4);
        System.arraycopy(this.array, n2, arrf, n3, n4);
    }

    @Override
    public void copyTo(int n2, ObservableFloatArray observableFloatArray, int n3, int n4) {
        this.rangeCheck(n2 + n4);
        observableFloatArray.set(n3, this.array, n2, n4);
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
        Arrays.fill(this.array, n3, this.size, 0.0f);
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
                n5 = ObservableFloatArrayImpl.hugeCapacity(n3);
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
            float[] arrf = new float[this.size];
            System.arraycopy(this.array, 0, arrf, 0, this.size);
            this.array = arrf;
        }
    }

    private void rangeCheck(int n2) {
        if (n2 > this.size) {
            throw new ArrayIndexOutOfBoundsException(this.size);
        }
    }

    private void rangeCheck(ObservableFloatArray observableFloatArray, int n2, int n3) {
        if (observableFloatArray == null) {
            throw new NullPointerException();
        }
        if (n2 < 0 || n2 + n3 > observableFloatArray.size()) {
            throw new ArrayIndexOutOfBoundsException(observableFloatArray.size());
        }
        if (n3 < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
        }
    }

    private void rangeCheck(float[] arrf, int n2, int n3) {
        if (arrf == null) {
            throw new NullPointerException();
        }
        if (n2 < 0 || n2 + n3 > arrf.length) {
            throw new ArrayIndexOutOfBoundsException(arrf.length);
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

