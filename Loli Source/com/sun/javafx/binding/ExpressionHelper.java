/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.binding;

import com.sun.javafx.binding.ExpressionHelperBase;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class ExpressionHelper<T>
extends ExpressionHelperBase {
    protected final ObservableValue<T> observable;

    public static <T> ExpressionHelper<T> addListener(ExpressionHelper<T> expressionHelper, ObservableValue<T> observableValue, InvalidationListener invalidationListener) {
        if (observableValue == null || invalidationListener == null) {
            throw new NullPointerException();
        }
        observableValue.getValue();
        return expressionHelper == null ? new SingleInvalidation(observableValue, invalidationListener) : expressionHelper.addListener(invalidationListener);
    }

    public static <T> ExpressionHelper<T> removeListener(ExpressionHelper<T> expressionHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return expressionHelper == null ? null : expressionHelper.removeListener(invalidationListener);
    }

    public static <T> ExpressionHelper<T> addListener(ExpressionHelper<T> expressionHelper, ObservableValue<T> observableValue, ChangeListener<? super T> changeListener) {
        if (observableValue == null || changeListener == null) {
            throw new NullPointerException();
        }
        return expressionHelper == null ? new SingleChange(observableValue, changeListener) : expressionHelper.addListener(changeListener);
    }

    public static <T> ExpressionHelper<T> removeListener(ExpressionHelper<T> expressionHelper, ChangeListener<? super T> changeListener) {
        if (changeListener == null) {
            throw new NullPointerException();
        }
        return expressionHelper == null ? null : expressionHelper.removeListener(changeListener);
    }

    public static <T> void fireValueChangedEvent(ExpressionHelper<T> expressionHelper) {
        if (expressionHelper != null) {
            expressionHelper.fireValueChangedEvent();
        }
    }

    private ExpressionHelper(ObservableValue<T> observableValue) {
        this.observable = observableValue;
    }

    protected abstract ExpressionHelper<T> addListener(InvalidationListener var1);

    protected abstract ExpressionHelper<T> removeListener(InvalidationListener var1);

    protected abstract ExpressionHelper<T> addListener(ChangeListener<? super T> var1);

    protected abstract ExpressionHelper<T> removeListener(ChangeListener<? super T> var1);

    protected abstract void fireValueChangedEvent();

    private static class Generic<T>
    extends ExpressionHelper<T> {
        private InvalidationListener[] invalidationListeners;
        private ChangeListener<? super T>[] changeListeners;
        private int invalidationSize;
        private int changeSize;
        private boolean locked;
        private T currentValue;

        private Generic(ObservableValue<T> observableValue, InvalidationListener invalidationListener, InvalidationListener invalidationListener2) {
            super(observableValue);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener, invalidationListener2};
            this.invalidationSize = 2;
        }

        private Generic(ObservableValue<T> observableValue, ChangeListener<? super T> changeListener, ChangeListener<? super T> changeListener2) {
            super(observableValue);
            this.changeListeners = new ChangeListener[]{changeListener, changeListener2};
            this.changeSize = 2;
            this.currentValue = observableValue.getValue();
        }

        private Generic(ObservableValue<T> observableValue, InvalidationListener invalidationListener, ChangeListener<? super T> changeListener) {
            super(observableValue);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.currentValue = observableValue.getValue();
        }

        @Override
        protected Generic<T> addListener(InvalidationListener invalidationListener) {
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
        protected ExpressionHelper<T> removeListener(InvalidationListener invalidationListener) {
            if (this.invalidationListeners != null) {
                for (int i2 = 0; i2 < this.invalidationSize; ++i2) {
                    if (!invalidationListener.equals(this.invalidationListeners[i2])) continue;
                    if (this.invalidationSize == 1) {
                        if (this.changeSize == 1) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        this.invalidationListeners = null;
                        this.invalidationSize = 0;
                        break;
                    }
                    if (this.invalidationSize == 2 && this.changeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[1 - i2]);
                    }
                    int n2 = this.invalidationSize - i2 - 1;
                    InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
                    if (this.locked) {
                        this.invalidationListeners = new InvalidationListener[this.invalidationListeners.length];
                        System.arraycopy(arrinvalidationListener, 0, this.invalidationListeners, 0, i2);
                    }
                    if (n2 > 0) {
                        System.arraycopy(arrinvalidationListener, i2 + 1, this.invalidationListeners, i2, n2);
                    }
                    --this.invalidationSize;
                    if (this.locked) break;
                    this.invalidationListeners[this.invalidationSize] = null;
                    break;
                }
            }
            return this;
        }

        @Override
        protected ExpressionHelper<T> addListener(ChangeListener<? super T> changeListener) {
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
                this.currentValue = this.observable.getValue();
            }
            return this;
        }

        @Override
        protected ExpressionHelper<T> removeListener(ChangeListener<? super T> changeListener) {
            if (this.changeListeners != null) {
                for (int i2 = 0; i2 < this.changeSize; ++i2) {
                    if (!changeListener.equals(this.changeListeners[i2])) continue;
                    if (this.changeSize == 1) {
                        if (this.invalidationSize == 1) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        this.changeListeners = null;
                        this.changeSize = 0;
                        break;
                    }
                    if (this.changeSize == 2 && this.invalidationSize == 0) {
                        return new SingleChange(this.observable, this.changeListeners[1 - i2]);
                    }
                    int n2 = this.changeSize - i2 - 1;
                    ChangeListener<? super T>[] arrchangeListener = this.changeListeners;
                    if (this.locked) {
                        this.changeListeners = new ChangeListener[this.changeListeners.length];
                        System.arraycopy(arrchangeListener, 0, this.changeListeners, 0, i2);
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

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        protected void fireValueChangedEvent() {
            InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
            int n2 = this.invalidationSize;
            ChangeListener<? super T>[] arrchangeListener = this.changeListeners;
            int n3 = this.changeSize;
            try {
                this.locked = true;
                for (int i2 = 0; i2 < n2; ++i2) {
                    try {
                        arrinvalidationListener[i2].invalidated(this.observable);
                        continue;
                    }
                    catch (Exception exception) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
                    }
                }
                if (n3 > 0) {
                    boolean bl;
                    T t2 = this.currentValue;
                    this.currentValue = this.observable.getValue();
                    boolean bl2 = this.currentValue == null ? t2 != null : (bl = !this.currentValue.equals(t2));
                    if (bl) {
                        for (int i3 = 0; i3 < n3; ++i3) {
                            try {
                                arrchangeListener[i3].changed(this.observable, t2, this.currentValue);
                                continue;
                            }
                            catch (Exception exception) {
                                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
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

    private static class SingleChange<T>
    extends ExpressionHelper<T> {
        private final ChangeListener<? super T> listener;
        private T currentValue;

        private SingleChange(ObservableValue<T> observableValue, ChangeListener<? super T> changeListener) {
            super(observableValue);
            this.listener = changeListener;
            this.currentValue = observableValue.getValue();
        }

        @Override
        protected ExpressionHelper<T> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, invalidationListener, this.listener);
        }

        @Override
        protected ExpressionHelper<T> removeListener(InvalidationListener invalidationListener) {
            return this;
        }

        @Override
        protected ExpressionHelper<T> addListener(ChangeListener<? super T> changeListener) {
            return new Generic(this.observable, this.listener, changeListener);
        }

        @Override
        protected ExpressionHelper<T> removeListener(ChangeListener<? super T> changeListener) {
            return changeListener.equals(this.listener) ? null : this;
        }

        @Override
        protected void fireValueChangedEvent() {
            boolean bl;
            T t2 = this.currentValue;
            this.currentValue = this.observable.getValue();
            boolean bl2 = this.currentValue == null ? t2 != null : (bl = !this.currentValue.equals(t2));
            if (bl) {
                try {
                    this.listener.changed(this.observable, t2, this.currentValue);
                }
                catch (Exception exception) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
                }
            }
        }
    }

    private static class SingleInvalidation<T>
    extends ExpressionHelper<T> {
        private final InvalidationListener listener;

        private SingleInvalidation(ObservableValue<T> observableValue, InvalidationListener invalidationListener) {
            super(observableValue);
            this.listener = invalidationListener;
        }

        @Override
        protected ExpressionHelper<T> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, this.listener, invalidationListener);
        }

        @Override
        protected ExpressionHelper<T> removeListener(InvalidationListener invalidationListener) {
            return invalidationListener.equals(this.listener) ? null : this;
        }

        @Override
        protected ExpressionHelper<T> addListener(ChangeListener<? super T> changeListener) {
            return new Generic(this.observable, this.listener, changeListener);
        }

        @Override
        protected ExpressionHelper<T> removeListener(ChangeListener<? super T> changeListener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            try {
                this.listener.invalidated(this.observable);
            }
            catch (Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }
    }
}

