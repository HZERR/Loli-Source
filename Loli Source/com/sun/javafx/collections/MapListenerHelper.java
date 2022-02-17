/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import com.sun.javafx.binding.ExpressionHelperBase;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;

public abstract class MapListenerHelper<K, V>
extends ExpressionHelperBase {
    public static <K, V> MapListenerHelper<K, V> addListener(MapListenerHelper<K, V> mapListenerHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return mapListenerHelper == null ? new SingleInvalidation(invalidationListener) : mapListenerHelper.addListener(invalidationListener);
    }

    public static <K, V> MapListenerHelper<K, V> removeListener(MapListenerHelper<K, V> mapListenerHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return mapListenerHelper == null ? null : mapListenerHelper.removeListener(invalidationListener);
    }

    public static <K, V> MapListenerHelper<K, V> addListener(MapListenerHelper<K, V> mapListenerHelper, MapChangeListener<? super K, ? super V> mapChangeListener) {
        if (mapChangeListener == null) {
            throw new NullPointerException();
        }
        return mapListenerHelper == null ? new SingleChange(mapChangeListener) : mapListenerHelper.addListener(mapChangeListener);
    }

    public static <K, V> MapListenerHelper<K, V> removeListener(MapListenerHelper<K, V> mapListenerHelper, MapChangeListener<? super K, ? super V> mapChangeListener) {
        if (mapChangeListener == null) {
            throw new NullPointerException();
        }
        return mapListenerHelper == null ? null : mapListenerHelper.removeListener(mapChangeListener);
    }

    public static <K, V> void fireValueChangedEvent(MapListenerHelper<K, V> mapListenerHelper, MapChangeListener.Change<? extends K, ? extends V> change) {
        if (mapListenerHelper != null) {
            mapListenerHelper.fireValueChangedEvent(change);
        }
    }

    public static <K, V> boolean hasListeners(MapListenerHelper<K, V> mapListenerHelper) {
        return mapListenerHelper != null;
    }

    protected abstract MapListenerHelper<K, V> addListener(InvalidationListener var1);

    protected abstract MapListenerHelper<K, V> removeListener(InvalidationListener var1);

    protected abstract MapListenerHelper<K, V> addListener(MapChangeListener<? super K, ? super V> var1);

    protected abstract MapListenerHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> var1);

    protected abstract void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> var1);

    private static class Generic<K, V>
    extends MapListenerHelper<K, V> {
        private InvalidationListener[] invalidationListeners;
        private MapChangeListener<? super K, ? super V>[] changeListeners;
        private int invalidationSize;
        private int changeSize;
        private boolean locked;

        private Generic(InvalidationListener invalidationListener, InvalidationListener invalidationListener2) {
            this.invalidationListeners = new InvalidationListener[]{invalidationListener, invalidationListener2};
            this.invalidationSize = 2;
        }

        private Generic(MapChangeListener<? super K, ? super V> mapChangeListener, MapChangeListener<? super K, ? super V> mapChangeListener2) {
            this.changeListeners = new MapChangeListener[]{mapChangeListener, mapChangeListener2};
            this.changeSize = 2;
        }

        private Generic(InvalidationListener invalidationListener, MapChangeListener<? super K, ? super V> mapChangeListener) {
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new MapChangeListener[]{mapChangeListener};
            this.changeSize = 1;
        }

        @Override
        protected Generic<K, V> addListener(InvalidationListener invalidationListener) {
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
        protected MapListenerHelper<K, V> removeListener(InvalidationListener invalidationListener) {
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
        protected MapListenerHelper<K, V> addListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            if (this.changeListeners == null) {
                this.changeListeners = new MapChangeListener[]{mapChangeListener};
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
                this.changeListeners[this.changeSize++] = mapChangeListener;
            }
            return this;
        }

        @Override
        protected MapListenerHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            if (this.changeListeners != null) {
                for (int i2 = 0; i2 < this.changeSize; ++i2) {
                    if (!mapChangeListener.equals(this.changeListeners[i2])) continue;
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
                    MapChangeListener<? super K, ? super V>[] arrmapChangeListener = this.changeListeners;
                    if (this.locked) {
                        this.changeListeners = new MapChangeListener[this.changeListeners.length];
                        System.arraycopy(arrmapChangeListener, 0, this.changeListeners, 0, i2);
                    }
                    if (n2 > 0) {
                        System.arraycopy(arrmapChangeListener, i2 + 1, this.changeListeners, i2, n2);
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
        protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
            InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
            int n2 = this.invalidationSize;
            MapChangeListener<? super K, ? super V>[] arrmapChangeListener = this.changeListeners;
            int n3 = this.changeSize;
            try {
                int n4;
                this.locked = true;
                for (n4 = 0; n4 < n2; ++n4) {
                    try {
                        arrinvalidationListener[n4].invalidated(change.getMap());
                        continue;
                    }
                    catch (Exception exception) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
                    }
                }
                for (n4 = 0; n4 < n3; ++n4) {
                    try {
                        arrmapChangeListener[n4].onChanged(change);
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

    private static class SingleChange<K, V>
    extends MapListenerHelper<K, V> {
        private final MapChangeListener<? super K, ? super V> listener;

        private SingleChange(MapChangeListener<? super K, ? super V> mapChangeListener) {
            this.listener = mapChangeListener;
        }

        @Override
        protected MapListenerHelper<K, V> addListener(InvalidationListener invalidationListener) {
            return new Generic(invalidationListener, this.listener);
        }

        @Override
        protected MapListenerHelper<K, V> removeListener(InvalidationListener invalidationListener) {
            return this;
        }

        @Override
        protected MapListenerHelper<K, V> addListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            return new Generic(this.listener, mapChangeListener);
        }

        @Override
        protected MapListenerHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            return mapChangeListener.equals(this.listener) ? null : this;
        }

        @Override
        protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
            try {
                this.listener.onChanged(change);
            }
            catch (Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }
    }

    private static class SingleInvalidation<K, V>
    extends MapListenerHelper<K, V> {
        private final InvalidationListener listener;

        private SingleInvalidation(InvalidationListener invalidationListener) {
            this.listener = invalidationListener;
        }

        @Override
        protected MapListenerHelper<K, V> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.listener, invalidationListener);
        }

        @Override
        protected MapListenerHelper<K, V> removeListener(InvalidationListener invalidationListener) {
            return invalidationListener.equals(this.listener) ? null : this;
        }

        @Override
        protected MapListenerHelper<K, V> addListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            return new Generic(this.listener, mapChangeListener);
        }

        @Override
        protected MapListenerHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
            try {
                this.listener.invalidated(change.getMap());
            }
            catch (Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }
    }
}

