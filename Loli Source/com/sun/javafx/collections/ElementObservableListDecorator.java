/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ElementObserver;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.WeakListChangeListener;
import javafx.util.Callback;

public final class ElementObservableListDecorator<E>
extends ObservableListBase<E>
implements ObservableList<E> {
    private final ObservableList<E> decoratedList;
    private final ListChangeListener<E> listener;
    private ElementObserver<E> observer;

    public ElementObservableListDecorator(ObservableList<E> observableList, Callback<E, Observable[]> callback) {
        this.observer = new ElementObserver<E>(callback, new Callback<E, InvalidationListener>(){

            @Override
            public InvalidationListener call(final E e2) {
                return new InvalidationListener(){

                    @Override
                    public void invalidated(Observable observable) {
                        int n2;
                        ElementObservableListDecorator.this.beginChange();
                        if (ElementObservableListDecorator.this.decoratedList instanceof RandomAccess) {
                            int n3 = ElementObservableListDecorator.this.size();
                            for (n2 = 0; n2 < n3; ++n2) {
                                if (ElementObservableListDecorator.this.get(n2) != e2) continue;
                                ElementObservableListDecorator.this.nextUpdate(n2);
                            }
                        } else {
                            Iterator iterator = ElementObservableListDecorator.this.iterator();
                            while (iterator.hasNext()) {
                                if (iterator.next() == e2) {
                                    ElementObservableListDecorator.this.nextUpdate(n2);
                                }
                                ++n2;
                            }
                        }
                        ElementObservableListDecorator.this.endChange();
                    }
                };
            }
        }, this);
        this.decoratedList = observableList;
        int n2 = this.decoratedList.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.observer.attachListener(this.decoratedList.get(i2));
        }
        this.listener = new ListChangeListener<E>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends E> change) {
                while (change.next()) {
                    int n2;
                    if (!change.wasAdded() && !change.wasRemoved()) continue;
                    int n3 = change.getRemovedSize();
                    List list = change.getRemoved();
                    for (n2 = 0; n2 < n3; ++n2) {
                        ElementObservableListDecorator.this.observer.detachListener(list.get(n2));
                    }
                    if (ElementObservableListDecorator.this.decoratedList instanceof RandomAccess) {
                        n2 = change.getTo();
                        for (int i2 = change.getFrom(); i2 < n2; ++i2) {
                            ElementObservableListDecorator.this.observer.attachListener(ElementObservableListDecorator.this.decoratedList.get(i2));
                        }
                        continue;
                    }
                    for (Object e2 : change.getAddedSubList()) {
                        ElementObservableListDecorator.this.observer.attachListener(e2);
                    }
                }
                change.reset();
                ElementObservableListDecorator.this.fireChange(change);
            }
        };
        this.decoratedList.addListener(new WeakListChangeListener<E>(this.listener));
    }

    @Override
    public <T> T[] toArray(T[] arrT) {
        return this.decoratedList.toArray(arrT);
    }

    @Override
    public Object[] toArray() {
        return this.decoratedList.toArray();
    }

    @Override
    public List<E> subList(int n2, int n3) {
        return this.decoratedList.subList(n2, n3);
    }

    @Override
    public int size() {
        return this.decoratedList.size();
    }

    @Override
    public E set(int n2, E e2) {
        return this.decoratedList.set(n2, e2);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return this.decoratedList.retainAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return this.decoratedList.removeAll(collection);
    }

    @Override
    public E remove(int n2) {
        return this.decoratedList.remove(n2);
    }

    @Override
    public boolean remove(Object object) {
        return this.decoratedList.remove(object);
    }

    @Override
    public ListIterator<E> listIterator(int n2) {
        return this.decoratedList.listIterator(n2);
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.decoratedList.listIterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return this.decoratedList.lastIndexOf(object);
    }

    @Override
    public Iterator<E> iterator() {
        return this.decoratedList.iterator();
    }

    @Override
    public boolean isEmpty() {
        return this.decoratedList.isEmpty();
    }

    @Override
    public int indexOf(Object object) {
        return this.decoratedList.indexOf(object);
    }

    @Override
    public E get(int n2) {
        return this.decoratedList.get(n2);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return this.decoratedList.containsAll(collection);
    }

    @Override
    public boolean contains(Object object) {
        return this.decoratedList.contains(object);
    }

    @Override
    public void clear() {
        this.decoratedList.clear();
    }

    @Override
    public boolean addAll(int n2, Collection<? extends E> collection) {
        return this.decoratedList.addAll(n2, collection);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return this.decoratedList.addAll(collection);
    }

    @Override
    public void add(int n2, E e2) {
        this.decoratedList.add(n2, e2);
    }

    @Override
    public boolean add(E e2) {
        return this.decoratedList.add(e2);
    }

    @Override
    public boolean setAll(Collection<? extends E> collection) {
        return this.decoratedList.setAll(collection);
    }

    @Override
    public boolean setAll(E ... arrE) {
        return this.decoratedList.setAll(arrE);
    }

    @Override
    public boolean retainAll(E ... arrE) {
        return this.decoratedList.retainAll(arrE);
    }

    @Override
    public boolean removeAll(E ... arrE) {
        return this.decoratedList.removeAll(arrE);
    }

    @Override
    public void remove(int n2, int n3) {
        this.decoratedList.remove(n2, n3);
    }

    @Override
    public boolean addAll(E ... arrE) {
        return this.decoratedList.addAll(arrE);
    }
}

