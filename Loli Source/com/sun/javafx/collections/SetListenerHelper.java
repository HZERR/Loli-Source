/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import com.sun.javafx.binding.ExpressionHelperBase;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.collections.SetChangeListener;

public abstract class SetListenerHelper<E>
extends ExpressionHelperBase {
    public static <E> SetListenerHelper<E> addListener(SetListenerHelper<E> setListenerHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return setListenerHelper == null ? new SingleInvalidation(invalidationListener) : setListenerHelper.addListener(invalidationListener);
    }

    public static <E> SetListenerHelper<E> removeListener(SetListenerHelper<E> setListenerHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return setListenerHelper == null ? null : setListenerHelper.removeListener(invalidationListener);
    }

    public static <E> SetListenerHelper<E> addListener(SetListenerHelper<E> setListenerHelper, SetChangeListener<? super E> setChangeListener) {
        if (setChangeListener == null) {
            throw new NullPointerException();
        }
        return setListenerHelper == null ? new SingleChange(setChangeListener) : setListenerHelper.addListener(setChangeListener);
    }

    public static <E> SetListenerHelper<E> removeListener(SetListenerHelper<E> setListenerHelper, SetChangeListener<? super E> setChangeListener) {
        if (setChangeListener == null) {
            throw new NullPointerException();
        }
        return setListenerHelper == null ? null : setListenerHelper.removeListener(setChangeListener);
    }

    public static <E> void fireValueChangedEvent(SetListenerHelper<E> setListenerHelper, SetChangeListener.Change<? extends E> change) {
        if (setListenerHelper != null) {
            setListenerHelper.fireValueChangedEvent(change);
        }
    }

    public static <E> boolean hasListeners(SetListenerHelper<E> setListenerHelper) {
        return setListenerHelper != null;
    }

    protected abstract SetListenerHelper<E> addListener(InvalidationListener var1);

    protected abstract SetListenerHelper<E> removeListener(InvalidationListener var1);

    protected abstract SetListenerHelper<E> addListener(SetChangeListener<? super E> var1);

    protected abstract SetListenerHelper<E> removeListener(SetChangeListener<? super E> var1);

    protected abstract void fireValueChangedEvent(SetChangeListener.Change<? extends E> var1);

    private static class Generic<E>
    extends SetListenerHelper<E> {
        private InvalidationListener[] invalidationListeners;
        private SetChangeListener<? super E>[] changeListeners;
        private int invalidationSize;
        private int changeSize;
        private boolean locked;

        private Generic(InvalidationListener invalidationListener, InvalidationListener invalidationListener2) {
            this.invalidationListeners = new InvalidationListener[]{invalidationListener, invalidationListener2};
            this.invalidationSize = 2;
        }

        private Generic(SetChangeListener<? super E> setChangeListener, SetChangeListener<? super E> setChangeListener2) {
            this.changeListeners = new SetChangeListener[]{setChangeListener, setChangeListener2};
            this.changeSize = 2;
        }

        private Generic(InvalidationListener invalidationListener, SetChangeListener<? super E> setChangeListener) {
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new SetChangeListener[]{setChangeListener};
            this.changeSize = 1;
        }

        @Override
        protected Generic<E> addListener(InvalidationListener invalidationListener) {
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
        protected SetListenerHelper<E> removeListener(InvalidationListener invalidationListener) {
            if (this.invalidationListeners != null) {
                for (int i2 = 0; i2 < this.invalidationSize; ++i2) {
                    if (!invalidationListener.equals(this.invalidationListeners[i2])) continue;
                    if (this.invalidationSize == 1) {
                        if (this.changeSize == 1) {
                            return new SingleChange(this.changeListeners[0]);
                        }
                        this.invalidationListeners = null;
                        this.invalidationSize = 0;
                        break;
                    }
                    if (this.invalidationSize == 2 && this.changeSize == 0) {
                        return new SingleInvalidation(this.invalidationListeners[1 - i2]);
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
        protected SetListenerHelper<E> addListener(SetChangeListener<? super E> setChangeListener) {
            if (this.changeListeners == null) {
                this.changeListeners = new SetChangeListener[]{setChangeListener};
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
                this.changeListeners[this.changeSize++] = setChangeListener;
            }
            return this;
        }

        @Override
        protected SetListenerHelper<E> removeListener(SetChangeListener<? super E> setChangeListener) {
            if (this.changeListeners != null) {
                for (int i2 = 0; i2 < this.changeSize; ++i2) {
                    if (!setChangeListener.equals(this.changeListeners[i2])) continue;
                    if (this.changeSize == 1) {
                        if (this.invalidationSize == 1) {
                            return new SingleInvalidation(this.invalidationListeners[0]);
                        }
                        this.changeListeners = null;
                        this.changeSize = 0;
                        break;
                    }
                    if (this.changeSize == 2 && this.invalidationSize == 0) {
                        return new SingleChange(this.changeListeners[1 - i2]);
                    }
                    int n2 = this.changeSize - i2 - 1;
                    SetChangeListener<? super E>[] arrsetChangeListener = this.changeListeners;
                    if (this.locked) {
                        this.changeListeners = new SetChangeListener[this.changeListeners.length];
                        System.arraycopy(arrsetChangeListener, 0, this.changeListeners, 0, i2);
                    }
                    if (n2 > 0) {
                        System.arraycopy(arrsetChangeListener, i2 + 1, this.changeListeners, i2, n2);
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
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
            int n2 = this.invalidationSize;
            SetChangeListener<? super E>[] arrsetChangeListener = this.changeListeners;
            int n3 = this.changeSize;
            try {
                int n4;
                this.locked = true;
                for (n4 = 0; n4 < n2; ++n4) {
                    try {
                        arrinvalidationListener[n4].invalidated(change.getSet());
                        continue;
                    }
                    catch (Exception exception) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
                    }
                }
                for (n4 = 0; n4 < n3; ++n4) {
                    try {
                        arrsetChangeListener[n4].onChanged(change);
                        continue;
                    }
                    catch (Exception exception) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
                    }
                }
            }
            finally {
                this.locked = false;
            }
        }
    }

    private static class SingleChange<E>
    extends SetListenerHelper<E> {
        private final SetChangeListener<? super E> listener;

        private SingleChange(SetChangeListener<? super E> setChangeListener) {
            this.listener = setChangeListener;
        }

        @Override
        protected SetListenerHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(invalidationListener, this.listener);
        }

        @Override
        protected SetListenerHelper<E> removeListener(InvalidationListener invalidationListener) {
            return this;
        }

        @Override
        protected SetListenerHelper<E> addListener(SetChangeListener<? super E> setChangeListener) {
            return new Generic(this.listener, setChangeListener);
        }

        @Override
        protected SetListenerHelper<E> removeListener(SetChangeListener<? super E> setChangeListener) {
            return setChangeListener.equals(this.listener) ? null : this;
        }

        @Override
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            try {
                this.listener.onChanged(change);
            }
            catch (Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }
    }

    private static class SingleInvalidation<E>
    extends SetListenerHelper<E> {
        private final InvalidationListener listener;

        private SingleInvalidation(InvalidationListener invalidationListener) {
            this.listener = invalidationListener;
        }

        @Override
        protected SetListenerHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.listener, invalidationListener);
        }

        @Override
        protected SetListenerHelper<E> removeListener(InvalidationListener invalidationListener) {
            return invalidationListener.equals(this.listener) ? null : this;
        }

        @Override
        protected SetListenerHelper<E> addListener(SetChangeListener<? super E> setChangeListener) {
            return new Generic(this.listener, setChangeListener);
        }

        @Override
        protected SetListenerHelper<E> removeListener(SetChangeListener<? super E> setChangeListener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            try {
                this.listener.invalidated(change.getSet());
            }
            catch (Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }
    }
}

