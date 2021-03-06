/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.binding;

import com.sun.javafx.binding.ExpressionHelperBase;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableSetValue;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public abstract class SetExpressionHelper<E>
extends ExpressionHelperBase {
    protected final ObservableSetValue<E> observable;

    public static <E> SetExpressionHelper<E> addListener(SetExpressionHelper<E> setExpressionHelper, ObservableSetValue<E> observableSetValue, InvalidationListener invalidationListener) {
        if (observableSetValue == null || invalidationListener == null) {
            throw new NullPointerException();
        }
        observableSetValue.getValue();
        return setExpressionHelper == null ? new SingleInvalidation(observableSetValue, invalidationListener) : setExpressionHelper.addListener(invalidationListener);
    }

    public static <E> SetExpressionHelper<E> removeListener(SetExpressionHelper<E> setExpressionHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return setExpressionHelper == null ? null : setExpressionHelper.removeListener(invalidationListener);
    }

    public static <E> SetExpressionHelper<E> addListener(SetExpressionHelper<E> setExpressionHelper, ObservableSetValue<E> observableSetValue, ChangeListener<? super ObservableSet<E>> changeListener) {
        if (observableSetValue == null || changeListener == null) {
            throw new NullPointerException();
        }
        return setExpressionHelper == null ? new SingleChange(observableSetValue, changeListener) : setExpressionHelper.addListener(changeListener);
    }

    public static <E> SetExpressionHelper<E> removeListener(SetExpressionHelper<E> setExpressionHelper, ChangeListener<? super ObservableSet<E>> changeListener) {
        if (changeListener == null) {
            throw new NullPointerException();
        }
        return setExpressionHelper == null ? null : setExpressionHelper.removeListener(changeListener);
    }

    public static <E> SetExpressionHelper<E> addListener(SetExpressionHelper<E> setExpressionHelper, ObservableSetValue<E> observableSetValue, SetChangeListener<? super E> setChangeListener) {
        if (observableSetValue == null || setChangeListener == null) {
            throw new NullPointerException();
        }
        return setExpressionHelper == null ? new SingleSetChange(observableSetValue, setChangeListener) : setExpressionHelper.addListener(setChangeListener);
    }

    public static <E> SetExpressionHelper<E> removeListener(SetExpressionHelper<E> setExpressionHelper, SetChangeListener<? super E> setChangeListener) {
        if (setChangeListener == null) {
            throw new NullPointerException();
        }
        return setExpressionHelper == null ? null : setExpressionHelper.removeListener(setChangeListener);
    }

    public static <E> void fireValueChangedEvent(SetExpressionHelper<E> setExpressionHelper) {
        if (setExpressionHelper != null) {
            setExpressionHelper.fireValueChangedEvent();
        }
    }

    public static <E> void fireValueChangedEvent(SetExpressionHelper<E> setExpressionHelper, SetChangeListener.Change<? extends E> change) {
        if (setExpressionHelper != null) {
            setExpressionHelper.fireValueChangedEvent(change);
        }
    }

    protected SetExpressionHelper(ObservableSetValue<E> observableSetValue) {
        this.observable = observableSetValue;
    }

    protected abstract SetExpressionHelper<E> addListener(InvalidationListener var1);

    protected abstract SetExpressionHelper<E> removeListener(InvalidationListener var1);

    protected abstract SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> var1);

    protected abstract SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> var1);

    protected abstract SetExpressionHelper<E> addListener(SetChangeListener<? super E> var1);

    protected abstract SetExpressionHelper<E> removeListener(SetChangeListener<? super E> var1);

    protected abstract void fireValueChangedEvent();

    protected abstract void fireValueChangedEvent(SetChangeListener.Change<? extends E> var1);

    public static class SimpleChange<E>
    extends SetChangeListener.Change<E> {
        private E old;
        private E added;
        private boolean addOp;

        public SimpleChange(ObservableSet<E> observableSet) {
            super(observableSet);
        }

        public SimpleChange(ObservableSet<E> observableSet, SetChangeListener.Change<? extends E> change) {
            super(observableSet);
            this.old = change.getElementRemoved();
            this.added = change.getElementAdded();
            this.addOp = change.wasAdded();
        }

        public SimpleChange<E> setRemoved(E e2) {
            this.old = e2;
            this.added = null;
            this.addOp = false;
            return this;
        }

        public SimpleChange<E> setAdded(E e2) {
            this.old = null;
            this.added = e2;
            this.addOp = true;
            return this;
        }

        @Override
        public boolean wasAdded() {
            return this.addOp;
        }

        @Override
        public boolean wasRemoved() {
            return !this.addOp;
        }

        @Override
        public E getElementAdded() {
            return this.added;
        }

        @Override
        public E getElementRemoved() {
            return this.old;
        }

        public String toString() {
            return this.addOp ? "added " + this.added : "removed " + this.old;
        }
    }

    private static class Generic<E>
    extends SetExpressionHelper<E> {
        private InvalidationListener[] invalidationListeners;
        private ChangeListener<? super ObservableSet<E>>[] changeListeners;
        private SetChangeListener<? super E>[] setChangeListeners;
        private int invalidationSize;
        private int changeSize;
        private int setChangeSize;
        private boolean locked;
        private ObservableSet<E> currentValue;

        private Generic(ObservableSetValue<E> observableSetValue, InvalidationListener invalidationListener, InvalidationListener invalidationListener2) {
            super(observableSetValue);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener, invalidationListener2};
            this.invalidationSize = 2;
        }

        private Generic(ObservableSetValue<E> observableSetValue, ChangeListener<? super ObservableSet<E>> changeListener, ChangeListener<? super ObservableSet<E>> changeListener2) {
            super(observableSetValue);
            this.changeListeners = new ChangeListener[]{changeListener, changeListener2};
            this.changeSize = 2;
            this.currentValue = (ObservableSet)observableSetValue.getValue();
        }

        private Generic(ObservableSetValue<E> observableSetValue, SetChangeListener<? super E> setChangeListener, SetChangeListener<? super E> setChangeListener2) {
            super(observableSetValue);
            this.setChangeListeners = new SetChangeListener[]{setChangeListener, setChangeListener2};
            this.setChangeSize = 2;
            this.currentValue = (ObservableSet)observableSetValue.getValue();
        }

        private Generic(ObservableSetValue<E> observableSetValue, InvalidationListener invalidationListener, ChangeListener<? super ObservableSet<E>> changeListener) {
            super(observableSetValue);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.currentValue = (ObservableSet)observableSetValue.getValue();
        }

        private Generic(ObservableSetValue<E> observableSetValue, InvalidationListener invalidationListener, SetChangeListener<? super E> setChangeListener) {
            super(observableSetValue);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.setChangeListeners = new SetChangeListener[]{setChangeListener};
            this.setChangeSize = 1;
            this.currentValue = (ObservableSet)observableSetValue.getValue();
        }

        private Generic(ObservableSetValue<E> observableSetValue, ChangeListener<? super ObservableSet<E>> changeListener, SetChangeListener<? super E> setChangeListener) {
            super(observableSetValue);
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.setChangeListeners = new SetChangeListener[]{setChangeListener};
            this.setChangeSize = 1;
            this.currentValue = (ObservableSet)observableSetValue.getValue();
        }

        @Override
        protected SetExpressionHelper<E> addListener(InvalidationListener invalidationListener) {
            if (this.invalidationListeners == null) {
                this.invalidationListeners = new InvalidationListener[]{invalidationListener};
                this.invalidationSize = 1;
            } else {
                int n2 = this.invalidationListeners.length;
                if (this.locked) {
                    int n3 = this.invalidationSize < n2 ? n2 : n2 * 3 / 2 + 1;
                    this.invalidationListeners = Arrays.copyOf(this.invalidationListeners, n3);
                } else if (this.invalidationSize == n2) {
                    this.invalidationSize = Generic.trim(this.invalidationSize, this.invalidationListeners);
                    if (this.invalidationSize == n2) {
                        int n4 = n2 * 3 / 2 + 1;
                        this.invalidationListeners = Arrays.copyOf(this.invalidationListeners, n4);
                    }
                }
                this.invalidationListeners[this.invalidationSize++] = invalidationListener;
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> removeListener(InvalidationListener invalidationListener) {
            if (this.invalidationListeners != null) {
                for (int i2 = 0; i2 < this.invalidationSize; ++i2) {
                    if (!invalidationListener.equals(this.invalidationListeners[i2])) continue;
                    if (this.invalidationSize == 1) {
                        if (this.changeSize == 1 && this.setChangeSize == 0) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        if (this.changeSize == 0 && this.setChangeSize == 1) {
                            return new SingleSetChange(this.observable, this.setChangeListeners[0]);
                        }
                        this.invalidationListeners = null;
                        this.invalidationSize = 0;
                        break;
                    }
                    int n2 = this.invalidationSize - i2 - 1;
                    InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
                    if (this.locked) {
                        this.invalidationListeners = new InvalidationListener[this.invalidationListeners.length];
                        System.arraycopy(arrinvalidationListener, 0, this.invalidationListeners, 0, i2 + 1);
                    }
                    if (n2 > 0) {
                        System.arraycopy(arrinvalidationListener, i2 + 1, this.invalidationListeners, i2, n2);
                    }
                    --this.invalidationSize;
                    if (this.locked) break;
                    this.invalidationListeners[--this.invalidationSize] = null;
                    break;
                }
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> changeListener) {
            if (this.changeListeners == null) {
                this.changeListeners = new ChangeListener[]{changeListener};
                this.changeSize = 1;
            } else {
                int n2 = this.changeListeners.length;
                if (this.locked) {
                    int n3 = this.changeSize < n2 ? n2 : n2 * 3 / 2 + 1;
                    this.changeListeners = Arrays.copyOf(this.changeListeners, n3);
                } else if (this.changeSize == n2) {
                    this.changeSize = Generic.trim(this.changeSize, this.changeListeners);
                    if (this.changeSize == n2) {
                        int n4 = n2 * 3 / 2 + 1;
                        this.changeListeners = Arrays.copyOf(this.changeListeners, n4);
                    }
                }
                this.changeListeners[this.changeSize++] = changeListener;
            }
            if (this.changeSize == 1) {
                this.currentValue = (ObservableSet)this.observable.getValue();
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> changeListener) {
            if (this.changeListeners != null) {
                for (int i2 = 0; i2 < this.changeSize; ++i2) {
                    if (!changeListener.equals(this.changeListeners[i2])) continue;
                    if (this.changeSize == 1) {
                        if (this.invalidationSize == 1 && this.setChangeSize == 0) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        if (this.invalidationSize == 0 && this.setChangeSize == 1) {
                            return new SingleSetChange(this.observable, this.setChangeListeners[0]);
                        }
                        this.changeListeners = null;
                        this.changeSize = 0;
                        break;
                    }
                    int n2 = this.changeSize - i2 - 1;
                    ChangeListener<? super ObservableSet<E>>[] arrchangeListener = this.changeListeners;
                    if (this.locked) {
                        this.changeListeners = new ChangeListener[this.changeListeners.length];
                        System.arraycopy(arrchangeListener, 0, this.changeListeners, 0, i2 + 1);
                    }
                    if (n2 > 0) {
                        System.arraycopy(arrchangeListener, i2 + 1, this.changeListeners, i2, n2);
                    }
                    --this.changeSize;
                    if (this.locked) break;
                    this.changeListeners[this.changeSize] = null;
                    break;
                }
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> setChangeListener) {
            if (this.setChangeListeners == null) {
                this.setChangeListeners = new SetChangeListener[]{setChangeListener};
                this.setChangeSize = 1;
            } else {
                int n2 = this.setChangeListeners.length;
                if (this.locked) {
                    int n3 = this.setChangeSize < n2 ? n2 : n2 * 3 / 2 + 1;
                    this.setChangeListeners = Arrays.copyOf(this.setChangeListeners, n3);
                } else if (this.setChangeSize == n2) {
                    this.setChangeSize = Generic.trim(this.setChangeSize, this.setChangeListeners);
                    if (this.setChangeSize == n2) {
                        int n4 = n2 * 3 / 2 + 1;
                        this.setChangeListeners = Arrays.copyOf(this.setChangeListeners, n4);
                    }
                }
                this.setChangeListeners[this.setChangeSize++] = setChangeListener;
            }
            if (this.setChangeSize == 1) {
                this.currentValue = (ObservableSet)this.observable.getValue();
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> setChangeListener) {
            if (this.setChangeListeners != null) {
                for (int i2 = 0; i2 < this.setChangeSize; ++i2) {
                    if (!setChangeListener.equals(this.setChangeListeners[i2])) continue;
                    if (this.setChangeSize == 1) {
                        if (this.invalidationSize == 1 && this.changeSize == 0) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        if (this.invalidationSize == 0 && this.changeSize == 1) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        this.setChangeListeners = null;
                        this.setChangeSize = 0;
                        break;
                    }
                    int n2 = this.setChangeSize - i2 - 1;
                    SetChangeListener<? super E>[] arrsetChangeListener = this.setChangeListeners;
                    if (this.locked) {
                        this.setChangeListeners = new SetChangeListener[this.setChangeListeners.length];
                        System.arraycopy(arrsetChangeListener, 0, this.setChangeListeners, 0, i2 + 1);
                    }
                    if (n2 > 0) {
                        System.arraycopy(arrsetChangeListener, i2 + 1, this.setChangeListeners, i2, n2);
                    }
                    --this.setChangeSize;
                    if (this.locked) break;
                    this.setChangeListeners[this.setChangeSize] = null;
                    break;
                }
            }
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            if (this.changeSize == 0 && this.setChangeSize == 0) {
                this.notifyListeners(this.currentValue, null);
            } else {
                ObservableSet<E> observableSet = this.currentValue;
                this.currentValue = (ObservableSet)this.observable.getValue();
                this.notifyListeners(observableSet, null);
            }
        }

        @Override
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            SimpleChange<? extends E> simpleChange = this.setChangeSize == 0 ? null : new SimpleChange<E>(this.observable, change);
            this.notifyListeners(this.currentValue, simpleChange);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void notifyListeners(ObservableSet<E> observableSet, SimpleChange<E> simpleChange) {
            InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
            int n2 = this.invalidationSize;
            ChangeListener<? super ObservableSet<E>>[] arrchangeListener = this.changeListeners;
            int n3 = this.changeSize;
            SetChangeListener<? super E>[] arrsetChangeListener = this.setChangeListeners;
            int n4 = this.setChangeSize;
            try {
                int n5;
                this.locked = true;
                for (n5 = 0; n5 < n2; ++n5) {
                    arrinvalidationListener[n5].invalidated(this.observable);
                }
                if (this.currentValue != observableSet || simpleChange != null) {
                    for (n5 = 0; n5 < n3; ++n5) {
                        arrchangeListener[n5].changed(this.observable, observableSet, this.currentValue);
                    }
                    if (n4 > 0) {
                        if (simpleChange != null) {
                            for (n5 = 0; n5 < n4; ++n5) {
                                arrsetChangeListener[n5].onChanged(simpleChange);
                            }
                        } else {
                            simpleChange = new SimpleChange(this.observable);
                            if (this.currentValue == null) {
                                for (Object e2 : observableSet) {
                                    simpleChange.setRemoved(e2);
                                    for (int i2 = 0; i2 < n4; ++i2) {
                                        arrsetChangeListener[i2].onChanged(simpleChange);
                                    }
                                }
                            } else if (observableSet == null) {
                                for (Object e3 : this.currentValue) {
                                    simpleChange.setAdded(e3);
                                    for (int i3 = 0; i3 < n4; ++i3) {
                                        arrsetChangeListener[i3].onChanged(simpleChange);
                                    }
                                }
                            } else {
                                int n6;
                                for (Object e4 : observableSet) {
                                    if (this.currentValue.contains(e4)) continue;
                                    simpleChange.setRemoved(e4);
                                    for (n6 = 0; n6 < n4; ++n6) {
                                        arrsetChangeListener[n6].onChanged(simpleChange);
                                    }
                                }
                                for (Object e4 : this.currentValue) {
                                    if (observableSet.contains(e4)) continue;
                                    simpleChange.setAdded(e4);
                                    for (n6 = 0; n6 < n4; ++n6) {
                                        arrsetChangeListener[n6].onChanged(simpleChange);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            finally {
                this.locked = false;
            }
        }
    }

    private static class SingleSetChange<E>
    extends SetExpressionHelper<E> {
        private final SetChangeListener<? super E> listener;
        private ObservableSet<E> currentValue;

        private SingleSetChange(ObservableSetValue<E> observableSetValue, SetChangeListener<? super E> setChangeListener) {
            super(observableSetValue);
            this.listener = setChangeListener;
            this.currentValue = (ObservableSet)observableSetValue.getValue();
        }

        @Override
        protected SetExpressionHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, invalidationListener, this.listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(InvalidationListener invalidationListener) {
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> changeListener) {
            return new Generic(this.observable, changeListener, this.listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> changeListener) {
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> setChangeListener) {
            return new Generic(this.observable, this.listener, setChangeListener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> setChangeListener) {
            return setChangeListener.equals(this.listener) ? null : this;
        }

        @Override
        protected void fireValueChangedEvent() {
            block8: {
                ObservableSet<E> observableSet = this.currentValue;
                this.currentValue = (ObservableSet)this.observable.getValue();
                if (this.currentValue == observableSet) break block8;
                SimpleChange simpleChange = new SimpleChange(this.observable);
                if (this.currentValue == null) {
                    for (Object e2 : observableSet) {
                        this.listener.onChanged(simpleChange.setRemoved(e2));
                    }
                } else if (observableSet == null) {
                    for (Object e3 : this.currentValue) {
                        this.listener.onChanged(simpleChange.setAdded(e3));
                    }
                } else {
                    for (Object e4 : observableSet) {
                        if (this.currentValue.contains(e4)) continue;
                        this.listener.onChanged(simpleChange.setRemoved(e4));
                    }
                    for (Object e4 : this.currentValue) {
                        if (observableSet.contains(e4)) continue;
                        this.listener.onChanged(simpleChange.setAdded(e4));
                    }
                }
            }
        }

        @Override
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            this.listener.onChanged(new SimpleChange<E>(this.observable, change));
        }
    }

    private static class SingleChange<E>
    extends SetExpressionHelper<E> {
        private final ChangeListener<? super ObservableSet<E>> listener;
        private ObservableSet<E> currentValue;

        private SingleChange(ObservableSetValue<E> observableSetValue, ChangeListener<? super ObservableSet<E>> changeListener) {
            super(observableSetValue);
            this.listener = changeListener;
            this.currentValue = (ObservableSet)observableSetValue.getValue();
        }

        @Override
        protected SetExpressionHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, invalidationListener, this.listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(InvalidationListener invalidationListener) {
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> changeListener) {
            return new Generic(this.observable, this.listener, changeListener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> changeListener) {
            return changeListener.equals(this.listener) ? null : this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> setChangeListener) {
            return new Generic(this.observable, this.listener, setChangeListener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> setChangeListener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            ObservableSet<E> observableSet = this.currentValue;
            this.currentValue = (ObservableSet)this.observable.getValue();
            if (this.currentValue != observableSet) {
                this.listener.changed(this.observable, observableSet, this.currentValue);
            }
        }

        @Override
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            this.listener.changed(this.observable, this.currentValue, this.currentValue);
        }
    }

    private static class SingleInvalidation<E>
    extends SetExpressionHelper<E> {
        private final InvalidationListener listener;

        private SingleInvalidation(ObservableSetValue<E> observableSetValue, InvalidationListener invalidationListener) {
            super(observableSetValue);
            this.listener = invalidationListener;
        }

        @Override
        protected SetExpressionHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, this.listener, invalidationListener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(InvalidationListener invalidationListener) {
            return invalidationListener.equals(this.listener) ? null : this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> changeListener) {
            return new Generic(this.observable, this.listener, changeListener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> changeListener) {
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> setChangeListener) {
            return new Generic(this.observable, this.listener, setChangeListener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> setChangeListener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            this.listener.invalidated(this.observable);
        }

        @Override
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            this.listener.invalidated(this.observable);
        }
    }
}

