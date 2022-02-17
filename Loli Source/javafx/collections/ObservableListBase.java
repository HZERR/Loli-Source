/*
 * Decompiled with CFR 0.150.
 */
package javafx.collections;

import com.sun.javafx.collections.ListListenerHelper;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeBuilder;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class ObservableListBase<E>
extends AbstractList<E>
implements ObservableList<E> {
    private ListListenerHelper<E> listenerHelper;
    private final ListChangeBuilder<E> changeBuilder = new ListChangeBuilder(this);

    protected final void nextUpdate(int n2) {
        this.changeBuilder.nextUpdate(n2);
    }

    protected final void nextSet(int n2, E e2) {
        this.changeBuilder.nextSet(n2, e2);
    }

    protected final void nextReplace(int n2, int n3, List<? extends E> list) {
        this.changeBuilder.nextReplace(n2, n3, list);
    }

    protected final void nextRemove(int n2, List<? extends E> list) {
        this.changeBuilder.nextRemove(n2, list);
    }

    protected final void nextRemove(int n2, E e2) {
        this.changeBuilder.nextRemove(n2, e2);
    }

    protected final void nextPermutation(int n2, int n3, int[] arrn) {
        this.changeBuilder.nextPermutation(n2, n3, arrn);
    }

    protected final void nextAdd(int n2, int n3) {
        this.changeBuilder.nextAdd(n2, n3);
    }

    protected final void beginChange() {
        this.changeBuilder.beginChange();
    }

    protected final void endChange() {
        this.changeBuilder.endChange();
    }

    @Override
    public final void addListener(InvalidationListener invalidationListener) {
        this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, invalidationListener);
    }

    @Override
    public final void removeListener(InvalidationListener invalidationListener) {
        this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, invalidationListener);
    }

    @Override
    public final void addListener(ListChangeListener<? super E> listChangeListener) {
        this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, listChangeListener);
    }

    @Override
    public final void removeListener(ListChangeListener<? super E> listChangeListener) {
        this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, listChangeListener);
    }

    protected final void fireChange(ListChangeListener.Change<? extends E> change) {
        ListListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
    }

    protected final boolean hasListeners() {
        return ListListenerHelper.hasListeners(this.listenerHelper);
    }

    @Override
    public boolean addAll(E ... arrE) {
        return this.addAll(Arrays.asList(arrE));
    }

    @Override
    public boolean setAll(E ... arrE) {
        return this.setAll((Collection<? extends E>)Arrays.asList(arrE));
    }

    @Override
    public boolean setAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(E ... arrE) {
        return this.removeAll((Collection<?>)Arrays.asList(arrE));
    }

    @Override
    public boolean retainAll(E ... arrE) {
        return this.retainAll((Collection<?>)Arrays.asList(arrE));
    }

    @Override
    public void remove(int n2, int n3) {
        this.removeRange(n2, n3);
    }
}

