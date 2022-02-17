/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ListBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class ListExpression<E>
implements ObservableListValue<E> {
    private static final ObservableList EMPTY_LIST = FXCollections.emptyObservableList();

    @Override
    public ObservableList<E> getValue() {
        return (ObservableList)this.get();
    }

    public static <E> ListExpression<E> listExpression(final ObservableListValue<E> observableListValue) {
        if (observableListValue == null) {
            throw new NullPointerException("List must be specified.");
        }
        return observableListValue instanceof ListExpression ? (ListExpression<E>)observableListValue : new ListBinding<E>(){
            {
                super.bind(observableListValue);
            }

            @Override
            public void dispose() {
                super.unbind(observableListValue);
            }

            @Override
            protected ObservableList<E> computeValue() {
                return (ObservableList)observableListValue.get();
            }

            @Override
            public ObservableList<ObservableListValue<E>> getDependencies() {
                return FXCollections.singletonObservableList(observableListValue);
            }
        };
    }

    public int getSize() {
        return this.size();
    }

    public abstract ReadOnlyIntegerProperty sizeProperty();

    public abstract ReadOnlyBooleanProperty emptyProperty();

    public ObjectBinding<E> valueAt(int n2) {
        return Bindings.valueAt(this, n2);
    }

    public ObjectBinding<E> valueAt(ObservableIntegerValue observableIntegerValue) {
        return Bindings.valueAt(this, observableIntegerValue);
    }

    public BooleanBinding isEqualTo(ObservableList<?> observableList) {
        return Bindings.equal(this, observableList);
    }

    public BooleanBinding isNotEqualTo(ObservableList<?> observableList) {
        return Bindings.notEqual(this, observableList);
    }

    public BooleanBinding isNull() {
        return Bindings.isNull(this);
    }

    public BooleanBinding isNotNull() {
        return Bindings.isNotNull(this);
    }

    public StringBinding asString() {
        return (StringBinding)StringFormatter.convert(this);
    }

    @Override
    public int size() {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.size() : observableList.size();
    }

    @Override
    public boolean isEmpty() {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.isEmpty() : observableList.isEmpty();
    }

    @Override
    public boolean contains(Object object) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.contains(object) : observableList.contains(object);
    }

    @Override
    public Iterator<E> iterator() {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.iterator() : observableList.iterator();
    }

    @Override
    public Object[] toArray() {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.toArray() : observableList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] arrT) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.toArray(arrT) : observableList.toArray(arrT);
    }

    @Override
    public boolean add(E e2) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.add(e2) : observableList.add(e2);
    }

    @Override
    public boolean remove(Object object) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.remove(object) : observableList.remove(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.contains(collection) : observableList.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.addAll(collection) : observableList.addAll(collection);
    }

    @Override
    public boolean addAll(int n2, Collection<? extends E> collection) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.addAll(n2, collection) : observableList.addAll(n2, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.removeAll(collection) : observableList.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.retainAll(collection) : observableList.retainAll(collection);
    }

    @Override
    public void clear() {
        ObservableList observableList = (ObservableList)this.get();
        if (observableList == null) {
            EMPTY_LIST.clear();
        } else {
            observableList.clear();
        }
    }

    @Override
    public E get(int n2) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.get(n2) : observableList.get(n2);
    }

    @Override
    public E set(int n2, E e2) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.set(n2, e2) : observableList.set(n2, e2);
    }

    @Override
    public void add(int n2, E e2) {
        ObservableList observableList = (ObservableList)this.get();
        if (observableList == null) {
            EMPTY_LIST.add(n2, e2);
        } else {
            observableList.add(n2, e2);
        }
    }

    @Override
    public E remove(int n2) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.remove(n2) : observableList.remove(n2);
    }

    @Override
    public int indexOf(Object object) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.indexOf(object) : observableList.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.lastIndexOf(object) : observableList.lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator() {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.listIterator() : observableList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int n2) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.listIterator(n2) : observableList.listIterator(n2);
    }

    @Override
    public List<E> subList(int n2, int n3) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.subList(n2, n3) : observableList.subList(n2, n3);
    }

    @Override
    public boolean addAll(E ... arrE) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.addAll(arrE) : observableList.addAll(arrE);
    }

    @Override
    public boolean setAll(E ... arrE) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.setAll(arrE) : observableList.setAll(arrE);
    }

    @Override
    public boolean setAll(Collection<? extends E> collection) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.setAll(collection) : observableList.setAll(collection);
    }

    @Override
    public boolean removeAll(E ... arrE) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.removeAll(arrE) : observableList.removeAll(arrE);
    }

    @Override
    public boolean retainAll(E ... arrE) {
        ObservableList observableList = (ObservableList)this.get();
        return observableList == null ? EMPTY_LIST.retainAll(arrE) : observableList.retainAll(arrE);
    }

    @Override
    public void remove(int n2, int n3) {
        ObservableList observableList = (ObservableList)this.get();
        if (observableList == null) {
            EMPTY_LIST.remove(n2, n3);
        } else {
            observableList.remove(n2, n3);
        }
    }
}

