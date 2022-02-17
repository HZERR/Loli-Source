/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import com.sun.javafx.binding.ExpressionHelperBase;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;

public abstract class ListListenerHelper<E>
extends ExpressionHelperBase {
    public static <E> ListListenerHelper<E> addListener(ListListenerHelper<E> listListenerHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return listListenerHelper == null ? new SingleInvalidation(invalidationListener) : listListenerHelper.addListener(invalidationListener);
    }

    public static <E> ListListenerHelper<E> removeListener(ListListenerHelper<E> listListenerHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return listListenerHelper == null ? null : listListenerHelper.removeListener(invalidationListener);
    }

    public static <E> ListListenerHelper<E> addListener(ListListenerHelper<E> listListenerHelper, ListChangeListener<? super E> listChangeListener) {
        if (listChangeListener == null) {
            throw new NullPointerException();
        }
        return listListenerHelper == null ? new SingleChange(listChangeListener) : listListenerHelper.addListener(listChangeListener);
    }

    public static <E> ListListenerHelper<E> removeListener(ListListenerHelper<E> listListenerHelper, ListChangeListener<? super E> listChangeListener) {
        if (listChangeListener == null) {
            throw new NullPointerException();
        }
        return listListenerHelper == null ? null : listListenerHelper.removeListener(listChangeListener);
    }

    public static <E> void fireValueChangedEvent(ListListenerHelper<E> listListenerHelper, ListChangeListener.Change<? extends E> change) {
        if (listListenerHelper != null) {
            change.reset();
            listListenerHelper.fireValueChangedEvent(change);
        }
    }

    public static <E> boolean hasListeners(ListListenerHelper<E> listListenerHelper) {
        return listListenerHelper != null;
    }

    protected abstract ListListenerHelper<E> addListener(InvalidationListener var1);

    protected abstract ListListenerHelper<E> removeListener(InvalidationListener var1);

    protected abstract ListListenerHelper<E> addListener(ListChangeListener<? super E> var1);

    protected abstract ListListenerHelper<E> removeListener(ListChangeListener<? super E> var1);

    protected abstract void fireValueChangedEvent(ListChangeListener.Change<? extends E> var1);

    private static class Generic<E>
    extends ListListenerHelper<E> {
        private InvalidationListener[] invalidationListeners;
        private ListChangeListener<? super E>[] changeListeners;
        private int invalidationSize;
        private int changeSize;
        private boolean locked;

        private Generic(InvalidationListener invalidationListener, InvalidationListener invalidationListener2) {
            this.invalidationListeners = new InvalidationListener[]{invalidationListener, invalidationListener2};
            this.invalidationSize = 2;
        }

        private Generic(ListChangeListener<? super E> listChangeListener, ListChangeListener<? super E> listChangeListener2) {
            this.changeListeners = new ListChangeListener[]{listChangeListener, listChangeListener2};
            this.changeSize = 2;
        }

        private Generic(InvalidationListener invalidationListener, ListChangeListener<? super E> listChangeListener) {
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ListChangeListener[]{listChangeListener};
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
        protected ListListenerHelper<E> removeListener(InvalidationListener invalidationListener) {
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
        protected ListListenerHelper<E> addListener(ListChangeListener<? super E> listChangeListener) {
            if (this.changeListeners == null) {
                this.changeListeners = new ListChangeListener[]{listChangeListener};
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
                this.changeListeners[this.changeSize++] = listChangeListener;
            }
            return this;
        }

        @Override
        protected ListListenerHelper<E> removeListener(ListChangeListener<? super E> listChangeListener) {
            if (this.changeListeners != null) {
                for (int i2 = 0; i2 < this.changeSize; ++i2) {
                    if (!listChangeListener.equals(this.changeListeners[i2])) continue;
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
                    ListChangeListener<? super E>[] arrlistChangeListener = this.changeListeners;
                    if (this.locked) {
                        this.changeListeners = new ListChangeListener[this.changeListeners.length];
                        System.arraycopy(arrlistChangeListener, 0, this.changeListeners, 0, i2);
                    }
                    if (n2 > 0) {
                        System.arraycopy(arrlistChangeListener, i2 + 1, this.changeListeners, i2, n2);
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
        protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
            InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
            int n2 = this.invalidationSize;
            ListChangeListener<? super E>[] arrlistChangeListener = this.changeListeners;
            int n3 = this.changeSize;
            try {
                int n4;
                this.locked = true;
                for (n4 = 0; n4 < n2; ++n4) {
                    try {
                        arrinvalidationListener[n4].invalidated(change.getList());
                        continue;
                    }
                    catch (Exception exception) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
                    }
                }
                for (n4 = 0; n4 < n3; ++n4) {
                    change.reset();
                    try {
                        arrlistChangeListener[n4].onChanged(change);
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
    extends ListListenerHelper<E> {
        private final ListChangeListener<? super E> listener;

        private SingleChange(ListChangeListener<? super E> listChangeListener) {
            this.listener = listChangeListener;
        }

        @Override
        protected ListListenerHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(invalidationListener, this.listener);
        }

        @Override
        protected ListListenerHelper<E> removeListener(InvalidationListener invalidationListener) {
            return this;
        }

        @Override
        protected ListListenerHelper<E> addListener(ListChangeListener<? super E> listChangeListener) {
            return new Generic(this.listener, listChangeListener);
        }

        @Override
        protected ListListenerHelper<E> removeListener(ListChangeListener<? super E> listChangeListener) {
            return listChangeListener.equals(this.listener) ? null : this;
        }

        @Override
        protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
            try {
                this.listener.onChanged(change);
            }
            catch (Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }
    }

    private static class SingleInvalidation<E>
    extends ListListenerHelper<E> {
        private final InvalidationListener listener;

        private SingleInvalidation(InvalidationListener invalidationListener) {
            this.listener = invalidationListener;
        }

        @Override
        protected ListListenerHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.listener, invalidationListener);
        }

        @Override
        protected ListListenerHelper<E> removeListener(InvalidationListener invalidationListener) {
            return invalidationListener.equals(this.listener) ? null : this;
        }

        @Override
        protected ListListenerHelper<E> addListener(ListChangeListener<? super E> listChangeListener) {
            return new Generic(this.listener, listChangeListener);
        }

        @Override
        protected ListListenerHelper<E> removeListener(ListChangeListener<? super E> listChangeListener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
            try {
                this.listener.invalidated(change.getList());
            }
            catch (Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }
    }
}

