/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

class SoftHashMap<K, V>
extends AbstractMap<K, V>
implements Serializable {
    private final Map<K, KeySoftReference<K, V>> hash = new HashMap<K, KeySoftReference<K, V>>();
    private final ReferenceQueue<V> queue = new ReferenceQueue();

    SoftHashMap() {
    }

    @Override
    public V get(Object key) {
        this.expungeStaleEntries();
        V result = null;
        KeySoftReference<K, V> soft_ref = this.hash.get(key);
        if (soft_ref != null && (result = (V)soft_ref.get()) == null) {
            this.hash.remove(key);
        }
        return result;
    }

    private void expungeStaleEntries() {
        Reference<V> ref;
        while ((ref = this.queue.poll()) != null) {
            KeySoftReference keyRef = (KeySoftReference)ref;
            this.hash.remove(keyRef.key);
        }
    }

    @Override
    public V put(K key, V value) {
        this.expungeStaleEntries();
        KeySoftReference<K, V> keyRef = new KeySoftReference<K, V>(key, value, this.queue);
        SoftReference result = this.hash.put(key, keyRef);
        if (result == null) {
            return null;
        }
        return (V)result.get();
    }

    @Override
    public V remove(Object key) {
        this.expungeStaleEntries();
        SoftReference result = this.hash.remove(key);
        if (result == null) {
            return null;
        }
        return (V)result.get();
    }

    @Override
    public void clear() {
        this.hash.clear();
    }

    @Override
    public int size() {
        this.expungeStaleEntries();
        return this.hash.size();
    }

    @Override
    public boolean containsKey(Object key) {
        this.expungeStaleEntries();
        SoftReference keyRef = this.hash.get(key);
        if (keyRef != null) {
            Object result = keyRef.get();
            if (result != null) {
                return true;
            }
            this.hash.remove(key);
        }
        return false;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        this.expungeStaleEntries();
        LinkedHashSet<Map.Entry<K, V>> result = new LinkedHashSet<Map.Entry<K, V>>();
        for (final Map.Entry<K, KeySoftReference<K, V>> entry : this.hash.entrySet()) {
            final Object value = entry.getValue().get();
            if (value == null) continue;
            result.add(new Map.Entry<K, V>(){

                @Override
                public K getKey() {
                    return entry.getKey();
                }

                @Override
                public V getValue() {
                    return value;
                }

                @Override
                public V setValue(V v2) {
                    entry.setValue(new KeySoftReference(entry.getKey(), v2, SoftHashMap.this.queue));
                    return value;
                }
            });
        }
        return result;
    }

    public static class KeySoftReference<K, V>
    extends SoftReference<V> {
        final K key;

        public KeySoftReference(K key, V referent, ReferenceQueue<V> q2) {
            super(referent, q2);
            this.key = key;
        }
    }
}

