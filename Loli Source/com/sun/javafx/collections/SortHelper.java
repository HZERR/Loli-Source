/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class SortHelper {
    private int[] permutation;
    private int[] reversePermutation;
    private static final int INSERTIONSORT_THRESHOLD = 7;

    public <T extends Comparable<? super T>> int[] sort(List<T> list) {
        Comparable[] arrcomparable = (Comparable[])Array.newInstance(Comparable.class, list.size());
        try {
            arrcomparable = list.toArray(arrcomparable);
        }
        catch (ArrayStoreException arrayStoreException) {
            throw new ClassCastException();
        }
        int[] arrn = this.sort(arrcomparable);
        ListIterator<T> listIterator = list.listIterator();
        for (int i2 = 0; i2 < arrcomparable.length; ++i2) {
            listIterator.next();
            listIterator.set(arrcomparable[i2]);
        }
        return arrn;
    }

    public <T> int[] sort(List<T> list, Comparator<? super T> comparator) {
        Object[] arrobject = list.toArray();
        int[] arrn = this.sort(arrobject, comparator);
        ListIterator<T> listIterator = list.listIterator();
        for (int i2 = 0; i2 < arrobject.length; ++i2) {
            listIterator.next();
            listIterator.set(arrobject[i2]);
        }
        return arrn;
    }

    public <T extends Comparable<? super T>> int[] sort(T[] arrT) {
        return this.sort(arrT, null);
    }

    public <T> int[] sort(T[] arrT, Comparator<? super T> comparator) {
        Object[] arrobject = (Object[])arrT.clone();
        int[] arrn = this.initPermutation(arrT.length);
        if (comparator == null) {
            this.mergeSort(arrobject, arrT, 0, arrT.length, 0);
        } else {
            this.mergeSort(arrobject, arrT, 0, arrT.length, 0, comparator);
        }
        this.reversePermutation = null;
        this.permutation = null;
        return arrn;
    }

    public <T> int[] sort(T[] arrT, int n2, int n3, Comparator<? super T> comparator) {
        SortHelper.rangeCheck(arrT.length, n2, n3);
        Object[] arrobject = SortHelper.copyOfRange(arrT, n2, n3);
        int[] arrn = this.initPermutation(arrT.length);
        if (comparator == null) {
            this.mergeSort(arrobject, arrT, n2, n3, -n2);
        } else {
            this.mergeSort(arrobject, arrT, n2, n3, -n2, comparator);
        }
        this.reversePermutation = null;
        this.permutation = null;
        return Arrays.copyOfRange(arrn, n2, n3);
    }

    public int[] sort(int[] arrn, int n2, int n3) {
        SortHelper.rangeCheck(arrn.length, n2, n3);
        int[] arrn2 = SortHelper.copyOfRange(arrn, n2, n3);
        int[] arrn3 = this.initPermutation(arrn.length);
        this.mergeSort(arrn2, arrn, n2, n3, -n2);
        this.reversePermutation = null;
        this.permutation = null;
        return Arrays.copyOfRange(arrn3, n2, n3);
    }

    private static void rangeCheck(int n2, int n3, int n4) {
        if (n3 > n4) {
            throw new IllegalArgumentException("fromIndex(" + n3 + ") > toIndex(" + n4 + ")");
        }
        if (n3 < 0) {
            throw new ArrayIndexOutOfBoundsException(n3);
        }
        if (n4 > n2) {
            throw new ArrayIndexOutOfBoundsException(n4);
        }
    }

    private static int[] copyOfRange(int[] arrn, int n2, int n3) {
        int n4 = n3 - n2;
        if (n4 < 0) {
            throw new IllegalArgumentException(n2 + " > " + n3);
        }
        int[] arrn2 = new int[n4];
        System.arraycopy(arrn, n2, arrn2, 0, Math.min(arrn.length - n2, n4));
        return arrn2;
    }

    private static <T> T[] copyOfRange(T[] arrT, int n2, int n3) {
        return SortHelper.copyOfRange(arrT, n2, n3, arrT.getClass());
    }

    private static <T, U> T[] copyOfRange(U[] arrU, int n2, int n3, Class<? extends T[]> class_) {
        int n4 = n3 - n2;
        if (n4 < 0) {
            throw new IllegalArgumentException(n2 + " > " + n3);
        }
        Object[] arrobject = class_ == Object[].class ? new Object[n4] : (Object[])Array.newInstance(class_.getComponentType(), n4);
        System.arraycopy(arrU, n2, arrobject, 0, Math.min(arrU.length - n2, n4));
        return arrobject;
    }

    private void mergeSort(int[] arrn, int[] arrn2, int n2, int n3, int n4) {
        int n5;
        int n6 = n3 - n2;
        if (n6 < 7) {
            for (int i2 = n2; i2 < n3; ++i2) {
                for (int i3 = i2; i3 > n2 && Integer.valueOf(arrn2[i3 - 1]).compareTo(arrn2[i3]) > 0; --i3) {
                    this.swap(arrn2, i3, i3 - 1);
                }
            }
            return;
        }
        int n7 = n2;
        int n8 = n3;
        int n9 = (n2 += n4) + (n3 += n4) >>> 1;
        this.mergeSort(arrn2, arrn, n2, n9, -n4);
        this.mergeSort(arrn2, arrn, n9, n3, -n4);
        if (Integer.valueOf(arrn[n9 - 1]).compareTo(arrn[n9]) <= 0) {
            System.arraycopy(arrn, n2, arrn2, n7, n6);
            return;
        }
        int n10 = n2;
        int n11 = n9;
        for (n5 = n7; n5 < n8; ++n5) {
            if (n11 >= n3 || n10 < n9 && Integer.valueOf(arrn[n10]).compareTo(arrn[n11]) <= 0) {
                arrn2[n5] = arrn[n10];
                this.permutation[this.reversePermutation[n10++]] = n5;
                continue;
            }
            arrn2[n5] = arrn[n11];
            this.permutation[this.reversePermutation[n11++]] = n5;
        }
        for (n5 = n7; n5 < n8; ++n5) {
            this.reversePermutation[this.permutation[n5]] = n5;
        }
    }

    private void mergeSort(Object[] arrobject, Object[] arrobject2, int n2, int n3, int n4) {
        int n5;
        int n6 = n3 - n2;
        if (n6 < 7) {
            for (int i2 = n2; i2 < n3; ++i2) {
                for (int i3 = i2; i3 > n2 && ((Comparable)arrobject2[i3 - 1]).compareTo(arrobject2[i3]) > 0; --i3) {
                    this.swap(arrobject2, i3, i3 - 1);
                }
            }
            return;
        }
        int n7 = n2;
        int n8 = n3;
        int n9 = (n2 += n4) + (n3 += n4) >>> 1;
        this.mergeSort(arrobject2, arrobject, n2, n9, -n4);
        this.mergeSort(arrobject2, arrobject, n9, n3, -n4);
        if (((Comparable)arrobject[n9 - 1]).compareTo(arrobject[n9]) <= 0) {
            System.arraycopy(arrobject, n2, arrobject2, n7, n6);
            return;
        }
        int n10 = n2;
        int n11 = n9;
        for (n5 = n7; n5 < n8; ++n5) {
            if (n11 >= n3 || n10 < n9 && ((Comparable)arrobject[n10]).compareTo(arrobject[n11]) <= 0) {
                arrobject2[n5] = arrobject[n10];
                this.permutation[this.reversePermutation[n10++]] = n5;
                continue;
            }
            arrobject2[n5] = arrobject[n11];
            this.permutation[this.reversePermutation[n11++]] = n5;
        }
        for (n5 = n7; n5 < n8; ++n5) {
            this.reversePermutation[this.permutation[n5]] = n5;
        }
    }

    private void mergeSort(Object[] arrobject, Object[] arrobject2, int n2, int n3, int n4, Comparator comparator) {
        int n5;
        int n6 = n3 - n2;
        if (n6 < 7) {
            for (int i2 = n2; i2 < n3; ++i2) {
                for (int i3 = i2; i3 > n2 && comparator.compare(arrobject2[i3 - 1], arrobject2[i3]) > 0; --i3) {
                    this.swap(arrobject2, i3, i3 - 1);
                }
            }
            return;
        }
        int n7 = n2;
        int n8 = n3;
        int n9 = (n2 += n4) + (n3 += n4) >>> 1;
        this.mergeSort(arrobject2, arrobject, n2, n9, -n4, comparator);
        this.mergeSort(arrobject2, arrobject, n9, n3, -n4, comparator);
        if (comparator.compare(arrobject[n9 - 1], arrobject[n9]) <= 0) {
            System.arraycopy(arrobject, n2, arrobject2, n7, n6);
            return;
        }
        int n10 = n2;
        int n11 = n9;
        for (n5 = n7; n5 < n8; ++n5) {
            if (n11 >= n3 || n10 < n9 && comparator.compare(arrobject[n10], arrobject[n11]) <= 0) {
                arrobject2[n5] = arrobject[n10];
                this.permutation[this.reversePermutation[n10++]] = n5;
                continue;
            }
            arrobject2[n5] = arrobject[n11];
            this.permutation[this.reversePermutation[n11++]] = n5;
        }
        for (n5 = n7; n5 < n8; ++n5) {
            this.reversePermutation[this.permutation[n5]] = n5;
        }
    }

    private void swap(int[] arrn, int n2, int n3) {
        int n4 = arrn[n2];
        arrn[n2] = arrn[n3];
        arrn[n3] = n4;
        this.permutation[this.reversePermutation[n2]] = n3;
        this.permutation[this.reversePermutation[n3]] = n2;
        int n5 = this.reversePermutation[n2];
        this.reversePermutation[n2] = this.reversePermutation[n3];
        this.reversePermutation[n3] = n5;
    }

    private void swap(Object[] arrobject, int n2, int n3) {
        Object object = arrobject[n2];
        arrobject[n2] = arrobject[n3];
        arrobject[n3] = object;
        this.permutation[this.reversePermutation[n2]] = n3;
        this.permutation[this.reversePermutation[n3]] = n2;
        int n4 = this.reversePermutation[n2];
        this.reversePermutation[n2] = this.reversePermutation[n3];
        this.reversePermutation[n3] = n4;
    }

    private int[] initPermutation(int n2) {
        this.permutation = new int[n2];
        this.reversePermutation = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.permutation[i2] = this.reversePermutation[i2] = i2;
        }
        return this.permutation;
    }
}

