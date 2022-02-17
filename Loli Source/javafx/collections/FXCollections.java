/*
 * Decompiled with CFR 0.150.
 */
package javafx.collections;

import com.sun.javafx.collections.ListListenerHelper;
import com.sun.javafx.collections.MapAdapterChange;
import com.sun.javafx.collections.MapListenerHelper;
import com.sun.javafx.collections.ObservableFloatArrayImpl;
import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.collections.ObservableSequentialListWrapper;
import com.sun.javafx.collections.ObservableSetWrapper;
import com.sun.javafx.collections.SetAdapterChange;
import com.sun.javafx.collections.SetListenerHelper;
import com.sun.javafx.collections.SortableList;
import com.sun.javafx.collections.SourceAdapterChange;
import com.sun.javafx.collections.UnmodifiableObservableMap;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.collections.WeakMapChangeListener;
import javafx.collections.WeakSetChangeListener;
import javafx.util.Callback;

public class FXCollections {
    private static ObservableMap EMPTY_OBSERVABLE_MAP = new EmptyObservableMap();
    private static ObservableList EMPTY_OBSERVABLE_LIST = new EmptyObservableList();
    private static ObservableSet EMPTY_OBSERVABLE_SET = new EmptyObservableSet();
    private static Random r;

    private FXCollections() {
    }

    public static <E> ObservableList<E> observableList(List<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        return list instanceof RandomAccess ? new ObservableListWrapper<E>(list) : new ObservableSequentialListWrapper<E>(list);
    }

    public static <E> ObservableList<E> observableList(List<E> list, Callback<E, Observable[]> callback) {
        if (list == null || callback == null) {
            throw new NullPointerException();
        }
        return list instanceof RandomAccess ? new ObservableListWrapper<E>(list, callback) : new ObservableSequentialListWrapper<E>(list, callback);
    }

    public static <K, V> ObservableMap<K, V> observableMap(Map<K, V> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        return new ObservableMapWrapper<K, V>(map);
    }

    public static <E> ObservableSet<E> observableSet(Set<E> set) {
        if (set == null) {
            throw new NullPointerException();
        }
        return new ObservableSetWrapper<E>(set);
    }

    public static <E> ObservableSet<E> observableSet(E ... arrE) {
        if (arrE == null) {
            throw new NullPointerException();
        }
        HashSet hashSet = new HashSet(arrE.length);
        Collections.addAll(hashSet, arrE);
        return new ObservableSetWrapper(hashSet);
    }

    public static <K, V> ObservableMap<K, V> unmodifiableObservableMap(ObservableMap<K, V> observableMap) {
        if (observableMap == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableObservableMap<K, V>(observableMap);
    }

    public static <K, V> ObservableMap<K, V> checkedObservableMap(ObservableMap<K, V> observableMap, Class<K> class_, Class<V> class_2) {
        if (observableMap == null || class_ == null || class_2 == null) {
            throw new NullPointerException();
        }
        return new CheckedObservableMap<K, V>(observableMap, class_, class_2);
    }

    public static <K, V> ObservableMap<K, V> synchronizedObservableMap(ObservableMap<K, V> observableMap) {
        if (observableMap == null) {
            throw new NullPointerException();
        }
        return new SynchronizedObservableMap<K, V>(observableMap);
    }

    public static <K, V> ObservableMap<K, V> emptyObservableMap() {
        return EMPTY_OBSERVABLE_MAP;
    }

    public static ObservableIntegerArray observableIntegerArray() {
        return new ObservableIntegerArrayImpl();
    }

    public static ObservableIntegerArray observableIntegerArray(int ... arrn) {
        return new ObservableIntegerArrayImpl(arrn);
    }

    public static ObservableIntegerArray observableIntegerArray(ObservableIntegerArray observableIntegerArray) {
        return new ObservableIntegerArrayImpl(observableIntegerArray);
    }

    public static ObservableFloatArray observableFloatArray() {
        return new ObservableFloatArrayImpl();
    }

    public static ObservableFloatArray observableFloatArray(float ... arrf) {
        return new ObservableFloatArrayImpl(arrf);
    }

    public static ObservableFloatArray observableFloatArray(ObservableFloatArray observableFloatArray) {
        return new ObservableFloatArrayImpl(observableFloatArray);
    }

    public static <E> ObservableList<E> observableArrayList() {
        return FXCollections.observableList(new ArrayList());
    }

    public static <E> ObservableList<E> observableArrayList(Callback<E, Observable[]> callback) {
        return FXCollections.observableList(new ArrayList(), callback);
    }

    public static <E> ObservableList<E> observableArrayList(E ... arrE) {
        ObservableList<E> observableList = FXCollections.observableArrayList();
        observableList.addAll(arrE);
        return observableList;
    }

    public static <E> ObservableList<E> observableArrayList(Collection<? extends E> collection) {
        ObservableList<? extends E> observableList = FXCollections.observableArrayList();
        observableList.addAll(collection);
        return observableList;
    }

    public static <K, V> ObservableMap<K, V> observableHashMap() {
        return FXCollections.observableMap(new HashMap());
    }

    public static <E> ObservableList<E> concat(ObservableList<E> ... arrobservableList) {
        if (arrobservableList.length == 0) {
            return FXCollections.observableArrayList();
        }
        if (arrobservableList.length == 1) {
            return FXCollections.observableArrayList(arrobservableList[0]);
        }
        ArrayList<E> arrayList = new ArrayList<E>();
        for (ObservableList<E> observableList : arrobservableList) {
            arrayList.addAll(observableList);
        }
        return FXCollections.observableList(arrayList);
    }

    public static <E> ObservableList<E> unmodifiableObservableList(ObservableList<E> observableList) {
        if (observableList == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableObservableListImpl<E>(observableList);
    }

    public static <E> ObservableList<E> checkedObservableList(ObservableList<E> observableList, Class<E> class_) {
        if (observableList == null) {
            throw new NullPointerException();
        }
        return new CheckedObservableList<E>(observableList, class_);
    }

    public static <E> ObservableList<E> synchronizedObservableList(ObservableList<E> observableList) {
        if (observableList == null) {
            throw new NullPointerException();
        }
        return new SynchronizedObservableList<E>(observableList);
    }

    public static <E> ObservableList<E> emptyObservableList() {
        return EMPTY_OBSERVABLE_LIST;
    }

    public static <E> ObservableList<E> singletonObservableList(E e2) {
        return new SingletonObservableList<E>(e2);
    }

    public static <E> ObservableSet<E> unmodifiableObservableSet(ObservableSet<E> observableSet) {
        if (observableSet == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableObservableSet<E>(observableSet);
    }

    public static <E> ObservableSet<E> checkedObservableSet(ObservableSet<E> observableSet, Class<E> class_) {
        if (observableSet == null) {
            throw new NullPointerException();
        }
        return new CheckedObservableSet<E>(observableSet, class_);
    }

    public static <E> ObservableSet<E> synchronizedObservableSet(ObservableSet<E> observableSet) {
        if (observableSet == null) {
            throw new NullPointerException();
        }
        return new SynchronizedObservableSet<E>(observableSet);
    }

    public static <E> ObservableSet<E> emptyObservableSet() {
        return EMPTY_OBSERVABLE_SET;
    }

    public static <T> void copy(ObservableList<? super T> observableList, List<? extends T> list) {
        int n2 = list.size();
        if (n2 > observableList.size()) {
            throw new IndexOutOfBoundsException("Source does not fit in dest");
        }
        Object[] arrobject = observableList.toArray();
        System.arraycopy(list.toArray(), 0, arrobject, 0, n2);
        observableList.setAll(arrobject);
    }

    public static <T> void fill(ObservableList<? super T> observableList, T t2) {
        Object[] arrobject = new Object[observableList.size()];
        Arrays.fill(arrobject, t2);
        observableList.setAll(arrobject);
    }

    public static <T> boolean replaceAll(ObservableList<T> observableList, T t2, T t3) {
        Object[] arrobject = observableList.toArray();
        boolean bl = false;
        for (int i2 = 0; i2 < arrobject.length; ++i2) {
            if (!arrobject[i2].equals(t2)) continue;
            arrobject[i2] = t3;
            bl = true;
        }
        if (bl) {
            observableList.setAll(arrobject);
        }
        return bl;
    }

    public static void reverse(ObservableList observableList) {
        Object[] arrobject = observableList.toArray();
        for (int i2 = 0; i2 < arrobject.length / 2; ++i2) {
            Object object = arrobject[i2];
            arrobject[i2] = arrobject[arrobject.length - i2 - 1];
            arrobject[arrobject.length - i2 - 1] = object;
        }
        observableList.setAll(arrobject);
    }

    public static void rotate(ObservableList observableList, int n2) {
        Object[] arrobject = observableList.toArray();
        int n3 = observableList.size();
        if ((n2 %= n3) < 0) {
            n2 += n3;
        }
        if (n2 == 0) {
            return;
        }
        int n4 = 0;
        int n5 = 0;
        while (n5 != n3) {
            Object object = arrobject[n4];
            int n6 = n4;
            do {
                if ((n6 += n2) >= n3) {
                    n6 -= n3;
                }
                Object object2 = arrobject[n6];
                arrobject[n6] = object;
                object = object2;
                ++n5;
            } while (n6 != n4);
            ++n4;
        }
        observableList.setAll(arrobject);
    }

    public static void shuffle(ObservableList<?> observableList) {
        if (r == null) {
            r = new Random();
        }
        FXCollections.shuffle(observableList, r);
    }

    public static void shuffle(ObservableList observableList, Random random) {
        Object[] arrobject = observableList.toArray();
        for (int i2 = observableList.size(); i2 > 1; --i2) {
            FXCollections.swap(arrobject, i2 - 1, random.nextInt(i2));
        }
        observableList.setAll(arrobject);
    }

    private static void swap(Object[] arrobject, int n2, int n3) {
        Object object = arrobject[n2];
        arrobject[n2] = arrobject[n3];
        arrobject[n3] = object;
    }

    public static <T extends Comparable<? super T>> void sort(ObservableList<T> observableList) {
        if (observableList instanceof SortableList) {
            ((SortableList)((Object)observableList)).sort();
        } else {
            ArrayList<T> arrayList = new ArrayList<T>(observableList);
            Collections.sort(arrayList);
            observableList.setAll((Collection<T>)arrayList);
        }
    }

    public static <T> void sort(ObservableList<T> observableList, Comparator<? super T> comparator) {
        if (observableList instanceof SortableList) {
            ((SortableList)((Object)observableList)).sort(comparator);
        } else {
            ArrayList<T> arrayList = new ArrayList<T>(observableList);
            Collections.sort(arrayList, comparator);
            observableList.setAll((Collection<T>)arrayList);
        }
    }

    private static class SynchronizedObservableMap<K, V>
    extends SynchronizedMap<K, V>
    implements ObservableMap<K, V> {
        private final ObservableMap<K, V> backingMap;
        private MapListenerHelper listenerHelper;
        private final MapChangeListener<K, V> listener;

        SynchronizedObservableMap(ObservableMap<K, V> observableMap, Object object) {
            super(observableMap, object);
            this.backingMap = observableMap;
            this.listener = change -> MapListenerHelper.fireValueChangedEvent(this.listenerHelper, new MapAdapterChange(this, change));
            this.backingMap.addListener(new WeakMapChangeListener<K, V>(this.listener));
        }

        SynchronizedObservableMap(ObservableMap<K, V> observableMap) {
            this(observableMap, new Object());
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void addListener(InvalidationListener invalidationListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, invalidationListener);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, invalidationListener);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void addListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, mapChangeListener);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void removeListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, mapChangeListener);
            }
        }
    }

    private static class SynchronizedCollection<E>
    implements Collection<E> {
        private final Collection<E> backingCollection;
        final Object mutex;

        SynchronizedCollection(Collection<E> collection, Object object) {
            this.backingCollection = collection;
            this.mutex = object;
        }

        SynchronizedCollection(Collection<E> collection) {
            this(collection, new Object());
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingCollection.size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isEmpty() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingCollection.isEmpty();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean contains(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingCollection.contains(object);
            }
        }

        @Override
        public Iterator<E> iterator() {
            return this.backingCollection.iterator();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Object[] toArray() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingCollection.toArray();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public <T> T[] toArray(T[] arrT) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingCollection.toArray(arrT);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean add(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingCollection.add(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean remove(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingCollection.remove(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsAll(Collection<?> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingCollection.containsAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean addAll(Collection<? extends E> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingCollection.addAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean removeAll(Collection<?> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingCollection.removeAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean retainAll(Collection<?> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingCollection.retainAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.mutex;
            synchronized (object) {
                this.backingCollection.clear();
            }
        }
    }

    private static class SynchronizedMap<K, V>
    implements Map<K, V> {
        final Object mutex;
        private final Map<K, V> backingMap;
        private transient Set<K> keySet = null;
        private transient Set<Map.Entry<K, V>> entrySet = null;
        private transient Collection<V> values = null;

        SynchronizedMap(Map<K, V> map, Object object) {
            this.backingMap = map;
            this.mutex = object;
        }

        SynchronizedMap(Map<K, V> map) {
            this(map, new Object());
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingMap.size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isEmpty() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingMap.isEmpty();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsKey(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingMap.containsKey(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsValue(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingMap.containsValue(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V get(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingMap.get(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V put(K k2, V v2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingMap.put(k2, v2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V remove(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingMap.remove(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void putAll(Map<? extends K, ? extends V> map) {
            Object object = this.mutex;
            synchronized (object) {
                this.backingMap.putAll(map);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.mutex;
            synchronized (object) {
                this.backingMap.clear();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<K> keySet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.keySet == null) {
                    this.keySet = new SynchronizedSet<K>(this.backingMap.keySet(), this.mutex);
                }
                return this.keySet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Collection<V> values() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.values == null) {
                    this.values = new SynchronizedCollection<V>(this.backingMap.values(), this.mutex);
                }
                return this.values;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.entrySet == null) {
                    this.entrySet = new SynchronizedSet<Map.Entry<K, V>>(this.backingMap.entrySet(), this.mutex);
                }
                return this.entrySet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingMap.equals(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingMap.hashCode();
            }
        }
    }

    private static class CheckedObservableMap<K, V>
    extends AbstractMap<K, V>
    implements ObservableMap<K, V> {
        private final ObservableMap<K, V> backingMap;
        private final Class<K> keyType;
        private final Class<V> valueType;
        private MapListenerHelper listenerHelper;
        private final MapChangeListener<K, V> listener;
        private transient Set<Map.Entry<K, V>> entrySet = null;

        CheckedObservableMap(ObservableMap<K, V> observableMap, Class<K> class_, Class<V> class_2) {
            this.backingMap = observableMap;
            this.keyType = class_;
            this.valueType = class_2;
            this.listener = change -> this.callObservers(new MapAdapterChange(this, change));
            this.backingMap.addListener(new WeakMapChangeListener<K, V>(this.listener));
        }

        private void callObservers(MapChangeListener.Change<? extends K, ? extends V> change) {
            MapListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
        }

        void typeCheck(Object object, Object object2) {
            if (object != null && !this.keyType.isInstance(object)) {
                throw new ClassCastException("Attempt to insert " + object.getClass() + " key into map with key type " + this.keyType);
            }
            if (object2 != null && !this.valueType.isInstance(object2)) {
                throw new ClassCastException("Attempt to insert " + object2.getClass() + " value into map with value type " + this.valueType);
            }
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {
            this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, invalidationListener);
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, invalidationListener);
        }

        @Override
        public void addListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, mapChangeListener);
        }

        @Override
        public void removeListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, mapChangeListener);
        }

        @Override
        public int size() {
            return this.backingMap.size();
        }

        @Override
        public boolean isEmpty() {
            return this.backingMap.isEmpty();
        }

        @Override
        public boolean containsKey(Object object) {
            return this.backingMap.containsKey(object);
        }

        @Override
        public boolean containsValue(Object object) {
            return this.backingMap.containsValue(object);
        }

        @Override
        public V get(Object object) {
            return this.backingMap.get(object);
        }

        @Override
        public V put(K k2, V v2) {
            this.typeCheck(k2, v2);
            return this.backingMap.put(k2, v2);
        }

        @Override
        public V remove(Object object) {
            return this.backingMap.remove(object);
        }

        @Override
        public void putAll(Map map) {
            Object[] arrobject = map.entrySet().toArray();
            ArrayList arrayList = new ArrayList(arrobject.length);
            for (Object object : arrobject) {
                Map.Entry entry = (Map.Entry)object;
                Object k2 = entry.getKey();
                Object v2 = entry.getValue();
                this.typeCheck(k2, v2);
                arrayList.add(new AbstractMap.SimpleImmutableEntry(k2, v2));
            }
            for (Map.Entry entry : arrayList) {
                this.backingMap.put(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public void clear() {
            this.backingMap.clear();
        }

        @Override
        public Set<K> keySet() {
            return this.backingMap.keySet();
        }

        @Override
        public Collection<V> values() {
            return this.backingMap.values();
        }

        @Override
        public Set entrySet() {
            if (this.entrySet == null) {
                this.entrySet = new CheckedEntrySet(this.backingMap.entrySet(), this.valueType);
            }
            return this.entrySet;
        }

        @Override
        public boolean equals(Object object) {
            return object == this || this.backingMap.equals(object);
        }

        @Override
        public int hashCode() {
            return this.backingMap.hashCode();
        }

        static class CheckedEntrySet<K, V>
        implements Set<Map.Entry<K, V>> {
            private final Set<Map.Entry<K, V>> s;
            private final Class<V> valueType;

            CheckedEntrySet(Set<Map.Entry<K, V>> set, Class<V> class_) {
                this.s = set;
                this.valueType = class_;
            }

            @Override
            public int size() {
                return this.s.size();
            }

            @Override
            public boolean isEmpty() {
                return this.s.isEmpty();
            }

            public String toString() {
                return this.s.toString();
            }

            @Override
            public int hashCode() {
                return this.s.hashCode();
            }

            @Override
            public void clear() {
                this.s.clear();
            }

            @Override
            public boolean add(Map.Entry<K, V> entry) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                final Iterator<Map.Entry<K, V>> iterator = this.s.iterator();
                final Class<V> class_ = this.valueType;
                return new Iterator<Map.Entry<K, V>>(){

                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public void remove() {
                        iterator.remove();
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        return CheckedEntrySet.checkedEntry((Map.Entry)iterator.next(), class_);
                    }
                };
            }

            @Override
            public Object[] toArray() {
                Object[] arrobject = this.s.toArray();
                Object[] arrobject2 = CheckedEntry.class.isInstance(arrobject.getClass().getComponentType()) ? arrobject : new Object[arrobject.length];
                for (int i2 = 0; i2 < arrobject.length; ++i2) {
                    arrobject2[i2] = CheckedEntrySet.checkedEntry((Map.Entry)arrobject[i2], this.valueType);
                }
                return arrobject2;
            }

            @Override
            public <T> T[] toArray(T[] arrT) {
                T[] arrT2 = this.s.toArray(arrT.length == 0 ? arrT : Arrays.copyOf(arrT, 0));
                for (int i2 = 0; i2 < arrT2.length; ++i2) {
                    arrT2[i2] = CheckedEntrySet.checkedEntry((Map.Entry)arrT2[i2], this.valueType);
                }
                if (arrT2.length > arrT.length) {
                    return arrT2;
                }
                System.arraycopy(arrT2, 0, arrT, 0, arrT2.length);
                if (arrT.length > arrT2.length) {
                    arrT[arrT2.length] = null;
                }
                return arrT;
            }

            @Override
            public boolean contains(Object object) {
                if (!(object instanceof Map.Entry)) {
                    return false;
                }
                CheckedEntry<K, V, V> checkedEntry = (CheckedEntry<K, V, V>)object;
                return this.s.contains(checkedEntry instanceof CheckedEntry ? checkedEntry : CheckedEntrySet.checkedEntry(checkedEntry, this.valueType));
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                for (Object obj : collection) {
                    if (this.contains(obj)) continue;
                    return false;
                }
                return true;
            }

            @Override
            public boolean remove(Object object) {
                if (!(object instanceof Map.Entry)) {
                    return false;
                }
                return this.s.remove(new AbstractMap.SimpleImmutableEntry((Map.Entry)object));
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return this.batchRemove(collection, false);
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return this.batchRemove(collection, true);
            }

            private boolean batchRemove(Collection<?> collection, boolean bl) {
                boolean bl2 = false;
                Iterator<Map.Entry<K, V>> iterator = this.iterator();
                while (iterator.hasNext()) {
                    if (collection.contains(iterator.next()) == bl) continue;
                    iterator.remove();
                    bl2 = true;
                }
                return bl2;
            }

            @Override
            public boolean equals(Object object) {
                if (object == this) {
                    return true;
                }
                if (!(object instanceof Set)) {
                    return false;
                }
                Set set = (Set)object;
                return set.size() == this.s.size() && this.containsAll(set);
            }

            static <K, V, T> CheckedEntry<K, V, T> checkedEntry(Map.Entry<K, V> entry, Class<T> class_) {
                return new CheckedEntry<K, V, T>(entry, class_);
            }

            private static class CheckedEntry<K, V, T>
            implements Map.Entry<K, V> {
                private final Map.Entry<K, V> e;
                private final Class<T> valueType;

                CheckedEntry(Map.Entry<K, V> entry, Class<T> class_) {
                    this.e = entry;
                    this.valueType = class_;
                }

                @Override
                public K getKey() {
                    return this.e.getKey();
                }

                @Override
                public V getValue() {
                    return this.e.getValue();
                }

                @Override
                public int hashCode() {
                    return this.e.hashCode();
                }

                public String toString() {
                    return this.e.toString();
                }

                @Override
                public V setValue(V v2) {
                    if (v2 != null && !this.valueType.isInstance(v2)) {
                        throw new ClassCastException(this.badValueMsg(v2));
                    }
                    return this.e.setValue(v2);
                }

                private String badValueMsg(Object object) {
                    return "Attempt to insert " + object.getClass() + " value into map with value type " + this.valueType;
                }

                @Override
                public boolean equals(Object object) {
                    if (object == this) {
                        return true;
                    }
                    if (!(object instanceof Map.Entry)) {
                        return false;
                    }
                    return this.e.equals(new AbstractMap.SimpleImmutableEntry((Map.Entry)object));
                }
            }
        }
    }

    private static class EmptyObservableMap<K, V>
    extends AbstractMap<K, V>
    implements ObservableMap<K, V> {
        @Override
        public void addListener(InvalidationListener invalidationListener) {
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
        }

        @Override
        public void addListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
        }

        @Override
        public void removeListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean containsKey(Object object) {
            return false;
        }

        @Override
        public boolean containsValue(Object object) {
            return false;
        }

        @Override
        public V get(Object object) {
            return null;
        }

        @Override
        public Set<K> keySet() {
            return FXCollections.emptyObservableSet();
        }

        @Override
        public Collection<V> values() {
            return FXCollections.emptyObservableSet();
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return FXCollections.emptyObservableSet();
        }

        @Override
        public boolean equals(Object object) {
            return object instanceof Map && ((Map)object).isEmpty();
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    private static class CheckedObservableSet<E>
    extends AbstractSet<E>
    implements ObservableSet<E> {
        private final ObservableSet<E> backingSet;
        private final Class<E> type;
        private SetListenerHelper listenerHelper;
        private final SetChangeListener<E> listener;

        CheckedObservableSet(ObservableSet<E> observableSet, Class<E> class_) {
            if (observableSet == null || class_ == null) {
                throw new NullPointerException();
            }
            this.backingSet = observableSet;
            this.type = class_;
            this.listener = change -> this.callObservers(new SetAdapterChange(this, change));
            this.backingSet.addListener(new WeakSetChangeListener<E>(this.listener));
        }

        private void callObservers(SetChangeListener.Change<? extends E> change) {
            SetListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
        }

        void typeCheck(Object object) {
            if (object != null && !this.type.isInstance(object)) {
                throw new ClassCastException("Attempt to insert " + object.getClass() + " element into collection with element type " + this.type);
            }
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, invalidationListener);
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, invalidationListener);
        }

        @Override
        public void addListener(SetChangeListener<? super E> setChangeListener) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, setChangeListener);
        }

        @Override
        public void removeListener(SetChangeListener<? super E> setChangeListener) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, setChangeListener);
        }

        @Override
        public int size() {
            return this.backingSet.size();
        }

        @Override
        public boolean isEmpty() {
            return this.backingSet.isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return this.backingSet.contains(object);
        }

        @Override
        public Object[] toArray() {
            return this.backingSet.toArray();
        }

        @Override
        public <T> T[] toArray(T[] arrT) {
            return this.backingSet.toArray(arrT);
        }

        @Override
        public boolean add(E e2) {
            this.typeCheck(e2);
            return this.backingSet.add(e2);
        }

        @Override
        public boolean remove(Object object) {
            return this.backingSet.remove(object);
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return this.backingSet.containsAll(collection);
        }

        @Override
        public boolean addAll(Collection<? extends E> collection) {
            Object[] arrobject = null;
            try {
                arrobject = collection.toArray((Object[])Array.newInstance(this.type, 0));
            }
            catch (ArrayStoreException arrayStoreException) {
                throw new ClassCastException();
            }
            return this.backingSet.addAll(Arrays.asList(arrobject));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return this.backingSet.retainAll(collection);
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return this.backingSet.removeAll(collection);
        }

        @Override
        public void clear() {
            this.backingSet.clear();
        }

        @Override
        public boolean equals(Object object) {
            return object == this || this.backingSet.equals(object);
        }

        @Override
        public int hashCode() {
            return this.backingSet.hashCode();
        }

        @Override
        public Iterator<E> iterator() {
            final Iterator iterator = this.backingSet.iterator();
            return new Iterator<E>(){

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public E next() {
                    return iterator.next();
                }

                @Override
                public void remove() {
                    iterator.remove();
                }
            };
        }
    }

    private static class SynchronizedObservableSet<E>
    extends SynchronizedSet<E>
    implements ObservableSet<E> {
        private final ObservableSet<E> backingSet;
        private SetListenerHelper listenerHelper;
        private final SetChangeListener<E> listener;

        SynchronizedObservableSet(ObservableSet<E> observableSet, Object object) {
            super(observableSet, object);
            this.backingSet = observableSet;
            this.listener = change -> SetListenerHelper.fireValueChangedEvent(this.listenerHelper, new SetAdapterChange(this, change));
            this.backingSet.addListener(new WeakSetChangeListener<E>(this.listener));
        }

        SynchronizedObservableSet(ObservableSet<E> observableSet) {
            this(observableSet, new Object());
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void addListener(InvalidationListener invalidationListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, invalidationListener);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, invalidationListener);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void addListener(SetChangeListener<? super E> setChangeListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, setChangeListener);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void removeListener(SetChangeListener<? super E> setChangeListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, setChangeListener);
            }
        }
    }

    private static class SynchronizedSet<E>
    implements Set<E> {
        final Object mutex;
        private final Set<E> backingSet;

        SynchronizedSet(Set<E> set, Object object) {
            this.backingSet = set;
            this.mutex = object;
        }

        SynchronizedSet(Set<E> set) {
            this(set, new Object());
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isEmpty() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.isEmpty();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean contains(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingSet.contains(object);
            }
        }

        @Override
        public Iterator<E> iterator() {
            return this.backingSet.iterator();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Object[] toArray() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.toArray();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public <E> E[] toArray(E[] arrE) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.toArray(arrE);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean add(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.add(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean remove(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingSet.remove(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsAll(Collection<?> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.containsAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean addAll(Collection<? extends E> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.addAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean retainAll(Collection<?> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.retainAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean removeAll(Collection<?> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.removeAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.mutex;
            synchronized (object) {
                this.backingSet.clear();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingSet.equals(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingSet.hashCode();
            }
        }
    }

    private static class UnmodifiableObservableSet<E>
    extends AbstractSet<E>
    implements ObservableSet<E> {
        private final ObservableSet<E> backingSet;
        private SetListenerHelper<E> listenerHelper;
        private SetChangeListener<E> listener;

        public UnmodifiableObservableSet(ObservableSet<E> observableSet) {
            this.backingSet = observableSet;
            this.listener = null;
        }

        private void initListener() {
            if (this.listener == null) {
                this.listener = change -> this.callObservers(new SetAdapterChange(this, change));
                this.backingSet.addListener(new WeakSetChangeListener<E>(this.listener));
            }
        }

        private void callObservers(SetChangeListener.Change<? extends E> change) {
            SetListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>(){
                private final Iterator<? extends E> i;
                {
                    this.i = backingSet.iterator();
                }

                @Override
                public boolean hasNext() {
                    return this.i.hasNext();
                }

                @Override
                public E next() {
                    return this.i.next();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public int size() {
            return this.backingSet.size();
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {
            this.initListener();
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, invalidationListener);
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, invalidationListener);
        }

        @Override
        public void addListener(SetChangeListener<? super E> setChangeListener) {
            this.initListener();
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, setChangeListener);
        }

        @Override
        public void removeListener(SetChangeListener<? super E> setChangeListener) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, setChangeListener);
        }

        @Override
        public boolean add(E e2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    private static class EmptyObservableSet<E>
    extends AbstractSet<E>
    implements ObservableSet<E> {
        @Override
        public void addListener(InvalidationListener invalidationListener) {
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
        }

        @Override
        public void addListener(SetChangeListener<? super E> setChangeListener) {
        }

        @Override
        public void removeListener(SetChangeListener<? super E> setChangeListener) {
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean contains(Object object) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return collection.isEmpty();
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <E> E[] toArray(E[] arrE) {
            if (arrE.length > 0) {
                arrE[0] = null;
            }
            return arrE;
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator(){

                @Override
                public boolean hasNext() {
                    return false;
                }

                public Object next() {
                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static class CheckedObservableList<T>
    extends ObservableListBase<T>
    implements ObservableList<T> {
        private final ObservableList<T> list;
        private final Class<T> type;
        private final ListChangeListener<T> listener;

        CheckedObservableList(ObservableList<T> observableList, Class<T> class_) {
            if (observableList == null || class_ == null) {
                throw new NullPointerException();
            }
            this.list = observableList;
            this.type = class_;
            this.listener = change -> this.fireChange(new SourceAdapterChange(this, change));
            observableList.addListener(new WeakListChangeListener<T>(this.listener));
        }

        void typeCheck(Object object) {
            if (object != null && !this.type.isInstance(object)) {
                throw new ClassCastException("Attempt to insert " + object.getClass() + " element into collection with element type " + this.type);
            }
        }

        @Override
        public int size() {
            return this.list.size();
        }

        @Override
        public boolean isEmpty() {
            return this.list.isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return this.list.contains(object);
        }

        @Override
        public Object[] toArray() {
            return this.list.toArray();
        }

        @Override
        public <T> T[] toArray(T[] arrT) {
            return this.list.toArray(arrT);
        }

        @Override
        public String toString() {
            return this.list.toString();
        }

        @Override
        public boolean remove(Object object) {
            return this.list.remove(object);
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return this.list.containsAll(collection);
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return this.list.removeAll(collection);
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return this.list.retainAll(collection);
        }

        @Override
        public boolean removeAll(T ... arrT) {
            return this.list.removeAll(arrT);
        }

        @Override
        public boolean retainAll(T ... arrT) {
            return this.list.retainAll(arrT);
        }

        @Override
        public void remove(int n2, int n3) {
            this.list.remove(n2, n3);
        }

        @Override
        public void clear() {
            this.list.clear();
        }

        @Override
        public boolean equals(Object object) {
            return object == this || this.list.equals(object);
        }

        @Override
        public int hashCode() {
            return this.list.hashCode();
        }

        @Override
        public T get(int n2) {
            return (T)this.list.get(n2);
        }

        @Override
        public T remove(int n2) {
            return (T)this.list.remove(n2);
        }

        @Override
        public int indexOf(Object object) {
            return this.list.indexOf(object);
        }

        @Override
        public int lastIndexOf(Object object) {
            return this.list.lastIndexOf(object);
        }

        @Override
        public T set(int n2, T t2) {
            this.typeCheck(t2);
            return this.list.set(n2, t2);
        }

        @Override
        public void add(int n2, T t2) {
            this.typeCheck(t2);
            this.list.add(n2, t2);
        }

        @Override
        public boolean addAll(int n2, Collection<? extends T> collection) {
            Object[] arrobject = null;
            try {
                arrobject = collection.toArray((Object[])Array.newInstance(this.type, 0));
            }
            catch (ArrayStoreException arrayStoreException) {
                throw new ClassCastException();
            }
            return this.list.addAll(n2, (Collection<T>)Arrays.asList(arrobject));
        }

        @Override
        public boolean addAll(Collection<? extends T> collection) {
            Object[] arrobject = null;
            try {
                arrobject = collection.toArray((Object[])Array.newInstance(this.type, 0));
            }
            catch (ArrayStoreException arrayStoreException) {
                throw new ClassCastException();
            }
            return this.list.addAll((Collection<T>)Arrays.asList(arrobject));
        }

        @Override
        public ListIterator<T> listIterator() {
            return this.listIterator(0);
        }

        @Override
        public ListIterator<T> listIterator(final int n2) {
            return new ListIterator<T>(){
                ListIterator<T> i;
                {
                    this.i = list.listIterator(n2);
                }

                @Override
                public boolean hasNext() {
                    return this.i.hasNext();
                }

                @Override
                public T next() {
                    return this.i.next();
                }

                @Override
                public boolean hasPrevious() {
                    return this.i.hasPrevious();
                }

                @Override
                public T previous() {
                    return this.i.previous();
                }

                @Override
                public int nextIndex() {
                    return this.i.nextIndex();
                }

                @Override
                public int previousIndex() {
                    return this.i.previousIndex();
                }

                @Override
                public void remove() {
                    this.i.remove();
                }

                @Override
                public void set(T t2) {
                    this.typeCheck(t2);
                    this.i.set(t2);
                }

                @Override
                public void add(T t2) {
                    this.typeCheck(t2);
                    this.i.add(t2);
                }
            };
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>(){
                private final Iterator<T> it;
                {
                    this.it = list.iterator();
                }

                @Override
                public boolean hasNext() {
                    return this.it.hasNext();
                }

                @Override
                public T next() {
                    return this.it.next();
                }

                @Override
                public void remove() {
                    this.it.remove();
                }
            };
        }

        @Override
        public boolean add(T t2) {
            this.typeCheck(t2);
            return this.list.add(t2);
        }

        @Override
        public List<T> subList(int n2, int n3) {
            return Collections.checkedList(this.list.subList(n2, n3), this.type);
        }

        @Override
        public boolean addAll(T ... arrT) {
            try {
                Object[] arrobject = (Object[])Array.newInstance(this.type, arrT.length);
                System.arraycopy(arrT, 0, arrobject, 0, arrT.length);
                return this.list.addAll(arrobject);
            }
            catch (ArrayStoreException arrayStoreException) {
                throw new ClassCastException();
            }
        }

        @Override
        public boolean setAll(T ... arrT) {
            try {
                Object[] arrobject = (Object[])Array.newInstance(this.type, arrT.length);
                System.arraycopy(arrT, 0, arrobject, 0, arrT.length);
                return this.list.setAll(arrobject);
            }
            catch (ArrayStoreException arrayStoreException) {
                throw new ClassCastException();
            }
        }

        @Override
        public boolean setAll(Collection<? extends T> collection) {
            Object[] arrobject = null;
            try {
                arrobject = collection.toArray((Object[])Array.newInstance(this.type, 0));
            }
            catch (ArrayStoreException arrayStoreException) {
                throw new ClassCastException();
            }
            return this.list.setAll((Collection<T>)Arrays.asList(arrobject));
        }
    }

    private static class SynchronizedObservableList<T>
    extends SynchronizedList<T>
    implements ObservableList<T> {
        private ListListenerHelper helper;
        private final ObservableList<T> backingList;
        private final ListChangeListener<T> listener;

        SynchronizedObservableList(ObservableList<T> observableList, Object object) {
            super(observableList, object);
            this.backingList = observableList;
            this.listener = change -> ListListenerHelper.fireValueChangedEvent(this.helper, new SourceAdapterChange(this, change));
            this.backingList.addListener(new WeakListChangeListener<T>(this.listener));
        }

        SynchronizedObservableList(ObservableList<T> observableList) {
            this(observableList, new Object());
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean addAll(T ... arrT) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.addAll(arrT);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean setAll(T ... arrT) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.setAll(arrT);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean removeAll(T ... arrT) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.removeAll(arrT);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean retainAll(T ... arrT) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.retainAll(arrT);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void remove(int n2, int n3) {
            Object object = this.mutex;
            synchronized (object) {
                this.backingList.remove(n2, n3);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean setAll(Collection<? extends T> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.setAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public final void addListener(InvalidationListener invalidationListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.helper = ListListenerHelper.addListener(this.helper, invalidationListener);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public final void removeListener(InvalidationListener invalidationListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.helper = ListListenerHelper.removeListener(this.helper, invalidationListener);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void addListener(ListChangeListener<? super T> listChangeListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.helper = ListListenerHelper.addListener(this.helper, listChangeListener);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void removeListener(ListChangeListener<? super T> listChangeListener) {
            Object object = this.mutex;
            synchronized (object) {
                this.helper = ListListenerHelper.removeListener(this.helper, listChangeListener);
            }
        }
    }

    private static class SynchronizedList<T>
    implements List<T> {
        final Object mutex;
        private final List<T> backingList;

        SynchronizedList(List<T> list, Object object) {
            this.backingList = list;
            this.mutex = object;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isEmpty() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.isEmpty();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean contains(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingList.contains(object);
            }
        }

        @Override
        public Iterator<T> iterator() {
            return this.backingList.iterator();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Object[] toArray() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.toArray();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public <T> T[] toArray(T[] arrT) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.toArray(arrT);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean add(T t2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.add(t2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean remove(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingList.remove(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsAll(Collection<?> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.containsAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean addAll(Collection<? extends T> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.addAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean addAll(int n2, Collection<? extends T> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.addAll(n2, collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean removeAll(Collection<?> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.removeAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean retainAll(Collection<?> collection) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.retainAll(collection);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.mutex;
            synchronized (object) {
                this.backingList.clear();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public T get(int n2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.get(n2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public T set(int n2, T t2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.set(n2, t2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void add(int n2, T t2) {
            Object object = this.mutex;
            synchronized (object) {
                this.backingList.add(n2, t2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public T remove(int n2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.remove(n2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int indexOf(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingList.indexOf(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int lastIndexOf(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingList.lastIndexOf(object);
            }
        }

        @Override
        public ListIterator<T> listIterator() {
            return this.backingList.listIterator();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public ListIterator<T> listIterator(int n2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.listIterator(n2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public List<T> subList(int n2, int n3) {
            Object object = this.mutex;
            synchronized (object) {
                return new SynchronizedList<T>(this.backingList.subList(n2, n3), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public String toString() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.toString();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.mutex;
            synchronized (object) {
                return this.backingList.hashCode();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object object) {
            Object object2 = this.mutex;
            synchronized (object2) {
                return this.backingList.equals(object);
            }
        }
    }

    private static class UnmodifiableObservableListImpl<T>
    extends ObservableListBase<T>
    implements ObservableList<T> {
        private final ObservableList<T> backingList;
        private final ListChangeListener<T> listener;

        public UnmodifiableObservableListImpl(ObservableList<T> observableList) {
            this.backingList = observableList;
            this.listener = change -> this.fireChange(new SourceAdapterChange(this, change));
            this.backingList.addListener(new WeakListChangeListener<T>(this.listener));
        }

        @Override
        public T get(int n2) {
            return (T)this.backingList.get(n2);
        }

        @Override
        public int size() {
            return this.backingList.size();
        }

        @Override
        public boolean addAll(T ... arrT) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(T ... arrT) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(Collection<? extends T> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(T ... arrT) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(T ... arrT) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(int n2, int n3) {
            throw new UnsupportedOperationException();
        }
    }

    private static class SingletonObservableList<E>
    extends AbstractList<E>
    implements ObservableList<E> {
        private final E element;

        public SingletonObservableList(E e2) {
            if (e2 == null) {
                throw new NullPointerException();
            }
            this.element = e2;
        }

        @Override
        public boolean addAll(E ... arrE) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(E ... arrE) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(E ... arrE) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(E ... arrE) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(int n2, int n3) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
        }

        @Override
        public void addListener(ListChangeListener<? super E> listChangeListener) {
        }

        @Override
        public void removeListener(ListChangeListener<? super E> listChangeListener) {
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object object) {
            return this.element.equals(object);
        }

        @Override
        public E get(int n2) {
            if (n2 != 0) {
                throw new IndexOutOfBoundsException();
            }
            return this.element;
        }
    }

    private static class EmptyObservableList<E>
    extends AbstractList<E>
    implements ObservableList<E> {
        private static final ListIterator iterator = new ListIterator(){

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            public Object previous() {
                throw new NoSuchElementException();
            }

            @Override
            public int nextIndex() {
                return 0;
            }

            @Override
            public int previousIndex() {
                return -1;
            }

            public void set(Object object) {
                throw new UnsupportedOperationException();
            }

            public void add(Object object) {
                throw new UnsupportedOperationException();
            }
        };

        @Override
        public final void addListener(InvalidationListener invalidationListener) {
        }

        @Override
        public final void removeListener(InvalidationListener invalidationListener) {
        }

        @Override
        public void addListener(ListChangeListener<? super E> listChangeListener) {
        }

        @Override
        public void removeListener(ListChangeListener<? super E> listChangeListener) {
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean contains(Object object) {
            return false;
        }

        @Override
        public Iterator<E> iterator() {
            return iterator;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return collection.isEmpty();
        }

        @Override
        public E get(int n2) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int indexOf(Object object) {
            return -1;
        }

        @Override
        public int lastIndexOf(Object object) {
            return -1;
        }

        @Override
        public ListIterator<E> listIterator() {
            return iterator;
        }

        @Override
        public ListIterator<E> listIterator(int n2) {
            if (n2 != 0) {
                throw new IndexOutOfBoundsException();
            }
            return iterator;
        }

        @Override
        public List<E> subList(int n2, int n3) {
            if (n2 != 0 || n3 != 0) {
                throw new IndexOutOfBoundsException();
            }
            return this;
        }

        @Override
        public boolean addAll(E ... arrE) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(E ... arrE) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(E ... arrE) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(E ... arrE) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(int n2, int n3) {
            throw new UnsupportedOperationException();
        }
    }
}

