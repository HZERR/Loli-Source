/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.jcip.annotations.GuardedBy
 */
package org.pushingpixels.substance.internal.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.jcip.annotations.GuardedBy;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.SoftHashMap;

public class LazyResettableHashMap<T> {
    private static final Object staticLock = new Object();
    private final Object instanceLock = new Object();
    @GuardedBy(value="staticLock")
    private static List<LazyResettableHashMap<?>> all;
    @GuardedBy(value="instanceLock")
    private Map<HashMapKey, T> cache;
    private String displayName;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public LazyResettableHashMap(String displayName) {
        this.displayName = displayName;
        Object object = staticLock;
        synchronized (object) {
            if (all == null) {
                all = new LinkedList();
            }
            all.add(this);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void createIfNecessary() {
        Object object = this.instanceLock;
        synchronized (object) {
            if (this.cache == null) {
                this.cache = new SoftHashMap<HashMapKey, T>();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void put(HashMapKey key, T entry) {
        Object object = this.instanceLock;
        synchronized (object) {
            this.createIfNecessary();
            this.cache.put(key, entry);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public T get(HashMapKey key) {
        Object object = this.instanceLock;
        synchronized (object) {
            if (this.cache == null) {
                return null;
            }
            return this.cache.get(key);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean containsKey(HashMapKey key) {
        Object object = this.instanceLock;
        synchronized (object) {
            if (this.cache == null) {
                return false;
            }
            return this.cache.containsKey(key);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int size() {
        Object object = this.instanceLock;
        synchronized (object) {
            if (this.cache == null) {
                return 0;
            }
            return this.cache.size();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void reset() {
        Object object = staticLock;
        synchronized (object) {
            if (all != null) {
                for (LazyResettableHashMap<?> map : all) {
                    Object object2 = map.instanceLock;
                    synchronized (object2) {
                        if (map.cache != null) {
                            map.cache.clear();
                        }
                    }
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List<String> getStats() {
        Object object = staticLock;
        synchronized (object) {
            if (all != null) {
                String key;
                LinkedList<String> result = new LinkedList<String>();
                TreeMap<String, Integer> mapCounter = new TreeMap<String, Integer>();
                TreeMap<String, Integer> entryCounter = new TreeMap<String, Integer>();
                for (LazyResettableHashMap<?> lazyResettableHashMap : all) {
                    key = lazyResettableHashMap.displayName;
                    if (!mapCounter.containsKey(key)) {
                        mapCounter.put(key, 0);
                        entryCounter.put(key, 0);
                    }
                    mapCounter.put(key, (Integer)mapCounter.get(key) + 1);
                    entryCounter.put(key, (Integer)entryCounter.get(key) + lazyResettableHashMap.size());
                }
                for (Map.Entry entry : mapCounter.entrySet()) {
                    key = (String)entry.getKey();
                    result.add(entry.getValue() + " " + key + " with " + entryCounter.get(key) + " entries total");
                }
                return result;
            }
            return null;
        }
    }
}

