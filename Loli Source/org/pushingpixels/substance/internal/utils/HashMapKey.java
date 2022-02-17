/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.util.Arrays;

public class HashMapKey {
    private Object[] keyFields;

    public HashMapKey(Object ... fields) {
        this.keyFields = fields;
    }

    public int hashCode() {
        return Arrays.deepHashCode(this.keyFields);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HashMapKey)) {
            return false;
        }
        HashMapKey key2 = (HashMapKey)obj;
        return Arrays.equals(this.keyFields, key2.keyFields);
    }
}

