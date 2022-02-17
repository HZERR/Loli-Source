/*
 * Decompiled with CFR 0.150.
 */
package javafx.collections.transformation;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SortHelper;
import com.sun.javafx.collections.SourceAdapterChange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

public final class SortedList<E>
extends TransformationList<E, E> {
    private Comparator<Element<E>> elementComparator;
    private Element<E>[] sorted;
    private int[] perm;
    private int size;
    private final SortHelper helper = new SortHelper();
    private final Element<E> tempElement = new Element<Object>(null, -1);
    private ObjectProperty<Comparator<? super E>> comparator;

    public SortedList(@NamedArg(value="source") ObservableList<? extends E> observableList, @NamedArg(value="comparator") Comparator<? super E> comparator) {
        super(observableList);
        this.sorted = new Element[observableList.size() * 3 / 2 + 1];
        this.perm = new int[this.sorted.length];
        this.size = observableList.size();
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.sorted[i2] = new Element(observableList.get(i2), i2);
            this.perm[i2] = i2;
        }
        if (comparator != null) {
            this.setComparator(comparator);
        }
    }

    public SortedList(@NamedArg(value="source") ObservableList<? extends E> observableList) {
        this(observableList, null);
    }

    @Override
    protected void sourceChanged(ListChangeListener.Change<? extends E> change) {
        if (this.elementComparator != null) {
            this.beginChange();
            while (change.next()) {
                if (change.wasPermutated()) {
                    this.updatePermutationIndexes(change);
                    continue;
                }
                if (change.wasUpdated()) {
                    this.update(change);
                    continue;
                }
                this.addRemove(change);
            }
            this.endChange();
        } else {
            this.updateUnsorted(change);
            this.fireChange(new SourceAdapterChange<E>(this, change));
        }
    }

    public final ObjectProperty<Comparator<? super E>> comparatorProperty() {
        if (this.comparator == null) {
            this.comparator = new ObjectPropertyBase<Comparator<? super E>>(){

                @Override
                protected void invalidated() {
                    Comparator comparator = (Comparator)this.get();
                    SortedList.this.elementComparator = comparator != null ? new ElementComparator(comparator) : null;
                    SortedList.this.doSortWithPermutationChange();
                }

                @Override
                public Object getBean() {
                    return SortedList.this;
                }

                @Override
                public String getName() {
                    return "comparator";
                }
            };
        }
        return this.comparator;
    }

    public final Comparator<? super E> getComparator() {
        return this.comparator == null ? null : (Comparator)this.comparator.get();
    }

    public final void setComparator(Comparator<? super E> comparator) {
        this.comparatorProperty().set(comparator);
    }

    @Override
    public E get(int n2) {
        if (n2 >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        return (E)((Element)this.sorted[n2]).e;
    }

    @Override
    public int size() {
        return this.size;
    }

    private void doSortWithPermutationChange() {
        if (this.elementComparator != null) {
            int[] arrn = this.helper.sort(this.sorted, 0, this.size, this.elementComparator);
            for (int i2 = 0; i2 < this.size; ++i2) {
                this.perm[((Element)this.sorted[i2]).index] = i2;
            }
            this.fireChange(new NonIterableChange.SimplePermutationChange(0, this.size, arrn, this));
        } else {
            int n2;
            int[] arrn = new int[this.size];
            int[] arrn2 = new int[this.size];
            for (n2 = 0; n2 < this.size; ++n2) {
                arrn[n2] = arrn2[n2] = n2;
            }
            n2 = 0;
            int n3 = 0;
            while (n3 < this.size) {
                int n4 = ((Element)this.sorted[n3]).index;
                if (n4 == n3) {
                    ++n3;
                    continue;
                }
                Element<E> element = this.sorted[n4];
                this.sorted[n4] = this.sorted[n3];
                this.sorted[n3] = element;
                this.perm[n3] = n3;
                this.perm[n4] = n4;
                arrn[arrn2[n3]] = n4;
                arrn[arrn2[n4]] = n3;
                int n5 = arrn2[n3];
                arrn2[n3] = arrn2[n4];
                arrn2[n4] = n5;
                n2 = 1;
            }
            if (n2 != 0) {
                this.fireChange(new NonIterableChange.SimplePermutationChange(0, this.size, arrn, this));
            }
        }
    }

    @Override
    public int getSourceIndex(int n2) {
        return ((Element)this.sorted[n2]).index;
    }

    private void updatePermutationIndexes(ListChangeListener.Change<? extends E> change) {
        int n2 = 0;
        while (n2 < this.size) {
            int n3 = change.getPermutation(((Element)this.sorted[n2]).index);
            ((Element)this.sorted[n2]).index = n3;
            this.perm[n3] = n2++;
        }
    }

    private void updateUnsorted(ListChangeListener.Change<? extends E> change) {
        while (change.next()) {
            if (change.wasPermutated()) {
                Element[] arrelement = new Element[this.sorted.length];
                for (int i2 = 0; i2 < this.size; ++i2) {
                    if (i2 >= change.getFrom() && i2 < change.getTo()) {
                        int n2 = change.getPermutation(i2);
                        arrelement[n2] = this.sorted[i2];
                        arrelement[n2].index = n2;
                        this.perm[i2] = i2;
                        continue;
                    }
                    arrelement[i2] = this.sorted[i2];
                }
                this.sorted = arrelement;
            }
            if (change.wasRemoved()) {
                int n3 = change.getFrom() + change.getRemovedSize();
                System.arraycopy(this.sorted, n3, this.sorted, change.getFrom(), this.size - n3);
                System.arraycopy(this.perm, n3, this.perm, change.getFrom(), this.size - n3);
                this.size -= change.getRemovedSize();
                this.updateIndices(n3, n3, -change.getRemovedSize());
            }
            if (!change.wasAdded()) continue;
            this.ensureSize(this.size + change.getAddedSize());
            this.updateIndices(change.getFrom(), change.getFrom(), change.getAddedSize());
            System.arraycopy(this.sorted, change.getFrom(), this.sorted, change.getTo(), this.size - change.getFrom());
            System.arraycopy(this.perm, change.getFrom(), this.perm, change.getTo(), this.size - change.getFrom());
            this.size += change.getAddedSize();
            for (int i3 = change.getFrom(); i3 < change.getTo(); ++i3) {
                this.sorted[i3] = new Element(change.getList().get(i3), i3);
                this.perm[i3] = i3;
            }
        }
    }

    private void ensureSize(int n2) {
        if (this.sorted.length < n2) {
            Element[] arrelement = new Element[n2 * 3 / 2 + 1];
            System.arraycopy(this.sorted, 0, arrelement, 0, this.size);
            this.sorted = arrelement;
            int[] arrn = new int[n2 * 3 / 2 + 1];
            System.arraycopy(this.perm, 0, arrn, 0, this.size);
            this.perm = arrn;
        }
    }

    private void updateIndices(int n2, int n3, int n4) {
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (((Element)this.sorted[i2]).index >= n2) {
                Element<E> element = this.sorted[i2];
                ((Element)element).index = ((Element)element).index + n4;
            }
            if (this.perm[i2] < n3) continue;
            int n5 = i2;
            this.perm[n5] = this.perm[n5] + n4;
        }
    }

    private int findPosition(E e2) {
        if (this.sorted.length == 0) {
            return 0;
        }
        ((Element)this.tempElement).e = e2;
        int n2 = Arrays.binarySearch(this.sorted, 0, this.size, this.tempElement, this.elementComparator);
        return n2;
    }

    private void insertToMapping(E e2, int n2) {
        int n3 = this.findPosition(e2);
        if (n3 < 0) {
            n3 ^= 0xFFFFFFFF;
        }
        this.ensureSize(this.size + 1);
        this.updateIndices(n2, n3, 1);
        System.arraycopy(this.sorted, n3, this.sorted, n3 + 1, this.size - n3);
        this.sorted[n3] = new Element<E>(e2, n2);
        System.arraycopy(this.perm, n2, this.perm, n2 + 1, this.size - n2);
        this.perm[n2] = n3;
        ++this.size;
        this.nextAdd(n3, n3 + 1);
    }

    private void setAllToMapping(List<? extends E> list, int n2) {
        this.ensureSize(n2);
        this.size = n2;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.sorted[i2] = new Element<E>(list.get(i2), i2);
        }
        int[] arrn = this.helper.sort(this.sorted, 0, this.size, this.elementComparator);
        System.arraycopy(arrn, 0, this.perm, 0, this.size);
        this.nextAdd(0, this.size);
    }

    private void removeFromMapping(int n2, E e2) {
        int n3 = this.perm[n2];
        System.arraycopy(this.sorted, n3 + 1, this.sorted, n3, this.size - n3 - 1);
        System.arraycopy(this.perm, n2 + 1, this.perm, n2, this.size - n2 - 1);
        --this.size;
        this.sorted[this.size] = null;
        this.updateIndices(n2 + 1, n3, -1);
        this.nextRemove(n3, e2);
    }

    private void removeAllFromMapping() {
        ArrayList arrayList = new ArrayList(this);
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.sorted[i2] = null;
        }
        this.size = 0;
        this.nextRemove(0, arrayList);
    }

    private void update(ListChangeListener.Change<? extends E> change) {
        int n2;
        int[] arrn = this.helper.sort(this.sorted, 0, this.size, this.elementComparator);
        for (n2 = 0; n2 < this.size; ++n2) {
            this.perm[((Element)this.sorted[n2]).index] = n2;
        }
        this.nextPermutation(0, this.size, arrn);
        int n3 = change.getTo();
        for (n2 = change.getFrom(); n2 < n3; ++n2) {
            this.nextUpdate(this.perm[n2]);
        }
    }

    private void addRemove(ListChangeListener.Change<? extends E> change) {
        int n2;
        int n3;
        if (change.getFrom() == 0 && change.getRemovedSize() == this.size) {
            this.removeAllFromMapping();
        } else {
            n3 = change.getRemovedSize();
            for (n2 = 0; n2 < n3; ++n2) {
                this.removeFromMapping(change.getFrom(), change.getRemoved().get(n2));
            }
        }
        if (this.size == 0) {
            this.setAllToMapping(change.getList(), change.getTo());
        } else {
            n3 = change.getTo();
            for (n2 = change.getFrom(); n2 < n3; ++n2) {
                this.insertToMapping(change.getList().get(n2), n2);
            }
        }
    }

    private static class ElementComparator<E>
    implements Comparator<Element<E>> {
        private final Comparator<? super E> comparator;

        public ElementComparator(Comparator<? super E> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(Element<E> element, Element<E> element2) {
            return this.comparator.compare(((Element)element).e, ((Element)element2).e);
        }
    }

    private static class Element<E> {
        private E e;
        private int index;

        public Element(E e2, int n2) {
            this.e = e2;
            this.index = n2;
        }
    }
}

