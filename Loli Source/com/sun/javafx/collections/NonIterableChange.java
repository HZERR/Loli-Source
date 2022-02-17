/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ChangeHelper;
import java.util.Collections;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class NonIterableChange<E>
extends ListChangeListener.Change<E> {
    private final int from;
    private final int to;
    private boolean invalid = true;
    private static final int[] EMPTY_PERM = new int[0];

    protected NonIterableChange(int n2, int n3, ObservableList<E> observableList) {
        super(observableList);
        this.from = n2;
        this.to = n3;
    }

    @Override
    public int getFrom() {
        this.checkState();
        return this.from;
    }

    @Override
    public int getTo() {
        this.checkState();
        return this.to;
    }

    @Override
    protected int[] getPermutation() {
        this.checkState();
        return EMPTY_PERM;
    }

    @Override
    public boolean next() {
        if (this.invalid) {
            this.invalid = false;
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        this.invalid = true;
    }

    public void checkState() {
        if (this.invalid) {
            throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
        }
    }

    public String toString() {
        boolean bl = this.invalid;
        this.invalid = false;
        String string = this.wasPermutated() ? ChangeHelper.permChangeToString(this.getPermutation()) : (this.wasUpdated() ? ChangeHelper.updateChangeToString(this.from, this.to) : ChangeHelper.addRemoveChangeToString(this.from, this.to, this.getList(), this.getRemoved()));
        this.invalid = bl;
        return "{ " + string + " }";
    }

    public static class SimpleUpdateChange<E>
    extends NonIterableChange<E> {
        public SimpleUpdateChange(int n2, ObservableList<E> observableList) {
            this(n2, n2 + 1, observableList);
        }

        public SimpleUpdateChange(int n2, int n3, ObservableList<E> observableList) {
            super(n2, n3, observableList);
        }

        @Override
        public List<E> getRemoved() {
            return Collections.emptyList();
        }

        @Override
        public boolean wasUpdated() {
            return true;
        }
    }

    public static class SimplePermutationChange<E>
    extends NonIterableChange<E> {
        private final int[] permutation;

        public SimplePermutationChange(int n2, int n3, int[] arrn, ObservableList<E> observableList) {
            super(n2, n3, observableList);
            this.permutation = arrn;
        }

        @Override
        public List<E> getRemoved() {
            this.checkState();
            return Collections.emptyList();
        }

        @Override
        protected int[] getPermutation() {
            this.checkState();
            return this.permutation;
        }
    }

    public static class SimpleAddChange<E>
    extends NonIterableChange<E> {
        public SimpleAddChange(int n2, int n3, ObservableList<E> observableList) {
            super(n2, n3, observableList);
        }

        @Override
        public boolean wasRemoved() {
            this.checkState();
            return false;
        }

        @Override
        public List<E> getRemoved() {
            this.checkState();
            return Collections.emptyList();
        }
    }

    public static class SimpleRemovedChange<E>
    extends NonIterableChange<E> {
        private final List<E> removed;

        public SimpleRemovedChange(int n2, int n3, E e2, ObservableList<E> observableList) {
            super(n2, n3, observableList);
            this.removed = Collections.singletonList(e2);
        }

        @Override
        public boolean wasRemoved() {
            this.checkState();
            return true;
        }

        @Override
        public List<E> getRemoved() {
            this.checkState();
            return this.removed;
        }
    }

    public static class GenericAddRemoveChange<E>
    extends NonIterableChange<E> {
        private final List<E> removed;

        public GenericAddRemoveChange(int n2, int n3, List<E> list, ObservableList<E> observableList) {
            super(n2, n3, observableList);
            this.removed = list;
        }

        @Override
        public List<E> getRemoved() {
            this.checkState();
            return this.removed;
        }
    }
}

